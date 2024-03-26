/*
 * Copyright (c) 2024, Kevin Nickerson
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
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.util.ArrayList;

/**
 *
 * @author Kevin Nickerson
 */
public class PathCircle  extends PathSegment
{
   LLALocation centerPoint;
   LLALocation startPoint;

   PathCircle (LLALocation center, LLALocation edge)
   {
      centerPoint = center;
      startPoint = edge;
   }
   
   PathCircle (LLALocation center, double radiusFT)
   {
      centerPoint = center;
      startPoint = center.moveFT (0, radiusFT);
   }

   @Override
   ArrayList<Coordinate> toCoordinates ()
   {
      double startBearing = centerPoint.getBearing (startPoint);
      double endBearing = centerPoint.getBearing (startPoint);
      double radiusFT = centerPoint.distanceFT (startPoint);

      ArrayList<LLALocation> points = PathArc.addArcFT (startBearing, centerPoint, endBearing, radiusFT, ArcDirection.CLOCKWISE);
      
      ArrayList<Coordinate> c = new ArrayList<> ();
      for (LLALocation g : points)
         c.add (g.toCoordinate ());
      
      return c;
   }

   @Override
   LLALocation startPoint ()
   {
      return startPoint;
   }

   @Override
   LLALocation endPoint ()
   {
      return startPoint;
   }

   @Override
   Path2D toPath2D (LLALocation GPSBaseCoordinate, Path2D path)
   {
      SVGGPSCoordinate center = new SVGGPSCoordinate (GPSBaseCoordinate, centerPoint);
      double r = Math.abs (startPoint.distanceFT (centerPoint));
      
      Ellipse2D.Double circle = new Ellipse2D.Double(center.xCoordinate ()-r, center.yCoordinate ()-r, r*2, r*2);
      path.append (circle, false);

      return path;
   }

   @Override
   PathBounds getBounds ()
   {
      double r = startPoint.distanceFT (centerPoint);
      r *= 1.5;
      
      LLALocation ul = centerPoint.moveFT (360-45, r);
      LLALocation lr = centerPoint.moveFT (90+45, r);
      
      return new PathBounds (ul,lr);
   }
}
