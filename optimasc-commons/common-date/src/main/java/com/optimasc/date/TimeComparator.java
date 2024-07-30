package com.optimasc.date;

import java.util.Calendar;
import java.util.Comparator;

/** A comparator that compares the fields associated
 *  with the time in calendar objects up to the specified
 *  accuracy. It supports both ignoring (when <code>localTime</code>
 *  is set) timezone or normalizing to UTC before comparing.
 * 
 * @author Carl Eric Codere.
 *
 */
public class TimeComparator implements Comparator
{
  protected int accuracy;
  protected boolean localTime;

  
  /**
   * 
   * @param accuracy [in] The accuracy to which the compare fields
   *   against.
   * @param localTime [in] If comparison will ignore timzeones
   *  or not.
   */
  public TimeComparator(int accuracy, boolean localTime)
  {
    this.accuracy = accuracy;
    this.localTime = localTime;
  }

  protected int timeCompare(Calendar left, Calendar right)
  {
    // Make both times to UTC timezones before comparing the time,
    // only if these are not local times.
    if (localTime == false)
    {
      left = (Calendar) DateTime.normalize(left);
      right = (Calendar) DateTime.normalize(right);
    }
    
    
    int hourResult = right.get(Calendar.HOUR_OF_DAY) - left.get(Calendar.HOUR_OF_DAY);
    
    // Independent of the precision, the hour is already different.
    // So we can return immediately.
    if (hourResult != 0)
    {
      return hourResult;
    }
    
    int minuteResult = right.get(Calendar.MINUTE) - left.get(Calendar.MINUTE);
    
    if (accuracy==DateTime.TimeAccuracy.MINUTE)
    {
      return minuteResult;
    }
    
    if (minuteResult != 0)
    {
      return minuteResult;
    }

    int secondsResult = right.get(Calendar.SECOND) - left.get(Calendar.SECOND);
    
    if (accuracy==DateTime.TimeAccuracy.SECOND)
    {
      return secondsResult;
    }
    
    if (secondsResult != 0)
    {
      return secondsResult;
    }
    
    int millisResult = right.get(Calendar.MILLISECOND) - left.get(Calendar.MILLISECOND);
    
    if (accuracy==DateTime.TimeAccuracy.MILLISECOND)
    {
      return millisResult;
    }
    
    return millisResult;
  }

  public int compare(Object o1, Object o2)
  {
    
    return timeCompare((Calendar)o1,(Calendar)o2);
  }

}
