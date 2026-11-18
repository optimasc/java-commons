package com.optimasc.text;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.FieldPosition;
import java.text.ParseException;
import java.util.Locale;

import com.optimasc.lang.MediaType;

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

/**
 * Contains classes for converting a type to another type with error checking
 * based on {@link java.text.Format}. The module is completely compatible with
 * CDC 1.1.
 * 
 * <p>
 * Most of the clases herein support parsing in lenient mode, where the parser
 * will try to convert the data to the correct output Object if not necessarily
 * 100% compliant with the syntax. See individual class documentation for more
 * information.
 * </p>
 *
 * <p>
 * <b>Thread safety.</b> The only mutable state of a converter is its
 * <code>lenient</code> flag. Parsing and formatting do not modify any state,
 * so an instance may be used concurrently as long as
 * {@link DataConverter#setLenient(boolean)} is not called on it while it is
 * shared. The instances returned by the <code>getInstance()</code> methods
 * are shared by every caller in the virtual machine and therefore MUST NOT be
 * reconfigured; create a new instance when a different <code>lenient</code>
 * setting is required.
 * </p>
 *
 * @author Carl Eric Codere
 */
public class StandardFormatters
{
  // Do not instantiate
  protected StandardFormatters()
  {

  }
  
  /**
   * Text type converter, as defined by the IETF draft-codere-ldapsyntax
   * <code>Text</code> syntax (section 2.3.7).
   * 
   * <p>
   * A value must not contain control characters (General_Category Cc) other
   * than CHARACTER TABULATION (U+0009), LINE FEED (U+000A) and CARRIAGE RETURN
   * (U+000D), surrogates (Cs), private use characters (Co) or non-characters.
   * All other characters, including format characters (Cf) and the empty
   * string, are allowed. Characters outside the Basic Multilingual Plane are
   * represented as surrogate pairs. The value is converted to Unicode
   * normalization form C (NFC).
   * </p>
   * 
   * <p>
   * When <code>lenient</code> is <code>true</code>, which is the default,
   * prohibited control characters are removed instead of being rejected.
   * </p>
   * 
   * <p>
   * {@link TextConverter} is the same converter under the name used by the
   * specification for the Text type.
   * </p>
   * 
   * The converter is compliant with the following syntaxes:
   * <ul>
   * <li>IETF draft-codere-ldapsyntax <code>Text</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.12.3)</li>
   * </ul>
   * 
   * The converter is stricter but similar to the the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>string</code> canonical representation</li>
   * </ul>
   */
  public static class StringConverter extends DataConverter
  {
    private static final DataConverter instance = new StringConverter();
    

    public StringConverter()
    {
      super(String.class, true);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return parse(value, lenient);
    }

    /**
     * Parses and verifies a value.
     * 
     * @param value
     *          [in] The value to parse.
     * @param lenient
     *          [in] <code>true</code> to repair a value that does not conform,
     *          <code>false</code> to reject it.
     * @return The value as a <code>String</code>.
     * @throws ParseException
     *           If the value does not conform and cannot be repaired.
     */
    protected Object parse(CharSequence value, boolean lenient) throws ParseException
    {
      return StringNormalizer.toNFC(StringNormalizer.mapText(value, lenient));
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    /**
     * Formats a value. The value is verified as if parsed with
     * <code>lenient</code> set to <code>false</code>, whatever the mode of this
     * converter: a value that does not conform is rejected and never repaired.
     * 
     * @throws IllegalArgumentException
     *           If the value is not a <code>String</code> or does not conform.
     */
    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof String) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      try
      {
        return toAppendTo.append((String) parse((CharSequence) value, false));
      } catch (ParseException e)
      {
        throw new IllegalArgumentException("Value cannot be represented: " + e.getMessage());
      }
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    
  }

  /**
   * Text type converter. This is the format of the IETF
   * draft-codere-ldapsyntax <code>Text</code> syntax (OID:
   * 1.3.6.1.4.1.61799.5.40.12.3); it behaves exactly as
   * {@link StringConverter}.
   */
  public static class TextConverter extends StringConverter
  {
    private static final DataConverter instance = new TextConverter();

    public TextConverter()
    {
      super();
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }

  /**
   * Maps and verifies a value of the XML Schema <code>string</code> type and of
   * the types derived from it, whose characters are the XML <code>Char</code>
   * production: U+0009, U+000A, U+000D, U+0020 to U+D7FF, U+E000 to U+FFFD and
   * U+10000 to U+10FFFF. Unlike {@link StringConverter Text}, private use
   * characters and the non-characters U+FDD0 to U+FDEF are allowed, and the
   * value is not normalized.
   * 
   * @param whiteSpace
   *          [in] 0 to preserve whitespace (<code>string</code>), 1 to replace
   *          U+0009, U+000A and U+000D by SPACE (<code>normalizedString</code>),
   *          2 to also collapse spaces (<code>token</code>).
   */
  static String mapXSDString(CharSequence value, boolean lenient, int whiteSpace)
      throws ParseException
  {
    int length = value.length();
    StringBuffer out = new StringBuffer(length);
    for (int i = 0; i < length; i++)
    {
      char ch = value.charAt(i);
      if ((ch >= 0xD800) && (ch <= 0xDBFF))
      {
        if ((i + 1 >= length) || (value.charAt(i + 1) < 0xDC00) || (value.charAt(i + 1) > 0xDFFF))
        {
          throw new ParseException("String contains an unpaired surrogate character.", i);
        }
        out.append(ch).append(value.charAt(i + 1));
        i++;
        continue;
      }
      if ((ch >= 0xDC00) && (ch <= 0xDFFF))
      {
        throw new ParseException("String contains an unpaired surrogate character.", i);
      }
      if ((ch == 0xFFFE) || (ch == 0xFFFF))
      {
        throw new ParseException("String contains a character that is not an XML character.", i);
      }
      if ((ch == '\t') || (ch == '\n') || (ch == '\r'))
      {
        if (whiteSpace == 0)
        {
          out.append(ch);
          continue;
        }
        if (lenient == false)
        {
          throw new ParseException("String contains a tab, line feed or carriage return.", i);
        }
        out.append(' ');
        continue;
      }
      if (ch < 0x20)
      {
        if (lenient == false)
        {
          throw new ParseException("String contains a control character that is not an XML character.", i);
        }
        continue;
      }
      out.append(ch);
    }
    if (whiteSpace < 2)
    {
      return out.toString();
    }
    // Collapse: no leading, trailing or consecutive spaces.
    StringBuffer collapsed = new StringBuffer(out.length());
    for (int i = 0; i < out.length(); i++)
    {
      char c = out.charAt(i);
      if ((c == ' ') && ((collapsed.length() == 0) || (collapsed.charAt(collapsed.length() - 1) == ' ')))
      {
        if (lenient == false)
        {
          throw new ParseException("Leading or consecutive spaces are not allowed.", i);
        }
        continue;
      }
      collapsed.append(c);
    }
    if ((collapsed.length() > 0) && (collapsed.charAt(collapsed.length() - 1) == ' '))
    {
      if (lenient == false)
      {
        throw new ParseException("Trailing space is not allowed.", out.length() - 1);
      }
      collapsed.setLength(collapsed.length() - 1);
    }
    return collapsed.toString();
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>string</code> type. The characters allowed are those of the XML
   * <code>Char</code> production; the empty string is allowed and the value is
   * not normalized.
   * 
   * <p>
   * When <code>lenient</code> is <code>true</code>, which is the default,
   * control characters that are not XML characters are removed instead of
   * being rejected.
   * </p>
   */
  public static class XSDStringConverter extends DataConverter
  {
    private static final DataConverter instance = new XSDStringConverter();

    /** 0 (preserve), 1 (replace) or 2 (collapse). */
    private final int whiteSpace;

    public XSDStringConverter()
    {
      this(0);
    }

    protected XSDStringConverter(int whiteSpace)
    {
      super(String.class, true);
      this.whiteSpace = whiteSpace;
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return mapXSDString(value, lenient, whiteSpace);
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof String) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      try
      {
        return toAppendTo.append(mapXSDString((String) value, false, whiteSpace));
      } catch (ParseException e)
      {
        throw new IllegalArgumentException("Value cannot be represented: " + e.getMessage());
      }
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>normalizedString</code> type: an {@link XSDStringConverter XML Schema
   * string} without U+0009, U+000A or U+000D. When <code>lenient</code> is
   * <code>true</code>, which is the default, these characters are replaced by
   * SPACE, as done by XML Schema processors; otherwise they are rejected.
   */
  public static class XSDNormalizedStringConverter extends XSDStringConverter
  {
    private static final DataConverter instance = new XSDNormalizedStringConverter();

    public XSDNormalizedStringConverter()
    {
      super(1);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>token</code> type: an {@link XSDNormalizedStringConverter XML Schema
   * normalizedString} without leading, trailing or consecutive spaces. When
   * <code>lenient</code> is <code>true</code>, which is the default, the
   * whitespace is collapsed, as done by XML Schema processors; otherwise
   * non-collapsed values are rejected.
   */
  public static class XSDTokenConverter extends XSDStringConverter
  {
    private static final DataConverter instance = new XSDTokenConverter();

    public XSDTokenConverter()
    {
      super(2);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>hexBinary</code> type. It is the same as {@link HexBinaryConverter},
   * except that the empty string, which represents a zero-length octet
   * string, is allowed.
   */
  public static class XSDHexBinaryConverter extends HexBinaryConverter
  {
    private static final DataConverter instance = new XSDHexBinaryConverter();

    public XSDHexBinaryConverter()
    {
      super();
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      if (value.length() == 0)
      {
        return new byte[0];
      }
      return super.parseObject(value);
    }
  }


  /**
   * Converter for the W3C XML Schema Definition Language (XSD) 1.1
   * <code>base64Binary</code> type. It is the same as
   * {@link Base64CanonicalConverter}, except that, when <code>lenient</code>
   * is <code>true</code>, which is the default, the whitespace that the XML
   * Schema lexical representation allows between characters is ignored. The
   * empty string represents a zero-length octet string.
   */
  public static class XSDBase64BinaryConverter extends Base64CanonicalConverter
  {
    private static final DataConverter instance = new XSDBase64BinaryConverter();

    public XSDBase64BinaryConverter()
    {
      super();
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      if (lenient == false)
      {
        return super.parseObject(value);
      }
      StringBuffer compact = new StringBuffer(value.length());
      for (int i = 0; i < value.length(); i++)
      {
        char c = value.charAt(i);
        if ((c != ' ') && (c != '\t') && (c != '\n') && (c != '\r'))
        {
          compact.append(c);
        }
      }
      return super.parseObject(compact);
    }
  }


  /**
   * Normalized String type converter, as defined by the IETF
   * draft-codere-ldapsyntax <code>Normalized String</code> syntax (OID:
   * 1.3.6.1.4.1.61799.5.40.12.4, section 2.3.8).
   * 
   * <p>
   * CHARACTER TABULATION (U+0009), LINE FEED (U+000A), CARRIAGE RETURN
   * (U+000D) and the separators of General_Category Zs (other than SPACE), Zl
   * and Zp are mapped to SPACE (U+0020); the result must then satisfy the
   * restrictions of {@link StringConverter Text}. The value is converted to
   * Unicode normalization form C (NFC).
   * </p>
   * 
   * <p>
   * When <code>lenient</code> is <code>false</code>, only the canonical form,
   * in which SPACE is the only whitespace character, is accepted. When
   * <code>lenient</code> is <code>true</code>, which is the default, whitespace
   * is mapped to SPACE and prohibited control characters are removed.
   * </p>
   * 
   * The converter is stricter but similar to the the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>normalizedString</code> canonical representation</li>
   * </ul>
   */
  public static class NormalizedStringConverter extends StringConverter
  {
    private static final DataConverter instance = new NormalizedStringConverter();
    
    public NormalizedStringConverter()
    {
      super();
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    protected Object parse(CharSequence value, boolean lenient) throws ParseException
    {
      return StringNormalizer.toNFC(StringNormalizer.mapNormalizedString(value, lenient));
    }
  }

  /**
   * Token type converter, as defined by the IETF draft-codere-ldapsyntax
   * <code>Token</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.12.5, section
   * 2.3.9): a {@link NormalizedStringConverter Normalized String} without
   * leading, trailing or consecutive SPACE characters. The empty string is
   * allowed.
   * 
   * <p>
   * When <code>lenient</code> is <code>false</code>, only the canonical form
   * is accepted. When <code>lenient</code> is <code>true</code>, which is the
   * default, whitespace is mapped to SPACE, prohibited control characters are
   * removed, leading and trailing spaces are removed and consecutive spaces
   * are collapsed.
   * </p>
   * 
   * The converter is stricter but similar to the the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>token</code> canonical representation</li>
   * </ul>
   */
  public static class TokenConverter extends NormalizedStringConverter
  {
    private static final DataConverter instance = new TokenConverter();
    
    
    public TokenConverter()
    {
      super();
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    protected Object parse(CharSequence value, boolean lenient) throws ParseException
    {
      int inLength = value.length();
      if (lenient == false)
      {
        // Canonical form: no leading, trailing or consecutive SPACE.
        if ((inLength > 0) && (value.charAt(0) == ' '))
        {
          throw new ParseException("Leading space is not allowed.", 0);
        }
        if ((inLength > 0) && (value.charAt(inLength - 1) == ' '))
        {
          throw new ParseException("Trailing space is not allowed.", inLength - 1);
        }
        for (int i = 0; i + 1 < inLength; i++)
        {
          if ((value.charAt(i) == ' ') && (value.charAt(i + 1) == ' '))
          {
            throw new ParseException("Consecutive spaces are not allowed.", i);
          }
        }
      }
      String mapped = StringNormalizer.toNFC(StringNormalizer.mapNormalizedString(value,
          lenient));
      if (lenient == false)
      {
        return mapped;
      }
      // Remove leading and trailing spaces, and collapse consecutive spaces.
      StringBuffer out = new StringBuffer(mapped.length());
      for (int i = 0; i < mapped.length(); i++)
      {
        char c = mapped.charAt(i);
        if ((c == ' ') && ((out.length() == 0) || (out.charAt(out.length() - 1) == ' ')))
        {
          continue;
        }
        out.append(c);
      }
      if ((out.length() > 0) && (out.charAt(out.length() - 1) == ' '))
      {
        out.setLength(out.length() - 1);
      }
      return out.toString();
    }
    
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    
  }

  /**
   * hexBinary type converter that converts to a <code>byte[]</code>. The
   * standard syntax accepted is where each binary octet is encoded as a
   * character tuple, consisting of two hexadecimal digits ([0-9a-fA-F])
   * representing the octet. Converting to string will always output
   * using the canonical representation.
   * 
   * <p>
   * When <code>lenient</code> is <code>false</code>, then it represents the canonical
   * representation where the lower case hexadecimal digits ([a-f]) are not
   * allowed.
   *
   * When <code>lenient</code> is <code>true</code>, which is the default,
   * the syntax is compliant with the following formats:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>hexBinary</code> lexical representation</li>
   * </ul>
   * 
   * When <code>lenient</code> is <code>false</code>, the syntax is compliant
   * with the following formats:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>hexBinary</code> canonical representation</li>
   * <li>IETF RFC 4648 base16 or hex encoding.</li>
   * </ul>
   * 
   * <p>
   * Restriction: the empty string, which represents a zero-length octet
   * string in XML Schema, is rejected.
   * </p>
   **/
  public static class HexBinaryConverter extends DataConverter
  {
    private static final String HEXES = "0123456789ABCDEF";
    private static final DataConverter instance = new HexBinaryConverter();
    
    

    public HexBinaryConverter()
    {
      super(byte[].class, true);
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    

    public Object parseObject(CharSequence value) throws ParseException
    {
      if (((value.length() & 0x01) == 0x01) || (value.length() < 2))
      {
        throw new ParseException(
            "The string length must be even and must at least 2 characters, as each pair of characters represent one octet.",
            0);
      }
      int len = value.length();
      byte[] data = new byte[len / 2];
      if (lenient == false)
      {
        for (int i = 0; i < len; i += 2)
        {
          int byteValue;
          char c1 = value.charAt(i);
          if ((c1 >= '0') && (c1 <= '9'))
          {
            byteValue = (int) (c1 - 0x30) << 4;
          }
          else if ((c1 >= 'A') && (c1 <= 'F'))
          {
            byteValue = (int) (c1 - 0x37) << 4;
          }
          else
          {
            throw new ParseException(
                "The string contain non-allowed hexadecimal chatacters: '" + c1 + "'", i);
          }
          char c2 = value.charAt(i + 1);
          if ((c2 >= '0') && (c2 <= '9'))
          {
            byteValue = byteValue | (int) (c2 - 0x30);
          }
          else if ((c2 >= 'A') && (c2 <= 'F'))
          {
            byteValue = byteValue | (int) (c2 - 0x37);
          }
          else
          {
            throw new ParseException(
                "The string contain non-allowed hexadecimal chatacters: '" + c2 + "'",
                i + 1);
          }
          data[i / 2] = (byte) byteValue;
        }
      }
      else
      {
        for (int i = 0; i < len; i += 2)
        {
          int byteValue;
          char c1 = value.charAt(i);
          if ((c1 >= '0') && (c1 <= '9'))
          {
            byteValue = (int) (c1 - 0x30) << 4;
          }
          else if ((c1 >= 'A') && (c1 <= 'F'))
          {
            byteValue = (int) (c1 - 0x37) << 4;
          }
          else if ((c1 >= 'a') && (c1 <= 'f'))
          {
            byteValue = (int) (c1 - 0x57) << 4;
          }
          else
          {
            throw new ParseException(
                "The string contain non-allowed hexadecimal chatacters: '" + c1 + "'", i);
          }
          char c2 = value.charAt(i + 1);
          if ((c2 >= '0') && (c2 <= '9'))
          {
            byteValue = byteValue | (int) (c2 - 0x30);
          }
          else if ((c2 >= 'A') && (c2 <= 'F'))
          {
            byteValue = byteValue | (int) (c2 - 0x37);
          }
          else if ((c2 >= 'a') && (c2 <= 'f'))
          {
            byteValue = byteValue | (int) (c2 - 0x57);
          }
          else
          {
            throw new ParseException(
                "The string contain non-allowed hexadecimal chatacters: '" + c2 + "'",
                i + 1);
          }
          data[i / 2] = (byte) byteValue;
        }
      }
      return data;
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof byte[]) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      byte[] buffer = (byte[]) value;
      byte b;
      toAppendTo.ensureCapacity(toAppendTo.capacity() + 2 * buffer.length);
      for (int i = 0; i < buffer.length; i++)
      {
        b = buffer[i];
        toAppendTo.append(HEXES.charAt((b & 0xF0) >> 4)).append(HEXES.charAt((b & 0x0F)));
      }
      return toAppendTo;
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }
  }

  /**
   * base64 type converter that converts to a <code>byte[]</code>. The standard
   * syntax accepted is the one defined in IETF RFC 4648 base64 encoding.
   * 
   * The syntax is compliant with the following formats:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) base64Binary built-in datatype
   * canonical representation</li>
   * <li>IETF RFC 4648 base64 encoding.</li>
   * </ul>
   * 
   * <p>
   * Restriction: only the canonical representation is accepted. Whitespace,
   * which the XML Schema lexical representation allows between characters, is
   * rejected, as are padding characters other than one or two final
   * <code>'='</code>, and a final character whose unused bits are not zero.
   * </p>
   * 
   * @author Christian d'Heureuse
   **/
  public static class Base64CanonicalConverter extends DataConverter
  {
    private static final DataConverter instance = new Base64CanonicalConverter();
    
    public Base64CanonicalConverter()
    {
      super(byte[].class, true);
    }

    // Mapping table from 6-bit nibbles to Base64 characters.
    private static final char[] map1 = new char[64];
    static
    {
      int i = 0;
      for (char c = 'A'; c <= 'Z'; c++)
        map1[i++] = c;
      for (char c = 'a'; c <= 'z'; c++)
        map1[i++] = c;
      for (char c = '0'; c <= '9'; c++)
        map1[i++] = c;
      map1[i++] = '+';
      map1[i++] = '/';
    }

    // Mapping table from Base64 characters to 6-bit nibbles.
    private static final byte[] map2 = new byte[128];
    static
    {
      for (int i = 0; i < map2.length; i++)
        map2[i] = -1;
      for (int i = 0; i < 64; i++)
        map2[map1[i]] = (byte) i;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      int iLen = value.length();
      int iOff = 0;

      if (iLen % 4 != 0)
        throw new ParseException(
            "Length of Base64 encoded input string is not a multiple of 4.", 0);
      int padding = 0;
      while (iLen > 0 && value.charAt(iOff + iLen - 1) == '=')
      {
        iLen--;
        padding++;
      }
      if (padding > 2)
        throw new ParseException("Too many padding characters in Base64 encoded data.", iLen);
      int oLen = (iLen * 3) / 4;
      byte[] out = new byte[oLen];
      int ip = iOff;
      int iEnd = iOff + iLen;
      int op = 0;
      while (ip < iEnd)
      {
        int i0 = value.charAt(ip);
        if (i0 > 127 || map2[i0] < 0)
          throw new ParseException("Illegal character in Base64 encoded data.", ip);
        int b0 = map2[i0];
        ip++;
        int i1 = value.charAt(ip);
        if (i1 > 127 || map2[i1] < 0)
          throw new ParseException("Illegal character in Base64 encoded data.", ip);
        int b1 = map2[i1];
        ip++;
        int i2 = ip < iEnd ? value.charAt(ip++) : 'A';
        if (i2 > 127 || map2[i2] < 0)
          throw new ParseException("Illegal character in Base64 encoded data.", ip - 1);
        int b2 = map2[i2];
        int i3 = ip < iEnd ? value.charAt(ip++) : 'A';
        if (i3 > 127 || map2[i3] < 0)
          throw new ParseException("Illegal character in Base64 encoded data.", ip - 1);
        int b3 = map2[i3];
        int o0 = (b0 << 2) | (b1 >>> 4);
        int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
        int o2 = ((b2 & 3) << 6) | b3;
        // The bits after the last encoded octet must be zero (canonical form).
        if ((ip == iEnd) && (((padding == 2) && ((b1 & 0x0F) != 0))
            || ((padding == 1) && ((b2 & 0x03) != 0))))
          throw new ParseException("Non-canonical final character in Base64 encoded data.", iEnd - 1);
        out[op++] = (byte) o0;
        if (op < oLen)
          out[op++] = (byte) o1;
        if (op < oLen)
          out[op++] = (byte) o2;
      }
      return out;
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof byte[]) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      byte[] in = (byte[]) value;
      int iLen = in.length;
      int iOff = 0;
      int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
      int oLen = ((iLen + 2) / 3) * 4; // output length including padding
      char[] out = new char[oLen];
      int ip = iOff;
      int iEnd = iOff + iLen;
      int op = 0;
      while (ip < iEnd)
      {
        int i0 = in[ip++] & 0xff;
        int i1 = ip < iEnd ? in[ip++] & 0xff : 0;
        int i2 = ip < iEnd ? in[ip++] & 0xff : 0;
        int o0 = i0 >>> 2;
        int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
        int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
        int o3 = i2 & 0x3F;
        out[op++] = map1[o0];
        out[op++] = map1[o1];
        out[op] = op < oDataLen ? map1[o2] : '=';
        op++;
        out[op] = op < oDataLen ? map1[o3] : '=';
        op++;
      }
      toAppendTo.append(out);
      out = null;
      return toAppendTo;
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    

  }

  /**
   * Type converter to native Java objects for a language identifier. A
   * language identifier is represented by a Java object of type
   * <code>Locale</code>.
   *
   * <p>
   * The input is an IETF BCP 47 (RFC 5646) language tag. Values are reduced
   * to the following subset, which <code>java.util.Locale</code> can
   * represent without loss on all supported platforms, and which is also the
   * canonical string form produced by {@link #format}:
   * </p>
   *
   * <pre>
   *     langtag  = language
   *                 ["-" region]
   *
   *     language = 2ALPHA            ; shortest ISO 639 code
   *     region   = 2ALPHA            ; ISO 3166-1 code
   * </pre>
   *
   * <p>
   * A tag that is not well-formed is always rejected. In both modes, the
   * language is converted to lower case, the region to upper case, and a
   * 3-letter ISO 639-2 language code is replaced by its 2-letter ISO 639-1
   * code. These changes keep the value of the tag. A tag is rejected in both
   * modes if its language has no 2-letter ISO 639-1 code (such as
   * <code>haw</code>), has 4 to 8 letters, or has an extended language subtag
   * (such as <code>zh-yue</code>, Cantonese). Private use tags
   * (<code>x-...</code>) and grandfathered tags (such as
   * <code>i-klingon</code>) are also rejected.
   * </p>
   *
   * <p>
   * The script, a UN M.49 region, the variants, the extensions and the private
   * use subtags cannot be represented. How they are handled depends on the
   * <code>lenient</code> setting:
   * </p>
   *
   * <ul>
   * <li>When <code>lenient</code> is <code>false</code>, which is the default,
   * a tag containing any of them is rejected, as the specification requires
   * for values that cannot be represented without loss.</li>
   * <li>When <code>lenient</code> is <code>true</code>, they are removed. This
   * loses information: <code>zh-Hant-TW</code> becomes <code>zh-TW</code>,
   * and <code>es-419</code> (Latin American Spanish) becomes <code>es</code>.
   * Use this mode only when reading values, and never when writing them.</li>
   * </ul>
   *
   * <p>
   * The tag <code>x-default</code>, in any case, is also accepted. As in XMP,
   * it denotes the default alternative rather than a particular language. It
   * is represented by {@link #X_DEFAULT}, a <code>Locale</code> whose language
   * is <code>x-default</code>, and is never replaced by the default locale of
   * the Java virtual machine.
   * </p>
   *
   * <p>
   * The <code>Locale</code> constructor rewrites three language codes to their
   * deprecated forms (<code>he</code> to <code>iw</code>, <code>yi</code> to
   * <code>ji</code>, <code>id</code> to <code>in</code>); this converter maps
   * them back to the current codes on output, so that formatting a parsed
   * value returns the original tag. The canonical string form is the RFC 5646
   * tag, <em>not</em> <code>Locale.toString()</code>, which uses an underscore
   * separator.
   * </p>
   *
   * <p>
   * A <code>Locale</code> that cannot be represented as a tag of the subset
   * above, such as one with a variant, is rejected by {@link #format} rather
   * than being written with the unrepresentable information silently removed.
   * </p>
   *
   * This is compliant, with the restrictions above, with the following syntaxes:
   * <ul>
   * <li>IETF draft-codere-ldapsyntax <code>Language</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.19.1)</li>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>language</code></li>
   * </ul>
   **/
  public static class LocaleTypeConverter extends DataConverter
  {
    public static final char TAG_SEPARATOR = '-';

    /** Language tag that denotes the default alternative, as in XMP. */
    public static final String X_DEFAULT_TAG = "x-default";

    /**
     * Value of the <code>x-default</code> tag. Any <code>Locale</code> whose
     * language is <code>x-default</code> is equal to it.
     */
    public static final Locale X_DEFAULT = new Locale(X_DEFAULT_TAG);

    /**
     * Language codes rewritten by the <code>Locale</code> constructor, and the
     * current ISO 639-1 code each one corresponds to. The two arrays are
     * parallel.
     */
    private static final String[] DEPRECATED_CODES = { "iw", "ji", "in" };
    private static final String[] CURRENT_CODES = { "he", "yi", "id" };

    private static final DataConverter instance = new LocaleTypeConverter();

    public LocaleTypeConverter()
    {
      super(Locale.class, true);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    /**
     * Returns the current ISO 639-1 code for a language code stored by
     * <code>Locale</code>, or the code itself if it is not one of the three
     * rewritten by the <code>Locale</code> constructor.
     */
    private static String toCurrentCode(String language)
    {
      for (int i = 0; i < DEPRECATED_CODES.length; i++)
      {
        if (DEPRECATED_CODES[i].equals(language))
        {
          return CURRENT_CODES[i];
        }
      }
      return language;
    }

    private static boolean isLowerCase(String s)
    {
      for (int i = 0; i < s.length(); i++)
      {
        if ((s.charAt(i) < 'a') || (s.charAt(i) > 'z'))
        {
          return false;
        }
      }
      return true;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      if (value.toString().equalsIgnoreCase(X_DEFAULT_TAG))
      {
        return X_DEFAULT;
      }
      String[] tag = LanguageTags.parse(value, lenient);
      if (tag[1].length() == 0)
      {
        return new Locale(tag[0]);
      }
      return new Locale(tag[0], tag[1]);
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof Locale) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      Locale locale = (Locale) value;
      if (locale.equals(X_DEFAULT))
      {
        return toAppendTo.append(X_DEFAULT_TAG);
      }
      String language = toCurrentCode(locale.getLanguage());
      String region = locale.getCountry();
      String variant = locale.getVariant();

      if ((language.length() == 3) && isLowerCase(language))
      {
        // A 3-letter code is written as its ISO 639-1 code.
        String code = LanguageTags.toISO6391(language);
        if (code != null)
        {
          language = code;
        }
      }
      if ((language.length() != 2) || (isLowerCase(language) == false))
      {
        throw new IllegalArgumentException("Locale language '" + language
            + "' is not a 2 letter ISO 639-1 code and cannot be represented");
      }
      if (variant.length() != 0)
      {
        throw new IllegalArgumentException("Locale variant '" + variant
            + "' cannot be represented as a language tag");
      }
      toAppendTo.append(language);
      if (region.length() == 0)
      {
        return toAppendTo;
      }
      if ((region.length() != 2) || (region.charAt(0) < 'A') || (region.charAt(0) > 'Z')
          || (region.charAt(1) < 'A') || (region.charAt(1) > 'Z'))
      {
        throw new IllegalArgumentException("Locale region '" + region
            + "' is not an ISO 3166-1 code and cannot be represented");
      }
      toAppendTo.append(TAG_SEPARATOR);
      toAppendTo.append(region);
      return toAppendTo;
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }
  }
  
  /** Type converter to native Java Objects for an URI. This 
   *  returns a <code>java.net.URI</code> instance.
   *  
   * <p>
   * Values are URI references conforming to IETF RFC 3986. The empty string,
   * and strings containing characters outside the US-ASCII visible
   * repertoire (such as IRIs, which <code>java.net.URI</code> otherwise
   * accepts), are rejected. A <code>URI</code> containing such characters is
   * also rejected by {@link #format}.
   * </p>
   *  
   * This is equivalent to the following syntax:
   * 
   * <ul> 
   *  <li>XMLSchema 1.1 <code>anyURI</code>, restricted as described above</li>
   *  <li>IETF draft-codere-ldapsyntax <code>URI</code> syntax</li>
   * </ul>
   */
  public static class URIConverter extends DataConverter
  {
    private static final DataConverter instance = new URIConverter();

    public URIConverter()
    {
      super(URI.class, true);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    /**
     * Returns the index of the first character that is not a visible
     * US-ASCII character (U+0021 to U+007E), or -1 if there is none.
     */
    private static int indexOfNonVisibleAscii(String value)
    {
      for (int i = 0; i < value.length(); i++)
      {
        char c = value.charAt(i);
        if ((c < 0x21) || (c > 0x7E))
        {
          return i;
        }
      }
      return -1;
    }

    public Object parseObject(String value) throws ParseException
    {
      if (value.length() == 0)
      {
        throw new ParseException("Empty URI is not allowed.", 0);
      }
      int index = indexOfNonVisibleAscii(value);
      if (index != -1)
      {
        throw new ParseException("URI contains a character that is not visible US-ASCII.", index);
      }
      URI uri = null;
      try
      {
        uri = new URI(value);
      } catch (URISyntaxException e)
      {
        throw new ParseException("String does not match required syntax for '"
            + clz.getName() + "'", 0);
      }
      return uri;
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof URI) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      String s = value.toString();
      if ((s.length() == 0) || (indexOfNonVisibleAscii(s) != -1))
      {
        throw new IllegalArgumentException("URI '" + s
            + "' is not a non-empty RFC 3986 URI reference");
      }
      return toAppendTo.append(s);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return parseObject(value.toString());
    }
  }
  
  
  /** Type converter to native Java Objects for an ASN.1 OBJECT IDENTIFIER. An <code>OID</code> 
   * is represented by a Java Object of type <code>String</code> holding the
   * dotted-decimal form.
   * 
   * <p>
   * The accepted syntax is the <code>numericoid</code> production of IETF
   * RFC 4512: at least two arcs, each a non-negative decimal number of any
   * size, separated by <code>'.'</code>. In addition, the value must be a
   * valid ASN.1 OBJECT IDENTIFIER (ITU-T X.660): the first arc is 0, 1 or 2,
   * and when the first arc is 0 or 1 the second arc is at most 39.
   * </p>
   * 
   * <p>
   * When <code>lenient</code> is <code>false</code>, arcs with leading zeros
   * are rejected. When <code>lenient</code> is <code>true</code>, which is the
   * default, leading zeros are removed.
   * </p>
   * 
   * <p>
   * Restriction: the <code>descr</code> (short name) form of the LDAP OID
   * syntax and the ASN.1 NameForm are not supported.
   * </p>
   * 
   * <p>
   * {@link #format} accepts a <code>String</code> or an <code>int[]</code>
   * and always writes the canonical dotted-decimal form.
   * </p>
   */
  public static class OIDConverter extends DataConverter
  {
    private static final DataConverter instance = new OIDConverter();

    public OIDConverter()
    {
      super(String.class, true);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      String oid;
      if (value instanceof int[])
      {
        int[] v = (int[]) value;
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < v.length; i++)
        {
          if (i > 0)
          {
            buffer.append('.');
          }
          buffer.append(v[i]);
        }
        oid = buffer.toString();
      }
      else if (value instanceof String)
      {
        oid = (String) value;
      }
      else
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "' or int[]");
      }
      try
      {
        return toAppendTo.append((String) parseObject(oid));
      } catch (ParseException e)
      {
        throw new IllegalArgumentException("Illegal OBJECT IDENTIFIER '" + oid + "': "
            + e.getMessage());
      }
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      int len = value.length();
      int i = 0;
      int arcs = 0;
      int firstArc = 0;
      StringBuffer buffer = new StringBuffer(len);
      while (true)
      {
        int start = i;
        while ((i < len) && (value.charAt(i) >= '0') && (value.charAt(i) <= '9'))
        {
          i++;
        }
        if (i == start)
        {
          throw new ParseException("Expecting positive number but found another character", i);
        }
        int digits = start;
        if ((value.charAt(start) == '0') && ((i - start) > 1))
        {
          if (lenient == false)
          {
            throw new ParseException("Leading zeros are not allowed in an arc", start);
          }
          while ((digits < i - 1) && (value.charAt(digits) == '0'))
          {
            digits++;
          }
        }
        String arc = value.subSequence(digits, i).toString();
        if (arcs == 0)
        {
          if (arc.length() != 1 || arc.charAt(0) > '2')
          {
            throw new ParseException("First arc must be 0, 1 or 2", start);
          }
          firstArc = arc.charAt(0) - '0';
        }
        else if ((arcs == 1) && (firstArc < 2))
        {
          if ((arc.length() > 2) || (Integer.parseInt(arc) > 39))
          {
            throw new ParseException("Second arc must be between 0 and 39", start);
          }
        }
        buffer.append(arc);
        arcs++;
        if (i == len)
        {
          break;
        }
        if (value.charAt(i) != '.')
        {
          throw new ParseException("Expecting '.' but found another character", i);
        }
        buffer.append('.');
        i++;
      }
      if (arcs < 2)
      {
        throw new ParseException("An OBJECT IDENTIFIER must have at least two arcs", len);
      }
      return buffer.toString();
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence)value);
    }
  }
  

  /**
   * Type converter to native Java Objects for NAME definition as specified in
   * W3C HTML 4.01 specification. A <code>ASCIIName</code> is represented by a
   * Java Object of type <code>String</code>.
   * 
   * <p>
   * ASCIIName values must begin with a letter ([A-Za-z]) and may be followed by
   * any number of letters, digits ([0-9]), hyphens ("-"), underscores ("_"),
   * colons (":"), and periods (".").
   * </p>
   * 
   * When in <code>lenient</code> mode, when parsing, all invalid characters are automatically
   * removed and empty strings are allowed. 
   *
   * The default mode is not lenient.
   * 
   * */
  public static class ASCIINameConverter extends DataConverter
  {
    private static final DataConverter instance = new ASCIINameConverter();
    
    /**
     * Verifies if the character is a subset of XML NAME type, which is
     * compatible with HTML4 standard.
     * 
     * @param ch
     *          [in] The character to verify
     * @return true if character is valid, false otherwise.
     */
    public static boolean isNameChar(char ch)
    {
      //@formatter:off
      if (isNameStartChar(ch)==true) return true;
      if (ch==':') return true;
      if (ch=='_') return true;
      if (ch=='-') return true;
      if (ch=='.') return true;
      if ((ch >= '0') && (ch <= '9')) return true;
      //@formatter:on
      return false;
    }

    /**
     * Verifies if the start character is a subset of XML NAME type, which is
     * compatible with HTML4 standard.
     * 
     * @param ch
     *          [in] The character to verify
     * @return true if character is valid, false otherwise.
     */
    public static boolean isNameStartChar(char ch)
    {
      //@formatter:off
      if ((ch >= 'A') && (ch <= 'Z')) return true;
      if ((ch >= 'a') && (ch <= 'z')) return true;
      return false;
      //@formatter:on
    }

    public ASCIINameConverter(boolean lenient)
    {
      super(String.class, lenient);
    }

    public ASCIINameConverter()
    {
      this(false);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      int inLength = value.length();
      int outOffset;
      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }
      char c;
      char[] outBuffer = new char[inLength];
      if (lenient == false)
      {
        outOffset = inLength;
        c = value.charAt(0);
        if (isNameStartChar(c) == false)
        {
          throw new ParseException("Illegal character '" + c + "' found.", 0);
        }
        outBuffer[0] = c;
        for (int i = 1; i < value.length(); i++)
        {
          c = value.charAt(i);
          if (isNameChar(c) == false)
          {
            throw new ParseException("Illegal character '" + c + "' found.", i);
          }
          outBuffer[i] = c;
        }
      }
      else
      {
        // Invalid characters are removed; the first character kept must be
        // a valid start character, so that the result is a valid name.
        outOffset = 0;
        for (int i = 0; i < inLength; i++)
        {
          c = value.charAt(i);
          if (((outOffset == 0) && (isNameStartChar(c) == false)) || (isNameChar(c) == false))
          {
            continue;
          }
          outBuffer[outOffset++] = c;
        }
      }
      String result = new String(outBuffer, 0, outOffset);
      outBuffer = null;
      return result;
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      return toAppendTo.append(value.toString());
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    
  }
  
  
  /**
   * Base class for the converters of XML names ({@link XSDNCNameConverter},
   * {@link XSDNameConverter}, {@link XSDNMTOKENConverter}, and, with the narrower
   * character classes of the LDAP syntaxes, {@link NCNameConverter} and
   * {@link QualifiedNameConverter}). A name is represented by a Java Object of
   * type <code>String</code>.
   * 
   * <p>
   * The characters allowed are the <code>NameStartChar</code> and
   * <code>NameChar</code> productions of the W3C Extensible Markup Language
   * (XML) 1.1 specification, which are also those of XML 1.0 Fifth Edition and
   * hence of the W3C XML Schema 1.1 name types. Characters outside the Basic
   * Multilingual Plane (U+10000 to U+EFFFF) are allowed and are represented as
   * surrogate pairs.
   * </p>
   * 
   * <p>
   * When in <code>lenient</code> mode, invalid characters are removed, so that
   * the result is a valid name, and empty strings are allowed. Otherwise
   * invalid characters cause an exception.
   * </p>
   */
  public static abstract class XMLNameConverter extends DataConverter
  {
    /** Name without a colon (NCName). */
    protected static final int KIND_NCNAME = 0;
    /** Name with an optional prefix separated by a single colon (QName). */
    protected static final int KIND_QNAME = 1;
    /** Name in which colons are ordinary name characters (Name). */
    protected static final int KIND_NAME = 2;
    /** Sequence of name characters without a start character rule (NMTOKEN). */
    protected static final int KIND_NMTOKEN = 3;

    /** One of the <code>KIND_xxx</code> constants. */
    private final int kind;

    protected XMLNameConverter(boolean lenient, int kind)
    {
      super(String.class, lenient);
      this.kind = kind;
    }

    /**
     * Verifies if the character is an XML 1.1 <code>NameStartChar</code>,
     * excluding the colon. Only supports characters in the Basic Multilingual
     * Plane (BMP).
     * 
     * @param ch
     *          [in] The character to verify.
     * @return true if character is valid, false otherwise.
     */
    public static boolean isNameStartChar(char ch)
    {
      //@formatter:off
      if ((ch >= 'A') && (ch <= 'Z')) return true;
      if ((ch >= 'a') && (ch <= 'z')) return true;
      if (ch == '_') return true;
      if ((ch >= 0xC0) && (ch <= 0xD6)) return true;
      if ((ch >= 0xD8) && (ch <= 0xF6)) return true;
      if ((ch >= 0xF8) && (ch <= 0x2FF)) return true;
      if ((ch >= 0x370) && (ch <= 0x37D)) return true;
      if ((ch >= 0x37F) && (ch <= 0x1FFF)) return true;
      if ((ch >= 0x200C) && (ch <= 0x200D)) return true;
      if ((ch >= 0x2070) && (ch <= 0x218F)) return true;
      if ((ch >= 0x2C00) && (ch <= 0x2FEF)) return true;
      if ((ch >= 0x3001) && (ch <= 0xD7FF)) return true;
      if ((ch >= 0xF900) && (ch <= 0xFDCF)) return true;
      if ((ch >= 0xFDF0) && (ch <= 0xFFFD)) return true;
      return false;
      //@formatter:on
    }

    /**
     * Verifies if the character is an XML 1.1 <code>NameChar</code>,
     * excluding the colon. Only supports characters in the Basic Multilingual
     * Plane (BMP).
     * 
     * @param ch
     *          [in] The character to verify.
     * @return true if character is valid, false otherwise.
     */
    public static boolean isNameChar(char ch)
    {
      //@formatter:off
      if (isNameStartChar(ch)) return true;
      if ((ch == '-') || (ch == '.') || (ch == 0xB7)) return true;
      if ((ch >= '0') && (ch <= '9')) return true;
      if ((ch >= 0x300) && (ch <= 0x36F)) return true;
      if ((ch >= 0x203F) && (ch <= 0x2040)) return true;
      return false;
      //@formatter:on
    }

    /**
     * Returns <code>true</code> if the character may start a name or the local
     * part of a qualified name. The default implementation uses
     * {@link #isNameStartChar(char)}.
     */
    protected boolean isStartCharacter(char ch)
    {
      return isNameStartChar(ch);
    }

    /**
     * Returns <code>true</code> if the character may appear in a name after
     * its first character, the colon excepted. The default implementation
     * uses {@link #isNameChar(char)}.
     */
    protected boolean isNameCharacter(char ch)
    {
      return isNameChar(ch);
    }

    /**
     * Returns <code>true</code> if characters U+10000 to U+EFFFF, encoded as
     * surrogate pairs, are allowed. The default implementation returns
     * <code>true</code>.
     */
    protected boolean allowSupplementary()
    {
      return true;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return parse(value, lenient);
    }

    /**
     * Parses and verifies a value.
     * 
     * @param value
     *          [in] The value to parse.
     * @param lenient
     *          [in] <code>true</code> to repair a value that does not conform,
     *          <code>false</code> to reject it.
     * @return The value as a <code>String</code>.
     * @throws ParseException
     *           If the value does not conform and cannot be repaired.
     */
    protected Object parse(CharSequence value, boolean lenient) throws ParseException
    {
      int inLength = value.length();
      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }
      StringBuffer out = new StringBuffer(inLength);
      // Length of out where the current part (prefix or local part) starts.
      int partStart = 0;
      boolean foundColon = false;
      int i = 0;
      while (i < inLength)
      {
        char c = value.charAt(i);
        int count = 1;
        boolean valid;
        if (allowSupplementary() && (c >= 0xD800) && (c <= 0xDB7F) && (i + 1 < inLength)
            && (value.charAt(i + 1) >= 0xDC00) && (value.charAt(i + 1) <= 0xDFFF))
        {
          // U+10000 to U+EFFFF: both a start character and a name character.
          count = 2;
          valid = true;
        }
        else if (c == ':')
        {
          switch (kind)
          {
            case KIND_NAME:
            case KIND_NMTOKEN:
              valid = true;
              break;
            case KIND_QNAME:
              // Only one colon, and only after a non-empty prefix.
              valid = (foundColon == false) && (out.length() > partStart);
              break;
            default:
              valid = false;
              break;
          }
        }
        else if ((kind != KIND_NMTOKEN) && (out.length() == partStart))
        {
          valid = isStartCharacter(c);
        }
        else
        {
          valid = isNameCharacter(c);
        }
        if (valid == false)
        {
          if (lenient == false)
          {
            throw new ParseException("Illegal character '" + c + "' found.", i);
          }
          i++;
          continue;
        }
        for (int j = 0; j < count; j++)
        {
          out.append(value.charAt(i + j));
        }
        if ((c == ':') && (kind == KIND_QNAME))
        {
          foundColon = true;
          partStart = out.length();
        }
        i += count;
      }
      // The local part of a QName must not be empty.
      if (foundColon && (out.length() == partStart))
      {
        if (lenient == false)
        {
          throw new ParseException("Missing local part after ':'.", inLength);
        }
        out.setLength(out.length() - 1);
      }
      return out.toString();
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    /**
     * Formats a value. The value is verified as if parsed with
     * <code>lenient</code> set to <code>false</code>, whatever the mode of this
     * converter: a value that does not conform is rejected and never repaired.
     * 
     * @throws IllegalArgumentException
     *           If the value is not a <code>CharSequence</code> or does not conform.
     */
    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      try
      {
        return toAppendTo.append((String) parse((CharSequence) value, false));
      } catch (ParseException e)
      {
        throw new IllegalArgumentException("Value cannot be represented: " + e.getMessage());
      }
    }
  }


  /**
   * Type converter to native Java Objects for the IETF draft-codere-ldapsyntax
   * <code>NCName</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.12.6), a subset of
   * the XML non-colonized name. A value conforms to the regular expression
   * <code>[\p{L}\p{Nl}_][\p{L}\p{Nl}\p{Nd}._-]*</code>.
   * 
   * <p>
   * Restriction: characters outside the Basic Multilingual Plane are rejected,
   * as their General_Category is not available on all supported platforms.
   * </p>
   * 
   * <p>
   * When in <code>lenient</code> mode, invalid characters are removed, so that
   * the result is a valid name, and empty strings are allowed. The default
   * mode is not lenient.
   * </p>
   * 
   * <p>
   * Use {@link XSDNCNameConverter} for the XML Schema <code>NCName</code> type,
   * which allows more characters.
   * </p>
   * */
  public static class NCNameConverter extends XMLNameConverter
  {
    private static final DataConverter instance = new NCNameConverter();

    public NCNameConverter(boolean lenient)
    {
      super(lenient, KIND_NCNAME);
    }

    public NCNameConverter()
    {
      this(false);
    }

    protected NCNameConverter(boolean lenient, int kind)
    {
      super(lenient, kind);
    }

    /**
     * Verifies if the character matches <code>[\p{L}\p{Nl}_]</code>, the
     * first character of a name of the NCName syntax.
     * 
     * @param ch
     *          [in] The character to verify.
     * @return true if character is valid, false otherwise.
     */
    public static boolean isNameStartChar(char ch)
    {
      return Character.isLetter(ch) || (Character.getType(ch) == Character.LETTER_NUMBER)
          || (ch == '_');
    }

    /**
     * Verifies if the character matches
     * <code>[\p{L}\p{Nl}\p{Nd}._-]</code>, the other characters of a name of
     * the NCName syntax.
     * 
     * @param ch
     *          [in] The character to verify.
     * @return true if character is valid, false otherwise.
     */
    public static boolean isNameChar(char ch)
    {
      return isNameStartChar(ch) || (Character.getType(ch) == Character.DECIMAL_DIGIT_NUMBER)
          || (ch == '.') || (ch == '-');
    }

    protected boolean isStartCharacter(char ch)
    {
      return isNameStartChar(ch);
    }

    protected boolean isNameCharacter(char ch)
    {
      return isNameChar(ch);
    }

    protected boolean allowSupplementary()
    {
      return false;
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Type converter to native Java Objects for the W3C XML Schema Definition
   * Language (XSD) 1.1 <code>NCName</code> type, canonical representation: an
   * XML name without colon, using the characters of {@link XMLNameConverter}.
   * 
   * The default mode is not lenient.
   * */
  public static class XSDNCNameConverter extends XMLNameConverter
  {
    private static final DataConverter instance = new XSDNCNameConverter();

    public XSDNCNameConverter(boolean lenient)
    {
      super(lenient, KIND_NCNAME);
    }

    public XSDNCNameConverter()
    {
      this(false);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }
  
  
  /**
   * Type converter to native Java Objects for the IETF draft-codere-ldapsyntax
   * <code>Qualified Name</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.12.7), of
   * the form <code>prefix:local-part</code> or <code>local-part</code>, where
   * both parts are names as defined in {@link NCNameConverter}.
   * 
   * The default mode is not lenient.
   * */
  public static class QualifiedNameConverter extends NCNameConverter
  {
    private static final DataConverter instance = new QualifiedNameConverter();

    public QualifiedNameConverter(boolean lenient)
    {
      super(lenient, KIND_QNAME);
    }

    public QualifiedNameConverter()
    {
      this(false);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Type converter to native Java Objects for an XML name (<code>Name</code>):
   * a <code>NameStartChar</code> or colon followed by any number of
   * <code>NameChar</code> or colons. See {@link XMLNameConverter} for the
   * characters allowed.
   * 
   * <p>
   * In XML Schema the whitespace of a <code>Name</code> is collapsed before
   * validation. This converter only accepts the canonical representation, so
   * leading and trailing whitespace is rejected, or removed in
   * <code>lenient</code> mode.
   * </p>
   * 
   * The default mode is not lenient.
   * 
   * This is equivalent to the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>Name</code>, canonical representation</li>
   * </ul>
   * */
  public static class XSDNameConverter extends XMLNameConverter
  {
    private static final DataConverter instance = new XSDNameConverter();

    public XSDNameConverter(boolean lenient)
    {
      super(lenient, KIND_NAME);
    }

    public XSDNameConverter()
    {
      this(false);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }


  /**
   * Type converter to native Java Objects for an XML name token
   * (<code>NMTOKEN</code>): one or more <code>NameChar</code> or colons, with
   * no rule on the first character. See {@link XMLNameConverter} for the
   * characters allowed.
   * 
   * <p>
   * In XML Schema the whitespace of a <code>NMTOKEN</code> is collapsed before
   * validation. This converter only accepts the canonical representation, so
   * leading and trailing whitespace is rejected, or removed in
   * <code>lenient</code> mode.
   * </p>
   * 
   * The default mode is not lenient.
   * 
   * This is equivalent to the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Definition Language (XSD) 1.1 <code>NMTOKEN</code>, canonical representation</li>
   * </ul>
   * */
  public static class XSDNMTOKENConverter extends XMLNameConverter
  {
    private static final DataConverter instance = new XSDNMTOKENConverter();

    public XSDNMTOKENConverter(boolean lenient)
    {
      super(lenient, KIND_NMTOKEN);
    }

    public XSDNMTOKENConverter()
    {
      this(false);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }
  
  
  /**
   * Type converter to native Java Objects for an Internet media type. A media
   * type is represented by a Java Object of type
   * {@link com.optimasc.lang.MediaType}.
   * 
   * <p>
   * As defined by the IETF draft-codere-ldapsyntax <code>Media Type</code>
   * syntax (OID: 1.3.6.1.4.1.61799.5.40.26.5), a value is a type and a subtype
   * separated by <code>'/'</code>, each matching the
   * <code>restricted-name</code> of IETF RFC 6838:
   * <code>[A-Za-z0-9][A-Za-z0-9!#$&amp;^_.+-]{0,126}</code>. The value is
   * limited to 3 to 255 characters.
   * </p>
   * 
   * <p>
   * Parameters (<code>;name=value</code>) following the subtype are also
   * accepted; their syntax is verified by {@link com.optimasc.lang.MediaType}.
   * </p>
   */
  public static class MediaTypeConverter extends DataConverter 
  {
    private static final DataConverter instance = new MediaTypeConverter();

    /** Minimum length in characters of a media type. */
    public static final int MIN_LENGTH = 3;
    /** Maximum length in characters of a media type. */
    public static final int MAX_LENGTH = 255;

     public MediaTypeConverter()
     {
       super(MediaType.class,false);
     }

    public static DataConverter getInstance()
    {
      return instance;
    }

    /** Maximum length in characters of the type or of the subtype. */
    private static final int MAX_NAME_LENGTH = 127;

    /**
     * Verifies that the value starts with a <code>type/subtype</code> pair of
     * RFC 6838 restricted names, optionally followed by parameters starting
     * with <code>';'</code>, which are not verified here.
     * 
     * @return -1 if the value is valid, otherwise the index of the error.
     */
    private static int indexOfError(String value)
    {
      int length = value.length();
      if ((length < MIN_LENGTH) || (length > MAX_LENGTH))
      {
        return 0;
      }
      int separator = value.indexOf('/');
      if (separator == -1)
      {
        return length;
      }
      int error = indexOfNameError(value, 0, separator);
      if (error != -1)
      {
        return error;
      }
      int parameters = value.indexOf(';', separator + 1);
      return indexOfNameError(value, separator + 1, (parameters == -1) ? length : parameters);
    }

    /** Verifies a restricted name between start and end. */
    private static int indexOfNameError(String value, int start, int end)
    {
      if ((end <= start) || (end - start > MAX_NAME_LENGTH))
      {
        return start;
      }
      for (int i = start; i < end; i++)
      {
        char c = value.charAt(i);
        boolean alphaNumeric = ((c >= 'A') && (c <= 'Z')) || ((c >= 'a') && (c <= 'z'))
            || ((c >= '0') && (c <= '9'));
        if (alphaNumeric || ((i > start) && ("!#$&^_.+-".indexOf(c) != -1)))
        {
          continue;
        }
        return i;
      }
      return -1;
    }

    public Object parseObject(String value) throws ParseException
    {
      int error = indexOfError(value);
      if (error != -1)
      {
        throw new ParseException("Media type must be a type/subtype pair of restricted names, "
            + "optionally followed by parameters, of 3 to 255 characters.", error);
      }
      return new MediaType(value);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return parseObject(value.toString());
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      MediaType mt;
      if ((value instanceof MediaType) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      mt = (MediaType) value;
      String s = mt.toString();
      if (indexOfError(s) != -1)
      {
        throw new IllegalArgumentException("Media type '" + s
            + "' cannot be represented: the type and subtype must be restricted names, "
            + "and the value must have 3 to 255 characters");
      }
      return toAppendTo.append(s);
    }
  }
  

  /**
   * Type converter to native Java Objects for a subset of visible US-ASCII
   * characters. This is equivalent to the ASN.1 <code>VisibleString</code>. A
   * <code>VisibleString</code> is represented by a Java Object of type
   * <code>String</code>.
   * 
   * <p>
   * The repertoire is U+0020 to U+007E.
   * </p>
   * 
   * When in <code>lenient</code> mode, which is the default, characters
   * outside the repertoire are removed. Otherwise they cause an exception.
   * The empty string is allowed.
   * 
   * This is equivalent to the following syntax:
   * 
   * <ul> 
   *  <li>ASN.1 <code>VisibleString</code></li>
   *  <li>LDAP IETF RFC  <code>VisibleString</code> syntax</li>
   * </ul>
   * 
   * */
  public static class VisibleStringConverter extends DataConverter
  {
    private static final DataConverter instance = new VisibleStringConverter();
    
    public static final int MIN_CHAR = 32;
    public static final int MAX_CHAR = 126;

    public VisibleStringConverter()
    {
      super(String.class, true);
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    

    public Object parseObject(CharSequence value) throws ParseException
    {
      return parse(value, lenient);
    }

    /**
     * Parses and verifies a value.
     * 
     * @param value
     *          [in] The value to parse.
     * @param lenient
     *          [in] <code>true</code> to repair a value that does not conform,
     *          <code>false</code> to reject it.
     * @return The value as a <code>String</code>.
     * @throws ParseException
     *           If the value does not conform and cannot be repaired.
     */
    protected Object parse(CharSequence value, boolean lenient) throws ParseException
    {
      int inLength = value.length();
      int outOffset;
      char c;
      char[] outBuffer = new char[inLength];
      if (lenient == false)
      {
        outOffset = inLength;
        for (int i = 0; i < inLength; i++)
        {
          c = value.charAt(i);
          if ((c < MIN_CHAR) || (c > MAX_CHAR))
          {
            throw new ParseException("Illegal character '" + c + "' found.", i);
          }
          outBuffer[i] = c;
        }
      }
      else
      {
        outOffset = 0;
        for (int i = 0; i < inLength; i++)
        {
          c = value.charAt(i);
          if ((c < MIN_CHAR) || (c > MAX_CHAR))
          {
            continue;
          }
          outBuffer[outOffset++] = c;
        }
      }
      String result = new String(outBuffer, 0, outOffset);
      outBuffer = null;
      return result;
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    /**
     * Formats a value. The value is verified as if parsed with
     * <code>lenient</code> set to <code>false</code>, whatever the mode of this
     * converter: a value that does not conform is rejected and never repaired.
     * 
     * @throws IllegalArgumentException
     *           If the value is not a <code>CharSequence</code> or does not conform.
     */
    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      try
      {
        return toAppendTo.append((String) parse((CharSequence) value, false));
      } catch (ParseException e)
      {
        throw new IllegalArgumentException("Value cannot be represented: " + e.getMessage());
      }
    }

  }
  
  
  
  /**
   * Type converter to native Java Objects for the ASN.1 <code>IA5String</code>
   * repertoire (U+0000 to U+007F). An <code>IA5String</code> is represented by
   * a Java Object of type <code>String</code>.
   * 
   * When in <code>lenient</code> mode, which is the default, characters
   * outside the repertoire are removed. Otherwise they cause an exception.
   * The empty string is allowed.
   * 
   * This is equivalent to the following syntax:
   * 
   * <ul> 
   *  <li>ASN.1 <code>IA5String</code></li>
   *  <li>LDAP IETF RFC 4517 <code>IA5 String</code> syntax (OID: 1.3.6.1.4.1.1466.115.121.1.26)</li>
   * </ul>
   * 
   * */
  public static class IA5StringConverter extends DataConverter
  {
    private static final DataConverter instance = new IA5StringConverter();
    
    public static final int MIN_CHAR = 0;
    public static final int MAX_CHAR = 127;

    public IA5StringConverter()
    {
      super(String.class, true);
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return parse(value, lenient);
    }

    /**
     * Parses and verifies a value.
     * 
     * @param value
     *          [in] The value to parse.
     * @param lenient
     *          [in] <code>true</code> to repair a value that does not conform,
     *          <code>false</code> to reject it.
     * @return The value as a <code>String</code>.
     * @throws ParseException
     *           If the value does not conform and cannot be repaired.
     */
    protected Object parse(CharSequence value, boolean lenient) throws ParseException
    {
      int inLength = value.length();
      char[] outBuffer = new char[inLength];
      int outOffset = 0;
      for (int i = 0; i < inLength; i++)
      {
        char c = value.charAt(i);
        if (c > MAX_CHAR)
        {
          if (lenient == false)
          {
            throw new ParseException("Illegal character '" + c + "' found.", i);
          }
          continue;
        }
        outBuffer[outOffset++] = c;
      }
      return new String(outBuffer, 0, outOffset);
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    /**
     * Formats a value. The value is verified as if parsed with
     * <code>lenient</code> set to <code>false</code>, whatever the mode of this
     * converter: a value that does not conform is rejected and never repaired.
     * 
     * @throws IllegalArgumentException
     *           If the value is not a <code>CharSequence</code> or does not conform.
     */
    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      try
      {
        return toAppendTo.append((String) parse((CharSequence) value, false));
      } catch (ParseException e)
      {
        throw new IllegalArgumentException("Value cannot be represented: " + e.getMessage());
      }
    }
  }
  
  
  /**
   * Type converter to native Java Objects for a subset of visible US-ASCII
   * characters. This is equivalent to the ASN.1 <code>PrintableString</code>. A
   * <code>PrintableString</code> is represented by a Java Object of type
   * <code>String</code>.
   * 
   * <p>
   * The repertoire is <code>A-Z</code>, <code>a-z</code>, <code>0-9</code>,
   * SPACE and <code>' ( ) + , - . / : = ?</code>.
   * </p>
   * 
   * When in <code>lenient</code> mode, which is the default, characters
   * outside the repertoire are removed and empty strings are allowed.
   * Otherwise they cause an exception.
   * 
   * This is equivalent to the following syntax:
   * 
   * <ul> 
   *  <li>ASN.1 <code>PrintableString</code></li>
   *  <li>LDAP IETF RFC 4517 <code>Printable String</code> syntax (OID: 1.3.6.1.4.1.1466.115.121.1.44)</li>
   * </ul>
   * 
   * */
  public static class PrintableStringConverter extends DataConverter
  {
    private static final DataConverter instance = new PrintableStringConverter();
    
    public static final int MIN_CHAR = 32;
    public static final int MAX_CHAR = 122;
    
    /**
     * Verifies if the character is in the ASN.1 <code>PrintableString</code>
     * repertoire: <code>A-Z</code>, <code>a-z</code>, <code>0-9</code>,
     * SPACE and <code>' ( ) + , - . / : = ?</code>.
     * 
     * @param c
     *          [in] The character to verify
     * @return true if the character is valid, false otherwise.
     */
    public static boolean isPrintableChar(char c)
    {
      //@formatter:off
      if ((c >= 'A') && (c <= 'Z')) return true;
      if ((c >= 'a') && (c <= 'z')) return true;
      if ((c >= '0') && (c <= '9')) return true;
      switch (c)
      {
        case ' ': case '\'': case '(': case ')': case '+': case ',':
        case '-': case '.': case '/': case ':': case '=': case '?':
          return true;
        default:
          return false;
      }
      //@formatter:on
    }


    public PrintableStringConverter()
    {
      super(String.class, true);
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    
    
    
    

    public Object parseObject(CharSequence value) throws ParseException
    {
      return parse(value, lenient);
    }

    /**
     * Parses and verifies a value.
     * 
     * @param value
     *          [in] The value to parse.
     * @param lenient
     *          [in] <code>true</code> to repair a value that does not conform,
     *          <code>false</code> to reject it.
     * @return The value as a <code>String</code>.
     * @throws ParseException
     *           If the value does not conform and cannot be repaired.
     */
    protected Object parse(CharSequence value, boolean lenient) throws ParseException
    {
      int inLength = value.length();
      int outOffset;
      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty strings are not allowed.", 0);
      }
      char c;
      char[] outBuffer = new char[inLength];
      if (lenient == false)
      {
        outOffset = inLength;
        for (int i = 0; i < inLength; i++)
        {
          c = value.charAt(i);
          switch (c)
          {
            case 32:
            case 39:
            case 61:
            case 63:
              break;
            default:
              if (isPrintableChar(c))
              {
                break;
              }
              throw new ParseException("Illegal character '" + c + "' found.", i);
          }
          outBuffer[i] = c;
        }
      }
      else
      {
        outOffset = 0;
        for (int i = 0; i < inLength; i++)
        {
          c = value.charAt(i);
          switch (c)
          {
            case 32:
            case 39:
            case 61:
            case 63:
              break;
            default:
              if (isPrintableChar(c))
              {
                break;
              }
              continue;
          }
          outBuffer[outOffset++] = c;
        }
      }
      String result = new String(outBuffer, 0, outOffset);
      outBuffer = null;
      return result;
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    /**
     * Formats a value. The value is verified as if parsed with
     * <code>lenient</code> set to <code>false</code>, whatever the mode of this
     * converter: a value that does not conform is rejected and never repaired.
     * 
     * @throws IllegalArgumentException
     *           If the value is not a <code>CharSequence</code> or does not conform.
     */
    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      try
      {
        return toAppendTo.append((String) parse((CharSequence) value, false));
      } catch (ParseException e)
      {
        throw new IllegalArgumentException("Value cannot be represented: " + e.getMessage());
      }
    }

  }
  

  /**
   * Type converter to native Java Objects for the IANA ISO-8859-1 character
   * set. <code>Lating1String</code> is represented by a Java Object of type
   * <code>String</code>.
   * 
   * When in <code>lenient</code> mode, empty strings are allowed. By default
   * parsing is lenient. In all cases, whatever the mode non ISO-8859-1
   * characters will cause an exception.
   * 
   * */
  public static class Latin1StringConverter extends DataConverter
  {
    private static final DataConverter instance = new Latin1StringConverter();
    
    public static final int MIN_CHAR = 0;
    public static final int MAX_CHAR = 255;

    protected Latin1StringConverter()
    {
      super(String.class, true);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      int inLength = value.length();
      int outOffset;
      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }
      char c;
      char[] outBuffer = new char[inLength];
      outOffset = inLength;
      for (int i = 0; i < inLength; i++)
      {
        c = value.charAt(i);
        if ((c < MIN_CHAR) || (c > MAX_CHAR))
        {
          throw new ParseException("Illegal character '" + c + "' found.", i);
        }
        outBuffer[i] = c;
      }
      String result = new String(outBuffer, 0, outOffset);
      outBuffer = null;
      return result;
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      return toAppendTo.append(value.toString());
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    
  }

  /**
   * Boolean type converter. This converter converts a boolean string
   * representation to a <code>Boolean</code> Java Object.
   * 
   * The default canonical representation consists of the <code>TRUE</code> and
   * <code>FALSE</code> tokens in upper-case letters. It is also possible to
   * specify the canonical representations of the true and false values.
   * 
   * <p>
   * In all cases, when in lenient mode, which is the default, the comparison
   * with the true and false tokens are done without case sensitivity. The
   * default mode is lenient.
   * </p>
   * 
   * When <code>lenient</code> is <code>false</code> the converter is compliant
   * with the following syntaxes when using default tokens":
   * <ul>
   * <li>ASN.1 ITU X.680 <code>BOOLEAN</code></li>
   * <li>LDAP IETF RFC 4517 (OID: 1.3.6.1.4.1.1466.115.121.1.7)</li>
   * <li>SQL99 Boolean syntax</li>
   * </ul>
   * 
   * When <code>lenient</code> is <code>true</code> the converter
   * additionally accepts, but does not produce, the following syntaxes when
   * using default tokens:
   * <ul>
   * <li>ISO/IEC 11404 General purpose datatypes.</li>
   * <li>vCard IETF RFC 6350 BOOLEAN type</li>
   * </ul>
   * 
   * <p>
   * Use {@link XSDBooleanConverter} for the W3C XML Schema
   * <code>boolean</code> type, whose lexical space is case-sensitive and
   * whose canonical form is lower case.
   * </p>
   */
  public static class BooleanConverter extends DataConverter
  {
    private static final DataConverter instance = new BooleanConverter();
    
    protected final String TRUE_TOKEN;
    protected final String FALSE_TOKEN;

    public BooleanConverter()
    {
      super(Boolean.class, true);
      TRUE_TOKEN = "TRUE";
      FALSE_TOKEN = "FALSE";
    }

    public BooleanConverter(String falseToken, String trueToken)
    {
      super(Boolean.class, true);
      TRUE_TOKEN = trueToken;
      FALSE_TOKEN = falseToken;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      int inLength = value.length();
      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }
      if ((inLength != TRUE_TOKEN.length()) && (inLength != FALSE_TOKEN.length()))
      {
        throw new ParseException("Invalid boolean token values.", 0);
      }
      // The whole value must match a token, not only its start.
      String str = value.toString();
      if (lenient)
      {
        if (str.equalsIgnoreCase(TRUE_TOKEN))
        {
          return Boolean.TRUE;
        }
        if (str.equalsIgnoreCase(FALSE_TOKEN))
        {
          return Boolean.FALSE;
        }
      }
      else
      {
        if (str.equals(TRUE_TOKEN))
        {
          return Boolean.TRUE;
        }
        if (str.equals(FALSE_TOKEN))
        {
          return Boolean.FALSE;
        }
      }
      throw new ParseException("Invalid boolean token values.", 0);
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof Boolean) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      boolean boolValue = ((Boolean) value).booleanValue();
      if (boolValue)
        return toAppendTo.append(TRUE_TOKEN);
      return toAppendTo.append(FALSE_TOKEN);
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    
  }
  
  /**
   * Boolean type converter for the W3C XML Schema Definition Language (XSD)
   * 1.1 <code>boolean</code> type. This converter converts a boolean string
   * representation to a <code>Boolean</code> Java Object.
   * 
   * <p>
   * The lexical space is <code>true</code>, <code>false</code>, <code>1</code>
   * and <code>0</code>, compared with case sensitivity. The canonical
   * representation produced by {@link #format} is <code>true</code> or
   * <code>false</code>.
   * </p>
   * 
   * <p>
   * When <code>lenient</code> is <code>true</code>, which is the default,
   * all four lexical forms are accepted. When <code>lenient</code> is
   * <code>false</code>, only the canonical representation is accepted.
   * </p>
   */
  public static class XSDBooleanConverter extends BooleanConverter
  {
    private static final DataConverter instance = new XSDBooleanConverter();

    public XSDBooleanConverter()
    {
      super("false", "true");
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      String str = value.toString();
      if (str.equals(TRUE_TOKEN) || (lenient && str.equals("1")))
      {
        return Boolean.TRUE;
      }
      if (str.equals(FALSE_TOKEN) || (lenient && str.equals("0")))
      {
        return Boolean.FALSE;
      }
      throw new ParseException("Invalid boolean token values.", 0);
    }

    public static DataConverter getInstance()
    {
      return instance;
    }
  }

}
