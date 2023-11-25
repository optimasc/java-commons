package com.optimasc.io;

import java.io.IOException;
import java.io.OutputStream;

/** Represents a seekable, readable and writable stream
 *  composed of a byte array. It supports either a fixed
 *  byte array, or a growable byte array.  
 * 
 * @author Carl Eric Codere
 *
 */
public class ByteArrayStream extends SeekableDataStream
{
  protected byte[] buf;
  protected long streamPos;
  protected long markPos;
  protected boolean canResize;
  protected int length;
  
  public ByteArrayStream(byte[] buffer)
  {
    super();
    canResize = false;
    this.buf = buffer;
    markPos = 0;
  }
  
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
  
  
  public void write(int b) throws IOException
  {
    buf[(int) streamPos++] = (byte)b;
  }


  public void write(byte[] b) throws IOException
  {
    write(b,0,b.length);
  }

  public void write(byte[] b, int off, int len) throws IOException
  {
  }


  public long length()
  {
    return length;
  }



  public void seek(long newPosition) throws IOException
  {
    streamPos = newPosition;
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

  public boolean isCached()
  {
    return false;
  }

    
}
