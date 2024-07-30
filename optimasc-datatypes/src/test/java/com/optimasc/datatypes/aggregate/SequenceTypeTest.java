package com.optimasc.datatypes.aggregate;

import java.math.BigInteger;
import java.util.Arrays;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.lang.CharacterSet;
import com.optimasc.lang.OctetSequence;

import junit.framework.TestCase;

/** Generic test cases for sequences, not all 
 *  cases will be tested. 
 *  
 * @author Carl Eric Codere
 *
 */
public class SequenceTypeTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  /*------------------  choice functionality ----------------*/
  public void testChoicesIntegerSequence()
  {
    int[] choices[] = new int[][]
        {
        {0,1,2},
        {2,3,4,5}
        };
    
    SequenceType datatype = new SequenceType(choices,new UnnamedTypeReference(new IntegralType()),int[].class);
    
    int emptyArray[] = new int[0];  
    assertEquals(false,datatype.validateChoice(emptyArray));
    int choice1[] = new int[]{0,1,2};
    assertEquals(true,datatype.validateChoice(choice1));
    int choice2[] = new int[]{2,3,4,5};
    assertEquals(true,datatype.validateChoice(choice2));

    boolean success;
    
    success = false;
    try
    {
      assertEquals(false,datatype.validateChoice("Choice3"));
    } catch (IllegalArgumentException e)
    {
      success = true;
    }
    assertEquals(true, success);
  } 
  
  
  /*------------------ toValue functionality ----------------*/
  
  public void testToValueBinarySequence()
  {
    boolean success = false;
    IntegralType octet = new IntegralType(0,255);
    TypeCheckResult conversionResult = new TypeCheckResult();
    
    // Basic with byte[].class sequence with no length specification
    SequenceType datatype = new SequenceType(new UnnamedTypeReference(octet),byte[].class);
    // No bounds defined
    assertEquals(0,datatype.getMinLength());
    assertEquals(Integer.MIN_VALUE,datatype.getMaxLength());
    assertEquals(null,datatype.getChoices());
    assertEquals(true,datatype.isBounded());
    assertEquals(false,datatype.isOrdered());
    assertEquals(false,datatype.isNumeric());
    assertEquals(byte[].class,datatype.getClassType());
    
     byte[] values = new byte[]{0,(byte) 255,127,32};
     assertEquals(values,datatype.toValue(values, conversionResult));
     assertEquals(null,conversionResult.error);
     
     // Basic with byte[].class sequence with no length specification
     datatype = new SequenceType(0,3,new UnnamedTypeReference(octet),byte[].class);
     // No bounds defined
     assertEquals(0,datatype.getMinLength());
     assertEquals(3,datatype.getMaxLength());
     assertEquals(true,datatype.isBounded());
     assertEquals(false,datatype.isOrdered());
     assertEquals(false,datatype.isNumeric());
     assertEquals(byte[].class,datatype.getClassType());
     
      // Valid values
      values = new byte[]{(byte) 255,127,32};
      assertEquals(values,datatype.toValue(values, conversionResult));
      assertEquals(null,conversionResult.error);
      
      values = new byte[0];
      assertEquals(values,datatype.toValue(values, conversionResult));
      assertEquals(null,conversionResult.error);
      
      // Invalid values
      values = new byte[]{32,(byte) 255,127,32};
      assertEquals(null,datatype.toValue(values, conversionResult));
      assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)conversionResult.error).getCode());
      
      short[] svalues = new short[]{0,(byte) 255,127,32};
      assertEquals(null,datatype.toValue(svalues, conversionResult));
      assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
      
  }
  
  
  public void testToValueBinarySequenceChoices()
  {
    boolean success = false;
    IntegralType octet = new IntegralType(0,255);
    TypeCheckResult conversionResult = new TypeCheckResult();
    
    Object[] choices = new Object[]
   {
     new byte[]{1,2,3,4},
     new byte[]{(byte) 255,(byte) 180,54,55,51},
   };
    
    // Basic with byte[].class sequence with no length specification
    SequenceType datatype = new SequenceType(choices,new UnnamedTypeReference(octet),byte[].class);
    // No bounds defined
    assertEquals(4,datatype.getMinLength());
    assertEquals(5,datatype.getMaxLength());
    assertTrue(Arrays.equals(choices,datatype.getChoices()));
    assertEquals(true,datatype.isBounded());
    assertEquals(false,datatype.isOrdered());
    assertEquals(false,datatype.isNumeric());
    assertEquals(byte[].class,datatype.getClassType());
    
     byte[] values = new byte[]{(byte) 255,(byte) 180,54,55,51};
     assertEquals(values,datatype.toValue(values, conversionResult));
     assertEquals(null,conversionResult.error);

     // Invalid values
     values = new byte[]{0,(byte) 255,127,32};
     assertEquals(null,datatype.toValue(values, conversionResult));
     assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
  }
  
  
  public void testToValueIntegerSequence()
  {
    boolean success = false;
    IntegralType intType = new IntegralType(0,Integer.MAX_VALUE);
    TypeCheckResult conversionResult = new TypeCheckResult();
    
    // Basic with byte[].class sequence with no length specification
    SequenceType datatype = new SequenceType(new UnnamedTypeReference(intType),int[].class);
    // No bounds defined
    assertEquals(0,datatype.getMinLength());
    assertEquals(Integer.MIN_VALUE,datatype.getMaxLength());
    assertEquals(null,datatype.getChoices());
    assertEquals(true,datatype.isBounded());
    assertEquals(false,datatype.isOrdered());
    assertEquals(false,datatype.isNumeric());
    assertEquals(int[].class,datatype.getClassType());
    
     int[] values = new int[]{0,255,127,32};
     assertEquals(values,datatype.toValue(values, conversionResult));
     assertEquals(null,conversionResult.error);
     
      // Invalid values
      values = new int[]{32,-4,127,32};
      assertEquals(null,datatype.toValue(values, conversionResult));
      assertEquals(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,((DatatypeException)conversionResult.error).getCode());
      
      short[] svalues = new short[]{0,(byte) 255,127,32};
      assertEquals(null,datatype.toValue(svalues, conversionResult));
      assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
      
  }
  
  
  public void testToValueIntegerSequenceChoices()
  {
    boolean success = false;
    IntegralType intType = new IntegralType(0,Integer.MAX_VALUE);
    TypeCheckResult conversionResult = new TypeCheckResult();
    
    Object[] choices = new Object[]
   {
     new int[]{1,2,3,4},
     new int[]{255,180,54,55,51},
   };
    
    // Basic with byte[].class sequence with no length specification
    SequenceType datatype = new SequenceType(choices,new UnnamedTypeReference(intType),int[].class);
    // No bounds defined
    assertEquals(4,datatype.getMinLength());
    assertEquals(5,datatype.getMaxLength());
    assertTrue(Arrays.equals(choices,datatype.getChoices()));
    assertEquals(true,datatype.isBounded());
    assertEquals(false,datatype.isOrdered());
    assertEquals(false,datatype.isNumeric());
    assertEquals(int[].class,datatype.getClassType());
    
     int[] values = new int[]{255,180,54,55,51};
     assertEquals(values,datatype.toValue(values, conversionResult));
     assertEquals(null,conversionResult.error);

     // Invalid values
     values = new int[]{0,(byte) 255,127,32};
     assertEquals(null,datatype.toValue(values, conversionResult));
     assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
  }
  
  

  /** A Sequence of characters represented by a String. */
  public void testToValueCharacterSequenceUnbounded()
  {
    boolean success = false;
    CharacterType charType = new CharacterType(CharacterSet.LATIN1);
    TypeCheckResult conversionResult = new TypeCheckResult();
    SequenceType datatype = new SequenceType(new UnnamedTypeReference(charType),String.class);
    
    // No bounds defined
    assertEquals(0,datatype.getMinLength());
    assertEquals(Integer.MIN_VALUE,datatype.getMaxLength());
    assertEquals(null,datatype.getChoices());
    assertEquals(true,datatype.isBounded());
    assertEquals(false,datatype.isOrdered());
    assertEquals(false,datatype.isNumeric());
    assertEquals(String.class,datatype.getClassType());
    
    String value = "Hello world";
    assertEquals(value,datatype.toValue(value, conversionResult));
    assertEquals(null,conversionResult.error);
    
    value = "";
    assertEquals(value,datatype.toValue(value, conversionResult));
    assertEquals(null,conversionResult.error);
    
    // Invalid values for Latin1 string
    value = "\u0100";
    assertEquals(null,datatype.toValue(value, conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE ,((DatatypeException)conversionResult.error).getCode());
    
    
    Integer v = new Integer(34);
    assertEquals(null,datatype.toValue(v, conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
  }
  
  
  public void testToValueCharacterSequenceBounded()
  {
    boolean success = false;
    CharacterType charType = new CharacterType(CharacterSet.LATIN1);
    TypeCheckResult conversionResult = new TypeCheckResult();
    SequenceType datatype = new SequenceType(1,11,new UnnamedTypeReference(charType),String.class);
    
    // Bounds defined
    assertEquals(1,datatype.getMinLength());
    assertEquals(11,datatype.getMaxLength());
    assertEquals(null,datatype.getChoices());
    assertEquals(true,datatype.isBounded());
    assertEquals(false,datatype.isOrdered());
    assertEquals(false,datatype.isNumeric());
    assertEquals(String.class,datatype.getClassType());

    // Valid values
    String value = "Hello world";
    assertEquals(value,datatype.toValue(value, conversionResult));
    assertEquals(null,conversionResult.error);
    
    value = "Hi!";
    assertEquals(value,datatype.toValue(value, conversionResult));
    assertEquals(null,conversionResult.error);
    
    value = "Z";
    assertEquals(value,datatype.toValue(value, conversionResult));
    assertEquals(null,conversionResult.error);
    
    
    // Invalid values, outside length bounds
    
    value = "";
    assertEquals(null,datatype.toValue(value, conversionResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)conversionResult.error).getCode());
    
    value = "123456789012";
    assertEquals(null,datatype.toValue(value, conversionResult));
    assertEquals(DatatypeException.ERROR_BOUNDS_RANGE,((DatatypeException)conversionResult.error).getCode());
  }
  
  
  public void testToValueCharacterSequenceChoices()
  {
    boolean success = false;
    CharacterType charType = new CharacterType(CharacterSet.LATIN1);
    TypeCheckResult conversionResult = new TypeCheckResult();
    
    String choices[] =
      { "Choice1", "Choice2"};    
    
    SequenceType datatype = new SequenceType(choices,new UnnamedTypeReference(charType),String.class);
    
    // Bounds defined
    assertEquals(7,datatype.getMinLength());
    assertEquals(7,datatype.getMaxLength());
    assertTrue(Arrays.equals(choices,datatype.getChoices()));
    assertEquals(true,datatype.isBounded());
    assertEquals(false,datatype.isOrdered());
    assertEquals(false,datatype.isNumeric());
    assertEquals(String.class,datatype.getClassType());

    // Valid values
    String value = "Choice1";
    assertEquals(value,datatype.toValue(value, conversionResult));
    assertEquals(null,conversionResult.error);
    
    value = "Choice2";
    assertEquals(value,datatype.toValue(value, conversionResult));
    assertEquals(null,conversionResult.error);
    
    // Invalid values
    
    value = "Choice3";
    assertEquals(null,datatype.toValue(value, conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
    
    value = "123456789012";
    assertEquals(null,datatype.toValue(value, conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
  }
  
  /*-------------------- equals/restriction functionality ---------------*/
  public void testEquals()
  {
    CharacterType charType = new CharacterType(CharacterSet.LATIN1);
    IntegralType intType = new IntegralType(0,Integer.MAX_VALUE);
    
    String choices[] =
      { "Choice1", "Choice2"};
    
    Object[] intChoices = new Object[]
   {
     new int[]{1,2,3,4},
     new int[]{255,180,54,55,51},
   };
    
    SequenceType datatype01 = new SequenceType(choices,new UnnamedTypeReference(charType),String.class);
    SequenceType datatype02 = new SequenceType(choices,new UnnamedTypeReference(charType),String.class);
    SequenceType datatype03 = new SequenceType(0,12,new UnnamedTypeReference(charType),String.class);
    SequenceType datatype05 = new SequenceType(intChoices,new UnnamedTypeReference(intType),int[].class);
    
    assertTrue(datatype01.equals(datatype01));
    assertTrue(datatype01.equals(datatype02));
    
    assertFalse(datatype01.equals(datatype03));
    assertFalse(datatype01.equals(datatype05));
    
  }
  
  public void testEqualsRestrictionLengths()
  {
    CharacterType charType = new CharacterType(CharacterSet.LATIN1);
    
    SequenceType datatype01 = new SequenceType(12,12,new UnnamedTypeReference(charType),String.class);
    SequenceType datatype02 = new SequenceType(0,12,new UnnamedTypeReference(charType),String.class);
    SequenceType datatype03 = new SequenceType(12,12,new UnnamedTypeReference(charType),String.class);
    
    assertFalse(datatype01.equals(datatype02));
    assertFalse(datatype03.equals(datatype02));
    assertTrue(datatype01.equals(datatype03));
    
    assertTrue(datatype01.isRestrictionOf(datatype02));
    assertFalse(datatype01.isRestrictionOf(datatype01));
  }
  
  public void testEqualsRestrictionTypes()
  {
    IntegralType intType = new IntegralType(Integer.MIN_VALUE,Integer.MAX_VALUE);
    IntegralType octetType = new IntegralType(0,255);
    
    SequenceType datatype01 = new SequenceType(new UnnamedTypeReference(intType),int[].class);
    SequenceType datatype02 = new SequenceType(new UnnamedTypeReference(octetType),int[].class);
    
    assertFalse(datatype01.equals(datatype02));
    
    // Verify that the restriction of the base types.
    assertTrue(datatype02.isRestrictionOf(datatype01));
    assertFalse(datatype01.isRestrictionOf(datatype02));
  }
  
  public void testEqualsRestrictionChoices()
  {
    IntegralType intType = new IntegralType(Integer.MIN_VALUE,Integer.MAX_VALUE);
    
    Object[] intChoices01 = new Object[]
   {
     new int[]{6,7,8,9,10,11},
     new int[]{255,180,54,55,51},
   };
    
    Object[] intChoices02 = new Object[]
   {
     new int[]{6,7,8,9,10,11},
     new int[]{255,180,54,55,51},
     new int[]{255,180,54,55,52},
   };

    
    
    SequenceType datatype01 = new SequenceType(intChoices01,new UnnamedTypeReference(intType),int[].class);
    SequenceType datatype02 = new SequenceType(intChoices02,new UnnamedTypeReference(intType),int[].class);
    
    assertFalse(datatype01.equals(datatype02));
    
    // Verify that the restriction of the base types.
    assertTrue(datatype02.isRestrictionOf(datatype01));
    assertFalse(datatype01.isRestrictionOf(datatype02));
  }
  
  
  
  
  
}
