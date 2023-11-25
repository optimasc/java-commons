package com.optimasc.date;

import java.math.BigDecimal;

import junit.framework.TestCase;

public class TestJulianDay extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  /** Tests the julian day conversion routine. The 
   *  values here have been compared with the values
   *  calculated by the online Julian Day converter
   *  at NASA JPL (https://ssd.jpl.nasa.gov/tools/jdc/#/cd)
   *  for positive years and here for negative values:
   *  https://www.fourmilab.ch/documents/calendar/
   *  as the NASA one uses Julian calendar for years
   *  before the Gregorian calendar.
   * 
   */
  public void testToDouble()
  {
     // Valid values
     double result = JulianDateTime.encode(2000, 01, 01, 23*60*60+59*60);
     BigDecimal aa = new BigDecimal(result);
     aa = aa.setScale(4, BigDecimal.ROUND_DOWN);
     BigDecimal bb = new BigDecimal("2451545.4993");
     bb = bb.setScale(4, BigDecimal.ROUND_DOWN);
     assertEquals(true,aa.equals(bb));

     // Valid values
     result = JulianDateTime.encode(-2000, 01, 01, 0);
     System.out.println(result);
     aa = new BigDecimal(result);
     aa = aa.setScale(4, BigDecimal.ROUND_DOWN);
     bb = new BigDecimal("990574.5");
     bb = bb.setScale(4, BigDecimal.ROUND_DOWN);
     assertEquals(true,aa.equals(bb));
     
  }

}
