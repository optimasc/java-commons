package com.optimasc.text;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import junit.framework.TestCase;

public class StandardFormattersTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  
  /** Tests boolean conversion valid use-cases */
  public void testBooleanConverterValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.BooleanConverter();
    assertEquals(true,converter.isLenient());

    String strValue = "true";
    Boolean value = (Boolean) converter.parseObject(strValue);
    assertEquals(true, value.booleanValue());
    assertEquals("TRUE",converter.format(value));
    
    strValue = "TRUE";
    value = (Boolean) converter.parseObject(strValue);
    assertEquals(true, value.booleanValue());
    assertEquals("TRUE",converter.format(value));
    
    strValue = "false";
    value = (Boolean) converter.parseObject(strValue);
    assertEquals(false, value.booleanValue());
    assertEquals("FALSE",converter.format(value));
    
    strValue = "FALSE";
    value = (Boolean) converter.parseObject(strValue);
    assertEquals(false, value.booleanValue());
    assertEquals("FALSE",converter.format(value));
  }
  
  
  /** Tests boolean conversion canonical valid use-cases */
  public void testBooleanConverterCanonicalValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.BooleanConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);

    String strValue;
    Boolean value;
    strValue = "TRUE";
    value = (Boolean) converter.parseObject(strValue);
    assertEquals(true, value.booleanValue());
    assertEquals("TRUE",converter.format(value));
    
    strValue = "FALSE";
    value = (Boolean) converter.parseObject(strValue);
    assertEquals(false, value.booleanValue());
    assertEquals("FALSE",converter.format(value));
  }
  
  /** Tests boolean conversion canonical invalid use-cases */
  public void testBooleanConverterCanonicalInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.BooleanConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);

    String strValue;
    Boolean value;
    boolean fail;

    // Invalid token
    try{
      fail = true;
      strValue = "true";
      value = (Boolean) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
    // Invalid token
    try{
      fail = true;
      strValue = "false";
      value = (Boolean) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    // Extra data
    try{
      fail = true;
      strValue = "FALSEx";
      value = (Boolean) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try{
      fail = true;
      strValue = "yTRUE";
      value = (Boolean) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try{
      fail = true;
      strValue = "ABCD";
      value = (Boolean) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
  }
  
  /** Tests visible string conversion valid use-cases */
  public void testVisibleStringConverterValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.VisibleStringConverter();
    assertEquals(true,converter.isLenient());

    String strValue = " 0131232Az ";
    String value = (String) converter.parseObject(strValue);
    assertEquals(strValue, converter.format(value));
    
    strValue = " 0131232A\nz ";
    value = (String) converter.parseObject(strValue);
    assertEquals(" 0131232Az ", converter.format(value));
    
    strValue = "";
    value = (String) converter.parseObject(strValue);
    assertEquals(strValue, converter.format(value));
  }
  
  /** Tests visible string conversion invalid use-cases */
  public void testVisibleStringConverterCanonicalInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.VisibleStringConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);

    String strValue;
    boolean fail;

    // The empty string is allowed by the syntax.
    assertEquals("", converter.parseObject(""));
    
    // Empty string
    try{
      fail = true;
      strValue = "hello\n";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(5,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
  }
  
  /** Tests hexadecimal canonical input conversion valid use-cases */
  public void testHexBinaryCanonicalValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.HexBinaryConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);
    
    String strValue;
    byte[] value;

    strValue = "00";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{0x00}, value));
    assertEquals(strValue, converter.format(value));

    strValue = "ABCDEF";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{(byte) 0xAB,(byte) 0xCD,(byte) 0xEF}, value));
    assertEquals(strValue, converter.format(value));

    strValue = "25FF";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{0x25,(byte) 0xFF}, value));
    assertEquals(strValue, converter.format(value));
    
    strValue = "AF25";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{(byte) 0xAF, (byte)0x25}, value));
    assertEquals(strValue.toUpperCase(), converter.format(value));
    

    strValue = "0102030405060708090A0B0C0D0E0F10";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0e,0x0F,0x10}, value));
    assertEquals(strValue, converter.format(value));
  }
  
  
  /** Tests OID canonical input conversion valid use-cases */
  public void testOIDValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.OIDConverter();
    assertEquals(true,converter.isLenient());
    final String res = "1.2.3.5";
    
    String strValue; 
    String output;
    
    try {
      strValue = "1.2.3.5";
      output = (String) converter.parseObject(strValue);
      assertEquals(res, output);
      assertEquals(strValue,converter.format(output));
      assertEquals(strValue,converter.format(new int[]{1,2,3,5}));
    } catch (ParseException e)
    {
      fail();
    }
  }
  
  /** Tests OID input conversion invalid use-cases (canonical mode) */
  public void testOIDInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.OIDConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);
    
    int[] output;
    String strValue;
    boolean fail=true;
    
    try {
      fail = true;
      strValue = "1.";
      output = (int[]) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(2,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try {
      fail = true;
      strValue = "1.3.AB";
      output = (int[]) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(4,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
  }
  
  
  public void testQualifiedNameValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.QualifiedNameConverter();
    assertEquals(false,converter.isLenient());
    
    String strValue; 
    String output;

    String res = "_312:ch";
    
    try {
      strValue = "_312:ch";
      output = (String) converter.parseObject(strValue);
      assertEquals(res,output);
      assertEquals(strValue,converter.format(output));
    } catch (ParseException e)
    {
      fail();
    }
    
    res = "_312";
    
    try {
      strValue = "_312";
      output = (String) converter.parseObject(strValue);
      assertEquals(res,output);
      assertEquals(strValue,converter.format(output));
    } catch (ParseException e)
    {
      fail();
    }
    
  }
  
  
  public void testQualifiedNameInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.QualifiedNameConverter();
    assertEquals(false,converter.isLenient());
    
    String strValue; 
    String output;
    boolean fail = true;

    String res = ":ch";
    
    try {
      fail = true;
      strValue = ":ch";
      output = (String) converter.parseObject(strValue);
      assertEquals(res,output);
      assertEquals(strValue,converter.format(output));
    } catch (ParseException e)
    {
        assertEquals(0,e.getErrorOffset());
        fail = false;
      }
    assertFalse(fail);
    
    res = "_3:1:2";
    
    try {
      fail = true;
      strValue = "_3:1:2";
      output = (String) converter.parseObject(strValue);
      assertEquals(res,output);
      assertEquals(strValue,converter.format(output));
    } catch (ParseException e)
      {
        // The local part cannot start with a digit.
        assertEquals(3,e.getErrorOffset());
        fail = false;
      }
    assertFalse(fail);
    
  }
  
  
  
  
  /** Tests Locale canonical input conversion valid use-cases */
  public void testLocaleValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.LocaleTypeConverter();
    assertEquals(true,converter.isLenient());
    
    String language;
    String region;
    String strValue;
    Locale locale;
    
    try {
      language = "FR";
      region = "";
      strValue = language;
      locale = (Locale) converter.parseObject(strValue);
      assertEquals("fr",locale.getLanguage());
      assertEquals("fr",converter.format(locale));
    } catch (ParseException e)
    {
      fail();
    }
    
    try {
      language = "FR";
      region = "ca";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
      assertEquals("fr",locale.getLanguage());
      assertEquals("CA",locale.getCountry());
      assertEquals("fr-CA",converter.format(locale));
    } catch (ParseException e)
    {
      fail();
    }
    
    try {
      language = "fr";
      region = "CA";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
      assertEquals("fr",locale.getLanguage());
      assertEquals("CA",locale.getCountry());
      assertEquals("fr-CA",converter.format(locale));
    } catch (ParseException e)
    {
      fail();
    }
    
  }
  
  /** Tests Locale canonical input conversion valid use-cases */
  public void testLocaleCanonicalValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.LocaleTypeConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);
    
    String language;
    String region;
    String strValue;
    Locale locale;
    
    try {
      language = "en";
      region = "";
      strValue = language;
      locale = (Locale) converter.parseObject(strValue);
      assertEquals("en",locale.getLanguage());
      assertEquals("en",converter.format(locale));
    } catch (ParseException e)
    {
      fail();
    }
    
    try {
      language = "en";
      region = "US";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
      assertEquals("en",locale.getLanguage());
      assertEquals("US",locale.getCountry());
      assertEquals("en-US",converter.format(locale));
    } catch (ParseException e)
    {
      fail();
    }
  }

  /** Tests Locale input conversion invalid use-cases (canonical mode) */
  public void testLocaleCanonicalInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.LocaleTypeConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);
    
    String language;
    String region;
    String strValue;
    Locale locale;
    boolean fail=true;
    
    try {
      fail = true;
      language = "en";
      region = "Us";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(4,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try {
      fail = true;
      language = "FR";
      region = "ca";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
  }
  
  
  /** Tests Locale input conversion invalid use-cases */
  public void testLocaleInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.LocaleTypeConverter();
    assertEquals(true,converter.isLenient());
//    converter.setLenient(false);
    
    String language;
    String region;
    String strValue;
    Locale locale;
    boolean fail=true;
    
    try {
      fail = true;
      language = "en";
      region = "";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(3,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try {
      fail = true;
      language = "en";
      region = "!";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      // The error is at the invalid character.
      assertEquals(3,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try {
      fail = true;
      language = "";
      region = "";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try {
      fail = true;
      language = "";
      region = "";
      strValue = "";
      locale = (Locale) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);    
    

    try {
      fail = true;
      language = "abc";
      region = "";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      // 3 letter languages are valid; the region is missing.
      assertEquals(4,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    // "goblin" is a well-formed variant: removed in lenient mode,
    // rejected otherwise.
    assertEquals(new Locale("en"), converter.parseObject("en-goblin"));
    converter.setLenient(false);
    try {
      fail = true;
      locale = (Locale) converter.parseObject("en-goblin");
    } catch (ParseException e)
    {
      assertEquals(3,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);    
    
  }
  
  
  
  /** Tests hexadecimal canonical input conversion valid use-cases */
  public void testHexBinaryValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.HexBinaryConverter();
    assertEquals(true,converter.isLenient());
    
    String strValue;
    byte[] value;

    strValue = "00";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{0x00}, value));
    assertEquals(strValue, converter.format(value));

    strValue = "AbCDEf";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{(byte) 0xAB,(byte) 0xCD,(byte) 0xEF}, value));
    assertEquals(strValue.toUpperCase(), converter.format(value));

    strValue = "25FF";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{0x25,(byte) 0xFF}, value));
    assertEquals(strValue, converter.format(value));
    
    strValue = "Af25";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{(byte) 0xAF, (byte)0x25}, value));
    assertEquals(strValue.toUpperCase(), converter.format(value));
    

    strValue = "0102030405060708090a0B0C0D0E0f10";
    value = (byte[]) converter.parseObject(strValue);
    assertTrue(Arrays.equals(new byte[]{0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,0x0A,0x0B,0x0C,0x0D,0x0e,0x0F,0x10}, value));
    assertEquals(strValue.toUpperCase(), converter.format(value));
  }
  
  
  /** Tests hexadecimal conversion invalid use-cases */
  public void testHexBinaryCanonicalConverterInvalid()
  {
    DataConverter converter = new StandardFormatters.HexBinaryConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);
    boolean fail;

    String strValue;
    byte[] value;

    try
    {
      fail = true;
      strValue = "";
      value = (byte[]) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    // Odd values
    try
    {
      fail = true;
      strValue = "1";
      value = (byte[]) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try
    {
      fail = true;
      strValue = "abc";
      value = (byte[]) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    // Lower characters
    try
    {
      fail = true;
      strValue = "ABcdef";
      value = (byte[]) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(2,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    try
    {
      fail = true;
      strValue = "012Z";
      value = (byte[]) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(3,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    
    try
    {
      fail = true;
      strValue = "-2440";
      value = (byte[]) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
  }
  
  
  /*** Test string ***/
  
  /** Tests XMLSchema string conversion valid use-cases in lenient mode */
  public void testStringConverterValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.StringConverter();
    assertEquals(true,converter.isLenient());

    String strValue = " 0131232Az ";
    String value = (String) converter.parseObject(strValue);
    assertEquals(strValue, converter.format(value));
    
    strValue = " \t\n0131232A\u0000z ";
    value = (String) converter.parseObject(strValue);
    assertEquals(" \t\n0131232Az ", converter.format(value));
    
    strValue = "\u001F";
    value = (String) converter.parseObject(strValue);
    assertEquals("", converter.format(value));
    
    strValue = "";
    value = (String) converter.parseObject(strValue);
    assertEquals(strValue, converter.format(value));
  }
  
  /** Tests XMLSchema string conversion invalid use-cases */
  public void testStringConverterCanonicalInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.StringConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);

    String strValue;
    boolean fail;

    // The empty string is allowed by the syntax.
    assertEquals("", converter.parseObject(""));
    
    // Control character
    try{
      fail = true;
      strValue = "\u001F";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    // Control character
    try{
      fail = true;
      strValue = " 0131232A\u0000z ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(9,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
  }
  
  
  /*** Test normalizedString ***/
  
  /** Tests XMLSchema normalizedString conversion valid use-cases in lenient mode */
  public void testNormalizedStringConverterValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.NormalizedStringConverter();
    assertEquals(true,converter.isLenient());

    String strValue = " 0131232Az ";
    String value = (String) converter.parseObject(strValue);
    assertEquals(strValue, converter.format(value));
    
    strValue = " \t\n0131232A\u0000z ";
    value = (String) converter.parseObject(strValue);
    assertEquals("   0131232Az ", converter.format(value));
    
    strValue = " 0131232A\rz ";
    value = (String) converter.parseObject(strValue);
    assertEquals(" 0131232A z ", converter.format(value));
    
    
    strValue = "\u001F";
    value = (String) converter.parseObject(strValue);
    assertEquals("", converter.format(value));
    
    strValue = "";
    value = (String) converter.parseObject(strValue);
    assertEquals(strValue, converter.format(value));
  }
  
  /** Tests XMLSchema normalizedString conversion invalid use-cases */
  public void testNormalizedStringConverterCanonicalInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.NormalizedStringConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);

    String strValue;
    boolean fail;

    // The empty string is allowed by the syntax.
    assertEquals("", converter.parseObject(""));
    
    // Control character
    try{
      fail = true;
      strValue = "\u001F";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    // \r \n \t characters
    try{
      fail = true;
      strValue = " 0131232A\tz ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(9,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
    // Control character
    try{
      fail = true;
      strValue = " 0131232A\u0000z ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(9,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
  }
  
  
  /*** Test token ***/
  
  /** Tests XMLSchema token conversion valid use-cases in lenient mode */
  public void testTokenConverterValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.TokenConverter();
    assertEquals(true,converter.isLenient());

    String strValue = "  0131  232Az ";
    String value = (String) converter.parseObject(strValue);
    assertEquals("0131 232Az", converter.format(value));
    
    strValue = " \t\n0131232A\u0000z ";
    value = (String) converter.parseObject(strValue);
    assertEquals("0131232Az", converter.format(value));
    
    strValue = " 0131232A\rz ";
    value = (String) converter.parseObject(strValue);
    assertEquals("0131232A z", converter.format(value));
    
    
    strValue = "\u001F";
    value = (String) converter.parseObject(strValue);
    assertEquals("", converter.format(value));
    
    strValue = "";
    value = (String) converter.parseObject(strValue);
    assertEquals(strValue, converter.format(value));
    
    strValue = " \t\n0131\t\n\r 232A\u0000z ";
    value = (String) converter.parseObject(strValue);
    assertEquals("0131 232Az", converter.format(value));
    
  }
  
  /** Tests XMLSchema normalizedString conversion invalid use-cases */
  public void testTokenConverterCanonicalInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.TokenConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);

    String strValue;
    boolean fail;

    // The empty string is allowed by the syntax.
    assertEquals("", converter.parseObject(""));
    
    // Control character
    try{
      fail = true;
      strValue = "\u001F";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    // \r \n \t characters
    try{
      fail = true;
      strValue = " 0131232A\tz ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try{
      fail = true;
      strValue = " 0131232A\nz ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try{
      fail = true;
      strValue = "A\r0131232Az";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(1,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try{
      fail = true;
      strValue = "01312  32Az";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(5,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
    
    // Control character
    try{
      fail = true;
      strValue = "0131232A\u0000z ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(10,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
  }
  
  
  
  
  /** Tests timestamp values */
/*  public void testLocalTimestampValid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.LocalTimeStampConverter();
    
    String strValue;
    
    strValue = "1990-12-31T23:59:01";
    try 
    {
       Date value = (Date) converter.parseObject(strValue);
       Calendar cal = Calendar.getInstance();
       cal.setTime(value);
       cal.clear(Calendar.ZONE_OFFSET);
       assertEquals(1990,cal.get(Calendar.YEAR));
       assertEquals(Calendar.DECEMBER,cal.get(Calendar.MONTH));
       assertEquals(31,cal.get(Calendar.DAY_OF_MONTH));
       assertEquals(23,cal.get(Calendar.HOUR_OF_DAY));
       assertEquals(59,cal.get(Calendar.MINUTE));
       assertEquals(01,cal.get(Calendar.SECOND));
       assertEquals(00,cal.get(Calendar.MILLISECOND));
    } catch (ParseException e)
    {
      e.printStackTrace();
      System.out.println(e.getErrorOffset());
      fail();
    }
    

    strValue = "1584-01-01T01:59:59";
    try 
    {
       Date value = (Date) converter.parseObject(strValue);
       Calendar cal = Calendar.getInstance();
       cal.setTime(value);
       cal.clear(Calendar.ZONE_OFFSET);
       assertEquals(1584,cal.get(Calendar.YEAR));
       assertEquals(Calendar.JANUARY,cal.get(Calendar.MONTH));
       assertEquals(01,cal.get(Calendar.DAY_OF_MONTH));
       assertEquals(01,cal.get(Calendar.HOUR_OF_DAY));
       assertEquals(59,cal.get(Calendar.MINUTE));
       assertEquals(59,cal.get(Calendar.SECOND));
       assertEquals(00,cal.get(Calendar.MILLISECOND));
       assertEquals(false,cal.isSet(Calendar.ZONE_OFFSET));
    } catch (ParseException e)
    {
      e.printStackTrace();
      System.out.println(e.getErrorOffset());
      fail();
    }
    
  }*/
  

  /* Regression tests for compliance with the Schema Value Representation
   * Specification. */

  private static void assertParseFails(DataConverter converter, String value, int offset)
  {
    try
    {
      converter.parseObject(value);
      fail("Expected ParseException for '" + value + "'");
    } catch (ParseException e)
    {
      if (offset >= 0)
      {
        assertEquals(offset, e.getErrorOffset());
      }
    }
  }

  public void testPrintableStringRepertoire() throws ParseException
  {
    DataConverter converter = new StandardFormatters.PrintableStringConverter();
    converter.setLenient(false);
    assertEquals("Az09 '()+,-./:=?", converter.parseObject("Az09 '()+,-./:=?"));
    assertParseFails(converter, "a*b", 1);
    assertParseFails(converter, "a@b", 1);
    assertParseFails(converter, "caf\u00e9", 3);
    converter.setLenient(true);
    assertEquals("ab", converter.parseObject("a*@b"));
  }

  public void testVisibleStringRejectsDelete()
  {
    DataConverter converter = new StandardFormatters.VisibleStringConverter();
    converter.setLenient(false);
    assertParseFails(converter, "a\u007fb", 1);
  }

  public void testIA5String() throws ParseException
  {
    DataConverter converter = new StandardFormatters.IA5StringConverter();
    converter.setLenient(false);
    assertEquals("a\u0000\u007f", converter.parseObject("a\u0000\u007f"));
    assertParseFails(converter, "caf\u00e9", 3);
  }

  public void testBase64Invalid()
  {
    DataConverter converter = new StandardFormatters.Base64CanonicalConverter();
    assertParseFails(converter, "abc", 0);
    assertParseFails(converter, "AAA\u00e9", 3);
    assertParseFails(converter, "AAAA====", -1);
    assertParseFails(converter, "AB==", 1);
    assertParseFails(converter, "AAB=", 2);
  }

  public void testBase64Valid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.Base64CanonicalConverter();
    assertTrue(Arrays.equals(new byte[] { 0 }, (byte[]) converter.parseObject("AA==")));
    assertTrue(Arrays.equals(new byte[] { 0, 0 }, (byte[]) converter.parseObject("AAA=")));
    assertEquals("AQID", converter.format(new byte[] { 1, 2, 3 }));
  }

  public void testOIDCompliance() throws ParseException
  {
    DataConverter converter = new StandardFormatters.OIDConverter();
    String large = "2.25.329800735698586629295641978511506172918";
    assertEquals(large, converter.parseObject(large));
    assertEquals(large, converter.format(large));
    assertEquals("1.3", converter.parseObject("1.03"));
    assertEquals("1.3", converter.format("1.03"));
    assertParseFails(converter, "1", 1);
    assertParseFails(converter, "3.1", 0);
    assertParseFails(converter, "1.40", 2);
    assertEquals("2.999", converter.parseObject("2.999"));
    converter.setLenient(false);
    assertParseFails(converter, "1.03", 2);
    try
    {
      converter.format(new Integer(5));
      fail();
    } catch (IllegalArgumentException e)
    {
    }
  }

  public void testQualifiedNameParts() throws ParseException
  {
    DataConverter converter = new StandardFormatters.QualifiedNameConverter();
    assertParseFails(converter, "a:", 2);
    assertParseFails(converter, "a:1b", 2);
    assertParseFails(converter, "a:-b", 2);
    assertEquals("xs:string", converter.parseObject("xs:string"));

    converter = new StandardFormatters.QualifiedNameConverter(true);
    assertEquals("xs:string", converter.parseObject("xs:string"));
    assertEquals("a:b", converter.parseObject("a:1b"));
    assertEquals("ch", converter.parseObject(":ch"));
    assertEquals("a", converter.parseObject("a:"));
  }

  public void testNCNameLenientStart() throws ParseException
  {
    DataConverter converter = new StandardFormatters.NCNameConverter(true);
    assertEquals("abc", converter.parseObject("-1abc"));
    assertEquals("", converter.parseObject(""));
  }

  public void testBooleanExactToken()
  {
    DataConverter converter = new StandardFormatters.BooleanConverter();
    assertParseFails(converter, "TRUEX", 0);
    converter.setLenient(false);
    assertParseFails(converter, "TRUEX", 0);
  }

  public void testXSDBoolean() throws ParseException
  {
    DataConverter converter = new StandardFormatters.XSDBooleanConverter();
    assertEquals(Boolean.TRUE, converter.parseObject("true"));
    assertEquals(Boolean.TRUE, converter.parseObject("1"));
    assertEquals(Boolean.FALSE, converter.parseObject("false"));
    assertEquals(Boolean.FALSE, converter.parseObject("0"));
    assertEquals("true", converter.format(Boolean.TRUE));
    assertEquals("false", converter.format(Boolean.FALSE));
    assertParseFails(converter, "TRUE", 0);
    converter.setLenient(false);
    assertParseFails(converter, "1", 0);
  }

  public void testURICompliance() throws Exception
  {
    DataConverter converter = new StandardFormatters.URIConverter();
    assertEquals(new java.net.URI("http://example.com/a?b#c"),
        converter.parseObject("http://example.com/a?b#c"));
    assertParseFails(converter, "", 0);
    assertParseFails(converter, "http://example.com/\u00fc", 19);
    try
    {
      converter.format(new java.net.URI("http://example.com/\u00fc"));
      fail();
    } catch (IllegalArgumentException e)
    {
    }
  }

  public void testMediaTypeLength() throws ParseException
  {
    DataConverter converter = new StandardFormatters.MediaTypeConverter();
    assertEquals("text/plain", converter.format(converter.parseObject("text/plain")));
    StringBuffer buffer = new StringBuffer("text/");
    while (buffer.length() < 256)
    {
      buffer.append('a');
    }
    assertParseFails(converter, buffer.toString(), 0);
  }

  public void testSharedInstances()
  {
    assertSame(StandardFormatters.OIDConverter.getInstance(),
        StandardFormatters.OIDConverter.getInstance());
    assertTrue(StandardFormatters.XSDBooleanConverter.getInstance()
        instanceof StandardFormatters.XSDBooleanConverter);
    assertTrue(StandardFormatters.MediaTypeConverter.getInstance()
        instanceof StandardFormatters.MediaTypeConverter);
  }


  public void testTextConverter() throws ParseException
  {
    DataConverter converter = new StandardFormatters.TextConverter();
    converter.setLenient(false);
    // Supplementary characters (U+1F600) are allowed.
    assertEquals("a\ud83d\ude00b", converter.parseObject("a\ud83d\ude00b"));
    // Unpaired surrogates, plane 15 private use and non-characters are not.
    assertParseFails(converter, "a\ud83db", 1);
    assertParseFails(converter, "a\udb80\udc00", 1);
    assertParseFails(converter, "a\ud83f\udffe", 1);
    assertParseFails(converter, "a\u0001", 1);
    assertSame(StandardFormatters.TextConverter.getInstance(),
        StandardFormatters.TextConverter.getInstance());
  }

  public void testNameCharacters() throws ParseException
  {
    DataConverter converter = new StandardFormatters.XSDNCNameConverter();
    // Combining marks (Devanagari vowel sign) and supplementary characters.
    assertEquals("\u0928\u092e\u0938\u094d\u0924\u0947",
        converter.parseObject("\u0928\u092e\u0938\u094d\u0924\u0947"));
    assertEquals("a\ud840\udc00", converter.parseObject("a\ud840\udc00"));
    assertEquals("e\u0301", converter.parseObject("e\u0301"));
    assertParseFails(converter, "\u0301e", 0);
    assertParseFails(converter, "a:b", 1);
    assertParseFails(converter, "a\u00d7", 1);
  }

  public void testNameConverter() throws ParseException
  {
    DataConverter converter = new StandardFormatters.XSDNameConverter();
    assertEquals(false, converter.isLenient());
    assertEquals(":a:b", converter.parseObject(":a:b"));
    assertEquals("_x-1.2", converter.parseObject("_x-1.2"));
    assertParseFails(converter, "1abc", 0);
    assertParseFails(converter, "-abc", 0);
    assertParseFails(converter, "a b", 1);
    assertParseFails(converter, "", 0);
    assertEquals("a:b", converter.format("a:b"));

    converter = new StandardFormatters.XSDNameConverter(true);
    assertEquals("abc", converter.parseObject(" 1abc "));
    assertSame(StandardFormatters.XSDNameConverter.getInstance(),
        StandardFormatters.XSDNameConverter.getInstance());
  }

  public void testNMTOKENConverter() throws ParseException
  {
    DataConverter converter = new StandardFormatters.XSDNMTOKENConverter();
    assertEquals(false, converter.isLenient());
    assertEquals("1abc", converter.parseObject("1abc"));
    assertEquals("-.:9", converter.parseObject("-.:9"));
    assertParseFails(converter, "a b", 1);
    assertParseFails(converter, "a/b", 1);
    assertParseFails(converter, "", 0);

    converter = new StandardFormatters.XSDNMTOKENConverter(true);
    assertEquals("ab", converter.parseObject(" a b "));
  }


  public void testMediaTypeNames() throws ParseException
  {
    DataConverter converter = new StandardFormatters.MediaTypeConverter();
    assertEquals("text/xhtml", converter.format(converter.parseObject("text/xhtml")));
    assertEquals("application/alto-costmap+json",
        converter.format(converter.parseObject("application/alto-costmap+json")));
    // Parameters are accepted.
    assertEquals("text/plain;charset=utf-8",
        converter.format(converter.parseObject("text/plain;charset=utf-8")));
    assertParseFails(converter, "text/pl@in", 7);
    assertParseFails(converter, "+text/plain", 0);
    assertParseFails(converter, "text/", 5);
    assertParseFails(converter, "textplain", 9);
  }

  public void testLanguageSubset() throws ParseException
  {
    DataConverter lenient = new StandardFormatters.LocaleTypeConverter();
    DataConverter strict = new StandardFormatters.LocaleTypeConverter();
    strict.setLenient(false);
    String[][] cases = {
        // input, lenient result (null: rejected), strict result (null: rejected)
        { "en", "en", "en" },
        { "fr-CA", "fr-CA", "fr-CA" },
        { "FR-ca", "fr-CA", null },
        // 3-letter code with an ISO 639-1 code: mapped when lenient
        { "fra", "fr", null },
        { "fre-CA", "fr-CA", null },
        { "deu", "de", null },
        { "heb-IL", "he-IL", null },
        // no ISO 639-1 code: rejected in both modes
        { "haw", null, null },
        { "fil-PH", null, null },
        // script, UN M.49 region, variants, extensions, private use: removed when lenient
        { "sr-Latn-RS", "sr-RS", null },
        { "zh-Hant", "zh", null },
        { "es-419", "es", null },
        { "de-CH-1996", "de-CH", null },
        { "en-US-u-ca-gregory", "en-US", null },
        { "en-US-x-twain", "en-US", null },
        { "sl-rozaj-biske", "sl", null },
        // extended language: rejected, as removing it changes the language
        { "zh-yue", null, null },
        { "zh-cmn-Hans-CN", null, null },
        // private use and grandfathered tags, other languages, malformed tags
        { "x-private", null, null },
        { "i-klingon", null, null },
        { "en-GB-oed", null, null },
        { "abcd", null, null },
        { "abcdefghi", null, null },
        { "en--US", null, null },
        { "en-US-", null, null },
        { "en-a", null, null },
        { "en-a-b", null, null },
        { "en-x", null, null },
        { "en_US", null, null },
        { "en-US-Latn", null, null } };
    for (int i = 0; i < cases.length; i++)
    {
      checkLocale(lenient, cases[i][0], cases[i][1]);
      checkLocale(strict, cases[i][0], cases[i][2]);
    }
  }

  private static void checkLocale(DataConverter converter, String value, String expected)
  {
    try
    {
      String result = converter.format(converter.parseObject(value));
      assertEquals(value + " (lenient=" + converter.isLenient() + ")", expected, result);
    } catch (ParseException e)
    {
      assertNull(value + " (lenient=" + converter.isLenient() + ") rejected: " + e.getMessage(),
          expected);
    }
  }

  public void testLocaleFormat()
  {
    DataConverter converter = StandardFormatters.LocaleTypeConverter.getInstance();
    assertEquals("fr", converter.format(new Locale("fra")));
    assertEquals("he", converter.format(new Locale("he")));
    String[][] invalid = { { "haw", "" }, { "en", "419" }, { "en", "US" } };
    for (int i = 0; i < invalid.length; i++)
    {
      Locale locale = (i == 2) ? new Locale("en", "US", "POSIX")
          : new Locale(invalid[i][0], invalid[i][1]);
      try
      {
        converter.format(locale);
        fail("Formatted " + locale);
      } catch (IllegalArgumentException e)
      {
      }
    }
  }


  public void testLDAPNCName() throws ParseException
  {
    DataConverter converter = new StandardFormatters.NCNameConverter();
    // Examples of the NCName syntax.
    assertEquals("MyID", converter.parseObject("MyID"));
    assertEquals("attribute.0.subdivision", converter.parseObject("attribute.0.subdivision"));
    // [\p{L}\p{Nl}_][\p{L}\p{Nl}\p{Nd}._-]*
    assertEquals("_\u00e9t\u00e9-1", converter.parseObject("_\u00e9t\u00e9-1"));
    assertParseFails(converter, "e\u0301", 1);
    assertParseFails(converter, "a\u00b7b", 1);
    assertParseFails(converter, "a\u203fb", 1);
    assertParseFails(converter, "a\ud840\udc00", 1);
    assertParseFails(converter, "1a", 0);

    converter = new StandardFormatters.QualifiedNameConverter();
    assertEquals("attribute.0:subdivision", converter.parseObject("attribute.0:subdivision"));
    assertParseFails(converter, "a:e\u0301", 3);
  }

  public void testTextDraft() throws ParseException
  {
    // Format characters and space separators are kept.
    DataConverter converter = new StandardFormatters.TextConverter();
    assertEquals("a\u200db\u00a0c\u200bd", converter.parseObject("a\u200db\u00a0c\u200bd"));
    converter.setLenient(false);
    assertEquals("a\u200db\u00a0c", converter.parseObject("a\u200db\u00a0c"));
    assertParseFails(converter, "a\ufdd0", 1);
    assertParseFails(converter, "a\ue000", 1);

    // Normalized String: tab, LF, CR, Zs, Zl and Zp map to SPACE; Cf is kept.
    converter = new StandardFormatters.NormalizedStringConverter();
    assertEquals("a b c d\u200be", converter.parseObject("a\tb\u00a0c\u2028d\u200be"));
    // U+0085 and U+000B are prohibited control characters, not whitespace.
    assertEquals("ab", converter.parseObject("a\u0085\u000bb"));
    converter.setLenient(false);
    assertEquals("a\u200bb", converter.parseObject("a\u200bb"));
    assertParseFails(converter, "a\u00a0b", 1);

    // Token
    converter = new StandardFormatters.TokenConverter();
    assertEquals("a b", converter.parseObject(" a\u00a0 b "));
    assertEquals("_Token_", converter.parseObject("_Token_"));
    converter.setLenient(false);
    assertEquals("", converter.parseObject(""));
    assertEquals("This is a token with spaces.",
        converter.parseObject("This is a token with spaces."));
  }

  public void testVisibleStringExamples() throws ParseException
  {
    DataConverter converter = new StandardFormatters.VisibleStringConverter();
    converter.setLenient(false);
    assertEquals("hello world", converter.parseObject("hello world"));
    assertEquals("(x+y)=z", converter.parseObject("(x+y)=z"));
    assertEquals("", converter.parseObject(""));
  }


}
