package com.optimasc.date;

import java.util.Calendar;

/** Class used to convert a date and time or date or {@link DateTime}
 *  to a duration numeric value from a certain fixed time 'epoch' with
 *  a certain time scale.
 *  
 *  <p>Several assumptions are made in the conversions.
 *   
 *   <ul>
 *    <li>All calendar dates and times and encoded represent 
 *     proleptic Gregorian date and times.</li>
 *    <li>The year numbering is compatible with ISO 8601:2004,
 *     astronomical year numbering, hence year 0 is equal to 1 BC.</li>
 *    <li>It assumes that times are all associated with the UTC
 *      timezone, unless otherwise noted for precisions of seconds
 *      and milliseconds.</li>
 *   </ul>
 *  </p>
 *  
 **/
public abstract class DateConverter
{
  /** The number of seconds per day, assuming there are no leap seconds. */
  public static final int SECONDS_DAY = 24*60*60;
  /** The number of milliseconds per day, assuming there are no leap seconds. */
  public static final int MILLISECONDS_DAY = 24*60*60*1000;
  
  /** The number of years in a Gregorian leap cycle */
  protected static final int YEARS_IN_GREGORIAN_CYCLE = 400;
  /** The number of days per leap cycle */
  protected static final long DAYS_IN_GREGORIAN_CYCLE =     ((365 * YEARS_IN_GREGORIAN_CYCLE) + 100 - 4 + 1);
  
  /** The accumulated number of days per year.
   *  Index 0 = Non-Leap year.
   *  Index 1 = Leap years.
   * 
   */
  protected static final int julian_days_by_month[][] = {
    {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, 304, 334},
    {0, 31, 60, 91, 121, 152, 182, 213, 244, 274, 305, 335},
};
  
  /** A table providing the number of days pear year,
   *  with index 0 = Non leap year and index 1 = for a leap year.
   */
  protected static final int length_of_year[] = { 365, 366 };
  
  /** The number of dayes per month where January is at index 0.
   *  Index 0 = Non-Leap year.
   *  Index 1 = Leap years.
   * 
   */
  protected static final int days_in_month[][] = {
    {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
    {31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31},
};

  
  /** Verifies if the specified year is a leap year or
   *  not.
   * 
   * @param year [in] The year that needs to be verified.
   * @return 1 if the year is a leap year otherwise 0.
   */
  private static int isLeapYear(int year) 
  {
     boolean b=year % 4 == 0 && (year % 100 != 0 || year % 400 == 0);
     if (b)
       return 1;
     return 0;
  }
  
  
  /** Encode a date and time to the specified time scale 
   *  and point of reference  'epoch'. 
   *
   * @param value [in] The value, should contain 
   *   at least valid years, month and days.
   * @param format [in] The time scale unit of the value
   *   as well as the reference value. If the value
   *   is <code>null</code> the default time unit and
   *   time reference is used.
   * @return The value according to specified reference 
   *  'epoch' in the correct time scale unit.
   * @throws IllegalArgumentException If the input date and
   *  time contains invalid values or precisions. 
   *  
   */
  public static long toDuration(DateTime value, DateTimeFormat targetFormat)
  {
    long dateValue;
    long timeValue = 0;
    
    if (targetFormat == null)
    {
      targetFormat = DateTimeFormat.DEFAULT_DAY;
    }
    
    switch (targetFormat.timeUnit)
    {
      case DateTimeFormat.TimeUnit.DAYS:
        dateValue =  encodeDate(value.date.year,value.date.month,value.date.day)-targetFormat.epoch;
        return dateValue;
      case DateTimeFormat.TimeUnit.SECONDS:
        dateValue =  encodeDate(value.date.year,value.date.month,value.date.day);
        // Convert to seconds
        dateValue = dateValue * SECONDS_DAY;
        if (value.time != null)
        {
          timeValue = value.time.longValue()/1000;
        }
        return dateValue + timeValue - targetFormat.epoch;
      case DateTimeFormat.TimeUnit.MILLISECONDS:
        dateValue =  encodeDate(value.date.year,value.date.month,value.date.day);
        // Convert to seconds
        dateValue = dateValue * MILLISECONDS_DAY;
        if (value.time != null)
        {
          timeValue = value.time.longValue();
        }
        return dateValue + timeValue - targetFormat.epoch;
      default:
        throw new IllegalArgumentException("Unsupported time unit.");
    }
  }
  
  /** Generic routine that permits to decode an instant value 
   *  using the specified time scale and point of reference 
   *  'epoch'. 
   *
   *  
   * @param value [in] The value, using the specified units
   *   and reference point.
   * @param format [in] The time scale unit of the value
   *   as well as the reference value. If the value
   *   is <code>null</code> the default time unit and
   *   time reference is used.
   * @return The filled up date time with correct precision.
   */
  public static DateTime toDateTime(long value, DateTimeFormat format)
  {
    DateTime.Date date = null;
    DateTime.Time time = null;
    long remainder;
    long quotient;
    if (format == null)
    {
      format = DateTimeFormat.DEFAULT_DAY;
    }
    switch (format.timeUnit)
    {
      case DateTimeFormat.TimeUnit.DAYS:
        date =  decodeDate(value+format.epoch);
        return new DateTime(date,null,Calendar.DAY_OF_MONTH);
      case DateTimeFormat.TimeUnit.SECONDS:
        // Convert to days
        quotient = (value+format.epoch)/SECONDS_DAY;
        // Convert to second
        remainder = (value+format.epoch) % SECONDS_DAY;
        // Convert to milliseconds
        remainder = remainder * 1000;
        date = decodeDate(quotient);
        time = decodeTime(remainder);
        return new DateTime(date,time,Calendar.SECOND);
      case DateTimeFormat.TimeUnit.MILLISECONDS:
        // Convert to days
        quotient = (value+format.epoch)/MILLISECONDS_DAY;
        // Convert to second
        remainder = (value+format.epoch) % MILLISECONDS_DAY;
        date = decodeDate(quotient);
        time = decodeTime(remainder);
        return new DateTime(date,time,Calendar.MILLISECOND);
      default:
        throw new IllegalArgumentException("Unsupported time unit.");
    }
  }
  
  /** Method that converts a Gregorian proleptic full date
   *  to a number of days since the internal epoch.
   * 
   * @param year [in] The year, in astronomical year numbering.
   * @param month [in] The month of the year [1..12]
   * @param day [in] The day of the month [1..31]
   * @return The number of days elapsed according to the internal
   *   reference implementation.
   */
  public static long encodeDate(int year, int month, int day)
  {
    /* check for invalid dates */
    if ((month < DateTime.MIN_MONTH) || (month > DateTime.MAX_MONTH))
    {
      throw new IllegalArgumentException("Invalid month value, should be "+DateTime.MIN_MONTH+".."+DateTime.MAX_MONTH);
    }
    if ((day < DateTime.MIN_DAY) || (day > DateTime.MAX_DAY))
    {
      throw new IllegalArgumentException("Invalid day value, should be "+DateTime.MIN_DAY+".."+DateTime.MAX_DAY);
    }
    long days    = 0;
    int     orig_year = year;
    long      cycles  = 0;
    month--;
    
    if( orig_year > 100 ) 
    {
        cycles = (orig_year - 100) / 400;
        orig_year -= cycles * 400;
        days      += (long)cycles * DAYS_IN_GREGORIAN_CYCLE;
    }
    else if( orig_year < -300 ) 
    {
        cycles = (orig_year - 100) / 400;
        orig_year -= cycles * 400;
        days      += (long)cycles * DAYS_IN_GREGORIAN_CYCLE;
    }
    
    if( orig_year > 0 ) {
        year = 0;
        while( year < orig_year ) 
        {
            days += length_of_year[isLeapYear(year)];
            year++;
        }
    }
    else if ( orig_year < 0 ) {
        year = -1;
        do {
            days -= length_of_year[isLeapYear(year)];
            year--;
        } while( year >= orig_year );
    }
    
    days += julian_days_by_month[isLeapYear(orig_year)][month];
    days += day - 1;
    return days;
  }
  
  /** Decodes a time specification which is the number of 
   *  milliseconds elapsed since 00:00:00 UTC. 
   * 
   * @param value [in] The number of milliseconds elapsed.
   * @return The time value
   */
  protected static DateTime.Time decodeTime(long value)
  {
    int seconds = (int) (value / 1000) % 60 ;
    int minutes = (int) ((value / (1000*60)) % 60);
    int hours   = (int) ((value / (1000*60*60)) % 24);
    int y = 60*60*1000;
    int milliseconds = (int) (value-(hours*y)-(minutes*(y/60))-(seconds*1000));
    return new DateTime.Time(hours,minutes,seconds,milliseconds,false);
  }
  

  protected static DateTime.Date decodeDate(long value)
  {
    int v_tm_mon;
    long v_tm_tday;
    int day;
    int month;
    int leap;
    long m;
    long time = value;
    int year = 0;
    int cycles = 0;
    
    v_tm_tday = time;
    m = v_tm_tday;
    if (m >= 0) 
    {
        /* Gregorian cycles, this is huge optimization for distant times */
        cycles = (int)(m /  DAYS_IN_GREGORIAN_CYCLE);
        if( cycles!=0 )  {
            m -= (cycles *  DAYS_IN_GREGORIAN_CYCLE);
            year += (cycles * YEARS_IN_GREGORIAN_CYCLE);
        }
        /* Years */
        leap = isLeapYear (year);
        while (m >=  length_of_year[leap]) {
            m -=  length_of_year[leap];
            year++;
            leap = isLeapYear(year);
        }
        /* Months */
        v_tm_mon = 0;
        while (m >=  days_in_month[leap][v_tm_mon]) {
            m -=  days_in_month[leap][v_tm_mon];
            v_tm_mon++;
        }
    } else {
        year--;
        /* Gregorian cycles */
        cycles = (int)((m /  DAYS_IN_GREGORIAN_CYCLE) + 1);
        if( cycles!=0 ) {
            m -= (cycles *  DAYS_IN_GREGORIAN_CYCLE);
            year += (cycles * YEARS_IN_GREGORIAN_CYCLE);
        }
        /* Years */
        leap = isLeapYear (year);
        while (m <  - length_of_year[leap]) {
            m +=  length_of_year[leap];
            year--;
            leap = isLeapYear (year);
        }
        /* Months */
        v_tm_mon = 11;
        while (m <  -days_in_month[leap][v_tm_mon]) {
            m +=  days_in_month[leap][v_tm_mon];
            v_tm_mon--;
        }
        m +=  days_in_month[leap][v_tm_mon];
    }
    /* At this point m is less than a year so casting to an int is safe */
    month = v_tm_mon+1;
    day = (int) m + 1;
    return new DateTime.Date(year, month, day);
  }
  
  
  
  
}
