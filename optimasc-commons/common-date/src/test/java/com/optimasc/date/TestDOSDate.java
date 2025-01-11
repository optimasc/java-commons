package com.optimasc.date;

import java.util.Calendar;

import junit.framework.TestCase;

public class TestDOSDate extends TestCase
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
    DateTime.Date date = new DateTime.Date(1980,01,01);
    DateTime.Time time = new DateTime.Time(13,49,25,50,false);
    DateTime dateTime = new DateTime(date,time,Calendar.SECOND);
    
    long value = DOSDate.converter.encode(dateTime.toCalendar());
    assertTrue(dateTime.matches(DOSDate.converter.decode(value)));
  }

  public void testGetPrecision()
  {
    assertEquals(Calendar.DAY_OF_MONTH,DOSDate.converter.getPrecision());
  }

  public void testGetMinBits()
  {
    assertEquals(16,DOSDate.converter.getMinBits());
  }

  public void testEncodeDecodeDateTimeValid()
  {
    DateTime.Date date = new DateTime.Date(2024,06,25);
    DateTime.Time time = new DateTime.Time(13,49,25,50,true);
    DateTime dateTime = new DateTime(date,time,Calendar.SECOND);
    
    long value = DOSDate.converter.encode(dateTime);
    assertTrue(dateTime.matches(DOSDate.converter.decode(value)));
  }
  
  
  public void testEncodeDateTimeInvalid()
  {
    DateTime.Date dateLow = new DateTime.Date(DOSDate.converter.getMinimumYear()-1,06,25);
    DateTime.Date dateHigh = new DateTime.Date(DOSDate.converter.getMaximumYear()+1,06,25);
    DateTime.Time time = new DateTime.Time(13,49,25,50,true);
    DateTime dateTimeLow = new DateTime(dateLow,time,Calendar.SECOND);
    DateTime dateTimeHigh = new DateTime(dateHigh,time,Calendar.SECOND);
    boolean exceptionThrown;
    long value;

    exceptionThrown = false;
    try 
    {
       value = DOSDate.converter.encode(dateTimeLow);
    } catch (IllegalArgumentException e)
    {
      exceptionThrown = true;
    }
    assertEquals(true,exceptionThrown);
    
    exceptionThrown = true;
    try 
    {
       value = DOSDate.converter.encode(dateTimeHigh);
    } catch (IllegalArgumentException e)
    {
      exceptionThrown = true;
    }
    assertEquals(true,exceptionThrown);
  }
  

  public void testGetMinimumYear()
  {
    assertEquals(1980,DOSDate.converter.getMinimumYear());
  }

  public void testGetMaximumYear()
  {
    assertEquals(2107,DOSDate.converter.getMaximumYear());
  }

}
