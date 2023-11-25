 package com.optimasc.io;

/** Byte array read and write routines for java data type primitives. The routines
 *  supports both reading and writing from big endian and little endian data.
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

  public static int putLongLittle(final long value, final byte[] buffer, final int offset)
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

  public static int putIntLittle(final int value, final byte[] buffer, final int offset)
  {
    buffer[offset + 0] = (byte) (value >> 0 & 0xff);
    buffer[offset + 1] = (byte) (value >> 8 & 0xff);
    buffer[offset + 2] = (byte) (value >> 16 & 0xff);
    buffer[offset + 3] = (byte) (value >> 24 & 0xff);
    return offset+4;
  }

  public static int putShortLittle(final short value, final byte[] buffer, final int offset)
  {
    buffer[offset + 0] = (byte)(value >> 0 & 0xff);
    buffer[offset + 1] = (byte)(value >> 8 & 0xff);
    return offset+2;
  }

  public static int putFloatLittle(final float value, final byte[] buffer, final int offset)
  {
    return putIntLittle(Float.floatToIntBits(value),buffer,offset);
  }

  public static int putDoubleLittle(final double value, final byte[] buffer, final int offset)
  {
    return putLongLittle(Double.doubleToLongBits(value),buffer,offset);
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

  public static long getShortLittle(final byte[] buffer, final int offset)
  {
    return (short)(((buffer[offset + 0] & 0xff) << 0) +
        ((buffer[offset + 1] & 0xff) << 8));
  }


  //
  public static int putLongBig(final long value, final byte[] buffer, final int offset)
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

  public static int putIntBig(final int value, final byte[] buffer, final int offset)
  {
    buffer[offset + 0] = (byte) (value >> 24 & 0xff);
    buffer[offset + 1] = (byte) (value >> 16 & 0xff);
    buffer[offset + 2] = (byte) (value >> 8 & 0xff);
    buffer[offset + 3] = (byte) (value >> 0 & 0xff);
    return offset+4;
  }

  public static int putShortBig(final short value, final byte[] buffer, final int offset)
  {
    buffer[offset + 0] = (byte)(value >> 8 & 0xff);
    buffer[offset + 1] = (byte)(value >> 0 & 0xff);
    return offset+2;
  }

  public static int putFloatBig(final float value, final byte[] buffer, final int offset)
  {
    return putIntBig(Float.floatToIntBits(value),buffer,offset);
  }

  public static int putDoubleBig(final double value, final byte[] buffer, final int offset)
  {
    return putLongBig(Double.doubleToLongBits(value),buffer,offset);
  }



  public static double getDoubleBig(final byte[] buffer, final int offset)
  {
    long value = getLongBig(buffer,offset);
    return Double.longBitsToDouble(value);
  }

  public static float getFloatBig(final byte[] buffer, final int offset)
  {
    int value = getIntBig(buffer,offset);
    return Float.intBitsToFloat(value);
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

  public static long getShortBig(final byte[] buffer, final int offset)
  {
    return (short)(((buffer[offset + 0] & 0xff) << 8) +
        ((buffer[offset + 1] & 0xff) << 0));
  }


}