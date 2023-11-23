// Generator : SpinalHDL v1.9.3    git head : 029104c77a54c53f1edda327a3bea333f7d65fd9
// Component : IntAlu
// Git hash  : 78fdf551202278e69ad6c2e3e8287253cf0938ec

`timescale 1ns/1ps

module IntAlu (
  input               io_fu_if_valid,
  output              io_fu_if_ready,
  input      [2:0]    io_fu_if_payload_fu,
  input      [5:0]    io_fu_if_payload_op,
  input      [31:0]   io_fu_if_payload_rs1,
  input      [31:0]   io_fu_if_payload_rs2,
  input      [5:0]    io_fu_if_payload_rd_index,
  output              io_result_valid,
  input               io_result_ready,
  output reg [31:0]   io_result_payload
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

  wire       [0:0]    tmp_io_result_payload;
  wire       [31:0]   src1;
  wire       [31:0]   src2;
  reg                 is_minus_flag;
  wire       [31:0]   src2_neg;
  wire       [31:0]   add_pinB;
  wire       [32:0]   add_result;
  `ifndef SYNTHESIS
  reg [39:0] io_fu_if_payload_fu_string;
  reg [47:0] io_fu_if_payload_op_string;
  `endif


  assign tmp_io_result_payload = add_result[32];
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
  assign src1 = io_fu_if_payload_rs1;
  assign src2 = io_fu_if_payload_rs2;
  assign io_result_valid = io_fu_if_valid;
  always @(*) begin
    io_result_payload = 32'h00000000;
    case(io_fu_if_payload_op)
      CSRRW : begin
        io_result_payload = add_result[31 : 0];
      end
      SUB : begin
        io_result_payload = add_result[31 : 0];
      end
      SLT, SLTU : begin
        io_result_payload = {31'd0, tmp_io_result_payload};
      end
      XOR_1 : begin
        io_result_payload = (src1 ^ src2);
      end
      OR_1 : begin
        io_result_payload = (src1 | src2);
      end
      AND_1 : begin
        io_result_payload = (src1 & src2);
      end
      default : begin
      end
    endcase
  end

  assign src2_neg = ((~ src2) + 32'h00000001);
  assign add_pinB = (is_minus_flag ? src2_neg : src2);
  assign add_result = ({1'b0,src1} + {1'b0,add_pinB});
  always @(*) begin
    is_minus_flag = 1'b0;
    case(io_fu_if_payload_op)
      CSRRW : begin
        is_minus_flag = 1'b0;
      end
      SUB : begin
        is_minus_flag = 1'b1;
      end
      SLT, SLTU : begin
        is_minus_flag = 1'b1;
      end
      default : begin
      end
    endcase
  end


endmodule
