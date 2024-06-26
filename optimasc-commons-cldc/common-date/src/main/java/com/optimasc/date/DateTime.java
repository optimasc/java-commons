package com.optimasc.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Class that represents a Gregorian date and time using Astronomical Year
 * Numbering to conform to ISO 8601. The precision indicates the precision of
 * this DateTime.
 * 
 * <p>
 * The following precisions are allowed as well as their meanings:
 * <ul>
 * <li>{@link Calendar#YEAR} Only the year value of the {@link Date} part is
 * valid. Time is invalid and is null.</li>
 * <li>{@link Calendar#DAY_OF_MONTH} All the fields of the Date are valid. Time
 * is invalid and is null.</li>
 * <li>{@link Calendar#SECOND} All the fields of the Date are valid as well as
 * all the Time fields to a resolution of seconds.</li>
 * <li>{@link Calendar#MILLISECOND} All the fields of the Date are valid as well
 * as all the Time fields to a resolution of milliseconds.</li>
 * </ul>
 * </p>
 * 
 * @author Carl Eric Codere
 *
 */
public class DateTime
{
  /** Minimum integer value for month representation. */
  public static int MIN_MONTH = 1;
  /** Maximum integer value for month representation. */
  public static int MAX_MONTH = 12;
  /** Minimum integer value for day representation. */
  public static int MIN_DAY = 1;
  /** Maximum integer value for day representation. */
  public static int MAX_DAY = 31;

  /** Average number of days per year in the Gregorian calendar. */
  public static double DAYS_YEAR = 365.2425d;

  /** ISO8601 Timezone, only one exists which is UTC+0000 **/
  public static final TimeZone ZULU = TimeZone.getTimeZone("GMT");

  /** The date part of this date-time */
  public Date date;
  /** The time part of this date-time */
  public Time time;
  /** The precision of this value. */
  public int precision;

  /**
   * Class that represents a Gregorian date.
   * 
   * @author Carl Eric Codere
   *
   */
  public static class Date
  {
    /** The year, with both positive, zero and negative values allowed. */
    public int year;
    /** The month of the year, in [1..12] range */
    public int month;
    /** The day of the month, in [1..31] range */
    public int day;

    public Date(int year, int month, int day)
    {
      super();
      this.year = year;
      this.month = month;
      this.day = day;
    }
  }

  /**
   * Class that represents a 24-hour time, either in UTC timezone or unknown
   * local time.
   * 
   * @author Carl Eric Codere
   *
   */
  public static class Time
  {
    public int hour;
    public int minute;
    public int second;
    public int millisecond;
    /**
     * <code>true</code> if this is a local time, otherwise this is an UTC
     * normalized time
     */
    public boolean localTime;

    public Time(int hour, int minute, int second, int millisecond, boolean localTime)
    {
      super();
      this.hour = hour;
      this.minute = minute;
      this.second = second;
      this.millisecond = millisecond;
      this.localTime = localTime;
    }

    public Time(int hour, int minute, int second, boolean localTime)
    {
      super();
      this.hour = hour;
      this.minute = minute;
      this.second = second;
      this.millisecond = 0;
      this.localTime = localTime;
    }

    /**
     * Represents the number of milliseconds elapsed since midnight.
     */
    public int intValue()
    {
      return (hour * 60 * 60 + minute * 60 + second) * 1000 + millisecond;
    }

    /**
     * Returns a Time type from a value that contains the number of milliseconds
     * elapsed since 00:00:00
     * 
     * @param value
     *          [in] The value in milliseconds
     * @param localTime
     *          [in] Indicates if this is a local time or an UTC time.
     * @return The time value
     */
    public static Time toTime(int value, boolean localTime)
    {
      int hour = value / (3600 * 1000);
      int remainder = value % (3600 * 1000);
      int minute = remainder / (60 * 1000);
      remainder = remainder % (60 * 1000);
      int second = remainder / 1000;
      int millisecond = remainder % (1000);
      return new Time(hour, minute, second, millisecond, localTime);
    }

    public long longValue()
    {
      return intValue();
    }

    public float floatValue()
    {
      return intValue();
    }

    public double doubleValue()
    {
      return intValue();
    }
  }

  public DateTime(Date date, Time time, int precison)
  {
    super();
    this.date = date;
    this.time = time;
    this.precision = precison;
  }

  public DateTime(Date date, int precison)
  {
    super();
    this.date = date;
    this.time = null;
    this.precision = precison;
  }

  public DateTime(int year, int month, int day)
  {
    super();
    this.date = new Date(year, month, day);
    this.time = null;
    this.precision = Calendar.DAY_OF_MONTH;
  }

  public DateTime(int year)
  {
    super();
    this.date = new Date(year, 1, 1);
    this.time = null;
    this.precision = Calendar.YEAR;
  }

  public DateTime(Calendar cal)
  {
    super();
    cal = normalize(cal);
    int year = cal.get(Calendar.YEAR);
    // Adjust the era
    if (cal instanceof GregorianCalendar)
    {
      // Adjust the year correctly for Astronomical year numbering
      // to align with ISO 8601.
      int era = cal.get(Calendar.ERA);
      if (era == GregorianCalendar.BC)
      {
        // Adjust the year correctly to ISO 8601 standard.
        // 1 BC becomes 0
        // 2 BC becomes -1
        // ...
        year = (year - 1);
        // Invert the sign
        year = -year;
      }
    }
    this.date = new Date(year, cal.get(Calendar.MONTH) + 1,
        cal.get(Calendar.DAY_OF_MONTH));
    this.time = new Time(cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE),
        cal.get(Calendar.SECOND), cal.get(Calendar.MILLISECOND), false);
    this.precision = Calendar.MILLISECOND;
  }

  public Calendar toCalendar()
  {
    if (time != null)
    {
      if (time.localTime == true)
      {
        throw new IllegalArgumentException(
            "Cannot convert a local time to a Calendar which requires a Timezone");
      }
    }
    GregorianCalendar calendar;
    calendar = new GregorianCalendar(ZULU);
    calendar.setGregorianChange(new java.util.Date(Long.MIN_VALUE));
    if (date.year <= 0)
    {
      calendar.set(Calendar.ERA, GregorianCalendar.BC);
      calendar.set(Calendar.YEAR, Math.abs(date.year - 1));
    }
    else
    {
      calendar.set(Calendar.ERA, GregorianCalendar.AD);
      calendar.set(Calendar.YEAR, date.year);
    }
    
    if (precision == Calendar.YEAR)
    {
      calendar.set(Calendar.MONTH, 0);
      calendar.set(Calendar.DAY_OF_MONTH, 1);
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      calendar.setTimeZone(ZULU);
      return calendar;
    }
    
    calendar.set(Calendar.MONTH, date.month - 1);
    calendar.set(Calendar.DAY_OF_MONTH, date.day);
    
    if (precision == Calendar.DAY_OF_MONTH)
    {
      calendar.set(Calendar.HOUR_OF_DAY, 0);
      calendar.set(Calendar.MINUTE, 0);
      calendar.set(Calendar.SECOND, 0);
      calendar.set(Calendar.MILLISECOND, 0);
      calendar.setTimeZone(ZULU);
      return calendar;
    }
    
    
    if (time!=null)
    {
      calendar.set(Calendar.HOUR_OF_DAY, time.hour);
      calendar.set(Calendar.MINUTE, time.minute);
      calendar.set(Calendar.SECOND, time.second);
      
      if (precision == Calendar.SECOND)
      {
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.setTimeZone(ZULU);
        return calendar;
      }
      calendar.set(Calendar.MILLISECOND, time.millisecond);
    }
    calendar.setTimeZone(ZULU);
    return calendar;
  }

  /**
   * Normalize this instance to UTC.
   * 
   * <p>
   * For example, <code>2000-03-04T23:00:00+03:00</code> normalizes to
   * <code>2000-03-04T20:00:00Z</code>. Internally it uses the "GMT" timezone to
   * normalize to UTC using the raw offset.
   * </p>
   *
   * @param input
   *          [in] The calendar to normalize to UTC
   * @return The normalized calendar which is now in UTC timezone.
   */
  public static Calendar normalize(Calendar input)
  {
    java.util.Date currentDate = input.getTime();
    long currentOffset = input.getTimeZone().getRawOffset();
    java.util.Date date = new java.util.Date(currentDate.getTime() + currentOffset);
    Calendar cal = Calendar.getInstance(ZULU);
    cal.setTime(date);
    return cal;
  }

}
