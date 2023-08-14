import random
import math
import cocotb
import numpy as np
from cocotb.clock import Clock
from cocotb.triggers import FallingEdge, RisingEdge, Timer, Edge
import sys
sys.path.append("/home/lyc/projects/riscv/FlappyRiscv/src/main/scala/EasonLib/CocoSim/templates/")
from common_drivers import spram_driver,sprom_driver

CLK_NS = 1

async def reset_dut(reset_n, duration_ns):
    reset_n.value = 0
    await Timer(duration_ns, units="ns")
    reset_n.value = 1
    reset_n._log.info("Reset complete.")


async def driver(dut, ):
    await RisingEdge(dut.resetn)
    while(1):
        await RisingEdge(dut.clk)
        dut.uop_0_rs1_index.value = 1
        dut.uop_0_rs2_index.value = 2
        dut.uop_0_rd_index.value = 3
        dut.uop_1_rs1_index.value = 4
        dut.uop_1_rs2_index.value = 5
        dut.uop_1_rd_index.value = 6

        dut.uop_0_rs1_source.value = 0
        dut.uop_0_rs1_is_used.value = 1
        dut.uop_0_rs1_kind.value = 0
        dut.uop_0_rs1_width.value = 2
        dut.uop_0_rs2_source.value = 0
        dut.uop_0_rs2_is_used.value = 1
        dut.uop_0_rs2_kind.value = 0
        dut.uop_0_rs2_width.value = 2
        dut.uop_0_rs2_source.value = 0
        dut.uop_0_rd_is_used.value = 1
        dut.uop_0_rd_kind.value = 0
        dut.uop_0_rd_width.value = 2

        dut.uop_1_rs1_source.value = 0
        dut.uop_1_rs1_is_used.value = 1
        dut.uop_1_rs1_kind.value = 0
        dut.uop_1_rs1_width.value = 2
        dut.uop_1_rs2_source.value = 0
        dut.uop_1_rs2_is_used.value = 1
        dut.uop_1_rs2_kind.value = 0
        dut.uop_1_rs2_width.value = 2
        dut.uop_1_rd_is_used.value = 1
        dut.uop_1_rd_kind.value = 0
        dut.uop_1_rd_width.value = 2
        if dut.ready.value == 1:
            dut.valid.value = 1
        else:
            dut.valid.value = 0


@cocotb.test()
async def tb(dut):
    # "Clock" is a built in class for toggling a clock signal
    cocotb.start_soon(Clock(dut.clk, CLK_NS, units='ns').start())
    # run reset_dut immediately before continuing
    await cocotb.start(reset_dut(dut.resetn, CLK_NS * 10))

    np.random.seed(2023)
    
    # Drivers
    
    dut.uop_1_rs1_source.value = 0
    
    dut.uop_1_rs2_is_used.value = 0
    
    dut.valid.value = 0
    
    dut.uop_1_imm.value = 0
    
    dut.uop_1_rs2_index.value = 0
    
    dut.uop_0_rs1_is_used.value = 0
    
    dut.uop_1_rd_index.value = 0
    
    dut.uop_1_rs2_kind.value = 0
    
    dut.flush.value = 0
    
    dut.uop_0_rs1_index.value = 0
    
    dut.uop_1_rd_is_used.value = 0
    
    dut.uop_0_rd_width.value = 0
    
    dut.uop_1_rs1_is_used.value = 0
    
    dut.uop_1_rs1_width.value = 0
    
    dut.uop_0_rs2_is_used.value = 0
    
    dut.retired_rdInfo_0_phyIndex.value = 0
    
    dut.uop_0_rs2_width.value = 0
    
    dut.retired_rdInfo_1_valid.value = 0
    
    dut.uop_0_rs1_kind.value = 0
    
    dut.uop_0_rd_is_used.value = 0
    
    dut.uop_1_op.value = 0
    
    dut.uop_1_rs2_width.value = 0
    
    dut.uop_0_rd_index.value = 0
    
    dut.uop_1_rd_width.value = 0
    
    dut.uop_0_op.value = 0
    
    dut.uop_0_rs1_width.value = 0
    
    dut.uop_1_rs2_source.value = 0
    
    dut.retired_ready_1.value = 0
    
    dut.retired_rdInfo_1_phyIndex.value = 0
    
    dut.uop_0_rs1_source.value = 0
    
    dut.uop_0_imm.value = 0
    
    dut.retired_rdInfo_0_valid.value = 0
    
    dut.uop_1_rs1_kind.value = 0
    
    dut.uop_1_pc.value = 0
    
    dut.retired_rdInfo_1_oldPhyIndex.value = 0
    
    dut.uop_0_pc.value = 0
    
    dut.uop_0_rs2_source.value = 0
    
    dut.uop_1_rd_kind.value = 0
    
    dut.retired_flist_restore.value = 0
    
    dut.retired_rdInfo_0_archIndex.value = 0
    
    dut.retired_branch_error_restore.value = 0
    
    dut.uop_0_rs2_kind.value = 0
    
    dut.uop_1_rs1_index.value = 0
    
    dut.retired_ready_0.value = 0
    
    dut.retired_rdInfo_1_archIndex.value = 0
    
    dut.retired_rdInfo_0_oldPhyIndex.value = 0
    
    dut.uop_0_rd_kind.value = 0
    
    dut.uop_0_rs2_index.value = 0
    
    # cocotb.start_soon(spram_driver(dut, "ram", ramblock, dut.mem_rd, dut.mem_wr, dut.mem_addr, dut.mem_wdata,dut.mem_rdata))
    cocotb.start_soon(driver(dut))

    # Monitors
    # cocotb.start_soon()


    await Timer(10, units="ns")
    await RisingEdge(dut.clk)
    await RisingEdge(dut.clk)


    await Timer(1000, units="ns")


if __name__ == '__main__':
    print('test')