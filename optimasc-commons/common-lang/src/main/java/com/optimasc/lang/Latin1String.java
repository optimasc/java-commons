package com.optimasc.lang;

import java.io.UnsupportedEncodingException;

/** This class represents the ISO-8859-1 character
 *  string which is composed only of bytes. 
 *  Latin1 strings are immutable and
 *  cannot be changed once they are created.
 *  
 * @author Carl Eric Codere
 *
 */
public class Latin1String implements CharSequence
{
  protected final byte[] buffer;
  
  /** Creates a byte based string by copying the values
   *  of the array into a new local array.
   * 
   * @param bytes [in] The input data that composes
   *  the character string.
   * @param offset [in] The offset in the bytes array
   *  where the character string starts
   * @param length [in] The length of the string
   *  represented by the byte array.
   *    
   */
  public Latin1String(byte[] bytes, int offset, int length)
  {
    buffer = new byte[length];
    for (int i=offset; i < length; i++)
    {
      char c = (char)(bytes[i] & 0xFF);
      if (isValidChar(c))
      {
        buffer[i-offset] = (byte) c; 
      } else
      {
        throw new IllegalArgumentException("Character '"+c+"'cannot be converted to a byte.");
      }
    }
  }
  
  protected boolean isValidChar(char c)
  {
    if ((c < 0) || (c > 255))
      return false;
    return true;
  }
  
  
  public Latin1String(CharSequence sequence)
  {
    char c;
    int inLength = sequence.length();
    buffer = new byte[inLength];
    for (int i=0; i < inLength; i++)
    {
      c = sequence.charAt(i);
      if (isValidChar(c))
      {
        buffer[i] = (byte)c;
      } else
      {
        throw new IllegalArgumentException("Character '"+c+"'cannot be converted to a byte.");
      }
    }
  }
  
  
  public int length()
  {
    return buffer.length;
  }

  public char charAt(int index)
  {
    return (char)(buffer[index] & 0xFF);
  }

  public CharSequence subSequence(int start, int end)
  {
    Latin1String string = new Latin1String(buffer,start,end);
    return string;
  }

  public String toString()
  {
    try
    {
      return new String(buffer,"ISO-8859-1");
    } catch (UnsupportedEncodingException e)
    {
      return null;
    }
  }
  
  

}
