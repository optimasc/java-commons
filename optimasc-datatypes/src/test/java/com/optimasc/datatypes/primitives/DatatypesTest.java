package com.optimasc.datatypes.primitives;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.TestCase;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.derived.LatinCharType;
import com.optimasc.datatypes.derived.UCS2CharType;
import com.optimasc.datatypes.derived.UCS2StringType;
import com.optimasc.lang.DateTimeConstants;
import com.optimasc.lang.GregorianDateTime;

public class DatatypesTest extends TestCase
{

  public static final int SAMPLE_MIN_LENGTH = 0;
  public static final int SAMPLE_MAX_LENGTH = 1024;

  public static final String SAMPLE_NAME = "DatatypeName";
  public static final String SAMPLE_COMMENT = "This is my comment";

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testBasicDataType(Datatype datatype)
  {
    datatype.setName(SAMPLE_NAME);
    assertEquals(SAMPLE_NAME, datatype.getName());
    datatype.setComment(SAMPLE_COMMENT);
    assertEquals(SAMPLE_COMMENT, datatype.getComment());
  }

  public void testLengthFacetBasic(LengthFacet lengthFacet)
  {
    int oldMinLength = lengthFacet.getMinLength();
    int oldMaxLength = lengthFacet.getMaxLength();

    assertFalse("min. length and max. length should not be equal.", oldMinLength == oldMaxLength);
    assertTrue("min. length should be >= 0.", oldMinLength >= 0);
    assertTrue("max. length should be >= 0.", oldMaxLength >= 0);
    assertTrue("max. length should be >= 0.", oldMaxLength > oldMinLength);

    /* Try to set invalid values. */
    boolean success = false;
    try
    {
      lengthFacet.setMinLength(-1);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertTrue("Negative length is not valid", success);
    success = false;
    try
    {
      lengthFacet.setMinLength(-1);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertTrue("Negative length is not valid", success);
    success = false;

    lengthFacet.setMinLength(SAMPLE_MIN_LENGTH);
    lengthFacet.setMaxLength(SAMPLE_MAX_LENGTH);
    assertEquals(SAMPLE_MIN_LENGTH, lengthFacet.getMinLength());
    assertEquals(SAMPLE_MAX_LENGTH, lengthFacet.getMaxLength());

    lengthFacet.setMinLength(oldMinLength);
    lengthFacet.setMaxLength(oldMaxLength);
  }


  public void testBinaryType()
  {
    boolean success = false;
    BinaryType datatype = new BinaryType();
    BinaryType otherDatatype = new BinaryType();
    testBasicDataType(datatype);
    assertEquals(false, datatype.isOrdered());
    assertEquals(byte[].class, datatype.getClassType());
    assertTrue(byte[].class.isInstance(datatype.getObjectType()));
    assertEquals(otherDatatype, datatype);
    assertEquals(datatype, datatype);

    if (datatype instanceof LengthFacet)
    {
      LengthFacet lengthFacet = (LengthFacet) datatype;
      testLengthFacetBasic(lengthFacet);
    }

    if (datatype instanceof PatternFacet)
    {
      PatternFacet patternFacet = (PatternFacet) datatype;
      Pattern pattern = Pattern.compile(patternFacet.getPattern());
      Matcher m = pattern.matcher("");

      m.reset("0Fa001");
      assertEquals(true, m.matches());
      m.reset("C0");
      assertEquals(true, m.matches());
      m.reset("C");
      assertEquals(false, m.matches());
      m.reset("C1DZ");
      assertEquals(false, m.matches());
    }

    /* Valid use case */
    success = true;
    try
    {
      byte[] b = (byte[]) datatype.parse("0Fa001");
      assertEquals(3, b.length);
      assertEquals(0x0F, b[0] & 0xFF);
      assertEquals(0xA0, b[1] & 0xFF);
      assertEquals(0x01, b[2] & 0xFF);
    } catch (ParseException e)
    {
      fail();
    }

    /* Invalid use case */
    success = false;
    try
    {
      byte[] b = (byte[]) datatype.parse("ZFa001");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Invalid use case - length facet */
    success = false;
    try
    {
      datatype.setMaxLength(1);
      byte[] b = (byte[]) datatype.parse("0Fa001");
      assertEquals(3, b.length);
      assertEquals(0x0F, b[0] & 0xFF);
      assertEquals(0xA0, b[1] & 0xFF);
      assertEquals(0x01, b[2] & 0xFF);
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Equals check */
    assertFalse(new IntegralType().equals(datatype));
    /* Since we changed the length, the datatypes should not be equal. */
    assertFalse(otherDatatype.equals(datatype));

  }

  public void testBooleanType()
  {
    boolean success = false;
    BooleanType datatype = new BooleanType();
    BooleanType otherDatatype = new BooleanType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(Boolean.class, datatype.getClassType());
    assertTrue(Boolean.class.isInstance(datatype.getObjectType()));
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    if (datatype instanceof LengthFacet)
    {
      LengthFacet lengthFacet = (LengthFacet) datatype;
      testLengthFacetBasic(lengthFacet);
    }

    if (datatype instanceof PatternFacet)
    {
      PatternFacet patternFacet = (PatternFacet) datatype;
      Pattern pattern = Pattern.compile(patternFacet.getPattern());
      Matcher m = pattern.matcher("");

      m.reset("TRUE");
      assertEquals(true, m.matches());
      m.reset("FALSE");
      assertEquals(true, m.matches());
      m.reset("0");
      assertEquals(true, m.matches());
      m.reset("1");
      assertEquals(true, m.matches());
      m.reset("true");
      assertEquals(true, m.matches());
      m.reset("false");
      assertEquals(true, m.matches());

      m.reset("C1DZ");
      assertEquals(false, m.matches());
    }

    /* Valid use case */
    success = true;
    try
    {
      Boolean b;
      b = (Boolean) datatype.parse("true");
      assertEquals(Boolean.TRUE, b);
      b = (Boolean) datatype.parse("false");
      assertEquals(Boolean.FALSE, b);

      b = (Boolean) datatype.parse("TRUE");
      assertEquals(Boolean.TRUE, b);
      b = (Boolean) datatype.parse("FALSE");
      assertEquals(Boolean.FALSE, b);

      b = (Boolean) datatype.parse("True");
      assertEquals(Boolean.TRUE, b);
      b = (Boolean) datatype.parse("False");
      assertEquals(Boolean.FALSE, b);

      b = (Boolean) datatype.parse("1");
      assertEquals(Boolean.TRUE, b);
      b = (Boolean) datatype.parse("0");
      assertEquals(Boolean.FALSE, b);

    } catch (ParseException e)
    {
      fail();
    }

    /* Invalid use case */
    success = false;
    try
    {
      byte[] b = (byte[]) datatype.parse("ZFa001");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Equals check */
    assertFalse(new IntegralType().equals(datatype));

  }

  public void testTimeType()
  {
    boolean success = false;
    TimeType datatype = new TimeType();
    TimeType otherDatatype = new TimeType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(GregorianDateTime.class, datatype.getClassType());
    assertTrue(GregorianDateTime.class.isInstance(datatype.getObjectType()));
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    if (datatype instanceof LengthFacet)
    {
      LengthFacet lengthFacet = (LengthFacet) datatype;
      testLengthFacetBasic(lengthFacet);
    }

    if (datatype instanceof PatternFacet)
    {
      PatternFacet patternFacet = (PatternFacet) datatype;
      Pattern pattern = Pattern.compile(patternFacet.getPattern());
      Matcher m = pattern.matcher("");

      m.reset("00:00:00");
      assertEquals(true, m.matches());
      m.reset("15:45:54.900");
      assertEquals(true, m.matches());
      m.reset("15:45:54Z");
      assertEquals(true, m.matches());
      m.reset("15:45:54.90Z");
      assertEquals(true, m.matches());
      m.reset("15:45:54.90+00:30");
      assertEquals(true, m.matches());

      // Invalid cases
      m.reset("ZFa001");
      assertEquals(false, m.matches());
      m.reset("01");
      assertEquals(false, m.matches());
      m.reset("31:");
      assertEquals(false, m.matches());
      m.reset("13:466:");
      assertEquals(false, m.matches());
    }

    /* Valid use case */
    success = true;
    try
    {
      GregorianDateTime c;
      c = (GregorianDateTime) datatype.parse("00:00:00");
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getEra());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getYear());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(00, c.getHour());
      assertEquals(00, c.getMinute());
      assertEquals(00, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      
      c = (GregorianDateTime) datatype.parse("15:45:54.900");
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getEra());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getYear());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(900, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());
      
      
      c = (GregorianDateTime) datatype.parse("15:45:54Z");
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getEra());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getYear());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(00, c.getTimezone());

      c = (GregorianDateTime) datatype.parse("15:45:54.90Z");
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getEra());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getYear());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(90, c.getFractionalSecond());
      assertEquals(00, c.getTimezone());

      c = (GregorianDateTime) datatype.parse("15:45:54.90+00:30");
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getEra());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getYear());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(90, c.getFractionalSecond());
      assertEquals(30, c.getTimezone());
      
      
      c = (GregorianDateTime) datatype.parse("15:45:54.90-10:30");
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getEra());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getYear());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(90, c.getFractionalSecond());
      assertEquals(-630, c.getTimezone());
      
    } catch (ParseException e)
    {
      fail();
    }

    /* Invalid use case */
    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("ZFa001");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("01");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("31:");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("13:466:");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Equals check */
    assertFalse(new IntegralType().equals(datatype));

  }

  public void testDateType()
  {
    boolean success = false;
    DateTimeType datatype = new DateTimeType();
    DateTimeType otherDatatype = new DateTimeType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(GregorianDateTime.class, datatype.getClassType());
    assertTrue(GregorianDateTime.class.isInstance(datatype.getObjectType()));
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    if (datatype instanceof LengthFacet)
    {
      LengthFacet lengthFacet = (LengthFacet) datatype;
      testLengthFacetBasic(lengthFacet);
    }

    if (datatype instanceof PatternFacet)
    {
      PatternFacet patternFacet = (PatternFacet) datatype;
      String patternRegex = patternFacet.getPattern();
      Pattern pattern = Pattern.compile(patternRegex);
      Matcher m = pattern.matcher("");

      m.reset("1989");
      assertEquals(true, m.matches());
      m.reset("9001-12");
      assertEquals(true, m.matches());
      m.reset("9001-01");
      assertEquals(true, m.matches());
      m.reset("1900-01-01");
      assertEquals(true, m.matches());
      m.reset("1900-01-11");
      assertEquals(true, m.matches());
      m.reset("2018-01-29");
      assertEquals(true, m.matches());
      m.reset("2018-11-31");
      assertEquals(true, m.matches());

      m.reset("2018-11-31T00:00:00");
      assertEquals(true, m.matches());
      m.reset("2018-11-31T00:00:00");
      assertEquals(true, m.matches());
      m.reset("2018-11-31T15:45:54.900");
      assertEquals(true, m.matches());
      m.reset("2018-11-31T15:45:54Z");
      assertEquals(true, m.matches());
      m.reset("2018-11-31T15:45:54.90Z");
      assertEquals(true, m.matches());
      m.reset("2018-11-31T15:45:54.90+00:30");
      assertEquals(true, m.matches());

      // Invalid cases
      m.reset("-555555");
      assertEquals(false, m.matches());
      m.reset("1999-");
      assertEquals(false, m.matches());
      m.reset("1999-12-");
      assertEquals(false, m.matches());
      //   m.reset("1999-12-01T00");
      //   assertEquals(false,m.matches());
      //   m.reset("1999-12-01T");
      assertEquals(false, m.matches());
      m.reset("ZFa001");
      assertEquals(false, m.matches());
      m.reset("01");
      assertEquals(false, m.matches());
      m.reset("31:");
      assertEquals(false, m.matches());
      m.reset("13:466:");
      assertEquals(false, m.matches());
    }

    /* Valid use case */
    success = true;
    try
    {
      GregorianDateTime c;
      datatype.setResolution(DateTimeType.RESOLUTION_YEAR);
      c = (GregorianDateTime) datatype.parse("1989");
      assertEquals(1989, c.getYear());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_MONTH);
      c = (GregorianDateTime) datatype.parse("9001-12");
      assertEquals(9001, c.getYear());
      assertEquals(12, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_MONTH);
      c = (GregorianDateTime) datatype.parse("9001-01");
      assertEquals(9001, c.getYear());
      assertEquals(01, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_DAY);
      c = (GregorianDateTime) datatype.parse("1900-01-01");
      assertEquals(1900, c.getYear());
      assertEquals(01, c.getMonth());
      assertEquals(01, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_DAY);
      c = (GregorianDateTime) datatype.parse("1900-01-11");
      assertEquals(1900, c.getYear());
      assertEquals(01, c.getMonth());
      assertEquals(11, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_DAY);
      c = (GregorianDateTime) datatype.parse("2018-01-29");
      assertEquals(2018, c.getYear());
      assertEquals(01, c.getMonth());
      assertEquals(29, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_DAY);
      c = (GregorianDateTime) datatype.parse("2018-11-31");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(31, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDateTime) datatype.parse("2018-11-31T00:01:02");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(31, c.getDay());
      assertEquals(00, c.getHour());
      assertEquals(01, c.getMinute());
      assertEquals(02, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDateTime) datatype.parse("2018-11-31T15:45:54.900");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(31, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(900, c.getFractionalSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDateTime) datatype.parse("2018-11-31T15:45:54Z");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(31, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(00, c.getTimezone());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getFractionalSecond());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDateTime) datatype.parse("2018-11-31T15:45:54.90Z");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(31, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(90, c.getFractionalSecond());
      assertEquals(00, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDateTime) datatype.parse("2018-11-31T15:45:54.45+00:30");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(31, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(45, c.getFractionalSecond());
      assertEquals(30, c.getTimezone());

      //      !!! ADD TIMEZONE SUPPORT!!!

    } catch (ParseException e)
    {
      fail();
    }

    /* Invalid use case */

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("-555555");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("1999-");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("1999-12-");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("01");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("ZFa001");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("31:");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDateTime cal = (GregorianDateTime) datatype.parse("13:466:");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Equals check */
    assertFalse(new IntegralType().equals(datatype));

  }

  public void testIntegralType()
  {
    boolean success = false;
    IntegralType datatype = new IntegralType();
    IntegralType otherDatatype = new IntegralType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(BigInteger.class, datatype.getClassType());
    assertTrue(BigInteger.class.isInstance(datatype.getObjectType()));
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    if (datatype instanceof LengthFacet)
    {
      LengthFacet lengthFacet = (LengthFacet) datatype;
      testLengthFacetBasic(lengthFacet);
    }

    if (datatype instanceof PatternFacet)
    {
      PatternFacet patternFacet = (PatternFacet) datatype;
      Pattern pattern = Pattern.compile(patternFacet.getPattern());
      Matcher m = pattern.matcher("");

      m.reset("00");
      assertEquals(true, m.matches());
      m.reset("-242");
      assertEquals(true, m.matches());
      m.reset("1");
      assertEquals(true, m.matches());
      m.reset("1432345534");
      assertEquals(true, m.matches());

      // Invalid cases
      m.reset("ZFa001");
      assertEquals(false, m.matches());
      m.reset("31:");
      assertEquals(false, m.matches());
      m.reset("13:466:");
      assertEquals(false, m.matches());
      m.reset("00:00:00");
      assertEquals(false, m.matches());
      m.reset("-");
      assertEquals(false, m.matches());
      m.reset("+");
      assertEquals(false, m.matches());
      m.reset("0AB");
      assertEquals(false, m.matches());
    }

    /* Valid use case */
    success = true;
    try
    {
      BigInteger intValue;
      intValue = (BigInteger) datatype.parse("00");
      assertEquals(0, intValue.intValue());

      intValue = (BigInteger) datatype.parse("-242");
      assertEquals(-242, intValue.intValue());

      intValue = (BigInteger) datatype.parse("1");
      assertEquals(1, intValue.intValue());

      intValue = (BigInteger) datatype.parse("1432345534");
      assertEquals(1432345534, intValue.intValue());

    } catch (ParseException e)
    {
      fail();
    }

    /* Invalid use case */
    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("ZFa001");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Invalid use case */
    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("31:");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("13:466:");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("00:00:00");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("-");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("+");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("0AB");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Equals check */
    assertFalse(new TimeType().equals(datatype));


    /** Special range of values and validates its not working as expected. */
    datatype.setMinInclusive(0);
    datatype.setMaxInclusive(63);
    assertEquals(0, datatype.getMinInclusive());
    assertEquals(63, datatype.getMaxInclusive());
    assertEquals(BigInteger.class, datatype.getClassType());
    assertTrue(BigInteger.class.isInstance(datatype.getObjectType()));

    try
    {
      Number intValue;
      intValue = (Number) datatype.parse("00");
      assertEquals(0, intValue.intValue());
      intValue = (Number) datatype.parse("63");
      assertEquals(63, intValue.intValue());
    } catch (Exception e)
    {
      fail();
    }

    /* Invalid use case */
    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("64");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Invalid use case */
    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("-1");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /** Special range of values and validates its not working as expected. */
    datatype.setMinInclusive(-16384);
    datatype.setMaxInclusive(255);
    assertEquals(-16384, datatype.getMinInclusive());
    assertEquals(255, datatype.getMaxInclusive());
    assertEquals(BigInteger.class, datatype.getClassType());
    assertTrue(BigInteger.class.isInstance(datatype.getObjectType()));

    try
    {
      Number intValue;
      intValue = (Number) datatype.parse("-15000");
      assertEquals(-15000, intValue.intValue());
      intValue = (Number) datatype.parse("255");
      assertEquals(255, intValue.intValue());
    } catch (Exception e)
    {
      fail();
    }

    /* Invalid use case */
    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("256");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Invalid use case */
    success = false;
    try
    {
      BigInteger intValue = (BigInteger) datatype.parse("-326789");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

  }

  public void testLatinCharType()
  {
    boolean success = false;
    Character charValue;
    CharacterType datatype = new LatinCharType();
    CharacterType otherDatatype = new LatinCharType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(Character.class, datatype.getClassType());
    assertTrue(Character.class.isInstance(datatype.getObjectType()));
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    if (datatype instanceof LengthFacet)
    {
      LengthFacet lengthFacet = (LengthFacet) datatype;
      testLengthFacetBasic(lengthFacet);
    }

    if (datatype instanceof PatternFacet)
    {
    }

    /* Valid use case */
    success = true;
    try
    {

      charValue = (Character) datatype.parse("0");
      assertEquals('0', charValue.charValue());

      charValue = (Character) datatype.parse("5");
      assertEquals('5', charValue.charValue());

      charValue = (Character) datatype.parse("\u007F");
      assertEquals('\u007F', charValue.charValue());

      charValue = (Character) datatype.parse("\u00FF");
      assertEquals('\u00FF', charValue.charValue());

    } catch (ParseException e)
    {
      fail();
    }

    /* Invalid use case */
    success = false;
    try
    {
      charValue = (Character) datatype.parse("\u0100");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Invalid use case */
    success = false;
    try
    {
      charValue = (Character) datatype.parse("\uFFFF");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);
    /* Equals check */
    assertFalse(new TimeType().equals(datatype));

    /** Special range of values and validates its not working as expected. */
    assertEquals(Character.class, datatype.getClassType());
    assertTrue(Character.class.isInstance(datatype.getObjectType()));
  }

  public void testStringType()
  {
    boolean success = false;
    String stringValue;
    StringType datatype = new UCS2StringType();
    StringType otherDatatype = new UCS2StringType();
    int minLength = datatype.getMinLength();
    int maxLength = datatype.getMaxLength();
    testBasicDataType(datatype);
    assertEquals(false, datatype.isOrdered());
    assertEquals(String.class, datatype.getClassType());
    /** String class in all cases, except in case of enumeration. */
    assertTrue(String.class.isInstance(datatype.getObjectType()));
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);
    assertEquals(StringType.WHITESPACE_PRESERVE, datatype.getWhitespace());
    /* Default is UCS-2 character set support. */
    assertEquals(new UCS2CharType(), datatype.getBaseTypeReference().getType());

    try
    {
      datatype.setWhitespace("blahwrong");
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true,success);

    if (datatype instanceof LengthFacet)
    {
      testLengthFacetBasic(datatype);

      /* Check the length validation */
      try
      {
        stringValue = (String) datatype.parse("ZFa001");
        stringValue = (String) datatype.parse("");
        stringValue = (String) datatype.parse("HELLO123536663");

        datatype.setMinLength(2);
        datatype.setMaxLength(2);
        stringValue = (String) datatype.parse("HE");

        datatype.setMinLength(1);
        datatype.setMaxLength(4);
        stringValue = (String) datatype.parse("HEA");

        datatype.setMinLength(0);
        datatype.setMaxLength(8);
        stringValue = (String) datatype.parse("");

      } catch (ParseException e)
      {
        fail();
      }

      /* invalid values */
      success = false;
      try
      {
        datatype.setMinLength(1);
        datatype.setMaxLength(4);
        stringValue = (String) datatype.parse("HEASFFD");
      } catch (ParseException e)
      {
        success = true;
      }
      assertEquals(true, success);

      success = false;
      try
      {
        datatype.setMinLength(1);
        datatype.setMaxLength(1);
        stringValue = (String) datatype.parse("HE");
      } catch (ParseException e)
      {
        success = true;
      }
      assertEquals(true, success);

      success = false;
      try
      {
        datatype.setMinLength(1);
        datatype.setMaxLength(8);
        stringValue = (String) datatype.parse("");
      } catch (ParseException e)
      {
        success = true;
      }
      assertEquals(true, success);

    }

    if (datatype instanceof PatternFacet)
    {
      datatype.setPattern("[a-z]+");
      PatternFacet patternFacet = (PatternFacet) datatype;
      Pattern pattern = Pattern.compile(patternFacet.getPattern());
      assertEquals("[a-z]+", datatype.getPattern());
      Matcher m = pattern.matcher("");

      m.reset("a");
      assertEquals(true, m.matches());
      m.reset("avcvcvx");
      assertEquals(true, m.matches());
    }

    /* Valid use case */
    success = true;
    try
    {

      stringValue = (String) datatype.parse("00");
      assertEquals("00", stringValue);

      stringValue = (String) datatype.parse(" 0  0");
      assertEquals(" 0  0", stringValue);

      /* Change whitespace format. */
      datatype.setWhitespace(StringType.WHITESPACE_REPLACE);
      stringValue = (String) datatype.parse(" \t0 \r\n0");
      assertEquals("  0   0", stringValue);

      datatype.setWhitespace(StringType.WHITESPACE_COLLAPSE);
      stringValue = (String) datatype.parse(" \t0 \r\n0");
      assertEquals("0 0", stringValue);

    } catch (ParseException e)
    {
      fail();
    }

    /* Equals check */
    assertFalse(new TimeType().equals(datatype));

    /* Enumeration check: First reset the values. */
    datatype.setMinLength(minLength);
    datatype.setMaxLength(maxLength);
    datatype.setPattern(null);

    String[] choices = new String[] { "one", "two", "three" };
    datatype.setChoices(choices);
    /* Check the choices. */
    Object[] returnedChoices = datatype.getChoices();
    for (int i = 0; i < returnedChoices.length; i++)
    {
      assertEquals(choices[i],returnedChoices[i]);
    }
    try
    {
      /* Verify that only those choices are allowed. */
      stringValue = (String) datatype.parse("hello");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    try
    {
      /* Verify that only those choices are allowed. */
      stringValue = (String) datatype.parse("");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    try
    {
      /* Verify that only those choices are allowed. */
      stringValue = (String) datatype.parse("two");
    } catch (ParseException e)
    {
      fail();
    }



  }

}
