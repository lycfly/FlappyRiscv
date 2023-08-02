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
    while (1):
        await RisingEdge(dut.clk)



@cocotb.test()
async def tb(dut):
    # "Clock" is a built in class for toggling a clock signal
    cocotb.start_soon(Clock(dut.clk, CLK_NS, units='ns').start())
    # run reset_dut immediately before continuing
    await cocotb.start(reset_dut(dut.resetn, CLK_NS * 10))

    np.random.seed(2023)
    
    # Drivers
    
    dut.core_en.value = 0
    
    # dut.ibus_rsp_payload_id.value = 0
    
    dut.ibus_rsp_valid.value = 0
    
    dut.pcJumpTarget_payload.value = 0
    
    dut.pcPredicted_payload.value = 0
    
    dut.fetchStage_flush.value = 0
    
    dut.ibus_cmd_ready.value = 0
    
    dut.pcJumpTarget_valid.value = 0
    
    dut.pcPredicted_valid.value = 0
    
    dut.ibus_rsp_payload_rdata.value = 0
    
    dut.fetchStage_isDone.value = 0
    
    # cocotb.start_soon(spram_driver(dut, "ram", ramblock, dut.mem_rd, dut.mem_wr, dut.mem_addr, dut.mem_wdata,dut.mem_rdata))

    # Monitors
    # cocotb.start_soon()


    await RisingEdge(dut.resetn)
    await Timer(10, units="ns")
    await RisingEdge(dut.clk)
    await RisingEdge(dut.clk)


    await Timer(1000, units="ns")


if __name__ == '__main__':
    print('test')