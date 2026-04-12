package com.optimasc.date;

import java.util.Calendar;
import java.util.Comparator;


/**
 * A comparator that compares the fields associated with the time in calendar
 * objects up to the specified accuracy. It supports both ignoring (when
 * <code>localTime</code> is set) timezone or normalizing to UTC before
 * comparing.
 * 
 * @author Carl Eric Codere.
 */
public class TimeComparator implements Comparator
{
  protected int accuracy;
  protected boolean localTime;

  /**
   * @param accuracy
   *          [in] The accuracy to which the compare fields against.
   * @param localTime
   *          [in] If comparison will ignore timezones or not.
   */
  public TimeComparator(int accuracy, boolean localTime)
  {
    this.accuracy = accuracy;
    this.localTime = localTime;
  }

  protected int timeCompare(Calendar left, Calendar right)
  {
    // Normalize both times to UTC before comparing, only if
    // these are not local times.
    if (localTime == false)
    {
      left = (Calendar) DateTime.normalize(left);
      right = (Calendar) DateTime.normalize(right);
    }

    // Handle endOfDay sentinel for GregorianDatetimeCalendar instances,
    // since internally 24:00:00 is stored as 00:00:00 in Calendar fields.
/*    int leftHour = (left instanceof GregorianDatetimeCalendar)
        && ((GregorianDatetimeCalendar) left).endOfDay ? 24
        : left.get(Calendar.HOUR_OF_DAY);
    int rightHour = (right instanceof GregorianDatetimeCalendar)
        && ((GregorianDatetimeCalendar) right).endOfDay ? 24
        : right.get(Calendar.HOUR_OF_DAY);*/
      int leftHour =  left.get(Calendar.HOUR_OF_DAY);
      int rightHour = right.get(Calendar.HOUR_OF_DAY);

    if (leftHour < rightHour) return -1;
    if (leftHour > rightHour) return 1;

    // Hours are equal, check minutes
    int leftMinute = left.get(Calendar.MINUTE);
    int rightMinute = right.get(Calendar.MINUTE);

    if (accuracy == DateTime.TimeAccuracy.MINUTE)
    {
      if (leftMinute < rightMinute) return -1;
      if (leftMinute > rightMinute) return 1;
      return 0;
    }

    if (leftMinute < rightMinute) return -1;
    if (leftMinute > rightMinute) return 1;

    // Minutes are equal, check seconds
    int leftSecond = left.get(Calendar.SECOND);
    int rightSecond = right.get(Calendar.SECOND);

    if (accuracy == DateTime.TimeAccuracy.SECOND)
    {
      if (leftSecond < rightSecond) return -1;
      if (leftSecond > rightSecond) return 1;
      return 0;
    }

    if (leftSecond < rightSecond) return -1;
    if (leftSecond > rightSecond) return 1;

    // Seconds are equal, check milliseconds
    int leftMillis = left.get(Calendar.MILLISECOND);
    int rightMillis = right.get(Calendar.MILLISECOND);

    if (leftMillis < rightMillis) return -1;
    if (leftMillis > rightMillis) return 1;
    return 0;
  }

  public int compare(Object o1, Object o2)
  {
    return timeCompare((Calendar) o1, (Calendar) o2);
  }
}
