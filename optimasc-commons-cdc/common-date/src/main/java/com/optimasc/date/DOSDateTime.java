package com.optimasc.date;

import java.util.Calendar;

import com.optimasc.date.*;


/** Class to convert between date time objects and a 
 *  MS-DOS timestamp format.
 *  
 *  <p>Due to limitations in the format, leap seconds
 *   (60), are converted to 59, and the hour of 24
 *   is converted to 00, which is consistent with ISO 8601:2019.</p>
 *
 * @author Carl Eric Codere
 */
public class DOSDateTime extends DateEncoder
{
  public static final DateEncoder converter = new DOSDateTime();

  public static final int SIZE = 32;
  
  public long encode(Calendar cal)
  {
    final int year = cal.get(Calendar.YEAR);
    if ((year < DOSDate.MIN_YEAR) || (year > DOSDate.MAX_YEAR))
    {
      throw new IllegalArgumentException("Invalid year value, should be "+DOSDate.MIN_YEAR+".."+DOSDate.MAX_YEAR);
    }
    final long dTime = ((year - 1980) << 25)
            | ((cal.get(Calendar.MONTH) + 1) << 21)
            | (cal.get(Calendar.DAY_OF_MONTH) << 16)
            | (cal.get(Calendar.HOUR_OF_DAY) << 11)
            | (cal.get(Calendar.MINUTE) << 5)
            | (cal.get(Calendar.SECOND) >> 1);
    return dTime;
  }

  public int getPrecision()
  {
    return Calendar.SECOND;
  }

  public int getMinBits()
  {
    return SIZE;
  }

  public DateTime decode(long value)
  {
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    
   year = (int)(1980 + ((value >> 25) & 0x7f));
   month = (int)((value >> 21) & 0x0f);
   day = (int)(value >> 16) & 0x1f;
   hour = (int)(value >> 11) & 0x1f;
   minute = (int)(value >> 5) & 0x3f;
   second = (int)(value << 1) & 0x3e;
   DateTime.Time time = new DateTime.Time(hour,minute,second,true);
   DateTime.Date date = new DateTime.Date(year, month, day);
   return new DateTime(date,time,DateTime.TimeAccuracy.SECOND);
  }
  
  

  public long encode(DateTime value)
  {
    int hour;
    int second;
    if (value.resolution < DateTime.TimeAccuracy.SECOND)
    {
      throw new IllegalArgumentException("DateTime Resolution must be at least seconds");
    }
    if ((value.date.year < DOSDate.MIN_YEAR) || (value.date.year > DOSDate.MAX_YEAR))
    {
      throw new IllegalArgumentException("Invalid year value, should be "+DOSDate.MIN_YEAR+".."+DOSDate.MAX_YEAR);
    }
    /* check for invalid dates */
    if ((value.date.day < DateTime.MIN_DAY) || (value.date.day > DateTime.MAX_DAY))
    {
      throw new IllegalArgumentException("Invalid day value, should be "+DateTime.MIN_DAY+".."+DateTime.MAX_DAY);
    }
    if ((value.time.hour < DateTime.MIN_HOUR) || (value.time.hour > DateTime.MAX_HOUR))
    {
      throw new IllegalArgumentException("Invalid hour value, should be "+DateTime.MIN_HOUR+".."+DateTime.MAX_HOUR);
    }
    if ((value.time.minute < DateTime.MIN_MINUTE) || (value.time.minute > DateTime.MAX_MINUTE))
    {
      throw new IllegalArgumentException("Invalid minute value, should be "+DateTime.MIN_MINUTE+".."+DateTime.MAX_MINUTE);
    }
    if ((value.time.second < DateTime.MIN_SECOND) || (value.time.second > DateTime.MAX_SECOND))
    {
      throw new IllegalArgumentException("Invalid second value, should be "+DateTime.MIN_SECOND+".."+DateTime.MAX_SECOND);
    }
    if (value.time.localTime==false)
    {
      throw new IllegalArgumentException("Trying to encode a non local time which is not supported by this encoded format");
    }
    hour = value.time.hour;
    second = value.time.second;
    if (hour == 24)
    {
      hour = 0;
    }
    if (second == 60)
    {
      second=  59;
    }
    int year = value.date.year - 1980;
    if (year < 0)
        return 0;
    long dTime = (year << 25)
            | (value.date.month << 21)
            | (value.date.day << 16);
    dTime = dTime 
        | (value.time.hour << 11) 
        | (value.time.minute << 5)
        | (value.time.second >> 1);
    return dTime;
  }

  public int getMinimumYear()
  {
    return DOSDate.MIN_YEAR;
  }

  public int getMaximumYear()
  {
    return DOSDate.MAX_YEAR;
  }

}
