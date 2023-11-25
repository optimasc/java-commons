package com.optimasc.io;

import java.io.IOException;
import java.io.OutputStream;

public class ByteArrayOutputStream extends SeekableDataOutputStream
{
  protected byte[] buf;
  protected int length;
  protected int currentPos;

  public long getStreamPosition() throws IOException
  {
    return currentPos;
  }

  public void seek(long pos) throws IOException
  {
    bitOffset = 0;
    currentPos = (int) pos;
  }

  public long length() throws IOException
  {
    return length;
  }
  
  /** Expand by the specified number of bytes.
   *  
   * @param i The additional size of the buffer
   *   to allocate.
   */
  private void expand(int i)
  {
    if (length + i <= buf.length) {
        return;
    }
    byte[] newbuf = new byte[(length + i) * 2];
    System.arraycopy(buf, 0, newbuf, 0, length);
    buf = null;
    length = length + i;
    buf = newbuf;
  }

  public void write(int b) throws IOException
  {
    if (currentPos == buf.length) 
    {
      expand(1);
    }
    buf[currentPos++] = (byte) b;
  }

  public void close() throws IOException
  {
    buf = null;
  }

  public void flush() throws IOException
  {
    // Do nothing
  }

  @Override
  public void write(byte[] b) throws IOException
  {
    // TODO Auto-generated method stub
    
  }

  public void write(byte[] b, int off, int len) throws IOException
  {
    if (off < 0 || off > b.length || len < 0
            || len > b.length - off) 
    {
        throw new IndexOutOfBoundsException("Invalid index");
    }
    if (len == 0) 
    {
        return;
    }
    expand(len);
    System.arraycopy(b, off, buf, currentPos, len);
    currentPos += len;
  }
  
  
  public boolean isCached()
  {
    return false;
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
}
