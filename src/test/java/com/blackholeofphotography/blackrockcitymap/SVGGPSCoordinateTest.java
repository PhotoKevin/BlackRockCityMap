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
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class SVGGPSCoordinateTest
{
   
   public SVGGPSCoordinateTest ()
   {
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

   @Test
   public void testXCoordinate ()
   {
      System.out.println ("testXCoordinate");
      LLALocation basePoint = new LLALocation (42.0, -88.0, 200);
      LLALocation secondPoint = basePoint.moveFT (90.0, 10).moveFT (180, 5);
      SVGGPSCoordinate s = new SVGGPSCoordinate (basePoint, secondPoint);
            
      int expResult = 10;
      int result = (int) Math.round (s.xCoordinate ());
      assertEquals (expResult, result);

      secondPoint = basePoint.moveFT (270.0, 10).moveFT (180, 5);
      s = new SVGGPSCoordinate (basePoint, secondPoint);
            
      expResult = -10;
      result = (int) Math.round (s.xCoordinate ());
      assertEquals (expResult, result);

   }

   @Test
   public void testYCoordinate ()
   {
      System.out.println ("testYCoordinate");
      LLALocation basePoint = new LLALocation (42.0, -88.0, 200);
      LLALocation secondPoint = basePoint.moveFT (90.0, 10).moveFT (180, 5);
      SVGGPSCoordinate s = new SVGGPSCoordinate (basePoint, secondPoint);
            
      long expResult = 5;
      long result = Math.round (s.yCoordinate ());
      assertEquals (expResult, result);
      
      secondPoint = basePoint.moveFT (90.0, 10).moveFT (0, 5);
      s = new SVGGPSCoordinate (basePoint, secondPoint);
            
      expResult = -5;
      result = Math.round (s.yCoordinate ());
      assertEquals (expResult, result);

   }

}
