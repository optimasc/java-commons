package com.optimasc.datatypes.defined;

import java.math.BigDecimal;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DatatypeTest;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.BooleanType;

public class BinaryTypeTest extends DatatypeTest
{
  protected BinaryType defaultInstance;
  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = new BinaryType();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(byte[].class, defaultInstance.getClassType());
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
    BinaryType instance = defaultInstance;
    assertEquals(false,instance.isOrdered());
    assertEquals(false,instance.isNumeric());
    assertEquals(true,instance.isBounded());
    assertEquals(null,instance.getChoices());
  }
  
  /*------------------ length/size functionality ----------------*/
   public void testLengthUnbounded()
   {
     BinaryType datatype = new BinaryType();
     
     byte[] emptyArray = new byte[0];
     byte[] smallArray = new byte[]{0,1,2,3,4,5,6};

     // No length/size limitations
     assertEquals(0,datatype.getMinLength());
     assertEquals(Integer.MIN_VALUE,datatype.getMaxLength());
     
   }
   
   public void testLengthInvalid()
   {
     BinaryType datatype= null;
     
     boolean success;
     
     success = false;
     try
     {
       datatype = new BinaryType(-1,255);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);
     
     success = false;
     try
     {
       datatype = new BinaryType(32,1);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);
     
     success = false;
     try
     {
       datatype = new BinaryType(-34,-2);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);
     
     success = false;
     try
     {
       datatype = new BinaryType(0,-2);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);

     // Minimum value only unbounded is not possible
/*     success = false;
     try
     {
       datatype = new BinaryType(Integer.MIN_VALUE,255);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);*/
     
   }
   

   public void testLengthBounded()
   {
     BinaryType datatype = new BinaryType(1,Integer.MAX_VALUE);
     

     // No length/size limitations
     assertEquals(1,datatype.getMinLength());
     assertEquals(Integer.MAX_VALUE,datatype.getMaxLength());
     
   }
   
  /*------------------  choice functionality ----------------*/
  
  
  /*------------------ valueOf functionality ----------------*/
  
  public void testToValueObjectLengthsBoth()
  {
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Bounded data type with a minimum length of 0 elements and
    // a maximum of 2 elements.
    BinaryType boundedDatatype = new BinaryType(0,2);
    
    // Invalid object class
    assertEquals(null,boundedDatatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Valid object class but invalid length.
    assertEquals(null,boundedDatatype.toValue(new byte[]{1,2,3}, checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());

    byte[] emptyArray = new byte[0];
    assertEquals(emptyArray,boundedDatatype.toValue(emptyArray, checkResult));
    assertEquals(null,checkResult.error);

    byte[] oneArray = new byte[]{0,1,2};
    assertEquals(null,boundedDatatype.toValue(oneArray, checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());
    
    byte[] secondArray = new byte[]{0,1};
    assertEquals(secondArray,boundedDatatype.toValue(secondArray, checkResult));
    assertEquals(null,checkResult.error);
  }
  
  
  public void testToValueObjectLengthsEqual()
  {
    TypeCheckResult checkResult = new TypeCheckResult();

    byte[] emptyArray = new byte[0];
    byte[] twoElementArray = new byte[]{7,8};
    
    BinaryType boundedDatatype = new BinaryType(2,2);
    
    // Invalid object class
    assertEquals(null,boundedDatatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Valid object class but invalid length.
    assertEquals(null,boundedDatatype.toValue(new byte[]{1,2,3}, checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,boundedDatatype.toValue(emptyArray, checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());

    // Within length constraints
    assertEquals(twoElementArray,boundedDatatype.toValue(twoElementArray, checkResult));
    assertEquals(null,checkResult.error);
  }
  

  /** The minimum length is 1 and the maximum length is not-bounded. */
  public void testToValueObjectLengthsMaxLengthUnbounded()
  {
    TypeCheckResult checkResult = new TypeCheckResult();

    byte[] emptyArray = new byte[0];
    byte[] smallArray = new byte[]{0,1,2,3,4,5,6};
    byte[] twoElementArray = new byte[]{7,8};
    
    BinaryType boundedDatatype = new BinaryType(1,Integer.MIN_VALUE);
    
    // Invalid object class
    assertEquals(null,boundedDatatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Not of the minimum length value
    assertEquals(null,boundedDatatype.toValue(emptyArray, checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());

    assertEquals(smallArray,boundedDatatype.toValue(smallArray, checkResult));
    assertEquals(null,checkResult.error);
    
    assertEquals(twoElementArray,boundedDatatype.toValue(twoElementArray, checkResult));
    assertEquals(null,checkResult.error);
  }
  
  /*-------------------- equals/restriction functionality ---------------*/
  
  
  public void testEqualsRestriction()
  {
    TypeCheckResult checkResult = new TypeCheckResult();

    byte[] emptyArray = new byte[0];
    byte[] smallArray = new byte[]{0,1,2,3,4,5,6};
    byte[] twoElementArray = new byte[]{7,8};
    byte[] mediumArray = new byte[]{01,2,3,4,5,6,7,8,9,10,11,12,13,14,15};
   
    byte[] choices[] = new byte[][]{twoElementArray,mediumArray};
    
    BinaryType choiceDatatype = new BinaryType(choices);
    BinaryType smallerChoiceDatatype = new BinaryType(new byte[][]{emptyArray});
    
    BinaryType lengthBoundedDatatype = new BinaryType(0,32);
    BinaryType otherLengthBoundedDatatype = new BinaryType(0,32);
    BinaryType unboundedDatatype = new BinaryType();
    
    assertTrue(unboundedDatatype.equals(unboundedDatatype));
    assertFalse(unboundedDatatype.equals(lengthBoundedDatatype));
    assertFalse(choiceDatatype.equals(lengthBoundedDatatype));
    assertTrue(lengthBoundedDatatype.equals(otherLengthBoundedDatatype));
    assertFalse(choiceDatatype.equals(smallerChoiceDatatype));

    
    assertFalse(lengthBoundedDatatype.isRestrictionOf(otherLengthBoundedDatatype));
    assertTrue(lengthBoundedDatatype.isRestrictionOf(unboundedDatatype));
    assertTrue(choiceDatatype.isRestrictionOf(unboundedDatatype));
    assertTrue(choiceDatatype.isRestrictionOf(lengthBoundedDatatype));
    assertTrue(choiceDatatype.isRestrictionOf(smallerChoiceDatatype));
    assertTrue(smallerChoiceDatatype.isRestrictionOf(choiceDatatype));
    
    boolean success = false;
    try
    {
      assertFalse(choiceDatatype.isRestrictionOf(new BooleanType()));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
        
  }

  
}
