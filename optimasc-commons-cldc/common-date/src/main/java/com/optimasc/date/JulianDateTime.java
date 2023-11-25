package com.optimasc.date;

import java.util.Calendar;

public class JulianDateTime
{
  /** Represents the number of bits to encode the value 
   *  in this format. The value is returned in the LSB
   *  of the <code>long</code> primitive type.
   */
  public static final int SIZE = 64;
  
  /** Return the precision supported by this encoded value. Possible
   *  values from lowest precision to highest precision are
   *  as follows:
   *
   *  <dl>
   *   <dt>{@link java.util.Calendar#YEAR}</dt>
   *   <dd>The long value represents years (YYYYY) </dd>
   *   <dt>{@link java.util.Calendar#MONTH}</dt>
   *   <dd>The long value can represent a year and a month (YYYY-MM) </dd>
   *   <dt>{@link java.util.Calendar#DAY_OF_MONTH}</dt>
   *   <dd>The long value can represent a full date (YYYY-MM-DD)</dd>
   *   <dt>{@link java.util.Calendar#SECOND}</dt>
   *   <dd>The long value can represent a full date and time up to a precision
   *    of seconds (YYYY-MM-DDThh:mm:ss).</dd>
   *   <dt>{@link java.util.Calendar#MILLISECOND}</dt>
   *   <dd>The long value can represent a full date and time up to a precision
   *    of seconds (YYYY-MM-DDThh:mm:Sss.sss).</dd>
   *  </dl>
   *
   */
  public static final int PRECISION = Calendar.SECOND;
  
  /** Convert a Gregorian calendar date to a Julian Day (JD) */
  public static double encode(final Calendar cal)
  {
    double a;
    double rjd;
    double J1;
    double h;
    double s;

    int year = cal.get(Calendar.YEAR);
    // Do not forget to add 1 month, as calendar is ZERO based (what the heck...)
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    int hour = cal.get(Calendar.HOUR_OF_DAY);
    int second = cal.get(Calendar.SECOND);
    int minute = cal.get(Calendar.MINUTE);
    int millisecond = cal.get(Calendar.MILLISECOND);

    h = hour + (minute / 60.0) + (second / 3600.0)
        + (millisecond / (3600.0 * 1000));
    rjd = -1 * Math.floor(7 * (Math.floor((month + 9.0) / 12.0) + year) / 4.0);
    s = 1.0;
    if (month - 9.0 < 0)
    {
      s = -1.0;
    }
    a = Math.abs(month - 9.0);
    J1 = Math.floor((year + s * Math.floor(a / 7.0)));
    J1 = -1 * Math.floor((Math.floor(J1 / 100) + 1) * 3.0 / 4.0);
    rjd = rjd + Math.floor(275 * month / 9.0) + day + (1 * J1);
    rjd = rjd + 1721027.0 + 2.0 + 367.0 * year - 0.5;
    rjd = rjd + (h / 24.0);
    return rjd;
  }
  
  protected static int round(float d)
  {
      return (int) (java.lang.Math.floor(d * 10 + 0.5) / 10);
  }
  

  /** Convert a Julian Day (JD) to a Gregorial calendar date */
  public static Calendar decode(double jday)
  {
    int year;
    int month = 0;
    int day;
    int hour;
    int second;
    int minute;
    int millisecond;
    long C;
    long T;
    int Y;

    double Z, F, A, B, D, I, RJ, RH;

    Calendar cal = Calendar.getInstance();

    Z = Math.floor(jday + 0.5);
    F = jday + 0.5 - Z;
    /*
     * if (Z < 2299161) A = Z else
     */
    {
      I = Math.floor((Z - 1867216.25) / 36524.25);
      A = Z + 1 + I - Math.floor(I / 4);
    }
    B = A + 1524;
    C = (long) Math.floor((B - 122.1) / 365.25);
    D = Math.floor(365.25 * C);
    T = (long) Math.floor((B - D) / 30.600);
    RJ = B - D - Math.floor(30.6001 * T) + F;
    day = (int) Math.floor(RJ);
    RH = (RJ - Math.floor(RJ)) * 24;
    hour = (int) Math.floor(RH);
    minute = (int) Math.floor((RH - hour) * 60);
    second = (int) (round((float) (((RH - hour) * 60.0 - minute) * 60.0)));
    millisecond = 0;
    if (T < 14)
    {
      month = (int) (T - 1);
    } else
    {
      if ((T == 14) || (T == 15))
      {
        month = (int) (T - 13);
      }
    }
    if (month > 2)
    {
      Y = (int) (C - 4716);
    } else
    {
      Y = (int) (C - 4715);
    }
    if (Y < 0)
    {
      year = 0;
    } else
    {
      year = Y;
    }
    cal.set(Calendar.YEAR, year);
    // Do not forget to remove 1 month, as calendar is ZERO based (what the heck...)
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.DAY_OF_MONTH, day);
    cal.set(Calendar.HOUR_OF_DAY, hour);
    cal.set(Calendar.MINUTE, minute);
    cal.set(Calendar.SECOND, second);
    return cal;
  }

  protected static final int MTAB[] = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
  protected static final int YEAR_MIN = -4799;
  
  /** Converts a Gregorian calendar value (including Gregorian
   *  proleptic calendar) to a Julian day number, as defined by the
   *  SOFA Astronomy library. Valid ranges are from -4799 January 1. 
   *  This code has been converted to Java from the C version SOFA 
   *  Astronomy library and modified to return the Julian Day and add 
   *  support for seconds.
   * 
   * @param year The year
   * @param month The month number, from 1 to 12
   * @param day The day number 1 to 31
   * @param seconds The number of seconds past midnight, from 0 to 86400
   * @return The Julian day number
   */
  public static double encode(int year, int month, int day, int seconds)
  {
    int ly, my;
    long iypmy;

 /* Validate year and month. */
    if (year < YEAR_MIN)
      throw new IllegalArgumentException("Minimum supported year is "+YEAR_MIN);
    if (month < 1 || month > 12) 
      throw new IllegalArgumentException("Month must be between 1 and 12");

 /* If February in a leap year, 1, otherwise 0. */
    ly = 0;
    if (month == 2)
    {
    if (year % 4 == 0)
    {
        if(year % 100 == 0)
        {
            // if year is divisible by 400, then the year is a leap year
            if ( year%400 == 0){
                ly = 1;
            }else{
                ly = 0;
            }
        }
        else{
            ly = 1;
        }
    }else
    {
        ly = 0;
    }
    }
    

 /* Validate day, taking into account leap years. */
    if ( (day < 1) || (day > (MTAB[month-1] + ly)))
       throw new IllegalArgumentException("Day must be between 1 and "+MTAB[month-1+1]);
    
    if ( (seconds < 0) || (seconds > 86400))
      throw new IllegalArgumentException("Seconds must be between 0 and 86400");

 /* Return result. */
    my = (month - 14) / 12;
    iypmy = (long) (year + my);
    double djm0 = 2400000.5d;
    double djm = (double)((1461L * (iypmy + 4800L)) / 4L
                  + (367L * (long) (month - 2 - 12 * my)) / 12L
                  - (3L * ((iypmy + 4900L) / 100L)) / 4L
                  + (long) day - 2432076L);
    
    /* Take into account the time */
    djm = djm + (double)((double)seconds / 86400d);
    /* Return value. */
    return djm+djm0;
  }

}
