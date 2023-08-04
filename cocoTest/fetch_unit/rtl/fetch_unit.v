// Generator : SpinalHDL v1.9.0    git head : 7d30dbacbd3aa1be42fb2a3d4da5675703aae2ae
// Component : fetch_unit
// Git hash  : 19ab183b0375704520f39fb8e2ec4eed64088039

`timescale 1ns/1ps

module fetch_unit (
  input               core_en,
  output              ibus_cmd_valid,
  input               ibus_cmd_ready,
  output reg [31:0]   ibus_cmd_payload_address,
  input               ibus_rsp_valid,
  output              ibus_rsp_ready,
  input      [63:0]   ibus_rsp_payload_rdata,
  output reg [31:0]   if2id_itf_0_instr_data,
  output reg [31:0]   if2id_itf_0_instr_pc,
  output              if2id_itf_0_valid,
  output reg [31:0]   if2id_itf_1_instr_data,
  output reg [31:0]   if2id_itf_1_instr_pc,
  output              if2id_itf_1_valid,
  input               fetchStage_flush,
  input               fetchStage_isDone,
  output              fetchStage_isReady,
  input               pcJumpTarget_valid,
  input      [31:0]   pcJumpTarget_payload,
  input               pcPredicted_valid,
  input      [31:0]   pcPredicted_payload,
  input               clk,
  input               resetn
);
  localparam RVL = 2'd0;
  localparam RVH = 2'd1;
  localparam RVC = 2'd2;
  localparam INVALID = 2'd3;

  wire                addressFifo_io_push_valid;
  reg                 addressFifo_io_pop_ready;
  reg                 addressFifo_io_flush;
  reg                 validNumFifo_push_valid;
  reg        [2:0]    validNumFifo_push_payload;
  reg                 validNumFifo_pop_valid;
  reg        [2:0]    validNumFifo_pop_pop_req_num;
  reg                 validNumFifo_flush;
  reg                 instrFifo_io_push_0_valid;
  reg        [32:0]   instrFifo_io_push_0_payload;
  reg                 instrFifo_io_push_1_valid;
  reg        [32:0]   instrFifo_io_push_1_payload;
  reg                 instrFifo_io_push_2_valid;
  reg        [32:0]   instrFifo_io_push_2_payload;
  reg                 instrFifo_io_push_3_valid;
  reg        [32:0]   instrFifo_io_push_3_payload;
  reg                 instrFifo_io_flush;
  wire       [31:0]   rvcDecompressor_2_i;
  wire       [31:0]   rvcDecompressor_3_i;
  wire                addressFifo_io_push_ready;
  wire                addressFifo_io_pop_valid;
  wire       [31:0]   addressFifo_io_pop_payload;
  wire       [31:0]   addressFifo_io_head;
  wire       [2:0]    addressFifo_io_occupancy;
  wire                validNumFifo_push_ready;
  wire                validNumFifo_pop_ready;
  wire       [2:0]    validNumFifo_pop_pop_out_data;
  wire                validNumFifo_pop_pop_real;
  wire       [2:0]    validNumFifo_head;
  wire       [2:0]    validNumFifo_occupancy;
  wire                instrFifo_io_push_0_ready;
  wire                instrFifo_io_push_1_ready;
  wire                instrFifo_io_push_2_ready;
  wire                instrFifo_io_push_3_ready;
  wire                instrFifo_io_pop_0_valid;
  wire       [32:0]   instrFifo_io_pop_0_payload;
  wire                instrFifo_io_pop_1_valid;
  wire       [32:0]   instrFifo_io_pop_1_payload;
  wire       [3:0]    instrFifo_io_occupancy;
  wire                instrFifo_io_ov_waterline;
  wire       [2:0]    trailone_index_0_cnt_out;
  wire       [2:0]    trailone_index_1_cnt_out;
  wire       [2:0]    trailone_index_2_cnt_out;
  wire       [2:0]    trailone_index_3_cnt_out;
  wire       [2:0]    instr_push_valid_vec_popc_cnt_out;
  wire       [1:0]    instr_pop_valid_popc_cnt_out;
  wire       [31:0]   rvcDecompressor_2_decompInstr_inst;
  wire                rvcDecompressor_2_decompInstr_illegal;
  wire       [31:0]   rvcDecompressor_3_decompInstr_inst;
  wire                rvcDecompressor_3_decompInstr_illegal;
  reg        [32:0]   tmp_io_push_0_payload;
  reg                 tmp_io_push_0_valid;
  reg        [32:0]   tmp_io_push_1_payload;
  reg                 tmp_io_push_1_valid;
  reg        [32:0]   tmp_io_push_2_payload;
  reg                 tmp_io_push_2_valid;
  reg        [32:0]   tmp_io_push_3_payload;
  reg                 tmp_io_push_3_valid;
  wire       [31:0]   tmp_if2id_itf_0_instr_pc;
  wire       [31:0]   tmp_if2id_itf_0_instr_pc_1;
  wire       [31:0]   tmp_if2id_itf_1_instr_pc;
  wire       [31:0]   tmp_if2id_itf_1_instr_pc_1;
  wire       [31:0]   tmp_if2id_itf_1_instr_pc_2;
  reg        [31:0]   pc;
  wire       [31:0]   default_next_pc;
  wire                fetch_pending;
  wire       [1:0]    addressOffset;
  wire                ibus_cmd_fire;
  wire                valid_instr_slice_0;
  wire                valid_instr_slice_1;
  wire                valid_instr_slice_2;
  wire                valid_instr_slice_3;
  reg                 instr_slices_0_valid;
  reg        [32:0]   instr_slices_0_payload;
  reg                 instr_slices_1_valid;
  reg        [32:0]   instr_slices_1_payload;
  reg                 instr_slices_2_valid;
  reg        [32:0]   instr_slices_2_payload;
  reg                 instr_slices_3_valid;
  reg        [32:0]   instr_slices_3_payload;
  wire       [3:0]    tail_one_matrix_0;
  reg        [3:0]    tail_one_matrix_1;
  reg        [3:0]    tail_one_matrix_2;
  reg        [3:0]    tail_one_matrix_3;
  wire       [1:0]    tail_one_index_vec_0;
  wire       [1:0]    tail_one_index_vec_1;
  wire       [1:0]    tail_one_index_vec_2;
  wire       [1:0]    tail_one_index_vec_3;
  wire                tail_one_valid_vec_0;
  wire                tail_one_valid_vec_1;
  wire                tail_one_valid_vec_2;
  wire                tail_one_valid_vec_3;
  wire       [0:0]    slice_valid_vec_0;
  wire       [0:0]    slice_valid_vec_1;
  wire       [0:0]    slice_valid_vec_2;
  wire       [0:0]    slice_valid_vec_3;
  reg                 tmp_1;
  reg        [15:0]   tmp_instr_slices_0_payload;
  reg        [1:0]    tmp_2;
  reg        [1:0]    tmp_3;
  reg        [1:0]    tmp_4;
  reg        [1:0]    tmp_5;
  wire                ibus_rsp_fire;
  reg        [3:0]    instr_push_valid_vec;
  wire                toplevel_instrFifo_io_push_0_fire;
  reg        [1:0]    instr_pop_valid;
  wire                toplevel_instrFifo_io_pop_0_fire;
  wire                toplevel_instrFifo_io_pop_1_fire;
  reg        [2:0]    pc_pop_offset;
  wire       [2:0]    pc_step_all_1;
  wire       [2:0]    pc_step_all_2;
  wire       [31:0]   real_instr_0;
  wire       [31:0]   real_instr_1;
  wire       [2:0]    pc_step_0;
  wire       [2:0]    pc_step_1;
  wire                tmp_real_instr_0;
  wire                tmp_real_instr_1;
  wire                toplevel_addressFifo_io_pop_fire;
  `ifndef SYNTHESIS
  reg [55:0] tmp_2_string;
  reg [55:0] tmp_3_string;
  reg [55:0] tmp_4_string;
  reg [55:0] tmp_5_string;
  `endif


  assign tmp_if2id_itf_0_instr_pc = (addressFifo_io_head + tmp_if2id_itf_0_instr_pc_1);
  assign tmp_if2id_itf_0_instr_pc_1 = {29'd0, pc_pop_offset};
  assign tmp_if2id_itf_1_instr_pc = (addressFifo_io_head + tmp_if2id_itf_1_instr_pc_1);
  assign tmp_if2id_itf_1_instr_pc_1 = {29'd0, pc_pop_offset};
  assign tmp_if2id_itf_1_instr_pc_2 = {29'd0, pc_step_all_1};
  StreamFifoWithHead addressFifo (
    .io_push_valid   (addressFifo_io_push_valid       ), //i
    .io_push_ready   (addressFifo_io_push_ready       ), //o
    .io_push_payload (ibus_cmd_payload_address[31:0]  ), //i
    .io_pop_valid    (addressFifo_io_pop_valid        ), //o
    .io_pop_ready    (addressFifo_io_pop_ready        ), //i
    .io_pop_payload  (addressFifo_io_pop_payload[31:0]), //o
    .io_head         (addressFifo_io_head[31:0]       ), //o
    .io_flush        (addressFifo_io_flush            ), //i
    .io_occupancy    (addressFifo_io_occupancy[2:0]   ), //o
    .clk             (clk                             ), //i
    .resetn          (resetn                          )  //i
  );
  PopToleranceFifo validNumFifo (
    .push_valid       (validNumFifo_push_valid           ), //i
    .push_ready       (validNumFifo_push_ready           ), //o
    .push_payload     (validNumFifo_push_payload[2:0]    ), //i
    .pop_valid        (validNumFifo_pop_valid            ), //i
    .pop_ready        (validNumFifo_pop_ready            ), //o
    .pop_pop_req_num  (validNumFifo_pop_pop_req_num[2:0] ), //i
    .pop_pop_out_data (validNumFifo_pop_pop_out_data[2:0]), //o
    .pop_pop_real     (validNumFifo_pop_pop_real         ), //o
    .head             (validNumFifo_head[2:0]            ), //o
    .flush            (validNumFifo_flush                ), //i
    .occupancy        (validNumFifo_occupancy[2:0]       ), //o
    .clk              (clk                               ), //i
    .resetn           (resetn                            )  //i
  );
  StreamFifo1delayMultiPort instrFifo (
    .io_push_0_valid   (instrFifo_io_push_0_valid        ), //i
    .io_push_0_ready   (instrFifo_io_push_0_ready        ), //o
    .io_push_0_payload (instrFifo_io_push_0_payload[32:0]), //i
    .io_push_1_valid   (instrFifo_io_push_1_valid        ), //i
    .io_push_1_ready   (instrFifo_io_push_1_ready        ), //o
    .io_push_1_payload (instrFifo_io_push_1_payload[32:0]), //i
    .io_push_2_valid   (instrFifo_io_push_2_valid        ), //i
    .io_push_2_ready   (instrFifo_io_push_2_ready        ), //o
    .io_push_2_payload (instrFifo_io_push_2_payload[32:0]), //i
    .io_push_3_valid   (instrFifo_io_push_3_valid        ), //i
    .io_push_3_ready   (instrFifo_io_push_3_ready        ), //o
    .io_push_3_payload (instrFifo_io_push_3_payload[32:0]), //i
    .io_pop_0_valid    (instrFifo_io_pop_0_valid         ), //o
    .io_pop_0_ready    (fetchStage_isDone                ), //i
    .io_pop_0_payload  (instrFifo_io_pop_0_payload[32:0] ), //o
    .io_pop_1_valid    (instrFifo_io_pop_1_valid         ), //o
    .io_pop_1_ready    (fetchStage_isDone                ), //i
    .io_pop_1_payload  (instrFifo_io_pop_1_payload[32:0] ), //o
    .io_flush          (instrFifo_io_flush               ), //i
    .io_occupancy      (instrFifo_io_occupancy[3:0]      ), //o
    .io_ov_waterline   (instrFifo_io_ov_waterline        ), //o
    .clk               (clk                              ), //i
    .resetn            (resetn                           )  //i
  );
  lzc trailone_index_0 (
    .mode    (1'b1                         ), //i
    .lead    (1'b0                         ), //i
    .trail   (1'b1                         ), //i
    .data_in (tail_one_matrix_0[3:0]       ), //i
    .cnt_out (trailone_index_0_cnt_out[2:0])  //o
  );
  lzc trailone_index_1 (
    .mode    (1'b1                         ), //i
    .lead    (1'b0                         ), //i
    .trail   (1'b1                         ), //i
    .data_in (tail_one_matrix_1[3:0]       ), //i
    .cnt_out (trailone_index_1_cnt_out[2:0])  //o
  );
  lzc trailone_index_2 (
    .mode    (1'b1                         ), //i
    .lead    (1'b0                         ), //i
    .trail   (1'b1                         ), //i
    .data_in (tail_one_matrix_2[3:0]       ), //i
    .cnt_out (trailone_index_2_cnt_out[2:0])  //o
  );
  lzc trailone_index_3 (
    .mode    (1'b1                         ), //i
    .lead    (1'b0                         ), //i
    .trail   (1'b1                         ), //i
    .data_in (tail_one_matrix_3[3:0]       ), //i
    .cnt_out (trailone_index_3_cnt_out[2:0])  //o
  );
  popcount instr_push_valid_vec_popc (
    .din_vld (toplevel_instrFifo_io_push_0_fire     ), //i
    .data_in (instr_push_valid_vec[3:0]             ), //i
    .cnt_out (instr_push_valid_vec_popc_cnt_out[2:0])  //o
  );
  popcount_1 instr_pop_valid_popc (
    .din_vld (toplevel_instrFifo_io_pop_0_fire ), //i
    .data_in (instr_pop_valid[1:0]             ), //i
    .cnt_out (instr_pop_valid_popc_cnt_out[1:0])  //o
  );
  RvcDecompressor rvcDecompressor_2 (
    .i                   (rvcDecompressor_2_i[31:0]               ), //i
    .decompInstr_inst    (rvcDecompressor_2_decompInstr_inst[31:0]), //o
    .decompInstr_illegal (rvcDecompressor_2_decompInstr_illegal   )  //o
  );
  RvcDecompressor rvcDecompressor_3 (
    .i                   (rvcDecompressor_3_i[31:0]               ), //i
    .decompInstr_inst    (rvcDecompressor_3_decompInstr_inst[31:0]), //o
    .decompInstr_illegal (rvcDecompressor_3_decompInstr_illegal   )  //o
  );
  always @(*) begin
    case(tail_one_index_vec_0)
      2'b00 : begin
        tmp_io_push_0_payload = instr_slices_0_payload;
        tmp_io_push_0_valid = instr_slices_0_valid;
      end
      2'b01 : begin
        tmp_io_push_0_payload = instr_slices_1_payload;
        tmp_io_push_0_valid = instr_slices_1_valid;
      end
      2'b10 : begin
        tmp_io_push_0_payload = instr_slices_2_payload;
        tmp_io_push_0_valid = instr_slices_2_valid;
      end
      default : begin
        tmp_io_push_0_payload = instr_slices_3_payload;
        tmp_io_push_0_valid = instr_slices_3_valid;
      end
    endcase
  end

  always @(*) begin
    case(tail_one_index_vec_1)
      2'b00 : begin
        tmp_io_push_1_payload = instr_slices_0_payload;
        tmp_io_push_1_valid = instr_slices_0_valid;
      end
      2'b01 : begin
        tmp_io_push_1_payload = instr_slices_1_payload;
        tmp_io_push_1_valid = instr_slices_1_valid;
      end
      2'b10 : begin
        tmp_io_push_1_payload = instr_slices_2_payload;
        tmp_io_push_1_valid = instr_slices_2_valid;
      end
      default : begin
        tmp_io_push_1_payload = instr_slices_3_payload;
        tmp_io_push_1_valid = instr_slices_3_valid;
      end
    endcase
  end

  always @(*) begin
    case(tail_one_index_vec_2)
      2'b00 : begin
        tmp_io_push_2_payload = instr_slices_0_payload;
        tmp_io_push_2_valid = instr_slices_0_valid;
      end
      2'b01 : begin
        tmp_io_push_2_payload = instr_slices_1_payload;
        tmp_io_push_2_valid = instr_slices_1_valid;
      end
      2'b10 : begin
        tmp_io_push_2_payload = instr_slices_2_payload;
        tmp_io_push_2_valid = instr_slices_2_valid;
      end
      default : begin
        tmp_io_push_2_payload = instr_slices_3_payload;
        tmp_io_push_2_valid = instr_slices_3_valid;
      end
    endcase
  end

  always @(*) begin
    case(tail_one_index_vec_3)
      2'b00 : begin
        tmp_io_push_3_payload = instr_slices_0_payload;
        tmp_io_push_3_valid = instr_slices_0_valid;
      end
      2'b01 : begin
        tmp_io_push_3_payload = instr_slices_1_payload;
        tmp_io_push_3_valid = instr_slices_1_valid;
      end
      2'b10 : begin
        tmp_io_push_3_payload = instr_slices_2_payload;
        tmp_io_push_3_valid = instr_slices_2_valid;
      end
      default : begin
        tmp_io_push_3_payload = instr_slices_3_payload;
        tmp_io_push_3_valid = instr_slices_3_valid;
      end
    endcase
  end

  `ifndef SYNTHESIS
  always @(*) begin
    case(tmp_2)
      RVL : tmp_2_string = "RVL    ";
      RVH : tmp_2_string = "RVH    ";
      RVC : tmp_2_string = "RVC    ";
      INVALID : tmp_2_string = "INVALID";
      default : tmp_2_string = "???????";
    endcase
  end
  always @(*) begin
    case(tmp_3)
      RVL : tmp_3_string = "RVL    ";
      RVH : tmp_3_string = "RVH    ";
      RVC : tmp_3_string = "RVC    ";
      INVALID : tmp_3_string = "INVALID";
      default : tmp_3_string = "???????";
    endcase
  end
  always @(*) begin
    case(tmp_4)
      RVL : tmp_4_string = "RVL    ";
      RVH : tmp_4_string = "RVH    ";
      RVC : tmp_4_string = "RVC    ";
      INVALID : tmp_4_string = "INVALID";
      default : tmp_4_string = "???????";
    endcase
  end
  always @(*) begin
    case(tmp_5)
      RVL : tmp_5_string = "RVL    ";
      RVH : tmp_5_string = "RVH    ";
      RVC : tmp_5_string = "RVC    ";
      INVALID : tmp_5_string = "INVALID";
      default : tmp_5_string = "???????";
    endcase
  end
  `endif

  assign default_next_pc = (pc + 32'h00000008);
  assign addressOffset = addressFifo_io_head[2 : 1];
  assign ibus_cmd_fire = (ibus_cmd_valid && ibus_cmd_ready);
  assign addressFifo_io_push_valid = (ibus_cmd_fire && addressFifo_io_push_ready);
  assign fetch_pending = ((((! core_en) || instrFifo_io_ov_waterline) || (! validNumFifo_push_ready)) || (! addressFifo_io_push_ready));
  assign fetchStage_isReady = instrFifo_io_pop_0_valid;
  assign ibus_cmd_valid = (! fetch_pending);
  always @(*) begin
    if((pcJumpTarget_valid && ibus_cmd_ready)) begin
      ibus_cmd_payload_address = pcJumpTarget_payload;
    end else begin
      if((pcPredicted_valid && ibus_cmd_ready)) begin
        ibus_cmd_payload_address = pcPredicted_payload;
      end else begin
        ibus_cmd_payload_address = pc;
      end
    end
  end

  assign ibus_rsp_ready = 1'b1;
  assign valid_instr_slice_0 = (addressOffset <= 2'b00);
  assign valid_instr_slice_1 = (addressOffset <= 2'b01);
  assign valid_instr_slice_2 = (addressOffset <= 2'b10);
  assign valid_instr_slice_3 = (addressOffset <= 2'b11);
  assign slice_valid_vec_0 = instr_slices_0_valid;
  assign slice_valid_vec_1 = instr_slices_1_valid;
  assign slice_valid_vec_2 = instr_slices_2_valid;
  assign slice_valid_vec_3 = instr_slices_3_valid;
  assign tail_one_matrix_0 = {{{slice_valid_vec_3,slice_valid_vec_2},slice_valid_vec_1},slice_valid_vec_0};
  always @(*) begin
    instr_slices_0_valid = 1'b0;
    if(tmp_1) begin
      if(valid_instr_slice_0) begin
        instr_slices_0_valid = 1'b1;
      end
    end else begin
      if((ibus_rsp_payload_rdata[1 : 0] != 2'b11)) begin
        if(valid_instr_slice_0) begin
          instr_slices_0_valid = 1'b1;
        end
      end
    end
  end

  always @(*) begin
    instr_slices_0_payload = 33'h000000000;
    if(tmp_1) begin
      if(valid_instr_slice_0) begin
        instr_slices_0_payload = {{1'b0,ibus_rsp_payload_rdata[15 : 0]},tmp_instr_slices_0_payload};
      end
    end else begin
      if((ibus_rsp_payload_rdata[1 : 0] != 2'b11)) begin
        if(valid_instr_slice_0) begin
          instr_slices_0_payload = {{1'b1,16'h0000},ibus_rsp_payload_rdata[15 : 0]};
        end
      end
    end
  end

  always @(*) begin
    tmp_2 = INVALID;
    if(tmp_1) begin
      if(valid_instr_slice_0) begin
        tmp_2 = RVH;
      end
    end else begin
      if((ibus_rsp_payload_rdata[1 : 0] != 2'b11)) begin
        tmp_2 = RVC;
      end else begin
        tmp_2 = RVL;
      end
    end
  end

  always @(*) begin
    instr_slices_1_valid = 1'b0;
    case(tmp_2)
      RVL : begin
        if(valid_instr_slice_1) begin
          instr_slices_1_valid = 1'b1;
        end
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[17 : 16] != 2'b11)) begin
          if(valid_instr_slice_1) begin
            instr_slices_1_valid = 1'b1;
          end
        end
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    instr_slices_1_payload = 33'h000000000;
    case(tmp_2)
      RVL : begin
        if(valid_instr_slice_1) begin
          instr_slices_1_payload = {1'b0,ibus_rsp_payload_rdata[31 : 0]};
        end
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[17 : 16] != 2'b11)) begin
          if(valid_instr_slice_1) begin
            instr_slices_1_payload = {{1'b1,16'h0000},ibus_rsp_payload_rdata[31 : 16]};
          end
        end
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    tmp_3 = INVALID;
    case(tmp_2)
      RVL : begin
        tmp_3 = RVH;
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[17 : 16] != 2'b11)) begin
          tmp_3 = RVC;
        end else begin
          tmp_3 = RVL;
        end
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    instr_slices_2_valid = 1'b0;
    case(tmp_3)
      RVL : begin
        if(valid_instr_slice_2) begin
          instr_slices_2_valid = 1'b1;
        end
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[33 : 32] != 2'b11)) begin
          if(valid_instr_slice_2) begin
            instr_slices_2_valid = 1'b1;
          end
        end
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    instr_slices_2_payload = 33'h000000000;
    case(tmp_3)
      RVL : begin
        if(valid_instr_slice_2) begin
          instr_slices_2_payload = {1'b0,ibus_rsp_payload_rdata[47 : 16]};
        end
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[33 : 32] != 2'b11)) begin
          if(valid_instr_slice_2) begin
            instr_slices_2_payload = {{1'b1,16'h0000},ibus_rsp_payload_rdata[47 : 32]};
          end
        end
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    tmp_4 = INVALID;
    case(tmp_3)
      RVL : begin
        tmp_4 = RVH;
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[33 : 32] != 2'b11)) begin
          tmp_4 = RVC;
        end else begin
          tmp_4 = RVL;
        end
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    instr_slices_3_valid = 1'b0;
    case(tmp_4)
      RVL : begin
        if(valid_instr_slice_3) begin
          instr_slices_3_valid = 1'b1;
        end
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[49 : 48] != 2'b11)) begin
          if(valid_instr_slice_3) begin
            instr_slices_3_valid = 1'b1;
          end
        end
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    instr_slices_3_payload = 33'h000000000;
    case(tmp_4)
      RVL : begin
        if(valid_instr_slice_3) begin
          instr_slices_3_payload = {1'b0,ibus_rsp_payload_rdata[63 : 32]};
        end
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[49 : 48] != 2'b11)) begin
          if(valid_instr_slice_3) begin
            instr_slices_3_payload = {{1'b1,16'h0000},ibus_rsp_payload_rdata[63 : 48]};
          end
        end
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    tmp_5 = INVALID;
    case(tmp_4)
      RVL : begin
        tmp_5 = RVH;
      end
      RVH, RVC : begin
        if((ibus_rsp_payload_rdata[49 : 48] != 2'b11)) begin
          tmp_5 = RVC;
        end else begin
          tmp_5 = RVL;
        end
      end
      default : begin
      end
    endcase
  end

  assign tail_one_index_vec_0 = trailone_index_0_cnt_out[1 : 0];
  assign tail_one_valid_vec_0 = (trailone_index_0_cnt_out != 3'b100);
  always @(*) begin
    tail_one_matrix_1 = tail_one_matrix_0;
    tail_one_matrix_1[tail_one_index_vec_0] = 1'b0;
  end

  assign tail_one_index_vec_1 = trailone_index_1_cnt_out[1 : 0];
  assign tail_one_valid_vec_1 = (trailone_index_1_cnt_out != 3'b100);
  always @(*) begin
    tail_one_matrix_2 = tail_one_matrix_1;
    tail_one_matrix_2[tail_one_index_vec_1] = 1'b0;
  end

  assign tail_one_index_vec_2 = trailone_index_2_cnt_out[1 : 0];
  assign tail_one_valid_vec_2 = (trailone_index_2_cnt_out != 3'b100);
  always @(*) begin
    tail_one_matrix_3 = tail_one_matrix_2;
    tail_one_matrix_3[tail_one_index_vec_2] = 1'b0;
  end

  assign tail_one_index_vec_3 = trailone_index_3_cnt_out[1 : 0];
  assign tail_one_valid_vec_3 = (trailone_index_3_cnt_out != 3'b100);
  always @(*) begin
    instrFifo_io_push_0_payload = 33'h000000000;
    if(ibus_rsp_fire) begin
      instrFifo_io_push_0_payload = tmp_io_push_0_payload;
    end
  end

  always @(*) begin
    instrFifo_io_push_0_valid = 1'b0;
    if(ibus_rsp_fire) begin
      instrFifo_io_push_0_valid = (tail_one_valid_vec_0 && tmp_io_push_0_valid);
    end
  end

  assign ibus_rsp_fire = (ibus_rsp_valid && ibus_rsp_ready);
  always @(*) begin
    instrFifo_io_push_1_payload = 33'h000000000;
    if(ibus_rsp_fire) begin
      instrFifo_io_push_1_payload = tmp_io_push_1_payload;
    end
  end

  always @(*) begin
    instrFifo_io_push_1_valid = 1'b0;
    if(ibus_rsp_fire) begin
      instrFifo_io_push_1_valid = (tail_one_valid_vec_1 && tmp_io_push_1_valid);
    end
  end

  always @(*) begin
    instrFifo_io_push_2_payload = 33'h000000000;
    if(ibus_rsp_fire) begin
      instrFifo_io_push_2_payload = tmp_io_push_2_payload;
    end
  end

  always @(*) begin
    instrFifo_io_push_2_valid = 1'b0;
    if(ibus_rsp_fire) begin
      instrFifo_io_push_2_valid = (tail_one_valid_vec_2 && tmp_io_push_2_valid);
    end
  end

  always @(*) begin
    instrFifo_io_push_3_payload = 33'h000000000;
    if(ibus_rsp_fire) begin
      instrFifo_io_push_3_payload = tmp_io_push_3_payload;
    end
  end

  always @(*) begin
    instrFifo_io_push_3_valid = 1'b0;
    if(ibus_rsp_fire) begin
      instrFifo_io_push_3_valid = (tail_one_valid_vec_3 && tmp_io_push_3_valid);
    end
  end

  always @(*) begin
    instr_push_valid_vec[0] = instrFifo_io_push_0_valid;
    instr_push_valid_vec[1] = instrFifo_io_push_1_valid;
    instr_push_valid_vec[2] = instrFifo_io_push_2_valid;
    instr_push_valid_vec[3] = instrFifo_io_push_3_valid;
  end

  assign toplevel_instrFifo_io_push_0_fire = (instrFifo_io_push_0_valid && instrFifo_io_push_0_ready);
  always @(*) begin
    validNumFifo_push_valid = 1'b0;
    if(toplevel_instrFifo_io_push_0_fire) begin
      if((instr_push_valid_vec_popc_cnt_out != 3'b000)) begin
        validNumFifo_push_valid = 1'b1;
      end
    end
  end

  always @(*) begin
    validNumFifo_push_payload = 3'b000;
    if(toplevel_instrFifo_io_push_0_fire) begin
      if((instr_push_valid_vec_popc_cnt_out != 3'b000)) begin
        validNumFifo_push_payload = instr_push_valid_vec_popc_cnt_out;
      end
    end
  end

  assign toplevel_instrFifo_io_pop_0_fire = (instrFifo_io_pop_0_valid && fetchStage_isDone);
  always @(*) begin
    instr_pop_valid[0] = toplevel_instrFifo_io_pop_0_fire;
    instr_pop_valid[1] = toplevel_instrFifo_io_pop_1_fire;
  end

  assign toplevel_instrFifo_io_pop_1_fire = (instrFifo_io_pop_1_valid && fetchStage_isDone);
  always @(*) begin
    validNumFifo_pop_pop_req_num = 3'b000;
    if(toplevel_instrFifo_io_pop_0_fire) begin
      validNumFifo_pop_pop_req_num = {1'd0, instr_pop_valid_popc_cnt_out};
    end
  end

  always @(*) begin
    validNumFifo_pop_valid = 1'b0;
    if(toplevel_instrFifo_io_pop_0_fire) begin
      validNumFifo_pop_valid = 1'b1;
    end
  end

  always @(*) begin
    addressFifo_io_pop_ready = 1'b0;
    if(toplevel_instrFifo_io_pop_0_fire) begin
      addressFifo_io_pop_ready = (validNumFifo_pop_pop_real && (validNumFifo_pop_valid && validNumFifo_pop_ready));
    end
  end

  assign tmp_real_instr_0 = instrFifo_io_pop_0_payload[32];
  assign rvcDecompressor_2_i = instrFifo_io_pop_0_payload[31 : 0];
  assign real_instr_0 = ((tmp_real_instr_0 && (! rvcDecompressor_2_decompInstr_illegal)) ? rvcDecompressor_2_decompInstr_inst : instrFifo_io_pop_0_payload[31 : 0]);
  assign pc_step_0 = (tmp_real_instr_0 ? 3'b010 : 3'b100);
  assign pc_step_all_1 = (3'b000 + pc_step_0);
  assign tmp_real_instr_1 = instrFifo_io_pop_1_payload[32];
  assign rvcDecompressor_3_i = instrFifo_io_pop_1_payload[31 : 0];
  assign real_instr_1 = ((tmp_real_instr_1 && (! rvcDecompressor_3_decompInstr_illegal)) ? rvcDecompressor_3_decompInstr_inst : instrFifo_io_pop_1_payload[31 : 0]);
  assign pc_step_1 = (tmp_real_instr_1 ? 3'b010 : 3'b100);
  assign pc_step_all_2 = (pc_step_all_1 + pc_step_1);
  always @(*) begin
    if2id_itf_0_instr_data = 32'h00000000;
    if(toplevel_instrFifo_io_pop_0_fire) begin
      if2id_itf_0_instr_data = real_instr_0;
    end
  end

  always @(*) begin
    if2id_itf_0_instr_pc = 32'h00000000;
    if(toplevel_instrFifo_io_pop_0_fire) begin
      if2id_itf_0_instr_pc = (tmp_if2id_itf_0_instr_pc + 32'h00000000);
    end
  end

  assign if2id_itf_0_valid = toplevel_instrFifo_io_pop_0_fire;
  always @(*) begin
    if2id_itf_1_instr_data = 32'h00000000;
    if(toplevel_instrFifo_io_pop_1_fire) begin
      if2id_itf_1_instr_data = real_instr_1;
    end
  end

  always @(*) begin
    if2id_itf_1_instr_pc = 32'h00000000;
    if(toplevel_instrFifo_io_pop_1_fire) begin
      if2id_itf_1_instr_pc = (tmp_if2id_itf_1_instr_pc + tmp_if2id_itf_1_instr_pc_2);
    end
  end

  assign if2id_itf_1_valid = toplevel_instrFifo_io_pop_1_fire;
  assign toplevel_addressFifo_io_pop_fire = (addressFifo_io_pop_valid && addressFifo_io_pop_ready);
  always @(*) begin
    instrFifo_io_flush = 1'b0;
    if(fetchStage_flush) begin
      instrFifo_io_flush = 1'b1;
    end
  end

  always @(*) begin
    addressFifo_io_flush = 1'b0;
    if(fetchStage_flush) begin
      addressFifo_io_flush = 1'b1;
    end
  end

  always @(*) begin
    validNumFifo_flush = 1'b0;
    if(fetchStage_flush) begin
      validNumFifo_flush = 1'b1;
    end
  end

  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      pc <= 32'h00000000;
      tmp_1 <= 1'b0;
      tmp_instr_slices_0_payload <= 16'h0000;
      pc_pop_offset <= 3'b000;
    end else begin
      if(pcJumpTarget_valid) begin
        pc <= pcJumpTarget_payload;
      end else begin
        if(pcPredicted_valid) begin
          pc <= pcPredicted_payload;
        end else begin
          if(fetch_pending) begin
            pc <= pc;
          end else begin
            pc <= default_next_pc;
          end
        end
      end
      if((tmp_5 == RVL)) begin
        tmp_1 <= 1'b1;
        tmp_instr_slices_0_payload <= ibus_rsp_payload_rdata[63 : 48];
      end
      if(toplevel_addressFifo_io_pop_fire) begin
        pc_pop_offset <= 3'b000;
      end else begin
        if(toplevel_instrFifo_io_pop_0_fire) begin
          pc_pop_offset <= pc_step_all_2;
        end
      end
    end
  end


endmodule

//RvcDecompressor_1 replaced by RvcDecompressor

module RvcDecompressor (
  input      [31:0]   i,
  output     [31:0]   decompInstr_inst,
  output              decompInstr_illegal
);

  wire       [11:0]   tmp_tmp_ret_inst;
  wire       [5:0]    tmp_tmp_ret_inst_1;
  reg        [31:0]   tmp_ret_inst_1;
  wire       [1:0]    tmp_ret_inst_2;
  reg        [2:0]    tmp_ret_inst_3;
  wire       [2:0]    tmp_ret_inst_4;
  wire       [6:0]    tmp_ret_inst_5;
  wire       [4:0]    tmp_ret_inst_6;
  wire                tmp_ret_inst_7;
  wire       [4:0]    tmp_ret_inst_8;
  wire       [11:0]   tmp_ret_inst_9;
  wire       [11:0]   tmp_ret_inst_10;
  reg        [31:0]   ret_inst;
  reg                 ret_illegal;
  wire       [4:0]    rch;
  wire       [4:0]    rcl;
  wire       [11:0]   addi5spnImm;
  wire       [11:0]   lwImm;
  wire       [11:0]   ldImm;
  wire                tmp_addImm;
  reg        [11:0]   addImm;
  wire                tmp_jalImm;
  reg        [9:0]    tmp_jalImm_1;
  wire       [20:0]   jalImm;
  wire                tmp_luiImm;
  reg        [14:0]   tmp_luiImm_1;
  wire       [31:0]   luiImm;
  wire       [4:0]    shiftImm;
  wire                tmp_addi16spImm;
  reg        [2:0]    tmp_addi16spImm_1;
  wire       [11:0]   addi16spImm;
  wire                tmp_jImm;
  reg        [9:0]    tmp_jImm_1;
  wire       [20:0]   jImm;
  wire                tmp_bImm;
  reg        [4:0]    tmp_bImm_1;
  wire       [12:0]   bImm;
  wire       [4:0]    x0;
  wire       [4:0]    x1;
  wire       [4:0]    x2;
  wire       [31:0]   tmp_ret_inst;

  assign tmp_tmp_ret_inst_1 = {i[12],i[6 : 2]};
  assign tmp_tmp_ret_inst = {6'd0, tmp_tmp_ret_inst_1};
  assign tmp_ret_inst_9 = {{{4'b0000,i[8 : 7]},i[12 : 9]},2'b00};
  assign tmp_ret_inst_10 = {{{4'b0000,i[8 : 7]},i[12 : 9]},2'b00};
  assign tmp_ret_inst_2 = i[11 : 10];
  assign tmp_ret_inst_4 = {i[12],i[6 : 5]};
  assign tmp_ret_inst_5 = 7'h00;
  assign tmp_ret_inst_6 = i[6 : 2];
  assign tmp_ret_inst_7 = i[12];
  assign tmp_ret_inst_8 = i[11 : 7];
  always @(*) begin
    case(tmp_ret_inst_2)
      2'b00 : tmp_ret_inst_1 = tmp_ret_inst;
      2'b01 : tmp_ret_inst_1 = (tmp_ret_inst | 32'h40000000);
      2'b10 : tmp_ret_inst_1 = {{{{addImm,rch},3'b111},rch},7'h13};
      default : tmp_ret_inst_1 = ({{{{{7'h00,rcl},rch},tmp_ret_inst_3},rch},(i[12] ? 7'h3b : 7'h33)} | ((i[6 : 5] == 2'b00) ? 32'h40000000 : 32'h00000000));
    endcase
  end

  always @(*) begin
    case(tmp_ret_inst_4)
      3'b000 : tmp_ret_inst_3 = 3'b000;
      3'b001 : tmp_ret_inst_3 = 3'b100;
      3'b010 : tmp_ret_inst_3 = 3'b110;
      3'b011 : tmp_ret_inst_3 = 3'b111;
      3'b100 : tmp_ret_inst_3 = 3'b000;
      3'b101 : tmp_ret_inst_3 = 3'b000;
      3'b110 : tmp_ret_inst_3 = 3'b010;
      default : tmp_ret_inst_3 = 3'b011;
    endcase
  end

  always @(*) begin
    ret_inst = 32'bxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx;
    case({i[1 : 0],i[15 : 13]})
      5'h00 : begin
        ret_inst = {{{{addi5spnImm,5'h02},3'b000},rcl},7'h13};
      end
      5'h02 : begin
        ret_inst = {{{{lwImm,rch},3'b010},rcl},7'h03};
      end
      5'h05 : begin
        ret_inst = {{{{{ldImm[11 : 5],rcl},rch},3'b011},ldImm[4 : 0]},7'h27};
      end
      5'h06 : begin
        ret_inst = {{{{{lwImm[11 : 5],rcl},rch},3'b010},lwImm[4 : 0]},7'h23};
      end
      5'h08 : begin
        ret_inst = {{{{addImm,i[11 : 7]},3'b000},i[11 : 7]},7'h13};
      end
      5'h09 : begin
        ret_inst = {{{{{jalImm[20],jalImm[10 : 1]},jalImm[11]},jalImm[19 : 12]},x1},7'h6f};
      end
      5'h0a : begin
        ret_inst = {{{{addImm,5'h00},3'b000},i[11 : 7]},7'h13};
      end
      5'h0b : begin
        ret_inst = ((i[11 : 7] == 5'h02) ? {{{{addi16spImm,i[11 : 7]},3'b000},i[11 : 7]},7'h13} : {{luiImm[31 : 12],i[11 : 7]},7'h37});
      end
      5'h0c : begin
        ret_inst = tmp_ret_inst_1;
      end
      5'h0d : begin
        ret_inst = {{{{{jImm[20],jImm[10 : 1]},jImm[11]},jImm[19 : 12]},x0},7'h6f};
      end
      5'h0e : begin
        ret_inst = {{{{{{{bImm[12],bImm[10 : 5]},x0},rch},3'b000},bImm[4 : 1]},bImm[11]},7'h63};
      end
      5'h0f : begin
        ret_inst = {{{{{{{bImm[12],bImm[10 : 5]},x0},rch},3'b001},bImm[4 : 1]},bImm[11]},7'h63};
      end
      5'h10 : begin
        ret_inst = {{{{{{6'h00,i[12]},i[6 : 2]},i[11 : 7]},3'b001},i[11 : 7]},7'h13};
      end
      5'h12 : begin
        ret_inst = {{{{{{{{4'b0000,i[3 : 2]},i[12]},i[6 : 4]},2'b00},x2},3'b010},i[11 : 7]},7'h03};
      end
      5'h14 : begin
        ret_inst = ((i[12 : 2] == 11'h400) ? 32'h00100073 : ((i[6 : 2] == 5'h00) ? {{{{12'h000,i[11 : 7]},3'b000},(i[12] ? x1 : x0)},7'h67} : {{{{{tmp_ret_inst_5,tmp_ret_inst_6},(tmp_ret_inst_7 ? tmp_ret_inst_8 : x0)},3'b000},i[11 : 7]},7'h33}));
      end
      5'h16 : begin
        ret_inst = {{{{{tmp_ret_inst_9[11 : 5],i[6 : 2]},x2},3'b010},tmp_ret_inst_10[4 : 0]},7'h23};
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    ret_illegal = 1'b0;
    case({i[1 : 0],i[15 : 13]})
      5'h00 : begin
        if((i[12 : 2] == 11'h000)) begin
          ret_illegal = 1'b1;
        end
      end
      5'h02 : begin
      end
      5'h05 : begin
      end
      5'h06 : begin
      end
      5'h08 : begin
      end
      5'h09 : begin
      end
      5'h0a : begin
      end
      5'h0b : begin
      end
      5'h0c : begin
      end
      5'h0d : begin
      end
      5'h0e : begin
      end
      5'h0f : begin
      end
      5'h10 : begin
      end
      5'h12 : begin
      end
      5'h14 : begin
      end
      5'h16 : begin
      end
      default : begin
        ret_illegal = 1'b1;
      end
    endcase
  end

  assign rch = {2'b01,i[9 : 7]};
  assign rcl = {2'b01,i[4 : 2]};
  assign addi5spnImm = {{{{{2'b00,i[10 : 7]},i[12 : 11]},i[5]},i[6]},2'b00};
  assign lwImm = {{{{5'h00,i[5]},i[12 : 10]},i[6]},2'b00};
  assign ldImm = {{{4'b0000,i[6 : 5]},i[12 : 10]},3'b000};
  assign tmp_addImm = i[12];
  always @(*) begin
    addImm[11] = tmp_addImm;
    addImm[10] = tmp_addImm;
    addImm[9] = tmp_addImm;
    addImm[8] = tmp_addImm;
    addImm[7] = tmp_addImm;
    addImm[6] = tmp_addImm;
    addImm[5] = tmp_addImm;
    addImm[4 : 0] = i[6 : 2];
  end

  assign tmp_jalImm = i[12];
  always @(*) begin
    tmp_jalImm_1[9] = tmp_jalImm;
    tmp_jalImm_1[8] = tmp_jalImm;
    tmp_jalImm_1[7] = tmp_jalImm;
    tmp_jalImm_1[6] = tmp_jalImm;
    tmp_jalImm_1[5] = tmp_jalImm;
    tmp_jalImm_1[4] = tmp_jalImm;
    tmp_jalImm_1[3] = tmp_jalImm;
    tmp_jalImm_1[2] = tmp_jalImm;
    tmp_jalImm_1[1] = tmp_jalImm;
    tmp_jalImm_1[0] = tmp_jalImm;
  end

  assign jalImm = {{{{{{{{tmp_jalImm_1,i[8]},i[10 : 9]},i[6]},i[7]},i[2]},i[11]},i[5 : 3]},1'b0};
  assign tmp_luiImm = i[12];
  always @(*) begin
    tmp_luiImm_1[14] = tmp_luiImm;
    tmp_luiImm_1[13] = tmp_luiImm;
    tmp_luiImm_1[12] = tmp_luiImm;
    tmp_luiImm_1[11] = tmp_luiImm;
    tmp_luiImm_1[10] = tmp_luiImm;
    tmp_luiImm_1[9] = tmp_luiImm;
    tmp_luiImm_1[8] = tmp_luiImm;
    tmp_luiImm_1[7] = tmp_luiImm;
    tmp_luiImm_1[6] = tmp_luiImm;
    tmp_luiImm_1[5] = tmp_luiImm;
    tmp_luiImm_1[4] = tmp_luiImm;
    tmp_luiImm_1[3] = tmp_luiImm;
    tmp_luiImm_1[2] = tmp_luiImm;
    tmp_luiImm_1[1] = tmp_luiImm;
    tmp_luiImm_1[0] = tmp_luiImm;
  end

  assign luiImm = {{tmp_luiImm_1,i[6 : 2]},12'h000};
  assign shiftImm = i[6 : 2];
  assign tmp_addi16spImm = i[12];
  always @(*) begin
    tmp_addi16spImm_1[2] = tmp_addi16spImm;
    tmp_addi16spImm_1[1] = tmp_addi16spImm;
    tmp_addi16spImm_1[0] = tmp_addi16spImm;
  end

  assign addi16spImm = {{{{{tmp_addi16spImm_1,i[4 : 3]},i[5]},i[2]},i[6]},4'b0000};
  assign tmp_jImm = i[12];
  always @(*) begin
    tmp_jImm_1[9] = tmp_jImm;
    tmp_jImm_1[8] = tmp_jImm;
    tmp_jImm_1[7] = tmp_jImm;
    tmp_jImm_1[6] = tmp_jImm;
    tmp_jImm_1[5] = tmp_jImm;
    tmp_jImm_1[4] = tmp_jImm;
    tmp_jImm_1[3] = tmp_jImm;
    tmp_jImm_1[2] = tmp_jImm;
    tmp_jImm_1[1] = tmp_jImm;
    tmp_jImm_1[0] = tmp_jImm;
  end

  assign jImm = {{{{{{{{tmp_jImm_1,i[8]},i[10 : 9]},i[6]},i[7]},i[2]},i[11]},i[5 : 3]},1'b0};
  assign tmp_bImm = i[12];
  always @(*) begin
    tmp_bImm_1[4] = tmp_bImm;
    tmp_bImm_1[3] = tmp_bImm;
    tmp_bImm_1[2] = tmp_bImm;
    tmp_bImm_1[1] = tmp_bImm;
    tmp_bImm_1[0] = tmp_bImm;
  end

  assign bImm = {{{{{tmp_bImm_1,i[6 : 5]},i[2]},i[11 : 10]},i[4 : 3]},1'b0};
  assign x0 = 5'h00;
  assign x1 = 5'h01;
  assign x2 = 5'h02;
  assign tmp_ret_inst = {{{{tmp_tmp_ret_inst,rch},3'b101},rch},7'h13};
  assign decompInstr_inst = ret_inst;
  assign decompInstr_illegal = ret_illegal;

endmodule

module popcount_1 (
  input               din_vld,
  input      [1:0]    data_in,
  output     [1:0]    cnt_out
);

  wire       [0:0]    popsum_init_0;
  wire       [0:0]    popsum_init_1;
  wire       [1:0]    popsum_stage1_ele0;

  assign popsum_init_0 = data_in[0];
  assign popsum_init_1 = data_in[1];
  assign popsum_stage1_ele0 = ({1'b0,popsum_init_0} + {1'b0,popsum_init_1});
  assign cnt_out = (din_vld ? popsum_stage1_ele0 : 2'b00);

endmodule

module popcount (
  input               din_vld,
  input      [3:0]    data_in,
  output     [2:0]    cnt_out
);

  wire       [0:0]    popsum_init_0;
  wire       [0:0]    popsum_init_1;
  wire       [0:0]    popsum_init_2;
  wire       [0:0]    popsum_init_3;
  wire       [1:0]    popsum_stage1_ele0;
  wire       [1:0]    popsum_stage1_ele1;
  wire       [2:0]    popsum_stage2_ele0;

  assign popsum_init_0 = data_in[0];
  assign popsum_init_1 = data_in[1];
  assign popsum_init_2 = data_in[2];
  assign popsum_init_3 = data_in[3];
  assign popsum_stage1_ele0 = ({1'b0,popsum_init_0} + {1'b0,popsum_init_1});
  assign popsum_stage1_ele1 = ({1'b0,popsum_init_2} + {1'b0,popsum_init_3});
  assign popsum_stage2_ele0 = ({1'b0,popsum_stage1_ele0} + {1'b0,popsum_stage1_ele1});
  assign cnt_out = (din_vld ? popsum_stage2_ele0 : 3'b000);

endmodule

//lzc_3 replaced by lzc

//lzc_2 replaced by lzc

//lzc_1 replaced by lzc

module lzc (
  input               mode,
  input               lead,
  input               trail,
  input      [3:0]    data_in,
  output     [2:0]    cnt_out
);

  wire       [2:0]    tmp_cnt_out;
  reg        [3:0]    data_s;
  wire       [3:0]    datain_reverse;
  wire       [3:0]    stage_node_0;
  reg        [1:0]    stage_node_1;
  wire       [1:0]    stage_index1_0;
  wire       [1:0]    stage_index1_1;
  wire       [1:0]    stage_index2_0;
  wire                empty;
  reg        [1:0]    out_remap;

  assign tmp_cnt_out = {1'd0, out_remap};
  assign datain_reverse = {data_in[0],{data_in[1],{data_in[2],data_in[3]}}};
  always @(*) begin
    data_s = 4'b0000;
    if(mode) begin
      if(lead) begin
        data_s = datain_reverse;
      end else begin
        if(trail) begin
          data_s = data_in;
        end
      end
    end else begin
      if(lead) begin
        data_s = (~ datain_reverse);
      end else begin
        if(trail) begin
          data_s = (~ data_in);
        end
      end
    end
  end

  assign stage_node_0 = data_s;
  always @(*) begin
    stage_node_1[0] = (stage_node_0[0] || stage_node_0[1]);
    stage_node_1[1] = (stage_node_0[2] || stage_node_0[3]);
  end

  assign stage_index1_0 = (stage_node_0[0] ? 2'b00 : 2'b01);
  assign stage_index1_1 = (stage_node_0[2] ? 2'b10 : 2'b11);
  assign stage_index2_0 = (stage_node_1[0] ? stage_index1_0 : stage_index1_1);
  assign empty = ((lead || trail) ? (! (|data_s)) : 1'b0);
  always @(*) begin
    out_remap = 2'b00;
    if(lead) begin
      out_remap = (2'b11 - stage_index2_0);
    end else begin
      if(trail) begin
        out_remap = stage_index2_0;
      end
    end
  end

  assign cnt_out = ((lead && empty) ? 3'b100 : tmp_cnt_out);

endmodule

module StreamFifo1delayMultiPort (
  input               io_push_0_valid,
  output              io_push_0_ready,
  input      [32:0]   io_push_0_payload,
  input               io_push_1_valid,
  output              io_push_1_ready,
  input      [32:0]   io_push_1_payload,
  input               io_push_2_valid,
  output              io_push_2_ready,
  input      [32:0]   io_push_2_payload,
  input               io_push_3_valid,
  output              io_push_3_ready,
  input      [32:0]   io_push_3_payload,
  output              io_pop_0_valid,
  input               io_pop_0_ready,
  output     [32:0]   io_pop_0_payload,
  output              io_pop_1_valid,
  input               io_pop_1_ready,
  output     [32:0]   io_pop_1_payload,
  input               io_flush,
  output reg [3:0]    io_occupancy,
  output              io_ov_waterline,
  input               clk,
  input               resetn
);

  wire       [1:0]    tmp_inc_num_1;
  wire       [1:0]    tmp_inc_num_2;
  wire       [0:0]    tmp_inc_num_3;
  wire       [2:0]    tmp_inc_num_4;
  wire       [1:0]    tmp_inc_num_5;
  wire       [1:0]    tmp_inc_num_6;
  wire       [0:0]    tmp_inc_num_7;
  wire       [1:0]    tmp_dec_num_1;
  wire       [0:0]    tmp_dec_num_2;
  wire       [3:0]    tmp_io_push_0_ready;
  wire       [3:0]    tmp_io_push_1_ready;
  wire       [3:0]    tmp_io_push_2_ready;
  wire       [3:0]    tmp_io_push_3_ready;
  wire       [4:0]    tmp_pushPtrs_inc_0;
  wire       [0:0]    tmp_pushPtrs_inc_0_1;
  wire       [4:0]    tmp_pushPtrs_inc_1;
  wire       [1:0]    tmp_pushPtrs_inc_1_1;
  wire       [4:0]    tmp_pushPtrs_inc_2;
  wire       [2:0]    tmp_pushPtrs_inc_2_1;
  wire       [4:0]    tmp_pushPtrs_inc_3;
  wire       [2:0]    tmp_pushPtrs_inc_3_1;
  wire       [4:0]    tmp_popPtrs_inc_0;
  wire       [0:0]    tmp_popPtrs_inc_0_1;
  wire       [4:0]    tmp_popPtrs_inc_1;
  wire       [1:0]    tmp_popPtrs_inc_1_1;
  reg        [32:0]   tmp_readedVec_0;
  wire       [3:0]    tmp_readedVec_0_1;
  reg        [32:0]   tmp_readedVec_1;
  wire       [3:0]    tmp_readedVec_1_1;
  wire       [2:0]    tmp_when;
  wire       [4:0]    tmp_pushPtrGlobalInc;
  wire       [3:0]    tmp_pushPtrGlobalInc_1;
  wire       [4:0]    tmp_popPtrGlobalInc;
  wire       [2:0]    tmp_popPtrGlobalInc_1;
  wire       [4:0]    tmp_pushPtrGlobal;
  wire       [4:0]    tmp_popPtrGlobal;
  wire       [3:0]    tmp_io_occupancy;
  reg        [32:0]   vec_0;
  reg        [32:0]   vec_1;
  reg        [32:0]   vec_2;
  reg        [32:0]   vec_3;
  reg        [32:0]   vec_4;
  reg        [32:0]   vec_5;
  reg        [32:0]   vec_6;
  reg        [32:0]   vec_7;
  reg        [32:0]   vec_8;
  reg        [32:0]   vec_9;
  reg        [32:0]   vec_10;
  reg        [32:0]   vec_11;
  reg        [32:0]   vec_12;
  reg        [32:0]   vec_13;
  reg        [3:0]    pushPtrGlobal;
  reg        [3:0]    popPtrGlobal;
  wire                ptrMatch;
  reg                 risingOccupancy;
  wire                empty;
  wire                full;
  wire                pushing_vec_0;
  wire                pushing_vec_1;
  wire                pushing_vec_2;
  wire                pushing_vec_3;
  wire                popping_vec_0;
  wire                popping_vec_1;
  wire                tmp_inc_num;
  wire       [2:0]    inc_num;
  wire                tmp_dec_num;
  wire       [1:0]    dec_num;
  reg        [4:0]    pushPtrs_0;
  reg        [4:0]    pushPtrs_1;
  reg        [4:0]    pushPtrs_2;
  reg        [4:0]    pushPtrs_3;
  reg        [4:0]    popPtrs_0;
  reg        [4:0]    popPtrs_1;
  wire       [4:0]    pushPtrs_inc_0;
  wire       [4:0]    pushPtrs_inc_1;
  wire       [4:0]    pushPtrs_inc_2;
  wire       [4:0]    pushPtrs_inc_3;
  wire       [4:0]    popPtrs_inc_0;
  wire       [4:0]    popPtrs_inc_1;
  wire       [32:0]   readedVec_0;
  wire       [32:0]   readedVec_1;
  wire       [15:0]   tmp_1;
  wire       [15:0]   tmp_2;
  wire       [15:0]   tmp_3;
  wire       [15:0]   tmp_4;
  wire       [4:0]    pushPtrGlobalInc;
  wire       [4:0]    popPtrGlobalInc;
  wire       [3:0]    ptrDif;

  assign tmp_inc_num_1 = ({1'b0,(pushing_vec_0 == tmp_inc_num)} + tmp_inc_num_2);
  assign tmp_inc_num_3 = (pushing_vec_1 == tmp_inc_num);
  assign tmp_inc_num_2 = {1'd0, tmp_inc_num_3};
  assign tmp_inc_num_5 = ({1'b0,(pushing_vec_2 == tmp_inc_num)} + tmp_inc_num_6);
  assign tmp_inc_num_4 = {1'd0, tmp_inc_num_5};
  assign tmp_inc_num_7 = (pushing_vec_3 == tmp_inc_num);
  assign tmp_inc_num_6 = {1'd0, tmp_inc_num_7};
  assign tmp_dec_num_2 = (popping_vec_1 == tmp_dec_num);
  assign tmp_dec_num_1 = {1'd0, tmp_dec_num_2};
  assign tmp_io_push_0_ready = (4'b1110 - io_occupancy);
  assign tmp_io_push_1_ready = (4'b1110 - io_occupancy);
  assign tmp_io_push_2_ready = (4'b1110 - io_occupancy);
  assign tmp_io_push_3_ready = (4'b1110 - io_occupancy);
  assign tmp_pushPtrs_inc_0_1 = 1'b0;
  assign tmp_pushPtrs_inc_0 = {4'd0, tmp_pushPtrs_inc_0_1};
  assign tmp_pushPtrs_inc_1_1 = {1'b0,1'b1};
  assign tmp_pushPtrs_inc_1 = {3'd0, tmp_pushPtrs_inc_1_1};
  assign tmp_pushPtrs_inc_2_1 = {1'b0,2'b10};
  assign tmp_pushPtrs_inc_2 = {2'd0, tmp_pushPtrs_inc_2_1};
  assign tmp_pushPtrs_inc_3_1 = {1'b0,2'b11};
  assign tmp_pushPtrs_inc_3 = {2'd0, tmp_pushPtrs_inc_3_1};
  assign tmp_popPtrs_inc_0_1 = 1'b0;
  assign tmp_popPtrs_inc_0 = {4'd0, tmp_popPtrs_inc_0_1};
  assign tmp_popPtrs_inc_1_1 = {1'b0,1'b1};
  assign tmp_popPtrs_inc_1 = {3'd0, tmp_popPtrs_inc_1_1};
  assign tmp_when = {1'd0, dec_num};
  assign tmp_pushPtrGlobalInc_1 = {1'b0,inc_num};
  assign tmp_pushPtrGlobalInc = {1'd0, tmp_pushPtrGlobalInc_1};
  assign tmp_popPtrGlobalInc_1 = {1'b0,dec_num};
  assign tmp_popPtrGlobalInc = {2'd0, tmp_popPtrGlobalInc_1};
  assign tmp_pushPtrGlobal = (pushPtrGlobalInc - 5'h0e);
  assign tmp_popPtrGlobal = (popPtrGlobalInc - 5'h0e);
  assign tmp_io_occupancy = (4'b1110 + ptrDif);
  assign tmp_readedVec_0_1 = popPtrs_0[3 : 0];
  assign tmp_readedVec_1_1 = popPtrs_1[3 : 0];
  always @(*) begin
    case(tmp_readedVec_0_1)
      4'b0000 : tmp_readedVec_0 = vec_0;
      4'b0001 : tmp_readedVec_0 = vec_1;
      4'b0010 : tmp_readedVec_0 = vec_2;
      4'b0011 : tmp_readedVec_0 = vec_3;
      4'b0100 : tmp_readedVec_0 = vec_4;
      4'b0101 : tmp_readedVec_0 = vec_5;
      4'b0110 : tmp_readedVec_0 = vec_6;
      4'b0111 : tmp_readedVec_0 = vec_7;
      4'b1000 : tmp_readedVec_0 = vec_8;
      4'b1001 : tmp_readedVec_0 = vec_9;
      4'b1010 : tmp_readedVec_0 = vec_10;
      4'b1011 : tmp_readedVec_0 = vec_11;
      4'b1100 : tmp_readedVec_0 = vec_12;
      default : tmp_readedVec_0 = vec_13;
    endcase
  end

  always @(*) begin
    case(tmp_readedVec_1_1)
      4'b0000 : tmp_readedVec_1 = vec_0;
      4'b0001 : tmp_readedVec_1 = vec_1;
      4'b0010 : tmp_readedVec_1 = vec_2;
      4'b0011 : tmp_readedVec_1 = vec_3;
      4'b0100 : tmp_readedVec_1 = vec_4;
      4'b0101 : tmp_readedVec_1 = vec_5;
      4'b0110 : tmp_readedVec_1 = vec_6;
      4'b0111 : tmp_readedVec_1 = vec_7;
      4'b1000 : tmp_readedVec_1 = vec_8;
      4'b1001 : tmp_readedVec_1 = vec_9;
      4'b1010 : tmp_readedVec_1 = vec_10;
      4'b1011 : tmp_readedVec_1 = vec_11;
      4'b1100 : tmp_readedVec_1 = vec_12;
      default : tmp_readedVec_1 = vec_13;
    endcase
  end

  assign ptrMatch = (pushPtrGlobal == popPtrGlobal);
  assign empty = (ptrMatch && (! risingOccupancy));
  assign full = (ptrMatch && risingOccupancy);
  assign pushing_vec_0 = (io_push_0_valid && io_push_0_ready);
  assign pushing_vec_1 = (io_push_1_valid && io_push_1_ready);
  assign pushing_vec_2 = (io_push_2_valid && io_push_2_ready);
  assign pushing_vec_3 = (io_push_3_valid && io_push_3_ready);
  assign popping_vec_0 = (io_pop_0_valid && io_pop_0_ready);
  assign popping_vec_1 = (io_pop_1_valid && io_pop_1_ready);
  assign tmp_inc_num = 1'b1;
  assign inc_num = ({1'b0,tmp_inc_num_1} + tmp_inc_num_4);
  assign tmp_dec_num = 1'b1;
  assign dec_num = ({1'b0,(popping_vec_0 == tmp_dec_num)} + tmp_dec_num_1);
  assign io_push_0_ready = (4'b0000 < tmp_io_push_0_ready);
  assign io_push_1_ready = (4'b0001 < tmp_io_push_1_ready);
  assign io_push_2_ready = (4'b0010 < tmp_io_push_2_ready);
  assign io_push_3_ready = (4'b0011 < tmp_io_push_3_ready);
  assign io_ov_waterline = (4'b0110 <= io_occupancy);
  assign pushPtrs_inc_0 = ({1'b0,pushPtrGlobal} + tmp_pushPtrs_inc_0);
  always @(*) begin
    if((5'h0e <= pushPtrs_inc_0)) begin
      pushPtrs_0 = (pushPtrs_inc_0 - 5'h0e);
    end else begin
      pushPtrs_0 = pushPtrs_inc_0;
    end
  end

  assign pushPtrs_inc_1 = ({1'b0,pushPtrGlobal} + tmp_pushPtrs_inc_1);
  always @(*) begin
    if((5'h0e <= pushPtrs_inc_1)) begin
      pushPtrs_1 = (pushPtrs_inc_1 - 5'h0e);
    end else begin
      pushPtrs_1 = pushPtrs_inc_1;
    end
  end

  assign pushPtrs_inc_2 = ({1'b0,pushPtrGlobal} + tmp_pushPtrs_inc_2);
  always @(*) begin
    if((5'h0e <= pushPtrs_inc_2)) begin
      pushPtrs_2 = (pushPtrs_inc_2 - 5'h0e);
    end else begin
      pushPtrs_2 = pushPtrs_inc_2;
    end
  end

  assign pushPtrs_inc_3 = ({1'b0,pushPtrGlobal} + tmp_pushPtrs_inc_3);
  always @(*) begin
    if((5'h0e <= pushPtrs_inc_3)) begin
      pushPtrs_3 = (pushPtrs_inc_3 - 5'h0e);
    end else begin
      pushPtrs_3 = pushPtrs_inc_3;
    end
  end

  assign popPtrs_inc_0 = ({1'b0,popPtrGlobal} + tmp_popPtrs_inc_0);
  always @(*) begin
    if((5'h0e <= popPtrs_inc_0)) begin
      popPtrs_0 = (popPtrs_inc_0 - 5'h0e);
    end else begin
      popPtrs_0 = popPtrs_inc_0;
    end
  end

  assign popPtrs_inc_1 = ({1'b0,popPtrGlobal} + tmp_popPtrs_inc_1);
  always @(*) begin
    if((5'h0e <= popPtrs_inc_1)) begin
      popPtrs_1 = (popPtrs_inc_1 - 5'h0e);
    end else begin
      popPtrs_1 = popPtrs_inc_1;
    end
  end

  assign readedVec_0 = tmp_readedVec_0;
  assign readedVec_1 = tmp_readedVec_1;
  assign io_pop_0_valid = (4'b0000 < io_occupancy);
  assign io_pop_0_payload = readedVec_0;
  assign io_pop_1_valid = (4'b0001 < io_occupancy);
  assign io_pop_1_payload = readedVec_1;
  assign tmp_1 = ({15'd0,1'b1} <<< pushPtrs_0[3 : 0]);
  assign tmp_2 = ({15'd0,1'b1} <<< pushPtrs_1[3 : 0]);
  assign tmp_3 = ({15'd0,1'b1} <<< pushPtrs_2[3 : 0]);
  assign tmp_4 = ({15'd0,1'b1} <<< pushPtrs_3[3 : 0]);
  assign pushPtrGlobalInc = ({1'b0,pushPtrGlobal} + tmp_pushPtrGlobalInc);
  assign popPtrGlobalInc = ({1'b0,popPtrGlobal} + tmp_popPtrGlobalInc);
  assign ptrDif = (pushPtrGlobal - popPtrGlobal);
  always @(*) begin
    if(ptrMatch) begin
      io_occupancy = (risingOccupancy ? 4'b1110 : 4'b0000);
    end else begin
      io_occupancy = ((popPtrGlobal < pushPtrGlobal) ? ptrDif : tmp_io_occupancy);
    end
  end

  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      pushPtrGlobal <= 4'b0000;
      popPtrGlobal <= 4'b0000;
      risingOccupancy <= 1'b0;
    end else begin
      if((tmp_when < inc_num)) begin
        risingOccupancy <= 1'b1;
      end else begin
        risingOccupancy <= 1'b0;
      end
      if((inc_num != 3'b000)) begin
        if((5'h0e <= pushPtrGlobalInc)) begin
          pushPtrGlobal <= tmp_pushPtrGlobal[3:0];
        end else begin
          pushPtrGlobal <= pushPtrGlobalInc[3:0];
        end
      end
      if((dec_num != 2'b00)) begin
        if((5'h0e <= popPtrGlobalInc)) begin
          popPtrGlobal <= tmp_popPtrGlobal[3:0];
        end else begin
          popPtrGlobal <= popPtrGlobalInc[3:0];
        end
      end
      if(io_flush) begin
        pushPtrGlobal <= 4'b0000;
        popPtrGlobal <= 4'b0000;
        risingOccupancy <= 1'b0;
      end
    end
  end

  always @(posedge clk) begin
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
    end
    if(pushing_vec_2) begin
      if(tmp_3[0]) begin
        vec_0 <= io_push_2_payload;
      end
      if(tmp_3[1]) begin
        vec_1 <= io_push_2_payload;
      end
      if(tmp_3[2]) begin
        vec_2 <= io_push_2_payload;
      end
      if(tmp_3[3]) begin
        vec_3 <= io_push_2_payload;
      end
      if(tmp_3[4]) begin
        vec_4 <= io_push_2_payload;
      end
      if(tmp_3[5]) begin
        vec_5 <= io_push_2_payload;
      end
      if(tmp_3[6]) begin
        vec_6 <= io_push_2_payload;
      end
      if(tmp_3[7]) begin
        vec_7 <= io_push_2_payload;
      end
      if(tmp_3[8]) begin
        vec_8 <= io_push_2_payload;
      end
      if(tmp_3[9]) begin
        vec_9 <= io_push_2_payload;
      end
      if(tmp_3[10]) begin
        vec_10 <= io_push_2_payload;
      end
      if(tmp_3[11]) begin
        vec_11 <= io_push_2_payload;
      end
      if(tmp_3[12]) begin
        vec_12 <= io_push_2_payload;
      end
      if(tmp_3[13]) begin
        vec_13 <= io_push_2_payload;
      end
    end
    if(pushing_vec_3) begin
      if(tmp_4[0]) begin
        vec_0 <= io_push_3_payload;
      end
      if(tmp_4[1]) begin
        vec_1 <= io_push_3_payload;
      end
      if(tmp_4[2]) begin
        vec_2 <= io_push_3_payload;
      end
      if(tmp_4[3]) begin
        vec_3 <= io_push_3_payload;
      end
      if(tmp_4[4]) begin
        vec_4 <= io_push_3_payload;
      end
      if(tmp_4[5]) begin
        vec_5 <= io_push_3_payload;
      end
      if(tmp_4[6]) begin
        vec_6 <= io_push_3_payload;
      end
      if(tmp_4[7]) begin
        vec_7 <= io_push_3_payload;
      end
      if(tmp_4[8]) begin
        vec_8 <= io_push_3_payload;
      end
      if(tmp_4[9]) begin
        vec_9 <= io_push_3_payload;
      end
      if(tmp_4[10]) begin
        vec_10 <= io_push_3_payload;
      end
      if(tmp_4[11]) begin
        vec_11 <= io_push_3_payload;
      end
      if(tmp_4[12]) begin
        vec_12 <= io_push_3_payload;
      end
      if(tmp_4[13]) begin
        vec_13 <= io_push_3_payload;
      end
    end
  end


endmodule

module PopToleranceFifo (
  input               push_valid,
  output              push_ready,
  input      [2:0]    push_payload,
  input               pop_valid,
  output              pop_ready,
  input      [2:0]    pop_pop_req_num,
  output reg [2:0]    pop_pop_out_data,
  output reg          pop_pop_real,
  output     [2:0]    head,
  input               flush,
  output reg [2:0]    occupancy,
  input               clk,
  input               resetn
);

  reg        [2:0]    tmp_tmp_pop_pop_out_data;
  wire       [2:0]    tmp_occupancy;
  reg        [2:0]    vec_0;
  reg        [2:0]    vec_1;
  reg        [2:0]    vec_2;
  reg        [2:0]    vec_3;
  reg        [2:0]    vec_4;
  reg        [2:0]    vec_5;
  reg        [2:0]    pushPtrGlobal;
  reg        [2:0]    popPtrGlobal;
  wire                ptrMatch;
  reg                 risingOccupancy;
  wire                empty;
  wire                full;
  wire                pushing;
  wire       [2:0]    tmp_pop_pop_out_data;
  wire       [7:0]    tmp_1;
  wire       [2:0]    tmp_vec_0;
  wire       [7:0]    tmp_2;
  wire       [2:0]    ptrDif;

  assign tmp_occupancy = (3'b110 + ptrDif);
  always @(*) begin
    case(popPtrGlobal)
      3'b000 : tmp_tmp_pop_pop_out_data = vec_0;
      3'b001 : tmp_tmp_pop_pop_out_data = vec_1;
      3'b010 : tmp_tmp_pop_pop_out_data = vec_2;
      3'b011 : tmp_tmp_pop_pop_out_data = vec_3;
      3'b100 : tmp_tmp_pop_pop_out_data = vec_4;
      default : tmp_tmp_pop_pop_out_data = vec_5;
    endcase
  end

  assign ptrMatch = (pushPtrGlobal == popPtrGlobal);
  assign empty = (ptrMatch && (! risingOccupancy));
  assign full = (ptrMatch && risingOccupancy);
  assign pushing = (push_valid && push_ready);
  assign push_ready = (! full);
  assign pop_ready = ((! empty) || (empty && (head != 3'b000)));
  assign tmp_pop_pop_out_data = tmp_tmp_pop_pop_out_data;
  assign tmp_1 = ({7'd0,1'b1} <<< popPtrGlobal);
  assign head = tmp_pop_pop_out_data;
  always @(*) begin
    pop_pop_out_data = tmp_pop_pop_out_data;
    if((pop_valid && pop_ready)) begin
      if((tmp_pop_pop_out_data == pop_pop_req_num)) begin
        pop_pop_out_data = 3'b000;
      end
    end
  end

  always @(*) begin
    pop_pop_real = 1'b0;
    if((pop_valid && pop_ready)) begin
      if((tmp_pop_pop_out_data == pop_pop_req_num)) begin
        pop_pop_real = 1'b1;
      end
    end
  end

  assign tmp_vec_0 = (tmp_pop_pop_out_data - pop_pop_req_num);
  assign tmp_2 = ({7'd0,1'b1} <<< pushPtrGlobal);
  assign ptrDif = (pushPtrGlobal - popPtrGlobal);
  always @(*) begin
    if(ptrMatch) begin
      occupancy = (risingOccupancy ? 3'b110 : 3'b000);
    end else begin
      occupancy = ((popPtrGlobal < pushPtrGlobal) ? ptrDif : tmp_occupancy);
    end
  end

  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      pushPtrGlobal <= 3'b000;
      popPtrGlobal <= 3'b000;
      risingOccupancy <= 1'b0;
    end else begin
      if((pop_valid && pop_ready)) begin
        if((tmp_pop_pop_out_data == pop_pop_req_num)) begin
          if((3'b101 <= popPtrGlobal)) begin
            popPtrGlobal <= 3'b000;
          end else begin
            popPtrGlobal <= (popPtrGlobal + 3'b001);
          end
        end
      end
      if(pushing) begin
        if((3'b101 <= pushPtrGlobal)) begin
          pushPtrGlobal <= 3'b000;
        end else begin
          pushPtrGlobal <= (pushPtrGlobal + 3'b001);
        end
      end
      if((pushing != pop_pop_real)) begin
        risingOccupancy <= pushing;
      end
      if(flush) begin
        pushPtrGlobal <= 3'b000;
        popPtrGlobal <= 3'b000;
        risingOccupancy <= 1'b0;
      end
    end
  end

  always @(posedge clk) begin
    if((pop_valid && pop_ready)) begin
      if(tmp_1[0]) begin
        vec_0 <= tmp_vec_0;
      end
      if(tmp_1[1]) begin
        vec_1 <= tmp_vec_0;
      end
      if(tmp_1[2]) begin
        vec_2 <= tmp_vec_0;
      end
      if(tmp_1[3]) begin
        vec_3 <= tmp_vec_0;
      end
      if(tmp_1[4]) begin
        vec_4 <= tmp_vec_0;
      end
      if(tmp_1[5]) begin
        vec_5 <= tmp_vec_0;
      end
    end
    if(pushing) begin
      if(tmp_2[0]) begin
        vec_0 <= push_payload;
      end
      if(tmp_2[1]) begin
        vec_1 <= push_payload;
      end
      if(tmp_2[2]) begin
        vec_2 <= push_payload;
      end
      if(tmp_2[3]) begin
        vec_3 <= push_payload;
      end
      if(tmp_2[4]) begin
        vec_4 <= push_payload;
      end
      if(tmp_2[5]) begin
        vec_5 <= push_payload;
      end
    end
  end


endmodule

module StreamFifoWithHead (
  input               io_push_valid,
  output              io_push_ready,
  input      [31:0]   io_push_payload,
  output              io_pop_valid,
  input               io_pop_ready,
  output     [31:0]   io_pop_payload,
  output     [31:0]   io_head,
  input               io_flush,
  output reg [2:0]    io_occupancy,
  input               clk,
  input               resetn
);

  wire       [2:0]    tmp_pushPtr_valueNext;
  wire       [0:0]    tmp_pushPtr_valueNext_1;
  wire       [2:0]    tmp_popPtr_valueNext;
  wire       [0:0]    tmp_popPtr_valueNext_1;
  reg        [31:0]   tmp_readed;
  wire       [2:0]    tmp_io_occupancy;
  reg        [31:0]   vec_0;
  reg        [31:0]   vec_1;
  reg        [31:0]   vec_2;
  reg        [31:0]   vec_3;
  reg        [31:0]   vec_4;
  reg        [31:0]   vec_5;
  reg                 pushPtr_willIncrement;
  reg                 pushPtr_willClear;
  reg        [2:0]    pushPtr_valueNext;
  reg        [2:0]    pushPtr_value;
  wire                pushPtr_willOverflowIfInc;
  wire                pushPtr_willOverflow;
  reg                 popPtr_willIncrement;
  reg                 popPtr_willClear;
  reg        [2:0]    popPtr_valueNext;
  reg        [2:0]    popPtr_value;
  wire                popPtr_willOverflowIfInc;
  wire                popPtr_willOverflow;
  wire                ptrMatch;
  reg                 risingOccupancy;
  wire                empty;
  wire                full;
  wire                pushing;
  wire                popping;
  wire       [31:0]   readed;
  wire       [7:0]    tmp_1;
  wire       [2:0]    ptrDif;

  assign tmp_pushPtr_valueNext_1 = pushPtr_willIncrement;
  assign tmp_pushPtr_valueNext = {2'd0, tmp_pushPtr_valueNext_1};
  assign tmp_popPtr_valueNext_1 = popPtr_willIncrement;
  assign tmp_popPtr_valueNext = {2'd0, tmp_popPtr_valueNext_1};
  assign tmp_io_occupancy = (3'b110 + ptrDif);
  always @(*) begin
    case(popPtr_value)
      3'b000 : tmp_readed = vec_0;
      3'b001 : tmp_readed = vec_1;
      3'b010 : tmp_readed = vec_2;
      3'b011 : tmp_readed = vec_3;
      3'b100 : tmp_readed = vec_4;
      default : tmp_readed = vec_5;
    endcase
  end

  always @(*) begin
    pushPtr_willIncrement = 1'b0;
    if(pushing) begin
      pushPtr_willIncrement = 1'b1;
    end
  end

  always @(*) begin
    pushPtr_willClear = 1'b0;
    if(io_flush) begin
      pushPtr_willClear = 1'b1;
    end
  end

  assign pushPtr_willOverflowIfInc = (pushPtr_value == 3'b101);
  assign pushPtr_willOverflow = (pushPtr_willOverflowIfInc && pushPtr_willIncrement);
  always @(*) begin
    if(pushPtr_willOverflow) begin
      pushPtr_valueNext = 3'b000;
    end else begin
      pushPtr_valueNext = (pushPtr_value + tmp_pushPtr_valueNext);
    end
    if(pushPtr_willClear) begin
      pushPtr_valueNext = 3'b000;
    end
  end

  always @(*) begin
    popPtr_willIncrement = 1'b0;
    if(popping) begin
      popPtr_willIncrement = 1'b1;
    end
  end

  always @(*) begin
    popPtr_willClear = 1'b0;
    if(io_flush) begin
      popPtr_willClear = 1'b1;
    end
  end

  assign popPtr_willOverflowIfInc = (popPtr_value == 3'b101);
  assign popPtr_willOverflow = (popPtr_willOverflowIfInc && popPtr_willIncrement);
  always @(*) begin
    if(popPtr_willOverflow) begin
      popPtr_valueNext = 3'b000;
    end else begin
      popPtr_valueNext = (popPtr_value + tmp_popPtr_valueNext);
    end
    if(popPtr_willClear) begin
      popPtr_valueNext = 3'b000;
    end
  end

  assign ptrMatch = (pushPtr_value == popPtr_value);
  assign empty = (ptrMatch && (! risingOccupancy));
  assign full = (ptrMatch && risingOccupancy);
  assign pushing = (io_push_valid && io_push_ready);
  assign popping = (io_pop_valid && io_pop_ready);
  assign io_push_ready = (! full);
  assign readed = tmp_readed;
  assign io_head = readed;
  assign io_pop_valid = (! empty);
  assign io_pop_payload = readed;
  assign tmp_1 = ({7'd0,1'b1} <<< pushPtr_value);
  assign ptrDif = (pushPtr_value - popPtr_value);
  always @(*) begin
    if(ptrMatch) begin
      io_occupancy = (risingOccupancy ? 3'b110 : 3'b000);
    end else begin
      io_occupancy = ((popPtr_value < pushPtr_value) ? ptrDif : tmp_io_occupancy);
    end
  end

  always @(posedge clk or negedge resetn) begin
    if(!resetn) begin
      pushPtr_value <= 3'b000;
      popPtr_value <= 3'b000;
      risingOccupancy <= 1'b0;
    end else begin
      pushPtr_value <= pushPtr_valueNext;
      popPtr_value <= popPtr_valueNext;
      if((pushing != popping)) begin
        risingOccupancy <= pushing;
      end
      if(io_flush) begin
        risingOccupancy <= 1'b0;
      end
    end
  end

  always @(posedge clk) begin
    if(pushing) begin
      if(tmp_1[0]) begin
        vec_0 <= io_push_payload;
      end
      if(tmp_1[1]) begin
        vec_1 <= io_push_payload;
      end
      if(tmp_1[2]) begin
        vec_2 <= io_push_payload;
      end
      if(tmp_1[3]) begin
        vec_3 <= io_push_payload;
      end
      if(tmp_1[4]) begin
        vec_4 <= io_push_payload;
      end
      if(tmp_1[5]) begin
        vec_5 <= io_push_payload;
      end
    end
  end


endmodule
