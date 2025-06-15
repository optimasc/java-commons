package com.optimasc.date;

/** Predefined epochs as well as time units used to measure the time 
 *  elapsed since that epoch.
 *  
 * @author Carl Eric Codere
 *
 */
public class DateTimeEpochs
{
   /** Standard Julian Day Number (JDN), without the fractional part. */
   public static final DateTimeFormat JULIAN_DAY = new DateTimeFormat(DateTimeFormat.TimeUnit.DAYS,-1721060L);
   /** Standard time elapsed since Epoch which is equal to JDN 0 in milliseconds. */
   public static final DateTimeFormat JULIAN_MILLISECONDS = new DateTimeFormat(DateTimeFormat.TimeUnit.MILLISECONDS,-1721060L*24L*3600L*1000L);
   /** UNIX/POSIX epoch which has a resolution of seconds since 1970-01-01 UTC Gregorian Calendar.
    *  The UNIX epoch is 719528 days after instance 0000-01-01. This has been verified
    *  against the JDK code */
   public static final DateTimeFormat POSIX = new DateTimeFormat(DateTimeFormat.TimeUnit.SECONDS,+719528L*24L*60L*60L);
   /** Javascript epoch which has a resolution of milliseconds since 1970-01-01 UTC Gregorian Calendar. */
   public static final DateTimeFormat JAVASCRIPT = new DateTimeFormat(DateTimeFormat.TimeUnit.MILLISECONDS,+719528L*24L*60L*60L*1000L);
   /** Network time protocol (IETF RFC 5905) epoch which has a resolution of seconds since 1900-01-01 UTC Gregorian Calendar.
    *  The values have been verified against the tables in IETF RFC 5905 */
   public static final DateTimeFormat NTP = new DateTimeFormat(DateTimeFormat.TimeUnit.SECONDS,+693961L*24L*60L*60L); 
   /** BMFF/MP4/LabView/Classic MacOS epoch which has a resolution of seconds since 1904-01-01 UTC Gregorian Calendar 
    *  This value has been verified against the OxCal calculator
    */
   public static final DateTimeFormat MBFF = new DateTimeFormat(DateTimeFormat.TimeUnit.SECONDS,-695421L*24L*60L*60L);
   /** Lilian day number since 1582-10-14 Gregorian Calendar, where day 1 is 1582-10-15.
    *  This has been verified against the Epoch (computing) Wikipedia page on 2024-06-25.
    *  */
   public static final DateTimeFormat LILIAN = new DateTimeFormat(DateTimeFormat.TimeUnit.DAYS,+578100L);
   /** Rata Die day number since 0000-12-31 Gregorian Calendar, where day 1 is 0001-01-01.
    *  This epoch is used by Paradox tables for encoding dates, Microsoft .NET,Go and REXX among
    *  others. 
    *  This has been verified against the Epoch (computing) Wikipedia page on 2024-06-25.
    * */
   public static final DateTimeFormat RATA_DIE_DAY = new DateTimeFormat(DateTimeFormat.TimeUnit.DAYS,+365);
   /** NTFS modified timestamp epoch which has a resolution of milliseconds since 1601-01-01 UTC Gregorian Calendar.
    * From a NTFS timestamp which has a resolution of of 100 ns, you must divide the value by 10000 
    * (value * 100 ns/intervals = ns -> ns/1000000 -> ms)
    * get the number of milliseconds.
    */  
   public static final DateTimeFormat MNTFS = new DateTimeFormat(DateTimeFormat.TimeUnit.MILLISECONDS,+584754L*24L*60L*60L*1000L); 
   
}
