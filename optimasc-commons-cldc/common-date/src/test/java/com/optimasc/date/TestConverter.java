package com.optimasc.date;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import junit.framework.TestCase;

public class TestConverter extends TestCase
{
  public static final TimeZone UTC = TimeZone.getTimeZone("GMT"); 


  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public void testEpochs()
  {
    DateTime.Date date = new DateTime.Date(2024,06,25);
    DateTime.Time time = new DateTime.Time(13,49,25,50,false);
    DateTime dateTime = new DateTime(date,time,Calendar.SECOND);
    System.out.println(dateTime.toCalendar().getTime().getTime());
    
    long value;
    DateTime result;
    
    // Calculate the values == Default days
    value = DateConverter.toDuration(dateTime, null);
    result = DateConverter.toDateTime(value, null);
    assertEquals(Calendar.DAY_OF_MONTH,result.precision);
    assertEquals(date.year,result.date.year);
    assertEquals(date.month,result.date.month);
    assertEquals(date.day,result.date.day);
    
    value = DateConverter.toDuration(dateTime, DateTimeFormat.DEFAULT_SECONDS);
    result = DateConverter.toDateTime(value, DateTimeFormat.DEFAULT_SECONDS);
    assertEquals(Calendar.SECOND,result.precision);
    assertEquals(date.year,result.date.year);
    assertEquals(date.month,result.date.month);
    assertEquals(date.day,result.date.day);
    assertEquals(time.hour,result.time.hour);
    assertEquals(time.minute,result.time.minute);
    assertEquals(time.second,result.time.second);
    
    
    // Hard-coded values -- JULIAN DAY NUMBER
    value = DateConverter.toDuration(dateTime, DateTimeEpochs.JULIAN_DAY);
    result = DateConverter.toDateTime(value, DateTimeEpochs.JULIAN_DAY);
    assertEquals(2460487L,value);
    assertEquals(Calendar.DAY_OF_MONTH,result.precision);
    assertEquals(date.year,result.date.year);
    assertEquals(date.month,result.date.month);
    assertEquals(date.day,result.date.day);

    // Hard-coded values -- UNIX EPOCH
    value = DateConverter.toDuration(dateTime, DateTimeEpochs.POSIX);
    result = DateConverter.toDateTime(value, DateTimeEpochs.POSIX);
    assertEquals(1719323365L,value);
    assertEquals(Calendar.SECOND,result.precision);
    assertEquals(date.year,result.date.year);
    assertEquals(date.month,result.date.month);
    assertEquals(date.day,result.date.day);
    assertEquals(time.hour,result.time.hour);
    assertEquals(time.minute,result.time.minute);
    assertEquals(time.second,result.time.second);
    
    // Hard-coded values -- Javascript EPOCH
    value = DateConverter.toDuration(dateTime, DateTimeEpochs.JAVASCRIPT);
    result = DateConverter.toDateTime(value, DateTimeEpochs.JAVASCRIPT);
    assertEquals(1719323365050L,value);
    assertEquals(Calendar.MILLISECOND,result.precision);
    assertEquals(date.year,result.date.year);
    assertEquals(date.month,result.date.month);
    assertEquals(date.day,result.date.day);
    assertEquals(time.hour,result.time.hour);
    assertEquals(time.minute,result.time.minute);
    assertEquals(time.second,result.time.second);
    assertEquals(time.millisecond,result.time.millisecond);
    
    // Hard-coded values -- NTP
/*    value = DateConverter.toDuration(dateTime, DateTimeEpochs.NTP);
    result = DateConverter.toDateTime(value, DateTimeEpochs.NTP);
    assertEquals(1719323382997L,value);
    assertEquals(date.year,result.date.year);
    assertEquals(date.month,result.date.month);
    assertEquals(date.day,result.date.day);*/
    
    // Hard-coded values -- Lilian
    value = DateConverter.toDuration(dateTime, DateTimeEpochs.LILIAN);
    result = DateConverter.toDateTime(value, DateTimeEpochs.LILIAN);
    assertEquals(161327L,value);
    assertEquals(Calendar.DAY_OF_MONTH,result.precision);
    assertEquals(date.year,result.date.year);
    assertEquals(date.month,result.date.month);
    assertEquals(date.day,result.date.day);
    
    // Hard-coded values -- Rata Die day number
    value = DateConverter.toDuration(dateTime, DateTimeEpochs.RATA_DIE_DAY);
    result = DateConverter.toDateTime(value, DateTimeEpochs.RATA_DIE_DAY);
    assertEquals(739062L,value);
    assertEquals(Calendar.DAY_OF_MONTH,result.precision);
    assertEquals(date.year,result.date.year);
    assertEquals(date.month,result.date.month);
    assertEquals(date.day,result.date.day);
  }
  
  /** Test the converter using UNIX Epoch as a reference */
  public void testConverter()
  {
    GregorianCalendar calendar = new GregorianCalendar(UTC);
    calendar.setGregorianChange(new Date(Long.MIN_VALUE));
    for (int year=-32768; year < 32768; year++)
    {
       System.out.println("Year : "+year);
       for (int month=1; month < 13; month++)
       {
         int day = 1;
//          System.out.println("Month : "+month);
//          long calendarMillis =  Date.UTC(year-1900, month-1, 1, 00, 00, 00);
          calendar = new GregorianCalendar(UTC);
          calendar.setGregorianChange(new Date(Long.MIN_VALUE));
          if (year <= 0)
          {
             calendar.set(Calendar.ERA, GregorianCalendar.BC);
             calendar.set(Calendar.YEAR,Math.abs(year - 1));
          } else
          {
             calendar.set(Calendar.ERA, GregorianCalendar.AD);
             calendar.set(Calendar.YEAR,year);
          }          
          calendar.set(Calendar.MONTH,month-1);
          calendar.set(Calendar.DAY_OF_MONTH,day);
          calendar.set(Calendar.HOUR_OF_DAY,0);
          calendar.set(Calendar.MINUTE,0);
          calendar.set(Calendar.SECOND,0);
          calendar.set(Calendar.MILLISECOND,0);
          calendar.setTimeZone(UTC);
          DateTime dateTime = new DateTime(year,month,day);
          long calculatedDuration = DateConverter.toDuration(dateTime,DateTimeEpochs.POSIX);
          DateTime decodedDate = DateConverter.toDateTime(calculatedDuration, DateTimeEpochs.POSIX);
          assertEquals(year,decodedDate.date.year);
          assertEquals(month,decodedDate.date.month);
          assertEquals(day,decodedDate.date.day);
          long calendarDuration = (calendar.getTimeInMillis()/(1000));
          assertEquals(calendarDuration,calculatedDuration);
       }
    }
  }

}
