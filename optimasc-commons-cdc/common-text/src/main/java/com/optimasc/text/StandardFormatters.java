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
   * Text type converter. 
   * 
   * The parsing does the following operations:
   * 
   * <ul>
   *    <li>Verifies that string does not contains surrogate characters (General category "Cs")</li>
   *    <li>Verifies that string does not contain private use characters (General category "Co")</li>
   *    <li>Verifies that string does not contain non-characters (PropList "# Cn" category)</li>
   *    <li>Verifies that string does not contain control characters (General category "Cc")
   *      other than CHARACTER TABULATION (U+0009), LINE FEED (U+000A) and CARRIAGE RETURN (U+000D)</li>
   *    <li>Convert all other characters to Unicode normalization form C (NFC)</li>   
   * </ul>
   * 
   * <p>
   * When <code>lenient</code> is <code>true</code>, which is the default, prohibited
   * control and format characters are removed instead of being rejected.
   * </p>
   * 
   * <p>
   * Restriction: characters are checked one UTF-16 code unit at a time, so
   * characters outside the Basic Multilingual Plane, which Java represents as
   * surrogate pairs, are rejected.
   * </p>
   * 
   * The converter is compliant with the following syntaxes, with the
   * restriction above:
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
      int inLength = value.length();

      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }
      
      String resultString = null; 
      if (lenient == true)
      {
       resultString = StringNormalizer.mapAndVerifyString(value, false);
      } else
      {
        StringNormalizer.verifyProhibitedString(value);
        resultString = value.toString();
      }
      resultString = StringNormalizer.toNFC(resultString);
      return resultString;
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
      return toAppendTo.append((String) value);
    }
    
    public static DataConverter getInstance()
    {
      return instance;
    }
    
  }

  /**
   * Normalized String type converter. 
   * 
   * The parsing does the following operations:
   * 
   * <ul>
   *    <li>Verifies that string does not contains surrogate characters (General category "Cs")</li>
   *    <li>Verifies that string does not contain private use characters (General category "Co")</li>
   *    <li>Verifies that string does not contain non-characters (PropList "# Cn" category)</li>
   *    <li>Verifies that string does not contain bidirectional markers or deprecated charters</li>
   *    <li>Removes all control characters </li>
   *    <li>All other control code (e.g., Cc) points or code points with a control function (e.g., Cf) are ignored and removed.</li>
   *    <li>ZERO WIDTH SPACE (U+200B) is ignored and removed.</li>
   *    <li>All other code points with Separator (space, line, or paragraph) property (e.g., Zs, Zl, or Zp) are converted to SPACE (U+0020)</li>
   *      <li>CHARACTER TABULATION (U+0009), LINE FEED (LF) (U+000A), LINE TABULATION (U+000B), FORM FEED (FF) (U+000C), CARRIAGE RETURN (CR) (U+000D), and NEXT
   *       LINE (NEL) (U+0085) are converted to SPACE (U+0020).</li>
   *    <li>Convert all other characters to Unicode normalization form C (NFC)</li>   
   * </ul>
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

    public Object parseObject(CharSequence value) throws ParseException
    {
      int inLength = value.length();
      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }

      if (lenient == false)
      {
        StringNormalizer.verifyProhibitedNormalizedString(value);
      }
      String resultString = StringNormalizer.mapAndVerifyNormalizedString(value,false);
      resultString = StringNormalizer.toNFC(resultString);
      return resultString;
    }
  }

  /**
   * Token String type converter. 
   * 
   * The parsing does the following operations:
   * 
   * <ul>
   *    <li>Verifies that string does not contains surrogate characters (General category "Cs")</li>
   *    <li>Verifies that string does not contain private use characters (General category "Co")</li>
   *    <li>Verifies that string does not contain non-characters (PropList "# Cn" category)</li>
   *    <li>Verifies that string does not contain bidirectional markers or deprecated charters</li>
   *    <li>Removes all control characters </li>
   *    <li>All other control code (e.g., Cc) points or code points with a control function (e.g., Cf) are ignored and removed.</li>
   *    <li>ZERO WIDTH SPACE (U+200B) is ignored and removed.</li>
   *    <li>All other code points with Separator (space, line, or paragraph) property (e.g., Zs, Zl, or Zp) are converted to SPACE (U+0020)</li>
   *      <li>CHARACTER TABULATION (U+0009), LINE FEED (LF) (U+000A), LINE TABULATION (U+000B), FORM FEED (FF) (U+000C), CARRIAGE RETURN (CR) (U+000D), and NEXT
   *       LINE (NEL) (U+0085) are converted to SPACE (U+0020).</li>
   *    <li>Convert all other characters to Unicode normalization form C (NFC)</li>   
   *    <li>Removes all contiguous SPACE characters</li>   
   *    <li>Removes all leading and trailing SPACE characters</li>   
   * </ul>
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

    public Object parseObject(CharSequence value) throws ParseException
    {
      int inLength = value.length();

      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }
      
      if (lenient == false)
      {
        StringNormalizer.verifyProhibitedToken(value);
      }
      String resultString = StringNormalizer.mapAndVerifyNormalizedString(value,false);
      resultString = StringNormalizer.toNFC(resultString);
      return StringNormalizer.collapseWhitespace(resultString.trim());
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
   * It supports a subset of the language tag format defined in IETF RFC 5646.
   * The differences are specified below:
   * </p>
   *
   * <ul>
   * <li>Language codes are based on the 2 lower-case character language code
   * as defined by ISO 639-1. Three-letter ISO 639-2/639-3 codes are not
   * supported.</li>
   * <li>The country code is based on the 2 upper-case character code
   * identifying countries under ISO 3166-1. Numeric UN M.49 region codes are
   * not supported.</li>
   * <li>Script subtags, extension subtags, extended language subtags and
   * private use tags (<code>x-</code>) are not supported, because
   * <code>java.util.Locale</code> cannot represent them on this platform.</li>
   * </ul>
   *
   * <p>
   * Hence the syntax is as follows:
   * </p>
   *
   * <pre>
   *     langtag  = language
   *                 ["-" region]
   *
   *     language = 2ALPHA   ; shortest ISO 639 code
   *     region   = 2ALPHA   ; ISO 3166-1 code
   * </pre>
   *
   * <p>
   * In <code>lenient</code> mode, the case is automatically converted to the
   * correct expected case, and does not raise an error if not correctly set
   * initially.
   * </p>
   *
   * <p>
   * The canonical string form produced by {@link #format} is the RFC 5646 tag,
   * <em>not</em> <code>Locale.toString()</code>, which uses an underscore
   * separator. The <code>Locale</code> constructor additionally rewrites three
   * language codes to their deprecated forms for backward compatibility
   * (<code>he</code> to <code>iw</code>, <code>yi</code> to <code>ji</code>,
   * <code>id</code> to <code>in</code>); this converter maps them back to the
   * current codes on output, so that formatting a parsed value returns the
   * original tag. A value parsed from a deprecated code is therefore formatted
   * using the current code.
   * </p>
   * 
   * <p>
   * Three-letter ISO 639-2 and ISO 639-3 codes such as <code>haw</code> are
   * rejected, although <code>Locale</code> can represent them. This is
   * stricter than the Language syntax.
   * </p>
   *
   * <p>
   * A <code>Locale</code> that cannot be represented as a tag of the subset
   * above is rejected by {@link #format} rather than being written with the
   * unrepresentable information silently removed.
   * </p>
   **/
  public static class LocaleTypeConverter extends DataConverter
  {
    public static final char TAG_SEPARATOR = '-';

    /** Length in characters of a language tag without a region. */
    private static final int LANGUAGE_LENGTH = 2;

    /** Length in characters of a language tag with a region. */
    private static final int TAG_LENGTH = 5;

    /** Offset of the region subtag within a tag of {@link #TAG_LENGTH}. */
    private static final int REGION_OFFSET = 3;

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

    public Object parseObject(CharSequence value) throws ParseException
    {
      int length = value.length();
      char c1;
      char c2;

      // The error offset is the position where the input stops being a
      // valid tag.
      if (length < LANGUAGE_LENGTH)
      {
        int offset = ((length == 1) && (Character.isLetter(value.charAt(0)))) ? 1 : 0;
        throw new ParseException("Language tag must be " + LANGUAGE_LENGTH + " or "
            + TAG_LENGTH + " characters in length.", offset);
      }

      // Lenient: we convert the characters ourselves to lower case.
      if (lenient)
      {
        c1 = Character.toLowerCase(value.charAt(0));
        c2 = Character.toLowerCase(value.charAt(1));
      }
      else
      {
        c1 = value.charAt(0);
        c2 = value.charAt(1);
      }

      if ((c1 < 'a') || (c1 > 'z'))
      {
        throw new ParseException(
            "Language code character is not lower-case ascii letter", 0);
      }
      if ((c2 < 'a') || (c2 > 'z'))
      {
        throw new ParseException(
            "Language code character is not lower-case ascii letter", 1);
      }

      String language = new String(new char[] { c1, c2 });

      if (length == LANGUAGE_LENGTH)
      {
        return new Locale(language);
      }

      if (value.charAt(LANGUAGE_LENGTH) != TAG_SEPARATOR)
      {
        throw new ParseException("Expected tag separator '" + TAG_SEPARATOR + "'",
            LANGUAGE_LENGTH);
      }
      if (length != TAG_LENGTH)
      {
        throw new ParseException("Language tag must be " + LANGUAGE_LENGTH + " or "
            + TAG_LENGTH + " characters in length.", Math.min(length, TAG_LENGTH));
      }

      // Lenient: we convert the characters ourselves to upper case.
      if (lenient)
      {
        c1 = Character.toUpperCase(value.charAt(REGION_OFFSET));
        c2 = Character.toUpperCase(value.charAt(REGION_OFFSET + 1));
      }
      else
      {
        c1 = value.charAt(REGION_OFFSET);
        c2 = value.charAt(REGION_OFFSET + 1);
      }

      if ((c1 < 'A') || (c1 > 'Z'))
      {
        throw new ParseException("Region code character is not upper-case ascii letter",
            REGION_OFFSET);
      }
      if ((c2 < 'A') || (c2 > 'Z'))
      {
        throw new ParseException("Region code character is not upper-case ascii letter",
            REGION_OFFSET + 1);
      }

      return new Locale(language, new String(new char[] { c1, c2 }));
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof Locale) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      Locale locale = (Locale) value;
      String language = toCurrentCode(locale.getLanguage());
      String region = locale.getCountry();
      String variant = locale.getVariant();

      if (language.length() != LANGUAGE_LENGTH)
      {
        throw new IllegalArgumentException("Locale language '" + language
            + "' is not a 2 character ISO 639-1 code and cannot be represented");
      }
      if (variant.length() != 0)
      {
        throw new IllegalArgumentException("Locale variant '" + variant
            + "' cannot be represented as a language tag");
      }
      if (region.length() == 0)
      {
        return toAppendTo.append(language);
      }
      if (region.length() != LANGUAGE_LENGTH)
      {
        throw new IllegalArgumentException("Locale region '" + region
            + "' is not a 2 character ISO 3166-1 code and cannot be represented");
      }
      toAppendTo.append(language);
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
   * When in <code>lenient</code> mode, all invalid characters automatically
   * removed and empty strings are allowed. By default parsing is lenient. In
   * all whatever the mode non US-ASCII characters will cause an exception.
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
   * Type converter to native Java Objects for NCName definition is a strict subset
   * of the XMLSchema datatype specification. A <code>NCName</code> is represented by a
   * Java Object of type <code>String</code>.
   * 
   * <p>
   * Name values must begin with a unicode letter, non-digit number, ideograph, underscore ("_"), and may 
   * be followed by any number of letters, numbers, ideographs, connector punctuations,  
   * hyphens ("-"), underscores ("_") and periods (".").
   * </p>
   * 
   * When in <code>lenient</code> mode, all invalid characters automatically
   * removed and empty strings are allowed. 
   *
   * The default mode is not lenient.
   * 
   * */
  public static class NCNameConverter extends DataConverter
  {
    private static final DataConverter instance = new NCNameConverter();
    
    /**
     * Verifies if the character is a subset of XML NAME type.
     * 
     * @param ch
     *          [in] The character to verify. Only supports
     *          characters in the Basic Multilanguage Plane (BMP).
     * @return true if character is valid, false otherwise.
     */
    public static boolean isNameChar(char ch)
    {
      //@formatter:off
      /* \p{L} or _ */
      if (isNameStartChar(ch)==true) return true;
      switch (Character.getType(ch))
      {
        /* \p{Nd} */
        case Character.DECIMAL_DIGIT_NUMBER:
          /* \p{Pc} */
        case Character.CONNECTOR_PUNCTUATION:
          return true;
        default:
          break;
      }
      if (ch=='-') return true;
      if (ch=='.') return true;
      if (ch==0xB7) return true;
      //@formatter:on
      return false;
    }

    /**
     * Verifies if the start character is a subset of XML NAME type.
     * 
     * @param ch
     *          [in] The character to verify. Only supports
     *          characters in the Basic Multilanguage Plane (BMP).
     * @return true if character is valid, false otherwise.
     */
    public static boolean isNameStartChar(char ch)
    {
      //@formatter:off
      /* \p{L} */
      if (Character.isLetter(ch)) return true;
      /* \p{Nl} */
      if (Character.getType(ch)==Character.LETTER_NUMBER) return true;
      if (ch == '_') return true;
      return false;
      //@formatter:on
    }

    public NCNameConverter(boolean lenient)
    {
      super(String.class, lenient);
    }

    public NCNameConverter()
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
   * Type converter to native Java Objects for QualifiedName definition is a strict subset
   * of the XML Namespaces specification. A <code>QName</code> is represented by a
   * Java Object of type <code>String</code>.
   * 
   * <p>
   * Name values must begin with a unicode letter, non-digit number, ideograph, underscore ("_"), and may 
   * be followed by any number of letters, numbers, ideographs, connector punctuations,  
   * hyphens ("-"), underscores ("_") and periods ("."). It is possible to have a (":") character
   * that separates the prefix from the local part. When present, both the prefix and the
   * local part must be valid names as defined in {@link NCNameConverter}.
   * </p>
   * 
   * When in <code>lenient</code> mode, all invalid characters automatically
   * removed and empty strings are allowed. 
   *
   * The default mode is not lenient.
   * 
   * */
  public static class QualifiedNameConverter extends DataConverter
  {
    private static final DataConverter instance = new QualifiedNameConverter();
    

    public QualifiedNameConverter(boolean lenient)
    {
      super(String.class, lenient);
    }

    public QualifiedNameConverter()
    {
      this(false);
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      int inLength = value.length();
      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }
      char c;
      char[] outBuffer = new char[inLength];
      int outOffset = 0;
      // Offset in outBuffer where the current part (prefix or local part) starts.
      int partStart = 0;
      boolean foundColon = false;
      for (int i = 0; i < inLength; i++)
      {
        c = value.charAt(i);
        boolean valid;
        if (c == ':')
        {
          // Only one colon, and only after a non-empty prefix.
          valid = (foundColon == false) && (outOffset > partStart);
        }
        else if (outOffset == partStart)
        {
          valid = NCNameConverter.isNameStartChar(c);
        }
        else
        {
          valid = NCNameConverter.isNameChar(c);
        }
        if (valid == false)
        {
          if (lenient == false)
          {
            throw new ParseException("Illegal character '" + c + "' found.", i);
          }
          continue;
        }
        outBuffer[outOffset++] = c;
        if (c == ':')
        {
          foundColon = true;
          partStart = outOffset;
        }
      }
      // The local part must not be empty.
      if (foundColon && (outOffset == partStart))
      {
        if (lenient == false)
        {
          throw new ParseException("Missing local part after ':'.", inLength);
        }
        outOffset--;
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
   * Type converter to native Java Objects for an Internet media type. A media
   * type is represented by a Java Object of type
   * {@link com.optimasc.lang.MediaType}.
   * 
   * <p>
   * The string representation is limited to 3 to 255 characters.
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

    public Object parseObject(String value) throws ParseException
    {
      if ((value.length() < MIN_LENGTH) || (value.length() > MAX_LENGTH))
      {
        throw new ParseException("Media type must be between " + MIN_LENGTH + " and "
            + MAX_LENGTH + " characters in length.", 0);
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
      if ((s.length() < MIN_LENGTH) || (s.length() > MAX_LENGTH))
      {
        throw new IllegalArgumentException("Media type '" + s
            + "' is not between " + MIN_LENGTH + " and " + MAX_LENGTH + " characters in length");
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
   * outside the repertoire are removed and empty strings are allowed.
   * Otherwise they cause an exception.
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

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      return toAppendTo.append(value.toString());
    }

  }
  
  
  
  /**
   * Type converter to native Java Objects for the ASN.1 <code>IA5String</code>
   * repertoire (U+0000 to U+007F). An <code>IA5String</code> is represented by
   * a Java Object of type <code>String</code>.
   * 
   * When in <code>lenient</code> mode, which is the default, characters
   * outside the repertoire are removed and empty strings are allowed.
   * Otherwise they cause an exception.
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
      int inLength = value.length();
      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }
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

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      return toAppendTo.append(value.toString());
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

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof CharSequence) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      return toAppendTo.append(value.toString());
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
   * with the true and false tokens are done without case sensitivity.
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
