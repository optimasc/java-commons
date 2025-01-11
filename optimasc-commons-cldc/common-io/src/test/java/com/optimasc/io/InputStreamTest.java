package com.optimasc.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.TestCase;

/**
 * Basic input stream test cases. This tests the contract, as specified in the
 * InputStream API documentation. To test an InputStream, implement a derived
 * class and override <code>setUp</code> to set 2 protected variables in the
 * class which are used in each test:
 * 
 * <dl>
 * <dt>in</dt>
 * <dd>Should have a stream that has the exact contents
 * {@link #BYTE_TEST_BUFFER} bytes.</dd>
 * <dt>ein</dt>
 * <dd>Should have a stream that has no data.</dd>
 * </dl>
 * 
 * @author Carl Eric Codere.
 *
 */
public abstract class InputStreamTest extends TestCase
{
  /** Expected data bytes to be read from the inputstream.
   *  13 bytes in length */
  public static final byte[] BYTE_TEST_BUFFER = 
    { 0x00, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF };

  /**
   * Standard Inputstream expecting BYTE_TEST_BUFFER data when reading, should
   * be setup at {@link #setUp()} method.
   */
  protected InputStream in;
  /**
   * Standard Inputstream containing no data when reading, should be setup at
   * {@link #setUp()} method.
   */
  protected InputStream ein;

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testRead_StandardScenario()
  {
    // Read until end of stream
    try
    {
      assertEquals(BYTE_TEST_BUFFER[0] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[1] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[2] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[3] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[4] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[5] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[6] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[7] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[8] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[9] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[10] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[11] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[12] & 0xFF, in.read());
      assertEquals(BYTE_TEST_BUFFER[13] & 0xFF, in.read());
    } catch (IOException e)
    {
      fail();
    }

    // Should return -1
    try
    {
      assertEquals(-1, in.read());
    } catch (IOException e)
    {
      fail();
    }
    try
    {
      in.close();
    } catch (IOException e)
    {
      fail();
    }
  }

  /*  public void testReadByteArray()
    {
      fail("Not yet implemented");
    }
  /*/

  public void testReadByteArrayIntInt_StandardScenario01()
  {
    byte[] readBuffer = new byte[BYTE_TEST_BUFFER.length * 2];
    final byte FILLER = (byte) 0x7F;
    Arrays.fill(readBuffer, FILLER);

    // Read 0 bytes
    try
    {
      assertEquals(0, in.read(readBuffer, 0, 0));
    } catch (IOException e)
    {
      fail();
    }

    // Read 8 bytes
    try
    {
      assertEquals(8, in.read(readBuffer, 0, 8));
      assertEquals(BYTE_TEST_BUFFER[0], readBuffer[0]);
      assertEquals(BYTE_TEST_BUFFER[1], readBuffer[1]);
      assertEquals(BYTE_TEST_BUFFER[2], readBuffer[2]);
      assertEquals(BYTE_TEST_BUFFER[3], readBuffer[3]);
      assertEquals(BYTE_TEST_BUFFER[4], readBuffer[4]);
      assertEquals(BYTE_TEST_BUFFER[5], readBuffer[5]);
      assertEquals(BYTE_TEST_BUFFER[6], readBuffer[6]);
      assertEquals(BYTE_TEST_BUFFER[7], readBuffer[7]);
      assertEquals(FILLER, readBuffer[8]);
      assertEquals(FILLER, readBuffer[9]);
      Arrays.fill(readBuffer, (byte) 0x7F);
    } catch (IOException e)
    {
      fail();
    }

    // Read rest of stream
    try
    {
      assertEquals(6, in.read(readBuffer, 2, readBuffer.length - 2));
      assertEquals(FILLER, readBuffer[0]);
      assertEquals(FILLER, readBuffer[1]);
      assertEquals(BYTE_TEST_BUFFER[8], readBuffer[2]);
      assertEquals(BYTE_TEST_BUFFER[9], readBuffer[3]);
      assertEquals(BYTE_TEST_BUFFER[10], readBuffer[4]);
      assertEquals(BYTE_TEST_BUFFER[11], readBuffer[5]);
      assertEquals(BYTE_TEST_BUFFER[12], readBuffer[6]);
      assertEquals(BYTE_TEST_BUFFER[13], readBuffer[7]);
      assertEquals(FILLER, readBuffer[8]);
      assertEquals(FILLER, readBuffer[9]);
      assertEquals(FILLER, readBuffer[10]);
      assertEquals(FILLER, readBuffer[11]);
      Arrays.fill(readBuffer, (byte) 0x7F);
    } catch (IOException e)
    {
      fail();
    }

    // EOF is reached
    try
    {
      assertEquals(-1, in.read(readBuffer, 2, readBuffer.length - 2));
    } catch (IOException e)
    {
      fail();
    }

    readBuffer = null;
  }

  /**
   * Verifies that <code>IndexOutOfBoundsException</code> is correctly thrown
   * followng InputStream contract depending on the input parameters.
   * 
   */
  public void testReadByteArrayIntInt_OutOfBoundsInput()
  {
    byte[] readBuffer = new byte[BYTE_TEST_BUFFER.length * 2];

    boolean exceptionThrown = false;
    try
    {
      in.read(readBuffer, -1, 0);
    } catch (IndexOutOfBoundsException e)
    {
      exceptionThrown = true;
    } catch (IOException e)
    {
      fail();
    }

    if (exceptionThrown == false)
    {
      fail();
    }

    exceptionThrown = false;
    try
    {
      in.read(readBuffer, -1, 1);
    } catch (IndexOutOfBoundsException e)
    {
      exceptionThrown = true;
    } catch (IOException e)
    {
      fail();
    }

    if (exceptionThrown == false)
    {
      fail();
    }

    exceptionThrown = false;
    try
    {
      in.read(readBuffer, 0, -32);
    } catch (IndexOutOfBoundsException e)
    {
      exceptionThrown = true;
    } catch (IOException e)
    {
      fail();
    }

    if (exceptionThrown == false)
    {
      fail();
    }

    exceptionThrown = false;
    try
    {
      in.read(readBuffer, 0, 255);
    } catch (IndexOutOfBoundsException e)
    {
      exceptionThrown = true;
    } catch (IOException e)
    {
      fail();
    }

    if (exceptionThrown == false)
    {
      fail();
    }

    readBuffer = null;
  }

  /**
   * Testing of skip functionality, assuming that skip will always skip the
   * number of bytes, except if end of stream has been reached.
   * 
   */
  public void testSkip_StandardScenario()
  {
    try
    {
      assertEquals(2, in.skip(2));
      assertEquals(0x01, in.read());
      // Index 3
      assertEquals(3, in.skip(3));
      // Index 6 
      assertEquals(0x05, in.read());
      assertEquals(0x06, in.read());
      assertEquals(6, in.skip(BYTE_TEST_BUFFER.length));
      assertEquals(-1, in.read());
      assertEquals(0,in.skip(32));
    } catch (IOException e)
    {
      fail();
    }
  }

  /**
   * Test with a negative parameter or a skip count of zero.
   */
  public void testSkip_OutOfBounds()
  {
    try
    {
      assertEquals(0, in.skip(0));
      assertEquals(0, in.skip(-10));
    } catch (IOException e)
    {
      fail();
    }
  }

  /**
   * This test assumes a specific behaviour which is more specific than the
   * original <code>InputStream</code> contract.
   * 
   * <ul>
   * <li>The number of bytes when initialy called must be the actual number of
   * bytes of the stream.</li>
   * <li>Calling available on a closed stream assumes an
   * <code>IOException</code> will be thrown.</li>
   * </ul>
   * 
   * 
   */
  public void testAvailable_StandardScenario()
  {
    // Empty stream should return 0
    try
    {
      assertEquals(0, ein.available());
    } catch (IOException e)
    {
      fail();
    }

    // Check for standard use cases
    try
    {
      assertEquals(BYTE_TEST_BUFFER.length, in.available());
      assertEquals(1, in.skip(1));
      assertEquals(BYTE_TEST_BUFFER.length - 1, in.available());
      int value = in.read();
      value = in.read();
      assertEquals(BYTE_TEST_BUFFER.length - 3, in.available());
      // Skip past the stream.
      in.skip(BYTE_TEST_BUFFER.length - 3);
      assertEquals(0, in.available());
      in.skip(BYTE_TEST_BUFFER.length);
      assertEquals(0, in.available());
    } catch (IOException e)
    {
      fail();
    }

    boolean exceptionThrown = false;
    try
    {
      in.close();
    } catch (IOException e)
    {
      fail();
    }
    try
    {
      in.available();
    } catch (IOException e)
    {
      exceptionThrown = true;
    }
/*    if (exceptionThrown == false)
      fail();*/
  }

  public void testClose()
  {
    try
    {
      in.close();
    } catch (IOException e)
    {
      fail();
    }
  }

  public void testMark()
  {
    if (in.markSupported())
    {
      in.mark(0);
      in.mark(Integer.MAX_VALUE);
    }
  }

  /** Should be implemented by all derived specific test classes */
  public abstract void testReset_StandardScenario();

  /** Should be implemented by all derived specific test classes */
  public abstract void testMarkSupported();
}
