// Verilated -*- C++ -*-
// DESCRIPTION: Verilator output: Design implementation internals
// See Vtop.h for the primary calling header

#include "verilated.h"
#include "verilated_dpi.h"

#include "Vtop___024root.h"

extern const VlUnpacked<CData/*2:0*/, 8> Vtop__ConstPool__TABLE_he16900a8_0;
extern const VlUnpacked<QData/*55:0*/, 4> Vtop__ConstPool__TABLE_hd552ea8d_0;

VL_INLINE_OPT void Vtop___024root___ico_sequent__TOP__0(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___ico_sequent__TOP__0\n"); );
    // Init
    CData/*0:0*/ fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0;
    fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0 = 0;
    CData/*0:0*/ fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0;
    fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0 = 0;
    CData/*0:0*/ fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0;
    fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0 = 0;
    CData/*0:0*/ fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0;
    fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0 = 0;
    CData/*1:0*/ __Vtableidx1;
    __Vtableidx1 = 0;
    CData/*1:0*/ __Vtableidx2;
    __Vtableidx2 = 0;
    CData/*1:0*/ __Vtableidx3;
    __Vtableidx3 = 0;
    CData/*1:0*/ __Vtableidx4;
    __Vtableidx4 = 0;
    CData/*2:0*/ __Vtableidx5;
    __Vtableidx5 = 0;
    CData/*2:0*/ __Vtableidx6;
    __Vtableidx6 = 0;
    // Body
    vlSelf->fetch_unit__DOT__core_en = vlSelf->core_en;
    vlSelf->fetch_unit__DOT__ibus_cmd_ready = vlSelf->ibus_cmd_ready;
    vlSelf->fetch_unit__DOT__ibus_rsp_valid = vlSelf->ibus_rsp_valid;
    vlSelf->fetch_unit__DOT__ibus_rsp_payload_rdata 
        = vlSelf->ibus_rsp_payload_rdata;
    vlSelf->fetch_unit__DOT__fetchStage_flush = vlSelf->fetchStage_flush;
    vlSelf->fetch_unit__DOT__pcJumpTarget_valid = vlSelf->pcJumpTarget_valid;
    vlSelf->fetch_unit__DOT__pcJumpTarget_payload = vlSelf->pcJumpTarget_payload;
    vlSelf->fetch_unit__DOT__pcPredicted_valid = vlSelf->pcPredicted_valid;
    vlSelf->fetch_unit__DOT__pcPredicted_payload = vlSelf->pcPredicted_payload;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_1 
        = (0xfU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_1 
        = (0xfU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_2 
        = (0xfU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_4 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal;
    vlSelf->ibus_rsp_ready = vlSelf->fetch_unit__DOT__ibus_rsp_ready;
    vlSelf->fetch_unit__DOT__default_next_pc = ((IData)(8U) 
                                                + vlSelf->fetch_unit__DOT__pc);
    vlSelf->fetch_unit__DOT__instrFifo_io_flush = 0U;
    vlSelf->fetch_unit__DOT__validNumFifo_flush = 0U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_0 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willOverflowIfInc 
        = (3U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willOverflowIfInc 
        = (3U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value));
    vlSelf->fetch_unit__DOT__fetchStage_isDone = vlSelf->fetchStage_isDone;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_1 
        = (0xfU & ((IData)(1U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_2 
        = (0xfU & ((IData)(2U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_3 
        = (0xfU & ((IData)(3U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal)));
    vlSelf->fetch_unit__DOT__clk = vlSelf->clk;
    vlSelf->fetch_unit__DOT__resetn = vlSelf->resetn;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrDif 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value) 
                 - (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrDif 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal) 
                 - (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal)));
    vlSelf->fetch_unit__DOT__ibus_cmd_payload_address 
        = (((IData)(vlSelf->pcJumpTarget_valid) & (IData)(vlSelf->ibus_cmd_ready))
            ? vlSelf->pcJumpTarget_payload : (((IData)(vlSelf->pcPredicted_valid) 
                                               & (IData)(vlSelf->ibus_cmd_ready))
                                               ? vlSelf->pcPredicted_payload
                                               : vlSelf->fetch_unit__DOT__pc));
    vlSelf->fetch_unit__DOT__addressFifo_io_flush = 0U;
    if (vlSelf->fetchStage_flush) {
        vlSelf->fetch_unit__DOT__instrFifo_io_flush = 1U;
        vlSelf->fetch_unit__DOT__validNumFifo_flush = 1U;
        vlSelf->fetch_unit__DOT__addressFifo_io_flush = 1U;
    }
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc_1 
        = vlSelf->fetch_unit__DOT__pc_pop_offset;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value) 
           == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data 
        = ((0U == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal))
            ? (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_0)
            : ((1U == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal))
                ? (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_1)
                : ((2U == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal))
                    ? (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_2)
                    : (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_3))));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch 
        = ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal) 
           == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal));
    vlSelf->fetch_unit__DOT__ibus_rsp_fire = ((IData)(vlSelf->fetch_unit__DOT__ibus_rsp_ready) 
                                              & (IData)(vlSelf->ibus_rsp_valid));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal) 
           == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrDif 
        = (0xfU & ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal) 
                   - (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_5 
        = (0xfU & ((IData)(1U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed 
        = ((0U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value))
            ? vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_0
            : ((1U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value))
                ? vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_1
                : ((2U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value))
                    ? vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_2
                    : vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_3)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_flush 
        = vlSelf->fetch_unit__DOT__instrFifo_io_flush;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__flush 
        = vlSelf->fetch_unit__DOT__validNumFifo_flush;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1 
        = (0xffffU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_0)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_0_ready 
        = vlSelf->fetch_unit__DOT__fetchStage_isDone;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_1_ready 
        = vlSelf->fetch_unit__DOT__fetchStage_isDone;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_1 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_1))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_1));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_2 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_2))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_2));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_3 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_3))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_3));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__clk 
        = vlSelf->fetch_unit__DOT__clk;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__clk 
        = vlSelf->fetch_unit__DOT__clk;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__clk = vlSelf->fetch_unit__DOT__clk;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__resetn 
        = vlSelf->fetch_unit__DOT__resetn;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__resetn 
        = vlSelf->fetch_unit__DOT__resetn;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__resetn 
        = vlSelf->fetch_unit__DOT__resetn;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_payload 
        = vlSelf->fetch_unit__DOT__ibus_cmd_payload_address;
    vlSelf->ibus_cmd_payload_address = vlSelf->fetch_unit__DOT__ibus_cmd_payload_address;
    if (vlSelf->fetch_unit__DOT__addressFifo_io_flush) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__io_flush = 1U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willClear = 0U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willClear = 0U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willClear = 1U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willClear = 1U;
    } else {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__io_flush = 0U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willClear = 0U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willClear = 0U;
    }
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc_1 
        = vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc_1;
    vlSelf->fetch_unit__DOT__addressFifo_io_occupancy 
        = ((((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy) 
             & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch)) 
            << 2U) | (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrDif));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__empty 
        = ((~ (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy)) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__full 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy));
    vlSelf->fetch_unit__DOT__validNumFifo_head = vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data;
    vlSelf->fetch_unit__DOT__validNumFifo_occupancy 
        = ((((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy) 
             & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch)) 
            << 2U) | (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrDif));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__full 
        = ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch) 
           & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__empty 
        = ((~ (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy)) 
           & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__empty 
        = ((~ (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy)) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__full = 
        ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch) 
         & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_occupancy 
        = (0xfU & ((IData)(0xcU) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrDif)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_1 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_5))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_5));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_0 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
        = ((8U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
            ? ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11
                : ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8)))
            : ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                ? ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4))
                : ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0))));
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed;
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc 
        = (vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed 
           + vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc_1);
    vlSelf->fetch_unit__DOT__addressOffset = (3U & 
                                              (vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed 
                                               >> 1U));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2 
        = (0xffffU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_1)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3 
        = (0xffffU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_2)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4 
        = (0xffffU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_3)));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_occupancy 
        = vlSelf->fetch_unit__DOT__addressFifo_io_occupancy;
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_valid 
        = (1U & (~ (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__empty)));
    vlSelf->fetch_unit__DOT__addressFifo_io_push_ready 
        = (1U & (~ (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__full)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__head 
        = vlSelf->fetch_unit__DOT__validNumFifo_head;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_pop_pop_out_data 
        = vlSelf->fetch_unit__DOT__validNumFifo_head;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__occupancy 
        = vlSelf->fetch_unit__DOT__validNumFifo_occupancy;
    vlSelf->fetch_unit__DOT__validNumFifo_push_ready 
        = (1U & (~ (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__full)));
    vlSelf->fetch_unit__DOT__validNumFifo_pop_ready 
        = (1U & (~ ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__empty) 
                    | (0U == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data)))));
    vlSelf->fetch_unit__DOT__instrFifo_io_occupancy 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch)
            ? ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy)
                ? 0xcU : 0U) : (((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal) 
                                 < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal))
                                 ? (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrDif)
                                 : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_occupancy)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1 
        = (0xfU & ((IData)(1U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_1)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ldImm 
        = ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                              >> 5U)) << 6U)) | (0x38U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 0xaU)) 
                                                    << 3U)));
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_payload 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0;
    vlSelf->fetch_unit__DOT__tmp_pc_step_all_1 = (1U 
                                                  & (IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 0x20U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_8 
        = (0x1fU & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                            >> 7U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi5spnImm 
        = ((0x3c0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                               >> 7U)) << 6U)) | ((0x30U 
                                                   & ((IData)(
                                                              (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                               >> 0xbU)) 
                                                      << 4U)) 
                                                  | ((8U 
                                                      & ((IData)(
                                                                 (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                  >> 5U)) 
                                                         << 3U)) 
                                                     | (4U 
                                                        & ((IData)(
                                                                   (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                    >> 6U)) 
                                                           << 2U)))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__lwImm 
        = ((0x40U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                              >> 5U)) << 6U)) | ((0x38U 
                                                  & ((IData)(
                                                             (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                              >> 0xaU)) 
                                                     << 3U)) 
                                                 | (4U 
                                                    & ((IData)(
                                                               (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                >> 6U)) 
                                                       << 2U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9 
        = ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                              >> 7U)) << 6U)) | (0x3cU 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 9U)) 
                                                    << 2U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl 
        = (8U | (7U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                               >> 2U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_i = (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2 
        = (3U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                         >> 0xaU)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch 
        = (8U | (7U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                               >> 7U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6 
        = (0x1fU & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                            >> 2U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7 
        = (1U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                         >> 0xcU)));
    vlSelf->fetch_unit__DOT__addressFifo_io_head = vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_payload 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_head 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__readed 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload;
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc 
        = vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc;
    vlSelf->fetch_unit__DOT__valid_instr_slice_1 = 
        (1U >= (IData)(vlSelf->fetch_unit__DOT__addressOffset));
    vlSelf->fetch_unit__DOT__valid_instr_slice_2 = 
        (2U >= (IData)(vlSelf->fetch_unit__DOT__addressOffset));
    vlSelf->fetch_unit__DOT__valid_instr_slice_0 = 
        (0U >= (IData)(vlSelf->fetch_unit__DOT__addressOffset));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_valid 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_valid;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_ready 
        = vlSelf->fetch_unit__DOT__addressFifo_io_push_ready;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_ready 
        = vlSelf->fetch_unit__DOT__validNumFifo_push_ready;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_ready 
        = vlSelf->fetch_unit__DOT__validNumFifo_pop_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_occupancy 
        = vlSelf->fetch_unit__DOT__instrFifo_io_occupancy;
    vlSelf->fetch_unit__DOT__instrFifo_io_ov_waterline 
        = (4U <= (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_occupancy));
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid 
        = (1U < (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_occupancy));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready 
        = (0xfU & ((IData)(0xcU) - (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_occupancy)));
    vlSelf->fetchStage_isReady = (0U < (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_occupancy));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
        = ((8U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
            ? ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11
                : ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8)))
            : ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                ? ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4))
                : ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0))));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_0_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_payload;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__readedVec_0 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_payload;
    vlSelf->fetch_unit__DOT__pc_step_all_1 = ((IData)(vlSelf->fetch_unit__DOT__tmp_pc_step_all_1)
                                               ? 2U
                                               : 4U);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_10 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__i 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2_i;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__shiftImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_luiImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addi16spImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_bImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addi16spImm_1 
        = (7U & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_luiImm_1 
        = (0x7fffU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_bImm_1 
        = (0x1fU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm_1 
        = (0x3ffU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_4 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7) 
            << 2U) | (3U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                    >> 5U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst_1 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7) 
            << 5U) | (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm 
        = ((0xfe0U & ((- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))) 
                      << 5U)) | (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6));
    vlSelf->fetch_unit__DOT__instr_slices_0_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instr_slices_0_valid = 0U;
    vlSelf->fetch_unit__DOT__tmp_2 = 3U;
    if (vlSelf->fetch_unit__DOT__tmp_1) {
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_0) {
            vlSelf->fetch_unit__DOT__instr_slices_0_payload 
                = (((QData)((IData)((0xffffU & (IData)(vlSelf->ibus_rsp_payload_rdata)))) 
                    << 0x10U) | (QData)((IData)(vlSelf->fetch_unit__DOT__tmp_instr_slices_0_payload)));
            vlSelf->fetch_unit__DOT__instr_slices_0_valid = 1U;
            vlSelf->fetch_unit__DOT__tmp_2 = 1U;
        }
    } else if ((3U != (3U & (IData)(vlSelf->ibus_rsp_payload_rdata)))) {
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_0) {
            vlSelf->fetch_unit__DOT__instr_slices_0_payload 
                = (0x100000000ULL | (QData)((IData)(
                                                    (0xffffU 
                                                     & (IData)(vlSelf->ibus_rsp_payload_rdata)))));
            vlSelf->fetch_unit__DOT__instr_slices_0_valid = 1U;
        }
        vlSelf->fetch_unit__DOT__tmp_2 = 2U;
    } else {
        vlSelf->fetch_unit__DOT__tmp_2 = 0U;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_ov_waterline 
        = vlSelf->fetch_unit__DOT__instrFifo_io_ov_waterline;
    vlSelf->fetch_unit__DOT__fetch_pending = (1U & 
                                              ((~ (IData)(vlSelf->core_en)) 
                                               | ((~ 
                                                   ((IData)(vlSelf->fetch_unit__DOT__addressFifo_io_push_ready) 
                                                    & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_push_ready))) 
                                                  | (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_ov_waterline))));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_1_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_1 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid) 
           & (IData)(vlSelf->fetchStage_isDone));
    vlSelf->if2id_itf_1_valid = ((IData)(vlSelf->fetchStage_isDone) 
                                 & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_1_ready 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_2_ready 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_3_ready 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready;
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_ready 
        = (2U < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_ready 
        = (1U < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_ready 
        = (3U < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready 
        = (0U < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready));
    vlSelf->fetch_unit__DOT__fetchStage_isReady = vlSelf->fetchStage_isReady;
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_valid 
        = vlSelf->fetchStage_isReady;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_0_valid 
        = vlSelf->fetchStage_isReady;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_0 
        = ((IData)(vlSelf->fetchStage_isReady) & (IData)(vlSelf->fetchStage_isDone));
    vlSelf->if2id_itf_0_valid = ((IData)(vlSelf->fetchStage_isDone) 
                                 & (IData)(vlSelf->fetchStage_isReady));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ldImm 
        = ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                              >> 5U)) << 6U)) | (0x38U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 0xaU)) 
                                                    << 3U)));
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_payload 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1;
    vlSelf->fetch_unit__DOT__tmp_pc_step_all_2 = (1U 
                                                  & (IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 0x20U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_8 
        = (0x1fU & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                            >> 7U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi5spnImm 
        = ((0x3c0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                               >> 7U)) << 6U)) | ((0x30U 
                                                   & ((IData)(
                                                              (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                               >> 0xbU)) 
                                                      << 4U)) 
                                                  | ((8U 
                                                      & ((IData)(
                                                                 (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                  >> 5U)) 
                                                         << 3U)) 
                                                     | (4U 
                                                        & ((IData)(
                                                                   (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                    >> 6U)) 
                                                           << 2U)))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__lwImm 
        = ((0x40U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                              >> 5U)) << 6U)) | ((0x38U 
                                                  & ((IData)(
                                                             (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                              >> 0xaU)) 
                                                     << 3U)) 
                                                 | (4U 
                                                    & ((IData)(
                                                               (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                >> 6U)) 
                                                       << 2U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9 
        = ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                              >> 7U)) << 6U)) | (0x3cU 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 9U)) 
                                                    << 2U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl 
        = (8U | (7U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                               >> 2U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_i = (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2 
        = (3U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                         >> 0xaU)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch 
        = (8U | (7U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                               >> 7U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6 
        = (0x1fU & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                            >> 2U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7 
        = (1U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                         >> 0xcU)));
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc_2 
        = vlSelf->fetch_unit__DOT__pc_step_all_1;
    if ((2U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        if ((1U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
        }
    } else if ((1U & (~ vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))) {
        if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
                }
            } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                 >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
            }
        } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
            }
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
        } else if ((0U == (0x7ffU & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                     >> 2U)))) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
        }
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_illegal 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi16spImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addi16spImm_1) 
            << 9U) | ((0x180U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                          >> 3U)) << 7U)) 
                      | ((0x40U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                            >> 5U)) 
                                   << 6U)) | ((0x20U 
                                               & ((IData)(
                                                          (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                           >> 2U)) 
                                                  << 5U)) 
                                              | (0x10U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 6U)) 
                                                    << 4U))))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__luiImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_luiImm_1) 
            << 0x11U) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6) 
                         << 0xcU));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_bImm_1) 
            << 8U) | ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                         >> 5U)) << 6U)) 
                      | ((0x20U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                            >> 2U)) 
                                   << 5U)) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2) 
                                               << 3U) 
                                              | (6U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 3U)) 
                                                    << 1U))))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jImm_1 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm_1;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm_1) 
            << 0xbU) | ((0x400U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                            >> 8U)) 
                                   << 0xaU)) | ((0x300U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 9U)) 
                                                    << 8U)) 
                                                | ((0x80U 
                                                    & ((IData)(
                                                               (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                >> 6U)) 
                                                       << 7U)) 
                                                   | ((0x40U 
                                                       & ((IData)(
                                                                  (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                   >> 7U)) 
                                                          << 6U)) 
                                                      | ((0x20U 
                                                          & ((IData)(
                                                                     (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                      >> 2U)) 
                                                             << 5U)) 
                                                         | ((0x10U 
                                                             & ((IData)(
                                                                        (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                         >> 0xbU)) 
                                                                << 4U)) 
                                                            | (0xeU 
                                                               & ((IData)(
                                                                          (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                           >> 3U)) 
                                                                  << 1U)))))))));
    __Vtableidx5 = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_4;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_3 
        = Vtop__ConstPool__TABLE_he16900a8_0[__Vtableidx5];
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst_1;
    vlSelf->fetch_unit__DOT__slice_valid_vec_0 = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    __Vtableidx1 = vlSelf->fetch_unit__DOT__tmp_2;
    vlSelf->fetch_unit__DOT__tmp_2_string = Vtop__ConstPool__TABLE_hd552ea8d_0
        [__Vtableidx1];
    vlSelf->fetch_unit__DOT__instr_slices_1_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instr_slices_1_valid = 0U;
    vlSelf->fetch_unit__DOT__tmp_3 = 3U;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tmp_2))) {
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_1) {
            vlSelf->fetch_unit__DOT__instr_slices_1_payload 
                = (QData)((IData)(vlSelf->ibus_rsp_payload_rdata));
            vlSelf->fetch_unit__DOT__instr_slices_1_valid = 1U;
        }
        vlSelf->fetch_unit__DOT__tmp_3 = 1U;
    } else if (((1U == (IData)(vlSelf->fetch_unit__DOT__tmp_2)) 
                | (2U == (IData)(vlSelf->fetch_unit__DOT__tmp_2)))) {
        if ((3U != (3U & (IData)((vlSelf->ibus_rsp_payload_rdata 
                                  >> 0x10U))))) {
            if (vlSelf->fetch_unit__DOT__valid_instr_slice_1) {
                vlSelf->fetch_unit__DOT__instr_slices_1_payload 
                    = (0x100000000ULL | (QData)((IData)(
                                                        (0xffffU 
                                                         & (IData)(
                                                                   (vlSelf->ibus_rsp_payload_rdata 
                                                                    >> 0x10U))))));
                vlSelf->fetch_unit__DOT__instr_slices_1_valid = 1U;
            }
            vlSelf->fetch_unit__DOT__tmp_3 = 2U;
        } else {
            vlSelf->fetch_unit__DOT__tmp_3 = 0U;
        }
    }
    vlSelf->ibus_cmd_valid = (1U & (~ (IData)(vlSelf->fetch_unit__DOT__fetch_pending)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_2 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_1) 
           == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num));
    vlSelf->fetch_unit__DOT__if2id_itf_1_valid = vlSelf->if2id_itf_1_valid;
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_pop_1_fire 
        = vlSelf->if2id_itf_1_valid;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_init_1 
        = vlSelf->if2id_itf_1_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_ready 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_ready 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_ready 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_ready 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__din_vld 
        = vlSelf->if2id_itf_0_valid;
    vlSelf->fetch_unit__DOT__if2id_itf_0_valid = vlSelf->if2id_itf_0_valid;
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_pop_0_fire 
        = vlSelf->if2id_itf_0_valid;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_init_0 
        = vlSelf->if2id_itf_0_valid;
    vlSelf->fetch_unit__DOT__if2id_itf_0_instr_pc = 0U;
    vlSelf->fetch_unit__DOT__instr_pop_valid = (((IData)(vlSelf->if2id_itf_1_valid) 
                                                 << 1U) 
                                                | (IData)(vlSelf->if2id_itf_0_valid));
    vlSelf->fetch_unit__DOT__validNumFifo_pop_valid = 0U;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_1_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_payload;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__readedVec_1 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_payload;
    vlSelf->fetch_unit__DOT__pc_step_all_2 = (7U & 
                                              ((IData)(vlSelf->fetch_unit__DOT__pc_step_all_1) 
                                               + ((IData)(vlSelf->fetch_unit__DOT__tmp_pc_step_all_2)
                                                   ? 2U
                                                   : 4U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_10 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__i 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3_i;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__shiftImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_luiImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addi16spImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_bImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addi16spImm_1 
        = (7U & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_luiImm_1 
        = (0x7fffU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_bImm_1 
        = (0x1fU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm_1 
        = (0x3ffU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_4 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7) 
            << 2U) | (3U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                    >> 5U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst_1 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7) 
            << 5U) | (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm 
        = ((0xfe0U & ((- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))) 
                      << 5U)) | (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6));
    vlSelf->fetch_unit__DOT__if2id_itf_1_instr_pc = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__decompInstr_illegal 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_illegal;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst 
        = (0x5013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst) 
                       << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                     << 0xfU) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                 << 7U))));
    vlSelf->fetch_unit__DOT__slice_valid_vec_1 = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    __Vtableidx2 = vlSelf->fetch_unit__DOT__tmp_3;
    vlSelf->fetch_unit__DOT__tmp_3_string = Vtop__ConstPool__TABLE_hd552ea8d_0
        [__Vtableidx2];
    vlSelf->fetch_unit__DOT__instr_slices_2_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instr_slices_2_valid = 0U;
    vlSelf->fetch_unit__DOT__tmp_4 = 3U;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tmp_3))) {
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_2) {
            vlSelf->fetch_unit__DOT__instr_slices_2_payload 
                = (QData)((IData)((vlSelf->ibus_rsp_payload_rdata 
                                   >> 0x10U)));
            vlSelf->fetch_unit__DOT__instr_slices_2_valid = 1U;
        }
        vlSelf->fetch_unit__DOT__tmp_4 = 1U;
    } else if (((1U == (IData)(vlSelf->fetch_unit__DOT__tmp_3)) 
                | (2U == (IData)(vlSelf->fetch_unit__DOT__tmp_3)))) {
        if ((3U != (3U & (IData)((vlSelf->ibus_rsp_payload_rdata 
                                  >> 0x20U))))) {
            if (vlSelf->fetch_unit__DOT__valid_instr_slice_2) {
                vlSelf->fetch_unit__DOT__instr_slices_2_payload 
                    = (0x100000000ULL | (QData)((IData)(
                                                        (0xffffU 
                                                         & (IData)(
                                                                   (vlSelf->ibus_rsp_payload_rdata 
                                                                    >> 0x20U))))));
                vlSelf->fetch_unit__DOT__instr_slices_2_valid = 1U;
            }
            vlSelf->fetch_unit__DOT__tmp_4 = 2U;
        } else {
            vlSelf->fetch_unit__DOT__tmp_4 = 0U;
        }
    }
    vlSelf->fetch_unit__DOT__ibus_cmd_valid = vlSelf->ibus_cmd_valid;
    vlSelf->fetch_unit__DOT__ibus_cmd_fire = ((IData)(vlSelf->ibus_cmd_valid) 
                                              & (IData)(vlSelf->ibus_cmd_ready));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_1 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_2;
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__if2id_itf_0_instr_pc 
            = vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc;
    }
    vlSelf->if2id_itf_0_instr_pc = vlSelf->fetch_unit__DOT__if2id_itf_0_instr_pc;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__data_in 
        = vlSelf->fetch_unit__DOT__instr_pop_valid;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_stage1_ele0 
        = (3U & ((IData)(vlSelf->if2id_itf_0_valid) 
                 + ((IData)(vlSelf->fetch_unit__DOT__instr_pop_valid) 
                    >> 1U)));
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__validNumFifo_pop_valid = 1U;
    }
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_valid 
        = vlSelf->fetch_unit__DOT__validNumFifo_pop_valid;
    if ((2U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        if ((1U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
        }
    } else if ((1U & (~ vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))) {
        if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
                }
            } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                 >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
            }
        } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
            }
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
        } else if ((0U == (0x7ffU & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                     >> 2U)))) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
        }
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_illegal 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi16spImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addi16spImm_1) 
            << 9U) | ((0x180U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                          >> 3U)) << 7U)) 
                      | ((0x40U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                            >> 5U)) 
                                   << 6U)) | ((0x20U 
                                               & ((IData)(
                                                          (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                           >> 2U)) 
                                                  << 5U)) 
                                              | (0x10U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 6U)) 
                                                    << 4U))))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__luiImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_luiImm_1) 
            << 0x11U) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6) 
                         << 0xcU));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_bImm_1) 
            << 8U) | ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                         >> 5U)) << 6U)) 
                      | ((0x20U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                            >> 2U)) 
                                   << 5U)) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2) 
                                               << 3U) 
                                              | (6U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 3U)) 
                                                    << 1U))))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jImm_1 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm_1;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm_1) 
            << 0xbU) | ((0x400U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                            >> 8U)) 
                                   << 0xaU)) | ((0x300U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 9U)) 
                                                    << 8U)) 
                                                | ((0x80U 
                                                    & ((IData)(
                                                               (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                >> 6U)) 
                                                       << 7U)) 
                                                   | ((0x40U 
                                                       & ((IData)(
                                                                  (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                   >> 7U)) 
                                                          << 6U)) 
                                                      | ((0x20U 
                                                          & ((IData)(
                                                                     (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                      >> 2U)) 
                                                             << 5U)) 
                                                         | ((0x10U 
                                                             & ((IData)(
                                                                        (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                         >> 0xbU)) 
                                                                << 4U)) 
                                                            | (0xeU 
                                                               & ((IData)(
                                                                          (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                           >> 3U)) 
                                                                  << 1U)))))))));
    __Vtableidx6 = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_4;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_3 
        = Vtop__ConstPool__TABLE_he16900a8_0[__Vtableidx6];
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst_1;
    if (vlSelf->if2id_itf_1_valid) {
        vlSelf->fetch_unit__DOT__if2id_itf_1_instr_pc 
            = (vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc 
               + vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc_2);
    }
    vlSelf->if2id_itf_1_instr_pc = vlSelf->fetch_unit__DOT__if2id_itf_1_instr_pc;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_1 
        = ((0U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2))
            ? vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst
            : ((1U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2))
                ? (0x40000000U | vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst)
                : ((2U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2))
                    ? (0x7013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm) 
                                   << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                 << 0xfU) 
                                                | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                   << 7U))))
                    : ((((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                         << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                       << 0xfU) | (
                                                   ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_3) 
                                                    << 0xcU) 
                                                   | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                       << 7U) 
                                                      | ((0x1000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                                                          ? 0x3bU
                                                          : 0x33U))))) 
                       | ((0U == (3U & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                        >> 5U))) ? 0x40000000U
                           : 0U)))));
    vlSelf->fetch_unit__DOT__slice_valid_vec_2 = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    __Vtableidx3 = vlSelf->fetch_unit__DOT__tmp_4;
    vlSelf->fetch_unit__DOT__tmp_4_string = Vtop__ConstPool__TABLE_hd552ea8d_0
        [__Vtableidx3];
    vlSelf->fetch_unit__DOT__tmp_5 = 3U;
    vlSelf->fetch_unit__DOT__instr_slices_3_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instr_slices_3_valid = 0U;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tmp_4))) {
        vlSelf->fetch_unit__DOT__tmp_5 = 1U;
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_3) {
            vlSelf->fetch_unit__DOT__instr_slices_3_payload 
                = (QData)((IData)((vlSelf->ibus_rsp_payload_rdata 
                                   >> 0x20U)));
            vlSelf->fetch_unit__DOT__instr_slices_3_valid = 1U;
        }
    } else if (((1U == (IData)(vlSelf->fetch_unit__DOT__tmp_4)) 
                | (2U == (IData)(vlSelf->fetch_unit__DOT__tmp_4)))) {
        if ((3U != (3U & (IData)((vlSelf->ibus_rsp_payload_rdata 
                                  >> 0x30U))))) {
            vlSelf->fetch_unit__DOT__tmp_5 = 2U;
            if (vlSelf->fetch_unit__DOT__valid_instr_slice_3) {
                vlSelf->fetch_unit__DOT__instr_slices_3_payload 
                    = (0x100000000ULL | (QData)((IData)(
                                                        (0xffffU 
                                                         & (IData)(
                                                                   (vlSelf->ibus_rsp_payload_rdata 
                                                                    >> 0x30U))))));
                vlSelf->fetch_unit__DOT__instr_slices_3_valid = 1U;
            }
        } else {
            vlSelf->fetch_unit__DOT__tmp_5 = 0U;
        }
    }
    vlSelf->fetch_unit__DOT__addressFifo_io_push_valid 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo_io_push_ready) 
           & (IData)(vlSelf->fetch_unit__DOT__ibus_cmd_fire));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__dec_num 
        = (3U & (((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_0) 
                  == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num)) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_1)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__decompInstr_illegal 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_illegal;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst 
        = (0x5013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst) 
                       << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                     << 0xfU) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                 << 7U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst = 0U;
    if ((2U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        if ((1U & (~ vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))) {
            if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                    if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                  >> 0xdU)))) {
                        vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                            = (0x2023U | ((0xfe000000U 
                                           & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9) 
                                              << 0x14U)) 
                                          | ((0x1f00000U 
                                              & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                 << 0x12U)) 
                                             | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x2) 
                                                 << 0xfU) 
                                                | (0xf80U 
                                                   & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9) 
                                                      << 7U))))));
                    }
                } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                     >> 0xdU)))) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                        = ((0x400U == (0x7ffU & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                 >> 2U)))
                            ? 0x100073U : ((0U == (0x1fU 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                      >> 2U)))
                                            ? (0x67U 
                                               | ((0xf8000U 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                      << 8U)) 
                                                  | (((0x1000U 
                                                       & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                                                       ? (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x1)
                                                       : (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0)) 
                                                     << 7U)))
                                            : (0x33U 
                                               | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_5) 
                                                   << 0x19U) 
                                                  | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6) 
                                                      << 0x14U) 
                                                     | ((((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7)
                                                           ? (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_8)
                                                           : (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0)) 
                                                         << 0xfU) 
                                                        | (0xf80U 
                                                           & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)))))));
                }
            } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                              >> 0xdU)))) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                        = (0x2003U | ((0xc000000U & 
                                       (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                        << 0x18U)) 
                                      | ((0x2000000U 
                                          & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                             << 0xdU)) 
                                         | ((0x1c00000U 
                                             & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                << 0x12U)) 
                                            | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x2) 
                                                << 0xfU) 
                                               | (0xf80U 
                                                  & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))))));
                }
            } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                 >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                    = (0x1013U | ((0x2000000U & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                 << 0xdU)) 
                                  | ((0x1f00000U & 
                                      (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                       << 0x12U)) | 
                                     ((0xf8000U & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                   << 8U)) 
                                      | (0xf80U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)))));
            }
        }
    } else if ((1U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
            = ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                ? ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                    ? ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                        ? (0x1063U | ((0x80000000U 
                                       & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                          << 0x13U)) 
                                      | ((0x7e000000U 
                                          & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                             << 0x14U)) 
                                         | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0) 
                                             << 0x14U) 
                                            | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                << 0xfU) 
                                               | ((0xf00U 
                                                   & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                                      << 7U)) 
                                                  | (0x80U 
                                                     & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                                        >> 4U))))))))
                        : (0x63U | ((0x80000000U & 
                                     ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                      << 0x13U)) | 
                                    ((0x7e000000U & 
                                      ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                       << 0x14U)) | 
                                     (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0) 
                                       << 0x14U) | 
                                      (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                        << 0xfU) | 
                                       ((0xf00U & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                                   << 7U)) 
                                        | (0x80U & 
                                           ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                            >> 4U)))))))))
                    : ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                        ? (0x6fU | ((0x80000000U & 
                                     (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                      << 0xbU)) | (
                                                   (0x7fe00000U 
                                                    & (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                                       << 0x14U)) 
                                                   | ((0x100000U 
                                                       & (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                                          << 9U)) 
                                                      | ((0xff000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm) 
                                                         | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0) 
                                                            << 7U))))))
                        : vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_1))
                : ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                    ? ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                        ? ((2U == (0x1fU & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                            >> 7U)))
                            ? (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi16spImm) 
                                         << 0x14U) 
                                        | ((0xf8000U 
                                            & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                               << 8U)) 
                                           | (0xf80U 
                                              & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))))
                            : (0x37U | ((0xfffff000U 
                                         & vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__luiImm) 
                                        | (0xf80U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))))
                        : (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm) 
                                     << 0x14U) | (0xf80U 
                                                  & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))))
                    : ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                        ? (0x6fU | ((0x80000000U & 
                                     (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                      << 0xbU)) | (
                                                   (0x7fe00000U 
                                                    & (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                                       << 0x14U)) 
                                                   | ((0x100000U 
                                                       & (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                                          << 9U)) 
                                                      | ((0xff000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm) 
                                                         | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x1) 
                                                            << 7U))))))
                        : (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm) 
                                     << 0x14U) | ((0xf8000U 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                      << 8U)) 
                                                  | (0xf80U 
                                                     & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)))))));
    } else if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                          >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                    = (0x2023U | ((0x4000000U & ((IData)(
                                                         (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                          >> 5U)) 
                                                 << 0x1aU)) 
                                  | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7) 
                                      << 0x19U) | (
                                                   ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                                                    << 0x14U) 
                                                   | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                       << 0xfU) 
                                                      | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2) 
                                                          << 0xaU) 
                                                         | (0x200U 
                                                            & ((IData)(
                                                                       (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                        >> 6U)) 
                                                               << 9U))))))));
            }
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                = (0x3027U | ((0xc000000U & ((IData)(
                                                     (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                      >> 5U)) 
                                             << 0x1aU)) 
                              | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7) 
                                  << 0x19U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                                                << 0x14U) 
                                               | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                   << 0xfU) 
                                                  | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2) 
                                                     << 0xaU))))));
        }
    } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                      >> 0xdU)))) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                = (0x2003U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__lwImm) 
                               << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                             << 0xfU) 
                                            | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                                               << 7U))));
        }
    } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                         >> 0xdU)))) {
        vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
            = (0x10013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi5spnImm) 
                            << 0x14U) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                                         << 7U)));
    }
    __Vtableidx4 = vlSelf->fetch_unit__DOT__tmp_5;
    vlSelf->fetch_unit__DOT__tmp_5_string = Vtop__ConstPool__TABLE_hd552ea8d_0
        [__Vtableidx4];
    vlSelf->fetch_unit__DOT__slice_valid_vec_3 = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__datain_reverse 
        = (((IData)(vlSelf->fetch_unit__DOT__instr_slices_0_valid) 
            << 3U) | (((IData)(vlSelf->fetch_unit__DOT__instr_slices_1_valid) 
                       << 2U) | (((IData)(vlSelf->fetch_unit__DOT__instr_slices_2_valid) 
                                  << 1U) | (IData)(vlSelf->fetch_unit__DOT__instr_slices_3_valid))));
    vlSelf->fetch_unit__DOT__tail_one_matrix_0 = (((IData)(vlSelf->fetch_unit__DOT__instr_slices_3_valid) 
                                                   << 3U) 
                                                  | (((IData)(vlSelf->fetch_unit__DOT__instr_slices_2_valid) 
                                                      << 2U) 
                                                     | (((IData)(vlSelf->fetch_unit__DOT__instr_slices_1_valid) 
                                                         << 1U) 
                                                        | (IData)(vlSelf->fetch_unit__DOT__instr_slices_0_valid))));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_valid 
        = vlSelf->fetch_unit__DOT__addressFifo_io_push_valid;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushing 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo_io_push_ready) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo_io_push_valid));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_6 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__dec_num;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrGlobalInc 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__dec_num;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out 
        = ((IData)(vlSelf->if2id_itf_0_valid) ? (IData)(vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_stage1_ele0)
            : 0U);
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out;
    vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_1 
        = ((0U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2))
            ? vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst
            : ((1U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2))
                ? (0x40000000U | vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst)
                : ((2U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2))
                    ? (0x7013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm) 
                                   << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                 << 0xfU) 
                                                | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                   << 7U))))
                    : ((((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                         << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                       << 0xfU) | (
                                                   ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_3) 
                                                    << 0xcU) 
                                                   | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                       << 7U) 
                                                      | ((0x1000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                                                          ? 0x3bU
                                                          : 0x33U))))) 
                       | ((0U == (3U & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                        >> 5U))) ? 0x40000000U
                           : 0U)))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst;
    vlSelf->fetch_unit__DOT__real_instr_0 = (((~ (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal)) 
                                              & (IData)(vlSelf->fetch_unit__DOT__tmp_pc_step_all_1))
                                              ? vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst
                                              : vlSelf->fetch_unit__DOT__rvcDecompressor_2_i);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_in 
        = vlSelf->fetch_unit__DOT__tail_one_matrix_0;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s 
        = (0xfU & (~ (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_0)));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement = 0U;
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__pushing) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement = 1U;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobalInc 
        = (0xfU & ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal) 
                   + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrGlobalInc)));
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num 
            = vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out;
    }
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_req_num 
        = vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_vec_0 
        = (7U & ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data) 
                 - (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_out_data 
        = vlSelf->fetch_unit__DOT__validNumFifo_head;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_real = 0U;
    if (((IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_valid) 
         & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_ready))) {
        if (((IData)(vlSelf->fetch_unit__DOT__validNumFifo_head) 
             == (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_out_data = 0U;
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_real = 1U;
        }
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst = 0U;
    if ((2U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        if ((1U & (~ vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))) {
            if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                    if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                  >> 0xdU)))) {
                        vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                            = (0x2023U | ((0xfe000000U 
                                           & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9) 
                                              << 0x14U)) 
                                          | ((0x1f00000U 
                                              & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                 << 0x12U)) 
                                             | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x2) 
                                                 << 0xfU) 
                                                | (0xf80U 
                                                   & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9) 
                                                      << 7U))))));
                    }
                } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                     >> 0xdU)))) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                        = ((0x400U == (0x7ffU & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                 >> 2U)))
                            ? 0x100073U : ((0U == (0x1fU 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                      >> 2U)))
                                            ? (0x67U 
                                               | ((0xf8000U 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                      << 8U)) 
                                                  | (((0x1000U 
                                                       & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                                                       ? (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x1)
                                                       : (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0)) 
                                                     << 7U)))
                                            : (0x33U 
                                               | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_5) 
                                                   << 0x19U) 
                                                  | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6) 
                                                      << 0x14U) 
                                                     | ((((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7)
                                                           ? (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_8)
                                                           : (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0)) 
                                                         << 0xfU) 
                                                        | (0xf80U 
                                                           & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)))))));
                }
            } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                              >> 0xdU)))) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                        = (0x2003U | ((0xc000000U & 
                                       (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                        << 0x18U)) 
                                      | ((0x2000000U 
                                          & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                             << 0xdU)) 
                                         | ((0x1c00000U 
                                             & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                << 0x12U)) 
                                            | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x2) 
                                                << 0xfU) 
                                               | (0xf80U 
                                                  & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))))));
                }
            } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                 >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                    = (0x1013U | ((0x2000000U & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                 << 0xdU)) 
                                  | ((0x1f00000U & 
                                      (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                       << 0x12U)) | 
                                     ((0xf8000U & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                   << 8U)) 
                                      | (0xf80U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)))));
            }
        }
    } else if ((1U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
            = ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                ? ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                    ? ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                        ? (0x1063U | ((0x80000000U 
                                       & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                          << 0x13U)) 
                                      | ((0x7e000000U 
                                          & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                             << 0x14U)) 
                                         | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0) 
                                             << 0x14U) 
                                            | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                << 0xfU) 
                                               | ((0xf00U 
                                                   & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                                      << 7U)) 
                                                  | (0x80U 
                                                     & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                                        >> 4U))))))))
                        : (0x63U | ((0x80000000U & 
                                     ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                      << 0x13U)) | 
                                    ((0x7e000000U & 
                                      ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                       << 0x14U)) | 
                                     (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0) 
                                       << 0x14U) | 
                                      (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                        << 0xfU) | 
                                       ((0xf00U & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                                   << 7U)) 
                                        | (0x80U & 
                                           ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                            >> 4U)))))))))
                    : ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                        ? (0x6fU | ((0x80000000U & 
                                     (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                      << 0xbU)) | (
                                                   (0x7fe00000U 
                                                    & (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                                       << 0x14U)) 
                                                   | ((0x100000U 
                                                       & (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                                          << 9U)) 
                                                      | ((0xff000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm) 
                                                         | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0) 
                                                            << 7U))))))
                        : vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_1))
                : ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                    ? ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                        ? ((2U == (0x1fU & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                            >> 7U)))
                            ? (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi16spImm) 
                                         << 0x14U) 
                                        | ((0xf8000U 
                                            & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                               << 8U)) 
                                           | (0xf80U 
                                              & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))))
                            : (0x37U | ((0xfffff000U 
                                         & vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__luiImm) 
                                        | (0xf80U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))))
                        : (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm) 
                                     << 0x14U) | (0xf80U 
                                                  & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))))
                    : ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                        ? (0x6fU | ((0x80000000U & 
                                     (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                      << 0xbU)) | (
                                                   (0x7fe00000U 
                                                    & (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                                       << 0x14U)) 
                                                   | ((0x100000U 
                                                       & (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                                          << 9U)) 
                                                      | ((0xff000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm) 
                                                         | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x1) 
                                                            << 7U))))))
                        : (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm) 
                                     << 0x14U) | ((0xf8000U 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                      << 8U)) 
                                                  | (0xf80U 
                                                     & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)))))));
    } else if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                          >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                    = (0x2023U | ((0x4000000U & ((IData)(
                                                         (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                          >> 5U)) 
                                                 << 0x1aU)) 
                                  | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7) 
                                      << 0x19U) | (
                                                   ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                                                    << 0x14U) 
                                                   | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                       << 0xfU) 
                                                      | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2) 
                                                          << 0xaU) 
                                                         | (0x200U 
                                                            & ((IData)(
                                                                       (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                        >> 6U)) 
                                                               << 9U))))))));
            }
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                = (0x3027U | ((0xc000000U & ((IData)(
                                                     (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                      >> 5U)) 
                                             << 0x1aU)) 
                              | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7) 
                                  << 0x19U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                                                << 0x14U) 
                                               | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                   << 0xfU) 
                                                  | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2) 
                                                     << 0xaU))))));
        }
    } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                      >> 0xdU)))) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                = (0x2003U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__lwImm) 
                               << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                             << 0xfU) 
                                            | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                                               << 7U))));
        }
    } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                         >> 0xdU)))) {
        vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
            = (0x10013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi5spnImm) 
                            << 0x14U) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                                         << 7U)));
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__decompInstr_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_inst;
    vlSelf->fetch_unit__DOT__if2id_itf_0_instr_data = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__empty 
        = (1U & (~ (IData)((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s)))));
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_node_0 
        = vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_0 
        = ((1U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s))
            ? 0U : 1U);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_1 
        = ((4U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s))
            ? 2U : 3U);
    fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0 
        = (IData)((0U != (3U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s))));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_pushPtr_valueNext_1 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willOverflow 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willOverflowIfInc) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_pushPtr_valueNext 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement;
    vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_out_data 
        = vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_out_data;
    vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_real 
        = vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_real;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst;
    vlSelf->fetch_unit__DOT__real_instr_1 = (((~ (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal)) 
                                              & (IData)(vlSelf->fetch_unit__DOT__tmp_pc_step_all_2))
                                              ? vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst
                                              : vlSelf->fetch_unit__DOT__rvcDecompressor_3_i);
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__if2id_itf_0_instr_data 
            = vlSelf->fetch_unit__DOT__real_instr_0;
    }
    vlSelf->if2id_itf_0_instr_data = vlSelf->fetch_unit__DOT__if2id_itf_0_instr_data;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_node_1 
        = (((IData)((0U != (0xcU & (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s)))) 
            << 1U) | (IData)(fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0));
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index2_0 
        = ((IData)(fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0)
            ? (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_0)
            : (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_1));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_valueNext 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value) 
                 + (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_pushPtr_valueNext)));
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willClear) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_valueNext = 0U;
    }
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready = 0U;
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready 
            = ((IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_real) 
               & ((IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_valid) 
                  & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_ready)));
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__decompInstr_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_inst;
    vlSelf->fetch_unit__DOT__if2id_itf_1_instr_data = 0U;
    if (vlSelf->if2id_itf_1_valid) {
        vlSelf->fetch_unit__DOT__if2id_itf_1_instr_data 
            = vlSelf->fetch_unit__DOT__real_instr_1;
    }
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__tmp_cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index2_0;
    if ((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s))) {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_0 
            = vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index2_0;
        vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out 
            = vlSelf->fetch_unit__DOT__trailone_index_0__DOT__tmp_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_0 = 0U;
        vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out = 4U;
    }
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_ready 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popping 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo_io_pop_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready));
    vlSelf->if2id_itf_1_instr_data = vlSelf->fetch_unit__DOT__if2id_itf_1_instr_data;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_0))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_0_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_0_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    } else if ((1U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_0))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_1_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_0_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    } else if ((2U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_0))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_2_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_0_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    } else {
        vlSelf->fetch_unit__DOT__tmp_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_3_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_0_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    }
    vlSelf->fetch_unit__DOT__tail_one_matrix_1 = vlSelf->fetch_unit__DOT__tail_one_matrix_0;
    vlSelf->fetch_unit__DOT__tail_one_matrix_1 = ((~ 
                                                   ((IData)(1U) 
                                                    << (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_0))) 
                                                  & (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement = 0U;
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__popping) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement = 1U;
    }
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out;
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_0 = 
        (4U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload = 0ULL;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_in 
        = vlSelf->fetch_unit__DOT__tail_one_matrix_1;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__datain_reverse 
        = ((8U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1) 
                  << 3U)) | ((4U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1) 
                                    << 1U)) | ((2U 
                                                & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1) 
                                                   >> 1U)) 
                                               | (1U 
                                                  & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1) 
                                                     >> 3U)))));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s 
        = (0xfU & (~ (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1)));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_popPtr_valueNext_1 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willOverflow 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willOverflowIfInc) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_popPtr_valueNext 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement;
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid = 0U;
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__tmp_io_push_0_payload;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__empty 
        = (1U & (~ (IData)((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s)))));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_node_0 
        = vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_0 
        = ((1U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s))
            ? 0U : 1U);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_1 
        = ((4U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s))
            ? 2U : 3U);
    fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0 
        = (IData)((0U != (3U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s))));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_valueNext 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value) 
                 + (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_popPtr_valueNext)));
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willClear) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_valueNext = 0U;
    }
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid 
            = ((IData)(vlSelf->fetch_unit__DOT__tail_one_valid_vec_0) 
               & (IData)(vlSelf->fetch_unit__DOT__tmp_io_push_0_valid));
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_0 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_0 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready));
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_push_0_fire 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_node_1 
        = (((IData)((0U != (0xcU & (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s)))) 
            << 1U) | (IData)(fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index2_0 
        = ((IData)(fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0)
            ? (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_0)
            : (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_1));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__din_vld 
        = vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_push_0_fire;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__tmp_cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index2_0;
    if ((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s))) {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_1 
            = vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index2_0;
        vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out 
            = vlSelf->fetch_unit__DOT__trailone_index_1__DOT__tmp_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_1 = 0U;
        vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out = 4U;
    }
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_1))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_0_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_1_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    } else if ((1U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_1))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_1_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_1_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    } else if ((2U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_1))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_2_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_1_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    } else {
        vlSelf->fetch_unit__DOT__tmp_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_3_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_1_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    }
    vlSelf->fetch_unit__DOT__tail_one_matrix_2 = vlSelf->fetch_unit__DOT__tail_one_matrix_1;
    vlSelf->fetch_unit__DOT__tail_one_matrix_2 = ((~ 
                                                   ((IData)(1U) 
                                                    << (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_1))) 
                                                  & (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out;
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_1 = 
        (4U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload = 0ULL;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_in 
        = vlSelf->fetch_unit__DOT__tail_one_matrix_2;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__datain_reverse 
        = ((8U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2) 
                  << 3U)) | ((4U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2) 
                                    << 1U)) | ((2U 
                                                & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2) 
                                                   >> 1U)) 
                                               | (1U 
                                                  & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2) 
                                                     >> 3U)))));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s 
        = (0xfU & (~ (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2)));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid = 0U;
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__tmp_io_push_1_payload;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__empty 
        = (1U & (~ (IData)((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s)))));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_node_0 
        = vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_0 
        = ((1U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s))
            ? 0U : 1U);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_1 
        = ((4U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s))
            ? 2U : 3U);
    fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0 
        = (IData)((0U != (3U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s))));
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid 
            = ((IData)(vlSelf->fetch_unit__DOT__tail_one_valid_vec_1) 
               & (IData)(vlSelf->fetch_unit__DOT__tmp_io_push_1_valid));
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_1 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_1 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_1_ready));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele0 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid)));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_node_1 
        = (((IData)((0U != (0xcU & (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s)))) 
            << 1U) | (IData)(fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index2_0 
        = ((IData)(fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0)
            ? (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_0)
            : (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_1));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_3 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_1) 
           == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__tmp_cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index2_0;
    if ((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s))) {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_2 
            = vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index2_0;
        vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out 
            = vlSelf->fetch_unit__DOT__trailone_index_2__DOT__tmp_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_2 = 0U;
        vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out = 4U;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_2 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_3;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_2))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_0_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_2_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    } else if ((1U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_2))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_1_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_2_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    } else if ((2U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_2))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_2_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_2_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    } else {
        vlSelf->fetch_unit__DOT__tmp_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_3_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_2_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    }
    vlSelf->fetch_unit__DOT__tail_one_matrix_3 = vlSelf->fetch_unit__DOT__tail_one_matrix_2;
    vlSelf->fetch_unit__DOT__tail_one_matrix_3 = ((~ 
                                                   ((IData)(1U) 
                                                    << (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_2))) 
                                                  & (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_1 
        = (3U & (((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_0) 
                  == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num)) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_2)));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out;
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_2 = 
        (4U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload = 0ULL;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_in 
        = vlSelf->fetch_unit__DOT__tail_one_matrix_3;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__datain_reverse 
        = ((8U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3) 
                  << 3U)) | ((4U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3) 
                                    << 1U)) | ((2U 
                                                & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3) 
                                                   >> 1U)) 
                                               | (1U 
                                                  & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3) 
                                                     >> 3U)))));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s 
        = (0xfU & (~ (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3)));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid = 0U;
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__tmp_io_push_2_payload;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__empty 
        = (1U & (~ (IData)((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s)))));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_node_0 
        = vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_0 
        = ((1U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s))
            ? 0U : 1U);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_1 
        = ((4U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s))
            ? 2U : 3U);
    fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0 
        = (IData)((0U != (3U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s))));
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid 
            = ((IData)(vlSelf->fetch_unit__DOT__tail_one_valid_vec_2) 
               & (IData)(vlSelf->fetch_unit__DOT__tmp_io_push_2_valid));
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_2 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_2 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_2_ready));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_node_1 
        = (((IData)((0U != (0xcU & (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s)))) 
            << 1U) | (IData)(fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index2_0 
        = ((IData)(fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0)
            ? (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_0)
            : (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_1));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__tmp_cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index2_0;
    if ((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s))) {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_3 
            = vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index2_0;
        vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out 
            = vlSelf->fetch_unit__DOT__trailone_index_3__DOT__tmp_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_3 = 0U;
        vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out = 4U;
    }
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_3))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_0_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_3_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    } else if ((1U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_3))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_1_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_3_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    } else if ((2U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_3))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_2_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_3_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    } else {
        vlSelf->fetch_unit__DOT__tmp_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_3_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_3_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    }
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out;
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_3 = 
        (4U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid = 0U;
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__tmp_io_push_3_payload;
        vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid 
            = ((IData)(vlSelf->fetch_unit__DOT__tail_one_valid_vec_3) 
               & (IData)(vlSelf->fetch_unit__DOT__tmp_io_push_3_valid));
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_3 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec = 
        (((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid) 
          << 3U) | (((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid) 
                     << 2U) | (((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid) 
                                << 1U) | (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid))));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_3 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_3_ready));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele1 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid)));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__data_in 
        = vlSelf->fetch_unit__DOT__instr_push_valid_vec;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_7 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_3) 
           == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage2_ele0 
        = (7U & ((IData)(vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele0) 
                 + (IData)(vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele1)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_6 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_7;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_5 
        = (3U & (((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_2) 
                  == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num)) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_6)));
    if (vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_push_0_fire) {
        vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out 
            = vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage2_ele0;
        vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__cnt_out 
            = vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out;
        vlSelf->fetch_unit__DOT__validNumFifo_push_payload = 0U;
        vlSelf->fetch_unit__DOT__validNumFifo_push_valid = 0U;
        if ((0U != (IData)(vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out))) {
            vlSelf->fetch_unit__DOT__validNumFifo_push_payload 
                = vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out;
            vlSelf->fetch_unit__DOT__validNumFifo_push_valid = 1U;
        }
    } else {
        vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out = 0U;
        vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__cnt_out 
            = vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out;
        vlSelf->fetch_unit__DOT__validNumFifo_push_payload = 0U;
        vlSelf->fetch_unit__DOT__validNumFifo_push_valid = 0U;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_4 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_5;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_payload 
        = vlSelf->fetch_unit__DOT__validNumFifo_push_payload;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_valid 
        = vlSelf->fetch_unit__DOT__validNumFifo_push_valid;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushing 
        = ((IData)(vlSelf->fetch_unit__DOT__validNumFifo_push_ready) 
           & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_push_valid));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__inc_num 
        = (7U & ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_1) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_4)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrGlobalInc 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__inc_num;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobalInc 
        = (0xfU & ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal) 
                   + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrGlobalInc)));
}

void Vtop___024root___eval_ico(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_ico\n"); );
    // Body
    if (vlSelf->__VicoTriggered.at(0U)) {
        Vtop___024root___ico_sequent__TOP__0(vlSelf);
    }
}

void Vtop___024root___eval_act(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_act\n"); );
}

VL_INLINE_OPT void Vtop___024root___nba_sequent__TOP__0(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___nba_sequent__TOP__0\n"); );
    // Body
    if (vlSelf->resetn) {
        vlSelf->fetch_unit__DOT__pc = ((IData)(vlSelf->pcJumpTarget_valid)
                                        ? vlSelf->pcJumpTarget_payload
                                        : ((IData)(vlSelf->pcPredicted_valid)
                                            ? vlSelf->pcPredicted_payload
                                            : ((IData)(vlSelf->fetch_unit__DOT__fetch_pending)
                                                ? vlSelf->fetch_unit__DOT__pc
                                                : vlSelf->fetch_unit__DOT__default_next_pc)));
        vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal 
            = ((3U <= (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal))
                ? 0U : (3U & ((IData)(1U) + (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal))));
        if (((IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_valid) 
             & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_ready))) {
            if (((IData)(vlSelf->fetch_unit__DOT__validNumFifo_head) 
                 == (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num))) {
                vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal 
                    = ((3U <= (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal))
                        ? 0U : (3U & ((IData)(1U) + (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal))));
            }
        }
        if ((0U == (IData)(vlSelf->fetch_unit__DOT__tmp_5))) {
            vlSelf->fetch_unit__DOT__tmp_instr_slices_0_payload 
                = (0xffffU & (IData)((vlSelf->ibus_rsp_payload_rdata 
                                      >> 0x30U)));
            vlSelf->fetch_unit__DOT__tmp_1 = 1U;
        }
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value 
            = vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_valueNext;
        if (((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushing) 
             != (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popping))) {
            vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy 
                = vlSelf->fetch_unit__DOT__addressFifo__DOT__pushing;
        }
        if (vlSelf->fetch_unit__DOT__addressFifo_io_flush) {
            vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy = 0U;
        }
        if (((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushing) 
             != (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_real))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy 
                = vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushing;
        }
        if (vlSelf->fetch_unit__DOT__validNumFifo_flush) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal = 0U;
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal = 0U;
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy = 0U;
        }
        if (vlSelf->if2id_itf_0_valid) {
            vlSelf->fetch_unit__DOT__pc_pop_offset 
                = vlSelf->fetch_unit__DOT__pc_step_all_2;
        }
        vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy 
            = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_6) 
               < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__inc_num));
        if ((0U != (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__inc_num))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal 
                = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobalInc))
                    ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobalInc));
        }
        if ((0U != (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__dec_num))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal 
                = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobalInc))
                    ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobalInc));
        }
        if (vlSelf->fetch_unit__DOT__instrFifo_io_flush) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy = 0U;
            vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal = 0U;
            vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal = 0U;
        }
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value 
            = vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_valueNext;
    } else {
        vlSelf->fetch_unit__DOT__pc = 0U;
        vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal = 0U;
        vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal = 0U;
        vlSelf->fetch_unit__DOT__tmp_instr_slices_0_payload = 0U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value = 0U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy = 0U;
        vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy = 0U;
        vlSelf->fetch_unit__DOT__pc_pop_offset = 0U;
        vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy = 0U;
        vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal = 0U;
        vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal = 0U;
        vlSelf->fetch_unit__DOT__tmp_1 = 0U;
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value = 0U;
    }
    vlSelf->fetch_unit__DOT__default_next_pc = ((IData)(8U) 
                                                + vlSelf->fetch_unit__DOT__pc);
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrDif 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal) 
                 - (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch 
        = ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal) 
           == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willOverflowIfInc 
        = (3U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value));
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc_1 
        = vlSelf->fetch_unit__DOT__pc_pop_offset;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_0 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_1 
        = (0xfU & ((IData)(1U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_2 
        = (0xfU & ((IData)(2U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_3 
        = (0xfU & ((IData)(3U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_4 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal) 
           == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrDif 
        = (0xfU & ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal) 
                   - (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_5 
        = (0xfU & ((IData)(1U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willOverflowIfInc 
        = (3U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrDif 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value) 
                 - (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value)));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value) 
           == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value));
    vlSelf->fetch_unit__DOT__validNumFifo_occupancy 
        = ((((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy) 
             & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch)) 
            << 2U) | (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrDif));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__full 
        = ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch) 
           & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__empty 
        = ((~ (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__risingOccupancy)) 
           & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__ptrMatch));
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc_1 
        = vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc_1;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_1 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_1))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_1));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_2 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_2))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_2));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_3 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_3))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_3));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__empty 
        = ((~ (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy)) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__full = 
        ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch) 
         & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_occupancy 
        = (0xfU & ((IData)(0xcU) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrDif)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_1 
        = ((0xcU <= (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_5))
            ? 0U : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_5));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_0 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1;
    vlSelf->fetch_unit__DOT__addressFifo_io_occupancy 
        = ((((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy) 
             & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch)) 
            << 2U) | (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrDif));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__empty 
        = ((~ (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy)) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__full 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__ptrMatch) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__risingOccupancy));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__occupancy 
        = vlSelf->fetch_unit__DOT__validNumFifo_occupancy;
    vlSelf->fetch_unit__DOT__validNumFifo_push_ready 
        = (1U & (~ (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__full)));
    vlSelf->fetch_unit__DOT__instrFifo_io_occupancy 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrMatch)
            ? ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__risingOccupancy)
                ? 0xcU : 0U) : (((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal) 
                                 < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal))
                                 ? (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__ptrDif)
                                 : (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_occupancy)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1 
        = (0xfU & ((IData)(1U) + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrs_1)));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_occupancy 
        = vlSelf->fetch_unit__DOT__addressFifo_io_occupancy;
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_valid 
        = (1U & (~ (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__empty)));
    vlSelf->fetch_unit__DOT__addressFifo_io_push_ready 
        = (1U & (~ (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__full)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_ready 
        = vlSelf->fetch_unit__DOT__validNumFifo_push_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_occupancy 
        = vlSelf->fetch_unit__DOT__instrFifo_io_occupancy;
    vlSelf->fetch_unit__DOT__instrFifo_io_ov_waterline 
        = (4U <= (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_occupancy));
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid 
        = (1U < (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_occupancy));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready 
        = (0xfU & ((IData)(0xcU) - (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_occupancy)));
    vlSelf->fetchStage_isReady = (0U < (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_occupancy));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_valid 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_valid;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_ready 
        = vlSelf->fetch_unit__DOT__addressFifo_io_push_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_ov_waterline 
        = vlSelf->fetch_unit__DOT__instrFifo_io_ov_waterline;
    vlSelf->fetch_unit__DOT__fetch_pending = (1U & 
                                              ((~ (IData)(vlSelf->core_en)) 
                                               | ((~ 
                                                   ((IData)(vlSelf->fetch_unit__DOT__addressFifo_io_push_ready) 
                                                    & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_push_ready))) 
                                                  | (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_ov_waterline))));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_1_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_1 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid) 
           & (IData)(vlSelf->fetchStage_isDone));
    vlSelf->if2id_itf_1_valid = ((IData)(vlSelf->fetchStage_isDone) 
                                 & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_valid));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_1_ready 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_2_ready 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_3_ready 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready;
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_ready 
        = (2U < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_ready 
        = (1U < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_ready 
        = (3U < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready 
        = (0U < (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_io_push_0_ready));
    vlSelf->fetch_unit__DOT__fetchStage_isReady = vlSelf->fetchStage_isReady;
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_valid 
        = vlSelf->fetchStage_isReady;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_0_valid 
        = vlSelf->fetchStage_isReady;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_0 
        = ((IData)(vlSelf->fetchStage_isReady) & (IData)(vlSelf->fetchStage_isDone));
    vlSelf->if2id_itf_0_valid = ((IData)(vlSelf->fetchStage_isDone) 
                                 & (IData)(vlSelf->fetchStage_isReady));
    vlSelf->ibus_cmd_valid = (1U & (~ (IData)(vlSelf->fetch_unit__DOT__fetch_pending)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_2 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_1) 
           == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num));
    vlSelf->fetch_unit__DOT__if2id_itf_1_valid = vlSelf->if2id_itf_1_valid;
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_pop_1_fire 
        = vlSelf->if2id_itf_1_valid;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_init_1 
        = vlSelf->if2id_itf_1_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_ready 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_ready 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_ready 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_ready;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_ready 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__din_vld 
        = vlSelf->if2id_itf_0_valid;
    vlSelf->fetch_unit__DOT__if2id_itf_0_valid = vlSelf->if2id_itf_0_valid;
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_pop_0_fire 
        = vlSelf->if2id_itf_0_valid;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_init_0 
        = vlSelf->if2id_itf_0_valid;
    vlSelf->fetch_unit__DOT__instr_pop_valid = (((IData)(vlSelf->if2id_itf_1_valid) 
                                                 << 1U) 
                                                | (IData)(vlSelf->if2id_itf_0_valid));
    vlSelf->fetch_unit__DOT__ibus_cmd_valid = vlSelf->ibus_cmd_valid;
    vlSelf->fetch_unit__DOT__ibus_cmd_fire = ((IData)(vlSelf->ibus_cmd_valid) 
                                              & (IData)(vlSelf->ibus_cmd_ready));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_1 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_2;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__data_in 
        = vlSelf->fetch_unit__DOT__instr_pop_valid;
    vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_stage1_ele0 
        = (3U & ((IData)(vlSelf->if2id_itf_0_valid) 
                 + ((IData)(vlSelf->fetch_unit__DOT__instr_pop_valid) 
                    >> 1U)));
    vlSelf->fetch_unit__DOT__addressFifo_io_push_valid 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo_io_push_ready) 
           & (IData)(vlSelf->fetch_unit__DOT__ibus_cmd_fire));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__dec_num 
        = (3U & (((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popping_vec_0) 
                  == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num)) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_dec_num_1)));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_valid 
        = vlSelf->fetch_unit__DOT__addressFifo_io_push_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_when_6 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__dec_num;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrGlobalInc 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__dec_num;
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out 
            = vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__popsum_stage1_ele0;
        vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__cnt_out 
            = vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out;
        vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num = 0U;
        vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num 
            = vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out = 0U;
        vlSelf->fetch_unit__DOT__instr_pop_valid_popc__DOT__cnt_out 
            = vlSelf->fetch_unit__DOT__instr_pop_valid_popc_cnt_out;
        vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num = 0U;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobalInc 
        = (0xfU & ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__popPtrGlobal) 
                   + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_popPtrGlobalInc)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_req_num 
        = vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num;
}

VL_INLINE_OPT void Vtop___024root___nba_sequent__TOP__1(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___nba_sequent__TOP__1\n"); );
    // Body
    if (((IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_valid) 
         & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_ready))) {
        if ((1U & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_0 
                = vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_vec_0;
        }
        if ((8U & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_3 
                = vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_vec_0;
        }
        if ((2U & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_1 
                = vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_vec_0;
        }
        if ((4U & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_2 
                = vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_vec_0;
        }
    }
    if (vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushing) {
        if ((1U & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_0 
                = vlSelf->fetch_unit__DOT__validNumFifo_push_payload;
        }
        if ((8U & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_3 
                = vlSelf->fetch_unit__DOT__validNumFifo_push_payload;
        }
        if ((2U & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_1 
                = vlSelf->fetch_unit__DOT__validNumFifo_push_payload;
        }
        if ((4U & (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_2 
                = vlSelf->fetch_unit__DOT__validNumFifo_push_payload;
        }
    }
    if (vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_0) {
        if ((0x40U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((0x80U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((8U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((0x100U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((0x10U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((0x200U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((0x20U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((0x400U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
        if ((0x800U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
        }
    }
    if (vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_1) {
        if ((0x40U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((0x80U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((8U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((0x100U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((0x10U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((0x200U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((0x20U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((0x400U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
        if ((0x800U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
        }
    }
    if (vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_2) {
        if ((0x40U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((0x80U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((8U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((0x100U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((0x10U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((0x200U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((0x20U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((0x400U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
        if ((0x800U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
        }
    }
    if (vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_3) {
        if ((0x40U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((0x80U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((8U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((0x100U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((0x10U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((0x200U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((0x20U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((0x400U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
        if ((0x800U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4))) {
            vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11 
                = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
        }
    }
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__pushing) {
        if ((2U & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_1 
                = vlSelf->fetch_unit__DOT__ibus_cmd_payload_address;
        }
        if ((8U & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_3 
                = vlSelf->fetch_unit__DOT__ibus_cmd_payload_address;
        }
        if ((1U & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_0 
                = vlSelf->fetch_unit__DOT__ibus_cmd_payload_address;
        }
        if ((4U & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_1))) {
            vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_2 
                = vlSelf->fetch_unit__DOT__ibus_cmd_payload_address;
        }
    }
}

VL_INLINE_OPT void Vtop___024root___nba_sequent__TOP__2(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___nba_sequent__TOP__2\n"); );
    // Body
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_1 
        = (0xfU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal)));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_2 
        = (0xfU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushPtrGlobal)));
    vlSelf->fetch_unit__DOT__validNumFifo_pop_valid = 0U;
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__validNumFifo_pop_valid = 1U;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_1 
        = (0xffffU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_0)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_2 
        = (0xffffU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_1)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_3 
        = (0xffffU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_2)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_4 
        = (0xffffU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrs_3)));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_1 
        = (0xfU & ((IData)(1U) << (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value)));
    vlSelf->fetch_unit__DOT__ibus_cmd_payload_address 
        = (((IData)(vlSelf->pcJumpTarget_valid) & (IData)(vlSelf->ibus_cmd_ready))
            ? vlSelf->pcJumpTarget_payload : (((IData)(vlSelf->pcPredicted_valid) 
                                               & (IData)(vlSelf->ibus_cmd_ready))
                                               ? vlSelf->pcPredicted_payload
                                               : vlSelf->fetch_unit__DOT__pc));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushing 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo_io_push_ready) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo_io_push_valid));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_valid 
        = vlSelf->fetch_unit__DOT__validNumFifo_pop_valid;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_push_payload 
        = vlSelf->fetch_unit__DOT__ibus_cmd_payload_address;
    vlSelf->ibus_cmd_payload_address = vlSelf->fetch_unit__DOT__ibus_cmd_payload_address;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement = 0U;
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__pushing) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement = 1U;
    }
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_pushPtr_valueNext_1 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willOverflow 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willOverflowIfInc) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_pushPtr_valueNext 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willIncrement;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_valueNext 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_value) 
                 + (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_pushPtr_valueNext)));
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_willClear) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__pushPtr_valueNext = 0U;
    }
}

VL_INLINE_OPT void Vtop___024root___nba_comb__TOP__0(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___nba_comb__TOP__0\n"); );
    // Init
    CData/*0:0*/ fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0;
    fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0 = 0;
    CData/*0:0*/ fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0;
    fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0 = 0;
    CData/*0:0*/ fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0;
    fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0 = 0;
    CData/*0:0*/ fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0;
    fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0 = 0;
    CData/*1:0*/ __Vtableidx1;
    __Vtableidx1 = 0;
    CData/*1:0*/ __Vtableidx2;
    __Vtableidx2 = 0;
    CData/*1:0*/ __Vtableidx3;
    __Vtableidx3 = 0;
    CData/*1:0*/ __Vtableidx4;
    __Vtableidx4 = 0;
    CData/*2:0*/ __Vtableidx5;
    __Vtableidx5 = 0;
    CData/*2:0*/ __Vtableidx6;
    __Vtableidx6 = 0;
    // Body
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data 
        = ((0U == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal))
            ? (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_0)
            : ((1U == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal))
                ? (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_1)
                : ((2U == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__popPtrGlobal))
                    ? (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_2)
                    : (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__vec_3))));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
        = ((8U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
            ? ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11
                : ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8)))
            : ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                ? ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4))
                : ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0))));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
        = ((8U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
            ? ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11
                : ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_11
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_10)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_9
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_8)))
            : ((4U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                ? ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_7
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_6)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_5
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_4))
                : ((2U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                    ? ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_3
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_2)
                    : ((1U & (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1_1))
                        ? vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_1
                        : vlSelf->fetch_unit__DOT__instrFifo__DOT__vec_0))));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed 
        = ((0U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value))
            ? vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_0
            : ((1U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value))
                ? vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_1
                : ((2U == (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value))
                    ? vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_2
                    : vlSelf->fetch_unit__DOT__addressFifo__DOT__vec_3)));
    vlSelf->fetch_unit__DOT__validNumFifo_head = vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data;
    vlSelf->fetch_unit__DOT__validNumFifo_pop_ready 
        = (1U & (~ ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__empty) 
                    | (0U == (IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data)))));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_vec_0 
        = (7U & ((IData)(vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_tmp_pop_pop_out_data) 
                 - (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ldImm 
        = ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                              >> 5U)) << 6U)) | (0x38U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 0xaU)) 
                                                    << 3U)));
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_payload 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0;
    vlSelf->fetch_unit__DOT__tmp_pc_step_all_1 = (1U 
                                                  & (IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 0x20U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_8 
        = (0x1fU & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                            >> 7U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi5spnImm 
        = ((0x3c0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                               >> 7U)) << 6U)) | ((0x30U 
                                                   & ((IData)(
                                                              (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                               >> 0xbU)) 
                                                      << 4U)) 
                                                  | ((8U 
                                                      & ((IData)(
                                                                 (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                  >> 5U)) 
                                                         << 3U)) 
                                                     | (4U 
                                                        & ((IData)(
                                                                   (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                    >> 6U)) 
                                                           << 2U)))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__lwImm 
        = ((0x40U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                              >> 5U)) << 6U)) | ((0x38U 
                                                  & ((IData)(
                                                             (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                              >> 0xaU)) 
                                                     << 3U)) 
                                                 | (4U 
                                                    & ((IData)(
                                                               (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                >> 6U)) 
                                                       << 2U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9 
        = ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                              >> 7U)) << 6U)) | (0x3cU 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 9U)) 
                                                    << 2U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl 
        = (8U | (7U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                               >> 2U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_i = (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2 
        = (3U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                         >> 0xaU)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch 
        = (8U | (7U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                               >> 7U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6 
        = (0x1fU & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                            >> 2U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7 
        = (1U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                         >> 0xcU)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ldImm 
        = ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                              >> 5U)) << 6U)) | (0x38U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 0xaU)) 
                                                    << 3U)));
    vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_payload 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1;
    vlSelf->fetch_unit__DOT__tmp_pc_step_all_2 = (1U 
                                                  & (IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 0x20U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_8 
        = (0x1fU & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                            >> 7U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi5spnImm 
        = ((0x3c0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                               >> 7U)) << 6U)) | ((0x30U 
                                                   & ((IData)(
                                                              (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                               >> 0xbU)) 
                                                      << 4U)) 
                                                  | ((8U 
                                                      & ((IData)(
                                                                 (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                  >> 5U)) 
                                                         << 3U)) 
                                                     | (4U 
                                                        & ((IData)(
                                                                   (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                    >> 6U)) 
                                                           << 2U)))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__lwImm 
        = ((0x40U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                              >> 5U)) << 6U)) | ((0x38U 
                                                  & ((IData)(
                                                             (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                              >> 0xaU)) 
                                                     << 3U)) 
                                                 | (4U 
                                                    & ((IData)(
                                                               (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                >> 6U)) 
                                                       << 2U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9 
        = ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                              >> 7U)) << 6U)) | (0x3cU 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 9U)) 
                                                    << 2U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl 
        = (8U | (7U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                               >> 2U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_i = (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2 
        = (3U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                         >> 0xaU)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch 
        = (8U | (7U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                               >> 7U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6 
        = (0x1fU & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                            >> 2U)));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7 
        = (1U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                         >> 0xcU)));
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed;
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc 
        = (vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed 
           + vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc_1);
    vlSelf->fetch_unit__DOT__addressOffset = (3U & 
                                              (vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_readed 
                                               >> 1U));
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__head 
        = vlSelf->fetch_unit__DOT__validNumFifo_head;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__tmp_pop_pop_out_data 
        = vlSelf->fetch_unit__DOT__validNumFifo_head;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_ready 
        = vlSelf->fetch_unit__DOT__validNumFifo_pop_ready;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_out_data 
        = vlSelf->fetch_unit__DOT__validNumFifo_head;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_real = 0U;
    if (((IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_valid) 
         & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_ready))) {
        if (((IData)(vlSelf->fetch_unit__DOT__validNumFifo_head) 
             == (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_req_num))) {
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_out_data = 0U;
            vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_real = 1U;
        }
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_0_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_payload;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__readedVec_0 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_0_payload;
    vlSelf->fetch_unit__DOT__pc_step_all_1 = ((IData)(vlSelf->fetch_unit__DOT__tmp_pc_step_all_1)
                                               ? 2U
                                               : 4U);
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_10 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__i 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2_i;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__shiftImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_luiImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addi16spImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_bImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addi16spImm_1 
        = (7U & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_luiImm_1 
        = (0x7fffU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_bImm_1 
        = (0x1fU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm_1 
        = (0x3ffU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_4 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7) 
            << 2U) | (3U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                    >> 5U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst_1 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7) 
            << 5U) | (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm 
        = ((0xfe0U & ((- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7))) 
                      << 5U)) | (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_pop_1_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_payload;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__readedVec_1 
        = vlSelf->fetch_unit__DOT__instrFifo_io_pop_1_payload;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_10 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__i 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3_i;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__shiftImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_luiImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addi16spImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_bImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addi16spImm_1 
        = (7U & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_luiImm_1 
        = (0x7fffU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_bImm_1 
        = (0x1fU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm_1 
        = (0x3ffU & (- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_4 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7) 
            << 2U) | (3U & (IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                    >> 5U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst_1 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7) 
            << 5U) | (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm 
        = ((0xfe0U & ((- (IData)((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7))) 
                      << 5U)) | (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6));
    vlSelf->fetch_unit__DOT__addressFifo_io_head = vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_payload 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_head 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__readed 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_payload;
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc 
        = vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc;
    vlSelf->fetch_unit__DOT__if2id_itf_0_instr_pc = 0U;
    vlSelf->fetch_unit__DOT__valid_instr_slice_1 = 
        (1U >= (IData)(vlSelf->fetch_unit__DOT__addressOffset));
    vlSelf->fetch_unit__DOT__valid_instr_slice_2 = 
        (2U >= (IData)(vlSelf->fetch_unit__DOT__addressOffset));
    vlSelf->fetch_unit__DOT__valid_instr_slice_0 = 
        (0U >= (IData)(vlSelf->fetch_unit__DOT__addressOffset));
    vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_out_data 
        = vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_out_data;
    vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_real 
        = vlSelf->fetch_unit__DOT__validNumFifo__DOT__pop_pop_real;
    vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc_2 
        = vlSelf->fetch_unit__DOT__pc_step_all_1;
    vlSelf->fetch_unit__DOT__pc_step_all_2 = (7U & 
                                              ((IData)(vlSelf->fetch_unit__DOT__pc_step_all_1) 
                                               + ((IData)(vlSelf->fetch_unit__DOT__tmp_pc_step_all_2)
                                                   ? 2U
                                                   : 4U)));
    if ((2U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        if ((1U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
        }
    } else if ((1U & (~ vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))) {
        if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
                }
            } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                 >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
            }
        } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
            }
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
        } else if ((0U == (0x7ffU & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                     >> 2U)))) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal = 1U;
        }
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_illegal 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi16spImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_addi16spImm_1) 
            << 9U) | ((0x180U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                          >> 3U)) << 7U)) 
                      | ((0x40U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                            >> 5U)) 
                                   << 6U)) | ((0x20U 
                                               & ((IData)(
                                                          (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                           >> 2U)) 
                                                  << 5U)) 
                                              | (0x10U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 6U)) 
                                                    << 4U))))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__luiImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_luiImm_1) 
            << 0x11U) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6) 
                         << 0xcU));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_bImm_1) 
            << 8U) | ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                         >> 5U)) << 6U)) 
                      | ((0x20U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                            >> 2U)) 
                                   << 5U)) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2) 
                                               << 3U) 
                                              | (6U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 3U)) 
                                                    << 1U))))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jImm_1 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm_1;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_jalImm_1) 
            << 0xbU) | ((0x400U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                            >> 8U)) 
                                   << 0xaU)) | ((0x300U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                             >> 9U)) 
                                                    << 8U)) 
                                                | ((0x80U 
                                                    & ((IData)(
                                                               (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                >> 6U)) 
                                                       << 7U)) 
                                                   | ((0x40U 
                                                       & ((IData)(
                                                                  (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                   >> 7U)) 
                                                          << 6U)) 
                                                      | ((0x20U 
                                                          & ((IData)(
                                                                     (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                      >> 2U)) 
                                                             << 5U)) 
                                                         | ((0x10U 
                                                             & ((IData)(
                                                                        (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                         >> 0xbU)) 
                                                                << 4U)) 
                                                            | (0xeU 
                                                               & ((IData)(
                                                                          (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                           >> 3U)) 
                                                                  << 1U)))))))));
    __Vtableidx5 = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_4;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_3 
        = Vtop__ConstPool__TABLE_he16900a8_0[__Vtableidx5];
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst_1;
    if ((2U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        if ((1U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
        }
    } else if ((1U & (~ vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))) {
        if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
                }
            } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                 >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
            }
        } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
            }
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
        } else if ((0U == (0x7ffU & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                     >> 2U)))) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal = 1U;
        }
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_illegal 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi16spImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_addi16spImm_1) 
            << 9U) | ((0x180U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                          >> 3U)) << 7U)) 
                      | ((0x40U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                            >> 5U)) 
                                   << 6U)) | ((0x20U 
                                               & ((IData)(
                                                          (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                           >> 2U)) 
                                                  << 5U)) 
                                              | (0x10U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 6U)) 
                                                    << 4U))))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__luiImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_luiImm_1) 
            << 0x11U) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6) 
                         << 0xcU));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_bImm_1) 
            << 8U) | ((0xc0U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                         >> 5U)) << 6U)) 
                      | ((0x20U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                            >> 2U)) 
                                   << 5U)) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2) 
                                               << 3U) 
                                              | (6U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 3U)) 
                                                    << 1U))))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jImm_1 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm_1;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
        = (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_jalImm_1) 
            << 0xbU) | ((0x400U & ((IData)((vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                            >> 8U)) 
                                   << 0xaU)) | ((0x300U 
                                                 & ((IData)(
                                                            (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                             >> 9U)) 
                                                    << 8U)) 
                                                | ((0x80U 
                                                    & ((IData)(
                                                               (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                >> 6U)) 
                                                       << 7U)) 
                                                   | ((0x40U 
                                                       & ((IData)(
                                                                  (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                   >> 7U)) 
                                                          << 6U)) 
                                                      | ((0x20U 
                                                          & ((IData)(
                                                                     (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                      >> 2U)) 
                                                             << 5U)) 
                                                         | ((0x10U 
                                                             & ((IData)(
                                                                        (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                         >> 0xbU)) 
                                                                << 4U)) 
                                                            | (0xeU 
                                                               & ((IData)(
                                                                          (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                           >> 3U)) 
                                                                  << 1U)))))))));
    __Vtableidx6 = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_4;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_3 
        = Vtop__ConstPool__TABLE_he16900a8_0[__Vtableidx6];
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst_1;
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__if2id_itf_0_instr_pc 
            = vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc;
    }
    vlSelf->if2id_itf_0_instr_pc = vlSelf->fetch_unit__DOT__if2id_itf_0_instr_pc;
    vlSelf->fetch_unit__DOT__instr_slices_0_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instr_slices_0_valid = 0U;
    vlSelf->fetch_unit__DOT__tmp_2 = 3U;
    if (vlSelf->fetch_unit__DOT__tmp_1) {
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_0) {
            vlSelf->fetch_unit__DOT__instr_slices_0_payload 
                = (((QData)((IData)((0xffffU & (IData)(vlSelf->ibus_rsp_payload_rdata)))) 
                    << 0x10U) | (QData)((IData)(vlSelf->fetch_unit__DOT__tmp_instr_slices_0_payload)));
            vlSelf->fetch_unit__DOT__instr_slices_0_valid = 1U;
            vlSelf->fetch_unit__DOT__tmp_2 = 1U;
        }
    } else if ((3U != (3U & (IData)(vlSelf->ibus_rsp_payload_rdata)))) {
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_0) {
            vlSelf->fetch_unit__DOT__instr_slices_0_payload 
                = (0x100000000ULL | (QData)((IData)(
                                                    (0xffffU 
                                                     & (IData)(vlSelf->ibus_rsp_payload_rdata)))));
            vlSelf->fetch_unit__DOT__instr_slices_0_valid = 1U;
        }
        vlSelf->fetch_unit__DOT__tmp_2 = 2U;
    } else {
        vlSelf->fetch_unit__DOT__tmp_2 = 0U;
    }
    vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready = 0U;
    vlSelf->fetch_unit__DOT__if2id_itf_1_instr_pc = 0U;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__decompInstr_illegal 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_illegal;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst 
        = (0x5013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_tmp_ret_inst) 
                       << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                     << 0xfU) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                 << 7U))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__decompInstr_illegal 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_illegal;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jImm 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm;
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst 
        = (0x5013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_tmp_ret_inst) 
                       << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                     << 0xfU) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                 << 7U))));
    vlSelf->fetch_unit__DOT__slice_valid_vec_0 = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    __Vtableidx1 = vlSelf->fetch_unit__DOT__tmp_2;
    vlSelf->fetch_unit__DOT__tmp_2_string = Vtop__ConstPool__TABLE_hd552ea8d_0
        [__Vtableidx1];
    vlSelf->fetch_unit__DOT__instr_slices_1_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instr_slices_1_valid = 0U;
    vlSelf->fetch_unit__DOT__tmp_3 = 3U;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tmp_2))) {
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_1) {
            vlSelf->fetch_unit__DOT__instr_slices_1_payload 
                = (QData)((IData)(vlSelf->ibus_rsp_payload_rdata));
            vlSelf->fetch_unit__DOT__instr_slices_1_valid = 1U;
        }
        vlSelf->fetch_unit__DOT__tmp_3 = 1U;
    } else if (((1U == (IData)(vlSelf->fetch_unit__DOT__tmp_2)) 
                | (2U == (IData)(vlSelf->fetch_unit__DOT__tmp_2)))) {
        if ((3U != (3U & (IData)((vlSelf->ibus_rsp_payload_rdata 
                                  >> 0x10U))))) {
            if (vlSelf->fetch_unit__DOT__valid_instr_slice_1) {
                vlSelf->fetch_unit__DOT__instr_slices_1_payload 
                    = (0x100000000ULL | (QData)((IData)(
                                                        (0xffffU 
                                                         & (IData)(
                                                                   (vlSelf->ibus_rsp_payload_rdata 
                                                                    >> 0x10U))))));
                vlSelf->fetch_unit__DOT__instr_slices_1_valid = 1U;
            }
            vlSelf->fetch_unit__DOT__tmp_3 = 2U;
        } else {
            vlSelf->fetch_unit__DOT__tmp_3 = 0U;
        }
    }
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready 
            = ((IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_pop_real) 
               & ((IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_valid) 
                  & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_pop_ready)));
    }
    vlSelf->fetch_unit__DOT__addressFifo__DOT__io_pop_ready 
        = vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popping 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo_io_pop_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo_io_pop_ready));
    if (vlSelf->if2id_itf_1_valid) {
        vlSelf->fetch_unit__DOT__if2id_itf_1_instr_pc 
            = (vlSelf->fetch_unit__DOT__tmp_if2id_itf_0_instr_pc 
               + vlSelf->fetch_unit__DOT__tmp_if2id_itf_1_instr_pc_2);
    }
    vlSelf->if2id_itf_1_instr_pc = vlSelf->fetch_unit__DOT__if2id_itf_1_instr_pc;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_1 
        = ((0U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2))
            ? vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst
            : ((1U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2))
                ? (0x40000000U | vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst)
                : ((2U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2))
                    ? (0x7013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm) 
                                   << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                 << 0xfU) 
                                                | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                   << 7U))))
                    : ((((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                         << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                       << 0xfU) | (
                                                   ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_3) 
                                                    << 0xcU) 
                                                   | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                       << 7U) 
                                                      | ((0x1000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                                                          ? 0x3bU
                                                          : 0x33U))))) 
                       | ((0U == (3U & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                        >> 5U))) ? 0x40000000U
                           : 0U)))));
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_1 
        = ((0U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2))
            ? vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst
            : ((1U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2))
                ? (0x40000000U | vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst)
                : ((2U == (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2))
                    ? (0x7013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm) 
                                   << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                 << 0xfU) 
                                                | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                   << 7U))))
                    : ((((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                         << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                       << 0xfU) | (
                                                   ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_3) 
                                                    << 0xcU) 
                                                   | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                       << 7U) 
                                                      | ((0x1000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                                                          ? 0x3bU
                                                          : 0x33U))))) 
                       | ((0U == (3U & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                        >> 5U))) ? 0x40000000U
                           : 0U)))));
    vlSelf->fetch_unit__DOT__slice_valid_vec_1 = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    __Vtableidx2 = vlSelf->fetch_unit__DOT__tmp_3;
    vlSelf->fetch_unit__DOT__tmp_3_string = Vtop__ConstPool__TABLE_hd552ea8d_0
        [__Vtableidx2];
    vlSelf->fetch_unit__DOT__instr_slices_2_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instr_slices_2_valid = 0U;
    vlSelf->fetch_unit__DOT__tmp_4 = 3U;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tmp_3))) {
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_2) {
            vlSelf->fetch_unit__DOT__instr_slices_2_payload 
                = (QData)((IData)((vlSelf->ibus_rsp_payload_rdata 
                                   >> 0x10U)));
            vlSelf->fetch_unit__DOT__instr_slices_2_valid = 1U;
        }
        vlSelf->fetch_unit__DOT__tmp_4 = 1U;
    } else if (((1U == (IData)(vlSelf->fetch_unit__DOT__tmp_3)) 
                | (2U == (IData)(vlSelf->fetch_unit__DOT__tmp_3)))) {
        if ((3U != (3U & (IData)((vlSelf->ibus_rsp_payload_rdata 
                                  >> 0x20U))))) {
            if (vlSelf->fetch_unit__DOT__valid_instr_slice_2) {
                vlSelf->fetch_unit__DOT__instr_slices_2_payload 
                    = (0x100000000ULL | (QData)((IData)(
                                                        (0xffffU 
                                                         & (IData)(
                                                                   (vlSelf->ibus_rsp_payload_rdata 
                                                                    >> 0x20U))))));
                vlSelf->fetch_unit__DOT__instr_slices_2_valid = 1U;
            }
            vlSelf->fetch_unit__DOT__tmp_4 = 2U;
        } else {
            vlSelf->fetch_unit__DOT__tmp_4 = 0U;
        }
    }
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement = 0U;
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__popping) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement = 1U;
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst = 0U;
    if ((2U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        if ((1U & (~ vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))) {
            if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                    if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                  >> 0xdU)))) {
                        vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                            = (0x2023U | ((0xfe000000U 
                                           & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9) 
                                              << 0x14U)) 
                                          | ((0x1f00000U 
                                              & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                 << 0x12U)) 
                                             | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x2) 
                                                 << 0xfU) 
                                                | (0xf80U 
                                                   & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_9) 
                                                      << 7U))))));
                    }
                } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                     >> 0xdU)))) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                        = ((0x400U == (0x7ffU & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                 >> 2U)))
                            ? 0x100073U : ((0U == (0x1fU 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                      >> 2U)))
                                            ? (0x67U 
                                               | ((0xf8000U 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                      << 8U)) 
                                                  | (((0x1000U 
                                                       & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                                                       ? (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x1)
                                                       : (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0)) 
                                                     << 7U)))
                                            : (0x33U 
                                               | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_5) 
                                                   << 0x19U) 
                                                  | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_6) 
                                                      << 0x14U) 
                                                     | ((((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7)
                                                           ? (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_8)
                                                           : (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0)) 
                                                         << 0xfU) 
                                                        | (0xf80U 
                                                           & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)))))));
                }
            } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
                if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                              >> 0xdU)))) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                        = (0x2003U | ((0xc000000U & 
                                       (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                        << 0x18U)) 
                                      | ((0x2000000U 
                                          & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                             << 0xdU)) 
                                         | ((0x1c00000U 
                                             & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                << 0x12U)) 
                                            | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x2) 
                                                << 0xfU) 
                                               | (0xf80U 
                                                  & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))))));
                }
            } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                 >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                    = (0x1013U | ((0x2000000U & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                 << 0xdU)) 
                                  | ((0x1f00000U & 
                                      (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                       << 0x12U)) | 
                                     ((0xf8000U & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                   << 8U)) 
                                      | (0xf80U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)))));
            }
        }
    } else if ((1U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
            = ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                ? ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                    ? ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                        ? (0x1063U | ((0x80000000U 
                                       & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                          << 0x13U)) 
                                      | ((0x7e000000U 
                                          & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                             << 0x14U)) 
                                         | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0) 
                                             << 0x14U) 
                                            | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                << 0xfU) 
                                               | ((0xf00U 
                                                   & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                                      << 7U)) 
                                                  | (0x80U 
                                                     & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                                        >> 4U))))))))
                        : (0x63U | ((0x80000000U & 
                                     ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                      << 0x13U)) | 
                                    ((0x7e000000U & 
                                      ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                       << 0x14U)) | 
                                     (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0) 
                                       << 0x14U) | 
                                      (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                        << 0xfU) | 
                                       ((0xf00U & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                                   << 7U)) 
                                        | (0x80U & 
                                           ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__bImm) 
                                            >> 4U)))))))))
                    : ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                        ? (0x6fU | ((0x80000000U & 
                                     (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                      << 0xbU)) | (
                                                   (0x7fe00000U 
                                                    & (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                                       << 0x14U)) 
                                                   | ((0x100000U 
                                                       & (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                                          << 9U)) 
                                                      | ((0xff000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm) 
                                                         | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x0) 
                                                            << 7U))))))
                        : vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_1))
                : ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                    ? ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                        ? ((2U == (0x1fU & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                            >> 7U)))
                            ? (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi16spImm) 
                                         << 0x14U) 
                                        | ((0xf8000U 
                                            & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                               << 8U)) 
                                           | (0xf80U 
                                              & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))))
                            : (0x37U | ((0xfffff000U 
                                         & vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__luiImm) 
                                        | (0xf80U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))))
                        : (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm) 
                                     << 0x14U) | (0xf80U 
                                                  & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i))))
                    : ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)
                        ? (0x6fU | ((0x80000000U & 
                                     (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                      << 0xbU)) | (
                                                   (0x7fe00000U 
                                                    & (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                                       << 0x14U)) 
                                                   | ((0x100000U 
                                                       & (vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm 
                                                          << 9U)) 
                                                      | ((0xff000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__jalImm) 
                                                         | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__x1) 
                                                            << 7U))))))
                        : (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addImm) 
                                     << 0x14U) | ((0xf8000U 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                                                      << 8U)) 
                                                  | (0xf80U 
                                                     & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)))))));
    } else if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                          >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                    = (0x2023U | ((0x4000000U & ((IData)(
                                                         (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                          >> 5U)) 
                                                 << 0x1aU)) 
                                  | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7) 
                                      << 0x19U) | (
                                                   ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                                                    << 0x14U) 
                                                   | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                       << 0xfU) 
                                                      | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2) 
                                                          << 0xaU) 
                                                         | (0x200U 
                                                            & ((IData)(
                                                                       (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                                        >> 6U)) 
                                                               << 9U))))))));
            }
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                = (0x3027U | ((0xc000000U & ((IData)(
                                                     (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_0 
                                                      >> 5U)) 
                                             << 0x1aU)) 
                              | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_7) 
                                  << 0x19U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                                                << 0x14U) 
                                               | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                                   << 0xfU) 
                                                  | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__tmp_ret_inst_2) 
                                                     << 0xaU))))));
        }
    } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_2_i)) {
        if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                      >> 0xdU)))) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
                = (0x2003U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__lwImm) 
                               << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rch) 
                                             << 0xfU) 
                                            | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                                               << 7U))));
        }
    } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_2_i 
                         >> 0xdU)))) {
        vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst 
            = (0x10013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__addi5spnImm) 
                            << 0x14U) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__rcl) 
                                         << 7U)));
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst = 0U;
    if ((2U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        if ((1U & (~ vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))) {
            if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                    if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                  >> 0xdU)))) {
                        vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                            = (0x2023U | ((0xfe000000U 
                                           & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9) 
                                              << 0x14U)) 
                                          | ((0x1f00000U 
                                              & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                 << 0x12U)) 
                                             | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x2) 
                                                 << 0xfU) 
                                                | (0xf80U 
                                                   & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_9) 
                                                      << 7U))))));
                    }
                } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                     >> 0xdU)))) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                        = ((0x400U == (0x7ffU & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                 >> 2U)))
                            ? 0x100073U : ((0U == (0x1fU 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                      >> 2U)))
                                            ? (0x67U 
                                               | ((0xf8000U 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                      << 8U)) 
                                                  | (((0x1000U 
                                                       & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                                                       ? (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x1)
                                                       : (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0)) 
                                                     << 7U)))
                                            : (0x33U 
                                               | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_5) 
                                                   << 0x19U) 
                                                  | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_6) 
                                                      << 0x14U) 
                                                     | ((((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7)
                                                           ? (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_8)
                                                           : (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0)) 
                                                         << 0xfU) 
                                                        | (0xf80U 
                                                           & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)))))));
                }
            } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
                if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                              >> 0xdU)))) {
                    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                        = (0x2003U | ((0xc000000U & 
                                       (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                        << 0x18U)) 
                                      | ((0x2000000U 
                                          & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                             << 0xdU)) 
                                         | ((0x1c00000U 
                                             & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                << 0x12U)) 
                                            | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x2) 
                                                << 0xfU) 
                                               | (0xf80U 
                                                  & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))))));
                }
            } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                 >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                    = (0x1013U | ((0x2000000U & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                 << 0xdU)) 
                                  | ((0x1f00000U & 
                                      (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                       << 0x12U)) | 
                                     ((0xf8000U & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                   << 8U)) 
                                      | (0xf80U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)))));
            }
        }
    } else if ((1U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
            = ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                ? ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                    ? ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                        ? (0x1063U | ((0x80000000U 
                                       & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                          << 0x13U)) 
                                      | ((0x7e000000U 
                                          & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                             << 0x14U)) 
                                         | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0) 
                                             << 0x14U) 
                                            | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                << 0xfU) 
                                               | ((0xf00U 
                                                   & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                                      << 7U)) 
                                                  | (0x80U 
                                                     & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                                        >> 4U))))))))
                        : (0x63U | ((0x80000000U & 
                                     ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                      << 0x13U)) | 
                                    ((0x7e000000U & 
                                      ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                       << 0x14U)) | 
                                     (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0) 
                                       << 0x14U) | 
                                      (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                        << 0xfU) | 
                                       ((0xf00U & ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                                   << 7U)) 
                                        | (0x80U & 
                                           ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__bImm) 
                                            >> 4U)))))))))
                    : ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                        ? (0x6fU | ((0x80000000U & 
                                     (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                      << 0xbU)) | (
                                                   (0x7fe00000U 
                                                    & (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                                       << 0x14U)) 
                                                   | ((0x100000U 
                                                       & (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                                          << 9U)) 
                                                      | ((0xff000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm) 
                                                         | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x0) 
                                                            << 7U))))))
                        : vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_1))
                : ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                    ? ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                        ? ((2U == (0x1fU & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                            >> 7U)))
                            ? (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi16spImm) 
                                         << 0x14U) 
                                        | ((0xf8000U 
                                            & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                               << 8U)) 
                                           | (0xf80U 
                                              & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))))
                            : (0x37U | ((0xfffff000U 
                                         & vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__luiImm) 
                                        | (0xf80U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))))
                        : (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm) 
                                     << 0x14U) | (0xf80U 
                                                  & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i))))
                    : ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)
                        ? (0x6fU | ((0x80000000U & 
                                     (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                      << 0xbU)) | (
                                                   (0x7fe00000U 
                                                    & (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                                       << 0x14U)) 
                                                   | ((0x100000U 
                                                       & (vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm 
                                                          << 9U)) 
                                                      | ((0xff000U 
                                                          & vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__jalImm) 
                                                         | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__x1) 
                                                            << 7U))))))
                        : (0x13U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addImm) 
                                     << 0x14U) | ((0xf8000U 
                                                   & (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                                                      << 8U)) 
                                                  | (0xf80U 
                                                     & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)))))));
    } else if ((0x8000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                          >> 0xdU)))) {
                vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                    = (0x2023U | ((0x4000000U & ((IData)(
                                                         (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                          >> 5U)) 
                                                 << 0x1aU)) 
                                  | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7) 
                                      << 0x19U) | (
                                                   ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                                                    << 0x14U) 
                                                   | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                       << 0xfU) 
                                                      | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2) 
                                                          << 0xaU) 
                                                         | (0x200U 
                                                            & ((IData)(
                                                                       (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                                        >> 6U)) 
                                                               << 9U))))))));
            }
        } else if ((0x2000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                = (0x3027U | ((0xc000000U & ((IData)(
                                                     (vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_readedVec_1 
                                                      >> 5U)) 
                                             << 0x1aU)) 
                              | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_7) 
                                  << 0x19U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                                                << 0x14U) 
                                               | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                                   << 0xfU) 
                                                  | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__tmp_ret_inst_2) 
                                                     << 0xaU))))));
        }
    } else if ((0x4000U & vlSelf->fetch_unit__DOT__rvcDecompressor_3_i)) {
        if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                      >> 0xdU)))) {
            vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
                = (0x2003U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__lwImm) 
                               << 0x14U) | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rch) 
                                             << 0xfU) 
                                            | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                                               << 7U))));
        }
    } else if ((1U & (~ (vlSelf->fetch_unit__DOT__rvcDecompressor_3_i 
                         >> 0xdU)))) {
        vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst 
            = (0x10013U | (((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__addi5spnImm) 
                            << 0x14U) | ((IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__rcl) 
                                         << 7U)));
    }
    vlSelf->fetch_unit__DOT__slice_valid_vec_2 = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    __Vtableidx3 = vlSelf->fetch_unit__DOT__tmp_4;
    vlSelf->fetch_unit__DOT__tmp_4_string = Vtop__ConstPool__TABLE_hd552ea8d_0
        [__Vtableidx3];
    vlSelf->fetch_unit__DOT__tmp_5 = 3U;
    vlSelf->fetch_unit__DOT__instr_slices_3_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instr_slices_3_valid = 0U;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tmp_4))) {
        vlSelf->fetch_unit__DOT__tmp_5 = 1U;
        if (vlSelf->fetch_unit__DOT__valid_instr_slice_3) {
            vlSelf->fetch_unit__DOT__instr_slices_3_payload 
                = (QData)((IData)((vlSelf->ibus_rsp_payload_rdata 
                                   >> 0x20U)));
            vlSelf->fetch_unit__DOT__instr_slices_3_valid = 1U;
        }
    } else if (((1U == (IData)(vlSelf->fetch_unit__DOT__tmp_4)) 
                | (2U == (IData)(vlSelf->fetch_unit__DOT__tmp_4)))) {
        if ((3U != (3U & (IData)((vlSelf->ibus_rsp_payload_rdata 
                                  >> 0x30U))))) {
            vlSelf->fetch_unit__DOT__tmp_5 = 2U;
            if (vlSelf->fetch_unit__DOT__valid_instr_slice_3) {
                vlSelf->fetch_unit__DOT__instr_slices_3_payload 
                    = (0x100000000ULL | (QData)((IData)(
                                                        (0xffffU 
                                                         & (IData)(
                                                                   (vlSelf->ibus_rsp_payload_rdata 
                                                                    >> 0x30U))))));
                vlSelf->fetch_unit__DOT__instr_slices_3_valid = 1U;
            }
        } else {
            vlSelf->fetch_unit__DOT__tmp_5 = 0U;
        }
    }
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_popPtr_valueNext_1 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement;
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willOverflow 
        = ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willOverflowIfInc) 
           & (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_popPtr_valueNext 
        = vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willIncrement;
    vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst;
    vlSelf->fetch_unit__DOT__real_instr_0 = (((~ (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_illegal)) 
                                              & (IData)(vlSelf->fetch_unit__DOT__tmp_pc_step_all_1))
                                              ? vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__ret_inst
                                              : vlSelf->fetch_unit__DOT__rvcDecompressor_2_i);
    vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst;
    vlSelf->fetch_unit__DOT__real_instr_1 = (((~ (IData)(vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_illegal)) 
                                              & (IData)(vlSelf->fetch_unit__DOT__tmp_pc_step_all_2))
                                              ? vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__ret_inst
                                              : vlSelf->fetch_unit__DOT__rvcDecompressor_3_i);
    __Vtableidx4 = vlSelf->fetch_unit__DOT__tmp_5;
    vlSelf->fetch_unit__DOT__tmp_5_string = Vtop__ConstPool__TABLE_hd552ea8d_0
        [__Vtableidx4];
    vlSelf->fetch_unit__DOT__slice_valid_vec_3 = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__datain_reverse 
        = (((IData)(vlSelf->fetch_unit__DOT__instr_slices_0_valid) 
            << 3U) | (((IData)(vlSelf->fetch_unit__DOT__instr_slices_1_valid) 
                       << 2U) | (((IData)(vlSelf->fetch_unit__DOT__instr_slices_2_valid) 
                                  << 1U) | (IData)(vlSelf->fetch_unit__DOT__instr_slices_3_valid))));
    vlSelf->fetch_unit__DOT__tail_one_matrix_0 = (((IData)(vlSelf->fetch_unit__DOT__instr_slices_3_valid) 
                                                   << 3U) 
                                                  | (((IData)(vlSelf->fetch_unit__DOT__instr_slices_2_valid) 
                                                      << 2U) 
                                                     | (((IData)(vlSelf->fetch_unit__DOT__instr_slices_1_valid) 
                                                         << 1U) 
                                                        | (IData)(vlSelf->fetch_unit__DOT__instr_slices_0_valid))));
    vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_valueNext 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_value) 
                 + (IData)(vlSelf->fetch_unit__DOT__addressFifo__DOT__tmp_popPtr_valueNext)));
    if (vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_willClear) {
        vlSelf->fetch_unit__DOT__addressFifo__DOT__popPtr_valueNext = 0U;
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_2__DOT__decompInstr_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_2_decompInstr_inst;
    vlSelf->fetch_unit__DOT__if2id_itf_0_instr_data = 0U;
    if (vlSelf->if2id_itf_0_valid) {
        vlSelf->fetch_unit__DOT__if2id_itf_0_instr_data 
            = vlSelf->fetch_unit__DOT__real_instr_0;
    }
    vlSelf->fetch_unit__DOT__rvcDecompressor_3__DOT__decompInstr_inst 
        = vlSelf->fetch_unit__DOT__rvcDecompressor_3_decompInstr_inst;
    vlSelf->fetch_unit__DOT__if2id_itf_1_instr_data = 0U;
    if (vlSelf->if2id_itf_1_valid) {
        vlSelf->fetch_unit__DOT__if2id_itf_1_instr_data 
            = vlSelf->fetch_unit__DOT__real_instr_1;
    }
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_in 
        = vlSelf->fetch_unit__DOT__tail_one_matrix_0;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s 
        = (0xfU & (~ (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_0)));
    vlSelf->if2id_itf_0_instr_data = vlSelf->fetch_unit__DOT__if2id_itf_0_instr_data;
    vlSelf->if2id_itf_1_instr_data = vlSelf->fetch_unit__DOT__if2id_itf_1_instr_data;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__empty 
        = (1U & (~ (IData)((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s)))));
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_node_0 
        = vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s;
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_0 
        = ((1U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s))
            ? 0U : 1U);
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_1 
        = ((4U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s))
            ? 2U : 3U);
    fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0 
        = (IData)((0U != (3U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s))));
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_node_1 
        = (((IData)((0U != (0xcU & (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s)))) 
            << 1U) | (IData)(fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0));
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index2_0 
        = ((IData)(fetch_unit__DOT__trailone_index_0__DOT____VdfgTmp_hff61d294__0)
            ? (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_0)
            : (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index1_1));
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__tmp_cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index2_0;
    if ((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_0__DOT__data_s))) {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_0 
            = vlSelf->fetch_unit__DOT__trailone_index_0__DOT__stage_index2_0;
        vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out 
            = vlSelf->fetch_unit__DOT__trailone_index_0__DOT__tmp_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_0 = 0U;
        vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out = 4U;
    }
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_0))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_0_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_0_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    } else if ((1U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_0))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_1_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_0_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    } else if ((2U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_0))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_2_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_0_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    } else {
        vlSelf->fetch_unit__DOT__tmp_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_3_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_0_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    }
    vlSelf->fetch_unit__DOT__tail_one_matrix_1 = vlSelf->fetch_unit__DOT__tail_one_matrix_0;
    vlSelf->fetch_unit__DOT__tail_one_matrix_1 = ((~ 
                                                   ((IData)(1U) 
                                                    << (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_0))) 
                                                  & (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1));
    vlSelf->fetch_unit__DOT__trailone_index_0__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out;
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_0 = 
        (4U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_0_cnt_out));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload = 0ULL;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_in 
        = vlSelf->fetch_unit__DOT__tail_one_matrix_1;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__datain_reverse 
        = ((8U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1) 
                  << 3U)) | ((4U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1) 
                                    << 1U)) | ((2U 
                                                & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1) 
                                                   >> 1U)) 
                                               | (1U 
                                                  & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1) 
                                                     >> 3U)))));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s 
        = (0xfU & (~ (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_1)));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid = 0U;
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload 
            = vlSelf->fetch_unit__DOT__tmp_io_push_0_payload;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_payload;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__empty 
        = (1U & (~ (IData)((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s)))));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_node_0 
        = vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_0 
        = ((1U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s))
            ? 0U : 1U);
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_1 
        = ((4U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s))
            ? 2U : 3U);
    fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0 
        = (IData)((0U != (3U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s))));
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid 
            = ((IData)(vlSelf->fetch_unit__DOT__tail_one_valid_vec_0) 
               & (IData)(vlSelf->fetch_unit__DOT__tmp_io_push_0_valid));
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_0_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_0 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_0 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready));
    vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_push_0_fire 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_ready) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_node_1 
        = (((IData)((0U != (0xcU & (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s)))) 
            << 1U) | (IData)(fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index2_0 
        = ((IData)(fetch_unit__DOT__trailone_index_1__DOT____VdfgTmp_hff61d294__0)
            ? (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_0)
            : (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index1_1));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__din_vld 
        = vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_push_0_fire;
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__tmp_cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index2_0;
    if ((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_1__DOT__data_s))) {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_1 
            = vlSelf->fetch_unit__DOT__trailone_index_1__DOT__stage_index2_0;
        vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out 
            = vlSelf->fetch_unit__DOT__trailone_index_1__DOT__tmp_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_1 = 0U;
        vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out = 4U;
    }
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_1))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_0_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_1_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    } else if ((1U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_1))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_1_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_1_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    } else if ((2U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_1))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_2_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_1_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    } else {
        vlSelf->fetch_unit__DOT__tmp_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_3_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_1_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    }
    vlSelf->fetch_unit__DOT__tail_one_matrix_2 = vlSelf->fetch_unit__DOT__tail_one_matrix_1;
    vlSelf->fetch_unit__DOT__tail_one_matrix_2 = ((~ 
                                                   ((IData)(1U) 
                                                    << (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_1))) 
                                                  & (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2));
    vlSelf->fetch_unit__DOT__trailone_index_1__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out;
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_1 = 
        (4U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_1_cnt_out));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload = 0ULL;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_in 
        = vlSelf->fetch_unit__DOT__tail_one_matrix_2;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__datain_reverse 
        = ((8U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2) 
                  << 3U)) | ((4U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2) 
                                    << 1U)) | ((2U 
                                                & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2) 
                                                   >> 1U)) 
                                               | (1U 
                                                  & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2) 
                                                     >> 3U)))));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s 
        = (0xfU & (~ (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_2)));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid = 0U;
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload 
            = vlSelf->fetch_unit__DOT__tmp_io_push_1_payload;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_payload;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__empty 
        = (1U & (~ (IData)((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s)))));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_node_0 
        = vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s;
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_0 
        = ((1U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s))
            ? 0U : 1U);
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_1 
        = ((4U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s))
            ? 2U : 3U);
    fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0 
        = (IData)((0U != (3U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s))));
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid 
            = ((IData)(vlSelf->fetch_unit__DOT__tail_one_valid_vec_1) 
               & (IData)(vlSelf->fetch_unit__DOT__tmp_io_push_1_valid));
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_1_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_1 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_1 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_1_ready));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele0 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid)));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_node_1 
        = (((IData)((0U != (0xcU & (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s)))) 
            << 1U) | (IData)(fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index2_0 
        = ((IData)(fetch_unit__DOT__trailone_index_2__DOT____VdfgTmp_hff61d294__0)
            ? (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_0)
            : (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index1_1));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_3 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_1) 
           == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__tmp_cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index2_0;
    if ((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_2__DOT__data_s))) {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_2 
            = vlSelf->fetch_unit__DOT__trailone_index_2__DOT__stage_index2_0;
        vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out 
            = vlSelf->fetch_unit__DOT__trailone_index_2__DOT__tmp_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_2 = 0U;
        vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out = 4U;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_2 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_3;
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_2))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_0_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_2_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    } else if ((1U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_2))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_1_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_2_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    } else if ((2U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_2))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_2_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_2_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    } else {
        vlSelf->fetch_unit__DOT__tmp_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_3_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_2_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    }
    vlSelf->fetch_unit__DOT__tail_one_matrix_3 = vlSelf->fetch_unit__DOT__tail_one_matrix_2;
    vlSelf->fetch_unit__DOT__tail_one_matrix_3 = ((~ 
                                                   ((IData)(1U) 
                                                    << (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_2))) 
                                                  & (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_1 
        = (3U & (((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_0) 
                  == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num)) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_2)));
    vlSelf->fetch_unit__DOT__trailone_index_2__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out;
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_2 = 
        (4U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_2_cnt_out));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload = 0ULL;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_in 
        = vlSelf->fetch_unit__DOT__tail_one_matrix_3;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__datain_reverse 
        = ((8U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3) 
                  << 3U)) | ((4U & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3) 
                                    << 1U)) | ((2U 
                                                & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3) 
                                                   >> 1U)) 
                                               | (1U 
                                                  & ((IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3) 
                                                     >> 3U)))));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s = 0U;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s 
        = (0xfU & (~ (IData)(vlSelf->fetch_unit__DOT__tail_one_matrix_3)));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid = 0U;
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload 
            = vlSelf->fetch_unit__DOT__tmp_io_push_2_payload;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_payload;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__empty 
        = (1U & (~ (IData)((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s)))));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_node_0 
        = vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s;
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_0 
        = ((1U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s))
            ? 0U : 1U);
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_1 
        = ((4U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s))
            ? 2U : 3U);
    fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0 
        = (IData)((0U != (3U & (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s))));
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid 
            = ((IData)(vlSelf->fetch_unit__DOT__tail_one_valid_vec_2) 
               & (IData)(vlSelf->fetch_unit__DOT__tmp_io_push_2_valid));
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_2_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_2 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_2 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_2_ready));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_node_1 
        = (((IData)((0U != (0xcU & (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s)))) 
            << 1U) | (IData)(fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index2_0 
        = ((IData)(fetch_unit__DOT__trailone_index_3__DOT____VdfgTmp_hff61d294__0)
            ? (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_0)
            : (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index1_1));
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__tmp_cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index2_0;
    if ((0U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_3__DOT__data_s))) {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_3 
            = vlSelf->fetch_unit__DOT__trailone_index_3__DOT__stage_index2_0;
        vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out 
            = vlSelf->fetch_unit__DOT__trailone_index_3__DOT__tmp_cnt_out;
    } else {
        vlSelf->fetch_unit__DOT__tail_one_index_vec_3 = 0U;
        vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out = 4U;
    }
    if ((0U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_3))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_0_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_3_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_0_valid;
    } else if ((1U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_3))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_1_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_3_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_1_valid;
    } else if ((2U == (IData)(vlSelf->fetch_unit__DOT__tail_one_index_vec_3))) {
        vlSelf->fetch_unit__DOT__tmp_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_2_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_3_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_2_valid;
    } else {
        vlSelf->fetch_unit__DOT__tmp_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__instr_slices_3_payload;
        vlSelf->fetch_unit__DOT__tmp_io_push_3_valid 
            = vlSelf->fetch_unit__DOT__instr_slices_3_valid;
    }
    vlSelf->fetch_unit__DOT__trailone_index_3__DOT__cnt_out 
        = vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out;
    vlSelf->fetch_unit__DOT__tail_one_valid_vec_3 = 
        (4U != (IData)(vlSelf->fetch_unit__DOT__trailone_index_3_cnt_out));
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload = 0ULL;
    vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid = 0U;
    if (vlSelf->fetch_unit__DOT__ibus_rsp_fire) {
        vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload 
            = vlSelf->fetch_unit__DOT__tmp_io_push_3_payload;
        vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid 
            = ((IData)(vlSelf->fetch_unit__DOT__tail_one_valid_vec_3) 
               & (IData)(vlSelf->fetch_unit__DOT__tmp_io_push_3_valid));
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_payload 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_payload;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__io_push_3_valid 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_init_3 
        = vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid;
    vlSelf->fetch_unit__DOT__instr_push_valid_vec = 
        (((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid) 
          << 3U) | (((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid) 
                     << 2U) | (((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_1_valid) 
                                << 1U) | (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_0_valid))));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_3 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid) 
           & (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_3_ready));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele1 
        = (3U & ((IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_2_valid) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo_io_push_3_valid)));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__data_in 
        = vlSelf->fetch_unit__DOT__instr_push_valid_vec;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_7 
        = ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_3) 
           == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num));
    vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage2_ele0 
        = (7U & ((IData)(vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele0) 
                 + (IData)(vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage1_ele1)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_6 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_7;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_5 
        = (3U & (((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushing_vec_2) 
                  == (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num)) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_6)));
    if (vlSelf->fetch_unit__DOT__toplevel_instrFifo_io_push_0_fire) {
        vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out 
            = vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__popsum_stage2_ele0;
        vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__cnt_out 
            = vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out;
        vlSelf->fetch_unit__DOT__validNumFifo_push_payload = 0U;
        vlSelf->fetch_unit__DOT__validNumFifo_push_valid = 0U;
        if ((0U != (IData)(vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out))) {
            vlSelf->fetch_unit__DOT__validNumFifo_push_payload 
                = vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out;
            vlSelf->fetch_unit__DOT__validNumFifo_push_valid = 1U;
        }
    } else {
        vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out = 0U;
        vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc__DOT__cnt_out 
            = vlSelf->fetch_unit__DOT__instr_push_valid_vec_popc_cnt_out;
        vlSelf->fetch_unit__DOT__validNumFifo_push_payload = 0U;
        vlSelf->fetch_unit__DOT__validNumFifo_push_valid = 0U;
    }
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_4 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_5;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_payload 
        = vlSelf->fetch_unit__DOT__validNumFifo_push_payload;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__push_valid 
        = vlSelf->fetch_unit__DOT__validNumFifo_push_valid;
    vlSelf->fetch_unit__DOT__validNumFifo__DOT__pushing 
        = ((IData)(vlSelf->fetch_unit__DOT__validNumFifo_push_ready) 
           & (IData)(vlSelf->fetch_unit__DOT__validNumFifo_push_valid));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__inc_num 
        = (7U & ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_1) 
                 + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_inc_num_4)));
    vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrGlobalInc 
        = vlSelf->fetch_unit__DOT__instrFifo__DOT__inc_num;
    vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobalInc 
        = (0xfU & ((IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__pushPtrGlobal) 
                   + (IData)(vlSelf->fetch_unit__DOT__instrFifo__DOT__tmp_pushPtrGlobalInc)));
}

void Vtop___024root___eval_nba(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_nba\n"); );
    // Body
    if (vlSelf->__VnbaTriggered.at(0U)) {
        Vtop___024root___nba_sequent__TOP__0(vlSelf);
    }
    if (vlSelf->__VnbaTriggered.at(1U)) {
        Vtop___024root___nba_sequent__TOP__1(vlSelf);
    }
    if (vlSelf->__VnbaTriggered.at(0U)) {
        Vtop___024root___nba_sequent__TOP__2(vlSelf);
    }
    if ((vlSelf->__VnbaTriggered.at(0U) | vlSelf->__VnbaTriggered.at(1U))) {
        Vtop___024root___nba_comb__TOP__0(vlSelf);
    }
}

void Vtop___024root___eval_triggers__ico(Vtop___024root* vlSelf);
#ifdef VL_DEBUG
VL_ATTR_COLD void Vtop___024root___dump_triggers__ico(Vtop___024root* vlSelf);
#endif  // VL_DEBUG
void Vtop___024root___eval_triggers__act(Vtop___024root* vlSelf);
#ifdef VL_DEBUG
VL_ATTR_COLD void Vtop___024root___dump_triggers__act(Vtop___024root* vlSelf);
#endif  // VL_DEBUG
#ifdef VL_DEBUG
VL_ATTR_COLD void Vtop___024root___dump_triggers__nba(Vtop___024root* vlSelf);
#endif  // VL_DEBUG

void Vtop___024root___eval(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval\n"); );
    // Init
    CData/*0:0*/ __VicoContinue;
    VlTriggerVec<2> __VpreTriggered;
    IData/*31:0*/ __VnbaIterCount;
    CData/*0:0*/ __VnbaContinue;
    // Body
    vlSelf->__VicoIterCount = 0U;
    __VicoContinue = 1U;
    while (__VicoContinue) {
        __VicoContinue = 0U;
        Vtop___024root___eval_triggers__ico(vlSelf);
        if (vlSelf->__VicoTriggered.any()) {
            __VicoContinue = 1U;
            if (VL_UNLIKELY((0x64U < vlSelf->__VicoIterCount))) {
#ifdef VL_DEBUG
                Vtop___024root___dump_triggers__ico(vlSelf);
#endif
                VL_FATAL_MT("/home/lyc/projects/riscv/FlappyRiscv/cocoTest/fetch_unit/rtl/fetch_unit.v", 7, "", "Input combinational region did not converge.");
            }
            vlSelf->__VicoIterCount = ((IData)(1U) 
                                       + vlSelf->__VicoIterCount);
            Vtop___024root___eval_ico(vlSelf);
        }
    }
    __VnbaIterCount = 0U;
    __VnbaContinue = 1U;
    while (__VnbaContinue) {
        __VnbaContinue = 0U;
        vlSelf->__VnbaTriggered.clear();
        vlSelf->__VactIterCount = 0U;
        vlSelf->__VactContinue = 1U;
        while (vlSelf->__VactContinue) {
            vlSelf->__VactContinue = 0U;
            Vtop___024root___eval_triggers__act(vlSelf);
            if (vlSelf->__VactTriggered.any()) {
                vlSelf->__VactContinue = 1U;
                if (VL_UNLIKELY((0x64U < vlSelf->__VactIterCount))) {
#ifdef VL_DEBUG
                    Vtop___024root___dump_triggers__act(vlSelf);
#endif
                    VL_FATAL_MT("/home/lyc/projects/riscv/FlappyRiscv/cocoTest/fetch_unit/rtl/fetch_unit.v", 7, "", "Active region did not converge.");
                }
                vlSelf->__VactIterCount = ((IData)(1U) 
                                           + vlSelf->__VactIterCount);
                __VpreTriggered.andNot(vlSelf->__VactTriggered, vlSelf->__VnbaTriggered);
                vlSelf->__VnbaTriggered.set(vlSelf->__VactTriggered);
                Vtop___024root___eval_act(vlSelf);
            }
        }
        if (vlSelf->__VnbaTriggered.any()) {
            __VnbaContinue = 1U;
            if (VL_UNLIKELY((0x64U < __VnbaIterCount))) {
#ifdef VL_DEBUG
                Vtop___024root___dump_triggers__nba(vlSelf);
#endif
                VL_FATAL_MT("/home/lyc/projects/riscv/FlappyRiscv/cocoTest/fetch_unit/rtl/fetch_unit.v", 7, "", "NBA region did not converge.");
            }
            __VnbaIterCount = ((IData)(1U) + __VnbaIterCount);
            Vtop___024root___eval_nba(vlSelf);
        }
    }
}

#ifdef VL_DEBUG
void Vtop___024root___eval_debug_assertions(Vtop___024root* vlSelf) {
    if (false && vlSelf) {}  // Prevent unused
    Vtop__Syms* const __restrict vlSymsp VL_ATTR_UNUSED = vlSelf->vlSymsp;
    VL_DEBUG_IF(VL_DBG_MSGF("+    Vtop___024root___eval_debug_assertions\n"); );
    // Body
    if (VL_UNLIKELY((vlSelf->core_en & 0xfeU))) {
        Verilated::overWidthError("core_en");}
    if (VL_UNLIKELY((vlSelf->ibus_cmd_ready & 0xfeU))) {
        Verilated::overWidthError("ibus_cmd_ready");}
    if (VL_UNLIKELY((vlSelf->ibus_rsp_valid & 0xfeU))) {
        Verilated::overWidthError("ibus_rsp_valid");}
    if (VL_UNLIKELY((vlSelf->fetchStage_flush & 0xfeU))) {
        Verilated::overWidthError("fetchStage_flush");}
    if (VL_UNLIKELY((vlSelf->fetchStage_isDone & 0xfeU))) {
        Verilated::overWidthError("fetchStage_isDone");}
    if (VL_UNLIKELY((vlSelf->pcJumpTarget_valid & 0xfeU))) {
        Verilated::overWidthError("pcJumpTarget_valid");}
    if (VL_UNLIKELY((vlSelf->pcPredicted_valid & 0xfeU))) {
        Verilated::overWidthError("pcPredicted_valid");}
    if (VL_UNLIKELY((vlSelf->clk & 0xfeU))) {
        Verilated::overWidthError("clk");}
    if (VL_UNLIKELY((vlSelf->resetn & 0xfeU))) {
        Verilated::overWidthError("resetn");}
}
#endif  // VL_DEBUG
