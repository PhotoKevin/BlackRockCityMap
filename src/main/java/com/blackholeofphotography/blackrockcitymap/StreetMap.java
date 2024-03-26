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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represent the street layout of Black Rock City.
 * This class works off of an ASCII data file that shows where
 * the roads and plazas and portals are. 
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class StreetMap
{
   private char[][] data;
   
   public StreetMap (String filepath)
   {
      try
      {
         ClassLoader classloader = Thread.currentThread().getContextClassLoader();
         URL u = classloader.getResource(filepath);
         InputStream resource = u.openStream ();
         
         List<String> in = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());

         //Path p = Paths.get(u.toURI());      
         
         data = new char[150][100];
         //List<String> in = Files.readAllLines (p); // Paths.get(f));
         int row = 0;
         for (String s : in)
         {
            s = s.trim ();
            if (! s.startsWith (";") && ! s.isEmpty ())
            {
               s = s.split (";")[0];
               int col = 0;
               for (char ch : s.toCharArray ())
                  data[row][col++] = ch;
               row += 1;
            }
         }
      }
      catch (IOException e)
      {
         e.printStackTrace();
      }
   }
   
   public void print ()
   {
      for (int r = 0; r<data.length; r++)
      {
         if (data[r][0] != '\0')
         {
            for (int c = 0; c<data[r].length; c++)
            {
               char x = data[r][c];
               if (x != '\0')
                  System.out.print (x);
            }
            System.out.println ();
         }
      }
   }
         
   private int row (AnnularStreet a)
   {
      if (a.getStreetLetter () == AnnularStreet.ESPLANADE)
         return 0;
      
      return 2 * (a.getStreetLetter () - 'A' + 1);
   }
   
   private int column (RadialStreet r)
   {
      int minuteOffset = r.getMinutes () - 2 * 60;
      int col = minuteOffset / 15 + minuteOffset % 15;
      col *= 2;
      col = ((10-2) * 4 * 2) - col;

      return col;
   }
   
   /**
    * Determine if there is a road toward the man from
    * this intersection
    * @param intersection The intersection of interest.
    * @return true/false
    */
   
   public boolean existsMansideRoad (Intersection intersection)
   {
      int row = row (intersection.annular);
      int col = column (intersection.radial);
      
      if (row < 2)
         return false;
         
      else 
         return data[row-1][col] != ' ';
   }
   
   public char maxRoadLetter ()
   {
      char maxLetter = 0;
      for (int r = 0; r<data.length; r++)
         if (data[r][0] != '\0')
             maxLetter += 1;
      
      // There are two letters per street
      return  (char) (maxLetter/2 + 'A'-1);
   }

   /**
    * Determine if there is a road away from the man from
    * this intersection
    * @param intersection The intersection of interest.
    * @return true/false
    */
   
   public boolean existsOutsideRoad (Intersection intersection)
   {
      int row = row (intersection.annular);
      int col = column (intersection.radial);
      
      if ((intersection.annular.getStreetLetter () >= maxRoadLetter()) && intersection.annular.getStreetLetter () != AnnularStreet.ESPLANADE)
         return false;
      
      if (data[row+1][0] == '\0')
          return false;

      if (row > data.length - 2)
         return false;
         
      else 
         return data[row+1][col] != ' ';
   }
   
   /**
    * Determine if there is a road in the Counter Clockwise direction from
    * this intersection
    * @param intersection The intersection of interest.
    * @return true/false
    */
   public boolean existsCounterClockwiseRoad (Intersection intersection)
   {
      int row = row (intersection.annular);
      int col = column (intersection.radial);
      
      if (col > data[0].length - 2)
         return false;
         
      else 
         return data[row][col+1] != ' ';
   }

   /**
    * Determine if there is a road in the Clockwise direction from
    * this intersection
    * @param intersection The intersection of interest.
    * @return true/false
    */
   public boolean existsClockwiseRoad (Intersection intersection)
   {
      int row = row (intersection.annular);
      int col = column (intersection.radial);
      
      if (col < 2)
         return false;
         
      else 
         return data[row][col-1] != ' ';
   }
   
   private boolean existsRoad (Intersection i, ManDirection dir)
   {
      switch (dir)
      {
         case TOWARD_MAN: return this.existsMansideRoad (i);
         case FROM_MAN: return this.existsOutsideRoad (i);
         case COUNTER_CLOCKWISE: return this.existsCounterClockwiseRoad (i);
         case CLOCKWISE: return this.existsClockwiseRoad (i);
      }
      return false;
   }
   
   /**
    * Determine if this intersection is a Plaza
    * @param intersection The intersection of interest.
    * @return true/false
    */
   public boolean isPlaza (Intersection intersection)
   {
      int row = row (intersection.annular);
      int col = column (intersection.radial);

      return data[row][col] == 'Z';
   }
   
   /**
    * Determine if this intersection is a Portal.
    * Be aware that a portal might also be a Plaza Portal.
    * This returns false in that case.
    * @param intersection The intersection of interest.
    * @return true/false
    */
   public boolean isPortal (Intersection intersection)
   {
      int row = row (intersection.annular);
      int col = column (intersection.radial);
      
      if (isPlazaPortal (intersection))
         return false;

      return data[row][col] == 'P';
   }
   
   /**
    * Determine if this intersection is a Plaza Portal.
    * A Plaza portal is a portal that opens into a plaza.
    * They traditionally only happen at 3:00Esp and 9:00Esp
    * @param intersection The intersection of interest.
    * @return true/false
    */
   public boolean isPlazaPortal (Intersection intersection)
   {
      int row = row (intersection.annular);
      int col = column (intersection.radial);
      
      return data[row][col] == 'P' && data[row+4][col] == 'Z';
   }
   
      /**
    * Determine if this intersection is a Plaza Portal.
    * A Plaza portal is a portal that opens into a plaza.
    * They traditionally only happen at 3:00Esp and 9:00Esp
    * A Mid Plaza Portal is an intersection between a Portal and its plaza
    * @param intersection The intersection of interest.
    * @return true/false
    */
   public boolean isMidPlazaPortal (Intersection intersection)
   {
      int row = row (intersection.annular);
      int col = column (intersection.radial);
      
      // Lazy here. A portal is only on Esplanade. 
      // A Mid plaza portal intersection has to be A
      // and the Plaza B. If they ever put one out on C
      // this will be wrong.
      
      if (intersection.annular.getStreetLetter () != 'A')
         return false;
      
      return data[0][col] == 'P' && data[row+2][col] == 'Z';
   }

   
   /**
    * Get the corners of the block where the supplied intersection is the 
    * early inside corner.
    * @param earlyInside
    * @return ArrayList of intersections for the corners.
    * @throws java.lang.Exception
    */
   public ArrayList<Intersection> getBlockCorners (Intersection earlyInside) throws Exception
   {
      ManDirection currentDirection;
      ArrayList<Intersection> corners = new ArrayList<> ();
      corners.add (earlyInside);
      currentDirection = ManDirection.CLOCKWISE;
      Intersection p = earlyInside.getNextIntersection (currentDirection);
      
      // This works like walking along a left wall
      do 
      {
         if (this.existsRoad (p, currentDirection.left ()))
         {
            corners.add (p);
            currentDirection = currentDirection.left ();
            p = p.getNextIntersection (currentDirection);
         }
         else if (this.existsRoad (p, currentDirection.straight ()))
         {
            // Don't add this corner
            currentDirection = currentDirection.straight ();
            p = p.getNextIntersection (currentDirection);
         }
         else if (this.existsRoad (p, currentDirection.right ()))
         {
            corners.add (p);
            currentDirection = currentDirection.right ();
            p = p.getNextIntersection (currentDirection);
         }
         else
         {
            corners.add (p);
            currentDirection = currentDirection.reverse ();
            p = p.getNextIntersection (currentDirection);
         }
      } while (! p.equals (earlyInside) && corners.size () < 20);

      // The code that calls this can only handle rectangular blocks.
      if (corners.size () != 4)
         throw new Exception ();
      
      return corners;
   }
}
