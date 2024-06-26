package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.optimasc.datatypes.CharacterSetEncodingFacet;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.SubSet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.lang.CharacterSet;

/** Abstract datatype that represents a character. 
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li></code>character</code> ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 * 
 *  <p>Contrary to ISO/IEC 11404, this type is considered ordered and a character
 *  set specification can be associated with it. </p>
 *  
 * <p>Internally, values of this type are represented as {@link Character} objects.</p>
 *  
 */
public abstract class CharacterType extends PrimitiveType implements CharacterSetEncodingFacet,OrderedFacet
{
  
  /** Character Set Repertoire list  */
  protected CharacterSet characterSet;
  
  protected static final BigDecimal ZERO = BigDecimal.valueOf(0);
  protected static final BigDecimal UCS4_MAX = BigDecimal.valueOf(0x10FFFFL);
  
  protected CharacterType(CharacterSet characterSet)
  {
    super(Datatype.CHAR,true);
    this.characterSet = characterSet;
  }
  
  protected CharacterType(CharacterSet characterSet, boolean ordered)
  {
    super(Datatype.CHAR,ordered);
    this.characterSet = characterSet;
  }
  
  
  public Class getClassType()
  {
    return Character.class;
  }

  /** Verifies if this character set names is
   *  compatible with the passed in character set
   *  name. Character name are compatible
   *  under the following conditions:
   *  
   *  <ul> 
   *   <li>Both character set repertoire are equal.<li>
   *   <li>Both character set are unicode character sets.</li>
   *   <li>A character set is a subset of the other character set</li>
   *  </ul>
   * 
   * @param charSet [in] The other character set
   * @return The common character set to use or <code>null</code> if they 
   *   are not compatible.
   */
  public CharacterSet getCommonCharacterSet(CharacterSet charSet)
  {
    if (charSet.equals(charSet))
    {
      return charSet;
    }
    if (characterSet.isSubSet(charSet))
    {
      return charSet;
    }
    
    if (charSet.isSubSet(characterSet))
    {
      return charSet;
    }
    return null;
  }

  /** Compares this CharacterType to the specified object. 
   *  The result is true if and only if the argument is not null 
   *  and is a CharType object that has the same constraints 
   *  (repertoireList) as this object
   * 
   */
  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }
    if (!(obj instanceof CharacterType))
    {
        return false;
    }
    
    return super.equals(obj);
    
  }
  
  /** {@inheritDoc}
   * 
     *  <p>This specific implementation will return <code>null</code> if 
     *  the character type is not defined as being ordered. If the character
     *  type is defined as ordered, if the value is not an integer value
     *  (hence not <code>Float</code>, <code>Double</code> or <code>BigDecimal</code>
     *  with a scale more than 0), then the codepoint will be verified against
     *  the character set repertoire, and if ok will return the character,
     *  otherwise an error will be thrown.
     *  </p>
   *  <p></p>
   * 
   */
  public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
    long intValue;
    
    // Throw and exception when value is not ordered.
    if (ordered ==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"This type is not ordered, hence unsupported value of class,  '"+ordinalValue.getClass().getName()+"'.");
      return null;
    }
    
    if (ordinalValue instanceof BigDecimal)
    {
      BigDecimal bigDecimal = (BigDecimal)ordinalValue;
      if (TypeUtilities.isLongValueExact(bigDecimal)==false)
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,"Character is not a natural number or is beyong that can be represented.");
        return null;
      }
      intValue = bigDecimal.longValue();
    }
    
    if (ordinalValue instanceof BigInteger)
    {
      BigInteger bigInteger = (BigInteger)ordinalValue;
      if (TypeUtilities.isLongValueExact(bigInteger)==false)
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,"Character is beyond the range of the Unicode character repertoire.");
        return null;
      }
      intValue = bigInteger.longValue();
    } else
    {
      intValue = ordinalValue.longValue();
    }
    if (characterSet.isValid(intValue)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,"Character is beyond the range of the Unicode character repertoire.");
      return null;
    }
    return new Character((char)intValue);
  }
  
  

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    if (characterSet.isValid(ordinalValue)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,"Character is beyond the range of the Unicode character repertoire.");
      return null;
    }
    return new Character((char)ordinalValue);
  }

  /** {@inheritDoc}
   * 
   *  <p>The input can either be a value represented as <code>Character</code>
   *  or <code>Number</code> object. The value returned is an object of type 
   *  <code>Character</code>.</p>
   * 
   */
  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    if (Character.class.isInstance(value))
    {
      conversionResult.reset();
      return value;
    }
    if (Number.class.isInstance(value))
    {
      return toValue((Number)value,conversionResult);
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }

  public CharacterSet getRepertoireList()
  {
    return characterSet;
  }
  
  

  public CharacterSet getCharacterSet()
  {
    return characterSet;
  }

  public boolean isRestriction(Type value)
  {
    if (!(value instanceof SubSet))
      return false;
    return ((SubSet)value).isSubset(value);
  }

  public boolean isBounded()
  {
    return true;
  }

}
