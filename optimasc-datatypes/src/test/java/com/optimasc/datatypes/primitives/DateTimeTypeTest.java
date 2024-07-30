package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DatatypeTest;
import com.optimasc.datatypes.DateTimeEnumerationFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.date.DateTime;
import com.optimasc.lang.GregorianDatetimeCalendar;


public class DateTimeTypeTest  extends DatatypeTest
{
  protected DateTimeType defaultInstance;
  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = (DateTimeType) DateTimeType.getInstance().getType();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(GregorianDatetimeCalendar.class, defaultInstance.getClassType());
  }
  
  public void testStringAttributes()
  {
    testBasicDataType(defaultInstance);
  }
  
  public void testUserData()
  {
    testUserData(defaultInstance);
  }

  public void testProperties()
  {
    DateTimeType instance = defaultInstance;
    assertEquals(true,instance.isOrdered());
    assertEquals(false,instance.isNumeric());
    assertEquals(false,instance.isBounded());
    assertEquals(null,instance.getChoices());
  }
  
  /*------------------ Accuracy/Timezone functionality ----------------*/
  public void testAccuracyAndLocalTime()
  {
    DateTimeType timeType;
    // Default accuracy
    assertEquals(DateTime.TimeAccuracy.SECOND,defaultInstance.getAccuracy());
    assertEquals(true,defaultInstance.localTime);
    
    // Set other types of accuracy and timezones information -- All valid
    timeType = new DateTimeType(DateTime.TimeAccuracy.MILLISECOND,false);
    assertEquals(DateTime.TimeAccuracy.MILLISECOND,timeType.getAccuracy());
    assertEquals(false,timeType.localTime);
    
    timeType = new DateTimeType(DateTime.TimeAccuracy.MILLISECOND,true);
    assertEquals(DateTime.TimeAccuracy.MILLISECOND,timeType.getAccuracy());
    assertEquals(true,timeType.localTime);

    timeType = new DateTimeType(DateTime.TimeAccuracy.MINUTE,false);
    assertEquals(DateTime.TimeAccuracy.MINUTE,timeType.getAccuracy());
    assertEquals(false,timeType.localTime);
    
    timeType = new DateTimeType(DateTime.TimeAccuracy.MINUTE,true);
    assertEquals(DateTime.TimeAccuracy.MINUTE,timeType.getAccuracy());
    assertEquals(true,timeType.localTime);
    
    timeType = new DateTimeType(DateTime.TimeAccuracy.DAY,false);
    assertEquals(DateTime.TimeAccuracy.DAY,timeType.getAccuracy());
    assertEquals(false,timeType.localTime);
    
    timeType = new DateTimeType(DateTime.TimeAccuracy.YEAR,true);
    assertEquals(DateTime.TimeAccuracy.YEAR,timeType.getAccuracy());
    assertEquals(true,timeType.localTime);
    
    
    // Invalid accuracy values for a Time type
    
    boolean success = false;
    
    try
    {
      timeType = new DateTimeType(-1,false);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      timeType = new DateTimeType(-56,false);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
    success = false;
    try
    {
      timeType = new DateTimeType(Integer.MIN_VALUE,false);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
  }
  
  /*------------------ valueOf functionality ----------------*/
  
  /** Verifies that calendar fields are of the values passed. Also checks
   *  if Calendar instance is in UTC TZ
   */
  public static void checkTimestampUTC(Calendar cal, int year, int month, int day, int hour, int minute, int second, int millis)
  {
    checkDate(cal,year,month,day);
    assertEquals(hour,cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(minute,cal.get(Calendar.MINUTE));
    assertEquals(second,cal.get(Calendar.SECOND));
    assertEquals(millis,cal.get(Calendar.MILLISECOND));
    // Check if we are in UTC or not
    TimeZone tz = cal.getTimeZone();
    assertEquals(0,tz.getRawOffset());
  }
  
  
  /** Verifies that calendar fields are of the values passed. Also checks
   *  if Calendar instance is in UTC TZ. The year is an astronomical year
   *  numbering as specified in ISO 8601:2004.
   */
  public static void checkYear(Calendar cal, int year)
  {
    if (year <= 0)
    {
      assertEquals(GregorianCalendar.BC,cal.get(Calendar.ERA));
      year = year + 1;
      year = -year;
    } else
    {
      assertEquals(GregorianCalendar.AD,cal.get(Calendar.ERA));
    }
    assertEquals(year,cal.get(Calendar.YEAR));
  }
  
  
  public static void checkDate(Calendar cal, int year, int month, int day)
  {
    checkYear(cal,year);
    assertEquals(month,cal.get(Calendar.MONTH));
    assertEquals(day,cal.get(Calendar.DAY_OF_MONTH));
  }
  
  
  
  public static void checkTime(Calendar cal, int hour, int minute, int second, int millis)
  {
    assertEquals(hour,cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(minute,cal.get(Calendar.MINUTE));
    assertEquals(second,cal.get(Calendar.SECOND));
    assertEquals(millis,cal.get(Calendar.MILLISECOND));
  }
  
  
  public void testToValueObjectYear()
  {
    GregorianDatetimeCalendar result;
    DateTimeType datatype = new DateTimeType(DateTime.TimeAccuracy.YEAR,true);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Date values -- valid
    Date dateValue = DateTimeConstants.yearSputnik.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkYear(result,1957);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    
    dateValue = DateTimeConstants.dateJulianDayEpoch.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkYear(result,-4713);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    dateValue = DateTimeConstants.timeStampRataDieEpoch.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkYear(result,0001);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    
    // Calendar values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.yearSputnik, checkResult);
    checkYear(result,1957);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.dateJulianDayEpoch, checkResult);
    checkYear(result,-4713);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.timeStampRataDieEpoch, checkResult);
    checkYear(result,0001);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    

    // Date values -- valid
    DateTime dt = new DateTime(DateTimeConstants.yearSputnik);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkYear(result,1957);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    dt = new DateTime(DateTimeConstants.dateJulianDayEpoch);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkYear(result,-4713);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);

    dt = new DateTime(DateTimeConstants.timeStampRataDieEpoch);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkYear(result,0001);
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);

    // Other object values -- invalid
    assertEquals(null,datatype.toValue(new byte[]{0,1,2}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue("Hello", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  public void testToValueObjectDay()
  {
    GregorianDatetimeCalendar result;
    DateTimeType datatype = new DateTimeType(DateTime.TimeAccuracy.DAY,true);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Date values -- valid
    // "extend" operation from YYYY to YYYY-MM-DD 
    Date dateValue = DateTimeConstants.yearSputnik.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkDate(result,1957,01-1,01);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    // YYYY-MM-DD -> YYYY-MM-DD
    dateValue = DateTimeConstants.dateJulianDayEpoch.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkDate(result,-4713,12-1,24);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    // YYYY-MM-DDTHH:MM:SS -> YYYY-MM-DD "round" operation
    dateValue = DateTimeConstants.timeStampRataDieEpoch.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkDate(result,0001,01-1,01);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    
    // Calendar values -- valid
    // "extend" operation from YYYY to YYYY-MM-DD 
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.yearSputnik, checkResult);
    checkDate(result,1957,01-1,01);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.dateJulianDayEpoch, checkResult);
    checkDate(result,-4713,12-1,24);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.timeStampRataDieEpoch, checkResult);
    checkDate(result,0001,01-1,01);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    

    // Date values -- valid
    DateTime dt = new DateTime(DateTimeConstants.yearSputnik);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkDate(result,1957,01-1,01);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    dt = new DateTime(DateTimeConstants.dateJulianDayEpoch);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkDate(result,-4713,12-1,24);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);

    dt = new DateTime(DateTimeConstants.timeStampRataDieEpoch);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkDate(result,0001,01-1,01);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(false, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);

    // Other object values -- invalid
    assertEquals(null,datatype.toValue(new byte[]{0,1,2}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue("Hello", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  public void testToValueObjectMillisecondsUTC()
  {
    GregorianDatetimeCalendar result;
    DateTimeType datatype = new DateTimeType(DateTime.TimeAccuracy.MILLISECOND,false);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Date values -- valid
    // "extend" operation from YYYY to YYYY-MM-DDTHH:MM:SS.sss 
    Date dateValue = DateTimeConstants.yearSputnikUTC.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkTimestampUTC(result,1957,01-1,01,00,00,00,00);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    // YYYY-MM-DD -> YYYY-MM-DD
    dateValue = DateTimeConstants.dateJulianDayEpochUTC.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkDate(result,-4713,12-1,24);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    // YYYY-MM-DDTHH:MM:SS -> YYYY-MM-DD "round" operation
    dateValue = DateTimeConstants.timeStampMoonUTC.getTime();
    result = (GregorianDatetimeCalendar) datatype.toValue(dateValue, checkResult);
    checkTimestampUTC(result,1969,7-1,21,02,56,15,00);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    
    // Calendar values -- valid
    // "extend" operation from YYYY to YYYY-MM-DD 
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.yearSputnikUTC, checkResult);
    checkTimestampUTC(result,1957,01-1,01,00,00,00,00);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.dateJulianDayEpochUTC, checkResult);
    checkDate(result,-4713,12-1,24);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.timeStampMoonUTC, checkResult);
    checkTimestampUTC(result,1969,7-1,21,02,56,15,00);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    

    // Date values -- valid
    DateTime dt = new DateTime(DateTimeConstants.yearSputnikUTC);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkTimestampUTC(result,1957,01-1,01,00,00,00,00);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    dt = new DateTime(DateTimeConstants.dateJulianDayEpochUTC);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkDate(result,-4713,12-1,24);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);

    dt = new DateTime(DateTimeConstants.timeStampMoonUTC);
    result = (GregorianDatetimeCalendar) datatype.toValue(dt, checkResult);
    checkTimestampUTC(result,1969,7-1,21,02,56,15,00);
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.YEAR));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.DAY_OF_MONTH));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.HOUR_OF_DAY));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MINUTE));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.SECOND));
    assertEquals(true, result.isUserSet(GregorianDatetimeCalendar.MILLISECOND));
    assertEquals(null,checkResult.error);
    
    
    // Other object values -- invalid
    assertEquals(null,datatype.toValue(new int[]{0,1,2}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue("Hello", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  public void testToValueObjectsChoicesYearLocal()
  {
    GregorianDatetimeCalendar result;
    TypeCheckResult checkResult = new TypeCheckResult();
    
    Calendar choices[] = new Calendar[]{DateTimeConstants.dateUNIXEpoch}; 
    DateTimeType datatype = new DateTimeType(DateTime.TimeAccuracy.YEAR,true,choices);
    
    // Date values
    assertEquals(null,datatype.toValue(DateTimeConstants.dateJulianDayEpoch.getTime(), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,datatype.toValue(DateTimeConstants.timestampMinuteSputnik.getTime(), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.dateUNIXEpoch.getTime(), checkResult);
    checkYear(result,1970);
    assertEquals(null,checkResult.error);
    
    // Calendar values
    assertEquals(null,datatype.toValue(DateTimeConstants.dateJulianDayEpoch, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,datatype.toValue(DateTimeConstants.timestampMinuteSputnik, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    result = (GregorianDatetimeCalendar) datatype.toValue(DateTimeConstants.dateUNIXEpoch, checkResult);
    checkYear(result,1970);
    assertEquals(null,checkResult.error);
  }
  
  public void testToValueObjectsRangesLocal()
  {
    GregorianDatetimeCalendar result;
    TypeCheckResult checkResult = new TypeCheckResult();
    
    DateTimeType rangeType = new DateTimeType(DateTime.TimeAccuracy.SECOND,true,
        DateTimeConstants.timeStampJulianDayEpoch,
        DateTimeConstants.timeStampRataDieEpoch);
    
    assertEquals(true,rangeType.isBounded());
    assertEquals(DateTimeConstants.timeStampJulianDayEpoch,rangeType.getMinInclusive());
    assertEquals(DateTimeConstants.timeStampRataDieEpoch,rangeType.getMaxInclusive());

    checkResult.reset();
    result = (GregorianDatetimeCalendar) rangeType.toValue(DateTimeConstants.timeStampJulianDayEpoch, checkResult);
    assertNotNull(result);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) rangeType.toValue(DateTimeConstants.timeStampRataDieEpoch, checkResult);
    assertNotNull(result);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) rangeType.toValue(DateTimeConstants.timeStampMoon, checkResult);
    assertNull(result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    result = (GregorianDatetimeCalendar) rangeType.toValue(DateTimeConstants.dateUNIXEpoch, checkResult);
    assertNull(result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  /*------------- Selecting/Enumeration facet functionality ----------------*/
  
  /** No limitation through enumeration, all values are allowed. */
  public void testChoicesNone()
  {
    DateTimeType timeType = defaultInstance;
    
    DateTimeEnumerationFacet enumFacet = (DateTimeEnumerationFacet)timeType; 
    assertEquals(null,enumFacet.getChoices());
    
    Calendar calendarValue = Calendar.getInstance();
    calendarValue.set(Calendar.HOUR_OF_DAY,12);
    calendarValue.set(Calendar.MINUTE,13);
    calendarValue.set(Calendar.SECOND,0);
    calendarValue.set(Calendar.MILLISECOND,0);
    
    
    assertEquals(true,enumFacet.validateChoice(calendarValue));
  }
  
  
  /** Enumerated allowed values with accuracy of minutes */
  public void testValidateChoicesMinutes()
  {
    // Valid choices
    
    Calendar choices[] =
      {DateTimeConstants.timeStampSputnik,DateTimeConstants.timeStampEndWWII,
        DateTimeConstants.timeStampMoon};
    
    DateTimeType timeType = new DateTimeType(DateTime.TimeAccuracy.MINUTE,true,choices);
    DateTimeEnumerationFacet enumFacet = (DateTimeEnumerationFacet)timeType; 
    assertNotNull(enumFacet.getChoices());
    
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampRataDieEpoch));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampUNIXEpoch));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampJulianDayEpoch ));
    
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnik));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampEndWWII));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoon));
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timestampMinuteMoon));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timestampMinuteSputnik));

    // This is ALSO valid ,since these are considered local values, since timezones
    // are not compared.
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoonUTC));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnikUTC));
    
  }
  

  /** Enumerated allowed values with accuracy of minutes */
  public void testValidateChoicesSeconds()
  {
    // Valid choices
    
    Calendar choices[] =
      {DateTimeConstants.timeStampSputnikUTC,
        DateTimeConstants.timeStampMoonUTC};
    
    DateTimeType timeType = new DateTimeType(DateTime.TimeAccuracy.SECOND,true,choices);
    DateTimeEnumerationFacet enumFacet = (DateTimeEnumerationFacet)timeType; 
    assertNotNull(enumFacet.getChoices());
    
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampRataDieEpoch));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampUNIXEpoch));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampJulianDayEpoch ));
    
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnik));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampEndWWII));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoon));
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnikUTC));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoonUTC));
    
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timestampMinuteMoon));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timestampMinuteSputnik));

    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoonUTC));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnikUTC));
    
    // With only the year ,these should fail
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.yearSputnik));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.yearEndWWII));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.yearMoon));
    
  }
  
  
  /** Enumerated allowed values with accuracy of year */
  public void testValidateChoicesYear()
  {
    // Valid choices
    
    Calendar choices[] =
      {DateTimeConstants.timeStampSputnikUTC,
        DateTimeConstants.timeStampMoonUTC};
    
    DateTimeType timeType = new DateTimeType(DateTime.TimeAccuracy.YEAR,true,choices);
    DateTimeEnumerationFacet enumFacet = (DateTimeEnumerationFacet)timeType; 
    assertNotNull(enumFacet.getChoices());
    
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampRataDieEpoch));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampUNIXEpoch));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampJulianDayEpoch ));
    
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnik));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampEndWWII));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoon));
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnikUTC));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoonUTC));
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timestampMinuteMoon));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timestampMinuteSputnik));

    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoonUTC));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnikUTC));
    

    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.yearSputnik));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.yearEndWWII));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.yearMoon));
    
  }
  
  /** Enumerated allowed values with accuracy of year */
  public void testValidateChoicesDay()
  {
    // Valid choices
    
    Calendar choices[] =
      {DateTimeConstants.timeStampSputnikUTC,
        DateTimeConstants.timeStampMoonUTC};
    
    DateTimeType timeType = new DateTimeType(DateTime.TimeAccuracy.DAY,true,choices);
    DateTimeEnumerationFacet enumFacet = (DateTimeEnumerationFacet)timeType; 
    assertNotNull(enumFacet.getChoices());
    
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampRataDieEpoch));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampUNIXEpoch));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampJulianDayEpoch ));
    
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnik));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.timeStampEndWWII));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoon));
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnikUTC));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoonUTC));
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timestampMinuteMoon));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timestampMinuteSputnik));

    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampMoonUTC));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.timeStampSputnikUTC));
    
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.dateSputnik));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.dateEndWWII));
    assertEquals(true,enumFacet.validateChoice(DateTimeConstants.dateStampMoon));

    
    // These should fail, since only the year is specified
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.yearSputnik));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.yearEndWWII));
    assertEquals(false,enumFacet.validateChoice(DateTimeConstants.yearMoon));
  }
  
  /*----------------------------- ranges --------------------------------*/
  public void testRange()
  {
    DateTimeType timeType;
    boolean success;
    // Try invalid range
    success = false;
    try
    {
      timeType = new DateTimeType(DateTime.TimeAccuracy.MINUTE,false,DateTimeConstants.timeStampEndWWII,
          DateTimeConstants.timeStampJulianDayEpoch);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
    
    DateTimeType rangeType = new DateTimeType(DateTime.TimeAccuracy.SECOND,true,
        DateTimeConstants.timeStampJulianDayEpoch,
        DateTimeConstants.timeStampRataDieEpoch);
    
    assertEquals(true,rangeType.isBounded());
    assertEquals(DateTimeConstants.timeStampJulianDayEpoch,rangeType.getMinInclusive());
    assertEquals(DateTimeConstants.timeStampRataDieEpoch,rangeType.getMaxInclusive());

    assertEquals(true,rangeType.validateRange(DateTimeConstants.timeStampJulianDayEpoch));
    assertEquals(true,rangeType.validateRange(DateTimeConstants.timeStampRataDieEpoch));
    assertEquals(false,rangeType.validateRange(DateTimeConstants.timeStampMoon));
    assertEquals(false,rangeType.validateRange(DateTimeConstants.dateUNIXEpoch));
    

    rangeType = new DateTimeType(DateTime.TimeAccuracy.DAY,true,
        DateTimeConstants.dateRataDieEpoch,
        DateTimeConstants.dateUNIXEpoch);
    assertEquals(true,rangeType.isBounded());

    assertEquals(false,rangeType.validateRange(DateTimeConstants.timeStampJulianDayEpoch));
    assertEquals(false,rangeType.validateRange(DateTimeConstants.dateJulianDayEpoch));
    assertEquals(true,rangeType.validateRange(DateTimeConstants.timeStampRataDieEpoch));
    assertEquals(true,rangeType.validateRange(DateTimeConstants.timeStampMoon));
    assertEquals(true,rangeType.validateRange(DateTimeConstants.dateUNIXEpoch));
    assertEquals(true,rangeType.validateRange(DateTimeConstants.dateSputnik));
    
  }
  
  /*-------------------- equals/restriction functionality ---------------*/
  public void testEquals()
  {
    DateTimeType type01 = new DateTimeType();
    DateTimeType type02 = new DateTimeType();
    
    assertTrue(type01.equals(type02));
    
    DateTimeType typeLocalSecs = new DateTimeType(DateTime.TimeAccuracy.SECOND,true);
    DateTimeType typeUTCSecs = new DateTimeType(DateTime.TimeAccuracy.SECOND,false);
    assertFalse(typeLocalSecs.equals(typeUTCSecs));
    
    DateTimeType typeLocalMS = new DateTimeType(DateTime.TimeAccuracy.MILLISECOND,true);
    assertFalse(typeLocalSecs.equals(typeLocalMS));    
  }
  
  public void testEqualsChoices()
  {
    Calendar choices[] =
      {DateTimeConstants.timeStampSputnikUTC,
        DateTimeConstants.timeStampMoonUTC};
    
    
    DateTimeType typeLocalSecs = new DateTimeType(DateTime.TimeAccuracy.SECOND,true);
    DateTimeType typeLocalSecsChoices = new DateTimeType(DateTime.TimeAccuracy.SECOND,true,choices);
    DateTimeType typeLocalOtherSecsChoices = new DateTimeType(DateTime.TimeAccuracy.SECOND,true,choices);
    
    assertFalse(typeLocalSecs.equals(typeLocalSecsChoices));
    assertTrue(typeLocalOtherSecsChoices.equals(typeLocalSecsChoices));
    
  }
  
  public void testIsRestriction()
  {
    
    DateTimeType type01 = new DateTimeType();
    DateTimeType type02 = new DateTimeType();
    
    assertEquals(false,type01.isRestrictionOf(type02));
    
    DateTimeType typeLocalSecs = new DateTimeType(DateTime.TimeAccuracy.SECOND,true);
    DateTimeType typeLocalMS = new DateTimeType(DateTime.TimeAccuracy.MILLISECOND,true);
    assertTrue(typeLocalSecs.isRestrictionOf(typeLocalMS));
    assertFalse(typeLocalMS.isRestrictionOf(typeLocalSecs));
    
    type01 = new DateTimeType();
    type02 = new DateTimeType(DateTime.TimeAccuracy.SECOND,true,DateTimeConstants.timeStampJulianDayEpoch,
        DateTimeConstants.timeStampEndWWII);
    assertTrue(type02.isRestrictionOf(type01));
    assertFalse(type01.isRestrictionOf(type02));

    
    
    boolean success = false;
    try
    {
      assertFalse(typeLocalSecs.isRestrictionOf(new BooleanType()));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
    
  }
  

}
