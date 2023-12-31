package boom_v1.exec.FPU.hardfloat


/*============================================================================

This Chisel source file is part of a pre-release version of the HardFloat IEEE
Floating-Point Arithmetic Package, by John R. Hauser (with some contributions
from Yunsup Lee and Andrew Waterman, mainly concerning testing).

Copyright 2010, 2011, 2012, 2013, 2014, 2015, 2016, 2017 The Regents of the
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

object fNFromRecFN
{
  def apply(expWidth: Int, sigWidth: Int, in: Bits) =
  {
    val minNormExp = (BigInt(1)<<(expWidth - 1)) + 2

    val rawIn = rawFloatFromRecFN(expWidth, sigWidth, in)

    val isSubnormal = rawIn.sExp < S(minNormExp)
    val denormShiftDist = U(1) - rawIn.sExp(log2Up(sigWidth - 1) - 1 downto 0).asUInt
    val denormFract = ((rawIn.sig>>1)>>denormShiftDist)(sigWidth - 2 downto 0)

    val expOut =
      Mux(isSubnormal,
        U(0),
        rawIn.sExp(expWidth - 1 downto 0).asUInt -
          U((BigInt(1)<<(expWidth - 1)) + 1)
      ) | ((rawIn.isNaN || rawIn.isInf) #* expWidth).asUInt
    val fractOut =
      Mux(isSubnormal,
        denormFract,
        Mux(rawIn.isInf, U(0), rawIn.sig(sigWidth - 2 downto 0))
      )
    Cat(rawIn.sign, expOut, fractOut)
  }
}

