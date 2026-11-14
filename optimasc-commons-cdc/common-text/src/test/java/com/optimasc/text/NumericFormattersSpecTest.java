package com.optimasc.text;

import java.math.BigInteger;
import java.text.ParseException;

import junit.framework.TestCase;

/** Tests of the numeric converters against the LDAP and XML Schema syntaxes. */
public class NumericFormattersSpecTest extends TestCase
{
  private static void assertParseFails(DataConverter converter, String value)
  {
    try
    {
      converter.parseObject(value);
      fail("Expected ParseException for '" + value + "'");
    } catch (ParseException e)
    {
    }
  }

  private static void assertFormatFails(DataConverter converter, Object value)
  {
    try
    {
      converter.format(value);
      fail("Expected IllegalArgumentException for '" + value + "'");
    } catch (IllegalArgumentException e)
    {
    }
  }

  public void testLDAPIntegerRules() throws ParseException
  {
    DataConverter[] converters = { NumericFormatters.ByteConverter.getLDAPInstance(),
        NumericFormatters.ShortConverter.getLDAPInstance(),
        NumericFormatters.IntConverter.getLDAPInstance(),
        NumericFormatters.LongConverter.getLDAPInstance(),
        NumericFormatters.IntegerCanonicalConverter.getLDAPInstance() };
    for (int i = 0; i < converters.length; i++)
    {
      DataConverter converter = converters[i];
      assertEquals("0", converter.format(converter.parseObject("0")));
      assertEquals("-123", converter.format(converter.parseObject("-123")));
      assertParseFails(converter, "+5");
      assertParseFails(converter, "05");
      assertParseFails(converter, "-0");
      assertParseFails(converter, "-05");
      assertParseFails(converter, "-");
      assertParseFails(converter, "");
      assertParseFails(converter, "1 ");
      assertParseFails(converter, "--5");
    }
  }

  public void testXSDLexical() throws ParseException
  {
    DataConverter converter = NumericFormatters.IntConverter.getInstance();
    assertEquals(new Integer(5), converter.parseObject("+005"));
    assertEquals(new Integer(0), converter.parseObject("-0"));
    assertEquals("5", converter.format(converter.parseObject("+005")));
    assertParseFails(converter, "-");
    assertParseFails(converter, "--5");
  }

  public void testRanges() throws ParseException
  {
    assertEquals(new Integer(-128), NumericFormatters.ByteConverter.getLDAPInstance()
        .parseObject("-128"));
    assertParseFails(NumericFormatters.ByteConverter.getLDAPInstance(), "128");
    assertParseFails(NumericFormatters.ShortConverter.getLDAPInstance(), "-32769");
    assertEquals(new Long(Long.MIN_VALUE), NumericFormatters.LongConverter.getLDAPInstance()
        .parseObject("-9223372036854775808"));
    // Used to wrap around to a negative value.
    assertParseFails(NumericFormatters.LongConverter.getLDAPInstance(),
        "9223372036854775808");
    assertParseFails(NumericFormatters.LongConverter.getLDAPInstance(),
        "99999999999999999999999");
    assertEquals(new Long(4294967295L), NumericFormatters.unsignedIntConverter
        .getLDAPInstance().parseObject("4294967295"));
    assertParseFails(NumericFormatters.unsignedIntConverter.getLDAPInstance(), "4294967296");
    assertParseFails(NumericFormatters.unsignedByteConverter.getLDAPInstance(), "-1");
  }

  public void testUInt64() throws ParseException
  {
    DataConverter converter = NumericFormatters.unsignedLongConverter.getLDAPInstance();
    BigInteger max = new BigInteger("18446744073709551615");
    assertEquals(max, converter.parseObject("18446744073709551615"));
    assertEquals("18446744073709551615", converter.format(max));
    assertEquals(new BigInteger("844674407370955"), converter.parseObject("844674407370955"));
    assertParseFails(converter, "18446744073709551616");
    assertParseFails(converter, "-1");
    assertFormatFails(converter, max.add(BigInteger.valueOf(1)));
  }

  public void testPercentage() throws ParseException
  {
    DataConverter converter = NumericFormatters.PercentageConverter.getInstance();
    assertEquals(new Integer(0), converter.parseObject("0"));
    assertEquals(new Integer(99), converter.parseObject("99"));
    assertEquals(new Integer(100), converter.parseObject("100"));
    assertEquals("100", converter.format(new Integer(100)));
    assertParseFails(converter, "101");
    assertParseFails(converter, "-1");
    assertParseFails(converter, "050");
    assertFormatFails(converter, new Integer(101));
  }

  public void testIntegerFormatRejectsFractions()
  {
    assertEquals("5", NumericFormatters.IntConverter.getInstance().format(new Double(5.0)));
    assertFormatFails(NumericFormatters.IntConverter.getInstance(), new Double(1.5));
    assertFormatFails(NumericFormatters.IntConverter.getInstance(), new Long(1L << 40));
    assertFormatFails(NumericFormatters.IntConverter.getInstance(), "5");
  }

  public void testReal() throws ParseException
  {
    DataConverter converter = NumericFormatters.RealConverter.getInstance();
    // Examples of the Real syntax.
    assertEquals(new Double(3.14159), converter.parseObject("3.14159"));
    assertEquals(new Double(Double.NEGATIVE_INFINITY), converter.parseObject("MINUS-INFINITY"));
    assertEquals(new Double(0), converter.parseObject("0"));
    assertEquals(new Double(-53000), converter.parseObject("-5.3E4"));
    assertEquals(new Double(5000), converter.parseObject("5E3"));
    assertTrue(((Double) converter.parseObject("NOT-A-NUMBER")).isNaN());
    assertEquals("PLUS-INFINITY", converter.format(converter.parseObject("PLUS-INFINITY")));
    assertEquals("NOT-A-NUMBER", converter.format(new Double(Double.NaN)));
    assertEquals("-0.0", converter.format(converter.parseObject("-0")));
    assertEquals("0.5", converter.format(converter.parseObject("0.5")));
    assertEquals("1.0E-5", converter.format(converter.parseObject("1E-5")));
    // Leading zeros, plus signs, lower case exponent and missing digits.
    assertParseFails(converter, "00.5");
    assertParseFails(converter, "01");
    assertParseFails(converter, "+1");
    assertParseFails(converter, "1E+5");
    assertParseFails(converter, "1e5");
    assertParseFails(converter, ".5");
    assertParseFails(converter, "5.");
    assertParseFails(converter, "1E");
    assertParseFails(converter, "INF");
    assertParseFails(converter, "NaN");
    assertParseFails(converter, "");
    // Too large for a double.
    assertParseFails(converter, "1E400");
  }

  public void testRealRoundTrip() throws ParseException
  {
    DataConverter converter = NumericFormatters.Float64Converter.getInstance();
    double[] values = { 0.1, -1.0 / 3, Double.MAX_VALUE, Double.MIN_VALUE, 1e21, 123456789.0 };
    for (int i = 0; i < values.length; i++)
    {
      Double value = new Double(values[i]);
      assertEquals(value, converter.parseObject(converter.format(value)));
    }
  }

  public void testFloat32() throws ParseException
  {
    DataConverter converter = NumericFormatters.Float32Converter.getInstance();
    assertEquals(new Float(3.14159f), converter.parseObject("3.14159"));
    assertEquals(new Float(-53000f), converter.parseObject("-5.3E4"));
    assertEquals("3.14159", converter.format(converter.parseObject("3.14159")));
    assertEquals("MINUS-INFINITY", converter.format(new Float(Float.NEGATIVE_INFINITY)));
    // Too large for a float, but not for a double.
    assertParseFails(converter, "1E39");
    assertFormatFails(converter, new Double(1.0));
  }

  public void testXSDDouble() throws ParseException
  {
    DataConverter converter = NumericFormatters.XSDDoubleConverter.getInstance();
    assertEquals(new Double(12.5), converter.parseObject("+12.5"));
    assertEquals(new Double(0.5), converter.parseObject(".5"));
    assertEquals(new Double(5), converter.parseObject("5."));
    assertEquals(new Double(1e5), converter.parseObject("1e+5"));
    assertEquals(new Double(Double.POSITIVE_INFINITY), converter.parseObject("+INF"));
    assertEquals(new Double(Double.POSITIVE_INFINITY), converter.parseObject("1E400"));
    assertEquals("1.25E2", converter.format(new Double(125)));
    assertEquals("1.0E0", converter.format(new Double(1)));
    assertEquals("-1.0E-3", converter.format(new Double(-0.001)));
    assertEquals("0.0E0", converter.format(new Double(0)));
    assertEquals("-0.0E0", converter.format(new Double(-0.0)));
    assertEquals("1.0E21", converter.format(new Double(1e21)));
    assertEquals("INF", converter.format(new Double(Double.POSITIVE_INFINITY)));
    assertEquals("-INF", converter.format(new Double(Double.NEGATIVE_INFINITY)));
    assertEquals("NaN", converter.format(new Double(Double.NaN)));
    assertParseFails(converter, ".");
    assertParseFails(converter, "1E");
    assertParseFails(converter, "nan");
    assertParseFails(converter, "-NaN");
    assertParseFails(converter, "PLUS-INFINITY");

    converter = NumericFormatters.XSDFloatConverter.getInstance();
    assertEquals(new Float(0.1f), converter.parseObject("0.1"));
    assertEquals("1.0E-1", converter.format(new Float(0.1f)));
  }
}
