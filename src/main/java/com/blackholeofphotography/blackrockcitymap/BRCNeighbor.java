
package com.blackholeofphotography.blackrockcitymap;

import com.blackholeofphotography.llalocation.LLALocation;
import org.json.JSONObject;

/**
 *
 * @author Kevin Nickerson
 */
public class BRCNeighbor
{
   String mName;
   double mDistance;

   public BRCNeighbor (String name, double distance)
   {
      mName = name;
      mDistance = distance;
   }

   
   public JSONObject toJSON ()
   {
      JSONObject jo = new JSONObject ();
      jo.put ("name", mName);
      jo.put ("distance", mDistance);
      
      return jo;
   }
}
