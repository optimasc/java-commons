package com.optimasc.io;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UTFDataFormatException;

public abstract class SeekableDataStream extends SeekableDataInputStream implements DataOutput, BitOutput, Seekable
{
  /** Write buffer */
  protected byte writeBuffer[] = new byte[8];
  

  public abstract void write(int b) throws IOException;
  public abstract void close() throws IOException;
  public abstract void flush() throws IOException;
  public abstract  void write(byte[] b, int off, int len) throws IOException;
  public abstract void writeTo(OutputStream out) throws IOException;

  public void writeBoolean(boolean v) throws IOException
  {
    if (v == true)
      write(1);
    else
      write(0);
  }

  public void writeShort(int v) throws IOException
  {
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      writeBuffer[0] = (byte)((v >> 8) & 0xff);
      writeBuffer[1] = (byte)(v & 0xff);
      
    } else
    {
      writeBuffer[0] = (byte)(v & 0xff);
      writeBuffer[1] = (byte)((v >> 8) & 0xff);
    }
    write(writeBuffer,0,2);
  }

  public void writeChar(int v) throws IOException
  {
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      writeBuffer[0] = (byte)((v >> 8) & 0xff);
      writeBuffer[1] = (byte)(v & 0xff);
      
    } else
    {
      writeBuffer[0] = (byte)(v & 0xff);
      writeBuffer[1] = (byte)((v >> 8) & 0xff);
    }
    write(writeBuffer,0,2);
  }

  public void writeInt(int v) throws IOException
  {
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      writeBuffer[0] = (byte)((v >> 24) & 0xff);
      writeBuffer[1] = (byte)((v >> 16) & 0xff);
      writeBuffer[2] = (byte)((v >> 8) & 0xff);
      writeBuffer[3] =  (byte)(v & 0xff);
    } else
    {
      writeBuffer[0] = (byte)(v & 0xff);
      writeBuffer[1] = (byte)((v >> 8) & 0xff);
      writeBuffer[2] = (byte)((v >> 16) & 0xff);
      writeBuffer[3] =  (byte)((v >> 24) & 0xff);
    }
    write(writeBuffer,0,4);
  }

  public void writeLong(long v) throws IOException
  {
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      writeBuffer[0] = (byte)((v >> 56) & 0xff);
      writeBuffer[1] = (byte)((v >> 48) & 0xff);
      writeBuffer[2] = (byte)((v >> 40) & 0xff);
      writeBuffer[3] =  (byte)((v >> 32) & 0xff);
      writeBuffer[4] = (byte)((v >> 24) & 0xff);
      writeBuffer[5] =  (byte)((v >> 16) & 0xff);
      writeBuffer[6] = (byte)((v >> 8) & 0xff);
      writeBuffer[7] = (byte)(v & 0xff);
    } else
    {
      writeBuffer[0] = (byte)(v & 0xff);
      writeBuffer[1] = (byte)((v >> 8) & 0xff);
      writeBuffer[2] = (byte)((v >> 16) & 0xff);  
      writeBuffer[3] = (byte)((v >> 24) & 0xff);
      writeBuffer[4] = (byte)((v >> 32) & 0xff);
      writeBuffer[5] = (byte)((v >> 40) & 0xff);
      writeBuffer[6] = (byte)((v >> 48) & 0xff);
      writeBuffer[7] = (byte)((v >> 56) & 0xff);
    }
    write(writeBuffer,0,8);
  }

  public void writeFloat(float v) throws IOException
  {
    int value = Float.floatToIntBits(v);
    writeInt(value);
  }

  public void writeDouble(double v) throws IOException
  {
    long value = Double.doubleToLongBits(v);
    writeLong(value);
  }

  public void writeBytes(String s) throws IOException
  {
    for (int i = 0; i < s.length(); i++)
    {
      write(s.charAt(i) & 0xFF);
    }
  }

  public void writeChars(String s) throws IOException
  {
    char[] buffer = s.toCharArray();
    for (int i = 0; i < buffer.length; i++)
    {
      writeChar(buffer[i]);
    }
  }

  public void writeUTF(String str) throws IOException
  {
    byte[] buffer = str.getBytes("UTF-8");
    if (buffer.length > 65535)
    {
      throw new UTFDataFormatException("Length of string exceeds 64K");
    }
    writeShort(buffer.length & 0xFFFF);
    write(buffer,0,buffer.length);
  }
  
  public void writeByte(int v) throws IOException
  {
    write(v);
  }
  public void writeBit(int bit) throws IOException
  {
    writeBits((1 & bit), 1);
  }

  public void writeBits(long bits, int numBits) throws IOException
  {
    throw new IllegalArgumentException("Unsupported operation");
  }


}
