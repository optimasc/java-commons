package com.optimasc.io;

import java.io.IOException;
import java.util.Arrays;

/** This class expands on parent classes and tests
 *  the writing of data as well as seek operations. 
 *  
 *  The tests in this class assume the following:
 *   * An empty stream when created and which can grow.
 *  
 * @author Carl Eric Codere
 *
 */
public abstract class SeekableDataStreamTest extends SeekableDataInputStreamTest
{
  protected SeekableDataStream ds;
  
  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testFlush()
  {
    try
    {
      ds.flush();
    } catch (IOException e)
    {
      fail();
    }
  }

  /** Basic I/O Routine testing */
  public void testWriteByteArrayIntInt()
  {
    /* The buffer must be greater than BYTE_TEST_BUFFER */
    final byte[] buffer = {0x55,0x54,0x53,0x52,
                           0x50,0x49,0x48,0x47,
                           0x46,0x45,0x44,0x43,
                           0x42,0x41,0x40,0x39};
    byte[] readBuffer = new byte[buffer.length];
    assertTrue(buffer.length > BYTE_TEST_BUFFER.length);
    
    try
    {
      /* Partially overwrite existing data */
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      ds.seek(1);
      assertEquals(1,ds.getStreamPosition());
      ds.write(buffer,0,buffer.length);
      assertEquals(1+buffer.length,ds.getStreamPosition());
      assertEquals(1+buffer.length,ds.length());
      ds.seek(1);
      ds.read(readBuffer);
      assertTrue(Arrays.equals(buffer, readBuffer));
      ds.seek(0);
      /* This should be the initial value of BYTE_TEST_BUFFER */
      assertEquals(0x00,ds.read());
      assertEquals(1,ds.getStreamPosition());
      assertEquals(1+buffer.length,ds.length());
      Arrays.fill(readBuffer, (byte)0);
      ds.read(readBuffer,0,readBuffer.length);
      assertTrue(Arrays.equals(buffer, readBuffer));
    } catch (IOException e)
    {
      fail();
    }
  }
  
  /** Basic I/O Routine testing */
  public void testWriteByte()
  {
    /* The buffer must be greater than BYTE_TEST_BUFFER */
    final byte[] buffer = {0x55,0x54,0x53,0x52,
                           0x50,0x49,0x48,0x47,
                           0x46,0x45,0x44,0x43,
                           0x42,0x41,0x40,0x39};
    byte[] readBuffer = new byte[buffer.length];
    assertTrue(buffer.length > BYTE_TEST_BUFFER.length);
    
    try
    {
      /* Partially overwrite existing data */
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      ds.seek(1);
      assertEquals(1,ds.getStreamPosition());
      for (int i=0; i < buffer.length; i++)
      {
        ds.write(buffer[i]);
      }
      assertEquals(1+buffer.length,ds.getStreamPosition());
      assertEquals(1+buffer.length,ds.length());
      ds.seek(1);
      ds.read(readBuffer);
      assertTrue(Arrays.equals(buffer, readBuffer));
      ds.seek(0);
      /* This should be the initial value of BYTE_TEST_BUFFER */
      assertEquals(0x00,ds.read());
      assertEquals(1,ds.getStreamPosition());
      assertEquals(1+buffer.length,ds.length());
      Arrays.fill(readBuffer, (byte)0);
      ds.read(readBuffer,0,readBuffer.length);
      assertTrue(Arrays.equals(buffer, readBuffer));
    } catch (IOException e)
    {
      fail();
    }
  }
  

/*  public void testWriteTo()
  {
    fail("Not yet implemented");
  }*/

  public void testWriteBoolean_BigEndian()
  {
    ds.setByteOrder(ByteOrder.BIG_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeBoolean(true);
      ds.seek(pos);
      assertEquals(true,ds.readBoolean());
      assertEquals(BYTE_TEST_BUFFER.length+1,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  
  public void testWriteBoolean_LittleEndian()
  {
    ds.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeBoolean(true);
      ds.seek(pos);
      assertEquals(true,ds.readBoolean());
      assertEquals(BYTE_TEST_BUFFER.length+1,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  

  public void testWriteShort_BigEndian()
  {
    ds.setByteOrder(ByteOrder.BIG_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeShort(0x1234);
      ds.seek(pos);
      assertEquals(0x12,ds.read());
      assertEquals(0x34,ds.read());
      ds.seek(pos);
      assertEquals(0x1234,ds.readShort());
      assertEquals(BYTE_TEST_BUFFER.length+2,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  
  public void testWriteShort_LittleEndian()
  {
    ds.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeShort(0x1234);
      ds.seek(pos);
      assertEquals(0x34,ds.read());
      assertEquals(0x12,ds.read());
      ds.seek(pos);
      assertEquals(0x1234,ds.readShort());
      assertEquals(BYTE_TEST_BUFFER.length+2,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  
  public void testWriteChar_LittleEndian()
  {
    ds.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeChar('\uA728');
      ds.seek(pos);
      assertEquals(0x28,ds.read());
      assertEquals(0xA7,ds.read());
      ds.seek(pos);
      assertEquals('\uA728',ds.readChar());
      assertEquals(BYTE_TEST_BUFFER.length+2,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  
  public void testWriteChar_BigEndian()
  {
    ds.setByteOrder(ByteOrder.BIG_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeChar('\uA728');
      ds.seek(pos);
      assertEquals(0xA7,ds.read());
      assertEquals(0x28,ds.read());
      ds.seek(pos);
      assertEquals('\uA728',ds.readChar());
      assertEquals(BYTE_TEST_BUFFER.length+2,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  

  public void testWriteInt_BigEndian()
  {
    ds.setByteOrder(ByteOrder.BIG_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeInt(0x12345678);
      ds.seek(pos);
      assertEquals(0x12,ds.read());
      assertEquals(0x34,ds.read());
      assertEquals(0x56,ds.read());
      assertEquals(0x78,ds.read());
      ds.seek(pos);
      assertEquals(0x12345678,ds.readInt());
      assertEquals(BYTE_TEST_BUFFER.length+4,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  
  public void testWriteInt_LittleEndian()
  {
    ds.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeInt(0x12345678);
      ds.seek(pos);
      assertEquals(0x78,ds.read());
      assertEquals(0x56,ds.read());
      assertEquals(0x34,ds.read());
      assertEquals(0x12,ds.read());
      ds.seek(pos);
      assertEquals(0x12345678,ds.readInt());
      assertEquals(BYTE_TEST_BUFFER.length+4,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  

  public void testWriteLong_BigEndian()
  {
    long value = 0x123456787F7E6543L;
    byte[] buffer = new byte[9];
    ds.setByteOrder(ByteOrder.BIG_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeLong(value);
      ds.seek(pos);
      /* start reading at offset 1 in array for further testing */
      ds.read(buffer,1,8);
      assertEquals(0x12,buffer[1]);
      assertEquals(0x34,buffer[2]);
      assertEquals(0x56,buffer[3]);
      assertEquals(0x78,buffer[4]);
      assertEquals(0x7F,buffer[5]);
      assertEquals(0x7E,buffer[6]);
      assertEquals(0x65,buffer[7]);
      assertEquals(0x43,buffer[8]);
      ds.seek(pos);
      assertEquals(value,ds.readLong());
      assertEquals(BYTE_TEST_BUFFER.length+8,ds.length());
    } catch (IOException e)
    {
      fail();
    }
    buffer = null;
  }
  
  public void testWriteLong_LittleEndian()
  {
    long value = 0x123456787F7E6543L;
    byte[] buffer = new byte[9];
    ds.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeLong(value);
      ds.seek(pos);
      /* start reading at offset 1 in array for further testing */
      ds.read(buffer,1,8);
      assertEquals(0x12,buffer[8]);
      assertEquals(0x34,buffer[7]);
      assertEquals(0x56,buffer[6]);
      assertEquals(0x78,buffer[5]);
      assertEquals(0x7F,buffer[4]);
      assertEquals(0x7E,buffer[3]);
      assertEquals(0x65,buffer[2]);
      assertEquals(0x43,buffer[1]);
      ds.seek(pos);
      assertEquals(value,ds.readLong());
      assertEquals(BYTE_TEST_BUFFER.length+8,ds.length());
    } catch (IOException e)
    {
      fail();
    }
    buffer = null;
  }
  

/*  public void testWriteFloat()
  {
    fail("Not yet implemented");
  }

  public void testWriteDouble()
  {
    fail("Not yet implemented");
  }*/

/*  public void testWriteBytes()
  {
    fail("Not yet implemented");
  }

  public void testWriteChars()
  {
    fail("Not yet implemented");
  }

  public void testWriteUTF()
  {
    fail("Not yet implemented");
  }*/

  public void testWriteByte_BigEndian()
  {
    ds.setByteOrder(ByteOrder.BIG_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeByte((byte)254);
      ds.seek(pos);
      assertEquals(254,ds.read());
      ds.seek(pos);
      assertEquals(254,ds.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER.length+1,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  public void testWriteByte_LittleEndian()
  {
    ds.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,ds.length());
      long pos = ds.getStreamPosition();
      assertEquals(BYTE_TEST_BUFFER.length,pos);
      ds.writeByte((byte)254);
      ds.seek(pos);
      assertEquals(254,ds.read());
      ds.seek(pos);
      assertEquals(254,ds.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER.length+1,ds.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  
/*  public void testWriteBit()
  {
    fail("Not yet implemented");
  }

  public void testWriteBits()
  {
    fail("Not yet implemented");
  }*/

}
