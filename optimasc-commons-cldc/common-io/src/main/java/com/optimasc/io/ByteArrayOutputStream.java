package com.optimasc.io;

import java.io.IOException;
import java.io.OutputStream;

/** Output Stream that directly writes to a byte array buffer that can
 *  dynamically grow in size and which is seekable.
 *  This is similar to {@link java.io.ByteArrayOutputStream} with the additional 
 *  functionality afforded by the {@link com.optimasc.io.AbstractDataOutputStream} 
 *  class as well as not being synchronized and hence <em>not</em> thread-safe.
 *
 * 
 * @author Carl Eric Codere
 *
 */
public class ByteArrayOutputStream extends SeekableDataOutputStream
{
  protected byte[] buf;
  protected int count;
  protected long streamPos;
  /** Depending on the type of constructor called, indicates
   *  if the underlying buffer can be resized or not.
   */
  protected boolean canResize;

  public ByteArrayOutputStream(byte[] buffer)
  {
    super();
    canResize = false;
    buf = buffer;
  }
  
  public ByteArrayOutputStream(int size)
  {
    super();
    canResize = true;
    if (size < 0)
    {
      throw new IllegalArgumentException("Negative size");
    }
    buf = new byte[size];
  }
  
  
  public void write(int b) throws IOException
  {
    if ((streamPos+1) > buf.length)
    {
      if (canResize)
      {
        int newcount = count + 1;
        byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
        System.arraycopy(buf, 0, newbuf, 0, count);
        buf = newbuf;
        count++;
      } else
      {
        throw new IOException("Trying to write past end of buffer");
      }
    }
    buf[(int) streamPos++] = (byte) b;
    if (streamPos > count)
    {
      count = (int) streamPos;
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
        int newcount = count + len;
        byte newbuf[] = new byte[Math.max(buf.length << 1, newcount)];
        System.arraycopy(buf, 0, newbuf, 0, count);
        count = count + len;
        buf = newbuf;
      } else
      {
        throw new IOException("Trying to write past end of buffer");
      }
    }
    System.arraycopy(b, off, buf, (int)streamPos, len);
    streamPos = streamPos + len;
    if (streamPos > count)
    {
      count = (int) streamPos;
    }
  }

  public int read() throws IOException
  {
    if (streamPos > buf.length)
    {
      return -1;
    }
    return buf[(int) streamPos++];
  }

  public int read(byte[] b, int off, int len) throws IOException
  {
    if (streamPos > buf.length)
    {
      return -1;
    }
    System.arraycopy(buf, (int) streamPos, b, off, len);
    streamPos = streamPos + len;
    return len;
  }

  public long length()
  {
    return count;
  }

  public void seek(long pos) throws IOException
  {
    streamPos = pos;
  }

  public long getStreamPosition() throws IOException
  {
    return streamPos;
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
    out.write(buf,0,count);
  }

  public boolean isCached()
  {
    return false;
  }

  public void close() throws IOException
  {
  }

  public void flush() throws IOException
  {
  }

}
