package com.optimasc.date;

import java.util.Calendar;

import com.optimasc.date.*;


/** Class to convert between date time objects and a 
 *  NTFS timestamp format. The input and outputs of
 *  this class are in network order (big endian), while on the 
 *  NTFS file system, they are in little-endian, 
 *  hence the results must be swapped when being stored or read
 *  from storage.
 *  
 *  <p>Due to limitation of date time formats in 
 *  the java platform, even though NTFS supports
 *  a resolution of 100 ns, the values returned
 *  are at a resolution of 1 ms.</p>
 *  
 * @author Carl Eric Codere
 */
public class NTFSDateTime extends DateEncoder
{
  public static final DateEncoder converter = new NTFSDateTime();
  
  public static final int MIN_YEAR = 1601;
  public static final int MAX_YEAR = 30828;
  

  public static final int SIZE = 64;
  
  public long encode(Calendar cal)
  {
    return encode(new DateTime(cal));
  }

  public int getPrecision()
  {
    return Calendar.MILLISECOND;
  }

  public int getMinBits()
  {
    return SIZE;
  }

  public DateTime decode(long value)
  {
    /* Convert 100 ns to milliseconds */
    value = value / 10000L;
    return DateConverter.toDateTime(value, DateTimeEpochs.MNTFS);    
  }
  
  

  public long encode(DateTime value)
  {
    long returnval;
    if (value.resolution < DateTime.TimeAccuracy.MILLISECOND)
    {
      throw new IllegalArgumentException("DateTime Resolution must be at least milliseconds");
    }
    if ((value.date.year < MIN_YEAR) || (value.date.year > MAX_YEAR))
    {
      throw new IllegalArgumentException("Invalid year value, should be "+MIN_YEAR+".."+MAX_YEAR);
    }
    /* check for invalid dates */
    if ((value.date.month < DateTime.MIN_MONTH) || (value.date.month > DateTime.MAX_MONTH))
    {
      throw new IllegalArgumentException("Invalid month value, should be "+DateTime.MIN_MONTH+".."+DateTime.MAX_MONTH);
    }
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
    if (value.time.localTime==true)
    {
      throw new IllegalArgumentException("Trying to encode a local time which is not supported by this encoded format");
    }
    /* Return value is in milliseconds */
    returnval = DateConverter.toDuration(value, DateTimeEpochs.MNTFS);
    /* Convert to 100's of nanoseconds */
    returnval = returnval * (1000000L/100L);
    return returnval;
  }

  public int getMinimumYear()
  {
    return MIN_YEAR;
  }

  public int getMaximumYear()
  {
    return MAX_YEAR;
  }

}
