package com.optimasc.text;

import java.text.ParseException;
import java.text.ParsePosition;

import junit.framework.TestCase;

public class ParsersTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public void testParsePositiveNumberValid()
  {
    ParsePosition pos = new ParsePosition(0);
    String strValue;
    int result;
    

    strValue = "123";
    pos.setIndex(0);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 1, 127);
     assertEquals(123,result);
     assertEquals(3,pos.getIndex());
    } catch (ParseException e)
    {
      fail();
    }
    
    
    strValue = "12345A";
    pos.setIndex(0);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 1, 5);
     assertEquals(12345,result);
     assertEquals(5,pos.getIndex());
    } catch (ParseException e)
    {
      fail();
    }
    
    strValue = "12345A";
    pos.setIndex(0);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 5, 5);
     assertEquals(12345,result);
     assertEquals(5,pos.getIndex());
    } catch (ParseException e)
    {
      fail();
    }
    
    strValue = "ABCD12345A";
    pos.setIndex(4);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 1, 3);
     assertEquals(123,result);
     assertEquals(7,pos.getIndex());
    } catch (ParseException e)
    {
      fail();
    }
    
    strValue = "ABCD00";
    pos.setIndex(4);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 2, 2);
     assertEquals(0,result);
     assertEquals(6,pos.getIndex());
    } catch (ParseException e)
    {
      fail();
    }
    
    strValue = "ABCD00";
    pos.setIndex(4);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 2, 3);
     assertEquals(0,result);
     assertEquals(6,pos.getIndex());
    } catch (ParseException e)
    {
      fail();
    }
    
    // Use-case: minDigits = 0;
    strValue = "A9BCD";
    pos.setIndex(1);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 1, 3);
     assertEquals(9,result);
     assertEquals(2,pos.getIndex());
    } catch (ParseException e)
    {
      fail();
    }
    
  }
  
  public void testParsePositiveNumberInvalid()
  {
    ParsePosition pos = new ParsePosition(0);
    String strValue;
    int result;
    boolean fail = true;
  
    strValue = "ABCD";
    fail = false;
    pos.setIndex(0);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 2, 3);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
    // Minimum digits is 2 and we have only 1 digit
    strValue = "0";
    fail = false;
    pos.setIndex(0);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 2, 2);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    // Minimum digits is 2 and we have only 1 digit
    strValue = "A01BZ";
    fail = false;
    pos.setIndex(1);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 3, 3);
    } catch (ParseException e)
    {
      assertEquals(3,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    

    // Illegal minDigits and maxDigits
    strValue = "1234";
    fail = false;
    pos.setIndex(0);
    pos.setErrorIndex(-1);
    try
    {
     result = Parsers.parsePositiveNumber(strValue, pos, 3, 2);
    } catch (ParseException e1)
    {
      fail();
    }
    catch (IllegalArgumentException e)
    {
      fail = false;
    }
    assertFalse(fail);
    
    
    
  }
}
