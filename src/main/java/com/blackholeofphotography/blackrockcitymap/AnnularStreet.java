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

/**
 * Specify an Annular (letter) street.
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class AnnularStreet 
{
   /**
    * The street letter for this street.
    */
   private final char streetLetter;
   /**
    * Constant to represent The Esplanade 
    */
   public final static char ESPLANADE = 'Z';
   /**
    * Constant to represent Rod's Road
    */
   public final static char RODS_ROAD = 'R';
   /**
    * Constant to represent the Inner Circle (The road going around the center camp tent).
    */
   public final static char INNER_CIRCLE = 'S';
   
   public final static char TEMPLE = 'T';
   public final static char THE_MAN = 'W';

   /**
    * Create a new Annular street
    * @param StreetLetter The letter of the street
    */
   public AnnularStreet (char StreetLetter)
   {
      streetLetter = Character.toUpperCase (StreetLetter);
   }
   
   /**
    * Get the letter for this street
    * @return The street letter
    */
   public char getStreetLetter ()
   {
      return streetLetter;
   }

   
   /**
    * Get the letter of the next street going away from the man.
    * @return The next street letter
    */
   
   private char getNextStreetLetter ()
   {
      switch (this.streetLetter)
      {
      case AnnularStreet.ESPLANADE:
         return 'A';
      case AnnularStreet.RODS_ROAD:
         return AnnularStreet.RODS_ROAD;
      case AnnularStreet.INNER_CIRCLE:
         return AnnularStreet.RODS_ROAD;
      case AnnularStreet.TEMPLE:
         return AnnularStreet.TEMPLE;
      case AnnularStreet.THE_MAN:
         return AnnularStreet.THE_MAN;
      default:
         return (char) (streetLetter + 1);
      }
   }

   /**
    * Get the letter of the previous street going toward the man.
    * @return The previous street letter
    */
   
   private char getPrevStreetLetter ()
   {
      switch (this.streetLetter)
      {
      case AnnularStreet.ESPLANADE:
         return AnnularStreet.ESPLANADE;
      case AnnularStreet.RODS_ROAD:
         return AnnularStreet.INNER_CIRCLE;
      case AnnularStreet.INNER_CIRCLE:
         return AnnularStreet.INNER_CIRCLE;
      case AnnularStreet.TEMPLE:
         return AnnularStreet.TEMPLE;
      case AnnularStreet.THE_MAN:
         return AnnularStreet.THE_MAN;
      case 'A':
         return AnnularStreet.ESPLANADE;
      default:
         return (char) (streetLetter - 1);
      }
   }

   /**
    * Get the next street in the specified direction.
    * @param direction Direction relative to the man
    * @return The next street
    */
   public AnnularStreet getNextStreet (ManDirection direction)
   {
      switch (direction)
      {
         case TOWARD_MAN:
            return new AnnularStreet (getPrevStreetLetter ());
         case FROM_MAN:
            return new AnnularStreet (getNextStreetLetter ());
         default:
            return new AnnularStreet (streetLetter);
      }
   }
   
   /**
    * Determine if this is one of the streets going around center camp.
    * @return True if this is a street going around center camp
    */
   public boolean isCenterCamp ()
   {
      return this.streetLetter == AnnularStreet.INNER_CIRCLE ||
             this.streetLetter == AnnularStreet.RODS_ROAD;
   }
   
   /**
    * Determine if this street is the same as some other
    * @param a The street to compare against
    * @return True if these are the same street
    */
   public boolean equals (AnnularStreet a)
   {
      return this.getStreetLetter () == a.getStreetLetter ();
   }
   
   /**
    * Get the distance from the man to the center of the street.
    * @param d The data set for this year.
    * @return Distance in Feet
    */
   public double getRadius (BurningData d)
   {
      return d.getStreetRadiusFT (this.streetLetter);
   }
   
   public static double getRadius (BurningData d, char roadLetter)
   {
      return new AnnularStreet (roadLetter).getRadius (d);
   }
   
   /**
    * Get the distance from the man to the specified street edge.
    * @param d The data set for this year.
    * @param edge The street edge of interest  (center, manside, outside).
    * @return Distance in Feet
    */
   public double getRadius (BurningData d, AnnularOffset edge)
   {
      double radius = getRadius (d);
      if (edge == AnnularOffset.MANSIDE)
         radius -= d.getRegularStreetWidth () / 2;
      else if (edge == AnnularOffset.OUTSIDE)
         radius -= d.getRegularStreetWidth () / 2;
      
      return radius;
   }
   
   @Override
   public String toString ()
   {
      switch (this.streetLetter)
      {
      case AnnularStreet.ESPLANADE:
         return "Esplanade";
      case AnnularStreet.RODS_ROAD:
         return "Rod's Road";
      case AnnularStreet.INNER_CIRCLE:
         return "Inner Circle";
      case AnnularStreet.TEMPLE:
         return "Temple";
      case AnnularStreet.THE_MAN:
         return "The Man";
      default:
         return ""+this.streetLetter;
      }
   }
}


