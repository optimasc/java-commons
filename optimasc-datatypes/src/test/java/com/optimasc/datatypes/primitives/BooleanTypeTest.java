package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.DatatypeTest;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Calendar;

import com.optimasc.datatypes.BoundedFacet;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;

import junit.framework.TestCase;

/** BooleanType testing */
public class BooleanTypeTest extends DatatypeTest
{
  protected Datatype defaultInstance;

  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = (Datatype) BooleanType.getInstance().getType();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(Boolean.class, defaultInstance.getClassType());
    assertEquals(Boolean.class, BooleanType.DEFAULT_ORDERED_INSTANCE.getClassType());
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
    // Ordered version
    BooleanType instance = new BooleanType(true);
    assertEquals(true,instance.isOrdered());
    assertEquals(false,instance.isNumeric());
    
    if (instance instanceof BoundedFacet)
    {
      assertEquals(true,((BoundedFacet)instance).isBounded());
    }
    // non-ordered version
    
    instance = new BooleanType(false);
    assertEquals(false,instance.isOrdered());
    assertEquals(false,instance.isNumeric());
    
    if (instance instanceof BoundedFacet)
    {
      assertEquals(false,((BoundedFacet)instance).isBounded());
    }
  }
  
  
  public void testBooleanNotOrderedType()
  {
    boolean success = false;
    TypeCheckResult checkResult = new TypeCheckResult();
    BooleanType datatype = new BooleanType(false);
    BooleanType otherDatatype = new BooleanType(false);
    testBasicDataType(datatype);
    assertEquals(false, datatype.isOrdered());
    assertEquals(false, datatype.isNumeric());
    assertEquals(Boolean.class, datatype.getClassType());
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);

    
    assertEquals(Boolean.FALSE,datatype.toValue(Boolean.FALSE, checkResult));
    assertEquals(null,checkResult.error);
    
    assertEquals(null,datatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  public void testBooleanOrderedType()
  {
    boolean success = false;
    TypeCheckResult checkResult = new TypeCheckResult();
    BooleanType datatype = new BooleanType(true);
    BooleanType otherDatatype = new BooleanType(true);
    testBasicDataType(datatype);
    assertEquals(true, datatype.isOrdered());
    assertEquals(false, datatype.isNumeric());
    assertEquals(Boolean.class, datatype.getClassType());
    assertEquals(datatype, datatype);
    assertEquals(otherDatatype, datatype);
    

  }
  
  /*------------------ toValue functionality ----------------*/
  
  public void testToValueLongOrdered()
  {
    OrderedFacet datatype = new BooleanType(true);
    TypeCheckResult checkResult = new TypeCheckResult();

    assertEquals(Boolean.TRUE,datatype.toValue(Integer.MIN_VALUE, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(-55, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(0, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(Integer.MAX_VALUE, checkResult));
    assertEquals(null,checkResult.error);
    
  }
  
  public void testToValueObjectOrdered()
  {
    OrderedFacet datatype = new BooleanType(true);
    TypeCheckResult checkResult = new TypeCheckResult();

    // Boolean values -- valid
    assertEquals(Boolean.FALSE,datatype.toValue(Boolean.FALSE, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(Boolean.TRUE, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(new Boolean(true), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new Boolean(false), checkResult));
    assertEquals(null,checkResult.error);


    // Number values -- valid
    assertEquals(Boolean.TRUE,datatype.toValue(new Byte(Byte.MIN_VALUE), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new Byte((byte) 0), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(new Byte(Byte.MAX_VALUE), checkResult));
    assertEquals(null,checkResult.error);
    
    assertEquals(Boolean.TRUE,datatype.toValue(new Short(Short.MIN_VALUE), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new Short((byte) 0), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(new Short(Short.MAX_VALUE), checkResult));
    assertEquals(null,checkResult.error);
    
    assertEquals(Boolean.TRUE,datatype.toValue(new Integer(Integer.MIN_VALUE), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new Integer(0), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(new Integer(Integer.MAX_VALUE), checkResult));
    assertEquals(null,checkResult.error);
    
    assertEquals(Boolean.TRUE,datatype.toValue(new Long(Long.MIN_VALUE), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new Long(0), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(new Long(Long.MAX_VALUE), checkResult));
    assertEquals(null,checkResult.error);
    
    assertEquals(Boolean.TRUE,datatype.toValue(BigInteger.valueOf(12), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(BigInteger.valueOf(0), checkResult));
    assertEquals(null,checkResult.error);
    
    assertEquals(Boolean.FALSE,datatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(BigDecimal.valueOf(-55), checkResult));
    assertEquals(null,checkResult.error);

    assertEquals(Boolean.TRUE,datatype.toValue(new BigDecimal(Math.PI), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new BigDecimal(0.000), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(new Double(Math.PI), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new Double(0.0d), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(new Float(Math.PI), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new Float(0.0f), checkResult));
    assertEquals(null,checkResult.error);
    
    // Other object values -- invalid
    assertEquals(null,datatype.toValue(Calendar.getInstance(), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(Calendar.getInstance().getTime(), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  
  public void testToValueLongUnordered()
  {
    OrderedFacet datatype = new BooleanType(false);
    TypeCheckResult checkResult = new TypeCheckResult();
    
    assertEquals(null,datatype.toValue(Integer.MIN_VALUE, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(-55, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(0, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(Integer.MAX_VALUE, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  public void testToValueObjectUnordered()
  {
    OrderedFacet datatype = new BooleanType(false);
    TypeCheckResult checkResult = new TypeCheckResult();

    // Boolean values -- valid
    assertEquals(Boolean.FALSE,datatype.toValue(Boolean.FALSE, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(Boolean.TRUE, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.TRUE,datatype.toValue(new Boolean(true), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(Boolean.FALSE,datatype.toValue(new Boolean(false), checkResult));
    assertEquals(null,checkResult.error);


    // Number values -- invalid
    assertEquals(null,datatype.toValue(new Byte(Byte.MIN_VALUE), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Byte((byte) 0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Byte(Byte.MAX_VALUE), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(null,datatype.toValue(new Short(Short.MIN_VALUE), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Short((byte) 0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Short(Short.MAX_VALUE), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(null,datatype.toValue(new Integer(Integer.MIN_VALUE), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Integer(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Integer(Integer.MAX_VALUE), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(null,datatype.toValue(new Long(Long.MIN_VALUE), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Long(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Long(Long.MAX_VALUE), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(null,datatype.toValue(BigInteger.valueOf(12), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(BigInteger.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(null,datatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(BigDecimal.valueOf(-55), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,datatype.toValue(new BigDecimal(Math.PI), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new BigDecimal(0.000), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Double(Math.PI), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Double(0.0d), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Float(Math.PI), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(new Float(0.0f), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    // Other object values -- invalid
    assertEquals(null,datatype.toValue(Calendar.getInstance(), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null,datatype.toValue(Calendar.getInstance().getTime(), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  /*----------------------------- ranges --------------------------------*/
  public void testRange()
  {
    BooleanType ordered01 = new BooleanType(true);

    assertEquals(BigDecimal.valueOf(0),ordered01.getMinInclusive());
    assertEquals(BigDecimal.valueOf(1),ordered01.getMaxInclusive());
    
    assertEquals(false,ordered01.validateRange(34));
    assertEquals(false,ordered01.validateRange(-1));
    assertEquals(true,ordered01.validateRange(0));
    assertEquals(true,ordered01.validateRange(1));

    assertEquals(false,ordered01.validateRange(BigDecimal.valueOf(34)));
    assertEquals(false,ordered01.validateRange(BigDecimal.valueOf(-1)));
    assertEquals(true,ordered01.validateRange(BigDecimal.valueOf(0)));
    assertEquals(true,ordered01.validateRange(BigDecimal.valueOf(1)));
    
  }
  
  
  
  /*-------------------- equals/restriction functionality ---------------*/
  public void testEquals()
  {
    BooleanType ordered01 = new BooleanType(true);
    BooleanType ordered02 = new BooleanType(true);
    BooleanType unordered01 = new BooleanType(false);
    
    assertTrue(ordered01.equals(ordered02));
    assertTrue(ordered01.equals(ordered01));
    assertFalse(ordered01.equals(unordered01));
    assertFalse(new IntegralType().equals(ordered01));
    assertFalse(new IntegralType().equals(unordered01));
    
    assertFalse(ordered01.isRestrictionOf(unordered01));
    

    boolean success = false;
    try
    {
      assertFalse(ordered01.isRestrictionOf(new CharacterType()));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
  }  


}
