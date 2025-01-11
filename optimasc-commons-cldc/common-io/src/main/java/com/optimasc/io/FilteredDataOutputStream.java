package com.optimasc.io;

import java.io.IOException;
import java.io.OutputStream;

/** A filtered output stream that permits to have access to all methods
 *  of {@link com.optimasc.io.AbstractDataOutputStream}. 
 * 
 * @author Carl Eric codere
 *
 */
public class FilteredDataOutputStream extends AbstractDataOutputStream
{
  protected OutputStream out;
  
  public FilteredDataOutputStream(OutputStream out)
  {
    super();
    this.out = out;
  }

  public void write(int b) throws IOException
  {
    out.write(b);
  }

  public void close() throws IOException
  {
    out.close();
  }

  public void flush() throws IOException
  {
    out.flush();
  }

  public void write(byte[] b) throws IOException
  {
    out.write(b);
  }

  public void write(byte[] b, int off, int len) throws IOException
  {
    out.write(b,off,len);
  }
}
