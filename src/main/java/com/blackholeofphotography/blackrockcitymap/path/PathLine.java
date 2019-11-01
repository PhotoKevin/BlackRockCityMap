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
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class PathLine extends PathSegment
{
   LLALocation startPoint;
   LLALocation endPoint;
   
   PathLine (LLALocation start, LLALocation end)
   {
      startPoint = start;
      endPoint = end;
   }
   
   @Override
   public String toString ()
   {
      return "Line: " + startPoint.toString () + " " + endPoint.toString () + "\n";
   }

   @Override
   ArrayList<Coordinate> toCoordinates ()
   {
      ArrayList<Coordinate> points = new ArrayList<> ();
      
      points.add (startPoint.toCoordinate ());
      points.add (endPoint.toCoordinate ());
      return points;
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

   @Override
   Path2D toPath2D (LLALocation GPSBaseCoordinate, Path2D path)
   {
      SVGGPSCoordinate start = new SVGGPSCoordinate (GPSBaseCoordinate, startPoint);
      SVGGPSCoordinate end = new SVGGPSCoordinate (GPSBaseCoordinate, endPoint);
      
      if (path.getCurrentPoint () == null)
         path.moveTo (start.xCoordinate (), start.yCoordinate ());
      else
         path.lineTo (start.xCoordinate (), start.yCoordinate ());
      
      path.lineTo (end.xCoordinate (), end.yCoordinate ());
      
      return path;
   }

   /**
    * Calculate the bounds of this path.
    * @return The smallest bounding box that contains this path segment.
    */

   @Override
   PathBounds getBounds ()
   {
      double minLat;
      double maxLat;
      double minLong;
      double maxLong;
      double alt = 0.0;

      minLong = Math.min (startPoint.toCoordinate ().getLongitude (), endPoint.toCoordinate ().getLongitude ());
      maxLong = Math.max (startPoint.toCoordinate ().getLongitude (), endPoint.toCoordinate ().getLongitude ());

      minLat = Math.min (startPoint.toCoordinate ().getLatitude (), endPoint.toCoordinate ().getLatitude ());
      maxLat = Math.max (startPoint.toCoordinate ().getLatitude (), endPoint.toCoordinate ().getLatitude ());
      LLALocation ul = new LLALocation (maxLat, minLong, alt);
      LLALocation lr = new LLALocation (minLat, maxLong, alt);
      
      return new PathBounds (ul,lr);
   }

}
