//Defines the accepted characters and character lengths for text fields.

package com.numerex.micromed.scdl.core;

import javax.swing.text.*;

@SuppressWarnings("serial")
public class TextFieldConstraints extends PlainDocument
{
   private int max = 10;

   public static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
   public static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
   public static final String SPECIAL_CHARACTERS = ".-_* ";
   public static final String ALPHA = LOWERCASE + UPPERCASE;
   public static final String NUMERIC = "0123456789";
   public static final String FLOAT = NUMERIC + ".";
   public static final String ALPHA_NUMERIC = ALPHA + NUMERIC + SPECIAL_CHARACTERS;

   protected String acceptedChars = null;
   
   public TextFieldConstraints(int max, String acceptedchars)
   {
        this.max = max;
        acceptedChars = acceptedchars;
   } 

@SuppressWarnings("static-access")
@Override
   public void insertString(int offs, String str, AttributeSet a)
      throws BadLocationException
   {
	   if (str == null) return;
	
	   if (acceptedChars.equals(UPPERCASE))
		   str = str.toUpperCase();
	   else if (acceptedChars.equals(LOWERCASE))
		   str = str.toLowerCase();
	
	   for (int i=0; i < str.length(); i++)
	   {
		   if (acceptedChars.indexOf(str.valueOf(str.charAt(i))) == -1)
			   return;
	   }
	
	   if (acceptedChars.equals(FLOAT))
	   {
		   if (str.indexOf(".") != -1)
		   {
			   if (getText(0, getLength()).indexOf(".") != -1)
	           {
	              return;
	           }
		   }
	   }

	   // check string being inserted does not exceed max length
	
	   if (getLength()+str.length()>max)
	   {
		   // If it does, then truncate it
		   str = str.substring(0, max - getLength());
	   }
	   super.insertString(offs, str, a);
   	}
}