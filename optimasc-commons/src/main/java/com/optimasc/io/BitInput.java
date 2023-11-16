package com.optimasc.io;

import java.io.IOException;

/** Interface to be implemented for reading bits.
 * 
 * @author Carl Eric Codere
 *
 */
public interface BitInput
{
  /** Reads a single bit from the stream and returns the value 0 or 1.
   * 
   * @return
   * @throws EOFException if the stream reaches the end before reading all the bits. 
   * @throws IOException if an I/O Error occurs
   * 
   */
  public int readBit() throws IOException;
  
  
  /** Reads a bitstring from the stream and returns it as a long, with the first bit read becoming the most 
   *  significant bit of the output.
   * 
   * @return
   * @throws IllegalArgumentException - if numBits is not between 0 and 64, inclusive.
   * @throws EOFException - if the stream reaches the end before reading all the bits.
   * @throws IOException - if an I/O error occurs.
   * 
   */
  public long readBits(int numBits) throws IOException;

}
