package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.derived.UCS2CharType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;
import com.optimasc.utils.StringUtilities;


/** Represents a character string datatype. The
 *  actual encoding of the string depends on the
 *  {@code characterType} property. By default, it
 *  supports UCS-2 character encoding.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>Depends on character type (UTF8String, PrintableString, VisibleString) ASN.1 datatype</li>
 *   <li>charactstring ISO/IEC 11404 General purpose datatype</li>
 *   <li>string XMLSchema built-in datatype</li>
 *  </ul>
 *  
 *  By default, the allowed minimum length of the string
 *  type is 0 characters, and the default maximum length
 *  is {@code Integer.MAX_VALUE}, {@code pattern} and {@code choices}
 *  are set to null.
 *  
 *
 * @author Carl Eric Codère
 */
public class StringType extends Datatype implements EnumerationFacet, LengthFacet, PatternFacet, ConstructedSimple, DatatypeConverter
{
  protected static final String STRING_INSTANCE = new String();
  
  
  /** No normalization is done, the value is not changed */
  public static final String WHITESPACE_PRESERVE = "preserve";
  /** All occurrences of #x9 (tab), #xA (line feed) and #xD (carriage return) are replaced with #x20 (space) */ 
  public static final String WHITESPACE_REPLACE = "replace";
  /** After the processing implied by replace, contiguous sequences of #x20's are collapsed to a single #x20, and leading and trailing #x20's are removed. */
  public static final String WHITESPACE_COLLAPSE = "collapse";
  
  protected static final String REGEX_PATTERN = ".*";
  

  protected static final char CHAR_TAB = 0x09;
  protected static final char CHAR_LF = 0x0A; 
  protected static final char CHAR_CR = 0x0D; 
  protected static final char CHAR_SPACE = 0x20; 
  
  protected String pattern;
  
  protected String whitespace;

  protected EnumerationHelper enumHelper;
  protected LengthHelper lengthHelper;
  
  protected CharType characterType;
  
  
  public StringType()
  {
      super(Datatype.VARCHAR);
      whitespace = WHITESPACE_PRESERVE;
      enumHelper = new EnumerationHelper(this);
      lengthHelper = new LengthHelper();
      setMinLength(0);
      setMaxLength(Integer.MAX_VALUE);
      setElementType(new UCS2CharType());
  }
  


  public int getSize()
  {
      return (getMaxLength()*characterType.getSize());
  }  

  public Class getClassType()
  {
    switch (characterType.getSize())
    {
      case 1:
         return byte[].class;
      case 2: 
         return String.class;
      case 3:
      case 4:
         return int[].class;
    }
    return null;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    String s;
    if (value instanceof char[])
    {
      char[] chArr = (char[])value;
      for (int i = 0; i < chArr.length; i++)
      {
        if (characterType.isValidCharacter(chArr[i])==false)
        {
          throw new IllegalArgumentException("Invalid characters found.");
        }
      }
      if ((chArr.length < getMinLength()) || (chArr.length > getMaxLength()))
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The string does not match the datatype specification");
      }
      s = new String(chArr);
      validatePattern(s);
      if (validateChoice(s)==false)
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The string does not match the datatype specification");
      }
    }
    else
    if (value instanceof String)
    {
      String string = (String)value;
      int charCount = 0;
      while (charCount < string.length())
      {
        int ch = string.codePointAt(charCount);
        if (characterType.isValidCharacter(ch)==false)
        {
          DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The string does not contain valid characters for this repertoire");
        }
        charCount+= Character.charCount(ch);
      }
      if ((charCount < getMinLength()) || (charCount > getMaxLength()))
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The string does not match the datatype specification");
      }
      validatePattern(string);
      if (validateChoice(string)==false)
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The string does not match the datatype specification");
      }
    } else
    {
      throw new IllegalArgumentException("Invalid object type - should be String instance");
    }
  }

  public Object accept(DatatypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  

  public void setMinLength(int value)
  {
    lengthHelper.setMinLength(value);
  }

  public void setMaxLength(int value)
  {
    lengthHelper.setMaxLength(value);
  }

  public int getMinLength()
  {
    return lengthHelper.getMinLength();
  }

  public int getMaxLength()
  {
    return lengthHelper.getMaxLength();
  }

  public String getPattern()
  {
    return pattern;
  }

  
  /** Validates if the value of string is within the allowed pattern name
   *  of allowed values.
   * 
   * @param value The value to check
   * @throws DatatypeException if the value is not allowed.
   */
  public void validatePattern(String value) throws DatatypeException
  {
    if ((pattern != null) && (pattern.length() > 0))
    {
/*                RE r = new RE(pattern);            // Create new pattern
        if (r.match(value)=false)                // Match pattern
          throw new NumberFormatException("The string does not match the datatype specification"); */
    }
    
  }

  public Object[] getChoices()
  {
    return enumHelper.getChoices();
  }

  /** This can be used to set the allowed string values as a list. The array
   *  should contain String objects that represent allowed values for this
   *  datatype. To clear the choice simply set the value to null.
   *
   */
  public void setChoices(Object[] choices)
  {
    int index;
    int maxLength = 0;
    int minLength = 0;
    /* Set the minimum and maximum length with choices */
    for (index = 0; index < choices.length; index++)
    {
      if (choices[index].toString().length() > maxLength)
      {
        maxLength = choices[index].toString().length();
      }
    }
    setMaxLength(maxLength);
    enumHelper.setChoices(choices);
  }

  public boolean validateChoice(Object value)
  {
    return enumHelper.validateChoice(value);
  }


  public Datatype getElementType()
  {
    return characterType;
  }


  public void setPattern(String value)
  {
    pattern = value;
  }


  public void setElementType(Datatype value)
  {
    if ((value instanceof CharType)==false)
    {
      throw new IllegalArgumentException("Element type must be of Character type.");
    }
    characterType = (CharType) value;
  }



  public Object getObjectType()
  {
    return STRING_INSTANCE;
  }



  public Object parse(String value) throws ParseException
  {
    try
    {
      validate(value);
      value = normalizeWhitespaces(value,whitespace);
      return value;
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Error validating string.",0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Error validating string.",0);
    }
  }
  
  public static String normalizeWhitespaces(String value, String type)
  {
    int whitespaceType = 0;
    if (type.equals(WHITESPACE_PRESERVE))
    {
      return value;
    } else
    if (type.equals(WHITESPACE_REPLACE))
    {
      whitespaceType = StringUtilities.WHITESPACE_REPLACE;
    } else
      if (type.equals(WHITESPACE_COLLAPSE))
    {
        whitespaceType = StringUtilities.WHITESPACE_COLLAPSE;
    } else
    {
      throw new IllegalArgumentException("Invalid normalization type '"+type+"'");
    }
    return StringUtilities.normalizeWhitespaces(value,whitespaceType);
  }



  public String getWhitespace()
  {
    return whitespace;
  }



  public void setWhitespace(String whitespace)
  {
    if (whitespace.equals(WHITESPACE_PRESERVE))
    {
    } else
    if (whitespace.equals(WHITESPACE_REPLACE))
    {
    } else
      if (whitespace.equals(WHITESPACE_COLLAPSE))
    {
    } else
    {
      throw new IllegalArgumentException("Invalid normalization type '"+whitespace+"'");
    }
    this.whitespace = whitespace;
  }



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
    StringType stringType;
    if (!(obj instanceof StringType))
    {
      return false;
    }
    stringType = (StringType) obj;
    if (this.getMinLength() != stringType.getMinLength())
    {
      return false;
    }
    if (this.getElementType().equals(stringType.getElementType())==false)
    {
      return false;
    }
    if (this.getWhitespace().equals(stringType.getWhitespace())==false)
    {
      return false;
    }
    String pattern = getPattern();
    if ((pattern!=null) && (pattern.equals(stringType.getPattern())==false))
    {
      return false;
    }
    return true;
  }
  
  
  

}
