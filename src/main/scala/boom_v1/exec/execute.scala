package boom_v1.exec

import boom_v1.IMMType._
import boom_v1.Utils._
import boom_v1.Utils.chiselExtract._
import boom_v1.Utils.chiselDotDef._
import boom_v1.exec.FPU.FPConstants
import boom_v1.exec.FUConstants._
import boom_v1.exec.lsu.DCMemPortIO
import boom_v1.predictor.{BHTUpdate, BTBUpdate}
import boom_v1.regfile.MStatus
import boom_v1.{Causes, FUType, MicroOp, Parameters}
import spinal.sim._
import spinal.core._
import spinal.core.sim._
import spinal.lib._

import scala.collection.mutable.ArrayBuffer
import scala.util.Random
import scala.language.postfixOps
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// Execution Units
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//
// Christopher Celio
// 2013 Apr 27
//
// The issue window schedules micro-ops onto a specific execution pipeline
// A given execution pipeline may contain multiple functional units; one or more
// read ports, and one or more writeports.


  class ExeUnitResp(data_width: Int)(implicit p: Parameters) extends Bundle
  {
    val uop = new MicroOp()
    val data = Bits(width = data_width bits)
    val fflags = Flow(new FFlagsResp) // write fflags to ROB
  }

  class FFlagsResp(implicit p: Parameters) extends Bundle
  {
    val uop = new MicroOp()
    val flags = Bits(width=FPConstants.FLAGS_SZ bits)
  }

  class ExecutionUnitIO(num_rf_read_ports: Int
                        , num_rf_write_ports: Int
                        , num_bypass_ports: Int
                        , data_width: Int
                       )(implicit p: Parameters) extends Bundle
  {
    // describe which functional units we support (used by the issue window)
    val fu_types = out Bits(FUC_SZ bits)

    val req     = slave(Stream(new FuncUnitReq(data_width)))
    val resp    = Vec(master(Stream(new ExeUnitResp(data_width))),num_rf_write_ports)
    val bypass  = new BypassData(num_bypass_ports, data_width).asOutput
    val brinfo  = new BrResolutionInfo().asInput

    // only used by the branch unit
    val br_unit = new BranchUnitResp().asOutput
    val get_rob_pc = new RobPCRequest().flip
    val get_pred = new GetPredictionInfo
    val status = new MStatus().asInput

    // only used by the fpu unit
    val fcsr_rm = in Bits(FPConstants.RM_SZ bits)

    // only used by the mem unit
    val lsu_io = new LoadStoreUnitIO(p.DECODE_WIDTH).flip
    val dmem   = new DCMemPortIO() // TODO move this out of ExecutionUnit
    val com_exception = in Bool()
  }

  abstract class ExecutionUnit(val num_rf_read_ports: Int
                               , val num_rf_write_ports: Int
                               , val num_bypass_stages: Int
                               , val data_width: Int
                               , val num_variable_write_ports: Int = 0
                               , var bypassable: Boolean           = false
                               , val is_mem_unit: Boolean          = false
                               , var uses_csr_wport: Boolean       = false
                               ,     is_branch_unit: Boolean       = false
                               , val has_alu       : Boolean       = false
                               , val has_fpu       : Boolean       = false
                               , val has_mul       : Boolean       = false
                               , val has_div       : Boolean       = false
                               , val has_fdiv      : Boolean       = false
                              )(implicit p: Parameters) extends Module
  {
    val io = new ExecutionUnitIO(num_rf_read_ports, num_rf_write_ports
      , num_bypass_stages, data_width)

    io.resp.foreach(_.payload.fflags.valid := Bool(value = false))

    // TODO add "number of fflag ports", so we can properly account for FPU+Mem combinations
    def numBypassPorts: Int = num_bypass_stages
    def hasBranchUnit : Boolean = is_branch_unit
    def isBypassable  : Boolean = bypassable
    def hasFFlags     : Boolean = has_fpu || has_fdiv

    def supportedFuncUnits =
    {
      new SupportedFuncUnits(
        alu = has_alu,
        bru = is_branch_unit,
        mem = is_mem_unit,
        muld = has_mul || has_div,
        fpu = has_fpu,
        csr = uses_csr_wport,
        fdiv = has_fdiv)
    }
  }

  class ALUExeUnit(
                    is_branch_unit  : Boolean = false,
                    shares_csr_wport: Boolean = false,
                    has_fpu         : Boolean = false,
                    has_mul         : Boolean = false,
                    has_div         : Boolean = false,
                    has_fdiv        : Boolean = false,
                    use_slow_mul    : Boolean = false)
                  (implicit p: Parameters)
    extends ExecutionUnit(
      num_rf_read_ports = if (has_fpu) 3 else 2,
      num_rf_write_ports = 1,
      num_bypass_stages = if (has_fpu || (has_mul && !use_slow_mul)) 3 else 1, // TODO XXX dfmaLatency else 1,
      data_width = if (has_fpu || has_fdiv) 65 else 64,
      bypassable = true,
      is_mem_unit = false,
      uses_csr_wport = shares_csr_wport,
      is_branch_unit = is_branch_unit,
      has_alu  = true,
      has_fpu  = has_fpu,
      has_mul  = has_mul,
      has_div  = has_div,
      has_fdiv = has_fdiv)(p)
  {
    val has_muldiv = has_div || (has_mul && use_slow_mul)

    require(p.dfmaLatency == 3) // fix the above num_bypass_stages==3 hack before removing this line

    println ("     ExeUnit--")
    println ("       - ALU")
    if (has_fpu) println ("       - FPU (Latency: " + p.dfmaLatency + ")")
    if (has_mul && !use_slow_mul) println ("       - Mul (pipelined)")
    if (has_div && has_mul && use_slow_mul) println ("       - Mul/Div (unpipelined)")
    else if (has_mul && use_slow_mul) println ("       - Mul (unpipelined)")
    else if (has_div) println ("       - Div")
    if (has_fdiv) println ("       - FDiv/FSqrt")

    val muldiv_busy = Bool(false)
    val fdiv_busy = Bool(false)


    // The Functional Units --------------------
    val fu_units = ArrayBuffer[FunctionalUnit]()

    io.fu_types := FU_ALU |
      Mux(Bool(has_fpu), FU_FPU, U(0)) |
      Mux(Bool(has_mul && !use_slow_mul), FU_MUL, U(0)) |
      (Mux(!muldiv_busy && Bool(has_mul && use_slow_mul), FU_MUL, U(0))) |
      (Mux(!muldiv_busy && Bool(has_div), FU_DIV, U(0))) |
      (Mux(Bool(shares_csr_wport), FU_CSR, U(0))) |
      (Mux(Bool(is_branch_unit), FU_BRU, U(0))) |
      Mux(!fdiv_busy && Bool(has_fdiv), FU_FDV, U(0))


    // ALU Unit -------------------------------
    val alu = (new ALUUnit(is_branch_unit = is_branch_unit, num_stages = num_bypass_stages))
    alu.io.req.valid         := io.req.valid &&
      (io.req.payload.uop.fu_code.asBits.asUInt === FU_ALU ||
        io.req.payload.uop.fu_code.asBits.asUInt === FU_BRU ||
        io.req.payload.uop.fu_code.asBits.asUInt === FU_CSR)
    alu.io.req.payload.uop      := io.req.payload.uop
    alu.io.req.payload.kill     := io.req.payload.kill
    alu.io.req.payload.rs1_data := io.req.payload.rs1_data
    alu.io.req.payload.rs2_data := io.req.payload.rs2_data
    alu.io.brinfo <> io.brinfo

    // branch unit is embedded inside the ALU
    if (is_branch_unit)
    {
      io.br_unit <> alu.io.br_unit
      alu.io.get_rob_pc <> io.get_rob_pc
      io.get_pred <> alu.io.get_pred
      alu.io.status <> io.status
    }
    else
    {
      io.br_unit.brinfo.valid := Bool(false)
    }
    fu_units += alu

    // Pipelined, IMul Unit ------------------
    var imul: PipelinedMulUnit = null
    if (has_mul && !use_slow_mul)
    {
      imul = (new PipelinedMulUnit(p.IMUL_STAGES))
      imul.io.req.valid         := io.req.valid && io.req.payload.uop.fu_code_is(FU_MUL)
      imul.io.req.payload.uop      := io.req.payload.uop
      imul.io.req.payload.rs1_data := io.req.payload.rs1_data
      imul.io.req.payload.rs2_data := io.req.payload.rs2_data
      imul.io.req.payload.kill     := io.req.payload.kill
      imul.io.brinfo <> io.brinfo
      fu_units += imul
      if (has_fpu) require (p.IMUL_STAGES == p.dfmaLatency)
    }

    // FPU Unit -----------------------
    var fpu: FPUUnit = null
    val fpu_resp_val = Wire(init=Bool(false))
    val fpu_resp_fflags = Wire(Flow(new FFlagsResp()))
    fpu_resp_fflags.valid := Bool(false)
    if (has_fpu)
    {
      fpu = (new FPUUnit())
      fpu.io.req.valid           := io.req.valid && io.req.payload.uop.fu_code_is(FU_FPU)
      fpu.io.req.payload.uop        := io.req.payload.uop
      fpu.io.req.payload.rs1_data   := io.req.payload.rs1_data
      fpu.io.req.payload.rs2_data   := io.req.payload.rs2_data
      fpu.io.req.payload.rs3_data   := io.req.payload.rs3_data
      fpu.io.req.payload.kill       := io.req.payload.kill
      fpu.io.fcsr_rm             := io.fcsr_rm
      fpu.io.brinfo <> io.brinfo
      fpu_resp_val := fpu.io.resp.valid
      fpu_resp_fflags := fpu.io.resp.payload.fflags
      fu_units += fpu
    }

    // Bypassing ------------------------------
    // (only the ALU is bypassable)

    io.bypass <> alu.io.bypass

    // FDiv/FSqrt Unit -----------------------
    var fdivsqrt: FDivSqrtUnit = null
    val fdiv_resp_val = Wire(init=Bool(false))
    val fdiv_resp_uop = Wire(new MicroOp())
    val fdiv_resp_data = Wire(Bits(width=65))
    val fdiv_resp_fflags = Wire(new ValidIO(new FFlagsResp()))
    fdiv_resp_fflags.valid := Bool(false)
    if (has_fdiv)
    {
      fdivsqrt = (new FDivSqrtUnit())
      fdivsqrt.io.req.valid         := io.req.valid && io.req.payload.uop.fu_code_is(FU_FDV)
      fdivsqrt.io.req.bits.uop      := io.req.payload.uop
      fdivsqrt.io.req.bits.rs1_data := io.req.payload.rs1_data
      fdivsqrt.io.req.bits.rs2_data := io.req.payload.rs2_data
      fdivsqrt.io.req.bits.kill     := io.req.payload.kill
      fdivsqrt.io.fcsr_rm           := io.fcsr_rm
      fdivsqrt.io.brinfo <> io.brinfo

      // share write port with the pipelined units
      fdivsqrt.io.resp.ready := !(fu_units.map(_.io.resp.valid).reduce(_|_))

      fdiv_busy := !fdivsqrt.io.req.ready || (io.req.valid && io.req.bits.uop.fu_code_is(FU_FDV))

      fdiv_resp_val := fdivsqrt.io.resp.valid
      fdiv_resp_uop := fdivsqrt.io.resp.bits.uop
      fdiv_resp_data := fdivsqrt.io.resp.bits.data
      fdiv_resp_fflags := fdivsqrt.io.resp.bits.fflags

      fu_units += fdivsqrt
    }

    // Mul/Div/Rem Unit -----------------------
    var muldiv: MulDivUnit = null
    val muldiv_resp_val = Wire(Bool())
    muldiv_resp_val := Bool(false)
    if (has_muldiv)
    {
      muldiv = (new MulDivUnit())
      muldiv.io.req.valid           := io.req.valid &&
        ((io.req.payload.uop.fu_code_is(FU_DIV) && Bool(has_div)) ||
          (io.req.payload.uop.fu_code_is(FU_MUL) && Bool(has_mul && use_slow_mul)))
      muldiv.io.req.payload.uop        := io.req.payload.uop
      muldiv.io.req.payload.rs1_data   := io.req.payload.rs1_data
      muldiv.io.req.payload.rs2_data   := io.req.payload.rs2_data
      muldiv.io.brinfo              := io.brinfo
      muldiv.io.req.payload.kill       := io.req.payload.kill

      // share write port with the pipelined units
      muldiv.io.resp.ready := !(fu_units.map(_.io.resp.valid).reduce(_|_))

      muldiv_resp_val := muldiv.io.resp.valid
      muldiv_busy := !muldiv.io.req.ready ||
        (io.req.valid && (io.req.payload.uop.fu_code_is(FU_DIV) ||
          (io.req.payload.uop.fu_code_is(FU_MUL) && Bool(has_mul && use_slow_mul))))
      fu_units += muldiv
    }

    // Outputs (Write Port #0)  ---------------

    io.resp(0).valid    := fu_units.map(_.io.resp.valid).reduce(_|_)
    val uop_seleted = new MicroOp()
    uop_seleted.assignFromBits(
      PriorityMux(fu_units.map(f => (f.io.resp.valid, f.io.resp.payload.uop.asBits))))
    io.resp(0).payload.uop := uop_seleted
    io.resp(0).payload.data:= PriorityMux(fu_units.map(f => (f.io.resp.valid, f.io.resp.payload.data.asBits))).asBits
    // pulled out for critical path reasons
    io.resp(0).payload.uop.csr_addr := ImmGen(alu.io.resp.payload.uop.imm_packed, IS_I.toU().asBits).asUInt
    io.resp(0).payload.uop.ctrl.csr_cmd := alu.io.resp.payload.uop.ctrl.csr_cmd

    io.resp(0).payload.fflags := Mux(fpu_resp_val, fpu_resp_fflags, fdiv_resp_fflags)

    assert ((CountOne(fu_units.map(_.io.resp.valid)) <= UInt(1) && !muldiv_resp_val && !fdiv_resp_val) ||
      (CountOne(fu_units.map(_.io.resp.valid)) <= UInt(2) && (muldiv_resp_val || fdiv_resp_val)) ||
      (CountOne(fu_units.map(_.io.resp.valid)) <= UInt(3) && muldiv_resp_val && fdiv_resp_val)
      , "Multiple functional units are fighting over the write port.")
  }

  class FDivSqrtExeUnit(implicit p: Parameters)
    extends ExecutionUnit(num_rf_read_ports = 2
      , num_rf_write_ports = 1
      , num_bypass_stages = 0
      , data_width = 65
      , num_variable_write_ports = 1
      , has_fdiv = true
    )
  {
    println ("     ExeUnit--")
    println ("       - FDiv/FSqrt")
    val fdiv_busy = Wire(Bool())
    io.fu_types := Mux(!fdiv_busy, FU_FDV, Bits(0))

    val fdivsqrt = (new FDivSqrtUnit())
    fdivsqrt.io.req <> io.req
    fdivsqrt.io.fcsr_rm    := io.fcsr_rm
    io.resp(0).valid       := fdivsqrt.io.resp.valid
    io.resp(0).payload.uop    := fdivsqrt.io.resp.bits.uop
    io.resp(0).payload.data   := fdivsqrt.io.resp.bits.data
    io.resp(0).payload.fflags := fdivsqrt.io.resp.bits.fflags
    fdivsqrt.io.brinfo <> io.brinfo
    io.bypass <> fdivsqrt.io.bypass

    fdiv_busy := !fdivsqrt.io.req.ready || io.req.valid
  }


  class MemExeUnit(implicit p: Parameters) extends ExecutionUnit(num_rf_read_ports = 2,
    num_rf_write_ports = 1,
    num_bypass_stages = 0,
//    data_width = if(p(tile.TileKey).core.fpu.nonEmpty) 65 else p.xLen,
    data_width = if(p.usingFPU) 65 else p.xLen,
    num_variable_write_ports = 1,
    bypassable = false,
    is_mem_unit = true)(p)
  {
    println ("     ExeUnit--")
    println ("       - Mem")

    io.fu_types := FU_MEM

    // Perform address calculation
    val maddrcalc = (new MemAddrCalcUnit())
    maddrcalc.io.req <> io.req

    maddrcalc.io.brinfo <> io.brinfo
    io.bypass <> maddrcalc.io.bypass  // TODO this is not where the bypassing should occur from, is there any bypassing happening?!

    //   val lsu = Module(new LoadStoreUnit(DECODE_WIDTH))
    //   lsu.io.dec_st_vals       := io.lsu_io.dec_st_vals
    //   lsu.io.dec_ld_vals       := io.lsu_io.dec_ld_vals
    //   lsu.io.dec_uops          := io.lsu_io.dec_uops


    //   lsu.io.commit_store_mask := io.lsu_io.commit_store_mask
    //   lsu.io.commit_load_mask  := io.lsu_io.commit_load_mask
    //   lsu.io.commit_load_at_rob_head := io.lsu_io.commit_load_at_rob_head

    //   lsu.io.brinfo            := io.brinfo
    //   lsu.io.exception         := io.lsu_io.exception
    //   lsu.io.nack              <> io.dmem.nack
    //   io.lsu_io.counters       <> lsu.io.counters
    //   lsu.io.debug_tsc         <> io.lsu_io.debug_tsc

    //   io.lsu_io.new_ldq_idx := lsu.io.new_ldq_idx
    //   io.lsu_io.new_stq_idx := lsu.io.new_stq_idx
    //   io.lsu_io.laq_full := lsu.io.laq_full
    //   io.lsu_io.stq_full := lsu.io.stq_full
    //   io.lsu_io.lsu_clr_bsy_valid := lsu.io.lsu_clr_bsy_valid // TODO is there a better way to clear the busy bits in the ROB
    //   io.lsu_io.lsu_clr_bsy_rob_idx := lsu.io.lsu_clr_bsy_rob_idx
    //   io.lsu_io.lsu_fencei_rdy := lsu.io.lsu_fencei_rdy

    // enqueue addresses,st-data at the end of Execute
    //   lsu.io.exe_resp <> maddrcalc.io.resp
    io.lsu_io.exe_resp <> maddrcalc.io.resp

    //   io.lsu_io.ptw <> lsu.io.ptw
    //   io.lsu_io.xcpt <> lsu.io.xcpt

    // HellaCache Req
    //   lsu.io.dmem_req_ready := io.dmem.req.ready
    //   lsu.io.dmem_is_ordered:= io.dmem.ordered


    // TODO get rid of com_exception and guard with an assert? Need to surpress within dc-shim.
    //   assert (!(io.com_exception && lsu.io.memreq_uop.is_load && lsu.io.memreq_val),
    //      "[execute] a valid load is returning while an exception is being thrown.")
    io.dmem.req.valid     := Mux(io.com_exception && io.lsu_io.memreq_uop.is_load,
      Bool(false),
      io.lsu_io.memreq_val)
    io.dmem.req.bits.addr  := io.lsu_io.memreq_addr
    io.dmem.req.bits.data  := io.lsu_io.memreq_wdata
    io.dmem.req.bits.uop   := io.lsu_io.memreq_uop
    io.dmem.req.bits.kill  := io.lsu_io.memreq_kill // load kill request sent to memory

    // I should be timing forwarding to coincide with dmem resps, so I'm not clobbering
    //anything....
    val memresp_val    = Mux(io.com_exception && io.dmem.resp.bits.uop.is_load, Bool(false),
      io.lsu_io.forward_val || io.dmem.resp.valid)
    val memresp_rf_wen = (io.dmem.resp.valid && (io.dmem.resp.bits.uop.mem_cmd === M_XRD || io.dmem.resp.bits.uop.is_amo)) ||  // TODO should I refactor this to use is_load?
      io.lsu_io.forward_val
    val memresp_uop    = Mux(io.lsu_io.forward_val, io.lsu_io.forward_uop,
      io.dmem.resp.bits.uop)

    var memresp_data:Bits = null
    if (!usingFPU)
    {
      memresp_data = Mux(io.lsu_io.forward_val, io.lsu_io.forward_data
        , io.dmem.resp.bits.data_subword)
    }
    else
    {
      //recode FP values
      val typ = io.dmem.resp.bits.typ
      val load_single = typ === rocket.MT_W || typ === rocket.MT_WU
      val rec_s = hardfloat.recFNFromFN(8, 24, io.dmem.resp.bits.data)
      val rec_d = hardfloat.recFNFromFN(11, 53, io.dmem.resp.bits.data)
      val fp_load_data_recoded = Mux(load_single, Cat(SInt(-1, 32), rec_s), rec_d)

      val typ_f = io.lsu_io.forward_uop.mem_typ
      val load_single_f = typ_f === rocket.MT_W || typ_f === rocket.MT_WU
      val rec_s_f = hardfloat.recFNFromFN(8, 24, io.lsu_io.forward_data)
      val rec_d_f = hardfloat.recFNFromFN(11, 53, io.lsu_io.forward_data)
      val fp_load_data_recoded_forwarded = Mux(load_single_f, Cat(SInt(-1,32), rec_s_f), rec_d_f)

      memresp_data = Mux(io.lsu_io.forward_val && !io.lsu_io.forward_uop.fp_val, io.lsu_io.forward_data,
        Mux(io.lsu_io.forward_val && io.lsu_io.forward_uop.fp_val , fp_load_data_recoded_forwarded,
          Mux(memresp_uop.fp_val                              , fp_load_data_recoded,
            io.dmem.resp.bits.data_subword)))
    }



    io.lsu_io.memresp.valid := memresp_val
    io.lsu_io.memresp.bits  := memresp_uop


    // Hook up loads to the response
    io.resp(0).valid := memresp_val
    io.resp(0).bits.uop := memresp_uop
    io.resp(0).bits.uop.ctrl.rf_wen := memresp_rf_wen
    io.resp(0).bits.data := memresp_data
  }


  class ALUMemExeUnit(
                       is_branch_unit    : Boolean = false,
                       shares_csr_wport: Boolean = false,
                       fp_mem_support  : Boolean = true, // does memory need to support FP loads/stores?
                       has_fpu         : Boolean = false,
                       has_mul         : Boolean = false,
                       has_div         : Boolean = false,
                       has_fdiv        : Boolean = false,
                       use_slow_mul    : Boolean = false)
                     (implicit p: Parameters)
    extends ExecutionUnit(
      num_rf_read_ports = if (has_fpu) 3 else 2,
      num_rf_write_ports = 2,
      num_bypass_stages = if (has_fpu || (has_mul && !use_slow_mul)) p(tile.TileKey).core.fpu.get.dfmaLatency else 1,
      data_width = if (fp_mem_support) 65 else 64,
      num_variable_write_ports = 1,
      bypassable = true,
      is_mem_unit = true,
      uses_csr_wport = shares_csr_wport,
      is_branch_unit = is_branch_unit,
      has_alu = true,
      has_fpu = has_fpu,
      has_mul = has_mul,
      has_div = has_div,
      has_fdiv = has_fdiv)(p)
  {
    println ("     ExeUnit--")
    println ("       - ALU")
    if (has_fpu) println ("       - FPU (Latency: " + dfmaLatency + " cycles)")
    if (has_mul && !use_slow_mul) println ("       - Mul (pipelined: " + IMUL_STAGES + " cycles)")
    if (has_div && has_mul && use_slow_mul) println ("       - Mul/Div (unpipelined)")
    else if (has_mul && use_slow_mul) println ("       - Mul (unpipelined)")
    else if (has_div) println ("       - Div")
    if (has_fdiv) println ("       - FDiv/FSqrt")
    println ("       - Mem")

    val muldiv_busy = Wire(Bool())
    val fdiv_busy = Wire(Bool())
    io.fu_types := FU_ALU |
      FU_MEM |
      Mux(Bool(has_fpu), FU_FPU, Bits(0)) |
      (Mux(Bool(has_mul && !use_slow_mul), FU_MUL, Bits(0))) |
      (Mux(!muldiv_busy && Bool(use_slow_mul), FU_MUL, Bits(0))) |
      (Mux(!muldiv_busy && Bool(has_div), FU_DIV, Bits(0))) |
      (Mux(Bool(shares_csr_wport), FU_CSR, Bits(0))) |
      Mux(Bool(is_branch_unit), FU_BRU, Bits(0)) |
      Mux(!fdiv_busy && Bool(has_fdiv), FU_FDV, Bits(0))


    val memresp_val = Wire(Bool())
    val fdiv_resp_val = Wire(Bool())


    // ALU Unit -------------------------------
    val alu = Module(new ALUUnit(is_branch_unit = is_branch_unit, num_stages = num_bypass_stages))
    alu.io.req.valid         := io.req.valid &&
      (io.req.bits.uop.fu_code_is(FU_ALU) ||
        io.req.bits.uop.fu_code_is(FU_BRU) ||
        io.req.bits.uop.fu_code_is(FU_CSR))
    alu.io.req.bits.uop      := io.req.bits.uop
    alu.io.req.bits.kill     := io.req.bits.kill
    alu.io.req.bits.rs1_data := io.req.bits.rs1_data
    alu.io.req.bits.rs2_data := io.req.bits.rs2_data

    alu.io.brinfo <> io.brinfo

    // branch unit is embedded inside the ALU
    if (is_branch_unit)
    {
      io.br_unit <> alu.io.br_unit
      alu.io.get_rob_pc <> io.get_rob_pc
      io.get_pred <> alu.io.get_pred
      alu.io.status <> io.status
    }
    else
    {
      io.br_unit.brinfo.valid := Bool(false)
    }

    // Pipelined, IMul Unit -----------------------
    var imul: PipelinedMulUnit = null
    if (!use_slow_mul)
    {
      imul = Module(new PipelinedMulUnit(IMUL_STAGES))
      imul.io.req.valid := io.req.valid && (io.req.bits.uop.fu_code_is(FU_MUL) && Bool(!use_slow_mul))
      imul.io.req.bits.uop      := io.req.bits.uop
      imul.io.req.bits.rs1_data := io.req.bits.rs1_data
      imul.io.req.bits.rs2_data := io.req.bits.rs2_data
      imul.io.req.bits.kill     := io.req.bits.kill
      imul.io.brinfo <> io.brinfo
      if (has_fpu) require (IMUL_STAGES == dfmaLatency)
    }

    // FPU Unit -----------------------
    var fpu: FPUUnit = null
    if (has_fpu)
    {
      fpu = Module(new FPUUnit())
      fpu.io.req.valid           := io.req.valid && io.req.bits.uop.fu_code_is(FU_FPU)
      fpu.io.req.bits.uop        := io.req.bits.uop
      fpu.io.req.bits.rs1_data   := io.req.bits.rs1_data
      fpu.io.req.bits.rs2_data   := io.req.bits.rs2_data
      fpu.io.req.bits.rs3_data   := io.req.bits.rs3_data
      fpu.io.req.bits.kill       := io.req.bits.kill
      fpu.io.fcsr_rm             := io.fcsr_rm
      fpu.io.brinfo <> io.brinfo
      // TODO use bundle interfacing
    }

    // Outputs (Write Port #0)  ---------------

    val fu_units = ArrayBuffer[FunctionalUnit]()
    fu_units += alu
    if (has_mul && !use_slow_mul) fu_units += imul
    if (has_fpu) fu_units += fpu

    io.resp(0).valid    := fu_units.map(_.io.resp.valid).reduce(_|_)
    io.resp(0).bits.uop := new MicroOp().fromBits(PriorityMux(fu_units.map(f => (f.io.resp.valid, f.io.resp.bits.uop.toBits))))
    io.resp(0).bits.data:= PriorityMux(fu_units.map(f => (f.io.resp.valid, f.io.resp.bits.data.toBits))).toBits
    // pulled out for critical path reasons
    io.resp(0).bits.uop.csr_addr := ImmGen(alu.io.resp.bits.uop.imm_packed, IS_I).toUInt
    io.resp(0).bits.uop.ctrl.csr_cmd := alu.io.resp.bits.uop.ctrl.csr_cmd

    if (has_fpu)
    {
      io.resp(0).bits.fflags.valid      := fpu.io.resp.valid
      io.resp(0).bits.fflags.bits.uop   := fpu.io.resp.bits.fflags.bits.uop
      io.resp(0).bits.fflags.bits.flags := fpu.io.resp.bits.fflags.bits.flags
    }

    assert (PopCount(fu_units.map(_.io.resp.valid)) <= UInt(1)
      , "Multiple functional units are fighting over the write port.")

    // Mul/Div/Rem Unit -----------------------
    var muldiv: MulDivUnit = null
    val muldiv_resp_val = Wire(Bool())
    val muldiv_resp_uop = Wire(new MicroOp())
    val muldiv_resp_data = Wire(Bits(width=64))
    muldiv_resp_val := Bool(false)
    muldiv_busy := Bool(false)
    if (has_div || (has_mul && use_slow_mul))
    {
      muldiv = Module(new MulDivUnit())

      muldiv.io.req.valid           := io.req.valid &&
        ((io.req.bits.uop.fu_code_is(FU_DIV) && Bool(has_div)) ||
          (io.req.bits.uop.fu_code_is(FU_MUL) && Bool(has_mul && use_slow_mul)))
      muldiv.io.req.bits.uop        := io.req.bits.uop
      muldiv.io.req.bits.rs1_data   := io.req.bits.rs1_data
      muldiv.io.req.bits.rs2_data   := io.req.bits.rs2_data
      muldiv.io.req.bits.kill       := io.req.bits.kill

      muldiv.io.brinfo <> io.brinfo

      muldiv.io.resp.ready := !memresp_val && !fdiv_resp_val //share write port with the memory, fdiv

      muldiv_resp_val := muldiv.io.resp.valid
      muldiv_resp_uop := muldiv.io.resp.bits.uop
      muldiv_resp_data:= muldiv.io.resp.bits.data
      muldiv_busy := !muldiv.io.req.ready ||
        (io.req.valid && (io.req.bits.uop.fu_code_is(FU_DIV) ||
          (io.req.bits.uop.fu_code_is(FU_MUL) && Bool(has_mul && use_slow_mul))))
    }


    // FDiv/FSqrt Unit -----------------------
    var fdivsqrt: FDivSqrtUnit = null
    val fdiv_resp_uop = Wire(new MicroOp())
    val fdiv_resp_data = Wire(Bits(width=65))
    val fdiv_resp_fflags = Wire(new ValidIO(new FFlagsResp()))
    fdiv_resp_val := Bool(false)
    fdiv_resp_fflags.valid := Bool(false)
    fdiv_busy := Bool(false)
    if (has_fdiv)
    {
      fdivsqrt = Module(new FDivSqrtUnit())
      fdivsqrt.io.req.valid         := io.req.valid && io.req.bits.uop.fu_code_is(FU_FDV)
      fdivsqrt.io.req.bits.uop      := io.req.bits.uop
      fdivsqrt.io.req.bits.rs1_data := io.req.bits.rs1_data
      fdivsqrt.io.req.bits.rs2_data := io.req.bits.rs2_data
      fdivsqrt.io.req.bits.kill     := io.req.bits.kill
      fdivsqrt.io.fcsr_rm           := io.fcsr_rm
      fdivsqrt.io.brinfo <> io.brinfo

      fdivsqrt.io.resp.ready := !memresp_val //share write port with memory

      fdiv_busy := !fdivsqrt.io.req.ready || (io.req.valid && io.req.bits.uop.fu_code_is(FU_FDV))

      fdiv_resp_val := fdivsqrt.io.resp.valid
      fdiv_resp_uop := fdivsqrt.io.resp.bits.uop
      fdiv_resp_data := fdivsqrt.io.resp.bits.data
      fdiv_resp_fflags := fdivsqrt.io.resp.bits.fflags
    }

    // Bypassing --------------------------------
    // (only the ALU is bypassable)

    io.bypass <> alu.io.bypass

    // Perform address calculation
    val maddrcalc = Module(new MemAddrCalcUnit())
    maddrcalc.io.req <> io.req
    maddrcalc.io.brinfo <> io.brinfo

    //   val lsu = Module(new LoadStoreUnit(DECODE_WIDTH))

    //   lsu.io.dec_st_vals       := io.lsu_io.dec_st_vals
    //   lsu.io.dec_ld_vals       := io.lsu_io.dec_ld_vals
    //   lsu.io.dec_uops          := io.lsu_io.dec_uops

    //   lsu.io.commit_store_mask := io.lsu_io.commit_store_mask
    //   lsu.io.commit_load_mask  := io.lsu_io.commit_load_mask
    //   lsu.io.commit_load_at_rob_head := io.lsu_io.commit_load_at_rob_head

    //   lsu.io.brinfo            := io.brinfo
    //   lsu.io.exception         := io.lsu_io.exception
    //   lsu.io.nack              <> io.dmem.nack
    //   io.lsu_io.counters       <> lsu.io.counters
    //   lsu.io.debug_tsc         <> io.lsu_io.debug_tsc

    //   io.lsu_io.new_ldq_idx := lsu.io.new_ldq_idx
    //   io.lsu_io.new_stq_idx := lsu.io.new_stq_idx
    //   io.lsu_io.laq_full := lsu.io.laq_full
    //   io.lsu_io.stq_full := lsu.io.stq_full
    //   io.lsu_io.lsu_clr_bsy_valid := lsu.io.lsu_clr_bsy_valid
    //   io.lsu_io.lsu_clr_bsy_rob_idx := lsu.io.lsu_clr_bsy_rob_idx
    //   io.lsu_io.lsu_fencei_rdy := lsu.io.lsu_fencei_rdy

    // enqueue addresses,st-data at the end of Execute
    //   lsu.io.exe_resp <> maddrcalc.io.resp
    io.lsu_io.exe_resp <> maddrcalc.io.resp

    //   io.lsu_io.ptw <> lsu.io.ptw
    //   io.lsu_io.xcpt <> lsu.io.xcpt

    // HellaCache Req
    //   lsu.io.dmem_req_ready := io.dmem.req.ready
    //   lsu.io.dmem_is_ordered:= io.dmem.ordered

    io.dmem.req.valid     := Mux(io.com_exception && io.lsu_io.memreq_uop.is_load, Bool(false),
      io.lsu_io.memreq_val)
    io.dmem.req.bits.addr  := io.lsu_io.memreq_addr
    io.dmem.req.bits.data  := io.lsu_io.memreq_wdata
    io.dmem.req.bits.uop   := io.lsu_io.memreq_uop
    io.dmem.req.bits.kill  := io.lsu_io.memreq_kill // load kill request sent to memory

    // I'm timing forwarding to coincide with dmem resps, so I'm not clobbering anything...
    memresp_val := Mux(io.com_exception && io.dmem.resp.bits.uop.is_load, Bool(false),
      io.lsu_io.forward_val || io.dmem.resp.valid)


    val memresp_rf_wen = (io.dmem.resp.valid && (io.dmem.resp.bits.uop.mem_cmd === M_XRD || io.dmem.resp.bits.uop.is_amo)) ||
      io.lsu_io.forward_val
    val memresp_uop    = Mux(io.lsu_io.forward_val, io.lsu_io.forward_uop,
      io.dmem.resp.bits.uop)

    var memresp_data:UInt= null
    if (!fp_mem_support)
    {
      memresp_data = Mux(io.lsu_io.forward_val, io.lsu_io.forward_data
        , io.dmem.resp.bits.data_subword)
    }
    else
    {
      // TODO CODE REVIEW throwing resources to try and salvage critical path...
      //recode FP values
      // I'm doing this twice for two different paths (cache path and forwarding path)!
      // Also, this code is duplicated elsewhere - can we refactor this out?
      val typ = io.dmem.resp.bits.typ
      val load_single = typ === rocket.MT_W || typ === rocket.MT_WU
      val rec_s = hardfloat.recFNFromFN(8, 24, io.dmem.resp.bits.data)
      val rec_d = hardfloat.recFNFromFN(11, 53, io.dmem.resp.bits.data)
      val fp_load_data_recoded = Mux(load_single, Cat(SInt(-1, 32), rec_s), rec_d)

      val typ_f = io.lsu_io.forward_uop.mem_typ
      val load_single_f = typ_f === rocket.MT_W || typ_f === rocket.MT_WU
      val rec_s_f = hardfloat.recFNFromFN(8, 24, io.lsu_io.forward_data)
      val rec_d_f = hardfloat.recFNFromFN(11, 53, io.lsu_io.forward_data)
      val fp_load_data_recoded_forwarded = Mux(load_single_f, Cat(SInt(-1,32), rec_s_f), rec_d_f)

      memresp_data = Mux(io.lsu_io.forward_val && !io.lsu_io.forward_uop.fp_val, io.lsu_io.forward_data,
        Mux(io.lsu_io.forward_val && io.lsu_io.forward_uop.fp_val,  fp_load_data_recoded_forwarded,
          Mux(io.dmem.resp.bits.uop.fp_val,                     fp_load_data_recoded,
            io.dmem.resp.bits.data_subword)))
    }

    io.lsu_io.memresp.valid := memresp_val
    io.lsu_io.memresp.bits  := memresp_uop

    io.resp(1).valid                := memresp_val || fdiv_resp_val || muldiv_resp_val
    io.resp(1).bits.uop             := MuxCase(memresp_uop, Seq(
      memresp_val -> memresp_uop,
      fdiv_resp_val -> fdiv_resp_uop,
      muldiv_resp_val -> muldiv_resp_uop))
    io.resp(1).bits.uop.ctrl.rf_wen := (memresp_val && memresp_rf_wen) || fdiv_resp_val || muldiv_resp_val
    io.resp(1).bits.data            := MuxCase(memresp_data, Seq(
      memresp_val -> memresp_data,
      fdiv_resp_val -> fdiv_resp_data,
      muldiv_resp_val -> muldiv_resp_data))
    io.resp(1).bits.fflags          := fdiv_resp_fflags
  }







object execute_inst {
  def main(args: Array[String]): Unit = {
    SpinalConfig(
      defaultConfigForClockDomains = ClockDomainConfig(resetKind = ASYNC,
        clockEdge = RISING,
        resetActiveLevel = LOW),
      mode = Verilog,
      oneFilePerComponent = false,
      nameWhenByFile = false,
      inlineConditionalExpression = true,
      enumPrefixEnable = false,
      anonymSignalPrefix = "tmp",
      targetDirectory = "rtl_gen")
      .addStandardMemBlackboxing(blackboxAll)
      .generate(new execute())
  }.printPruned()
}