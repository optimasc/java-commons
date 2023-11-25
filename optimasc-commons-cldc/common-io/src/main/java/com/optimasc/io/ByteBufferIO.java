 package com.optimasc.io;

/** Byte array read and write routines for java data type primitives. The routines
 *  supports both reading and writing from big endian and little endian data. The
 *  parameter and method names try to mimic the <code>ByteBuffer</code> class.
 *
 *  <p>With some benchmarking, it has been confirmed that doing similar
 *  operations through <code>DataInputStream</code> is 22 times slower on Java 6 desktop
 *  platform compared to calling these methods directly (On java 8, it is around 20 times
 *  slower).</p>
 *
 * @author Carl Eric Codere
 *
 */
public class ByteBufferIO {
  
  
  /** Writes a long value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 8 byte
   *  little-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The long value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putLongLittle(final byte[] buffer, final int offset, final long value)
  {
    buffer[offset + 0] = (byte) (value >> 0 & 0xff);
    buffer[offset + 1] = (byte) (value >> 8 & 0xff);
    buffer[offset + 2] = (byte) (value >> 16 & 0xff);
    buffer[offset + 3] = (byte) (value >> 24 & 0xff);
    buffer[offset + 4] = (byte) (value >> 32 & 0xff);
    buffer[offset + 5] = (byte) (value >> 40 & 0xff);
    buffer[offset + 6] = (byte) (value >> 48 & 0xff);
    buffer[offset + 7] = (byte) (value >> 56 & 0xff);
    return offset+8;
  }
  
  

  /** Writes an int value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 4 byte
   *  little-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The int value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putIntLittle(final byte[] buffer, final int offset, final int value)
  {
    buffer[offset + 0] = (byte) (value >> 0 & 0xff);
    buffer[offset + 1] = (byte) (value >> 8 & 0xff);
    buffer[offset + 2] = (byte) (value >> 16 & 0xff);
    buffer[offset + 3] = (byte) (value >> 24 & 0xff);
    return offset+4;
  }
  
  
  

  /** Writes a short value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 2 byte
   *  little-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The short value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putShortLittle(final byte[] buffer, final int offset, final short value)
  {
    buffer[offset + 0] = (byte)(value >> 0 & 0xff);
    buffer[offset + 1] = (byte)(value >> 8 & 0xff);
    return offset+2;
  }

  /** Writes a float value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 4 byte
   *  IEEE754 compatible little-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The float value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putFloatLittle(final byte[] buffer, final int offset, final float value)
  {
    return putIntLittle(buffer,offset,Float.floatToIntBits(value));
  }

  /** Writes a double value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 8 byte
   *  IEEE754 compatible little-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The double value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putDoubleLittle(final byte[] buffer, final int offset, final double value)
  {
    return putLongLittle(buffer,offset,Double.doubleToLongBits(value));
  }
  
  /** Writes a char value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 2 byte
   *  UCS-2 character little-endian value in the byte buffer. 
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The char value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putCharLittle(final byte[] buffer, final int offset, final char value)
  {
    return putShortLittle(buffer,offset,(short)value);
  }


  /** Writes a byte value at specified byte offset in the byte buffer and
   *  returns the updated byte offset.  
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The byte value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int put(final byte[] buffer, int offset, final byte value)
  {
    buffer[offset++] = value;
    return offset;
  }
  
  /** Writes a byte array at the specified byte offset in the byte 
   *  buffer and returns the updated byte offset.  
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param src [in] The byte values to be written. 
   * @param srcOffset [in] The offset at which the bytes will be read from <code>src</code>
   * @param length [in] The number of bytes to copy
   * @return <code>offset</code> + number of bytes written.
   */
  public static int put(final byte[] buffer, final int offset, final byte[] src, int srcOffset, int length)
  {
    System.arraycopy(src, srcOffset, buffer, offset, length);
    return offset + length; 
  }
  
  
  public static double getDoubleLittle(final byte[] buffer, final int offset)
  {
    long value = getLongLittle(buffer,offset);
    return Double.longBitsToDouble(value);
  }

  public static float getFloatLittle(final byte[] buffer, final int offset)
  {
    int value = getIntLittle(buffer,offset);
    return Float.intBitsToFloat(value);
  }


  public static long getLongLittle(final byte[] buffer, final int offset)
  {
    return
        ((long)(buffer[offset]   & 0xff) << 0) |
        ((long)(buffer[offset+1] & 0xff) << 8) |
        ((long)(buffer[offset+2] & 0xff) << 16) |
        ((long)(buffer[offset+3] & 0xff) << 24) |
        ((long)(buffer[offset+4] & 0xff) << 32) |
        ((long)(buffer[offset+5] & 0xff) << 40) |
        ((long)(buffer[offset+6] & 0xff) << 48) |
        ((long)(buffer[offset+7] & 0xff)<< 56);
  }

  public static int getIntLittle(final byte[] buffer, final int offset)
  {
    return ((buffer[offset + 0] & 0xff) << 0) +
        ((buffer[offset + 1] & 0xff) << 8) +
        ((buffer[offset + 2] & 0xff) << 16) +
        ((buffer[offset + 3] & 0xff) << 24);
  }

  public static short getShortLittle(final byte[] buffer, final int offset)
  {
    return (short)(((buffer[offset + 0] & 0xff) << 0) +
        ((buffer[offset + 1] & 0xff) << 8));
  }

  //
  /** Writes a long value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 8 byte
   *  big-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The long value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putLongBig(final byte[] buffer, final int offset, final long value)
  {
    buffer[offset + 0] = (byte) (value >> 56 & 0xff);
    buffer[offset + 1] = (byte) (value >> 48 & 0xff);
    buffer[offset + 2] = (byte) (value >> 40 & 0xff);
    buffer[offset + 3] = (byte) (value >> 32 & 0xff);
    buffer[offset + 4] = (byte) (value >> 24 & 0xff);
    buffer[offset + 5] = (byte) (value >> 16 & 0xff);
    buffer[offset + 6] = (byte) (value >>  8 & 0xff);
    buffer[offset + 7] = (byte) (value >>  0 & 0xff);
    return offset+8;
  }

  /** Writes an int value at specified byte offset in the byte buffer and
   * returns the updated byte offset. The value written is written as a 4 byte
   * big-endian value in the byte buffer.
   *   
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The int value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putIntBig(final byte[] buffer, final int offset, final int value)
  {
    buffer[offset + 0] = (byte) (value >> 24 & 0xff);
    buffer[offset + 1] = (byte) (value >> 16 & 0xff);
    buffer[offset + 2] = (byte) (value >> 8 & 0xff);
    buffer[offset + 3] = (byte) (value >> 0 & 0xff);
    return offset+4;
  }

  /** Writes a short value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 2 byte
   *  big-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The short value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putShortBig(final byte[] buffer, final int offset, final short value)
  {
    buffer[offset + 0] = (byte)(value >> 8 & 0xff);
    buffer[offset + 1] = (byte)(value >> 0 & 0xff);
    return offset+2;
  }

  /** Writes a float value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 4 byte
   *  IEEE754 compatible big-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The float value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putFloatBig(final byte[] buffer, final int offset, final float value)
  {
    return putIntBig(buffer,offset,Float.floatToIntBits(value));
  }

  /** Writes a double value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 8 byte
   *  IEEE754 compatible big-endian value in the byte buffer.
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The double value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putDoubleBig(final byte[] buffer, final int offset, final double value)
  {
    return putLongBig(buffer,offset,Double.doubleToLongBits(value));
  }
  
  
  /** Writes a char value at specified byte offset in the byte buffer and
   *  returns the updated byte offset. The value written is written as a 2 byte
   *  UCS-2 character big-endian value in the byte buffer. 
   * 
   * @param buffer [in,out] The buffer that will receive the written values 
   * @param offset [in] The offset at which the bytes will be written
   * @param value [in] The char value to be written. 
   * @return <code>offset</code> + number of bytes written.
   */
  public static int putCharBig(final byte[] buffer, final int offset, final char value)
  {
    return putShortBig(buffer,offset,(short)value);
  }
  




  public static long getLongBig(final byte[] buffer, final int offset)
  {
    return
        ((long)(buffer[offset]   & 0xff) << 56) |
        ((long)(buffer[offset+1] & 0xff) << 48) |
        ((long)(buffer[offset+2] & 0xff) << 40) |
        ((long)(buffer[offset+3] & 0xff) << 32) |
        ((long)(buffer[offset+4] & 0xff) << 24) |
        ((long)(buffer[offset+5] & 0xff) << 18) |
        ((long)(buffer[offset+6] & 0xff) << 8) |
        ((long)(buffer[offset+7] & 0xff)<< 0);
  }

  public static int getIntBig(final byte[] buffer, final int offset)
  {
    return ((buffer[offset + 0] & 0xff) << 24) +
        ((buffer[offset + 1] & 0xff) << 16) +
        ((buffer[offset + 2] & 0xff) << 8) +
        ((buffer[offset + 3] & 0xff) << 0);
  }

  public static short getShortBig(final byte[] buffer, final int offset)
  {
    return (short)(((buffer[offset + 0] & 0xff) << 8) +
        ((buffer[offset + 1] & 0xff) << 0));
  }
  
  
  public static float getFloatBig(final byte[] buffer, final int offset)
  {
    int value = getIntBig(buffer,offset);
    return Float.intBitsToFloat(value);
  }


  public static double getDoubleBig(final byte[] buffer, final int offset)
  {
    long value = getLongBig(buffer,offset);
    return Double.longBitsToDouble(value);
  }
  


}