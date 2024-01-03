package boom_v1.regfile
import boom_v1.Parameters
import spinal.core._
import spinal.lib._
//******************************************************************************
// Copyright (c) 2015, The Regents of the University of California (Regents).
// All Rights Reserved. See LICENSE for license details.
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
// RISCV Processor Datapath Register File
//------------------------------------------------------------------------------
//------------------------------------------------------------------------------
//
// Christopher Celio
// 2013 May 1

  class RegisterFileReadPortIO(addr_width: Int, data_width: Int)(implicit p: Parameters) extends Bundle
  {
    val addr = in UInt(addr_width bits)
    val data = out Bits(data_width bits)
  }

  class RegisterFileWritePortIO(addr_width: Int, data_width: Int)(implicit p: Parameters) extends Bundle
  {
    val wen  = in Bool()
    val addr = in UInt(addr_width bits)
    val data = in Bits(data_width bits)
  }


  class RegisterFile( num_registers: Int
                      , num_read_ports: Int
                      , num_write_ports: Int
                      , register_width: Int
                      , enable_bypassing: Boolean)
                    (implicit p: Parameters) extends Module
  {
    val io = new Bundle
    {
      val read_ports = Vec(new RegisterFileReadPortIO(p.PREG_SZ, register_width), num_read_ports)
      val write_ports = Vec(new RegisterFileWritePortIO(p.PREG_SZ, register_width), num_write_ports)
    }

    // --------------------------------------------------------------

    val regfile = Mem(Bits(width=register_width bits), num_registers)

    // --------------------------------------------------------------

    val read_data = Vec(Bits(width = register_width bits),num_read_ports)

    for (i <- 0 until num_read_ports)
    {
      read_data(i) := Mux(io.read_ports(i).addr === U(0), B(0),
        regfile(io.read_ports(i).addr))
    }

    // --------------------------------------------------------------
    // bypass out of the ALU's write ports

    if (enable_bypassing)
    {
      for (i <- 0 until num_read_ports)
      {
        val bypass_ens = io.write_ports.map(x => x.wen &&
          x.addr =/= U(0) &&
          x.addr === io.read_ports(i).addr)

        val bypass_data = MuxOH(Vec(bypass_ens), Vec(io.write_ports.map(_.data)))

        io.read_ports(i).data := Mux(bypass_ens.reduce(_|_), bypass_data, read_data(i))
      }
    }
    else
    {
      for (i <- 0 until num_read_ports)
      {
        io.read_ports(i).data := read_data(i)
      }
    }

    // --------------------------------------------------------------

    for (i <- 0 until num_write_ports)
    {
      when (io.write_ports(i).wen && (io.write_ports(i).addr =/= U(0)))
      {
        regfile(io.write_ports(i).addr) := io.write_ports(i).data
      }
      //      if (DEBUG_PRINTF)
      //      {
      //         printf("writeport[%d], %s -> p%d = 0x%x\n", UInt(i), Mux(io.write_ports(i).wen, Str("WEN"), Str(" "))
      //            , io.write_ports(i).addr
      //            , io.write_ports(i).data
      //            )
      //      }
    }
  }


}
