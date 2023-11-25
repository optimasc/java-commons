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
 * An abstract filter input stream that implements reading raw binary data that
 * including bit reading and reading data in different endian formats.
 * 
 * <p>By default, reading data primitives is assumed to be in 
 * big endian format, {@link #setByteOrder(ByteOrder)} can be 
 * used to change the endian of the input data. </p> 
 *
 * @author Carl Eric Codere
 */
public abstract class AbstractDataInputStream extends InputStream implements DataInputEx, BitInput
{
  protected int readCount;
  protected ByteOrder byteOrder;
  protected int bitOffset = 8;
  protected int bitBuffer;

  /** Read buffer */
  protected byte readBuffer[] = new byte[8];

  public AbstractDataInputStream()
  {
    readCount = 0;
    byteOrder = ByteOrder.BIG_ENDIAN;
  }
  
  
  public void setByteOrder(ByteOrder byteOrder)
  {
    this.byteOrder = byteOrder;
  }

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

  public long readBits(int numBits) throws IOException
  {
    if ((numBits < 0) || (numBits > 64))
    {
      throw new IllegalArgumentException("number of its must be between 0 and 64");
    }
    if (byteOrder == ByteOrder.LITTLE_ENDIAN)
    {
      int value = 0;
      for (int i = 0; i < numBits; i++)
      {
        /* Bit offset is from 0 to 7 */
        if (bitOffset == 8)
        {
          bitBuffer = read();

          if (bitBuffer == -1)
            throw new EOFException();

          bitOffset = 0;
        }
        int bit = (bitBuffer >>> bitOffset);
        bitOffset++;
        value |= ((bit & 0x01) << i);
      }
      return value;
    }
    else
    {
      int value = 0;
      for (int i = numBits - 1; i >= 0; i--)
      {
        /* Bit offset is from 0 to 7 */
        if (bitOffset == 8)
        {
          bitBuffer = read();

          if (bitBuffer == -1)
            throw new EOFException();

          bitOffset = 0;
        }
        int bit = (bitBuffer >>> bitOffset);
        bitOffset++;
        value |= ((bit & 0x01) << i);
      }
      return value;
    }
  }

  public void skipBits(int numBits) throws IOException
  {
    if ((numBits < 0) || (numBits > 32))
    {
      throw new IllegalArgumentException("number of its must be between 0 and 64");
    }
    if (byteOrder == ByteOrder.LITTLE_ENDIAN)
    {
      for (int i = 0; i < numBits; i++)
      {
        /* Bit offset is from 0 to 7 */
        if (bitOffset == 8)
        {
          bitBuffer = read();

          if (bitBuffer == -1)
            throw new EOFException();

          bitOffset = 0;
        }
        bitOffset++;
      }
    }
    else
    {
      for (int i = numBits - 1; i >= 0; i--)
      {
        /* Bit offset is from 0 to 7 */
        if (bitOffset == 8)
        {
          bitBuffer = read();
          if (bitBuffer == -1)
            throw new EOFException();

          bitOffset = 0;
        }
        bitOffset++;
      }
    }
  }

  public int peekBits(int numBits) throws IOException
  {
    int localBitBuffer = bitBuffer;
    int localBitOffset = bitOffset;
    if (markSupported() ==false)
    {
      throw new IOException("mark() is not supported so bit peeking is not supported.");
    }
    mark((numBits / 8)+1);
    int value = 0;
    if ((numBits < 0) || (numBits > 32))
    {
      throw new IllegalArgumentException("number of its must be between 0 and 64");
    }
    if (byteOrder == ByteOrder.LITTLE_ENDIAN)
    {
      for (int i = 0; i < numBits; i++)
      {
        /* Bit offset is from 0 to 7 */
        if (localBitOffset == 8)
        {
          localBitBuffer = read();

          if (localBitBuffer == -1)
            throw new EOFException();

          localBitOffset = 0;
        }
        int bit = (localBitBuffer >>> localBitOffset);
        localBitOffset++;
        value |= ((bit & 0x01) << i);
      }
    }
    else
    {
      for (int i = numBits - 1; i >= 0; i--)
      {
        /* Bit offset is from 0 to 7 */
        if (localBitOffset == 8)
        {
          localBitBuffer = read();

          if (localBitBuffer == -1)
            throw new EOFException();

          localBitOffset = 0;
        }
        int bit = (localBitBuffer >>> localBitOffset);
        localBitOffset++;
        value |= ((bit & 0x01) << i);
      }
    }
    reset();
    return value;
  }

  public int getBitOffset() throws IOException
  {
    return bitOffset;
  }

  public int readBit() throws IOException
  {
    /* Bit offset is from 0 to 7 */
    if (bitOffset == 8)
    {
      bitBuffer = read();

      if (bitBuffer == -1)
        throw new EOFException();

      bitOffset = 0;
    }
    int bit = (bitBuffer >>> bitOffset);
    bitOffset++;
    return bit & 0x01;
  }

  public void setBitOffset(int offset)
  {
    if ((offset < 0) || (offset > 7))
      throw new IllegalArgumentException();
    bitOffset = offset;
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
  
  /*-************************ InputStream methods *************************/
  public abstract void close() throws IOException;
  public abstract void mark(int readlimit); 
  public abstract boolean markSupported(); 
  public abstract int read(byte[] b) throws IOException; 
  public abstract int read(byte[] b, int off, int len) throws IOException; 
  public abstract void reset() throws IOException; 
  public abstract long skip(long n) throws IOException; 

}
