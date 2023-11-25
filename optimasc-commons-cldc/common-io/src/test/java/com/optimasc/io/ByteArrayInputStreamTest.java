package com.optimasc.io;

public class ByteArrayInputStreamTest extends AbstractDataInputStreamTest
{
  protected static final byte[] emptyBuffer = new byte[0];
  

  protected void setUp() throws Exception
  {
    super.setUp();
    ein = new ByteArrayInputStream(emptyBuffer);
    in = new ByteArrayInputStream(BYTE_TEST_BUFFER);
    sis1 = new ByteArrayInputStream(STRING_TEST_NEWLINE_BUFFER_01.getBytes("ISO-8859-1"));
    sis2 = new ByteArrayInputStream(STRING_TEST_NEWLINE_BUFFER_02.getBytes("ISO-8859-1"));
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

}
