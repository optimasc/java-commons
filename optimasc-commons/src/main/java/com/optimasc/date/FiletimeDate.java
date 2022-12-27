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

    /** Difference between Filetime epoch and Unix epoch (in ms). */
    private static final long FILETIME_EPOCH_DIFF = 11644473600000L;

    /** One millisecond expressed in units of 100s of nanoseconds. */
    private static final long FILETIME_ONE_MILLISECOND = 10 * 1000;

    /** Converts a FILETIME encoded Date and Time ime to
     *  a Gregorian Calendar representation.
     *
     * @return
     */
    
    public Calendar toCalendar(long filetime)
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
    public long toLong(Calendar cal)
    {
       Date d = cal.getTime();
       return (d.getTime() + FILETIME_EPOCH_DIFF) * FILETIME_ONE_MILLISECOND;
    }


}
