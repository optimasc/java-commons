package com.optimasc.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;

/** Implementation of the the {@link com.optimasc.io#SeekableDataInputStream} interface that 
 *  works on a file on disk and which is also Seekable.
 *  
 * @author Carl Eric Codere <carl-eric.codere@optimasc.com>
 *
 */
public class FileDataInputStream extends SeekableDataInputStream
{
  protected RandomAccessFile fd;
  
  public FileDataInputStream(File f) throws FileNotFoundException
  {
    fd = new RandomAccessFile(f,"r");
  }
  
  public FileDataInputStream(RandomAccessFile fd)
  {
    this.fd = fd;
  }

  public void close() throws IOException
  {
    fd.close();      
  }

  public int read(byte[] b) throws IOException
  {
    return fd.read(b);
  }

  public int read(byte[] b, int off, int len) throws IOException
  {
    return fd.read(b, off, len);
  }

  public int read() throws IOException
  {
    return fd.read();
  }

  public long getStreamPosition() throws IOException
  {
    return fd.getFilePointer();
  }

  public void seek(long pos) throws IOException
  {
    fd.seek(pos);
  }

  public long length() throws IOException
  {
    return fd.length();
  }

  public int available() throws IOException
  {
    return (int)(fd.length()-fd.getFilePointer());
  }

  public boolean isCached()
  {
    return false;
  }
  

}
