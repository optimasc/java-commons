package com.optimasc.lang;

/** Represents a sequence of octet, that is, 
 *  a sequence of byte values. 
 * 
 * @author Carl Eric Codere
 *
 */
public interface OctetSequence
{
  /** Returns the unsigned byte at the specified index. */
  public int octetAt(int index);
  /** Returns the length of this octet sequence. */
  public int length();
}
