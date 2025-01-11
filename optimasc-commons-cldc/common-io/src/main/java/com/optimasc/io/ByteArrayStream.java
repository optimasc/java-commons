package com.optimasc.io;

import java.io.IOException;
import java.io.OutputStream;

/** Represents a seekable, readable and writable stream
 *  composed of a byte array. It supports either a fixed
 *  byte array, or an internally allocated byte array that
 *  can grow.   
 * 
 * @author Carl Eric Codere
 *
 */
public class ByteArrayStream extends SeekableDataStream
{
  protected byte[] buf;
  protected int streamPos;
  protected long markPos;
  protected boolean canResize;
  protected int length;
  
  /** Instantiates a fixed length byte array stream based on the 
   *  specified buffer.
   * 
   * @param buffer [in] The byte array backing this stream.
   */
  public ByteArrayStream(byte[] buffer)
  {
    super();
    canResize = false;
    this.buf = buffer;
    markPos = 0;
  }
  
  /** Instantiates a byte array stream with
   *  a specified initial size that can grow
   *  as required. 
   * 
   * @param size [in] The initial size in bytes
   *   of the byte array. A value of zero is allowed
   */
  public ByteArrayStream(int size)
  {
    super();
    canResize = true;
    if (size < 0)
    {
      throw new IllegalArgumentException("Negative size");
    }
    buf = new byte[size];
    markPos = 0;
  }
  
  
  public void write(byte[] b) throws IOException
  {
    write(b,0,b.length);
  }

  
  public void write(int b) throws IOException
  {
    if ((streamPos+1) > buf.length)
    {
      if (canResize)
      {
        int newcount = length + 1;
        byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
        System.arraycopy(buf, 0, newbuf, 0, length);
        buf = newbuf;
        length++;
      } else
      {
        throw new IOException("Trying to write past end of buffer");
      }
    }
    buf[(int) streamPos++] = (byte) b;
    if (streamPos > length)
    {
      length = (int) streamPos;
    }
  }

  public void write(byte[] b, int off, int len) throws IOException
  {
    if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) > b.length)
        || ((off + len) < 0))
    {
      throw new IndexOutOfBoundsException();
    } else if (len == 0)
    {
      return;
    }
    if ((streamPos+len) > buf.length)
    {
      if (canResize)
      {
        int newcount = length + len;
        byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
        System.arraycopy(buf, 0, newbuf, 0, length);
        length = len-streamPos;
        buf = newbuf;
      } else
      {
        throw new IOException("Trying to write past end of buffer");
      }
    }
    System.arraycopy(b, off, buf, (int)streamPos, len);
    streamPos = streamPos + len;
    if (streamPos > length)
    {
      length = (int) streamPos;
    }
  }


  public long length()
  {
    return length;
  }



  public void seek(long newPosition) throws IOException
  {
    if (newPosition > Integer.MAX_VALUE)
    {
      throw new IOException(ByteArrayStream.class.getName()+" seek position is out of range.");
    }
    streamPos = (int)newPosition;
  }


  public int read() throws IOException
  {
    if ((streamPos+1) > length)
    {
      return -1;
    }
    readCount++;
    return buf[(int) streamPos++] & 0xff;
  }

  public int read(byte[] b, int off, int len) throws IOException
  {
    if ((off | len) < 0 || off > b.length || b.length - off < len)
    {
      throw new IndexOutOfBoundsException("Invalid parameters");
    }
    if (streamPos > length)
    {
      return -1;
    }
    System.arraycopy(buf, (int) streamPos, b, off, len);
    streamPos = streamPos + len;
    readCount += len;
    return len;
  }


  public long getStreamPosition() throws IOException
  {
    return streamPos;
  }


  public long skip(long n) throws IOException
  {
    seek(streamPos + n);
    return n;
  }


  public int available() throws IOException
  {
    return (int) (length-streamPos);
  }


  public void mark(int readlimit)
  {
    markPos = streamPos;
  }


  public void reset() throws IOException
  {
    seek(markPos);
  }


  public boolean markSupported()
  {
    return true;
  }


  public void close() throws IOException
  {
    buf = null;
  }

  /** This method does nothing, as there is 
   *  nothing to flush.
   * 
   */
  public void flush() throws IOException
  {
  }

  public int read(byte[] b) throws IOException
  {
    return read(b,0,b.length);
  }

  /**
   * Takes the contents of this stream and writes it to the output stream
   * {@code out}.
   *
   * @param out
   *            an OutputStream on which to write the contents of this stream.
   * @throws IOException
   *             if an error occurs while writing to {@code out}.
   */
  public void writeTo(OutputStream out) throws IOException
  {
    out.write(buf,0,length);
  }

  /** Always returns false, since there 
   *  is no caching for byte array streams.
   *  
   *  @return Always returns false;
   * 
   */
  public boolean isCached()
  {
    return false;
  }

    
}
