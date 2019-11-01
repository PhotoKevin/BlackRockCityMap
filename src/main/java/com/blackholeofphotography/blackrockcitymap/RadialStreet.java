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
 * Specify a Radial street.
 * Radial streets go in 15 minute increments, but not all streets exist at
 * all distances from the man. 
 * 
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class RadialStreet 
{
   /**
    * The street in quarter hours. i.e. 5 for 2:15. This is the 
    * internal representation.
    */
   private final int quarterHours;

   /**
    * Create a new Radial street from the supplied getHour/minute.
    * @param hour Radial street getHour
    * @param minute Radial street minute
    */
   public RadialStreet (int hour, int minute)
   {
      quarterHours = hour * 4 + minute / 15;
   }
   
   /**
    * Create a new Radial street from the supplied getMinutes.
    * @param minutes Radial street getMinutes. i.e. 120 is 2:00
    */
   public RadialStreet (int minutes)
   {
      quarterHours = minutes / 15;
   }
   
   /**
    * Create a new Radial street from the supplied time string
    * @param timeStr A time expressed as a string such as "2:30"
    */
   public RadialStreet (String timeStr)
   {
      String[] split = timeStr.split (":");
      int _hour = Integer.parseInt (split[0]);
      int _minute = Integer.parseInt (split[1]);
      quarterHours = _hour * 4 + _minute / 15;
   }
   
   /**
    * Get the hour portion of the Radial street. i.e. return 2 for "2:30"
    * @return The hour
    */
   public double getHour ()
   {
      return quarterHours / 4.0;
   }
   
   /**
    * Get the Radial street in minutes. i.e. return 150 for "2:30"
    * @return The street as minutes.
    */
   public int getMinutes ()
   {
      return quarterHours * 15;
   }

   /**
    * Get the next street in the specified direction.
    * @param direction Direction relative to the man
    * @return The next street
    */
   public RadialStreet getNextStreet (ManDirection direction)
   {
      switch (direction)
      {
         case CLOCKWISE:
            return new RadialStreet (this.getMinutes () + 15);
         case COUNTER_CLOCKWISE:
            return new RadialStreet (this.getMinutes () - 15);
         default:
            return new RadialStreet (this.getMinutes ());
      }
   }
   
   @Override
   public String toString ()
   {
      int _hour = this.quarterHours / 4;
      int _minute = this.quarterHours % 4;
      return String.format ("%d:%02d", _hour, _minute*15);
   }
   
   public boolean equals (RadialStreet r)
   {
      if (this.getMinutes() == r.getMinutes ())
         return true;
      
      return false;
   }
}
