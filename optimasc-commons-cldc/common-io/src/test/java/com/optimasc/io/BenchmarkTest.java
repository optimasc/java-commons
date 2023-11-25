package com.optimasc.io;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class BenchmarkTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testBenchmark()
  {
    byte[] buffer = new byte[32768 * 1024];
    long count = 0;
    long readValue = 0;
    for (int i = 0; i < buffer.length; i++)
    {
      buffer[0] = (byte) (i & 0xFF);
    }

    InputStream is = new ByteArrayInputStream(buffer);
    long timeElapsed = 0;
    
    int iterationCount = 20;

    for (int i = 0; i < iterationCount; i++)
    {
      try
      {
        is.reset();
        long beforeBenchmark = System.currentTimeMillis();
        readValue = is.read();
        while (readValue != -1)
        {
          readValue += is.read();
        }
        long afterBenchmark = System.currentTimeMillis();
        timeElapsed = timeElapsed + (afterBenchmark - beforeBenchmark);
      } catch (IOException e)
      {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
    }
    System.out.println(readValue);
    System.out.println(timeElapsed / iterationCount);

  }

}
