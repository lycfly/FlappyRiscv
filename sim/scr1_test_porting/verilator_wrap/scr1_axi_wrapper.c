
#include <stdio.h>
#include <verilated.h>
#include "Vscr1_top_tb_axi.h"
#ifdef VCD_TRACE
#include "verilated_vcd_c.h"
#endif // #ifdef VCD_TRACE
#ifdef FST_TRACE
#include "verilated_fst_c.h"
#endif // #ifdef FST_TRACE

#define STRINGIFY(s) _STRINGIFY(s)
#define _STRINGIFY(s) #s

Vscr1_top_tb_axi *top;

vluint64_t main_time = 0;

int main(int argc, char** argv) {
    Verilated::commandArgs(argc, argv);

    top = new Vscr1_top_tb_axi;
#ifdef VCD_TRACE
    Verilated::traceEverOn(true);
    VerilatedVcdC* tfp = new VerilatedVcdC;
#ifdef TRACE_LVLV
    top->trace(tfp, TRACE_LVLV);
#else
    top->trace(tfp, 99);  // Trace 99 levels of hierarchy by default
#endif // #ifdef TRACE_LVLV

#ifdef VCD_FNAME
    tfp->open(STRINGIFY(VCD_FNAME));
#else
    tfp->open("./simx.vcd");
#endif // #ifdef VCD_FNAME
#endif // #ifdef VCD_TRACE

#ifdef FST_TRACE
    Verilated::traceEverOn(true);
    VerilatedFstC* tfp = new VerilatedFstC;
#ifdef TRACE_LVLV
    top->trace(tfp, TRACE_LVLV);
#else
    top->trace(tfp, 99);  // Trace 99 levels of hierarchy by default
#endif // #ifdef TRACE_LVLV

#ifdef FST_FNAME
    tfp->open(STRINGIFY(FST_FNAME));
#else
    tfp->open("./simx.fst");
#endif // #ifdef FST_FNAME
#endif // #ifdef FST_TRACE

    while (!Verilated::gotFinish()) {
        if ((main_time % 10) == 1) {
            top->clk = 1;
        }
        if ((main_time % 10) == 6) {
            top->clk = 0;
        }
        top->eval();
        main_time++;
#ifdef VCD_TRACE
        tfp->dump(main_time);
#elif FST_TRACE
        tfp->dump(main_time);
#endif // #ifdef VCD_TRACE
    }
    top->final();
#ifdef VCD_TRACE
    tfp->close();
#elif FST_TRACE
    tfp->close();
#endif // #ifdef VCD_TRACE
    delete top;
}

