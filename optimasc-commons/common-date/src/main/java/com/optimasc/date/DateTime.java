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
 * The following resolutions are allowed as well as their meanings:
 * <ul>
 * <li>{@link TimeAccuracy#YEAR} Resolution is at the year level.</li>
 * <li>{@link TimeAccuracy#DAY} Resolution is at the year-month-day level.</li>
 * <li>{@link TimeAccuracy#MINUTE} Resolution is up to the year-month-day-hour-minute level.</li>
 * <li>{@link TimeAccuracy#SECOND} All the fields of the Date are valid as well as
 * all the Time fields to a resolution of seconds.</li>
 * <li>{@link TimeAccuracy#MILLISECOND} Resolution is up to the year-month-day-hour-minute-second-ms level..</li>
 * </ul>
 * </p>
 * 
 * @author Carl Eric Codere
 *
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

  /** Time resolution units for date and times. Higher resolutions 
   *  have higher ordered values. */
  public static class TimeAccuracy
  {
    /** Resolution is up to the year level. Value is 0x01 */
    public static final int YEAR = Calendar.YEAR;
   /** Resolution is up to the year-month-day level. Value is 0x05 */
    public static final int DAY = Calendar.DAY_OF_MONTH;
    /** Resolution is up to the year-month-day-hour-minute level. Value is 0x0C */
    public static final int MINUTE = Calendar.MINUTE;
    /** Resolution is up to the year-month-day-hour-minute-second level. Value is 0x0D */
    public static final int SECOND = Calendar.SECOND;
    /** Resolution is up to the year-month-day-hour-minute-second-ms level. Value is 0x0E */
    public static final int MILLISECOND = Calendar.MILLISECOND;
    
    /** Verifies that the resolution is one of the valid values. 
     * 
     * @param resolution [in] The resolution to verify.
     * @throws IllegalArgumentException If value is not one
     *  of the allowed resolutions.
     */
    public static void isValidResolution(int resolution) throws IllegalArgumentException
    {
      switch (resolution)
      {
        case TimeAccuracy.YEAR:
        case TimeAccuracy.DAY:  
        case TimeAccuracy.MINUTE:  
        case TimeAccuracy.SECOND:  
        case TimeAccuracy.MILLISECOND:
          return;
        default:
          throw new IllegalArgumentException("Invalid or unknown date-time resolution");
      }
    }
    
  }
  
  

  /** Average number of days per year in the Gregorian calendar. */
  public static double DAYS_YEAR = 365.2425d;

  /** ISO8601 Timezone, only one exists which is UTC+0000 **/
  public static final TimeZone ZULU = TimeZone.getTimeZone("GMT");

  /** The date part of this date-time */
  public Date date;
  /** The time part of this date-time */
  public Time time;
  /** The precision of this value. */
  public int resolution;

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
    this.resolution = precison;
  }

  public DateTime(Date date, int precison)
  {
    super();
    this.date = date;
    this.time = null;
    this.resolution = precison;
  }

  public DateTime(int year, int month, int day)
  {
    super();
    this.date = new Date(year, month, day);
    this.time = null;
    this.resolution = TimeAccuracy.DAY;
  }

  public DateTime(int year)
  {
    super();
    this.date = new Date(year, 1, 1);
    this.time = null;
    this.resolution = TimeAccuracy.YEAR;
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
    this.resolution = TimeAccuracy.MILLISECOND;
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
    
    if (resolution == TimeAccuracy.YEAR)
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
    
    if (resolution == TimeAccuracy.DAY)
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
      
      if (resolution == TimeAccuracy.SECOND)
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
    
    // Optimization, if the calendar is already normalized simple return it
    if (currentOffset == 0)
      return input;
    java.util.Date date = new java.util.Date(currentDate.getTime() + currentOffset);
    Calendar cal = Calendar.getInstance();
    cal.setTimeZone(ZULU);
    cal.setTime(date);
    return cal;
  }
  

}
