package com.optimasc.io;

public class FilteredDataInputStreamTest extends AbstractDataInputStreamTest
{
  protected static final byte[] emptyBuffer = new byte[0];
  

  protected void setUp() throws Exception
  {
    super.setUp();
    ein = new FilteredDataInputStream(new java.io.ByteArrayInputStream(emptyBuffer));
    in = new FilteredDataInputStream(new java.io.ByteArrayInputStream(BYTE_TEST_BUFFER));
    sis1 = new FilteredDataInputStream(new java.io.ByteArrayInputStream(STRING_TEST_NEWLINE_BUFFER_01.getBytes("ISO-8859-1")));
    sis2 = new FilteredDataInputStream(new java.io.ByteArrayInputStream(STRING_TEST_NEWLINE_BUFFER_02.getBytes("ISO-8859-1")));
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

}
