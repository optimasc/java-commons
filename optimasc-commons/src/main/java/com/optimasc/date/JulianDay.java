/*
 * 
 * See License.txt for more information on the licensing terms
 * for this source code.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * OPTIMA SC INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.optimasc.date;

import java.util.Calendar;

/**
 * Utility class used to convert between a Calendar object and a Julian day, as
 * used in astronomy standards.
 * 
 * @author Carl Eric Cod�re
 */
public final class JulianDay extends DateConverter
{
  public static final DateConverter converter = new JulianDay();
  
  private static final int JULIAN_OFFSET = 32045;
  private static final int DAYS_PER_5_MONTHS = 153;
  private static final int DAYS_PER_4_YEARS = 1461;
  private static final int DAYS_PER_400_YEARS = 146097;

  public long toLong(Calendar cal)
  {
    int year;
    int month;
    int inputYear = cal.get(Calendar.YEAR);
    // Do not forget to add 1, because the default calendar is ZERO based.
    int inputMonth = cal.get(Calendar.MONTH) + 1;
    int inputDay = cal.get(Calendar.DAY_OF_MONTH);

    /* check for invalid dates */
    if (inputYear == 0 || inputYear < -4714 || inputMonth <= 0
        || inputMonth > 12 || inputDay <= 0 || inputDay > 31)
    {
      return 0;
    }

    /* check for dates before SDN 1 (Nov 25, 4714 B.C.) */
    if (inputYear == -4714)
    {
      if (inputMonth < 11)
      {
        return (0);
      }
      if (inputMonth == 11 && inputDay < 25)
      {
        return (0);
      }
    }
    /* Make year always a positive number. */
    if (inputYear < 0)
    {
      year = inputYear + 4801;
    } else
    {
      year = inputYear + 4800;
    }
    /* Adjust the start of the year. */
    if (inputMonth > 2)
    {
      month = inputMonth - 3;
    } else
    {
      month = inputMonth + 9;
      year--;
    }
    return (((year / 100) * DAYS_PER_400_YEARS) / 4
        + ((year % 100) * DAYS_PER_4_YEARS) / 4
        + (month * DAYS_PER_5_MONTHS + 2) / 5 + inputDay - JULIAN_OFFSET);

  }

  /**
   * Convert a Gregorian calendar date to a Julian Day (JD), with only the
   * values of YEAR, MONTH and DAY_OF_MONTH set
   */
  public Calendar toCalendar(long sdn)
  {
    int century;
    int year;
    int month;
    int day;
    long temp;
    int dayOfYear;
    Calendar cal = Calendar.getInstance();

    if (sdn <= 0)
    {
      return null;
    }
    temp = (sdn + JULIAN_OFFSET) * 4 - 1;

    /* Calculate the century (year/100). */
    century = (int) (temp / DAYS_PER_400_YEARS);

    /* Calculate the year and day of year (1 <= dayOfYear <= 366). */
    temp = ((temp % DAYS_PER_400_YEARS) / 4) * 4 + 3;
    year = (int) ((century * 100) + (temp / DAYS_PER_4_YEARS));
    dayOfYear = (int) ((temp % DAYS_PER_4_YEARS) / 4 + 1);

    /* Calculate the month and day of month. */
    temp = dayOfYear * 5 - 3;
    month = (int) (temp / DAYS_PER_5_MONTHS);
    day = (int) ((temp % DAYS_PER_5_MONTHS) / 5 + 1);

    /* Convert to the normal beginning of the year. */
    if (month < 10)
    {
      month += 3;
    } else
    {
      year += 1;
      month -= 9;
    }

    /* Adjust to the B.C./A.D. type numbering. */
    year -= 4800;
    if (year <= 0)
      year--;

    cal.set(Calendar.YEAR, year);
    // Do not forget to remove 1 month, as calendar is ZERO based (what the heck...)
    cal.set(Calendar.MONTH, month - 1);
    cal.set(Calendar.DAY_OF_MONTH, day);
    return cal;
  }

  /** Convert a Gregorian calendar date to a Julian Day (JD) */
  public static double CalendarToJulianDay(final Calendar cal)
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

  /** Convert a Julian Day (JD) to a Gregorial calendar date */
  public static Calendar JulianDayToCalendar(double jday)
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
    second = (int) (com.optimasc.math.Math
        .round((float) (((RH - hour) * 60.0 - minute) * 60.0)));
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
  public static double toDouble(int year, int month, int day, int seconds)
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
