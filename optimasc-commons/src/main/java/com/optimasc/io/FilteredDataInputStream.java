package com.optimasc.io;

import java.io.IOException;
import java.io.InputStream;

/** A filtered input stream that permits to have access to all methods
 *  of {@link com.optimasc.io.AbstractDataInputStream}. 
 * 
 * @author Carl Eric codere
 *
 */
public class FilteredDataInputStream extends AbstractDataInputStream
{
  protected InputStream in;
  
  /** Creates this stream using the specified 
   *  input stream.
   * 
   * @param in [in] The underlying input stream,
   */
  public FilteredDataInputStream(InputStream in)
  {
    super();
    this.in = in;
  }

  public long skip(long n) throws IOException
  {
    return in.skip(n);
  }

  public int available() throws IOException
  {
    return in.available();
  }

  public synchronized void mark(int readlimit)
  {
    in.mark(readlimit);
  }

  public synchronized void reset() throws IOException
  {
    in.reset();
  }

  public boolean markSupported()
  {
    return in.markSupported();
  }

  public int read(byte[] b) throws IOException
  {
    int value = in.read(b);
    readCount +=  value;
    return value;
  }

  public int read(byte[] buffer, int off, int len) throws IOException
  {
    int read = in.read(buffer, off, len);
    readCount = readCount + read;
    return read;
  }

  public void close() throws IOException
  {
    in.close();
  }

  public int read() throws IOException
  {
    int value = in.read();
    readCount++;
    return value;
  }

}
