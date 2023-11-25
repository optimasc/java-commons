package com.optimasc.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;


/** Implementation of the the {@link com.optimasc.io.DataOutput} interface that 
 *  works on a file on disk.
 *  
 * @author Carl Eric Codere <carl-eric.codere@optimasc.com>
 *
 */
public class FileDataOutputStream extends SeekableDataOutputStream
{
  protected RandomAccessFile fd;

  public FileDataOutputStream(File f) throws IOException
  {
    fd = new RandomAccessFile(f, "rw");
  }
  
  public FileDataOutputStream(RandomAccessFile fd) throws IOException
  {
    this.fd = fd;
  }
  
  public FileDataOutputStream(String name) throws IOException
  {
    fd = new RandomAccessFile(name, "rw");
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

  public void write(int b) throws IOException
  {
    fd.write(b);
  }

  @Override
  public void close() throws IOException
  {
    fd.close();
  }

  public void flush() throws IOException
  {
  }

  public void write(byte[] b) throws IOException
  {
    fd.write(b);
  }

  public void write(byte[] b, int off, int len) throws IOException
  {
    fd.write(b, off, len);
  }

  public boolean isCached()
  {
    return false;
  }

}
