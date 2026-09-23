/*
 * Copyright (c) 2026, Kevin Nickerson
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

import com.blackholeofphotography.blackrockcitymap.path.Path;
import com.blackholeofphotography.llalocation.LLALocation;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Kevin Nickerson
 */
public class CityGraph
{
   List<BRCNode> nodes = new ArrayList<> ();
   
   /**
    * This year's data set.
    */
   private final BurningDataJson d;

   public CityGraph (int year, LLALocation relocate)
   {
      d = new BurningDataJson (year);
      if (relocate != null)
         d.setGoldenSpikeOverride (relocate);
      
      buildNodeMap ();
   }
  
   
   public List<BRCNode> getGraph ()
   {
      return nodes;
   }
   
   private void buildNodeMap ()
   {

      Intersection i = null;
      try
      {
         addMainCityGrid ();
         addEsplanade ();
         if (d.getYear () >= 2026)
            addCenterCamp2026 ();
         else if (d.getYear () >= 2024)
            addCenterCamp2024 ();
         else
            addCenterCampPre2024 ();
      }
   
      catch (Exception ex)
      {
         System.out.print (i);
         ex.printStackTrace ();
      }
   }
   
   private void addMainCityGrid ()
   {
      ArrayList<Intersection> allIntersections = d.getAllIntersections ();

      for (Intersection i : allIntersections)
      {
         if (i.toString ().startsWith ("2:00"))
            System.out.println ("2:00");
         LLALocation corner = i.corner (d);
         BRCNode nn = new BRCNode (i.toString (), corner);
         nodes.add (nn);

         if (d.existsOutsideRoad (i))
         {
            Intersection from = i.getNextIntersection (ManDirection.FROM_MAN);
            BRCNeighbor nf = new BRCNeighbor (from.toString (), corner.distance (from.corner (d)));
            nn.addNeighbor (nf);
         }
         
         if (d.existsMansideRoad (i))
         {
            Intersection from = i.getNextIntersection (ManDirection.TOWARD_MAN);
            BRCNeighbor nf = new BRCNeighbor (from.toString (), corner.distance (from.corner (d)));
            nn.addNeighbor (nf);
         }
         
         if (d.existsClockwiseRoad (i))
         {
            Intersection from = i.getNextIntersection (ManDirection.CLOCKWISE);
            BRCNeighbor nf = new BRCNeighbor (from.toString (), corner.distance (from.corner (d)));
            nn.addNeighbor (nf);
         }
         
         if (d.existsCounterClockwiseRoad (i))
         {
            Intersection from = i.getNextIntersection (ManDirection.COUNTER_CLOCKWISE);
            BRCNeighbor nf = new BRCNeighbor (from.toString (), corner.distance (from.corner (d)));
            nn.addNeighbor (nf);
         }

      }
   }
   
   // Unlike the maps which carefully calculate the intersections, 
   // for the graph version I just eyeballed them.
   private void addCenterCampPre2024 ()
   {
      addNeighbor ("12:00R", new String[] {"6:30Z", "5:30Z"});
      addNeighbor ("12:00R", new String[] {"6:30A", "5:30A", "12:00S"});
      
      addNeighbor ("6:00S", new String[]{"2:32S", "9:28S"});
      addNeighbor ("12:00S", new String[]{"2:32S", "9:28S"});
      
      addNeighbor ("6:00R", new String[]{"6:00S", "4:05R", "7:55R"});
      addNeighbor ("4:05R", new String[]{"5:30A", "5:30C"});
      addNeighbor ("7:55R", new String[]{"6:30A", "6:30C"});
      addNeighbor ("9:28S", "6:30A");
      addNeighbor ("2:32S", "5:30A");
   }
   
   private void addCenterCamp2024 ()
   {
      addNeighbor ("12:00R", new String[] {"6:30Z", "5:30Z"});
      addNeighbor ("12:00R", new String[] {"6:30A", "5:30A"});
      
      
      // Go around the inner circle
      addNeighbor ("4:10S", new String[] {"6:00S", "2:25S"});
      addNeighbor ("7:50S", new String[] {"6:00S", "9:35S"});
      addNeighbor ("12:00S", new String[] {"2:25S", "9:35S", "12:00R"});
      
      // Link inner circle to main grid
      addNeighbor ("6:00S", "6:00C");
      addNeighbor ("2:25S", "5:30A");
      addNeighbor ("4:10S", "5:30B");
      addNeighbor ("9:35S", "6:30A");
      addNeighbor ("7:50S", "6:30B");
   }
   
   private void addCenterCamp2026 ()
   {
      addNeighbor ("12:00R", new String[] {"6:30Z", "5:30Z"});
      addNeighbor ("12:00R", new String[] {"6:30A", "5:30A"});
      
      addNeighbor ("6:19A", new String[] {"6:19B", "6:30A", "9:00S", "12:00U"});
      addNeighbor ("6:19B", new String[] {"6:19C", "6:30B", "6:00S"});
      addNeighbor ("6:19C", new String[] {"6:00C", "6:30C"});
      
      addNeighbor ("5:41A", new String[] {"5:41B", "5:30A", "3:00S", "12:00U"});
      addNeighbor ("5:41B", new String[] {"5:41C", "5:30B", "6:00S"});
      addNeighbor ("5:41C", new String[] {"6:00C", "5:30C"});
      
//      int r66Radius = d.getRoute66Radius ();
      
//      // This is where A crosses 6:00
//      LLALocation aCenter = d.GS ().moveFT (d.GS ().getBearing (d.getCenterCampLLA ()), d.getStreetRadiusFT ('A'));
//      LLALocation pA66Early = aCenter.moveFT (d.GS (), -r66Radius);
//      LLALocation pA66Late = aCenter.moveFT (d.GS (), r66Radius);
//      
//      double earlyBearing = d.GS ().getBearing (pA66Early);
//      double lateBearing = d.GS ().getBearing (pA66Late);
      
      // Go around the inner circle
      addNeighbor ("3:00S",  new String[] {"3:00S", "6:00S",          "12:00S"});
      addNeighbor ("6:00S",  new String[] {"3:00S", "6:00S", "9:00S",         });
      addNeighbor ("9:00S",  new String[] {         "6:00S", "9:00S", "12:00S"});
      addNeighbor ("12:00S", new String[] {"3:00S",          "9:00S", "12:00S"});
      
      addNeighbor ("6:00C", "6:00S");
   }
   
   private void addEsplanade ()
   {
      addNeighbor ("12:00R", new String[]{"5:30Z", "6:30Z"});
      addNeighbor ("2:00Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("3:00Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("4:00Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("5:00Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",                   "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("7:00Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z",                   "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("8:00Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("9:00Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("2:30Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("3:30Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("4:30Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("5:30Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",                            "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("6:30Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z",                            "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("7:30Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("8:30Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("9:30Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",          "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
      addNeighbor ("10:00Z", new String[] {"2:00Z", "2:30Z", "3:00Z", "3:30Z", "4:00Z", "4:30Z", "5:00Z", "5:30Z",         "6:30Z", "7:00Z", "7:30Z", "8:00Z", "8:30Z", "9:00Z", "9:30Z", "10:00Z"});
   }

   private void addNeighbor (String s1, String[] slist)
   {
      for (var s2 : slist)
      {
         addNeighbor (s1, s2);
         addNeighbor (s2, s1);
      }
   }
   
   private void addNeighbor (String s1, String s2)
   {
      if (s1.equals (s2))
         return;
      
      BRCNode node = findNode (s1);
      if (node == null)
      {
         Intersection i = Intersection.of (s1);
         LLALocation l = i.corner (d);
         node = new BRCNode (s1, l);
         nodes.add (node);
      }
      
      BRCNeighbor neighbor = node.findNeighbor (s2);
      if (neighbor == null)
      {
         Intersection i = Intersection.of (s2);
         LLALocation l = i.corner (d);
         node.addNeighbor (new BRCNeighbor (s2, l.distance (node.mLLALocation)));
      }
      
   }
   
   private BRCNode findNode (String name)
   {
      for (var n : nodes)
         if (n.mName.equals (name))
            return n;
      
      return null;
   }


   
   public List<Path> drawCity ()
   {
      List<Path> drawing = new ArrayList<> ();
      List<BRCNode>  cityGraph = getGraph ();
      
      for (var node : cityGraph)
      {
         for (var neighbor : node.mNeighbors)
         {
            Path p = new Path (node.mName, Color.RED);
            p.addPoint (node.mLLALocation);
            BRCNode nn = findNode (neighbor.mName);
            p.addPoint (nn.mLLALocation);
            drawing.add (p);
         }
      }
      
      return drawing;
   }
   
  
   public void write (String baseFilename)
   {
      JSONArray ja = new JSONArray ();
      for (var n: getGraph ())
         ja.put (n.toJSON ());

      File ko = new File (baseFilename + ".json");
      try (BufferedWriter writer = new BufferedWriter(new FileWriter(ko)))
      {
         writer.write (ja.toString (2));
      }
      catch (IOException ex)
      {
         
      }            
   }

}
