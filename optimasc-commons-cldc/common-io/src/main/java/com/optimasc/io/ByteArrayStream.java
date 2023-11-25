package com.optimasc.io;

import java.io.IOException;

/** Represents a seekable, readable and writable stream
 *  composed of a byte array. It supports either a fixed
 *  byte array, or a growable byte array.  
 * 
 * @author Carl Eric Codere
 *
 */
public class ByteArrayStream extends AbstractDataStream
{
  protected byte[] buf;
  protected long streamPos;
  protected long markPos;
  protected boolean canResize;
  
  public ByteArrayStream(byte[] buffer)
  {
    super();
    canResize = false;
    this.buf = buffer;
    markPos = -1;
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
    markPos = -1;
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
    return buf.length;
  }



  public void seek(long newPosition) throws IOException
  {
    streamPos = newPosition;
  }


  public int read() throws IOException
  {
    if ((streamPos+1) > buf.length)
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
    if (streamPos > buf.length)
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
    return buf.length;
  }


  public synchronized void mark(int readlimit)
  {
    markPos = streamPos;
  }


  public synchronized void reset() throws IOException
  {
    if (markPos == -1)
    {
      throw new IOException("Stream is not marked.");
    }
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


    
}
