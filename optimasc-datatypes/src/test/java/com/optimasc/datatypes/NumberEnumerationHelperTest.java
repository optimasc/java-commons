package com.optimasc.datatypes;

import junit.framework.TestCase;

public class NumberEnumerationHelperTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public void testNumberEnumerationHelperDouble()
  {
    Number choices[]=new Number[]{new Double(100.0d),new Double(1d),new Double(-55d)};
    NumberEnumerationHelper helper = new NumberEnumerationHelper();
    helper.setAllowedValues(choices);
    assertEquals(true,helper.isValid(new Double(1d)));
    assertEquals(false,helper.isValid(new Double(1.5d)));
    assertEquals(false,helper.isValid(0));
  }
  
  public void testNumberEnumerationHelperInt()
  {
    Number choices[]=new Number[]{new Integer(10),new Integer(100),new Integer(-55)};
    NumberEnumerationHelper helper = new NumberEnumerationHelper();
    helper.setAllowedValues(choices);
    assertEquals(true,helper.isValid(new Integer(100)));
    assertEquals(false,helper.isValid(new Double(1.5d)));
    assertEquals(false,helper.isValid(new Double(1.5d)));
    assertEquals(false,helper.isValid(0));
    assertEquals(false,helper.isValid(new Integer(11)));
  }
  

}
