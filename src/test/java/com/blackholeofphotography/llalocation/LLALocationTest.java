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
package com.blackholeofphotography.llalocation;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

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
   
   @BeforeClass
   public static void setUpClass ()
   {
   }
   
   @AfterClass
   public static void tearDownClass ()
   {
   }
   
   @Before
   public void setUp ()
   {
   }
   
   @After
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
}
