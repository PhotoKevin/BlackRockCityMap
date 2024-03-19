/*
 * Copyright (c) 2023, Kevin Nickerson
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

import com.blackholeofphotography.blackrockcitymap.path.Path;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import mil.nga.sf.geojson.FeatureConverter;
import mil.nga.sf.geojson.GeometryCollection;
import mil.nga.sf.geojson.LineString;
import mil.nga.sf.geojson.Position;

/**
 *
 * @author Kevin Nickerson
 */
public class BurningGeoJSON
{

   public static void createGeoJSON (int year)
   {
      BlackRockCity city = new BlackRockCity (year);
      ArrayList<Path> drawing = city.drawCity ();
      ArrayList<mil.nga.sf.geojson.Geometry> geo = new ArrayList<> ();


      for (Path pp : drawing)
      {
         LineString ls = new LineString ();
         ArrayList<Position> line = new ArrayList<> ();
         for (de.micromata.opengis.kml.v_2_2_0.Coordinate c : pp.toCoordinates ())
         {
            Position p = new Position ((double) c.getLongitude (), (double) c.getLatitude ());
            line.add (p);
         }
         ls.setCoordinates (line);
         geo.add (ls);
      }
// https://github.com/ngageoint
// https://ngageoint.github.io/simple-features-geojson-java/docs/api/index.html


      GeometryCollection gc = new GeometryCollection (geo);

      String content; // = FeatureConverter.toStringValue(geometry);
      content = FeatureConverter.toStringValue (gc);
      
      //System.out.println (k.toString());
      File ko = new File (year + "BRC.json");
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(ko)))
      {
         writer.write (content);
      }
      catch (IOException ex)
      {
         
      }            
   }   
}
