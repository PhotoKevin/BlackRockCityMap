/*
 * Copyright (c) 2025, Kevin Nickerson
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

import static com.blackholeofphotography.blackrockcitymap.BlackRockCity.KeyHolePoint;
import com.blackholeofphotography.blackrockcitymap.path.ArcDirection;
import com.blackholeofphotography.blackrockcitymap.path.Path;
import com.blackholeofphotography.llalocation.LLAGeometry;
import com.blackholeofphotography.llalocation.LLALocation;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Kevin Nickerson
 */
public class CenterCamp2023
{
   private final BurningData d;
   public CenterCamp2023 (BurningData dataSet)
   {
      d = dataSet;
   }
   
   public ArrayList<Path> drawCentralCity ()
   {
      ArrayList<Path> drawing = new ArrayList<> ();
      // Get a bearing that points to the center of 5:30A.
      // Pretend that's a portal and point the 3:00 road at it.
      // Ditto for the 9:00 road from Center Camp
      double bearing530A = d.getCenterCampLLA ().getBearing (new Intersection (5, 30, 'A').corner (d));
      double bearing630A = d.getCenterCampLLA ().getBearing (new Intersection (6, 30, 'A').corner (d));

      Path p = this.drawCentralBlock ("CenterCamp", 
              KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampInnerRadius (), -d.getCenterCampKeyholeNarrowest ()),
              new Intersection (3, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.CounterClockwise), 
              KeyHolePoint (d.getCenterCampLLA (), bearing530A, d.getCenterThemeCampOuterRadius (), d.getRegularStreetWidth ()),
              KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampOuterRadius (), -d.getCenterCampKeyholeWidest ()));
      drawing.add (p);

      p = this.drawCentralBlock ("CenterCamp", 
              new Intersection (3, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.Clockwise), 
              new Intersection (6, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.CounterClockwise), 
              new Intersection (6, 0, AnnularStreet.RODS_ROAD).corner (d, IntersectionOffset.CounterClockwiseManside),
              KeyHolePoint (d.getCenterCampLLA (), bearing530A, d.getCenterThemeCampOuterRadius (), -d.getRegularStreetWidth ()));
      drawing.add (p);


      p = this.drawCentralBlock ("CenterCamp", 
              new Intersection (6, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.Clockwise), 
              new Intersection (9, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.CounterClockwise), 
              KeyHolePoint (d.getCenterCampLLA (), bearing630A, d.getCenterThemeCampOuterRadius (), d.getRegularStreetWidth ()),
              new Intersection (6, 0, AnnularStreet.RODS_ROAD).corner (d, IntersectionOffset.ClockwiseManside)); 
      drawing.add (p);

      p = this.drawCentralBlock ("CenterCamp", 
              new Intersection (9, 0, AnnularStreet.INNER_CIRCLE).corner (d, IntersectionOffset.Clockwise), 
              KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampInnerRadius (), d.getCenterCampKeyholeNarrowest ()),
              KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampOuterRadius (), d.getCenterCampKeyholeWidest ()),
              KeyHolePoint (d.getCenterCampLLA (), bearing630A, d.getCenterThemeCampOuterRadius (), -d.getRegularStreetWidth ()));
      drawing.add (p);

      p = drawOuterCentralCornerBlock ("WedgeBlock", true);
      drawing.add (p);
      p = drawOuterCentralCornerBlock ("WedgeBlock", false);
      drawing.add (p);

      drawing.add (this.drawInnerCentralCornerBlock ("WedgeBlock", true));
      drawing.add (this.drawInnerCentralCornerBlock ("WedgeBlock", false));
      
      return drawing;
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

         double radiusC = p530C.distanceFT (d.GS ());
         double radiusD = p530D.distanceFT (d.GS ());
         
         ArrayList<LLALocation> RodsRoadDIntersection = LLAGeometry.Intersection (d.GS (), radiusD, d.getCenterCampLLA (), radiusRROutside);
         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (d.GS (), radiusC, d.getCenterCampLLA (), radiusRROutside);

         LLALocation p1 = p530D.getClosest (RodsRoadDIntersection);
         LLALocation p4 = p530C.getClosest (RodsRoadCIntersection);         

         CityBlock.addArcSegment (d.GS (), p1, p530D, ArcDirection.COUNTER_CLOCKWISE);
         CityBlock.addLineSegment (p530D, p530C);
         CityBlock.addArcSegment (d.GS (), p530C, p4, ArcDirection.CLOCKWISE);
         CityBlock.addArcSegment (d.getCenterCampLLA (), p4, p1, ArcDirection.CLOCKWISE);
      }
      else
      {
         LLALocation p630C = new Intersection (6,30,'C').corner (d, IntersectionOffset.CounterClockwiseOutside);
         LLALocation p630D = new Intersection (6,30,'D').corner (d, IntersectionOffset.CounterClockwiseManside);

         double radiusC = p630C.distanceFT (d.GS ());
         double radiusD = p630D.distanceFT (d.GS ());
         
         ArrayList<LLALocation> RodsRoadDIntersection = LLAGeometry.Intersection (d.GS (), radiusD, d.getCenterCampLLA (), radiusRROutside);
         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (d.GS (), radiusC, d.getCenterCampLLA (), radiusRROutside);

         LLALocation p1 = p630D.getClosest (RodsRoadDIntersection);
         LLALocation p4 = p630C.getClosest (RodsRoadCIntersection);         

         CityBlock.addArcSegment (d.GS (), p1, p630D, ArcDirection.CLOCKWISE);
         CityBlock.addLineSegment (p630D, p630C);
         CityBlock.addArcSegment (d.GS (), p630C, p4, ArcDirection.COUNTER_CLOCKWISE);
         CityBlock.addArcSegment (d.getCenterCampLLA (), p4, p1, ArcDirection.COUNTER_CLOCKWISE);
      }
      
      CityBlock.closePath ();
      return CityBlock;
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

         double radiusC = p530C.distanceFT (d.GS ());
         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (d.GS (), radiusC, d.getCenterCampLLA (), radiusRROutside);
         ArrayList<LLALocation> RodsRoad530Intersection = LLAGeometry.Intersection (d.getCenterCampLLA (), radiusRROutside, p530B, p530C);

         LLALocation p1 = p530C.getClosest (RodsRoadCIntersection);
         LLALocation p3 = p530B.getClosest (RodsRoad530Intersection);         

         CityBlock.addArcSegment (d.GS (), p1, p530C, ArcDirection.COUNTER_CLOCKWISE);
         CityBlock.addLineSegment (p530C, p3);

         CityBlock.addArcSegment (d.getCenterCampLLA (), p3, p1, ArcDirection.CLOCKWISE);
      }
      else
      {
         LLALocation p630B = new Intersection (6,30,'B').corner (d, IntersectionOffset.CounterClockwiseOutside);
         LLALocation p630C = new Intersection (6,30,'C').corner (d, IntersectionOffset.CounterClockwiseManside);

         double radiusC = p630C.distanceFT (d.GS ());
         ArrayList<LLALocation> RodsRoadCIntersection = LLAGeometry.Intersection (d.GS (), radiusC, d.getCenterCampLLA (), radiusRROutside);
         ArrayList<LLALocation> RodsRoad630Intersection = LLAGeometry.Intersection (d.getCenterCampLLA (), radiusRROutside, p630B, p630C);
        
         LLALocation p1 = p630C.getClosest (RodsRoadCIntersection);
         LLALocation p3 = p630B.getClosest (RodsRoad630Intersection);         

         CityBlock.addArcSegment (d.GS (), p1, p630C, ArcDirection.CLOCKWISE);
         CityBlock.addLineSegment (p630C, p3);

         CityBlock.addArcSegment (d.getCenterCampLLA (), p3, p1, ArcDirection.COUNTER_CLOCKWISE);
      }

      CityBlock.closePath ();
      return CityBlock;
   }
   /**
    * Draw one of the four blocks inside of Rod's Road going around Central Camp
    * @param folderPath Name of the path.
    * @param earlyInside Point closest to Center Camp on the early side 
    * @param lateInside Point closest to Center Camp on the late side 
    * @param lateOutside Point furthest from Center Camp on the early side 
    * @param earlyOutside Point furthest from Center Camp on the late side 
    * @return 
    */
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
}
