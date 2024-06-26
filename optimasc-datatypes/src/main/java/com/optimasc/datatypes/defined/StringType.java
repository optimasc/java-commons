package com.optimasc.datatypes.defined;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.CharacterSetEncodingFacet;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.lang.CharacterSet;
import com.optimasc.text.StringUtilities;


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
public abstract class StringType extends SequenceType implements CharacterSetEncodingFacet,EnumerationFacet, PatternFacet
{
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
  
  
  /** Creates a string type definition. This routine verifies the
   *  minLength and maxLength characters, if they are equal created
   *  a {@link Datatype#CHAR} type, otherwise it creates a 
   *  {@link Datatype#VARCHAR} type.   
   * 
   * @param minLength The minimum length of allowed characters
   * @param maxLength The maximum length of allowed characters
   * @param charType The type reference that should point to a <code>CharacterType</code>
   *   type.
   */
  public StringType(int minLength, int maxLength, TypeReference charType)
  {
    super(Datatype.VARCHAR,minLength,maxLength,charType);
    if ((charType.getType() instanceof CharacterType)==false)
    {
       throw new IllegalArgumentException("The type reference should point to '"+charType.getType().getClass().getName()+"'"
            + " but points to '"+charType.getType().getClass().getName()+"'.");   
    }
    whitespace = WHITESPACE_PRESERVE;
    enumHelper = new EnumerationHelper(this);
    if (minLength == maxLength)
    {
      this.type = Datatype.CHAR;
    }
  }
  
  /** Creates a string type definition with no length bounds. 
   *  It creates a {@link Datatype#CLOB} type.   
   * 
   * @param charType The type reference that should point to a <code>CharacterType</code>
   *   type.
   */
  public StringType(TypeReference charType)
  {
    super(Datatype.CLOB,charType);
    if ((charType.getType() instanceof CharacterType)==false)
    {
       throw new IllegalArgumentException("The type reference should point to '"+charType.getType().getClass().getName()+"'"
            + " but points to '"+charType.getType().getClass().getName()+"'.");   
    }
    whitespace = WHITESPACE_PRESERVE;
    enumHelper = new EnumerationHelper(this);
  }
  

  public Class getClassType()
  {
     return String.class;
  }


  /** {@inheritDoc}
   * 
   *  <p>This verification and conversion routine supports the following inputs:
   *   <ul>
   *     <li><code>char[]</code>, including support for surrogate pairs.</li>
   *     <li><code>CharSequence</code>, including support for surrogate pairs.</li>
   *     <li><code>int[]</code>, where each element is considered a unicode codepoint.</li>
   *   </ul>
   *   The output value is a <code>String</code> that also includes any surrogate
   *   pairs in UTF-16 encoding, as required, if the character cannot be represented
   *   in one character.
   *  </p>
   * 
   */
  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    StringBuffer buffer = new StringBuffer(32);
    int charCount = 0;
    CharacterType charType = (CharacterType)elementType.getType();
    conversionResult.reset();
    if (value instanceof char[])
    {
      char[] chArr = (char[])value;
      int charIndex = 0;
      while (charIndex < chArr.length)
      {
        // Manually check for surrogate pairs
        int ch = chArr[charIndex];
        buffer.append((char)ch);

        // Reconstruct a full UCS-4 codepoint
        if ((ch & HIGH_SURROGATE)==HIGH_SURROGATE)
        {
          ch = (ch - HIGH_SURROGATE)*0x400;
          charIndex++;
          int ch1 = chArr[charIndex];
          buffer.append((char)ch1);
          ch = 0x10000+ch+(ch1-LOW_SURROGATE);
        }
        if (charType.toValue(ch,conversionResult)==null)
        {
          return null;
        }
        charIndex++;
        charCount++;
      }
    }
    else
    if (value instanceof CharSequence)
    {
      CharSequence string = (CharSequence)value;
      int charIndex = 0;
      while (charIndex < string.length())
      {
        // Manually check for surrogate pairs
        int ch = string.charAt(charIndex);
        buffer.append((char)ch);

        // Reconstruct a full UCS-4 codepoint
        if ((ch & HIGH_SURROGATE)==HIGH_SURROGATE)
        {
          ch = (ch - HIGH_SURROGATE)*0x400;
          charIndex++;
          int ch1 = string.charAt(charIndex);
          buffer.append((char)ch1);
          ch = 0x10000+ch+(ch1-LOW_SURROGATE);
        }
        
        if (charType.toValue(ch,conversionResult)==null)
        {
          return null;
        }
        charIndex++;
        charCount++;
      }
    } else
    if (value instanceof int[])
    {
      // Each int value is a unicode codepoint.
      int[] chArr = (int[])value;
      int charIndex = 0;
      charCount = chArr.length;
      for (int i=0; i < chArr.length; i++)
      {
        int ch = chArr[i];
        if (charType.toValue(ch,conversionResult)==null)
        {
          return null;
        }
        char[] result = CharacterSet.toChars(ch);
        buffer.append(result);
      }
    } else
    {
      throw new IllegalArgumentException("Invalid object type - should be String instance");
    }
    
    String string = buffer.toString();
    if (lengthHelper.validateLength(charCount)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_BOUNDS_RANGE,
          "The sequence length does not match the datatype specification, " + lengthHelper.toString()
          +", got "+charCount);
      return null;
    }
    validatePattern(string);
    if (validateChoice(string)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NO_SUBCLASS,"The string does not match the datatype specification");
      return null;
    }
    return string;
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
  public boolean validatePattern(CharSequence value)
  {
    return true;
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
    /* Set the minimum and maximum length with choices */
    for (index = 0; index < choices.length; index++)
    {
      if (choices[index].toString().length() > maxLength)
      {
        maxLength = choices[index].toString().length();
      }
    }
    lengthHelper.setLength(0, maxLength);
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


  /** Returns the character set associated 
   *  with the character type. 
   * 
   */
  public CharacterSet getCharacterSet()
  {
    return ((CharacterType)elementType.getType()).getCharacterSet();
  }


}
