package com.optimasc.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

public abstract class SeekableDataInputStream extends AbstractDataInputStream implements Seekable, BitInput
{
  protected long markPos;
  protected long currentPos;
  protected long length;
  protected int bitOffset = 8;
  protected int bitBuffer;
  
  
  public SeekableDataInputStream()
  {
    super();
  }
  
  
  public void reset() throws IOException
  {
    seek(markPos);
  }
  

  public void mark(int readlimit)
  {
    try
    {
      markPos =  getStreamPosition();
    } catch (IOException e)
    {
      markPos = 0;
    }
  }

  public boolean markSupported()
  {
    return true;
  }

  public long skip(long n) throws IOException
  {
    long currentPos = getStreamPosition();
    long currentLength = length();
    if ((currentPos + n) > currentLength)
    {
      n = currentLength - currentPos;
    }
    if (n <= 0)
    {
      return 0;
    }
    seek(currentPos + n);
    return n;
  }
  
  public long length() throws IOException
  {
    return length;
  }


  public int available() throws IOException
  {
    return (int) (length - currentPos);
  }
  
  public int getBitOffset() throws IOException
  {
    return bitOffset;
  }

  public int readBit() throws IOException
  {
    int offset = bitOffset;
    int currentByte = read();

    if (currentByte == -1) {
      throw new EOFException();
    }

    offset = (offset + 1) & 7;

    if (offset != 0) {
      currentByte >>= 8 - offset;
      seek(getStreamPosition() - 1);
    }

    bitOffset = offset;
    return currentByte & 1;
  }

  /** Sets the bit offset to an integer between 0 and 7, inclusive.
  *
  * @param offset the desired offset, as an int between 0 and 7, inclusive.
  * @throws IllegalArgumentException if bitOffset is not between 0 and 7, inclusive.
  */
  public void setBitOffset(int offset)
  {
    if ((offset < 0) || (offset > 7))
      throw new IllegalArgumentException();
    bitOffset = offset;
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
  
  public long readBits(int numBits) throws IOException
  {

    if ((numBits < 0) || (numBits > 64)) {
      throw new IllegalArgumentException("number of its must be between 0 and 64");
    }

    long res = 0;

    for (int i = 0; i < numBits; i++) {
      res <<= 1;
      res |= readBit();
    }

    return res;
  }
  
  
  public abstract void close() throws IOException;
  public abstract int read(byte[] b, int off, int len) throws IOException;
  public abstract int read() throws IOException;
  public abstract long getStreamPosition() throws IOException;
  public abstract void seek(long pos) throws IOException;
}
