package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.BoundedProperty;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.OrderedProperty;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.facets.CharacterSetEncodingFacet;
import com.optimasc.datatypes.facets.NumberEnumerationFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.CharacterSet;
import com.optimasc.lang.NumberComparator;
import com.optimasc.lang.NumberSelectItem;

/** Abstract datatype that represents a character. 
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li></code>character</code> ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 * 
 *  <p>Contrary to ISO/IEC 11404, this type is considered ordered and a character
 *  set specification can be associated with it and is bounded.</p>
 *  
 * <p>Internally, values of this type are represented as {@link Integer} objects that
 * represent a Unicode code point.</p>
 *  
 */
public class CharacterType extends PrimitiveType implements CharacterSetEncodingFacet,OrderedProperty,BoundedProperty, NumberEnumerationFacet
{
  /** Character Set Repertoire list  */
  protected CharacterSet characterSet;
  
  protected static final BigDecimal ZERO = BigDecimal.valueOf(0);
  protected static final BigDecimal UCS4_MAX = BigDecimal.valueOf(0x10FFFFL);
  

  /** Creates a default instance of the specified
   *  character type with the character set that contains 
   *  the Unicode Basic Multilanguage Plane (BMP).
   * 
   */
  public CharacterType()
  {
    super(true);
    this.characterSet = CharacterSet.BMP;
  }  
  
  public CharacterType(CharacterSet characterSet)
  {
    super(true);
    setCharacterSet(characterSet);
  }
  
  public CharacterType(CharacterSet characterSet, boolean ordered)
  {
    super(ordered);
    setCharacterSet(characterSet);
  }
  
  
  public Class getClassType()
  {
    return Integer.class;
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
    if (characterSet.equals(charSet))
    {
      return charSet;
    }
    if (characterSet.isRestrictionOf(charSet))
    {
      return characterSet;
    }
    
    if (charSet.isRestrictionOf(characterSet))
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
    
    
    if (this instanceof CharacterSetEncodingFacet)
    {
      CharacterSetEncodingFacet thisObject = (CharacterSetEncodingFacet) this;
      if ((obj instanceof CharacterSetEncodingFacet) == false)
      {
        return false;
      }
      CharacterSetEncodingFacet otherObject = (CharacterSetEncodingFacet) obj;
      if (otherObject.getCharacterSet().equals(thisObject.getCharacterSet()) == false)
      {
        return false;
      }
    }
    
    
    if (this instanceof NumberEnumerationFacet)
    {
      NumberEnumerationFacet thisObject = (NumberEnumerationFacet) this;
      if ((obj instanceof NumberEnumerationFacet) == false)
      {
        return false;
      }
      NumberEnumerationFacet otherObject = (NumberEnumerationFacet) obj;
      
      NumberEnumerationFacet other = (NumberEnumerationFacet) obj;
      Number maxInclusive = thisObject.getMaxInclusive();
      Number minInclusive = thisObject.getMinInclusive();
      if (maxInclusive == null)
      {
        if (other.getMaxInclusive() != null)
          return false;
      }
      // We check that the scales are equal here!
      else if (NumberComparator.INSTANCE.compare(maxInclusive,other.getMaxInclusive())!=0)
        return false;
      if (minInclusive == null)
      {
        if (other.getMinInclusive() != null)
          return false;
      }
      // We check that the scales are equal here!
      else if (NumberComparator.INSTANCE.compare(minInclusive,other.getMinInclusive())!=0)
        return false;
    }
    return true;
    
    
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
  protected Object toValueNumber(Number ordinalValue, TypeCheckResult conversionResult)
  {
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
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Character is not a natural number.");
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
    return new Integer((int)intValue);
  }
  
  

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
    if (characterSet.isValid(ordinalValue)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_CHARACTER_NOT_REPERTOIRE,"Character is beyond the range of the Unicode character repertoire.");
      return null;
    }
    return new Integer((int)ordinalValue);
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
    conversionResult.reset();
    if (Character.class.isInstance(value))
    {
      long v = ((Character)value).charValue();
      return toValue(v,conversionResult);
    }
    if (Number.class.isInstance(value))
    {
      return toValueNumber((Number)value,conversionResult);
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }

  public CharacterSet getCharacterSet()
  {
    return characterSet;
  }

  public boolean isRestrictionOf(Datatype value)
  {
    if ((value instanceof CharacterType)==false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '"+value.getClass().getName()+"'.");
    }
    return characterSet.isRestrictionOf(((CharacterType)value).characterSet);
  }

  public boolean isBounded()
  {
    return true;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  public Number getMinInclusive()
  {
    return new Long(characterSet.getMinInclusive());
  }

  public Number getMaxInclusive()
  {
    return new Long(characterSet.getMaxInclusive());
  }

  public boolean isValid(long value)
  {
    if (ordered==false)
      return false;
    return characterSet.isValid(value);
  }

  public boolean validateChoice(Number value)
  {
    if (ordered==false)
      return false;
    return characterSet.isValid(value.longValue());
  }
  
  public void setCharacterSet(CharacterSet charset)
  {
    this.characterSet = charset;
  }
  
  public String toString()
  {
    String oid= getCharacterSet().oid;
    if (oid != null)
    {
      oid = oid.replace('.', ' ');
      return "character("+oid+")";
    } else
    {
      return "character";
    }
  }

  public NumberSelectItem[] getAllowedValuesAsSelectItems()
  {
    return characterSet.getSelectingItems();
  }

  /** This is not supported and will throw 
   *  an <code>UnsupportedOperationException</code>.
   *  
   *  To get the range of allowed values, call
   *  {@link #getAllowedValuesAsSelectItems()} or
   *  {@link #getCharacterSet()}.
   */
  public Object[] getAllowedValues()
  {
    throw new UnsupportedOperationException();
  }

  /** This is not supported and will throw 
   *  an <code>UnsupportedOperationException</code>.
   *  
   *  To set the range of allowed values, call
   *  {@link #setCharacterSet(CharacterSet)}.
   */
  public void setAllowedValues(Object[] choices)
  {
    throw new UnsupportedOperationException();
  }

  public boolean isValid(Object value)
  {
    if (ordered==false)
      return false;
    if ((value instanceof Number)==false)
    {
      return false;
    }
    return characterSet.isValid(((Number)value).intValue());
  }

  public Class getAllowedValuesClass()
  {
    return Integer.class;
  }

  /** This is not supported and will throw 
   *  an <code>UnsupportedOperationException</code>.
   *  
   *  To set the range of allowed values, call
   *  {@link #setCharacterSet(CharacterSet)}.
   */
  public void setAllowedValuesAsSelectItems(NumberSelectItem[] values)
  {
    throw new UnsupportedOperationException();
  }

  /** This is not supported and will throw 
   *  an <code>UnsupportedOperationException</code>.
   *  
   *  To set the range of allowed values, call
   *  {@link #setCharacterSet(CharacterSet)}.
   */
  public void setAllowedValues(long[] values)
  {
    throw new UnsupportedOperationException();
  }
  
  

}
