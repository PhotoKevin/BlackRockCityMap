package com.blackholeofphotography.blackrockcitymap;


import com.blackholeofphotography.llalocation.LLALocation;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The BRC City data is a JSON file.
 */


public class CityData extends StreetMap 
{
   private static final Logger logger = LoggerFactory.getLogger(CityData.class);
   private final int mYear;
   private LLALocation goldenSpike;
   private JSONObject cityData;
   
   public CityData (int aYear) 
   {
      super (getStreetMap (aYear));

      mYear = aYear;
      try
      {
         List<String> in;
         ClassLoader classloader = Thread.currentThread().getContextClassLoader();
         
         URL u = classloader.getResource(String.format ("%d_City-Data.json", mYear));
        
         InputStream resource = u.openStream ();
         StringBuilder sb = new StringBuilder ();

         in = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8)).lines().collect(Collectors.toList());
         for (String s : in)
            sb.append (s);
         
         cityData = new JSONObject (sb.toString ());
      }
      catch (JSONException | IOException ex)
      {
         logger.error ("BurningDataJson.BurningDataJson", ex);
      }
   }
   
   private static String getStreetMap (int year)
   {
      return String.format ("%d_StreetMap.txt", year);
   }
   
   /**
    * Location of the GoldenSpike
    * @return Location of GoldenSpike
    */
   public LLALocation GS ()
   {
      if (goldenSpike == null)
      {
         try
         {
            JSONObject gs = cityData.getJSONObject ("golden_spike");
            double latitude = gs.getDouble ("latitude");
            double longitude = gs.getDouble ("longitude");
            double altitudeFT = gs.getInt ("elevation");
            double elevationMeters = ft2KM (altitudeFT) * 1000;

            goldenSpike = new LLALocation (latitude, longitude, elevationMeters);
         }
         catch (JSONException e)
         {
            throw new RuntimeException (e);
         }
      }

      return goldenSpike;
   } 

   /**
    * Location of P1 on the perimeter fence
    *
    * @return Location of P1
    */
   public LLALocation getP1 () 
   {
      try
      {
         JSONObject p1 = cityData.getJSONObject ("p1");
         double latitude = p1.getDouble ("latitude");
         double longitude = p1.getDouble ("longitude");
         double elevationMeters = GS ().getAltitude ();

         return new LLALocation (latitude, longitude, elevationMeters);
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }
   
   public int getYear ()
   {
      return mYear;
   }

   /**
    * Radius of center of Esplanade
    *
    * @return Radius in feet.
    */
   public double getEsplanadeRadius  () 
   {
      try
      {
         return cityData.getDouble ("esplanade_radius");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   /**
    * Distance from man to center of center camp.
    *
    * @return Distance in feet.
    */
   private double getManToCenterCampRadius () 
   {
      try
      {
         return cityData.getDouble ("center_camp_man_distance");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   /**
    * Distance from man to center of center camp.
    *
    * @return Distance in feet.
    */
   private double getManToTempleRadius () 
   {
      try
      {
         return cityData.getDouble ("temple_man_distance");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }
   
   public int getRoute66Radius ()
   {
      try
      {
         JSONObject centerCamp = cityData.getJSONObject ("center_camp");
         return centerCamp.getInt ("route66_radius");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   /**
    * Inner radius of the center theme camps
    *
    * @return Inner radius in feet
    */
   public double getCenterThemeCampInnerRadius () 
   {
      try
      {
         JSONObject centerCamp = cityData.getJSONObject ("center_camp");
         return centerCamp.getDouble ("inner_radius");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }
   
   /**
    * Outer radius of the center them camps.
    *
    * @return Outer radius in feet
    */
   public double getCenterThemeCampOuterRadius () 
   {
      try
      {
         JSONObject centerCamp = cityData.getJSONObject ("center_camp");
         return centerCamp.getDouble ("outer_radius");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   /**
    * Width of the center camp keyhole at its widest point.
    *
    * @return Keyhole width in feet.
    */
   public double getCenterCampKeyholeWidest () 
   {
      try
      {
         JSONObject centerCamp = cityData.getJSONObject ("center_camp");
         return centerCamp.getDouble ("keyhole_widest");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   /**
    * Width of the center camp keyhole at its narrowest point.
    *
    * @return Keyhole width in feet.
    */
   public double getCenterCampKeyholeNarrowest ()
   {
      try
      {
         JSONObject centerCamp = cityData.getJSONObject ("center_camp");
         return centerCamp.getDouble ("keyhole_narrowest");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }
   
   /**
    * Radius of the center of Rod's Road.
    *
    * @return Radius in feet.
    */
   private double getRodsRoadRadius ()
   {
      try
      {
         return cityData.getDouble ("temple_man_distance");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   /**
    * Width of a regular street.
    *
    * @return Width of a street in feet.
    * @apiNote 2014 (and possibly earlier years) had regular streets and skinny streets.
    * Hence, the name here.
    */
   public double getRegularStreetWidth ()
   {
      try
      {

         return cityData.getDouble ("street_width_radial");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   public double getAltitudeMeters ()
   {
      double elev = this.GS ().getAltitude ();
      return ft2KM (elev) * 1000;
   }
   
   public double getAnnularWidth (char roadLetter)
   {
      try
      {
         JSONObject annularStreets = cityData.getJSONObject ("annular_streets");
         JSONObject streetData;
         if (roadLetter == AnnularStreet.ESPLANADE)
            streetData = annularStreets.getJSONObject ("esplanade");
         else
            streetData = annularStreets.getJSONObject (String.valueOf (roadLetter).toLowerCase ());

         return streetData.getDouble ("width");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }
   
   public boolean hasAnnularStreet (char roadLetter)
   {
      try
      {
         JSONObject annularStreets = cityData.getJSONObject ("annular_streets");
         if (roadLetter == AnnularStreet.ESPLANADE)
            return annularStreets.has ("esplanade");
         else
            return annularStreets.has (String.valueOf (roadLetter).toLowerCase ());
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }


   public double getRadialWidth ()
   {
      try
      {
         return cityData.getDouble ("street_width_radial");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   public double getPedestrianWidth ()
   {
      try
      {
         return cityData.getDouble ("street_width_pedestrian");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
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
    * This has always been 45 degrees, but we do the math anyway.
    * @return Offset in degrees
    */
   private double TrueNorthOffset ()
   {
      double bearing = GS ().getBearing (getP1 ());
      bearing += 2 * 360.0 / 5; // 12:00 is at P3 or 2/5 of the circle away

      return normalizeAngle (bearing);
   }
   
   /**
    * Get the compass bearing of the street
    *
    * @param radial 
    * @return Bearing in degrees from north
    */
   public double getBearing (RadialStreet radial)
   {
      double hours = radial.getMinutes () / 60.0;
      double offset = TrueNorthOffset ();
      // 10.5 = 0.0
      // 4.5 = 180
      // 1 getHour = 360 / 12 = 30 degrees
      // 10:30 = 

      return ((hours * 360.0 / 12.0) % 360) + offset;
   }

   private double normalizeAngle (double angle)
   {
      while (angle < 0.0)
         angle += 360.0;

      while (angle > 360.0)
         angle -= 360.0;
      return angle;
   }

   private double normalizeTime (double time)
   {
      while (time < 0.0)
         time += 12.0;

      while (time > 12.0)
         time -= 12.0;

      return time;
   }

   /**
    * Distance from the center of an annular street to the man
    *
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
      case AnnularStreet.ROUTE_66:
         return this.getRoute66Radius ();
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
    *
    * @return Plaza radius in feet
    */
   public double getPlazaRadius ()
   {
      try
      {
         return cityData.getDouble ("plaza_radius");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }
   
   public double getBPlazaDepth ()
   {
      try
      {
         return cityData.getDouble ("b_plaza_depth");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   public double getBPlazaWidth ()
   {
      try
      {
         return cityData.getDouble ("b_plaza_width");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }


   /**
    * Radius of  plaza around man
    *
    * @return Man Plaza radius in feet
    */
   public double getManPlazaRadius ()
   {
      try
      {
         // The CSV claims radius, but checking G Earth, it's diameter.
         return cityData.getDouble ("man_plaza_radius");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   /**
    * Radius of plaza around temple
    *
    * @return Temple Plaza radius in feet
    */
   public double getTemplePlazaRadius ()
   {
      try
      {
         return cityData.getDouble ("temple_plaza_radius");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }
   
   /**
    * Width of a portal at its mouth (widest point)
    *
    * @return Portal width in feet
    */
   public double getPortalWidth ()
   {
      try
      {
         return cityData.getDouble ("portal_mouth_width");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }

   public boolean isCorner (Intersection i)
   {
      if (! existsIntersection (i))
         return false;
      
      return ((existsOutsideRoad (i) || existsMansideRoad (i)) &&
              ((existsClockwiseRoad (i) || existsCounterClockwiseRoad (i))));
   }
   


   public Intersection getIntersection (int hour, int minute, char streetLetter)
   {
      return new Intersection (hour, minute, streetLetter);
   }

   public ArrayList<Intersection> getAllIntersections ()
   {
      ArrayList<Intersection> intersections = new ArrayList<> ();

      for (int hour=2; hour<10; hour++)
      {
         for (int quarterHour = 0; quarterHour < 4; quarterHour++)
         {
            Intersection startCorner = getIntersection (hour, quarterHour * 15, AnnularStreet.ESPLANADE);
            if (existsIntersection (startCorner))
               intersections.add (startCorner);
         }
      }

      for (char ch = 'A'; ch<= getMaxRoadLetter (); ch++)
      {
         for (int hour = 2; hour < 10; hour++)
         {
            for (int quarterHour = 0; quarterHour < 4; quarterHour++)
            {
               Intersection i = getIntersection (hour, quarterHour * 15, ch);
               if (existsIntersection (i))
                  intersections.add (i);
            }
         }
         Intersection edge = getIntersection (10, 0, ch);
         intersections.add (edge);

      }
      Intersection edge = getIntersection (10, 0, AnnularStreet.ESPLANADE);
      intersections.add (edge);
      return intersections;
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
    *
    * @param roadLetter Letter of the inside annular street for the block.
    * @return The depth in feet
    */
   public double getBlockDepth (char roadLetter)
   {
      try
      {
         JSONObject annularStreets = cityData.getJSONObject ("annular_streets");
         JSONObject streetData;
         if (roadLetter == AnnularStreet.ESPLANADE)
            streetData = annularStreets.getJSONObject ("esplanade");
         else
            streetData = annularStreets.getJSONObject (String.valueOf (roadLetter).toLowerCase ());

         return streetData.getDouble ("depth");
      }
      catch (JSONException e)
      {
         throw new RuntimeException (e);
      }
   }
   
   public void setGoldenSpikeOverride (LLALocation gs)
   {
      goldenSpike = gs;
   }
}
