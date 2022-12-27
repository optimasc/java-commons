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

/** Utility class used to convert between a Calendar object and a 
 *  MS-DOS Date and time value.
 *  
 * @author Carl Eric Codere
 */
public final class DOSDate extends DateConverter
{
    public static final DateConverter converter = new DOSDate();
  
    /**
     * Smallest supported DOS date/time value in a ZIP file,
     * which is January 1<sup>st</sup>, 1980 AD 00:00:00 local time.
     */
    static final long MIN_DOS_TIME = (1 << 21) | (1 << 16); // 0x210000;


    /** Converts an MS-DOS encoded Date and Time to 
     *  a Gregorian Calendar representation. Since the
     *  MS-DOS Datetime does not contain any timezone information,
     *  the TZ shall be set to GMT by default. 
     * 
     * @return
     */
    public Calendar toCalendar(long datetime)
    {
        if (datetime <= 0)
           datetime =  MIN_DOS_TIME;
        Calendar cal = Calendar.getInstance();
//        cal.setTimeZone(TimeZone.getTimeZone("GMT"));
        cal.set(Calendar.YEAR, (int)(1980 + ((datetime >> 25) & 0x7f)));
        cal.set(Calendar.MONTH, (int)((datetime >> 21) & 0x0f) - 1);
        cal.set(Calendar.DAY_OF_MONTH, (int)(datetime >> 16) & 0x1f);
        cal.set(Calendar.HOUR_OF_DAY, (int)(datetime >> 11) & 0x1f);
        cal.set(Calendar.MINUTE, (int)(datetime >> 5) & 0x3f);
        cal.set(Calendar.SECOND, (int)(datetime << 1) & 0x3e);
        return cal;
    }
    
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

    /** Converts a Calendar Datetime to an MS-DOS encoded Datetime */
    public long toLong(final Calendar cal)
    {
        final int year = cal.get(Calendar.YEAR) - 1980;
        if (year < 0)
            return MIN_DOS_TIME;
        if (year > 0x7f)
            throw new IllegalArgumentException(
                    "Year of Java time is later than 2107 AD: " + (1980 + year));
        final long dTime = (year << 25)
                | ((cal.get(Calendar.MONTH) + 1) << 21)
                | (cal.get(Calendar.DAY_OF_MONTH) << 16)
                | (cal.get(Calendar.HOUR_OF_DAY) << 11)
                | (cal.get(Calendar.MINUTE) << 5)
                | (cal.get(Calendar.SECOND) >> 1);
        return dTime;
    }

    /** Converts a Calendar Datetime to an MS-DOS encoded date */
    public static int CalendarToDOSDate(final Calendar cal)
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
    
    /** Converts a Calendar Datetime to an MS-DOS encoded time */
    public static int CalendarToDOSTime(final Calendar cal)
    {
        final int dTime = 
                (cal.get(Calendar.HOUR_OF_DAY) << 11)
                | (cal.get(Calendar.MINUTE) << 5)
                | (cal.get(Calendar.SECOND) >> 1);
        return dTime & 0xFFF;
    }
    

}
