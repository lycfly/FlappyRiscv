import random
import math
import cocotb
import numpy as np
from cocotb.clock import Clock
from cocotb.triggers import FallingEdge, RisingEdge, Timer, Edge
import sys
sys.path.append("/home/lyc/projects/riscv/FlappyRiscv/src/main/scala/EasonLib/CocoSim/templates/")
from common_drivers import *
from number_utils import *

CLK_NS = 1

async def reset_dut(reset_n, duration_ns):
    reset_n.value = 0
    await Timer(duration_ns, units="ns")
    reset_n.value = 1
    reset_n._log.info("Reset complete.")


async def driver(dut, ):
    while (1):
        await RisingEdge(dut.clk)

async def fetch_done_driver(dut, is_ready, is_done):
    while (1):
        is_done.value = is_ready.value.integer
        await RisingEdge(dut.clk)


@cocotb.test()
async def tb(dut):
    # "Clock" is a built in class for toggling a clock signal
    cocotb.start_soon(Clock(dut.clk, CLK_NS, units='ns').start())
    # run reset_dut immediately before continuing
    await cocotb.start(reset_dut(dut.resetn, CLK_NS * 10))

    np.random.seed(2023)
    
    irom = []
    rvc = 1
    if rvc:
        instr16 = "1000110011100001"
        instr64_str = instr16*4
        # instr64_int = int('0b'+instr64,2)
        for i in range(1000):
            irom.append(instr64_str)
    else :
        instr32 = "11001100111000111100110011100011"
        instr64_str = instr32*2
        # instr64_int = int('0b'+instr64,2)
        for i in range(1000):
            irom.append(instr64_str)
        print(np.array(irom)[0])

    # Drivers
    
    dut.core_en.value = 0
    
    # dut.ibus_rsp_payload_id.value = 0
        
    dut.pcJumpTarget_payload.binstr = '0'
    
    dut.pcPredicted_payload.value = 0
    
    dut.fetchStage_flush.value = 0
    
    dut.ibus_cmd_ready.value = 1
    
    dut.pcJumpTarget_valid.value = 0
    
    dut.pcPredicted_valid.value = 0
        
    # dut.fetchStage_isDone.value = 0
    cocotb.start_soon(fetch_done_driver(dut, dut.fetchStage_isReady, dut.fetchStage_isDone))
    cocotb.start_soon(stream_sprom_driver(dut, "irom", irom, 
                              dut.ibus_cmd_valid, dut.ibus_cmd_payload_address, dut.ibus_cmd_ready,
                              dut.ibus_rsp_valid, dut.ibus_rsp_ready, dut.ibus_rsp_payload_rdata))
    await Timer( CLK_NS* 10, units="ns")
    await RisingEdge(dut.clk)

    dut.core_en.value = 1

    # Monitors
    # cocotb.start_soon()


    await Timer(10, units="ns")
    await RisingEdge(dut.clk)
    await RisingEdge(dut.clk)


    await Timer(100, units="ns")


if __name__ == '__main__':
    print('test')