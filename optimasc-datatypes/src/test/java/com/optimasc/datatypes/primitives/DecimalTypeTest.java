package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DatatypeTest;
import com.optimasc.datatypes.NumberEnumerationFacet;
import com.optimasc.datatypes.NumberRangeFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;

public class DecimalTypeTest extends DatatypeTest
{
  DecimalType defaultInstance;
  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = new DecimalType(2);
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(BigDecimal.class, defaultInstance.getClassType());
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
    DecimalType instance = defaultInstance;
    assertEquals(true,instance.isOrdered());
    assertEquals(true,instance.isNumeric());
    assertEquals(false,instance.isBounded());
  }
  
  
  /*----------------------- Range facet functionality ---------------------*/
  /** Test unbounded value. */
  public void testRangeUnBounded()
  {
    AbstractNumberType numericType = defaultInstance;
    
    // Check first non-bounded value
    assertEquals(2,numericType.getScale());
    assertEquals(Integer.MAX_VALUE,numericType.getPrecision());
    NumberRangeFacet rangedType = (NumberRangeFacet) defaultInstance;
    
    assertEquals(false,rangedType.isBounded());
    
    
    
    assertEquals(null,rangedType.getMinInclusive());
    assertEquals(null,rangedType.getMaxInclusive());
    
    // Verify that choices are null
    if (rangedType instanceof NumberEnumerationFacet)
    {
      NumberEnumerationFacet enumFacet = (NumberEnumerationFacet) rangedType;
      assertEquals(null,enumFacet.getChoices());
    }
    
    // No bounds, so everything should be allowed.
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Long.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Long.MAX_VALUE,2)));
    
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Integer.MAX_VALUE,2)));

    // Exceptions, this should fail, as this is decimal values for an integer type!
    assertEquals(true,rangedType.validateRange(new BigDecimal(Float.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(new BigDecimal("0.55")));
    assertEquals(true,rangedType.validateRange(new BigDecimal("423423.564")));
  }
  
  
  /** Test bounded value. */
  public void testRangeBounded()
  {
    AbstractNumberType numericType = new DecimalType(BigDecimal.valueOf(0, 2),BigDecimal.valueOf(999999, 2));
    
    assertEquals(2,numericType.getScale());
    // This value is consisten with industry values (IBM/Microsoft)
    assertEquals(6,numericType.getPrecision());
    NumberRangeFacet rangedType = numericType;
    
    assertEquals(true,rangedType.isBounded());
    assertEquals(BigDecimal.valueOf(0,2),rangedType.getMinInclusive());
    assertEquals(BigDecimal.valueOf(999999, 2),rangedType.getMaxInclusive());
    
    // Verify that choices are null
    if (numericType instanceof NumberEnumerationFacet)
    {
      NumberEnumerationFacet enumFacet = numericType;
      assertEquals(null,enumFacet.getChoices());
    }
    
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MAX_VALUE)));
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Integer.MIN_VALUE,2)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0,2)));
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Integer.MAX_VALUE,2)));

    // Exceptions, this should fail, as this is decimal values for an integer type!
    assertEquals(false,rangedType.validateRange(new BigDecimal(-323515.624645)));
    assertEquals(false,rangedType.validateRange(new BigDecimal(Float.MAX_VALUE)));
  }
  
  /** Test bounded value where lower bound is equal to upper bound. */
   public void testRangeEqual()
  {
    AbstractNumberType numericType = new DecimalType(BigDecimal.valueOf(-2559,1),BigDecimal.valueOf(+2559,1));
    assertEquals(1,numericType.getScale());
    assertEquals(4,numericType.getPrecision());
    
    NumberRangeFacet rangedType = numericType;
    
    assertEquals(true,rangedType.isBounded());
    assertEquals(BigDecimal.valueOf(-2559,1),rangedType.getMinInclusive());
    assertEquals(BigDecimal.valueOf(+2559,1),rangedType.getMaxInclusive());
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Byte.MAX_VALUE,1)));
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MAX_VALUE)));
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Byte.MIN_VALUE)));
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Integer.MAX_VALUE)));

    assertEquals(false,rangedType.validateRange(new BigDecimal("253535325")));
    assertEquals(true,rangedType.validateRange(new BigDecimal(0.55)));
    assertEquals(false,rangedType.validateRange(new BigDecimal("-256.0")));
    assertEquals(true,rangedType.validateRange(new BigDecimal("2.7712")));
  }
  

  /** Verify when the ranges are not valid */
  public void testRangeIllegalBounds()
  {
    boolean success = false;
   
    // Bounds are not correctly ordered
    try
    {
      NumberRangeFacet rangedType = new DecimalType(BigDecimal.valueOf(Integer.MAX_VALUE),BigDecimal.valueOf(Integer.MIN_VALUE));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
    // Bounds do not have the same scale 
    success = false;
    try
    {
      NumberRangeFacet rangedType = new DecimalType(BigDecimal.valueOf(Integer.MAX_VALUE,4),BigDecimal.valueOf(Integer.MIN_VALUE,2));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
  }
  
  /*------------- Selecting/Enumeration facet functionality ----------------*/
  
  /** No limitation through enumeration, all values are allowed. */
  public void testChoicesNone()
  {
    AbstractNumberType numericType = defaultInstance;
    
    assertEquals(2,numericType.getScale());
    assertEquals(Integer.MAX_VALUE,numericType.getPrecision());
    
    NumberEnumerationFacet enumFacet = (NumberEnumerationFacet)numericType; 
    assertEquals(null,enumFacet.getChoices());
    
    // The range will be null
    if (enumFacet instanceof NumberRangeFacet)
    {
      NumberRangeFacet rangeType = (NumberRangeFacet) enumFacet;
      assertEquals(null,rangeType.getMinInclusive());
      assertEquals(null,rangeType.getMaxInclusive());
    }
    
    
    assertEquals(true,enumFacet.validateChoice(BigDecimal.valueOf(Integer.MIN_VALUE,2)));
    assertEquals(true,enumFacet.validateChoice(BigDecimal.valueOf(0)));
    assertEquals(true,enumFacet.validateChoice(BigDecimal.valueOf(Integer.MAX_VALUE,2)));
    
    assertEquals(true,enumFacet.validateChoice(Integer.MIN_VALUE));
    assertEquals(true,enumFacet.validateChoice(0));
    assertEquals(true,enumFacet.validateChoice(Integer.MAX_VALUE));
    
  }
  
  
  /** Test when choices are invalid, when the scale of the choices are not same */
  public void testChoicesInvalid()
  {
    boolean success = false;
    
    try
    {
      BigDecimal choices[] = new BigDecimal[] {BigDecimal.valueOf(0,2),new BigDecimal(0.5)};
      // Check first non-bounded value
      NumberEnumerationFacet enumFacet = new DecimalType(choices);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
  }
  
  /** No limitation through enumeration, all values are allowed. */
  public void testValidateChoices()
  {
    BigDecimal choices[] = {
        BigDecimal.valueOf(800000,2),
        BigDecimal.valueOf(1102500,2),
        BigDecimal.valueOf(2205000,2),
        BigDecimal.valueOf(4410000,2),
        BigDecimal.valueOf(4800000,2)
    };   
    BigDecimal minInclusive = choices[0];
    BigDecimal maxInclusive = choices[4];
    BigDecimal midValue = choices[2];
    
    AbstractNumberType numericType = new DecimalType(choices);
    assertEquals(2,numericType.getScale());
    assertEquals(7,numericType.getPrecision());
    
    
    NumberEnumerationFacet enumFacet = (NumberEnumerationFacet)numericType; 
    assertNotNull(enumFacet.getChoices());
    
    //== The range will be set
    if (enumFacet instanceof NumberRangeFacet)
    {
      NumberRangeFacet rangeType = (NumberRangeFacet) enumFacet;
      assertEquals(minInclusive,rangeType.getMinInclusive());
      assertEquals(maxInclusive,rangeType.getMaxInclusive());
    }
    
    
    assertEquals(false,enumFacet.validateChoice(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertEquals(false,enumFacet.validateChoice(BigDecimal.valueOf(0)));
    assertEquals(true,enumFacet.validateChoice(minInclusive));
    assertEquals(true,enumFacet.validateChoice(midValue));    
    assertEquals(true,enumFacet.validateChoice(maxInclusive));
    assertEquals(true,enumFacet.validateChoice(BigDecimal.valueOf(48000)));
    assertEquals(false,enumFacet.validateChoice(BigDecimal.valueOf(44100,2)));
    
    assertEquals(false,enumFacet.validateChoice(BigDecimal.valueOf(Integer.MAX_VALUE)));
    
    assertEquals(false,enumFacet.validateChoice(Integer.MIN_VALUE));
    assertEquals(false,enumFacet.validateChoice(0));
    assertEquals(true,enumFacet.validateChoice(minInclusive.intValue()));
    assertEquals(true,enumFacet.validateChoice(midValue.intValue()));    
    assertEquals(true,enumFacet.validateChoice(maxInclusive.intValue()));
    assertEquals(false,enumFacet.validateChoice(Integer.MAX_VALUE));
    
  }

  /*------------------ toValue functionality ----------------*/
  
  /** No restriction facet */
  public void testToValueStandard()
  {
    TypeCheckResult checkResult = new TypeCheckResult();
    
    AbstractNumberType numberType = new DecimalType(2);

    // Value within scale
    BigDecimal result = (BigDecimal) numberType.toValue(new Integer(44100), checkResult);
    assertEquals(2,result.scale());
    assertEquals(44100,result.longValue());
    assertEquals(null,checkResult.error);
    
    // Value within scale
    result = (BigDecimal) numberType.toValue(BigDecimal.valueOf(120,2), checkResult);
    assertEquals(2,result.scale());
    assertTrue(1.20==result.doubleValue());
    assertEquals(null,checkResult.error);
    
    // Value within scale
    result = (BigDecimal) numberType.toValue(new BigDecimal(1.5), checkResult);
    assertEquals(2,result.scale());
    assertEquals(1,result.longValue());
    assertEquals(null,checkResult.error);
    
    // Value within scale
    result = (BigDecimal) numberType.toValue(155, checkResult);
    assertEquals(2,result.scale());
    assertEquals(155,result.longValue());
    assertEquals(null,checkResult.error);
    
    
    // Loss of precision, value still valid but error indicating narrowing result
    result = (BigDecimal) numberType.toValue(BigDecimal.valueOf(44101,4), checkResult);
    assertEquals(2,result.scale());
    assertEquals(4,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    // Wrong datatype
    result = (BigDecimal) numberType.toValue(new byte[]{01,2,3}, checkResult);
    assertEquals(null,result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  /** Range restriction facet */
  public void testToValueBounded()
  {
    TypeCheckResult checkResult = new TypeCheckResult();
    
    BigDecimal minRange = new BigDecimal("-100.50");
    BigDecimal maxRange = new BigDecimal("+100.50");
        
    AbstractNumberType numberType = new DecimalType(minRange,maxRange);
    assertEquals(minRange,numberType.getMinInclusive());
    assertEquals(maxRange,numberType.getMaxInclusive());
    
    // Value within range
    BigDecimal result = (BigDecimal) numberType.toValue(new Integer(25), checkResult);
    assertEquals(2,result.scale());
    assertEquals(25,result.longValue());
    assertEquals(null,checkResult.error);
    
    // Value within range
    result = (BigDecimal) numberType.toValue(BigDecimal.valueOf(10000,2), checkResult);
    assertEquals(2,result.scale());
    assertEquals(100,result.longValue());
    assertEquals(null,checkResult.error);
    
    // Value within range
    result = (BigDecimal) numberType.toValue(new BigDecimal(1.5), checkResult);
    assertEquals(2,result.scale());
    assertEquals(1,result.longValue());
    assertEquals(null,checkResult.error);
    
    // Value within range
    result = (BigDecimal) numberType.toValue(new Double(-100.5), checkResult);
    assertEquals(2,result.scale());
    assertEquals(-100,result.longValue());
    assertEquals(null,checkResult.error);
    
    //******************* Values outside range ******************//
    
    result = (BigDecimal) numberType.toValue(new Integer(-512), checkResult);
    assertEquals(2,result.scale());
    assertEquals(minRange,result);
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    result = (BigDecimal) numberType.toValue(BigDecimal.valueOf(1000000,2), checkResult);
    assertEquals(2,result.scale());
    assertEquals(maxRange,result);
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    result = (BigDecimal) numberType.toValue(new BigDecimal(1000.5), checkResult);
    assertEquals(2,result.scale());
    assertEquals(maxRange,result);
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    result = (BigDecimal) numberType.toValue(new Double(-100.6), checkResult);
    assertEquals(2,result.scale());
    assertEquals(minRange,result);
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    
    // Wrong datatype
    result = (BigDecimal) numberType.toValue(new byte[]{01,2,3}, checkResult);
    assertEquals(null,result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  /** Choice restriction facet */
  public void testToValueChoices()
  {
    TypeCheckResult checkResult = new TypeCheckResult();
    
    BigDecimal minRange = new BigDecimal("-100.50");
    BigDecimal medianRange = new BigDecimal("0.00");
    BigDecimal maxRange = new BigDecimal("+100.50");
    
    BigDecimal choices[] = new BigDecimal[]{minRange,medianRange,maxRange};
        
    AbstractNumberType numberType = new DecimalType(choices);
    assertEquals(2,numberType.getScale());
    
    BigDecimal result;

    // Wrong choice value
    result = (BigDecimal) numberType.toValue(new Integer(-512), checkResult);
    assertEquals(null,result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    // Wrong datatype
    result = (BigDecimal) numberType.toValue(new byte[]{01,2,3}, checkResult);
    assertEquals(null,result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    // Correct value
    result = (BigDecimal) numberType.toValue(BigDecimal.valueOf(0,2), checkResult);
    assertEquals(medianRange,result);
    assertEquals(null,checkResult.error);
  }
  
  /*-------------------- equals/isRestrictionOf functionality ---------------*/
  public void testEqualsIsRestrictionOf()
  {
    BigDecimal minRange = new BigDecimal("-100.50");
    BigDecimal maxRange = new BigDecimal("+100.50");
    AbstractNumberType numberTypeRange = new DecimalType(minRange,maxRange); 
    AbstractNumberType numberTypeZeroScale = new DecimalType(0);
    AbstractNumberType numberTypeSmallScale = new DecimalType(2);
    AbstractNumberType numberTypeLargeScale = new DecimalType(6);
    AbstractNumberType numberTypeLargeScaleAlt = new DecimalType(6);
    BigDecimal medianRange = new BigDecimal("0.00");
    
    BigDecimal choices[] = new BigDecimal[]{minRange,medianRange,maxRange};
        
    AbstractNumberType numberTypeChoices = new DecimalType(choices);
    
    assertTrue(numberTypeRange.equals(numberTypeRange));
    assertTrue(numberTypeChoices.equals(numberTypeChoices));
    
    assertFalse(numberTypeRange.equals(numberTypeZeroScale));
    assertFalse(numberTypeRange.equals(numberTypeSmallScale));
    assertFalse(numberTypeRange.equals(numberTypeZeroScale));
    assertFalse(numberTypeRange.equals(numberTypeLargeScale));
    assertFalse(numberTypeRange.equals(numberTypeChoices));
    
    assertFalse(numberTypeZeroScale.equals(numberTypeSmallScale));
    assertFalse(numberTypeSmallScale.equals(numberTypeChoices));
    assertFalse(numberTypeLargeScale.equals(numberTypeSmallScale));
    

    assertFalse(numberTypeRange.isRestrictionOf(numberTypeRange));
    assertFalse(numberTypeChoices.isRestrictionOf(numberTypeChoices));
    
    // Scales are different
    assertTrue(numberTypeRange.isRestrictionOf(numberTypeZeroScale));
    assertFalse(numberTypeZeroScale.isRestrictionOf(numberTypeRange));
    
    assertTrue(numberTypeRange.isRestrictionOf(numberTypeLargeScale));
    assertFalse(numberTypeLargeScale.isRestrictionOf(numberTypeRange));
    
    assertTrue(numberTypeSmallScale.isRestrictionOf(numberTypeLargeScale));
    assertFalse(numberTypeLargeScale.isRestrictionOf(numberTypeSmallScale));
    
    assertFalse(numberTypeSmallScale.isRestrictionOf(numberTypeChoices));
    assertTrue(numberTypeChoices.isRestrictionOf(numberTypeSmallScale));
    
  }  
  
}
