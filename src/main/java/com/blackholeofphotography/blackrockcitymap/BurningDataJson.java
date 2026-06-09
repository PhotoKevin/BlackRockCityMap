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


import com.blackholeofphotography.llalocation.LLALocation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;


/**
 * The BRC City data is a JSON file.
 */


public class BurningDataJson
{
//   private Map<String,String> Value = new HashMap<>();
   private StreetMap strMap;
   private int year;
   private LLALocation goldenSpike;
   JSONObject cityData;
   
   
   
   public BurningDataJson (int aYear) 
   {
      this.year = aYear;
      try
      {
         List<String> in;
         ClassLoader classloader = Thread.currentThread().getContextClassLoader();
         
         URL u = classloader.getResource(String.format ("%d-City-Map.json", year));
        
         InputStream resource = u.openStream ();
         StringBuilder sb = new StringBuilder ();

         in = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
         for (String s : in)
            sb.append (s);
         
         cityData = new JSONObject (sb.toString ());
         
//         var it =  cityData.keys ();
//         while(it.hasNext()) 
//            System.out.println(it.next());
         
         strMap = new StreetMap (String.format ("%d-StreetMap.txt", year));
      }
      catch (JSONException | IOException ex)
      {
         Logger.getLogger (BurningDataJson.class.getName()).log (Level.SEVERE, null, ex);
      }
   }
   
   /**
    * Location of the GoldenSpike
    * @return Location of GoldenSpike
    */
   public LLALocation GS ()
   {
      if (goldenSpike == null)
      {
         JSONObject gs = cityData.getJSONObject ("golden_spike");
         double latitude = gs.getDouble ("latitude");
         double longitude = gs.getDouble ("longitude");
         double altitudeFT = gs.getInt ("elevation");
         double elevationMeters = ft2KM (altitudeFT) * 1000;

         goldenSpike = new LLALocation (latitude, longitude, elevationMeters);
      }

      return goldenSpike;
   } 

   /**
    * Location of P1 on the perimeter fence
    * @return Location of P1
    */
   public LLALocation P1 () 
   {
      JSONObject p1 = cityData.getJSONObject ("p1");
      double latitude = p1.getDouble ("latitude");
      double longitude = p1.getDouble ("longitude");
      double elevationMeters = GS ().getAltitude ();

      return new LLALocation (latitude, longitude, elevationMeters);
   }
   
   public int getYear ()
   {
      return year;
   }
   /**
    * Get the key value as a double, print an error if it's missing
    * @param key Key name (first column in CSV)
    * @return Value as double (second column in CSV)
    */
//   private double getDoubleValue (String key)
//   {
//      String s = Value.get (key);
//      if (s == null)
//      {
//         System.out.println ("Key value " + key + " not found");
//         return 0;
//      }
//      
//      return Double.parseDouble (s);
//   }

   /**
    * Radius of center of Esplanade
    * @return Radius in feet.
    */
   public double getEsplanadeRadius  () 
   {
      return cityData.getDouble ("esplanade_radius");
   }

   /**
    * Distance from man to center of center camp.
    * @return Distance in feet.
    */
   private double getManToCenterCampRadius () 
   {
      return cityData.getDouble ("center_camp_man_distance");
   }

   /**
    * Distance from man to center of center camp.
    * @return Distance in feet.
    */
   private double getManToTempleRadius () 
   {
      return cityData.getDouble ("temple_man_distance");
   }
   /**
    * Inner radius of the center theme camps
    * @return Inner radius in feet
    */
   public double getCenterThemeCampInnerRadius () 
   {
      JSONObject centerCamp = cityData.getJSONObject ("center_camp");
      return centerCamp.getDouble ("inner_radius");
   }
   
   /**
    * Outer radius of the center them camps.
    * @return Outer radius in feet
    */
   public double getCenterThemeCampOuterRadius () 
   {
      JSONObject centerCamp = cityData.getJSONObject ("center_camp");
      return centerCamp.getDouble ("outer_radius");
   }
   /**
    * Width of the center camp keyhole at its widest point.
    * @return Keyhole width in feet.
    */
   public double getCenterCampKeyholeWidest () 
   {
      JSONObject centerCamp = cityData.getJSONObject ("center_camp");
      return centerCamp.getDouble ("keyhole_widest");
   }

   /**
    * Width of the center camp keyhole at its narrowest point.
    * @return Keyhole width in feet.
    */
   public double getCenterCampKeyholeNarrowest () 
   {
      JSONObject centerCamp = cityData.getJSONObject ("center_camp");
      return centerCamp.getDouble ("keyhole_narrowest");
   }
   
   /**
    * Radius of the center of Rod's Road.
    * @return Radius in feet.
    */
   private double getRodsRoadRadius () 
   {
      // TODO: Support old map
      return cityData.getDouble ("temple_man_distance");
   }

   /**
    * Width of a regular street.
    * @return Width of a street in feet.
    * @apiNote 2014 (and possibly earlier years) had regular streets and skinny streets. 
    * Hence the name here. 
    */
   public double getRegularStreetWidth () 
   {
      return cityData.getDouble ("street_width_radial");
   }
   
   public double getAnnularWidth (char roadLetter)
   {
      if (roadLetter == 'S')
         System.out.print ("");

      JSONObject annularStreets = cityData.getJSONObject ("annular_streets");
      JSONObject streetData;
      if (roadLetter == AnnularStreet.ESPLANADE)
         streetData = annularStreets.getJSONObject ("esplanade");
      else
         streetData = annularStreets.getJSONObject (String.valueOf (roadLetter).toLowerCase ());
      
      return streetData.getDouble ("width");
   }

   public double getRadialWidth ()
   {
      return cityData.getDouble ("street_width_radial");
   }

   public double getPedestrianWidth ()
   {
      return cityData.getDouble ("street_width_pedestrian");
   }
   
   /**
    * Get the LLALocation of Center Camp.
    * @return The location of center camp
    */
   public LLALocation getCenterCampLLA ()
   {
      return this.GS ().moveFT (this.getBearing (new RadialStreet ("6:00")), this.getManToCenterCampRadius ());
   }
   
   public LLALocation getTempleLLA ()
   {
      return this.GS ().moveFT (this.getBearing (new RadialStreet ("12:00")), this.getManToTempleRadius ());
   }

   
   /**
    * The offset of True north vs the 12:00 radial street.
    * @return Offset in degrees
    */
   private double TrueNorthOffset ()
   {
      double bearing = GS ().getBearing (P1 ());
      // TODO: Do I really need this?
      return 45;
   }
   
   /**
    * Get the compass bearing of the street
    * @param radial 
    * @return Bearing in degrees from north
    */
   public double getBearing (RadialStreet radial)
   {
      double timeVal = radial.getHour ();
      double offset = TrueNorthOffset ();
      // 10.5 = 0.0
      // 4.5 = 180
      // 1 getHour = 360 / 12 = 30 degrees
      // 10:30 = 

      return ((timeVal * 360.0 / 12.0) % 360) + offset;
   }

   /**
    * Distance from the center of an annular street to the man
    * @param roadLetter Letter of the Annular Street
    * @return Distance in feet.
    */
   public double getStreetRadiusFT (char roadLetter)
   {
      double dist = this.getEsplanadeRadius ();

      switch (roadLetter)
      {
      case AnnularStreet.ESPLANADE:
         return dist;
      case AnnularStreet.RODS_ROAD:
         return this.getRodsRoadRadius ();
      case AnnularStreet.INNER_CIRCLE:
         return this.getCenterThemeCampOuterRadius ();
      case AnnularStreet.TEMPLE:
         return this.getTemplePlazaRadius ();
      case AnnularStreet.THE_MAN:
         return this.getManPlazaRadius ();
      default:
         dist += getBlockDepth (AnnularStreet.ESPLANADE);
         dist += getAnnularWidth (AnnularStreet.ESPLANADE);
         for (char ch='A'; ch<roadLetter; ch++)
         {
            dist += getBlockDepth (ch);
            dist += getAnnularWidth (ch); // getRegularStreetWidth ();
         }
      }
      return dist;
   }

   /**
    * Radius of a plaza
    * @return Plaza radius in feet
    */
   public double getPlazaRadius ()
   {
      return cityData.getDouble ("plaza_radius");
   }
   
   public double getBPlazaDepth ()
   {
      return cityData.getDouble ("b_plaza_depth");
   }

   public double getBPlazaWidth ()
   {
      return cityData.getDouble ("b_plaza_width");
   }


   /**
    * Radius of  plaza around man
    * @return Man Plaza radius in feet
    */
   public double getManPlazaRadius ()
   {
      // The CSV claims radius, but checking G Earth, it's diameter.
      return cityData.getDouble ("man_plaza_radius");
   }

   /**
    * Radius of plaza around temple
    * @return Temple Plaza radius in feet
    */
   public double getTemplePlazaRadius ()
   {
      return cityData.getDouble ("temple_plaza_radius");
   }
   
   /**
    * Width of a portal at its mouth (widest point)
    * @return Portal width in feet
    */
   public double getPortalWidth ()
   {
      return cityData.getDouble ("portal_mouth_width");
   }

   /**
    * Determine if this intersection is a Plaza
    * @param intersection
    * @return true/false
    */
   public boolean isPlaza (Intersection intersection)
   {
      return this.strMap.isPlaza (intersection);
   }
   
   public boolean isBPlaza (Intersection intersection)
   {
      return this.strMap.isBPlaza (intersection);
   }
   
   /**
    * Determine if this intersection is a Portal.
    * @param intersection
    * @return true/false
    */
   public boolean isPortal (Intersection intersection)
   {
      return this.strMap.isPortal (intersection);
   }
   
   /**
    * Determine if this intersection is a Plaza Portal.
    * @param intersection intersection of interest.
    * @return true/false
    */
   public boolean isPlazaPortal (Intersection intersection)
   {
      return this.strMap.isPlazaPortal (intersection);
   }
   
      /**
    * Determine if this intersection is a Mid - Plaza Portal.
    * That's the intersections between a portal and its plaza
    * @param intersection intersection of interest.
    * @return true/false
    */
   public boolean isMidPlazaPortal (Intersection intersection)
   {
      return this.strMap.isMidPlazaPortal (intersection);
   }

   public char maxRoadLetter ()
   {
       return strMap.maxRoadLetter();
   }
   /**
    * Determine if there is a road toward the man from
    * this intersection
    * @param intersection The intersection of interest.
    * @return true/false
    */
   
   public boolean existsMansideRoad (Intersection intersection)
   {
      return this.strMap.existsMansideRoad (intersection);
   }

   /**
    * Determine if there is a road away from the man from
    * this intersection
    * @param intersection The intersection of interest.
    * @return true/false
    */
   
   public boolean existsOutsideRoad (Intersection intersection)
   {
      return this.strMap.existsOutsideRoad (intersection);
   }
   
   /**
    * Determine if there is a road in the Counter Clockwise direction from
    * this intersection
    * @param intersection The intersection of interest.
    * @return true/false
    */
   public boolean existsCounterClockwiseRoad (Intersection intersection)
   {
      return this.strMap.existsCounterClockwiseRoad (intersection);
   }

   /**
    * Determine if there is a road in the Clockwise direction from
    * this intersection
    * @param intersection The intersection of interest.
    * @return true/false
    */
   public boolean existsClockwiseRoad (Intersection intersection)
   {
      return this.strMap.existsClockwiseRoad (intersection);
   }
   
   public boolean isPedestrianWalkway (Intersection intersection, ManDirection direction)
   {
      return this.strMap.isPedestrianWalkway (intersection, direction);
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
      return strMap.getBlockCorners (earlyInside);
   }
   
   /**
    * Name of an annular street
    * @param roadLetter Letter of the street
    * @return Name of the street.
    */
   public String getStreetNameXXX (char roadLetter)
   {
      // TODO: Pretty sure this isn't needed
      String tag = "" + roadLetter + "RN";
      return tag;
   }


   private static double ft2KM (double ft)
   {
      return ft * 0.0003048;
   }
      
   /**
    * Get the depth of a block, not including any roads.
    * @param roadLetter Letter of the inside annular street for the block.
    * @return The depth in feet
    */
   public double getBlockDepth (char roadLetter)
   {
      if (roadLetter == 'S')
         System.out.print ("");
      JSONObject annularStreets = cityData.getJSONObject ("annular_streets");
      JSONObject streetData;
      if (roadLetter == AnnularStreet.ESPLANADE)
         streetData = annularStreets.getJSONObject ("esplanade");
      else
         streetData = annularStreets.getJSONObject (String.valueOf (roadLetter).toLowerCase ());

      return streetData.getDouble ("depth");
   }
   
   public void setGoldenSpikeOverride (LLALocation gs)
   {
      goldenSpike = gs;
   }
}
