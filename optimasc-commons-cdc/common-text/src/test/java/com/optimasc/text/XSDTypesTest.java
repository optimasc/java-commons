package com.optimasc.text;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;

import junit.framework.TestCase;

import com.optimasc.text.NumericFormatters.*;
import com.optimasc.text.StandardDateFormatters.*;
import com.optimasc.text.StandardFormatters.*;

/**
 * Verifies that each XML Schema type of the Schema Value Representation
 * Specification has a converter, which accepts the lexical representation
 * of the type and rejects invalid values.
 */
public class XSDTypesTest extends TestCase
{
  private static void check(String type, DataConverter c, String[] ok, String[] ko)
  {
    for (int i = 0; i < ok.length; i++)
    {
      try
      {
        c.format(c.parseObject(ok[i]));
      } catch (Exception e)
      {
        fail(type + ": valid value rejected '" + ok[i] + "': " + e.getMessage());
      }
    }
    for (int i = 0; i < ko.length; i++)
    {
      try
      {
        c.parseObject(ko[i]);
        fail(type + ": invalid value accepted '" + ko[i] + "'");
      } catch (ParseException e)
      {
      }
    }
  }

  private static void canonical(DataConverter c, String in, String expected) throws ParseException
  {
    assertEquals(in, expected, c.format(c.parseObject(in)));
  }

  public void testAllTypes()
  {
    check("anyURI", URIConverter.getInstance(), new String[] { "http://example.com/a#b", "a/b" },
        new String[] { "http://example.com/\u00e9" });
    check("base64Binary", XSDBase64BinaryConverter.getInstance(), new String[] { "", "AQID", "AQ ID",
        "AA==" }, new String[] { "AB==", "A" });
    check("boolean", XSDBooleanConverter.getInstance(), new String[] { "true", "false", "1", "0" },
        new String[] { "TRUE", "yes" });
    check("byte", XSDByteConverter.getInstance(), new String[] { "+127", "-128", "007", "-0" },
        new String[] { "128", "1.0" });
    check("date", XSDDateConverter.getInstance(), new String[] { "0001-01-01", "2000-02-29" },
        new String[] { "1900-02-29", "2000-01-01Z" });
    check("dateTime", XSDDateTimeConverter.getInstance(), new String[] { "2000-01-01T10:20:30",
        "2000-01-01T10:20:30.123", "2000-01-01T24:00:00" }, new String[] {
        "2000-01-01T10:20:30Z", "2000-01-01T23:59:60" });
    check("dateTimeStamp", XSDDateTimeStampConverter.getInstance(), new String[] {
        "2000-01-01T10:20:30Z", "2000-01-01T10:20:30.5-14:00" }, new String[] {
        "2000-01-01T10:20:30", "2000-01-01T10:20:30+14:30" });
    check("decimal", XSDDecimalConverter.getInstance(), new String[] { "1", "+1.50", ".5", "5.",
        "-0.0" }, new String[] { ".", "1E5", "1.2.3", "" });
    check("double", XSDDoubleConverter.getInstance(), new String[] { "1e5", "INF", "-INF", "NaN",
        ".5" }, new String[] { "PLUS-INFINITY", "nan" });
    check("duration", XSDDurationConverter.getInstance(), new String[] { "P1Y2M3DT4H5M6.789S",
        "PT1.12345S" }, new String[] { "-P1D", "P1DT", "P" });
    check("float", XSDFloatConverter.getInstance(), new String[] { "0.1", "INF" },
        new String[] { "1e" });
    check("gYear", XSDGYearConverter.getInstance(), new String[] { "2020" },
        new String[] { "2020Z", "20" });
    check("hexBinary", XSDHexBinaryConverter.getInstance(), new String[] { "", "0aFF" },
        new String[] { "0", "GG" });
    check("int", XSDIntConverter.getInstance(), new String[] { "+2147483647", "-2147483648" },
        new String[] { "2147483648" });
    check("integer", XSDIntegerConverter.getInstance(), new String[] {
        "+123456789012345678901234567890", "-0", "007" }, new String[] { "1.0", "--1" });
    check("language", LocaleTypeConverter.getInstance(), new String[] { "en", "fr-CA" },
        new String[] { "e", "en_CA" });
    check("long", XSDLongConverter.getInstance(), new String[] { "-9223372036854775808" },
        new String[] { "9223372036854775808" });
    check("Name", XSDNameConverter.getInstance(), new String[] { ":a:b", "_x-1.2" },
        new String[] { "1a", "a b" });
    check("NCName", XSDNCNameConverter.getInstance(), new String[] { "e\u0301", "a\u00b7b" },
        new String[] { "a:b", "1a" });
    check("NMTOKEN", XSDNMTOKENConverter.getInstance(), new String[] { "1a", "-.:9" },
        new String[] { "a b" });
    check("nonNegativeInteger", XSDNonNegativeIntegerConverter.getInstance(), new String[] { "0",
        "-0", "+18446744073709551616" }, new String[] { "-1" });
    check("normalizedString", XSDNormalizedStringConverter.getInstance(), new String[] { "",
        "a\tb", " a  b " }, new String[] { "a\ufffeb" });
    check("short", XSDShortConverter.getInstance(), new String[] { "-32768", "+32767" },
        new String[] { "32768" });
    check("string", XSDStringConverter.getInstance(), new String[] { "", "a\tb\n",
        "\ue000\ufdd0", "\ud83d\ude00" }, new String[] { "a\ufffe", "a\ud83d" });
    check("time", XSDTimeConverter.getInstance(), new String[] { "10:20:30", "10:20:30.5",
        "24:00:00" }, new String[] { "10:20:30Z", "23:59:60" });
    check("token", XSDTokenConverter.getInstance(), new String[] { "", " a \t b " },
        new String[] { "a\uffffb" });
    check("unsignedByte", XSDUnsignedByteConverter.getInstance(), new String[] { "+255", "-0" },
        new String[] { "256", "-1" });
    check("unsignedInt", XSDUnsignedIntConverter.getInstance(), new String[] { "4294967295" },
        new String[] { "4294967296" });
    check("unsignedLong", XSDUnsignedLongConverter.getInstance(), new String[] {
        "18446744073709551615" }, new String[] { "18446744073709551616" });
    check("unsignedShort", XSDUnsignedShortConverter.getInstance(), new String[] { "65535" },
        new String[] { "65536" });
  }

  public void testCanonicalForms() throws ParseException
  {
    canonical(XSDDecimalConverter.getInstance(), "+001.500", "1.5");
    canonical(XSDDecimalConverter.getInstance(), "5.", "5");
    canonical(XSDDecimalConverter.getInstance(), "-.25", "-0.25");
    canonical(XSDDecimalConverter.getInstance(), "-0.000", "0");
    canonical(XSDDecimalConverter.getInstance(), "0.0010", "0.001");
    canonical(XSDDecimalConverter.getInstance(), "1200", "1200");
    assertEquals(XSDDecimalConverter.getInstance().parseObject("1.50"),
        XSDDecimalConverter.getInstance().parseObject("1.5"));
    assertEquals("1.25", XSDDecimalConverter.getInstance().format(new BigDecimal("1.250")));
    canonical(XSDBooleanConverter.getInstance(), "1", "true");
    canonical(XSDByteConverter.getInstance(), "+007", "7");
    canonical(XSDIntegerConverter.getInstance(), "-0", "0");
    canonical(XSDHexBinaryConverter.getInstance(), "0aff", "0AFF");
    canonical(XSDHexBinaryConverter.getInstance(), "", "");
    canonical(XSDBase64BinaryConverter.getInstance(), "AQ ID", "AQID");
    canonical(XSDNormalizedStringConverter.getInstance(), "a\tb\nc", "a b c");
    canonical(XSDTokenConverter.getInstance(), "  a \t\n b  ", "a b");
    // No normalization of XML Schema strings (Text uses NFC).
    canonical(XSDStringConverter.getInstance(), "e\u0301", "e\u0301");
    assertTrue(Arrays.equals(new byte[0],
        (byte[]) XSDBase64BinaryConverter.getInstance().parseObject("")));
  }

  public void testStrictModes()
  {
    DataConverter c = new XSDTokenConverter();
    c.setLenient(false);
    check("token (strict)", c, new String[] { "a b", "" }, new String[] { " a", "a  b", "a\tb" });
    c = new XSDNormalizedStringConverter();
    c.setLenient(false);
    check("normalizedString (strict)", c, new String[] { " a  b " }, new String[] { "a\nb" });
    c = new XSDBase64BinaryConverter();
    c.setLenient(false);
    check("base64Binary (strict)", c, new String[] { "AQID" }, new String[] { "AQ ID" });
  }
}
