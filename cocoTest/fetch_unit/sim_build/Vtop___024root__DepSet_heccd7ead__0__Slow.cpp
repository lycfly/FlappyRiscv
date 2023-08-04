// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See Vtop.h for the primary calling header

#include "verilated.h"
#include "verilated_dpi.h"

#include "Vtop___024root.h"

VL_ATTR_COLD void Vtop___024root___eval_static(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_static\n"); );
}

VL_ATTR_COLD void Vtop___024root___eval_initial__TOP(Vtop___024root* vlSelf);

VL_ATTR_COLD void Vtop___024root___eval_initial(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_initial\n"); );
    // Body
    Vtop___024root___eval_initial__TOP(vlSelf);
    vlSelf->__Vtrigrprev__TOP__clk = vlSelf->clk;
    vlSelf->__Vtrigrprev__TOP__resetn = vlSelf->resetn;
}

VL_ATTR_COLD void Vtop___024root___eval_initial__TOP(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_initial__TOP\n"); );
    // Body
    vlSelf->fetch_unit__DOT__ibus_rsp_ready = 1U;
    vlSelf->fetch_unit__DOT__valid_instr_slice_3 = 1U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_0_1 = 0U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_1_1 = 1U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_2_1 = 2U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_3_1 = 3U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrs_inc_0_1 = 0U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrs_inc_1_1 = 1U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num = 1U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num = 1U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_5 = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0 = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x1 = 1U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x2 = 2U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_5 = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0 = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x1 = 1U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x2 = 2U;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__mode = 1U;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__lead = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__trail = 1U;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__mode = 1U;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__lead = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__trail = 1U;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__mode = 1U;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__lead = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__trail = 1U;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__mode = 1U;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__lead = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__trail = 1U;
}

VL_ATTR_COLD void Vtop___024root___eval_final(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_final\n"); );
}

VL_ATTR_COLD void Vtop___024root___eval_triggers__stl(Vtop___024root* vlSelf);
#ifdef VL_DEBUG
VL_ATTR_COLD void Vtop___024root___dump_triggers__stl(Vtop___024root* vlSelf);
#endif  // VL_DEBUG
VL_ATTR_COLD void Vtop___024root___eval_stl(Vtop___024root* vlSelf);

VL_ATTR_COLD void Vtop___024root___eval_settle(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_settle\n"); );
    // Init
    CData/*0:0*/ __VstlContinue;
    // Body
    vlSelf->__VstlIterCount = 0U;
    __VstlContinue = 1U;
    while (__VstlContinue) {
        __VstlContinue = 0U;
        Vtop___024root___eval_triggers__stl(vlSelf);
        if (vlSelf->__VstlTriggered.any()) {
            __VstlContinue = 1U;
            if (VL_UNLIKELY((0x64U < vlSelf->__VstlIterCount))) {
#ifdef VL_DEBUG
                Vtop___024root___dump_triggers__stl(vlSelf);
#endif
                VL_FATAL_MT("/home/lyc/projects/riscv/FlappyRiscv/cocoTest/fetch_unit/rtl/fetch_unit.v", 7, "", "Settle region did not converge.");
            }
            vlSelf->__VstlIterCount = ((IData)(1U) 
                                       + vlSelf->__VstlIterCount);
            Vtop___024root___eval_stl(vlSelf);
        }
    }
}

#ifdef VL_DEBUG
VL_ATTR_COLD void Vtop___024root___dump_triggers__stl(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___dump_triggers__stl\n"); );
    // Body
    if ((1U & (~ (IData)(vlSelf->__VstlTriggered.any())))) {
        VL_DBG_MSGF("         No triggers active\n");
    }
    if (vlSelf->__VstlTriggered.at(0U)) {
        VL_DBG_MSGF("         'stl' region trigger index 0 is active: Internal 'stl' trigger - first iteration\n");
    }
}
#endif  // VL_DEBUG

void Vtop___024root___ico_sequent__TOP__0(Vtop___024root* vlSelf);

VL_ATTR_COLD void Vtop___024root___eval_stl(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_stl\n"); );
    // Body
    if (vlSelf->__VstlTriggered.at(0U)) {
        Vtop___024root___ico_sequent__TOP__0(vlSelf);
    }
}

#ifdef VL_DEBUG
VL_ATTR_COLD void Vtop___024root___dump_triggers__ico(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___dump_triggers__ico\n"); );
    // Body
    if ((1U & (~ (IData)(vlSelf->__VicoTriggered.any())))) {
        VL_DBG_MSGF("         No triggers active\n");
    }
    if (vlSelf->__VicoTriggered.at(0U)) {
        VL_DBG_MSGF("         'ico' region trigger index 0 is active: Internal 'ico' trigger - first iteration\n");
    }
}
#endif  // VL_DEBUG

#ifdef VL_DEBUG
VL_ATTR_COLD void Vtop___024root___dump_triggers__act(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___dump_triggers__act\n"); );
    // Body
    if ((1U & (~ (IData)(vlSelf->__VactTriggered.any())))) {
        VL_DBG_MSGF("         No triggers active\n");
    }
    if (vlSelf->__VactTriggered.at(0U)) {
        VL_DBG_MSGF("         'act' region trigger index 0 is active: @(posedge clk or negedge resetn)\n");
    }
    if (vlSelf->__VactTriggered.at(1U)) {
        VL_DBG_MSGF("         'act' region trigger index 1 is active: @(posedge clk)\n");
    }
}
#endif  // VL_DEBUG

#ifdef VL_DEBUG
VL_ATTR_COLD void Vtop___024root___dump_triggers__nba(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___dump_triggers__nba\n"); );
    // Body
    if ((1U & (~ (IData)(vlSelf->__VnbaTriggered.any())))) {
        VL_DBG_MSGF("         No triggers active\n");
    }
    if (vlSelf->__VnbaTriggered.at(0U)) {
        VL_DBG_MSGF("         'nba' region trigger index 0 is active: @(posedge clk or negedge resetn)\n");
    }
    if (vlSelf->__VnbaTriggered.at(1U)) {
        VL_DBG_MSGF("         'nba' region trigger index 1 is active: @(posedge clk)\n");
    }
}
#endif  // VL_DEBUG

VL_ATTR_COLD void Vtop___024root___ctor_var_reset(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___ctor_var_reset\n"); );
    // Body
    vlSelf->core_en = VL_RAND_RESET_I(1);
    vlSelf->ibus_cmd_valid = VL_RAND_RESET_I(1);
    vlSelf->ibus_cmd_ready = VL_RAND_RESET_I(1);
    vlSelf->ibus_cmd_payload_address = VL_RAND_RESET_I(32);
    vlSelf->ibus_rsp_valid = VL_RAND_RESET_I(1);
    vlSelf->ibus_rsp_ready = VL_RAND_RESET_I(1);
    vlSelf->ibus_rsp_payload_rdata = VL_RAND_RESET_Q(64);
    vlSelf->if2id_itf_0_instr_data = VL_RAND_RESET_I(32);
    vlSelf->if2id_itf_0_instr_pc = VL_RAND_RESET_I(32);
    vlSelf->if2id_itf_0_valid = VL_RAND_RESET_I(1);
    vlSelf->if2id_itf_1_instr_data = VL_RAND_RESET_I(32);
    vlSelf->if2id_itf_1_instr_pc = VL_RAND_RESET_I(32);
    vlSelf->if2id_itf_1_valid = VL_RAND_RESET_I(1);
    vlSelf->fetchStage_flush = VL_RAND_RESET_I(1);
    vlSelf->fetchStage_isDone = VL_RAND_RESET_I(1);
    vlSelf->fetchStage_isReady = VL_RAND_RESET_I(1);
    vlSelf->pcJumpTarget_valid = VL_RAND_RESET_I(1);
    vlSelf->pcJumpTarget_payload = VL_RAND_RESET_I(32);
    vlSelf->pcPredicted_valid = VL_RAND_RESET_I(1);
    vlSelf->pcPredicted_payload = VL_RAND_RESET_I(32);
    vlSelf->clk = VL_RAND_RESET_I(1);
    vlSelf->resetn = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__core_en = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__ibus_cmd_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__ibus_cmd_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__ibus_cmd_payload_address = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__ibus_rsp_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__ibus_rsp_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__ibus_rsp_payload_rdata = VL_RAND_RESET_Q(64);
    vlSelf->fetch_unit__DOT__if2id_itf_0_instr_data = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__if2id_itf_0_instr_pc = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__if2id_itf_0_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__if2id_itf_1_instr_data = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__if2id_itf_1_instr_pc = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__if2id_itf_1_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__fetchStage_flush = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__fetchStage_isDone = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__fetchStage_isReady = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__pcJumpTarget_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__pcJumpTarget_payload = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__pcPredicted_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__pcPredicted_payload = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__clk = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__resetn = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo_io_push_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo_io_flush = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo_push_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo_push_payload = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo_pop_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo_flush = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo_io_flush = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_i = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_i = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo_io_push_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo_io_head = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo_io_occupancy = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo_push_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo_pop_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_out_data = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_real = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo_head = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo_occupancy = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo_io_occupancy = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo_io_ov_waterline = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_inst = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_illegal = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_inst = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_illegal = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_io_push_0_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__tmp_io_push_0_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_io_push_1_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__tmp_io_push_1_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_io_push_2_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__tmp_io_push_2_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_io_push_3_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__tmp_io_push_3_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc_1 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc_1 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc_2 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__pc = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__default_next_pc = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__fetch_pending = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressOffset = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__ibus_cmd_fire = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__valid_instr_slice_0 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__valid_instr_slice_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__valid_instr_slice_2 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__valid_instr_slice_3 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_slices_0_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_slices_0_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instr_slices_1_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_slices_1_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instr_slices_2_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_slices_2_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instr_slices_3_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_slices_3_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__tail_one_matrix_0 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__tail_one_matrix_1 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__tail_one_matrix_2 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__tail_one_matrix_3 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__tail_one_index_vec_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__tail_one_index_vec_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__tail_one_index_vec_2 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__tail_one_index_vec_3 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_0 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_2 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_3 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__slice_valid_vec_0 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__slice_valid_vec_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__slice_valid_vec_2 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__slice_valid_vec_3 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_instr_slices_0_payload = VL_RAND_RESET_I(16);
    vlSelf->fetch_unit__DOT__tmp_2 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__tmp_3 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__tmp_4 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__tmp_5 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__ibus_rsp_fire = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_push_0_fire = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_pop_valid = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_pop_0_fire = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_pop_1_fire = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__pc_pop_offset = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__pc_step_all_1 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__pc_step_all_2 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__real_instr_0 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__real_instr_1 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__pc_step_0 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__pc_step_1 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__tmp_real_instr_0 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_real_instr_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__toplevel_addressFifo_io_pop_fire = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__tmp_2_string = VL_RAND_RESET_Q(56);
    vlSelf->fetch_unit__DOT__tmp_3_string = VL_RAND_RESET_Q(56);
    vlSelf->fetch_unit__DOT__tmp_4_string = VL_RAND_RESET_Q(56);
    vlSelf->fetch_unit__DOT__tmp_5_string = VL_RAND_RESET_Q(56);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_payload = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_payload = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_head = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_flush = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_occupancy = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__clk = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__resetn = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_pushPtr_valueNext = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_pushPtr_valueNext_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_popPtr_valueNext = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_popPtr_valueNext_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_io_occupancy = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_0 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_1 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_2 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_3 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_4 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_5 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willClear = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_valueNext = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willOverflowIfInc = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willOverflow = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willClear = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_valueNext = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willOverflowIfInc = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willOverflow = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__empty = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__full = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushing = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popping = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__readed = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_1 = VL_RAND_RESET_I(8);
    vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrDif = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_payload = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_req_num = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_out_data = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_real = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__head = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__flush = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__occupancy = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__clk = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__resetn = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_occupancy = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_0 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_1 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_2 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_3 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_4 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_5 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__empty = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__full = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushing = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_pop_pop_out_data = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_1 = VL_RAND_RESET_I(8);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_vec_0 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_2 = VL_RAND_RESET_I(8);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrDif = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_0_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_0_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_0_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_1_valid = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_1_ready = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_1_payload = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_flush = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_occupancy = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_ov_waterline = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__clk = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__resetn = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_2 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_3 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_4 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_5 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_6 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_7 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_2 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_1_ready = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_2_ready = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_3_ready = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_0 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_0_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_1_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_2 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_2_1 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_3 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrs_inc_3_1 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrs_inc_0 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrs_inc_0_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrs_inc_1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrs_inc_1_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrGlobalInc = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrGlobalInc_1 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrGlobalInc = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrGlobalInc_1 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrGlobal = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrGlobal = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_occupancy = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_12 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_13 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__empty = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__full = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_0 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_2 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_3 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_0 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__inc_num = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__dec_num = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_0 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_2 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_3 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_0 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_inc_0 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_inc_1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_inc_2 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_inc_3 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_inc_0 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_inc_1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__readedVec_0 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__readedVec_1 = VL_RAND_RESET_Q(33);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1 = VL_RAND_RESET_I(16);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2 = VL_RAND_RESET_I(16);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3 = VL_RAND_RESET_I(16);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4 = VL_RAND_RESET_I(16);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobalInc = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobalInc = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrDif = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__mode = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__lead = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__trail = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_in = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__tmp_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__datain_reverse = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_node_0 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_node_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index2_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__empty = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__out_remap = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__mode = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__lead = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__trail = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_in = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__tmp_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__datain_reverse = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_node_0 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_node_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index2_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__empty = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__out_remap = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__mode = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__lead = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__trail = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_in = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__tmp_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__datain_reverse = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_node_0 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_node_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index2_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__empty = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__out_remap = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__mode = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__lead = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__trail = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_in = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__tmp_cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__datain_reverse = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_node_0 = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_node_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index2_0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__empty = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__out_remap = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__din_vld = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__data_in = VL_RAND_RESET_I(4);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__cnt_out = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_0 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_2 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_3 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele1 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage2_ele0 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__din_vld = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__data_in = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__cnt_out = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_init_0 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_init_1 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_stage1_ele0 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__i = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__decompInstr_inst = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__decompInstr_illegal = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst_1 = VL_RAND_RESET_I(6);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_1 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_3 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_4 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_5 = VL_RAND_RESET_I(7);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_8 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9 = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_10 = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi5spnImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__lwImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ldImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm_1 = VL_RAND_RESET_I(10);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm = VL_RAND_RESET_I(21);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_luiImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_luiImm_1 = VL_RAND_RESET_I(15);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__luiImm = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__shiftImm = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addi16spImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addi16spImm_1 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi16spImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jImm_1 = VL_RAND_RESET_I(10);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jImm = VL_RAND_RESET_I(21);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_bImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_bImm_1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm = VL_RAND_RESET_I(13);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x2 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__i = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__decompInstr_inst = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__decompInstr_illegal = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst_1 = VL_RAND_RESET_I(6);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_1 = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2 = VL_RAND_RESET_I(2);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_3 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_4 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_5 = VL_RAND_RESET_I(7);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7 = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_8 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9 = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_10 = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi5spnImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__lwImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ldImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm_1 = VL_RAND_RESET_I(10);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm = VL_RAND_RESET_I(21);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_luiImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_luiImm_1 = VL_RAND_RESET_I(15);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__luiImm = VL_RAND_RESET_I(32);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__shiftImm = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addi16spImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addi16spImm_1 = VL_RAND_RESET_I(3);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi16spImm = VL_RAND_RESET_I(12);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jImm_1 = VL_RAND_RESET_I(10);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jImm = VL_RAND_RESET_I(21);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_bImm = VL_RAND_RESET_I(1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_bImm_1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm = VL_RAND_RESET_I(13);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x1 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x2 = VL_RAND_RESET_I(5);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst = VL_RAND_RESET_I(32);
    vlSelf->__Vtrigrprev__TOP__clk = VL_RAND_RESET_I(1);
    vlSelf->__Vtrigrprev__TOP__resetn = VL_RAND_RESET_I(1);
}
