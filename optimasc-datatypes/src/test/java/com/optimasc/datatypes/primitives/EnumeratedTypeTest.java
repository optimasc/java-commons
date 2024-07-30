package com.optimasc.datatypes.primitives;

import java.util.Arrays;

import com.optimasc.datatypes.primitives.EnumeratedType.*;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DatatypeTest;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;

public class EnumeratedTypeTest extends DatatypeTest
{

  protected EnumeratedType defaultInstance;
  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = new EnumeratedType();
    
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(Object[].class, defaultInstance.getClassType());
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
    EnumeratedType instance = defaultInstance;
    assertEquals(true,instance.isOrdered());
    assertEquals(true,instance.isBounded());
    assertEquals(false,instance.isNumeric());
  }

  /*----------------------- No elements ------------------------*/
  public void testNoChoices()
  {
    EnumeratedType datatype = defaultInstance;
    
    // No choices set by default
    assertEquals(0,datatype.getChoices().length);
    assertEquals(null,datatype.getMinInclusive());
    assertEquals(null,datatype.getMaxInclusive());
    assertEquals(null,datatype.getChoice(0));
    assertEquals(-1,datatype.getEnumOrdinalValue("symbol"));
    assertEquals(false,datatype.validateChoice("symbol"));
    assertEquals(false,datatype.validateRange(12));
    assertEquals(false,datatype.validateRange(100));
    
  }
  
  public void testStringChoices()
  {
    boolean success = false;
    EnumeratedType datatype = new EnumeratedType();
    EnumeratedType otherDatatype = new EnumeratedType();
    
    // Create some different types of choices.
    Object[] dataTypeChoices = new String[]{"Choice1","Choice2"};

    datatype.setChoices(dataTypeChoices);
    assertEquals(2,datatype.getChoices().length);
    assertEquals(0,datatype.getMinInclusive().intValue());
    assertEquals(1,datatype.getMaxInclusive().intValue());
    assertEquals(1,datatype.getEnumOrdinalValue("Choice2"));
    assertEquals(-1,datatype.getEnumOrdinalValue("Choice19"));
    assertEquals("Choice1",datatype.getChoice(0));
    assertEquals("Choice2",datatype.getChoice(1));
    assertEquals(null,datatype.getChoice(2));
    assertEquals(true,datatype.validateChoice("Choice2"));
    assertEquals(false,datatype.validateChoice("ChoiceOut"));
    assertEquals(true,datatype.validateRange(0));
    assertEquals(true,datatype.validateRange(1));
    assertEquals(false,datatype.validateRange(241));
    assertEquals(false,datatype.validateRange(255));
    
    assertTrue(Arrays.equals(dataTypeChoices,datatype.getChoices()));
    assertFalse(datatype.equals(otherDatatype));
    
  }
  
  public void testEnumerationElementChoices()
  {
    boolean success = false;
    EnumeratedType datatype = new EnumeratedType();
    EnumeratedType otherDatatype = new EnumeratedType();
    
    // Create some different types of choices.
    // More complex choice options
    Object[] dataTypeEnum = new Object[]{
        new EnumerationElement("Choice1",1),
        new EnumerationElement("Choice2",2),
        new EnumerationElement("Choice3",55)
    };
    Object[] otherDataTypeEnum = new Object[]{
        new EnumerationElement("Other1",34),
    };

    datatype.setChoices(dataTypeEnum);
    assertEquals(3,datatype.getChoices().length);
    assertEquals(1,datatype.getMinInclusive().intValue());
    assertEquals(55,datatype.getMaxInclusive().intValue());
    assertEquals(2,datatype.getEnumOrdinalValue("Choice2"));
    assertEquals(55,datatype.getEnumOrdinalValue("Choice3"));
    assertEquals(-1,datatype.getEnumOrdinalValue("Choice19"));
    assertEquals(new EnumerationElement("Choice1",1),datatype.getChoice(1));
    assertEquals(new EnumerationElement("Choice3",55),datatype.getChoice(55));
    assertEquals(null,datatype.getChoice(356));
    assertEquals(true,datatype.validateChoice(new EnumerationElement("Choice2",2)));
    assertEquals(false,datatype.validateChoice(new EnumerationElement("ChoiceOut",2)));
    
    assertEquals(false,datatype.validateRange(0));
    
    assertEquals(true,datatype.validateRange(1));
    assertEquals(false,datatype.validateRange(241));
    assertEquals(false,datatype.validateRange(255));
    
    assertTrue(Arrays.equals(dataTypeEnum,datatype.getChoices()));
    assertFalse(datatype.equals(otherDatatype));
    
    
    assertEquals(1,datatype.getMinInclusive().intValue());
    assertEquals(55,datatype.getMaxInclusive().intValue());
    assertEquals(55,datatype.getEnumOrdinalValue("Choice3"));
    assertEquals(-1,datatype.getEnumOrdinalValue("Choice19"));

    assertTrue(Arrays.equals(dataTypeEnum,datatype.getChoices()));
    assertFalse(datatype.equals(otherDatatype));

    otherDatatype.setChoices(otherDataTypeEnum);
    assertFalse(datatype.equals(otherDatatype));

    otherDatatype.setChoices(dataTypeEnum);
    assertTrue(datatype.equals(otherDatatype));
  }
  
  /*-------------------- valueOf functionality ---------------*/
  public void testValueOfObjectEnumerationElement()
  {
    boolean success = false;
    TypeCheckResult checkResult = new TypeCheckResult();
    EnumeratedType datatype = new EnumeratedType();
    
    EnumerationElement element2 =new EnumerationElement("Choice2",2); 
    EnumerationElement otherElement =new EnumerationElement("Choice5",6346); 
    
    // Create some different types of choices.
    // More complex choice options
    Object[] dataTypeEnum = new Object[]{
        new EnumerationElement("Choice1",1),
        element2,
        new EnumerationElement("Choice3",55)
    };
    datatype.setChoices(dataTypeEnum);
    
    assertEquals(null,datatype.toValue(otherElement, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(element2,datatype.toValue(element2, checkResult));
    assertEquals(null,((DatatypeException)checkResult.error));
    
    assertEquals(element2,datatype.toValue(element2.value, checkResult));
    assertEquals(null,((DatatypeException)checkResult.error));
    
    assertEquals(element2,datatype.toValue(new Integer(element2.value), checkResult));
    assertEquals(null,((DatatypeException)checkResult.error));
    
    
  }
  
  
  public void testValueOfObjectString()
  {
    boolean success = false;
    TypeCheckResult checkResult = new TypeCheckResult();
    EnumeratedType datatype = new EnumeratedType();
    
    String element2 = "Choice2";
    String otherElement = "ChoiceOut";
    Object[] dataTypeChoices = new String[]{"Choice1",element2};
    
    datatype.setChoices(dataTypeChoices);
    
    assertEquals(null,datatype.toValue(otherElement, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(element2,datatype.toValue(element2, checkResult));
    assertEquals(null,((DatatypeException)checkResult.error));
    
    assertEquals(element2,datatype.toValue(1, checkResult));
    assertEquals(null,((DatatypeException)checkResult.error));
    
    assertEquals(element2,datatype.toValue(new Integer(1), checkResult));
    assertEquals(null,((DatatypeException)checkResult.error));
    
    
  }
  
  
  /*-------------------- equals/restriction functionality ---------------*/
  public void testEqualsIsRestrictionOf()
  {
    
    // Create some different types of choices.
    // More complex choice options
    Object[] dataTypeEnum = new Object[]{
        new EnumerationElement("Choice1",1),
        new EnumerationElement("Choice2",2),
        new EnumerationElement("Choice3",55)
    };
    Object[] otherDataTypeEnum = new Object[]{
        new EnumerationElement("Other1",34),
    };
    
    EnumeratedType datatype = new EnumeratedType(dataTypeEnum);
    EnumeratedType otherDatatype = new EnumeratedType(otherDataTypeEnum);
    
    assertTrue(datatype.equals(datatype));
    assertFalse(datatype.isRestrictionOf(datatype));
    
    assertFalse(datatype.equals(defaultInstance));
    assertFalse(datatype.equals(otherDatatype));
    assertTrue(otherDatatype.isRestrictionOf(datatype));
    
    otherDatatype.setChoices(dataTypeEnum);
    assertTrue(datatype.equals(otherDatatype));
    
  }
  

}
