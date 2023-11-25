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
  
  protected static final int PRECISION = Calendar.DAY_OF_MONTH;
  
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
  public Calendar decode(long data)
  {
    // Convert to a Julian Day
    data = data + PARADOX_OFFSET;
    return JulianDay.converter.decode(data);
  }
  
  
  public long encode(Calendar cal)
  {
    return JulianDay.converter.encode(cal)-PARADOX_OFFSET;
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
  
  public int getPrecision()
  {
    return PRECISION;
  }


  public int getMinBits()
  {
    return JulianDay.SIZE;
  }
  
}
