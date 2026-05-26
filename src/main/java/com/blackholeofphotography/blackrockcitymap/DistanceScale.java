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

import com.blackholeofphotography.blackrockcitymap.path.Path;
import com.blackholeofphotography.llalocation.LLALocation;
import java.awt.Color;
import java.util.ArrayList;

/**
 *
 * @author Kevin Nickerson
 */
public class DistanceScale
{
   public static ArrayList<Path> DrawScale (BurningData d)
   {
      ArrayList<Path> paths = new ArrayList<> ();
      LLALocation P1 = d.GS ().moveFT (0, 500);
      LLALocation p0 = P1.moveFT (0, 200);
      LLALocation P2 = P1.moveFT (90, 5280 / 2);
      LLALocation p3 = P2.moveFT (0, 200);
     
      Path ppp = new Path ("Scale", Color.RED);
      
      ppp.addLineSegment (p0, P1);
      ppp.addLineSegment (P1, P2);
      ppp.addLineSegment (P2, p3);
      paths.add (ppp);
      
      ppp = new Path ("Scale", Color.RED);
      P2 = P1.moveFT (90, 5280/4);
      p3 = P2.moveFT (0, 200);
      ppp.addLineSegment (P2, p3);
      paths.add (ppp);
      
      ppp = new Path ("Scale", Color.GREEN);
      
      P1 = P1.moveFT (180, 250);
      p0 = P1.moveFT (0, 200);
      P2 = P1.moveKM (90, 1.00);
      p3 = P2.moveFT (0, 200);
      ppp.addLineSegment (p0, P1);
      ppp.addLineSegment (P1, P2);
      ppp.addLineSegment (P2, p3);
      paths.add (ppp);

      ppp = new Path ("Scale", Color.GREEN);
      P2 = P1.moveKM (90, 0.5);
      p3 = P2.moveFT (0, 200);
      ppp.addLineSegment (P2, p3);
      paths.add (ppp);

      
      return paths;
   }
}
