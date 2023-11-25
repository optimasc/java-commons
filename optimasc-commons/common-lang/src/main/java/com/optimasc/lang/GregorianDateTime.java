package com.optimasc.lang;

import java.util.GregorianCalendar;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/** Date and time value representation. This represents a Gregorian
 *  calendar fields which is a simplified version of the <code>XMLGregorianCalendar</code>
 *  in Java SDK 1.5.
 *
 * @author Carl Eric Codere
 *
 */

public class GregorianDateTime implements Cloneable
{
  protected int era;
  protected int year;
  protected int month;
  protected int day;
  protected int hour;
  protected int minute;
  protected int second;
  protected int fractionalSecond;
  protected int timezone;

  /**
   * Year index for MIN_ and MAX_FIELD_VALUES.
   */
  private static final int YEAR = 0;

  /**
   * Month index for MIN_ and MAX_FIELD_VALUES.
   */
  private static final int MONTH = 1;

  /**
   * Day index for MIN_ and MAX_FIELD_VALUES.
   */
  private static final int DAY = 2;

  /**
   * Hour index for MIN_ and MAX_FIELD_VALUES.
   */
  private static final int HOUR = 3;

  /**
   * Minute index for MIN_ and MAX_FIELD_VALUES.
   */
  private static final int MINUTE = 4;

  /**
   * Second index for MIN_ and MAX_FIELD_VALUES.
   */
  private static final int SECOND = 5;

  /**
   * Second index for MIN_ and MAX_FIELD_VALUES.
   */
  private static final int MILLISECOND = 6;

  /**
   * Timezone index for MIN_ and MAX_FIELD_VALUES
   */
  private static final int TIMEZONE = 7;

  /**
   * Minimum field values indexed by YEAR..TIMEZONE.
   */
  private static final int MIN_FIELD_VALUE[] = { Integer.MIN_VALUE, //Year field can be smaller than this,
                                                                    // only constraint on integer value of year.
      DateTimeConstants.JANUARY, 1, //day of month
      0, //hour
      0, //minute
      0, //second
      0, //millisecond
      -14 * 60 //timezone
  };

  /**
   * Maximum field values indexed by YEAR..TIMEZONE.
   */
  private static final int MAX_FIELD_VALUE[] = { Integer.MAX_VALUE, // Year field can be bigger than this,
                                                                    // only constraint on integer value of year.
      DateTimeConstants.DECEMBER, 31, //day of month
      23, //hour
      59, //minute
      60, //second (leap second allows for 60)
      999, //millisecond
      DateTimeConstants.MAX_TIMEZONE_OFFSET //timezone
  };

  /**
   * field names indexed by YEAR..TIMEZONE.
   */
  private static final String FIELD_NAME[] = { "Year", "Month", "Day", "Hour", "Minute",
      "Second", "Millisecond", "Timezone" };

  public GregorianDateTime()
  {
    clear();
  }

  /** Constructs a date-time representation based on values. Fields which
   *  are not defined or used should be set {@link DateTimeConstants#FIELD_UNDEFINED}.
   *
   * @param ayear Year
   * @param amonth Month, allowed values are between 1 and 12
   * @param aday Day, allowed values are between 1 and 31
   * @param ahour Hours, allowed values are between 0 and 23
   * @param aminute Minutes, allowed values are between 0 and 59
   * @param second Seconds, allowed values are between 0 and 60 (for leap seconds)
   * @param fractionalSeconds fractional values of a second, between 0 and 999
   * @param tz Timezone offset in minutes
   */
  public GregorianDateTime(int ayear, int amonth, int aday, int ahour, int aminute,
      int second, int fractionalSeconds, int tz)
  {
    clear();
    setYear(ayear);
    setMonth(amonth);
    setDay(aday);
    setHour(ahour);
    setMinute(aminute);
    setSecond(second);
    setFractionalSecond(fractionalSeconds);
    setTimezone(tz);
  }

  /** Constructs a date representation based on values. Fields which
   *  are not defined will be set to {@link DateTimeConstants#FIELD_UNDEFINED}.
   *
   * @param ayear Year
   * @param amonth Month, allowed values are between 1 and 12
   * @param aday Day, allowed values are between 1 and 31
   */
  public GregorianDateTime(int ayear, int amonth, int aday)
  {
    clear();
    setYear(ayear);
    setMonth(amonth);
    setDay(aday);
    setHour(DateTimeConstants.FIELD_UNDEFINED);
    setMinute(DateTimeConstants.FIELD_UNDEFINED);
    setSecond(DateTimeConstants.FIELD_UNDEFINED);
    setFractionalSecond(DateTimeConstants.FIELD_UNDEFINED);
    setTimezone(DateTimeConstants.FIELD_UNDEFINED);
  }

  /** Constructs a time representation based on values. Fields which
   *  are not defined will be set to {@link DateTimeConstants#FIELD_UNDEFINED}.
   *
   * @param ahour Hours, allowed values are between 0 and 23
   * @param aminute Minutes, allowed values are between 0 and 59
   * @param second Seconds, allowed values are between 0 and 60 (for leap seconds)
   * @param fractionalSeconds fractional values of a second, between 0 and 999
   * @param tz Timezone offset in minutes
   */
  public GregorianDateTime(int ahour, int aminute,
      int second, int fractionalSeconds, int tz)
  {
    clear();
    setYear(DateTimeConstants.FIELD_UNDEFINED);
    setMonth(DateTimeConstants.FIELD_UNDEFINED);
    setDay(DateTimeConstants.FIELD_UNDEFINED);
    setHour(ahour);
    setMinute(aminute);
    setSecond(second);
    setFractionalSecond(fractionalSeconds);
    setTimezone(tz);
  }



  /** Clear all fields and set them to {@link #FIELD_UNDEFINED} */
  public void clear()
  {
    era = DateTimeConstants.FIELD_UNDEFINED;
    year = DateTimeConstants.FIELD_UNDEFINED;
    month = DateTimeConstants.FIELD_UNDEFINED;
    day = DateTimeConstants.FIELD_UNDEFINED;
    hour = DateTimeConstants.FIELD_UNDEFINED;
    minute = DateTimeConstants.FIELD_UNDEFINED;
    second = DateTimeConstants.FIELD_UNDEFINED;
    fractionalSecond = DateTimeConstants.FIELD_UNDEFINED;
    timezone = DateTimeConstants.FIELD_UNDEFINED;
  }

  public int getEra()
  {
    return era;
  }

  public void setEra(int era)
  {
    this.era = era;
  }

  public int getYear()
  {
    return year;
  }

  public void setYear(int year)
  {
    this.year = year;
  }

  public int getMonth()
  {
    return month;
  }

  public void setMonth(int month)
  {
    if (month == DateTimeConstants.FIELD_UNDEFINED)
    {
      this.month = month;
      return;
    }
    if ((month < MIN_FIELD_VALUE[MONTH]) || (month > MAX_FIELD_VALUE[MONTH]))
      throw new IllegalArgumentException("Illegal " + FIELD_NAME[MONTH]
          + " value, must be between " + MIN_FIELD_VALUE[MONTH] + " and "
          + MAX_FIELD_VALUE[MONTH]);
    this.month = month;
  }

  public int getDay()
  {
    return day;
  }

  public void setDay(int day)
  {
    if (day == DateTimeConstants.FIELD_UNDEFINED)
    {
      this.day = day;
      return;
    }
    if ((day < MIN_FIELD_VALUE[DAY]) || (day > MAX_FIELD_VALUE[DAY]))
      throw new IllegalArgumentException("Illegal " + FIELD_NAME[DAY]
          + " value, must be between " + MIN_FIELD_VALUE[DAY] + " and "
          + MAX_FIELD_VALUE[DAY]);
    this.day = day;
  }

  public int getHour()
  {
    return hour;
  }

  public void setHour(int hour)
  {
    if (hour == DateTimeConstants.FIELD_UNDEFINED)
    {
      this.hour = hour;
      return;
    }
    if ((hour < MIN_FIELD_VALUE[HOUR]) || (hour > MAX_FIELD_VALUE[HOUR]))
      throw new IllegalArgumentException("Illegal " + FIELD_NAME[HOUR]
          + " value, must be between " + MIN_FIELD_VALUE[HOUR] + " and "
          + MAX_FIELD_VALUE[HOUR]);
    this.hour = hour;
  }

  public int getMinute()
  {
    return minute;
  }

  public void setMinute(int minute)
  {
    if (minute == DateTimeConstants.FIELD_UNDEFINED)
    {
      this.minute = minute;
      return;
    }
    if ((minute < MIN_FIELD_VALUE[MINUTE]) || (minute > MAX_FIELD_VALUE[MINUTE]))
      throw new IllegalArgumentException("Illegal " + FIELD_NAME[MINUTE]
          + " value, must be between " + MIN_FIELD_VALUE[MINUTE] + " and "
          + MAX_FIELD_VALUE[MINUTE]);
    this.minute = minute;
  }

  public int getSecond()
  {
    return second;
  }

  public void setSecond(int second)
  {
    if (second == DateTimeConstants.FIELD_UNDEFINED)
    {
      this.second = second;
      return;
    }
    if ((second < MIN_FIELD_VALUE[SECOND]) || (second > MAX_FIELD_VALUE[SECOND]))
      throw new IllegalArgumentException("Illegal " + FIELD_NAME[SECOND]
          + " value, must be between " + MIN_FIELD_VALUE[SECOND] + " and "
          + MAX_FIELD_VALUE[SECOND]);
    this.second = second;
  }

  public int getFractionalSecond()
  {
    return fractionalSecond;
  }

  public void setFractionalSecond(int fractionSecond)
  {
    if (fractionSecond == DateTimeConstants.FIELD_UNDEFINED)
    {
      this.fractionalSecond = fractionSecond;
      return;
    }

    if ((fractionSecond < MIN_FIELD_VALUE[MILLISECOND])
        || (fractionSecond > MAX_FIELD_VALUE[MILLISECOND]))
      throw new IllegalArgumentException("Illegal " + FIELD_NAME[MILLISECOND]
          + " value, must be between " + MIN_FIELD_VALUE[MILLISECOND] + " and "
          + MAX_FIELD_VALUE[MILLISECOND]);
    this.fractionalSecond = fractionSecond;
  }

  public int getTimezone()
  {
    return timezone;
  }

  public void setTimezone(int timeZone)
  {
    if (timeZone == DateTimeConstants.FIELD_UNDEFINED)
    {
      this.timezone = timeZone;
      return;
    }
    if ((timeZone < DateTimeConstants.MIN_TIMEZONE_OFFSET)
        || (timeZone > DateTimeConstants.MAX_TIMEZONE_OFFSET))
      throw new IllegalArgumentException("Illegal " + FIELD_NAME[TIMEZONE]
          + " value, must be between " + MIN_FIELD_VALUE[TIMEZONE] + " and "
          + MAX_FIELD_VALUE[TIMEZONE]);
    this.timezone = timeZone;
  }

  /** Compares this calendar to the specified object. The result is true if and only if the
   *   argument is not null and is an GregorianDateTime object that represents the same
   *   instant in time as this object.
   *
   */
  public boolean equals(Object obj)
  {
    if (obj == this)
      return true;
    if (obj instanceof GregorianDateTime)
    {
      GregorianDateTime other = (GregorianDateTime) obj;
      if (era != other.era)
      {
        return false;
      }
      if (year != other.year)
      {
        return false;
      }
      if (month != other.month)
      {
        return false;
      }
      if (day != other.day)
      {
        return false;
      }
      if (hour != other.hour)
      {
        return false;
      }
      if (minute != other.minute)
      {
        return false;
      }
      if (second != other.second)
      {
        return false;
      }
      if (fractionalSecond != other.fractionalSecond)
      {
        return false;
      }
      if (timezone != other.timezone)
      {
        return false;
      }
    }
    return true;
  }

  /**
   * Constructs a new XMLGregorianCalendar object.
   *
   * String parsing documented by {@link #parse(String)}.
   *
   * Returns a non-null valid XMLGregorianCalendar object that holds the value
   * indicated by the lexicalRepresentation parameter.
   *
   * @param lexicalRepresentation
   *          Lexical representation of one the eight XML Schema date/time
   *          datatypes.
   * @throws IllegalArgumentException
   *           If the given string does not conform as documented in
   *           {@link #parse(String)}.
   * @throws NullPointerException
   *           If the given string is null.
   */
  protected GregorianDateTime(String lexicalRepresentation)
      throws IllegalArgumentException
  {
    clear();
    // compute format string for this lexical representation.
    String format = null;
    String lexRep = lexicalRepresentation;
    final int NOT_FOUND = -1;
    int lexRepLength = lexRep.length();

    // current parser needs a format string,
    // use following heuristics to figure out what xml schema date/time
    // datatype this lexical string could represent.
    if (lexRep.indexOf('T') != NOT_FOUND)
    {
      // found Date Time separater, must be xsd:DateTime
      format = "%Y-%M-%DT%h:%m:%s" + "%z";
    }
    else if (lexRepLength >= 3 && lexRep.charAt(2) == ':')
    {
      // found ":", must be xsd:Time
      format = "%h:%m:%s" + "%z";
    }
    else if (lexRep.startsWith("--"))
    {
      // check for GDay || GMonth || GMonthDay
      if (lexRepLength >= 3 && lexRep.charAt(2) == '-')
      {
        // GDAY
        // Fix 4971612: invalid SCCS macro substitution in data string
        format = "---%D" + "%z";
      }
      else if (lexRepLength == 4
          || (lexRepLength >= 6 && (lexRep.charAt(4) == '+' || (lexRep.charAt(4) == '-' && (lexRep
              .charAt(5) == '-' || lexRepLength == 10)))))
      {
        // GMonth
        // Fix 4971612: invalid SCCS macro substitution in data string
        format = "--%M--%z";
        Parser p = new Parser(format, lexRep);
        try
        {
          p.parse();
          // check for validity
          if (!isValid())
          {
            throw new IllegalArgumentException("'"+lexicalRepresentation+"' is not a valid representation of an ISO 8601 Calendar value");
          }
          return;
        } catch (IllegalArgumentException e)
        {
          format = "--%M%z";
        }
      }
      else
      {
        // GMonthDay or invalid lexicalRepresentation
        format = "--%M-%D" + "%z";
      }
    }
    else
    {
      // check for Date || GYear | GYearMonth
      int countSeparator = 0;

      // start at index 1 to skip potential negative sign for year.

      int timezoneOffset = lexRep.indexOf(':');
      if (timezoneOffset != NOT_FOUND)
      {

        // found timezone, strip it off for distinguishing
        // between Date, GYear and GYearMonth so possible
        // negative sign in timezone is not mistaken as
        // a separator.
        lexRepLength -= 6;
      }

      for (int i = 1; i < lexRepLength; i++)
      {
        if (lexRep.charAt(i) == '-')
        {
          countSeparator++;
        }
      }
      if (countSeparator == 0)
      {
        // GYear
        format = "%Y" + "%z";
      }
      else if (countSeparator == 1)
      {
        // GYearMonth
        format = "%Y-%M" + "%z";
      }
      else
      {
        // Date or invalid lexicalRepresentation
        // Fix 4971612: invalid SCCS macro substitution in data string
        format = "%Y-%M-%D" + "%z";
      }
    }
    Parser p = new Parser(format, lexRep);
    p.parse();

    // check for validity
    if (!isValid())
    {
      throw new IllegalArgumentException("'"+lexicalRepresentation+"' is not a valid representation of an ISO 8601 Calendar value");
    }
  }

  /**
   * Validate instance based on ISO 8601 constraints.
   *
   * @return true if data values are valid, including {@link DateTimeConstants#FIELD_UNDEFINED}
   */
  public boolean isValid()
  {
    // since setters do not allow for invalid values,
    // (except for exceptional case of year field of zero),
    // no need to check for anything except for constraints
    // between fields.

    //check if days in month is valid. Can be dependent on leap year.
    if (getMonth() == DateTimeConstants.FEBRUARY)
    {
      // years could not be set
      int maxDays = DateTimeConstants.FIELD_UNDEFINED;
      int years = getYear();
      if (years != DateTimeConstants.FIELD_UNDEFINED)
      {
        maxDays = maximumDayInMonthFor(getYear(), DateTimeConstants.FEBRUARY);
      }
      else
      {
        // year is undefined, allow 29 days
        maxDays = 29;
      }
      if (getDay() > maxDays)
      {
        return false;
      }
    }

    // http://www.w3.org/2001/05/xmlschema-errata#e2-45
    if (getHour() == 24)
    {
      if (getMinute() != 0)
      {
        return false;
      }
      else if (getSecond() != 0)
      {
        return false;
      }
    }

    // XML Schema 1.0 specification defines year value of zero as
    // invalid. Allow this class to set year field to zero
    // since XML Schema 1.0 errata states that lexical zero will
    // be allowed in next version and treated as 1 B.C.E.
    // optimize check.
    if (year == 0)
    {
      return false;
    }
    return true;
  }

  /** Normalize this instance to UTC. If this datetime contains
   *  a timezone specification, then a new datetime is calculated
   *  based on UTC. If the timezone specification is not
   *  set, then this instance is returned.
   *
   *  It requires the following valid values for this to work:
   *  <code>YYYY-MM-DDTHH:MM:SS</code> or <code>THH:MM:SS</code>.
   *
   * @return this Calendar normalized to UTC or this instance
   */
  public GregorianDateTime normalize()
  {
    if (timezone == DateTimeConstants.FIELD_UNDEFINED)
    {
      return this;
    }
    Calendar cal = toGregorianCalendar();
    // Adjust the timezones
    cal.add(Calendar.MINUTE, -timezone);

    int year = cal.get(Calendar.YEAR);
    if ((cal.get(Calendar.ERA)==GregorianCalendar.BC))
    {
      year = - year;
    }

    if (getYear()==DateTimeConstants.FIELD_UNDEFINED)
    {
      year = DateTimeConstants.FIELD_UNDEFINED;
    }

    int month = DateTimeConstants.FIELD_UNDEFINED;;
    if (getMonth() != DateTimeConstants.FIELD_UNDEFINED)
    {
      month = cal.get(Calendar.MONTH)+DateTimeConstants.JANUARY - Calendar.JANUARY;
    }

    int day = DateTimeConstants.FIELD_UNDEFINED;
    if (getDay() != DateTimeConstants.FIELD_UNDEFINED)
    {
      day = cal.get(Calendar.DAY_OF_MONTH);
    }

    if (getHour()==DateTimeConstants.FIELD_UNDEFINED)
    {
      throw new IllegalArgumentException("Hour field should be defined to normalize");
    }
    if (getMinute()==DateTimeConstants.FIELD_UNDEFINED)
    {
      throw new IllegalArgumentException("Minute field should be defined to normalize");
    }
    if (getSecond()==DateTimeConstants.FIELD_UNDEFINED)
    {
      throw new IllegalArgumentException("Second field should be defined to normalize");
    }


    GregorianDateTime normalized = new GregorianDateTime(
        year,
        month,
        day,
        cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        cal.get(Calendar.SECOND),
        getFractionalSecond(),
        0
        );

    return normalized;
  }


  public void setTime(int hour, int minute, int second)
  {
    setHour(hour);
    setMinute(minute);
    setSecond(second);
  }

  /**
   * <p>
   * Normalize this instance to UTC.
   * </p>
   *
   * <p>
   * 2000-03-04T23:00:00+03:00 normalizes to 2000-03-04T20:00:00Z
   * </p>
   */
  private GregorianDateTime normalizeToTimezone(GregorianDateTime cal, int timezone)
  {

    GregorianDateTime result = null;
    try
    {
      result = (GregorianDateTime) cal.clone();
      // If timezone is not set, then do nothing.
      if (timezone == DateTimeConstants.FIELD_UNDEFINED)
      {
        return result;
      }
    } catch (CloneNotSupportedException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    int minutes = timezone;

    // normalizing to UTC time negates the timezone offset before
    // addition.
    minutes = -minutes;
    Duration d = new Duration(minutes >= 0, // isPositive
        0, //years
        0, //months
        0, //days
        0, //hours
        minutes < 0 ? -minutes : minutes, // absolute
        0 //seconds
    );
    result.add(d);

    // set to zulu UTC time.
    result.setTimezone(0);
    return result;
  }

  /** Convert this Date and time representation to a {@link GregorianCalendar}. Conversion
   *  may lose some precision on some of the fields.
   *
   * @return An instance of Calendar representing this Date and time value
   */
  public Calendar toGregorianCalendar()
  {
    // Convert from minutes to milliseconds
    int rawOffset = getTimezone() * 60 * 1000;
    TimeZone tz = null;
    if (rawOffset != DateTimeConstants.FIELD_UNDEFINED)
    {
      String[] ids = TimeZone.getAvailableIDs(rawOffset);
      if ((ids != null) && (ids.length > 0))
      {
        tz = TimeZone.getTimeZone(ids[0]);
      } else
      {
        tz = TimeZone.getDefault();
      }
    }
    else
    {
      tz = TimeZone.getDefault();
    }
    GregorianCalendar cal = new GregorianCalendar(tz, Locale.getDefault());
    cal.clear();
    cal.setGregorianChange(new Date(Long.MIN_VALUE));

    int value;
    value = getYear();
    if (value < 0)
    {
      cal.set(Calendar.ERA, GregorianCalendar.BC);
    }
    if (value != DateTimeConstants.FIELD_UNDEFINED)
      cal.set(Calendar.YEAR, Math.abs(value));

    value = getMonth();
    if (value != DateTimeConstants.FIELD_UNDEFINED)
      cal.set(Calendar.MONTH, value - DateTimeConstants.JANUARY + Calendar.JANUARY);

    value = getDay();
    if (value != DateTimeConstants.FIELD_UNDEFINED)
      cal.set(Calendar.DAY_OF_MONTH, value);

    value = getHour();
    if (value != DateTimeConstants.FIELD_UNDEFINED)
      cal.set(Calendar.HOUR_OF_DAY, value);

    value = getMinute();
    if (value != DateTimeConstants.FIELD_UNDEFINED)
      cal.set(Calendar.MINUTE, value);

    value = getFractionalSecond();
    if (value != DateTimeConstants.FIELD_UNDEFINED)
      cal.set(Calendar.MILLISECOND, value);

    return cal;

  }

  /**
   * Compute <code>value*signum</code>
   *
   */
  static int sanitize(int value, int signum)
  {
    if (signum == 0)
    {
      return 0;
    }
    return (signum < 0) ? -value : value;
  }

  /** Add duration to this instance.
   *
   * @param duration Duration to add to this Calendar
   */
  public void add(Duration duration)
  {

    /*
     * Extracted from
     * http://www.w3.org/TR/xmlschema-2/#adding-durations-to-dateTimes
     * to ensure implemented properly. See spec for definitions of methods
     * used in algorithm.
     *
     * Given a dateTime S and a duration D, specifies how to compute a
     * dateTime E where E is the end of the time period with start S and
     * duration D i.e. E = S + D.
     *
     * The following is the precise specification.
     * These steps must be followed in the same order.
     * If a field in D is not specified, it is treated as if it were zero.
     * If a field in S is not specified, it is treated in the calculation
     * as if it were the minimum allowed value in that field, however,
     * after the calculation is concluded, the corresponding field in
     * E is removed (set to unspecified).
     *
     * Months (may be modified additionally below)
         *  temp := S[month] + D[month]
         *  E[month] := modulo(temp, 1, 13)
         *  carry := fQuotient(temp, 1, 13)
     */

    boolean fieldUndefined[] = { false, false, false, false, false, false };

    int signum = duration.getSign();

    int startMonth = getMonth();
    if (startMonth == DateTimeConstants.FIELD_UNDEFINED)
    {
      startMonth = MIN_FIELD_VALUE[MONTH];
      fieldUndefined[MONTH] = true;
    }

    long dMonths = sanitize(duration.getYears(), signum);
    long temp = startMonth + dMonths;
    setMonth((int) ((temp - 1) % 12)+1);
    long carry = (int) ((temp - 1) / 12);

    /* Years (may be modified additionally below)
     *  E[year] := S[year] + D[year] + carry
     */
    long startYear = getYear();
    if (startYear == DateTimeConstants.FIELD_UNDEFINED)
    {
      fieldUndefined[YEAR] = true;
      startYear = 0;
    }
    long dYears = sanitize(duration.getYears(), signum);
    long endYear = startYear + dYears + carry;
    setYear((int) endYear);

    /* Zone
         *  E[zone] := S[zone]
     *
     * no-op since adding to this, not to a new end point.
     */

    /* Seconds
     *  temp := S[second] + D[second]
     *  E[second] := modulo(temp, 60)
     *  carry := fQuotient(temp, 60)
     */
    int startSeconds;
    if (getSecond() == DateTimeConstants.FIELD_UNDEFINED)
    {
      fieldUndefined[SECOND] = true;
      startSeconds = 0;
    }
    else
    {
      // seconds + fractionalSeconds
      startSeconds = getSecond();
    }

    // Duration seconds is SECONDS + FRACTIONALSECONDS.
    long dSeconds = Duration.sanitize(duration.getSeconds(), signum);
    long tempBD = startSeconds + dSeconds;
    long fQuotient = tempBD / 60;
    long endSeconds = tempBD - (fQuotient * 60);

    carry = fQuotient;
    setSecond((int) endSeconds);

    /* Minutes
         *  temp := S[minute] + D[minute] + carry
         *  E[minute] := modulo(temp, 60)
         *  carry := fQuotient(temp, 60)
     */
    int startMinutes = getMinute();
    if (startMinutes == DateTimeConstants.FIELD_UNDEFINED)
    {
      fieldUndefined[MINUTE] = true;
      startMinutes = MIN_FIELD_VALUE[MINUTE];
    }
    long dMinutes = sanitize(duration.getMinutes(), signum);

    temp = startMinutes + dMinutes + carry;
    setMinute((int) (temp % 60));
    carry = temp / 60;

    /* Hours
         *  temp := S[hour] + D[hour] + carry
         *  E[hour] := modulo(temp, 24)
         *  carry := fQuotient(temp, 24)
     */
    int startHours = getHour();
    if (startHours == DateTimeConstants.FIELD_UNDEFINED)
    {
      fieldUndefined[HOUR] = true;
      startHours = MIN_FIELD_VALUE[HOUR];
    }
    long dHours = sanitize(duration.getHours(), signum);

    temp = startHours + dHours + carry;
    setHour((int) (temp % 24));
    carry = temp / 24;

    /* Days
     *  if S[day] > maximumDayInMonthFor(E[year], E[month])
     *       + tempDays := maximumDayInMonthFor(E[year], E[month])
     *  else if S[day] < 1
     *       + tempDays := 1
     *  else
     *       + tempDays := S[day]
     *  E[day] := tempDays + D[day] + carry
     *  START LOOP
     *       + IF E[day] < 1
     *             # E[day] := E[day] +
     *                 maximumDayInMonthFor(E[year], E[month] - 1)
     *             # carry := -1
     *       + ELSE IF E[day] > maximumDayInMonthFor(E[year], E[month])
     *             # E[day] :=
     *                    E[day] - maximumDayInMonthFor(E[year], E[month])
     *             # carry := 1
     *       + ELSE EXIT LOOP
     *       + temp := E[month] + carry
     *       + E[month] := modulo(temp, 1, 13)
     *       + E[year] := E[year] + fQuotient(temp, 1, 13)
     *       + GOTO START LOOP
     */
    long tempDays;
    int startDay = getDay();
    if (startDay == DateTimeConstants.FIELD_UNDEFINED)
    {
      fieldUndefined[DAY] = true;
      startDay = MIN_FIELD_VALUE[DAY];
    }
    long dDays = sanitize(duration.getDays(), signum);
    int maxDayInMonth = maximumDayInMonthFor(getYear(), getMonth());
    if (startDay > maxDayInMonth)
    {
      tempDays = maxDayInMonth;
    }
    else if (startDay < 1)
    {
      tempDays = 1;
    }
    else
    {
      tempDays = startDay;
    }
    long endDays = dDays + carry;
    int monthCarry;
    int intTemp;
    while (true)
    {
      if (endDays < 0)
      {
        // calculate days in previous month, watch for month roll over
        long mdimf = 0;
        if (month >= 2)
        {
          mdimf = maximumDayInMonthFor(getYear(), getMonth() - 1);
        }
        else
        {
          // roll over to December of previous year
          mdimf = maximumDayInMonthFor(getYear() - 1, 12);
        }
        endDays = endDays + mdimf;
        monthCarry = -1;
      }
      else if (endDays > maximumDayInMonthFor(getYear(), getMonth()))
      {
        endDays = endDays + -maximumDayInMonthFor(getYear(), getMonth());
        monthCarry = 1;
      }
      else
      {
        break;
      }

      intTemp = getMonth() + monthCarry;
      int endMonth = (intTemp - 1) % (13 - 1);
      int quotient;
      if (endMonth < 0)
      {
        endMonth = (13 - 1) + endMonth + 1;
        quotient = (intTemp - 1) / 12;
      }
      else
      {
        quotient = (intTemp - 1) / (13 - 1);
        endMonth += 1;
      }
      setMonth(endMonth);
      if (quotient != 0)
      {
        setYear(getYear() + quotient);
      }
    }
    setDay((int) endDays);

    // set fields that where undefined before this addition, back to undefined.
    for (int i = YEAR; i <= SECOND; i++)
    {
      if (fieldUndefined[i])
      {
        switch (i)
        {
          case YEAR:
            setYear(DateTimeConstants.FIELD_UNDEFINED);
            break;
          case MONTH:
            setMonth(DateTimeConstants.FIELD_UNDEFINED);
            break;
          case DAY:
            setDay(DateTimeConstants.FIELD_UNDEFINED);
            break;
          case HOUR:
            setHour(DateTimeConstants.FIELD_UNDEFINED);
            break;
          case MINUTE:
            setMinute(DateTimeConstants.FIELD_UNDEFINED);
            break;
          case SECOND:
            setSecond(DateTimeConstants.FIELD_UNDEFINED);
            setFractionalSecond(DateTimeConstants.FIELD_UNDEFINED);
            break;
        }
      }
    }
  }

  private static int daysInMonth[] = { 0, // XML Schema months start at 1.
      31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

  /** Compare two instances of W3C XML Schema 1.0 date/time datatypes according to partial
   *  order relation defined in W3C XML Schema 1.0 Part 2, Section 3.2.7.3, Order relation on dateTime.
   *
   * @param rhs The Date time representation to compare with
   * @return The relationship between the two Calendars as {@link DateTimeConstants#LESSER},
   *   {@link DateTimeConstants#EQUAL}, {@link DateTimeConstants#GREATER} or {@link DateTimeConstants#INDETERMINATE}.
   */
  public int compare(GregorianDateTime rhs)
  {

    int result = DateTimeConstants.INDETERMINATE;
    GregorianDateTime P = this;
    GregorianDateTime Q = rhs;

    if (P.getTimezone() == Q.getTimezone())
    {
      // Optimization:
      // both instances are in same timezone or
      // both are FIELD_UNDEFINED.
      // Avoid costly normalization of timezone to 'Z' time.
      return internalCompare(P, Q);

    }
    else if (P.getTimezone() != DateTimeConstants.FIELD_UNDEFINED
        && Q.getTimezone() != DateTimeConstants.FIELD_UNDEFINED)
    {

      // Both instances have different timezones.
      // Normalize to UTC time and compare.
      P = (GregorianDateTime) P.normalize();
      Q = (GregorianDateTime) Q.normalize();
      return internalCompare(P, Q);
    }
    else if (P.getTimezone() != DateTimeConstants.FIELD_UNDEFINED)
    {

      if (P.getTimezone() != 0)
      {
        P = (GregorianDateTime) P.normalize();
      }

      // C. step 1
      GregorianDateTime MinQ = normalizeToTimezone(Q,
          DateTimeConstants.MIN_TIMEZONE_OFFSET);
      result = internalCompare(P, MinQ);
      if (result == DateTimeConstants.LESSER)
      {
        return result;
      }

      // C. step 2
      GregorianDateTime MaxQ = normalizeToTimezone(Q,
          DateTimeConstants.MAX_TIMEZONE_OFFSET);
      result = internalCompare(P, MaxQ);
      if (result == DateTimeConstants.GREATER)
      {
        return result;
      }
      else
      {
        // C. step 3
        return DateTimeConstants.INDETERMINATE;
      }
    }
    else
    { // Q.getTimezone() != DatatypeConstants.FIELD_UNDEFINED
      // P has no timezone and Q does.
      if (Q.getTimezone() != 0)
      {
        Q = (GregorianDateTime) normalizeToTimezone(Q, Q.getTimezone());
      }

      // D. step 1
      GregorianDateTime MaxP = normalizeToTimezone(P,
          DateTimeConstants.MAX_TIMEZONE_OFFSET);
      result = internalCompare(MaxP, Q);
      if (result == DateTimeConstants.LESSER)
      {
        return result;
      }

      // D. step 2
      GregorianDateTime MinP = normalizeToTimezone(P,
          DateTimeConstants.MIN_TIMEZONE_OFFSET);
      result = internalCompare(MinP, Q);
      if (result == DateTimeConstants.GREATER)
      {
        return result;
      }
      else
      {
        // D. step 3
        return DateTimeConstants.INDETERMINATE;
      }
    }
  }

  /**
   *
   * <p>
   * Implements Step B from http://www.w3.org/TR/xmlschema-2/#dateTime-order
   * </p>
   *
   * @param P
   *          calendar instance with normalized timezone offset or having same
   *          timezone as Q
   * @param Q
   *          calendar instance with normalized timezone offset or having same
   *          timezone as P
   *
   * @return result of comparing P and Q.
   */
  private static int internalCompare(GregorianDateTime P, GregorianDateTime Q)
  {

    int result;

    // compare Year.
    result = compareField(P.getYear(), Q.getYear());
    if (result != DateTimeConstants.EQUAL)
    {
      return result;
    }

    result = compareField(P.getMonth(), Q.getMonth());
    if (result != DateTimeConstants.EQUAL)
    {
      return result;
    }

    result = compareField(P.getDay(), Q.getDay());
    if (result != DateTimeConstants.EQUAL)
    {
      return result;
    }

    result = compareField(P.getHour(), Q.getHour());
    if (result != DateTimeConstants.EQUAL)
    {
      return result;
    }

    result = compareField(P.getMinute(), Q.getMinute());
    if (result != DateTimeConstants.EQUAL)
    {
      return result;
    }
    result = compareField(P.getSecond(), Q.getSecond());
    if (result != DateTimeConstants.EQUAL)
    {
      return result;
    }

    result = compareField(P.getFractionalSecond(), Q.getFractionalSecond());
    return result;
  }

  /**
   * <p>
   * Implement Step B from http://www.w3.org/TR/xmlschema-2/#dateTime-order.
   * </p>
   */
  private static int compareField(int Pfield, int Qfield)
  {
    if (Pfield == Qfield)
    {
      //fields are either equal in value or both undefined.
      // Step B. 1.1 AND optimized result of performing 1.1-1.4.
      return DateTimeConstants.EQUAL;
    }
    else
    {
      if (Pfield == DateTimeConstants.FIELD_UNDEFINED
          || Qfield == DateTimeConstants.FIELD_UNDEFINED)
      {
        // Step B. 1.2
        return DateTimeConstants.INDETERMINATE;
      }
      else
      {
        // Step B. 1.3-4.
        return (Pfield < Qfield ? DateTimeConstants.LESSER : DateTimeConstants.GREATER);
      }
    }
  }

  private static int maximumDayInMonthFor(int year, int month)
  {
    if (month != DateTimeConstants.FEBRUARY)
    {
      return daysInMonth[month];
    }
    else
    {
      if (((year % 400) == 0) || (((year % 100) != 0) && ((year % 4) == 0)))
      {
        // is a leap year.
        return 29;
      }
      else
      {
        return daysInMonth[DateTimeConstants.FEBRUARY];
      }
    }
  }

  protected Object clone() throws CloneNotSupportedException
  {
    return new GregorianDateTime(this.year, this.month, this.day, this.hour, this.minute,
        this.second, this.fractionalSecond, this.timezone);
  }

  private final class Parser
  {
    private final String format;
    private final String value;

    private final int flen;
    private final int vlen;

    private int fidx;
    private int vidx;

    private Parser(String format, String value)
    {
      this.format = format;
      this.value = value;
      this.flen = format.length();
      this.vlen = value.length();
    }

    /**
     * <p>
     * Parse a formated <code>String</code> into an
     * <code>GregorianDateTime</code>.
     * </p>
     *
     * <p>
     * If <code>String</code> is not formated as a legal
     * <code>GregorianDateTime</code> value, an
     * <code>IllegalArgumentException</code> is thrown.
     * </p>
     *
     * @throws IllegalArgumentException
     *           If <code>String</code> is not formated as a legal
     *           <code>GregorianDateTime</code> value.
     */
    public void parse() throws IllegalArgumentException
    {
      while (fidx < flen)
      {
        char fch = format.charAt(fidx++);

        if (fch != '%')
        { // not a meta character
          skip(fch);
          continue;
        }

        // seen meta character. we don't do error check against the format
        switch (format.charAt(fidx++))
        {
          case 'Y': // year
            setYear(parseBigInteger(4));
            break;

          case 'M': // month
            setMonth(parseInt(2, 2));
            break;

          case 'D': // days
            setDay(parseInt(2, 2));
            break;

          case 'h': // hours
            setHour(parseInt(2, 2));
            break;

          case 'm': // minutes
            setMinute(parseInt(2, 2));
            break;

          case 's': // parse seconds.
            setSecond(parseInt(2, 2));

            if (peek() == '.')
            {
              setFractionalSecond(parseFractional());
            }
            break;

          case 'z': // time zone. missing, 'Z', or [+-]nn:nn
            char vch = peek();
            if (vch == 'Z')
            {
              vidx++;
              setTimezone(0);
            }
            else if (vch == '+' || vch == '-')
            {
              vidx++;
              int h = parseInt(2, 2);
              skip(':');
              int m = parseInt(2, 2);
              setTimezone((h * 60 + m) * (vch == '+' ? 1 : -1));
            }

            break;

          default:
            // illegal meta character. impossible.
            throw new InternalError();
        }
      }

      if (vidx != vlen)
      {
        // some tokens are left in the input
        throw new IllegalArgumentException(value); //,vidx);
      }
    }

    private char peek() throws IllegalArgumentException
    {
      if (vidx == vlen)
      {
        return (char) -1;
      }
      return value.charAt(vidx);
    }

    private char read() throws IllegalArgumentException
    {
      if (vidx == vlen)
      {
        throw new IllegalArgumentException(value); //,vidx);
      }
      return value.charAt(vidx++);
    }

    private void skip(char ch) throws IllegalArgumentException
    {
      if (read() != ch)
      {
        throw new IllegalArgumentException(value); //,vidx-1);
      }
    }

    private int parseInt(int minDigits, int maxDigits) throws IllegalArgumentException
    {
      int vstart = vidx;
      while (isDigit(peek()) && (vidx - vstart) <= maxDigits)
      {
        vidx++;
      }
      if ((vidx - vstart) < minDigits)
      {
        // we are expecting more digits
        throw new IllegalArgumentException(value); //,vidx);
      }

      // NumberFormatException is IllegalArgumentException
      //           try {
      return Integer.parseInt(value.substring(vstart, vidx));
      //            } catch( NumberFormatException e ) {
      //                // if the value is too long for int, NumberFormatException is thrown
      //                throw new IllegalArgumentException(value,vstart);
      //            }
    }

    private int parseBigInteger(int minDigits) throws IllegalArgumentException
    {
      int vstart = vidx;

      // skip leading negative, if it exists
      if (peek() == '-')
      {
        vidx++;
      }
      while (isDigit(peek()))
      {
        vidx++;
      }
      if ((vidx - vstart) < minDigits)
      {
        // we are expecting more digits
        throw new IllegalArgumentException(value); //,vidx);
      }

      return Integer.parseInt((value.substring(vstart, vidx)));
    }

    private int parseFractional() throws IllegalArgumentException
    {
      int vstart = vidx;

      if (peek() == '.')
      {
        vidx++;
      }
      else
      {
        throw new IllegalArgumentException(value);
      }
      while (isDigit(peek()))
      {
        vidx++;
      }
      return Integer.parseInt((value.substring(vstart+1, vidx)));
    }
  }

  private static boolean isDigit(char ch)
  {
    return '0' <= ch && ch <= '9';
  }

  private void printNumber(StringBuffer out, int number, int nDigits)
  {
    String s = String.valueOf(number);
    for (int i = s.length(); i < nDigits; i++)
      out.append('0');
    out.append(s);
  }

  public String format(String format)
  {
    StringBuffer buf = new StringBuffer();
    int fidx = 0, flen = format.length();

    while (fidx < flen)
    {
      char fch = format.charAt(fidx++);
      if (fch != '%')
      {// not a meta char
        buf.append(fch);
        continue;
      }

      switch (format.charAt(fidx++))
      {
        case 'Y':
          printNumber(buf, getYear(), 4);
          break;
        case 'M':
          printNumber(buf, getMonth(), 2);
          break;
        case 'D':
          printNumber(buf, getDay(), 2);
          break;
        case 'h':
          printNumber(buf, getHour(), 2);
          break;
        case 'm':
          printNumber(buf, getMinute(), 2);
          break;
        case 's':
          printNumber(buf, getSecond(), 2);
          if (getFractionalSecond() != DateTimeConstants.FIELD_UNDEFINED)
          {
            String frac = "." + Integer.toString(getFractionalSecond());
            //skip leading zero.
            buf.append(frac);
//            buf.append(frac.substring(1, frac.length()));
          }
          break;
        case 'z':
          int offset = getTimezone();
          if (offset == 0)
          {
            buf.append('Z');
          }
          else if (offset != DateTimeConstants.FIELD_UNDEFINED)
          {
            if (offset < 0)
            {
              buf.append('-');
              offset *= -1;
            }
            else
            {
              buf.append('+');
            }
            printNumber(buf, offset / 60, 2);
            buf.append(':');
            printNumber(buf, offset % 60, 2);
          }
          break;
        default:
          throw new InternalError(); // impossible
      }
    }
    return buf.toString();
  }

  public static GregorianDateTime parse(String lexicalRepresentation)
  {
    return new GregorianDateTime(lexicalRepresentation);
  }

  /** Add 0 prefixes to the value until length is reached. */
  public static String addPrefix(int newLength, int value)
  {
    StringBuffer builder = new StringBuffer();
    builder.append(value);
    while (builder.length()<newLength)
    {
      builder.insert(0, '0');
    }
    return builder.toString();
  }

  /** Writes out the ISO 8601 representation of this date
   *  and time.
   *
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();

    if (year != DateTimeConstants.FIELD_UNDEFINED)
    {
      buffer.append(addPrefix(4,year));
    }
    if (month != DateTimeConstants.FIELD_UNDEFINED)
    {
      buffer.append('-');
      buffer.append(addPrefix(2,month));
      if (day != DateTimeConstants.FIELD_UNDEFINED)
      {
        buffer.append('-');
        buffer.append(addPrefix(2,day));
      }
    }

    if (hour != DateTimeConstants.FIELD_UNDEFINED)
    {
      buffer.append('T');
      buffer.append(addPrefix(2,hour));
      if (minute != DateTimeConstants.FIELD_UNDEFINED)
      {
        buffer.append(':');
        buffer.append(addPrefix(2,minute));
      }
      if (second != DateTimeConstants.FIELD_UNDEFINED)
      {
        buffer.append(':');
        buffer.append(addPrefix(2,second));
      }
      if (fractionalSecond != DateTimeConstants.FIELD_UNDEFINED)
      {
        buffer.append('.');
        buffer.append(addPrefix(3,fractionalSecond));
      }
      if (timezone!= DateTimeConstants.FIELD_UNDEFINED)
      {
        if (timezone == 0)
          buffer.append('Z');
        else
        {
          int tzHour = Math.abs(timezone / 60);
          int tzMinute = Math.abs(timezone % 60);
          if (timezone < 0)
          {
            buffer.append('-');
          } else
          {
            buffer.append('+');
          }
          buffer.append(addPrefix(2,tzHour));
          buffer.append(':');
          buffer.append(addPrefix(2,tzMinute));
        }
      }
      return buffer.toString();
    }



    return buffer.toString();
  }

}
