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

import com.blackholeofphotography.llalocation.LLALocation;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class IntersectionTest
{
   private static BurningData dataSet;

   public IntersectionTest ()
   {
      dataSet = new BurningData ("2019");
   }
   
   @BeforeAll
   public static void setUpClass ()
   {
      dataSet = new BurningData ("2019");
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
    * Test of corner method, of class Intersection.
    */
   @Test
   public void testCorner ()
   {
      double lat = 40.77897026953673;
      double lon = -119.18995362374274;
      
      System.out.println ("corner");
      IntersectionOffset io = IntersectionOffset.ClockwiseOutside;
      Intersection instance = new Intersection (2, 30, 'I');
      
      LLALocation result = instance.corner (dataSet, io);
      assertEquals (lat, result.getLatitude (), 0.0001);
      assertEquals (lon, result.getLongitude (), 0.0001);
   }



   /**
    * Test of toString method, of class Intersection.
    */
   @Test
   public void testToString ()
   {
      System.out.println ("toString");
      Intersection instance = new Intersection (6, 0, 'I');
      String expResult = "6:00I";
      String result = instance.toString ();
      assertEquals (expResult, result);
   }
   
}
