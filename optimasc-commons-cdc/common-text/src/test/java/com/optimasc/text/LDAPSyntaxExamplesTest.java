package com.optimasc.text;

import junit.framework.TestCase;

import com.optimasc.text.NumericFormatters.*;
import com.optimasc.text.StandardDateFormatters.*;
import com.optimasc.text.StandardFormatters.*;

/**
 * Verifies that each syntax of draft-codere-ldapsyntax-12 has a converter,
 * which accepts the examples of the draft and rejects invalid values.
 */
public class LDAPSyntaxExamplesTest extends TestCase
{
  private static DataConverter strict(DataConverter c)
  {
    c.setLenient(false);
    return c;
  }

  private static void check(String syntax, DataConverter c, String[] ok, String[] ko)
  {
    for (int i = 0; i < ok.length; i++)
    {
      try
      {
        c.format(c.parseObject(ok[i]));
      } catch (Exception e)
      {
        fail(syntax + ": valid value rejected '" + ok[i] + "': " + e.getMessage());
      }
    }
    for (int i = 0; i < ko.length; i++)
    {
      try
      {
        c.parseObject(ko[i]);
        fail(syntax + ": invalid value accepted '" + ko[i] + "'");
      } catch (java.text.ParseException e)
      {
      }
    }
  }

  public void testAllSyntaxes()
  {
    check("Date", new LDAPDateConverter(), new String[]{"9999-02-25","1583-01-31"}, new String[]{"1581-12-31","2021-02-29"});
    check("Date-Time", new LDAPDateTimeConverter(), new String[]{"1583-01-01T00:59:59","1975-01-19T23:45:34","1975-01-19T24:00:00","2016-12-31T23:59:60"}, new String[]{"1975-01-19T24:00:01","1975-01-19T23:45:34Z"});
    check("Duration", new DurationConverter(), new String[]{"P29M0D","P29MT0S","PT3445.5S"}, new String[]{"P","PT","P1DT","PT1.1234S","-P1D"});
    check("Real", new RealConverter(), new String[]{"3.14159","MINUS-INFINITY","0","-5.3E4","5E3"}, new String[]{"01","+1","1e5"});
    check("Time Of Day", new TimeOfDayConverter(), new String[]{"00:59:59","01:45:54","24:00:00","23:59:60"}, new String[]{"24:00:01","10:00:00Z"});
    check("Visible String", strict(new VisibleStringConverter()), new String[]{"hello world","(x+y)=z",""}, new String[]{"caf\u00e9","a\u007f"});
    check("Float32", new Float32Converter(), new String[]{"3.14159","MINUS-INFINITY","-5.3E4"}, new String[]{"1E39"});
    check("Float64", new Float64Converter(), new String[]{"3.1415926535897932","NOT-A-NUMBER","-5.3E4"}, new String[]{"1E400"});
    check("UInt8", unsignedByteConverter.getLDAPInstance(), new String[]{"0","34"}, new String[]{"256","-0","+1"});
    check("UInt16", unsignedShortConverter.getLDAPInstance(), new String[]{"0","64991"}, new String[]{"65536"});
    check("UInt32", unsignedIntConverter.getLDAPInstance(), new String[]{"0","40000000"}, new String[]{"4294967296"});
    check("UInt64", unsignedLongConverter.getLDAPInstance(), new String[]{"0","844674407370955"}, new String[]{"18446744073709551616"});
    check("Int8", ByteConverter.getLDAPInstance(), new String[]{"0","-123"}, new String[]{"128","007"});
    check("Int16", ShortConverter.getLDAPInstance(), new String[]{"15667","-32000"}, new String[]{"32768"});
    check("Int32", IntConverter.getLDAPInstance(), new String[]{"15667","-3200000"}, new String[]{"2147483648"});
    check("Int64", LongConverter.getLDAPInstance(), new String[]{"-2337203685477580","3372036854775807"}, new String[]{"9223372036854775808"});
    check("Percentage", PercentageConverter.getInstance(), new String[]{"0","99"}, new String[]{"101"});
    check("Language", strict(new LocaleTypeConverter()), new String[]{"en","fr-CA"}, new String[]{"e","en-"});
    check("Media Type", new MediaTypeConverter(), new String[]{"text/xhtml","application/alto-costmap+json"}, new String[]{"text/","+a/b"});
    check("OpenDate", strict(new OpenDateConverter()), new String[]{"2034","1975-01","1975-01-19","1975-01-19T19:20+01:00","1975-01-19T19:20:30+01:00","1975-01-19T19:20:30.451+01:00","1975-01-19T18:20Z","1975-01-19T18:20:30Z","1975-01-19T18:20:30.451Z","1975-01-19T24:00Z","2016-12-31T23:59:60Z"}, new String[]{"1975-01-19T19:20","1975-01-19T19:20:30.45Z","1000","1975-01-19T10:00+15:01"});
    check("URI", new URIConverter(), new String[]{"http://www.example.com/my/picture.jpg","ldap://ldap.example.com/cn=babs%20jensen"}, new String[]{"","http://x/\u00e9"});
    check("NCName", new NCNameConverter(), new String[]{"MyID","attribute.0.subdivision"}, new String[]{"1a","a:b"});
    check("Qualified Name", new QualifiedNameConverter(), new String[]{"MyID","attribute.0:subdivision"}, new String[]{"a:","a:b:c"});
    check("Text", strict(new TextConverter()), new String[]{"Hello world","Ceci est une phrase qui est encore plus longue que la pr\u00e9c\u00e9dente"}, new String[]{"a\u0001","a\ue000"});
    check("Normalized String", strict(new NormalizedStringConverter()), new String[]{"   Paragraph start with some leading spaces.","This is some other text with spaces."}, new String[]{"a\tb"});
    check("Token", strict(new TokenConverter()), new String[]{"This is a token with spaces.","_Token_",""}, new String[]{" a","a  b"});
    check("Time Of Day with Timezone", new TimeOfDayWithTimezoneConverter(), new String[]{"00:59:59Z","01:45:54-01:00","10:00:00+15:00","24:00:00Z"}, new String[]{"10:00:00","10:00:00+15:01"});
  }
}
