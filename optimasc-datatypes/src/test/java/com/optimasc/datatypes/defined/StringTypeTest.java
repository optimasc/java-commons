package com.optimasc.datatypes.defined;

import java.math.BigDecimal;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DatatypeTest;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.lang.CharacterSet;

public class StringTypeTest extends DatatypeTest
{
  protected StringType defaultInstance;
  protected TypeReference charType = new UnnamedTypeReference(new CharacterType(CharacterSet.BMP));
  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = new StringType();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(String.class, defaultInstance.getClassType());
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
    StringType instance = defaultInstance;
    assertEquals(false,instance.isOrdered());
    assertEquals(false,instance.isNumeric());
    assertEquals(true,instance.isBounded());
    assertEquals(null,instance.getChoices());
  }
  
  /*------------------ length/size functionality ----------------*/
   public void testLengthDefault()
   {
     StringType datatype = new StringType();
     
     assertEquals(0,datatype.getMinLength());
     assertEquals(Integer.MIN_VALUE,datatype.getMaxLength());
     
   }
   
   public void testLengthInvalid()
   {
     StringType datatype= null;
     
     boolean success;
     
     success = false;
     try
     {
       datatype = new StringType(-1,255,charType);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);
     
     success = false;
     try
     {
       datatype = new StringType(32,1,charType);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);
     
     success = false;
     try
     {
       datatype = new StringType(-34,-2,charType);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);
     
     success = false;
     try
     {
       datatype = new StringType(0,-2,charType);
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);

   }
   

   public void testLengthBounded()
   {
     StringType datatype = new StringType(1,Integer.MAX_VALUE,charType);
     

     // No length/size limitations
     assertEquals(1,datatype.getMinLength());
     assertEquals(Integer.MAX_VALUE,datatype.getMaxLength());
     
   }
   
  /*------------------  choice functionality ----------------*/
   public void testChoices()
   {
     String choices[] = new String[]{"Choice1","Choice2"};
     
     StringType datatype = new StringType(choices,charType);
     
     assertEquals(false,datatype.validateChoice(""));
     assertEquals(true,datatype.validateChoice("Choice1"));
     assertEquals(true,datatype.validateChoice("Choice2"));
     assertEquals(false,datatype.validateChoice("Choice3"));

     boolean success;
     
     success = false;
     try
     {
       assertEquals(false,datatype.validateChoice(new Integer(5)));
     } catch (IllegalArgumentException e)
     {
       success = true;
     }
     assertEquals(true, success);
   } 
  
  /*------------------ toValue functionality ----------------*/
  
  public void testToValueObjectLengthsBoth()
  {
    TypeCheckResult checkResult = new TypeCheckResult();
    
    // Bounded data type with a minimum length of 0 elements and
    // a maximum of 2 elements.
    StringType boundedDatatype = new StringType(0,2,charType);
    
    // Invalid object class
    assertEquals(null,boundedDatatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Valid object class but invalid length.
    assertEquals(null,boundedDatatype.toValue("123", checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());

    String emptyString = "";
    assertEquals(emptyString,boundedDatatype.toValue(emptyString, checkResult));
    assertEquals(null,checkResult.error);

    String oneString = "012";
    assertEquals(null,boundedDatatype.toValue(oneString, checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());
    
    String secondString = "01";
    assertEquals(secondString,boundedDatatype.toValue(secondString, checkResult));
    assertEquals(null,checkResult.error);
  }
  
  
  public void testToValueObjectLengthsEqual()
  {
    TypeCheckResult checkResult = new TypeCheckResult();

    String emptyString = "";
    String twoElementString = "78";
    
    StringType boundedDatatype = new StringType(2,2,charType);
    
    // Invalid object class
    assertEquals(null,boundedDatatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Valid object class but invalid length.
    assertEquals(null,boundedDatatype.toValue("123", checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());

    assertEquals(null,boundedDatatype.toValue(emptyString, checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());

    // Within length constraints
    assertEquals(twoElementString,boundedDatatype.toValue(twoElementString, checkResult));
    assertEquals(null,checkResult.error);
  }
  
  
  public void testToValueObjectChoices()
  {
    TypeCheckResult checkResult = new TypeCheckResult();

    String choices[] = new String[]{"Choice1","Choice2"};
    
    StringType datatype = new StringType(choices,charType);
    
    // Invalid object class
    assertEquals(null,datatype.toValue(BigDecimal.valueOf(0), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Valid object class but invalid choice
    assertEquals(null,datatype.toValue("123", checkResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(null,datatype.toValue("Choice3", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    

    // Within valid choice
    assertEquals("Choice2",datatype.toValue("Choice2", checkResult));
    assertEquals(null,checkResult.error);
  }
  
  
  public void testToValueObjectTypes()
  {
    TypeCheckResult checkResult = new TypeCheckResult();
    StringType datatype;
    
    // ASCII character set by default
    datatype = defaultInstance;
    assertEquals(CharacterSet.ASCII,datatype.getCharacterSet());
    
    // Valid but different data types within ASCII range
    String strValue = "hello";
    assertEquals(strValue,datatype.toValue(strValue, checkResult));
    assertEquals(null,checkResult.error);
    
    char charValue[] = {'h','e','l','l','o'};
    assertEquals(strValue,datatype.toValue(charValue, checkResult));
    assertEquals(null,checkResult.error);
    
    int intValue[] = {(int)'h',(int)'e',(int)'l',(int)'l',(int)'o'};
    assertEquals(strValue,datatype.toValue(intValue, checkResult));
    assertEquals(null,checkResult.error);

    // Wrong datatype
    assertEquals(null,datatype.toValue(new Integer(44), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());

    // Valid but different data types outside ASCII range
    strValue = "h\u00C8llo";
    assertEquals(null,datatype.toValue(strValue, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,((DatatypeException)checkResult.error).getCode());
    
    charValue = new char[]{'h','\u00C8','l','l','o'};
    assertEquals(null,datatype.toValue(charValue, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,((DatatypeException)checkResult.error).getCode());
    
    intValue = new int[]{(int)'h',(int)0xC8,(int)'l',(int)'l',(int)'o'};
    assertEquals(null,datatype.toValue(intValue, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,((DatatypeException)checkResult.error).getCode());
    
  }

  /*-------------------- whitespace normalisation functionality ---------------*/
  public void testWhitespace()
  {
    TypeCheckResult checkResult = new TypeCheckResult();
    StringType datatype = defaultInstance;
    datatype.setWhitespace(StringType.WHITESPACE_PRESERVE);
    
    String originalString = "\tThis is a string  .";
    String replacedString = " This is a string  .";
    String collapsedString = "This is a string .";
    assertEquals(originalString,datatype.toValue(originalString, checkResult));
    
    datatype.setWhitespace(StringType.WHITESPACE_REPLACE);
    assertEquals(replacedString,datatype.toValue(originalString, checkResult));
    
    datatype.setWhitespace(StringType.WHITESPACE_COLLAPSE);
    assertEquals(collapsedString,datatype.toValue(originalString, checkResult));
    
  }
  
  
  /*-------------------- equals/restriction functionality ---------------*/
  
  
  public void testEqualsRestriction()
  {
    TypeCheckResult checkResult = new TypeCheckResult();

    String emptyString = "";
    String smallString = "0123456";
    String twoElementString = " 78";
    String mediumString = "0123456789101112";
   
    String choices[] = new String[]{twoElementString,mediumString};
    
    StringType choiceDatatype = new StringType(choices,charType);
    StringType smallerChoiceDatatype = new StringType(new String[]{emptyString},charType);
    
    StringType lengthBoundedDatatype = new StringType(0,32,charType);
    StringType otherLengthBoundedDatatype = new StringType(0,32,charType);
    StringType unboundedDatatype = new StringType(charType);
    
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
  
  public void testRestrictionCharset()
  {
    TypeCheckResult checkResult = new TypeCheckResult();

    StringType bmpDatatype = new StringType(charType);
    StringType asciiDatatype = new StringType();
    
    assertFalse(bmpDatatype.isRestrictionOf(asciiDatatype));
    assertTrue(asciiDatatype.isRestrictionOf(bmpDatatype));
  }
  

  
}
