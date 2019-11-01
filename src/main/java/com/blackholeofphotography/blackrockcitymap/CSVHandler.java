/*
 * No, I took this from somewhere, but don't recall where. StackOverflow
 * most likely.
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

import java.util.*;

/**
 *
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public class CSVHandler
{
   private static String encodeToken (String s)
   {
      String encoded = new String ();
      for (int i=0; i<s.length(); i++)
      {
         if (s.charAt(i) == '\"')
            encoded += '\"';
         encoded += s.charAt(i);
      }
      if (!s.equals(encoded) || s.contains(" ") || s.contains(","))
         return "\"" + encoded + "\"";
      
      return s;         
   }
   
   private static String cleanToken (String s)
   {
      String cleaned = new String();

      s = s.trim();
//      System.out.println ("  Cleaning !" + s + "!");
      if (s.startsWith ("\""))
      {
         for (int i=1; i<s.length(); i++)
         {
            if (s.charAt(i) == '\"')
            {
               if (i < s.length()-1 && s.charAt(i+1) == '\"')
               {
                  cleaned += '\"';
                  i++;
               }
            }
            else
               cleaned += s.charAt(i);
         }
      }
      else
         cleaned = s;
      return cleaned;

   }
   
   public static  ArrayList<String> parseCSVLine (String s) 
   {
      ArrayList<String> store = new ArrayList<>();
      String curVal = new String ();

      int pos = 0;

//      System.out.println ("Parsing !" + s + "!");
      
      while (pos < s.length())
      {
         while (pos < s.length() && Character.isWhitespace(s.charAt(pos)))
            pos ++;

         char ch = s.charAt(pos);
         
         if (ch == ',') // End of token, store and go forward
         {
            store.add (curVal);
            curVal = new String();
            pos++;
         }
         else if (ch == '\"')
         {
            int dcount = 0;
            curVal += ch;
            pos++;
            boolean done = false;
            while (!done && pos < s.length())
            {
               curVal += s.charAt(pos);
               if (s.charAt(pos) == '\"' && pos < s.length()-1 && (dcount % 2)==1)
               {
//                  System.out.println ("Done @ " + pos);
                  done = true;
               }
               pos++;
            }
            
            curVal = cleanToken (curVal);
         }
         else
         {
            while (pos < s.length() && s.charAt(pos) != ',')
               curVal += s.charAt(pos++);
            curVal = curVal.trim();
         }
      }
      
      store.add (curVal);
      return store;
   }
}

