package com.optimasc.lang;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Gregorian Calendar that does not necessarily represent a fixed instant in
 * time. This class is compatible with the {@link java.lang.Calendar} class with
 * additional functionality.
 * 
 * <p>This calendar supports a year and zero in the constructor to be compatible
 * with ISO 8601:2004 which uses astronomical year numbering (0 is equal to 1 BC, -1 
 * is equal to 2 BC).</p> 
 * 
 * <p>This calendar permits to distinguish fields that were specifically set by the
 * caller and those that were internally calculated. Information on user set
 * fields can be retrieved by calling {@link #isUserSet(int)}.</p>
 * 
 * <p>
 * It is to note that {@link #setTimeZone(TimeZone)} should be called to set
 * timezone information and not the {@link #set(int, int)} method. And using the
 * constructors, and {@link #get(int)} and {@link #set(int, int)} methods,
 * months are based on 0 and not 1 as in the standard <code>Calendar</code>
 * </p>
 * 
 * @author Carl Eric Codere
 *
 */
public class GregorianDatetimeCalendar extends GregorianCalendar
{
  /**
   * Array indicating if the field was explicitly set by the caller or if it was
   * calculated internally.
   */
  protected boolean userSet[];

  /** ISO8601 Timezone, only one exists which is UTC+0000 **/
  public static final TimeZone ZULU = new SimpleTimeZone(0, "Z");

  /**
   * This valuie is returned by the explicit getters and setter methods of this
   * class, if the value is not defined.
   */
  public static final int FIELD_UNDEFINED = Integer.MIN_VALUE;

  /**
   * 
   */
  private static final long serialVersionUID = 3816584737503218331L;

  public GregorianDatetimeCalendar()
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    clear();
    Arrays.fill(userSet, false);
  }
  
  
  public static GregorianDatetimeCalendar normalize(Calendar input)
  {
    long currentDate = input.getTimeInMillis();
    long currentOffset = input.getTimeZone().getRawOffset();
    java.util.Date date = new java.util.Date(currentDate+currentOffset);
    GregorianDatetimeCalendar cal = new GregorianDatetimeCalendar();
    cal.setTime(date);
    cal.setTimeZone(ZULU);
    return cal;    
  }

  /**
   * Constructs a GregorianCalendar with the given date set in the default time
   * zone. The following options are supported:
   * 
   * @param year
   *          The year, if the value is zero, or negative, the era
   *          will be set to the <code>BC</code>.
   * @param month
   *          The month, allowed values from 0 to 11 where 0 represents
   *          <code>January</code> or {@link #FIELD_UNDEFINED}.
   * @param dayOfMonth
   *          The day in the month, allowed values are between 1 and 31 or
   *          {@link GregorianDatetimeCalendar#FIELD_UNDEFINED}
   * @throws IllegalArgumentException
   *           If one of the value is not within the allowed ranges.
   */
  public GregorianDatetimeCalendar(int year, int month, int dayOfMonth)
  {
    this();
    
    if (year <= 0)
    {
      year = Math.abs(- 1 - year);
      set(Calendar.ERA, GregorianCalendar.BC);
      set(Calendar.YEAR, year);
    } else
    {
      set(Calendar.ERA, GregorianCalendar.AD);
      set(Calendar.YEAR, year);
    }
    
    if (year < getMinimum(Calendar.YEAR))
      throw new IllegalArgumentException(
          "The year must be positive when using this constructor.");
    else
      set(Calendar.YEAR, year);

    if ((month == FIELD_UNDEFINED) && (dayOfMonth == FIELD_UNDEFINED))
    {
      return;
    }

    if ((month < getMinimum(Calendar.MONTH)) || (month > getMaximum(Calendar.MONTH)))
    {
      throw new IllegalArgumentException("The month must be between 0 and 11.");
    }
    {
      set(Calendar.MONTH, month);
    }

    if (dayOfMonth == FIELD_UNDEFINED)
    {
      return;
    }
    if ((dayOfMonth < getMinimum(Calendar.DAY_OF_MONTH))
        || (dayOfMonth > getMaximum(Calendar.DAY_OF_MONTH)))
    {
      throw new IllegalArgumentException("The day of month must be between 1 and 31.");
    }
    set(Calendar.DAY_OF_MONTH, dayOfMonth);
  }
  
  
  /**
   * Constructs a GregorianCalendar with the given date set in the specified time
   * zone. The following options are supported:
   * 
   * @param year
   *          The year, if the value is zero, or negative, the era
   *          will be set to the <code>BC</code>.
   * @param month
   *          The month, allowed values from 0 to 11 where 0 represents
   *          <code>January</code> or {@link #FIELD_UNDEFINED}.
   * @param dayOfMonth
   *          The day in the month, allowed values are between 1 and 31 or
   *          {@link GregorianDatetimeCalendar#FIELD_UNDEFINED}
   * @param tz
   *          The timezone offset in milliseconds
   * @throws IllegalArgumentException
   *           If one of the value is not within the allowed ranges.
   */
  public GregorianDatetimeCalendar(int year, int month, int dayOfMonth, TimeZone tz)
  {
    this();
    setTimeZone(tz);
    
    if (year <= 0)
    {
      year = Math.abs(- 1 - year);
      set(Calendar.ERA, GregorianCalendar.BC);
      set(Calendar.YEAR, year);
    } else
    {
      set(Calendar.ERA, GregorianCalendar.AD);
      set(Calendar.YEAR, year);
    }
    
    if (year < getMinimum(Calendar.YEAR))
      throw new IllegalArgumentException(
          "The year must be positive when using this constructor.");
    else
      set(Calendar.YEAR, year);

    if ((month == FIELD_UNDEFINED) && (dayOfMonth == FIELD_UNDEFINED))
    {
      return;
    }

    if ((month < getMinimum(Calendar.MONTH)) || (month > getMaximum(Calendar.MONTH)))
    {
      throw new IllegalArgumentException("The month must be between 0 and 11.");
    }
    {
      set(Calendar.MONTH, month);
    }

    if (dayOfMonth == FIELD_UNDEFINED)
    {
      return;
    }
    if ((dayOfMonth < getMinimum(Calendar.DAY_OF_MONTH))
        || (dayOfMonth > getMaximum(Calendar.DAY_OF_MONTH)))
    {
      throw new IllegalArgumentException("The day of month must be between 1 and 31.");
    }
    set(Calendar.DAY_OF_MONTH, dayOfMonth);
  }
  

  /**
   * Constructs a date-time representation based on values. It is possible to
   * specify minutes and seconds as undefined, but if they are defined, it
   * is considered to be 'local time'.
   *
   * @param year
   *          The year, if the value is zero, or negative, the era
   *          will be set to the <code>BC</code>.
   * @param month
   *          The month, allowed values from 0 to 11 where 0 represents
   *          <code>January</code>.
   * @param dayOfMonth
   *          The day in the month, allowed values are between 1 and 31
   * @param hour
   *          Hours, allowed values are between 0 and 23
   * @param minute
   *          Minutes, allowed values are between 0 and 59 or
   *          {@link #FIELD_UNDEFINED}
   * @param second
   *          Seconds, allowed values are between 0 and 60 (for leap seconds) or
   *          {@link #FIELD_UNDEFINED}
   * @throws IllegalArgumentException
   *           If one of the value is not within the allowed ranges.
   */
  public GregorianDatetimeCalendar(int year, int month, int dayOfMonth, int hour,
      int minute, int second)
  {
    this();
    if (year <= 0)
    {
      year = Math.abs(- 1 - year);
      set(Calendar.ERA, GregorianCalendar.BC);
      set(Calendar.YEAR, year);
    } else
    {
      set(Calendar.ERA, GregorianCalendar.AD);
      set(Calendar.YEAR, year);
    }

    if ((month < getMinimum(Calendar.MONTH)) || (month > getMaximum(Calendar.MONTH)))
    {
      throw new IllegalArgumentException("The month must be between 0 and 11.");
    }
    {
      set(Calendar.MONTH, month);
    }

    if ((dayOfMonth < getMinimum(Calendar.DAY_OF_MONTH))
        || (dayOfMonth > getMaximum(Calendar.DAY_OF_MONTH)))
    {
      throw new IllegalArgumentException("The day of month must be between 1 and 31.");
    }
    set(Calendar.DAY_OF_MONTH, dayOfMonth);

    if ((hour < getMinimum(Calendar.HOUR_OF_DAY))
        || (hour > getMaximum(Calendar.HOUR_OF_DAY)))
    {
      throw new IllegalArgumentException("The hour of day must be between 0 and 23.");
    }
    else
    {
      set(Calendar.HOUR_OF_DAY, hour);
    }

    if (minute == FIELD_UNDEFINED)
    {
      return;
    }

    if ((minute < getMinimum(Calendar.MINUTE)) || (minute > getMaximum(Calendar.MINUTE)))
    {
      throw new IllegalArgumentException("The minute must be between 0 and 59.");
    }
    set(Calendar.MINUTE, minute);

    if (second == FIELD_UNDEFINED)
    {
      return;
    }
    if ((second < getMinimum(Calendar.SECOND)) || (second > getMaximum(Calendar.SECOND)))
    {
      throw new IllegalArgumentException("The second must be between 0 and 59.");
    }
    set(Calendar.SECOND, second);
  }

  /**
   * Constructs a date-time representation based on values. All fields must be
   * have valid values. This assumes that this date-time contains timezone
   * information.
   *
   * @param year
   *          A positive year number.
   * @param month
   *          The month, allowed values from 0 to 11 where 0 represents
   *          <code>January</code>.
   * @param dayOfMonth
   *          The day in the month, allowed values are between 1 and 31.
   * @param hour
   *          Hours, allowed values are between 0 and 23.
   * @param minute
   *          Minutes, allowed values are between 0 and 59
   * @param second
   *          Seconds, allowed values are between 0 and 60 (for leap seconds)
   * @param milliseconds
   *          Milliseconds, allowed values between 0 and 999
   * @param tz
   *          The timezone offset in milliseconds
   */
  public GregorianDatetimeCalendar(int year, int month, int dayOfMonth, int hour,
      int minute, int second, int milliseconds, int tz)
  {
    this(year, month, dayOfMonth, hour, minute, second);
    setTimeZone(new SimpleTimeZone(tz, ""));
    

    if (milliseconds == FIELD_UNDEFINED)
    {

    }
    else if ((milliseconds < getMinimum(Calendar.MILLISECOND))
        || (milliseconds > getMaximum(Calendar.MILLISECOND)))
    {
      throw new IllegalArgumentException("The milliseconds must be between 0 and 999.");
    }
    else
    {
      set(Calendar.MILLISECOND, milliseconds);
    }

    if (tz == FIELD_UNDEFINED)
    {
      return;
    }

    if ((tz < getMinimum(Calendar.ZONE_OFFSET))
        || (milliseconds > getMaximum(Calendar.ZONE_OFFSET)))
    {
      throw new IllegalArgumentException(
          "The timezone offset must be a valid range in milliseconds.");
    }

    setTimeZone(new SimpleTimeZone(tz, ""));
  }
  
  
  public GregorianDatetimeCalendar(int hour,
      int minute, int second, int milliseconds, int tz)
  {
    this();
    
    if ((hour < getMinimum(Calendar.HOUR_OF_DAY))
        || (hour > getMaximum(Calendar.HOUR_OF_DAY)))
    {
      throw new IllegalArgumentException("The hour of day must be between 0 and 23.");
    }
    set(Calendar.HOUR_OF_DAY, hour);

    if ((minute < getMinimum(Calendar.MINUTE)) || (minute > getMaximum(Calendar.MINUTE)))
    {
      throw new IllegalArgumentException("The minute must be between 0 and 59.");
    }
    set(Calendar.MINUTE, minute);
    if ((second < getMinimum(Calendar.SECOND)) || (second > getMaximum(Calendar.SECOND)))
    {
      throw new IllegalArgumentException("The second must be between 0 and 59.");
    }
    set(Calendar.SECOND, second);
    
    
    if (milliseconds == FIELD_UNDEFINED)
    {

    }
    else if ((milliseconds < getMinimum(Calendar.MILLISECOND))
        || (milliseconds > getMaximum(Calendar.MILLISECOND)))
    {
      throw new IllegalArgumentException("The milliseconds must be between 0 and 999.");
    }
    else
    {
      set(Calendar.MILLISECOND, milliseconds);
    }
    if (tz == FIELD_UNDEFINED)
    {
      return;
    }

    if ((tz < getMinimum(Calendar.ZONE_OFFSET))
        || (milliseconds > getMaximum(Calendar.ZONE_OFFSET)))
    {
      throw new IllegalArgumentException(
          "The timezone offset must be a valid range in milliseconds.");
    }

  }

  public void setTimeInMillis(long millis)
  {
    super.setTimeInMillis(millis);
    // When this is called everything is automatically set.
    // But only do it outside a constructor
    if (userSet != null)
      Arrays.fill(userSet, true);
  }

  public void set(int field, int value)
  {
    super.set(field, value);
    userSet[field] = true;
    // Special case when HOUR is set or HOUR_OF_DAY is set,
    // automatically it sets the other field when fields
    // are calculated.
    if (field == HOUR_OF_DAY)
    {
      userSet[HOUR] = true;
      userSet[AM_PM] = true;
    }
    if (field == HOUR)
    {
      userSet[HOUR_OF_DAY] = true;
      userSet[AM_PM] = true;
    }
  }

  public boolean isUserSet(int field)
  {
    // If clear was called, then all fields 
    // may have been cleared EXCEPT for Timezone information
    if (isSet(field) == false)
    {
      userSet[field] = false;
    }
    return userSet[field];
  }

  public void setTimeZone(TimeZone value)
  {
    super.setTimeZone(value);
    set(DST_OFFSET, value.getDSTSavings());
    set(ZONE_OFFSET, value.getRawOffset());
  }

  /**
   * Returns the hour field value or {@link #FIELD_UNDEFINED} if the value has
   * not been manually set.
   * 
   * @return The hour between 0 and 24 or {@link #FIELD_UNDEFINED}
   */
  public int getHour()
  {
    if (isUserSet(Calendar.HOUR_OF_DAY) == false)
      return FIELD_UNDEFINED;
    return get(Calendar.HOUR_OF_DAY);
  }

  /**
   * Returns the minute field value or {@link #FIELD_UNDEFINED} if the value has
   * not been manually set.
   * 
   * @return The minute between 0 and 59 or {@link #FIELD_UNDEFINED}
   */
  public int getMinute()
  {
    if (isUserSet(Calendar.MINUTE) == false)
      return FIELD_UNDEFINED;
    return get(Calendar.MINUTE);
  }

  /**
   * Returns the second field value or {@link #FIELD_UNDEFINED} if the value has
   * not been manually set.
   * 
   * @return The second between 0 and 60 or {@link #FIELD_UNDEFINED}
   */
  public int getSecond()
  {
    if (isUserSet(Calendar.SECOND) == false)
      return FIELD_UNDEFINED;
    return get(Calendar.SECOND);
  }

  /**
   * Returns the millisecond field value or {@link #FIELD_UNDEFINED} if the
   * value has not been manually set.
   * 
   * @return The millisecond between 0 and 999 or {@link #FIELD_UNDEFINED}
   */
  public int getMillisecond()
  {
    if (isUserSet(Calendar.MILLISECOND) == false)
      return FIELD_UNDEFINED;
    return get(Calendar.MILLISECOND);
  }

  /**
   * Returns the month field value or {@link #FIELD_UNDEFINED} between 1 and 12
   * (contrary to the <code>Calendar</code> methods. if the value has not been
   * manually set.
   * 
   * @return The month of year between 1 and 12 or {@link #FIELD_UNDEFINED}
   */
  public int getMonth()
  {
    if (isUserSet(Calendar.MONTH) == false)
      return FIELD_UNDEFINED;
    return get(Calendar.MONTH) + 1;
  }

  /**
   * Returns the day of the month field value or {@link #FIELD_UNDEFINED} if the
   * value has not been manually set.
   * 
   * @return The day of month of year between 1 and 31 or
   *         {@link #FIELD_UNDEFINED}
   */
  public int getDay()
  {
    if (isUserSet(Calendar.DAY_OF_MONTH) == false)
      return FIELD_UNDEFINED;
    return get(Calendar.DAY_OF_MONTH);
  }

  /**
   * Returns the year field value or {@link #FIELD_UNDEFINED} if the value has
   * not been manually set.
   * 
   * @return The year or {@link #FIELD_UNDEFINED}
   */
  public int getYear()
  {
    if (isUserSet(Calendar.YEAR) == false)
      return FIELD_UNDEFINED;
    return get(Calendar.YEAR);
  }

  /**
   * Returns the timezone offsets in minutes or {@link #FIELD_UNDEFINED} if the
   * value has not been manually set.
   * 
   * @return The timezone offset in minutes or {@link #FIELD_UNDEFINED}.
   */
  public int getTimezone()
  {
    if (isUserSet(Calendar.ZONE_OFFSET) == false)
      return FIELD_UNDEFINED;
    return get(Calendar.ZONE_OFFSET) / (1000 * 60);
  }

  /**
   * Returns the ISO 8601 representation of this date and time.
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();

    int year = getYear();
    int month = getMonth();
    int day = getDay();
    int hour = getHour();
    int minute = getMinute();
    int second = getSecond();
    int millisecond = getMillisecond();
    int timezone = getTimezone();

    if (year != FIELD_UNDEFINED)
    {
      buffer.append(addPrefix(4, year));
    }
    if (month != FIELD_UNDEFINED)
    {
      buffer.append('-');
      buffer.append(addPrefix(2, month));
      if (day != FIELD_UNDEFINED)
      {
        buffer.append('-');
        buffer.append(addPrefix(2, day));
      }
    }

    if (hour != FIELD_UNDEFINED)
    {
      buffer.append('T');
      buffer.append(addPrefix(2, hour));
      if (minute != FIELD_UNDEFINED)
      {
        buffer.append(':');
        buffer.append(addPrefix(2, minute));
      }
      if (second != FIELD_UNDEFINED)
      {
        buffer.append(':');
        buffer.append(addPrefix(2, second));
      }
      if (millisecond != FIELD_UNDEFINED)
      {
        buffer.append('.');
        buffer.append(addPrefix(3, millisecond));
      }
      if (timezone != FIELD_UNDEFINED)
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
          }
          else
          {
            buffer.append('+');
          }
          buffer.append(addPrefix(2, tzHour));
          buffer.append(':');
          buffer.append(addPrefix(2, tzMinute));
        }
      }
      return buffer.toString();
    }
    return buffer.toString();
  }

  /** Add 0 prefixes to the value until length is reached. */
  public static String addPrefix(int newLength, int value)
  {
    StringBuffer builder = new StringBuffer();
    builder.append(value);
    while (builder.length() < newLength)
    {
      builder.insert(0, '0');
    }
    return builder.toString();
  }
  
  

}
