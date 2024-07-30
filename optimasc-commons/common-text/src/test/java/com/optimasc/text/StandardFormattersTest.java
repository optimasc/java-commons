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

    // Empty string
    try{
      fail = true;
      strValue = "";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
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
      assertEquals(4,e.getErrorOffset());
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
      assertEquals(2,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try {
      fail = true;
      language = "en";
      region = "goblin";
      strValue = language+"-"+region;
      locale = (Locale) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(5,e.getErrorOffset());
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

    // Empty string
    try{
      fail = true;
      strValue = "";
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
      strValue = "\u001F";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    // Control chacter
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

    // Empty string
    try{
      fail = true;
      strValue = "";
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
    
    try{
      fail = true;
      strValue = " 0131232A\nz ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(9,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try{
      fail = true;
      strValue = " \r0131232Az ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(1,e.getErrorOffset());
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
    assertEquals(" 0131232Az", converter.format(value));
    
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
    assertEquals(" 0131 232Az", converter.format(value));
    
  }
  
  /** Tests XMLSchema normalizedString conversion invalid use-cases */
  public void testTokenConverterCanonicalInvalid() throws ParseException
  {
    DataConverter converter = new StandardFormatters.TokenConverter();
    assertEquals(true,converter.isLenient());
    converter.setLenient(false);

    String strValue;
    boolean fail;

    // Empty string
    try{
      fail = true;
      strValue = "";
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
    
    try{
      fail = true;
      strValue = " 0131232A\nz ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(9,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try{
      fail = true;
      strValue = " \r0131232Az ";
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
      strValue = " 0131232A\u0000z ";
      String value = (String) converter.parseObject(strValue);
    } catch (ParseException e)
    {
      assertEquals(9,e.getErrorOffset());
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
  
}
