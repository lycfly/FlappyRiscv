// Generator : SpinalHDL v1.9.0    git head : 7d30dbacbd3aa1be42fb2a3d4da5675703aae2ae
// Component : rename
// Git hash  : b4c6ae4f065b1a8ff5aa3c65c18f66892b8899f9

`timescale 1ns/1ps

module rename (
  input               valid,
  output              ready,
  input               flush,
  input               uop_0_rs1_is_used,
  input      [1:0]    uop_0_rs1_source,
  input      [1:0]    uop_0_rs1_kind,
  input      [1:0]    uop_0_rs1_width,
  input      [4:0]    uop_0_rs1_index,
  input               uop_0_rs2_is_used,
  input      [1:0]    uop_0_rs2_source,
  input      [1:0]    uop_0_rs2_kind,
  input      [1:0]    uop_0_rs2_width,
  input      [4:0]    uop_0_rs2_index,
  input               uop_0_rd_is_used,
  input      [1:0]    uop_0_rd_kind,
  input      [1:0]    uop_0_rd_width,
  input      [4:0]    uop_0_rd_index,
  input      [31:0]   uop_0_imm,
  input      [4:0]    uop_0_op,
  input      [31:0]   uop_0_pc,
  input               uop_1_rs1_is_used,
  input      [1:0]    uop_1_rs1_source,
  input      [1:0]    uop_1_rs1_kind,
  input      [1:0]    uop_1_rs1_width,
  input      [4:0]    uop_1_rs1_index,
  input               uop_1_rs2_is_used,
  input      [1:0]    uop_1_rs2_source,
  input      [1:0]    uop_1_rs2_kind,
  input      [1:0]    uop_1_rs2_width,
  input      [4:0]    uop_1_rs2_index,
  input               uop_1_rd_is_used,
  input      [1:0]    uop_1_rd_kind,
  input      [1:0]    uop_1_rd_width,
  input      [4:0]    uop_1_rd_index,
  input      [31:0]   uop_1_imm,
  input      [4:0]    uop_1_op,
  input      [31:0]   uop_1_pc,
  output reg          dispRob_flist_popptr_store_0_valid,
  output reg [5:0]    dispRob_flist_popptr_store_0_payload,
  output reg          dispRob_flist_popptr_store_1_valid,
  output reg [5:0]    dispRob_flist_popptr_store_1_payload,
  output              dispRob_rdInfo_0_valid,
  output     [4:0]    dispRob_rdInfo_0_archIndex,
  output     [5:0]    dispRob_rdInfo_0_phyIndex,
  output     [5:0]    dispRob_rdInfo_0_oldPhyIndex,
  output              dispRob_rdInfo_1_valid,
  output     [4:0]    dispRob_rdInfo_1_archIndex,
  output     [5:0]    dispRob_rdInfo_1_phyIndex,
  output     [5:0]    dispRob_rdInfo_1_oldPhyIndex,
  input               retired_ready_0,
  input               retired_ready_1,
  input               retired_rdInfo_0_valid,
  input      [4:0]    retired_rdInfo_0_archIndex,
  input      [5:0]    retired_rdInfo_0_phyIndex,
  input      [5:0]    retired_rdInfo_0_oldPhyIndex,
  input               retired_rdInfo_1_valid,
  input      [4:0]    retired_rdInfo_1_archIndex,
  input      [5:0]    retired_rdInfo_1_phyIndex,
  input      [5:0]    retired_rdInfo_1_oldPhyIndex,
  input               retired_branch_error_restore,
  input      [5:0]    retired_flist_restore,
  output              uopRenamed_0_rs1_is_used,
  output     [1:0]    uopRenamed_0_rs1_source,
  output     [1:0]    uopRenamed_0_rs1_kind,
  output     [1:0]    uopRenamed_0_rs1_width,
  output     [5:0]    uopRenamed_0_rs1_index,
  output              uopRenamed_0_rs2_is_used,
  output     [1:0]    uopRenamed_0_rs2_source,
  output     [1:0]    uopRenamed_0_rs2_kind,
  output     [1:0]    uopRenamed_0_rs2_width,
  output     [5:0]    uopRenamed_0_rs2_index,
  output              uopRenamed_0_rd_is_used,
  output     [1:0]    uopRenamed_0_rd_kind,
  output     [1:0]    uopRenamed_0_rd_width,
  output     [5:0]    uopRenamed_0_rd_index,
  output     [31:0]   uopRenamed_0_imm,
  output     [4:0]    uopRenamed_0_op,
  output     [31:0]   uopRenamed_0_pc,
  output              uopRenamed_1_rs1_is_used,
  output     [1:0]    uopRenamed_1_rs1_source,
  output     [1:0]    uopRenamed_1_rs1_kind,
  output     [1:0]    uopRenamed_1_rs1_width,
  output reg [5:0]    uopRenamed_1_rs1_index,
  output              uopRenamed_1_rs2_is_used,
  output     [1:0]    uopRenamed_1_rs2_source,
  output     [1:0]    uopRenamed_1_rs2_kind,
  output     [1:0]    uopRenamed_1_rs2_width,
  output reg [5:0]    uopRenamed_1_rs2_index,
  output              uopRenamed_1_rd_is_used,
  output     [1:0]    uopRenamed_1_rd_kind,
  output     [1:0]    uopRenamed_1_rd_width,
  output     [5:0]    uopRenamed_1_rd_index,
  output     [31:0]   uopRenamed_1_imm,
  output     [4:0]    uopRenamed_1_op,
  output     [31:0]   uopRenamed_1_pc,
  input               clk,
  input               resetn
);
  localparam REGISTER_1 = 2'd0;
  localparam IMM = 2'd1;
  localparam PC = 2'd2;
  localparam X0 = 2'd3;
  localparam GPR = 2'd0;
  localparam FPR = 2'd1;
  localparam VR = 2'd2;
  localparam REGNONE = 2'd3;
  localparam B = 2'd0;
  localparam H = 2'd1;
  localparam W = 2'd2;
  localparam IDLE = 5'd0;
  localparam ADD = 5'd1;
  localparam SUB = 5'd2;
  localparam SLT = 5'd3;
  localparam SLTU = 5'd4;
  localparam AND_1 = 5'd5;
  localparam OR_1 = 5'd6;
  localparam XOR_1 = 5'd7;
  localparam SLL_1 = 5'd8;
  localparam SRL_1 = 5'd9;
  localparam SRA_1 = 5'd10;
  localparam LOAD = 5'd11;
  localparam LOADU = 5'd12;
  localparam STORE = 5'd13;
  localparam JAL = 5'd14;
  localparam JALR = 5'd15;
  localparam BEQ = 5'd16;
  localparam BNE = 5'd17;
  localparam BLT = 5'd18;
  localparam BGE = 5'd19;
  localparam BLTU = 5'd20;
  localparam BGEU = 5'd21;

  wire                srat_rat_if_rs1_read_0_valid;
  wire                srat_rat_if_rs1_read_1_valid;
  wire                srat_rat_if_rs2_read_0_valid;
  wire                srat_rat_if_rs2_read_1_valid;
  wire                srat_rat_if_rd_read_0_valid;
  wire                srat_rat_if_rd_read_1_valid;
  wire                arat_1_rd_write_0_valid;
  wire                arat_1_rd_write_1_valid;
  wire                flist_io_push_0_valid;
  wire                flist_io_push_1_valid;
  wire                flist_io_pop_0_ready;
  wire                flist_io_pop_1_ready;
  wire                srat_rat_if_phy_rs1_0_valid;
  wire       [5:0]    srat_rat_if_phy_rs1_0_payload;
  wire                srat_rat_if_phy_rs2_0_valid;
  wire       [5:0]    srat_rat_if_phy_rs2_0_payload;
  wire                srat_rat_if_phy_rd_0_valid;
  wire       [5:0]    srat_rat_if_phy_rd_0_payload;
  wire                srat_rat_if_phy_rs1_1_valid;
  wire       [5:0]    srat_rat_if_phy_rs1_1_payload;
  wire                srat_rat_if_phy_rs2_1_valid;
  wire       [5:0]    srat_rat_if_phy_rs2_1_payload;
  wire                srat_rat_if_phy_rd_1_valid;
  wire       [5:0]    srat_rat_if_phy_rd_1_payload;
  wire       [5:0]    arat_1_rat_out_0;
  wire       [5:0]    arat_1_rat_out_1;
  wire       [5:0]    arat_1_rat_out_2;
  wire       [5:0]    arat_1_rat_out_3;
  wire       [5:0]    arat_1_rat_out_4;
  wire       [5:0]    arat_1_rat_out_5;
  wire       [5:0]    arat_1_rat_out_6;
  wire       [5:0]    arat_1_rat_out_7;
  wire       [5:0]    arat_1_rat_out_8;
  wire       [5:0]    arat_1_rat_out_9;
  wire       [5:0]    arat_1_rat_out_10;
  wire       [5:0]    arat_1_rat_out_11;
  wire       [5:0]    arat_1_rat_out_12;
  wire       [5:0]    arat_1_rat_out_13;
  wire       [5:0]    arat_1_rat_out_14;
  wire       [5:0]    arat_1_rat_out_15;
  wire       [5:0]    arat_1_rat_out_16;
  wire       [5:0]    arat_1_rat_out_17;
  wire       [5:0]    arat_1_rat_out_18;
  wire       [5:0]    arat_1_rat_out_19;
  wire       [5:0]    arat_1_rat_out_20;
  wire       [5:0]    arat_1_rat_out_21;
  wire       [5:0]    arat_1_rat_out_22;
  wire       [5:0]    arat_1_rat_out_23;
  wire       [5:0]    arat_1_rat_out_24;
  wire       [5:0]    arat_1_rat_out_25;
  wire       [5:0]    arat_1_rat_out_26;
  wire       [5:0]    arat_1_rat_out_27;
  wire       [5:0]    arat_1_rat_out_28;
  wire       [5:0]    arat_1_rat_out_29;
  wire       [5:0]    arat_1_rat_out_30;
  wire       [5:0]    arat_1_rat_out_31;
  wire                flist_io_push_0_ready;
  wire                flist_io_push_1_ready;
  wire                flist_io_pop_0_valid;
  wire       [5:0]    flist_io_pop_0_payload;
  wire                flist_io_pop_1_valid;
  wire       [5:0]    flist_io_pop_1_payload;
  wire       [5:0]    flist_io_pop_ptr_store_0;
  wire       [5:0]    flist_io_pop_ptr_store_1;
  wire       [5:0]    flist_occupancy;
  wire                WawFound_0;
  wire       [5:0]    srat_rd_write_value_0;
  wire       [5:0]    srat_rd_write_value_1;
  reg                 srat_rd_write_enable_0;
  wire                srat_rd_write_enable_1;
  wire       [5:0]    oldPhyIndex_0;
  reg        [5:0]    oldPhyIndex_1;
  wire                is_branch_0;
  wire                is_branch_1;
  wire                toplevel_flist_io_pop_0_fire;
  wire                toplevel_flist_io_pop_1_fire;
  `ifndef SYNTHESIS
  reg [79:0] uop_0_rs1_source_string;
  reg [55:0] uop_0_rs1_kind_string;
  reg [7:0] uop_0_rs1_width_string;
  reg [79:0] uop_0_rs2_source_string;
  reg [55:0] uop_0_rs2_kind_string;
  reg [7:0] uop_0_rs2_width_string;
  reg [55:0] uop_0_rd_kind_string;
  reg [7:0] uop_0_rd_width_string;
  reg [39:0] uop_0_op_string;
  reg [79:0] uop_1_rs1_source_string;
  reg [55:0] uop_1_rs1_kind_string;
  reg [7:0] uop_1_rs1_width_string;
  reg [79:0] uop_1_rs2_source_string;
  reg [55:0] uop_1_rs2_kind_string;
  reg [7:0] uop_1_rs2_width_string;
  reg [55:0] uop_1_rd_kind_string;
  reg [7:0] uop_1_rd_width_string;
  reg [39:0] uop_1_op_string;
  reg [79:0] uopRenamed_0_rs1_source_string;
  reg [55:0] uopRenamed_0_rs1_kind_string;
  reg [7:0] uopRenamed_0_rs1_width_string;
  reg [79:0] uopRenamed_0_rs2_source_string;
  reg [55:0] uopRenamed_0_rs2_kind_string;
  reg [7:0] uopRenamed_0_rs2_width_string;
  reg [55:0] uopRenamed_0_rd_kind_string;
  reg [7:0] uopRenamed_0_rd_width_string;
  reg [39:0] uopRenamed_0_op_string;
  reg [79:0] uopRenamed_1_rs1_source_string;
  reg [55:0] uopRenamed_1_rs1_kind_string;
  reg [7:0] uopRenamed_1_rs1_width_string;
  reg [79:0] uopRenamed_1_rs2_source_string;
  reg [55:0] uopRenamed_1_rs2_kind_string;
  reg [7:0] uopRenamed_1_rs2_width_string;
  reg [55:0] uopRenamed_1_rd_kind_string;
  reg [7:0] uopRenamed_1_rd_width_string;
  reg [39:0] uopRenamed_1_op_string;
  `endif


  rat srat (
    .flush                     (flush                             ), //i
    .recovery                  (retired_branch_error_restore      ), //i
    .rat_if_rs1_read_0_valid   (srat_rat_if_rs1_read_0_valid      ), //i
    .rat_if_rs1_read_0_payload (uop_0_rs1_index[4:0]              ), //i
    .rat_if_rs1_read_1_valid   (srat_rat_if_rs1_read_1_valid      ), //i
    .rat_if_rs1_read_1_payload (uop_1_rs1_index[4:0]              ), //i
    .rat_if_rs2_read_0_valid   (srat_rat_if_rs2_read_0_valid      ), //i
    .rat_if_rs2_read_0_payload (uop_0_rs2_index[4:0]              ), //i
    .rat_if_rs2_read_1_valid   (srat_rat_if_rs2_read_1_valid      ), //i
    .rat_if_rs2_read_1_payload (uop_1_rs2_index[4:0]              ), //i
    .rat_if_rd_read_0_valid    (srat_rat_if_rd_read_0_valid       ), //i
    .rat_if_rd_read_0_payload  (uop_0_rd_index[4:0]               ), //i
    .rat_if_rd_read_1_valid    (srat_rat_if_rd_read_1_valid       ), //i
    .rat_if_rd_read_1_payload  (uop_1_rd_index[4:0]               ), //i
    .rat_if_rd_write_0_valid   (srat_rd_write_enable_0            ), //i
    .rat_if_rd_write_0_payload (uop_0_rd_index[4:0]               ), //i
    .rat_if_rd_write_1_valid   (srat_rd_write_enable_1            ), //i
    .rat_if_rd_write_1_payload (uop_1_rd_index[4:0]               ), //i
    .rat_if_rd_write_value_0   (srat_rd_write_value_0[5:0]        ), //i
    .rat_if_rd_write_value_1   (srat_rd_write_value_1[5:0]        ), //i
    .rat_if_phy_rs1_0_valid    (srat_rat_if_phy_rs1_0_valid       ), //o
    .rat_if_phy_rs1_0_payload  (srat_rat_if_phy_rs1_0_payload[5:0]), //o
    .rat_if_phy_rs1_1_valid    (srat_rat_if_phy_rs1_1_valid       ), //o
    .rat_if_phy_rs1_1_payload  (srat_rat_if_phy_rs1_1_payload[5:0]), //o
    .rat_if_phy_rs2_0_valid    (srat_rat_if_phy_rs2_0_valid       ), //o
    .rat_if_phy_rs2_0_payload  (srat_rat_if_phy_rs2_0_payload[5:0]), //o
    .rat_if_phy_rs2_1_valid    (srat_rat_if_phy_rs2_1_valid       ), //o
    .rat_if_phy_rs2_1_payload  (srat_rat_if_phy_rs2_1_payload[5:0]), //o
    .rat_if_phy_rd_0_valid     (srat_rat_if_phy_rd_0_valid        ), //o
    .rat_if_phy_rd_0_payload   (srat_rat_if_phy_rd_0_payload[5:0] ), //o
    .rat_if_phy_rd_1_valid     (srat_rat_if_phy_rd_1_valid        ), //o
    .rat_if_phy_rd_1_payload   (srat_rat_if_phy_rd_1_payload[5:0] ), //o
    .arat_in_0                 (arat_1_rat_out_0[5:0]             ), //i
    .arat_in_1                 (arat_1_rat_out_1[5:0]             ), //i
    .arat_in_2                 (arat_1_rat_out_2[5:0]             ), //i
    .arat_in_3                 (arat_1_rat_out_3[5:0]             ), //i
    .arat_in_4                 (arat_1_rat_out_4[5:0]             ), //i
    .arat_in_5                 (arat_1_rat_out_5[5:0]             ), //i
    .arat_in_6                 (arat_1_rat_out_6[5:0]             ), //i
    .arat_in_7                 (arat_1_rat_out_7[5:0]             ), //i
    .arat_in_8                 (arat_1_rat_out_8[5:0]             ), //i
    .arat_in_9                 (arat_1_rat_out_9[5:0]             ), //i
    .arat_in_10                (arat_1_rat_out_10[5:0]            ), //i
    .arat_in_11                (arat_1_rat_out_11[5:0]            ), //i
    .arat_in_12                (arat_1_rat_out_12[5:0]            ), //i
    .arat_in_13                (arat_1_rat_out_13[5:0]            ), //i
    .arat_in_14                (arat_1_rat_out_14[5:0]            ), //i
    .arat_in_15                (arat_1_rat_out_15[5:0]            ), //i
    .arat_in_16                (arat_1_rat_out_16[5:0]            ), //i
    .arat_in_17                (arat_1_rat_out_17[5:0]            ), //i
    .arat_in_18                (arat_1_rat_out_18[5:0]            ), //i
    .arat_in_19                (arat_1_rat_out_19[5:0]            ), //i
    .arat_in_20                (arat_1_rat_out_20[5:0]            ), //i
    .arat_in_21                (arat_1_rat_out_21[5:0]            ), //i
    .arat_in_22                (arat_1_rat_out_22[5:0]            ), //i
    .arat_in_23                (arat_1_rat_out_23[5:0]            ), //i
    .arat_in_24                (arat_1_rat_out_24[5:0]            ), //i
    .arat_in_25                (arat_1_rat_out_25[5:0]            ), //i
    .arat_in_26                (arat_1_rat_out_26[5:0]            ), //i
    .arat_in_27                (arat_1_rat_out_27[5:0]            ), //i
    .arat_in_28                (arat_1_rat_out_28[5:0]            ), //i
    .arat_in_29                (arat_1_rat_out_29[5:0]            ), //i
    .arat_in_30                (arat_1_rat_out_30[5:0]            ), //i
    .arat_in_31                (arat_1_rat_out_31[5:0]            ), //i
    .clk                       (clk                               ), //i
    .resetn                    (resetn                            )  //i
  );
  arat arat_1 (
    .flush              (1'b0                           ), //i
    .rd_write_0_valid   (arat_1_rd_write_0_valid        ), //i
    .rd_write_0_payload (retired_rdInfo_0_archIndex[4:0]), //i
    .rd_write_1_valid   (arat_1_rd_write_1_valid        ), //i
    .rd_write_1_payload (retired_rdInfo_1_archIndex[4:0]), //i
    .rd_write_value_0   (retired_rdInfo_0_phyIndex[5:0] ), //i
    .rd_write_value_1   (retired_rdInfo_1_phyIndex[5:0] ), //i
    .rat_out_0          (arat_1_rat_out_0[5:0]          ), //o
    .rat_out_1          (arat_1_rat_out_1[5:0]          ), //o
    .rat_out_2          (arat_1_rat_out_2[5:0]          ), //o
    .rat_out_3          (arat_1_rat_out_3[5:0]          ), //o
    .rat_out_4          (arat_1_rat_out_4[5:0]          ), //o
    .rat_out_5          (arat_1_rat_out_5[5:0]          ), //o
    .rat_out_6          (arat_1_rat_out_6[5:0]          ), //o
    .rat_out_7          (arat_1_rat_out_7[5:0]          ), //o
    .rat_out_8          (arat_1_rat_out_8[5:0]          ), //o
    .rat_out_9          (arat_1_rat_out_9[5:0]          ), //o
    .rat_out_10         (arat_1_rat_out_10[5:0]         ), //o
    .rat_out_11         (arat_1_rat_out_11[5:0]         ), //o
    .rat_out_12         (arat_1_rat_out_12[5:0]         ), //o
    .rat_out_13         (arat_1_rat_out_13[5:0]         ), //o
    .rat_out_14         (arat_1_rat_out_14[5:0]         ), //o
    .rat_out_15         (arat_1_rat_out_15[5:0]         ), //o
    .rat_out_16         (arat_1_rat_out_16[5:0]         ), //o
    .rat_out_17         (arat_1_rat_out_17[5:0]         ), //o
    .rat_out_18         (arat_1_rat_out_18[5:0]         ), //o
    .rat_out_19         (arat_1_rat_out_19[5:0]         ), //o
    .rat_out_20         (arat_1_rat_out_20[5:0]         ), //o
    .rat_out_21         (arat_1_rat_out_21[5:0]         ), //o
    .rat_out_22         (arat_1_rat_out_22[5:0]         ), //o
    .rat_out_23         (arat_1_rat_out_23[5:0]         ), //o
    .rat_out_24         (arat_1_rat_out_24[5:0]         ), //o
    .rat_out_25         (arat_1_rat_out_25[5:0]         ), //o
    .rat_out_26         (arat_1_rat_out_26[5:0]         ), //o
    .rat_out_27         (arat_1_rat_out_27[5:0]         ), //o
    .rat_out_28         (arat_1_rat_out_28[5:0]         ), //o
    .rat_out_29         (arat_1_rat_out_29[5:0]         ), //o
    .rat_out_30         (arat_1_rat_out_30[5:0]         ), //o
    .rat_out_31         (arat_1_rat_out_31[5:0]         ), //o
    .clk                (clk                            ), //i
    .resetn             (resetn                         )  //i
  );
  freelist flist (
    .io_push_0_valid            (flist_io_push_0_valid            ), //i
    .io_push_0_ready            (flist_io_push_0_ready            ), //o
    .io_push_0_payload          (retired_rdInfo_0_oldPhyIndex[5:0]), //i
    .io_push_1_valid            (flist_io_push_1_valid            ), //i
    .io_push_1_ready            (flist_io_push_1_ready            ), //o
    .io_push_1_payload          (retired_rdInfo_1_oldPhyIndex[5:0]), //i
    .io_pop_0_valid             (flist_io_pop_0_valid             ), //o
    .io_pop_0_ready             (flist_io_pop_0_ready             ), //i
    .io_pop_0_payload           (flist_io_pop_0_payload[5:0]      ), //o
    .io_pop_1_valid             (flist_io_pop_1_valid             ), //o
    .io_pop_1_ready             (flist_io_pop_1_ready             ), //i
    .io_pop_1_payload           (flist_io_pop_1_payload[5:0]      ), //o
    .io_flush                   (1'b0                             ), //i
    .io_pop_ptr_store_0         (flist_io_pop_ptr_store_0[5:0]    ), //o
    .io_pop_ptr_store_1         (flist_io_pop_ptr_store_1[5:0]    ), //o
    .io_pop_ptr_restore_valid   (retired_branch_error_restore     ), //i
    .io_pop_ptr_restore_payload (retired_flist_restore[5:0]       ), //i
    .occupancy                  (flist_occupancy[5:0]             ), //o
    .clk                        (clk                              ), //i
    .resetn                     (resetn                           )  //i
  );
  `ifndef SYNTHESIS
  always @(*) begin
    case(uop_0_rs1_source)
      REGISTER_1 : uop_0_rs1_source_string = "REGISTER_1";
      IMM : uop_0_rs1_source_string = "IMM       ";
      PC : uop_0_rs1_source_string = "PC        ";
      X0 : uop_0_rs1_source_string = "X0        ";
      default : uop_0_rs1_source_string = "??????????";
    endcase
  end
  always @(*) begin
    case(uop_0_rs1_kind)
      GPR : uop_0_rs1_kind_string = "GPR    ";
      FPR : uop_0_rs1_kind_string = "FPR    ";
      VR : uop_0_rs1_kind_string = "VR     ";
      REGNONE : uop_0_rs1_kind_string = "REGNONE";
      default : uop_0_rs1_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uop_0_rs1_width)
      B : uop_0_rs1_width_string = "B";
      H : uop_0_rs1_width_string = "H";
      W : uop_0_rs1_width_string = "W";
      default : uop_0_rs1_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uop_0_rs2_source)
      REGISTER_1 : uop_0_rs2_source_string = "REGISTER_1";
      IMM : uop_0_rs2_source_string = "IMM       ";
      PC : uop_0_rs2_source_string = "PC        ";
      X0 : uop_0_rs2_source_string = "X0        ";
      default : uop_0_rs2_source_string = "??????????";
    endcase
  end
  always @(*) begin
    case(uop_0_rs2_kind)
      GPR : uop_0_rs2_kind_string = "GPR    ";
      FPR : uop_0_rs2_kind_string = "FPR    ";
      VR : uop_0_rs2_kind_string = "VR     ";
      REGNONE : uop_0_rs2_kind_string = "REGNONE";
      default : uop_0_rs2_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uop_0_rs2_width)
      B : uop_0_rs2_width_string = "B";
      H : uop_0_rs2_width_string = "H";
      W : uop_0_rs2_width_string = "W";
      default : uop_0_rs2_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uop_0_rd_kind)
      GPR : uop_0_rd_kind_string = "GPR    ";
      FPR : uop_0_rd_kind_string = "FPR    ";
      VR : uop_0_rd_kind_string = "VR     ";
      REGNONE : uop_0_rd_kind_string = "REGNONE";
      default : uop_0_rd_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uop_0_rd_width)
      B : uop_0_rd_width_string = "B";
      H : uop_0_rd_width_string = "H";
      W : uop_0_rd_width_string = "W";
      default : uop_0_rd_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uop_0_op)
      IDLE : uop_0_op_string = "IDLE ";
      ADD : uop_0_op_string = "ADD  ";
      SUB : uop_0_op_string = "SUB  ";
      SLT : uop_0_op_string = "SLT  ";
      SLTU : uop_0_op_string = "SLTU ";
      AND_1 : uop_0_op_string = "AND_1";
      OR_1 : uop_0_op_string = "OR_1 ";
      XOR_1 : uop_0_op_string = "XOR_1";
      SLL_1 : uop_0_op_string = "SLL_1";
      SRL_1 : uop_0_op_string = "SRL_1";
      SRA_1 : uop_0_op_string = "SRA_1";
      LOAD : uop_0_op_string = "LOAD ";
      LOADU : uop_0_op_string = "LOADU";
      STORE : uop_0_op_string = "STORE";
      JAL : uop_0_op_string = "JAL  ";
      JALR : uop_0_op_string = "JALR ";
      BEQ : uop_0_op_string = "BEQ  ";
      BNE : uop_0_op_string = "BNE  ";
      BLT : uop_0_op_string = "BLT  ";
      BGE : uop_0_op_string = "BGE  ";
      BLTU : uop_0_op_string = "BLTU ";
      BGEU : uop_0_op_string = "BGEU ";
      default : uop_0_op_string = "?????";
    endcase
  end
  always @(*) begin
    case(uop_1_rs1_source)
      REGISTER_1 : uop_1_rs1_source_string = "REGISTER_1";
      IMM : uop_1_rs1_source_string = "IMM       ";
      PC : uop_1_rs1_source_string = "PC        ";
      X0 : uop_1_rs1_source_string = "X0        ";
      default : uop_1_rs1_source_string = "??????????";
    endcase
  end
  always @(*) begin
    case(uop_1_rs1_kind)
      GPR : uop_1_rs1_kind_string = "GPR    ";
      FPR : uop_1_rs1_kind_string = "FPR    ";
      VR : uop_1_rs1_kind_string = "VR     ";
      REGNONE : uop_1_rs1_kind_string = "REGNONE";
      default : uop_1_rs1_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uop_1_rs1_width)
      B : uop_1_rs1_width_string = "B";
      H : uop_1_rs1_width_string = "H";
      W : uop_1_rs1_width_string = "W";
      default : uop_1_rs1_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uop_1_rs2_source)
      REGISTER_1 : uop_1_rs2_source_string = "REGISTER_1";
      IMM : uop_1_rs2_source_string = "IMM       ";
      PC : uop_1_rs2_source_string = "PC        ";
      X0 : uop_1_rs2_source_string = "X0        ";
      default : uop_1_rs2_source_string = "??????????";
    endcase
  end
  always @(*) begin
    case(uop_1_rs2_kind)
      GPR : uop_1_rs2_kind_string = "GPR    ";
      FPR : uop_1_rs2_kind_string = "FPR    ";
      VR : uop_1_rs2_kind_string = "VR     ";
      REGNONE : uop_1_rs2_kind_string = "REGNONE";
      default : uop_1_rs2_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uop_1_rs2_width)
      B : uop_1_rs2_width_string = "B";
      H : uop_1_rs2_width_string = "H";
      W : uop_1_rs2_width_string = "W";
      default : uop_1_rs2_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uop_1_rd_kind)
      GPR : uop_1_rd_kind_string = "GPR    ";
      FPR : uop_1_rd_kind_string = "FPR    ";
      VR : uop_1_rd_kind_string = "VR     ";
      REGNONE : uop_1_rd_kind_string = "REGNONE";
      default : uop_1_rd_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uop_1_rd_width)
      B : uop_1_rd_width_string = "B";
      H : uop_1_rd_width_string = "H";
      W : uop_1_rd_width_string = "W";
      default : uop_1_rd_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uop_1_op)
      IDLE : uop_1_op_string = "IDLE ";
      ADD : uop_1_op_string = "ADD  ";
      SUB : uop_1_op_string = "SUB  ";
      SLT : uop_1_op_string = "SLT  ";
      SLTU : uop_1_op_string = "SLTU ";
      AND_1 : uop_1_op_string = "AND_1";
      OR_1 : uop_1_op_string = "OR_1 ";
      XOR_1 : uop_1_op_string = "XOR_1";
      SLL_1 : uop_1_op_string = "SLL_1";
      SRL_1 : uop_1_op_string = "SRL_1";
      SRA_1 : uop_1_op_string = "SRA_1";
      LOAD : uop_1_op_string = "LOAD ";
      LOADU : uop_1_op_string = "LOADU";
      STORE : uop_1_op_string = "STORE";
      JAL : uop_1_op_string = "JAL  ";
      JALR : uop_1_op_string = "JALR ";
      BEQ : uop_1_op_string = "BEQ  ";
      BNE : uop_1_op_string = "BNE  ";
      BLT : uop_1_op_string = "BLT  ";
      BGE : uop_1_op_string = "BGE  ";
      BLTU : uop_1_op_string = "BLTU ";
      BGEU : uop_1_op_string = "BGEU ";
      default : uop_1_op_string = "?????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_rs1_source)
      REGISTER_1 : uopRenamed_0_rs1_source_string = "REGISTER_1";
      IMM : uopRenamed_0_rs1_source_string = "IMM       ";
      PC : uopRenamed_0_rs1_source_string = "PC        ";
      X0 : uopRenamed_0_rs1_source_string = "X0        ";
      default : uopRenamed_0_rs1_source_string = "??????????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_rs1_kind)
      GPR : uopRenamed_0_rs1_kind_string = "GPR    ";
      FPR : uopRenamed_0_rs1_kind_string = "FPR    ";
      VR : uopRenamed_0_rs1_kind_string = "VR     ";
      REGNONE : uopRenamed_0_rs1_kind_string = "REGNONE";
      default : uopRenamed_0_rs1_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_rs1_width)
      B : uopRenamed_0_rs1_width_string = "B";
      H : uopRenamed_0_rs1_width_string = "H";
      W : uopRenamed_0_rs1_width_string = "W";
      default : uopRenamed_0_rs1_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_rs2_source)
      REGISTER_1 : uopRenamed_0_rs2_source_string = "REGISTER_1";
      IMM : uopRenamed_0_rs2_source_string = "IMM       ";
      PC : uopRenamed_0_rs2_source_string = "PC        ";
      X0 : uopRenamed_0_rs2_source_string = "X0        ";
      default : uopRenamed_0_rs2_source_string = "??????????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_rs2_kind)
      GPR : uopRenamed_0_rs2_kind_string = "GPR    ";
      FPR : uopRenamed_0_rs2_kind_string = "FPR    ";
      VR : uopRenamed_0_rs2_kind_string = "VR     ";
      REGNONE : uopRenamed_0_rs2_kind_string = "REGNONE";
      default : uopRenamed_0_rs2_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_rs2_width)
      B : uopRenamed_0_rs2_width_string = "B";
      H : uopRenamed_0_rs2_width_string = "H";
      W : uopRenamed_0_rs2_width_string = "W";
      default : uopRenamed_0_rs2_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_rd_kind)
      GPR : uopRenamed_0_rd_kind_string = "GPR    ";
      FPR : uopRenamed_0_rd_kind_string = "FPR    ";
      VR : uopRenamed_0_rd_kind_string = "VR     ";
      REGNONE : uopRenamed_0_rd_kind_string = "REGNONE";
      default : uopRenamed_0_rd_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_rd_width)
      B : uopRenamed_0_rd_width_string = "B";
      H : uopRenamed_0_rd_width_string = "H";
      W : uopRenamed_0_rd_width_string = "W";
      default : uopRenamed_0_rd_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uopRenamed_0_op)
      IDLE : uopRenamed_0_op_string = "IDLE ";
      ADD : uopRenamed_0_op_string = "ADD  ";
      SUB : uopRenamed_0_op_string = "SUB  ";
      SLT : uopRenamed_0_op_string = "SLT  ";
      SLTU : uopRenamed_0_op_string = "SLTU ";
      AND_1 : uopRenamed_0_op_string = "AND_1";
      OR_1 : uopRenamed_0_op_string = "OR_1 ";
      XOR_1 : uopRenamed_0_op_string = "XOR_1";
      SLL_1 : uopRenamed_0_op_string = "SLL_1";
      SRL_1 : uopRenamed_0_op_string = "SRL_1";
      SRA_1 : uopRenamed_0_op_string = "SRA_1";
      LOAD : uopRenamed_0_op_string = "LOAD ";
      LOADU : uopRenamed_0_op_string = "LOADU";
      STORE : uopRenamed_0_op_string = "STORE";
      JAL : uopRenamed_0_op_string = "JAL  ";
      JALR : uopRenamed_0_op_string = "JALR ";
      BEQ : uopRenamed_0_op_string = "BEQ  ";
      BNE : uopRenamed_0_op_string = "BNE  ";
      BLT : uopRenamed_0_op_string = "BLT  ";
      BGE : uopRenamed_0_op_string = "BGE  ";
      BLTU : uopRenamed_0_op_string = "BLTU ";
      BGEU : uopRenamed_0_op_string = "BGEU ";
      default : uopRenamed_0_op_string = "?????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_rs1_source)
      REGISTER_1 : uopRenamed_1_rs1_source_string = "REGISTER_1";
      IMM : uopRenamed_1_rs1_source_string = "IMM       ";
      PC : uopRenamed_1_rs1_source_string = "PC        ";
      X0 : uopRenamed_1_rs1_source_string = "X0        ";
      default : uopRenamed_1_rs1_source_string = "??????????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_rs1_kind)
      GPR : uopRenamed_1_rs1_kind_string = "GPR    ";
      FPR : uopRenamed_1_rs1_kind_string = "FPR    ";
      VR : uopRenamed_1_rs1_kind_string = "VR     ";
      REGNONE : uopRenamed_1_rs1_kind_string = "REGNONE";
      default : uopRenamed_1_rs1_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_rs1_width)
      B : uopRenamed_1_rs1_width_string = "B";
      H : uopRenamed_1_rs1_width_string = "H";
      W : uopRenamed_1_rs1_width_string = "W";
      default : uopRenamed_1_rs1_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_rs2_source)
      REGISTER_1 : uopRenamed_1_rs2_source_string = "REGISTER_1";
      IMM : uopRenamed_1_rs2_source_string = "IMM       ";
      PC : uopRenamed_1_rs2_source_string = "PC        ";
      X0 : uopRenamed_1_rs2_source_string = "X0        ";
      default : uopRenamed_1_rs2_source_string = "??????????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_rs2_kind)
      GPR : uopRenamed_1_rs2_kind_string = "GPR    ";
      FPR : uopRenamed_1_rs2_kind_string = "FPR    ";
      VR : uopRenamed_1_rs2_kind_string = "VR     ";
      REGNONE : uopRenamed_1_rs2_kind_string = "REGNONE";
      default : uopRenamed_1_rs2_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_rs2_width)
      B : uopRenamed_1_rs2_width_string = "B";
      H : uopRenamed_1_rs2_width_string = "H";
      W : uopRenamed_1_rs2_width_string = "W";
      default : uopRenamed_1_rs2_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_rd_kind)
      GPR : uopRenamed_1_rd_kind_string = "GPR    ";
      FPR : uopRenamed_1_rd_kind_string = "FPR    ";
      VR : uopRenamed_1_rd_kind_string = "VR     ";
      REGNONE : uopRenamed_1_rd_kind_string = "REGNONE";
      default : uopRenamed_1_rd_kind_string = "???????";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_rd_width)
      B : uopRenamed_1_rd_width_string = "B";
      H : uopRenamed_1_rd_width_string = "H";
      W : uopRenamed_1_rd_width_string = "W";
      default : uopRenamed_1_rd_width_string = "?";
    endcase
  end
  always @(*) begin
    case(uopRenamed_1_op)
      IDLE : uopRenamed_1_op_string = "IDLE ";
      ADD : uopRenamed_1_op_string = "ADD  ";
      SUB : uopRenamed_1_op_string = "SUB  ";
      SLT : uopRenamed_1_op_string = "SLT  ";
      SLTU : uopRenamed_1_op_string = "SLTU ";
      AND_1 : uopRenamed_1_op_string = "AND_1";
      OR_1 : uopRenamed_1_op_string = "OR_1 ";
      XOR_1 : uopRenamed_1_op_string = "XOR_1";
      SLL_1 : uopRenamed_1_op_string = "SLL_1";
      SRL_1 : uopRenamed_1_op_string = "SRL_1";
      SRA_1 : uopRenamed_1_op_string = "SRA_1";
      LOAD : uopRenamed_1_op_string = "LOAD ";
      LOADU : uopRenamed_1_op_string = "LOADU";
      STORE : uopRenamed_1_op_string = "STORE";
      JAL : uopRenamed_1_op_string = "JAL  ";
      JALR : uopRenamed_1_op_string = "JALR ";
      BEQ : uopRenamed_1_op_string = "BEQ  ";
      BNE : uopRenamed_1_op_string = "BNE  ";
      BLT : uopRenamed_1_op_string = "BLT  ";
      BGE : uopRenamed_1_op_string = "BGE  ";
      BLTU : uopRenamed_1_op_string = "BLTU ";
      BGEU : uopRenamed_1_op_string = "BGEU ";
      default : uopRenamed_1_op_string = "?????";
    endcase
  end
  `endif

  assign srat_rat_if_rs1_read_0_valid = (((valid && uop_0_rs1_is_used) && (uop_0_rs1_source == REGISTER_1)) && (uop_0_rs1_kind == GPR));
  assign srat_rat_if_rs2_read_0_valid = (((valid && uop_0_rs2_is_used) && (uop_0_rs2_source == REGISTER_1)) && (uop_0_rs2_kind == GPR));
  assign srat_rat_if_rd_read_0_valid = ((valid && uop_0_rd_is_used) && (uop_0_rd_kind == GPR));
  assign srat_rat_if_rs1_read_1_valid = (((valid && uop_1_rs1_is_used) && (uop_1_rs1_source == REGISTER_1)) && (uop_1_rs1_kind == GPR));
  assign srat_rat_if_rs2_read_1_valid = (((valid && uop_1_rs2_is_used) && (uop_1_rs2_source == REGISTER_1)) && (uop_1_rs2_kind == GPR));
  assign srat_rat_if_rd_read_1_valid = ((valid && uop_1_rd_is_used) && (uop_1_rd_kind == GPR));
  assign is_branch_0 = ((((((((uop_0_op == JAL) && (uop_0_op == JALR)) && (uop_0_op == BEQ)) && (uop_0_op == BNE)) && (uop_0_op == BLT)) && (uop_0_op == BGE)) && (uop_0_op == BLTU)) && (uop_0_op == BGEU));
  always @(*) begin
    dispRob_flist_popptr_store_0_payload = 6'h00;
    if(is_branch_0) begin
      dispRob_flist_popptr_store_0_payload = flist_io_pop_ptr_store_0;
    end
  end

  always @(*) begin
    dispRob_flist_popptr_store_0_valid = 1'b0;
    if(is_branch_0) begin
      dispRob_flist_popptr_store_0_valid = 1'b1;
    end
  end

  assign flist_io_pop_0_ready = (srat_rat_if_rd_read_0_valid && ready);
  assign flist_io_push_0_valid = (retired_rdInfo_0_valid && retired_ready_0);
  assign arat_1_rd_write_0_valid = (retired_rdInfo_0_valid && retired_ready_0);
  assign dispRob_rdInfo_0_valid = srat_rat_if_rd_read_0_valid;
  assign dispRob_rdInfo_0_archIndex = uop_0_rd_index;
  assign dispRob_rdInfo_0_phyIndex = flist_io_pop_0_payload;
  assign dispRob_rdInfo_0_oldPhyIndex = oldPhyIndex_0;
  assign is_branch_1 = ((((((((uop_1_op == JAL) && (uop_1_op == JALR)) && (uop_1_op == BEQ)) && (uop_1_op == BNE)) && (uop_1_op == BLT)) && (uop_1_op == BGE)) && (uop_1_op == BLTU)) && (uop_1_op == BGEU));
  always @(*) begin
    dispRob_flist_popptr_store_1_payload = 6'h00;
    if(is_branch_1) begin
      dispRob_flist_popptr_store_1_payload = flist_io_pop_ptr_store_1;
    end
  end

  always @(*) begin
    dispRob_flist_popptr_store_1_valid = 1'b0;
    if(is_branch_1) begin
      dispRob_flist_popptr_store_1_valid = 1'b1;
    end
  end

  assign flist_io_pop_1_ready = (srat_rat_if_rd_read_1_valid && ready);
  assign flist_io_push_1_valid = (retired_rdInfo_1_valid && retired_ready_1);
  assign arat_1_rd_write_1_valid = (retired_rdInfo_1_valid && retired_ready_1);
  assign dispRob_rdInfo_1_valid = srat_rat_if_rd_read_1_valid;
  assign dispRob_rdInfo_1_archIndex = uop_1_rd_index;
  assign dispRob_rdInfo_1_phyIndex = flist_io_pop_1_payload;
  assign dispRob_rdInfo_1_oldPhyIndex = oldPhyIndex_1;
  assign uopRenamed_0_op = uop_0_op;
  assign uopRenamed_0_imm = uop_0_imm;
  assign uopRenamed_0_pc = uop_0_pc;
  assign uopRenamed_0_rs1_is_used = srat_rat_if_phy_rs1_0_valid;
  assign uopRenamed_0_rs1_kind = uop_0_rs1_kind;
  assign uopRenamed_0_rs1_source = uop_0_rs1_source;
  assign uopRenamed_0_rs1_width = uop_0_rs1_width;
  assign uopRenamed_0_rs2_is_used = srat_rat_if_phy_rs2_0_valid;
  assign uopRenamed_0_rs2_kind = uop_0_rs2_kind;
  assign uopRenamed_0_rs2_source = uop_0_rs2_source;
  assign uopRenamed_0_rs2_width = uop_0_rs2_width;
  assign uopRenamed_0_rd_is_used = srat_rat_if_phy_rd_0_valid;
  assign uopRenamed_0_rd_kind = uop_0_rd_kind;
  assign uopRenamed_0_rd_width = uop_0_rd_width;
  assign uopRenamed_0_rs1_index = srat_rat_if_phy_rs1_0_payload;
  assign uopRenamed_0_rs2_index = srat_rat_if_phy_rs2_0_payload;
  assign uopRenamed_0_rd_index = flist_io_pop_0_payload;
  assign toplevel_flist_io_pop_0_fire = (flist_io_pop_0_valid && flist_io_pop_0_ready);
  always @(*) begin
    srat_rd_write_enable_0 = toplevel_flist_io_pop_0_fire;
    if(WawFound_0) begin
      srat_rd_write_enable_0 = 1'b0;
    end
  end

  assign srat_rd_write_value_0 = flist_io_pop_0_payload;
  assign oldPhyIndex_0 = srat_rat_if_phy_rd_0_payload;
  assign uopRenamed_1_op = uop_1_op;
  assign uopRenamed_1_imm = uop_1_imm;
  assign uopRenamed_1_pc = uop_1_pc;
  assign uopRenamed_1_rs1_is_used = srat_rat_if_phy_rs1_1_valid;
  assign uopRenamed_1_rs1_kind = uop_1_rs1_kind;
  assign uopRenamed_1_rs1_source = uop_1_rs1_source;
  assign uopRenamed_1_rs1_width = uop_1_rs1_width;
  assign uopRenamed_1_rs2_is_used = srat_rat_if_phy_rs2_1_valid;
  assign uopRenamed_1_rs2_kind = uop_1_rs2_kind;
  assign uopRenamed_1_rs2_source = uop_1_rs2_source;
  assign uopRenamed_1_rs2_width = uop_1_rs2_width;
  assign uopRenamed_1_rd_is_used = srat_rat_if_phy_rd_1_valid;
  assign uopRenamed_1_rd_kind = uop_1_rd_kind;
  assign uopRenamed_1_rd_width = uop_1_rd_width;
  always @(*) begin
    uopRenamed_1_rs1_index = srat_rat_if_phy_rs1_1_payload;
    if((uop_1_rs1_index == uop_0_rd_index)) begin
      uopRenamed_1_rs1_index = uopRenamed_1_rd_index;
    end
  end

  always @(*) begin
    uopRenamed_1_rs2_index = srat_rat_if_phy_rs2_1_payload;
    if((uop_1_rs2_index == uop_0_rd_index)) begin
      uopRenamed_1_rs2_index = uopRenamed_1_rd_index;
    end
  end

  assign uopRenamed_1_rd_index = flist_io_pop_1_payload;
  assign toplevel_flist_io_pop_1_fire = (flist_io_pop_1_valid && flist_io_pop_1_ready);
  assign srat_rd_write_enable_1 = toplevel_flist_io_pop_1_fire;
  assign srat_rd_write_value_1 = flist_io_pop_1_payload;
  always @(*) begin
    oldPhyIndex_1 = srat_rat_if_phy_rd_1_payload;
    if((uop_1_rd_index == uop_0_rd_index)) begin
      oldPhyIndex_1 = flist_io_pop_0_payload;
    end
  end

  assign WawFound_0 = (uop_0_rd_index == uop_1_rd_index);
  assign ready = flist_io_pop_1_valid;

endmodule

module freelist (
  input               io_push_0_valid,
  output              io_push_0_ready,
  input      [5:0]    io_push_0_payload,
  input               io_push_1_valid,
  output              io_push_1_ready,
  input      [5:0]    io_push_1_payload,
  output              io_pop_0_valid,
  input               io_pop_0_ready,
  output     [5:0]    io_pop_0_payload,
  output              io_pop_1_valid,
  input               io_pop_1_ready,
  output     [5:0]    io_pop_1_payload,
  input               io_flush,
  output     [5:0]    io_pop_ptr_store_0,
  output     [5:0]    io_pop_ptr_store_1,
  input               io_pop_ptr_restore_valid,
  input      [5:0]    io_pop_ptr_restore_payload,
  output reg [5:0]    occupancy,
  input               clk,
  input               resetn
);

  wire       [1:0]    tmp_inc_num_1;
  wire       [0:0]    tmp_inc_num_2;
  wire       [1:0]    tmp_dec_num_1;
  wire       [0:0]    tmp_dec_num_2;
  wire       [5:0]    tmp_io_push_0_ready;
  wire       [5:0]    tmp_io_push_1_ready;
  wire       [6:0]    tmp_pushPtrs_inc_0;
  wire       [0:0]    tmp_pushPtrs_inc_0_1;
  wire       [6:0]    tmp_pushPtrs_inc_1;
  wire       [1:0]    tmp_pushPtrs_inc_1_1;
  wire       [6:0]    tmp_popPtrs_inc_0;
  wire       [0:0]    tmp_popPtrs_inc_0_1;
  wire       [6:0]    tmp_popPtrs_inc_1;
  wire       [1:0]    tmp_popPtrs_inc_1_1;
  reg        [5:0]    tmp_readedVec_0;
  wire       [5:0]    tmp_readedVec_0_1;
  reg        [5:0]    tmp_readedVec_1;
  wire       [5:0]    tmp_readedVec_1_1;
  wire       [6:0]    tmp_pushPtrGlobalInc;
  wire       [2:0]    tmp_pushPtrGlobalInc_1;
  wire       [6:0]    tmp_popPtrGlobalInc;
  wire       [2:0]    tmp_popPtrGlobalInc_1;
  wire       [6:0]    tmp_pushPtrGlobal;
  wire       [6:0]    tmp_popPtrGlobal;
  wire       [5:0]    tmp_occupancy;
  reg        [5:0]    vec_0;
  reg        [5:0]    vec_1;
  reg        [5:0]    vec_2;
  reg        [5:0]    vec_3;
  reg        [5:0]    vec_4;
  reg        [5:0]    vec_5;
  reg        [5:0]    vec_6;
  reg        [5:0]    vec_7;
  reg        [5:0]    vec_8;
  reg        [5:0]    vec_9;
  reg        [5:0]    vec_10;
  reg        [5:0]    vec_11;
  reg        [5:0]    vec_12;
  reg        [5:0]    vec_13;
  reg        [5:0]    vec_14;
  reg        [5:0]    vec_15;
  reg        [5:0]    vec_16;
  reg        [5:0]    vec_17;
  reg        [5:0]    vec_18;
  reg        [5:0]    vec_19;
  reg        [5:0]    vec_20;
  reg        [5:0]    vec_21;
  reg        [5:0]    vec_22;
  reg        [5:0]    vec_23;
  reg        [5:0]    vec_24;
  reg        [5:0]    vec_25;
  reg        [5:0]    vec_26;
  reg        [5:0]    vec_27;
  reg        [5:0]    vec_28;
  reg        [5:0]    vec_29;
  reg        [5:0]    vec_30;
  reg        [5:0]    vec_31;
  reg        [5:0]    vec_32;
  reg        [5:0]    vec_33;
  reg        [5:0]    vec_34;
  reg        [5:0]    vec_35;
  reg        [5:0]    vec_36;
  reg        [5:0]    vec_37;
  reg        [5:0]    vec_38;
  reg        [5:0]    vec_39;
  reg        [5:0]    vec_40;
  reg        [5:0]    vec_41;
  reg        [5:0]    vec_42;
  reg        [5:0]    vec_43;
  reg        [5:0]    vec_44;
  reg        [5:0]    vec_45;
  reg        [5:0]    vec_46;
  reg        [5:0]    vec_47;
  reg        [5:0]    vec_48;
  reg        [5:0]    vec_49;
  reg        [5:0]    vec_50;
  reg        [5:0]    vec_51;
  reg        [5:0]    vec_52;
  reg        [5:0]    vec_53;
  reg        [5:0]    vec_54;
  reg        [5:0]    vec_55;
  reg        [5:0]    vec_56;
  reg        [5:0]    vec_57;
  reg        [5:0]    vec_58;
  reg        [5:0]    vec_59;
  reg        [5:0]    vec_60;
  reg        [5:0]    vec_61;
  reg        [5:0]    vec_62;
  reg        [5:0]    pushPtrGlobal;
  reg        [5:0]    popPtrGlobal;
  wire                ptrMatch;
  reg                 risingOccupancy;
  wire                empty;
  wire                full;
  wire                pushing_vec_0;
  wire                pushing_vec_1;
  wire                popping_vec_0;
  wire                popping_vec_1;
  wire                tmp_inc_num;
  wire       [1:0]    inc_num;
  wire                tmp_dec_num;
  wire       [1:0]    dec_num;
  reg        [6:0]    pushPtrs_0;
  reg        [6:0]    pushPtrs_1;
  reg        [6:0]    popPtrs_0;
  reg        [6:0]    popPtrs_1;
  wire       [6:0]    pushPtrs_inc_0;
  wire       [6:0]    pushPtrs_inc_1;
  wire       [6:0]    popPtrs_inc_0;
  wire       [6:0]    popPtrs_inc_1;
  wire       [5:0]    readedVec_0;
  wire       [5:0]    readedVec_1;
  wire       [63:0]   tmp_1;
  wire       [63:0]   tmp_2;
  wire       [6:0]    pushPtrGlobalInc;
  wire       [6:0]    popPtrGlobalInc;
  wire       [5:0]    ptrDif;

  assign tmp_inc_num_2 = (pushing_vec_1 == tmp_inc_num);
  assign tmp_inc_num_1 = {1'd0, tmp_inc_num_2};
  assign tmp_dec_num_2 = (popping_vec_1 == tmp_dec_num);
  assign tmp_dec_num_1 = {1'd0, tmp_dec_num_2};
  assign tmp_io_push_0_ready = (6'h3f - occupancy);
  assign tmp_io_push_1_ready = (6'h3f - occupancy);
  assign tmp_pushPtrs_inc_0_1 = 1'b0;
  assign tmp_pushPtrs_inc_0 = {6'd0, tmp_pushPtrs_inc_0_1};
  assign tmp_pushPtrs_inc_1_1 = {1'b0,1'b1};
  assign tmp_pushPtrs_inc_1 = {5'd0, tmp_pushPtrs_inc_1_1};
  assign tmp_popPtrs_inc_0_1 = 1'b0;
  assign tmp_popPtrs_inc_0 = {6'd0, tmp_popPtrs_inc_0_1};
  assign tmp_popPtrs_inc_1_1 = {1'b0,1'b1};
  assign tmp_popPtrs_inc_1 = {5'd0, tmp_popPtrs_inc_1_1};
  assign tmp_pushPtrGlobalInc_1 = {1'b0,inc_num};
  assign tmp_pushPtrGlobalInc = {4'd0, tmp_pushPtrGlobalInc_1};
  assign tmp_popPtrGlobalInc_1 = {1'b0,dec_num};
  assign tmp_popPtrGlobalInc = {4'd0, tmp_popPtrGlobalInc_1};
  assign tmp_pushPtrGlobal = (pushPtrGlobalInc - 7'h3f);
  assign tmp_popPtrGlobal = (popPtrGlobalInc - 7'h3f);
  assign tmp_occupancy = (6'h3f + ptrDif);
  assign tmp_readedVec_0_1 = popPtrs_0[5 : 0];
  assign tmp_readedVec_1_1 = popPtrs_1[5 : 0];
  always @(*) begin
    case(tmp_readedVec_0_1)
      6'b000000 : tmp_readedVec_0 = vec_0;
      6'b000001 : tmp_readedVec_0 = vec_1;
      6'b000010 : tmp_readedVec_0 = vec_2;
      6'b000011 : tmp_readedVec_0 = vec_3;
      6'b000100 : tmp_readedVec_0 = vec_4;
      6'b000101 : tmp_readedVec_0 = vec_5;
      6'b000110 : tmp_readedVec_0 = vec_6;
      6'b000111 : tmp_readedVec_0 = vec_7;
      6'b001000 : tmp_readedVec_0 = vec_8;
      6'b001001 : tmp_readedVec_0 = vec_9;
      6'b001010 : tmp_readedVec_0 = vec_10;
      6'b001011 : tmp_readedVec_0 = vec_11;
      6'b001100 : tmp_readedVec_0 = vec_12;
      6'b001101 : tmp_readedVec_0 = vec_13;
      6'b001110 : tmp_readedVec_0 = vec_14;
      6'b001111 : tmp_readedVec_0 = vec_15;
      6'b010000 : tmp_readedVec_0 = vec_16;
      6'b010001 : tmp_readedVec_0 = vec_17;
      6'b010010 : tmp_readedVec_0 = vec_18;
      6'b010011 : tmp_readedVec_0 = vec_19;
      6'b010100 : tmp_readedVec_0 = vec_20;
      6'b010101 : tmp_readedVec_0 = vec_21;
      6'b010110 : tmp_readedVec_0 = vec_22;
      6'b010111 : tmp_readedVec_0 = vec_23;
      6'b011000 : tmp_readedVec_0 = vec_24;
      6'b011001 : tmp_readedVec_0 = vec_25;
      6'b011010 : tmp_readedVec_0 = vec_26;
      6'b011011 : tmp_readedVec_0 = vec_27;
      6'b011100 : tmp_readedVec_0 = vec_28;
      6'b011101 : tmp_readedVec_0 = vec_29;
      6'b011110 : tmp_readedVec_0 = vec_30;
      6'b011111 : tmp_readedVec_0 = vec_31;
      6'b100000 : tmp_readedVec_0 = vec_32;
      6'b100001 : tmp_readedVec_0 = vec_33;
      6'b100010 : tmp_readedVec_0 = vec_34;
      6'b100011 : tmp_readedVec_0 = vec_35;
      6'b100100 : tmp_readedVec_0 = vec_36;
      6'b100101 : tmp_readedVec_0 = vec_37;
      6'b100110 : tmp_readedVec_0 = vec_38;
      6'b100111 : tmp_readedVec_0 = vec_39;
      6'b101000 : tmp_readedVec_0 = vec_40;
      6'b101001 : tmp_readedVec_0 = vec_41;
      6'b101010 : tmp_readedVec_0 = vec_42;
      6'b101011 : tmp_readedVec_0 = vec_43;
      6'b101100 : tmp_readedVec_0 = vec_44;
      6'b101101 : tmp_readedVec_0 = vec_45;
      6'b101110 : tmp_readedVec_0 = vec_46;
      6'b101111 : tmp_readedVec_0 = vec_47;
      6'b110000 : tmp_readedVec_0 = vec_48;
      6'b110001 : tmp_readedVec_0 = vec_49;
      6'b110010 : tmp_readedVec_0 = vec_50;
      6'b110011 : tmp_readedVec_0 = vec_51;
      6'b110100 : tmp_readedVec_0 = vec_52;
      6'b110101 : tmp_readedVec_0 = vec_53;
      6'b110110 : tmp_readedVec_0 = vec_54;
      6'b110111 : tmp_readedVec_0 = vec_55;
      6'b111000 : tmp_readedVec_0 = vec_56;
      6'b111001 : tmp_readedVec_0 = vec_57;
      6'b111010 : tmp_readedVec_0 = vec_58;
      6'b111011 : tmp_readedVec_0 = vec_59;
      6'b111100 : tmp_readedVec_0 = vec_60;
      6'b111101 : tmp_readedVec_0 = vec_61;
      default : tmp_readedVec_0 = vec_62;
    endcase
  end

  always @(*) begin
    case(tmp_readedVec_1_1)
      6'b000000 : tmp_readedVec_1 = vec_0;
      6'b000001 : tmp_readedVec_1 = vec_1;
      6'b000010 : tmp_readedVec_1 = vec_2;
      6'b000011 : tmp_readedVec_1 = vec_3;
      6'b000100 : tmp_readedVec_1 = vec_4;
      6'b000101 : tmp_readedVec_1 = vec_5;
      6'b000110 : tmp_readedVec_1 = vec_6;
      6'b000111 : tmp_readedVec_1 = vec_7;
      6'b001000 : tmp_readedVec_1 = vec_8;
      6'b001001 : tmp_readedVec_1 = vec_9;
      6'b001010 : tmp_readedVec_1 = vec_10;
      6'b001011 : tmp_readedVec_1 = vec_11;
      6'b001100 : tmp_readedVec_1 = vec_12;
      6'b001101 : tmp_readedVec_1 = vec_13;
      6'b001110 : tmp_readedVec_1 = vec_14;
      6'b001111 : tmp_readedVec_1 = vec_15;
      6'b010000 : tmp_readedVec_1 = vec_16;
      6'b010001 : tmp_readedVec_1 = vec_17;
      6'b010010 : tmp_readedVec_1 = vec_18;
      6'b010011 : tmp_readedVec_1 = vec_19;
      6'b010100 : tmp_readedVec_1 = vec_20;
      6'b010101 : tmp_readedVec_1 = vec_21;
      6'b010110 : tmp_readedVec_1 = vec_22;
      6'b010111 : tmp_readedVec_1 = vec_23;
      6'b011000 : tmp_readedVec_1 = vec_24;
      6'b011001 : tmp_readedVec_1 = vec_25;
      6'b011010 : tmp_readedVec_1 = vec_26;
      6'b011011 : tmp_readedVec_1 = vec_27;
      6'b011100 : tmp_readedVec_1 = vec_28;
      6'b011101 : tmp_readedVec_1 = vec_29;
      6'b011110 : tmp_readedVec_1 = vec_30;
      6'b011111 : tmp_readedVec_1 = vec_31;
      6'b100000 : tmp_readedVec_1 = vec_32;
      6'b100001 : tmp_readedVec_1 = vec_33;
      6'b100010 : tmp_readedVec_1 = vec_34;
      6'b100011 : tmp_readedVec_1 = vec_35;
      6'b100100 : tmp_readedVec_1 = vec_36;
      6'b100101 : tmp_readedVec_1 = vec_37;
      6'b100110 : tmp_readedVec_1 = vec_38;
      6'b100111 : tmp_readedVec_1 = vec_39;
      6'b101000 : tmp_readedVec_1 = vec_40;
      6'b101001 : tmp_readedVec_1 = vec_41;
      6'b101010 : tmp_readedVec_1 = vec_42;
      6'b101011 : tmp_readedVec_1 = vec_43;
      6'b101100 : tmp_readedVec_1 = vec_44;
      6'b101101 : tmp_readedVec_1 = vec_45;
      6'b101110 : tmp_readedVec_1 = vec_46;
      6'b101111 : tmp_readedVec_1 = vec_47;
      6'b110000 : tmp_readedVec_1 = vec_48;
      6'b110001 : tmp_readedVec_1 = vec_49;
      6'b110010 : tmp_readedVec_1 = vec_50;
      6'b110011 : tmp_readedVec_1 = vec_51;
      6'b110100 : tmp_readedVec_1 = vec_52;
      6'b110101 : tmp_readedVec_1 = vec_53;
      6'b110110 : tmp_readedVec_1 = vec_54;
      6'b110111 : tmp_readedVec_1 = vec_55;
      6'b111000 : tmp_readedVec_1 = vec_56;
      6'b111001 : tmp_readedVec_1 = vec_57;
      6'b111010 : tmp_readedVec_1 = vec_58;
      6'b111011 : tmp_readedVec_1 = vec_59;
      6'b111100 : tmp_readedVec_1 = vec_60;
      6'b111101 : tmp_readedVec_1 = vec_61;
      default : tmp_readedVec_1 = vec_62;
    endcase
  end

  assign ptrMatch = (pushPtrGlobal == popPtrGlobal);
  assign empty = (ptrMatch && (! risingOccupancy));
  assign full = (ptrMatch && risingOccupancy);
  assign pushing_vec_0 = (io_push_0_valid && io_push_0_ready);
  assign pushing_vec_1 = (io_push_1_valid && io_push_1_ready);
  assign popping_vec_0 = (io_pop_0_valid && io_pop_0_ready);
  assign popping_vec_1 = (io_pop_1_valid && io_pop_1_ready);
  assign tmp_inc_num = 1'b1;
  assign inc_num = ({1'b0,(pushing_vec_0 == tmp_inc_num)} + tmp_inc_num_1);
  assign tmp_dec_num = 1'b1;
  assign dec_num = ({1'b0,(popping_vec_0 == tmp_dec_num)} + tmp_dec_num_1);
  assign io_push_0_ready = (6'h00 < tmp_io_push_0_ready);
  assign io_push_1_ready = (6'h01 < tmp_io_push_1_ready);
  assign pushPtrs_inc_0 = ({1'b0,pushPtrGlobal} + tmp_pushPtrs_inc_0);
  always @(*) begin
    if((7'h3f <= pushPtrs_inc_0)) begin
      pushPtrs_0 = (pushPtrs_inc_0 - 7'h3f);
    end else begin
      pushPtrs_0 = pushPtrs_inc_0;
    end
  end

  assign pushPtrs_inc_1 = ({1'b0,pushPtrGlobal} + tmp_pushPtrs_inc_1);
  always @(*) begin
    if((7'h3f <= pushPtrs_inc_1)) begin
      pushPtrs_1 = (pushPtrs_inc_1 - 7'h3f);
    end else begin
      pushPtrs_1 = pushPtrs_inc_1;
    end
  end

  assign popPtrs_inc_0 = ({1'b0,popPtrGlobal} + tmp_popPtrs_inc_0);
  always @(*) begin
    if((7'h3f <= popPtrs_inc_0)) begin
      popPtrs_0 = (popPtrs_inc_0 - 7'h3f);
    end else begin
      popPtrs_0 = popPtrs_inc_0;
    end
  end

  assign io_pop_ptr_store_0 = popPtrs_0[5 : 0];
  assign popPtrs_inc_1 = ({1'b0,popPtrGlobal} + tmp_popPtrs_inc_1);
  always @(*) begin
    if((7'h3f <= popPtrs_inc_1)) begin
      popPtrs_1 = (popPtrs_inc_1 - 7'h3f);
    end else begin
      popPtrs_1 = popPtrs_inc_1;
    end
  end

  assign io_pop_ptr_store_1 = popPtrs_1[5 : 0];
  assign readedVec_0 = tmp_readedVec_0;
  assign readedVec_1 = tmp_readedVec_1;
  assign io_pop_0_valid = (6'h00 < occupancy);
  assign io_pop_0_payload = readedVec_0;
  assign io_pop_1_valid = (6'h01 < occupancy);
  assign io_pop_1_payload = readedVec_1;
  assign tmp_1 = ({63'd0,1'b1} <<< pushPtrs_0[5 : 0]);
  assign tmp_2 = ({63'd0,1'b1} <<< pushPtrs_1[5 : 0]);
  assign pushPtrGlobalInc = ({1'b0,pushPtrGlobal} + tmp_pushPtrGlobalInc);
  assign popPtrGlobalInc = ({1'b0,popPtrGlobal} + tmp_popPtrGlobalInc);
  assign ptrDif = (pushPtrGlobal - popPtrGlobal);
  always @(*) begin
    if(ptrMatch) begin
      occupancy = (risingOccupancy ? 6'h3f : 6'h00);
    end else begin
      occupancy = ((popPtrGlobal < pushPtrGlobal) ? ptrDif : tmp_occupancy);
    end
  end

  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      pushPtrGlobal <= 6'h3f;
      popPtrGlobal <= 6'h00;
      risingOccupancy <= 1'b0;
      vec_0 <= 6'h01;
      vec_1 <= 6'h02;
      vec_2 <= 6'h03;
      vec_3 <= 6'h04;
      vec_4 <= 6'h05;
      vec_5 <= 6'h06;
      vec_6 <= 6'h07;
      vec_7 <= 6'h08;
      vec_8 <= 6'h09;
      vec_9 <= 6'h0a;
      vec_10 <= 6'h0b;
      vec_11 <= 6'h0c;
      vec_12 <= 6'h0d;
      vec_13 <= 6'h0e;
      vec_14 <= 6'h0f;
      vec_15 <= 6'h10;
      vec_16 <= 6'h11;
      vec_17 <= 6'h12;
      vec_18 <= 6'h13;
      vec_19 <= 6'h14;
      vec_20 <= 6'h15;
      vec_21 <= 6'h16;
      vec_22 <= 6'h17;
      vec_23 <= 6'h18;
      vec_24 <= 6'h19;
      vec_25 <= 6'h1a;
      vec_26 <= 6'h1b;
      vec_27 <= 6'h1c;
      vec_28 <= 6'h1d;
      vec_29 <= 6'h1e;
      vec_30 <= 6'h1f;
      vec_31 <= 6'h20;
      vec_32 <= 6'h21;
      vec_33 <= 6'h22;
      vec_34 <= 6'h23;
      vec_35 <= 6'h24;
      vec_36 <= 6'h25;
      vec_37 <= 6'h26;
      vec_38 <= 6'h27;
      vec_39 <= 6'h28;
      vec_40 <= 6'h29;
      vec_41 <= 6'h2a;
      vec_42 <= 6'h2b;
      vec_43 <= 6'h2c;
      vec_44 <= 6'h2d;
      vec_45 <= 6'h2e;
      vec_46 <= 6'h2f;
      vec_47 <= 6'h30;
      vec_48 <= 6'h31;
      vec_49 <= 6'h32;
      vec_50 <= 6'h33;
      vec_51 <= 6'h34;
      vec_52 <= 6'h35;
      vec_53 <= 6'h36;
      vec_54 <= 6'h37;
      vec_55 <= 6'h38;
      vec_56 <= 6'h39;
      vec_57 <= 6'h3a;
      vec_58 <= 6'h3b;
      vec_59 <= 6'h3c;
      vec_60 <= 6'h3d;
      vec_61 <= 6'h3e;
      vec_62 <= 6'h3f;
    end else begin
      if(io_flush) begin
        vec_0 <= 6'h01;
      end
      if(io_flush) begin
        vec_1 <= 6'h02;
      end
      if(io_flush) begin
        vec_2 <= 6'h03;
      end
      if(io_flush) begin
        vec_3 <= 6'h04;
      end
      if(io_flush) begin
        vec_4 <= 6'h05;
      end
      if(io_flush) begin
        vec_5 <= 6'h06;
      end
      if(io_flush) begin
        vec_6 <= 6'h07;
      end
      if(io_flush) begin
        vec_7 <= 6'h08;
      end
      if(io_flush) begin
        vec_8 <= 6'h09;
      end
      if(io_flush) begin
        vec_9 <= 6'h0a;
      end
      if(io_flush) begin
        vec_10 <= 6'h0b;
      end
      if(io_flush) begin
        vec_11 <= 6'h0c;
      end
      if(io_flush) begin
        vec_12 <= 6'h0d;
      end
      if(io_flush) begin
        vec_13 <= 6'h0e;
      end
      if(io_flush) begin
        vec_14 <= 6'h0f;
      end
      if(io_flush) begin
        vec_15 <= 6'h10;
      end
      if(io_flush) begin
        vec_16 <= 6'h11;
      end
      if(io_flush) begin
        vec_17 <= 6'h12;
      end
      if(io_flush) begin
        vec_18 <= 6'h13;
      end
      if(io_flush) begin
        vec_19 <= 6'h14;
      end
      if(io_flush) begin
        vec_20 <= 6'h15;
      end
      if(io_flush) begin
        vec_21 <= 6'h16;
      end
      if(io_flush) begin
        vec_22 <= 6'h17;
      end
      if(io_flush) begin
        vec_23 <= 6'h18;
      end
      if(io_flush) begin
        vec_24 <= 6'h19;
      end
      if(io_flush) begin
        vec_25 <= 6'h1a;
      end
      if(io_flush) begin
        vec_26 <= 6'h1b;
      end
      if(io_flush) begin
        vec_27 <= 6'h1c;
      end
      if(io_flush) begin
        vec_28 <= 6'h1d;
      end
      if(io_flush) begin
        vec_29 <= 6'h1e;
      end
      if(io_flush) begin
        vec_30 <= 6'h1f;
      end
      if(io_flush) begin
        vec_31 <= 6'h20;
      end
      if(io_flush) begin
        vec_32 <= 6'h21;
      end
      if(io_flush) begin
        vec_33 <= 6'h22;
      end
      if(io_flush) begin
        vec_34 <= 6'h23;
      end
      if(io_flush) begin
        vec_35 <= 6'h24;
      end
      if(io_flush) begin
        vec_36 <= 6'h25;
      end
      if(io_flush) begin
        vec_37 <= 6'h26;
      end
      if(io_flush) begin
        vec_38 <= 6'h27;
      end
      if(io_flush) begin
        vec_39 <= 6'h28;
      end
      if(io_flush) begin
        vec_40 <= 6'h29;
      end
      if(io_flush) begin
        vec_41 <= 6'h2a;
      end
      if(io_flush) begin
        vec_42 <= 6'h2b;
      end
      if(io_flush) begin
        vec_43 <= 6'h2c;
      end
      if(io_flush) begin
        vec_44 <= 6'h2d;
      end
      if(io_flush) begin
        vec_45 <= 6'h2e;
      end
      if(io_flush) begin
        vec_46 <= 6'h2f;
      end
      if(io_flush) begin
        vec_47 <= 6'h30;
      end
      if(io_flush) begin
        vec_48 <= 6'h31;
      end
      if(io_flush) begin
        vec_49 <= 6'h32;
      end
      if(io_flush) begin
        vec_50 <= 6'h33;
      end
      if(io_flush) begin
        vec_51 <= 6'h34;
      end
      if(io_flush) begin
        vec_52 <= 6'h35;
      end
      if(io_flush) begin
        vec_53 <= 6'h36;
      end
      if(io_flush) begin
        vec_54 <= 6'h37;
      end
      if(io_flush) begin
        vec_55 <= 6'h38;
      end
      if(io_flush) begin
        vec_56 <= 6'h39;
      end
      if(io_flush) begin
        vec_57 <= 6'h3a;
      end
      if(io_flush) begin
        vec_58 <= 6'h3b;
      end
      if(io_flush) begin
        vec_59 <= 6'h3c;
      end
      if(io_flush) begin
        vec_60 <= 6'h3d;
      end
      if(io_flush) begin
        vec_61 <= 6'h3e;
      end
      if(io_flush) begin
        vec_62 <= 6'h3f;
      end
      if((dec_num < inc_num)) begin
        risingOccupancy <= 1'b1;
      end else begin
        risingOccupancy <= 1'b0;
      end
      if(pushing_vec_0) begin
        if(tmp_1[0]) begin
          vec_0 <= io_push_0_payload;
        end
        if(tmp_1[1]) begin
          vec_1 <= io_push_0_payload;
        end
        if(tmp_1[2]) begin
          vec_2 <= io_push_0_payload;
        end
        if(tmp_1[3]) begin
          vec_3 <= io_push_0_payload;
        end
        if(tmp_1[4]) begin
          vec_4 <= io_push_0_payload;
        end
        if(tmp_1[5]) begin
          vec_5 <= io_push_0_payload;
        end
        if(tmp_1[6]) begin
          vec_6 <= io_push_0_payload;
        end
        if(tmp_1[7]) begin
          vec_7 <= io_push_0_payload;
        end
        if(tmp_1[8]) begin
          vec_8 <= io_push_0_payload;
        end
        if(tmp_1[9]) begin
          vec_9 <= io_push_0_payload;
        end
        if(tmp_1[10]) begin
          vec_10 <= io_push_0_payload;
        end
        if(tmp_1[11]) begin
          vec_11 <= io_push_0_payload;
        end
        if(tmp_1[12]) begin
          vec_12 <= io_push_0_payload;
        end
        if(tmp_1[13]) begin
          vec_13 <= io_push_0_payload;
        end
        if(tmp_1[14]) begin
          vec_14 <= io_push_0_payload;
        end
        if(tmp_1[15]) begin
          vec_15 <= io_push_0_payload;
        end
        if(tmp_1[16]) begin
          vec_16 <= io_push_0_payload;
        end
        if(tmp_1[17]) begin
          vec_17 <= io_push_0_payload;
        end
        if(tmp_1[18]) begin
          vec_18 <= io_push_0_payload;
        end
        if(tmp_1[19]) begin
          vec_19 <= io_push_0_payload;
        end
        if(tmp_1[20]) begin
          vec_20 <= io_push_0_payload;
        end
        if(tmp_1[21]) begin
          vec_21 <= io_push_0_payload;
        end
        if(tmp_1[22]) begin
          vec_22 <= io_push_0_payload;
        end
        if(tmp_1[23]) begin
          vec_23 <= io_push_0_payload;
        end
        if(tmp_1[24]) begin
          vec_24 <= io_push_0_payload;
        end
        if(tmp_1[25]) begin
          vec_25 <= io_push_0_payload;
        end
        if(tmp_1[26]) begin
          vec_26 <= io_push_0_payload;
        end
        if(tmp_1[27]) begin
          vec_27 <= io_push_0_payload;
        end
        if(tmp_1[28]) begin
          vec_28 <= io_push_0_payload;
        end
        if(tmp_1[29]) begin
          vec_29 <= io_push_0_payload;
        end
        if(tmp_1[30]) begin
          vec_30 <= io_push_0_payload;
        end
        if(tmp_1[31]) begin
          vec_31 <= io_push_0_payload;
        end
        if(tmp_1[32]) begin
          vec_32 <= io_push_0_payload;
        end
        if(tmp_1[33]) begin
          vec_33 <= io_push_0_payload;
        end
        if(tmp_1[34]) begin
          vec_34 <= io_push_0_payload;
        end
        if(tmp_1[35]) begin
          vec_35 <= io_push_0_payload;
        end
        if(tmp_1[36]) begin
          vec_36 <= io_push_0_payload;
        end
        if(tmp_1[37]) begin
          vec_37 <= io_push_0_payload;
        end
        if(tmp_1[38]) begin
          vec_38 <= io_push_0_payload;
        end
        if(tmp_1[39]) begin
          vec_39 <= io_push_0_payload;
        end
        if(tmp_1[40]) begin
          vec_40 <= io_push_0_payload;
        end
        if(tmp_1[41]) begin
          vec_41 <= io_push_0_payload;
        end
        if(tmp_1[42]) begin
          vec_42 <= io_push_0_payload;
        end
        if(tmp_1[43]) begin
          vec_43 <= io_push_0_payload;
        end
        if(tmp_1[44]) begin
          vec_44 <= io_push_0_payload;
        end
        if(tmp_1[45]) begin
          vec_45 <= io_push_0_payload;
        end
        if(tmp_1[46]) begin
          vec_46 <= io_push_0_payload;
        end
        if(tmp_1[47]) begin
          vec_47 <= io_push_0_payload;
        end
        if(tmp_1[48]) begin
          vec_48 <= io_push_0_payload;
        end
        if(tmp_1[49]) begin
          vec_49 <= io_push_0_payload;
        end
        if(tmp_1[50]) begin
          vec_50 <= io_push_0_payload;
        end
        if(tmp_1[51]) begin
          vec_51 <= io_push_0_payload;
        end
        if(tmp_1[52]) begin
          vec_52 <= io_push_0_payload;
        end
        if(tmp_1[53]) begin
          vec_53 <= io_push_0_payload;
        end
        if(tmp_1[54]) begin
          vec_54 <= io_push_0_payload;
        end
        if(tmp_1[55]) begin
          vec_55 <= io_push_0_payload;
        end
        if(tmp_1[56]) begin
          vec_56 <= io_push_0_payload;
        end
        if(tmp_1[57]) begin
          vec_57 <= io_push_0_payload;
        end
        if(tmp_1[58]) begin
          vec_58 <= io_push_0_payload;
        end
        if(tmp_1[59]) begin
          vec_59 <= io_push_0_payload;
        end
        if(tmp_1[60]) begin
          vec_60 <= io_push_0_payload;
        end
        if(tmp_1[61]) begin
          vec_61 <= io_push_0_payload;
        end
        if(tmp_1[62]) begin
          vec_62 <= io_push_0_payload;
        end
      end
      if(pushing_vec_1) begin
        if(tmp_2[0]) begin
          vec_0 <= io_push_1_payload;
        end
        if(tmp_2[1]) begin
          vec_1 <= io_push_1_payload;
        end
        if(tmp_2[2]) begin
          vec_2 <= io_push_1_payload;
        end
        if(tmp_2[3]) begin
          vec_3 <= io_push_1_payload;
        end
        if(tmp_2[4]) begin
          vec_4 <= io_push_1_payload;
        end
        if(tmp_2[5]) begin
          vec_5 <= io_push_1_payload;
        end
        if(tmp_2[6]) begin
          vec_6 <= io_push_1_payload;
        end
        if(tmp_2[7]) begin
          vec_7 <= io_push_1_payload;
        end
        if(tmp_2[8]) begin
          vec_8 <= io_push_1_payload;
        end
        if(tmp_2[9]) begin
          vec_9 <= io_push_1_payload;
        end
        if(tmp_2[10]) begin
          vec_10 <= io_push_1_payload;
        end
        if(tmp_2[11]) begin
          vec_11 <= io_push_1_payload;
        end
        if(tmp_2[12]) begin
          vec_12 <= io_push_1_payload;
        end
        if(tmp_2[13]) begin
          vec_13 <= io_push_1_payload;
        end
        if(tmp_2[14]) begin
          vec_14 <= io_push_1_payload;
        end
        if(tmp_2[15]) begin
          vec_15 <= io_push_1_payload;
        end
        if(tmp_2[16]) begin
          vec_16 <= io_push_1_payload;
        end
        if(tmp_2[17]) begin
          vec_17 <= io_push_1_payload;
        end
        if(tmp_2[18]) begin
          vec_18 <= io_push_1_payload;
        end
        if(tmp_2[19]) begin
          vec_19 <= io_push_1_payload;
        end
        if(tmp_2[20]) begin
          vec_20 <= io_push_1_payload;
        end
        if(tmp_2[21]) begin
          vec_21 <= io_push_1_payload;
        end
        if(tmp_2[22]) begin
          vec_22 <= io_push_1_payload;
        end
        if(tmp_2[23]) begin
          vec_23 <= io_push_1_payload;
        end
        if(tmp_2[24]) begin
          vec_24 <= io_push_1_payload;
        end
        if(tmp_2[25]) begin
          vec_25 <= io_push_1_payload;
        end
        if(tmp_2[26]) begin
          vec_26 <= io_push_1_payload;
        end
        if(tmp_2[27]) begin
          vec_27 <= io_push_1_payload;
        end
        if(tmp_2[28]) begin
          vec_28 <= io_push_1_payload;
        end
        if(tmp_2[29]) begin
          vec_29 <= io_push_1_payload;
        end
        if(tmp_2[30]) begin
          vec_30 <= io_push_1_payload;
        end
        if(tmp_2[31]) begin
          vec_31 <= io_push_1_payload;
        end
        if(tmp_2[32]) begin
          vec_32 <= io_push_1_payload;
        end
        if(tmp_2[33]) begin
          vec_33 <= io_push_1_payload;
        end
        if(tmp_2[34]) begin
          vec_34 <= io_push_1_payload;
        end
        if(tmp_2[35]) begin
          vec_35 <= io_push_1_payload;
        end
        if(tmp_2[36]) begin
          vec_36 <= io_push_1_payload;
        end
        if(tmp_2[37]) begin
          vec_37 <= io_push_1_payload;
        end
        if(tmp_2[38]) begin
          vec_38 <= io_push_1_payload;
        end
        if(tmp_2[39]) begin
          vec_39 <= io_push_1_payload;
        end
        if(tmp_2[40]) begin
          vec_40 <= io_push_1_payload;
        end
        if(tmp_2[41]) begin
          vec_41 <= io_push_1_payload;
        end
        if(tmp_2[42]) begin
          vec_42 <= io_push_1_payload;
        end
        if(tmp_2[43]) begin
          vec_43 <= io_push_1_payload;
        end
        if(tmp_2[44]) begin
          vec_44 <= io_push_1_payload;
        end
        if(tmp_2[45]) begin
          vec_45 <= io_push_1_payload;
        end
        if(tmp_2[46]) begin
          vec_46 <= io_push_1_payload;
        end
        if(tmp_2[47]) begin
          vec_47 <= io_push_1_payload;
        end
        if(tmp_2[48]) begin
          vec_48 <= io_push_1_payload;
        end
        if(tmp_2[49]) begin
          vec_49 <= io_push_1_payload;
        end
        if(tmp_2[50]) begin
          vec_50 <= io_push_1_payload;
        end
        if(tmp_2[51]) begin
          vec_51 <= io_push_1_payload;
        end
        if(tmp_2[52]) begin
          vec_52 <= io_push_1_payload;
        end
        if(tmp_2[53]) begin
          vec_53 <= io_push_1_payload;
        end
        if(tmp_2[54]) begin
          vec_54 <= io_push_1_payload;
        end
        if(tmp_2[55]) begin
          vec_55 <= io_push_1_payload;
        end
        if(tmp_2[56]) begin
          vec_56 <= io_push_1_payload;
        end
        if(tmp_2[57]) begin
          vec_57 <= io_push_1_payload;
        end
        if(tmp_2[58]) begin
          vec_58 <= io_push_1_payload;
        end
        if(tmp_2[59]) begin
          vec_59 <= io_push_1_payload;
        end
        if(tmp_2[60]) begin
          vec_60 <= io_push_1_payload;
        end
        if(tmp_2[61]) begin
          vec_61 <= io_push_1_payload;
        end
        if(tmp_2[62]) begin
          vec_62 <= io_push_1_payload;
        end
      end
      if((inc_num != 2'b00)) begin
        if((7'h3f <= pushPtrGlobalInc)) begin
          pushPtrGlobal <= tmp_pushPtrGlobal[5:0];
        end else begin
          pushPtrGlobal <= pushPtrGlobalInc[5:0];
        end
      end
      if(io_pop_ptr_restore_valid) begin
        popPtrGlobal <= io_pop_ptr_restore_payload;
      end else begin
        if((dec_num != 2'b00)) begin
          if((7'h3f <= popPtrGlobalInc)) begin
            popPtrGlobal <= tmp_popPtrGlobal[5:0];
          end else begin
            popPtrGlobal <= popPtrGlobalInc[5:0];
          end
        end
      end
      if(io_flush) begin
        pushPtrGlobal <= 6'h00;
        popPtrGlobal <= 6'h00;
        risingOccupancy <= 1'b0;
      end
    end
  end


endmodule

module arat (
  input               flush,
  input               rd_write_0_valid,
  input      [4:0]    rd_write_0_payload,
  input               rd_write_1_valid,
  input      [4:0]    rd_write_1_payload,
  input      [5:0]    rd_write_value_0,
  input      [5:0]    rd_write_value_1,
  output     [5:0]    rat_out_0,
  output     [5:0]    rat_out_1,
  output     [5:0]    rat_out_2,
  output     [5:0]    rat_out_3,
  output     [5:0]    rat_out_4,
  output     [5:0]    rat_out_5,
  output     [5:0]    rat_out_6,
  output     [5:0]    rat_out_7,
  output     [5:0]    rat_out_8,
  output     [5:0]    rat_out_9,
  output     [5:0]    rat_out_10,
  output     [5:0]    rat_out_11,
  output     [5:0]    rat_out_12,
  output     [5:0]    rat_out_13,
  output     [5:0]    rat_out_14,
  output     [5:0]    rat_out_15,
  output     [5:0]    rat_out_16,
  output     [5:0]    rat_out_17,
  output     [5:0]    rat_out_18,
  output     [5:0]    rat_out_19,
  output     [5:0]    rat_out_20,
  output     [5:0]    rat_out_21,
  output     [5:0]    rat_out_22,
  output     [5:0]    rat_out_23,
  output     [5:0]    rat_out_24,
  output     [5:0]    rat_out_25,
  output     [5:0]    rat_out_26,
  output     [5:0]    rat_out_27,
  output     [5:0]    rat_out_28,
  output     [5:0]    rat_out_29,
  output     [5:0]    rat_out_30,
  output     [5:0]    rat_out_31,
  input               clk,
  input               resetn
);

  wire       [5:0]    rat_mem_0;
  reg        [5:0]    rat_mem_1;
  reg        [5:0]    rat_mem_2;
  reg        [5:0]    rat_mem_3;
  reg        [5:0]    rat_mem_4;
  reg        [5:0]    rat_mem_5;
  reg        [5:0]    rat_mem_6;
  reg        [5:0]    rat_mem_7;
  reg        [5:0]    rat_mem_8;
  reg        [5:0]    rat_mem_9;
  reg        [5:0]    rat_mem_10;
  reg        [5:0]    rat_mem_11;
  reg        [5:0]    rat_mem_12;
  reg        [5:0]    rat_mem_13;
  reg        [5:0]    rat_mem_14;
  reg        [5:0]    rat_mem_15;
  reg        [5:0]    rat_mem_16;
  reg        [5:0]    rat_mem_17;
  reg        [5:0]    rat_mem_18;
  reg        [5:0]    rat_mem_19;
  reg        [5:0]    rat_mem_20;
  reg        [5:0]    rat_mem_21;
  reg        [5:0]    rat_mem_22;
  reg        [5:0]    rat_mem_23;
  reg        [5:0]    rat_mem_24;
  reg        [5:0]    rat_mem_25;
  reg        [5:0]    rat_mem_26;
  reg        [5:0]    rat_mem_27;
  reg        [5:0]    rat_mem_28;
  reg        [5:0]    rat_mem_29;
  reg        [5:0]    rat_mem_30;
  reg        [5:0]    rat_mem_31;

  assign rat_mem_0 = 6'h00;
  assign rat_out_0 = rat_mem_0;
  assign rat_out_1 = rat_mem_1;
  assign rat_out_2 = rat_mem_2;
  assign rat_out_3 = rat_mem_3;
  assign rat_out_4 = rat_mem_4;
  assign rat_out_5 = rat_mem_5;
  assign rat_out_6 = rat_mem_6;
  assign rat_out_7 = rat_mem_7;
  assign rat_out_8 = rat_mem_8;
  assign rat_out_9 = rat_mem_9;
  assign rat_out_10 = rat_mem_10;
  assign rat_out_11 = rat_mem_11;
  assign rat_out_12 = rat_mem_12;
  assign rat_out_13 = rat_mem_13;
  assign rat_out_14 = rat_mem_14;
  assign rat_out_15 = rat_mem_15;
  assign rat_out_16 = rat_mem_16;
  assign rat_out_17 = rat_mem_17;
  assign rat_out_18 = rat_mem_18;
  assign rat_out_19 = rat_mem_19;
  assign rat_out_20 = rat_mem_20;
  assign rat_out_21 = rat_mem_21;
  assign rat_out_22 = rat_mem_22;
  assign rat_out_23 = rat_mem_23;
  assign rat_out_24 = rat_mem_24;
  assign rat_out_25 = rat_mem_25;
  assign rat_out_26 = rat_mem_26;
  assign rat_out_27 = rat_mem_27;
  assign rat_out_28 = rat_mem_28;
  assign rat_out_29 = rat_mem_29;
  assign rat_out_30 = rat_mem_30;
  assign rat_out_31 = rat_mem_31;
  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      rat_mem_1 <= 6'h00;
      rat_mem_2 <= 6'h00;
      rat_mem_3 <= 6'h00;
      rat_mem_4 <= 6'h00;
      rat_mem_5 <= 6'h00;
      rat_mem_6 <= 6'h00;
      rat_mem_7 <= 6'h00;
      rat_mem_8 <= 6'h00;
      rat_mem_9 <= 6'h00;
      rat_mem_10 <= 6'h00;
      rat_mem_11 <= 6'h00;
      rat_mem_12 <= 6'h00;
      rat_mem_13 <= 6'h00;
      rat_mem_14 <= 6'h00;
      rat_mem_15 <= 6'h00;
      rat_mem_16 <= 6'h00;
      rat_mem_17 <= 6'h00;
      rat_mem_18 <= 6'h00;
      rat_mem_19 <= 6'h00;
      rat_mem_20 <= 6'h00;
      rat_mem_21 <= 6'h00;
      rat_mem_22 <= 6'h00;
      rat_mem_23 <= 6'h00;
      rat_mem_24 <= 6'h00;
      rat_mem_25 <= 6'h00;
      rat_mem_26 <= 6'h00;
      rat_mem_27 <= 6'h00;
      rat_mem_28 <= 6'h00;
      rat_mem_29 <= 6'h00;
      rat_mem_30 <= 6'h00;
      rat_mem_31 <= 6'h00;
    end else begin
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h01)) begin
          rat_mem_1 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h02)) begin
          rat_mem_2 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h03)) begin
          rat_mem_3 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h04)) begin
          rat_mem_4 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h05)) begin
          rat_mem_5 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h06)) begin
          rat_mem_6 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h07)) begin
          rat_mem_7 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h08)) begin
          rat_mem_8 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h09)) begin
          rat_mem_9 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h0a)) begin
          rat_mem_10 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h0b)) begin
          rat_mem_11 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h0c)) begin
          rat_mem_12 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h0d)) begin
          rat_mem_13 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h0e)) begin
          rat_mem_14 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h0f)) begin
          rat_mem_15 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h10)) begin
          rat_mem_16 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h11)) begin
          rat_mem_17 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h12)) begin
          rat_mem_18 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h13)) begin
          rat_mem_19 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h14)) begin
          rat_mem_20 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h15)) begin
          rat_mem_21 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h16)) begin
          rat_mem_22 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h17)) begin
          rat_mem_23 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h18)) begin
          rat_mem_24 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h19)) begin
          rat_mem_25 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h1a)) begin
          rat_mem_26 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h1b)) begin
          rat_mem_27 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h1c)) begin
          rat_mem_28 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h1d)) begin
          rat_mem_29 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h1e)) begin
          rat_mem_30 <= rd_write_value_0;
        end
      end
      if(rd_write_0_valid) begin
        if((rd_write_0_payload == 5'h1f)) begin
          rat_mem_31 <= rd_write_value_0;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h01)) begin
          rat_mem_1 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h02)) begin
          rat_mem_2 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h03)) begin
          rat_mem_3 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h04)) begin
          rat_mem_4 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h05)) begin
          rat_mem_5 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h06)) begin
          rat_mem_6 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h07)) begin
          rat_mem_7 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h08)) begin
          rat_mem_8 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h09)) begin
          rat_mem_9 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h0a)) begin
          rat_mem_10 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h0b)) begin
          rat_mem_11 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h0c)) begin
          rat_mem_12 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h0d)) begin
          rat_mem_13 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h0e)) begin
          rat_mem_14 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h0f)) begin
          rat_mem_15 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h10)) begin
          rat_mem_16 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h11)) begin
          rat_mem_17 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h12)) begin
          rat_mem_18 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h13)) begin
          rat_mem_19 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h14)) begin
          rat_mem_20 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h15)) begin
          rat_mem_21 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h16)) begin
          rat_mem_22 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h17)) begin
          rat_mem_23 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h18)) begin
          rat_mem_24 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h19)) begin
          rat_mem_25 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h1a)) begin
          rat_mem_26 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h1b)) begin
          rat_mem_27 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h1c)) begin
          rat_mem_28 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h1d)) begin
          rat_mem_29 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h1e)) begin
          rat_mem_30 <= rd_write_value_1;
        end
      end
      if(rd_write_1_valid) begin
        if((rd_write_1_payload == 5'h1f)) begin
          rat_mem_31 <= rd_write_value_1;
        end
      end
      if(flush) begin
        rat_mem_1 <= 6'h00;
      end
      if(flush) begin
        rat_mem_2 <= 6'h00;
      end
      if(flush) begin
        rat_mem_3 <= 6'h00;
      end
      if(flush) begin
        rat_mem_4 <= 6'h00;
      end
      if(flush) begin
        rat_mem_5 <= 6'h00;
      end
      if(flush) begin
        rat_mem_6 <= 6'h00;
      end
      if(flush) begin
        rat_mem_7 <= 6'h00;
      end
      if(flush) begin
        rat_mem_8 <= 6'h00;
      end
      if(flush) begin
        rat_mem_9 <= 6'h00;
      end
      if(flush) begin
        rat_mem_10 <= 6'h00;
      end
      if(flush) begin
        rat_mem_11 <= 6'h00;
      end
      if(flush) begin
        rat_mem_12 <= 6'h00;
      end
      if(flush) begin
        rat_mem_13 <= 6'h00;
      end
      if(flush) begin
        rat_mem_14 <= 6'h00;
      end
      if(flush) begin
        rat_mem_15 <= 6'h00;
      end
      if(flush) begin
        rat_mem_16 <= 6'h00;
      end
      if(flush) begin
        rat_mem_17 <= 6'h00;
      end
      if(flush) begin
        rat_mem_18 <= 6'h00;
      end
      if(flush) begin
        rat_mem_19 <= 6'h00;
      end
      if(flush) begin
        rat_mem_20 <= 6'h00;
      end
      if(flush) begin
        rat_mem_21 <= 6'h00;
      end
      if(flush) begin
        rat_mem_22 <= 6'h00;
      end
      if(flush) begin
        rat_mem_23 <= 6'h00;
      end
      if(flush) begin
        rat_mem_24 <= 6'h00;
      end
      if(flush) begin
        rat_mem_25 <= 6'h00;
      end
      if(flush) begin
        rat_mem_26 <= 6'h00;
      end
      if(flush) begin
        rat_mem_27 <= 6'h00;
      end
      if(flush) begin
        rat_mem_28 <= 6'h00;
      end
      if(flush) begin
        rat_mem_29 <= 6'h00;
      end
      if(flush) begin
        rat_mem_30 <= 6'h00;
      end
      if(flush) begin
        rat_mem_31 <= 6'h00;
      end
    end
  end


endmodule

module rat (
  input               flush,
  input               recovery,
  input               rat_if_rs1_read_0_valid,
  input      [4:0]    rat_if_rs1_read_0_payload,
  input               rat_if_rs1_read_1_valid,
  input      [4:0]    rat_if_rs1_read_1_payload,
  input               rat_if_rs2_read_0_valid,
  input      [4:0]    rat_if_rs2_read_0_payload,
  input               rat_if_rs2_read_1_valid,
  input      [4:0]    rat_if_rs2_read_1_payload,
  input               rat_if_rd_read_0_valid,
  input      [4:0]    rat_if_rd_read_0_payload,
  input               rat_if_rd_read_1_valid,
  input      [4:0]    rat_if_rd_read_1_payload,
  input               rat_if_rd_write_0_valid,
  input      [4:0]    rat_if_rd_write_0_payload,
  input               rat_if_rd_write_1_valid,
  input      [4:0]    rat_if_rd_write_1_payload,
  input      [5:0]    rat_if_rd_write_value_0,
  input      [5:0]    rat_if_rd_write_value_1,
  output reg          rat_if_phy_rs1_0_valid,
  output reg [5:0]    rat_if_phy_rs1_0_payload,
  output reg          rat_if_phy_rs1_1_valid,
  output reg [5:0]    rat_if_phy_rs1_1_payload,
  output reg          rat_if_phy_rs2_0_valid,
  output reg [5:0]    rat_if_phy_rs2_0_payload,
  output reg          rat_if_phy_rs2_1_valid,
  output reg [5:0]    rat_if_phy_rs2_1_payload,
  output reg          rat_if_phy_rd_0_valid,
  output reg [5:0]    rat_if_phy_rd_0_payload,
  output reg          rat_if_phy_rd_1_valid,
  output reg [5:0]    rat_if_phy_rd_1_payload,
  input      [5:0]    arat_in_0,
  input      [5:0]    arat_in_1,
  input      [5:0]    arat_in_2,
  input      [5:0]    arat_in_3,
  input      [5:0]    arat_in_4,
  input      [5:0]    arat_in_5,
  input      [5:0]    arat_in_6,
  input      [5:0]    arat_in_7,
  input      [5:0]    arat_in_8,
  input      [5:0]    arat_in_9,
  input      [5:0]    arat_in_10,
  input      [5:0]    arat_in_11,
  input      [5:0]    arat_in_12,
  input      [5:0]    arat_in_13,
  input      [5:0]    arat_in_14,
  input      [5:0]    arat_in_15,
  input      [5:0]    arat_in_16,
  input      [5:0]    arat_in_17,
  input      [5:0]    arat_in_18,
  input      [5:0]    arat_in_19,
  input      [5:0]    arat_in_20,
  input      [5:0]    arat_in_21,
  input      [5:0]    arat_in_22,
  input      [5:0]    arat_in_23,
  input      [5:0]    arat_in_24,
  input      [5:0]    arat_in_25,
  input      [5:0]    arat_in_26,
  input      [5:0]    arat_in_27,
  input      [5:0]    arat_in_28,
  input      [5:0]    arat_in_29,
  input      [5:0]    arat_in_30,
  input      [5:0]    arat_in_31,
  input               clk,
  input               resetn
);

  reg        [5:0]    tmp_rat_if_phy_rs1_0_payload;
  reg        [5:0]    tmp_rat_if_phy_rs2_0_payload;
  reg        [5:0]    tmp_rat_if_phy_rd_0_payload;
  reg        [5:0]    tmp_rat_if_phy_rs1_1_payload;
  reg        [5:0]    tmp_rat_if_phy_rs2_1_payload;
  reg        [5:0]    tmp_rat_if_phy_rd_1_payload;
  wire       [5:0]    rat_mem_0;
  reg        [5:0]    rat_mem_1;
  reg        [5:0]    rat_mem_2;
  reg        [5:0]    rat_mem_3;
  reg        [5:0]    rat_mem_4;
  reg        [5:0]    rat_mem_5;
  reg        [5:0]    rat_mem_6;
  reg        [5:0]    rat_mem_7;
  reg        [5:0]    rat_mem_8;
  reg        [5:0]    rat_mem_9;
  reg        [5:0]    rat_mem_10;
  reg        [5:0]    rat_mem_11;
  reg        [5:0]    rat_mem_12;
  reg        [5:0]    rat_mem_13;
  reg        [5:0]    rat_mem_14;
  reg        [5:0]    rat_mem_15;
  reg        [5:0]    rat_mem_16;
  reg        [5:0]    rat_mem_17;
  reg        [5:0]    rat_mem_18;
  reg        [5:0]    rat_mem_19;
  reg        [5:0]    rat_mem_20;
  reg        [5:0]    rat_mem_21;
  reg        [5:0]    rat_mem_22;
  reg        [5:0]    rat_mem_23;
  reg        [5:0]    rat_mem_24;
  reg        [5:0]    rat_mem_25;
  reg        [5:0]    rat_mem_26;
  reg        [5:0]    rat_mem_27;
  reg        [5:0]    rat_mem_28;
  reg        [5:0]    rat_mem_29;
  reg        [5:0]    rat_mem_30;
  reg        [5:0]    rat_mem_31;

  always @(*) begin
    case(rat_if_rs1_read_0_payload)
      5'b00000 : tmp_rat_if_phy_rs1_0_payload = rat_mem_0;
      5'b00001 : tmp_rat_if_phy_rs1_0_payload = rat_mem_1;
      5'b00010 : tmp_rat_if_phy_rs1_0_payload = rat_mem_2;
      5'b00011 : tmp_rat_if_phy_rs1_0_payload = rat_mem_3;
      5'b00100 : tmp_rat_if_phy_rs1_0_payload = rat_mem_4;
      5'b00101 : tmp_rat_if_phy_rs1_0_payload = rat_mem_5;
      5'b00110 : tmp_rat_if_phy_rs1_0_payload = rat_mem_6;
      5'b00111 : tmp_rat_if_phy_rs1_0_payload = rat_mem_7;
      5'b01000 : tmp_rat_if_phy_rs1_0_payload = rat_mem_8;
      5'b01001 : tmp_rat_if_phy_rs1_0_payload = rat_mem_9;
      5'b01010 : tmp_rat_if_phy_rs1_0_payload = rat_mem_10;
      5'b01011 : tmp_rat_if_phy_rs1_0_payload = rat_mem_11;
      5'b01100 : tmp_rat_if_phy_rs1_0_payload = rat_mem_12;
      5'b01101 : tmp_rat_if_phy_rs1_0_payload = rat_mem_13;
      5'b01110 : tmp_rat_if_phy_rs1_0_payload = rat_mem_14;
      5'b01111 : tmp_rat_if_phy_rs1_0_payload = rat_mem_15;
      5'b10000 : tmp_rat_if_phy_rs1_0_payload = rat_mem_16;
      5'b10001 : tmp_rat_if_phy_rs1_0_payload = rat_mem_17;
      5'b10010 : tmp_rat_if_phy_rs1_0_payload = rat_mem_18;
      5'b10011 : tmp_rat_if_phy_rs1_0_payload = rat_mem_19;
      5'b10100 : tmp_rat_if_phy_rs1_0_payload = rat_mem_20;
      5'b10101 : tmp_rat_if_phy_rs1_0_payload = rat_mem_21;
      5'b10110 : tmp_rat_if_phy_rs1_0_payload = rat_mem_22;
      5'b10111 : tmp_rat_if_phy_rs1_0_payload = rat_mem_23;
      5'b11000 : tmp_rat_if_phy_rs1_0_payload = rat_mem_24;
      5'b11001 : tmp_rat_if_phy_rs1_0_payload = rat_mem_25;
      5'b11010 : tmp_rat_if_phy_rs1_0_payload = rat_mem_26;
      5'b11011 : tmp_rat_if_phy_rs1_0_payload = rat_mem_27;
      5'b11100 : tmp_rat_if_phy_rs1_0_payload = rat_mem_28;
      5'b11101 : tmp_rat_if_phy_rs1_0_payload = rat_mem_29;
      5'b11110 : tmp_rat_if_phy_rs1_0_payload = rat_mem_30;
      default : tmp_rat_if_phy_rs1_0_payload = rat_mem_31;
    endcase
  end

  always @(*) begin
    case(rat_if_rs2_read_0_payload)
      5'b00000 : tmp_rat_if_phy_rs2_0_payload = rat_mem_0;
      5'b00001 : tmp_rat_if_phy_rs2_0_payload = rat_mem_1;
      5'b00010 : tmp_rat_if_phy_rs2_0_payload = rat_mem_2;
      5'b00011 : tmp_rat_if_phy_rs2_0_payload = rat_mem_3;
      5'b00100 : tmp_rat_if_phy_rs2_0_payload = rat_mem_4;
      5'b00101 : tmp_rat_if_phy_rs2_0_payload = rat_mem_5;
      5'b00110 : tmp_rat_if_phy_rs2_0_payload = rat_mem_6;
      5'b00111 : tmp_rat_if_phy_rs2_0_payload = rat_mem_7;
      5'b01000 : tmp_rat_if_phy_rs2_0_payload = rat_mem_8;
      5'b01001 : tmp_rat_if_phy_rs2_0_payload = rat_mem_9;
      5'b01010 : tmp_rat_if_phy_rs2_0_payload = rat_mem_10;
      5'b01011 : tmp_rat_if_phy_rs2_0_payload = rat_mem_11;
      5'b01100 : tmp_rat_if_phy_rs2_0_payload = rat_mem_12;
      5'b01101 : tmp_rat_if_phy_rs2_0_payload = rat_mem_13;
      5'b01110 : tmp_rat_if_phy_rs2_0_payload = rat_mem_14;
      5'b01111 : tmp_rat_if_phy_rs2_0_payload = rat_mem_15;
      5'b10000 : tmp_rat_if_phy_rs2_0_payload = rat_mem_16;
      5'b10001 : tmp_rat_if_phy_rs2_0_payload = rat_mem_17;
      5'b10010 : tmp_rat_if_phy_rs2_0_payload = rat_mem_18;
      5'b10011 : tmp_rat_if_phy_rs2_0_payload = rat_mem_19;
      5'b10100 : tmp_rat_if_phy_rs2_0_payload = rat_mem_20;
      5'b10101 : tmp_rat_if_phy_rs2_0_payload = rat_mem_21;
      5'b10110 : tmp_rat_if_phy_rs2_0_payload = rat_mem_22;
      5'b10111 : tmp_rat_if_phy_rs2_0_payload = rat_mem_23;
      5'b11000 : tmp_rat_if_phy_rs2_0_payload = rat_mem_24;
      5'b11001 : tmp_rat_if_phy_rs2_0_payload = rat_mem_25;
      5'b11010 : tmp_rat_if_phy_rs2_0_payload = rat_mem_26;
      5'b11011 : tmp_rat_if_phy_rs2_0_payload = rat_mem_27;
      5'b11100 : tmp_rat_if_phy_rs2_0_payload = rat_mem_28;
      5'b11101 : tmp_rat_if_phy_rs2_0_payload = rat_mem_29;
      5'b11110 : tmp_rat_if_phy_rs2_0_payload = rat_mem_30;
      default : tmp_rat_if_phy_rs2_0_payload = rat_mem_31;
    endcase
  end

  always @(*) begin
    case(rat_if_rd_read_0_payload)
      5'b00000 : tmp_rat_if_phy_rd_0_payload = rat_mem_0;
      5'b00001 : tmp_rat_if_phy_rd_0_payload = rat_mem_1;
      5'b00010 : tmp_rat_if_phy_rd_0_payload = rat_mem_2;
      5'b00011 : tmp_rat_if_phy_rd_0_payload = rat_mem_3;
      5'b00100 : tmp_rat_if_phy_rd_0_payload = rat_mem_4;
      5'b00101 : tmp_rat_if_phy_rd_0_payload = rat_mem_5;
      5'b00110 : tmp_rat_if_phy_rd_0_payload = rat_mem_6;
      5'b00111 : tmp_rat_if_phy_rd_0_payload = rat_mem_7;
      5'b01000 : tmp_rat_if_phy_rd_0_payload = rat_mem_8;
      5'b01001 : tmp_rat_if_phy_rd_0_payload = rat_mem_9;
      5'b01010 : tmp_rat_if_phy_rd_0_payload = rat_mem_10;
      5'b01011 : tmp_rat_if_phy_rd_0_payload = rat_mem_11;
      5'b01100 : tmp_rat_if_phy_rd_0_payload = rat_mem_12;
      5'b01101 : tmp_rat_if_phy_rd_0_payload = rat_mem_13;
      5'b01110 : tmp_rat_if_phy_rd_0_payload = rat_mem_14;
      5'b01111 : tmp_rat_if_phy_rd_0_payload = rat_mem_15;
      5'b10000 : tmp_rat_if_phy_rd_0_payload = rat_mem_16;
      5'b10001 : tmp_rat_if_phy_rd_0_payload = rat_mem_17;
      5'b10010 : tmp_rat_if_phy_rd_0_payload = rat_mem_18;
      5'b10011 : tmp_rat_if_phy_rd_0_payload = rat_mem_19;
      5'b10100 : tmp_rat_if_phy_rd_0_payload = rat_mem_20;
      5'b10101 : tmp_rat_if_phy_rd_0_payload = rat_mem_21;
      5'b10110 : tmp_rat_if_phy_rd_0_payload = rat_mem_22;
      5'b10111 : tmp_rat_if_phy_rd_0_payload = rat_mem_23;
      5'b11000 : tmp_rat_if_phy_rd_0_payload = rat_mem_24;
      5'b11001 : tmp_rat_if_phy_rd_0_payload = rat_mem_25;
      5'b11010 : tmp_rat_if_phy_rd_0_payload = rat_mem_26;
      5'b11011 : tmp_rat_if_phy_rd_0_payload = rat_mem_27;
      5'b11100 : tmp_rat_if_phy_rd_0_payload = rat_mem_28;
      5'b11101 : tmp_rat_if_phy_rd_0_payload = rat_mem_29;
      5'b11110 : tmp_rat_if_phy_rd_0_payload = rat_mem_30;
      default : tmp_rat_if_phy_rd_0_payload = rat_mem_31;
    endcase
  end

  always @(*) begin
    case(rat_if_rs1_read_1_payload)
      5'b00000 : tmp_rat_if_phy_rs1_1_payload = rat_mem_0;
      5'b00001 : tmp_rat_if_phy_rs1_1_payload = rat_mem_1;
      5'b00010 : tmp_rat_if_phy_rs1_1_payload = rat_mem_2;
      5'b00011 : tmp_rat_if_phy_rs1_1_payload = rat_mem_3;
      5'b00100 : tmp_rat_if_phy_rs1_1_payload = rat_mem_4;
      5'b00101 : tmp_rat_if_phy_rs1_1_payload = rat_mem_5;
      5'b00110 : tmp_rat_if_phy_rs1_1_payload = rat_mem_6;
      5'b00111 : tmp_rat_if_phy_rs1_1_payload = rat_mem_7;
      5'b01000 : tmp_rat_if_phy_rs1_1_payload = rat_mem_8;
      5'b01001 : tmp_rat_if_phy_rs1_1_payload = rat_mem_9;
      5'b01010 : tmp_rat_if_phy_rs1_1_payload = rat_mem_10;
      5'b01011 : tmp_rat_if_phy_rs1_1_payload = rat_mem_11;
      5'b01100 : tmp_rat_if_phy_rs1_1_payload = rat_mem_12;
      5'b01101 : tmp_rat_if_phy_rs1_1_payload = rat_mem_13;
      5'b01110 : tmp_rat_if_phy_rs1_1_payload = rat_mem_14;
      5'b01111 : tmp_rat_if_phy_rs1_1_payload = rat_mem_15;
      5'b10000 : tmp_rat_if_phy_rs1_1_payload = rat_mem_16;
      5'b10001 : tmp_rat_if_phy_rs1_1_payload = rat_mem_17;
      5'b10010 : tmp_rat_if_phy_rs1_1_payload = rat_mem_18;
      5'b10011 : tmp_rat_if_phy_rs1_1_payload = rat_mem_19;
      5'b10100 : tmp_rat_if_phy_rs1_1_payload = rat_mem_20;
      5'b10101 : tmp_rat_if_phy_rs1_1_payload = rat_mem_21;
      5'b10110 : tmp_rat_if_phy_rs1_1_payload = rat_mem_22;
      5'b10111 : tmp_rat_if_phy_rs1_1_payload = rat_mem_23;
      5'b11000 : tmp_rat_if_phy_rs1_1_payload = rat_mem_24;
      5'b11001 : tmp_rat_if_phy_rs1_1_payload = rat_mem_25;
      5'b11010 : tmp_rat_if_phy_rs1_1_payload = rat_mem_26;
      5'b11011 : tmp_rat_if_phy_rs1_1_payload = rat_mem_27;
      5'b11100 : tmp_rat_if_phy_rs1_1_payload = rat_mem_28;
      5'b11101 : tmp_rat_if_phy_rs1_1_payload = rat_mem_29;
      5'b11110 : tmp_rat_if_phy_rs1_1_payload = rat_mem_30;
      default : tmp_rat_if_phy_rs1_1_payload = rat_mem_31;
    endcase
  end

  always @(*) begin
    case(rat_if_rs2_read_1_payload)
      5'b00000 : tmp_rat_if_phy_rs2_1_payload = rat_mem_0;
      5'b00001 : tmp_rat_if_phy_rs2_1_payload = rat_mem_1;
      5'b00010 : tmp_rat_if_phy_rs2_1_payload = rat_mem_2;
      5'b00011 : tmp_rat_if_phy_rs2_1_payload = rat_mem_3;
      5'b00100 : tmp_rat_if_phy_rs2_1_payload = rat_mem_4;
      5'b00101 : tmp_rat_if_phy_rs2_1_payload = rat_mem_5;
      5'b00110 : tmp_rat_if_phy_rs2_1_payload = rat_mem_6;
      5'b00111 : tmp_rat_if_phy_rs2_1_payload = rat_mem_7;
      5'b01000 : tmp_rat_if_phy_rs2_1_payload = rat_mem_8;
      5'b01001 : tmp_rat_if_phy_rs2_1_payload = rat_mem_9;
      5'b01010 : tmp_rat_if_phy_rs2_1_payload = rat_mem_10;
      5'b01011 : tmp_rat_if_phy_rs2_1_payload = rat_mem_11;
      5'b01100 : tmp_rat_if_phy_rs2_1_payload = rat_mem_12;
      5'b01101 : tmp_rat_if_phy_rs2_1_payload = rat_mem_13;
      5'b01110 : tmp_rat_if_phy_rs2_1_payload = rat_mem_14;
      5'b01111 : tmp_rat_if_phy_rs2_1_payload = rat_mem_15;
      5'b10000 : tmp_rat_if_phy_rs2_1_payload = rat_mem_16;
      5'b10001 : tmp_rat_if_phy_rs2_1_payload = rat_mem_17;
      5'b10010 : tmp_rat_if_phy_rs2_1_payload = rat_mem_18;
      5'b10011 : tmp_rat_if_phy_rs2_1_payload = rat_mem_19;
      5'b10100 : tmp_rat_if_phy_rs2_1_payload = rat_mem_20;
      5'b10101 : tmp_rat_if_phy_rs2_1_payload = rat_mem_21;
      5'b10110 : tmp_rat_if_phy_rs2_1_payload = rat_mem_22;
      5'b10111 : tmp_rat_if_phy_rs2_1_payload = rat_mem_23;
      5'b11000 : tmp_rat_if_phy_rs2_1_payload = rat_mem_24;
      5'b11001 : tmp_rat_if_phy_rs2_1_payload = rat_mem_25;
      5'b11010 : tmp_rat_if_phy_rs2_1_payload = rat_mem_26;
      5'b11011 : tmp_rat_if_phy_rs2_1_payload = rat_mem_27;
      5'b11100 : tmp_rat_if_phy_rs2_1_payload = rat_mem_28;
      5'b11101 : tmp_rat_if_phy_rs2_1_payload = rat_mem_29;
      5'b11110 : tmp_rat_if_phy_rs2_1_payload = rat_mem_30;
      default : tmp_rat_if_phy_rs2_1_payload = rat_mem_31;
    endcase
  end

  always @(*) begin
    case(rat_if_rd_read_1_payload)
      5'b00000 : tmp_rat_if_phy_rd_1_payload = rat_mem_0;
      5'b00001 : tmp_rat_if_phy_rd_1_payload = rat_mem_1;
      5'b00010 : tmp_rat_if_phy_rd_1_payload = rat_mem_2;
      5'b00011 : tmp_rat_if_phy_rd_1_payload = rat_mem_3;
      5'b00100 : tmp_rat_if_phy_rd_1_payload = rat_mem_4;
      5'b00101 : tmp_rat_if_phy_rd_1_payload = rat_mem_5;
      5'b00110 : tmp_rat_if_phy_rd_1_payload = rat_mem_6;
      5'b00111 : tmp_rat_if_phy_rd_1_payload = rat_mem_7;
      5'b01000 : tmp_rat_if_phy_rd_1_payload = rat_mem_8;
      5'b01001 : tmp_rat_if_phy_rd_1_payload = rat_mem_9;
      5'b01010 : tmp_rat_if_phy_rd_1_payload = rat_mem_10;
      5'b01011 : tmp_rat_if_phy_rd_1_payload = rat_mem_11;
      5'b01100 : tmp_rat_if_phy_rd_1_payload = rat_mem_12;
      5'b01101 : tmp_rat_if_phy_rd_1_payload = rat_mem_13;
      5'b01110 : tmp_rat_if_phy_rd_1_payload = rat_mem_14;
      5'b01111 : tmp_rat_if_phy_rd_1_payload = rat_mem_15;
      5'b10000 : tmp_rat_if_phy_rd_1_payload = rat_mem_16;
      5'b10001 : tmp_rat_if_phy_rd_1_payload = rat_mem_17;
      5'b10010 : tmp_rat_if_phy_rd_1_payload = rat_mem_18;
      5'b10011 : tmp_rat_if_phy_rd_1_payload = rat_mem_19;
      5'b10100 : tmp_rat_if_phy_rd_1_payload = rat_mem_20;
      5'b10101 : tmp_rat_if_phy_rd_1_payload = rat_mem_21;
      5'b10110 : tmp_rat_if_phy_rd_1_payload = rat_mem_22;
      5'b10111 : tmp_rat_if_phy_rd_1_payload = rat_mem_23;
      5'b11000 : tmp_rat_if_phy_rd_1_payload = rat_mem_24;
      5'b11001 : tmp_rat_if_phy_rd_1_payload = rat_mem_25;
      5'b11010 : tmp_rat_if_phy_rd_1_payload = rat_mem_26;
      5'b11011 : tmp_rat_if_phy_rd_1_payload = rat_mem_27;
      5'b11100 : tmp_rat_if_phy_rd_1_payload = rat_mem_28;
      5'b11101 : tmp_rat_if_phy_rd_1_payload = rat_mem_29;
      5'b11110 : tmp_rat_if_phy_rd_1_payload = rat_mem_30;
      default : tmp_rat_if_phy_rd_1_payload = rat_mem_31;
    endcase
  end

  assign rat_mem_0 = 6'h00;
  always @(*) begin
    rat_if_phy_rs1_0_valid = 1'b0;
    if(rat_if_rs1_read_0_valid) begin
      rat_if_phy_rs1_0_valid = 1'b1;
    end
  end

  always @(*) begin
    rat_if_phy_rs1_0_payload = 6'h00;
    if(rat_if_rs1_read_0_valid) begin
      rat_if_phy_rs1_0_payload = tmp_rat_if_phy_rs1_0_payload;
    end
  end

  always @(*) begin
    rat_if_phy_rs2_0_valid = 1'b0;
    if(rat_if_rs2_read_0_valid) begin
      rat_if_phy_rs2_0_valid = 1'b1;
    end
  end

  always @(*) begin
    rat_if_phy_rs2_0_payload = 6'h00;
    if(rat_if_rs2_read_0_valid) begin
      rat_if_phy_rs2_0_payload = tmp_rat_if_phy_rs2_0_payload;
    end
  end

  always @(*) begin
    rat_if_phy_rd_0_valid = 1'b0;
    if(rat_if_rd_read_0_valid) begin
      rat_if_phy_rd_0_valid = 1'b1;
    end
  end

  always @(*) begin
    rat_if_phy_rd_0_payload = 6'h00;
    if(rat_if_rd_read_0_valid) begin
      rat_if_phy_rd_0_payload = tmp_rat_if_phy_rd_0_payload;
    end
  end

  always @(*) begin
    rat_if_phy_rs1_1_valid = 1'b0;
    if(rat_if_rs1_read_1_valid) begin
      rat_if_phy_rs1_1_valid = 1'b1;
    end
  end

  always @(*) begin
    rat_if_phy_rs1_1_payload = 6'h00;
    if(rat_if_rs1_read_1_valid) begin
      rat_if_phy_rs1_1_payload = tmp_rat_if_phy_rs1_1_payload;
    end
  end

  always @(*) begin
    rat_if_phy_rs2_1_valid = 1'b0;
    if(rat_if_rs2_read_1_valid) begin
      rat_if_phy_rs2_1_valid = 1'b1;
    end
  end

  always @(*) begin
    rat_if_phy_rs2_1_payload = 6'h00;
    if(rat_if_rs2_read_1_valid) begin
      rat_if_phy_rs2_1_payload = tmp_rat_if_phy_rs2_1_payload;
    end
  end

  always @(*) begin
    rat_if_phy_rd_1_valid = 1'b0;
    if(rat_if_rd_read_1_valid) begin
      rat_if_phy_rd_1_valid = 1'b1;
    end
  end

  always @(*) begin
    rat_if_phy_rd_1_payload = 6'h00;
    if(rat_if_rd_read_1_valid) begin
      rat_if_phy_rd_1_payload = tmp_rat_if_phy_rd_1_payload;
    end
  end

  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      rat_mem_1 <= 6'h00;
      rat_mem_2 <= 6'h00;
      rat_mem_3 <= 6'h00;
      rat_mem_4 <= 6'h00;
      rat_mem_5 <= 6'h00;
      rat_mem_6 <= 6'h00;
      rat_mem_7 <= 6'h00;
      rat_mem_8 <= 6'h00;
      rat_mem_9 <= 6'h00;
      rat_mem_10 <= 6'h00;
      rat_mem_11 <= 6'h00;
      rat_mem_12 <= 6'h00;
      rat_mem_13 <= 6'h00;
      rat_mem_14 <= 6'h00;
      rat_mem_15 <= 6'h00;
      rat_mem_16 <= 6'h00;
      rat_mem_17 <= 6'h00;
      rat_mem_18 <= 6'h00;
      rat_mem_19 <= 6'h00;
      rat_mem_20 <= 6'h00;
      rat_mem_21 <= 6'h00;
      rat_mem_22 <= 6'h00;
      rat_mem_23 <= 6'h00;
      rat_mem_24 <= 6'h00;
      rat_mem_25 <= 6'h00;
      rat_mem_26 <= 6'h00;
      rat_mem_27 <= 6'h00;
      rat_mem_28 <= 6'h00;
      rat_mem_29 <= 6'h00;
      rat_mem_30 <= 6'h00;
      rat_mem_31 <= 6'h00;
    end else begin
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h01)) begin
          rat_mem_1 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h02)) begin
          rat_mem_2 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h03)) begin
          rat_mem_3 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h04)) begin
          rat_mem_4 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h05)) begin
          rat_mem_5 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h06)) begin
          rat_mem_6 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h07)) begin
          rat_mem_7 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h08)) begin
          rat_mem_8 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h09)) begin
          rat_mem_9 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h0a)) begin
          rat_mem_10 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h0b)) begin
          rat_mem_11 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h0c)) begin
          rat_mem_12 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h0d)) begin
          rat_mem_13 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h0e)) begin
          rat_mem_14 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h0f)) begin
          rat_mem_15 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h10)) begin
          rat_mem_16 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h11)) begin
          rat_mem_17 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h12)) begin
          rat_mem_18 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h13)) begin
          rat_mem_19 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h14)) begin
          rat_mem_20 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h15)) begin
          rat_mem_21 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h16)) begin
          rat_mem_22 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h17)) begin
          rat_mem_23 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h18)) begin
          rat_mem_24 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h19)) begin
          rat_mem_25 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h1a)) begin
          rat_mem_26 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h1b)) begin
          rat_mem_27 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h1c)) begin
          rat_mem_28 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h1d)) begin
          rat_mem_29 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h1e)) begin
          rat_mem_30 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_0_valid) begin
        if((rat_if_rd_write_0_payload == 5'h1f)) begin
          rat_mem_31 <= rat_if_rd_write_value_0;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h01)) begin
          rat_mem_1 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h02)) begin
          rat_mem_2 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h03)) begin
          rat_mem_3 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h04)) begin
          rat_mem_4 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h05)) begin
          rat_mem_5 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h06)) begin
          rat_mem_6 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h07)) begin
          rat_mem_7 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h08)) begin
          rat_mem_8 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h09)) begin
          rat_mem_9 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h0a)) begin
          rat_mem_10 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h0b)) begin
          rat_mem_11 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h0c)) begin
          rat_mem_12 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h0d)) begin
          rat_mem_13 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h0e)) begin
          rat_mem_14 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h0f)) begin
          rat_mem_15 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h10)) begin
          rat_mem_16 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h11)) begin
          rat_mem_17 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h12)) begin
          rat_mem_18 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h13)) begin
          rat_mem_19 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h14)) begin
          rat_mem_20 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h15)) begin
          rat_mem_21 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h16)) begin
          rat_mem_22 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h17)) begin
          rat_mem_23 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h18)) begin
          rat_mem_24 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h19)) begin
          rat_mem_25 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h1a)) begin
          rat_mem_26 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h1b)) begin
          rat_mem_27 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h1c)) begin
          rat_mem_28 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h1d)) begin
          rat_mem_29 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h1e)) begin
          rat_mem_30 <= rat_if_rd_write_value_1;
        end
      end
      if(rat_if_rd_write_1_valid) begin
        if((rat_if_rd_write_1_payload == 5'h1f)) begin
          rat_mem_31 <= rat_if_rd_write_value_1;
        end
      end
      if(flush) begin
        rat_mem_1 <= 6'h00;
      end
      if(flush) begin
        rat_mem_2 <= 6'h00;
      end
      if(flush) begin
        rat_mem_3 <= 6'h00;
      end
      if(flush) begin
        rat_mem_4 <= 6'h00;
      end
      if(flush) begin
        rat_mem_5 <= 6'h00;
      end
      if(flush) begin
        rat_mem_6 <= 6'h00;
      end
      if(flush) begin
        rat_mem_7 <= 6'h00;
      end
      if(flush) begin
        rat_mem_8 <= 6'h00;
      end
      if(flush) begin
        rat_mem_9 <= 6'h00;
      end
      if(flush) begin
        rat_mem_10 <= 6'h00;
      end
      if(flush) begin
        rat_mem_11 <= 6'h00;
      end
      if(flush) begin
        rat_mem_12 <= 6'h00;
      end
      if(flush) begin
        rat_mem_13 <= 6'h00;
      end
      if(flush) begin
        rat_mem_14 <= 6'h00;
      end
      if(flush) begin
        rat_mem_15 <= 6'h00;
      end
      if(flush) begin
        rat_mem_16 <= 6'h00;
      end
      if(flush) begin
        rat_mem_17 <= 6'h00;
      end
      if(flush) begin
        rat_mem_18 <= 6'h00;
      end
      if(flush) begin
        rat_mem_19 <= 6'h00;
      end
      if(flush) begin
        rat_mem_20 <= 6'h00;
      end
      if(flush) begin
        rat_mem_21 <= 6'h00;
      end
      if(flush) begin
        rat_mem_22 <= 6'h00;
      end
      if(flush) begin
        rat_mem_23 <= 6'h00;
      end
      if(flush) begin
        rat_mem_24 <= 6'h00;
      end
      if(flush) begin
        rat_mem_25 <= 6'h00;
      end
      if(flush) begin
        rat_mem_26 <= 6'h00;
      end
      if(flush) begin
        rat_mem_27 <= 6'h00;
      end
      if(flush) begin
        rat_mem_28 <= 6'h00;
      end
      if(flush) begin
        rat_mem_29 <= 6'h00;
      end
      if(flush) begin
        rat_mem_30 <= 6'h00;
      end
      if(flush) begin
        rat_mem_31 <= 6'h00;
      end
      if(recovery) begin
        rat_mem_1 <= arat_in_1;
      end
      if(recovery) begin
        rat_mem_2 <= arat_in_2;
      end
      if(recovery) begin
        rat_mem_3 <= arat_in_3;
      end
      if(recovery) begin
        rat_mem_4 <= arat_in_4;
      end
      if(recovery) begin
        rat_mem_5 <= arat_in_5;
      end
      if(recovery) begin
        rat_mem_6 <= arat_in_6;
      end
      if(recovery) begin
        rat_mem_7 <= arat_in_7;
      end
      if(recovery) begin
        rat_mem_8 <= arat_in_8;
      end
      if(recovery) begin
        rat_mem_9 <= arat_in_9;
      end
      if(recovery) begin
        rat_mem_10 <= arat_in_10;
      end
      if(recovery) begin
        rat_mem_11 <= arat_in_11;
      end
      if(recovery) begin
        rat_mem_12 <= arat_in_12;
      end
      if(recovery) begin
        rat_mem_13 <= arat_in_13;
      end
      if(recovery) begin
        rat_mem_14 <= arat_in_14;
      end
      if(recovery) begin
        rat_mem_15 <= arat_in_15;
      end
      if(recovery) begin
        rat_mem_16 <= arat_in_16;
      end
      if(recovery) begin
        rat_mem_17 <= arat_in_17;
      end
      if(recovery) begin
        rat_mem_18 <= arat_in_18;
      end
      if(recovery) begin
        rat_mem_19 <= arat_in_19;
      end
      if(recovery) begin
        rat_mem_20 <= arat_in_20;
      end
      if(recovery) begin
        rat_mem_21 <= arat_in_21;
      end
      if(recovery) begin
        rat_mem_22 <= arat_in_22;
      end
      if(recovery) begin
        rat_mem_23 <= arat_in_23;
      end
      if(recovery) begin
        rat_mem_24 <= arat_in_24;
      end
      if(recovery) begin
        rat_mem_25 <= arat_in_25;
      end
      if(recovery) begin
        rat_mem_26 <= arat_in_26;
      end
      if(recovery) begin
        rat_mem_27 <= arat_in_27;
      end
      if(recovery) begin
        rat_mem_28 <= arat_in_28;
      end
      if(recovery) begin
        rat_mem_29 <= arat_in_29;
      end
      if(recovery) begin
        rat_mem_30 <= arat_in_30;
      end
      if(recovery) begin
        rat_mem_31 <= arat_in_31;
      end
    end
  end


endmodule
