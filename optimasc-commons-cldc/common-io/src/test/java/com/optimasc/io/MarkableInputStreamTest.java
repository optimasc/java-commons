package com.optimasc.io;

import java.io.IOException;
import java.util.Arrays;

public abstract class MarkableInputStreamTest extends InputStreamTest
{
  
  /** Tests the full access to reset, mimics seeking the data. 
   *  It assumes this behaviour:
   *  
   *  <ul>
   *   <li>If <code>mark</code> has not been called, <code>reset</code>
   *   seeks back to the start of the stream.</code>.</li>
   *   <li>Assumes that <code>readLimit</code> of <code>mark</code> is not
   *   used and seeking anywhere in the stream is allowed.</li>
   *  </ul>
   */
  public void testReset_StandardScenario()
  {
    assertEquals(true,in.markSupported());
    
    // Testing of reset without mark being called first.
    byte[] readBuffer = new byte[BYTE_TEST_BUFFER.length * 2];
    final byte FILLER = (byte) 0x7F;
    Arrays.fill(readBuffer, FILLER);

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
      
      assertEquals(6, in.available());
      // Reset to start of stream
      in.reset();
      assertEquals(BYTE_TEST_BUFFER.length, in.available());
      
      
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
      
      assertEquals(6, in.available());
    } catch (IOException e)
    {
      fail();
    }
    
    // Call mark on the stream.
    try
    {
      // Reset to start of stream
      in.reset();
      assertEquals(BYTE_TEST_BUFFER.length, in.available());
      
      assertEquals(3,in.skip(3));
      in.mark(Integer.MAX_VALUE);
      assertEquals(BYTE_TEST_BUFFER.length-3,in.available());
      
      assertEquals(6, in.read(readBuffer, 2, 6));
      assertEquals(BYTE_TEST_BUFFER[3], readBuffer[2]);
      assertEquals(BYTE_TEST_BUFFER[4], readBuffer[3]);
      assertEquals(BYTE_TEST_BUFFER[5], readBuffer[4]);
      assertEquals(BYTE_TEST_BUFFER[6], readBuffer[5]);
      assertEquals(BYTE_TEST_BUFFER[7], readBuffer[6]);
      assertEquals(BYTE_TEST_BUFFER[8], readBuffer[7]);
      assertEquals(FILLER, readBuffer[8]);
      assertEquals(FILLER, readBuffer[9]);
      
      assertEquals(BYTE_TEST_BUFFER[9],in.read());
      assertEquals(BYTE_TEST_BUFFER[10],(byte)in.read());

      Arrays.fill(readBuffer, (byte) 0x7F);
      
      // Go back to position 3
      in.reset();
      assertEquals(BYTE_TEST_BUFFER.length-3,in.available());
      
      assertEquals(6, in.read(readBuffer, 2, 6));
      assertEquals(BYTE_TEST_BUFFER[3], readBuffer[2]);
      assertEquals(BYTE_TEST_BUFFER[4], readBuffer[3]);
      assertEquals(BYTE_TEST_BUFFER[5], readBuffer[4]);
      assertEquals(BYTE_TEST_BUFFER[6], readBuffer[5]);
      assertEquals(BYTE_TEST_BUFFER[7], readBuffer[6]);
      assertEquals(BYTE_TEST_BUFFER[8], readBuffer[7]);
      assertEquals(FILLER, readBuffer[8]);
      assertEquals(FILLER, readBuffer[9]);
      
      assertEquals(BYTE_TEST_BUFFER[9],in.read());
      assertEquals(BYTE_TEST_BUFFER[10],(byte)in.read());
      
      
    } catch (IOException e)
    {
       fail();
    }
    
    
    
  }

  public void testMarkSupported()
  {
    assertEquals(true,in.markSupported());
  }

}
