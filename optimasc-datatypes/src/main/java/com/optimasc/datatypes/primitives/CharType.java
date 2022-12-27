package com.optimasc.datatypes.primitives;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

public abstract class CharType extends PrimitiveType
{
  protected static final Character INSTANCE_TYPE = new Character('a');
  protected Charset charSet = null; 
  protected CharsetEncoder charsetEncoder = null;
  
  /** Repertoire list - CHARSET IANA value. */
  protected String repertoireList;
  
  
  public CharType()
  {
    super(Datatype.CHAR);
  }
  
  public Class getClassType()
  {
    return Character.class;
  }

  

  /** Checks if this character is valid for this repertoire.
   * 
   * @param c The character to validate represented as an UCS-4 character.
   * @return true if this character is valid or not.
   */
  public boolean isValidCharacter(int c)
  {
    try
    {
    CharBuffer cb = CharBuffer.allocate(1);
    char[] chars = Character.toChars(c);
    for (int i = 0; i < chars.length; i++)
    {
      cb.append(chars[i]);
    }
    charsetEncoder.encode(cb);
    } catch (CharacterCodingException e)
    {
      return false;
    }
    return true;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    checkClass(value);    
    if (value instanceof Character)
    {
      if (isValidCharacter(((Character)value).charValue())==false)
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE, "Character cannot be encoded in this repertoire");        
      }
    } else
    throw new IllegalArgumentException(
        "Invalid object type - should be Character instance");
  }

  /** Compares this CharType to the specified object. 
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
    if (!(obj instanceof CharType))
    {
        return false;
    }
    
    if (repertoireList.equals(((CharType)obj).repertoireList)==false)
    {
        return false;
    }
    
    return true;
    
  }
  
  public Object accept(DatatypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  

  public String getRepertoireList()
  {
    return repertoireList;
  }

  public void setRepertoireList(String repertoireList)
  {
    this.repertoireList = repertoireList;
    Charset cs = Charset.forName(repertoireList);
    charsetEncoder = cs.newEncoder();
    charsetEncoder.onMalformedInput(CodingErrorAction.REPORT);
    charsetEncoder.onUnmappableCharacter(CodingErrorAction.REPORT);
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
  


}
