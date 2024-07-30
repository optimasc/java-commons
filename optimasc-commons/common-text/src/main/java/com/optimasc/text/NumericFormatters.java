package com.optimasc.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.Locale;

/*
 * 
 * See License.txt for more information on the licensing terms
 * for this source code.
 * 
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * OPTIMA SC INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

/** Classes that permit formatting and parsing numbers of different formats. This
 *  includes error checking based on {@link java.text.Format}.
 * 
.  The return values of the {@link java.text.Format#parseObject(String)}
 * classes are guaranteed to by of type {@link java.lang.Number}. 
 * 
 * The module is completely compatible with CDC 1.1.
 * 
 * <p>Most of the classes herein support parsing in lenient mode, where the parser
 * will try to convert the data to the correct output Object if not necessarily
 * 100% compliant with the syntax. See individual class documentation for more
 * information.</p>
 *
 * It is to note that as much as possible, the binding from string representation
 * follows the JAXB 2.0 specification (JSR 222).
 * 
 * The recommended classes to use when no range is specified and required are 
 * {@link NumericFormatters.IntegerCanonicalConverter} and 
 * {@link NumericFormatters.nonNegativeIntegerConverter}.
 *
 * @author Carl Eric Codere
 */
public class NumericFormatters
{
  // Do not instantiate
  protected NumericFormatters()
  {
    
  }

  protected abstract static class NumberConverter extends DataConverter
  {
    protected NumberConverter(Class clz, boolean lenient)
    {
      super(clz, lenient);
    }
    
    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }
  }
  
  /** Abstract class for integer number formatting and parsing.
   * 
   */
  public abstract static class IntegerNumberConverter extends NumberConverter
  {
    protected long minValue;
    protected long maxValue;
    
    /** 
     * 
     * @param clz [in] The class associated with this java object representation. 
     * @param lenient [in] Lenient parsing mode or not.
     * @param minValue [in] minimum allowed range of value.
     * @param maxValue [in] maximum allowed range of value.
     */
    protected IntegerNumberConverter(Class clz, boolean lenient, long minValue, long maxValue)
    {
      super(clz, lenient);
      this.minValue = minValue;
      this.maxValue = maxValue;
    }

    /** Returns the numeric value that was parsed as a <code>long</code>. This
     *  method also verifies if the value is within the allowed range.
     * 
     * @param value [in] The character sequence to parse.
     * @return The numeric value.
     * @throws ParseException Thrown if the parsed value does not
     *  represent a number, or if the value is outside the allowed
     *  range. 
     */
    protected long checkValue(CharSequence value) throws ParseException
    {
      long result;
      // StrictCheck
      // Don't allow plus sign
      // Allow Minus sign
      // Do not allow leading zero
      if (lenient == false)
      {
        result = Parsers.parseNumber(value, 0, value.length(), false,
            true, false);
      } else
      // Lenient, accept everything
      {
        result = Parsers.parseNumber(value, 0, value.length(), true, true,
            true);
      }
      if ((result < minValue) || (result > maxValue))
      {
        throw new ParseException("Value is not within "+Long.toString(minValue)+" and "+Long.toString(maxValue),0);
      }
      return result;
    }
    
    

    /** Converts an object to its numeric representation, without taking
     *  locale information. The value Object must be of type {@link java.lang.Number}
     *  and the actual number must be within the allowed range. There may
     *  be a loss of precision when converting the Number to its numeric
     *  representation, as {@link java.lang.Number#longValue()} is called
     *  to convert the value before converting it to its string representation.
     *  
     *  @param value [in] The object representing this numeric value.
     *  @param toAppendTo [in, out] Where the formatted output will
     *   be appended to. 
     *  @param pos [in] Not used. 
     *   
     *  @throws IllegalArgumentException If the object is not of instance
     *   {@link java.lang.Number} or if the number is not within allowed
     *   range for this numeric type.
     */
    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof Number) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      long intResult = ((Number)value).longValue();
      if ((intResult < minValue) || (intResult > maxValue))
      {
        throw new IllegalArgumentException("Value is not within "+Long.toString(minValue)+" and "+Long.toString(maxValue));
      }
      return toAppendTo.append(value.toString());
    }
    
    
  }
  
  
  
  
  /**
   * Integer type converter. The syntax is the one supported by the Java
   * Language specification in decimal notation. This format is also compliant
   * with the following syntaxes:
   * <ul>
   * <li>ISO/IEC 11404 (2007)</li>
   * <li>SQL99 <signed integer> syntax</li>
   * </ul>
   */
  public static class IntegerConverter extends NumberConverter
  {
    public IntegerConverter()
    {
      super(BigInteger.class, false);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      // Don't allow plus sign
      // Allow Minus sign
      // Allow leading zero
      return BigInteger.valueOf(Parsers.parseNumber(value, 0, value.length(), false,
          true, true));
    }

    public Object parseObject(String value) throws ParseException
    {
      // Don't allow plus sign
      // Allow Minus sign
      // Allow leading zero
      return BigInteger.valueOf(Parsers.parseNumber(value, 0, value.length(), false,
          true, true));
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof BigInteger) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      return toAppendTo.append(value.toString());
    }
  }

  /**
   * Generic signed integer formatter and parser. This converter verifies if the numeric value is of
   * the following syntax:
   * 
   * <p>
   * <code>[-]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * In canonical representation (non-lenient) parsing mode, if a '+' 
   * sign or leading 0 digits are present, conversion will fail. 
   * In <code>lenient</code> mode, the above are accepted without 
   * raising an error.
   * 
   * <p>Based on the range of the parsed value, it can either convert the
   * value to a {@link java.lang.Long} or {@link java.math.BigInteger} if
   * the value does not fit within the range of an Integer. <em>Because of this,
   * do not rely on {@link #getClassType()} to get the actual class type 
   * returned.</em></p> 
   * 
   * 
   * When <code>lenient</code> is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>ASN.1 ITU X.680</li>
   * <li>LDAP IETF RFC 4517 (OID: 1.3.6.1.4.1.1466.115.121.1.27)</li>
   * <li>W3C XML Schema Second Edition (2004) <code>integer</code> built-in datatype canonical representation</li>
   * </ul>
   * 
   * When <code>lenient</code> is <code>true</code> the converter is also compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>integer</code> built-in datatype lexical representation</li>
   * <li>vCard IETF RFC 6350 INTEGER type</li>
   * <li>XMP ISO 16684-1:2011</li>
   * <li>Pascal Language Syntax</li>
   * </ul>
   */
  public static class IntegerCanonicalConverter extends NumberConverter
  {
    public IntegerCanonicalConverter()
    {
      super(BigInteger.class, true);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      BigInteger result = null;
      // StrictCheck
      // Don't allow plus sign
      // Allow Minus sign
      // Do not allow leading zero
      if (lenient == false)
      {
        result = Parsers.parseNumberAsBigInteger(value, 0, value.length(), false,
            true, false);
      } else
      {
        // Lenient, accept everything except minus sign
        result = Parsers.parseNumberAsBigInteger(value, 0, value.length(), true,
            true, true);
      }
      // If it fits in 62 bits then convert it to a Long value.
      if (result.bitLength() < 63)
      {
        return new Long(result.longValue());
      }
      return result;
      
    }

    /** Converts an Integer to its string representation. The string output
     *  is output as its canonical presentation.
     *  
     *  <p>The value passed should be of type {@link java.lang.Number}. Furthermore,
     *  the following rules are applied to create the string representation of the
     *  number:</p>
     *  
     *  <ul>
     *   <li>If the value represents a decimal value (<code>BigDecimal</code>, double 
     *   <code>Double</code> or single <code>Float</code> object, and if the scale (the
     *   value does not represent a full integer value) is different than 0, then
     *   an exception will be thrown.</li>
     *   <li>If the value is a <code>Number</code> instance but not one of 
     *    the standard Number types in the java standard runtime library (
     *    <code>Byte</code>, <code>Integer</code>, <code>BigInteger</code>...)
     *    then some precision may be lost, as the value will be converted
     *    to a long value before being returned as a string.</li> 
     *  </ul>  
     *  
     *   @param value [in] The object representing the integer.
     *   @param toAppendTo [in,out] Where the string canonical representation will
     *     be appended to.
     *   @param pos [in] Not used.
     *   @throws IllegalArgumentException Thrown if the value does not represent
     *    an integer value.
     */
    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof Number) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + Number.class.getName() + "'");
      }
      // Convert to BigDecimal to get scale information
      // Pass through to next step
      if (value instanceof Double)
      {
        Double realValue = (Double)value;
        value = new BigDecimal(realValue.doubleValue());
      }
      
      // Convert to BigDecimal to get scale information
      // Pass through to next step
      if (value instanceof Float)
      {
        Float realValue = (Float)value;
        value = new BigDecimal(realValue.doubleValue());
      }
      
      // Check if decimal values, otherwise go to next step
      if (value instanceof BigDecimal)
      {
        BigDecimal number = (BigDecimal) value;
        if (number.scale() != 0)
        {
          throw new IllegalArgumentException("Object value is non an integer numeric value");
        }
        value = number.toBigInteger();
      }
      
      
      // Check if sign is valid.
      if (value instanceof BigInteger)
      {
        BigInteger number = (BigInteger) value;
        return toAppendTo.append(number.toString());
      }
      
      // Long value only - just in case if we have another datatype
      return toAppendTo.append(Long.toString(((Number)value).longValue()));
    }

  }
  
  
  /** Signed byte integer type (byte) formatter and parser. The allowed
   *  numeric value range is between -128..127 inclusive.   
   *  
   *  The parsing verifies if the numeric value is of the following syntax:
   * 
   * <p>
   * <code>[-]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * and if so converts it to an {@link java.lang.Byte} object. In canonical
   * representation (non-lenient) parsing mode, if a '+' sign or leading 0
   * digits are present, conversion will fail. In lenient mode, the
   * above characters are accepted without raising an error. When formatting,
   * the value is verified to see if it fits within the allowed range.
   * 
   * When lenient mode is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>byte</code> built-in datatype canonical representation</li>
   * <li>
   * </ul>
   * 
   * When lenient is <code>true</code> the converter is compliant with the 
   * following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>byte</code> built-in datatype lexical representation</li>
   * </ul>
   */
  public static class ByteConverter extends IntegerNumberConverter
  {
    public ByteConverter()
    {
      super(Byte.class, true, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return new Byte((byte)checkValue(value));
    }
  }
  
  
  /** Signed short integer type (short) formatter and parser. The allowed
   *  numeric value range is between -32768..32767 inclusive.   
   *  
   *  The parsing verifies if the numeric value is of the following syntax:
   * 
   * <p>
   * <code>[-]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * and if so converts it to an {@link java.lang.Short} object. In canonical
   * representation (non-lenient) parsing mode, if a '+' sign or leading 0
   * digits are present, conversion will fail. In lenient mode, the
   * above characters are accepted without raising an error. When formatting,
   * the value is verified to see if it fits within the allowed range.
   * 
   * When lenient mode is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>short</code> built-in datatype canonical representation</li>
   * <li>
   * </ul>
   * 
   * When lenient is <code>true</code> the converter is compliant with the 
   * following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>short</code> built-in datatype lexical representation</li>
   * </ul>
   */
  public static class ShortConverter extends IntegerNumberConverter
  {
    public ShortConverter()
    {
      super(Short.class, true,Short.MIN_VALUE,Short.MAX_VALUE);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return new Short((short)checkValue(value));
    }
  }
  
  

  /** Signed integer type (int) formatter and parser. The allowed
   *  numeric value range is between -2147483648..2147483647 inclusive.   
   *  
   *  The parsing verifies if the numeric value is of the following syntax:
   * 
   * <p>
   * <code>[-]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * and if so converts it to an {@link java.lang.Integer} object. In canonical
   * representation (non-lenient) parsing mode, if a '+' sign or leading 0
   * digits are present, conversion will fail. In lenient mode, the
   * above characters are accepted without raising an error. When formatting,
   * the value is verified to see if it fits within the allowed range.
   * 
   * When lenient mode is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>int</code> built-in datatype canonical representation</li>
   * <li>
   * </ul>
   * 
   * When lenient is <code>true</code> the converter is compliant with the 
   * following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>int</code> built-in datatype lexical representation</li>
   * </ul>
   */
  public static class IntConverter extends IntegerNumberConverter
  {
    public IntConverter()
    {
      super(Integer.class, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return new Integer((int)checkValue(value));
    }
  }
  
  
  /** Signed long integer type (long) formatter and parser. The allowed
   *  numeric value range is between -9223372036854775808..9223372036854775807 inclusive.   
   *  
   *  The parsing verifies if the numeric value is of the following syntax:
   * 
   * <p>
   * <code>[-]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * and if so converts it to an {@link java.lang.Long} object. In canonical
   * representation (non-lenient) parsing mode, if a '+' sign or leading 0
   * digits are present, conversion will fail. In lenient mode, the
   * above characters are accepted without raising an error. When formatting,
   * the value is verified to see if it fits within the allowed range.
   * 
   * When lenient mode is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>long</code> built-in datatype canonical representation</li>
   * <li>
   * </ul>
   * 
   * When lenient is <code>true</code> the converter is compliant with the 
   * following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>long</code> built-in datatype lexical representation</li>
   * </ul>
   */
  public static class LongConverter extends IntegerNumberConverter
  {
    public LongConverter()
    {
      super(Long.class, true, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return new Long(checkValue(value));
    }
  }
  
  /**
   * Generic Non-negative integer formatter and parser. This class 
   * caters to an unlimited value ranges that are equal to 0 or positive integers. 
   * 
   * This parser verifies if the numeric value is of the following syntax:
   * 
   * <p>
   * <code>[+]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * <p>Based on the range of the parsed value, it can either convert the
   * value to a {@link java.lang.Long} or {@link java.lang.BigInteger} if
   * the value does not fit within the range of an Integer. <em>Because of this,
   * do not rely on {@link #getClassType()} to get the actual class type 
   * returned.</em></p> 
   * 
   * <p>In canonical representation (non-lenient) parsing mode, if a '+' sign or leading 0
   * digits are present, conversion will fail. In lenient mode, the
   * above are accepted without raising an error.</p>
   * 
   * When lenient mode is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>nonNegativeInteger</code> built-in datatype canonical
   * representation</li>
   * </ul>
   * 
   * When lenient mode is <code>true</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>nonNegativeInteger</code> built-in datatype lexical
   * representation</li>
   * </ul>
   */
  public static class nonNegativeIntegerConverter extends NumberConverter
  {
    public nonNegativeIntegerConverter()
    {
      super(BigInteger.class, true);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      BigInteger result = null;
      // StrictCheck
      // Don't allow plus sign
      // Do not allow Minus sign
      // Do not allow leading zero
      if (lenient == false)
      {
        result = Parsers.parseNumberAsBigInteger(value, 0, value.length(), false,
            false, false);
      } else
      {
        // Lenient, accept everything except minus sign
        result = Parsers.parseNumberAsBigInteger(value, 0, value.length(), true,
            false, true);
      }
      // If it fits in 62 bits then convert it to a Long value.
      if (result.bitLength() < 63)
      {
        return new Long(result.longValue());
      }
      return result;
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof Number) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + Number.class.getName() + "'");
      }
      // Convert to BigDecimal to get scale information
      // Pass through to next step
      if (value instanceof Double)
      {
        Double realValue = (Double)value;
        value = new BigDecimal(realValue.doubleValue());
      }
      
      // Convert to BigDecimal to get scale information
      // Pass through to next step
      if (value instanceof Float)
      {
        Float realValue = (Float)value;
        value = new BigDecimal(realValue.doubleValue());
      }
      
      // Check if decimal values, otherwise go to next step
      if (value instanceof BigDecimal)
      {
        BigDecimal number = (BigDecimal) value;
        if (number.scale() != 0)
        {
          throw new IllegalArgumentException("Object value is non an integer numeric value");
        }
        value = number.toBigInteger();
      }
      
      
      // Check if sign is valid.
      if (value instanceof BigInteger)
      {
        BigInteger number = (BigInteger) value;
        if (number.signum()==-1)
        {
          throw new IllegalArgumentException("Object value is negative, which is not allowed");
        }
        return toAppendTo.append(value.toString());
      }
      
      // Long value only - just in case if we have another datatype
      return toAppendTo.append(Long.toString(((Number)value).longValue()));
    }
  }
  
  
  /** Unsigned byte integer type (octet) formatter and parser. The allowed
   *  numeric value range is between 0..255 inclusive.   
   *  This converter verifies if the numeric value is of the following syntax:
   * 
   * <p>
   * <code>[+]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * and if so converts it to an {@link java.lang.Short} object. In canonical
   * representation (non-lenient) parsing mode, if a '+' sign or leading 0
   * digits are present, conversion will fail. In <code>lenient</code> mode, the
   * above are accepted without raising an error.
   * 
   * When lenient is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>unsignedByte</code> built-in datatype canonical
   * representation</li>
   * <li>
   * </ul>
   * 
   * When lenient is <code>true</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>unsignedByte</code> built-in datatype lexical
   * representation</li>
   * </ul>
   */
  public static class unsignedByteConverter extends IntegerNumberConverter
  {
    public unsignedByteConverter()
    {
      super(Short.class, true,0,255);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return new Short((short)(checkValue(value) & 0xFF));
    }
  }
  
  /** Unsigned short integer type formatter and parser. The allowed
   *  numeric value range is between 0..65535 inclusive.   
   *  This converter verifies if the numeric value is of the following syntax:
   * 
   * <p>
   * <code>[+]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * and if so converts it to an {@link java.lang.Integer} object. In canonical
   * representation (non-lenient) parsing mode, if a '+' sign or leading 0
   * digits are present, conversion will fail. In lenient mode, the
   * above are accepted without raising an error.
   * 
   * When lenient mode is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>unsignedShort</code> built-in datatype canonical
   * representation</li>
   * <li>
   * </ul>
   * 
   * When lenient mode is <code>true</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>unsignedShort</code> built-in datatype lexical
   * representation</li>
   * </ul>
   */
  public static class unsignedShortConverter extends IntegerNumberConverter
  {
    public unsignedShortConverter()
    {
      super(Integer.class, true,0,65535);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return new Integer((short)(checkValue(value) & 0xFFFF));
    }
  }
  
  
  /** Unsigned int integer type formatter and parser. The allowed
   *  numeric value range is between 0..4294967295 inclusive.   
   *  
   *  This converter verifies if the numeric value is of the following syntax:
   * 
   * <p>
   * <code>[+]?(([0-9])|([1-9][0-9]+))</code>
   * </p>
   * 
   * and if so converts it to an {@link java.lang.Long} object. In canonical
   * representation (non-lenient) parsing mode, if a '+' sign or leading 0
   * digits are present, conversion will fail. In <code>lenient</code> mode, the
   * above are accepted without raising an error.
   * 
   * When lenient mode is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>unsignedInt</code> built-in datatype canonical
   * representation</li>
   * <li>
   * </ul>
   * 
   * When lenient mode is <code>true</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) <code>unsignedInt</code> built-in datatype lexical
   * representation</li>
   * </ul>
   */
  public static class unsignedIntConverter extends IntegerNumberConverter
  {
    public unsignedIntConverter()
    {
      super(Long.class, true,0,4294967295L);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return new Long((long)(checkValue(value) & 0xFFFFFFFFL));
    }
  }
  
}
