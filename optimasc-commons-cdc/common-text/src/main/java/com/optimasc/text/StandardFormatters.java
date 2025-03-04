package com.optimasc.text;

import java.math.BigInteger;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
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
   * String type converter. Utility class for other data types that are kept as
   * strings. It supports all non-control characters. In <code>lenient</code>
   * mode, illegal characters are simply ignored and removed and an empty string
   * is allowed, while when <code>lenient</code> is <code>false</code> an
   * exception will be thrown when an illegal character is found or if the
   * string is empty.
   * 
   * When <code>lenient</code> is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) string built-in datatype canonical
   * representation</li>
   * <li>
   * </ul>
   * 
   */
  public static class StringConverter extends DataConverter
  {
    public static int CHAR_LINEFEED = 0x0A;
    public static int CHAR_TAB = 0x09;
    public static int CHAR_RETURN = 0x0D;
    private static DataConverter instance;
    

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

      int outIndex = 0;
      char[] out = new char[inLength];
      for (int i = 0; i < inLength; i++)
      {
        int v = value.charAt(i);
        // Valid characters
        if ((v == CHAR_TAB) || (v == CHAR_LINEFEED) || (v == CHAR_RETURN)
            || ((v >= 0x20) && (v <= 0xD7FF)) || ((v >= 0xE000) && (v <= 0xFFFD))
            || ((v >= 0x10000) && (v <= 0x10FFFF)))
        {
          out[outIndex++] = (char) v;
          continue;
        }
        if (lenient == false)
          throw new ParseException("String is composed of non-characters.", i);
      }
      return new String(out, 0, outIndex);
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
      if (instance == null)
      {
        synchronized (StringConverter.class)
        {
          if (instance == null)
            instance = new StringConverter();
        }
      }
      return instance;
    }
    
  }

  /**
   * Normalized String type converter. A normalized string is a
   * {@link StandardFormatters.StringConverter} that does not contain the
   * carriage return (#xD), line feed (#xA) nor tab (#x9) characters. If lenient
   * mode is <code>true</code>, any of the above characters will be replaced by
   * a space character. All other control characters will be removed from the
   * output. If these characters are found when lenient mode is disabled, an
   * exception will be thrown.
   * 
   * When <code>lenient</code> is <code>false</code> the converter is compliant
   * with the following syntaxes:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) normalizedString built-in datatype
   * canonical representation</li>
   * <li>
   * </ul>
   */
  public static class NormalizedStringConverter extends StringConverter
  {
    private static DataConverter instance;
    
    public NormalizedStringConverter()
    {
      super();
    }
    
    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (NormalizedStringConverter.class)
        {
          if (instance == null)
            instance = new NormalizedStringConverter();
        }
      }
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

      int outIndex = 0;
      char[] out = new char[inLength];
      for (int i = 0; i < inLength; i++)
      {
        int v = value.charAt(i);
        if ((v == CHAR_TAB) || (v == CHAR_LINEFEED) || (v == CHAR_RETURN))
        {
          if (lenient == false)
            throw new ParseException("String contains a CR/Newline/Tab.", i);
          // Replace by a space
          out[outIndex++] = (char) ' ';
          continue;
        }
        // Valid characters
        if (((v >= 0x20) && (v <= 0xD7FF)) || ((v >= 0xE000) && (v <= 0xFFFD))
            || ((v >= 0x10000) && (v <= 0x10FFFF)))
        {
          out[outIndex++] = (char) v;
          continue;
        }
        if (lenient == false)
          throw new ParseException("String is composed of non-characters.", i);
      }
      return new String(out, 0, outIndex);
    }
  }

  /**
   * Token type converter. A token is a string that does not contain the
   * carriage return (#xD), line feed (#xA) nor tab (#x9) characters nor any
   * control characters, that have no leading or trailing spaces (#x20) and that
   * have no internal sequences of two or more spaces.
   * 
   * <p>
   * When lenient mode is <code>true</code> the following will occur:
   * <ul>
   * <li>carriage return (#xD), line feed (#xA) or tab (#x9) are replaced by a
   * single space character.</li>
   * <li>leading and trailing spaces are removed.</li>.
   * <li>consecutive space characters are removed.</li>
   * <li>control characters are removed.</li>
   * </ul>
   * <p>
   * When lenient mode is disabled, an error will be thrown if any of the above
   * characters are encountered.
   * </p>
   * 
   */
  public static class TokenConverter extends NormalizedStringConverter
  {
    private static DataConverter instance;
    
    
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
      int outIndex = 0;
      int inLength = value.length();

      if ((inLength == 0) && (lenient == false))
      {
        throw new ParseException("Empty string are not allowed.", 0);
      }

      if (lenient)
      {
        value = Parsers.trimChar(value, ' ');
        inLength = value.length();
      }
      else
      {
      }

      char[] out = new char[inLength];

      // Replace some control characters by spaces.
      for (int i = 0; i < inLength; i++)
      {
        int v = value.charAt(i);
        // These control characters need to be replaced by a space in 
        // lenient mode, otherwise in strict mode checking, an error
        // is thrown.
        if ((v == CHAR_TAB) || (v == CHAR_LINEFEED) || (v == CHAR_RETURN))
        {
          if (lenient == false)
            throw new ParseException("String contains a CR/Newline/Tab.", i);
          // Replace by a space
          v = ' ';
        }
        out[i] = (char) v;
      }

      String input = new String(out);
      inLength = input.length();

      // Now redo the exercise and merge all multiple consecutive spaces
      // together.

      {
        int i = 0;
        while (i < inLength)
        {
          int v = input.charAt(i);

          if (v == ' ')
          {
            char cd = input.charAt(i + 1);
            if (cd == ' ')
            {
              if (lenient == false)
                throw new ParseException("String contains contiguous spaces.", i);
              // Ignore all spaces
              while ((input.charAt(i) == ' ') && (i < inLength))
              {
                i++;
              }
              i--;
            }
          }

          // Valid characters
          if (((v >= 0x20) && (v <= 0xD7FF)) || ((v >= 0xE000) && (v <= 0xFFFD))
              || ((v >= 0x10000) && (v <= 0x10FFFF)))
          {
            out[outIndex++] = (char) v;
            i++;
            continue;
          }

          if (lenient == false)
            throw new ParseException("String is composed of non-characters", i);
          i++;
        }
        return new String(out, 0, outIndex);
      }
    }
    
    
    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (TokenConverter.class)
        {
          if (instance == null)
            instance = new TokenConverter();
        }
      }
      return instance;
    }
    
  }

  /**
   * hexBinary type converter that converts to a <code>byte[]</code>. The
   * standard syntax accepted is where each binary octet is encoded as a
   * character tuple, consisting of two hexadecimal digits ([0-9a-fA-F])
   * representing the octet.
   * 
   * <p>
   * When <code>strictCheck</code> is enabled, then it represents the canonical
   * representation where the lower case hexadecimal digits ([a-f]) are not
   * allowed.
   *
   * When <code>strictCheck</code> is <code>false</code>, which is the default,
   * the syntax is compliant with the following formats:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) hexBinary built-in datatype
   * lexical representation</li>
   * <li>LDAP Ping directory hexadecimal string (OID: 1.3.6.1.4.1.30221.2.3.3)</li>
   * </ul>
   * 
   * When <code>strictCheck</code> is <code>true</code>, the syntax is compliant
   * with the following formats:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) hexBinary built-in datatype
   * canonical representation</li>
   * <li>IETF RFC 4648 base16 or hex encoding.</li>
   * </ul>
   **/
  public static class HexBinaryConverter extends DataConverter
  {
    private static final String HEXES = "0123456789ABCDEF";
    private static DataConverter instance;
    
    

    public HexBinaryConverter()
    {
      super(byte[].class, true);
    }
    
    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (HexBinaryConverter.class)
        {
          if (instance == null)
            instance = new HexBinaryConverter();
        }
      }
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
   * @author Christian d'Heureuse
   **/
  public static class Base64CanonicalConverter extends DataConverter
  {
    private static DataConverter instance;
    
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
        throw new IllegalArgumentException(
            "Length of Base64 encoded input string is not a multiple of 4.");
      while (iLen > 0 && value.charAt(iOff + iLen - 1) == '=')
        iLen--;
      int oLen = (iLen * 3) / 4;
      byte[] out = new byte[oLen];
      int ip = iOff;
      int iEnd = iOff + iLen;
      int op = 0;
      while (ip < iEnd)
      {
        int i0 = value.charAt(ip);
        int b0 = map2[i0];
        if (b0 < 0 || i0 > 127)
          throw new ParseException("Illegal character in Base64 encoded data.", ip);
        ip++;
        int i1 = value.charAt(ip);
        int b1 = map2[i1];
        if (b1 < 0 || i1 > 127)
          throw new ParseException("Illegal character in Base64 encoded data.", ip);
        ip++;
        int i2 = ip < iEnd ? value.charAt(ip++) : 'A';
        int b2 = map2[i2];
        if (b2 < 0 || i2 > 127)
          throw new ParseException("Illegal character in Base64 encoded data.", ip - 1);
        int i3 = ip < iEnd ? value.charAt(ip++) : 'A';
        int b3 = map2[i3];
        if (b3 < 0 || i3 > 127)
          throw new ParseException("Illegal character in Base64 encoded data.", ip - 1);
        int o0 = (b0 << 2) | (b1 >>> 4);
        int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
        int o2 = ((b2 & 3) << 6) | b3;
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
      if (instance == null)
      {
        synchronized (Base64CanonicalConverter.class)
        {
          if (instance == null)
            instance = new Base64CanonicalConverter();
        }
      }
      return instance;
    }
    

  }

  /**
   * Type converter to native Java Objects for a Locale. A <code>Locale</code> is 
   * represented by a Java Object of type <code>Locale</code>.
   *  
   * 
   * It supports a subset of
   * the format defined in IETF RFC 5646. The differences are specified below:
   * 
   * <ul>
   * <li>Language codes are based on 2 lower-case character language code as
   * defined by ISO 639-1.</li>
   * <li>The country code is based on the 2 upper-case character code
   * identifying countries under ISO 3166-1.</li>
   * </ul>
   * 
   * <p>
   * Hence the syntax is as follows:
   * 
   * <pre>
   *      langtag  = language
   *                  ["-" region]
   * 
   *     language      = 2ALPHA   ; shortest ISO 639 code
   *     region        = 2ALPHA   ; ISO 3166-1 code
   * </pre>
   * 
   * <p>
   * In <code>lenient</code> mode, the case is automatically converted to the
   * correct expected case, and does not raise an error if not correctly set
   * initially.
   * </p>
   * 
   * 
   **/
  public static class LocaleTypeConverter extends DataConverter
  {
    public static final char TAG_SEPARATOR = '-';

    private static DataConverter instance;

    public LocaleTypeConverter()
    {
      super(Locale.class, true);
    }

    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (LocaleTypeConverter.class)
        {
          if (instance == null)
            instance = new LocaleTypeConverter();
        }
      }
      return instance;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      String language;
      String region = null;
      char c1;
      char c2;
      char output[] = new char[2];
      int inLength = value.length();
      if (inLength < 2)
        throw new ParseException("Length must be at least 2 characters.", 0);

      // Lenient we convert the characters ourselves to lower case.
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

      output[0] = c1;
      output[1] = c2;
      language = new String(output);
      if (inLength == 2)
      {
        output = null;
        return new Locale(language);
      }
      inLength = inLength - 2;
      if (value.charAt(2) != TAG_SEPARATOR)
      {
        throw new ParseException("Expected tag separator '" + TAG_SEPARATOR + "'", 2);
      }
      inLength--;
      if (lenient)
      {
        if (inLength >= 1)
        {
          c1 = Character.toUpperCase(value.charAt(3));
          inLength--;
        }
        else
        {
          throw new ParseException(
              "The language tag with region is not 5 characters in length", 3);
        }

        if (inLength >= 1)
        {
          c2 = Character.toUpperCase(value.charAt(4));
          inLength--;
        }
        else
        {
          throw new ParseException(
              "The language tag with region is not 5 characters in length", 4);
        }

      }
      else
      {
        if (inLength >= 1)
        {

          c1 = value.charAt(3);
          inLength--;
        }
        else
        {
          throw new ParseException(
              "The language tag with region is not 5 characters in length", 3);
        }
        if (inLength >= 1)
        {
          c2 = value.charAt(4);
          inLength--;
        }
        else
        {
          throw new ParseException(
              "The language tag with region is not 5 characters in length", 4);
        }
      }

      if ((c1 < 'A') || (c1 > 'Z'))
      {
        throw new ParseException("Region code character is not upper-case ascii letter",
            3);
      }
      if ((c2 < 'A') || (c2 > 'Z'))
      {
        throw new ParseException("Region code character is not upper-case ascii letter",
            4);
      }
      if (inLength != 0)
      {
        throw new ParseException("Extra characters after region information", 5);
      }
      output[0] = c1;
      output[1] = c2;
      region = new String(output);
      output = null;
      return new Locale(language, region);
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof Locale) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      Locale locale = (Locale) value;
      String region = locale.getCountry();
      if ((region == null) || (region.length() == 0))
      {
        return toAppendTo.append(locale.getLanguage());
      }
      return toAppendTo.append(locale.getLanguage() + "-" + region);
    }

    public Object parseObject(String value) throws ParseException
    {
      return parseObject((CharSequence) value);
    }
  }

  /** Type converter to native Java Objects for an URI. */
  public static class URIConverter extends DataConverter
  {
    private static DataConverter instance;

    public URIConverter()
    {
      super(URI.class, true);
    }

    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (URIConverter.class)
        {
          if (instance == null)
            instance = new URIConverter();
        }
      }
      return instance;
    }

    public Object parseObject(String value) throws ParseException
    {
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
      return toAppendTo.append(value.toString());
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      return parseObject(value.toString());
    }
  }
  
  
  /** Type converter to native Java Objects for an ASN.1 OBJECT IDENTIFIER. An <code>OID</code> 
   * is represented by a Java Object of type <code>int[]</code>. 
   * 
   * This formatter only supports the NumberForm of the OBJECT IDENTIFIER syntax. */
  public static class OIDConverter extends DataConverter
  {
    private static DataConverter instance;

    public OIDConverter()
    {
      super(int[].class, true);
    }

    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (OIDConverter.class)
        {
          if (instance == null)
            instance = new OIDConverter();
        }
      }
      return instance;
    }

    public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((value instanceof int[]) == false)
      {
        throw new IllegalArgumentException("Object value is not instance of '"
            + clz.getName() + "'");
      }
      int[] v = (int[]) value;
      for (int i=0; i < v.length-1; i++)
      {
        toAppendTo.append(Integer.toString(v[i]));
        toAppendTo.append(".");
      }
      toAppendTo.append(Integer.toString(v[v.length-1]));
      return toAppendTo;
    }

    public Object parseObject(CharSequence value) throws ParseException
    {
      ParsePosition pos = new ParsePosition(0);
      List list = new ArrayList();
      int n = -1;
      while (pos.getIndex()<value.length())
      {
        n = Parsers.parsePositiveNumber(value, pos, 1, 64);
        list.add(new Integer(n));
        if (pos.getIndex()==value.length())
          break;
        if (value.charAt(pos.getIndex())!='.')
        {
          throw new ParseException("Expecting '.' but found another character",pos.getIndex());
        }
        pos.setIndex(pos.getIndex()+1);
        n = -1;
      }
      if (n == -1)
      {
        pos.setErrorIndex(pos.getIndex());
        throw new ParseException("Expecting positive number but found another character",pos.getIndex());
      }
      int result[] = new int[list.size()];
      for (int i=0; i < result.length; i++)
      {
        result[i] = ((Integer)list.get(i)).intValue();
      }
      return result;
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
    private static DataConverter instance;
    
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
        outOffset = 0;
        c = value.charAt(0);
        if (isNameStartChar(c) == false)
        {
        }
        else
        {
          outBuffer[outOffset++] = c;
        }
        for (int i = 1; i < value.length(); i++)
        {
          c = value.charAt(i);
          if (isNameChar(c) == false)
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
      if (instance == null)
      {
        synchronized (ASCIINameConverter.class)
        {
          if (instance == null)
            instance = new ASCIINameConverter();
        }
      }
      return instance;
    }
    
  }
  
  
  /**
   * Type converter to native Java Objects for NCName definition is a strict subet
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
    private static DataConverter instance;
    
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
        outOffset = 0;
        c = value.charAt(0);
        if (isNameStartChar(c) == false)
        {
        }
        else
        {
          outBuffer[outOffset++] = c;
        }
        for (int i = 1; i < value.length(); i++)
        {
          c = value.charAt(i);
          if (isNameChar(c) == false)
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
      if (instance == null)
      {
        synchronized (NCNameConverter.class)
        {
          if (instance == null)
            instance = new NCNameConverter();
        }
      }
      return instance;
    }
    
  }
  
  
  /**
   * Type converter to native Java Objects for QualifiedName definition is a strict subet
   * of the XMLSchema datatype specification. A <code>QName</code> is represented by a
   * Java Object of type <code>String</code>.
   * 
   * <p>
   * Name values must begin with a unicode letter, non-digit number, ideograph, underscore ("_"), and may 
   * be followed by any number of letters, numbers, ideographs, connector punctuations,  
   * hyphens ("-"), underscores ("_") and periods ("."). It is possible to have a (":") character
   * that separates the prefix from the rest.
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
    private static DataConverter instance;
    

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
      int outOffset;
      boolean foundColon = false;
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
        if (NCNameConverter.isNameStartChar(c) == false)
        {
          throw new ParseException("Illegal character '" + c + "' found.", 0);
        }
        outBuffer[0] = c;
        for (int i = 1; i < value.length(); i++)
        {
          c = value.charAt(i);
          if (c == ':')
          {
            if (foundColon == true)
            {
              throw new ParseException("Illegal character '" + c + "' found.", i);
            }
            foundColon = true;
          }
          else
          if (NCNameConverter.isNameChar(c) == false)
          {
            throw new ParseException("Illegal character '" + c + "' found.", i);
          }
          outBuffer[i] = c;
        }
      }
      else
      {
        outOffset = 0;
        c = value.charAt(0);
        if (NCNameConverter.isNameStartChar(c) == false)
        {
        }
        else
        {
          outBuffer[outOffset++] = c;
        }
        for (int i = 1; i < value.length(); i++)
        {
          c = value.charAt(i);
          if (c == ':')
          {
            if (foundColon == true)
            {
              continue;
            }
            foundColon = true;
          }
          if (NCNameConverter.isNameChar(c) == false)
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
      if (instance == null)
      {
        synchronized (QualifiedNameConverter.class)
        {
          if (instance == null)
            instance = new QualifiedNameConverter();
        }
      }
      return instance;
    }
    
  }
  
  
  
  
  /** Represents a Media Type converter */
  public static class MediaTypeConverter extends DataConverter 
  {
     public MediaTypeConverter()
     {
       super(MediaType.class,false);
     }

    public Object parseObject(String value) throws ParseException
    {
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
      return toAppendTo.append(mt.toString());
    }
  }
  

  /**
   * Type converter to native Java Objects for a subset of visible US-ASCII
   * characters. This is equivalent to the ASN.1 <code>VisibleString</code>. A
   * <code>VisibleString</code> is represented by a Java Object of type
   * <code>String</code>.
   * 
   * When in <code>lenient</code> mode, all control characters are automatically
   * removed and empty strings are allowed. By default parsing is lenient. In
   * all whatever the mode non US-ASCII characters will cause an exception.
   * 
   * */
  public static class VisibleStringConverter extends DataConverter
  {
    private static DataConverter instance;
    
    public static final int MIN_CHAR = 32;
    public static final int MAX_CHAR = 127;

    public VisibleStringConverter()
    {
      super(String.class, true);
    }
    
    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (VisibleStringConverter.class)
        {
          if (instance == null)
            instance = new VisibleStringConverter();
        }
      }
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
   * Type converter to native Java Objects for a subset of visible US-ASCII
   * characters. This is equivalent to the ASN.1 <code>PrintableString</code>. A
   * <code>PrintableString</code> is represented by a Java Object of type
   * <code>String</code>.
   * 
   * When in <code>lenient</code> mode, all control characters are automatically
   * removed and empty strings are allowed. By default parsing is lenient. In
   * all whatever the mode non printable characters will cause an exception.
   * 
   * */
  public static class PrintableStringConverter extends DataConverter
  {
    private static DataConverter instance;
    
    public static final int MIN_CHAR = 32;
    public static final int MAX_CHAR = 127;
    
    


    public PrintableStringConverter()
    {
      super(String.class, true);
    }
    
    public static DataConverter getInstance()
    {
      if (instance == null)
      {
        synchronized (PrintableStringConverter.class)
        {
          if (instance == null)
            instance = new PrintableStringConverter();
        }
      }
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
              if (((c >= 40) && (c <= 58)) || ((c >= 65) && ( c <= 90)) || ((c >= 97) || (c<=122)))
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
              if (((c >= 40) && (c <= 58)) || ((c >= 65) && ( c <= 90)) || ((c >= 97) || (c<=122)))
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
    private static DataConverter instance;
    
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
      if (instance == null)
      {
        synchronized (Latin1StringConverter.class)
        {
          if (instance == null)
            instance = new Latin1StringConverter();
        }
      }
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
   * <li>ASN.1 ITU X.680</li>
   * <li>LDAP IETF RFC 4517 (OID: 1.3.6.1.4.1.1466.115.121.1.7)</li>
   * <li>SQL99 Boolean syntax</li>
   * </ul>
   * 
   * When <code>lenient</code> is <code>true</code> the converter is
   * additionally compliant with the following syntaxes when using default
   * tokens:
   * <ul>
   * <li>W3C XML Schema Second Edition (2004) integer built-in datatype
   * canonical representation</li>
   * <li>ISO/IEC 11404 General purpose datatypes.</li>
   * <li>vCard IETF RFC 6350 BOOLEAN type</li>
   * </ul>
   */
  public static class BooleanConverter extends DataConverter
  {
    private static DataConverter instance;
    
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
      if (lenient)
      {
        int index = Parsers.indexOfIgnoreCase(value, TRUE_TOKEN, 0);
        if (index == 0)
        {
          return Boolean.TRUE;
        }
        else
        {
          index = Parsers.indexOfIgnoreCase(value, FALSE_TOKEN, 0);
          if (index == 0)
          {
            return Boolean.FALSE;
          }
        }
        throw new ParseException("Invalid boolean token values.", 0);
      }
      else
      {
        int index = Parsers.indexOf(value, TRUE_TOKEN, 0);
        if (index == 0)
        {
          return Boolean.TRUE;
        }
        else
        {
          index = Parsers.indexOf(value, FALSE_TOKEN, 0);
          if (index == 0)
          {
            return Boolean.FALSE;
          }
        }
        throw new ParseException("Invalid boolean token values.", 0);
      }
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
      if (instance == null)
      {
        synchronized (BooleanConverter.class)
        {
          if (instance == null)
            instance = new BooleanConverter();
        }
      }
      return instance;
    }
    
  }

}
