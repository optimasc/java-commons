package com.optimasc.date;

import java.util.Calendar;

import junit.framework.TestCase;

public class TestDOSDateTime extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  

  public void testEncodeCalendar()
  {
    Calendar cal = Calendar.getInstance();
    cal.set(Calendar.YEAR, 2002);
    cal.set(Calendar.MONTH, 03-1);
    cal.set(Calendar.DAY_OF_MONTH, 18);
    cal.set(Calendar.HOUR_OF_DAY, 14);
    cal.set(Calendar.MINUTE, 47);
    cal.set(Calendar.SECOND, 50);
    
    long value = DOSDateTime.converter.encode(cal);
    assertEquals(0x2C7275F9L,value);
  }

  public void testGetPrecision()
  {
    assertEquals(Calendar.SECOND,DOSDateTime.converter.getPrecision());
  }

  public void testGetMinBits()
  {
    assertEquals(32,DOSDateTime.converter.getMinBits());
  }

  /* Only local times are supported, but we indicate a UTC time */
  public void testEncodeDateTimeUTCTime()
  {
    DateTime.Date date = new DateTime.Date(1980,06,25);
    DateTime.Time time = new DateTime.Time(13,49,25,50,false);
    DateTime dateTime = new DateTime(date,time,Calendar.SECOND);
    boolean exceptionThrown;
    long value;

    exceptionThrown = false;
    try 
    {
       value = DOSDateTime.converter.encode(dateTime);
    } catch (IllegalArgumentException e)
    {
      exceptionThrown = true;
    }
    assertEquals(true,exceptionThrown);
  }  
  
  
  public void testEncodeDecodeDateTimeValid()
  {
    DateTime.Date date = new DateTime.Date(2002,03,18);
    DateTime.Time time = new DateTime.Time(14,47,50,true);
    DateTime dateTime = new DateTime(date,time,Calendar.SECOND);
    
    long value = DOSDateTime.converter.encode(dateTime);
    assertTrue(dateTime.matches(DOSDateTime.converter.decode(value)));
    assertEquals(0x2C7275F9L,value);
  }
  
  
  public void testEncodeDateTimeInvalid()
  {
    DateTime.Date dateLow = new DateTime.Date(DOSDateTime.converter.getMinimumYear()-1,06,25);
    DateTime.Date dateHigh = new DateTime.Date(DOSDateTime.converter.getMaximumYear()+1,06,25);
    DateTime.Time time = new DateTime.Time(13,49,25,50,false);
    DateTime dateTimeLow = new DateTime(dateLow,time,Calendar.SECOND);
    DateTime dateTimeHigh = new DateTime(dateHigh,time,Calendar.SECOND);
    boolean exceptionThrown;
    long value;

    exceptionThrown = false;
    try 
    {
       value = DOSDateTime.converter.encode(dateTimeLow);
    } catch (IllegalArgumentException e)
    {
      exceptionThrown = true;
    }
    assertEquals(true,exceptionThrown);
    
    exceptionThrown = true;
    try 
    {
       value = DOSDateTime.converter.encode(dateTimeHigh);
    } catch (IllegalArgumentException e)
    {
      exceptionThrown = true;
    }
    assertEquals(true,exceptionThrown);
  }
  

  public void testGetMinimumYear()
  {
    assertEquals(1980,DOSDateTime.converter.getMinimumYear());
  }

  public void testGetMaximumYear()
  {
    assertEquals(2107,DOSDateTime.converter.getMaximumYear());
  }

}
