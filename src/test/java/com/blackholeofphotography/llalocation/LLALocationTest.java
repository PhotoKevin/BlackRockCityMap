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


import com.blackholeofphotography.blackrockcitymap.path.Path;
import java.awt.Color;
import java.util.ArrayList;
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
public class LLALocationTest
{

   private class LLALocationTestData
   {
      public LLALocation a;
      public LLALocation b;
      public double distance;
      public double bearing;
      LLALocationTestData (LLALocation a,LLALocation b, double distance, double bearing)
      {
         this.a = a;
         this.b = b;
         this.distance = distance;
         this.bearing = bearing;
      }
   }
   
   public final LLALocationTestData[] testData;
           
   public LLALocationTest ()
   {
      testData = new LLALocationTestData[]
      {
         new LLALocationTestData (new LLALocation (0, 10, 0), new LLALocation (0, 0, 0), 0, 270.0),
         new LLALocationTestData (new LLALocation (10, 0, 0), new LLALocation (0, 0, 0), 0, 180.0)
      };
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
    * Test of getBearing method, of class LLALocation.
    */
   @Test
   public void testGetBearing ()
   {
      System.out.println ("getBearing");
      LLALocation g = new LLALocation (0, 10, 0);
      LLALocation instance = new LLALocation (0, 0, 0);
      double expResult = 90.0;
      double result = instance.getBearing (g);
      assertEquals (expResult, result, 0.0);
      // TODO review the generated test code and remove the default call to fail.
      //fail ("The test case is a prototype.");
      for (LLALocationTestData t : testData)
      {
         result = t.a.getBearing (t.b);
         
         assertEquals (t.bearing, result, 0.0);
      }
   }

   /**
    * Test of distanceFT method, of class LLALocation.
    */
   @Test
   public void testDistanceFT ()
   {
      System.out.println ("distanceFT");
      LLALocation lla = new LLALocation (0, 0, 0);
      LLALocation instance = new LLALocation (0, 0, 0);
      double expResult = 0.0;
      double result = instance.distanceFT (lla);
      assertEquals (expResult, result, 0.0);
   }
   
   @Test
   public void testDistanceNS ()
   {
      LLALocation GoldenSpike = new LLALocation (40.786400, -119.203500, 3904);
      LLALocation c0 = GoldenSpike.moveFT (90, 1000);
      double r = 1000.0;
      
      LLALocation a0 = c0.moveFT (45, r);
      LLALocation b0 = c0.moveFT (00, r);

      double baX = b0.distanceEWFT (a0);
      double caX = c0.distanceEWFT (a0);
      assertEquals (707, baX, 2);
      assertEquals (707, caX, 2);
      
      double abX = a0.distanceEWFT (b0);
      double acX = a0.distanceEWFT (c0);
      assertEquals (707, abX, 2);
      assertEquals (707, acX, 2);


      double baY = b0.distanceNSFT (a0);
      double caY = c0.distanceNSFT (a0);
      
      assertEquals (292, baY, 2);
      assertEquals (707, caY, 2);

      double abY = a0.distanceNSFT (b0);
      double acY = a0.distanceNSFT (c0);

      assertEquals (292, baY, 2);
      assertEquals (707, caY, 2);

   }
}
