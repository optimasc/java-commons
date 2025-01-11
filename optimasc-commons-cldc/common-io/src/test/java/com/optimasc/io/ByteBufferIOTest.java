package com.optimasc.io;

import junit.framework.TestCase;

public class ByteBufferIOTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

/*  public void testGetDoubleBig()
  {
    fail("Not yet implemented");
  }

  public void testGetDoubleLittle()
  {
    fail("Not yet implemented");
  }*/
  
/*  public void testGetFloatBig()
  {
    byte buffer[] = {0x00,0x12,0x34,0x56,0x78};
    float expectedValue = 1.0f;
    assertEquals(expectedValue, ByteBufferIO.getFloatBig(buffer, 1));
    
    fail("Not yet implemented");
  }
  

  public void testGetFloatLittle()
  {
    fail("Not yet implemented");
  }*/

  public void testGetIntBig()
  {
    byte buffer[] = {0x00,0x12,0x34,0x56,0x78};
    int expectedValue = 0x12345678;
    assertEquals(expectedValue, ByteBufferIO.getIntBig(buffer, 1));
  }

  public void testGetIntLittle()
  {
    byte buffer[] = {0x00,0x12,0x34,0x56,0x78};
    int expectedValue = 0x78563412;
    assertEquals(expectedValue, ByteBufferIO.getIntLittle(buffer, 1));
  }

  public void testGetLongBig()
  {
    byte buffer[] = {0x00,0x12,0x34,0x56,0x78,0x12,0x34,0x56,0x78};
    long expectedValue = 0x1234567812345678L;
    assertEquals(expectedValue, ByteBufferIO.getLongBig(buffer, 1));
  }

  public void testGetLongLittle()
  {
    byte buffer[] = {0x00,0x12,0x34,0x56,0x78,0x12,0x34,0x56,0x78};
    long expectedValue = 0x7856341278563412L;
    assertEquals(expectedValue, ByteBufferIO.getLongLittle(buffer, 1));
  }

  public void testGetShortBig()
  {
    byte buffer[] = {0x00,0x12,0x34};
    short expectedValue = 0x1234;
    assertEquals(expectedValue, ByteBufferIO.getShortBig(buffer, 1));
  }

  public void testGetShortLittle()
  {
    byte buffer[] = {0x00,0x12,0x34};
    short expectedValue = 0x3412;
    assertEquals(expectedValue, ByteBufferIO.getShortLittle(buffer, 1));
  }

/*  public void testPutByteArrayIntByte()
  {
    fail("Not yet implemented");
  }

  public void testPutByteArrayIntByteArrayIntInt()
  {
    fail("Not yet implemented");
  }*/

  public void testPutCharBig()
  {
    char value = 0x1234;
    byte buffer[] = {0x00,0x00,0x00};
    // Offset currentOffser + number of bytes
    assertEquals(3, ByteBufferIO.putCharBig(buffer, 1,value));
    
    assertEquals(0x12,buffer[1]);
    assertEquals(0x34,buffer[2]);
  }

  public void testPutCharLittle()
  {
    char value = 0x1234;
    byte buffer[] = {0x00,0x00,0x00};
    // Offset currentOffser + number of bytes
    assertEquals(3, ByteBufferIO.putCharLittle(buffer, 1,value));
    
    assertEquals(0x34,buffer[1]);
    assertEquals(0x12,buffer[2]);
  }

/*  public void testPutDoubleBig()
  {
    fail("Not yet implemented");
  }

  public void testPutDoubleLittle()
  {
    fail("Not yet implemented");
  }


  public void testPutFloatBig()
  {
    fail("Not yet implemented");
  }

  public void testPutFloatLittle()
  {
    fail("Not yet implemented");
  }*/

  public void testPutIntBig()
  {
    int value = 0x12345678;
    byte buffer[] = {0x00,0x00,0x00,0x00,0x00};
    // Offset currentOffser + number of bytes
    assertEquals(5, ByteBufferIO.putIntBig(buffer, 1,value));
    
    assertEquals(0x12,buffer[1]);
    assertEquals(0x34,buffer[2]);
    assertEquals(0x56,buffer[3]);
    assertEquals(0x78,buffer[4]);
  }

  public void testPutIntLittle()
  {
    int value = 0x12345678;
    byte buffer[] = {0x00,0x00,0x00,0x00,0x00};
    // Offset currentOffser + number of bytes
    assertEquals(5, ByteBufferIO.putIntLittle(buffer, 1,value));
    
    assertEquals(0x78,buffer[1]);
    assertEquals(0x56,buffer[2]);
    assertEquals(0x34,buffer[3]);
    assertEquals(0x12,buffer[4]);
  }

  public void testPutLongBig()
  {
    long value = 0x123456787F7E6543L;
    byte buffer[] = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    // Offset currentOffser + number of bytes
    assertEquals(9, ByteBufferIO.putLongBig(buffer, 1,value));
    
    assertEquals(0x12,buffer[1]);
    assertEquals(0x34,buffer[2]);
    assertEquals(0x56,buffer[3]);
    assertEquals(0x78,buffer[4]);
    assertEquals(0x7F,buffer[5]);
    assertEquals(0x7E,buffer[6]);
    assertEquals(0x65,buffer[7]);
    assertEquals(0x43,buffer[8]);
  }

  public void testPutLongLittle()
  {
    long value = 0x123456787F7E6543L;
    byte buffer[] = {0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
    // Offset currentOffser + number of bytes
    assertEquals(9, ByteBufferIO.putLongLittle(buffer, 1,value));
    
    assertEquals(0x12,buffer[8]);
    assertEquals(0x34,buffer[7]);
    assertEquals(0x56,buffer[6]);
    assertEquals(0x78,buffer[5]);
    assertEquals(0x7F,buffer[4]);
    assertEquals(0x7E,buffer[3]);
    assertEquals(0x65,buffer[2]);
    assertEquals(0x43,buffer[1]);
  }

  public void testPutShortBig()
  {
    short value = 0x1234;
    byte buffer[] = {0x00,0x00,0x00};
    // Offset currentOffser + number of bytes
    assertEquals(3, ByteBufferIO.putShortBig(buffer, 1,value));
    
    assertEquals(0x12,buffer[1]);
    assertEquals(0x34,buffer[2]);
  }

  public void testPutShortLittle()
  {
    short value = 0x1234;
    byte buffer[] = {0x00,0x00,0x00};
    // Offset currentOffser + number of bytes
    assertEquals(3, ByteBufferIO.putShortLittle(buffer, 1,value));
    
    assertEquals(0x34,buffer[1]);
    assertEquals(0x12,buffer[2]);
  }

}
