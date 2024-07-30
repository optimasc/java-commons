package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DatatypeTest;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.date.DateTime;
import com.optimasc.lang.CharacterSet;
import com.optimasc.lang.GregorianDatetimeCalendar;

import junit.framework.TestCase;

public class CharacterTypeTest extends DatatypeTest
{

  protected CharacterType defaultInstance;
  
  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = new CharacterType();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(Integer.class, defaultInstance.getClassType());
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
    CharacterType instance = defaultInstance;
    assertEquals(true,instance.isOrdered());
    assertEquals(false,instance.isNumeric());
  }
  
  /*-------------- commonCharacterSet functionality -----*/
  public void testgetCommonCharacterSet()
  {
    // Create some bounded values
    CharacterType UnicodeType = new CharacterType(CharacterSet.UNICODE,true);
    CharacterType BMPType = new CharacterType(CharacterSet.BMP,true);
    CharacterType ISO8BitType = new CharacterType(CharacterSet.ISO8BIT, true);
    CharacterType ASCIIType = new CharacterType(CharacterSet.ASCII, true);
    
    assertEquals(CharacterSet.UNICODE,UnicodeType.getCommonCharacterSet(UnicodeType.getCharacterSet()));
    assertEquals(CharacterSet.BMP,UnicodeType.getCommonCharacterSet(BMPType.getCharacterSet()));
    assertEquals(CharacterSet.ISO8BIT,ISO8BitType.getCommonCharacterSet(UnicodeType.getCharacterSet()));
    assertEquals(CharacterSet.ASCII,BMPType.getCommonCharacterSet(ASCIIType.getCharacterSet()));
  }  
  
  /*------------------ valueOf functionality ----------------*/
  
  public void testValueOfCharacter()
  {
    // Create some bounded values
    CharacterType BMPType = new CharacterType(CharacterSet.BMP,true);
    CharacterType Latin1Type = new CharacterType(CharacterSet.LATIN1, true);
    CharacterType ASCIIType = new CharacterType(CharacterSet.ASCII, true);

    // Latin1 Character
    char c= '\u00C0';
    Character charValue = new Character(c);
    Integer integerValue = new Integer(c);
    Integer intResult = new Integer(c);
    
    TypeCheckResult checkResult = new TypeCheckResult();

    assertEquals(intResult, Latin1Type.toValue(charValue, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(intResult, Latin1Type.toValue(integerValue, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(intResult, Latin1Type.toValue(c, checkResult));
    assertEquals(null,checkResult.error);
    
    assertEquals(intResult, BMPType.toValue(charValue, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(intResult, BMPType.toValue(integerValue, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(intResult, BMPType.toValue(c, checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(intResult, BMPType.toValue(BigDecimal.valueOf(c), checkResult));
    assertEquals(null,checkResult.error);
    assertEquals(intResult, BMPType.toValue(BigInteger.valueOf(c), checkResult));
    assertEquals(null,checkResult.error);
    

    assertEquals(null, ASCIIType.toValue(charValue, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,((DatatypeException)checkResult.error).getCode());
    assertEquals(null, ASCIIType.toValue(integerValue, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,((DatatypeException)checkResult.error).getCode());
    assertEquals(null, ASCIIType.toValue(c, checkResult));
    assertEquals(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,((DatatypeException)checkResult.error).getCode());
    
    assertEquals(null,ASCIIType.toValue(BigInteger.valueOf(-54), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,((DatatypeException)checkResult.error).getCode());
    

    // Invalid values
    assertEquals(null,ASCIIType.toValue("Hello", checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    assertEquals(null, BMPType.toValue(new BigDecimal(0.9), checkResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)checkResult.error).getCode());
    
  }
  
  
  /*----------------------------- ranges --------------------------------*/
  public void testRange()
  {
    CharacterType ordered01 = new CharacterType(CharacterSet.ASCII, true);

    assertEquals(BigDecimal.valueOf(0),ordered01.getMinInclusive());
    assertEquals(BigDecimal.valueOf(127),ordered01.getMaxInclusive());
    
    assertEquals(false,ordered01.validateRange(128));
    assertEquals(false,ordered01.validateRange(-1));
    assertEquals(true,ordered01.validateRange(127));
    assertEquals(true,ordered01.validateRange(0));

    assertEquals(false,ordered01.validateRange(BigDecimal.valueOf(200)));
    assertEquals(false,ordered01.validateRange(BigDecimal.valueOf(-1)));
    assertEquals(true,ordered01.validateRange(BigDecimal.valueOf(127)));
    assertEquals(true,ordered01.validateRange(BigDecimal.valueOf(54)));
    
  }
  
  
  /*-------------------- equals/isRestrictionOf functionality ---------------*/
  public void testEqualsIsRestrictionOf()
  {
    assertTrue(defaultInstance.equals(defaultInstance));
    assertFalse(defaultInstance.isRestrictionOf(defaultInstance));
    assertTrue(defaultInstance.equals(new CharacterType()));
    assertFalse(defaultInstance.isRestrictionOf(new CharacterType()));
    
    // Create some bounded values
    CharacterType UnicodeType = new CharacterType(CharacterSet.UNICODE,true);
    CharacterType Latin1Type = new CharacterType(CharacterSet.LATIN1, true);
    CharacterType ASCIIType = new CharacterType(CharacterSet.ASCII, true);
    
    assertFalse(ASCIIType.equals(UnicodeType));

    /*----------------- equals --------------*/
    
    // Check for type identity
    assertFalse(UnicodeType.equals(Latin1Type));
    assertFalse(ASCIIType.equals(Latin1Type));
    assertFalse(UnicodeType.equals(ASCIIType));
    
    /*----------------- --------------*/

    assertFalse(UnicodeType.isRestrictionOf(ASCIIType));
    assertFalse(Latin1Type.isRestrictionOf(ASCIIType));

    // Smaller range character set
    assertTrue(Latin1Type.isRestrictionOf(UnicodeType));
    assertTrue(ASCIIType.isRestrictionOf(Latin1Type));
    
    
    boolean success = false;
    try
    {
      assertFalse(ASCIIType.isRestrictionOf(new BooleanType()));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
    
    
  }  
  

}
