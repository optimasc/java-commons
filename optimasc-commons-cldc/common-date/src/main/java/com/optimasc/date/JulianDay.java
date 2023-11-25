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
  
  
  /** Represents the number of bits to encode the value 
   *  in this format. The value is returned in the LSB
   *  of the <code>long</code> primitive type.
   */
  public static final int SIZE = 32;
  
  protected static final int PRECISION = Calendar.DAY_OF_MONTH;
  
  
  protected static final int JULIAN_OFFSET = 32045;
  protected static final int DAYS_PER_5_MONTHS = 153;
  protected static final int DAYS_PER_4_YEARS = 1461;
  protected static final int DAYS_PER_400_YEARS = 146097;

  
  public long encode(Calendar cal)
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
  public Calendar decode(long sdn)
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

  public int getPrecision()
  {
    return PRECISION;
  }


  public int getMinBits()
  {
    return SIZE;
  }
  
  
  
}
