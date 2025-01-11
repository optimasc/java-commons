package com.optimasc.io;

import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;

/** Class that permits to create a seekable data input stream. It can do this
 *  if the original InputStream follows these rules:
 *  
 *  <ul>
 *   <li>{@link java.io.InputStream#mark(int)} is supported.</li>
 *   <li>{@link java.io.InputStream#available()} returns the full length 
 *    of the stream when this filter stream is created.</li>
 *   <li>{@link java.io.InputStream#skip(long)} always skips the correct
 *    number of bytes.</li>
 *  </ul>
 *  
 *  <p>The {@link java.io.ByteInputStream} and {@link java.io.FileInputStream}
 *  classes of the Java SDK do follow the above contracts and can be used directly.</p>.
 * 
 * @author Carl Eric Codere
 *
 */
public class FilteredDataInputStream extends SeekableDataInputStream implements DataInput
{
  protected InputStream in;
  
  /** Creates this stream using the specified 
   *  input stream.
   * 
   * @param in [in] The underlying input stream,
   * @throws IOException 
   */
  public FilteredDataInputStream(InputStream in) throws IOException
  {
    super();
    this.in = in;
    if (in.markSupported()==false)
    {
      throw new IllegalArgumentException("mark must be supported!");
    }
    length = in.available();
    markPos = 0;
  }

  public long getStreamPosition() throws IOException
  {
    return currentPos;
  }

  public void seek(long pos) throws IOException
  {
    in.reset();
    long toskip = pos;
    while (toskip > 0)
    {
      toskip = toskip - in.skip(toskip);
    }
    currentPos = pos;
  }
  
  public void close() throws IOException
  {
    in.close();
  }
  

  public int read(byte[] b) throws IOException
  {
    if (b.length == 0)
    {
      return 0;
    }
    return read(b,0,b.length);
  }


  public int read(byte[] b, int off, int len) throws IOException
  {
    int value = in.read(b, off, len);
    currentPos = currentPos + len;
    return value;
  }

  public int read() throws IOException
  {
    int value = in.read();
    currentPos++;
    return value;
  }

  public boolean isCached()
  {
    return false;
  }
  

}
