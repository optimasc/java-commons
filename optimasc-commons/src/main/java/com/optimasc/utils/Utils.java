/*
 * Copyright Aduna (http://www.aduna-software.com/) (c) 1997-2006.
 *
 * Licensed under the Aduna BSD-style license.
 */
 package com.optimasc.utils;


public class Utils {


  public static int putIntBig(int value, byte[] array, int offset) {
    array[offset]   = (byte)(0xff & (value >> 24));
    array[offset+1] = (byte)(0xff & (value >> 16));
    array[offset+2] = (byte)(0xff & (value >> 8));
    array[offset+3] = (byte)(0xff & value);
    return offset+4;
  }

  public static int getIntBig(byte[] array, int offset) {
    return
      ((array[offset]   & 0xff) << 24) |
      ((array[offset+1] & 0xff) << 16) |
      ((array[offset+2] & 0xff) << 8) |
       (array[offset+3] & 0xff);
  }

  public static int putLongBig(long value, byte[] array, int offset) {
    array[offset]   = (byte)(0xff & (value >> 56));
    array[offset+1] = (byte)(0xff & (value >> 48));
    array[offset+2] = (byte)(0xff & (value >> 40));
    array[offset+3] = (byte)(0xff & (value >> 32));
    array[offset+4] = (byte)(0xff & (value >> 24));
    array[offset+5] = (byte)(0xff & (value >> 16));
    array[offset+6] = (byte)(0xff & (value >> 8));
    array[offset+7] = (byte)(0xff & value);
    return offset+8;
  }

  public static long getLongBig(byte[] array, int offset) {
    return
      ((long)(array[offset]   & 0xff) << 56) |
      ((long)(array[offset+1] & 0xff) << 48) |
      ((long)(array[offset+2] & 0xff) << 40) |
      ((long)(array[offset+3] & 0xff) << 32) |
      ((long)(array[offset+4] & 0xff) << 24) |
      ((long)(array[offset+5] & 0xff) << 16) |
      ((long)(array[offset+6] & 0xff) << 8) |
      ((long)(array[offset+7] & 0xff));
  }

    public static int putShortBig(short value, byte[] array, int offset)
    {
        array[offset+1] = (byte)(value & 0xFF);
        array[offset] = (byte)((value  >>> 8) & 0xFF);
        return offset+2;
    }



    public static int putShortLittle(short value, byte[] array, int offset)
    {
        array[offset] = (byte)(value & 0xFF);
        array[offset+1] = (byte)((value  >>> 8) & 0xFF);
        return offset+2;
    }

  public static int putIntLittle(int value, byte[] array, int offset) {
    array[offset+3]   = (byte)(0xff & (value >> 24));
    array[offset+2] = (byte)(0xff & (value >> 16));
    array[offset+1] = (byte)(0xff & (value >> 8));
    array[offset+0] = (byte)(0xff & value);
    return offset+4;
  }



}