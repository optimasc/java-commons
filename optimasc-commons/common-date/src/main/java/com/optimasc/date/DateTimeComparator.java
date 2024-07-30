package com.optimasc.date;

import java.util.Calendar;
import java.util.Comparator;
import java.util.GregorianCalendar;

/** Comparator that compares two Gregorian
 *  calendars  according to the specified accuracy. 
 * 
 * @author Carl Eric Codere
 *
 */
public class DateTimeComparator extends TimeComparator
{
  
  public DateTimeComparator(int accuracy, boolean localTime)
  {
    super(accuracy, localTime);
  }
  
  
  /** Compares the era/year fields of both calendars.
   * 
   * @param leftCalendar
   * @param rightCalendar
   * @return 0 = if both calendars have the same era and year
   *        -1 if the left calendar year and era is before the right calendar year and era.
   *        1 if the left calendar year and era is after the right calendar year and era.
   */ 
  protected int compareYear(Calendar leftCalendar, Calendar rightCalendar)
  {
    
    int leftYear = leftCalendar.get(Calendar.YEAR);
    int rightYear = rightCalendar.get(Calendar.YEAR);
    
    if ((leftCalendar instanceof GregorianCalendar) && (rightCalendar instanceof GregorianCalendar))
    {
      int leftEra = leftCalendar.get(GregorianCalendar.ERA);
      int rightEra = rightCalendar.get(GregorianCalendar.ERA);
      if (leftEra != rightEra)
      {
        if (leftEra == GregorianCalendar.BC)
        {
          return -1;
        }
        return 1;
      }
      // Negate both years if <= 0
      if (leftEra == GregorianCalendar.BC)
      {
        leftYear = - leftYear;
      }
      if (rightEra == GregorianCalendar.BC)
      {
        rightYear = - rightYear;
      }
    }
      
    
    
    // Era's are equal, then check years
    if (leftYear == rightYear)
    {
      return 0;
    }
    if (leftYear < rightYear)
    {
      return -1;
    }
    return 1;
  }
  
  public int compare(Object o1, Object o2)
  {
    Calendar left = (Calendar) o1;
    Calendar right = (Calendar) o2;
    
    int yearResult = compareYear(left,right);
    
    if (accuracy==DateTime.TimeAccuracy.YEAR)
    {
      return yearResult;
    }
    // Independent of the precision, the ear/year is already different.
    // So we can return immediately.
    if (yearResult != 0)
    {
      return yearResult;
    }

    // Compare month
    int monthResult;

    int leftMonth = left.get(Calendar.MONTH);
    int rightMonth = right.get(Calendar.MONTH);
    monthResult = rightMonth - leftMonth;
    
    // Independent of the precision, the month is already different.
    // So we can return immediately.
    if (monthResult != 0)
    {
      return monthResult;
    }
    
    int dayResult = right.get(Calendar.DAY_OF_MONTH) - left.get(Calendar.DAY_OF_MONTH);

    if (accuracy==DateTime.TimeAccuracy.DAY)
    {
      return dayResult;
    }
    
    // Independent of the precision, the day is already different.
    // So we can return immediately.
    if (dayResult != 0)
    {
      return dayResult;
    }
    
    return super.timeCompare(left, right);
  }
}
