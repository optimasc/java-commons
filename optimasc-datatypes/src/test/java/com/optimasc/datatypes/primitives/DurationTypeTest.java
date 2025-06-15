package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.GregorianCalendar;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DatatypeTest;
import com.optimasc.datatypes.NumberEnumerationFacet;
import com.optimasc.datatypes.NumberRangeFacet;
import com.optimasc.datatypes.TypeFactory;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.date.DateTimeFormat;

import junit.framework.TestCase;

public class DurationTypeTest extends DatatypeTest
{
  DurationType defaultInstance;

  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = (DurationType) TypeFactory.getDefaultInstance(DurationType.class).getType();
    
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  
  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(Long.class, defaultInstance.getClassType());
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
    DurationType instance = defaultInstance;
    assertEquals(true,instance.isOrdered());
    assertEquals(true,instance.isNumeric());
    // Only natural numbers are allowed
    assertEquals(true,instance.isBounded());
  }
  
  /*----------------------- Time unit functionality ---------------------*/
  public void testValidTimeUnit()
  {
    DurationType datatype = defaultInstance;
    // Check default instance value.
    assertEquals(DateTimeFormat.TimeUnit.MILLISECONDS,datatype.getAccuracy());
    
    datatype = new DurationType(DateTimeFormat.TimeUnit.SECONDS);
    assertEquals(DateTimeFormat.TimeUnit.SECONDS,datatype.getAccuracy());

    datatype = new DurationType(DateTimeFormat.TimeUnit.DAYS);
    assertEquals(DateTimeFormat.TimeUnit.DAYS,datatype.getAccuracy());
    
  }
  
  public void testInvalidTimeUnit()
  {
    DurationType datatype;
    boolean success = false;
    
    try
    {
       datatype = new DurationType(-1);
    }
    catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
    success = false;
    try
    {
       datatype = new DurationType(GregorianCalendar.YEAR);
    }
    catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
  }
  
  
  
  /*----------------------- Range facet functionality ---------------------*/
  /** Test default bound values. */
  public void testRangeDefaultBounds()
  {
    
    // Check first non-bounded value
    NumberRangeFacet rangedType = (NumberRangeFacet) defaultInstance;
    
    assertEquals(true,rangedType.isBounded());
    
    assertEquals(BigDecimal.valueOf(0).longValue(),rangedType.getMinInclusive().longValue());
    assertEquals(null,rangedType.getMaxInclusive());
    
    // Verify that choices are null
    if (rangedType instanceof NumberEnumerationFacet)
    {
      NumberEnumerationFacet enumFacet = (NumberEnumerationFacet) rangedType;
      assertEquals(null,enumFacet.getChoices());
    }
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(Long.MAX_VALUE)));
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Integer.MIN_VALUE)));
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
    DurationType numericType = new DurationType(DateTimeFormat.TimeUnit.MILLISECONDS,Integer.MAX_VALUE);
    
    NumberRangeFacet rangedType = numericType;
    
    assertEquals(true,rangedType.isBounded());
    assertEquals(BigDecimal.valueOf(0).longValue(),rangedType.getMinInclusive().longValue());
    assertEquals(BigDecimal.valueOf(Integer.MAX_VALUE).longValue(),rangedType.getMaxInclusive().longValue());

    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MIN_VALUE)));
    assertEquals(true,rangedType.validateRange(BigDecimal.valueOf(0)));
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Long.MAX_VALUE)));
    
    assertEquals(false,rangedType.validateRange(BigDecimal.valueOf(Integer.MIN_VALUE)));
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
    DurationType numericType = new DurationType(DateTimeFormat.TimeUnit.MILLISECONDS,0);
    
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
      DurationType rangedType = new DurationType(Integer.MIN_VALUE);
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
    DurationType datatype = defaultInstance;
    
    assertEquals(null,datatype.getChoices());
    
    
    // TODO:  Should this be allowed.
    assertEquals(true,datatype.validateChoice(Integer.MIN_VALUE));
    
    
    assertEquals(true,datatype.validateChoice(0));
    assertEquals(true,datatype.validateChoice(Integer.MAX_VALUE));
    
  }
  
  
  /** Test when choices are invalid. For duration type,
   *  the choices cannot contain negative values. */
  public void testChoicesInvalid()
  {
    boolean success = false;
    
    try
    {
      long choices[] = new long[] {12,-35,0};
      DurationType enumFacet = new DurationType(DateTimeFormat.TimeUnit.DAYS,choices);
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
  }
  
  
  public void testValidateChoices()
  {
    long choices[] = {8000,11025,22050,44100,48000};
    BigDecimal minInclusive = BigDecimal.valueOf(choices[0]);
    BigDecimal maxInclusive = BigDecimal.valueOf(choices[4]);
    BigDecimal midValue = BigDecimal.valueOf(choices[2]);
    
    DurationType datatype = new DurationType(DateTimeFormat.TimeUnit.MILLISECONDS,choices);
    
    
    assertNotNull(datatype.getChoices());
    
    //== The range will be set
      assertEquals(minInclusive.longValue(),datatype.getMinInclusive().longValue());
      assertEquals(maxInclusive.longValue(),datatype.getMaxInclusive().longValue());
    
    
    assertEquals(false,datatype.validateChoice(Integer.MIN_VALUE));
    
    assertEquals(false,datatype.validateChoice(Integer.MIN_VALUE));
    assertEquals(false,datatype.validateChoice(0));
    assertEquals(true,datatype.validateChoice(minInclusive.intValue()));
    assertEquals(true,datatype.validateChoice(midValue.intValue()));    
    assertEquals(true,datatype.validateChoice(maxInclusive.intValue()));
    assertEquals(false,datatype.validateChoice(Integer.MAX_VALUE));
    
  }
  
  /*------------------ toValue functionality ----------------*/

  public void testToValueLongUnBounded()
  {
    DurationType datatype = defaultInstance;
    Long result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    checkResult.reset();
    result = (Long) datatype.toValue(0, checkResult);
    assertEquals(0,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (Long) datatype.toValue(Long.MIN_VALUE, checkResult);
    assertEquals(0,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    checkResult.reset();
    result = (Long) datatype.toValue(Long.MAX_VALUE, checkResult);
    assertEquals(Long.MAX_VALUE,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (Long) datatype.toValue(255, checkResult);
    assertEquals(255,result.longValue());
    assertEquals(null,checkResult.error);
  }
  
  public void testToValueLongBounded()
  {
    DurationType numberType = new DurationType(DateTimeFormat.TimeUnit.DAYS,255);
    Long result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    checkResult.reset();
    result = (Long) numberType.toValue(0, checkResult);
    assertEquals(0,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (Long) numberType.toValue(Long.MIN_VALUE, checkResult);
    assertEquals(0,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    checkResult.reset();
    result = (Long) numberType.toValue(Long.MAX_VALUE, checkResult);
    assertEquals(255,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    checkResult.reset();
    result = (Long) numberType.toValue(255, checkResult);
    assertEquals(255,result.longValue());
    assertEquals(null,checkResult.error);
  }
  
  public void testToValueLongChoices()
  {
    long choices[] = {800,8000,11025,22050,44100,48000};
    
    DurationType dataType = new DurationType(DateTimeFormat.TimeUnit.SECONDS,choices);
    Long result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    checkResult.reset();
    
    result = (Long) dataType.toValue(0, checkResult);
    assertEquals(null,result);
    assertNotNull(checkResult.error);
    
    checkResult.reset();
    result = (Long) dataType.toValue(44100, checkResult);
    assertEquals(44100,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (Long) dataType.toValue(Long.MAX_VALUE, checkResult);
    assertEquals(null,result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    checkResult.reset();
    result = (Long) dataType.toValue(-800, checkResult);
    assertEquals(null,result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
  }
  
  
  public void testToValueObjectBounded()
  {
    DurationType dataType = new DurationType(DateTimeFormat.TimeUnit.DAYS,255);
    Long result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    checkResult.reset();
    result = (Long) dataType.toValue(BigDecimal.valueOf(0), checkResult);
    assertEquals(0,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (Long) dataType.toValue(BigInteger.valueOf(Long.MIN_VALUE), checkResult);
    assertEquals(0,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    checkResult.reset();
    result = (Long) dataType.toValue(new Long(Long.MAX_VALUE), checkResult);
    assertEquals(255,result.longValue());
    assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)checkResult.error).getCode());
    
    checkResult.reset();
    result = (Long) dataType.toValue(new Short((short) 255), checkResult);
    assertEquals(255,result.longValue());
    assertEquals(null,checkResult.error);
  }
  
  public void testToValueObjectChoices()
  {
    long choices[] = {800,8000,11025,22050,44100,48000};
    
    DurationType numberType = new DurationType(DateTimeFormat.TimeUnit.SECONDS,choices);
    Long result;
    
    TypeCheckResult checkResult = new TypeCheckResult();
    
    checkResult.reset();
    
    result = (Long) numberType.toValue(BigInteger.valueOf(0), checkResult);
    assertEquals(null,result);
    assertNotNull(checkResult.error);
    
    checkResult.reset();
    result = (Long) numberType.toValue(BigDecimal.valueOf(44100), checkResult);
    assertEquals(44100,result.longValue());
    assertEquals(null,checkResult.error);
    
    checkResult.reset();
    result = (Long) numberType.toValue(new Integer(44100), checkResult);
    assertEquals(44100,result.longValue());
    assertEquals(null,checkResult.error);
    
    
    checkResult.reset();
    result = (Long) numberType.toValue(new Long(Long.MAX_VALUE), checkResult);
    assertEquals(null,result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    checkResult.reset();
    result = (Long) numberType.toValue(new Integer(-800), checkResult);
    assertEquals(null,result);
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
  }
  
  /*-------------------- equals/isRestrictionOf functionality ---------------*/
//!!!  
  public void testEqualsRestriction()
  {
    long choices[] = {800,8000,11025,22050,44100,48000};
    DurationType datatypeChoice = new DurationType(DateTimeFormat.TimeUnit.SECONDS,choices);
    DurationType datatypeChoice2 = new DurationType(DateTimeFormat.TimeUnit.MILLISECONDS,choices);
    
    DurationType datatypeDaysUnit = new DurationType(DateTimeFormat.TimeUnit.DAYS);
    DurationType datatypeOtherDaysUnit = new DurationType(DateTimeFormat.TimeUnit.DAYS);
    DurationType datatypeSecondsUnit = new DurationType(DateTimeFormat.TimeUnit.SECONDS);
    DurationType datatypeMillisecondsUnit = new DurationType(DateTimeFormat.TimeUnit.MILLISECONDS);

    DurationType datatypeRange = new DurationType(DateTimeFormat.TimeUnit.SECONDS,Integer.MAX_VALUE);
    
    assertFalse(datatypeChoice.equals(datatypeChoice2));
    assertFalse(datatypeChoice.equals(datatypeDaysUnit));
    assertFalse(datatypeChoice.equals(datatypeSecondsUnit));
    assertFalse(datatypeChoice.equals(datatypeMillisecondsUnit));
    assertFalse(datatypeChoice.equals(datatypeRange));
    assertFalse(datatypeSecondsUnit.equals(datatypeMillisecondsUnit));
    
    assertTrue(datatypeOtherDaysUnit.equals(datatypeDaysUnit));
    assertTrue(datatypeRange.equals(datatypeRange));
    
    // Restrictions
    assertFalse(datatypeRange.isRestrictionOf(datatypeRange));
    assertFalse(datatypeChoice.isRestrictionOf(datatypeChoice));
    
    // Units
    assertFalse(datatypeMillisecondsUnit.isRestrictionOf(datatypeChoice));
    assertFalse(datatypeMillisecondsUnit.isRestrictionOf(datatypeDaysUnit));
    assertFalse(datatypeMillisecondsUnit.isRestrictionOf(datatypeSecondsUnit));
    
    assertTrue(datatypeDaysUnit.isRestrictionOf(datatypeMillisecondsUnit));
    assertTrue(datatypeSecondsUnit.isRestrictionOf(datatypeMillisecondsUnit));

    assertTrue(datatypeChoice.isRestrictionOf(datatypeChoice2));
    assertFalse(datatypeChoice2.isRestrictionOf(datatypeChoice));
    
    
    // Ranges
    assertTrue(datatypeRange.isRestrictionOf(datatypeSecondsUnit));
    assertFalse(datatypeSecondsUnit.isRestrictionOf(datatypeRange));
    
    assertTrue(datatypeChoice.isRestrictionOf(datatypeRange));
    assertFalse(datatypeRange.isRestrictionOf(datatypeChoice));
    
    
  }  
}
