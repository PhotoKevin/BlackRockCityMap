/*
 * Copyright (c) 2024, Kevin Nickerson
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
 * Class to create a version of the Black Rock City map using the road center
 * lines instead of the edges.
 *
 * @author Kevin Nickerson
 */
public class BlackRockCityCenterline
{

   /**
    * This year's data set.
    */
   private final BurningData d;
   /**
    * Location of the Golden Spike.
    */
   private LLALocation GoldenSpike = null;

   public BlackRockCityCenterline (int year)
   {
      d = new BurningData (year);
      GoldenSpike = d.GS ();
   }
   
   public ArrayList<Path> drawCity ()
   {
      ArrayList<Path> drawing = new ArrayList<> ();
      try
      {
         drawing.add (Perimeter ());
         for (int hour = 2; hour < 10; hour++)
         {
            for (int qhour = 0; qhour < 4; qhour++)
               drawing.addAll (drawRadialFrom (hour, qhour * 15));
         }

         for (char ch = AnnularStreet.ESPLANADE; ch != d.maxRoadLetter () + 1; ch = AnnularStreet.getNextStreetLetter (ch))
         {
            drawing.addAll (drawAnnular (ch));

            for (int hour = 2; hour < 10; hour++)
            {
               for (int qhour = 0; qhour < 4; qhour++)
               {
                  Intersection i = new Intersection (hour, qhour * 15, ch);
                  if (d.isPlaza (i))
                  {
                     LLALocation center = i.corner (d);
                     Path plaza = new Path (String.format ("%d:%d%c", hour, qhour * 15, ch), Color.BLACK);
                     plaza.addCircle (center, center.moveFT (90, d.getPlazaRadius ()));
                     drawing.add (plaza);
                  }
               }
            }
         }

         Path tenOclock = new Path ("10:00", Color.BLACK);
         tenOclock.addLineSegment (new Intersection (10, 00, AnnularStreet.ESPLANADE).corner (d), new Intersection (10, 00, d.maxRoadLetter ()).corner (d));
         drawing.add (tenOclock);

         Path centerCamp = new Path ("CenterCamp", Color.BLACK);
         centerCamp.addCircle (d.getCenterCampLLA (), d.getCenterThemeCampInnerRadius ());
         drawing.add (centerCamp);

         Path man = new Path ("Man", Color.BLACK);
         man.addCircle (GoldenSpike, d.getManPlazaRadius ());
         drawing.add (man);
         
         Path temple = new Path ("Temple", Color.BLACK);
         man.addCircle (d.getTempleLLA (), d.getTemplePlazaRadius ());
         drawing.add (temple);
         
         
         drawing.addAll (drawRoadsToMan ());
         
         if (d.getYear () >= 2024)
            drawing.addAll (drawCentralPlaza2024 ());

      }
//      catch (IOException | NumberFormatException e)
//      {
//         System.out.println (e.toString ());
//      }
      catch (Exception e)
      {
         System.out.println (e.toString ());
         throw e;
      }

      return drawing;
   }

   /**
    * Create a Path for the perimeter fence.
    *
    * @return Path of the perimeter fence
    */
   public Path Perimeter ()
   {
      LLALocation P1 = d.P1 ();

      double radius = GoldenSpike.distanceFT (P1);
      double p1Bearing = GoldenSpike.getBearing (P1);
      Path ppp = new Path ("Perimeter");
      for (int i = 0; i < 5; i++)
         ppp.addPoint (GoldenSpike.moveFT (72 * i + p1Bearing, radius));

      ppp.closePath ();
      return ppp;
   }

   ArrayList<Path> drawRadialFrom (int hour, int minute)
   {
      ArrayList<Path> segments = new ArrayList<> ();

      Intersection start = new Intersection (hour, minute, AnnularStreet.ESPLANADE);
      
      if (d.isPlazaPortal (start))
      {
         Path edge = new Path ("Edge", Color.BLACK);
         for (LLALocation pt : getPortalEdge (start, ManDirection.CLOCKWISE))
            edge.addPoint (pt);
         segments.add (edge);

         edge = new Path ("Edge", Color.BLACK);
         for (LLALocation pt : getPortalEdge (start, ManDirection.COUNTER_CLOCKWISE))
            edge.addPoint (pt);

         segments.add (edge);
      }

      while (d.isPlazaPortal (start) || d.isPortal (start) || d.isMidPlazaPortal (start))
         start = start.getNextIntersection (ManDirection.FROM_MAN);

      while (!d.existsOutsideRoad (start) && !d.isPortal (start) && start.annular.getStreetLetter () != d.maxRoadLetter ())
         start = start.getNextIntersection (ManDirection.FROM_MAN);

      while (start.annular.getStreetLetter () != d.maxRoadLetter ())
      {
         Intersection end = start;
         while (d.existsOutsideRoad (end))
         {
            Intersection next = end.getNextIntersection (ManDirection.FROM_MAN);
            end = next;
            if (d.isPlaza (next))
               break;
         }

         if (end != start)
         {
            LLALocation p1 = start.corner (d);
            if (d.isPlaza (start))
               p1 = p1.moveFT (GoldenSpike.getBearing (p1), d.getPlazaRadius ());

            LLALocation p2 = end.corner (d);
            if (d.isPlaza (end))
               p2 = p2.moveFT (p2.getBearing (GoldenSpike), d.getPlazaRadius ());

            Path radial = new Path (String.format ("%d:%02d", hour, minute));
            radial.addLineSegment (p1, p2);
            segments.add (radial);
         }

         start = end;
         // Skip over missing roads
         while (!d.existsOutsideRoad (start) && start.annular.getStreetLetter () != d.maxRoadLetter ())
            start = start.getNextIntersection (ManDirection.FROM_MAN);

      }

      return segments;
   }

   ArrayList<Path> drawAnnular (char ch)
   {
      boolean pr = (ch == 'c');
      ArrayList<Path> segments = new ArrayList<> ();
      if (pr)
         System.out.println ("Start Annular");

      Intersection start = new Intersection (2, 0, ch);

      while (!d.existsClockwiseRoad (start) && !d.isPortal (start) && start.radial.getHour () != 10)
         start = start.getNextIntersection (ManDirection.CLOCKWISE);

      while (start.radial.getHour () != 10)
      {
         Intersection end = start;
         while (d.existsClockwiseRoad (end))
         {
            Intersection next = end.getNextIntersection (ManDirection.CLOCKWISE);
            end = next;

            if (d.isPlaza (next) || d.isPlazaPortal (next) || d.isMidPlazaPortal (next))
               break;
         }

         if (pr)
            System.out.printf ("\nAnnular segment %s %s\n", start.toString (), end.toString ());
         
         if (end != start)
         {
            
            LLALocation p1 = start.corner (d);
            if (d.isPlaza (start))
            {
               if (pr) System.out.println ("isPlaza");
               p1 = p1.moveFT (GoldenSpike.getBearing (p1)+90, d.getPlazaRadius ());
            }
            else if (d.isMidPlazaPortal (start))
            {
               if (pr) System.out.println ("isMidPlazaPortal");
               p1 = p1.moveFT (GoldenSpike.getBearing (p1)+90, d.getPlazaRadius ());
               
               Intersection esp = new Intersection (start.radial, AnnularStreet.ESPLANADE);
               ArrayList<LLALocation> portalEdge = getPortalEdge (esp, ManDirection.CLOCKWISE);
               ArrayList<LLALocation> points = LLAGeometry.Intersection (GoldenSpike, GoldenSpike.distanceFT (start.corner (d)), portalEdge.get (0), portalEdge.get(1));
               p1 = p1.getClosest (points);
            }
            else if (d.isPlazaPortal (start))
            {
               if (pr) System.out.println ("isPlazaPortal");

               ArrayList<LLALocation> portalEdge = getPortalEdge (start, ManDirection.CLOCKWISE);
               ArrayList<LLALocation> points = LLAGeometry.Intersection (GoldenSpike, GoldenSpike.distanceFT (start.corner (d)), portalEdge.get (0), portalEdge.get(1));
               p1 = p1.moveFT (GoldenSpike.getBearing (p1)+90, d.getPortalWidth ()/2);
               p1 = p1.getClosest (points);
            }
            else if (d.isPortal (start))
            {
               if (pr) System.out.println ("isPortal");
               p1 = p1.moveFT (GoldenSpike.getBearing (p1)+90, d.getPortalWidth ()/2);
            }

            LLALocation p2 = end.corner (d);
            if (d.isPlaza (end))
               p2 = p2.moveFT (p2.getBearing (GoldenSpike)+90, d.getPlazaRadius ());
            else if (d.isMidPlazaPortal (end))
            {
               Intersection esp = new Intersection (end.radial, AnnularStreet.ESPLANADE);
               ArrayList<LLALocation> portalEdge = getPortalEdge (esp, ManDirection.COUNTER_CLOCKWISE);
               ArrayList<LLALocation> points = LLAGeometry.Intersection (GoldenSpike, GoldenSpike.distanceFT (start.corner (d)), portalEdge.get (0), portalEdge.get(1));
               p2 = p2.moveFT (p2.getBearing (GoldenSpike)+90, d.getPlazaRadius ());
               p2 = p2.getClosest (points);
            }
            else if (d.isPlazaPortal (end))
            {
               ArrayList<LLALocation> portalEdge = getPortalEdge (end, ManDirection.COUNTER_CLOCKWISE);
               ArrayList<LLALocation> points = LLAGeometry.Intersection (GoldenSpike, GoldenSpike.distanceFT (start.corner (d)), portalEdge.get (0), portalEdge.get(1));
//                        p2 = p2.moveFT (p2.getBearing (GoldenSpike)+90, d.getPortalWidth ()/2);
               p2 = p2.getClosest (points);
            }
            else if (d.isPortal (end))
               p2 = p2.moveFT (p2.getBearing (GoldenSpike)+90, d.getPortalWidth ()/2);

            if (pr)
               System.out.printf ("Annular %s -> %s\n", start.toString (), end.toString ());
            Path annular = new Path (String.valueOf (ch), Color.BLACK);
            annular.addArcSegment (GoldenSpike, p1, p2, ArcDirection.CLOCKWISE);
            segments.add (annular);
         }

         start = end;
         // Skip over missing roads
         while (!d.existsClockwiseRoad (start) && start.radial.getHour () < 10)
            start = start.getNextIntersection (ManDirection.CLOCKWISE);
      }
      return segments;
   }
   
   private ArrayList<LLALocation> getPortalEdge (Intersection intersection, ManDirection edge)
   {
      System.out.printf ("getPortalEdge: %s %s\n", intersection.toString (), edge.toString ());
      ArrayList<LLALocation> points = new ArrayList<> ();
      double bearing = GoldenSpike.getBearing (intersection.corner (d));
      double sidewise = bearing;
      if (edge == ManDirection.CLOCKWISE)
         sidewise += 90;
      else
         sidewise -= 90;
      
      if (d.isPlaza (intersection))
      {
         LLALocation p1 = intersection.corner (d, IntersectionOffset.Center);
         p1 = p1.moveFT (sidewise, d.getPortalWidth ()/2);
        
         
         LLALocation p2 = intersection.getNextIntersection (ManDirection.FROM_MAN).corner (d, IntersectionOffset.Manside);
         p2 = p2.moveFT (sidewise, d.getRegularStreetWidth ()/2);
         
         ArrayList<LLALocation> pts = LLAGeometry.Intersection (GoldenSpike, d.getEsplanadeRadius (), p2, p1);
         points.add (p1.getClosest (pts));
         points.add (p2.getClosest (pts));
      }
      
      else if (d.isPlazaPortal (intersection))
      {
         LLALocation p1 = intersection.corner (d);
         p1 = p1.moveFT (sidewise, d.getPortalWidth ()/2);
         
         Intersection plaza = new Intersection (intersection.radial, 'B');
         LLALocation p2 = plaza.corner (d);
         p2 = p2.moveFT (bearing+180, d.getPlazaRadius ()/2);
         p2 = p2.moveFT (sidewise, d.getRegularStreetWidth ()/2);

         ArrayList<LLALocation> pts = LLAGeometry.Intersection (GoldenSpike, GoldenSpike.distanceFT (intersection.corner (d)), p1, p2);
         if (!pts.isEmpty ())
         {
            System.out.printf ("Dist: %f\n", p1.distanceFT (p1.getClosest (pts)));
            points.add (p1.getClosest (pts));

            pts = LLAGeometry.Intersection (plaza.corner (d), d.getPlazaRadius (), p1, p2);

            points.add (p2.getClosest (pts));
         }
         else
         {
            System.out.println ("was empty");
            points.add (p1);
            points.add (p2);
         }
      }
      
      return points;
   }
   
   ArrayList<Path> drawRoadsToMan ()
   {
      ArrayList<Path> drawing = new ArrayList<> ();

      LLALocation i300Esp = new Intersection (3, 00, AnnularStreet.ESPLANADE).corner (d);
      LLALocation i900Esp = new Intersection (9, 00, AnnularStreet.ESPLANADE).corner (d);      
      LLALocation i600Esp = new Intersection (6, 00, AnnularStreet.ESPLANADE).corner (d);

      ArrayList<LLALocation> man300 = LLAGeometry.Intersection (GoldenSpike, d.getManPlazaRadius (), GoldenSpike, i300Esp);
      ArrayList<LLALocation> man600 = LLAGeometry.Intersection (GoldenSpike, d.getManPlazaRadius (), GoldenSpike, i600Esp);
      ArrayList<LLALocation> central600 = LLAGeometry.Intersection (d.getCenterCampLLA (), d.getCenterThemeCampOuterRadius (), d.getCenterCampLLA (), GoldenSpike);

      ArrayList<LLALocation> temple600 = LLAGeometry.Intersection (d.getTempleLLA (), d.getTemplePlazaRadius (), d.getCenterCampLLA (), GoldenSpike);
      
      Path p = new Path ("3:00Man", Color.BLACK);
      p.addLineSegment (i300Esp, i300Esp.getClosest (man300));
      drawing.add (p);
      
      p = new Path ("9:00Man", Color.BLACK);
      p.addLineSegment (i900Esp, i900Esp.getClosest (man300));
      drawing.add (p);

      p = new Path ("6:00Man", Color.BLACK);
      p.addLineSegment (i600Esp.getClosest (central600), i600Esp.getClosest (man600));
      drawing.add (p);
      
      p = new Path ("6:00Temple", Color.BLACK);
      p.addLineSegment (d.GS ().getClosest (temple600), d.getTempleLLA ().getClosest (man600));
      drawing.add (p);
      
      drawing.add (fillPortalOpening (3));
      drawing.add (fillPortalOpening (9));
      
      LLALocation p4 = KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampOuterRadius (), -d.getCenterCampKeyholeWidest ());
      LLALocation p5 = KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampOuterRadius (), d.getCenterCampKeyholeWidest ());

      p = new Path ("Plaza", Color.BLACK);
      p.addArcSegment (d.getCenterCampLLA (), p5, p4, ArcDirection.CLOCKWISE);
      drawing.add (p);


      return drawing;
   }
   
   Path fillPortalOpening (int hour)
   {
      Intersection portal = new Intersection (hour, 0, AnnularStreet.ESPLANADE);
      ArrayList<LLALocation> earlyPortalEdge = getPortalEdge (portal, ManDirection.COUNTER_CLOCKWISE);
      ArrayList<LLALocation> latePortalEdge = getPortalEdge (portal, ManDirection.CLOCKWISE);
      
      ArrayList<LLALocation> earlyPoints = LLAGeometry.Intersection (GoldenSpike, GoldenSpike.distanceFT (portal.corner (d)), earlyPortalEdge.get (0), earlyPortalEdge.get(1));
      ArrayList<LLALocation> latePoints = LLAGeometry.Intersection (GoldenSpike, GoldenSpike.distanceFT (portal.corner (d)), latePortalEdge.get (0), latePortalEdge.get(1));

      LLALocation p1 = portal.corner (d, IntersectionOffset.CounterClockwise);
      p1 = p1.getClosest (latePoints);
      
      LLALocation p2 = portal.corner (d, IntersectionOffset.Clockwise);
      p2 = p2.getClosest (earlyPoints);

      Path p = new Path ("PortalOpening", Color.BLACK);
      p.addLineSegment (p1, p2);
      return p;
   }
   
   
   
   /**
    * Draw the 2024 Central Plaza blocks.
    * This is all very tedious working out where the points are
    * and drawing arcs and lines. I don't see a way to boil this
    * down to some common routines.
    * @return A set of Path's
    */
   ArrayList<Path> drawCentralPlaza2024 ()
   {
      ArrayList<Path> drawing = new ArrayList<> ();
      
      // Various normal intersections we need.
      Intersection i530A = new Intersection (5, 30, 'A');
      Intersection i630A = new Intersection (6, 30, 'A');
      Intersection i530B = new Intersection (5, 30, 'B');
      Intersection i630B = new Intersection (6, 30, 'B');
      Intersection i600C = new Intersection (6, 00, 'C');
      
      // places where the A and B roads intersect with center camp.
      ArrayList<LLALocation> centerAIntersection = LLAGeometry.Intersection (d.GS (), d.getStreetRadiusFT ('A'),
              d.getCenterCampLLA (), d.getCenterThemeCampInnerRadius ());

      ArrayList<LLALocation> centerBIntersection = LLAGeometry.Intersection (d.GS (), d.getStreetRadiusFT ('B'),
               d.getCenterCampLLA (), d.getCenterThemeCampInnerRadius ());

      LLALocation p1 = KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampInnerRadius (), -d.getCenterCampKeyholeNarrowest ());
      LLALocation p2 = p1.getClosest (centerAIntersection);
      LLALocation p3 = i530A.corner (d);
      LLALocation p4 = KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampOuterRadius (), -d.getCenterCampKeyholeWidest ());
      
      // Wedge between 12 and 3
      Path p = new Path ("EarlyWedge", Color.BLACK);
      p.addArcSegment (d.GS (), p2, p3, ArcDirection.COUNTER_CLOCKWISE);
      p.addArcSegment (d.getCenterCampLLA (), p3, p4, ArcDirection.COUNTER_CLOCKWISE);
      p.addLineSegment (p4, p1);
      drawing.add (p);
      
      // Wedge between 9 and 12
      p1 = KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampInnerRadius (), d.getCenterCampKeyholeNarrowest ());
      p2 = p1.getClosest (centerAIntersection);
      p3 = i630A.corner (d); //, IntersectionOffset); //.CounterClockwiseManside);
      p4 = KeyHolePoint (d.getCenterCampLLA (), d.getBearing (new RadialStreet ("12:00")), d.getCenterThemeCampOuterRadius (), d.getCenterCampKeyholeWidest ());
      
      p = new Path ("LateWedge", Color.BLACK);
      p.addArcSegment (d.GS (), p2, p3, ArcDirection.CLOCKWISE);
      p.addArcSegment (d.getCenterCampLLA (), p3, p4, ArcDirection.CLOCKWISE);
      p.addLineSegment (p4, p1);
      drawing.add (p);

      // Block between A and B on early side
      p4 = i530B.corner (d);
      p3 = p4.getClosest (centerBIntersection);
      
      p = new Path ("earlyAB", Color.BLACK);
      p.addArcSegment (d.GS (), p3, p4, ArcDirection.COUNTER_CLOCKWISE);
      drawing.add (p);

      p4 = i630B.corner (d);
      p3 = p4.getClosest (centerBIntersection);
      
      p = new Path ("lateAB", Color.BLACK);
      p.addArcSegment (d.GS (), p3, p4, ArcDirection.CLOCKWISE);
      drawing.add (p);
      

      LLALocation p5 = i600C.corner (d);
      ArrayList<LLALocation> centralIntersection = LLAGeometry.Intersection (d.getCenterCampLLA (), d.getCenterThemeCampInnerRadius (),
              p5, GoldenSpike);

      p = new Path ("lateBC", Color.BLACK);
      p.addPoint (p5);
      p.addLineSegment (p5, p5.getClosest (centralIntersection));
      drawing.add (p);

      return drawing;
   }
}
