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
public class RadialStreetTest
{
   private static BurningData dataSet;
   
   public RadialStreetTest ()
   {
      dataSet = new BurningData ("2018");
   }
   
   @BeforeClass
   public static void setUpClass ()
   {
      System.out.println ("Setup R");
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
    * Test of getHour method, of class RadialStreet.
    */
   @Test
   public void testHour ()
   {
      System.out.println ("hour");
      RadialStreet instance = new RadialStreet (6, 0);
      double expResult = 6.0;
      double result = instance.getHour ();
      assertEquals (expResult, result, 0.01);
   }

   /**
    * Test of toString method, of class RadialStreet.
    */
   @Test
   public void testToString ()
   {
      System.out.println ("toString");
      RadialStreet instance = new RadialStreet (6, 0);
      String expResult = "6:00";
      String result = instance.toString ();
      assertEquals (expResult, result);
   }

   /**
    * Test of getMinutes method, of class RadialStreet.
    */
   @Test
   public void testMinutes ()
   {
      System.out.println ("minutes");
//      RadialStreet instance = null;
//      int expResult = 0;
//      int result = instance.getMinutes ();
//      assertEquals (expResult, result);
//      // TODO review the generated test code and remove the default call to fail.
//      fail ("The test case is a prototype.");
   }

   /**
    * Test of getNextStreet method, of class RadialStreet.
    */
   @Test
   public void testMove ()
   {
      System.out.println ("move");
//      ManDirection direction = null;
//      RadialStreet instance = null;
//      RadialStreet expResult = null;
//      RadialStreet result = instance.getNextStreet (direction);
//      assertEquals (expResult, result);
//      // TODO review the generated test code and remove the default call to fail.
//      fail ("The test case is a prototype.");
   }

   /**
    * Test of equals method, of class RadialStreet.
    */
   @Test
   public void testEquals ()
   {
      System.out.println ("equals");
//      RadialStreet r = null;
//      RadialStreet instance = null;
//      boolean expResult = false;
//      boolean result = instance.equals (r);
//      assertEquals (expResult, result);
//      // TODO review the generated test code and remove the default call to fail.
//      fail ("The test case is a prototype.");
   }
   
}
