package com.optimasc.lang;

import java.io.UnsupportedEncodingException;

/** Represents a subset of the US-ASCII character set
 *  which can be printed (ASCII Table Characters 32-127). This
 *  is equivalent to ASN.1 type <code>VisibleString</code>.  
 * 
 * @author Carl Eric Codere
 *
 */
public class VisibleString extends Latin1String
{
  public VisibleString(byte[] bytes, int offset, int length)
  {
    super(bytes, offset, length);
  }

  protected boolean isValidChar(char c)
  {
    if ((c < 32) || (c > 127))
      return false;
    return true;
  }

  public String toString()
  {
    try
    {
      return new String(buffer,"US-ASCII");
    } catch (UnsupportedEncodingException e)
    {
      return null;
    }
  }
  
  

}
