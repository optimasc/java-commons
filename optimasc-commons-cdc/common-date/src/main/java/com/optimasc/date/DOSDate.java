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
import com.optimasc.date.DateTime.*;

/** Class to convert between date time objects and a 
 *  MS-DOS Date value.
 *  
 * @author Carl Eric Codere
 */
public class DOSDate extends DateEncoder
{
  public static final DateEncoder converter = new DOSDate();
  
  
    /** Represents the number of bits to encode the value 
     *  in this format. The value is returned in the LSB
     *  of the <code>long</code> primitive type.
     */
    public static final int SIZE = 16;
    
    protected static final int PRECISION = Calendar.DAY_OF_MONTH;
    
    public static final int MIN_YEAR = 1980;
    public static final int MAX_YEAR = 2107;
    
    /**
     * Smallest supported DOS date/time value in a ZIP file,
     * which is January 1<sup>st</sup>, 1980 AD 00:00:00 local time.
     */
    protected static final int MIN_DOS_TIME = (1 << 21) | (1 << 16); // 0x210000;


    
    /** Converts an MS-DOS encoded Date and Timeto  a Gregorian Calendar representation. Since the
     *  MS-DOS Datetime does not contain any timezone information, the timezone indicator 
     *  should not be interpreted. 
     * 
     * @return A Calendar giving the information.
     */
    public static Calendar DOSDateAndTimeToCalendar(int date, int time)
    {
     Calendar cal = Calendar.getInstance();
//     cal.setTimeZone(TimeZone.getTimeZone("GMT"));
     cal.set(Calendar.YEAR, (int)(1980 + ((date >> 9) & 0x7f)));
     cal.set(Calendar.MONTH, (int)((date >> 5) & 0x0f) - 1);
     cal.set(Calendar.DAY_OF_MONTH, (int)(date) & 0x1f);
     cal.set(Calendar.HOUR_OF_DAY, (int)(time >> 11) & 0x1f);
     cal.set(Calendar.MINUTE, (int)(time >> 5) & 0x3f);
     cal.set(Calendar.SECOND, (int)(time << 1) & 0x3e);
     return cal;
    }
    
    
    public DateTime decode(long date)
    {
     int year =  (int)(1980 + ((date >> 9) & 0x7f));
     int month = (int)((date >> 5) & 0x0f);
     int day = (int)(date) & 0x1f;
     return new DateTime(year,month,day);
    }
    

    /** Converts a Calendar Datetime to an MS-DOS encoded date */
    public long encode(final Calendar cal)
    {
        final int year = cal.get(Calendar.YEAR) - 1980;
        if (year < 0)
            return 0;
        if (year > 0x7f)
            throw new IllegalArgumentException(
                    "Year of Java time is later than 2107 AD: " + (1980 + year));
        final int dTime = (year << 9)
                | ((cal.get(Calendar.MONTH) + 1) << 5)
                | (cal.get(Calendar.DAY_OF_MONTH));
        return dTime & 0xFFFF;
    }
    

    public int getPrecision()
    {
      return PRECISION;
    }

    public int getMinBits()
    {
      return SIZE;
    }
    
    protected static long encode(int year, int month, int day)
    {
      /* check for invalid dates */
      if ((month < DateTime.MIN_MONTH) || (month > DateTime.MAX_MONTH))
      {
        throw new IllegalArgumentException("Invalid month value, should be "+DateTime.MIN_MONTH+".."+DateTime.MAX_MONTH);
      }
      if ((day < DateTime.MIN_DAY) || (day > DateTime.MAX_DAY))
      {
        throw new IllegalArgumentException("Invalid day value, should be "+DateTime.MIN_DAY+".."+DateTime.MAX_DAY);
      }
      if ((year < MIN_YEAR) || (year > MAX_YEAR))
      {
        throw new IllegalArgumentException("Invalid year value, should be "+MIN_YEAR+".."+MAX_YEAR);
      }
      year = year - 1980;
      if (year < 0)
          return 0;
      final int dTime = (year << 9)
              | (month << 5)
              | (day);
      return dTime & 0xFFFF;
    }    


    public long encode(DateTime value)
    {
      Date date = value.date;
      return encode(date.year,date.month,date.day);
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
