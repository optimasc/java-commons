package com.optimasc.io;

import java.io.IOException;

/** Basic interface that support writing bits of data.
 *  
 * @author Carl Eric Codere <carl-eric.codere@optimasc.com>
 *
 */
public interface BitOutput
{
  /** Writes a single bit, given by the least significant bit of the argument, 
   *  to the stream at the current bit offset within the current byte position. 
   *  The upper 31 bits of the argument are ignored. 
   *  
   *  The given bit replaces the previous bit at that position. 
   *  The bit offset is advanced by one and reduced modulo 8. 
   *   
   * @param bit The bit to write (the LSB is used)
   * @throws IOException In case of I/O Error
   */
  public void writeBit(int bit) throws IOException;
  
  /** Writes a sequence of bits, given by the numBits least significant bits of 
   * the bits argument in left-to-right order, to the stream at the current bit 
   * offset within the current byte position. The upper 64 - numBits bits of 
   * the argument are ignored. The bit offset is advanced by numBits and 
   * reduced modulo 8. Note that a bit offset of 0 always indicates the 
   * most-significant bit of the byte, and bytes of bits are written out in 
   * sequence as they are encountered.  
   * 
   * @param bits The bits to be written, which are in the LSB positions
   * @param numBits The number of bits to write, from 0 to 64 incluside
   * @throws IOException In case of I/O error
   * @throws IllegalArgumentException if numBits is invalid. 
   */
  public void writeBits(long bits, int numBits) throws IOException;

}
