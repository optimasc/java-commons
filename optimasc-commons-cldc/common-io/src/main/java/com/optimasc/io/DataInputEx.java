package com.optimasc.io;

import java.io.IOException;
import com.optimasc.io.ByteOrder;

/** Basic interface to read data from an underlying stream and convert the 
 *  data to java primitive types. This class contains mainly the same
 *  methods as the {@link java.io.DataInput} interface, except that 
 *  it is more generic as it does not depend on the underlying stream
 *  format. Those classes implementing this interface will document
 *  or describe the underlying format of the primitive data types.
 *   
 * @author Carl Eric Codere
 *
 */
public interface DataInputEx extends java.io.DataInput
{
  /** Reads bytes from the stream, and (conceptually) concatenates them according 
   *  to the correct byte order, converts the result to a long, 
   *  as an unsigned value between 0 and 4294967295 inclusive.
   *
   * @return An unsigned int value from the stream, as a long.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   */
  public long readUnsignedInt() throws IOException;
  
  /** Reads bytes from the stream, and (conceptually) concatenates them according 
   *  to the correct byte order, converts the result to an integer, 
   *  as an unsigned value between 0 and 65535 inclusive.
   *
   * @return An unsigned short value from the stream, as an int.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   */
  public int readUnsignedShort() throws IOException;
  
  /** Reads bytes from the stream, and (conceptually) concatenates them according 
   *  to the correct byte order, converts the result to a short, 
   *  as an unsigned value between -32768 and 32767 inclusive.
   *
   * @return An unsigned short value from the stream, as a short.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   */
  public short readShort() throws IOException;
  
  /** Reads bytes from the stream that represents a boolean 
   *  value, and converts the result to a boolean. 
   *
   * @return An boolean value
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   */
  public boolean readBoolean() throws IOException;
  
  /** Reads bytes from the stream that represents an unsigned 
   *  byte value, and converts the result to an integer. The
   *  value is within the range 0 and 255 inclusive. 
   *
   * @return An unsigned octet value from the stream, as an int.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   */
  public int readUnsignedByte() throws IOException;
  
  /** Reads bytes from the stream that represents a character
   *  value and converts the results to an UCS-2 character.
   *  
   * @return A character value
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  UnsupportedEncodingException if the character 
   *            cannot be decoded to a character in the BMP plane
   *            as an UCS-2 character.
   * @throws  IOException   if an I/O error occurs.
   *
   */  
  public char readChar() throws IOException;
  
  /** Reads bytes from the stream that represents a signed
   *  8-bit value and converts the results to a byte, which
   *  is inclusive -128 to 127.
   *  
   * @return A byte value
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   *
   */  
  public byte readByte() throws IOException;
  
  
  /** Reads bytes from the stream that represents a character
   *  string and converts the results to a java string. 
   *  
   * @return A string value.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  UnsupportedEncodingException if any of the characters 
   *            cannot be decoded to a valid character
   * @throws  IOException   if an I/O error occurs.
   *
   */  
  public String readString() throws IOException;
  
  /** Reads bytes from the stream, and (conceptually) concatenates them according 
   *  to the correct byte order, converts the result to an int, 
   *  as an signed value between -2,147483648 and 2147483647 inclusive.
   *
   * @return A signed int value from the stream, as an int.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   */
  public int readInt() throws IOException;
  
  /** Reads bytes from the stream, and (conceptually) concatenates them according 
   *  to the correct byte order, converts the result to a long, 
   *  as a signed 64-bit value.
   *
   * @return A signed long value from the stream, as a long.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   */
  public long readLong() throws IOException;
  
  
  /** Reads bytes from the stream that represents a real value
   *  of type float range and converts the results to a java float. 
   *  
   * @return A float value.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   *
   */  
  public float readFloat() throws IOException;
  
  /** Reads bytes from the stream that represents a real value
   *  of type double range and converts the results to a java double. 
   *  
   * @return A double value.
   * @throws  EOFException  if this stream reaches the end before reading
   *            all the bytes.
   * @throws  IOException   if an I/O error occurs.
   *
   */  
  public double readDouble() throws IOException;
  

  /** Closes this input stream and releases any
   *  system resources associated with the stream.
   *
   * @throws IOException If an I/O Error occurs
   */
//  public void close() throws IOException;
  
  


  /** Reads up to buffer.length bytes from the stream, and stores them into
   *  b starting at index 0. The number of bytes read is returned.
   *  If no bytes can be read because the end of the stream has been
   *  reached, -1 is returned.
   *
   * @param buffer an array of bytes to be written to.
   * @return the number of bytes actually read, or -1 to indicate EOF.
   * @throws IOException If an I/O Error occurs
   */
//  public int read(byte[] buffer) throws IOException;

//  public int peekBits(int numBits) throws IOException;

//  public void skipBits(int numBits) throws IOException;
  
  
  /**
   * Skips {@code count} number of bytes. This method will not throw an
   * {@link EOFException} if the end of the input is reached before
   * {@code count} bytes where skipped.
   * 
   * @param count [in] the number of bytes to skip.
   * @return the number of bytes actually skipped.
   * @throws IOException
   *             if a problem occurs during skipping.
   */
  public int skipBytes(int count) throws IOException;  

}
