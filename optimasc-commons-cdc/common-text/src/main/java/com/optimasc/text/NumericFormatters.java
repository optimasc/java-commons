package com.optimasc.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParseException;

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
 * <p>
 * The following table lists the converter of each numeric type name of the
 * Schema Value Representation Specification. The integer converters accept
 * the XML Schema lexical representation by default (<code>getInstance()</code>,
 * lenient); the instances returned by <code>getLDAPInstance()</code> (not
 * lenient) accept only the IETF RFC 4517 <code>INTEGER</code> syntax: an
 * optional <code>'-'</code>, no <code>'+'</code>, no leading zeros and no
 * negative zero. This is also the canonical representation of XML Schema.
 * </p>
 *
 * <table border="1">
 * <tr><th>LDAP syntax</th><th>XML Schema type</th><th>Converter</th><th>Value class</th></tr>
 * <tr><td>INTEGER</td><td>integer</td><td>{@link IntegerCanonicalConverter}</td><td>BigInteger</td></tr>
 * <tr><td>Int8</td><td>byte</td><td>{@link ByteConverter}</td><td>Integer</td></tr>
 * <tr><td>Int16</td><td>short</td><td>{@link ShortConverter}</td><td>Integer</td></tr>
 * <tr><td>Int32</td><td>int</td><td>{@link IntConverter}</td><td>Integer</td></tr>
 * <tr><td>Int64</td><td>long</td><td>{@link LongConverter}</td><td>Long</td></tr>
 * <tr><td>UInt8</td><td>unsignedByte</td><td>{@link unsignedByteConverter}</td><td>Integer</td></tr>
 * <tr><td>UInt16</td><td>unsignedShort</td><td>{@link unsignedShortConverter}</td><td>Integer</td></tr>
 * <tr><td>UInt32</td><td>unsignedInt</td><td>{@link unsignedIntConverter}</td><td>Long</td></tr>
 * <tr><td>UInt64</td><td>unsignedLong</td><td>{@link unsignedLongConverter}</td><td>BigInteger</td></tr>
 * <tr><td>Percentage</td><td>-</td><td>{@link PercentageConverter}</td><td>Integer</td></tr>
 * <tr><td>-</td><td>nonNegativeInteger</td><td>{@link nonNegativeIntegerConverter}</td><td>BigInteger</td></tr>
 * <tr><td>Real</td><td>-</td><td>{@link RealConverter}</td><td>Double</td></tr>
 * <tr><td>Float64</td><td>-</td><td>{@link Float64Converter}</td><td>Double</td></tr>
 * <tr><td>Float32</td><td>-</td><td>{@link Float32Converter}</td><td>Float</td></tr>
 * <tr><td>-</td><td>double</td><td>{@link XSDDoubleConverter}</td><td>Double</td></tr>
 * <tr><td>-</td><td>float</td><td>{@link XSDFloatConverter}</td><td>Float</td></tr>
 * <tr><td>-</td><td>decimal</td><td>{@link XSDDecimalConverter}</td><td>BigDecimal</td></tr>
 * </table>
 *
 * <p>
 * For the XML Schema integer types, the classes whose name starts with
 * <code>XSD</code> (such as {@link XSDIntConverter}) are the lenient
 * converters of the table above under the name of the XML Schema type.
 * </p>
 *
 * <p>
 * The shared instances MUST NOT be reconfigured.
 * </p>
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
  
  /**
   * Abstract class for integer number formatting and parsing with a range of
   * allowed values.
   *
   * <p>
   * When <code>lenient</code> is <code>true</code>, which is the default, the
   * lexical representation of XML Schema is accepted: an optional
   * <code>'+'</code> or <code>'-'</code> sign followed by one or more digits,
   * with leading zeros allowed. When <code>lenient</code> is
   * <code>false</code>, only the IETF RFC 4517 <code>INTEGER</code> syntax is
   * accepted, which is also the canonical representation of XML Schema:
   * </p>
   *
   * <p>
   * <code>-?(0|[1-9][0-9]*)</code>, excluding <code>-0</code>
   * </p>
   *
   * <p>
   * Values outside of the range are rejected in both modes. Formatting always
   * writes the canonical representation.
   * </p>
   */
  public abstract static class IntegerNumberConverter extends NumberConverter
  {
    /** Lowest value allowed, limited to the range of a long. */
    protected long minValue;
    /** Highest value allowed, limited to the range of a long. */
    protected long maxValue;
    /** Lowest value allowed. */
    protected final BigInteger minBound;
    /** Highest value allowed. */
    protected final BigInteger maxBound;

    protected IntegerNumberConverter(Class clz, boolean lenient, long minValue, long maxValue)
    {
      this(clz, lenient, BigInteger.valueOf(minValue), BigInteger.valueOf(maxValue));
    }

    protected IntegerNumberConverter(Class clz, boolean lenient, BigInteger minValue,
        BigInteger maxValue)
    {
      super(clz, lenient);
      // A null bound means that there is no limit in that direction.
      this.minBound = minValue;
      this.maxBound = maxValue;
      this.minValue = ((minValue == null) || (minValue.bitLength() > 63)) ? Long.MIN_VALUE
          : minValue.longValue();
      this.maxValue = ((maxValue == null) || (maxValue.bitLength() > 63)) ? Long.MAX_VALUE
          : maxValue.longValue();
    }

    /**
     * Parses the value and verifies that it is within the allowed range.
     *
     * @param value
     *          [in] The string to parse
     * @return The value
     * @throws ParseException
     *           if the syntax is invalid or the value is out of range
     */
    protected BigInteger checkBigValue(CharSequence value) throws ParseException
    {
      BigInteger result = getSyntax().parse(value);
      if (isInRange(result) == false)
      {
        throw new ParseException("Value is not within " + minBound + " and " + maxBound, 0);
      }
      return result;
    }

    /**
     * Returns the syntax accepted when parsing: the XML Schema lexical
     * representation ({@link IntegerFormat#XSD}) when lenient, otherwise the
     * IETF RFC 4517 <code>INTEGER</code> syntax ({@link IntegerFormat#LDAP}).
     */
    IntegerSyntax getSyntax()
    {
      return lenient ? IntegerSyntax.XSD : IntegerSyntax.LDAP;
    }

    /** Returns true if the value is within the bounds. */
    protected boolean isInRange(BigInteger value)
    {
      return ((minBound == null) || (value.compareTo(minBound) >= 0))
          && ((maxBound == null) || (value.compareTo(maxBound) <= 0));
    }

    /**
     * Parses the value and verifies that it is within the allowed range, which
     * must fit in a long.
     */
    protected long checkValue(CharSequence value) throws ParseException
    {
      return checkBigValue(value).longValue();
    }

    /** Converts a value within the range to an instance of the value class. */
    protected Object toValue(BigInteger value)
    {
      if (clz == Integer.class)
      {
        return new Integer(value.intValue());
      }
      if (clz == Long.class)
      {
        return new Long(value.longValue());
      }
      return value;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return toValue(checkBigValue(value));
    }

    /**
     * Returns the integral value of a number, or throws an
     * <code>IllegalArgumentException</code> if it has a fractional part or is
     * not a finite number.
     */
    static BigInteger toBigInteger(Object value)
    {
      if (value instanceof BigInteger)
      {
        return (BigInteger) value;
      }
      if ((value instanceof Integer) || (value instanceof Long) || (value instanceof Short)
          || (value instanceof Byte))
      {
        return BigInteger.valueOf(((Number) value).longValue());
      }
      BigDecimal decimal;
      if (value instanceof BigDecimal)
      {
        decimal = (BigDecimal) value;
      }
      else if ((value instanceof Double) || (value instanceof Float))
      {
        double d = ((Number) value).doubleValue();
        if (Double.isNaN(d) || Double.isInfinite(d))
        {
          throw new IllegalArgumentException("Object value is not a finite number");
        }
        decimal = new BigDecimal(d);
      }
      else
      {
        throw new IllegalArgumentException("Object value is not an integer number");
      }
      try
      {
        // Fails if there is a non-zero fractional part.
        return decimal.setScale(0, BigDecimal.ROUND_UNNECESSARY).unscaledValue();
      } catch (ArithmeticException e)
      {
        throw new IllegalArgumentException("Object value is not an integer numeric value");
      }
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      BigInteger number = toBigInteger(value);
      if (isInRange(number) == false)
      {
        throw new IllegalArgumentException("Value is not within " + minBound + " and "
            + maxBound);
      }
      return toAppendTo.append(number.toString());
    }
  }


  /**
   * Syntax of the string representation of an integer, described by a pattern
   * in the regular expression notation of IETF RFC 9485 (I-Regexp). Only the
   * following patterns are recognised:
   *
   * <pre>
   *     pattern = sign digits
   *     sign    = ""                          ; no sign
   *             / "[-]?"                      ; optional minus sign
   *             / "[+-]?"                     ; optional plus or minus sign
   *     digits  = "[0-9]+"                    ; leading zeros and -0 allowed
   *             / "(([0-9])|([1-9][0-9]+))"   ; no leading zeros, no -0
   * </pre>
   */
  static final class IntegerSyntax
  {
    private static final String[] SIGNS = { "[+-]?", "[-]?", "" };
    private static final String DIGITS = "[0-9]+";
    private static final String CANONICAL_DIGITS = "(([0-9])|([1-9][0-9]+))";

    /** IETF RFC 4517 INTEGER, XML Schema canonical representation. */
    static final IntegerSyntax LDAP = new IntegerSyntax("[-]?" + CANONICAL_DIGITS);
    /** XML Schema lexical representation. */
    static final IntegerSyntax XSD = new IntegerSyntax("[+-]?" + DIGITS);

    final String pattern;
    final boolean allowPlus;
    final boolean allowMinus;
    final boolean canonicalDigits;

    /**
     * @throws IllegalArgumentException
     *           if the pattern is not one of the recognised patterns
     */
    IntegerSyntax(String pattern)
    {
      this.pattern = pattern;
      String sign = null;
      for (int i = 0; i < SIGNS.length; i++)
      {
        if (pattern.startsWith(SIGNS[i]))
        {
          sign = SIGNS[i];
          break;
        }
      }
      String digits = pattern.substring(sign.length());
      if (digits.equals(DIGITS))
      {
        canonicalDigits = false;
      }
      else if (digits.equals(CANONICAL_DIGITS))
      {
        canonicalDigits = true;
      }
      else
      {
        throw new IllegalArgumentException("Unsupported integer pattern '" + pattern
            + "': expected a sign ('', '[-]?' or '[+-]?') followed by '" + DIGITS
            + "' or '" + CANONICAL_DIGITS + "'");
      }
      allowPlus = sign.equals("[+-]?");
      allowMinus = sign.length() > 0;
    }

    /** Parses a value of this syntax. */
    BigInteger parse(CharSequence value) throws ParseException
    {
      return Parsers.parseNumberAsBigInteger(value, 0, value.length(), allowPlus, allowMinus,
          canonicalDigits == false);
    }
  }


  /**
   * Integer formatter and parser whose accepted syntax is given by a pattern,
   * in the regular expression notation of IETF RFC 9485 (I-Regexp).
   *
   * <p>
   * The following patterns are recognised; any other pattern raises an
   * <code>IllegalArgumentException</code>:
   * </p>
   *
   * <pre>
   *     pattern = sign digits
   *     sign    = ""                          ; no sign
   *             / "[-]?"                      ; optional minus sign
   *             / "[+-]?"                     ; optional plus or minus sign
   *     digits  = "[0-9]+"                    ; leading zeros and -0 allowed
   *             / "(([0-9])|([1-9][0-9]+))"   ; no leading zeros, no -0
   * </pre>
   *
   * <p>
   * The constants of this class give the pattern of each standard:
   * </p>
   *
   * <table border="1">
   * <tr><th>Standard</th><th>Constant</th><th>Pattern</th></tr>
   * <tr><td>ASN.1 (ITU-T X.680)</td><td>{@link #ASN1}</td><td><code>[-]?(([0-9])|([1-9][0-9]+))</code></td></tr>
   * <tr><td>LDAP (IETF RFC 4517)</td><td>{@link #LDAP}</td><td><code>[-]?(([0-9])|([1-9][0-9]+))</code></td></tr>
   * <tr><td>XML Schema 1.1, canonical</td><td>{@link #XSD_CANONICAL}</td><td><code>[-]?(([0-9])|([1-9][0-9]+))</code></td></tr>
   * <tr><td>XML Schema 1.1, lexical</td><td>{@link #XSD}</td><td><code>[+-]?[0-9]+</code></td></tr>
   * <tr><td>ISO/IEC 11404</td><td>{@link #ISO11404}</td><td><code>[-]?[0-9]+</code></td></tr>
   * <tr><td>SQL 99</td><td>{@link #SQL99}</td><td><code>[+-]?[0-9]+</code></td></tr>
   * <tr><td>vCard (IETF RFC 6350)</td><td>{@link #VCARD}</td><td><code>[+-]?[0-9]+</code></td></tr>
   * <tr><td>XMP (ISO 16684-1)</td><td>{@link #XMP}</td><td><code>[+-]?[0-9]+</code></td></tr>
   * </table>
   *
   * <p>
   * Formatting always writes the canonical representation (no plus sign, no
   * leading zeros, no negative zero), which is valid in every syntax above.
   * The <code>lenient</code> flag does not change the syntax accepted: it is
   * given by the pattern only.
   * </p>
   *
   * <p>
   * Example: <code>new IntegerFormat(IntegerFormat.LDAP, Integer.class, -128, 127)</code>
   * is the converter of the LDAP Int8 syntax.
   * </p>
   */
  public static class IntegerFormat extends IntegerNumberConverter
  {
    /** ASN.1 (ITU-T X.680) integer value notation. */
    public static final String ASN1 = "[-]?(([0-9])|([1-9][0-9]+))";
    /** IETF RFC 4517 <code>INTEGER</code> syntax. */
    public static final String LDAP = "[-]?(([0-9])|([1-9][0-9]+))";
    /** W3C XML Schema Definition Language (XSD) 1.1 canonical representation. */
    public static final String XSD_CANONICAL = "[-]?(([0-9])|([1-9][0-9]+))";
    /** W3C XML Schema Definition Language (XSD) 1.1 lexical representation. */
    public static final String XSD = "[+-]?[0-9]+";
    /** ISO/IEC 11404 General purpose datatypes. */
    public static final String ISO11404 = "[-]?[0-9]+";
    /** SQL 99 signed numeric literal. */
    public static final String SQL99 = "[+-]?[0-9]+";
    /** vCard (IETF RFC 6350) <code>INTEGER</code> value type. */
    public static final String VCARD = "[+-]?[0-9]+";
    /** XMP (ISO 16684-1) <code>Integer</code> value type. */
    public static final String XMP = "[+-]?[0-9]+";

    private final IntegerSyntax syntax;

    /**
     * Creates a converter of integers of any size, returned as
     * {@link java.math.BigInteger}.
     *
     * @param pattern
     *          [in] The syntax accepted when parsing
     * @throws IllegalArgumentException
     *           if the pattern is not recognised
     */
    public IntegerFormat(String pattern)
    {
      this(pattern, BigInteger.class, (BigInteger) null, (BigInteger) null);
    }

    /**
     * Creates a converter of integers within a range.
     *
     * @param pattern
     *          [in] The syntax accepted when parsing
     * @param valueClass
     *          [in] <code>Integer.class</code>, <code>Long.class</code> or
     *          <code>BigInteger.class</code>; the range must fit in it
     * @param minValue
     *          [in] Lowest value allowed
     * @param maxValue
     *          [in] Highest value allowed
     * @throws IllegalArgumentException
     *           if the pattern is not recognised
     */
    public IntegerFormat(String pattern, Class valueClass, long minValue, long maxValue)
    {
      this(pattern, valueClass, BigInteger.valueOf(minValue), BigInteger.valueOf(maxValue));
    }

    /**
     * Creates a converter of integers within a range.
     *
     * @param pattern
     *          [in] The syntax accepted when parsing
     * @param valueClass
     *          [in] <code>Integer.class</code>, <code>Long.class</code> or
     *          <code>BigInteger.class</code>; the range must fit in it
     * @param minValue
     *          [in] Lowest value allowed, or <code>null</code> for no limit
     * @param maxValue
     *          [in] Highest value allowed, or <code>null</code> for no limit
     * @throws IllegalArgumentException
     *           if the pattern is not recognised
     */
    public IntegerFormat(String pattern, Class valueClass, BigInteger minValue,
        BigInteger maxValue)
    {
      super(valueClass, false, minValue, maxValue);
      this.syntax = new IntegerSyntax(pattern);
    }

    /** Returns the pattern of the syntax accepted when parsing. */
    public String getPattern()
    {
      return syntax.pattern;
    }

    IntegerSyntax getSyntax()
    {
      return syntax;
    }
  }


  /**
   * Signed byte integer formatter and parser (-128..127), returning an
   * {@link java.lang.Integer}. See {@link IntegerNumberConverter} for the
   * syntax accepted.
   *
   * <ul>
   * <li>Lenient (default, {@link #getInstance()}): W3C XML Schema Definition
   * Language (XSD) 1.1 <code>byte</code> lexical representation</li>
   * <li>Not lenient ({@link #getLDAPInstance()}): IETF draft-codere-ldapsyntax
   * <code>Int8</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.1) and XSD
   * <code>byte</code> canonical representation</li>
   * </ul>
   */
  public static class ByteConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new ByteConverter(true);
    private static final DataConverter ldapInstance = new ByteConverter(false);

    public ByteConverter()
    {
      this(true);
    }

    public ByteConverter(boolean lenient)
    {
      super(Integer.class, lenient, Byte.MIN_VALUE, Byte.MAX_VALUE);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
    }
  }


  /**
   * Signed short integer formatter and parser (-32768..32767), returning an
   * {@link java.lang.Integer}. See {@link IntegerNumberConverter} for the
   * syntax accepted.
   *
   * <ul>
   * <li>Lenient (default, {@link #getInstance()}): W3C XML Schema Definition
   * Language (XSD) 1.1 <code>short</code> lexical representation</li>
   * <li>Not lenient ({@link #getLDAPInstance()}): IETF draft-codere-ldapsyntax
   * <code>Int16</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.2) and XSD
   * <code>short</code> canonical representation</li>
   * </ul>
   */
  public static class ShortConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new ShortConverter(true);
    private static final DataConverter ldapInstance = new ShortConverter(false);

    public ShortConverter()
    {
      this(true);
    }

    public ShortConverter(boolean lenient)
    {
      super(Integer.class, lenient, Short.MIN_VALUE, Short.MAX_VALUE);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
    }
  }


  /**
   * Signed integer formatter and parser (-2147483648..2147483647), returning
   * an {@link java.lang.Integer}. See {@link IntegerNumberConverter} for the
   * syntax accepted.
   *
   * <ul>
   * <li>Lenient (default, {@link #getInstance()}): W3C XML Schema Definition
   * Language (XSD) 1.1 <code>int</code> lexical representation</li>
   * <li>Not lenient ({@link #getLDAPInstance()}): IETF draft-codere-ldapsyntax
   * <code>Int32</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.4) and XSD
   * <code>int</code> canonical representation</li>
   * </ul>
   */
  public static class IntConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new IntConverter(true);
    private static final DataConverter ldapInstance = new IntConverter(false);

    public IntConverter()
    {
      this(true);
    }

    public IntConverter(boolean lenient)
    {
      super(Integer.class, lenient, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
    }
  }


  /**
   * Signed long integer formatter and parser
   * (-9223372036854775808..9223372036854775807), returning a
   * {@link java.lang.Long}. See {@link IntegerNumberConverter} for the syntax
   * accepted.
   *
   * <ul>
   * <li>Lenient (default, {@link #getInstance()}): W3C XML Schema Definition
   * Language (XSD) 1.1 <code>long</code> lexical representation</li>
   * <li>Not lenient ({@link #getLDAPInstance()}): IETF draft-codere-ldapsyntax
   * <code>Int64</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.8) and XSD
   * <code>long</code> canonical representation</li>
   * </ul>
   */
  public static class LongConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new LongConverter(true);
    private static final DataConverter ldapInstance = new LongConverter(false);

    public LongConverter()
    {
      this(true);
    }

    public LongConverter(boolean lenient)
    {
      super(Long.class, lenient, Long.MIN_VALUE, Long.MAX_VALUE);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
    }
  }


  /**
   * Unsigned byte integer formatter and parser (0..255), returning an
   * {@link java.lang.Integer}. See {@link IntegerNumberConverter} for the
   * syntax accepted.
   *
   * <ul>
   * <li>Lenient (default, {@link #getInstance()}): W3C XML Schema Definition
   * Language (XSD) 1.1 <code>unsignedByte</code> lexical representation</li>
   * <li>Not lenient ({@link #getLDAPInstance()}): IETF draft-codere-ldapsyntax
   * <code>UInt8</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.21) and XSD
   * <code>unsignedByte</code> canonical representation</li>
   * </ul>
   */
  public static class unsignedByteConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new unsignedByteConverter(true);
    private static final DataConverter ldapInstance = new unsignedByteConverter(false);

    public unsignedByteConverter()
    {
      this(true);
    }

    public unsignedByteConverter(boolean lenient)
    {
      super(Integer.class, lenient, 0, 255);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
    }
  }


  /**
   * Unsigned short integer formatter and parser (0..65535), returning an
   * {@link java.lang.Integer}. See {@link IntegerNumberConverter} for the
   * syntax accepted.
   *
   * <ul>
   * <li>Lenient (default, {@link #getInstance()}): W3C XML Schema Definition
   * Language (XSD) 1.1 <code>unsignedShort</code> lexical representation</li>
   * <li>Not lenient ({@link #getLDAPInstance()}): IETF draft-codere-ldapsyntax
   * <code>UInt16</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.22) and XSD
   * <code>unsignedShort</code> canonical representation</li>
   * </ul>
   */
  public static class unsignedShortConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new unsignedShortConverter(true);
    private static final DataConverter ldapInstance = new unsignedShortConverter(false);

    public unsignedShortConverter()
    {
      this(true);
    }

    public unsignedShortConverter(boolean lenient)
    {
      super(Integer.class, lenient, 0, 65535);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
    }
  }


  /**
   * Unsigned integer formatter and parser (0..4294967295), returning a
   * {@link java.lang.Long}. See {@link IntegerNumberConverter} for the syntax
   * accepted.
   *
   * <ul>
   * <li>Lenient (default, {@link #getInstance()}): W3C XML Schema Definition
   * Language (XSD) 1.1 <code>unsignedInt</code> lexical representation</li>
   * <li>Not lenient ({@link #getLDAPInstance()}): IETF draft-codere-ldapsyntax
   * <code>UInt32</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.24) and XSD
   * <code>unsignedInt</code> canonical representation</li>
   * </ul>
   */
  public static class unsignedIntConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new unsignedIntConverter(true);
    private static final DataConverter ldapInstance = new unsignedIntConverter(false);

    public unsignedIntConverter()
    {
      this(true);
    }

    public unsignedIntConverter(boolean lenient)
    {
      super(Long.class, lenient, 0, 4294967295L);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
    }
  }


  /**
   * Unsigned long integer formatter and parser (0..18446744073709551615),
   * returning a {@link java.math.BigInteger}. See
   * {@link IntegerNumberConverter} for the syntax accepted.
   *
   * <ul>
   * <li>Lenient (default, {@link #getInstance()}): W3C XML Schema Definition
   * Language (XSD) 1.1 <code>unsignedLong</code> lexical representation</li>
   * <li>Not lenient ({@link #getLDAPInstance()}): IETF draft-codere-ldapsyntax
   * <code>UInt64</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.28) and XSD
   * <code>unsignedLong</code> canonical representation</li>
   * </ul>
   */
  public static class unsignedLongConverter extends IntegerNumberConverter
  {
    /** 18446744073709551615, the highest value of an unsigned 64-bit integer. */
    public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");

    private static final DataConverter instance = new unsignedLongConverter(true);
    private static final DataConverter ldapInstance = new unsignedLongConverter(false);

    public unsignedLongConverter()
    {
      this(true);
    }

    public unsignedLongConverter(boolean lenient)
    {
      super(BigInteger.class, lenient, BigInteger.valueOf(0), MAX_VALUE);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
    }
  }


  /**
   * Percentage formatter and parser (0..100), returning an
   * {@link java.lang.Integer}, for the IETF draft-codere-ldapsyntax
   * <code>Percentage</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.2.20).
   *
   * <p>
   * Unlike the other integer converters, the default mode is not lenient:
   * only the IETF RFC 4517 <code>INTEGER</code> syntax is accepted (see
   * {@link IntegerNumberConverter}).
   * </p>
   */
  public static class PercentageConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new PercentageConverter(false);

    public PercentageConverter()
    {
      this(false);
    }

    public PercentageConverter(boolean lenient)
    {
      super(Integer.class, lenient, 0, 100);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public static DataConverter getLDAPInstance()
    {
      return instance;
    }
  }


  /**
   * Integer type converter.  This converter converts an integer 
   * number string representation to a <code>BigInteger</code> Java Object. 
   * 
   * The syntax is the one supported by the Java
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
   * sign, leading 0 digits or a negative zero (<code>-0</code>) are present,
   * conversion will fail. 
   * In <code>lenient</code> mode, the above are accepted without 
   * raising an error.
   * 
   * <p>The value returned is a <code>BigInteger</code> Java Object.</p>
   * 
   * 
   * When <code>lenient</code> is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>ASN.1 ITU X.680 <code>SignedNumber</code></li>
   * <li>LDAP IETF RFC 4517 <code>INTEGER</code> encoding (OID: 1.3.6.1.4.1.1466.115.121.1.27)</li>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>integer</code> canonical representation</li>
   * </ul>
   * 
   * When <code>lenient</code> is <code>true</code> the converter is also compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>integer</code> lexical representation</li>
   * <li>vCard IETF RFC 6350 <code>INTEGER</code> value type</li>
   * <li>XMP ISO 16684-1:2011</li>
   * <li>Pascal Language Syntax</li>
   * </ul>
   * 
   * <p>By default the parsing is lenient.</p>
   */
  public static class IntegerCanonicalConverter extends NumberConverter
  {
    private static final DataConverter instance = new IntegerCanonicalConverter(true);
    private static final DataConverter ldapInstance = new IntegerCanonicalConverter(false);

    public IntegerCanonicalConverter()
    {
      super(BigInteger.class, true);
    }

    /**
     * @param lenient
     *          [in] <code>true</code> for the XML Schema lexical
     *          representation, <code>false</code> for the IETF RFC 4517
     *          <code>INTEGER</code> syntax.
     */
    public IntegerCanonicalConverter(boolean lenient)
    {
      super(BigInteger.class, lenient);
    }

    /** Returns the shared lenient instance (XML Schema <code>integer</code>). */
    public static DataConverter getInstance()
    {
      return instance;
    }

    /** Returns the shared instance for the IETF RFC 4517 <code>INTEGER</code> syntax. */
    public static DataConverter getLDAPInstance()
    {
      return ldapInstance;
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
/*      if (result.bitLength() < 63)
      {
        return new Long(result.longValue());
      }*/
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
   * <p>The value returned is a <code>BigInteger</code> Java Object.</p>
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
/*      if (result.bitLength() < 63)
      {
        return new Long(result.longValue());
      }*/
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
  
  
  /**
   * Abstract class for IEEE 754 floating point formatting and parsing.
   * Decimal values are rounded to the nearest representable value, using the
   * rounding of {@link Double#parseDouble(String)} or
   * {@link Float#parseFloat(String)}.
   */
  public abstract static class FloatingPointConverter extends NumberConverter
  {
    /** <code>true</code> for single precision (Float), false for Double. */
    protected final boolean singlePrecision;

    protected FloatingPointConverter(boolean singlePrecision)
    {
      super(singlePrecision ? Float.class : Double.class, false);
      this.singlePrecision = singlePrecision;
    }

    /** Returns the value as a double, verifying its class. */
    protected double toDouble(Object value)
    {
      if (singlePrecision)
      {
        if ((value instanceof Float) == false)
        {
          throw new IllegalArgumentException("Object value is not instance of '"
              + clz.getName() + "'");
        }
      }
      else if (((value instanceof Double) == false) && ((value instanceof Float) == false))
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      return ((Number) value).doubleValue();
    }

    /** Converts a validated decimal string to the value class. */
    protected Object toValue(String decimal)
    {
      if (singlePrecision)
      {
        return new Float(Float.parseFloat(decimal));
      }
      return new Double(Double.parseDouble(decimal));
    }

    /** Returns an instance of the value class for a special value. */
    protected Object toValue(double value)
    {
      if (singlePrecision)
      {
        return new Float((float) value);
      }
      return new Double(value);
    }

    /** Returns the shortest decimal string of a finite value, without sign. */
    protected String toDecimalString(double absValue)
    {
      if (singlePrecision)
      {
        return Float.toString((float) absValue);
      }
      return Double.toString(absValue);
    }

    /** Returns the index after the digits starting at index. */
    static int skipDigits(CharSequence value, int index)
    {
      while ((index < value.length()) && (value.charAt(index) >= '0')
          && (value.charAt(index) <= '9'))
      {
        index++;
      }
      return index;
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }
  }


  /**
   * Formatter and parser for the IETF draft-codere-ldapsyntax
   * <code>Real</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.9), returning a
   * {@link java.lang.Double}.
   *
   * <p>
   * A value conforms to the following regular expression, where the integer
   * part has no leading zeros unless it is <code>0</code>:
   * </p>
   *
   * <p>
   * <code>([-]?[0-9]+(\.[0-9]+)?([E][-]?[0-9]+)?)|PLUS-INFINITY|MINUS-INFINITY|NOT-A-NUMBER</code>
   * </p>
   *
   * <p>
   * As specified for the Real syntax, values are represented as IEEE 754
   * double precision values: decimal values are rounded to the nearest
   * representable value, and finite values whose magnitude is too large to be
   * represented are rejected. Formatting writes the shortest decimal
   * representation that parses back to the same value, as produced by
   * {@link Double#toString(double)}, or one of the special values.
   * </p>
   */
  public static class RealConverter extends FloatingPointConverter
  {
    public static final String PLUS_INFINITY = "PLUS-INFINITY";
    public static final String MINUS_INFINITY = "MINUS-INFINITY";
    public static final String NOT_A_NUMBER = "NOT-A-NUMBER";

    private static final DataConverter instance = new RealConverter();

    public RealConverter()
    {
      this(false);
    }

    protected RealConverter(boolean singlePrecision)
    {
      super(singlePrecision);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    /**
     * Verifies the syntax of the Real syntax for a finite value.
     *
     * @return -1 if the value is valid, otherwise the index of the error.
     */
    static int indexOfError(CharSequence value)
    {
      int length = value.length();
      int i = 0;
      if ((i < length) && (value.charAt(i) == '-'))
      {
        i++;
      }
      int start = i;
      i = skipDigits(value, i);
      if (i == start)
      {
        return i;
      }
      if ((value.charAt(start) == '0') && (i - start > 1))
      {
        return start;
      }
      if ((i < length) && (value.charAt(i) == '.'))
      {
        int fraction = ++i;
        i = skipDigits(value, i);
        if (i == fraction)
        {
          return i;
        }
      }
      if ((i < length) && (value.charAt(i) == 'E'))
      {
        i++;
        if ((i < length) && (value.charAt(i) == '-'))
        {
          i++;
        }
        int exponent = i;
        i = skipDigits(value, i);
        if (i == exponent)
        {
          return i;
        }
      }
      return (i == length) ? -1 : i;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      String str = value.toString();
      if (str.equals(PLUS_INFINITY))
      {
        return toValue(Double.POSITIVE_INFINITY);
      }
      if (str.equals(MINUS_INFINITY))
      {
        return toValue(Double.NEGATIVE_INFINITY);
      }
      if (str.equals(NOT_A_NUMBER))
      {
        return toValue(Double.NaN);
      }
      int error = indexOfError(str);
      if (error != -1)
      {
        throw new ParseException("Invalid real number syntax.", error);
      }
      Object result = toValue(str);
      if (Double.isInfinite(((Number) result).doubleValue()))
      {
        throw new ParseException("The value is too large to be represented.", 0);
      }
      return result;
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      double d = toDouble(value);
      if (Double.isNaN(d))
      {
        return toAppendTo.append(NOT_A_NUMBER);
      }
      if (Double.isInfinite(d))
      {
        return toAppendTo.append((d > 0) ? PLUS_INFINITY : MINUS_INFINITY);
      }
      // 1/d distinguishes negative zero.
      if ((d < 0) || ((d == 0) && (1 / d < 0)))
      {
        toAppendTo.append('-');
      }
      return toAppendTo.append(toDecimalString(Math.abs(d)));
    }
  }


  /**
   * Formatter and parser for the IETF draft-codere-ldapsyntax
   * <code>Float64</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.9.8), returning a
   * {@link java.lang.Double}. The syntax is that of {@link RealConverter}.
   */
  public static class Float64Converter extends RealConverter
  {
    private static final DataConverter instance = new Float64Converter();

    public Float64Converter()
    {
      super(false);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Formatter and parser for the IETF draft-codere-ldapsyntax
   * <code>Float32</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.9.4), returning a
   * {@link java.lang.Float}. The syntax is that of {@link RealConverter}; values
   * are rounded to the nearest single precision value, and finite values too
   * large to be represented are rejected.
   */
  public static class Float32Converter extends RealConverter
  {
    private static final DataConverter instance = new Float32Converter();

    public Float32Converter()
    {
      super(true);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Formatter and parser for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>double</code> type, returning a {@link java.lang.Double}.
   *
   * <p>
   * The lexical representation is accepted:
   * <code>(\+|-)?([0-9]+(\.[0-9]*)?|\.[0-9]+)([Ee](\+|-)?[0-9]+)?|(\+|-)?INF|NaN</code>.
   * As in XML Schema 1.1, decimal values are rounded to the nearest
   * representable value, and values too large are rounded to infinity.
   * Formatting writes the canonical representation: <code>INF</code>,
   * <code>-INF</code>, <code>NaN</code>, or a mantissa with one digit before
   * the decimal point followed by an exponent, such as <code>1.25E2</code> or
   * <code>0.0E0</code>.
   * </p>
   */
  public static class XSDDoubleConverter extends FloatingPointConverter
  {
    private static final DataConverter instance = new XSDDoubleConverter();

    public XSDDoubleConverter()
    {
      this(false);
    }

    protected XSDDoubleConverter(boolean singlePrecision)
    {
      super(singlePrecision);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      String str = value.toString();
      int length = str.length();
      int i = 0;
      boolean negative = false;
      if ((i < length) && ((str.charAt(i) == '+') || (str.charAt(i) == '-')))
      {
        negative = str.charAt(i) == '-';
        i++;
      }
      if (str.substring(i).equals("INF"))
      {
        return toValue(negative ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
      }
      if (str.equals("NaN"))
      {
        return toValue(Double.NaN);
      }
      int start = i;
      i = skipDigits(str, i);
      int digits = i - start;
      if ((i < length) && (str.charAt(i) == '.'))
      {
        int fraction = ++i;
        i = skipDigits(str, i);
        digits += i - fraction;
      }
      if (digits == 0)
      {
        throw new ParseException("Expecting digits.", i);
      }
      if ((i < length) && ((str.charAt(i) == 'E') || (str.charAt(i) == 'e')))
      {
        i++;
        if ((i < length) && ((str.charAt(i) == '+') || (str.charAt(i) == '-')))
        {
          i++;
        }
        int exponent = i;
        i = skipDigits(str, i);
        if (i == exponent)
        {
          throw new ParseException("Expecting exponent digits.", i);
        }
      }
      if (i != length)
      {
        throw new ParseException("Invalid floating point number syntax.", i);
      }
      return toValue(str);
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      double d = toDouble(value);
      if (Double.isNaN(d))
      {
        return toAppendTo.append("NaN");
      }
      if (Double.isInfinite(d))
      {
        return toAppendTo.append((d > 0) ? "INF" : "-INF");
      }
      if ((d < 0) || ((d == 0) && (1 / d < 0)))
      {
        toAppendTo.append('-');
      }
      return toAppendTo.append(toScientific(toDecimalString(Math.abs(d))));
    }

    /**
     * Converts the result of <code>Double.toString</code> or
     * <code>Float.toString</code> of a non-negative finite value to the
     * canonical scientific notation of XML Schema.
     */
    static String toScientific(String decimal)
    {
      String mantissa = decimal;
      int exponent = 0;
      int e = decimal.indexOf('E');
      if (e != -1)
      {
        mantissa = decimal.substring(0, e);
        exponent = Integer.parseInt(decimal.substring(e + 1));
      }
      int point = mantissa.indexOf('.');
      String integerPart = (point == -1) ? mantissa : mantissa.substring(0, point);
      String fractionPart = (point == -1) ? "" : mantissa.substring(point + 1);
      String digits = integerPart + fractionPart;
      // Position of the first significant digit.
      int first = 0;
      while ((first < digits.length() - 1) && (digits.charAt(first) == '0'))
      {
        first++;
      }
      if (digits.charAt(first) == '0')
      {
        return "0.0E0";
      }
      exponent = exponent + integerPart.length() - 1 - first;
      // Significant digits without trailing zeros.
      int end = digits.length();
      while ((end > first + 1) && (digits.charAt(end - 1) == '0'))
      {
        end--;
      }
      String rest = (end > first + 1) ? digits.substring(first + 1, end) : "0";
      return digits.charAt(first) + "." + rest + "E" + exponent;
    }
  }


  /**
   * Formatter and parser for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>float</code> type, returning a {@link java.lang.Float}. The syntax is
   * that of {@link XSDDoubleConverter}, with values rounded to the nearest
   * single precision value.
   */
  public static class XSDFloatConverter extends XSDDoubleConverter
  {
    private static final DataConverter instance = new XSDFloatConverter();

    public XSDFloatConverter()
    {
      super(true);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>byte</code> type: the lexical representation of
   * {@link ByteConverter} (lenient).
   */
  public static class XSDByteConverter extends ByteConverter
  {
    private static final DataConverter instance = new XSDByteConverter();
    public XSDByteConverter() { super(true); }
    public static DataConverter getInstance() { return instance; }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>short</code> type: the lexical representation of
   * {@link ShortConverter} (lenient).
   */
  public static class XSDShortConverter extends ShortConverter
  {
    private static final DataConverter instance = new XSDShortConverter();
    public XSDShortConverter() { super(true); }
    public static DataConverter getInstance() { return instance; }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>int</code> type: the lexical representation of
   * {@link IntConverter} (lenient).
   */
  public static class XSDIntConverter extends IntConverter
  {
    private static final DataConverter instance = new XSDIntConverter();
    public XSDIntConverter() { super(true); }
    public static DataConverter getInstance() { return instance; }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>long</code> type: the lexical representation of
   * {@link LongConverter} (lenient).
   */
  public static class XSDLongConverter extends LongConverter
  {
    private static final DataConverter instance = new XSDLongConverter();
    public XSDLongConverter() { super(true); }
    public static DataConverter getInstance() { return instance; }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>unsignedByte</code> type: the lexical representation of
   * {@link unsignedByteConverter} (lenient).
   */
  public static class XSDUnsignedByteConverter extends unsignedByteConverter
  {
    private static final DataConverter instance = new XSDUnsignedByteConverter();
    public XSDUnsignedByteConverter() { super(true); }
    public static DataConverter getInstance() { return instance; }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>unsignedShort</code> type: the lexical representation of
   * {@link unsignedShortConverter} (lenient).
   */
  public static class XSDUnsignedShortConverter extends unsignedShortConverter
  {
    private static final DataConverter instance = new XSDUnsignedShortConverter();
    public XSDUnsignedShortConverter() { super(true); }
    public static DataConverter getInstance() { return instance; }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>unsignedInt</code> type: the lexical representation of
   * {@link unsignedIntConverter} (lenient).
   */
  public static class XSDUnsignedIntConverter extends unsignedIntConverter
  {
    private static final DataConverter instance = new XSDUnsignedIntConverter();
    public XSDUnsignedIntConverter() { super(true); }
    public static DataConverter getInstance() { return instance; }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>unsignedLong</code> type: the lexical representation of
   * {@link unsignedLongConverter} (lenient).
   */
  public static class XSDUnsignedLongConverter extends unsignedLongConverter
  {
    private static final DataConverter instance = new XSDUnsignedLongConverter();
    public XSDUnsignedLongConverter() { super(true); }
    public static DataConverter getInstance() { return instance; }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>integer</code> type, returning a {@link java.math.BigInteger}: an
   * optional sign followed by one or more digits, leading zeros allowed.
   * Formatting writes the canonical representation.
   */
  public static class XSDIntegerConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new XSDIntegerConverter();

    public XSDIntegerConverter()
    {
      super(BigInteger.class, true, (BigInteger) null, (BigInteger) null);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>nonNegativeInteger</code> type, returning a
   * {@link java.math.BigInteger}: an optional sign followed by one or more
   * digits, leading zeros allowed, with a value of zero or more (so
   * <code>-0</code> is allowed). Formatting writes the canonical
   * representation.
   */
  public static class XSDNonNegativeIntegerConverter extends IntegerNumberConverter
  {
    private static final DataConverter instance = new XSDNonNegativeIntegerConverter();

    public XSDNonNegativeIntegerConverter()
    {
      super(BigInteger.class, true, BigInteger.valueOf(0), (BigInteger) null);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }

  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>decimal</code> type, returning a {@link java.math.BigDecimal}.
   * 
   * <p>
   * The lexical representation is accepted:
   * <code>(\+|-)?([0-9]+(\.[0-9]*)?|\.[0-9]+)</code>. The value returned has no
   * trailing zeros in its fractional part, so that equal values are equal
   * objects. Formatting writes the canonical representation of XML Schema
   * 1.1: no sign for positive values, no leading zeros except a single
   * <code>0</code> before the decimal point, no trailing zeros, and no decimal
   * point for integer values (such as <code>5</code>, <code>-0.25</code>).
   * </p>
   */
  public static class XSDDecimalConverter extends NumberConverter
  {
    private static final DataConverter instance = new XSDDecimalConverter();

    private static final BigInteger TEN = BigInteger.valueOf(10);

    public XSDDecimalConverter()
    {
      super(BigDecimal.class, true);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    /** Returns the value without trailing zeros in its fractional part. */
    static BigDecimal normalize(BigDecimal value)
    {
      BigInteger unscaled = value.unscaledValue();
      int scale = value.scale();
      if (unscaled.signum() == 0)
      {
        return new BigDecimal(BigInteger.valueOf(0), 0);
      }
      while (scale > 0)
      {
        BigInteger[] division = unscaled.divideAndRemainder(TEN);
        if (division[1].signum() != 0)
        {
          break;
        }
        unscaled = division[0];
        scale--;
      }
      return new BigDecimal(unscaled, scale);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      int length = value.length();
      int i = 0;
      boolean negative = false;
      if ((i < length) && ((value.charAt(i) == '+') || (value.charAt(i) == '-')))
      {
        negative = value.charAt(i) == '-';
        i++;
      }
      StringBuffer digits = new StringBuffer(length);
      while ((i < length) && (value.charAt(i) >= '0') && (value.charAt(i) <= '9'))
      {
        digits.append(value.charAt(i++));
      }
      int scale = 0;
      if ((i < length) && (value.charAt(i) == '.'))
      {
        i++;
        while ((i < length) && (value.charAt(i) >= '0') && (value.charAt(i) <= '9'))
        {
          digits.append(value.charAt(i++));
          scale++;
        }
      }
      if (digits.length() == 0)
      {
        throw new ParseException("Expecting digits.", i);
      }
      if (i != length)
      {
        throw new ParseException("Invalid decimal number syntax.", i);
      }
      BigInteger unscaled = new BigInteger(digits.toString());
      if (negative)
      {
        unscaled = unscaled.negate();
      }
      return normalize(new BigDecimal(unscaled, scale));
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      BigDecimal decimal;
      if (value instanceof BigDecimal)
      {
        decimal = (BigDecimal) value;
      }
      else if ((value instanceof BigInteger) || (value instanceof Integer)
          || (value instanceof Long) || (value instanceof Short) || (value instanceof Byte))
      {
        decimal = new BigDecimal(IntegerNumberConverter.toBigInteger(value), 0);
      }
      else
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      decimal = normalize(decimal);
      BigInteger unscaled = decimal.unscaledValue();
      int scale = decimal.scale();
      if (unscaled.signum() < 0)
      {
        toAppendTo.append('-');
      }
      String digits = unscaled.abs().toString();
      if (scale <= 0)
      {
        toAppendTo.append(digits);
        for (int i = 0; i < -scale; i++)
        {
          toAppendTo.append('0');
        }
        return toAppendTo;
      }
      StringBuffer padded = new StringBuffer();
      for (int i = digits.length(); i <= scale; i++)
      {
        padded.append('0');
      }
      padded.append(digits);
      int point = padded.length() - scale;
      toAppendTo.append(padded.toString().substring(0, point));
      toAppendTo.append('.');
      toAppendTo.append(padded.toString().substring(point));
      return toAppendTo;
    }
  }

}
