package com.optimasc.datatypes;

import java.math.BigDecimal;

import com.optimasc.datatypes.primitives.BooleanType;

import junit.framework.TestCase;

public class DecimalRangeHelperTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public void testConstructorError()
  {
    boolean success = false;
    try
    {
      DecimalRangeHelper rangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(Integer.MAX_VALUE),BigDecimal.valueOf(0));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
  }
  
  public void testNoBounds()
  {
    DecimalRangeHelper rangeHelper = new DecimalRangeHelper(null,null);
    DecimalRangeHelper otherRangeHelper = new DecimalRangeHelper(null,null);
    
    assertNull(rangeHelper.getMinInclusive());
    assertNull(rangeHelper.getMaxInclusive());
    assertFalse(rangeHelper.isBounded());
    assertFalse(rangeHelper.isNaturalNumber());
    assertTrue(rangeHelper.validateRange(Integer.MIN_VALUE));
    assertTrue(rangeHelper.validateRange(0));
    assertTrue(rangeHelper.validateRange(Integer.MAX_VALUE));
    
    assertTrue(rangeHelper.equals(otherRangeHelper));
    assertFalse(rangeHelper.isRestrictionOf(otherRangeHelper));
    assertFalse(otherRangeHelper.isRestrictionOf(rangeHelper));
  }
  
  public void testNoUpperBoundOne()
  {
    BigDecimal ZERO = BigDecimal.valueOf(0);
    DecimalRangeHelper rangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(0),null);
    DecimalRangeHelper otherRangeHelper = new DecimalRangeHelper(null,null);
    
    assertEquals(ZERO,rangeHelper.getMinInclusive());
    assertEquals(null,rangeHelper.getMaxInclusive());
    assertEquals(true,rangeHelper.isBounded());
    assertEquals(true,rangeHelper.isNaturalNumber());
    assertEquals(false,rangeHelper.validateRange(Integer.MIN_VALUE));
    assertEquals(true,rangeHelper.validateRange(0));
    assertEquals(true,rangeHelper.validateRange(Integer.MAX_VALUE));
    
    assertFalse(rangeHelper.equals(otherRangeHelper));
    assertTrue(rangeHelper.isRestrictionOf(otherRangeHelper));
    assertFalse(otherRangeHelper.isRestrictionOf(rangeHelper));
  }
  
  public void testNoUpperBoundBoth()
  {
    BigDecimal ZERO = BigDecimal.valueOf(0);
    DecimalRangeHelper rangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(0),null);
    DecimalRangeHelper otherRangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(55),null);
    
    assertEquals(ZERO,rangeHelper.getMinInclusive());
    assertEquals(null,rangeHelper.getMaxInclusive());
    assertEquals(true,rangeHelper.isBounded());
    assertEquals(true,rangeHelper.isNaturalNumber());
    assertEquals(false,rangeHelper.validateRange(Integer.MIN_VALUE));
    assertEquals(true,rangeHelper.validateRange(0));
    assertEquals(true,rangeHelper.validateRange(Integer.MAX_VALUE));
    
    assertFalse(rangeHelper.equals(otherRangeHelper));
    assertFalse(rangeHelper.isRestrictionOf(otherRangeHelper));
    assertTrue(otherRangeHelper.isRestrictionOf(rangeHelper));
  }
  
  
  
  
  public static boolean isRestriction(Integer minVal, Integer maxVal, Integer lowRange, Integer maxRange)
  {
    // No bounds at all/
    if ((lowRange == null) && (maxRange == null))
    {
        if ((minVal == null) && (maxVal == null))
        {
          return false;
        }
        return true;
    }
    
    if ((lowRange!=null) && (maxRange==null))
    {
      if (minVal != null)
      {
        return (minVal.longValue() > lowRange.longValue());
      }
      return false;
    }
        
    if ((lowRange==null) && (maxRange!=null))
    {
      if (maxVal != null)
      {
        return (maxVal.longValue() > maxRange.longValue());
      }
      return false;
    }
    if (minVal == null)
      return false;
    if (maxVal == null)
      return false;
    
    return (minVal.longValue() > lowRange.longValue()) && (maxVal.longValue() < maxRange.longValue());
  }
  
/*  public void testIntersection()
  {
    assertEquals(true,isRestriction(0,255, -32767,32767));
    assertEquals(false,isRestriction(-32768,255, -32767,32767));
    assertEquals(false,isRestriction(-100000,-99999, -32767,32767));

    assertEquals(false,isRestriction(null,-99999, -32767,32767));    
    assertEquals(false,isRestriction(-100000,null, -32767,32767));    

    assertEquals(true,isRestriction(-100000,-99999, null,null));
    assertEquals(false,isRestriction(-100000,-99999, -32767,null));
    assertEquals(true,isRestriction(0,null, -32767,null));
    
  }*/
  

}
