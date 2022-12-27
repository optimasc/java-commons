package com.optimasc.date;

import java.util.Calendar;

/** Class that used to convert date and time in the paradox format
 *  to and from Calendar objects. The Paradox date format is simply
 *  an offset from the Julian Day value.
 *  
 * @author Carl Eric Codere
 *
 */
public final class ParadoxDate extends DateConverter
{
  public static final DateConverter converter = new ParadoxDate();
  
  private static final int PARADOX_OFFSET = 1721425;
  
  /** Converts a 4-byte representation of a Paradox date field to
   *  a calendar representation. Only the following values of
   *  calendar can be considered valid :
   *    Calendar.YEAR
   *    Calendar.MONTH
   *    Calendar.DAY_OF_MONTH;
   *    
   *  It is to note that a Paradox date is simply a Julian Day
   *  where the value zero stored is offset by PARADOX_OFFSET  
   *    
   * @param date The Paradox date value (4-byte value)
   * @return null if error otherwise a calendar instance.
   */
  public Calendar toCalendar(long data)
  {
    // Convert to a Julian Day
    data = data + PARADOX_OFFSET;
    return JulianDay.JulianDayToCalendar(data);
  }
  
  
  public long toLong(Calendar cal)
  {
    return JulianDay.converter.toLong(cal)-PARADOX_OFFSET;
  }

  /** Converts a 4-byte representation of a Paradox time field
   *  to a calendar representation. Internally a time field
   *  is the number of milleseconds after midnight. Only the 
   *  following values of the calendar shall be considered value
   *  on output:
   *    Calendar.HOUR_OF_DAY
   *    Calendar.MINUTE
   *    Calendar.SECOND
   *    Calendar.MILLISECOND
   * 
   * @param data 
   * @return
   */
  public static Calendar ParadoxTimeToCalendar(int data)
  {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.HOUR_OF_DAY, data / (3600*1000));
    cal.set(Calendar.MINUTE, data % (3600*1000));
    cal.set(Calendar.SECOND,  data % (60*1000));
    cal.set(Calendar.MILLISECOND, data % 1000);
    return cal;
  }
  
  public static int CalendarToParadoxTime(Calendar cal)
  {
    return cal.get(Calendar.HOUR_OF_DAY)*3600000 + cal.get(Calendar.MINUTE)*60000 + cal.get(Calendar.SECOND)*1000+cal.get(Calendar.MILLISECOND);
  }
  
  public static double CalendarToTimeStamp(Calendar cal)
  {
    double value = converter.toLong(cal);
    value = (value * 86400.0D + cal.get(11) * 3600 + cal.get(12) * 60 + cal.get(13)) * 1000.0D + cal.get(14);
    return value;
  }

  public static Calendar ParadoxTimestampToCalendar(double data)
  {
    double secvalues = data / 1000.0D;
    int days = (int)(secvalues / 86400.0D);
    int secs = (int)(secvalues % 86400.0D);
    Calendar cal = converter.toCalendar(days);
    cal.set(11, secs / 3600);
    cal.set(12, secs / 60 % 60);
    cal.set(13, secs % 60);
    cal.set(14, (int)(data % 1000.0D));
    return cal;
  }  

}
