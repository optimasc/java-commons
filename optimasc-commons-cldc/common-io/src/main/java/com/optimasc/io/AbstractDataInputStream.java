/*
 *
 * See License.txt for more information on the licensing terms
 * for this source code.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * OPTIMA SC INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.optimasc.io;

import java.io.DataInput;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UTFDataFormatException;

/**
 * An abstract input stream that implements reading raw binary data that
 * includes bit reading and reading data in different endian formats. 
 * 
 * <p>By default, reading data primitives is assumed to be in 
 * big endian format, {@link #setByteOrder(ByteOrder)} can be 
 * used to change the endian of the input data. </p>
 * 
 *  <p>It also assumes that the strings are encoded in modified UTF-8
 *  format, as defined by the Java SDK specification. </p>
 *
 * @author Carl Eric Codere
 */
public abstract class AbstractDataInputStream extends InputStream implements DataInputEx, BitInput
{
  protected int readCount;
  protected ByteOrder byteOrder;

  /** Read buffer */
  protected byte readBuffer[] = new byte[8];

  public AbstractDataInputStream()
  {
    readCount = 0;
    byteOrder = ByteOrder.BIG_ENDIAN;
  }
  
 
  /** Sets the desired byte order for future reads of data values from this stream.
  *
  * @param byteOrder one of {@link ByteOrder#BIG_ENDIAN} or {@link ByteOrder#LITTLE_ENDIAN},
  * indicating whether network byte order or its reverse will be used for future reads.
  * @throws IllegalArgumentException if the parameter is not one of the allowed
  *  parameters.
  */
  public void setByteOrder(ByteOrder byteOrder)
  {
    this.byteOrder = byteOrder;
  }

  /** Returns the byte order with which data values will be read from this stream.
  *
  * @return one of {@link ByteOrder#BIG_ENDIAN} or {@link ByteOrder#LITTLE_ENDIAN}, indicating which byte order is
  * being used.
  */
  public ByteOrder getByteOrder()
  {
    return byteOrder;
  }
  

  public void readFully(byte b[], int off, int len) throws IOException
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

  public byte readByte() throws IOException
  {
    int result = read();
    if (result == -1)
    {
      throw new EOFException();
    }
    return (byte) result;
  }

  /** Reads two input bytes and returns a short value. The
   *  reading uses the actively configured endian as set
   *  by {@link #setByteOrder(ByteOrder)} to read the data
   *  and convert it to java byte ordering.
   *  
   *  @return The signed 16-bit value read.
   *  
   *  @throws EOFException - if this stream reaches the end before reading all the bytes. 
   *  @throws IOException - if an I/O error occurs.
   *  
   */
  public short readShort() throws IOException
  {
    int result;

    readFully(readBuffer, 0, 2);
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      result = ((readBuffer[0] & 0xFF) << 8) | (readBuffer[1] & 0xFF);
    }
    else
    {
      result = ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    }
    return (short) result;
  }

  /** Reads two input bytes and returns it as an integer with
   *  the range between 0 and 65535. The
   *  reading uses the actively configured endian as set
   *  by {@link #setByteOrder(ByteOrder)} to read the data
   *  and convert it to java byte ordering.
   *  
   *  @return The unsigned 16-bit value read.
   *  
   *  @throws EOFException - if this stream reaches the end before reading all the bytes. 
   *  @throws IOException - if an I/O error occurs.
   *  
   */
  public int readUnsignedShort() throws IOException
  {
    int result;

    readFully(readBuffer, 0, 2);
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      result = ((readBuffer[0] & 0xFF) << 8) | (readBuffer[1] & 0xFF);
    }
    else
    {
      result = ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    }
    return (int) (result & 0xFFFF);
  }

  /** Reads four input bytes and returns a long value. The
   *  reading uses the actively configured endian as set
   *  by {@link #setByteOrder(ByteOrder)} to read the data
   *  and convert it to java byte ordering.
   *  
   *  @return The signed 32-bit value read.
   *  
   *  @throws EOFException - if this stream reaches the end before reading all the bytes. 
   *  @throws IOException - if an I/O error occurs.
   *  
   */
  public int readInt() throws IOException
  {
    int result;
    readFully(readBuffer, 0, 4);
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      result = ((readBuffer[0] & 0xFF) << 24) | ((readBuffer[1] & 0xFF) << 16)
          | ((readBuffer[2] & 0xFF) << 8) | (readBuffer[3] & 0xFF);
    }
    else
    {
      result = ((readBuffer[3] & 0xFF) << 24) | ((readBuffer[2] & 0xFF) << 16)
          | ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    }
    return result;
  }

  /** Reads four input bytes and returns it as an integer with
   *  the range between 0 and 4294967295. The
   *  reading uses the actively configured endian as set
   *  by {@link #setByteOrder(ByteOrder)} to read the data
   *  and convert it to java byte ordering.
   *  
   *  @return The unsigned 32-bit value read.
   *  
   *  @throws EOFException - if this stream reaches the end before reading all the bytes. 
   *  @throws IOException - if an I/O error occurs.
   *  
   */
  public long readUnsignedInt() throws IOException
  {
    int result;
    readFully(readBuffer, 0, 4);
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      result = ((readBuffer[0] & 0xFF) << 24) | ((readBuffer[1] & 0xFF) << 16)
          | ((readBuffer[2] & 0xFF) << 8) | (readBuffer[3] & 0xFF);
    }
    else
    {
      result = ((readBuffer[3] & 0xFF) << 24) | ((readBuffer[2] & 0xFF) << 16)
          | ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    }
    return ((long) result & 0xFFFFFFFFL);
  }

  /** Reads eight input bytes and returns a long value. The
   *  reading uses the actively configured endian as set
   *  by {@link #setByteOrder(ByteOrder)} to read the data
   *  and convert it to java byte ordering.
   *  
   *  @return The signed 64-bit value read.
   *  
   *  @throws EOFException - if this stream reaches the end before reading all the bytes. 
   *  @throws IOException - if an I/O error occurs.
   *  
   */
  public long readLong() throws IOException
  {
    long result = 0;
    readFully(readBuffer, 0, 8);
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      for (int j = 0; j <= 7; j++)
      {
        result <<= 8;
        result |= (readBuffer[j] & 0xFF);
      }
    }
    else
    {
      for (int j = 8 - 1; j >= 0; j--)
      {
        result <<= 8;
        result |= (readBuffer[j] & 0xFF);
      }
    }
    return result;
  }

  
  /** Reads one input byte and returns it as an integer with
   *  the range between 0 and 255. The
   *  reading uses the actively configured endian as set
   *  by {@link #setByteOrder(ByteOrder)} to read the data
   *  and convert it to java byte ordering.
   *  
   *  @return The unsigned 8-bit value read.
   *  
   *  @throws EOFException - if this stream reaches the end before reading all the bytes. 
   *  @throws IOException - if an I/O error occurs.
   *  
   */
  public int readUnsignedByte() throws IOException
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

  /** Reads one input byte and returns false if its
   *  value is zero, otherwise it returns true.
   *  
   *  @return The unsigned 8-bit value read.
   *  
   *  @throws EOFException - if this stream reaches the end before reading all the bytes. 
   *  @throws IOException - if an I/O error occurs.
   *  
   */
  public boolean readBoolean() throws IOException
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

  public int skipBytes(int n) throws IOException
  {
    return (int)skip(n);
  }

  public void readFully(byte b[]) throws IOException
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
    if (byteOrder == ByteOrder.BIG_ENDIAN)
    {
      result = ((readBuffer[0] & 0xFF) << 8) | (readBuffer[1] & 0xFF);
    }
    else
    {
      result = ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    }
    return (char) (result & 0xFFFF);
  }
  
  
  public String readString() throws IOException
  {
    return readUTF();
  }


  /*-************************ InputStream methods *************************/
  public abstract void close() throws IOException;
  public abstract void mark(int readlimit); 
  public abstract boolean markSupported(); 
  
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
  public abstract int read(byte[] b, int off, int len) throws IOException;
  /** Reads a single byte from the stream and returns it as an integer
   *  between 0 and 255. If the end of the stream is reached, -1 is returned.
   *
   *  The bit offset within the stream is reset to zero before the read occurs.
   *
   * @return a byte value from the stream, as an int, or -1 to indicate EOF.
   * @throws IOException
   */
  public abstract int read() throws IOException;
  public abstract void reset() throws IOException; 
  public abstract long skip(long n) throws IOException;
  public abstract int available() throws IOException;

  public int read(byte[] b) throws IOException
  {
    if (b.length == 0)
      return 0;
    return read(b,0,b.length);
  }
  
}
