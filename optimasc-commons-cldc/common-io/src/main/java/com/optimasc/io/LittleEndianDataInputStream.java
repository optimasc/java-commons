package com.optimasc.io;

import java.io.IOException;
import java.io.InputStream;

public class LittleEndianDataInputStream extends DataInputStream
{
  public LittleEndianDataInputStream(InputStream in)
  {
    super(in);
  }

  public short readShort() throws IOException
  {
    int result;

    readFully(readBuffer, 0, 2);
    result = ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    return (short) result;
  }

  public int readUnsignedShort() throws IOException
  {
    int result;

    readFully(readBuffer, 0, 2);
    result = ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    return (int) (result & 0xFFFF);
  }

  public int readInt() throws IOException
  {
    int result;
    readFully(readBuffer, 0, 4);
      result = ((readBuffer[3] & 0xFF) << 24) | ((readBuffer[2] & 0xFF) << 16)
          | ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    return result;  
  }

  public long readUnsignedInt() throws IOException
  {
    int result;
    readFully(readBuffer, 0, 4);
      result = ((readBuffer[3] & 0xFF) << 24) | ((readBuffer[2] & 0xFF) << 16)
          | ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    return ((long) result & 0xFFFFFFFFL);
  }

  public long readLong() throws IOException
  {
    long result = 0;
    readFully(readBuffer, 0, 8);
      for (int j = 8 - 1; j >= 0; j--)
      {
        result <<= 8;
        result |= (readBuffer[j] & 0xFF);
      }
    return result;
  }
  
  
  public char readChar() throws IOException
  {
    int result;
    readFully(readBuffer, 0, 2);
    result = ((readBuffer[1] & 0xFF) << 8) | (readBuffer[0] & 0xFF);
    return (char) (result & 0xFFFF);
  }
  
  public String readUTF() throws IOException
  {
    return readUTF(this);
  }
  

}
