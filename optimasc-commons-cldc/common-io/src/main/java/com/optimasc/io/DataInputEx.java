package com.optimasc.io;

import java.io.IOException;
import com.optimasc.io.ByteOrder;

/** Basic interface that support little and big endian
 *  reading of data as well reading bits of data. It
 *  is a simplified version of the {@link javax.imageio.stream.ImageInputStream}
 *  interface.
 &
 * @author Carl Eric Codere
 *
 */
public interface DataInputEx extends java.io.DataInput
{
  /** Byte order of data is big endian. This is the default value and
   *  can be set through {@link #setByteOrder(int)}.
   */
  public static int BIG_ENDIAN = 0;

  /** Byte order of data is little endian. The value can be set through
   * {@link #setByteOrder(int)}. */
  public static int LITTLE_ENDIAN = 1;



  /** Sets the desired byte order for future reads of data values from this stream.
   *
   * @param byteOrder one of {@link #BIG_ENDIAN} or {@link #LITTLE_ENDIAN},
   * indicating whether network byte order or its reverse will be used for future reads.
   * @throws IllegalArgumentException if the parameter is not one of the allowed
   *  parameters.
   */
  public void setByteOrder(ByteOrder byteOrder);


  /** Returns the byte order with which data values will be read from this stream.
   *
   * @return one of {@link #BIG_ENDIAN} or {@link #LITTLE_ENDIAN}, indicating which byte order is
   * being used.
   */
  public ByteOrder getByteOrder();

  /** Reads 4 bytes from the stream, and (conceptually) concatenates them according to the
   *  current byte order, converts the result to a long, as an unsigned value.
   *
   *  The bit offset within the stream is reset to zero before the read occurs.
   *
   * @return An unsigned int value from the stream, as a long.
   * @throws  EOFException  if this stream reaches the end before reading
   *               all the bytes.
    * @throws  IOException   if an I/O error occurs.
   */
  public long readUnsignedInt() throws IOException;


  /** Closes this input stream and releases any
   *  system resources associated with the stream.
   *
   * @throws IOException If an I/O Error occurs
   */
  public void close() throws IOException;

  /** Reads up to len bytes from the stream, and stores them into buffer
   *  starting at index off. The number of bytes read is returned.
   *  If no bytes can be read because the end of the stream has been
   *  reached, -1 is returned.
   *
   * The bit offset within the stream is reset to zero before the read occurs
   *
   * @param buffer an array of bytes to be written to.
   * @param off the starting position within buffer to write to.
   * @param len the maximum number of bytes to read.
   * @return the number of bytes actually read, or -1 to indicate EOF.
   * @throws IOException If an I/O Error occurs
   */
  public int read(byte[] buffer, int off, int len) throws IOException;

  /** Reads up to buffer.length bytes from the stream, and stores them into
   *  b starting at index 0. The number of bytes read is returned.
   *  If no bytes can be read because the end of the stream has been
   *  reached, -1 is returned.
   *
   * @param buffer an array of bytes to be written to.
   * @return the number of bytes actually read, or -1 to indicate EOF.
   * @throws IOException If an I/O Error occurs
   */
  public int read(byte[] buffer) throws IOException;

  /** Reads a single byte from the stream and returns it as an integer
   *  between 0 and 255. If the end of the stream is reached, -1 is returned.
   *
   *  The bit offset within the stream is reset to zero before the read occurs.
   *
   * @return a byte value from the stream, as an int, or -1 to indicate EOF.
   * @throws IOException
   */
  public int read() throws IOException;

  /** Sets the bit offset to an integer between 0 and 7, inclusive.
   *
   * @param offset the desired offset, as an int between 0 and 7, inclusive.
   * @throws IllegalArgumentException if bitOffset is not between 0 and 7, inclusive.
   */
  public void setBitOffset(int offset);

  public int peekBits(int numBits) throws IOException;

  public void skipBits(int numBits) throws IOException;

}
