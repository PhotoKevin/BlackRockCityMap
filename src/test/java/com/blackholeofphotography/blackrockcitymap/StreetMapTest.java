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

import java.util.ArrayList;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class StreetMapTest
{
   
   public StreetMapTest ()
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

   /**
    * Test of existsMansideRoad method, of class StreetMap.
    */
   @Test
   public void testExistsMansideRoad ()
   {
      System.out.println ("existsManside");
      StreetMap instance = new StreetMap ("2018-StreetMap.txt");
      boolean expResult = true;
      boolean result = instance.existsMansideRoad (new Intersection ("10:00", 'a'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.existsMansideRoad (new Intersection ( "3:45",   'G'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.existsMansideRoad (new Intersection ( "6:00",   'd'));
      assertEquals (expResult, result);
   }

   /**
    * Test of existsOutsideRoad method, of class StreetMap.
    */
   @Test
   public void testExistsOutsideRoad ()
   {
      System.out.println ("isOutside");
      StreetMap instance = new StreetMap ("2018-StreetMap.txt");
      boolean expResult = true;
      boolean result = instance.existsOutsideRoad (new Intersection ( "10:00",   'a'));
      assertEquals (expResult, result);

      expResult = true;
      result = instance.existsOutsideRoad (new Intersection ( "3:45",   'G'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.existsOutsideRoad (new Intersection ( "2:15",   'A'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.existsOutsideRoad (new Intersection ( "6:00",   'L'));
      assertEquals (expResult, result);
   }

   /**
    * Test of existsClockwiseRoad method, of class StreetMap.
    */
   @Test
   public void testExistsClockwiseRoad ()
   {
      System.out.println ("isClockwise");
      StreetMap instance = new StreetMap ("2018-StreetMap.txt");
      boolean expResult = false;
      boolean result = instance.existsClockwiseRoad (new Intersection ( "10:00",   'a'));
      assertEquals (expResult, result);

      expResult = true;
      result = instance.existsClockwiseRoad (new Intersection ( "3:45",   'G'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.existsClockwiseRoad (new Intersection ( "5:30",   'C'));
      assertEquals (expResult, result);
   }
   

   /**
    * Test of existsClockwiseRoad method, of class StreetMap.
    */
   @Test
   public void testExistsCounterClockwiseRoad ()
   {
      System.out.println ("existsCounterClockwise");
      StreetMap instance = new StreetMap ("2018-StreetMap.txt");
      boolean expResult = true;
      boolean result = instance.existsCounterClockwiseRoad (new Intersection ( "10:00",   'a'));
      assertEquals (expResult, result);

      expResult = true;
      result = instance.existsCounterClockwiseRoad (new Intersection ( "3:45",   'G'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.existsCounterClockwiseRoad (new Intersection ( "6:00",   'a'));
      assertEquals (expResult, result);
   }   
   
   @Test
   public void testBlockCorners ()
   {
      try
      {
         System.out.println ("blockCorners");
      StreetMap instance = new StreetMap ("2018-StreetMap.txt");
//      boolean expResult = true;
         ArrayList<Intersection> result = instance.getBlockCorners (new Intersection ( "2:00",   'a'));

         for (Intersection ii : result)
            System.out.print (ii.toString ());

         assertEquals (4, result.size ());
      }
      catch (Exception ex)
      {
         fail (ex.toString ());
      }
   }



   /**
    * Test of isPlaza method, of class StreetMap.
    */
   @Test
   public void testIsPlaza ()
   {
      System.out.println ("isPlaza");
      StreetMap instance = new StreetMap ("2018-StreetMap.txt");
      boolean expResult = false;
      boolean result = instance.isPlaza (new Intersection ( "10:00",   'a'));
      assertEquals (expResult, result);

      expResult = true;
      result = instance.isPlaza (new Intersection ( "3:00",   'B'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.isPlaza (new Intersection ( "5:30",   'C'));
      assertEquals (expResult, result);
   }
   


   /**
    * Test of isPlaza method, of class StreetMap.
    */
   @Test
   public void testIsPlazaPortal ()
   {
      System.out.println ("isPlazaPortal");
      StreetMap instance = new StreetMap ("2018-StreetMap.txt");
      boolean expResult = false;
      boolean result = instance.isPlazaPortal (new Intersection ( "10:00",   'a'));
      assertEquals (expResult, result);

      expResult = true;
      result = instance.isPlazaPortal (new Intersection ( "3:00",   'Z'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.isPlazaPortal (new Intersection ( "4:30",   'Z'));
      assertEquals (expResult, result);

      expResult = false;
      result = instance.isPlazaPortal (new Intersection ( "5:30",   'C'));
      assertEquals (expResult, result);
   }
}
