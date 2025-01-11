package com.optimasc.io;

import java.io.EOFException;
import java.io.IOException;

public abstract class SeekableDataInputStreamTest extends AbstractDataInputStreamTest
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testLength()
  {
    SeekableDataInputStream is;
    /* no data */
    is = (SeekableDataInputStream) ein;
    try
    {
      assertEquals(0,is.length());
    } catch (IOException e)
    {
      fail();
    }
    /* valid data */
    is = (SeekableDataInputStream) in;
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length,is.length());
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  public void testBitInput()
  {
    SeekableDataInputStream is;
    boolean exceptionThrown;
    int value;
    is = (SeekableDataInputStream) ein;
    /* empty data */
    exceptionThrown = false;
    try
    {
      assertEquals(0,is.readBit());
    } catch (EOFException e)
    {
      exceptionThrown = true;
    } catch (IOException e)
    {
      fail();
    }
    assertEquals(true,exceptionThrown);
    
    /*------------ Valid data values ------------*/
    is = (SeekableDataInputStream) in;
    
    try
    {
      assertEquals(0,is.readBits(8));
      assertEquals(0,is.read());
      assertEquals(0,is.readBits(7));
      assertEquals(1,is.readBit());
      assertEquals(0x02,is.readBits(8));
      is.seek(3);
      assertEquals(0x02,is.readBits(8));      
    } catch (IOException e)
    {
      fail();
    }
    
/*    { 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };*/
    
  }
  

  public void testGetStreamPositionSeek()
  {
    SeekableDataInputStream is;
    boolean exceptionThrown;
    int value;
    is = (SeekableDataInputStream) ein;
    /* empty data */
    try
    {
      assertEquals(0,is.getStreamPosition());
    } catch (IOException e)
    {
      fail();
    }
    /* seek past the data length */
    try
    {
       is.seek(10);
       assertEquals(10,is.getStreamPosition());
     } catch (IOException e)
     {
       fail();
     }
    /* check that the length of data is still valid */
    try
    {
      assertEquals(0,is.length());
    } catch (IOException e2)
    {
      fail();
    }
    /* try to read some data, should try EOFException */
    exceptionThrown = false;
    try
    {
       value = is.readUnsignedByte();
    } catch (EOFException e)
    {
      exceptionThrown = true;
    } catch (IOException e1)
    {
      fail();
    }
    assertEquals(true,exceptionThrown);

    /*------------ Valid data values ------------*/
    is = (SeekableDataInputStream) in;
    try
    {
      assertEquals(0,is.getStreamPosition());
    } catch (IOException e)
    {
      fail();
    }
    /** Seek to a new value */
    try
    {
      is.seek(5);
      assertEquals(5,is.getStreamPosition());
      assertEquals(0x04,is.read());
      assertEquals(6,is.getStreamPosition());
      assertEquals(BYTE_TEST_BUFFER.length,is.length());
      is.seek(12);
      assertEquals(12,is.getStreamPosition());
      assertEquals(0xFF,is.read());
      assertEquals(13,is.getStreamPosition());
      is.seek(0);
      assertEquals(0,is.getStreamPosition());
      assertEquals(0x00,is.read());
    } catch (IOException e)
    {
      fail();
    }
  }
}
