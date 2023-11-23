// Generator : SpinalHDL v1.9.3    git head : 029104c77a54c53f1edda327a3bea333f7d65fd9
// Component : Shifter
// Git hash  : 78fdf551202278e69ad6c2e3e8287253cf0938ec

`timescale 1ns/1ps

module Shifter (
  input               io_fu_if_valid,
  output              io_fu_if_ready,
  input      [2:0]    io_fu_if_payload_fu,
  input      [5:0]    io_fu_if_payload_op,
  input      [31:0]   io_fu_if_payload_rs1,
  input      [31:0]   io_fu_if_payload_rs2,
  input      [5:0]    io_fu_if_payload_rd_index,
  output              io_result_valid,
  input               io_result_ready,
  output     [31:0]   io_result_payload
);
  localparam ALU = 3'd0;
  localparam MUL = 3'd1;
  localparam LSU = 3'd2;
  localparam BRU = 3'd3;
  localparam CSR = 3'd4;
  localparam BIT_1 = 3'd5;
  localparam IDLE = 6'd0;
  localparam ADD = 6'd1;
  localparam SUB = 6'd2;
  localparam SLT = 6'd3;
  localparam SLTU = 6'd4;
  localparam AND_1 = 6'd5;
  localparam OR_1 = 6'd6;
  localparam XOR_1 = 6'd7;
  localparam SLL_1 = 6'd8;
  localparam SRL_1 = 6'd9;
  localparam SRA_1 = 6'd10;
  localparam LOAD = 6'd11;
  localparam LOADU = 6'd12;
  localparam STORE = 6'd13;
  localparam JAL = 6'd14;
  localparam JALR = 6'd15;
  localparam BEQ = 6'd16;
  localparam BNE = 6'd17;
  localparam BLT = 6'd18;
  localparam BGE = 6'd19;
  localparam BLTU = 6'd20;
  localparam BGEU = 6'd21;
  localparam MUL = 6'd22;
  localparam MULH = 6'd23;
  localparam MULHSU = 6'd24;
  localparam MULHU = 6'd25;
  localparam DIV = 6'd26;
  localparam DIVU = 6'd27;
  localparam REM_1 = 6'd28;
  localparam REMU = 6'd29;
  localparam CSRRW = 6'd30;
  localparam CSRRS = 6'd31;
  localparam CSRRC = 6'd32;
  localparam CSRRWI = 6'd33;
  localparam CSRRSI = 6'd34;
  localparam CSRRCI = 6'd35;

  reg                 shifter_1_direction;
  reg                 shifter_1_signed_1;
  wire       [4:0]    shifter_1_shift_amount;
  wire       [31:0]   shifter_1_data_out;
  wire       [31:0]   src2;
  `ifndef SYNTHESIS
  reg [39:0] io_fu_if_payload_fu_string;
  reg [47:0] io_fu_if_payload_op_string;
  `endif


  barrel_shift shifter_1 (
    .rotate_en    (1'b0                       ), //i
    .direction    (shifter_1_direction        ), //i
    .signed_1     (shifter_1_signed_1         ), //i
    .shift_amount (shifter_1_shift_amount[4:0]), //i
    .data_in      (io_fu_if_payload_rs1[31:0] ), //i
    .data_out     (shifter_1_data_out[31:0]   )  //o
  );
  `ifndef SYNTHESIS
  always @(*) begin
    case(io_fu_if_payload_fu)
      ALU : io_fu_if_payload_fu_string = "ALU  ";
      MUL : io_fu_if_payload_fu_string = "MUL  ";
      LSU : io_fu_if_payload_fu_string = "LSU  ";
      BRU : io_fu_if_payload_fu_string = "BRU  ";
      CSR : io_fu_if_payload_fu_string = "CSR  ";
      BIT_1 : io_fu_if_payload_fu_string = "BIT_1";
      default : io_fu_if_payload_fu_string = "?????";
    endcase
  end
  always @(*) begin
    case(io_fu_if_payload_op)
      IDLE : io_fu_if_payload_op_string = "IDLE  ";
      ADD : io_fu_if_payload_op_string = "ADD   ";
      SUB : io_fu_if_payload_op_string = "SUB   ";
      SLT : io_fu_if_payload_op_string = "SLT   ";
      SLTU : io_fu_if_payload_op_string = "SLTU  ";
      AND_1 : io_fu_if_payload_op_string = "AND_1 ";
      OR_1 : io_fu_if_payload_op_string = "OR_1  ";
      XOR_1 : io_fu_if_payload_op_string = "XOR_1 ";
      SLL_1 : io_fu_if_payload_op_string = "SLL_1 ";
      SRL_1 : io_fu_if_payload_op_string = "SRL_1 ";
      SRA_1 : io_fu_if_payload_op_string = "SRA_1 ";
      LOAD : io_fu_if_payload_op_string = "LOAD  ";
      LOADU : io_fu_if_payload_op_string = "LOADU ";
      STORE : io_fu_if_payload_op_string = "STORE ";
      JAL : io_fu_if_payload_op_string = "JAL   ";
      JALR : io_fu_if_payload_op_string = "JALR  ";
      BEQ : io_fu_if_payload_op_string = "BEQ   ";
      BNE : io_fu_if_payload_op_string = "BNE   ";
      BLT : io_fu_if_payload_op_string = "BLT   ";
      BGE : io_fu_if_payload_op_string = "BGE   ";
      BLTU : io_fu_if_payload_op_string = "BLTU  ";
      BGEU : io_fu_if_payload_op_string = "BGEU  ";
      MUL : io_fu_if_payload_op_string = "MUL   ";
      MULH : io_fu_if_payload_op_string = "MULH  ";
      MULHSU : io_fu_if_payload_op_string = "MULHSU";
      MULHU : io_fu_if_payload_op_string = "MULHU ";
      DIV : io_fu_if_payload_op_string = "DIV   ";
      DIVU : io_fu_if_payload_op_string = "DIVU  ";
      REM_1 : io_fu_if_payload_op_string = "REM_1 ";
      REMU : io_fu_if_payload_op_string = "REMU  ";
      CSRRW : io_fu_if_payload_op_string = "CSRRW ";
      CSRRS : io_fu_if_payload_op_string = "CSRRS ";
      CSRRC : io_fu_if_payload_op_string = "CSRRC ";
      CSRRWI : io_fu_if_payload_op_string = "CSRRWI";
      CSRRSI : io_fu_if_payload_op_string = "CSRRSI";
      CSRRCI : io_fu_if_payload_op_string = "CSRRCI";
      default : io_fu_if_payload_op_string = "??????";
    endcase
  end
  `endif

  assign io_fu_if_ready = io_result_ready;
  assign src2 = io_fu_if_payload_rs2;
  assign io_result_valid = io_fu_if_valid;
  assign shifter_1_shift_amount = src2[4:0];
  always @(*) begin
    shifter_1_direction = 1'b1;
    case(io_fu_if_payload_op)
      SLL_1 : begin
        shifter_1_direction = 1'b1;
      end
      SRL_1 : begin
        shifter_1_direction = 1'b0;
      end
      SRA_1 : begin
        shifter_1_direction = 1'b0;
      end
      default : begin
      end
    endcase
  end

  always @(*) begin
    shifter_1_signed_1 = 1'b0;
    case(io_fu_if_payload_op)
      SLL_1 : begin
        shifter_1_signed_1 = 1'b0;
      end
      SRL_1 : begin
        shifter_1_signed_1 = 1'b0;
      end
      SRA_1 : begin
        shifter_1_signed_1 = 1'b1;
      end
      default : begin
      end
    endcase
  end

  assign io_result_payload = shifter_1_data_out;

endmodule

module barrel_shift (
  input               rotate_en,
  input               direction,
  input               signed_1,
  input      [4:0]    shift_amount,
  input      [31:0]   data_in,
  output reg [31:0]   data_out
);

  wire       [63:0]   tmp_shifted_data;
  wire       [63:0]   tmp_shifted_data_1;
  wire       [31:0]   tmp_shifted_data_2;
  wire       [31:0]   tmp_data_out;
  wire       [0:0]    tmp_data_out_1;
  wire       [63:0]   tmp_shifted_data_3;
  wire       [63:0]   tmp_shifted_data_4;
  wire       [63:0]   tmp_shifted_data_5;
  wire       [31:0]   tmp_data_out_2;
  wire       [0:0]    tmp_data_out_3;
  reg        [63:0]   shifted_data;

  assign tmp_shifted_data = (tmp_shifted_data_1 <<< shift_amount);
  assign tmp_shifted_data_2 = data_in;
  assign tmp_shifted_data_1 = {32'd0, tmp_shifted_data_2};
  assign tmp_data_out_1 = rotate_en;
  assign tmp_data_out = {{31{tmp_data_out_1[0]}}, tmp_data_out_1};
  assign tmp_shifted_data_3 = ($signed(tmp_shifted_data_4) >>> shift_amount);
  assign tmp_shifted_data_4 = {data_in,32'h00000000};
  assign tmp_shifted_data_5 = ({data_in,32'h00000000} >>> shift_amount);
  assign tmp_data_out_3 = rotate_en;
  assign tmp_data_out_2 = {{31{tmp_data_out_3[0]}}, tmp_data_out_3};
  always @(*) begin
    if(direction) begin
      shifted_data = tmp_shifted_data;
    end else begin
      if(signed_1) begin
        shifted_data = tmp_shifted_data_3;
      end else begin
        shifted_data = tmp_shifted_data_5;
      end
    end
  end

  always @(*) begin
    if(direction) begin
      data_out = (shifted_data[31 : 0] | (shifted_data[63 : 32] & tmp_data_out));
    end else begin
      data_out = (shifted_data[63 : 32] | (shifted_data[31 : 0] & tmp_data_out_2));
    end
  end


endmodule
