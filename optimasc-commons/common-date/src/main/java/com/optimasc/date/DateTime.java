package com.optimasc.date;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
  /** Minimum integer value for the 24 hour representation. */
  public static int MIN_HOUR = 0;
  /** Maximum integer value for the 24 hour representation. */
  public static int MAX_HOUR = 24;
  /** Minimum integer value for the minute representation. */
  public static int MIN_MINUTE = 0;
  /** Maximum integer value for the minute representation. */
  public static int MAX_MINUTE = 59;
  /** Minimum integer value for the second representation. */
  public static int MIN_SECOND = 0;
  /** Maximum integer value for the second representation. 60
   *  is allowed as a leap second. */
  public static int MAX_SECOND = 60;
  /** Minimum integer value for the millisecond representation. */
  public static int MIN_MILLISECOND = 0;
  /** Maximum integer value for the millisecond representation. */
  public static int MAX_MILLISECOND = 999;
  

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

    /** Creates a new date from its individual
     *  components.
     * 
     * @param year The year number, where 0 
     *   represents 1 BC, -1 equal to 2 BC.
     * @param month The month value [1..12]
     * @param day The day in the month [1..31]
     * @throws IllegalArgumentException if the month 
     *   or day are outside valid ranges.
     */
    public Date(int year, int month, int day)
    {
      super();
      this.year = year;
      /* check for invalid dates */
      if ((month < DateTime.MIN_MONTH) || (month > DateTime.MAX_MONTH))
      {
        throw new IllegalArgumentException("Invalid month value, should be "+DateTime.MIN_MONTH+".."+DateTime.MAX_MONTH);
      }
      if ((day < DateTime.MIN_DAY) || (day > DateTime.MAX_DAY))
      {
        throw new IllegalArgumentException("Invalid day value, should be "+DateTime.MIN_DAY+".."+DateTime.MAX_DAY);
      }
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
    /** The hour in the day [0..24] */
    public int hour;
    /** The number of minutes past the hour [0.59] */
    public int minute;
    /** The number of seconds past the minute [0..60] */
    public int second;
    /** The milliseconds past the second [0..999] */
    public int millisecond;
    
    /** The number of milliseconds in a day */
    public static final int MILLISECONDS_IN_DAY = 86400000;
    /**
     * <code>true</code> if this is a local time, otherwise this is an UTC
     * normalized time
     */
    public boolean localTime;

    /** Creates a time from individual time components.
     * 
     * @param hour The hour in the day [0..24]
     * @param minute The number of minutes past the hour [0.59]
     * @param second The number of seconds past the minute [0..60]
     * @param millisecond The milliseconds past the second [0..999]
     * @param localTime true if this is a localtime, otherwise this 
     *  is an UTC time.
     *  
     * @throws IllegalArgumentException if one of the components
     *   of the time is invalid.
     */
    public Time(int hour, int minute, int second, int millisecond, boolean localTime)
    {
      super();
      if ((hour < DateTime.MIN_HOUR) || (hour > DateTime.MAX_HOUR))
      {
        throw new IllegalArgumentException("Invalid hour value, should be "+DateTime.MIN_HOUR+".."+DateTime.MAX_HOUR);
      }
      if ((minute < DateTime.MIN_MINUTE) || (minute > DateTime.MAX_MINUTE))
      {
        throw new IllegalArgumentException("Invalid minute value, should be "+DateTime.MIN_MINUTE+".."+DateTime.MAX_MINUTE);
      }
      if ((second < DateTime.MIN_SECOND) || (second > DateTime.MAX_SECOND))
      {
        throw new IllegalArgumentException("Invalid second value, should be "+DateTime.MIN_SECOND+".."+DateTime.MAX_SECOND);
      }
      if ((millisecond < DateTime.MIN_MILLISECOND) || (millisecond > DateTime.MAX_MILLISECOND))
      {
        throw new IllegalArgumentException("Invalid millisecond value, should be "+DateTime.MIN_MILLISECOND+".."+DateTime.MAX_MILLISECOND);
      }
      
      this.hour = hour;
      this.minute = minute;
      this.second = second;
      this.millisecond = millisecond;
      this.localTime = localTime;
    }

    /** Creates a time from individual time components.
     * 
     * @param hour The hour in the day [0..24]
     * @param minute The number of minutes past the hour [0.59]
     * @param second The number of seconds past the minute [0..60]
     * @param localTime true if this is a localtime, otherwise this 
     *  is an UTC time.
     *  
     * @throws IllegalArgumentException if one of the components
     *   of the time is invalid.
     */
    public Time(int hour, int minute, int second, boolean localTime)
    {
      super();
      if ((hour < DateTime.MIN_HOUR) || (hour > DateTime.MAX_HOUR))
      {
        throw new IllegalArgumentException("Invalid hour value, should be "+DateTime.MIN_HOUR+".."+DateTime.MAX_HOUR);
      }
      if ((minute < DateTime.MIN_MINUTE) || (minute > DateTime.MAX_MINUTE))
      {
        throw new IllegalArgumentException("Invalid minute value, should be "+DateTime.MIN_MINUTE+".."+DateTime.MAX_MINUTE);
      }
      if ((second < DateTime.MIN_SECOND) || (second > DateTime.MAX_SECOND))
      {
        throw new IllegalArgumentException("Invalid second value, should be "+DateTime.MIN_SECOND+".."+DateTime.MAX_SECOND);
      }
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
     * @throws IllegalArgumentException If the value is not a value
     *   within the allowed range.
     */
    public static Time toTime(int value, boolean localTime)
    {
      if ((value < 0) || (value > MILLISECONDS_IN_DAY))
      {
        throw new IllegalArgumentException("Number of milliseconds in a day, can be up to "+
      Integer.toString(MILLISECONDS_IN_DAY)+" but got "+Integer.toString(value));
      }
      int hour = value / (3600 * 1000);
      int remainder = value % (3600 * 1000);
      int minute = remainder / (60 * 1000);
      remainder = remainder % (60 * 1000);
      int second = remainder / 1000;
      int millisecond = remainder % (1000);
      return new Time(hour, minute, second, millisecond, localTime);
    }

    /**
     * Represents the number of milliseconds elapsed since midnight.
     */
    public long longValue()
    {
      return intValue();
    }

    /**
     * Represents the number of milliseconds elapsed since midnight.
     */
    public float floatValue()
    {
      return intValue();
    }

    /**
     * Represents the number of milliseconds elapsed since midnight.
     */
    public double doubleValue()
    {
      return intValue();
    }
  }

  /** Create a date and time with the specified 
   *  precision.
   *  
   */
  public DateTime(Date date, Time time, int precison)
  {
    super();
    this.date = date;
    this.time = time;
    this.resolution = precison;
  }

  /** Create a date and time with the specified 
   *  precision.
   *  
     * @param date [in] The date 
     * @param precision [in] The precision of
     *   this date time.
   */
  public DateTime(Date date, int precison)
  {
    super();
    this.date = date;
    this.time = null;
    this.resolution = precison;
  }

  /** Create a date and time with a precision of 
   *  {@link TimeAccuracy#DAY}. 
   *  
     * @param year The year number, where 0 
     *   represents 1 BC, -1 equal to 2 BC.
     * @param month The month value [1..12]
     * @param day The day in the month [1..31]
     * @throws IllegalArgumentException if the month 
     *   or day are outside valid ranges.
   */
  public DateTime(int year, int month, int day)
  {
    super();
    this.date = new Date(year, month, day);
    this.time = null;
    this.resolution = TimeAccuracy.DAY;
  }

  /** Create a date and time with a precision of 
   *  {@link TimeAccuracy#YEAR}. 
   *  
     * @param year The year number, where 0 
     *   represents 1 BC, -1 equal to 2 BC.
   */
  public DateTime(int year)
  {
    super();
    this.date = new Date(year, 1, 1);
    this.time = null;
    this.resolution = TimeAccuracy.YEAR;
  }

  /** Converts a Gregorian calendar instance
   *  to its internal date and time representation.
   *  
   *  Conversion includes the following:
   *  <ul>
   *   <li>normalization of the timezone to UTC using 
   *      {@link #normalize(Calendar)}</li>
   *   <li>if required, conversion of the Gregorian proleptic calendar 
   *     year to Astronomical Year Numbering. </li> 
   *   <li>setting the resolution of this date time to millisecond. </li> 
   *  </ul>    
   *  
   *   
   *  
   * 
   * @param cal [in] The calendar to convert
   */
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

  /** Returns the calendar representation of this 
   *  date and time. An error will be returned
   *  in the case where the date and time is
   *  a local time, since the <code>Calendar</code>
   *  requires a timezone.
   * 
   * @return The <code>Calendar</code> representation
   *  of this date and time.
   * @throws IllegalArgumentException if the time
   *  component of this date time represents a local time. 
   */
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
  
  /** Compares two <code>DateTime</code> 
   *  structures, and returns true if the
   *  common fields (lowest resolution
   *  values) are the same.
   * 
   */
  public boolean matches(DateTime obj)
  {
    if (obj == this)
      return true;
    int checkRes = Math.min(obj.resolution, resolution);
    return compare(checkRes,this,obj);
  }
  
  private static boolean compare(int resolution, DateTime obj, DateTime otherObj)
  {
    if (resolution >= TimeAccuracy.MILLISECOND)
    {
      if (otherObj.time.millisecond!=obj.time.millisecond)      
      {
        return false;
      }
    }
    
    if (resolution >= TimeAccuracy.SECOND)
    {
      if (otherObj.time.second!=obj.time.second)
      {
        return false;
      }
    }
    
    if (resolution >= TimeAccuracy.MINUTE)
    {
      if (otherObj.time.minute!=obj.time.minute)
      {
        return false;
      }
      
      if (otherObj.time.hour!=obj.time.hour)
      {
        return false;
      }
      if (otherObj.time.localTime!=obj.time.localTime)
      {
        return false;
      }
    }
    
    if (resolution >= TimeAccuracy.DAY)
    {
      if (otherObj.date.day!=obj.date.day)
      {
        return false;
      }
    }
    
    if (resolution >= TimeAccuracy.YEAR)
    {
      if (otherObj.date.month!=obj.date.month)
      {
        return false;
      }
      
      if (otherObj.date.year!=obj.date.year)
      {
        return false;
      }
    }
    return true;
  }
  

  /** Compares two <code>DateTime</code> 
   *  structures, and returns true if 
   *  they have the same resolution as 
   *  well as same field values.  
   * 
   */
  public boolean equals(Object obj)
  {
    DateTime otherObj;
    if (obj == this)
      return true;
    if ((obj instanceof DateTime)==false)
      return false;
    otherObj = (DateTime)obj;
    if (otherObj.resolution!=this.resolution)
    {
      return false;
    }
    return compare(resolution,this,otherObj);
  }

  /**
   * Normalize this calendar instance to UTC.
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
