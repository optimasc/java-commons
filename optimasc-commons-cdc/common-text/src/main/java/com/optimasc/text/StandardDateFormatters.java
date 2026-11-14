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
 * <tr><td>xsd:date</td><td>{@link ISO8601DateConverter}</td></tr>
 * <tr><td>xsd:dateTime</td><td>{@link XSDDateTimeConverter}</td></tr>
 * <tr><td>xsd:dateTimeStamp</td><td>{@link XSDDateTimeStampConverter}</td></tr>
 * <tr><td>xsd:duration</td><td>{@link DurationConverter}</td></tr>
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
   * differential) is required. Fractions of second beyond three digits are
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

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the IETF draft-codere-ldapsyntax <code>OpenDate</code>
   * syntax (OID: 1.3.6.1.4.1.61799.5.40.14.1): the W3C NOTE-datetime formats
   * (see {@link W3CDateTimeConverter}) with a year from 1582 to 9999. Fractions
   * of second beyond three digits are truncated.
   */
  public static class OpenDateConverter extends W3CDateTimeConverter
  {
    private static final DataConverter instance = new OpenDateConverter();

    public OpenDateConverter()
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
   * to 9999.
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
   * format (<code>hh:mm:ss</code>).
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
   * required timezone (<code>hh:mm:ss(Z|(+|-)hh:mm)</code>).
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
   * As required by the specification, negative durations are rejected and
   * fractions of second are truncated to three digits. The canonical string
   * is the input with fractions of second truncated and without trailing
   * zeros; the components are otherwise kept as written.
   * </p>
   */
  public static class DurationConverter extends DataConverter
  {
    private static final DataConverter instance = new DurationConverter();

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

}
