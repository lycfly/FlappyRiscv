package boom_v1.exec.FPU.hardfloat


/*============================================================================

This Chisel source file is part of a pre-release version of the HardFloat IEEE
Floating-Point Arithmetic Package, by John R. Hauser (with some contributions
from Yunsup Lee and Andrew Waterman, mainly concerning testing).

Copyright 2010, 2011, 2012, 2013, 2014, 2015, 2016 The Regents of the
University of California.  All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met:

 1. Redistributions of source code must retain the above copyright notice,
    this list of conditions, and the following disclaimer.

 2. Redistributions in binary form must reproduce the above copyright notice,
    this list of conditions, and the following disclaimer in the documentation
    and/or other materials provided with the distribution.

 3. Neither the name of the University nor the names of its contributors may
    be used to endorse or promote products derived from this software without
    specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE REGENTS AND CONTRIBUTORS "AS IS", AND ANY
EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE, ARE
DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

=============================================================================*/

import spinal.core._
import spinal.lib._
import boom_v1.Utils._
import consts._


class INToRecFN(intWidth: Int, expWidth: Int, sigWidth: Int) extends Module
{
  val io = new Bundle {
    val signedIn = in(Bool())
    val in = in(Bits(intWidth bits))
    val roundingMode   = in(UInt(3 bits))
    val detectTininess = in(UInt(1 bits))
    val outRecFN = out(Bits((expWidth + sigWidth + 1) bits))
    val exceptionFlags = out(Bits(5 bits))
  }

  //------------------------------------------------------------------------
  //------------------------------------------------------------------------
  val intAsRawFloat = rawFloatFromIN(io.signedIn, io.in);

  val roundAnyRawFNToRecFN =
      new RoundAnyRawFNToRecFN(
        intAsRawFloat.expWidth,
        intWidth,
        expWidth,
        sigWidth,
        flRoundOpt_sigMSBitAlwaysZero | flRoundOpt_neverUnderflows
      )
  roundAnyRawFNToRecFN.io.invalidExc     := False
  roundAnyRawFNToRecFN.io.infiniteExc    := False
  roundAnyRawFNToRecFN.io.in             := intAsRawFloat
  roundAnyRawFNToRecFN.io.roundingMode   := io.roundingMode
  roundAnyRawFNToRecFN.io.detectTininess := io.detectTininess
  io.outRecFN       := roundAnyRawFNToRecFN.io.out
  io.exceptionFlags := roundAnyRawFNToRecFN.io.exceptionFlags
}


