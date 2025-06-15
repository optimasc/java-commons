package com.optimasc.datatypes.defined;

import java.text.ParseException;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.CharacterSetEncodingFacet;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.CharacterSet;
import com.optimasc.text.DataConverter;
import com.optimasc.text.StringUtilities;


/** Datatype that represents a character string datatype. The actual encoding 
 *  of the string depends on the {@code characterType} property. By default, 
 *  it supports ASCII character encoding. This class also permits to do
 *  manual validation using the <code>com.optimasc.text.DataConverter</code> class, in this
 *  case the {@link com.optimasc.text.DataConverter#parseObject(CharSequence)} method
 *  is called to validate the input, and the format should return a string.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>BMPString</code>, <code>IA5String</code>, <code>UniversalString</code> or <code>VisibleString</code> (depending on CharacterType) ASN.1 datatype</li>
 *   <li><code>characterstring</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>string</code> XMLSchema built-in datatype</li>
 *   <li><code>CHARACTER</code>,<code>CHARACTER VARYING</code>,<code>CHARACTER LARGE OBJECT</code>,
 *     <code>NATIONAL CHARACTER</code>,<code>NATIONAL CHARACTER VARYING</code> or <code>NATIONAL CHARACTER LARGE OBJECT</code> 
 *     in SQL2003</li>
 *  </ul>
 *  
 *  <p>The default implementation when the character type is not specified is equivalent to</p>
 *  <ul>
 *   <li><code>IA5String(Size(1))</code> ASN.1 datatype</li>
 *   <li><code>character(1.3.6.1.4.1.1466.115.121.1.26)</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>CHARACTER(1) CHARACTER SET ISO8BIT</code> in SQL2003</li>
 *  </ul>
 *  </ul>
 *  
 *  <p>By default, the allowed minimum length of the string type is 0 characters, and the default maximum length
 *  is {@code Integer#MAX_VALUE}, {@code pattern} and {@code choices} are set to null.</p>
 *  
 *  <p>Internally, values of this type are represented as a <code>String</code> object.</p>
 *  
 *  <p>This class also permits to return a string with whitespace normalisation, which can
 *  be configured by calling {@link #setWhitespace(String)}. By default the setting is
 *  set to {@link #WHITESPACE_PRESERVE}.
 *  </p>
 *
 * @author Carl Eric Codère
 */
public class StringType extends SequenceType implements CharacterSetEncodingFacet,EnumerationFacet
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
  

  protected String whitespace;
  protected DataConverter formatter;

  /** Creates a default string type definition. The default string type
   *  definition supports the full US-ASCII character set, including
   *  control characters.
   * 
   */
  public StringType()
  {
    this(0,Integer.MIN_VALUE,new UnnamedTypeReference(new CharacterType(CharacterSet.ASCII)));
    whitespace = WHITESPACE_PRESERVE;
    formatter = null;
  }

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
    super(minLength,maxLength,charType, String.class);
    if ((charType.getType() instanceof CharacterType)==false)
    {
       throw new IllegalArgumentException("The type reference should point to '"+charType.getType().getClass().getName()+"'"
            + " but points to '"+charType.getType().getClass().getName()+"'.");   
    }
    formatter = null;
    whitespace = WHITESPACE_PRESERVE;
  }
  
  /** Creates a string type definition with no length bounds. 
   *  It creates a {@link Datatype#CLOB} type.   
   * 
   * @param charType The type reference that should point to a <code>CharacterType</code>
   *   type.
   */
  public StringType(TypeReference charType)
  {
    super(charType, String.class);
    if ((charType.getType() instanceof CharacterType)==false)
    {
       throw new IllegalArgumentException("The type reference should point to '"+charType.getType().getClass().getName()+"'"
            + " but points to '"+charType.getType().getClass().getName()+"'.");   
    }
    formatter = null;
    whitespace = WHITESPACE_PRESERVE;
  }
  
  /** Creates a string that must have values equal
   *  to one the specified selection values. The
   *  minimum length and maximum length is automatically
   *  calculated from the selection values.
   *
   * @param choices [in] A list of allowed values, where
   *  the choices elements must be of type <code>resultType</code>.
   * @param baseType [in] The base type of the sequence.
   * @throws IllegalArgumentException If the actual value 
   *  of choices elements is not of the correct java object
   *  type or if they do not meet the constraints of <code>baseType</code>.
   */
  public StringType(Object choices[], TypeReference baseType)
  {
    super(choices,baseType,String.class);
    if ((baseType.getType() instanceof CharacterType)==false)
    {
       throw new IllegalArgumentException("The type reference should point to '"+baseType.getType().getClass().getName()+"'"
            + " but points to '"+baseType.getType().getClass().getName()+"'.");   
    }
    formatter = null;
    whitespace = WHITESPACE_PRESERVE;
  }

  /** Creates a string that must match the formatter. 
   *  The
   *  minimum length and maximum length is automatically
   *  calculated from the selection values.
   *
   * @param validator [in] A string pattern validator.
   * @param baseType [in] The base type of the sequence.
   * @throws IllegalArgumentException If the actual value 
   *  of choices elements is not of the correct java object
   *  type or if they do not meet the constraints of <code>baseType</code>.
   */
  public StringType(DataConverter validator, TypeReference baseType)
  {
    super(baseType,String.class);
    if ((baseType.getType() instanceof CharacterType)==false)
    {
       throw new IllegalArgumentException("The type reference should point to '"+baseType.getType().getClass().getName()+"'"
            + " but points to '"+baseType.getType().getClass().getName()+"'.");   
    }
    whitespace = WHITESPACE_PRESERVE;
    formatter = validator;
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
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
      return null;
    }
    
    String string = buffer.toString();
    if (lengthHelper.validateLength(charCount)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_BOUNDS_RANGE,
          "The sequence length does not match the datatype specification, " + lengthHelper.toString()
          +", got "+charCount);
      return null;
    }
    if (validateFormat(string)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"The string does not match the datatype specification");
      return null;
    }
    if (validateChoice(string)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"The string does not match the datatype specification");
      return null;
    }
    string =normalizeWhitespaces(string,whitespace);
    return string;
  }


  /** Validates if the value of string is within the allowed pattern name
   *  of allowed values.
   * 
   * @param value The value to check
   * @return true if the validation is successful, otherwise false.
   */
  public boolean validateFormat(CharSequence value)
  {
    if (formatter==null)
      return true;
    try 
    {
     Object res = formatter.parseObject(value);
     if (res.getClass().isAssignableFrom(String.class)==false)
     {
       throw new IllegalArgumentException("Invalid data converter class, it does not return a class of type '"+getClassType().getName()+"'.");
     }
    } catch (ParseException e)
    {
      return false;
    }
    return true;
  }
  
  public DataConverter getFormatValidator()
  {
    return formatter;
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
    boolean b = super.equals(obj);
    if (b==false)
      return b;
    StringType stringType = (StringType) obj;
    if (this.getWhitespace().equals(stringType.getWhitespace())==false)
    {
      return false;
    }
    DataConverter dataConverter = stringType.getFormatValidator();
    if (dataConverter != formatter)
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

  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }

}
