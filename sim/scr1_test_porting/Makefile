#------------------------------------------------------------------------------
# Makefile for SCR1
#------------------------------------------------------------------------------

# PARAMETERS

# BUS = <AHB, AXI>
export CFG      ?= MYCORE
export BUS      ?= AHB

override ARCH         := IM
override VECT_IRQ     := 0
override IPIC         := 0
override TCM          := 1
override SIM_CFG_DEF  := SCR1_CFG_RV32EC_MY
# export all overrided variables
export ARCH
export VECT_IRQ
export IPIC
export TCM
export SIM_CFG_DEF


ARCH_lowercase = $(shell echo $(ARCH) | tr A-Z a-z)
BUS_lowercase  = $(shell echo $(BUS)  | tr A-Z a-z)

ifneq (,$(findstring e,$(ARCH_lowercase)))
	EXT_CFLAGS += -D__RVE_EXT
endif
ifneq (,$(findstring c,$(ARCH_lowercase)))
	EXT_CFLAGS += -D__RVC_EXT
endif

# Use this parameter to enable tracelog
TRACE ?= 0

ifeq ($(TRACE), 1)
    export SIM_TRACE_DEF = SCR1_TRACE_LOG_EN
else
    export SIM_TRACE_DEF = SCR1_TRACE_LOG_DIS
endif


# Use this parameter to pass additional options for simulation build command
SIM_BUILD_OPTS ?=

# Use this parameter to set the list of tests to run
# TARGETS = <riscv_isa, riscv_compliance, riscv_arch, coremark, dhrystone21, hello, isr_sample>
export TARGETS :=


export XLEN  ?= 32
export ABI   ?= ilp32
# Testbench memory delay patterns\
  (FFFFFFFF - no delay, 00000000 - random delay, 00000001 - max delay)
imem_pattern ?= FFFFFFFF
dmem_pattern ?= FFFFFFFF

VCS_OPTS       ?=
MODELSIM_OPTS  ?=
NCSIM_OPTS     ?=
VERILATOR_OPTS ?=

current_goal := $(subst run_,,$(MAKECMDGOALS))
current_goal := $(subst build_,,$(current_goal))

# current_goal := $(MAKECMDGOALS:run_%=%)
ifeq ($(current_goal),)
    current_goal := verilator
endif

# Paths
export root_dir := $(shell pwd)
export scr1_dir := $(root_dir)/../../dependencies/other_cores/scr1
export tst_dir  := $(scr1_dir)/sim/tests
export inc_dir  := $(tst_dir)/common
export bld_dir  := $(root_dir)/build/$(current_goal)_$(BUS)_$(CFG)_$(ARCH)_IPIC_$(IPIC)_TCM_$(TCM)_VIRQ_$(VECT_IRQ)_TRACE_$(TRACE)

test_results := $(bld_dir)/test_results.txt
test_info    := $(bld_dir)/test_info
sim_results  := $(bld_dir)/sim_results.txt

todo_list    := $(bld_dir)/todo.txt
# Environment
export CROSS_PREFIX  ?= riscv64-unknown-elf-
export RISCV_GCC     ?= $(CROSS_PREFIX)gcc
export RISCV_OBJDUMP ?= $(CROSS_PREFIX)objdump -D
export RISCV_OBJCOPY ?= $(CROSS_PREFIX)objcopy -O verilog
export RISCV_READELF ?= $(CROSS_PREFIX)readelf -s
#--
ifneq (,$(findstring axi,$(BUS_lowercase)))
export rtl_top_files := axi_top.files
export rtl_tb_files  := axi_tb.files
export top_module    := scr1_top_tb_axi
else
export rtl_top_files := ahb_top.files
export rtl_tb_files  := ahb_tb.files
export top_module    := scr1_top_tb_ahb
endif

rtl_src_dir := $(root_dir)/
rtl_core_files ?= mycore.files
rtl_top_files ?= ahb_top.files
rtl_tb_files ?= ahb_tb.files
rtl_inc_dir ?= $(root_dir)/includes
# rtl_inc_tb_dir ?= $(scr1_dir)/src/tb
rtl_inc_tb_dir ?= $(root_dir)/includes
top_module ?= scr1_top_tb_ahb

rtl_core_list := $(addprefix $(rtl_src_dir),$(shell cat $(rtl_src_dir)$(rtl_core_files)))
rtl_top_list := $(addprefix $(scr1_dir)/src/,$(shell cat $(scr1_dir)/src/$(rtl_top_files)))
rtl_tb_list := $(addprefix $(scr1_dir)/src/,$(shell cat $(scr1_dir)/src/$(rtl_tb_files)))
sv_list := $(rtl_core_list) $(rtl_top_list) $(rtl_tb_list)


ifeq ($(MAKECMDGOALS), $(filter $(MAKECMDGOALS),run_verilator run_verilator_wf build_verilator build_verilator_wf))
ifeq ($(BUS),AHB)
export scr1_wrapper  := $(root_dir)/verilator_wrap/scr1_ahb_wrapper.c
endif
ifeq ($(BUS),AXI)
export scr1_wrapper  := $(root_dir)/verilator_wrap/scr1_axi_wrapper.c
endif
export verilator_ver ?= $(shell  expr `verilator --version | cut -f2 -d' '`)
endif


ifneq (,$(findstring e,$(ARCH_lowercase)))
# Tests can be compiled for RVE only if gcc version 8.0.0 or higher
    GCCVERSIONGT7 := $(shell expr `$(RISCV_GCC) -dumpfullversion | cut -f1 -d'.'` \> 7)
    ifeq "$(GCCVERSIONGT7)" "1"
        ABI := ilp32e
    endif
endif

#--
ifeq (,$(findstring e,$(ARCH_lowercase)))
    # Comment this target if you don't want to run the riscv_isa
    TARGETS += riscv_isa

    # Comment this target if you don't want to run the riscv_compliance
    TARGETS += riscv_compliance
endif

# Comment this target if you don't want to run the riscv_arch
TARGETS += riscv_arch

# Comment this target if you don't want to run the isr_sample
TARGETS += isr_sample

# Comment this target if you don't want to run the coremark
TARGETS += coremark

# Comment this target if you don't want to run the dhrystone
TARGETS += dhrystone21

# Comment this target if you don't want to run the hello test
TARGETS += hello

# When RVE extension is on, we want to exclude some tests, even if they are given from the command line
ifneq (,$(findstring e,$(ARCH_lowercase)))
    excluded := riscv_isa riscv_compliance
    excluded := $(filter $(excluded), $(TARGETS))
    $(foreach test,$(excluded),$(warning Warning! $(test) test is not intended to run on an RVE extension, skipping it))
    override TARGETS := $(filter-out $(excluded), $(TARGETS))
endif

ARCH_lowercase := $(ARCH_lowercase)_zicsr

ifeq (,$(strip $(TARGETS)))
    $(error Error! No tests included, aborting)
endif

# Targets
.PHONY: tests run_modelsim run_vcs run_ncsim run_verilator run_verilator_wf build_verilator_wf

default: clean_test_list run_verilator

clean_test_list:
	rm -f $(test_info)

echo_out: tests
	@echo "                          Test               | build | simulation " ;
	@echo "$$(cat $(test_results))"

tests: $(TARGETS)

$(test_info): clean_test_list clean_hex tests
	cd $(bld_dir)

isr_sample: | $(bld_dir)
	$(MAKE) -C $(tst_dir)/isr_sample ARCH=$(ARCH_lowercase) IPIC=$(IPIC) VECT_IRQ=$(VECT_IRQ)

dhrystone21: | $(bld_dir)
	$(MAKE) -C $(tst_dir)/benchmarks/dhrystone21 EXT_CFLAGS="$(EXT_CFLAGS)" ARCH=$(ARCH_lowercase)

coremark: | $(bld_dir)
	-$(MAKE) -C $(tst_dir)/benchmarks/coremark EXT_CFLAGS="$(EXT_CFLAGS)" ARCH=$(ARCH_lowercase)

riscv_isa: | $(bld_dir)
	$(MAKE) -C $(tst_dir)/riscv_isa ARCH=$(ARCH_lowercase)

riscv_compliance: | $(bld_dir)
	$(MAKE) -C $(tst_dir)/riscv_compliance ARCH=$(ARCH_lowercase)

riscv_arch: | $(bld_dir)
	$(MAKE) -C $(tst_dir)/riscv_arch ARCH=$(ARCH_lowercase)

hello: | $(bld_dir)
	-$(MAKE) -C $(tst_dir)/hello EXT_CFLAGS="$(EXT_CFLAGS)" ARCH=$(ARCH_lowercase)

clean_hex: | $(bld_dir)
	$(RM) $(bld_dir)/*.hex

$(bld_dir):
	mkdir -p $(bld_dir)

build_modelsim: $(sv_list) $(test_info)
	cd $(bld_dir); \
	vlib work; \
	vmap work work; \
	vlog -work work -O1 -mfcu -sv \
	+incdir+$(rtl_inc_dir) \
	+incdir+$(rtl_inc_tb_dir) \
	+nowarnSVCHK  \
	+define+SCR1_TRGT_SIMULATION \
	+define+$(SIM_TRACE_DEF) \
	+define+$(SIM_CFG_DEF) \
	$(SIM_BUILD_OPTS) \
	$(sv_list)

build_vcs: $(sv_list) $(test_info)
	cd $(bld_dir); \
	vcs \
	-full64 \
	-lca \
	-sverilog \
	-notice \
	+lint=all,noVCDE,noNS,noVNGS,noSVA-DIU,noSVA-CE,noSVA-NSVU \
	-timescale=1ns/1ps \
	+incdir+$(rtl_inc_dir) \
	+incdir+$(rtl_inc_tb_dir) \
	+define+SCR1_TRGT_SIMULATION \
	+define+$(SIM_TRACE_DEF) \
	+define+$(SIM_CFG_DEF) \
	-nc \
	-debug_all \
	$(SIM_BUILD_OPTS) \
	$(sv_list)

build_ncsim: $(sv_list) $(test_info)
	cd $(bld_dir); \
	irun \
	-elaborate \
	-64bit \
	-disable_sem2009 \
	-verbose \
	-timescale 1ns/1ps \
	-incdir $(rtl_inc_dir) \
	-incdir $(rtl_inc_tb_dir) \
	-debug \
	+define+SCR1_TRGT_SIMULATION \
	+define+$(SIM_TRACE_DEF) \
	+define+$(SIM_CFG_DEF) \
	$(SIM_BUILD_OPTS) \
	$(sv_list) \
	-top $(top_module)

build_verilator: $(sv_list) $(test_info)
	cd $(bld_dir); \
	verilator \
	-cc \
	-sv \
	+1800-2017ext+sv \
	-Wno-fatal \
	--top-module $(top_module) \
	-DSCR1_TRGT_SIMULATION \
	-D$(SIM_TRACE_DEF) \
	-D$(SIM_CFG_DEF) \
	--clk clk \
	--exe $(scr1_wrapper) \
	--Mdir $(bld_dir)/verilator \
	-I$(rtl_inc_dir) \
	-I$(rtl_inc_tb_dir) \
	$(SIM_BUILD_OPTS) \
	$(sv_list); \
	cd verilator; \
	$(MAKE) -f V$(top_module).mk;

build_verilator_wf: $(sv_list) $(test_info)
	cd $(bld_dir); \
	verilator \
	-cc \
	-sv \
	+1800-2017ext+sv \
	-Wno-fatal \
	--top-module $(top_module) \
	-DSCR1_TRGT_SIMULATION \
	-D$(SIM_TRACE_DEF) \
	-D$(SIM_CFG_DEF) \
	-CFLAGS -DFST_TRACE -CFLAGS -DTRACE_LVLV=20 \
	-CFLAGS -DFST_FNAME=simx.fst \
	--clk clk \
	--exe $(scr1_wrapper) \
	--trace-fst \
	--trace-params \
    --trace-structs \
    --trace-underscore \
	--Mdir $(bld_dir)/verilator \
	-I$(rtl_inc_dir) \
	-I$(rtl_inc_tb_dir) \
	$(SIM_BUILD_OPTS) \
	$(sv_list); \
	cd verilator; \
	$(MAKE) -f V$(top_module).mk;


run_vcs: $(test_info)
	$(MAKE) build_vcs SIM_CFG_DEF=$(SIM_CFG_DEF) SIM_TRACE_DEF=$(SIM_TRACE_DEF) SIM_BUILD_OPTS="$(SIM_BUILD_OPTS)";
	printf "" > $(test_results);
	cd $(bld_dir); \
	$(bld_dir)/simv  -V \
	+test_info=$(test_info) \
	+test_results=$(test_results) \
	+imem_pattern=$(imem_pattern) \
	+dmem_pattern=$(dmem_pattern) \
	$(VCS_OPTS) | tee $(sim_results)  ;\
	printf "                          Test               | build | simulation \n" ; \
	printf "$$(cat $(test_results)) \n"
run_modelsim: $(test_info)
	$(MAKE) build_modelsim SIM_CFG_DEF=$(SIM_CFG_DEF) SIM_TRACE_DEF=$(SIM_TRACE_DEF) SIM_BUILD_OPTS="$(SIM_BUILD_OPTS)"; \
	printf "" > $(test_results); \
	cd $(bld_dir); \
	vsim -c -do "run -all" +nowarn3691 \
	+test_info=$(test_info) \
	+test_results=$(test_results) \
	+imem_pattern=$(imem_pattern) \
	+dmem_pattern=$(dmem_pattern) \
	work.$(top_module) \
	$(MODELSIM_OPTS) | tee $(sim_results)  ;\
	printf "Simulation performed on $$(vsim -version) \n" ;\
	printf "                          Test               | build | simulation \n" ; \
	printf "$$(cat $(test_results)) \n"

run_ncsim: $(test_info)
	$(MAKE) build_ncsim SIM_CFG_DEF=$(SIM_CFG_DEF) SIM_TRACE_DEF=$(SIM_TRACE_DEF) SIM_BUILD_OPTS="$(SIM_BUILD_OPTS)";
	printf "" > $(test_results);
	cd $(bld_dir); \
	irun \
	-R \
	-64bit \
	+test_info=$(test_info) \
	+test_results=$(test_results) \
	+imem_pattern=$(imem_pattern) \
	+dmem_pattern=$(dmem_pattern) \
	$(NCSIM_OPTS) | tee $(sim_results)  ;\
	printf "Simulation performed on $$(irun -version) \n" ;\
	printf "                          Test               | build | simulation \n" ; \
	printf "$$(cat $(test_results)) \n"

run_verilator: $(test_info)
	$(MAKE) build_verilator SIM_CFG_DEF=$(SIM_CFG_DEF) SIM_TRACE_DEF=$(SIM_TRACE_DEF) SIM_BUILD_OPTS="$(SIM_BUILD_OPTS)";
	printf "" > $(test_results);
	cd $(bld_dir); \
	echo $(top_module) | tee $(sim_results); \
	$(bld_dir)/verilator/V$(top_module) \
	+test_info=$(test_info) \
	+test_results=$(test_results) \
	+imem_pattern=$(imem_pattern) \
	+dmem_pattern=$(dmem_pattern) \
	$(VERILATOR_OPTS) | tee -a $(sim_results) ;\
	printf "Simulation performed on $$(verilator -version) \n" ;\
	printf "                          Test               | build | simulation \n" ; \
	printf "$$(cat $(test_results)) \n"

# $(MAKE) -C $(root_dir)/sim build_verilator_wf SIM_CFG_DEF=$(SIM_CFG_DEF) SIM_TRACE_DEF=$(SIM_TRACE_DEF) SIM_BUILD_OPTS="$(SIM_BUILD_OPTS)";

run_verilator_wf: $(test_info)
	$(MAKE) build_verilator_wf SIM_CFG_DEF=$(SIM_CFG_DEF) SIM_TRACE_DEF=$(SIM_TRACE_DEF) SIM_BUILD_OPTS="$(SIM_BUILD_OPTS)";
	printf "" > $(test_results); 
	cd $(bld_dir); \
	echo $(top_module) | tee $(sim_results); \
	$(bld_dir)/verilator/V$(top_module) \
	+test_info=$(test_info) \
	+test_results=$(test_results) \
	+imem_pattern=$(imem_pattern) \
	+dmem_pattern=$(dmem_pattern) \
	$(VERILATOR_OPTS) | tee -a $(sim_results)  ;\
	printf "Simulation performed on $$(verilator -version) \n" ;\
	printf "                          Test               | build | simulation \n" ; \
	printf "$$(cat $(test_results)) \n"
	
clean:
	$(RM) -R $(root_dir)/build/*