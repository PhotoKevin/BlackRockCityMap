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
package com.blackholeofphotography.blackrockcitymap.path;

import com.blackholeofphotography.llalocation.LLALocation;
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
public class PathArcTest
{
   private class PathArcTestData
   {
      LLALocation centerPoint;
      LLALocation startPoint;
      LLALocation endPoint;
      double startAngle;
      double endAngle;
      double degreesCW;
      double degreesCCW;
      double midBearingCW;
      double midBearingCCW;

      PathArcTestData (double startAngle, double endAngle, double degreesCW, double degreesCCW, double midBearingCW, double midBearingCCW)
      {
         this.centerPoint = new LLALocation (0, 0, 0);
         this.startAngle = startAngle;
         this.endAngle = endAngle;
         this.startPoint = centerPoint.moveFT (this.startAngle, 10);
         this.endPoint = centerPoint.moveFT (this.endAngle, 10);

         this.degreesCW = degreesCW;
         this.degreesCCW = degreesCCW;
         this.midBearingCW = midBearingCW;
         this.midBearingCCW = midBearingCCW;
      }
   }
   
   public final PathArcTestData[] testData;
           
   public PathArcTest ()
   {
      testData = new PathArcTestData[]
      {
         new PathArcTestData (  0,  90, 90, -90, 45,  225),
         new PathArcTestData ( 90, 180, 90, -90, 135, 315),
         
         new PathArcTestData (180, 270, 90, -90, 225,  45),
         new PathArcTestData (270, 360, 90, -90, 315, 135),
         
         new PathArcTestData (-10,  10, 20, -20,   0, 180),
         new PathArcTestData ( 10, -10, -20, 20, 180,   0),
         new PathArcTestData ( 55, 131, 76, 256,  93, 273),
         new PathArcTestData (128,  54, 286, 74, 271,  91)
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
    * Test of toString method, of class PathArc.
    */
   @Test
   public void testToString ()
   {
   }

   /**
    * Test of toCoordinates method, of class PathArc.
    */
   @Test
   public void testToCoordinates ()
   {
   }

   /**
    * Test of toPath2D method, of class PathArc.
    */
   @Test
   public void testToPath2D ()
   {
   }

   /**
    * Test of startPoint method, of class PathArc.
    */
   @Test
   public void testStartPoint ()
   {
   }

   /**
    * Test of endPoint method, of class PathArc.
    */
   @Test
   public void testEndPoint ()
   {
   }

   /**
    * Test of getBounds method, of class PathArc.
    */
   @Test
   public void testGetBounds ()
   {
   }

   /**
    * Test of getDegreesRotation method, of class PathArc.
    */
   @Test
   public void testGetDegreesRotation ()
   {
   }

   /**
    * Test of getMidBearing method, of class PathArc.
    */
   @Test
   public void testGetMidBearing ()
   {
      System.out.println ("getMidBearing");
      for (PathArcTestData t : testData)
      {
//         assertEquals (t.midBearingCW + t.midBearingCCW, 180.0, 0.0);
         
         PathArc arc = new PathArc (t.centerPoint, t.startPoint, t.endPoint, ArcDirection.CLOCKWISE);
         double result = arc.getMidBearing ();
         
         assertEquals (t.midBearingCW, result, 0.0);
         
         arc = new PathArc (t.centerPoint, t.startPoint, t.endPoint, ArcDirection.COUNTER_CLOCKWISE);
         result = arc.getMidBearing ();
         assertEquals (t.midBearingCCW, result, 0.0);

      }
   }
   
}
