package com.optimasc.text;

import java.math.BigInteger;
import java.text.ParseException;

import junit.framework.TestCase;

import com.optimasc.text.NumericFormatters.IntegerFormat;

/** Tests of the integer syntaxes of IntegerFormat. */
public class IntegerFormatTest extends TestCase
{
  /** Inputs tested against each syntax. */
  private static final String[] INPUTS = { "5", "+5", "05", "-0", "-5", "-05", "0", "--5",
      "5a", "", "-", "+", " 5" };

  /**
   * Canonical result of each input for each syntax, or null when the input is
   * rejected. Columns follow INPUTS.
   */
  private static final Object[][] EXPECTED = {
      { IntegerFormat.LDAP, "5", null, null, null, "-5", null, "0", null, null, null, null,
          null, null },
      { IntegerFormat.ISO11404, "5", null, "5", "0", "-5", "-5", "0", null, null, null, null,
          null, null },
      { IntegerFormat.SQL99, "5", "5", "5", "0", "-5", "-5", "0", null, null, null, null,
          null, null },
      { "[0-9]+", "5", null, "5", null, null, null, "0", null, null, null, null, null, null },
      { "(([0-9])|([1-9][0-9]+))", "5", null, null, null, null, null, "0", null, null, null,
          null, null, null } };

  public void testSyntaxes()
  {
    for (int row = 0; row < EXPECTED.length; row++)
    {
      String pattern = (String) EXPECTED[row][0];
      DataConverter converter = new IntegerFormat(pattern);
      for (int i = 0; i < INPUTS.length; i++)
      {
        String expected = (String) EXPECTED[row][i + 1];
        String message = pattern + " [" + INPUTS[i] + "]";
        try
        {
          assertEquals(message, expected, converter.format(converter.parseObject(INPUTS[i])));
        } catch (ParseException e)
        {
          assertNull(message + " rejected: " + e.getMessage(), expected);
        }
      }
    }
  }

  public void testConstants()
  {
    assertEquals(IntegerFormat.LDAP, IntegerFormat.ASN1);
    assertEquals(IntegerFormat.LDAP, IntegerFormat.XSD_CANONICAL);
    assertEquals(IntegerFormat.SQL99, IntegerFormat.XSD);
    assertEquals(IntegerFormat.SQL99, IntegerFormat.VCARD);
    assertEquals(IntegerFormat.SQL99, IntegerFormat.XMP);
    assertEquals("[-]?[0-9]+", IntegerFormat.ISO11404);
  }

  public void testRangesAndValueClasses() throws ParseException
  {
    DataConverter int8 = new IntegerFormat(IntegerFormat.LDAP, Integer.class, -128, 127);
    assertEquals(new Integer(-128), int8.parseObject("-128"));
    try
    {
      int8.parseObject("128");
      fail();
    } catch (ParseException e)
    {
    }
    DataConverter uint32 = new IntegerFormat(IntegerFormat.XMP, Long.class, 0, 4294967295L);
    assertEquals(new Long(4294967295L), uint32.parseObject("+4294967295"));
    DataConverter big = new IntegerFormat(IntegerFormat.SQL99);
    assertEquals(new BigInteger("123456789012345678901234567890"),
        big.parseObject("+0123456789012345678901234567890"));
    assertEquals("123456789012345678901234567890",
        big.format(new BigInteger("123456789012345678901234567890")));
    // The lenient flag does not change the syntax.
    DataConverter ldap = new IntegerFormat(IntegerFormat.LDAP);
    ldap.setLenient(true);
    try
    {
      ldap.parseObject("+5");
      fail();
    } catch (ParseException e)
    {
    }
    assertEquals(IntegerFormat.LDAP, ((IntegerFormat) ldap).getPattern());
  }

  public void testInvalidPatterns()
  {
    String[] patterns = { "", "[0-9]*", "-?[0-9]+", "[+]?[0-9]+", "[-]?[0-9]", "[+-]?",
        "\\d+", "[-]?(0|[1-9][0-9]*)" };
    for (int i = 0; i < patterns.length; i++)
    {
      try
      {
        new IntegerFormat(patterns[i]);
        fail("Pattern accepted: " + patterns[i]);
      } catch (IllegalArgumentException e)
      {
      }
    }
  }
}
