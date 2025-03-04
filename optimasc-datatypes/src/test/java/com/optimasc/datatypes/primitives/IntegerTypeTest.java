package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.optimasc.datatypes.BoundedFacet;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.NumberEnumerationFacet;
import com.optimasc.datatypes.NumberRangeFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;

import junit.framework.TestCase;

/** Test for primitive integer type. */
public class IntegerTypeTest extends AbstractNumberTest
{
  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = (AbstractNumberType) IntegralType.getInstance().getType();  
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(BigInteger.class,defaultInstance.getClassType());
  }
  
  
  /*----------------------- Range facet functionality ---------------------*/
  /** Test unbounded value. */
  public void testRangeUnBounded()
  {
    AbstractNumberType numericType = defaultInstance;
    
    // Check first non-bounded value
    assertEquals(0,numericType.getScale());
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
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Long.MAX_VALUE)));
    
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Integer.MAX_VALUE)));

    // Exceptions, this should fail, as this is decimal values for an integer type!
    assertEquals(false,rangedType.validateRange(new BigDecimal(Float.MIN_VALUE)));
    assertEquals(false,rangedType.validateRange(new BigDecimal(0.55)));
    assertEquals(false,rangedType.validateRange(new BigDecimal(423423.564)));
  }
  
  
  

  /** Test bounded value. */
  public void testRangeBounded()
  {
    AbstractNumberType numericType = new IntegralType(Integer.MIN_VALUE,Integer.MAX_VALUE);
    
    assertEquals(0,numericType.getScale());
    // This value is consisten with industry values (IBM/Microsoft)
    assertEquals(10,numericType.getPrecision());
    NumberRangeFacet rangedType = numericType;
    
    assertEquals(true,rangedType.isBounded());
    assertEquals(BigInteger.valueOf(Integer.MIN_VALUE).longValue(),rangedType.getMinInclusive().longValue());
    assertEquals(BigInteger.valueOf(Integer.MAX_VALUE).longValue(),rangedType.getMaxInclusive().longValue());
    
    // Verify that choices are null
    if (numericType instanceof NumberEnumerationFacet)
    {
      NumberEnumerationFacet enumFacet = numericType;
      assertEquals(null,enumFacet.getChoices());
    }
    
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MAX_VALUE)));
    
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Integer.MAX_VALUE)));

    // Exceptions, this should fail, as this is decimal values for an integer type!
    assertEquals(false,rangedType.validateRange(new BigDecimal(-323515.624645)));
    assertEquals(false,rangedType.validateRange(new BigDecimal(0.55)));
    assertEquals(false,rangedType.validateRange(new BigDecimal(Float.MAX_VALUE)));
  }
  
  /** Test bounded value where lower bound is equal to upper bound. */
  public void testRangeEqual()
  {
    AbstractNumberType numericType = new IntegralType(0,0);
    assertEquals(0,numericType.getScale());
    assertEquals(1,numericType.getPrecision());
    
    NumberRangeFacet rangedType = numericType;
    
    assertEquals(true,rangedType.isBounded());
    assertEquals(BigDecimal.valueOf(0).longValue(),rangedType.getMinInclusive().longValue());
    assertEquals(BigDecimal.valueOf(0).longValue(),rangedType.getMaxInclusive().longValue());
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MAX_VALUE)));
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Integer.MAX_VALUE)));

    // Exceptions, this should fail, as this is decimal values for an integer type!
    assertEquals(false,rangedType.validateRange(new BigDecimal(Float.MIN_VALUE)));
    assertEquals(false,rangedType.validateRange(new BigDecimal(0.55)));
    assertEquals(false,rangedType.validateRange(new BigDecimal(Float.MAX_VALUE)));
  }
  

  /** Verify if the lower bound is bigger than upper bound. */
  public void testRangeIllegalBounds()
  {
    boolean success = false;
    
    try
    {
      // Check first non-bounded value
      NumberRangeFacet rangedType = new IntegralType(Integer.MAX_VALUE,Integer.MIN_VALUE);
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
    
    assertEquals(0,numericType.getScale());
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
    
    
    assertEquals(true,enumFacet.validateChoice(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertEquals(true,enumFacet.validateChoice(BigDecimal.valueOf(0)));
    assertEquals(true,enumFacet.validateChoice(BigDecimal.valueOf(Integer.MAX_VALUE)));
    
    assertEquals(true,enumFacet.validateChoice(Integer.MIN_VALUE));
    assertEquals(true,enumFacet.validateChoice(0));
    assertEquals(true,enumFacet.validateChoice(Integer.MAX_VALUE));
    
  }
  
  
  /** Test when choices are invalid. For an integer type,
   *  the choices cannot contain fractional digits. */
  public void testChoicesInvalid()
  {
    boolean success = false;
    
    try
    {
      BigDecimal choices[] = new BigDecimal[] {BigDecimal.valueOf(0),new BigDecimal(0.5)};
      // Check first non-bounded value
      NumberEnumerationFacet enumFacet = new IntegralType();
      enumFacet.setChoices(choices);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
  }
  
  /** No limitation through enumeration, all values are allowed. */
  public void testValidateChoices()
  {
    long choices[] = {8000,11025,22050,44100,48000};
    BigDecimal minInclusive = BigDecimal.valueOf(choices[0]);
    BigDecimal maxInclusive = BigDecimal.valueOf(choices[4]);
    BigDecimal midValue = BigDecimal.valueOf(choices[2]);
    
    AbstractNumberType numericType = new IntegralType(choices);
    assertEquals(0,numericType.getScale());
    assertEquals(5,numericType.getPrecision());
    
    
    NumberEnumerationFacet enumFacet = (NumberEnumerationFacet)numericType; 
    assertNotNull(enumFacet.getChoices());
    
    //== The range will be set
    if (enumFacet instanceof NumberRangeFacet)
    {
      NumberRangeFacet rangeType = (NumberRangeFacet) enumFacet;
      assertEquals(minInclusive.longValue(),rangeType.getMinInclusive().longValue());
      assertEquals(maxInclusive.longValue(),rangeType.getMaxInclusive().longValue());
    }
    
    
    assertEquals(false,enumFacet.validateChoice(BigDecimal.valueOf(Integer.MIN_VALUE)));
    assertEquals(false,enumFacet.validateChoice(BigDecimal.valueOf(0)));
    assertEquals(true,enumFacet.validateChoice(minInclusive));
    assertEquals(true,enumFacet.validateChoice(midValue));    
    assertEquals(true,enumFacet.validateChoice(maxInclusive));
    assertEquals(false,enumFacet.validateChoice(BigDecimal.valueOf(Integer.MAX_VALUE)));
    
    assertEquals(false,enumFacet.validateChoice(Integer.MIN_VALUE));
    assertEquals(false,enumFacet.validateChoice(0));
    assertEquals(true,enumFacet.validateChoice(minInclusive.intValue()));
    assertEquals(true,enumFacet.validateChoice(midValue.intValue()));    
    assertEquals(true,enumFacet.validateChoice(maxInclusive.intValue()));
    assertEquals(false,enumFacet.validateChoice(Integer.MAX_VALUE));
    
  }
  
  /*------------------ toValue functionality ----------------*/

  public void testToValueLongUnBounded()
  {
    AbstractNumberType numberType = defaultInstance;
    BigInteger result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(0, checkResult);
    assertEquals(0,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(Long.MIN_VALUE, checkResult);
    assertEquals(Long.MIN_VALUE,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(Long.MAX_VALUE, checkResult);
    assertEquals(Long.MAX_VALUE,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(255, checkResult);
    assertEquals(255,result.longValue());
    assertEquals(null,checkResult.error);
  }
  
  public void testToValueLongBounded()
  {
    AbstractNumberType numberType = new IntegralType(-255,255);
    BigInteger result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(0, checkResult);
    assertEquals(0,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(Long.MIN_VALUE, checkResult);
    assertEquals(null,result);
    assertNotNull(checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(Long.MAX_VALUE, checkResult);
    assertEquals(null,result);
    assertNotNull(checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(255, checkResult);
    assertEquals(255,result.longValue());
    assertEquals(null,checkResult.error);
  }
  
  public void testToValueLongChoices()
  {
    long choices[] = {-800,8000,11025,22050,44100,48000};
    
    AbstractNumberType numberType = new IntegralType(choices);
    BigInteger result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    checkResult.reset();
    
    result = (BigInteger) numberType.toValue(0, checkResult);
    assertEquals(null,result);
    assertNotNull(checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(44100, checkResult);
    assertEquals(44100,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(Long.MAX_VALUE, checkResult);
    assertEquals(null,result);
    assertNotNull(checkResult.error);
    
    checkResult.reset();
    result = (BigInteger) numberType.toValue(-800, checkResult);
    assertEquals(-800,result.longValue());
    assertEquals(null,checkResult.error);
  }
  
  
  public void testToValueObjectUnBounded()
  {
    AbstractNumberType numberType = defaultInstance;
    BigInteger result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    result = (BigInteger) numberType.toValue(BigDecimal.valueOf(0), checkResult);
    assertEquals(0,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(BigDecimal.valueOf(Long.MIN_VALUE), checkResult);
    assertEquals(Long.MIN_VALUE,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Long(Long.MAX_VALUE), checkResult);
    assertEquals(Long.MAX_VALUE,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Integer(255), checkResult);
    assertEquals(255,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Short(Short.MIN_VALUE), checkResult);
    assertEquals(Short.MIN_VALUE,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Short(Byte.MAX_VALUE), checkResult);
    assertEquals(Byte.MAX_VALUE,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(BigInteger.ONE, checkResult);
    assertEquals(1,result.longValue());
    assertEquals(null,checkResult.error);
    
    // These should fail, as these values are Real types or scaled values.
    Double singleValue = new Double(343455.235253);
    result = (BigInteger) numberType.toValue(singleValue, checkResult);
    assertEquals(343455,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    
    BigDecimal decimalValue = new BigDecimal(0.555);
    result = (BigInteger) numberType.toValue(decimalValue, checkResult);
    assertEquals(0,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    Float floatValue = new Float(15.4);
    result = (BigInteger) numberType.toValue(floatValue, checkResult);
    assertEquals(15,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  public void testToValueBoundedByteSigned()
  {
    AbstractNumberType numberType = new IntegralType(Byte.MIN_VALUE,Byte.MAX_VALUE);
    BigInteger result;
    
    TypeCheckResult checkResult = new TypeCheckResult();

    // Within bounds
    result = (BigInteger) numberType.toValue(new Byte((byte) -127), checkResult);
    assertEquals(-127,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Short((short) 0), checkResult);
    assertEquals(0,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Integer(127), checkResult);
    assertEquals(127,result.longValue());
    assertEquals(null,checkResult.error);

    // Out of bounds
    result = (BigInteger) numberType.toValue(new Long(254), checkResult);
    assertEquals(127,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  public void testToValueBoundedByteUnsigned()
  {
    AbstractNumberType numberType = new IntegralType(0,255);
    BigInteger result;
    
    TypeCheckResult checkResult = new TypeCheckResult();

    // Within bounds
    result = (BigInteger) numberType.toValue(new Byte((byte) 0xFF), checkResult);
    assertEquals(255,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Short((short) 244), checkResult);
    assertEquals(244,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Integer(127), checkResult);
    assertEquals(127,result.longValue());
    assertEquals(null,checkResult.error);

    // Out of bounds
    result = (BigInteger) numberType.toValue(new Long(-254), checkResult);
    assertEquals(0,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
  }
  
  
  
  public void testToValueObjectBounded()
  {
    AbstractNumberType numberType = new IntegralType(-255,255);
    BigInteger result;
    
    TypeCheckResult checkResult = new TypeCheckResult();

    // Within bounds
    result = (BigInteger) numberType.toValue(new Byte((byte) -127), checkResult);
    assertEquals(-127,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Short((short) 0), checkResult);
    assertEquals(0,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Integer(127), checkResult);
    assertEquals(127,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Long(254), checkResult);
    assertEquals(254,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(BigInteger.valueOf(254), checkResult);
    assertEquals(254,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(BigDecimal.valueOf(255), checkResult);
    assertEquals(255,result.longValue());
    assertEquals(null,checkResult.error);
    

    // Out of bounds value -- but conversion within min and max range
    
    result = (BigInteger) numberType.toValue(new Short(Short.MIN_VALUE), checkResult);
    assertEquals(-255,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());

    result = (BigInteger) numberType.toValue(new Short(Short.MAX_VALUE), checkResult);
    assertEquals(255,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    result = (BigInteger) numberType.toValue(new Integer(Integer.MIN_VALUE), checkResult);
    assertEquals(-255,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());

    result = (BigInteger) numberType.toValue(new Integer(Integer.MAX_VALUE), checkResult);
    assertEquals(255,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    result = (BigInteger) numberType.toValue(new Long(Long.MIN_VALUE), checkResult);
    assertEquals(-255,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());

    result = (BigInteger) numberType.toValue(new Long(Long.MAX_VALUE), checkResult);
    assertEquals(+255,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
  }
  
  public void testToValueObjectChoices()
  {
    long choices[] = {-800,8000,11025,22050,44100,48000};
    
    AbstractNumberType numberType = new IntegralType(choices);
    BigInteger result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    result = (BigInteger) numberType.toValue(new Byte((byte) 0), checkResult);
    assertEquals(null,result);
    assertNotNull(checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Integer(44100), checkResult);
    assertEquals(44100,result.longValue());
    assertEquals(null,checkResult.error);
    
    result = (BigInteger) numberType.toValue(new Long(Long.MAX_VALUE), checkResult);
    assertEquals(null,result);
    assertNotNull(checkResult.error);
    
    result = (BigInteger) numberType.toValue(BigDecimal.valueOf(-800), checkResult);
    assertEquals(-800,result.longValue());
    assertEquals(null,checkResult.error);
  }

  
  
  /*-------------------- equals/isRestrictionOf functionality ---------------*/
  public void testEqualsIsRestrictionOf()
  {
    assertTrue(defaultInstance.equals(defaultInstance));
    assertFalse(defaultInstance.isRestrictionOf(defaultInstance));
    assertTrue(defaultInstance.equals(new IntegralType()));
    assertFalse(defaultInstance.isRestrictionOf(new IntegralType()));
    
    // Create some bounded values
    AbstractNumberType octetType = new IntegralType(0,255);
    AbstractNumberType byteType = new IntegralType(Byte.MIN_VALUE,Byte.MAX_VALUE);
    AbstractNumberType intType = new IntegralType(Integer.MIN_VALUE,Integer.MAX_VALUE);

    long sampleRates[] = {8000,11025,22050,44100,48000};

    AbstractNumberType samplingRateType = new IntegralType(sampleRates);
    AbstractNumberType fakeSamplingRateType = new IntegralType(8000,48000);
    
    /*----------------- equals --------------*/
    
    // Check for type identity
    assertFalse(octetType.equals(byteType));
    assertFalse(octetType.equals(intType));
    assertFalse(octetType.equals(samplingRateType));
    // This should not be equal, one has an explicit enumeration, and the
    // other one an explicit range.
    assertFalse(fakeSamplingRateType.equals(samplingRateType));
    
    
    /*------------------ subset --------------*/

    assertFalse(octetType.isRestrictionOf(byteType));
    assertFalse(byteType.isRestrictionOf(octetType));

    // octet has smaller range than int
    assertTrue(octetType.isRestrictionOf(intType));
    assertFalse(intType.isRestrictionOf(octetType));
    // byte has smaller range than int
    assertTrue(byteType.isRestrictionOf(intType));
    assertFalse(intType.isRestrictionOf(byteType));
    
    // This is one is tricky
    assertFalse(fakeSamplingRateType.isRestrictionOf(samplingRateType));
    assertTrue(samplingRateType.isRestrictionOf(fakeSamplingRateType));
    
    boolean success = false;
    try
    {
      assertFalse(intType.isRestrictionOf(new BooleanType()));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
  }  


}
