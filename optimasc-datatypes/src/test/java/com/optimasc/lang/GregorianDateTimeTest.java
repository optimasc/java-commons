package com.optimasc.lang;

import junit.framework.TestCase;

public class GregorianDateTimeTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public void testConstructorDefault()
  {
    GregorianDateTime cal = new GregorianDateTime();
    GregorianDateTime cal2;
    
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getYear());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getMonth());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getDay());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getHour());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getMinute());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getFractionalSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getTimezone());
    assertEquals(true, cal.isValid());
    
    cal2 = cal.normalize();
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getYear());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getMonth());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getDay());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getHour());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getMinute());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getFractionalSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getTimezone());
    assertEquals(true, cal2.isValid());
    
    
    assertEquals(true, cal.equals(cal2));
    
  }
  
  public void testConstructorString()
  {
    GregorianDateTime cal = new GregorianDateTime();
    GregorianDateTime cal2;
    
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getYear());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getMonth());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getDay());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getHour());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getMinute());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getFractionalSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal.getTimezone());
    assertEquals(true, cal.isValid());
    
    cal2 = cal.normalize();
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getYear());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getMonth());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getDay());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getHour());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getMinute());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getFractionalSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getTimezone());
    assertEquals(true, cal2.isValid());
    
    
    assertEquals(true, cal.equals(cal2));
    
  }
  
  public void testNormalize()
  {
    GregorianDateTime cal2;

    // Valid values: YYYY-MM-DDT:HH:MM:SS.sss
    GregorianDateTime cal = new GregorianDateTime(-32768,12,31,23,59,00,999,300);
    assertEquals(-32768, cal.getYear());
    assertEquals(12, cal.getMonth());
    assertEquals(31, cal.getDay());
    assertEquals(23, cal.getHour());
    assertEquals(59, cal.getMinute());
    assertEquals(00, cal.getSecond());
    assertEquals(999, cal.getFractionalSecond());
    assertEquals(300, cal.getTimezone());
    assertEquals(true, cal.isValid());
    
    cal2 = cal.normalize();
    assertEquals(-32768, cal2.getYear());
    assertEquals(12, cal2.getMonth());
    assertEquals(31, cal2.getDay());
    assertEquals(18, cal2.getHour());
    assertEquals(59, cal2.getMinute());
    assertEquals(00, cal2.getSecond());
    assertEquals(999, cal2.getFractionalSecond());
    assertEquals(0, cal2.getTimezone());
    assertEquals(true, cal2.isValid());
    
    // Valid values : HH:MM:SS
    cal = new GregorianDateTime(DateTimeConstants.FIELD_UNDEFINED,
        DateTimeConstants.FIELD_UNDEFINED,
        DateTimeConstants.FIELD_UNDEFINED,
        23,59,00,DateTimeConstants.FIELD_UNDEFINED,-840);
    
    cal2 = cal.normalize();
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getYear());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getMonth());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getDay());
    assertEquals(13, cal2.getHour());
    assertEquals(59, cal2.getMinute());
    assertEquals(00, cal2.getSecond());
    assertEquals(DateTimeConstants.FIELD_UNDEFINED, cal2.getFractionalSecond());
    assertEquals(0, cal2.getTimezone());
    assertEquals(true, cal2.isValid());
    
    
    // Valid values: YYYY-MM-DDT:HH:MM:SS.sss with already UTC TZ
    cal = new GregorianDateTime(-32768,12,31,23,59,00,999,0);
    
    cal2 = cal.normalize();
    assertEquals(-32768, cal2.getYear());
    assertEquals(12, cal2.getMonth());
    assertEquals(31, cal2.getDay());
    assertEquals(23, cal2.getHour());
    assertEquals(59, cal2.getMinute());
    assertEquals(00, cal2.getSecond());
    assertEquals(999, cal2.getFractionalSecond());
    assertEquals(0, cal2.getTimezone());
    assertEquals(true, cal2.isValid());
    
    
    
  }
  
  public void testConstructorValues()
  {
    GregorianDateTime cal2;

    // Valid values
    GregorianDateTime cal = new GregorianDateTime(-32768,12,31,23,59,00,999,300);
    assertEquals(-32768, cal.getYear());
    assertEquals(12, cal.getMonth());
    assertEquals(31, cal.getDay());
    assertEquals(23, cal.getHour());
    assertEquals(59, cal.getMinute());
    assertEquals(00, cal.getSecond());
    assertEquals(999, cal.getFractionalSecond());
    assertEquals(300, cal.getTimezone());
    assertEquals(true, cal.isValid());
    
  }
  
  

}
