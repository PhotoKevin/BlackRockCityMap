/*
 * Copyright (c) 2018, Kevin Nickerson (kevin@blackholeofphotography.com)
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

// https://github.com/micromata/javaapiforkml/tree/master/src

//import com.blackholeofphotography.blackrockcitymap.KML.Folder;
import com.blackholeofphotography.blackrockcitymap.path.Path;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.LineString;
import de.micromata.opengis.kml.v_2_2_0.LineStyle;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
//import de.micromata.opengis.kml.v_2_2_0.Placemark;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */


public class BurningKML
{
   public BurningKML ()
   {
   }

   private Folder findFolderByName (Folder rootFolder, String name)
   {
      
      for (Feature f : rootFolder.getFeature ())
      {
         if (f instanceof Folder == true)
         {
            Folder ff = (Folder) f;
            if (ff.getName ().equalsIgnoreCase (name))
               return ff;
         }
      }

      return rootFolder.createAndAddFolder ().withName (name);
   }

   public Folder addSubFolder (Folder rootFolder, String SubFolder)
   {
      String                  nextLevel;
      String                  rest;

      nextLevel               =  SubFolder;
      rest                    =  null;

      // Get top of list
      while (true)
      {
         File                 f  = new File (nextLevel);

         if (f.getParent() != null)
         {
            nextLevel         =  f.getParent ();
            if (rest == null)
               rest           =  f.getName ();
            else
               rest           =  f.getName () + "/" + rest;
         }
         else
            break;
      }
      
//      System.out.println ("Done " + nextLevel + " .. " + rest);
      if (nextLevel != null) 
      {
         Folder ff = findFolderByName (rootFolder, nextLevel);
         
         if (rest != null)
            return (addSubFolder (ff, rest));
         else
            return (ff);
      }
      
      return rootFolder;
   }
   
  
   public static void createKML (String baseFilename, int year, ArrayList<Path> drawing)
   {
      try
      {
         BurningKML bk;
         bk = new BurningKML ();
         Folder fYear;
         Kml root;
         root = new Kml ();
         fYear = root.createAndSetFolder ().withName (String.valueOf (year));


         for (Path pp : drawing)
         {
            Folder f = bk.addSubFolder (fYear, pp.getName ());
            Placemark place = f.createAndAddPlacemark ();
            LineString ls = place.createAndSetLineString ();
            ls.setCoordinates (pp.toCoordinates ());

            place.createAndAddStyle ().withLineStyle ( new LineStyle().withColor (pp.getKMLColor ()).withWidth (2.0));        
         }

         //System.out.println (k.toString());
         File ko = new File (baseFilename + ".kml");
         try (BufferedWriter writer = new BufferedWriter(new FileWriter(ko)))
         {
            root.marshal (writer);
         }
         catch (IOException ex)
         {

         }      
      }
      catch (Exception ex)
      {
         System.out.println (ex);
      }
   }
}
