package com.optimasc.io;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;

/** A filtered input stream that is similar to {@link java.io.DataInputStream}
 *  but which can be overriden (some of the methods are non-final). 
 * 
 * @author Carl Eric codere
 *
 */
public class DataInputStream extends InputStream implements DataInput
{
  protected InputStream in;
  protected byte[] readBuffer;
  protected int readCount;
  
  /** Creates this stream using the specified 
   *  input stream.
   * 
   * @param in [in] The underlying input stream,
   */
  public DataInputStream(InputStream in)
  {
    super();
    this.in = in;
    this.readCount = 0;
    this.readBuffer = new byte[8];
  }

  public long skip(long n) throws IOException
  {
    return in.skip(n);
  }

  public int available() throws IOException
  {
    return in.available();
  }

  public synchronized void mark(int readlimit)
  {
    in.mark(readlimit);
  }

  public synchronized void reset() throws IOException
  {
    in.reset();
  }

  public boolean markSupported()
  {
    return in.markSupported();
  }

  public int read(byte[] b) throws IOException
  {
    int value = in.read(b);
    readCount +=  value;
    return value;
  }

  public final int read(byte[] buffer, int off, int len) throws IOException
  {
    int read = in.read(buffer, off, len);
    readCount = readCount + read;
    return read;
  }

  public void close() throws IOException
  {
    in.close();
  }

  public final int read() throws IOException
  {
    int value = in.read();
    readCount++;
    return value;
  }
  
  
  public final void readFully(byte b[], int off, int len) throws IOException
  {
    if (len < 0)
    {
      throw new IndexOutOfBoundsException();
    }
    int n = 0;
    while (n < len)
    {
      int count = read(b, off + n, len - n);
      if (count < 0)
      {
        throw new EOFException();
      }
      n += count;
    }
  }

  /**
   * Reads from the stream <code>in</code> a representation of a Unicode
   * character string encoded in <a
   * href="DataInput.html#modified-utf-8">modified UTF-8</a> format; this string
   * of characters is then returned as a <code>String</code>. The details of the
   * modified UTF-8 representation are exactly the same as for the
   * <code>readUTF</code> method of <code>DataInput</code>.
   *
   * @param in
   *          a data input stream.
   * @return a Unicode string.
   * @exception EOFException
   *              if the input stream reaches the end before all the bytes.
   * @exception IOException
   *              if an I/O error occurs.
   * @exception UTFDataFormatException
   *              if the bytes do not represent a valid modified UTF-8 encoding
   *              of a Unicode string.
   * @see java.io.DataInputStream#readUnsignedShort()
   */
  public static String readUTF(DataInput in) throws IOException
  {
    int utflen = in.readUnsignedShort();
    byte[] bytearr = null;
    char[] chararr = null;
    bytearr = new byte[utflen];
    chararr = new char[utflen];

    int c, char2, char3;
    int count = 0;
    int chararr_count = 0;

    in.readFully(bytearr, 0, utflen);

    while (count < utflen)
    {
      c = (int) bytearr[count] & 0xff;
      if (c > 127)
      {
        break;
      }
      count++;
      chararr[chararr_count++] = (char) c;
    }

    while (count < utflen)
    {
      c = (int) bytearr[count] & 0xff;
      switch (c >> 4)
      {
        case 0:
        case 1:
        case 2:
        case 3:
        case 4:
        case 5:
        case 6:
        case 7:
          /* 0xxxxxxx*/
          count++;
          chararr[chararr_count++] = (char) c;
          break;
        case 12:
        case 13:
          /* 110x xxxx   10xx xxxx*/
          count += 2;
          if (count > utflen)
          {
            throw new UTFDataFormatException("malformed input: partial character at end");
          }
          char2 = (int) bytearr[count - 1];
          if ((char2 & 0xC0) != 0x80)
          {
            throw new UTFDataFormatException("malformed input around byte " + count);
          }
          chararr[chararr_count++] = (char) (((c & 0x1F) << 6) | (char2 & 0x3F));
          break;
        case 14:
          /* 1110 xxxx  10xx xxxx  10xx xxxx */
          count += 3;
          if (count > utflen)
          {
            throw new UTFDataFormatException("malformed input: partial character at end");
          }
          char2 = (int) bytearr[count - 2];
          char3 = (int) bytearr[count - 1];
          if (((char2 & 0xC0) != 0x80) || ((char3 & 0xC0) != 0x80))
          {
            throw new UTFDataFormatException("malformed input around byte " + (count - 1));
          }
          chararr[chararr_count++] = (char) (((c & 0x0F) << 12) | ((char2 & 0x3F) << 6) | ((char3 & 0x3F) << 0));
          break;
        default:
          /* 10xx xxxx,  1111 xxxx */
          throw new UTFDataFormatException("malformed input around byte " + count);
      }
    }
    // The number of chars produced may be less than utflen
    return new String(chararr, 0, chararr_count);
  }

  public final byte readByte() throws IOException
  {
    int result = read();
    if (result == -1)
    {
      throw new EOFException();
    }
    return (byte) result;
  }

  public short readShort() throws IOException
  {
    int result;

    readFully(readBuffer, 0, 2);
    result = ((readBuffer[0] & 0xFF) << 8) | (readBuffer[1] & 0xFF);
    return (short) result;
  }

  public int readUnsignedShort() throws IOException
  {
    int result;

    readFully(readBuffer, 0, 2);
      result = ((readBuffer[0] & 0xFF) << 8) | (readBuffer[1] & 0xFF);
    return (int) (result & 0xFFFF);
  }

  public int readInt() throws IOException
  {
    int result;
    readFully(readBuffer, 0, 4);
      result = ((readBuffer[0] & 0xFF) << 24) | ((readBuffer[1] & 0xFF) << 16)
          | ((readBuffer[2] & 0xFF) << 8) | (readBuffer[3] & 0xFF);
    return result;
  }

  public long readUnsignedInt() throws IOException
  {
    int result;
    readFully(readBuffer, 0, 4);
      result = ((readBuffer[0] & 0xFF) << 24) | ((readBuffer[1] & 0xFF) << 16)
          | ((readBuffer[2] & 0xFF) << 8) | (readBuffer[3] & 0xFF);
    return ((long) result & 0xFFFFFFFFL);
  }

  public long readLong() throws IOException
  {
    long result = 0;
    readFully(readBuffer, 0, 8);
      for (int j = 0; j <= 7; j++)
      {
        result <<= 8;
        result |= (readBuffer[j] & 0xFF);
      }
    return result;
  }

  public final int readUnsignedByte() throws IOException
  {
    int result = read();
    if (result == -1)
    {
      throw new EOFException();
    }
    return (int) (result & 0xFF);
  }

  public String readUTF() throws IOException
  {
    return readUTF(this);
  }

  public final boolean readBoolean() throws IOException
  {
    int result = read();
    if (result == -1)
    {
      throw new EOFException();
    }
    if (result == 0)
      return false;
    return true;
  }

  public final int skipBytes(int n) throws IOException
  {
    return (int)skip(n);
  }

  public final void readFully(byte b[]) throws IOException
  {
    readFully(b, 0, b.length);
  }

  public int getCount()
  {
    return (int) readCount;
  }

  public int resetCount()
  {
    int returnValue = readCount;
    readCount = 0;
    return returnValue;
  }

  public float readFloat() throws IOException
  {
    return Float.intBitsToFloat(readInt());
  }

  public double readDouble() throws IOException
  {
    return Double.longBitsToDouble(readLong());
  }

  public String readLine() throws IOException
  {
    return LineReader.readISOLine(this);
  }

  public char readChar() throws IOException
  {
    int result;
    readFully(readBuffer, 0, 2);
    result = ((readBuffer[0] & 0xFF) << 8) | (readBuffer[1] & 0xFF);
    return (char) (result & 0xFFFF);
  }
  
  
  

}
