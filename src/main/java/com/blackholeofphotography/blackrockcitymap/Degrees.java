/*
 * Copyright (c) 2019, Kevin Nickerson (kevin@blackholeofphotography.com)
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.blackholeofphotography.blackrockcitymap;

/**
 * Degrees functions in degrees.
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class Degrees
{
   public static double sin (double a)
   {
      return Math.sin (Math.toRadians (a));
   }

   public static double cos (double a)
   {
      return Math.cos (Math.toRadians (a));
   }
   
   public static double tan (double a)
   {
      return Math.tan (Math.toRadians (a));
   }

   public static double asin (double a)
   {
      return Math.toDegrees (Math.asin (a));
   }

   public static double acos (double a)
   {
      return Math.toDegrees (Math.acos (a));
   }

   public static double atan (double a)
   {
      return Math.toDegrees (Math.atan (a));
   }
   
   public static double atan2 (double x, double y)
   {
      return Math.toDegrees (Math.atan2 (x, y));
   }
}
