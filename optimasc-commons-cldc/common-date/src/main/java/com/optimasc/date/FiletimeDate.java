/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.date;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


/** Utility class used to convert between a Calendar object and a
 *  Microsoft 64-bit FILETIME timestamp. some of the information on the
 *  conversion taken from Niraj Tolia from StackOverFlow.
 *
 * @author Carl Eric Codere
 */
public final class FiletimeDate extends DateConverter
{
  public static final DateConverter converter = new FiletimeDate();
  
  
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
  public static final int PRECISION = Calendar.MILLISECOND;
  
  
    /** Difference between Filetime epoch and Unix epoch (in ms). */
    private static final long FILETIME_EPOCH_DIFF = 11644473600000L;

    /** One millisecond expressed in units of 100s of nanoseconds. */
    private static final long FILETIME_ONE_MILLISECOND = 10 * 1000;

    /** Converts a FILETIME encoded Date and Time ime to
     *  a Gregorian Calendar representation.
     *
     * @return
     */
    
    public Calendar decode(long filetime)
    {
       Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
       Date d = new Date();
       d.setTime((filetime / FILETIME_ONE_MILLISECOND) - FILETIME_EPOCH_DIFF);
       cal.setTime(d);
       return cal;
    }

    /** Converts a Calendar encoded Date and Time to
     *  a Gregorian Calendar representation.
     *
     * @return
     */
    public long encode(Calendar cal)
    {
       Date d = cal.getTime();
       return (d.getTime() + FILETIME_EPOCH_DIFF) * FILETIME_ONE_MILLISECOND;
    }

    public int getPrecision()
    {
      return PRECISION;
    }

    @Override
    public int getMinBits()
    {
      return SIZE;
    }

}
