package boom_v1.exec

import boom_v1.BranchType._
import boom_v1.RS1Type._
import boom_v1.RS2Type._
import boom_v1.MEMType._
import boom_v1.ScalarOpConstants._
import boom_v1.UOPs._
import boom_v1.Utils.{Fill, GetNewBrMask, ImmGen, IsKilledByBranch, MuxLookup, Sext, maskMatch}
import boom_v1.commit.{Exception, RobPCRequest}
import boom_v1.exec.FPU.FPConstants
import boom_v1.exec.FPU.hardfloat.fNFromRecFN
import boom_v1.predictor.boom.BpdUpdate
import boom_v1.{Causes, MStatus, MaskedDC, MicroOp, Parameters}
import boom_v1.predictor.{BHTUpdate, BTBUpdate, BranchPredictionResp, CFIType}
import spinal.sim._
import spinal.core._
import spinal.lib._
object FUConstants
{
  // bit mask, since a given execution pipeline may support multiple functional units
  val FUC_SZ = 8
  val FU_X   = MaskedDC(FUC_SZ)
  val FU_ALU = U(  1, FUC_SZ bits)
  val FU_BRU = U(  2, FUC_SZ bits)
  val FU_MEM = U(  4, FUC_SZ bits)
  val FU_MUL = U(  8, FUC_SZ bits)
  val FU_DIV = U( 16, FUC_SZ bits)
  val FU_FPU = U( 32, FUC_SZ bits)
  val FU_CSR = U( 64, FUC_SZ bits)
  val FU_FDV = U(128, FUC_SZ bits)
}
// tell the FUDecoders what units it needs to support
class SupportedFuncUnits(
                          val alu: Boolean  = false,
                          val bru: Boolean  = false,
                          val mem: Boolean  = false,
                          val muld: Boolean = false,
                          val fpu: Boolean  = false,
                          val csr: Boolean  = false,
                          val fdiv: Boolean = false)
{
}


class FunctionalUnitIo(num_stages: Int
                       , num_bypass_stages: Int
                       , data_width: Int
                      )(implicit p: Parameters) extends Bundle
{
  val req     = slave(Stream(new FuncUnitReq(data_width)))
  val resp    = master Stream(new FuncUnitResp(data_width))

  val brinfo  = new BrResolutionInfo().asInput

  val bypass  = new BypassData(num_bypass_stages, data_width).asOutput

  val br_unit = new BranchUnitResp().asOutput

  // only used by the fpu unit
  val fcsr_rm = in UInt(FPConstants.RM_SZ bits)

  // only used by branch unit
  // TODO name this, so ROB can also instantiate it
  val get_rob_pc = new RobPCRequest().flip
  val get_pred = new GetPredictionInfo
  val status = new MStatus().asInput
}

class GetPredictionInfo(implicit p: Parameters) extends Bundle
{
  val br_tag = out UInt(p.BR_TAG_SZ bits)
  val info = new BranchPredictionResp().asInput
}

class FuncUnitReq(data_width: Int)(implicit p: Parameters) extends Bundle
{
  val uop = new MicroOp()

  val num_operands = 3

  val rs1_data = UInt(width = data_width bits)
  val rs2_data = UInt(width = data_width bits)
  val rs3_data = UInt(width = data_width bits) // only used for FMA units
  //   val rs_data = Vec.fill(num_operands) {UInt(width=data_width)}
  //   def rs1_data = rs_data(0)
  //   def rs2_data = rs_data(1)
  //   def rs3_data = rs_data(2)

  val kill = Bool() // kill everything

}

class FuncUnitResp(data_width: Int)(implicit p: Parameters) extends Bundle
{
  val uop = new MicroOp()
  val data = UInt(width = data_width bits)
  val fflags = Flow(new FFlagsResp)
  val addr = UInt(width = p.vaddrBits+1 bits) // only for maddr -> LSU
  val mxcpt = Flow(UInt(width=Causes.all.max bits)) //only for maddr->LSU

}

class BypassData(num_bypass_ports: Int, data_width: Int)(implicit p: Parameters) extends Bundle
{
  val valid = Vec(Bool(), num_bypass_ports)
  val uop   = Vec(new MicroOp(), num_bypass_ports)
  val data  = Vec(UInt(width = data_width bits), num_bypass_ports)

  def getNumPorts: Int = num_bypass_ports
}



class BrResolutionInfo(implicit p: Parameters) extends Bundle
{
  val valid      = Bool()
  val mispredict = Bool()
  val mask       = UInt(width = p.MAX_BR_COUNT bits) // the resolve mask
  val tag        = UInt(width = p.BR_TAG_SZ bits)    // the branch tag that was resolved
  val exe_mask   = UInt(width = p.MAX_BR_COUNT bits) // the br_mask of the actual branch uop
  // used to reset the dec_br_mask
  val rob_idx    = UInt(width = p.ROB_ADDR_SZ bits)
  val ldq_idx    = UInt(width = p.MEM_ADDR_SZ bits)  // track the "tail" of loads and stores, so we can
  val stq_idx    = UInt(width = p.MEM_ADDR_SZ bits)  // quickly reset the LSU on a mispredict
  val taken      = Bool()                     // which direction did the branch go?
  val is_jr      = Bool()

  // for stats
  val btb_made_pred  = Bool()
  val btb_mispredict = Bool()
  val bpd_made_pred  = Bool()
  val bpd_mispredict = Bool()
}

// for critical path reasons, some of the elements in this bundle may be delayed.
class BranchUnitResp(implicit p: Parameters) extends Bundle
{
  val take_pc         = Bool()
  val target          = UInt(width = p.vaddrBits+1 bits)

  val pc              = UInt(width = p.vaddrBits+1 bits) // TODO this isn't really a branch_unit thing

  val brinfo          = new BrResolutionInfo()
  val btb_update_valid= Bool() // TODO turn this into a directed bundle so we can fold this into btb_update?
  val btb_update      = new BTBUpdate
  val bht_update      = Flow(new BHTUpdate)
  val bpd_update      = Flow(new BpdUpdate)

  val xcpt            = Flow(new Exception)

  val debug_btb_pred  = Bool() // just for debug, did the BTB and BHT predict taken?
}
abstract class FunctionalUnit(is_pipelined: Boolean
                              , num_stages: Int
                              , num_bypass_stages: Int
                              , data_width: Int
                              , has_branch_unit: Boolean = false)
                             (implicit p: Parameters) extends Module
{
  val io = new FunctionalUnitIo(num_stages, num_bypass_stages, data_width)
}


// Note: this helps track which uops get killed while in intermediate stages,
// but it is the job of the consumer to check for kills on the same cycle as consumption!!!
abstract class PipelinedFunctionalUnit(val num_stages: Int,
                                       val num_bypass_stages: Int,
                                       val earliest_bypass_stage: Int,
                                       val data_width: Int,
                                       is_branch_unit: Boolean = false
                                      )(implicit p: Parameters) extends FunctionalUnit(is_pipelined = true
  , num_stages = num_stages
  , num_bypass_stages = num_bypass_stages
  , data_width = data_width
  , has_branch_unit = is_branch_unit)(p)
{
  // pipelined functional unit is always ready
  io.req.ready := Bool(true)


  if (num_stages > 0)
  {
    val r_valids = RegInit(init = Vec.fill(num_stages) { Bool(false) })
    val r_uops   = Reg(Vec(new MicroOp(),num_stages))

    // handle incoming request
    r_valids(0) := io.req.valid && !IsKilledByBranch(io.brinfo, io.req.payload.uop) && !io.req.payload.kill
    r_uops(0)   := io.req.payload.uop
    r_uops(0).br_mask := GetNewBrMask(io.brinfo, io.req.payload.uop)

    // handle middle of the pipeline
    for (i <- 1 until num_stages)
    {
      r_valids(i) := r_valids(i-1) && !IsKilledByBranch(io.brinfo, r_uops(i-1)) && !io.req.payload.kill
      r_uops(i)   := r_uops(i-1)
      r_uops(i).br_mask := GetNewBrMask(io.brinfo, r_uops(i-1))

      if (num_bypass_stages != 0)// && i > earliest_bypass_stage)
      {
        io.bypass.uop(i-1) := r_uops(i-1)
      }
    }

    // handle outgoing (branch could still kill it)
    // consumer must also check for pipeline flushes (kills)
    io.resp.valid    := r_valids(num_stages-1) && !IsKilledByBranch(io.brinfo, r_uops(num_stages-1))
    io.resp.payload.uop := r_uops(num_stages-1)
    io.resp.payload.uop.br_mask := GetNewBrMask(io.brinfo, r_uops(num_stages-1))

    // bypassing (TODO allow bypass vector to have a different size from num_stages)
    if (num_bypass_stages > 0 && earliest_bypass_stage == 0)
    {
      io.bypass.uop(0) := io.req.payload.uop

      for (i <- 1 until num_bypass_stages)
      {
        io.bypass.uop(i) := r_uops(i-1)
      }
    }
  }
  else
  {
    require (num_stages == 0)
    // pass req straight through to response

    // valid doesn't check kill signals, let consumer deal with it.
    // The LSU already handles it and this hurts critical path.
    io.resp.valid    := io.req.valid && !IsKilledByBranch(io.brinfo, io.req.payload.uop)
    io.resp.payload.uop := io.req.payload.uop
    io.resp.payload.uop.br_mask := GetNewBrMask(io.brinfo, io.req.payload.uop)
  }

}

class ALUUnit(is_branch_unit: Boolean = false, num_stages: Int = 1)(implicit p: Parameters)
  extends PipelinedFunctionalUnit(num_stages = num_stages
    , num_bypass_stages = num_stages
    , earliest_bypass_stage = 0
    , data_width = 64  //xLen
    , is_branch_unit = is_branch_unit)(p)
{
  val uop = io.req.payload.uop

  // immediate generation
  val imm_xprlen = ImmGen(uop.imm_packed, uop.ctrl.imm_sel.asBits)

  // operand 1 select
  var op1_data: UInt = null
  if (is_branch_unit)
  {
    op1_data = Mux(io.req.payload.uop.ctrl.op1_sel === OP1_RS1 , io.req.payload.rs1_data,
      Mux(io.req.payload.uop.ctrl.op1_sel === OP1_PC  , Sext(io.get_rob_pc.curr_pc, p.xLen),
        U(0)))
  }
  else
  {
    op1_data = Mux(io.req.payload.uop.ctrl.op1_sel === OP1_RS1 , io.req.payload.rs1_data,
      U(0))
  }

  // operand 2 select
  val op2_data = Mux(io.req.payload.uop.ctrl.op2_sel === OP2_IMM,  Sext(imm_xprlen.asUInt, p.xLen),
    Mux(io.req.payload.uop.ctrl.op2_sel === OP2_IMMC, io.req.payload.uop.pop1(4 downto 0),
      Mux(io.req.payload.uop.ctrl.op2_sel === OP2_RS2 , io.req.payload.rs2_data,
        Mux(io.req.payload.uop.ctrl.op2_sel === OP2_FOUR, U(4),
          U(0)))))

  val alu = (new ALU())

  alu.io.in1 := op1_data
  alu.io.in2 := op2_data
  alu.io.fn  := io.req.payload.uop.ctrl.op_fcn
  alu.io.dw  := io.req.payload.uop.ctrl.fcn_dw


  if (is_branch_unit)
  {
    val uop_pc_ = io.get_rob_pc.curr_pc

    // The Branch Unit redirects the PC immediately, but delays the mispredict
    // signal a cycle (for critical path reasons)

    // Did I just get killed by the previous cycle's branch,
    // or by a flush pipeline?
    val killed = Bool(false)
    when (io.req.payload.kill ||
      (io.brinfo.valid &&
        io.brinfo.mispredict &&
        maskMatch(io.brinfo.mask, io.req.payload.uop.br_mask)
        ))
    {
      killed := Bool(true)
    }

    val rs1 = io.req.payload.rs1_data
    val rs2 = io.req.payload.rs2_data
    val br_eq  = (rs1 === rs2)
    val br_ltu = (rs1 < rs2)
    val br_lt  = (~(rs1(p.xLen-1) ^ rs2(p.xLen-1)) & br_ltu |
      rs1(p.xLen-1) & ~rs2(p.xLen-1))

    val pc_plus4 = (uop_pc_ + U(4))(p.vaddrBits downto 0)

    val pc_sel = MuxLookup(io.req.payload.uop.ctrl.br_type, PC_PLUS4,
      Seq  (
        BR_N  -> PC_PLUS4,
        BR_NE -> Mux(!br_eq,  PC_BRJMP, PC_PLUS4),
        BR_EQ -> Mux( br_eq,  PC_BRJMP, PC_PLUS4),
        BR_GE -> Mux(!br_lt,  PC_BRJMP, PC_PLUS4),
        BR_GEU-> Mux(!br_ltu, PC_BRJMP, PC_PLUS4),
        BR_LT -> Mux( br_lt,  PC_BRJMP, PC_PLUS4),
        BR_LTU-> Mux( br_ltu, PC_BRJMP, PC_PLUS4),
        BR_J  -> PC_BRJMP,
        BR_JR -> PC_JALR
      ))

    val bj_addr = UInt()

    val is_taken = io.req.valid &&
      !killed &&
      uop.is_br_or_jmp &&
      (pc_sel =/= PC_PLUS4)

    // "mispredict" means that a branch has been resolved and it must be killed
    val mispredict = (Bool()); mispredict := Bool(false)

    val is_br          = io.req.valid && !killed && uop.is_br_or_jmp && !uop.is_jump
    val is_br_or_jalr  = io.req.valid && !killed && uop.is_br_or_jmp && !uop.is_jal

    // did the BTB predict a br or jmp incorrectly?
    // (do we need to reset its history and teach it a new target?)
    val btb_mispredict = (Bool()); btb_mispredict := Bool(false)

    // did the bpd predict incorrectly (aka, should we correct its prediction?)
    val bpd_mispredict = (Bool()); bpd_mispredict := Bool(false)

    // if b/j is taken, does it go to the wrong target?
    val wrong_taken_target = !io.get_rob_pc.next_val || (io.get_rob_pc.next_pc =/= bj_addr)

    assert (!(io.req.valid && uop.is_jal && io.get_rob_pc.next_val && io.get_rob_pc.next_pc =/= bj_addr),
      "[func] JAL went to the wrong target.")

    when (is_br_or_jalr)
    {
      when (pc_sel === PC_JALR)
      {
        // only the BTB can predict JALRs (must also check it predicted taken)
        btb_mispredict := wrong_taken_target ||
          !io.get_pred.info.btb_resp.taken ||
          !uop.br_prediction.btb_hit ||
          io.status.debug // fun hack to perform fence.i on JALRs in debug mode
        bpd_mispredict := Bool(false)
      }
      when (pc_sel === PC_PLUS4)
      {
        btb_mispredict := uop.br_prediction.btb_hit && io.get_pred.info.btb_resp.taken
        bpd_mispredict := uop.br_prediction.bpd_predict_taken
      }
      when (pc_sel === PC_BRJMP)
      {
        btb_mispredict := wrong_taken_target ||
          !uop.br_prediction.btb_hit ||
          (uop.br_prediction.btb_hit && !io.get_pred.info.btb_resp.taken)
        bpd_mispredict := !uop.br_prediction.bpd_predict_taken
      }
    }

    when (is_br_or_jalr && pc_sel === PC_BRJMP && !mispredict && io.get_rob_pc.next_val)
    {
      when (io.get_rob_pc.next_pc =/= bj_addr)
      {
        // printf ("[FuncUnit] Branch jumped to 0x%x, should have jumped to 0x%x.\n",
        //   io.get_rob_pc.next_pc, bj_addr)
      }
      assert (io.get_rob_pc.next_pc === bj_addr, "[FuncUnit] branch is taken to the wrong target.")
    }

    when (is_br_or_jalr)
    {
      when (pc_sel === PC_JALR)
      {
        mispredict := btb_mispredict
      }
      when (pc_sel === PC_PLUS4)
      {
        mispredict := Mux(uop.br_prediction.wasBTB, btb_mispredict, bpd_mispredict)
      }
      when (pc_sel === PC_BRJMP)
      {
        mispredict := Mux(uop.br_prediction.wasBTB, btb_mispredict, bpd_mispredict)
      }
    }


    val br_unit =
      if (p.enableBrResolutionRegister) Reg(new BranchUnitResp)
      else (new BranchUnitResp)



    br_unit.take_pc := mispredict
    br_unit.target := Mux(pc_sel === PC_PLUS4, pc_plus4, bj_addr)

    // Delay branch resolution a cycle for critical path reasons.
    // If the rest of "br_unit" is being registered too, then we don't need to
    // register "brinfo" here, since in that case we would be double counting.
    val brinfo =
    if (p.enableBrResolutionRegister) (new BrResolutionInfo)
    else Reg(new BrResolutionInfo)

    // note: jal doesn't allocate a branch-mask, so don't clear a br-mask bit
    brinfo.valid          := io.req.valid && uop.is_br_or_jmp && !uop.is_jal && !killed
    brinfo.mispredict     := mispredict
    brinfo.mask           := U(1) << uop.br_tag
    brinfo.exe_mask       := GetNewBrMask(io.brinfo, uop.br_mask)
    brinfo.tag            := uop.br_tag
    brinfo.rob_idx        := uop.rob_idx
    brinfo.ldq_idx        := uop.ldq_idx
    brinfo.stq_idx        := uop.stq_idx
    brinfo.is_jr          := pc_sel === PC_JALR
    brinfo.taken          := is_taken
    brinfo.btb_mispredict := btb_mispredict
    brinfo.bpd_mispredict := bpd_mispredict
    brinfo.btb_made_pred  := uop.br_prediction.wasBTB
    brinfo.bpd_made_pred  := uop.br_prediction.bpd_predict_val

    br_unit.brinfo := brinfo

    // updates the BTB same cycle as PC redirect
    val lsb = log2Up(p.FETCH_WIDTH*p.coreInstBytes)

    // did a branch or jalr occur AND did we mispredict? AND was it taken? (i.e., should we update the BTB)
    val fetch_pc = ((uop_pc_ >> lsb) << lsb) + uop.fetch_pc_lob

    if (p.enableBTBContainsBranches)
    {
      br_unit.btb_update_valid := is_br_or_jalr && mispredict && is_taken
      // update on all branches (but not jal/jalr)
      br_unit.bht_update.valid := is_br && mispredict
    }
    else
    {
      br_unit.btb_update_valid := is_br_or_jalr && mispredict && uop.is_jump
      br_unit.bht_update.valid := Bool(false)
    }

    br_unit.btb_update.pc               := fetch_pc // tell the BTB which pc to tag check against
    br_unit.btb_update.br_pc            := uop_pc_
    br_unit.btb_update.target           := (br_unit.target.asSInt & S(-p.coreInstBytes, br_unit.target.getWidth bits)).asUInt
    br_unit.btb_update.prediction.valid := io.get_pred.info.btb_resp_valid // did this branch's fetch packet have
    // a BTB hit in fetch?
    br_unit.btb_update.prediction.payload  := io.get_pred.info.btb_resp       // give the BTB back its BTBResp
    br_unit.btb_update.taken            := is_taken   // was this branch/jal/jalr "taken"
    br_unit.btb_update.cfiType           := Mux(uop.is_jump , U(CFIType.jump) , Mux(uop.is_ret, U(CFIType.ret), U(CFIType.branch)))
//    br_unit.btb_update.isReturn         := uop.is_ret

    br_unit.bht_update.payload.taken            := is_taken   // was this branch "taken"
    br_unit.bht_update.payload.mispredict       := btb_mispredict     // need to reset the history in the BHT
    // that is updated only on BTB hits
    br_unit.bht_update.payload.prediction.valid := io.get_pred.info.btb_resp_valid // only update if hit in the BTB
    br_unit.bht_update.payload.prediction.payload  := io.get_pred.info.btb_resp
    br_unit.bht_update.payload.pc               := fetch_pc // what pc should the tag check be on?

    br_unit.bpd_update.valid                 := io.req.valid && uop.is_br_or_jmp &&
      !uop.is_jal && !killed
    br_unit.bpd_update.payload.is_br            := is_br
    br_unit.bpd_update.payload.brob_idx         := io.get_rob_pc.curr_brob_idx
    br_unit.bpd_update.payload.taken            := is_taken
    br_unit.bpd_update.payload.mispredict       := mispredict
    br_unit.bpd_update.payload.bpd_predict_val  := uop.br_prediction.bpd_predict_val
    br_unit.bpd_update.payload.bpd_mispredict   := bpd_mispredict
    br_unit.bpd_update.payload.pc               := fetch_pc
    br_unit.bpd_update.payload.br_pc            := uop_pc_
    br_unit.bpd_update.payload.history_ptr      := io.get_pred.info.bpd_resp.history_ptr
    br_unit.bpd_update.payload.info             := io.get_pred.info.bpd_resp.info
    if (!p.ENABLE_VLHR)
    {
      br_unit.bpd_update.payload.history.get := io.get_pred.info.bpd_resp.history.get
      br_unit.bpd_update.payload.history_u.get := io.get_pred.info.bpd_resp.history_u.get
    }

    // is the br_pc the last instruction in the fetch bundle?
    val is_last_inst = if (p.FETCH_WIDTH == 1) { Bool(true) }
    else { ((uop_pc_ >> UInt(log2Up(p.coreInstBytes) bits)) &
      (U(1)#*log2Up(p.FETCH_WIDTH)).asUInt) === U(p.FETCH_WIDTH-1) }
    br_unit.bpd_update.payload.new_pc_same_packet := !(is_taken) && !is_last_inst

    require (p.coreInstBytes == 4)


    // Branch/Jump Target Calculation
    // we can't push this through the ALU though, b/c jalr needs both PC+4 and rs1+offset

    def vaSign(a0: UInt, ea: UInt):Bool = {
      // efficient means to compress 64-bit VA into rc.as.vaddrBits+1 bits
      // (VA is bad if VA(rc.as.vaddrBits) =/= VA(rc.as.vaddrBits-1))
      val a = a0 >> p.vaddrBits-1
      val e = ea(p.vaddrBits downto p.vaddrBits-1)
      Mux(a === U(0) || a === U(1), e =/= U(0),
        Mux(a.asSInt === S(-1) || a.asSInt === S(-2), e.asSInt === S(-1),
          e(0)))
    }

    val bj_base = Mux(uop.uopc === uopJALR, io.req.payload.rs1_data, uop_pc_)
    val bj_offset = imm_xprlen(20 downto 0)
    val bj64 = (bj_base.asSInt + bj_offset).asUInt
    val bj_msb = Mux(uop.uopc === uopJALR, vaSign(io.req.payload.rs1_data, bj64), vaSign(uop_pc_, bj64))
    bj_addr := (Cat(bj_msb, bj64(p.vaddrBits-1 downto 0)).asSInt & S(-2)).asUInt

    br_unit.pc             := uop_pc_
    br_unit.debug_btb_pred := io.get_pred.info.btb_resp_valid && io.get_pred.info.btb_resp.taken

    // handle misaligned branch/jmp targets
    // TODO BUG only trip xcpt if taken to bj_addr
    br_unit.xcpt.valid     := bj_addr(1) && io.req.valid && mispredict && !killed
    br_unit.xcpt.payload.uop  := uop
    br_unit.xcpt.payload.cause:= U(Causes.misaligned_fetch)
    // TODO is there a better way to get this information to the CSR file? maybe use brinfo.target?
    br_unit.xcpt.payload.badvaddr:= bj_addr

    io.br_unit := br_unit
  }

  // Response
  // TODO add clock gate on resp bits from functional units
  //   io.resp.bits.data := RegEnable(alu.io.out, io.req.valid)
  //   val reg_data = Reg(outType = Bits(width = xLen))
  //   reg_data := alu.io.out
  //   io.resp.bits.data := reg_data

  val r_val  = RegInit(init = Vec.fill(num_stages) { Bool(false) })
  val r_data = Reg(Vec(UInt(width=p.xLen bits),num_stages))
  r_val (0) := io.req.valid
  r_data(0) := alu.io.out
  for (i <- 1 until num_stages)
  {
    r_val(i)  := r_val(i-1)
    r_data(i) := r_data(i-1)
  }
  io.resp.payload.data := r_data(num_stages-1)

  // Bypass
  // for the ALU, we can bypass same cycle as compute
  require (num_stages >= 1)
  require (num_bypass_stages >= 1)
  io.bypass.valid(0) := io.req.valid
  io.bypass.data (0) := alu.io.out
  for (i <- 1 until num_stages)
  {
    io.bypass.valid(i) := r_val(i-1)
    io.bypass.data (i) := r_data(i-1)
  }

  // Exceptions
  io.resp.payload.fflags.valid := Bool(false)
}


// passes in base+imm to calculate addresses, and passes store data, to the LSU
// for floating point, 65bit FP store-data needs to be decoded into 64bit FP form
class MemAddrCalcUnit(implicit p: Parameters) extends PipelinedFunctionalUnit(num_stages = 0
  , num_bypass_stages = 0
  , earliest_bypass_stage = 0
  , data_width = 65 // TODO enable this only if FP is enabled?
  , is_branch_unit = false)(p)
{
  // perform address calculation
  val sum = (io.req.payload.rs1_data.asSInt + io.req.payload.uop.imm_packed(19 downto 8).asSInt).asUInt
  val ea_sign = Mux(sum(p.vaddrBits-1), ~sum(63 downto p.vaddrBits) === U(0),
    sum(63 downto p.vaddrBits) =/= U(0))
  val effective_address = Cat(ea_sign, sum(p.vaddrBits-1 downto 0)).asUInt

  // compute store data
  // requires decoding 65-bit FP data
  val unrec_s = fNFromRecFN(8, 24, io.req.payload.rs2_data.asBits)
  val unrec_d = fNFromRecFN(11, 53, io.req.payload.rs2_data.asBits)
  val unrec_out = Mux(io.req.payload.uop.fp_single, Cat(Fill(32, unrec_s(31)), unrec_s).asUInt, unrec_d)

  var store_data:UInt = null
  if (!p.usingFPU) store_data = io.req.payload.rs2_data
  else store_data = Mux(io.req.payload.uop.fp_val, unrec_out, io.req.payload.rs2_data)

  io.resp.payload.addr := effective_address
  io.resp.payload.data := store_data

  if (data_width > 63)
  {
    assert (!(io.req.valid && io.req.payload.uop.ctrl.is_std &&
      io.resp.payload.data(64) === Bool(true)), "65th bit set in MemAddrCalcUnit.")
  }

  // Handle misaligned exceptions
  val typ = io.req.payload.uop.mem_typ
  val misaligned =
    (((typ === MT_H.asBits.asUInt) || (typ === MT_HU.asBits.asUInt)) && (effective_address(0) =/= U(0))) ||
      (((typ === MT_W.asBits.asUInt) || (typ === MT_WU.asBits.asUInt)) && (effective_address(1 downto 0) =/= U(0))) ||
      ((typ ===  MT_D.asBits.asUInt) && (effective_address(2 downto 0) =/= U(0)))

  val ma_ld = io.req.valid && io.req.payload.uop.uopc === uopLD && misaligned
  val ma_st = io.req.valid && (io.req.payload.uop.uopc === uopSTA || io.req.payload.uop.uopc === uopAMO_AG) && misaligned

  io.resp.payload.mxcpt.valid := ma_ld || ma_st
  io.resp.payload.mxcpt.payload  := Mux(ma_ld, U(Causes.misaligned_load),
    U(Causes.misaligned_store))
  assert (!(ma_ld && ma_st), "Mutually-exclusive exceptions are firing.")
}



// currently, bypassing is unsupported!
// All FP instructions are padded out to the max latency unit for easy
// write-port scheduling.
class FPUUnit(implicit p: Parameters) extends PipelinedFunctionalUnit(num_stages = 3
  , num_bypass_stages = 0
  , earliest_bypass_stage = 0
  , data_width = 65)(p)
{
  val fpu = (new FPU())
  fpu.io.req <> io.req
  fpu.io.req.bits.fcsr_rm := io.fcsr_rm

  io.resp.bits.data              := fpu.io.resp.bits.data
  io.resp.bits.fflags.valid      := fpu.io.resp.bits.fflags.valid
  io.resp.bits.fflags.bits.uop   := io.resp.bits.uop
  io.resp.bits.fflags.bits.flags := fpu.io.resp.bits.fflags.bits.flags // kill me now
}



// unpipelined, can only hold a single MicroOp at a time
// assumes at least one register between request and response
abstract class UnPipelinedFunctionalUnit(implicit p: Parameters)
  extends FunctionalUnit(is_pipelined = false
    , num_stages = 1
    , num_bypass_stages = 0
    , data_width = 64
    , has_branch_unit = false)(p)
{
  val r_uop = Reg(new MicroOp())

  val do_kill = (Bool())
  do_kill := io.req.payload.kill // irrelevant default

  when (io.req.fire)
  {
    // update incoming uop
    do_kill := IsKilledByBranch(io.brinfo, io.req.payload.uop) || io.req.payload.kill
    r_uop := io.req.payload.uop
    r_uop.br_mask := GetNewBrMask(io.brinfo, io.req.payload.uop)
  }
    .otherwise
    {
      do_kill := IsKilledByBranch(io.brinfo, r_uop) || io.req.payload.kill
      r_uop.br_mask := GetNewBrMask(io.brinfo, r_uop)
    }


  // assumes at least one pipeline register between request and response
  io.resp.payload.uop := r_uop
}


class MulDivUnit(implicit p: Parameters) extends UnPipelinedFunctionalUnit()(p)
{
  val muldiv = (new MulDiv(p.mulDivParams, width = p.xLen))

  // request
  muldiv.io.req.valid    := io.req.valid && !this.do_kill
  muldiv.io.req.bits.dw  := io.req.payload.uop.ctrl.fcn_dw
  muldiv.io.req.bits.fn  := io.req.payload.uop.ctrl.op_fcn
  muldiv.io.req.bits.in1 := io.req.payload.rs1_data
  muldiv.io.req.bits.in2 := io.req.payload.rs2_data
  io.req.ready           := muldiv.io.req.ready

  // handle pipeline kills and branch misspeculations
  muldiv.io.kill         := this.do_kill

  // response
  io.resp.valid          := muldiv.io.resp.valid
  muldiv.io.resp.ready   := io.resp.ready
  io.resp.payload.data      := muldiv.io.resp.bits.data
}

class PipelinedMulUnit(num_stages: Int)(implicit p: Parameters)
  extends PipelinedFunctionalUnit (num_stages = num_stages
    , num_bypass_stages = 0
    , earliest_bypass_stage = 0
    , data_width = 64)(p)
{
  val imul = (new IMul(num_stages))
  // request
  imul.io.valid := io.req.valid
  imul.io.in0   := io.req.payload.rs1_data
  imul.io.in1   := io.req.payload.rs2_data
  imul.io.dw    := io.req.payload.uop.ctrl.fcn_dw
  imul.io.fn    := io.req.payload.uop.ctrl.op_fcn

  // response
  io.resp.payload.data      := imul.io.out
}

}

