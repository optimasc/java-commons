package com.optimasc.text;

import junit.framework.TestCase;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Locale;


public class ParseStringTests extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  

    /* ---------- null targetClass ---------- */

    public void testNullTargetClassReturnsTrimmedString() throws Exception
    {
      Object result = Parsers.parseString(null, "  abc  ");
      assertEquals("abc", result);
    }

    /* ---------- BigDecimal ---------- */

    public void testBigDecimalParsing() throws Exception
    {
      Object result = Parsers.parseString(BigDecimal.class, "123.45");
      assertTrue(result instanceof BigDecimal);
      assertEquals(new BigDecimal("123.45"), result);
    }

    /* ---------- BigInteger ---------- */

    public void testBigIntegerParsingStandard() throws Exception
    {
      Object result = Parsers.parseString(BigInteger.class, "12345");
      assertTrue(result instanceof BigInteger);
      assertEquals(new BigInteger("12345"), result);
    }
    
    public void testBigIntegerParsingPositive() throws Exception
    {
      Object result = Parsers.parseString(BigInteger.class, "+12345");
      assertTrue(result instanceof BigInteger);
      assertEquals(new BigInteger("12345"), result);
    }
    
    public void testBigIntegerParsingNegative() throws Exception
    {
      Object result = Parsers.parseString(BigInteger.class, "-12345");
      assertTrue(result instanceof BigInteger);
      assertEquals(new BigInteger("-12345"), result);
    }
    
    public void testBigIntegerParsingNegativeZero() throws Exception
    {
      Object result = Parsers.parseString(BigInteger.class, "-012345");
      assertTrue(result instanceof BigInteger);
      assertEquals(new BigInteger("-12345"), result);
    }
    

    /* ---------- Long ---------- */

    public void testLongParsing() throws Exception
    {
      Object result = Parsers.parseString(Long.class, "123");
      assertEquals(new Long(123), result);
    }

    /* ---------- Integer ---------- */

    public void testIntegerParsing() throws Exception
    {
      Object result = Parsers.parseString(Integer.class, "42");
      assertEquals(new Integer(42), result);
    }

    public void testIntegerOutOfRange() throws Exception
    {
      try
      {
        Parsers.parseString(Integer.class,
            Long.toString((long) Integer.MAX_VALUE + 1));
        fail("Expected ParseException");
      }
      catch (ParseException expected)
      {
        // expected
      }
    }

    /* ---------- Short ---------- */

    public void testShortParsing() throws Exception
    {
      Object result = Parsers.parseString(Short.class, "10");
      assertEquals(new Short((short) 10), result);
    }

    public void testShortOutOfRange() throws Exception
    {
      try
      {
        Parsers.parseString(Short.class,
            Integer.toString(Short.MAX_VALUE + 1));
        fail("Expected ParseException");
      }
      catch (ParseException expected)
      {
        // expected
      }
    }

    /* ---------- Byte ---------- */

    public void testByteParsing() throws Exception
    {
      Object result = Parsers.parseString(Byte.class, "5");
      assertEquals(new Byte((byte) 5), result);
    }

    public void testByteOutOfRange() throws Exception
    {
      try
      {
        Parsers.parseString(Byte.class, "200");
        fail("Expected ParseException");
      }
      catch (ParseException expected)
      {
        // expected
      }
    }

    /* ---------- Double / Float ---------- */

    public void testDoubleParsing() throws Exception
    {
      Object result = Parsers.parseString(Double.class, "1.5");
      assertEquals(new Double(1.5), result);
    }

    public void testFloatParsing() throws Exception
    {
      Object result = Parsers.parseString(Float.class, "2.5");
      assertEquals(new Float(2.5f),result);
    }

    /* ---------- Boolean ---------- */

    public void testBooleanTrueVariants() throws Exception
    {
      assertEquals(Boolean.TRUE,
          Parsers.parseString(Boolean.class, "true"));
      assertEquals(Boolean.TRUE,
          Parsers.parseString(Boolean.class, "1"));
    }

    public void testBooleanFalseVariants() throws Exception
    {
      assertEquals(Boolean.FALSE,
          Parsers.parseString(Boolean.class, "false"));
      assertEquals(Boolean.FALSE,
          Parsers.parseString(Boolean.class, "0"));
    }

    public void testBooleanInvalid() throws Exception
    {
      try
      {
        Parsers.parseString(Boolean.class, "yes");
        fail("Expected ParseException");
      }
      catch (ParseException expected)
      {
        // expected
      }
    }

    /* ---------- Locale ---------- */

    public void testLocaleParsing() throws Exception
    {
      Object result = Parsers.parseString(Locale.class, "en-US");
      assertTrue(result instanceof Locale);
      Locale locale = (Locale) result;
      assertEquals("en", locale.getLanguage());
      assertEquals("US", locale.getCountry());
    }

    /* ---------- String ---------- */

    public void testStringParsing() throws Exception
    {
      Object result = Parsers.parseString(String.class, " abc ");
      assertEquals("abc", result);
    }

    /* ---------- StringBuffer ---------- */

    public void testStringBufferParsing() throws Exception
    {
      Object result = Parsers.parseString(StringBuffer.class, "abc");
      assertTrue(result instanceof StringBuffer);
      assertEquals("abc", result.toString());
    }

    /* ---------- Calendar ---------- */

    public void testCalendarParsing() throws Exception
    {
      Object result = Parsers.parseString(Calendar.class,
          "2024-01-01T10:00:00Z");
      assertTrue(result instanceof Calendar);
    }

    /* ---------- URI ---------- */

    public void testURIParsing() throws Exception
    {
      Object result = Parsers.parseString(URI.class,
          "http://example.com");
      assertTrue(result instanceof URI);
      assertEquals("http://example.com", result.toString());
    }

    /* ---------- byte[] ---------- */

    public void testHexBinaryParsing() throws Exception
    {
      byte[] result = (byte[]) Parsers.parseString(byte[].class, "0A0B0C");
      assertEquals(3, result.length);
    }

    /* ---------- String[] ---------- */

    public void testStringArrayParsing() throws Exception
    {
      String[] result = (String[]) Parsers.parseString(String[].class,
          "a;b;c");
      assertEquals(3, result.length);
      assertEquals("a", result[0]);
      assertEquals("b", result[1]);
      assertEquals("c", result[2]);
    }

    /* ---------- Unsupported class ---------- */

    public void testUnsupportedTargetClass() throws Exception
    {
      try
      {
        Parsers.parseString(Thread.class, "test");
        fail("Expected IllegalArgumentException");
      }
      catch (IllegalArgumentException expected)
      {
        // expected
      }
    }
    

}
