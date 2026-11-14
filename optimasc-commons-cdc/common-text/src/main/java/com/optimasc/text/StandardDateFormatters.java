package com.optimasc.text;

import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import com.optimasc.lang.GregorianDatetimeCalendar;

/**
 * Standard pre-defined converters for different time and date formats.
 *
 * <p>
 * The following table lists the converter of each date-time type name of the
 * Schema Value Representation Specification:
 * </p>
 *
 * <table border="1">
 * <tr><th>Type name</th><th>Converter</th></tr>
 * <tr><td>Date (1.3.6.1.4.1.61799.5.40.31)</td><td>{@link LDAPDateConverter}</td></tr>
 * <tr><td>Date-Time (1.3.6.1.4.1.61799.5.40.33)</td><td>{@link LDAPDateTimeConverter}</td></tr>
 * <tr><td>Duration (1.3.6.1.4.1.61799.5.40.34)</td><td>{@link DurationConverter}</td></tr>
 * <tr><td>Generalized Time (1.3.6.1.4.1.1466.115.121.1.24)</td><td>{@link GeneralizedTimeConverter}</td></tr>
 * <tr><td>OpenDate (1.3.6.1.4.1.61799.5.40.14.1)</td><td>{@link OpenDateConverter}</td></tr>
 * <tr><td>Time Of Day (1.3.6.1.4.1.61799.5.40.32)</td><td>{@link TimeOfDayConverter}</td></tr>
 * <tr><td>Time Of Day with Timezone (1.3.6.1.4.1.61799.5.40.35)</td><td>{@link TimeOfDayWithTimezoneConverter}</td></tr>
 * <tr><td>xsd:date</td><td>{@link XSDDateConverter}</td></tr>
 * <tr><td>xsd:dateTime</td><td>{@link XSDDateTimeConverter}</td></tr>
 * <tr><td>xsd:dateTimeStamp</td><td>{@link XSDDateTimeStampConverter}</td></tr>
 * <tr><td>xsd:duration</td><td>{@link XSDDurationConverter}</td></tr>
 * <tr><td>xsd:gYear</td><td>{@link XSDGYearConverter}</td></tr>
 * <tr><td>xsd:time</td><td>{@link XSDTimeConverter}</td></tr>
 * </table>
 *
 * <p>
 * The converters of the XML Schema types only accept years of 4 digits
 * (0000 to 9999); negative years and years of more than 4 digits are
 * rejected.
 * </p>
 * 
 * <p>
 * The Date-Time, Time Of Day, Time Of Day with Timezone, OpenDate and
 * Generalized Time syntaxes allow a leap second (second 60), which is
 * returned as a {@link GregorianDatetimeCalendar} whose
 * {@link GregorianDatetimeCalendar#isLeapSecond()} is <code>true</code> and is
 * formatted back as 60. The XML Schema types and the W3C NOTE-datetime do not
 * allow it.
 * </p>
 *
 * <p>
 * The instances returned by the <code>getInstance()</code> methods and the
 * constants of this class are shared and MUST NOT be reconfigured.
 * </p>
 */
public class StandardDateFormatters
{
  // Resolution: year
  public static final String PATTERN_ISO8601_YEAR = "yyyy";
  // Resolution: month
  public static final String PATTERN_ISO8601_YEAR_MONTH = "yyyy-MM";
  // Resolution day
  public static final String PATTERN_ISO8601_DATE = "yyyy-MM-dd";
  public static final String PATTERN_ISO8601_TIME = "HH:mm:ss.SX";
  public static final String PATTERN_ISO8601_DATETIME = "yyyy-MM-dd'T'HH:mm:ss";

  /** Lowest year allowed by the date syntaxes of draft-codere-ldapsyntax. */
  public static final int LDAP_MIN_YEAR = 1582;
  /** Highest year allowed by the date syntaxes of draft-codere-ldapsyntax. */
  public static final int LDAP_MAX_YEAR = 9999;

  /** Instant used as the Gregorian change date: pure (proleptic) Gregorian. */
  private static final Date PURE_GREGORIAN = new Date(Long.MIN_VALUE);

  /** ISO 8601 Time with or without timezone */
  public static final DateConverter ISO8601_TIME = new DateConverter(
   new String[]{
       "HH:mm:ss.SX",
       "HH:mm:ss.S",
       "HH:mm:ssX",
       "HH:mm:ss",
       "'T'HH:mm:ss.SX",
       "'T'HH:mm:ss.S",
       "'T'HH:mm:ssX",
       "'T'HH:mm:ss"});


  /**
   * Date/time pattern from IETF RFC 822 / IETF RFC 2822 standard. The day of
   * week and the seconds are optional, and the day of month may have 1 or 2
   * digits.
   */
  public static final DateConverter IETF_RFC_822_DATETIME = new DateConverter(
      new String[] {
          "EEE, d MMM yyyy HH:mm:ss Z",
          "d MMM yyyy HH:mm:ss Z",
          "EEE, d MMM yyyy HH:mm Z",
          "d MMM yyyy HH:mm Z"});

  /** Date/time pattern from ISO/IEC 8824 and PDF properties */
  public static final DateConverter DATE_ISO_8824 = new DateConverter(
      new String[]{
          "yyyyMMddHHmmssx"}
      );

  /** Date/time pattern from EXIF specification */
  public static final DateConverter DATE_EXIF = new DateConverter(
      new String[]{
       "yyyy:MM:dd HH:mm:ss"}
  );

  /** Date/time pattern for ISO 8601 standard date and local time (YYYY-MM-DDThh:mm:ss) */
  public static final DateConverter ISO8601_DATETIME = new DateConverter(
      new String[]{
          PATTERN_ISO8601_DATETIME}
  );


  /** Highest absolute timezone offset allowed by XML Schema (14:00). */
  private static final int XSD_MAX_ZONE_OFFSET = 14 * 60 * 60 * 1000;

  /**
   * Verifies the constraints that XML Schema and the W3C NOTE-datetime place
   * on a parsed value: no leap second, an end of day only when
   * <code>allowEndOfDay</code> is true, and a timezone offset of at most
   * 14:00 when <code>checkOffset</code> is true.
   */
  static void checkW3C(GregorianDatetimeCalendar cal, boolean allowEndOfDay,
      boolean checkOffset) throws ParseException
  {
    if (cal.isLeapSecond())
    {
      throw new ParseException("A leap second (60) is not allowed.", 0);
    }
    if (cal.isEndOfDay() && (allowEndOfDay == false))
    {
      throw new ParseException("The hour 24 is not allowed.", 0);
    }
    if (checkOffset && cal.isUserSet(Calendar.ZONE_OFFSET)
        && (Math.abs(cal.get(Calendar.ZONE_OFFSET)) > XSD_MAX_ZONE_OFFSET))
    {
      throw new ParseException("The timezone offset must be between -14:00 and +14:00.", 0);
    }
  }

  /**
   * Returns the value with the milliseconds removed (when
   * <code>keepMillis</code> is false) and, when the time is the end of day
   * (24:00:00), with the time set to 00:00:00 of the following day, or of the
   * same day when the value has no date.
   */
  static GregorianDatetimeCalendar normalizeTime(GregorianDatetimeCalendar cal,
      boolean keepMillis) throws ParseException
  {
    final int undefined = GregorianDatetimeCalendar.FIELD_UNDEFINED;
    boolean endOfDay = cal.isEndOfDay();
    boolean hasMillis = cal.isUserSet(Calendar.MILLISECOND);
    if ((endOfDay == false) && ((hasMillis == false) || keepMillis))
    {
      return cal;
    }
    int millis = (hasMillis && keepMillis && !endOfDay) ? cal.get(Calendar.MILLISECOND)
        : undefined;
    if (hasMillis && keepMillis && endOfDay)
    {
      millis = 0;
    }
    int tz = undefined;
    if (cal.isUserSet(Calendar.ZONE_OFFSET))
    {
      tz = cal.get(Calendar.ZONE_OFFSET);
    }
    int hour = endOfDay ? 0 : cal.get(Calendar.HOUR_OF_DAY);
    try
    {
      if (cal.isUserSet(Calendar.YEAR) == false)
      {
        return new GregorianDatetimeCalendar(hour, cal.get(Calendar.MINUTE),
            cal.get(Calendar.SECOND), millis, tz);
      }
      GregorianCalendar date = new GregorianCalendar(GregorianDatetimeCalendar.ZULU);
      date.setGregorianChange(PURE_GREGORIAN);
      date.clear();
      date.set(Calendar.ERA, cal.get(Calendar.ERA));
      date.set(Calendar.YEAR, cal.get(Calendar.YEAR));
      date.set(Calendar.MONTH, cal.get(Calendar.MONTH));
      date.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
      if (endOfDay)
      {
        date.add(Calendar.DAY_OF_MONTH, 1);
      }
      int year = date.get(Calendar.YEAR);
      if (date.get(Calendar.ERA) == GregorianCalendar.BC)
      {
        year = 1 - year;
      }
      return new GregorianDatetimeCalendar(year, date.get(Calendar.MONTH),
          date.get(Calendar.DAY_OF_MONTH), hour, cal.get(Calendar.MINUTE),
          cal.get(Calendar.SECOND), millis, tz);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException(e.getMessage(), 0);
    }
  }


  /**
   * Date converter for the ISO 8601 calendar date in extended format
   * (<code>YYYY-MM-DD</code>), without timezone.
   *
   * This is compliant with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>date</code>, without
   * timezone, which the specification requires to be rejected</li>
   * </ul>
   */
  public static class ISO8601DateConverter extends DateConverter
  {
    private static final DataConverter instance = new ISO8601DateConverter();

    public ISO8601DateConverter()
    {
      super(new String[]{PATTERN_ISO8601_DATE});
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>date</code> type (<code>YYYY-MM-DD</code>). As required by the
   * specification, a value with a timezone is rejected.
   */
  public static class XSDDateConverter extends ISO8601DateConverter
  {
    private static final DataConverter instance = new XSDDateConverter();

    public XSDDateConverter()
    {
      super();
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the IETF draft-codere-ldapsyntax <code>Date</code> syntax
   * (OID: 1.3.6.1.4.1.61799.5.40.31): an ISO 8601 calendar date in extended
   * format (<code>YYYY-MM-DD</code>) with a year from 1582 to 9999.
   */
  public static class LDAPDateConverter extends ISO8601DateConverter
  {
    private static final DataConverter instance = new LDAPDateConverter();

    public LDAPDateConverter()
    {
      super();
      minYear = LDAP_MIN_YEAR;
      maxYear = LDAP_MAX_YEAR;
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Date-time converter for the ASN.1 GeneralizedTime format as defined by IETF
   * RFC 4517 (OID: 1.3.6.1.4.1.1466.115.121.1.24).
   *
   * <p>
   * The minutes and seconds are optional, a fraction of second may follow the
   * seconds, separated by <code>'.'</code> or <code>','</code>, and the
   * timezone (<code>Z</code> or a <code>+hh[mm]</code> / <code>-hh[mm]</code>
   * differential) is required. The hour is from 00 to 23, and the second from
   * 00 to 60 (leap second). Fractions of second beyond three digits are
   * truncated.
   * </p>
   *
   * <p>
   * Restriction: a fraction of hour or of minute, which RFC 4517 allows when
   * the seconds or the minutes are absent, is rejected.
   * </p>
   */
  public static class GeneralizedTimeConverter extends DateConverter
  {
    private static final DataConverter instance = new GeneralizedTimeConverter();

    public GeneralizedTimeConverter()
    {
      super(
          new String[]{
              "yyyyMMddHHmmss.Sx",
              "yyyyMMddHHmmss,Sx",
              "yyyyMMddHHmmssx",
              "yyyyMMddHHmmx",
              "yyyyMMddHHx"});
    }

    /** RFC 4517 allows a leap second, but not the hour 24. */
    protected Calendar adjust(GregorianDatetimeCalendar calendar) throws ParseException
    {
      if (calendar.isEndOfDay())
      {
        throw new ParseException("The hour must be between 00 and 23.", 8);
      }
      return calendar;
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Date-time converter for the W3C NOTE-datetime - Date and Time Formats.
   * The precision of a value is that of its representation: year, year and
   * month, date, or date and time with a timezone, with minutes, seconds or
   * fractions of second. A value is formatted with the precision it was
   * parsed with.
   */
  public static class W3CDateTimeConverter extends DateConverter
  {
    private static final DataConverter instance = new W3CDateTimeConverter();

    public W3CDateTimeConverter()
    {
      super(
          new String[]{
              "yyyy",
              "yyyy-MM-dd'T'HH:mm:ss.SX",
              "yyyy-MM-dd'T'HH:mm:ssX",
              "yyyy-MM-dd'T'HH:mmX",
              "yyyy-MM-dd",
              "yyyy-MM"});
    }

    /** The W3C NOTE-datetime allows neither hour 24 nor a leap second. */
    protected Calendar adjust(GregorianDatetimeCalendar calendar) throws ParseException
    {
      checkW3C(calendar, false, false);
      return calendar;
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the IETF draft-codere-ldapsyntax <code>OpenDate</code>
   * syntax (OID: 1.3.6.1.4.1.61799.5.40.14.1): the W3C NOTE-datetime formats
   * (see {@link W3CDateTimeConverter}) with a year from 1582 to 9999 and,
   * when present, a fraction of second of exactly 3 digits (ASN.1
   * <code>Time=HMSF3</code>). In <code>lenient</code> mode, which is the
   * default, a fraction of 1 or 2 digits is also accepted. A fraction is
   * always formatted with 3 digits, and is omitted when it is zero. As in the ASN.1
   * TIME type, and unlike the W3C NOTE-datetime, the hour is from 00 to 24,
   * where <code>24:00</code> and <code>24:00:00</code> represent the end of the
   * day, and the second from 00 to 60, where 60 represents a leap second. An
   * end of day is returned as a {@link GregorianDatetimeCalendar} whose
   * {@link GregorianDatetimeCalendar#isEndOfDay()} is <code>true</code> and is
   * formatted back with hour 24.
   */
  public static class OpenDateConverter extends W3CDateTimeConverter
  {
    private static final DataConverter instance = new OpenDateConverter();

    public OpenDateConverter()
    {
      super();
      applyPatterns(new String[] {
          "yyyy",
          "yyyy-MM-dd'T'HH:mm:ss.SSSX",
          "yyyy-MM-dd'T'HH:mm:ssX",
          "yyyy-MM-dd'T'HH:mmX",
          "yyyy-MM-dd",
          "yyyy-MM"});
      maxFractionDigits = 3;
      lenientFractionDigits = true;
      minYear = LDAP_MIN_YEAR;
      maxYear = LDAP_MAX_YEAR;
    }

    /** A leap second and the end of day are allowed (ASN.1 TIME). */
    protected Calendar adjust(GregorianDatetimeCalendar calendar) throws ParseException
    {
      return calendar;
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>dateTime</code> type (<code>YYYY-MM-DDThh:mm:ss[.s+]</code>).
   *
   * <p>
   * As required by the specification, a value with a timezone is rejected,
   * fractions of second are truncated to whole seconds, and
   * <code>24:00:00</code> is converted to 00:00:00 of the following day. The
   * canonical representation has no fraction of second.
   * </p>
   */
  public static class XSDDateTimeConverter extends DateConverter
  {
    private static final DataConverter instance = new XSDDateTimeConverter();

    public XSDDateTimeConverter()
    {
      super(new String[] { "yyyy-MM-dd'T'HH:mm:ss", "yyyy-MM-dd'T'HH:mm:ss.S" });
      applyFormatPatterns(new String[] { "yyyy-MM-dd'T'HH:mm:ss" });
    }

    protected Calendar adjust(GregorianDatetimeCalendar calendar) throws ParseException
    {
      checkW3C(calendar, true, true);
      return normalizeTime(calendar, false);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the IETF draft-codere-ldapsyntax <code>Date-Time</code>
   * syntax (OID: 1.3.6.1.4.1.61799.5.40.33): a date and local time in ISO 8601
   * extended format (<code>YYYY-MM-DDThh:mm:ss</code>) with a year from 1582
   * to 9999, an hour from 00 to 24 and a second from 00 to 60 (leap second).
   * <code>24:00:00</code> is kept as the end of the day.
   */
  public static class LDAPDateTimeConverter extends DateConverter
  {
    private static final DataConverter instance = new LDAPDateTimeConverter();

    public LDAPDateTimeConverter()
    {
      super(new String[] { PATTERN_ISO8601_DATETIME });
      minYear = LDAP_MIN_YEAR;
      maxYear = LDAP_MAX_YEAR;
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>dateTimeStamp</code> type
   * (<code>YYYY-MM-DDThh:mm:ss[.s+](Z|(+|-)hh:mm)</code>).
   *
   * <p>
   * The timezone is required. As required by the specification, fractions of
   * second are truncated to three digits and <code>24:00:00</code> is
   * converted to 00:00:00 of the following day.
   * </p>
   */
  public static class XSDDateTimeStampConverter extends DateConverter
  {
    private static final DataConverter instance = new XSDDateTimeStampConverter();

    public XSDDateTimeStampConverter()
    {
      super(new String[] { "yyyy-MM-dd'T'HH:mm:ss.SX", "yyyy-MM-dd'T'HH:mm:ssX" });
    }

    protected Calendar adjust(GregorianDatetimeCalendar calendar) throws ParseException
    {
      checkW3C(calendar, true, true);
      return normalizeTime(calendar, true);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>gYear</code> type (<code>YYYY</code>), without timezone, which the
   * specification requires to be rejected.
   */
  public static class XSDGYearConverter extends DateConverter
  {
    private static final DataConverter instance = new XSDGYearConverter();

    public XSDGYearConverter()
    {
      super(new String[] { PATTERN_ISO8601_YEAR });
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>time</code> type (<code>hh:mm:ss[.s+]</code>).
   *
   * <p>
   * As required by the specification, a value with a timezone is rejected,
   * fractions of second are truncated to whole seconds, and
   * <code>24:00:00</code> is converted to 00:00:00. The canonical
   * representation has no fraction of second.
   * </p>
   */
  public static class XSDTimeConverter extends DateConverter
  {
    private static final DataConverter instance = new XSDTimeConverter();

    public XSDTimeConverter()
    {
      super(new String[] { "HH:mm:ss", "HH:mm:ss.S" });
      applyFormatPatterns(new String[] { "HH:mm:ss" });
    }

    protected Calendar adjust(GregorianDatetimeCalendar calendar) throws ParseException
    {
      checkW3C(calendar, true, true);
      return normalizeTime(calendar, false);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the IETF draft-codere-ldapsyntax <code>Time Of Day</code>
   * syntax (OID: 1.3.6.1.4.1.61799.5.40.32): a local time in ISO 8601 extended
   * format (<code>hh:mm:ss</code>), with an hour from 00 to 24 and a second
   * from 00 to 60 (leap second).
   *
   * <p>
   * <code>24:00:00</code> is accepted and is kept distinct from
   * <code>00:00:00</code>: it is returned as a
   * {@link GregorianDatetimeCalendar} whose
   * {@link GregorianDatetimeCalendar#isEndOfDay()} is <code>true</code>, and is
   * formatted back as <code>24:00:00</code>.
   * </p>
   */
  public static class TimeOfDayConverter extends DateConverter
  {
    private static final DataConverter instance = new TimeOfDayConverter();

    public TimeOfDayConverter()
    {
      super(new String[] { "HH:mm:ss" });
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the IETF draft-codere-ldapsyntax
   * <code>Time Of Day with Timezone</code> syntax (OID:
   * 1.3.6.1.4.1.61799.5.40.35): a time in ISO 8601 extended format with a
   * required timezone (<code>hh:mm:ss(Z|(+|-)hh:mm)</code>), with an hour
   * from 00 to 24, a second from 00 to 60 (leap second) and an offset from
   * -15:00 to +15:00.
   */
  public static class TimeOfDayWithTimezoneConverter extends DateConverter
  {
    private static final DataConverter instance = new TimeOfDayWithTimezoneConverter();

    public TimeOfDayWithTimezoneConverter()
    {
      super(new String[] { "HH:mm:ssX" });
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for durations in the ISO 8601 / W3C XML Schema format
   * <code>PnYnMnDTnHnMn[.n]S</code>, for the W3C XML Schema Definition
   * Language (XSD) 1.1 <code>duration</code> type and the IETF
   * draft-codere-ldapsyntax <code>Duration</code> syntax (OID:
   * 1.3.6.1.4.1.61799.5.40.34).
   *
   * <p>
   * The Duration category maps to <code>javax.xml.datatype.Duration</code>,
   * which is not available on CDC. As allowed by the specification for an
   * unmapped optional category, this converter represents the value as a
   * <code>String</code>: it validates the lexical form and returns the
   * canonical string.
   * </p>
   *
   * <p>
   * Negative durations are rejected, and a fraction of second may have at
   * most 3 digits, as required by the Duration syntax. Use
   * {@link XSDDurationConverter} for <code>xsd:duration</code>, where longer
   * fractions are truncated. The canonical string is the input without
   * trailing zeros in the fraction of second; the components are otherwise
   * kept as written.
   * </p>
   * 
   * <p>
   * A <code>'T'</code> designator that is not followed by a time component
   * (such as <code>P1DT</code>) is rejected, as in ISO 8601 and XML Schema.
   * </p>
   */
  public static class DurationConverter extends DataConverter
  {
    private static final DataConverter instance = new DurationConverter();

    /**
     * <code>true</code> if fractions of second of more than 3 digits are
     * truncated, <code>false</code> if they are rejected.
     */
    protected boolean truncateFraction = false;

    /** Designators of the date components, in order. */
    private static final String DATE_DESIGNATORS = "YMD";
    /** Designators of the time components, in order. */
    private static final String TIME_DESIGNATORS = "HMS";

    public DurationConverter()
    {
      super(String.class, false);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      int length = value.length();
      int i = 0;
      StringBuffer out = new StringBuffer(length);
      if ((length > 0) && (value.charAt(0) == '-'))
      {
        throw new ParseException("Negative durations are not allowed.", 0);
      }
      if ((length == 0) || (value.charAt(0) != 'P'))
      {
        throw new ParseException("Duration must start with 'P'.", 0);
      }
      out.append('P');
      i++;
      int components = 0;
      boolean inTime = false;
      int designatorIndex = 0;
      while (i < length)
      {
        char c = value.charAt(i);
        if (c == 'T')
        {
          if (inTime)
          {
            throw new ParseException("Duplicate 'T' designator.", i);
          }
          if (i + 1 >= length)
          {
            throw new ParseException("A time component must follow 'T'.", i + 1);
          }
          inTime = true;
          designatorIndex = 0;
          out.append('T');
          i++;
          continue;
        }
        int start = i;
        while ((i < length) && (value.charAt(i) >= '0') && (value.charAt(i) <= '9'))
        {
          i++;
        }
        if (i == start)
        {
          throw new ParseException("Expecting a number.", i);
        }
        String number = value.subSequence(start, i).toString();
        String fraction = null;
        if ((i < length) && (value.charAt(i) == '.'))
        {
          int fractionStart = ++i;
          while ((i < length) && (value.charAt(i) >= '0') && (value.charAt(i) <= '9'))
          {
            i++;
          }
          if (i == fractionStart)
          {
            throw new ParseException("Expecting digits after '.'.", i);
          }
          if ((i - fractionStart > 3) && (truncateFraction == false))
          {
            throw new ParseException("A fraction of second has at most 3 digits.",
                fractionStart + 3);
          }
          fraction = value.subSequence(fractionStart, Math.min(i, fractionStart + 3))
              .toString();
        }
        if (i >= length)
        {
          throw new ParseException("Missing component designator.", i);
        }
        char designator = value.charAt(i);
        String designators = inTime ? TIME_DESIGNATORS : DATE_DESIGNATORS;
        int index = designators.indexOf(designator, designatorIndex);
        if (index == -1)
        {
          throw new ParseException("Unexpected or misplaced designator '" + designator
              + "'.", i);
        }
        if ((fraction != null) && (designator != 'S' || !inTime))
        {
          throw new ParseException("Only seconds may have a fraction.", start);
        }
        designatorIndex = index + 1;
        out.append(number);
        if (fraction != null)
        {
          // Truncated to milliseconds, without trailing zeros.
          int end = fraction.length();
          while ((end > 0) && (fraction.charAt(end - 1) == '0'))
          {
            end--;
          }
          if (end > 0)
          {
            out.append('.').append(fraction.substring(0, end));
          }
        }
        out.append(designator);
        components++;
        i++;
      }
      if (components == 0)
      {
        throw new ParseException("A duration must have at least one component.", length);
      }
      return out.toString();
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof String) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      try
      {
        return toAppendTo.append((String) parseObject((String) value));
      } catch (ParseException e)
      {
        throw new IllegalArgumentException("Illegal duration '" + value + "': "
            + e.getMessage());
      }
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>duration</code> type. It is the same as {@link DurationConverter},
   * except that fractions of second of more than 3 digits are truncated, as
   * required by the specification.
   */
  public static class XSDDurationConverter extends DurationConverter
  {
    private static final DataConverter instance = new XSDDurationConverter();

    public XSDDurationConverter()
    {
      super();
      truncateFraction = true;
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }

}
