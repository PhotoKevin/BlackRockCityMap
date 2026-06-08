package com.blackholeofphotography.blackrockcitymap;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonHelper
{
   public static String getString (JSONObject jo, String key, String defaultValue)
   {
      try
      {
         if (jo.has (key))
            return jo.getString (key);
      }
      catch (JSONException ignored)
      {

      }

      return defaultValue;
   }

   public static int getInt (JSONObject jo, String key, int defaultValue)
   {
      try
      {
         if (jo.has (key))
            return jo.getInt (key);
      }
      catch (JSONException ignored)
      {

      }

      return defaultValue;
   }
   
   
   public static double getDouble (JSONObject jo, String key, int defaultValue)
   {
      try
      {
         if (jo.has (key))
            return jo.getDouble (key);
      }
      catch (JSONException ignored)
      {

      }

      return defaultValue;
   }
}
