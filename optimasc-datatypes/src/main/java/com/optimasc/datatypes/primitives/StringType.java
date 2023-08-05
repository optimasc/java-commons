package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.derived.LatinCharType;
import com.optimasc.datatypes.derived.UCS2CharType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.utils.StringUtilities;


/** Datatype that represents a character string datatype. The actual encoding 
 *  of the string depends on the {@code characterType} property. By default, 
 *  it supports UCS-2 character encoding, even though UTF-16 support 
 *  is checked in the validation routine.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>BMPString</code> or <code>VisibleString</code> (depending on CharacterType) ASN.1 datatype</li>
 *   <li><code>characterstring</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>string</code> XMLSchema built-in datatype</li>
 *   <li><code>CHARACTER</code>,<code>CHARACTER VARYING</code>,<code>CHARACTER LARGE OBJECT</code>,
 *     <code>NATIONAL CHARACTER</code>,<code>NATIONAL CHARACTER VARYING</code> or <code>NATIONAL CHARACTER LARGE OBJECT</code> 
 *     in SQL2003</li>
 *  </ul>
 *  
 *  <p>By default, the allowed minimum length of the string type is 0 characters, and the default maximum length
 *  is {@code Integer#MAX_VALUE}, {@code pattern} and {@code choices} are set to null.</p>
 *  
 *  <p>Internally, values of this type are represented as {@link String}.</p>
 *
 * @author Carl Eric Codère
 */
public abstract class StringType extends SequenceType implements EnumerationFacet, Parseable, LengthFacet, PatternFacet, DatatypeConverter
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
  
  protected static final int HIGH_SURROGATE = 0xD800;
  protected static final int LOW_SURROGATE = 0xDC00;
  
  protected String pattern;
  
  protected String whitespace;

  protected EnumerationHelper enumHelper;
  protected LengthHelper lengthHelper;
  
  
  /** Creates a string type definition. This routine verifies the
   *  minLength and maxLength characters, if they are equal created
   *  a {@link Datatype#CHAR} type, otherwise it creates a 
   *  {@link Datatype#VARCHAR} type.   
   * 
   * @param minLength The minimum length of allowed characters
   * @param maxLength The maximum length of allowed characters
   * @param charType The type of characters
   */
  public StringType(int minLength, int maxLength, TypeReference charType)
  {
    super(Datatype.VARCHAR,false,charType);
    whitespace = WHITESPACE_PRESERVE;
    enumHelper = new EnumerationHelper(this);
    lengthHelper = new LengthHelper();
    if (minLength == maxLength)
    {
      this.type = Datatype.CHAR;
    }
    setMinLength(minLength);
    setMaxLength(maxLength);
  }
  

  public Class getClassType()
  {
     return String.class;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    String s;
    CharacterType charType = (CharacterType)elementType.getType(); 
    if (value instanceof char[])
    {
      char[] chArr = (char[])value;
      for (int i = 0; i < chArr.length; i++)
      {
        if (charType.isValidCharacter(chArr[i])==false)
        {
          DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_CHARACTER,"The string does not contain valid characters for this repertoire");
        }
      }
      if ((chArr.length < getMinLength()))
      {
        DatatypeException.throwIt(DatatypeException.ERROR_STRING_ILLEGAL_LENGTH,"The string length does not match the datatype specification, expected "+getMinLength()+" characters, got "+chArr.length);
      }
      if  (chArr.length > getMaxLength())
      {
        DatatypeException.throwIt(DatatypeException.ERROR_STRING_TRUNCATION,"The string length does not match the datatype specification, expected "+getMaxLength()+" characters, got "+chArr.length);
      }
      s = new String(chArr);
      validatePattern(s);
      if (validateChoice(s)==false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,"The string does not match the datatype specification");
      }
    }
    else
    if (value instanceof String)
    {
      String string = (String)value;
      int charCount = 0;
      while (charCount < string.length())
      {
        // Manually check for surrogate pairs
        int ch = string.charAt(charCount);

        // Reconstruct a full UCS-4 codepoint
        if ((ch & HIGH_SURROGATE)==HIGH_SURROGATE)
        {
          ch = (ch - HIGH_SURROGATE)*0x400;
          charCount++;
          ch = 0x10000+ch+(string.charAt(charCount)-LOW_SURROGATE);
        }
        
        if (charType.isValidCharacter(ch)==false)
        {
          DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_CHARACTER,"The string does not contain valid characters for this repertoire");
        }
        charCount++;
      }
      if ((charCount < getMinLength()))
      {
        DatatypeException.throwIt(DatatypeException.ERROR_STRING_ILLEGAL_LENGTH,"The string length does not match the datatype specification, expected "+getMinLength()+" characters, got "+charCount);
      }
      if  (charCount > getMaxLength())
      {
        DatatypeException.throwIt(DatatypeException.ERROR_STRING_TRUNCATION,"The string length does not match the datatype specification, expected "+getMaxLength()+" characters, got "+charCount);
      }
      validatePattern(string);
      if (validateChoice(string)==false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,"The string does not match the datatype specification");
      }
    } else
    {
      throw new IllegalArgumentException("Invalid object type - should be String instance");
    }
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


  public void setPattern(String value)
  {
    pattern = value;
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
    if (this.getMaxLength() != stringType.getMaxLength())
    {
      return false;
    }
    if (this.getBaseTypeReference().equals(stringType.getBaseTypeReference())==false)
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
