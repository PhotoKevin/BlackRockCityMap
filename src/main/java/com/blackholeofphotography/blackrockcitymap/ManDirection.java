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
 * A direction relative to the man.
 * Also supplies a set of functions used to do a 'follow the right hand wall' to 
 * traverse a block.
 * @author Kevin Nickerson (kevin@blackholeofphotography.com)
 */
public enum ManDirection
{
   TOWARD_MAN,
   FROM_MAN,
   CLOCKWISE,
   COUNTER_CLOCKWISE;
   
   public ManDirection straight ()
   {
      return this;
   }
   
   public ManDirection next ()
   {
      switch (this)
      {
         case TOWARD_MAN:   return CLOCKWISE;
         case FROM_MAN:     return COUNTER_CLOCKWISE;
         case CLOCKWISE:   return FROM_MAN;
         case COUNTER_CLOCKWISE: return TOWARD_MAN;
      }
      
      return CLOCKWISE;
   }
   

   public ManDirection left ()
   {
      switch (this)
      {
         case TOWARD_MAN:   return CLOCKWISE;
         case FROM_MAN:     return COUNTER_CLOCKWISE;
         case CLOCKWISE:   return FROM_MAN;
         case COUNTER_CLOCKWISE: return TOWARD_MAN;
      }
      
      return COUNTER_CLOCKWISE;
   }

   public ManDirection right ()
   {
      switch (this)
      {
         case TOWARD_MAN:   return COUNTER_CLOCKWISE;
         case FROM_MAN:     return CLOCKWISE;
         case CLOCKWISE:   return TOWARD_MAN;
         case COUNTER_CLOCKWISE: return FROM_MAN;
      }
      
      return COUNTER_CLOCKWISE;
   }

   public ManDirection reverse ()
   {
      switch (this)
      {
         case TOWARD_MAN:   return FROM_MAN;
         case FROM_MAN:     return TOWARD_MAN;
         case CLOCKWISE:   return COUNTER_CLOCKWISE;
         case COUNTER_CLOCKWISE: return CLOCKWISE;
      }
      
      return COUNTER_CLOCKWISE;
   }   
}
