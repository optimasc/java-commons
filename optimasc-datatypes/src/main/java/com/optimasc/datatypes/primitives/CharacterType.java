package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import com.optimasc.datatypes.BoundedRangeFacet;
import com.optimasc.datatypes.CharacterSetEncodingFacet;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.visitor.TypeVisitor;

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
public abstract class CharacterType extends PrimitiveType implements CharacterSetEncodingFacet,Parseable,BoundedRangeFacet
{
  protected static final Character INSTANCE_TYPE = new Character('a');
  
  /** Repertoire list - CHARSET IANA value. */
  protected String charSetName;
  
  
  public CharacterType()
  {
    super(Datatype.CHAR,true);
  }
  
  public Class getClassType()
  {
    return Character.class;
  }

  

  /** Checks if this character is valid for this repertoire.
   * 
   * @param codePoint The character to validate represented as an UCS-4 character.
   * @return true if this character is valid or not.
   */
  public abstract boolean isValidCharacter(int codePoint);

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    checkClass(value);
    if (value instanceof Integer)
    {
      if (isValidCharacter(((Integer)value).intValue())==false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_CHARACTER, "Character cannot be encoded in this repertoire");        
      }
    } else
    if (value instanceof Character)
    {
      if (isValidCharacter(((Character)value).charValue())==false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_CHARACTER, "Character cannot be encoded in this repertoire");        
      }
    } else
    throw new IllegalArgumentException(
        "Invalid object type - should be Character instance");
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
  
  public String getRepertoireList()
  {
    return charSetName;
  }
  
  

  public Object getObjectType()
  {
    return INSTANCE_TYPE;
  }
  
  
  public Object parse(String value) throws ParseException
  {
    if (value.length()!=1)
    {
      throw new ParseException("String length is different than 1 character.",0);
    }
    Character chObject = new Character(value.charAt(0));
    try
    {
      validate(chObject);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Cannot convert string to character",0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Cannot convert string to character",0);
    }
    return chObject;
  }

  public String getCharSetName()
  {
    return charSetName;
  }

  public void setCharSetName(String charSetName)
  {
    this.charSetName = charSetName;
  }

  /** This will throw an exception as this method
   *  should not be called on <code>CharacterType</code>
   *  and is set internally in derived classes.
   *  
   */
  public void setMinInclusive(long value)
  {
    throw new IllegalArgumentException("Setting inclusive value for range is not supported for character types");
  }

  /** This will throw an exception as this method
   *  should not be called on <code>CharacterType</code>
   *  and is set internally in derived classes.
   *  
   */
  public void setMaxInclusive(long value)
  {
    throw new IllegalArgumentException("Setting inclusive value for range is not supported for character types");
  }

}
