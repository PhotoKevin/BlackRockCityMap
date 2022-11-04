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

import com.blackholeofphotography.blackrockcitymap.path.ArcDirection;
import com.blackholeofphotography.blackrockcitymap.path.Path;
import com.blackholeofphotography.llalocation.LLAGeometry;
import com.blackholeofphotography.llalocation.LLALocation;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Represent BlackRockCity
 * See the SVG files PlazaLayout and PlazaPortalLayout to understand how city
 * blocks are drawn.
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class BlackRockCity
{
   /**
    * This year's data set.
    */
   private BurningData d;   
   /**
    * Location of the Golden Spike.
    */
   private LLALocation GoldenSpike = null;
   
   public BlackRockCity (String datafile)
   {
      d = new BurningData (datafile);
      GoldenSpike = d.GS ();
   }

   private double PlazaPortalAngle (Intersection plazaIntersection)
   {
      double W = d.getPortalWidth ();
      
      return Degrees.atan (W / PlazaPortalLength());
   }

   
   // See PlazaPortalLayout.svg to understand the math
   
   private double PlazaPortalLength ()
   {
      double N = d.getRegularStreetWidth () / 2.0;
      double W = d.getPortalWidth ();
      double L = d.getBlockDepth ('A') + d.getBlockDepth (AnnularStreet.ESPLANADE) + d.getRegularStreetWidth ()- d.getPlazaRadius ();

      double X = (N * L) / (W - N);
      
      return L + X;
   }


   
   // See PlazaLayout.svg
   private Path drawCityBlock (String folderPath, Intersection earlyInside, Intersection lateInside, Intersection lateOutside, Intersection earlyOutside)
   {
      // These are 'normal' corner locations.
      LLALocation lateInsideCorner = lateInside.corner (d, IntersectionOffset.CounterClockwiseOutside);
      LLALocation earlyInsideCorner = earlyInside.corner (d, IntersectionOffset.ClockwiseOutside);
      LLALocation lateOutsideCorner = lateOutside.corner (d, IntersectionOffset.CounterClockwiseManside);
      LLALocation earlyOutsideCorner = earlyOutside.corner (d, IntersectionOffset.ClockwiseManside);

      double lateBearing = d.getBearing (lateInside.radial);
      double earlyBearing = d.getBearing (earlyInside.radial);
      double midBearing =  earlyBearing + (lateBearing - earlyBearing) /2;

      // First work out the starting point
      double streetEdge = lateInside.corner (d, IntersectionOffset.Outside).distanceFT (GoldenSpike);
      LLALocation startingPoint = GoldenSpike.moveFT (midBearing, streetEdge);
      LLALocation current = startingPoint;

      Path CityBlock = new Path (folderPath, Color.BLACK);
      
      // Move from the startingPoint (center inside) to lateInside.
      if (d.isPlaza (lateInside))
      {
         LLALocation p1; // Begining of the curve around the plaza
         LLALocation p2; // End of the curve around the plaza.

         // Lazy math. Not taking arcs into account
         p1 = lateInsideCorner.moveFT (lateBearing-90.0, d.getPlazaRadius ());
         p2 = lateInsideCorner.moveFT (lateBearing, d.getPlazaRadius ());

         // Two arcs. First the radial road from start to p1
         // Then the curve around the plaza to p2.
         CityBlock.addArcSegment (GoldenSpike, current, p1, ArcDirection.CLOCKWISE);
         CityBlock.addArcSegment (lateInside.corner(d), p1, p2, ArcDirection.CLOCKWISE);
      }
      else if (d.isPlazaPortal (lateInside)) // aka 3:00 or 9:00
      {
         LLALocation p1;
         // Lazy math. Not taking arcs into account
         p1 = lateInside.corner(d, IntersectionOffset.Outside).moveFT (lateBearing-90.0, d.getPortalWidth () );
//         p1 = moveAroundFT (GoldenSpike, lateInside.corner (d, IntersectionOffset.Outside), d.getPortalWidth (lateInside.radial.getHour ()));
         CityBlock.addArcSegment (GoldenSpike, current, p1, ArcDirection.CLOCKWISE);
      }
      else if (d.isPortal (lateInside) && earlyInside.annular.getStreetLetter () == AnnularStreet.ESPLANADE)
      {
         // Lazy math. Not taking arcs into account
         LLALocation realCorner = lateInsideCorner.moveFT (lateBearing-90, d.getPortalWidth ()/2);
         CityBlock.addArcSegment (GoldenSpike, startingPoint, realCorner, ArcDirection.CLOCKWISE);
      }
      else
      {
         LLALocation realCorner = lateInsideCorner;
         if (d.isPlaza (lateOutside) && earlyInside.annular.getStreetLetter () == 'A')
         {
            LLALocation g;
            double bearing = lateBearing;
            
            double espBlockFT = d.getBlockDepth (AnnularStreet.ESPLANADE) + d.getRegularStreetWidth ();
            double AblockFT = this.PlazaPortalLength () - espBlockFT;
            double pw = Degrees.tan (PlazaPortalAngle (lateInside)) * AblockFT;

            g = lateInside.corner (d, IntersectionOffset.Outside);

            g = g.moveFT (bearing-90.0, pw);
            realCorner = g;
         }
         
         CityBlock.addArcSegment (GoldenSpike, startingPoint, realCorner, ArcDirection.CLOCKWISE);
      }
      // End of center inside to lateinside
      


      // Move from the lateInside corner to the lateOutside
      if (d.isPlaza (lateOutside))
      {
         LLALocation p1; // Begining of the curve around the plaza
         LLALocation p2; // End of the curve around the plaza.
         
         // A straight line goes from the last point to p1.
         // This is implicit by adding the arc around the plaza.

         // Lazy math. Not taking arcs into account
         p1 = lateOutsideCorner.moveFT (lateBearing+180, d.getPlazaRadius ());
         p2 = lateOutsideCorner.moveFT (lateBearing-90.0, d.getPlazaRadius ());

         CityBlock.addArcSegment (lateOutside.corner(d), p1, p2, ArcDirection.CLOCKWISE);
         current = p2;
      }
      
      else if (d.isPlazaPortal (lateInside)) // Yes, lateInside. Need to deal with the odd corners at 3:00A and 9:00A
      {
         LLALocation g;

         double espBlockFT = d.getBlockDepth (AnnularStreet.ESPLANADE);
         double AblockFT = this.PlazaPortalLength () - espBlockFT;

         double portalWidth = Degrees.tan (PlazaPortalAngle (lateInside)) * AblockFT;

         g = lateOutside.corner(d, IntersectionOffset.Manside);
         g = g.moveFT (GoldenSpike, -portalWidth); // Negative to move counter clockwise

         CityBlock.addPoint (g);

         current = g;
      }
      else
      {
         CityBlock.addPoint (lateOutsideCorner);
         current = lateOutsideCorner;
      }
      // End of lateInside to lateOutside


      // Move from lateOutside to earlyOutside
      if (d.isPlaza (earlyOutside))
      {
         LLALocation p1; // Begining of the curve around the plaza
         LLALocation p2; // End of the curve around the plaza.

         // Lazy math. Not taking arcs into account
         p1 = earlyOutsideCorner.moveFT (earlyBearing+90.0, d.getPlazaRadius ());
         p2 = earlyOutsideCorner.moveFT (earlyBearing+180, d.getPlazaRadius ());

         CityBlock.addArcSegment (GoldenSpike, current, p1, ArcDirection.COUNTER_CLOCKWISE);
         CityBlock.addArcSegment (earlyOutside.corner(d), p1, p2, ArcDirection.CLOCKWISE);
      }
      else if (d.isPlazaPortal (earlyInside)) // Yes, earlyInside. Need to deal with the odd corners at 3:00A and 9:00A
      {
         LLALocation g;

         // Lazy math. Not taking arcs into account
         double espBlockFT = d.getBlockDepth (AnnularStreet.ESPLANADE);
         double AblockFT = this.PlazaPortalLength () - espBlockFT;
         double PortalWidth = Degrees.tan (PlazaPortalAngle (earlyInside)) * AblockFT;

         g = earlyOutside.corner(d, IntersectionOffset.Manside);
         g = g.moveFT (GoldenSpike, PortalWidth);

         CityBlock.addArcSegment (GoldenSpike, current, g, ArcDirection.COUNTER_CLOCKWISE);
      }
      else
      {
         CityBlock.addArcSegment (GoldenSpike, current, earlyOutsideCorner, ArcDirection.COUNTER_CLOCKWISE);
      }
      // End of lateOutside to earlyOutside

      // Move from earlyOutside to earlyInside. 
      if (d.isPlaza (earlyInside))
      {
         LLALocation p1; // Begining of the curve around the plaza
         LLALocation p2; // End of the curve around the plaza.

         p1 = earlyInsideCorner.moveFT (earlyBearing, d.getPlazaRadius ());
         p2 = earlyInsideCorner.moveFT (earlyBearing+90.0, d.getPlazaRadius ());
         
         // A straight line goes from the last point to p1.
         // This is implicit by adding the arc around the plaza.
         
         // Then the curve around the plaza to p2.
         CityBlock.addArcSegment (earlyInside.corner(d), p1, p2, ArcDirection.CLOCKWISE);
         current = p2;
      }
      else if (d.isPlazaPortal (earlyInside))
      {
         LLALocation p2; 
         
         p2 = earlyInside.corner(d).moveFT (earlyBearing+90.0, d.getPortalWidth ());
         p2 = p2.moveFT (earlyBearing, d.getRegularStreetWidth ()/2);
         
         CityBlock.addPoint (p2);
         
         current = p2;
      }
      else if (d.isPortal (earlyInside) && earlyInside.annular.getStreetLetter () == AnnularStreet.ESPLANADE)
      {
         LLALocation realCorner = earlyInsideCorner.moveFT (earlyBearing + 90, d.getPortalWidth ()/2);
         //points.addAll (genArc (GoldenSpike, startingPoint, realCorner, ArcDirection.CLOCKWISE));
         CityBlock.addPoint (realCorner);
         current = realCorner;
      }
      else
      {
         LLALocation realCorner = earlyInsideCorner;
         if (d.isPlaza (earlyOutside) && earlyInside.annular.getStreetLetter () == 'A')
         {
            LLALocation g;
            
            double espBlockFT = d.getBlockDepth (AnnularStreet.ESPLANADE) + d.getRegularStreetWidth ();
            double AblockFT = this.PlazaPortalLength () - espBlockFT;
            double pw = Degrees.tan (PlazaPortalAngle (earlyInside)) * AblockFT;

            g = earlyInside.corner(d, IntersectionOffset.Outside);

            g = g.moveFT (earlyBearing+90.0, pw);
            realCorner = g;
         }

         CityBlock.addPoint (realCorner);
         current = realCorner;
      }
      // End of earlyOutside to earlyInside
      
      CityBlock.addArcSegment (GoldenSpike, current, startingPoint, ArcDirection.CLOCKWISE);
      CityBlock.closePath ();
      return CityBlock;
   }
   
   private LLALocation KeyHolePoint (LLALocation centerPoint, double keyholeBearing, double radiusFT, double keyholeWidthFT)
   {
      double opposite = keyholeWidthFT / 2;
      double hypot = radiusFT;
      double halfAngle = Degrees.asin (opposite / hypot);
      double newBearing = keyholeBearing - halfAngle;
      return centerPoint.moveFT (newBearing, hypot);
   }
   
   // Draw one of the four blocks inside of Rod's Road.
   // See CenterCamp.svg 
   
   private Path drawCentralBlock (String folderPath, LLALocation earlyInside, LLALocation lateInside, LLALocation lateOutside, LLALocation earlyOutside)
   {
      Path p = new Path (folderPath, Color.BLACK);
      p.addArcSegment (d.getCenterCampLLA (), earlyInside, lateInside, ArcDirection.CLOCKWISE);
      p.addLineSegment (lateInside, lateOutside);    // Implicit line
      p.addArcSegment (d.getCenterCampLLA (), lateOutside, earlyOutside, ArcDirection.COUNTER_CLOCKWISE);
      p.addLineSegment (earlyOutside, earlyInside);
      // Back to start
      //p.addPoint (earlyInside);

      p.closePath ();
      return p;
   }
   
   /**
    * Create a Path for the perimeter fence.
    * @return Path of the perimeter fence
    */
   public Path Perimeter ()
   {
      LLALocation P1 = d.P1 ();
      
      double radius = GoldenSpike.distanceFT (P1);
      double p1Bearing = GoldenSpike.getBearing (P1);
      Path ppp = new Path ("Perimeter");
      for (int i=0; i<6; i++)
         ppp.addPoint (GoldenSpike.moveFT (72*i+p1Bearing, radius));
      
      return ppp;
   }
   
   
   /**
    * Draw either of the two inner (Manside) corner blocks next to Rod's Road. 
    * @param folderPath Name of the path.
    * @param early True to draw the one on the early side. False for the late side.
    * @return A path around the block
    */
   private Path drawInnerCentralCornerBlock (String folderPath, boolean early)
   {
      Path CityBlock = new Path (folderPath, Color.BLACK);
      
      double radiusRROutside = d.getCenterThemeCampOuterRadius () + d.getRegularStreetWidth (); // Radius of the ouside edge of Rod's Road
      if (early)
      {
         LLALocation p530B = new Intersection (5,30,'B').corner (d, IntersectionOffset.ClockwiseOutside);
         LLALocation p530C = new Intersection (5,30,'C').corner (d, IntersectionOffset.ClockwiseManside);

         double radiusC = p530C.distanceFT (GoldenSpike);
         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (GoldenSpike, radiusC, d.getCenterCampLLA (), radiusRROutside);
         ArrayList<LLALocation> RodsRoad530Intersection = LLAGeometry.Intersection (d.getCenterCampLLA (), radiusRROutside, p530B, p530C);

         LLALocation p1 = p530C.getClosest (RodsRoadCIntersection);
         LLALocation p3 = p530B.getClosest (RodsRoad530Intersection);         

         CityBlock.addArcSegment (GoldenSpike, p1, p530C, ArcDirection.COUNTER_CLOCKWISE);
         CityBlock.addLineSegment (p530C, p3);

         CityBlock.addArcSegment (d.getCenterCampLLA (), p3, p1, ArcDirection.CLOCKWISE);
      }
      else
      {
         LLALocation p630B = new Intersection (6,30,'B').corner (d, IntersectionOffset.CounterClockwiseOutside);
         LLALocation p630C = new Intersection (6,30,'C').corner (d, IntersectionOffset.CounterClockwiseManside);

         double radiusC = p630C.distanceFT (GoldenSpike);
         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (GoldenSpike, radiusC, d.getCenterCampLLA (), radiusRROutside);
         ArrayList<LLALocation> RodsRoad630Intersection = LLAGeometry.Intersection (d.getCenterCampLLA (), radiusRROutside, p630B, p630C);
        
         LLALocation p1 = p630C.getClosest (RodsRoadCIntersection);
         LLALocation p3 = p630B.getClosest (RodsRoad630Intersection);         

         CityBlock.addArcSegment (GoldenSpike, p1, p630C, ArcDirection.CLOCKWISE);
         CityBlock.addLineSegment (p630C, p3);

         CityBlock.addArcSegment (d.getCenterCampLLA (), p3, p1, ArcDirection.COUNTER_CLOCKWISE);
      }

      CityBlock.closePath ();
      return CityBlock;
   }

   
   /**
    * Draw either of the two outer (away from the man) corner blocks next to Rod's Road. 
    * @param folderPath Name of the path.
    * @param early True to draw the one on the early side. False for the late side.
    * @return A path around the block
    */
   
   private Path drawOuterCentralCornerBlock (String folderPath, boolean early)
   {
      Path CityBlock = new Path (folderPath, Color.BLACK);

      double radiusRROutside = d.getCenterThemeCampOuterRadius () + d.getRegularStreetWidth (); // Radius of the ouside edge of Rod's Road

      if (early)
      {
         LLALocation p530D = new Intersection (5,30,'D').corner (d, IntersectionOffset.ClockwiseManside);
         LLALocation p530C = new Intersection (5,30,'C').corner (d, IntersectionOffset.ClockwiseOutside);

         double radiusC = p530C.distanceFT (GoldenSpike);
         double radiusD = p530D.distanceFT (GoldenSpike);
         
         ArrayList<LLALocation> RodsRoadDIntersection = LLAGeometry.Intersection (GoldenSpike, radiusD, d.getCenterCampLLA (), radiusRROutside);
         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (GoldenSpike, radiusC, d.getCenterCampLLA (), radiusRROutside);

         LLALocation p1 = p530D.getClosest (RodsRoadDIntersection);
         LLALocation p4 = p530C.getClosest (RodsRoadCIntersection);         

         CityBlock.addArcSegment (GoldenSpike, p1, p530D, ArcDirection.COUNTER_CLOCKWISE);
         CityBlock.addLineSegment (p530D, p530C);
         CityBlock.addArcSegment (GoldenSpike, p530C, p4, ArcDirection.CLOCKWISE);
         CityBlock.addArcSegment (d.getCenterCampLLA (), p4, p1, ArcDirection.CLOCKWISE);
      }
      else
      {
         LLALocation p630C = new Intersection (6,30,'C').corner (d, IntersectionOffset.CounterClockwiseOutside);
         LLALocation p630D = new Intersection (6,30,'D').corner (d, IntersectionOffset.CounterClockwiseManside);

         double radiusC = p630C.distanceFT (GoldenSpike);
         double radiusD = p630D.distanceFT (GoldenSpike);
         
         ArrayList<LLALocation> RodsRoadDIntersection = LLAGeometry.Intersection (GoldenSpike, radiusD, d.getCenterCampLLA (), radiusRROutside);
         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (GoldenSpike, radiusC, d.getCenterCampLLA (), radiusRROutside);

         LLALocation p1 = p630D.getClosest (RodsRoadDIntersection);
         LLALocation p4 = p630C.getClosest (RodsRoadCIntersection);         

         CityBlock.addArcSegment (GoldenSpike, p1, p630D, ArcDirection.CLOCKWISE);
         CityBlock.addLineSegment (p630D, p630C);
         CityBlock.addArcSegment (GoldenSpike, p630C, p4, ArcDirection.COUNTER_CLOCKWISE);
         CityBlock.addArcSegment (d.getCenterCampLLA (), p4, p1, ArcDirection.COUNTER_CLOCKWISE);
      }
      
      CityBlock.closePath ();
      return CityBlock;
   }
   
   private Path drawOuterPlaya (String folderPath)
   {
      Path OuterPlaya = new Path (folderPath, Color.BLACK);
      
      double radiusRROutside = d.getCenterThemeCampOuterRadius () + d.getRegularStreetWidth (); // Radius of the ouside edge of Rod's Road

      LLALocation p300Esp = new Intersection (3,00, AnnularStreet.ESPLANADE).corner (d, IntersectionOffset.CounterClockwiseManside);
      LLALocation p900Esp = new Intersection (9,00, AnnularStreet.ESPLANADE).corner (d, IntersectionOffset.ClockwiseManside);

      LLALocation p300Man = new Intersection (3,00, AnnularStreet.THE_MAN).corner (d, IntersectionOffset.CounterClockwise);
      LLALocation p1200ManA = new Intersection (12,00, AnnularStreet.THE_MAN).corner (d, IntersectionOffset.Clockwise);
      LLALocation p1200ManB = new Intersection (12,00, AnnularStreet.THE_MAN).corner (d, IntersectionOffset.CounterClockwise);

      LLALocation p600TempleB = new Intersection (6,00, AnnularStreet.TEMPLE).corner (d, IntersectionOffset.Clockwise);
      LLALocation p600TempleA = new Intersection (6,00, AnnularStreet.TEMPLE).corner (d, IntersectionOffset.CounterClockwise);

      
      LLALocation p900Man = new Intersection (9,00, AnnularStreet.THE_MAN).corner (d, IntersectionOffset.Clockwise);

      LLALocation p1000L = new Intersection ("10:00", d.maxRoadLetter ()).corner (d, IntersectionOffset.ClockwiseOutside);
      LLALocation p200L = new Intersection ("2:00", d.maxRoadLetter ()).corner (d, IntersectionOffset.CounterClockwiseOutside);


      LLALocation a = new Intersection ("2:00", AnnularStreet.ESPLANADE).corner (d, IntersectionOffset.CounterClockwiseManside);
      LLALocation b = new Intersection ("10:00", AnnularStreet.ESPLANADE).corner (d, IntersectionOffset.ClockwiseManside);

      OuterPlaya.addArcSegment (GoldenSpike, a, p300Esp, ArcDirection.CLOCKWISE);
      OuterPlaya.addArcSegment (GoldenSpike, p300Man, p1200ManA, ArcDirection.COUNTER_CLOCKWISE);
      OuterPlaya.addPoint (p600TempleA);
      OuterPlaya.addArcSegment (d.getTempleLLA (), p600TempleA, p600TempleB, ArcDirection.COUNTER_CLOCKWISE);
      OuterPlaya.addPoint (p600TempleB);
      OuterPlaya.addArcSegment (GoldenSpike, p1200ManB, p900Man, ArcDirection.COUNTER_CLOCKWISE);
      OuterPlaya.addArcSegment (GoldenSpike, p900Esp, b, ArcDirection.CLOCKWISE);
      OuterPlaya.addArcSegment (GoldenSpike, p1000L, p200L, ArcDirection.COUNTER_CLOCKWISE);

      return OuterPlaya;
   }
   
      
   private Path drawInnerPlaya (String folderPath, boolean early)
   {
      Path InnerPlaya = new Path (folderPath, Color.BLACK);
      
      double radiusRROutside = d.getCenterThemeCampOuterRadius () + d.getRegularStreetWidth (); // Radius of the ouside edge of Rod's Road
      if (early)
      {
         LLALocation p300Esp = new Intersection (3,00, AnnularStreet.ESPLANADE).corner (d, IntersectionOffset.ClockwiseManside);
         LLALocation p300Man = new Intersection (3,00, AnnularStreet.THE_MAN).corner (d, IntersectionOffset.Clockwise);
         LLALocation p600Man = new Intersection (6,00, AnnularStreet.THE_MAN).corner (d, IntersectionOffset.CounterClockwise);
         LLALocation p1200RR = new Intersection (12,0, AnnularStreet.RODS_ROAD).corner (d, IntersectionOffset.ClockwiseOutside);

         double radiusESP = d.getEsplanadeRadius () - d.getRegularStreetWidth ()/2;

         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (GoldenSpike, radiusESP, d.getCenterCampLLA (), radiusRROutside);
         
         LLALocation p1 = p300Esp.getClosest (RodsRoadCIntersection);

         InnerPlaya.addArcSegment (GoldenSpike, p300Esp, p1, ArcDirection.CLOCKWISE);
         InnerPlaya.addArcSegment (d.getCenterCampLLA (), p1, p1200RR, ArcDirection.COUNTER_CLOCKWISE);
         InnerPlaya.addArcSegment (GoldenSpike, p600Man, p300Man, ArcDirection.COUNTER_CLOCKWISE);
      }
      else
      {
         LLALocation p600Man = new Intersection (6,00, AnnularStreet.THE_MAN).corner (d, IntersectionOffset.Clockwise);
         LLALocation p900Man = new Intersection (9,00, AnnularStreet.THE_MAN).corner (d, IntersectionOffset.CounterClockwise);
         LLALocation p900Esp = new Intersection (9,00, AnnularStreet.ESPLANADE).corner (d, IntersectionOffset.CounterClockwiseManside);
         LLALocation p1200RR = new Intersection (12,0, AnnularStreet.RODS_ROAD).corner (d, IntersectionOffset.CounterClockwiseOutside);

         double radiusESP = d.getEsplanadeRadius () - d.getRegularStreetWidth ()/2;

         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (GoldenSpike, radiusESP, d.getCenterCampLLA (), radiusRROutside);

         LLALocation p2 = p900Esp.getClosest (RodsRoadCIntersection);

         InnerPlaya.addArcSegment (GoldenSpike, p900Esp, p2, ArcDirection.COUNTER_CLOCKWISE);

         InnerPlaya.addArcSegment (d.getCenterCampLLA (), p2, p1200RR, ArcDirection.CLOCKWISE);
         InnerPlaya.addArcSegment (GoldenSpike, p600Man, p900Man, ArcDirection.CLOCKWISE);
      }
      
      return InnerPlaya;
   }
   
   public ArrayList<Path> drawCity ()
   {
      ArrayList<Path> drawing = new ArrayList<> ();
      try
      {
         // https://innovate.burningman.org/dataset/2017-golden-spike-and-general-city-map-data/

         drawing.add (Perimeter ());

         // Do the ESPLANADE blocks.
         for (int hour=2; hour<10; hour++)
         {
            for (int qhour=0; qhour <4; qhour++)
            {
               Intersection startCorner = new Intersection (new RadialStreet (hour, qhour*15), new AnnularStreet (AnnularStreet.ESPLANADE));
               if (d.existsClockwiseRoad (startCorner) && d.existsOutsideRoad (startCorner))
               {
                  ArrayList<Intersection> corners = d.getBlockCorners (startCorner);

                  Path CityBlock = drawCityBlock ("CityBlock", corners.get (0), corners.get (1), corners.get (2), corners.get (3));
                  drawing.add (CityBlock);
               }
            } 
         }

         for (char ch='A'; ch<=d.maxRoadLetter (); ch++)
         {
            for (int hour=2; hour<10; hour++)
            {
               for (int qhour=0; qhour <4; qhour++)
               {
                  Intersection startCorner = new Intersection (new RadialStreet (hour, qhour*15), new AnnularStreet (ch));
                  if (d.existsClockwiseRoad (startCorner) && d.existsOutsideRoad (startCorner))
                  {
                     ArrayList<Intersection> corners = d.getBlockCorners (startCorner);

                     Path CityBlock = drawCityBlock ("CityBlock", corners.get (0), corners.get (1), corners.get (2), corners.get (3));
                     drawing.add (CityBlock);
                     
                     //this.addPathToKML (CityBlock, 5.0);
                  }
               }
            }
         }
       

         // Get a bearing that points from the center of Center Camp to the center of 5:30A.
         // Pretend that's a portal and point the 3:00 road at it.
         // Ditto for the 9:00 road from Center Camp
         double bearing530A = d.getCenterCampLLA ().getBearing (new Intersection (5, 30, 'A').corner (d));
         double bearing630A = d.getCenterCampLLA ().getBearing (new Intersection (6, 30, 'A').corner (d));

         Path p = this.drawCentralBlock ("CenterCampEarlyManside", 
                 this.KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampInnerRadius (), -d.getCenterCampKeyholeNarrowest ()),
                 new Intersection (3, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.CounterClockwise), 
                 this.KeyHolePoint (d.getCenterCampLLA (), bearing530A, d.getCenterThemeCampOuterRadius (), d.getRegularStreetWidth ()),
                 this.KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampOuterRadius (), -d.getCenterCampKeyholeWidest ()));
         drawing.add (p);
         
         p = this.drawCentralBlock ("CenterCampEarlyOutside", 
                 new Intersection (3, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.Clockwise), 
                 new Intersection (6, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.CounterClockwise), 
                 new Intersection (6, 0, AnnularStreet.RODS_ROAD).corner (d, IntersectionOffset.CounterClockwiseManside),
                 this.KeyHolePoint (d.getCenterCampLLA (), bearing530A, d.getCenterThemeCampOuterRadius (), -d.getRegularStreetWidth ()));
         drawing.add (p);


         p = this.drawCentralBlock ("CenterCampLateOutside", 
                 new Intersection (6, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.Clockwise), 
                 new Intersection (9, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.CounterClockwise), 
                 this.KeyHolePoint (d.getCenterCampLLA (), bearing630A, d.getCenterThemeCampOuterRadius (), d.getRegularStreetWidth ()),
                 new Intersection (6, 0, AnnularStreet.RODS_ROAD).corner (d, IntersectionOffset.ClockwiseManside)); 
         drawing.add (p);

         p = this.drawCentralBlock ("CenterCampLateManside", 
                 new Intersection (9, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.Clockwise), 
                 this.KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampInnerRadius (), d.getCenterCampKeyholeNarrowest ()),
                 this.KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampOuterRadius (), d.getCenterCampKeyholeWidest ()),
                 this.KeyHolePoint (d.getCenterCampLLA (), bearing630A, d.getCenterThemeCampOuterRadius (), -d.getRegularStreetWidth ()));
         drawing.add (p);

         p = drawOuterCentralCornerBlock ("WedgeBlock", true);
         drawing.add (p);
         p = drawOuterCentralCornerBlock ("WedgeBlock", false);
         drawing.add (p);

         drawing.add (this.drawInnerCentralCornerBlock ("WedgeBlock", true));
         drawing.add (this.drawInnerCentralCornerBlock ("WedgeBlock", false));

         drawing.add (this.drawOuterPlaya ("OuterPlaya"));
         drawing.add (this.drawInnerPlaya ("OnnerPlaya", true));
         drawing.add (this.drawInnerPlaya ("OnnerPlaya", false));
      }
      catch (IOException | NumberFormatException e)
      {
         System.out.println (e.toString ());
      }
      catch (Exception e)
      {
         System.out.println (e.toString ());
      }
         
      return drawing;
   }
   
   Path drawRR ()
   {
      Path p = new Path ("RR", Color.RED);
      LLALocation p1200RR = new Intersection (12,00, AnnularStreet.TEMPLE).corner (d, IntersectionOffset.ClockwiseManside);
      LLALocation p1200RRa = new Intersection (12,00, AnnularStreet.TEMPLE).corner (d, IntersectionOffset.CounterClockwiseManside);

      p.addArcSegment (d.getTempleLLA (), p1200RR, p1200RRa, ArcDirection.CLOCKWISE);
      p.addPoint (d.getTempleLLA ());
      return p;
   }
   
}
