package com.optimasc.io;

import java.io.EOFException;
import java.io.IOException;
import java.util.Arrays;

import junit.framework.TestCase;


/** Data input stream test cases. This tests the contract, 
 *  as specified in the AbstractDataInputStream API documentation. To test
 *  an AbstractDataInputStream, implement a derived class and override
 *  <code>setUp</code> to set protected variables in the 
 *  class which are used in each test:
 *  
 *  <dl>
 *   <dt>in</dt>
 *   <dd>Should have a stream that has the exact contents {@link #BYTE_TEST_BUFFER}
 *    bytes. The instance of this class should be <code>AbstractDataInputStream</code></dd>
 *   <dt>ein</dt>
 *   <dd>Should have a stream that has no data. This is used for InputStream
 *    testing.</dd>
 *   <dt>sis1</dt>
 *   <dd>Should have a stream equal to {@link #STRING_TEST_NEWLINE_BUFFER_01}</dd>
 *   <dt>sis2</dt>
 *   <dd>Should have a stream equal to {@link #STRING_TEST_NEWLINE_BUFFER_02}</dd>
 *  </dl>
 *  
 * @author Carl Eric Codere.
 *
 */
public abstract class AbstractDataInputStreamTest extends MarkableInputStreamTest
{
  
  // Mapping of BYTE_TEST_BUFFER to boolean values
  protected static final boolean[] BOOLEAN_TEST_BUFFER = 
  {false,false,true,true,true,true,true,true,true,true,true,true,true,true};
  
  // Mapping of BYTE_TEST_BUFFER to short values in big endian
  protected static final short[] SHORT_BIG_TEST_BUFFER = 
  {0x0000,0x0102,0x0304,0x0506,0x0708,(short)0xFFFF,(short)0xFFFF};
  
  // Mapping of BYTE_TEST_BUFFER to short values in little endian
  protected static final short[] SHORT_LITTLE_TEST_BUFFER = 
  {0x0000,0x0201,0x0403,0x0605,0x0807,(short)0xFFFF,(short)0xFFFF};
  
  // Mapping of BYTE_TEST_BUFFER to int values in big endian
  protected static final int[] INT_BIG_TEST_BUFFER = 
  {0x00000102,
   0x03040506,
   0x0708FFFF,
   // Invalid on purpose
   0xFFFF0000};
  
  // Mapping of BYTE_TEST_BUFFER to int values in little endian
  protected static final int[] INT_LITTLE_TEST_BUFFER = 
  {0x02010000,
   0x06050403,
   0xFFFF0807,
   0x0000FFFF};
  
  
  
  // Mapping of BYTE_TEST_BUFFER to long values in big endian
  protected static final long[] LONG_BIG_TEST_BUFFER = 
  {0x0000010203040506L,
   // Invalid value
   0x0708FFFFFFFF0000L};
  
  // Mapping of BYTE_TEST_BUFFER to long values in little endian
  protected static final long[] LONG_LITTLE_TEST_BUFFER = 
  {0x0605040302010000L,
    // Invalid value
   0x0000FFFFFFFF0807L};
  
  // Mapping of BYTE_TEST_BUFFER to char values
  protected static final String STRING_TEST_BUFFER =
    "\u0000\u0000\u0001\u0002\u0003\u0004\u0005\u0006\u0007\u0008\u00FF\u00FF\u00FF\u00FF";

  protected static final String STRING_TEST_NEWLINE_BUFFER_01 = 
  "ABCDEFGH\n";     
  
  protected static final String STRING_TEST_NEWLINE_BUFFER_02 =
  "ABCDEFGH\r\n";
  
  protected AbstractDataInputStream sis1;
  protected AbstractDataInputStream sis2;
  
  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testGetSetByteOrder_StandardScenario()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    assertEquals(ByteOrder.BIG_ENDIAN,is.getByteOrder());
    
    is.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    assertEquals(ByteOrder.LITTLE_ENDIAN,is.getByteOrder());
    
    is.setByteOrder(ByteOrder.BIG_ENDIAN);
    assertEquals(ByteOrder.BIG_ENDIAN,is.getByteOrder());
    
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }


  public void testReadBoolean_StandardScenario()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    // Read until we get an EOFException
    try
    {
      assertEquals(BOOLEAN_TEST_BUFFER[0],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[1],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[2],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[3],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[4],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[5],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[6],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[7],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[8],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[9],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[10],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[11],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[12],is.readBoolean());
      assertEquals(BOOLEAN_TEST_BUFFER[13],is.readBoolean());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(true,is.readBoolean());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  /** Test case that reads from an empty input
   *  stream, we should directly get an EOFException.
   * 
   */
  public void testReadByte_OutOfBounds()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) ein;
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readByte());
    } catch (IOException e)
    {
      EOFThrown = true;
    }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  /** Test case that reads all bytes of the stream,
   *  and reads one more byte beyond, expecting in this
   *  last case an EOFException.
   * 
   */
  public void testReadByte_StandardScenario()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    // Read until we get an EOFException
    try
    {
      assertEquals(BYTE_TEST_BUFFER[0],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[1],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[2],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[3],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[4],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[5],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[6],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[7],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[8],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[9],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[10],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[11],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[12],is.readByte());
      assertEquals(BYTE_TEST_BUFFER[13],is.readByte());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readByte());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }  
  
  
  public void testReadChar()
  {
    
  }
  

/*  public void testReadDouble()
  {
    fail("Not yet implemented");
  }
  
  
  public void testReadFloat()
  {
    fail("Not yet implemented");
  }*/
  
  public void testReadFullyByteArray_OutOfBounds()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    byte[] readBuffer = new byte[BYTE_TEST_BUFFER.length*2];
    boolean EOFThrown = false;
    try
    {
      is.readFully(readBuffer);
    } catch (EOFException e)
    {
      EOFThrown = true;
    } catch (IOException e)
    {
      fail();
    }
    if (EOFThrown == false)
    {
      fail();
    }
    try 
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  public void testReadFullyByteArray_StandardScenario()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    byte[] readBuffer = new byte[5];
    try
    {
      assertEquals(BYTE_TEST_BUFFER[0],is.read());
      is.readFully(readBuffer);
      assertEquals(BYTE_TEST_BUFFER[1],readBuffer[0]); 
      assertEquals(BYTE_TEST_BUFFER[2],readBuffer[1]); 
      assertEquals(BYTE_TEST_BUFFER[3],readBuffer[2]); 
      assertEquals(BYTE_TEST_BUFFER[4],readBuffer[3]); 
      assertEquals(BYTE_TEST_BUFFER[5],readBuffer[4]); 
    } catch (EOFException e)
    {
      fail();
    } catch (IOException e)
    {
      fail();
    }
    try 
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }

  
  /** Test method {@link com.optimasc.io.AbstractDataInputStream#read(byte[])} 
   *  when the buffer is bigger than the data to read. */
  public void testReadByteArray_normal()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    byte[] readBuffer = new byte[BYTE_TEST_BUFFER.length*2];
    try
    {
      /* Read more than data then there is in the buffer.
       * should return the actual data length.
       */
      assertEquals(BYTE_TEST_BUFFER.length,is.read(readBuffer));
      /* Compare each byte of read buffer */
      for (int i=0;  i <BYTE_TEST_BUFFER.length; i++)
      {
        assertEquals(BYTE_TEST_BUFFER[i],readBuffer[i]);
      }
      /* Going beyond end of data, should return -1 */
      assertEquals(-1,is.read(readBuffer));
    } catch (IOException e)
    {
      fail();
    }
    try 
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  /** Test method {@link com.optimasc.io.AbstractDataInputStream#read(byte[])} 
   *  when the buffer length is zero. */
  public void testReadByteArray_zerolength()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) ein;
    byte[] readBuffer = new byte[0];
    try
    {
      assertEquals(0,is.read(readBuffer));
      assertEquals(0,is.read(readBuffer));
    } catch (IOException e)
    {
      fail();
    }
    try 
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  


  public void testReadFullyByteArrayIntInt_OutOfBounds()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    byte[] readBuffer = new byte[BYTE_TEST_BUFFER.length*2];
    boolean EOFThrown = false;
    try
    {
      is.readFully(readBuffer,10,readBuffer.length-10);
    } catch (EOFException e)
    {
      EOFThrown = true;
    } catch (IOException e)
    {
      fail();
    }
    if (EOFThrown == false)
    {
      fail();
    }
    try 
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  public void testReadFullyByteArrayIntInt_StandardScenario()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    byte[] readBuffer = new byte[32];
    final byte FILLER = (byte)0x7F;
    Arrays.fill(readBuffer, FILLER);
    try
    {
      assertEquals(BYTE_TEST_BUFFER[0],is.read());
      is.readFully(readBuffer,5,4);
      assertEquals(FILLER,readBuffer[0]); 
      assertEquals(FILLER,readBuffer[1]); 
      assertEquals(FILLER,readBuffer[2]); 
      assertEquals(FILLER,readBuffer[3]); 
      assertEquals(FILLER,readBuffer[4]); 
      assertEquals(BYTE_TEST_BUFFER[1],readBuffer[5]); 
      assertEquals(BYTE_TEST_BUFFER[2],readBuffer[6]); 
      assertEquals(BYTE_TEST_BUFFER[3],readBuffer[7]); 
      assertEquals(BYTE_TEST_BUFFER[4],readBuffer[8]); 
      assertEquals(FILLER,readBuffer[9]); 
    } catch (EOFException e)
    {
      fail();
    } catch (IOException e)
    {
      fail();
    }
    try 
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  public void testReadInt_StandardScenario_BigEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.BIG_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(INT_BIG_TEST_BUFFER[0],is.readInt());
      assertEquals(INT_BIG_TEST_BUFFER[1],is.readInt());
      assertEquals(INT_BIG_TEST_BUFFER[2],is.readInt());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readInt());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  public void testReadInt_StandardScenario_LittleEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(INT_LITTLE_TEST_BUFFER[0],is.readInt());
      assertEquals(INT_LITTLE_TEST_BUFFER[1],is.readInt());
      assertEquals(INT_LITTLE_TEST_BUFFER[2],is.readInt());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readInt());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  
  public void testReadLine_StandardScenario_NoNewLine()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    try
    {
      String s = is.readLine();
      assertEquals(STRING_TEST_BUFFER,s); 
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  public void testReadLine_StandardScenario_NewLines()
  {
    try
    {
      String s = sis1.readLine();
      String expectedValue = STRING_TEST_NEWLINE_BUFFER_01.substring(0,STRING_TEST_NEWLINE_BUFFER_01.length()-1);
      assertEquals(expectedValue,s); 
      s = sis2.readLine();
      expectedValue = STRING_TEST_NEWLINE_BUFFER_02.substring(0,STRING_TEST_NEWLINE_BUFFER_02.length()-2);
      assertEquals(expectedValue,s); 
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  
  public void testReadLine_StandardScenario_OutOfBounds()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) ein;
    try
    {
      assertEquals(null,is.readLine()); 
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  
  public void testReadLong_StandardScenario_BigEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.BIG_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(LONG_BIG_TEST_BUFFER[0],is.readLong());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readLong());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  

  public void testReadLong_StandardScenario_LittleEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(LONG_LITTLE_TEST_BUFFER[0],is.readLong());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readLong());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }

  public void testReadShort_StandardScenario_BigEndian()
  {
    
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.BIG_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(SHORT_BIG_TEST_BUFFER[0],is.readShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[1],is.readShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[2],is.readShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[3],is.readShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[4],is.readShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[5],is.readShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[6],is.readShort());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readShort());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }

  public void testReadShort_StandardScenario_LittleEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(SHORT_LITTLE_TEST_BUFFER[0],is.readShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[1],is.readShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[2],is.readShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[3],is.readShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[4],is.readShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[5],is.readShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[6],is.readShort());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readShort());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }

  /** Test case that reads from an empty input
   *  stream, we should directly get an EOFException.
   * 
   */
  public void testReadUnsignedByte_OutOfBounds()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) ein;
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readUnsignedByte());
    } catch (IOException e)
    {
      EOFThrown = true;
    }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  /** Test case that reads all bytes of the stream,
   *  and reads one more byte beyond, expecting in this
   *  last case an EOFException.
   * 
   */
  public void testReadUnsignedByte_StandardScenario()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    // Read until we get an EOFException
    try
    {
      assertEquals(BYTE_TEST_BUFFER[0] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[1] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[2] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[3] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[4] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[5] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[6] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[7] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[8] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[9] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[10] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[11] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[12] & 0xFF,is.readUnsignedByte());
      assertEquals(BYTE_TEST_BUFFER[13] & 0xFF,is.readUnsignedByte());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readUnsignedByte());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  public void testReadUnsignedInt_StandardScenario_BigEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.BIG_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(INT_BIG_TEST_BUFFER[0] & 0xFFFFFFFFL,is.readUnsignedInt());
      assertEquals(INT_BIG_TEST_BUFFER[1] & 0xFFFFFFFFL,is.readUnsignedInt());
      assertEquals(INT_BIG_TEST_BUFFER[2] & 0xFFFFFFFFL,is.readUnsignedInt());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readUnsignedInt());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  public void testReadUnsignedInt_StandardScenario_LittleEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(INT_LITTLE_TEST_BUFFER[0] & 0xFFFFFFFFL,is.readUnsignedInt());
      assertEquals(INT_LITTLE_TEST_BUFFER[1] & 0xFFFFFFFFL,is.readUnsignedInt());
      assertEquals(INT_LITTLE_TEST_BUFFER[2] & 0xFFFFFFFFL,is.readUnsignedInt());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readUnsignedInt());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  

/*  public void testGetCount()
  {
    fail("Not yet implemented");
  }

  public void testResetCount()
  {
    fail("Not yet implemented");
  }*/

  public void testReadUnsignedShort_StandardScenario_BigEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.BIG_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(SHORT_BIG_TEST_BUFFER[0] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[1] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[2] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[3] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[4] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[5] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_BIG_TEST_BUFFER[6] & 0xFFFF,is.readUnsignedShort());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readUnsignedShort());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }

  public void testReadUnsignedShort_StandardScenario_LittleEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(SHORT_LITTLE_TEST_BUFFER[0] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[1] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[2] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[3] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[4] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[5] & 0xFFFF,is.readUnsignedShort());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[6] & 0xFFFF,is.readUnsignedShort());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readUnsignedShort());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  
  
  
  public void testReadChar_StandardScenario_BigEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.BIG_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(SHORT_BIG_TEST_BUFFER[0] & 0xFFFF,is.readChar());
      assertEquals(SHORT_BIG_TEST_BUFFER[1] & 0xFFFF,is.readChar());
      assertEquals(SHORT_BIG_TEST_BUFFER[2] & 0xFFFF,is.readChar());
      assertEquals(SHORT_BIG_TEST_BUFFER[3] & 0xFFFF,is.readChar());
      assertEquals(SHORT_BIG_TEST_BUFFER[4] & 0xFFFF,is.readChar());
      assertEquals(SHORT_BIG_TEST_BUFFER[5] & 0xFFFF,is.readChar());
      assertEquals(SHORT_BIG_TEST_BUFFER[6] & 0xFFFF,is.readChar());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readChar());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }

  public void testReadChar_StandardScenario_LittleEndian()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    is.setByteOrder(ByteOrder.LITTLE_ENDIAN);
    // Read until we get an EOFException
    try
    {
      assertEquals(SHORT_LITTLE_TEST_BUFFER[0] & 0xFFFF,is.readChar());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[1] & 0xFFFF,is.readChar());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[2] & 0xFFFF,is.readChar());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[3] & 0xFFFF,is.readChar());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[4] & 0xFFFF,is.readChar());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[5] & 0xFFFF,is.readChar());
      assertEquals(SHORT_LITTLE_TEST_BUFFER[6] & 0xFFFF,is.readChar());
    } catch (IOException e)
    {
      fail();
    }
    
    // Should throw EOFException
    boolean EOFThrown = false;
    try
    {
      assertEquals(0,is.readChar());
    } catch (EOFException e)
    {
      EOFThrown =true;
    }
    catch (IOException e)
   {
       fail();    
   }
    if (EOFThrown == false)
    {
      fail();
    }
    try
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }
  

/*  public void testReadUTF()
  {
    fail("Not yet implemented");
  }*/


/*  
  public void testReadBits()
  {
    fail("Not yet implemented");
  }

  public void testSkipBits()
  {
    fail("Not yet implemented");
  }

  public void testPeekBits()
  {
    fail("Not yet implemented");
  }

  public void testGetBitOffset()
  {
    fail("Not yet implemented");
  }

  public void testReadBit()
  {
    fail("Not yet implemented");
  }

  public void testSetBitOffset()
  {
    fail("Not yet implemented");
  }*/

  public void testSkipBytes()
  {
    AbstractDataInputStream is = (AbstractDataInputStream) in;
    try
    {
      assertEquals(2, is.skipBytes(2));
      assertEquals(0x01, is.read());
      // Index 3
      assertEquals(3, is.skipBytes(3));
      // Index 6 
      assertEquals(0x05, is.read());
      assertEquals(0x06, is.read());
      assertEquals(6, is.skipBytes(BYTE_TEST_BUFFER.length));
      assertEquals(-1, is.read());
      assertEquals(0,is.skipBytes(32));
    } catch (IOException e)
    {
      fail();
    }
    try 
    {
      is.close();
    } catch (IOException e)
    {
      fail();
    }
  }

}
