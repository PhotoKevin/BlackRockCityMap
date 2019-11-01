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
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import java.awt.Color;
import java.awt.Point;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class Path
{
   private final String PathName;
   private Color PathColor;
   private final ArrayList<PathSegment> segments;
   private boolean closed = false; 
   
   public Path (String name)
   {
      PathColor = Color.BLACK;
      PathName = name;
      segments = new ArrayList<> ();
   }

   public Path (String name, Color color)
   {
      PathColor = color;
      PathName = name;
      segments = new ArrayList<> ();
   }
   
   public void addLineSegment (LLALocation start, LLALocation end)
   {
      segments.add (new PathLine (start, end));
   }
   
   public void addArcSegment (LLALocation center, LLALocation p1, LLALocation p2, ArcDirection dir)
   {
      segments.add (new PathArc (center, p1, p2, dir));
   }
   
   public void addPoint (LLALocation point)
   {
      segments.add (new PathPoint (point));
   }
   
   public void closePath ()
   {
      closed = true;
   }
   
   public String getName ()
   {
      return this.PathName;
   }
   
   public void setColor (Color color)
   {
      PathColor = color;
   }
   
   public Color getColor ()
   {
      return PathColor;
   }
   
   public String getKMLColor ()
   {
      return String.format ("#%02x%02x%02x%02x", PathColor.getAlpha (), PathColor.getBlue (), PathColor.getGreen(), PathColor.getRed());
   }
   
   /**
    * Convert all of the path segments to coordinates
    * @return ArrayList of Coordinates.
    */
   public ArrayList<Coordinate> toCoordinates ()
   {
      ArrayList<Coordinate> points = new ArrayList<> ();
      
      for (PathSegment seg : segments)
         points.addAll (seg.toCoordinates ());

      return points;
   }
   
   public Path2D toPath2D (LLALocation GPSBaseCoordinate)
   {
      Path2D path = new Path2D.Float ();
      
      for (PathSegment seg : segments)
         path = seg.toPath2D (GPSBaseCoordinate, path);

      if (closed)
         path.closePath ();
      
      return path;
   }
      
   @Override
   public String toString ()
   {
      String result = "";
      for (PathSegment s : segments)
      {
         result += s.toString ();
      }

      return result;
   }
   
   public PathBounds getBounds ()
   {
      double minLat = 999;
      double maxLat = -999;
      double minLong = 999;
      double maxLong = -999;
      double alt = 0.0;

      for (PathSegment s : segments)
      {
         PathBounds b = s.getBounds ();
         minLong = Math.min (minLong, b.UpperLeft.toCoordinate ().getLongitude ());
         maxLong = Math.max (maxLong, b.UpperLeft.toCoordinate ().getLongitude ());

         minLat =   Math.min (minLat, b.UpperLeft.toCoordinate ().getLatitude ());
         maxLat =   Math.max (maxLat, b.UpperLeft.toCoordinate ().getLatitude ());

         minLong = Math.min (minLong, b.LowerRight.toCoordinate ().getLongitude ());
         maxLong = Math.max (maxLong, b.LowerRight.toCoordinate ().getLongitude ());

         minLat =   Math.min (minLat, b.LowerRight.toCoordinate ().getLatitude ());
         maxLat =   Math.max (maxLat, b.LowerRight.toCoordinate ().getLatitude ());
      }
      
      LLALocation ul = new LLALocation (maxLat, minLong, 0.0);
      LLALocation lr = new LLALocation (minLat, maxLong, 0.0);
      return new PathBounds (ul, lr);
   }
}
