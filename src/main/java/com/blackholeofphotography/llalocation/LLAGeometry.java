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

import java.util.ArrayList;

/**
 * Some simple geometry on LLALocations
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class LLAGeometry
{
   // http://paulbourke.net/geometry/circlesphere/

   /**
    * Get the intersection of two circles c0 and c1. 
    * @param c0 Center of c0
    * @param r0 Radius of c0
    * @param c1 Center of c1
    * @param r1 Radius of c1
    * @return List of points of intersection. May be 0, 1, or 2 points.
    */
   public static  ArrayList<LLALocation> Intersection (LLALocation c0, double r0, LLALocation c1, double r1)
   {
      ArrayList<LLALocation> points = new ArrayList<> ();
      double a, dist, h;
      
      dist = c0.distanceFT (c1);

      if (dist > r0+r1)
         return points; // No intersection
      else if (dist == Math.abs (r0-r1))
         return points; // They touch at one point, I haven't done this yet.
      else if (dist <= Math.abs (r0-r1))
         return points; // One circle inside the other
      else if ((dist == 0.0) && (r0 == r1))
         return points; // Same circles

      // p2 is the middle of the intersection wedge. 
      // Determine the distance from point 0 to point 2. 
      a = (square(r0) - square(r1) + square(dist)) / (2.0 * dist);

      // h is the perpediculor offseft from p2
      h = Math.sqrt (square (r0) - square(a));
      
      double bearing = c0.getBearing (c1);
      LLALocation p2 = c0.moveFT (bearing, a);

      points.add (p2.moveFT (bearing+90, h));
      points.add (p2.moveFT (bearing-90, h));
      
      return points;
   }
   
   /**
    * Calculate the square. Makes the other equations a bit easier to read than doing things inline
    * @param a Value to square
    * @return a*a
    */
   private static double square (double a)
   {
      return a*a;
   }

   /**
    * Get the intersection of circle c0 and the line that goes through p0 and p1
    * @param c0 Center of c0
    * @param r Radius of c0
    * @param p0 A point on the line
    * @param p1 Another point on the line
    * @return List of points of intersection. May be 0, 1, or 2 points.
    */
   public static ArrayList<LLALocation> IntersectionOld (LLALocation c0, double r, LLALocation p0, LLALocation p1)
   {
      ArrayList<LLALocation> points = new ArrayList<> ();
      
      double angle = Math.toRadians (Math.abs (p0.getBearing (c0) - p0.getBearing (p1)));
      double dist = Math.cos (angle) * p0.distanceFT (c0);
      LLALocation closest = p0.moveFT (p0.getBearing (p1), dist);
      
      if (dist > r)
         return points; // No solution
      else if (dist == r)
         return points; // One point, not implemented yet.

      double closestBearing = c0.getBearing (closest);
      
      dist = Math.sqrt (r*r - closest.distanceFT (c0)*closest.distanceFT (c0));

      points.add (closest.moveFT (closestBearing+90, dist));
      points.add (closest.moveFT (closestBearing-90, dist));
      
      return points;
   }
   
   
   public static ArrayList<LLALocation> Intersection ( LLALocation center, double radius,
           LLALocation pointA, LLALocation pointB) 
   {
      ArrayList<LLALocation> points = new ArrayList<> ();

      double baX = pointB.distanceEWFT (pointA);
      double baY = pointB.distanceNSFT (pointA);
      double caX = center.distanceEWFT (pointA);
      double caY = center.distanceNSFT (pointA);
      
      if (pointA.getLatitude () > pointB.getLatitude ())
         baY *= -1;
      if (pointA.getLongitude () > pointB.getLongitude ())
         baX *= -1;

      if (pointA.getLatitude () > center.getLatitude ())
         caY *= -1;
      if (pointA.getLongitude () > center.getLongitude ())
         caX *= -1;
      

      double a = baX * baX + baY * baY;
      double bBy2 = baX * caX + baY * caY;
      double c = caX * caX + caY * caY - radius * radius;

      double pBy2 = bBy2 / a;
      double q = c / a;

      double disc = pBy2 * pBy2 - q;
      if (disc < 0) 
      {
         return points;
      }
      // if disc == 0 ... dealt with later
      double tmpSqrt = Math.sqrt(disc);
      double abScalingFactor1 = -pBy2 + tmpSqrt;
      double abScalingFactor2 = -pBy2 - tmpSqrt;
      
      //     Point p1 = new Point(pointA.x - baX * abScalingFactor1, 
      //                          pointA.y - baY * abScalingFactor1);

      double d1 = - baX * abScalingFactor1;
      double v1 = - baY * abScalingFactor1;
      LLALocation p1 = pointA.moveFT (90, d1);
      p1 = p1.moveFT (0, v1);

      if (disc == 0) 
      {
         points.add (p1);
         return points;
      }
      //  Point p2 = new Point(pointA.x - baX * abScalingFactor2, 
      //                       pointA.y - baY * abScalingFactor2);
      double d2 = - baX * abScalingFactor2;
      double v2 = - baY * abScalingFactor2;
      LLALocation p2 = pointA.moveFT (90, d2);
      p2 = p2.moveFT (0, v2);


      points.add (p1);
      points.add (p2);
      
//      System.out.print (p1);
//      System.out.print (" ");
//      System.out.println (p2);
      return points;
   }
}
