
`include "scr1_arch_description.svh"
`include "scr1_memif.svh"
`include "scr1_riscv_isa_decoding.svh"
`include "scr1_csr.svh"


module scr1_pipe_top (
    // Common
    input   logic                                       pipe_rst_n,                 // Pipe reset

    input   logic                                       clk,                        // Pipe clock

    // Instruction Memory Interface
    output  logic                                       pipe2imem_req_o,            // IMEM request
    output  type_scr1_mem_cmd_e                         pipe2imem_cmd_o,            // IMEM command
    output  logic [`SCR1_IMEM_AWIDTH-1:0]               pipe2imem_addr_o,           // IMEM address
    input   logic                                       imem2pipe_req_ack_i,        // IMEM request acknowledge
    input   logic [`SCR1_IMEM_DWIDTH-1:0]               imem2pipe_rdata_i,          // IMEM read data
    input   type_scr1_mem_resp_e                        imem2pipe_resp_i,           // IMEM response

    // Data Memory Interface
    output  logic                                       pipe2dmem_req_o,            // DMEM request
    output  type_scr1_mem_cmd_e                         pipe2dmem_cmd_o,            // DMEM command
    output  type_scr1_mem_width_e                       pipe2dmem_width_o,          // DMEM data width
    output  logic [`SCR1_DMEM_AWIDTH-1:0]               pipe2dmem_addr_o,           // DMEM address
    output  logic [`SCR1_DMEM_DWIDTH-1:0]               pipe2dmem_wdata_o,          // DMEM write data
    input   logic                                       dmem2pipe_req_ack_i,        // DMEM request acknowledge
    input   logic [`SCR1_DMEM_DWIDTH-1:0]               dmem2pipe_rdata_i,          // DMEM read data
    input   type_scr1_mem_resp_e                        dmem2pipe_resp_i,           // DMEM response


    input   logic                                       soc2pipe_irq_ext_i,         // External interrupt request
    input   logic                                       soc2pipe_irq_soft_i,        // Software generated interrupt request
    input   logic                                       soc2pipe_irq_mtimer_i,      // Machine timer interrupt request

    // Memory-mapped external timer
    input   logic [63:0]                                soc2pipe_mtimer_val_i,      // Machine timer value
    // Fuse
    input   logic [`SCR1_XLEN-1:0]                      soc2pipe_fuse_mhartid_i     // Fuse MHARTID value
);

wire resetn;
assign resetn = pipe_rst_n;

logic ibus_cmd_valid               ; 
logic ibus_cmd_ready               ; 
logic [31:0] ibus_cmd_payload_address     ; 
logic ibus_rsp_valid               ; 
logic ibus_rsp_ready               ; 
logic [31:0] ibus_rsp_payload_rdata       ; 
logic dbus_cmd_valid               ; 
logic dbus_cmd_ready               ; 
logic [31:0] dbus_cmd_payload_address     ; 
logic dbus_cmd_payload_write       ; 
logic [31:0] dbus_cmd_payload_wdata       ; 
logic [3:0] dbus_cmd_payload_wmask       ; 
logic dbus_rsp_valid               ; 
logic dbus_rsp_ready               ; 
logic [31:0] dbus_rsp_payload_rdata       ; 

assign pipe2imem_req_o = ibus_cmd_valid;
assign pipe2imem_cmd_o = SCR1_MEM_CMD_RD; 
assign pipe2imem_addr_o = ibus_cmd_payload_address;
assign ibus_cmd_ready = imem2pipe_req_ack_i;
assign ibus_rsp_payload_rdata = imem2pipe_rdata_i;
assign ibus_rsp_valid = imem2pipe_resp_i == SCR1_MEM_RESP_RDY_OK;

logic [2:0] width;
assign width = {2'd0,dbus_cmd_payload_wmask[0]} + {2'd0,dbus_cmd_payload_wmask[1]} +
               {2'd0,dbus_cmd_payload_wmask[2]} + {2'd0,dbus_cmd_payload_wmask[3]};
always@(*) begin
  if(dbus_cmd_payload_write) begin
    case(width) 
      3'd0: pipe2dmem_width_o = SCR1_MEM_WIDTH_BYTE;
      3'd1: pipe2dmem_width_o = SCR1_MEM_WIDTH_BYTE;
      3'd2: pipe2dmem_width_o = SCR1_MEM_WIDTH_HWORD;
      3'd4: pipe2dmem_width_o = SCR1_MEM_WIDTH_WORD;
      default: pipe2dmem_width_o = SCR1_MEM_WIDTH_BYTE;
    endcase
  end
  else begin
    pipe2dmem_width_o = SCR1_MEM_WIDTH_WORD;
  end
end

assign pipe2dmem_req_o = dbus_cmd_valid;
assign pipe2dmem_cmd_o = type_scr1_mem_cmd_e'(dbus_cmd_payload_write);
assign pipe2dmem_addr_o = dbus_cmd_payload_address;
assign pipe2dmem_wdata_o = dbus_cmd_payload_wdata;
assign dbus_cmd_ready = dmem2pipe_req_ack_i;
assign dbus_rsp_payload_rdata = dmem2pipe_rdata_i;
assign dbus_rsp_valid = dmem2pipe_resp_i == SCR1_MEM_RESP_RDY_OK;

ProteusCore U_PROTEUSCORE_0(
    .resetn                         ( resetn                        ),
    .ibus_cmd_valid                 ( ibus_cmd_valid                ),
    .ibus_cmd_ready                 ( ibus_cmd_ready                ),
    .ibus_cmd_payload_address       ( ibus_cmd_payload_address      ),
    .ibus_rsp_valid                 ( ibus_rsp_valid                ),
    .ibus_rsp_ready                 ( ibus_rsp_ready                ),
    .ibus_rsp_payload_rdata         ( ibus_rsp_payload_rdata        ),
    .dbus_cmd_valid                 ( dbus_cmd_valid                ),
    .dbus_cmd_ready                 ( dbus_cmd_ready                ),
    .dbus_cmd_payload_address       ( dbus_cmd_payload_address      ),
    .dbus_cmd_payload_write         ( dbus_cmd_payload_write        ),
    .dbus_cmd_payload_wdata         ( dbus_cmd_payload_wdata        ),
    .dbus_cmd_payload_wmask         ( dbus_cmd_payload_wmask        ),
    .dbus_rsp_valid                 ( dbus_rsp_valid                ),
    .dbus_rsp_ready                 ( dbus_rsp_ready                ),
    .dbus_rsp_payload_rdata         ( dbus_rsp_payload_rdata        ),
    .clk                            ( clk                           )
);


endmodule
