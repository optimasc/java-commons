package com.optimasc.date;

import java.util.Calendar;

import junit.framework.TestCase;

public class TestNTFSDateTime extends TestCase
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
    cal.set(Calendar.MONTH, 1-1);
    cal.set(Calendar.DAY_OF_MONTH, 19);
    cal.set(Calendar.HOUR_OF_DAY, 15);
    cal.set(Calendar.MINUTE, 31);
    cal.set(Calendar.SECOND, 24);
    cal.set(Calendar.MILLISECOND, 0);
    
    long value = NTFSDateTime.converter.encode(cal);
    assertEquals(126559278840000000L,value);
  }

  public void testGetPrecision()
  {
    assertEquals(Calendar.MILLISECOND,NTFSDateTime.converter.getPrecision());
  }

  public void testGetMinBits()
  {
    assertEquals(64,NTFSDateTime.converter.getMinBits());
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
       value = NTFSDateTime.converter.encode(dateTime);
    } catch (IllegalArgumentException e)
    {
      exceptionThrown = true;
    }
    assertEquals(true,exceptionThrown);
  }  
  
  
  
//  Taken from: https://www.forensicfocus.com/articles/interpretation-of-ntfs-timestamps/
  
  public void testEncodeDecodeDateTimeValid01()
  {
    DateTime.Date date = new DateTime.Date(1601,01,01);
    DateTime.Time time = new DateTime.Time(00,00,00,00,false);
    DateTime dateTime = new DateTime(date,time,Calendar.MILLISECOND);
    
    long value = NTFSDateTime.converter.encode(dateTime);
    assertTrue(dateTime.matches(NTFSDateTime.converter.decode(value)));
    assertEquals(0,value);
  }
  
    
  public void testEncodeDecodeDateTimeValid02()
  {
    DateTime.Date date = new DateTime.Date(2002,1,19);
    DateTime.Time time = new DateTime.Time(15,31,24,00,false);
    DateTime dateTime = new DateTime(date,time,Calendar.MILLISECOND);
    
    long value = NTFSDateTime.converter.encode(dateTime);
    assertTrue(dateTime.matches(NTFSDateTime.converter.decode(value)));
    assertEquals(126559278840000000L,value);
  }
  
  
  public void testEncodeDateTimeInvalid()
  {
    DateTime.Date dateLow = new DateTime.Date(NTFSDateTime.converter.getMinimumYear()-1,06,25);
    DateTime.Date dateHigh = new DateTime.Date(NTFSDateTime.converter.getMaximumYear()+1,06,25);
    DateTime.Time time = new DateTime.Time(13,49,25,50,false);
    DateTime dateTimeLow = new DateTime(dateLow,time,Calendar.MILLISECOND);
    DateTime dateTimeHigh = new DateTime(dateHigh,time,Calendar.MILLISECOND);
    boolean exceptionThrown;
    long value;

    exceptionThrown = false;
    try 
    {
       value = NTFSDateTime.converter.encode(dateTimeLow);
    } catch (IllegalArgumentException e)
    {
      exceptionThrown = true;
    }
    assertEquals(true,exceptionThrown);
    
    exceptionThrown = true;
    try 
    {
       value = NTFSDateTime.converter.encode(dateTimeHigh);
    } catch (IllegalArgumentException e)
    {
      exceptionThrown = true;
    }
    assertEquals(true,exceptionThrown);
  }
  

  public void testGetMinimumYear()
  {
    assertEquals(1601,NTFSDateTime.converter.getMinimumYear());
  }

  public void testGetMaximumYear()
  {
    assertEquals(30828,NTFSDateTime.converter.getMaximumYear());
  }

}
