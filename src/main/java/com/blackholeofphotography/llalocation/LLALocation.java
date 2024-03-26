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
package com.blackholeofphotography.llalocation;

import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import java.util.ArrayList;

/**
 * Represent a location on Earth as a Latitude, Longitude, and Altitude.
 * 
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class LLALocation
{
   public static final  int LONGITUDE            =  1;
   public static final  int LATITUDE             =  2;
   public static final  int LONGITUDE_DEG        =  3;
   public static final  int LATITUDE_DEG         =  4;
   public static final  int LONGITUDE_HEMISPHERE =  5; 
   public static final  int LATITUDE_HEMISPHERE  =  6;
   
   private static final  double MILES_PER_KM =  0.621371192;
   private static final  double R_KM            =  6371.0; // Radius of Earth in Kilometers


   private final double    latitude;
   private final double    longitude;
   private final double    altitude; // Meters
   
   /**
    * Create a new location.
    * @param lat Latitude in degrees
    * @param longi Longitude in degrees
    * @param meters Altitude in meters
    */
   public LLALocation (double lat, double longi, double meters)
   {
      latitude          =  lat;
      longitude         =  longi;
      altitude          =  meters;
   }

   /**
    * Calculate the bearing from here to the specified location.
    * @param lla The location
    * @return Bearing in degrees. 0 is North, 90 is East.
    */
   public double getBearing (LLALocation lla)
   {
      double               bearing;
      double               lat1, lon1;
      double               lat2, lon2;
      double               dLon;

      double               x, y;

      lat1                 =  Math.toRadians (this.latitude);
      lon1                 =  Math.toRadians (this.longitude);
      if (lla == null)
         System.out.println ();

      lat2                 =  Math.toRadians (lla.latitude);  
      lon2                 =  Math.toRadians (lla.longitude);

      dLon                 =  lon2 - lon1;

      y                    =  Math.sin(dLon) * Math.cos(lat2);
      x                    =  Math.cos(lat1)*Math.sin(lat2) -
                                 Math.sin(lat1)*Math.cos(lat2)*Math.cos(dLon);

      bearing              =  Math.toDegrees (Math.atan2(y, x));
      
      while (bearing > 360) bearing -= 360;
      while (bearing < 0) bearing += 360;

      return (bearing);
   }

   /**
    * Calculate the distance from here to the specified location.
    * @param lla The location.
    * @return Distance to the location.
    */
   public double distanceKM (LLALocation lla)
   {
      //final double         prec = 1000000.0;
      double               km;
      double               lat1, lon1;
      double               lat2, lon2;
      double               tmp;
      double               part1, part2;

      lat1                 =  Math.toRadians (this.latitude);
      lon1                 =  Math.toRadians (this.longitude);


      lat2                 =  Math.toRadians (lla.latitude);  
      lon2                 =  Math.toRadians (lla.longitude); 

      // If you just do the math in place, you'll get 
      // rounding errors when the two points are too close 
      // together and end up taking an acos of something 
      // like 1.000000002
//      tmp   =    (Math.sin(lat1)*Math.sin(lat2) +
//                  Math.cos(lat1)*Math.cos(lat2) *
//                  Math.cos(lon2-lon1));

      part1 =  Math.sin(lat1) * Math.sin(lat2);
      part2 =  Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon2-lon1);

      tmp         =  part1 + part2;

      if (tmp > 1.0)
         tmp = 1.0;

      km    =  Math.acos(tmp) * LLALocation.R_KM;
      if (Double.isNaN(km))
      {
//         System.out.println ("lat: " + this.latitude); 
//         System.out.println ("lon: " + this.longitude);
//
//         System.out.println ("lat: " + g.latitude);
//         System.out.println ("lon: " + g.longitude);
//
//
//         System.out.println ("km sin: " + Math.sin(lat1));
//         System.out.println ("km sin: " + Math.sin(lat2));
//         System.out.println ("km cos: " + Math.cos(lat1));
//         System.out.println ("km cos: " + Math.cos(lat2));
//         System.out.println ("km cos: " + Math.cos(lon2-lon1));
//         System.out.println ("km R: " +  R_KM);
//
//         System.out.println ("km acos: " +  (Math.sin(lat1)*Math.sin(lat2) +
//                  Math.cos(lat1)*Math.cos(lat2) *
//                  Math.cos(lon2-lon1)) * R_KM);

         km  = 0.0;
      }

               
      return Math.abs(km);
   }

   /**
    * Calculate the distance from here to the specified location.
    * @param lla The location.
    * @return Distance to the location.
    */
   double distanceMI (LLALocation lla)
   {
      return (distanceKM (lla) * LLALocation.MILES_PER_KM);
   }
   
   /**
    * Calculate the distance from here to the specified location.
    * @param lla The location.
    * @return Distance to the location.
    */
   public double distanceFT (LLALocation lla)
   {
      return distanceMI (lla) * 5280;
   }
   
   public double distanceNSFT (LLALocation lla)
   {
      LLALocation pt = new LLALocation (lla.getLatitude (), getLongitude (), lla.getAltitude ());
      return distanceFT (pt);
   }
   
   public double distanceEWFT (LLALocation lla)
   {
      LLALocation pt = new LLALocation (getLatitude (), lla.getLongitude (), lla.getAltitude ());
      return distanceFT (pt);
   }


   /**
    * Move from here the given direction and distance.
    * @param bearing The direction to move. 0 is North, 90 is east.
    * @param km The distance to move.
    * @return The new location
    */
   public LLALocation moveKM (double bearing, double km)
   {
      double               lat1, lon1;
      double               lat2, lon2;
      double               radiusKM = LLALocation.R_KM + this.altitude / 1000.0;

      bearing              =  Math.toRadians (bearing);

      lat1                 =  Math.toRadians (this.latitude);
      lon1                 =  Math.toRadians (this.longitude);

      lat2                 =  Math.asin (Math.sin(lat1)*Math.cos(km/radiusKM) +
                              Math.cos(lat1)*Math.sin(km/radiusKM)*Math.cos(bearing));

      lon2                 =  lon1 + Math.atan2(Math.sin(bearing)*Math.sin(km/radiusKM)*Math.cos(lat1),
                                 Math.cos(km/radiusKM)-Math.sin(lat1)*Math.sin(lat2));

      return (new LLALocation (Math.toDegrees(lat2), Math.toDegrees(lon2), this.altitude));      
   }
   

   /**
    * Move from here the given direction and distance.
    * @param bearing The direction to move. 0 is North, 90 is east.
    * @param miles The distance to move.
    * @return The new location
    */

   public LLALocation moveMI (double bearing, double miles)
   {
      return moveKM (bearing, miles / LLALocation.MILES_PER_KM);
   }
   /**
    * Move from here the given direction and distance.
    * @param bearing The direction to move. 0 is North, 90 is east.
    * @param feet The distance to move.
    * @return The new location
    */
   
   public LLALocation moveFT (double bearing, double feet)
   {
      return moveMI (bearing, feet / 5280.0);
   }
   
   /**
    * Move some distance around a circle centered on 'center'
    * @param center The center of the circle we're moving around.
    * @param km Distance to move. Positive is clockwise, Negative is counter clockwise
    * @return The new location.
    */
   
   public LLALocation moveKM (LLALocation center, double km)
   {
      double radius = this.distanceKM (center);
      double circumference = radius * 2 * Math.PI;
      double degrees = 360.0 * km / circumference;
      double newBearing = center.getBearing (this) + degrees;
            
      return center.moveKM (newBearing, radius);
   }

   /**
    * Move some distance around a circle centered on 'center'
    * @param center The center of the circle we're moving around.
    * @param miles Distance to move. Positive is clockwise, Negative is counter clockwise
    * @return The new location.
    */
   public LLALocation moveMI (LLALocation center, double miles)
   {
      return moveKM (center, miles / LLALocation.MILES_PER_KM);
   }

   /**
    * Move some distance around a circle centered on 'center'
    * @param center The center of the circle we're moving around.
    * @param feet Distance to move. Positive is clockwise, Negative is counter clockwise
    * @return The new location.
    */
   public LLALocation moveFT (LLALocation center, double feet)
   {
      return moveMI (center, feet / 5280.0);
   }
   
   /**
    * Search the list of supplied points and find the one closest to here.
    * @param points List of points to search
    * @return Closest point
    */
   public LLALocation getClosest (ArrayList<LLALocation> points)
   {
      LLALocation result = null;
      if (! points.isEmpty ())
      {
         result = points.get (0);
         double distance = this.distanceKM (result);
         for (LLALocation l : points)
         {
            if (this.distanceKM (l) < distance)
            {
               result = l;
               distance = this.distanceKM (result);
            }
         }
      }
      
      return result;
   }
   
   /**
    * Search the list of supplied points and find the one furthest from here.
    * @param points List of points to search
    * @return Furthest point
    */
   public LLALocation getFurthest (ArrayList<LLALocation> points)
   {
      LLALocation result = null;
      if (! points.isEmpty ())
      {
         result = points.get (0);
         double distance = this.distanceKM (result);
         for (LLALocation l : points)
         {
            if (this.distanceKM (l) > distance)
            {
               result = l;
               distance = this.distanceKM (result);
            }
         }
      }
      
      return result;
   }

   /**
    * Get the latitude of this location
    * @return Latitude in degrees
    */
   public double getLatitude ()
   {
      return (latitude);
   }

   /**
    * Get the longitude of this location
    * @return Longitude in degrees
    */
   
   public double getLongitude ()
   {
      return (longitude);
   }   
   
   /**
    * Get the Altitude of this location.
    * @return Altitude in meters.
    */
   public double getAltitude ()
   {
      return altitude;
   }
   
   /**
    * Convert this location to a KML coordinate
    * @return This location as a KML coordinate.
    */
   public Coordinate toCoordinate ()
   {
      return new Coordinate (this.longitude, this.latitude);
   }   
   
   @Override
   public String toString ()
   {
      return String.format ("%.5f, %.5f", getLatitude (), getLongitude ());
   }
}
