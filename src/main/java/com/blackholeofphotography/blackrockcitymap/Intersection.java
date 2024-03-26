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

package com.blackholeofphotography.blackrockcitymap;

import com.blackholeofphotography.llalocation.LLALocation;

/**
 * Define the intersection of a an Annular street and a Radial street.
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class Intersection 
{
   final RadialStreet radial;
   final AnnularStreet annular;

   /**
    * Create an intersection
    * @param r Radial Street
    * @param a Annular Street
    */
   public Intersection (RadialStreet r, AnnularStreet a)
   {
      radial = r;
      annular = a;
   }
   
      /**
    * Create an intersection
    * @param r Radial Street
    * @param ch Annular Street
    */
   public Intersection (RadialStreet r, char ch)
   {
      radial = r;
      annular = new AnnularStreet (ch);
   }

   
   /**
    * Create an intersection at the specified corner.
    * @param hour Hour of the intersection Radial street
    * @param minute Minute of the intersection Radial street
    * @param StreetLetter Annular street letter
    */
   public Intersection (int hour, int minute, char StreetLetter)
   {
      radial = new RadialStreet (hour, minute);
      annular = new AnnularStreet (StreetLetter);
   }
   
   /**
    * Create an intersection at the specified corner
    * @param RadialString Radial street expressed as time e.g. 3:00
    * @param StreetLetter Letter of the Annular street
    */
   public Intersection (String RadialString, char StreetLetter)
   {
      radial = new RadialStreet (RadialString);
      annular = new AnnularStreet (StreetLetter);
   }

   /**
    * Get the LLALocation of the center of the intersection.
    * @param dataSet The Dataset to use for city dimensions. 
    * @return an LLALocation
    */
   public LLALocation corner (BurningData dataSet)
   {
      return corner (dataSet, IntersectionOffset.Center);
   }
   
   /**
    * Get the LLALocation of the IntersectionOffset of the intersection
    * @param dataSet Dataset to use for city dimensions. 
    * @param io Intersection offset.
    * @return an LLALocation
    */
   public LLALocation corner (BurningData dataSet, IntersectionOffset io)
   {
      LLALocation g;
      double bearing = dataSet.getBearing (radial);
      double halfWidth = dataSet.getRegularStreetWidth () /2 ;

 
      if (this.annular.isCenterCamp ())
      {
         double bearing530A = dataSet.getCenterCampLLA ().getBearing (new Intersection (5, 30, 'A').corner (dataSet));
         double bearing630A = dataSet.getCenterCampLLA ().getBearing (new Intersection (6, 30, 'A').corner (dataSet));

         g = dataSet.getCenterCampLLA ();
         if (this.annular.getStreetLetter () == AnnularStreet.RODS_ROAD)
            g = g.moveFT (bearing, dataSet.getCenterThemeCampOuterRadius () + halfWidth);
         else
         {
            // Center is special. 3:00 and 9:00 are offset to follow a line from 
            // the center of Center Camp and go to where A intersects Rod's Road.
            if (this.radial.equals (new RadialStreet (3, 0)))
               bearing = bearing530A;
            else if (this.radial.equals (new RadialStreet (9, 0)))
               bearing = bearing630A;
            g = g.moveFT (bearing, dataSet.getCenterThemeCampInnerRadius ());
         }
      }
      else if (this.annular.getStreetLetter () == AnnularStreet.TEMPLE)
      {
         g = dataSet.getTempleLLA ().moveFT (bearing, AnnularStreet.getRadius (dataSet, AnnularStreet.TEMPLE));
      }
      else
      {
         double ft = annular.getRadius (dataSet);
         g = dataSet.GS ().moveFT (bearing, ft);
      }


      switch (io)
      {
      case ClockwiseManside:
         g = g.moveFT (bearing, -halfWidth);          
         g = g.moveFT (bearing+90.0, halfWidth);
         break;

      case CounterClockwiseManside:
         g = g.moveFT (bearing, -halfWidth);     
         g = g.moveFT (bearing+90.0, -halfWidth);
         break;

      case ClockwiseOutside:
         g = g.moveFT (bearing, halfWidth);
         g = g.moveFT (bearing+90.0, halfWidth);
         break;

      case CounterClockwiseOutside:
         g = g.moveFT (bearing, halfWidth);
         g = g.moveFT (bearing+90.0, -halfWidth);
         break;
         
      case Manside:
         g = g.moveFT (bearing, -halfWidth);          
         break;
         
      case Outside:
         g = g.moveFT (bearing, halfWidth);          
         break;
         
      case Clockwise:
         g = g.moveFT (bearing+90.0, halfWidth);
         break;
         
      case CounterClockwise:
         g = g.moveFT (bearing+90.0, -halfWidth);
         break;
         
      case Center:
         break;         
      }
                 
      return g;
   }
   
   /**
    * Get the next intersection in the specified direction.
    * @param direction Direction relative to the man.
    * @return The next intersection
    */
   public Intersection getNextIntersection (ManDirection direction)
   {
      return new Intersection (this.radial.getNextStreet (direction), this.annular.getNextStreet (direction));
   }
   
   @Override
   public String toString ()
   {
      return this.radial.toString () + this.annular.getStreetLetter ();
   }
   
   public boolean equals (Intersection ii)
   {
      if (! this.radial.equals (ii.radial))
         return false;
      else if (!this.annular.equals (ii.annular))
         return false;
      
      return true;
   }
}
