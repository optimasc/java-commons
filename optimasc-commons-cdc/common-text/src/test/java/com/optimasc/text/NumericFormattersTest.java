package com.optimasc.text;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

public class NumericFormattersTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testParseNumber()
  {
    long value;
    String input;
    int startPos = 0;
    int endPos = 0;
    boolean allowPlusSign = false;
    boolean allowNegativeSign = false;
    boolean leadingZero = false;
    boolean fail = true;

    /********* Valid cases **********/

    // Standard numeric value, with no specific option allowed
    fail = true;
    input = "123456789";
    startPos = 0;
    endPos = input.length();
    allowPlusSign = false;
    allowNegativeSign = false;
    leadingZero = false;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
      assertEquals(123456789, value);
    } catch (ParseException e)
    {
      fail();
    }

    // Use-case: Plus sign, negative and plus allowed, and positive number
    fail = true;
    input = "AB+1ZZ";
    startPos = 2;
    endPos = 4;
    allowPlusSign = true;
    allowNegativeSign = true;
    leadingZero = true;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
      assertEquals(1, value);
    } catch (ParseException e)
    {
      fail();
    }

    // Use-case: Plus sign, negative and plus allowed, start and end positions
    // for parsing different than a full string.
    fail = true;
    input = "A+123456789Z";
    startPos = 1;
    endPos = 11;
    allowPlusSign = true;
    allowNegativeSign = true;
    leadingZero = false;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
      assertEquals(123456789, value);
    } catch (ParseException e)
    {
      fail();
    }

    // Use-case: Negative sign, negative allowed, start and end positions
    // for parsing different than a full string.
    fail = true;
    input = "A-123456789Z";
    startPos = 1;
    endPos = 11;
    allowPlusSign = false;
    allowNegativeSign = true;
    leadingZero = false;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
      assertEquals(-123456789, value);
    } catch (ParseException e)
    {
      fail();
    }

    // Use-case: Zero value, with no leading zeros allowed
    fail = true;
    input = "0";
    startPos = 0;
    endPos = 1;
    allowPlusSign = false;
    allowNegativeSign = true;
    leadingZero = false;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
      assertEquals(0, value);
    } catch (ParseException e)
    {
      fail();
    }

    /********* Error cases **********/
    fail = true;
    input = "";
    startPos = 0;
    endPos = input.length();
    allowPlusSign = false;
    allowNegativeSign = false;
    leadingZero = false;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    // Use-case: Negative number input with negative sign not allowed.
    fail = true;
    input = "-123456789";
    startPos = 0;
    endPos = input.length();
    allowPlusSign = false;
    allowNegativeSign = false;
    leadingZero = false;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    // Use-case: Explicit positive number input with positive sign not allowed.
    fail = true;
    input = "+12";
    startPos = 0;
    endPos = 3;
    allowPlusSign = false;
    allowNegativeSign = false;
    leadingZero = false;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    // Use-case: Zero value with some leading characters, with no leading zeros allowed
    fail = true;
    input = "00";
    startPos = 0;
    endPos = 2;
    allowPlusSign = false;
    allowNegativeSign = true;
    leadingZero = false;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    // Use-case: Integer numeric value that contain non-digit characters
    fail = true;
    input = "123ABC789";
    startPos = 0;
    endPos = 9;
    allowPlusSign = true;
    allowNegativeSign = true;
    leadingZero = true;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
    } catch (ParseException e)
    {
      assertEquals(3, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    // Use-case: Integer numeric value that contain non-digit characters
    fail = true;
    input = "+1234BC789";
    startPos = 0;
    endPos = 10;
    allowPlusSign = true;
    allowNegativeSign = true;
    leadingZero = true;
    try
    {
      value = Parsers.parseNumber(input, startPos, endPos, allowPlusSign,
          allowNegativeSign, leadingZero);
    } catch (ParseException e)
    {
      assertEquals(5, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

  }

  /** Tests integer conversion valid use-cases */
  public void testIntegerConverterValid() throws ParseException
  {
    DataConverter converter = new NumericFormatters.IntegerConverter();
    assertEquals(BigInteger.class, converter.getClassType());

    String strValue = "0";
    Number value = (Number) converter.parseObject(strValue);
    assertEquals(0, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "-5555555";
    value = (Number) converter.parseObject(strValue);
    assertEquals(-5555555, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "256";
    value = (Number) converter.parseObject(strValue);
    assertEquals(256, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "-0001";
    value = (Number) converter.parseObject(strValue);
    assertEquals(-1, value.intValue());
    assertEquals("-1", converter.format(value));

  }

  /** Tests integer conversion invalid use-cases */
  public void testIntegerConverterInvalid()
  {
    DataConverter converter = new NumericFormatters.IntegerConverter();
    assertEquals(BigInteger.class, converter.getClassType());

    boolean fail;

    String strValue;
    BigInteger value;

    try
    {
      fail = true;
      strValue = "";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "ABCDEF";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "01Z";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(2, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "+12";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "1E10";
      value = (BigInteger) converter.parseObject(strValue);
      assertEquals(-1, value.intValue());
      assertEquals("-1", value.toString());
    } catch (ParseException e)
    {
      assertEquals(1, e.getErrorOffset());
      fail = false;
    }

  }

  /** Tests integer conversion valid use-cases */
  public void testIntegerCanonicalValidStrict() throws ParseException
  {
    DataConverter converter = new NumericFormatters.IntegerCanonicalConverter();
    converter.setLenient(false);
    assertEquals(BigInteger.class, converter.getClassType());

    String strValue = "0";
    Number value = (Number) converter.parseObject(strValue);
    assertEquals(0, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "-5555555";
    value = (Number) converter.parseObject(strValue);
    assertEquals(-5555555, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "256";
    value = (Number) converter.parseObject(strValue);
    assertEquals(256, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "-1";
    value = (Number) converter.parseObject(strValue);
    assertEquals(-1, value.intValue());
    assertEquals("-1", converter.format(value));

    strValue = "12";
    value = (Number) converter.parseObject(strValue);
    assertEquals(12, value.intValue());
    assertEquals("12", converter.format(value));

  }

  public void testIntegerCanonicalValidLenient() throws ParseException
  {
    DataConverter converter = new NumericFormatters.IntegerCanonicalConverter();
    assertEquals(BigInteger.class, converter.getClassType());

    String strValue = "0";
    Number value = (Number) converter.parseObject(strValue);
    assertEquals(0, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "-5555555";
    value = (Number) converter.parseObject(strValue);
    assertEquals(-5555555, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "256";
    value = (Number) converter.parseObject(strValue);
    assertEquals(256, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "-0001";
    value = (Number) converter.parseObject(strValue);
    assertEquals(-1, value.intValue());
    assertEquals("-1", converter.format(value));

    strValue = "014";
    value = (Number) converter.parseObject(strValue);
    assertEquals(14, value.intValue());
    assertEquals("14", converter.format(value));

    strValue = "+12";
    value = (Number) converter.parseObject(strValue);
    assertEquals(12, value.intValue());
    assertEquals("12", converter.format(value));

  }

  /**
   * Tests non-negative integer conversion valid use-cases when lenient is false
   */
  public void testnonNegativeIntegerCanonicalValid() throws ParseException
  {
    DataConverter converter = new NumericFormatters.nonNegativeIntegerConverter();
    converter.setLenient(false);

    String strValue = "0";
    Number value = (Number) converter.parseObject(strValue);
    assertEquals(0, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "5555555";
    value = (Number) converter.parseObject(strValue);
    assertEquals(5555555, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "256";
    value = (Number) converter.parseObject(strValue);
    assertEquals(256, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "1";
    value = (Number) converter.parseObject(strValue);
    assertEquals(1, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "12";
    value = (Number) converter.parseObject(strValue);
    assertEquals(12, value.intValue());
    assertEquals(strValue, converter.format(value));

  }

  /** Tests non-negative integer conversion valid use-cases when lenient is true */
  public void testnonNegativeIntegerValid() throws ParseException
  {
    DataConverter converter = new NumericFormatters.nonNegativeIntegerConverter();
    assertEquals(true, converter.isLenient());

    String strValue = "0";
    Number value = (Number) converter.parseObject(strValue);
    assertEquals(0, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "05555555";
    value = (Number) converter.parseObject(strValue);
    assertEquals(5555555, value.intValue());
    assertEquals("5555555", converter.format(value));

    strValue = "256";
    value = (Number) converter.parseObject(strValue);
    assertEquals(256, value.intValue());
    assertEquals(strValue, converter.format(value));

    strValue = "+1";
    value = (Number) converter.parseObject(strValue);
    assertEquals(1, value.intValue());
    assertEquals("1", converter.format(value));

    strValue = "12";
    value = (Number) converter.parseObject(strValue);
    assertEquals(12, value.intValue());
    assertEquals(strValue, converter.format(value));

  }

  /** Tests non-negative integer conversion invalid use-cases */
  public void testNonNegativeIntegerCanonicalConverterInvalid()
  {
    DataConverter converter = new NumericFormatters.nonNegativeIntegerConverter();
    assertEquals(true, converter.isLenient());
    converter.setLenient(false);
    boolean fail;

    String strValue;
    BigInteger value;

    try
    {
      fail = true;
      strValue = "";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "ABCDEF";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "01Z";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "-10";
      value = (BigInteger) converter.parseObject(strValue);
      assertEquals(-1, value.intValue());
      assertEquals("-1", value.toString());
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }

    try
    {
      fail = true;
      strValue = "-0";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }

    try
    {
      fail = true;
      strValue = "0012";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }

    try
    {
      fail = true;
      strValue = "+14";
      value = (BigInteger) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
  }

  /** Tests integer conversion invalid use-cases */
  public void testIntegerCanonicalConverterInvalid()
  {
    DataConverter converter = new NumericFormatters.IntegerCanonicalConverter();
    assertEquals(true, converter.isLenient());
    converter.setLenient(false);
    assertEquals(BigInteger.class, converter.getClassType());
    boolean fail;

    String strValue;
    Number value;

    try
    {
      fail = true;
      strValue = "";
      value = (Number) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "ABCDEF";
      value = (Number) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "01Z";
      value = (Number) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "1E10";
      value = (Number) converter.parseObject(strValue);
      assertEquals(-1, value.intValue());
      assertEquals("-1", value.toString());
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }

    try
    {
      fail = true;
      strValue = "-0";
      value = (Number) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(1, e.getErrorOffset());
      fail = false;
    }

    try
    {
      fail = true;
      strValue = "0012";
      value = (Number) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }

    try
    {
      fail = true;
      strValue = "+14";
      value = (Number) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0, e.getErrorOffset());
      fail = false;
    }
  }

}
