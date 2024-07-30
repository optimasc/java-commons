package com.optimasc.datatypes.primitives;

import java.util.Calendar;

import com.optimasc.lang.GregorianDatetimeCalendar;

/** Date-time constants for testing. */
public class DateTimeConstants
{
  
  /************************** Local timestamp second accuracy **********************/
  /// -4713-11-24T00:00:00 Epoch 0 of Julian day
  public static final Calendar timeStampJulianDayEpoch = new GregorianDatetimeCalendar(
      -4713,12-1,24,12,00,00);
  /// 0000-01-01T00:00:00
  public static final Calendar timeStampRataDieEpoch = new GregorianDatetimeCalendar(
       0001,01-1,01,00,00,00);
  /// 1970-01-01T00:00:00 Epoch 0 of POSIX time
  public static final Calendar timeStampUNIXEpoch = new GregorianDatetimeCalendar(
      1970,01-1,01,00,00,00);
  
  // Sputnik-1 Launch
  public static final Calendar timeStampSputnik = new GregorianDatetimeCalendar(
      1957,10-1,04,19,28,34);
  // End of World war II
  public static final Calendar timeStampEndWWII = new GregorianDatetimeCalendar(
      1945,5-1,8,23,01,00);
  // First man on the moon
  public static final Calendar timeStampMoon = new GregorianDatetimeCalendar(
      1969,7-1,21,02,56,15);

  /************************** UTC   timestamp second accuracy **********************/

  // Sputnik-1 Launch UTC
  public static final Calendar timeStampSputnikUTC = new GregorianDatetimeCalendar(
      1957,10-1,04,19,28,34,00,00);
  // First man on the moon UTC
  public static final Calendar timeStampMoonUTC = new GregorianDatetimeCalendar(
      1969,7-1,21,02,56,15,00,00);
  
  /***************************  date (day accuracy) *************************/
  
  /// -4713-11-24T00:00:00 Epoch 0 of Julian day
  public static final Calendar dateJulianDayEpoch = new GregorianDatetimeCalendar(
      -4713,12-1,24);
  
  public static final Calendar dateJulianDayEpochUTC = new GregorianDatetimeCalendar(
      -4713,12-1,24,GregorianDatetimeCalendar.ZULU);
  
  /// 0000-01-01T00:00:00
  public static final Calendar dateRataDieEpoch = new GregorianDatetimeCalendar(
       0001,01-1,01);
  /// 1970-01-01T00:00:00 Epoch 0 of POSIX time
  public static final Calendar dateUNIXEpoch = new GregorianDatetimeCalendar(
      1970,01-1,01);
  
  // Sputnik-1 Launch
  public static final Calendar dateSputnik = new GregorianDatetimeCalendar(
      1957,10-1,04);
  // End of World war II
  public static final Calendar dateEndWWII = new GregorianDatetimeCalendar(
      1945,5-1,8);
  // First man on the moon
  public static final Calendar dateStampMoon = new GregorianDatetimeCalendar(
      1969,7-1,21);
  
  /************************** year date (year accuracy) **********************/
  
  // Sputnik-1 Launch
  public static final Calendar yearSputnik = new GregorianDatetimeCalendar(
      1957,GregorianDatetimeCalendar.FIELD_UNDEFINED,GregorianDatetimeCalendar.FIELD_UNDEFINED);
  
  public static final Calendar yearSputnikUTC = new GregorianDatetimeCalendar(
      1957,GregorianDatetimeCalendar.FIELD_UNDEFINED,GregorianDatetimeCalendar.FIELD_UNDEFINED,GregorianDatetimeCalendar.ZULU);
  
  // End of World war II
  public static final Calendar yearEndWWII = new GregorianDatetimeCalendar(
      1945,GregorianDatetimeCalendar.FIELD_UNDEFINED,GregorianDatetimeCalendar.FIELD_UNDEFINED);
  // First man on the moon
  public static final Calendar yearMoon = new GregorianDatetimeCalendar(
      1969,GregorianDatetimeCalendar.FIELD_UNDEFINED,GregorianDatetimeCalendar.FIELD_UNDEFINED);
  
  /************************** Local timestamp minute accuracy **********************/
  
  // Sputnik-1 Launch Truncated to minutes
  public static final Calendar timestampMinuteSputnik = new GregorianDatetimeCalendar(
      1957,10-1,04,19,28,GregorianDatetimeCalendar.FIELD_UNDEFINED);
  // First man on the moon Truncated to minutes
  public static final Calendar timestampMinuteMoon = new GregorianDatetimeCalendar(
      1969,7-1,21,02,56,GregorianDatetimeCalendar.FIELD_UNDEFINED);

  
  /************************** UTC time accuracy **********************/
  
  // Sputnik-1 Launch UTC Time -- No date
  public static final Calendar timeSecondsSputnikUTC = new GregorianDatetimeCalendar(
      19,28,34,GregorianDatetimeCalendar.FIELD_UNDEFINED,00); 
  // First man on the moon UTC Time -- No date
  public static final Calendar timeSecondsMoonUTC = new GregorianDatetimeCalendar(
      02,56,15,GregorianDatetimeCalendar.FIELD_UNDEFINED,00);
  
}
