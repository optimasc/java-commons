package com.optimasc.io;

import java.io.IOException;
import java.io.InputStream;

public abstract class SeekableDataInputStream extends AbstractDataInputStream implements Seekable
{
  protected long markPos;
  protected long currentPos;
  protected long length;
  
  public SeekableDataInputStream()
  {
    super();
  }
  
  
  public void reset() throws IOException
  {
    seek(markPos);
  }
  

  public void mark(int readlimit)
  {
    try
    {
      markPos =  getStreamPosition();
    } catch (IOException e)
    {
      markPos = 0;
    }
  }

  public boolean markSupported()
  {
    return true;
  }

  public long skip(long n) throws IOException
  {
    long currentPos = getStreamPosition();
    long currentLength = length();
    if ((currentPos + n) > currentLength)
    {
      n = currentLength - currentPos;
    }
    if (n <= 0)
    {
      return 0;
    }
    seek(currentPos + n);
    return n;
  }
  
  public long length() throws IOException
  {
    return length;
  }


  public int available() throws IOException
  {
    return (int) (length - currentPos);
  }
  
  public abstract void close() throws IOException;
  public abstract int read(byte[] b) throws IOException; 
  public abstract int read(byte[] b, int off, int len) throws IOException;
  public abstract int read() throws IOException;
  public abstract long getStreamPosition() throws IOException;
  public abstract void seek(long pos) throws IOException;
  


}
