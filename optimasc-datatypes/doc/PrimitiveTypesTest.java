package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import junit.framework.TestCase;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.primitives.EnumType.EnumerationElement;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.aggregate.AggregateType;
import com.optimasc.datatypes.aggregate.ArrayType;
import com.optimasc.datatypes.aggregate.RecordType;
import com.optimasc.datatypes.defined.BinaryType;
import com.optimasc.datatypes.defined.IntType;
import com.optimasc.datatypes.defined.LatinCharType;
import com.optimasc.datatypes.defined.ShortType;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.defined.TimestampType;
import com.optimasc.datatypes.defined.UCS2CharType;
import com.optimasc.datatypes.derived.LatinStringType;
import com.optimasc.datatypes.derived.UCS2StringType;
import com.optimasc.datatypes.generated.FormalParameterType;
import com.optimasc.datatypes.generated.PointerType;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.generated.FormalParameterType.ParameterType;
import com.optimasc.datatypes.generated.ReferenceType;
import com.optimasc.datatypes.aggregate.ArrayType.Dimension;
import com.optimasc.lang.DateTimeConstants;
import com.optimasc.lang.GregorianDateTime;
import com.optimasc.lang.GregorianDatetimeCalendar;

public class PrimitiveTypesTest extends TestCase
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
      lengthFacet.setMinLength(Integer.MIN_VALUE);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertTrue("Negative length is not valid", success);
    success = false;
    try
    {
      lengthFacet.setMinLength(Integer.MIN_VALUE);
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
    TypeCheckResult checkResult = new TypeCheckResult();
    BinaryType datatype = new BinaryType();
    BinaryType otherDatatype = new BinaryType();
    // Bounded data type with a minimum length of 0 elements and
    // a maximum of 2 elements.
    BinaryType boundedDatatype = new BinaryType(0,2);
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(false, datatype.isNumeric());
    assertEquals(false, datatype.isBounded());
    assertEquals(byte[].class, datatype.getClassType());
    assertEquals(otherDatatype, datatype);
    assertEquals(datatype, datatype);

    /* Equals check */
    assertFalse(new IntegralType().equals(datatype));
    /* Since we changed the length, the datatypes should not be equal. */
    assertFalse(otherDatatype.equals(datatype));

    assertEquals(true, boundedDatatype.isBounded());

    // Invalid object class
    assertEquals(null,boundedDatatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Valid object class but invalid length.
    assertEquals(null,boundedDatatype.toValue(new byte[]{1,2,3}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    byte[] emptyArray = new byte[0];
    assertEquals(emptyArray,boundedDatatype.toValue(emptyArray, checkResult));
    assertEquals(null,checkResult.error);

    byte[] oneArray = new byte[]{0,1,2};
    assertEquals(emptyArray,boundedDatatype.toValue(oneArray, checkResult));
    assertEquals(null,checkResult.error);

  }






  public void testEnumType()
  {
    boolean success = false;
    EnumType datatype = new EnumType();
    EnumType otherDatatype = new EnumType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(0,datatype.getMinInclusive());
    assertEquals(0,datatype.getMaxInclusive());
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    // Create some different types of choices.
    Object[] dataTypeChoices = new String[]{"Choice1","Choice2"};
    Object[] otherDataTypeChoices = new String[]{"Choice1","Choice3"};

    datatype.setChoices(dataTypeChoices);
    assertEquals(0,datatype.getMinInclusive());
    assertEquals(1,datatype.getMaxInclusive());
    assertEquals(1,datatype.getEnumOrdinalValue("Choice2"));
    assertEquals(-1,datatype.getEnumOrdinalValue("Choice19"));

    assertTrue(Arrays.equals(dataTypeChoices,datatype.getChoices()));
    assertFalse(datatype.equals(otherDatatype));

    otherDatatype.setChoices(otherDataTypeChoices);
    assertFalse(datatype.equals(otherDatatype));

    otherDatatype.setChoices(dataTypeChoices);
    assertTrue(datatype.equals(otherDatatype));

    // More complex choice options
    Object[] dataTypeEnum = new Object[]{
        new EnumerationElement("Choice1",1),
        new EnumerationElement("Choice2",2),
        new EnumerationElement("Choice3",55)
    };
    Object[] otherDataTypeEnum = new Object[]{
        new EnumerationElement("Other1",34),
    };

    datatype.setChoices(dataTypeEnum);
    assertEquals(1,datatype.getMinInclusive());
    assertEquals(55,datatype.getMaxInclusive());
    assertEquals(55,datatype.getEnumOrdinalValue("Choice3"));
    assertEquals(-1,datatype.getEnumOrdinalValue("Choice19"));

    assertTrue(Arrays.equals(dataTypeEnum,datatype.getChoices()));
    assertFalse(datatype.equals(otherDatatype));

    otherDatatype.setChoices(otherDataTypeEnum);
    assertFalse(datatype.equals(otherDatatype));

    otherDatatype.setChoices(dataTypeEnum);
    assertTrue(datatype.equals(otherDatatype));
  }




  public void testDateType()
  {
    boolean success = false;
    DateTimeType datatype = new DateTimeType();
    DateTimeType otherDatatype = new DateTimeType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(GregorianDatetimeCalendar.class, datatype.getClassType());
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
      GregorianDatetimeCalendar c;
      datatype.setResolution(DateTimeType.RESOLUTION_YEAR);
      c = (GregorianDatetimeCalendar) datatype.parseObject("1989");
      assertEquals(1989, c.getYear());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_MONTH);
      c = (GregorianDatetimeCalendar) datatype.parseObject("9001-12");
      assertEquals(9001, c.getYear());
      assertEquals(12, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_MONTH);
      c = (GregorianDatetimeCalendar) datatype.parseObject("9001-01");
      assertEquals(9001, c.getYear());
      assertEquals(01, c.getMonth());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_DAY);
      c = (GregorianDatetimeCalendar) datatype.parseObject("1900-01-01");
      assertEquals(1900, c.getYear());
      assertEquals(01, c.getMonth());
      assertEquals(01, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_DAY);
      c = (GregorianDatetimeCalendar) datatype.parseObject("1900-01-11");
      assertEquals(1900, c.getYear());
      assertEquals(01, c.getMonth());
      assertEquals(11, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_DAY);
      c = (GregorianDatetimeCalendar) datatype.parseObject("2018-01-29");
      assertEquals(2018, c.getYear());
      assertEquals(01, c.getMonth());
      assertEquals(29, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_DAY);
      c = (GregorianDatetimeCalendar) datatype.parseObject("2018-11-30");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(30, c.getDay());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getHour());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMinute());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDatetimeCalendar) datatype.parseObject("2018-11-30T00:01:02Z");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(30, c.getDay());
      assertEquals(00, c.getHour());
      assertEquals(01, c.getMinute());
      assertEquals(02, c.getSecond());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());

      assertEquals(0, c.getTimezone());
      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDatetimeCalendar) datatype.parseObject("2018-11-30T15:45:54.900Z");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(30, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(900, c.getMillisecond());
      assertEquals(0, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDatetimeCalendar) datatype.parseObject("2018-11-30T15:45:54Z");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(30, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(00, c.getTimezone());
      assertEquals(DateTimeConstants.FIELD_UNDEFINED, c.getMillisecond());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDatetimeCalendar) datatype.parseObject("2018-11-30T15:45:54.90Z");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(30, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(900, c.getMillisecond());
      assertEquals(00, c.getTimezone());

      datatype.setResolution(DateTimeType.RESOLUTION_SECOND);
      c = (GregorianDatetimeCalendar) datatype.parseObject("2018-11-30T15:45:54.45+00:30");
      assertEquals(2018, c.getYear());
      assertEquals(11, c.getMonth());
      assertEquals(30, c.getDay());
      assertEquals(15, c.getHour());
      assertEquals(45, c.getMinute());
      assertEquals(54, c.getSecond());
      assertEquals(450, c.getMillisecond());
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
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) datatype.parseObject("-555555");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) datatype.parseObject("1999-");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) datatype.parseObject("1999-12-");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) datatype.parseObject("01");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) datatype.parseObject("ZFa001");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) datatype.parseObject("31:");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) datatype.parseObject("13:466:");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    /* Equals check */
    assertFalse(new IntegralType().equals(datatype));

  }

  public void testLatinCharType()
  {
    boolean success = false;
    TypeCheckResult checkResult = new TypeCheckResult();
    Character charValue;
    CharacterType datatype = new LatinCharType();
    CharacterType otherDatatype = new LatinCharType();
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(false, datatype.isNumeric());
    assertEquals(Character.class, datatype.getClassType());
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    /* Equals check */
    assertFalse(new TimeType().equals(datatype));

    assertEquals(Character.class, datatype.getClassType());

    // Validate the method to convert -- error cases wrong unicode values
    assertEquals(null,datatype.toValue(new BigDecimal("255.6"), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,datatype.toValue(new byte[]{12}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Validate the method to convert -- outside the character set repertoire
    assertEquals(null,datatype.toValue(new Character((char)0x2000), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Validate the method to convert -- VALID VALUES
    assertEquals(new Character((char)0x0020),datatype.toValue(new Integer(0x0020), checkResult));
    assertEquals(null,((DatatypeException)checkResult.error).getCode());


  }

/*  public void testStringType()
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
    // String class in all cases, except in case of enumeration.
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);
    assertEquals(StringType.WHITESPACE_PRESERVE, datatype.getWhitespace());
    // Default is UCS-2 character set support.
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
      TypeCheckResult errorResult = new TypeCheckResult();

      // Check the length validation //
      assertNull(datatype.toValue("ZFa001",errorResult));
      assertNull(datatype.toValue("",errorResult));
      assertNull(datatype.toValue("HELLO123536663",errorResult));

      datatype = new UCS2StringType(2,2);
      assertNotNull((String) datatype.toValue("HE",errorResult));

      datatype = new UCS2StringType(1,4);
      assertNotNull((String) datatype.toValue("HEA",errorResult));

      datatype = new UCS2StringType(0,8);
      assertNotNull((String) datatype.toValue("",errorResult));

      // invalid values //
      success = false;
      try
      {
        datatype.setMinLength(1);
        datatype.setMaxLength(4);
        stringValue = (String) datatype.parseObject("HEASFFD");
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
        stringValue = (String) datatype.parseObject("HE");
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
        stringValue = (String) datatype.parseObject("");
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

    // Valid use case //
    success = true;
    try
    {

      stringValue = (String) datatype.parseObject("00");
      assertEquals("00", stringValue);

      stringValue = (String) datatype.parseObject(" 0  0");
      assertEquals(" 0  0", stringValue);

      // Change whitespace format. //
      datatype.setWhitespace(StringType.WHITESPACE_REPLACE);
      stringValue = (String) datatype.parseObject(" \t0 \r\n0");
      assertEquals("  0   0", stringValue);

      datatype.setWhitespace(StringType.WHITESPACE_COLLAPSE);
      stringValue = (String) datatype.parseObject(" \t0 \r\n0");
      assertEquals("0 0", stringValue);

    } catch (ParseException e)
    {
      fail();
    }

    // Equals check //
    assertFalse(new TimeType().equals(datatype));

    // Enumeration check: First reset the values. //
    datatype.setMinLength(minLength);
    datatype.setMaxLength(maxLength);
    datatype.setPattern(null);

    String[] choices = new String[] { "one", "two", "three" };
    datatype.setChoices(choices);
    // Check the choices. //
    Object[] returnedChoices = datatype.getChoices();
    for (int i = 0; i < returnedChoices.length; i++)
    {
      assertEquals(choices[i],returnedChoices[i]);
    }
    try
    {
      // Verify that only those choices are allowed. //
      stringValue = (String) datatype.parseObject("hello");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    try
    {
      // Verify that only those choices are allowed. //
      stringValue = (String) datatype.parseObject("");
    } catch (ParseException e)
    {
      success = true;
    }
    assertEquals(true, success);

    try
    {
      // Verify that only those choices are allowed. //
      stringValue = (String) datatype.parseObject("two");
    } catch (ParseException e)
    {
      fail();
    }



  } */

}
