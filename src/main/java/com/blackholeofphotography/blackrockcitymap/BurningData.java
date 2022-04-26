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

/**
 * The BRC City data is a CSV file with three fields per line:
 * Name, Value, Description
 * For example: GS_LAT,40.7864,Golden Spike Latitude
 * The name is GS_LAT, the value 40.7864, and the description is "Golden Spike Latitude"
 * 
 * This class reads in one of the CSV files and produces two Map's. One of Name/Value and the other of Name/Description.
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */


public class BurningData
{
   private Map<String,String> Value = new HashMap<>();
   private Map<String,String> Description = new HashMap<>();
   private StreetMap strMap;
   
   /**
    * Location of the GoldenSpike
    * @return Location of GoldenSpike
    */
   public LLALocation GS ()
   {
      return getLLALocation ("GS");
   } 

   /**
    * Location of P1 on the perimeter fence
    * @return Location of P1
    */
   public LLALocation P1 () 
   {
      return getLLALocation ("P1");
   }
   
   /**
    * Get the key value as a double, print an error if it's missing
    * @param key Key name (first column in csv)
    * @return Value as double (second column in csv)
    */
   private double getDoubleValue (String key)
   {
      String s = Value.get (key);
      if (s == null)
         System.out.println ("Key value " + key + " not found");
      
      return Double.parseDouble (s);
   }

   /**
    * Radius of center of Esplanade
    * @return Radius in feet.
    */
   public double getEsplanadeRadius  () {return getDoubleValue ("ERCR");}

   /**
    * Distance from man to center of center camp.
    * @return Distance in feet.
    */
   private double getManToCenterCampRadius () {return getDoubleValue ("MCCR");}

   /**
    * Distance from man to center of center camp.
    * @return Distance in feet.
    */
   private double getManToTempleRadius () {return getDoubleValue ("MTR");}
   /**
    * Inner radius of the center theme camps
    * @return Inner radius in feet
    */
   public double getCenterThemeCampInnerRadius () {return getDoubleValue  ("CTCIR");}
   /**
    * Outer radius of the center them camps.
    * @return Outer radius in feet
    */
   public double getCenterThemeCampOuterRadius () {return getDoubleValue  ("CTCOR");}
   /**
    * Width of the center camp keyhole at its widest point.
    * @return Keyhole width in feet.
    */
   public double getCenterCampKeyholeWidest () {return getDoubleValue  ("PCCPKW");}

   /**
    * Width of the center camp keyhole at its narrowest point.
    * @return Keyhole width in feet.
    */
   public double getCenterCampKeyholeNarrowest () {return getDoubleValue  ("PCCPKN");}
   
   /**
    * Radius of the center of Rod's Road.
    * @return Radius in feet.
    */
   private double getRodsRoadRadius () {return getDoubleValue  ("RRCR");}

   /**
    * Width of a regular street.
    * @return Width of a street in feet.
    * @apiNote 2014 (and possibly earlier years) had regular streets and skinny streets. 
    * Hence the name here. 
    */
   public double getRegularStreetWidth () {return getDoubleValue  ("RSW");}
   
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
      double NS = getDoubleValue  ("MPC_DDD");

      return 360.0 - NS;
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
         dist += getRegularStreetWidth ();
         for (char ch='A'; ch<roadLetter; ch++)
         {
            dist += getBlockDepth (ch);
            dist += getRegularStreetWidth ();
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
      return getDoubleValue  ("PR");
   }
  

   /**
    * Radius of  plaza around man
    * @return Man Plaza radius in feet
    */
   public double getManPlazaRadius ()
   {
      return getDoubleValue  ("MPR");
   }

   /**
    * Radius of plaza around temple
    * @return Temple Plaza radius in feet
    */
   public double getTemplePlazaRadius ()
   {
      return getDoubleValue  ("TPR");
   }
   
   /**
    * Width of a portal at its mouth (widest point)
    * @return Portal width in feet
    */
   public double getPortalWidth ()
   {
      return getDoubleValue ("PW");
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
   public String getStreetName (char roadLetter)
   {
      String tag = "" + roadLetter + "RN";
      return (Value.get (tag));
   }


   private static double ft2KM (double ft)
   {
      return ft * 0.0003048;
   }
   
   /**
    * Return an LLALocation for one of the known prefixes. 
    * @param prefix Prefix to use. Really either GS or P1
    * @return The location
    */
   private LLALocation getLLALocation (String prefix)
   {
      double elev = getDoubleValue  ("GS_ELEV");
      elev = ft2KM (elev) * 1000;
      return new LLALocation (getDoubleValue (prefix+"_LAT"), getDoubleValue (prefix+"_LON"), elev);
   }
   
   /**
    * Get the depth of a block, not including any roads.
    * @param roadLetter Letter of the inside annular street for the block.
    * @return The depth in feet
    */
   public double getBlockDepth (char roadLetter)
   {
//      if (roadLetter == AnnularStreet.ESPLANADE)
//         return 400;
       if (roadLetter == 'L')
           return 0;
      
      return getDoubleValue  ("BD"+roadLetter);
   }


   public BurningData (String dataFile) 
   {
      try
      {
         List<String> in;
         ClassLoader classloader = Thread.currentThread().getContextClassLoader();
         
         URL u = classloader.getResource(dataFile + "-City-Map.csv");
         InputStream resource = u.openStream ();
         
         in = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
         for (String s : in)
         {
            ArrayList<String> columns = CSVHandler.parseCSVLine (s);
            if (columns.get(0).length() > 0 && columns.get(1).length() > 0)
            {
               Value.put (columns.get(0), columns.get(1));
               Description.put (columns.get(0), columns.get(2));
            }
         }
         
         strMap = new StreetMap (dataFile + "-StreetMap.txt");
      }
      catch (IOException ex)
      {
         Logger.getLogger (BurningData.class.getName()).log (Level.SEVERE, null, ex);
      }
   }
}
