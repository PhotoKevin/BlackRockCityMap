/*
 * Copyright (c) 2019-2022, Kevin Nickerson (kevin@blackholeofphotography.com)
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
package com.blackholeofphotography.llalocation;

import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class LLAGeometryTest
{
   
   public LLAGeometryTest ()
   {
   }
   
   @BeforeAll
   public static void setUpClass ()
   {
   }
   
   @AfterAll
   public static void tearDownClass ()
   {
   }
   
   @BeforeEach
   public void setUp ()
   {
   }
   
   @AfterEach
   public void tearDown ()
   {
   }

   /**
    * Test of Intersection method, of class LLAGeometry.
    */
   @Test
   public void testIntersectionCircleCircle ()
   {
      System.out.println ("Intersection");
      LLALocation c0 = new LLALocation (0, 0, 0);
      double r0 = 100.0;
      LLALocation c1 = c0.moveFT (0, 50);  
      double r1 = 100.0;
      ArrayList<LLALocation> expResult = new ArrayList<>();
      ArrayList<LLALocation> result = LLAGeometry.Intersection (c0, r0, c1, r1);
      //assertEquals (expResult, result);

   }

   /**
    * Test of Intersection method, of class LLAGeometry.
    */
   @Test
   public void testIntersectionCircleLine ()
   {
      System.out.println ("Intersection");
      LLALocation c0 = new LLALocation (0, 0, 0);
      double r = 100.0;
      LLALocation p0 = c0.moveFT (-90, 10);
      LLALocation p1 = c0.moveFT (90, 10);
      ArrayList<LLALocation> expResult = new ArrayList<>();
      expResult.add (c0.moveFT (0, 10));
      expResult.add (c0.moveFT (180, 10));
      ArrayList<LLALocation> result = LLAGeometry.Intersection (c0, r, p0, p1);
      //assertEquals (expResult, result);
   }
}
