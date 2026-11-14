package com.optimasc.text;

import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;

import com.optimasc.lang.GregorianDatetimeCalendar;

/** Tests of the converters of the date-time types of the specification. */
public class StandardDateFormattersTest extends TestCase
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

  /** Parses and formats the value, which must be in canonical form. */
  private static Calendar roundTrip(DataConverter converter, String value)
      throws ParseException
  {
    Calendar calendar = (Calendar) converter.parseObject(value);
    assertEquals(value, converter.format(calendar));
    return calendar;
  }

  public void testLDAPDate() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.LDAPDateConverter.getInstance();
    roundTrip(converter, "2020-02-29");
    roundTrip(converter, "1582-01-01");
    roundTrip(converter, "9999-12-31");
    assertParseFails(converter, "1581-12-31");
    assertParseFails(converter, "2021-02-29");
    assertParseFails(converter, "2020-04-31");
    assertParseFails(converter, "2020-13-01");
    assertParseFails(converter, "2020-00-01");
    assertParseFails(converter, "2020-01-01Z");
  }

  public void testXSDDate() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.ISO8601DateConverter.getInstance();
    Calendar calendar = roundTrip(converter, "0001-01-01");
    assertEquals(GregorianCalendar.AD, calendar.get(Calendar.ERA));
    roundTrip(converter, "2000-02-29");
    // Proleptic Gregorian: 1000 and 1900 are not leap years.
    assertParseFails(converter, "1000-02-29");
    assertParseFails(converter, "1900-02-29");
    assertParseFails(converter, "2020-01-01+05:00");
  }

  public void testLDAPDateTime() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.LDAPDateTimeConverter.getInstance();
    roundTrip(converter, "2020-01-02T10:20:30");
    assertParseFails(converter, "2020-01-02T10:20:30Z");
    assertParseFails(converter, "2020-01-02T10:20");
    assertParseFails(converter, "1000-01-02T10:20:30");
  }

  public void testXSDDateTime() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.XSDDateTimeConverter.getInstance();
    roundTrip(converter, "2020-01-02T10:20:30");
    // Fractions are truncated to seconds.
    Calendar calendar = (Calendar) converter.parseObject("2020-01-02T10:20:30.999");
    assertEquals("2020-01-02T10:20:30", converter.format(calendar));
    assertEquals(30, calendar.get(Calendar.SECOND));
    // 24:00:00 is 00:00:00 of the following day.
    calendar = (Calendar) converter.parseObject("2020-12-31T24:00:00");
    assertEquals("2021-01-01T00:00:00", converter.format(calendar));
    assertParseFails(converter, "2020-01-02T10:20:30Z");
    assertParseFails(converter, "2020-01-02T24:00:01");
    assertParseFails(converter, "2020-01-02T25:00:00");
  }

  public void testXSDDateTimeStamp() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.XSDDateTimeStampConverter.getInstance();
    roundTrip(converter, "2020-01-02T10:20:30Z");
    roundTrip(converter, "2020-01-02T10:20:30.5+05:30");
    roundTrip(converter, "2020-01-02T10:20:30.05-08:00");
    Calendar calendar = (Calendar) converter.parseObject("2020-01-02T10:20:30.12345Z");
    assertEquals(123, calendar.get(Calendar.MILLISECOND));
    calendar = (Calendar) converter.parseObject("2020-02-28T24:00:00-05:00");
    assertEquals("2020-02-29T00:00:00-05:00", converter.format(calendar));
    assertParseFails(converter, "2020-01-02T10:20:30");
    assertParseFails(converter, "2020-01-02T10:20:30-00:60");
  }

  public void testXSDTime() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.XSDTimeConverter.getInstance();
    roundTrip(converter, "10:20:30");
    assertEquals("10:20:30", converter.format(converter.parseObject("10:20:30.75")));
    assertEquals("00:00:00", converter.format(converter.parseObject("24:00:00")));
    assertParseFails(converter, "10:20:30Z");
    assertParseFails(converter, "10:20");
  }

  public void testTimeOfDay() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.TimeOfDayConverter.getInstance();
    roundTrip(converter, "10:20:30");
    // 24:00:00 is kept distinct from 00:00:00.
    GregorianDatetimeCalendar calendar = (GregorianDatetimeCalendar) roundTrip(converter,
        "24:00:00");
    assertTrue(calendar.isEndOfDay());
    assertParseFails(converter, "10:20:30Z");
    assertParseFails(converter, "10:60:00");
  }

  public void testTimeOfDayWithTimezone() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.TimeOfDayWithTimezoneConverter
        .getInstance();
    roundTrip(converter, "10:20:30Z");
    roundTrip(converter, "10:20:30-05:00");
    assertParseFails(converter, "10:20:30");
    assertParseFails(converter, "10:20:30-0500");
  }

  public void testOpenDate() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.OpenDateConverter.getInstance();
    roundTrip(converter, "2034");
    roundTrip(converter, "1975-01");
    roundTrip(converter, "1975-01-19");
    roundTrip(converter, "1975-01-19T19:20Z");
    roundTrip(converter, "1975-01-19T19:20:30+01:00");
    roundTrip(converter, "1975-01-19T19:20:30.451Z");
    roundTrip(converter, "1975-01-19T19:20:30.45+01:00");
    roundTrip(converter, "1975-01-19T19:20:30.5Z");
    // The fraction has 1 to 3 digits.
    assertEquals("1975-01-19T19:20:30.45Z",
        converter.format(converter.parseObject("1975-01-19T19:20:30.450Z")));
    assertParseFails(converter, "1975-01-19T19:20:30.4567Z");
    assertParseFails(converter, "1975-01-19T19:20:30.Z");
    // A leap second is allowed (ASN.1 TIME), but not hour 24.
    GregorianDatetimeCalendar leap = (GregorianDatetimeCalendar) roundTrip(converter,
        "2016-12-31T23:59:60Z");
    assertTrue(leap.isLeapSecond());
    roundTrip(converter, "2016-12-31T23:59:60.5+01:00");
    assertParseFails(converter, "1975-01-19T24:00:00Z");
    // The W3C NOTE-datetime itself allows no leap second.
    assertParseFails(StandardDateFormatters.W3CDateTimeConverter.getInstance(),
        "2016-12-31T23:59:60Z");
    assertParseFails(converter, "1975-01-19T19:20");
    assertParseFails(converter, "1000");
    assertParseFails(converter, "1975-02-30");
  }

  public void testXSDGYear() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.XSDGYearConverter.getInstance();
    roundTrip(converter, "2020");
    assertParseFails(converter, "2020Z");
    assertParseFails(converter, "20");
  }

  public void testGeneralizedTime() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.GeneralizedTimeConverter.getInstance();
    roundTrip(converter, "20200102102030Z");
    roundTrip(converter, "20200102102030.5Z");
    roundTrip(converter, "20200102102030.123+0530");
    roundTrip(converter, "202001021020-0800");
    roundTrip(converter, "2020010210Z");
    assertEquals("20200102102030.129Z",
        converter.format(converter.parseObject("20200102102030,1299Z")));
    assertEquals("20200102102030-05",
        converter.format(converter.parseObject("20200102102030-05")).substring(0, 17));
    assertParseFails(converter, "20200102102030");
    // RFC 4517: the hour is from 00 to 23.
    assertParseFails(converter, "2020010224Z");
    assertParseFails(converter, "20200102240000Z");
    assertParseFails(converter, "20200102102030.Z");
    assertParseFails(converter, "20201302102030Z");
  }

  public void testRFC822() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.IETF_RFC_822_DATETIME;
    Calendar calendar = (Calendar) converter.parseObject("Thu, 2 Jan 2020 10:20:30 +0000");
    assertEquals("Thu, 2 Jan 2020 10:20:30 +0000", converter.format(calendar));
    calendar = (Calendar) converter.parseObject("2 Jan 2020 10:20 EST");
    assertEquals(-5 * 60 * 60 * 1000, calendar.get(Calendar.ZONE_OFFSET));
  }

  public void testISO8601DateTimeConstant() throws ParseException
  {
    roundTrip(StandardDateFormatters.ISO8601_DATETIME, "2020-01-02T10:20:30");
  }

  public void testPlainCalendarFormatting()
  {
    // A plain Calendar is formatted with its daylight saving time offset,
    // and the year of a BC date in astronomical numbering.
    GregorianCalendar calendar = new GregorianCalendar(
        TimeZone.getTimeZone("America/New_York"));
    calendar.clear();
    calendar.set(2020, Calendar.JULY, 1, 12, 0, 0);
    assertEquals("2020-07-01T12:00:00-04:00",
        StandardDateFormatters.W3CDateTimeConverter.getInstance().format(calendar));

    GregorianCalendar bc = new GregorianCalendar(GregorianDatetimeCalendar.ZULU);
    bc.setGregorianChange(new java.util.Date(Long.MIN_VALUE));
    bc.clear();
    bc.set(Calendar.ERA, GregorianCalendar.BC);
    bc.set(1, Calendar.JANUARY, 1);
    DateConverter converter = new DateConverter();
    converter.applyPattern("-yyyy-MM-dd");
    assertEquals("0000-01-01", converter.format(bc));
  }

  public void testLocalTimeIsIndependentOfDefaultTimezone() throws ParseException
  {
    TimeZone saved = TimeZone.getDefault();
    try
    {
      TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
      // 02:30 does not exist on 8 March 2020 in New York.
      roundTrip(StandardDateFormatters.XSDDateTimeConverter.getInstance(),
          "2020-03-08T02:30:00");
    } finally
    {
      TimeZone.setDefault(saved);
    }
  }

  public void testDuration() throws ParseException
  {
    DataConverter converter = StandardDateFormatters.DurationConverter.getInstance();
    assertEquals("P1Y2M3DT4H5M6S", converter.parseObject("P1Y2M3DT4H5M6S"));
    assertEquals("PT1.5S", converter.parseObject("PT1.500S"));
    // At most 3 digits in the Duration syntax; truncated in xsd:duration.
    assertParseFails(converter, "PT1.1234S");
    assertEquals("PT1.123S",
        StandardDateFormatters.XSDDurationConverter.getInstance().parseObject("PT1.12345S"));
    // Examples of the Duration syntax.
    assertEquals("P29M0D", converter.parseObject("P29M0D"));
    assertEquals("P29MT0S", converter.parseObject("P29MT0S"));
    assertEquals("PT3445.5S", converter.parseObject("PT3445.5S"));
    assertEquals("PT1S", converter.parseObject("PT1.000S"));
    assertEquals("P0D", converter.parseObject("P0D"));
    assertEquals("PT1.5S", converter.format("PT1.5S"));
    assertParseFails(converter, "-P1D");
    assertParseFails(converter, "P");
    assertParseFails(converter, "PT");
    assertParseFails(converter, "P1DT");
    assertParseFails(converter, "P1.5D");
    assertParseFails(converter, "P1D1Y");
    assertParseFails(converter, "PT1S1M");
    assertParseFails(converter, "P1S");
    assertParseFails(converter, "P1Y1Y");
    assertParseFails(converter, "PT1.S");
    assertParseFails(converter, "1D");
  }

  public void testLeapSecond() throws ParseException
  {
    GregorianDatetimeCalendar calendar = (GregorianDatetimeCalendar) roundTrip(
        StandardDateFormatters.TimeOfDayConverter.getInstance(), "23:59:60");
    assertTrue(calendar.isLeapSecond());
    assertEquals(60, calendar.getSecond());
    assertEquals(59, calendar.get(Calendar.MINUTE));
    roundTrip(StandardDateFormatters.LDAPDateTimeConverter.getInstance(),
        "2016-12-31T23:59:60");
    roundTrip(StandardDateFormatters.TimeOfDayWithTimezoneConverter.getInstance(),
        "23:59:60Z");
    roundTrip(StandardDateFormatters.GeneralizedTimeConverter.getInstance(),
        "20161231235960Z");
    assertParseFails(StandardDateFormatters.XSDDateTimeConverter.getInstance(),
        "2016-12-31T23:59:60");
    assertParseFails(StandardDateFormatters.XSDTimeConverter.getInstance(), "23:59:60");
  }

  public void testLDAPDraftExamples() throws ParseException
  {
    roundTrip(StandardDateFormatters.LDAPDateConverter.getInstance(), "9999-02-25");
    roundTrip(StandardDateFormatters.LDAPDateConverter.getInstance(), "1583-01-31");
    roundTrip(StandardDateFormatters.LDAPDateTimeConverter.getInstance(),
        "1583-01-01T00:59:59");
    roundTrip(StandardDateFormatters.LDAPDateTimeConverter.getInstance(),
        "1975-01-19T23:45:34");
    roundTrip(StandardDateFormatters.LDAPDateTimeConverter.getInstance(),
        "1975-01-19T24:00:00");
    roundTrip(StandardDateFormatters.TimeOfDayConverter.getInstance(), "00:59:59");
    roundTrip(StandardDateFormatters.TimeOfDayConverter.getInstance(), "01:45:54");
    roundTrip(StandardDateFormatters.TimeOfDayWithTimezoneConverter.getInstance(),
        "00:59:59Z");
    roundTrip(StandardDateFormatters.TimeOfDayWithTimezoneConverter.getInstance(),
        "01:45:54-01:00");
    roundTrip(StandardDateFormatters.TimeOfDayWithTimezoneConverter.getInstance(),
        "24:00:00Z");
    String[] openDates = { "2034", "1975-01", "1975-01-19", "1975-01-19T19:20+01:00",
        "1975-01-19T19:20:30+01:00", "1975-01-19T19:20:30.451+01:00", "1975-01-19T18:20Z",
        "1975-01-19T18:20:30Z", "1975-01-19T18:20:30.451Z" };
    for (int i = 0; i < openDates.length; i++)
    {
      roundTrip(StandardDateFormatters.OpenDateConverter.getInstance(), openDates[i]);
    }
  }

  public void testTimezoneRange() throws ParseException
  {
    // Time Of Day with Timezone: -15:00 to +15:00.
    DataConverter converter = StandardDateFormatters.TimeOfDayWithTimezoneConverter
        .getInstance();
    roundTrip(converter, "10:00:00-15:00");
    roundTrip(converter, "10:00:00+14:30");
    roundTrip(converter, "10:00:00+15:00");
    assertParseFails(converter, "10:00:00+15:01");
    // XML Schema: -14:00 to +14:00.
    converter = StandardDateFormatters.XSDDateTimeStampConverter.getInstance();
    roundTrip(converter, "2020-01-01T10:00:00-14:00");
    assertParseFails(converter, "2020-01-01T10:00:00+14:30");
  }

  /**
   * Proposed regular expression for the Duration syntax: it requires at least
   * one component, and at least one time component after 'T'. It uses only
   * the features of IETF RFC 9485 (I-Regexp).
   */
  static final String DURATION_REGEX = "P(([0-9]+Y([0-9]+M)?([0-9]+D)?|[0-9]+M([0-9]+D)?|[0-9]+D)"
      + "(T([0-9]+H([0-9]+M)?([0-9]+(\\.[0-9]{1,3})?S)?|[0-9]+M([0-9]+(\\.[0-9]{1,3})?S)?"
      + "|[0-9]+(\\.[0-9]{1,3})?S))?"
      + "|T([0-9]+H([0-9]+M)?([0-9]+(\\.[0-9]{1,3})?S)?|[0-9]+M([0-9]+(\\.[0-9]{1,3})?S)?"
      + "|[0-9]+(\\.[0-9]{1,3})?S))";

  /** Generates every string of the alphabet up to the given length. */
  private static void generate(String prefix, String alphabet, int maxLength,
      java.util.List out)
  {
    out.add(prefix);
    if (prefix.length() == maxLength)
    {
      return;
    }
    for (int i = 0; i < alphabet.length(); i++)
    {
      generate(prefix + alphabet.charAt(i), alphabet, maxLength, out);
    }
  }

  /** The converter and the proposed regular expression accept the same values. */
  public void testDurationRegex()
  {
    DataConverter converter = StandardDateFormatters.DurationConverter.getInstance();
    java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(DURATION_REGEX);
    java.util.List values = new java.util.ArrayList();
    generate("P", "1YMDTHS.", 7, values);
    values.add("PT1.123S");
    values.add("PT1.1234S");
    values.add("P1Y2M3DT4H5M6.789S");
    int accepted = 0;
    for (int i = 0; i < values.size(); i++)
    {
      String value = (String) values.get(i);
      boolean parsed;
      try
      {
        converter.parseObject(value);
        parsed = true;
      } catch (ParseException e)
      {
        parsed = false;
      }
      assertEquals(value, pattern.matcher(value).matches(), parsed);
      if (parsed)
      {
        accepted++;
      }
    }
    assertTrue(accepted > 50);
    assertFalse(pattern.matcher("P1DT").matches());
    assertFalse(pattern.matcher("PT").matches());
    assertFalse(pattern.matcher("P").matches());
  }
}
