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

import com.blackholeofphotography.blackrockcitymap.SVGGPSCoordinate;
import com.blackholeofphotography.llalocation.LLALocation;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class PathArc extends PathSegment
{
   LLALocation centerPoint;
   LLALocation startPoint;
   LLALocation endPoint;
   ArcDirection direction;
   
   PathArc (LLALocation center, LLALocation p1, LLALocation p2, ArcDirection dir)
   {
      centerPoint = center;
      startPoint = p1;
      endPoint = p2;
      direction = dir;
   }
   
   @Override
   public String toString ()
   {
      return "Arc: " + centerPoint.toString ();
   }
   

   private ArrayList<LLALocation> addArcFT (double startAngle, LLALocation center, double endAngle, double radiusFT, ArcDirection direction)
   {
      ArrayList<LLALocation> points = new ArrayList<> ();
      LLALocation start = center.moveFT (startAngle, radiusFT);
      LLALocation end = center.moveFT (endAngle, radiusFT);
      points.add (start);

      while (startAngle < 0)
         startAngle += 360.0;

      while (endAngle < startAngle)
         endAngle += 360;

      if (direction == ArcDirection.CLOCKWISE)
      {
         do
         {
            startAngle += 1.0;
            double ang = startAngle;
            while (ang > 360) ang -= 360;
            LLALocation point = center.moveFT (ang, radiusFT);
            points.add (point);
         }
         while (startAngle < endAngle-1.0);
      }
      else
      {
         while (startAngle < endAngle)
            startAngle += 360;
         do
         {
            startAngle -= 1.0;
            double ang = startAngle;
            while (ang > 360) ang -= 360;
            LLALocation point = center.moveFT (ang, radiusFT);
            points.add (point);
         }
         while (startAngle > endAngle+1.0);
      }

      points.add (end);
      return points;
   }

   /**
    * Represent this arc as a series of KML coordinate points. aka approximate the arc
    * as a series of line segments.
    * @return List of KML Coordinates. 
    */
   @Override
   public ArrayList<Coordinate> toCoordinates ()
   {
      double startBearing = centerPoint.getBearing (startPoint);
      double endBearing = centerPoint.getBearing (endPoint);
      double radiusFT = centerPoint.distanceFT (startPoint);

      ArrayList<LLALocation> points = addArcFT (startBearing, centerPoint, endBearing, radiusFT, direction);
      
      ArrayList<Coordinate> c = new ArrayList<> ();
      for (LLALocation g : points)
         c.add (g.toCoordinate ());
      
      return c;
   }

   
   /**
    * Normalize the angle to something in 0..360 degrees
    * @param a Angle to normalize
    * @return The normalized angle
    */
   private double normalize (double a)
   {
      while (a < 0) a += 360.0;
      while (a >= 360.0) a -= 360.0;
      
      return a;
   }
   
   /**
    * Get the degrees this arc goes around. 
    * @return The rotational degrees of the arc.
    */
   private double getDegreesRotation ()
   {
      double startBearing = centerPoint.getBearing (startPoint);
      double endBearing = centerPoint.getBearing (endPoint);

      double degrees;
      if (this.direction == ArcDirection.COUNTER_CLOCKWISE)
         degrees = normalize (360-endBearing + startBearing);
      else // if (this.direction == ArcDirection.CLOCKWISE)
         degrees = -normalize (endBearing - startBearing);

      return degrees;
   }

   /**
    * Convert this arc into a Bézier curve. Code taken from stackoverflow.
    * This doesn't work on arcs longer than 90 degrees, so chop things up first.
    * @param GPSBaseCoordinate Location to use as the origin of the graph.
    * @return A Bézier curve
    */
   // https://stackoverflow.com/questions/734076/how-to-best-approximate-a-geometrical-arc-with-a-bezier-curve
   
   private CubicCurve2D bezier (LLALocation GPSBaseCoordinate)
   {
      SVGGPSCoordinate  c = new SVGGPSCoordinate (GPSBaseCoordinate, centerPoint);
      SVGGPSCoordinate  s = new SVGGPSCoordinate (GPSBaseCoordinate, startPoint);
      SVGGPSCoordinate  e = new SVGGPSCoordinate (GPSBaseCoordinate, endPoint);
      
      double xc = c.xCoordinate ();
      double yc = c.yCoordinate ();
      double x1 = s.xCoordinate ();
      double y1 = s.yCoordinate ();
      double x4 = e.xCoordinate ();
      double y4 = e.yCoordinate ();
      double q1, q2, k2;
      
      double ax, ay;
      double bx, by;
      
      ax = x1 - xc;
      ay = y1 - yc;
      bx = x4 - xc;
      by = y4 - yc;
      q1 = ax * ax + ay * ay;
      q2 = q1 + ax * bx + ay * by;
      k2 = 4.0/3.0 * (Math.sqrt(2 * q1 * q2) - q2) / (ax * by - ay * bx);

      double x2, y2, x3, y3;
      x2 = xc + ax - k2 * ay;
      y2 = yc + ay + k2 * ax;
      x3 = xc + bx + k2 * by;
      y3 = yc + by - k2 * bx;
      
      CubicCurve2D cur = new CubicCurve2D.Double (x1, y1, x2, y2, x3, y3, x4, y4);
      return cur;
   }


   /**
    * Calculate the bearing to the midpoint of the arc
    * @return The bearing. 0 is North, 90 is East.
    */
   public double getMidBearing ()
   {
      double startBearing = centerPoint.getBearing (startPoint);
      double degrees = this.getDegreesRotation ();
      double midBearing;
      
      if (this.direction == ArcDirection.CLOCKWISE)
         midBearing = startBearing - degrees/2;
      else
         midBearing = startBearing - degrees/2;
      
      return normalize (midBearing);
   }
   
   /**
    * Convert this arc into a series of Bézier curves. Bézier curves don't work well past
    * 90 degrees, so break the arc up as needed.
    * @param GPSBaseCoordinate The location to use as the graph origin
    * @return A Path2D object of the arc.
    */
   private Path2D toPath2DArcBez (LLALocation GPSBaseCoordinate)
   {
      double degrees = this.getDegreesRotation ();
      double midBearing = getMidBearing ();
      double radius = centerPoint.distanceFT (startPoint);
      
      if (Math.abs (degrees) > 80.0)
      {
         
         Path2D tmp1 = new Path2D.Double ();
         Path2D tmp2 = new Path2D.Double ();
         LLALocation midPoint = centerPoint.moveFT (midBearing, radius);
         PathArc arc1 = new PathArc (this.centerPoint, this.startPoint, midPoint, this.direction);
         PathArc arc2 = new PathArc (this.centerPoint, midPoint, this.endPoint, this.direction);
         
         tmp1 = arc1.toPath2D (GPSBaseCoordinate, tmp1);
         tmp2 = arc2.toPath2D (GPSBaseCoordinate, tmp2);
         tmp1.append (tmp2, true);

         return tmp1;
      }      
      
      return new Path2D.Double (bezier (GPSBaseCoordinate));
   }
   
   @Override
   public Path2D toPath2D (LLALocation GPSBaseCoordinate, Path2D path)
   {
      path.append (this.toPath2DArcBez (GPSBaseCoordinate), true);
      //path.append (this.toPath2DArc (GPSBaseCoordinate), true);
      //path.append (this.toPath2DArcTangent (GPSBaseCoordinate), true);
      
      return path;
   }
   

   /**
    * Get the starting point of this path segment
    * @return The starting point
    */
   @Override
   LLALocation startPoint ()
   {
      return this.startPoint;
   }

   /**
    * Get the endpoint of this path segment.
    * @return The endpoint
    */
   @Override
   LLALocation endPoint ()
   {
      return this.endPoint;
   }

   /**
    * Calculate the bounds of this path.
    * @return The smallest bounding box that contains this path segment.
    */
   @Override
   public PathBounds getBounds ()
   {
      double minLat;
      double maxLat;
      double minLong;
      double maxLong;
      double alt = startPoint.getAltitude ();

      minLong = Math.min (startPoint.toCoordinate ().getLongitude (), endPoint.toCoordinate ().getLongitude ());
      maxLong = Math.max (startPoint.toCoordinate ().getLongitude (), endPoint.toCoordinate ().getLongitude ());

      minLat = Math.min (startPoint.toCoordinate ().getLatitude (), endPoint.toCoordinate ().getLatitude ());
      maxLat = Math.max (startPoint.toCoordinate ().getLatitude (), endPoint.toCoordinate ().getLatitude ());
      
      LLALocation ul = new LLALocation (maxLat, minLong, alt);
      LLALocation lr = new LLALocation (minLat, maxLong, alt);
      return new PathBounds (ul, lr);
   }
}
