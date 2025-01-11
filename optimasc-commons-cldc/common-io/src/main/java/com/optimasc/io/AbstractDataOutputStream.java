package com.optimasc.io;

import java.io.DataOutput;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UTFDataFormatException;

/**
 * Default abstract implementation of stream that supports bit writing
 * and writing primitive types to series of bytes in both little
 * and big endian formats. 
 * 
 * A derived class should minimally implement the abstract methods and override
 * the {@link #write(byte[], int, int)} method for best performance. 
 * 
 * <p>By default, write data primitives is assumed to be in 
 * big endian format, {@link #setByteOrder(ByteOrder)} can be 
 * used to change the endian of the output data format. </p> 
 *
 * 
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class AbstractDataOutputStream extends OutputStream
    implements DataOutput, BitOutput
{
  protected ByteOrder byteOrder;
  protected int bitOffset = 8;
  protected int bitBuffer;
  
  /** Write buffer */
  protected byte writeBuffer[] = new byte[8];
  
  
  

  public AbstractDataOutputStream()
  {
    super();
    byteOrder = ByteOrder.BIG_ENDIAN;
  }
  
  
  
  
  public abstract void write(int b) throws IOException;
  public abstract void close() throws IOException;
  public abstract void flush() throws IOException;
  public abstract  void write(byte[] b, int off, int len) throws IOException;
  
  public void write(byte[] b) throws IOException
  {
    write(b, 0, b.length); 
  }
  

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
  
  protected long countUTFBytes(String str) 
  {
    int utfCount = 0, length = str.length();
    for (int i = 0; i < length; i++) {
        int charValue = str.charAt(i);
        if (charValue > 0 && charValue <= 127) {
            utfCount++;
        } else if (charValue <= 2047) {
            utfCount += 2;
        } else {
            utfCount += 3;
        }
    }
    return utfCount;
}
  

  /**
   * Writes the specified encoded in {@link DataInput modified UTF-8} to this
   * stream.
   * 
   * @param str
   *            the string to write to the target stream encoded in
   *            {@link DataInput modified UTF-8}.
   * @throws IOException
   *             if an error occurs while writing to the target stream.
   * @throws UTFDataFormatException
   *             if the encoded string is longer than 65535 bytes.
   * @see DataInputStream#readUTF()
   */
  public void writeUTF(String str) throws IOException
  {
    long length = countUTFBytes(str);
    if (length > 65535)
    {
      throw new UTFDataFormatException("Length of string exceeds 64K");
    }
    byte[] buffer = new byte[(int)length];
    int offset = 0;
    writeShort((short)length);
    offset = writeUTFBytesToBuffer(str, (int) length, buffer, offset);
    write(buffer,0,buffer.length);
  }
  
  

  protected int writeUTFBytesToBuffer(String str, long count,
                            byte[] buffer, int offset) throws IOException {
      int length = str.length();
      for (int i = 0; i < length; i++) {
          int charValue = str.charAt(i);
          if (charValue > 0 && charValue <= 127) {
              buffer[offset++] = (byte) charValue;
          } else if (charValue <= 2047) {
              buffer[offset++] = (byte) (0xc0 | (0x1f & (charValue >> 6)));
              buffer[offset++] = (byte) (0x80 | (0x3f & charValue));
          } else {
              buffer[offset++] = (byte) (0xe0 | (0x0f & (charValue >> 12)));
              buffer[offset++] = (byte) (0x80 | (0x3f & (charValue >> 6)));
              buffer[offset++] = (byte) (0x80 | (0x3f & charValue));
           }
      }
      return offset;
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

  /**  Sets the desired byte order for future write of data to this stream.
   * 
   * @param byteOrder [in] One of ByteOrder.BIG_ENDIAN or ByteOrder.LITTLE_ENDIAN, indicating whether 
   *   network byte order or its reverse will be used for future writes.
   */
  public void setByteOrder(ByteOrder byteOrder)
  {
    this.byteOrder = byteOrder;
  }

  /** Returns the byte order with which data values will be written to this stream.
   * 
   * @return One of ByteOrder.BIG_ENDIAN or ByteOrder.LITTLE_ENDIAN, indicating the byte order 
   *   being used.
   */
  public ByteOrder getByteOrder()
  {
    return byteOrder;
  }
  
}
