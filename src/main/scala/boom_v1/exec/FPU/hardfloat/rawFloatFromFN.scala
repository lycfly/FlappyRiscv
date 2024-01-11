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
import boom_v1.Utils._
object rawFloatFromFN {
  def apply(expWidth: Int, sigWidth: Int, in: Bits) = {
    val sign = in(expWidth + sigWidth - 1)
    val expIn = in(expWidth + sigWidth - 2 downto sigWidth - 1)
    val fractIn = in(sigWidth - 2 downto 0)

    val isZeroExpIn = (expIn === 0)
    val isZeroFractIn = (fractIn === 0)

    val normDist = countLeadingZeros(fractIn.asUInt)
    val subnormFract = (fractIn << normDist) (sigWidth - 3 downto 0) << 1
    val adjustedExp =
      Mux(isZeroExpIn,
        normDist ^ U((BigInt(1) << (expWidth + 1)) - 1),
        expIn.asUInt
      ) + (U(BigInt(1) << (expWidth - 1))
        | Mux(isZeroExpIn, U(2), U(1)))  // this should -126 or -127 for 32bit float
                                         // in recFN, this is (2^k + 2)+130 or (2^k + 1)+129

    val isZero = isZeroExpIn && isZeroFractIn
    val isSpecial = adjustedExp(expWidth downto expWidth - 1) === 3

    val out = (new RawFloat(expWidth, sigWidth))
    out.isNaN := isSpecial && !isZeroFractIn
    out.isInf := isSpecial && isZeroFractIn
    out.isZero := isZero
    out.sign := sign
    out.sExp := (False ## adjustedExp(expWidth downto 0)).asSInt
    out.sig :=
      U(0, 1 bits) ## !isZero ## Mux(isZeroExpIn, subnormFract, fractIn)
    out
  }
}

