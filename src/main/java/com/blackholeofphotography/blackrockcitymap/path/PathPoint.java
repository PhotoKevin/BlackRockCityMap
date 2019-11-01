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
public class PathPoint extends PathSegment
{
   LLALocation point;
   
   PathPoint (LLALocation point)
   {
      this.point = point;
   }

   @Override
   ArrayList<Coordinate> toCoordinates ()
   {
      ArrayList<Coordinate> points = new ArrayList<> ();
      points.add (point.toCoordinate ());
              
      return points;
   }

   @Override
   LLALocation startPoint ()
   {
      return this.point;
   }

   @Override
   LLALocation endPoint ()
   {
      return this.point;
   }

   @Override
   Path2D toPath2D (LLALocation GPSBaseCoordinate, Path2D path)
   {
      SVGGPSCoordinate s = new SVGGPSCoordinate (GPSBaseCoordinate, point);

      if (path.getCurrentPoint () == null)
         path.moveTo (s.xCoordinate (), s.yCoordinate ());
      else
         path.lineTo (s.xCoordinate (), s.yCoordinate ());
      
      return path;
   }

   @Override
   PathBounds getBounds ()
   {
      return new PathBounds (this.point, this.point);
   }
}
