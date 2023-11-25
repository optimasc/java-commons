package com.optimasc.io;

import java.io.IOException;

/** Implementation of an Input Stream that that directly works on a byte array buffer. 
 *  This is similar to {@link java.io.ByteArrayInputStram} with the additional 
 *  functionality afforded by the {@link com.optimasc.io.AbstractDataInputStream} 
 *  class.
 *
 * 
 * @author Carl Eric Codere
 *
 */
public class ByteArrayInputStream extends AbstractDataInputStream implements Seekable
{
  protected byte[] buf;
  protected final int length;
  protected final int offset;
  protected long streamPos;
  protected long markPos;
  
  
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
    // Initial bookmark is set to position 0
    markPos = 0;
  }
  
  /** Creates a virtual stream that is composed on the part of 
   *  the byte array used as input. The stream starts at position
   *  <code>off</code> in the array and has a length of <code>len</code>.
   * 
   * @param buffer [in] The buffer used to read the data.
   * @param off [in] Offset in buffer where the stream should start
   * @param len [in] Length of the stream in bytes, starting from <code>off</code> 
   */
  public ByteArrayInputStream(byte[] buffer, int off, int len)
  {
    super();
    this.buf = buffer;
    this.length = len;
    this.offset = off;
    // Initial bookmark is set to position 0
    markPos = 0;
  }
  

  public long length()
  {
    return length;
  }



  public void seek(long newPosition) throws IOException
  {
    streamPos = newPosition;
  }


  public final int read() throws IOException
  {
    if ((streamPos+1) > length)
    {
      return -1;
    }
    readCount++;
    return buf[ offset+(int)streamPos++] & 0xff;
  }

  public final int read(byte[] b, int off, int len) throws IOException
  {
    if ((off | len) < 0 || off > b.length || b.length - off < len)
    {
      throw new IndexOutOfBoundsException("Invalid parameters");
    }
    if (streamPos >= length)
    {
      return -1;
    }
    if (streamPos + len > length) 
    {
      len = (int) (length - streamPos);
    }
    if (len <= 0) 
    {
      return 0;
    }
    System.arraycopy(buf, (int) streamPos+offset, b, off, len);
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
    if ((streamPos + n) > length)
    {
      n = length - streamPos;
    }
    if (n <= 0)
    {
      return 0;
    }
    seek(streamPos + n);
    return n;
  }


  public int available() throws IOException
  {
    if (buf == null)
    {
      throw new IOException("Stream is closed.");
    }
    return (int) (length - streamPos);
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


  public int read(byte[] b) throws IOException
  {
    return read(b,0,b.length);
  }
  
  
  
}
