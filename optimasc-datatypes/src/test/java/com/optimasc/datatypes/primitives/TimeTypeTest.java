package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.DatatypeTest;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.TimeZone;

import com.optimasc.datatypes.BoundedProperty;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeFactory;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.defined.BinaryType;
import com.optimasc.datatypes.facets.DateTimeEnumerationFacet;
import com.optimasc.date.DateTime;
import com.optimasc.lang.CharacterSet;
import com.optimasc.lang.GregorianDatetimeCalendar;

public class TimeTypeTest extends DatatypeTest
{
  protected TimeType defaultInstance;
  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = (TimeType) TypeFactory.getDefaultInstance(TimeType.class).getType();
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
    TimeType instance = defaultInstance;
    assertEquals(true,instance.isOrdered());
    assertEquals(false,instance.isNumeric());
    assertEquals(true,instance.isBounded());
    assertEquals(null,instance.getAllowedValuesAsCalendars());
  }

  /*------------------ Accuracy/Timezone functionality ----------------*/
  public void testAccuracyAndLocalTime()
  {
    TimeType timeType;
    // Default accuracy
    assertEquals(DateTime.TimeAccuracy.SECOND,defaultInstance.getAccuracy());
    assertEquals(true,defaultInstance.localTime);
    
    // Set other types of accuracy and timezones information -- All valid
    timeType = new TimeType(DateTime.TimeAccuracy.MILLISECOND,false);
    assertEquals(DateTime.TimeAccuracy.MILLISECOND,timeType.getAccuracy());
    assertEquals(false,timeType.localTime);
    
    timeType = new TimeType(DateTime.TimeAccuracy.MILLISECOND,true);
    assertEquals(DateTime.TimeAccuracy.MILLISECOND,timeType.getAccuracy());
    assertEquals(true,timeType.localTime);

    timeType = new TimeType(DateTime.TimeAccuracy.MINUTE,false);
    assertEquals(DateTime.TimeAccuracy.MINUTE,timeType.getAccuracy());
    assertEquals(false,timeType.localTime);
    
    timeType = new TimeType(DateTime.TimeAccuracy.MINUTE,true);
    assertEquals(DateTime.TimeAccuracy.MINUTE,timeType.getAccuracy());
    assertEquals(true,timeType.localTime);
    
    // Invalid accuracy values for a Time type
    
    boolean success = false;
    
    try
    {
      timeType = new TimeType(DateTime.TimeAccuracy.DAY,false);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);

    success = false;
    try
    {
      timeType = new TimeType(DateTime.TimeAccuracy.YEAR,false);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
    success = false;
    try
    {
      timeType = new TimeType(Integer.MIN_VALUE,false);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
  }
  
  /*------------------ toValue functionality ----------------*/
  
  /** Verifies that calendar fields are of the values passed. Also checks
   *  if Calendar instance is in UTC TZ
   */
  public static void checkTimeUTC(Calendar cal, int hour, int minute, int second, int millis)
  {
    assertEquals(hour,cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(minute,cal.get(Calendar.MINUTE));
    assertEquals(second,cal.get(Calendar.SECOND));
    assertEquals(millis,cal.get(Calendar.MILLISECOND));
    // Check if we are in UTC or not
    TimeZone tz = cal.getTimeZone();
    assertEquals(0,tz.getRawOffset());
  }
  
  /** Verifies that calendar fields are of the values passed. Also checks
   *  if Calendar instance is in UTC TZ
   */
  public static void checkTimeUTC(Calendar cal, int hour, int minute, int second)
  {
    assertEquals(hour,cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(minute,cal.get(Calendar.MINUTE));
    assertEquals(second,cal.get(Calendar.SECOND));
    // Check if we are in UTC or not
    TimeZone tz = cal.getTimeZone();
    assertEquals(0,tz.getRawOffset());
  }
  
  /** Verifies that calendar fields are of the values passed. Also checks
   *  if Calendar instance is in UTC TZ
   */
  public static void checkTimeUTC(Calendar cal, int hour, int minute)
  {
    assertEquals(hour,cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(minute,cal.get(Calendar.MINUTE));
    // Check if we are in UTC or not
    TimeZone tz = cal.getTimeZone();
    assertEquals(0,tz.getRawOffset());
  }
  
  
  
  public static void checkTime(Calendar cal, int hour, int minute, int second, int millis)
  {
    assertEquals(hour,cal.get(Calendar.HOUR_OF_DAY));
    assertEquals(minute,cal.get(Calendar.MINUTE));
    assertEquals(second,cal.get(Calendar.SECOND));
    assertEquals(millis,cal.get(Calendar.MILLISECOND));
  }
  
  
  public static DateTime.Time createTime(Calendar cal)
  {
    boolean localTime = cal.getTimeZone().getRawOffset()!=0;
    return new DateTime.Time(cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        cal.get(Calendar.SECOND),
        cal.get(Calendar.MILLISECOND),localTime);
  }

  public static DateTime.Time createTime(Calendar cal, boolean localTime)
  {
    return new DateTime.Time(cal.get(Calendar.HOUR_OF_DAY),
        cal.get(Calendar.MINUTE),
        cal.get(Calendar.SECOND),
        cal.get(Calendar.MILLISECOND),localTime);
  }
  
  
  public void testToValueLongMinutesUTC()
  {
    GregorianDatetimeCalendar result;
    TimeType datatype = new TimeType(DateTime.TimeAccuracy.MINUTE,false);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // 00:00
    int minValue = 0; 
    // 12:29
    int midValue = (12*60)+29;
    // 23:59
    int maxValue = 23*60+59;
    
    // Valid values
    result = (GregorianDatetimeCalendar) datatype.toValue(minValue, checkResult);
    checkTimeUTC(result,0,0,0,0);
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(midValue, checkResult);
    checkTimeUTC(result,12,29,0,0);
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(maxValue, checkResult);
    checkTimeUTC(result,23,59,0,0);
    assertEquals(null,checkResult.error);

    // Invalid values
    assertEquals(null,datatype.toValue(-1, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(23*60+60, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,((DatatypeException)checkResult.error).getCode());
  }
  
  
  public void testToValueLongSecondsUTC()
  {
    GregorianDatetimeCalendar result;
    TimeType datatype = new TimeType(DateTime.TimeAccuracy.SECOND,false);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // 00:00:00
    int minValue = 0; 
    // 12:29:59
    int midValue = (12*60*60)+29*60+59;
    // 23:59:59
    int maxValue = (23*60*60)+59*60+59;
    
    // Valid values
    result = (GregorianDatetimeCalendar) datatype.toValue(minValue, checkResult);
    checkTimeUTC(result,0,0,0,0);
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(midValue, checkResult);
    checkTimeUTC(result,12,29,59,0);
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(maxValue, checkResult);
    checkTimeUTC(result,23,59,59,0);
    assertEquals(null,checkResult.error);

    // Invalid values
    assertEquals(null,datatype.toValue(-1, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,((DatatypeException)checkResult.error).getCode());
    // Take into account the leap seconds for the maximum allowed value
    assertEquals(null,datatype.toValue((23*60*60)+59*60+61, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,((DatatypeException)checkResult.error).getCode());
  }
  
  
  public void testToValueLongMillisecondsUTC()
  {
    GregorianDatetimeCalendar result;
    TimeType datatype = new TimeType(DateTime.TimeAccuracy.MILLISECOND,false);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // 00:00:00:00
    int minValue = 0; 
    // 12:29:59:0450
    int midValue = (((12*60*60)+29*60+59)*1000)+450;
    // 23:59:59:999
    int maxValue = (((23*60*60)+59*60+59)*1000)+999;
    
    // Valid values
    result = (GregorianDatetimeCalendar) datatype.toValue(minValue, checkResult);
    checkTimeUTC(result,0,0,0,0);
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(midValue, checkResult);
    checkTimeUTC(result,12,29,59,450);
    assertEquals(null,checkResult.error);
    
    result = (GregorianDatetimeCalendar) datatype.toValue(maxValue, checkResult);
    checkTimeUTC(result,23,59,59,999);
    assertEquals(null,checkResult.error);

    // Invalid values
    assertEquals(null,datatype.toValue(-1, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,((DatatypeException)checkResult.error).getCode());
    // Take into account the leap seconds for the maximum allowed value
    assertEquals(null,datatype.toValue((((23*60*60)+59*60+60)*1000)+1000, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,((DatatypeException)checkResult.error).getCode());
  }
  
  public void testToValueObjectMinutesUTC()
  {
    GregorianDatetimeCalendar result;
    TimeType datatype = new TimeType(DateTime.TimeAccuracy.MINUTE,false);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Create calendar values at UTC
    Calendar minCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    minCalendarValue.set(Calendar.HOUR_OF_DAY,0);
    minCalendarValue.set(Calendar.MINUTE,0);
    
    Calendar midCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    midCalendarValue.set(Calendar.HOUR_OF_DAY,13);
    midCalendarValue.set(Calendar.MINUTE,15);
    
    Calendar maxCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    maxCalendarValue.set(Calendar.HOUR_OF_DAY,23);
    maxCalendarValue.set(Calendar.MINUTE,59);
    
    
    // Date values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(minCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(midCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,13,15);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,23,59);
    assertEquals(null,checkResult.error);
    
    // Calendar values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(minCalendarValue, checkResult);
    checkTimeUTC(result,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(midCalendarValue, checkResult);
    checkTimeUTC(result,13,15);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue, checkResult);
    checkTimeUTC(result,23,59);
    assertEquals(null,checkResult.error);
    
    
    // Time values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(minCalendarValue), checkResult);
    checkTimeUTC(result,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(midCalendarValue), checkResult);
    checkTimeUTC(result,13,15);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(maxCalendarValue), checkResult);
    checkTimeUTC(result,23,59);
    assertEquals(null,checkResult.error);

    // Other object values -- invalid
    assertEquals(null,datatype.toValue(new byte[]{0,1,2}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue("Hello", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  public void testToValueObjectSecondsUTC()
  {
    GregorianDatetimeCalendar result;
    TimeType datatype = new TimeType(DateTime.TimeAccuracy.SECOND,false);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Create calendar values at UTC
    Calendar minCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    minCalendarValue.set(Calendar.HOUR_OF_DAY,0);
    minCalendarValue.set(Calendar.MINUTE,0);
    minCalendarValue.set(Calendar.SECOND,0);
    
    Calendar midCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    midCalendarValue.set(Calendar.HOUR_OF_DAY,13);
    midCalendarValue.set(Calendar.MINUTE,15);
    midCalendarValue.set(Calendar.SECOND,59);
    
    Calendar maxCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    maxCalendarValue.set(Calendar.HOUR_OF_DAY,23);
    maxCalendarValue.set(Calendar.MINUTE,59);
    maxCalendarValue.set(Calendar.SECOND,59);
    
    
    // Date values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(minCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(midCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,13,15,59);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,23,59,59);
    assertEquals(null,checkResult.error);
    
    // Calendar values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(minCalendarValue, checkResult);
    checkTimeUTC(result,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(midCalendarValue, checkResult);
    checkTimeUTC(result,13,15,59);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue, checkResult);
    checkTimeUTC(result,23,59,59);
    assertEquals(null,checkResult.error);
    
    
    // Time values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(minCalendarValue), checkResult);
    checkTimeUTC(result,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(midCalendarValue), checkResult);
    checkTimeUTC(result,13,15,59);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(maxCalendarValue), checkResult);
    checkTimeUTC(result,23,59,59);
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
    TimeType datatype = new TimeType(DateTime.TimeAccuracy.MILLISECOND,false);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Create calendar values at UTC
    Calendar minCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    minCalendarValue.set(Calendar.HOUR_OF_DAY,0);
    minCalendarValue.set(Calendar.MINUTE,0);
    minCalendarValue.set(Calendar.SECOND,0);
    minCalendarValue.set(Calendar.MILLISECOND,0);
    
    Calendar midCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    midCalendarValue.set(Calendar.HOUR_OF_DAY,13);
    midCalendarValue.set(Calendar.MINUTE,15);
    midCalendarValue.set(Calendar.SECOND,59);
    midCalendarValue.set(Calendar.MILLISECOND,999);
    
    Calendar maxCalendarValue = Calendar.getInstance(DateTime.ZULU);
    minCalendarValue.setTimeZone(DateTime.ZULU);
    maxCalendarValue.set(Calendar.HOUR_OF_DAY,23);
    maxCalendarValue.set(Calendar.MINUTE,59);
    maxCalendarValue.set(Calendar.SECOND,59);
    maxCalendarValue.set(Calendar.MILLISECOND,999);
    
    
    // Date values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(minCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,0,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(midCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,13,15,59,999);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue.getTime(), checkResult);
    checkTimeUTC(result,23,59,59,999);
    assertEquals(null,checkResult.error);
    
    // Calendar values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(minCalendarValue, checkResult);
    checkTimeUTC(result,0,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(midCalendarValue, checkResult);
    checkTimeUTC(result,13,15,59,999);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue, checkResult);
    checkTimeUTC(result,23,59,59,999);
    assertEquals(null,checkResult.error);
    
    
    // Time values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(minCalendarValue), checkResult);
    checkTimeUTC(result,0,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(midCalendarValue), checkResult);
    checkTimeUTC(result,13,15,59,999);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(maxCalendarValue), checkResult);
    checkTimeUTC(result,23,59,59,999);
    assertEquals(null,checkResult.error);

    // Other object values -- invalid
    assertEquals(null,datatype.toValue(new int[]{0,1,2}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue("Hello", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  public void testToValueObjectMillisecondsLocal()
  {
    GregorianDatetimeCalendar result;
    TimeType datatype = new TimeType(DateTime.TimeAccuracy.MILLISECOND,true);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Create calendar values at local time offset
    Calendar minCalendarValue = Calendar.getInstance();
    minCalendarValue.set(Calendar.HOUR_OF_DAY,0);
    minCalendarValue.set(Calendar.MINUTE,0);
    minCalendarValue.set(Calendar.SECOND,0);
    minCalendarValue.set(Calendar.MILLISECOND,0);
    
    Calendar midCalendarValue = Calendar.getInstance();
    midCalendarValue.set(Calendar.HOUR_OF_DAY,13);
    midCalendarValue.set(Calendar.MINUTE,15);
    midCalendarValue.set(Calendar.SECOND,59);
    midCalendarValue.set(Calendar.MILLISECOND,999);
    
    Calendar maxCalendarValue = Calendar.getInstance();
    maxCalendarValue.set(Calendar.HOUR_OF_DAY,23);
    maxCalendarValue.set(Calendar.MINUTE,59);
    maxCalendarValue.set(Calendar.SECOND,59);
    maxCalendarValue.set(Calendar.MILLISECOND,999);
    
    
    // Date values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(minCalendarValue.getTime(), checkResult);
    checkTime(result,0,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(midCalendarValue.getTime(), checkResult);
    checkTime(result,13,15,59,999);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue.getTime(), checkResult);
    checkTime(result,23,59,59,999);
    assertEquals(null,checkResult.error);
    
    // Calendar values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(minCalendarValue, checkResult);
    checkTime(result,0,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(midCalendarValue, checkResult);
    checkTime(result,13,15,59,999);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue, checkResult);
    checkTime(result,23,59,59,999);
    assertEquals(null,checkResult.error);
    
    
    // Time values -- valid
    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(minCalendarValue, true), checkResult);
    checkTime(result,0,0,0,0);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(midCalendarValue, true), checkResult);
    checkTime(result,13,15,59,999);
    assertEquals(null,checkResult.error);

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(maxCalendarValue, true), checkResult);
    checkTime(result,23,59,59,999);
    assertEquals(null,checkResult.error);

    // Other object values -- invalid
    assertEquals(null,datatype.toValue(new int[]{0,1,2}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue("Hello", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  public void testToValueObjectsChoicesMillisecondsLocal()
  {
    GregorianDatetimeCalendar result;
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Create calendar values at local time offset
    Calendar minCalendarValue = Calendar.getInstance();
    minCalendarValue.set(Calendar.HOUR_OF_DAY,0);
    minCalendarValue.set(Calendar.MINUTE,0);
    minCalendarValue.set(Calendar.SECOND,0);
    minCalendarValue.set(Calendar.MILLISECOND,0);
    
    Calendar midCalendarValue = Calendar.getInstance();
    midCalendarValue.set(Calendar.HOUR_OF_DAY,13);
    midCalendarValue.set(Calendar.MINUTE,15);
    midCalendarValue.set(Calendar.SECOND,59);
    midCalendarValue.set(Calendar.MILLISECOND,999);
    
    Calendar maxCalendarValue = Calendar.getInstance();
    maxCalendarValue.set(Calendar.HOUR_OF_DAY,23);
    maxCalendarValue.set(Calendar.MINUTE,59);
    maxCalendarValue.set(Calendar.SECOND,59);
    maxCalendarValue.set(Calendar.MILLISECOND,999);
    
    Calendar choices[] = new Calendar[]{maxCalendarValue}; 
    TimeType datatype = new TimeType(DateTime.TimeAccuracy.MILLISECOND,true,choices);
    
    
    // Date values -- all invalid
    assertEquals(null,datatype.toValue(minCalendarValue.getTime(), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,datatype.toValue(midCalendarValue.getTime(), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue.getTime(), checkResult);
    checkTime(result,23,59,59,999);
    assertEquals(null,checkResult.error);
    
    // Calendar values -- valid
    assertEquals(null, datatype.toValue(minCalendarValue, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,datatype.toValue(midCalendarValue, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    result = (GregorianDatetimeCalendar) datatype.toValue(maxCalendarValue, checkResult);
    checkTime(result,23,59,59,999);
    assertEquals(null,checkResult.error);
    
    
    // Time values -- valid
    assertEquals(null,datatype.toValue(createTime(minCalendarValue, true), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,datatype.toValue(createTime(midCalendarValue, true), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    result = (GregorianDatetimeCalendar) datatype.toValue(createTime(maxCalendarValue, true), checkResult);
    checkTime(result,23,59,59,999);
    assertEquals(null,checkResult.error);

    // Other object values -- invalid
    assertEquals(null,datatype.toValue(new int[]{0,1,2}, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue("Hello", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
  }
  
  /*------------- Selecting/Enumeration facet functionality ----------------*/
  
  /** No limitation through enumeration, all values are allowed. */
  public void testChoicesNone()
  {
    TimeType timeType = defaultInstance;
    
    DateTimeEnumerationFacet enumFacet = (DateTimeEnumerationFacet)timeType; 
    assertEquals(null,enumFacet.getAllowedValuesAsCalendars());
    
    Calendar calendarValue = Calendar.getInstance();
    calendarValue.set(Calendar.HOUR_OF_DAY,12);
    calendarValue.set(Calendar.MINUTE,13);
    calendarValue.set(Calendar.SECOND,0);
    calendarValue.set(Calendar.MILLISECOND,0);
    
    
    assertEquals(true,enumFacet.isValid(calendarValue));
    
  }
  
  
  /** Enumerated allowed values with accuracy of minutes */
  public void testValidateChoicesMinutes()
  {
    // Valid choices
    
    Calendar choices[] =
      {DateTimeConstants.timeStampSputnik,DateTimeConstants.timeStampEndWWII,
        DateTimeConstants.timeStampMoon};
    
    TimeType timeType = new TimeType(DateTime.TimeAccuracy.MINUTE,true,choices);
    DateTimeEnumerationFacet enumFacet = (DateTimeEnumerationFacet)timeType; 
    assertNotNull(enumFacet.getAllowedValuesAsCalendars());
    
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timeStampRataDieEpoch));
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timeStampUNIXEpoch));
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timeStampJulianDayEpoch ));
    
    
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeStampSputnik));
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeStampEndWWII));
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeStampMoon));
    
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timestampMinuteMoon));
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timestampMinuteSputnik));

    // This is ALSO valid ,since these are considered local values, since timezones
    // are not compared.
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeSecondsMoonUTC));
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeSecondsSputnikUTC));
    
  }
  

  /** Enumerated allowed values with accuracy of minutes */
  public void testValidateChoicesSeconds()
  {
    // Valid choices
    
    Calendar choices[] =
      {DateTimeConstants.timeStampSputnikUTC,
        DateTimeConstants.timeStampMoonUTC};
    
    TimeType timeType = new TimeType(DateTime.TimeAccuracy.SECOND,true,choices);
    DateTimeEnumerationFacet enumFacet = (DateTimeEnumerationFacet)timeType; 
    assertNotNull(enumFacet.getAllowedValuesAsCalendars());
    
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timeStampRataDieEpoch));
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timeStampUNIXEpoch));
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timeStampJulianDayEpoch ));
    
    
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeStampSputnik));
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timeStampEndWWII));
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeStampMoon));
    
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeStampSputnikUTC));
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeStampMoonUTC));
    
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timestampMinuteMoon));
    assertEquals(false,enumFacet.isValid(DateTimeConstants.timestampMinuteSputnik));

    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeSecondsMoonUTC));
    assertEquals(true,enumFacet.isValid(DateTimeConstants.timeSecondsSputnikUTC));
  }
  
  /*----------------------------- ranges --------------------------------*/
  public void testRange()
  {
    TimeType ordered01 = new TimeType(DateTime.TimeAccuracy.SECOND,true);

    TypeCheckResult checkResult = new TypeCheckResult();
    assertEquals(ordered01.toValue(0,checkResult),ordered01.getMinInclusive());
    assertEquals(ordered01.toValue(86399,checkResult),ordered01.getMaxInclusive());
    
    assertEquals(false,ordered01.isValid(100000));
    assertEquals(false,ordered01.isValid(-1));
    assertEquals(true,ordered01.isValid(127));
    assertEquals(true,ordered01.isValid(0));

    TimeType ordered02 = new TimeType(DateTime.TimeAccuracy.MINUTE,true);

    assertEquals(ordered02.toValue(0,checkResult),ordered02.getMinInclusive());
    assertEquals(ordered02.toValue(1439,checkResult),ordered02.getMaxInclusive());

    assertEquals(false,ordered02.isValid(100000));
    assertEquals(false,ordered02.isValid(-1));
    assertEquals(true,ordered02.isValid(127));
    assertEquals(true,ordered02.isValid(0));

    assertEquals(false,ordered02.isValid(BigDecimal.valueOf(312000)));
    assertEquals(false,ordered02.isValid(BigDecimal.valueOf(-1)));
    assertEquals(true,ordered02.isValid(BigDecimal.valueOf(127)));
    assertEquals(true,ordered02.isValid(BigDecimal.valueOf(1000)));
    
    TimeType ordered03 = new TimeType(DateTime.TimeAccuracy.MILLISECOND,true);

    assertEquals(false,ordered03.isValid(100000*1000));
    assertEquals(false,ordered03.isValid(-1));
    assertEquals(true,ordered03.isValid(127));
    assertEquals(true,ordered03.isValid(0));

    assertEquals(false,ordered03.isValid(BigDecimal.valueOf(312000*1000)));
    assertEquals(false,ordered03.isValid(BigDecimal.valueOf(-1)));
    assertEquals(true,ordered03.isValid(BigDecimal.valueOf(127)));
    assertEquals(true,ordered03.isValid(BigDecimal.valueOf(54)));
    
  }
  
  
  /*-------------------- equals/restriction functionality ---------------*/
  public void testEquals()
  {
    
    TimeType type01 = new TimeType();
    TimeType type02 = new TimeType();
    
    assertTrue(type01.equals(type02));
    
    TimeType typeLocalSecs = new TimeType(DateTime.TimeAccuracy.SECOND,true);
    TimeType typeUTCSecs = new TimeType(DateTime.TimeAccuracy.SECOND,false);
    assertFalse(typeLocalSecs.equals(typeUTCSecs));
    
    TimeType typeLocalMS = new TimeType(DateTime.TimeAccuracy.MILLISECOND,true);
    assertFalse(typeLocalSecs.equals(typeLocalMS));
    
  }
  
  
  public void testEqualsChoices()
  {
    Calendar choices[] =
      {DateTimeConstants.timeStampSputnikUTC,
        DateTimeConstants.timeStampMoonUTC};
    
    
    TimeType typeLocalSecs = new TimeType(DateTime.TimeAccuracy.SECOND,true);
    TimeType typeLocalSecsChoices = new TimeType(DateTime.TimeAccuracy.SECOND,true,choices);
    TimeType typeLocalOtherSecsChoices = new TimeType(DateTime.TimeAccuracy.SECOND,true,choices);
    
    assertFalse(typeLocalSecs.equals(typeLocalSecsChoices));
    assertTrue(typeLocalOtherSecsChoices.equals(typeLocalSecsChoices));
    
  }
  
  
  
  public void testIsRestriction()
  {
    
    TimeType type01 = new TimeType();
    TimeType type02 = new TimeType();
    
    assertEquals(false,type01.isRestrictionOf(type02));
    
    TimeType typeLocalSecs = new TimeType(DateTime.TimeAccuracy.SECOND,true);
    TimeType typeLocalMS = new TimeType(DateTime.TimeAccuracy.MILLISECOND,true);
    assertTrue(typeLocalSecs.isRestrictionOf(typeLocalMS));
    assertFalse(typeLocalMS.isRestrictionOf(typeLocalSecs));
    
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
