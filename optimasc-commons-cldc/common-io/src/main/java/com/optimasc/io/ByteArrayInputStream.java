package com.optimasc.io;

import java.io.IOException;

/** Implementation of an Input Stream that that directly works on a byte array buffer. 
 *  This is similar to {@link java.io.ByteArrayInputStram} with the additional 
 *  functionality afforded by the {@link com.optimasc.io.AbstractDataInputStream} 
 *  class as well as being not synchronized and hence <em>not</em> thread-safe.
 *
 * 
 * @author Carl Eric Codere
 *
 */
public class ByteArrayInputStream extends SeekableDataInputStream
{
  protected byte[] buf;
  protected final int offset;
  
  
  /** Create a stream from the byte array as input. 
   * 
   * @param buffer [in] The byte array that will be represented as a stream.
   */
  public ByteArrayInputStream(byte[] buffer)
  {
    super();
    this.buf = buffer;
    this.length = buffer.length;
    this.offset = 0;
  }
  
  /** Creates a virtual stream that is composed on the part of 
   *  the byte array used as input. The stream starts at position
   *  <code>off</code> in the array and has a length of <code>len</code>.
   * 
   * @param buffer [in] The buffer used to read the data.
   * @param off [in] Offset in buffer where the stream should start
   * @param len [in] Length of the stream in bytes, starting from <code>off</code> 
   */
  public ByteArrayInputStream(byte[] buffer, int off, int len) throws IOException
  {
    super();
    this.buf = buffer;
    this.length = len;
    this.offset = off;
  }
  

  public final long length()
  {
    return length;
  }

  public final void seek(long newPosition) throws IOException
  {
    currentPos = newPosition;
  }


  public final int read() throws IOException
  {
    if ((currentPos+1) > length)
    {
      return -1;
    }
    readCount++;
    return buf[ offset+(int)currentPos++] & 0xff;
  }

  public final int read(byte[] b, int off, int len) throws IOException
  {
    if ((off | len) < 0 || off > b.length || b.length - off < len)
    {
      throw new IndexOutOfBoundsException("Invalid parameters");
    }
    if (currentPos >= length)
    {
      return -1;
    }
    if (currentPos + len > length) 
    {
      len = (int) (length - currentPos);
    }
    if (len <= 0) 
    {
      return 0;
    }
    System.arraycopy(buf, (int) currentPos+offset, b, off, len);
    currentPos = currentPos + len;
    readCount += len;
    return len;
  }

  public final long getStreamPosition() throws IOException
  {
    return currentPos;
  }

  public final int available() throws IOException
  {
    if (buf == null)
    {
      throw new IOException("Stream is closed.");
    }
    return (int) (length - currentPos);
  }


  public void close() throws IOException
  {
    buf = null;
  }


  public int read(byte[] b) throws IOException
  {
    return read(b,0,b.length);
  }

  public boolean isCached()
  {
    return false;
  }
  
  
  
}
