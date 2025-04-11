package com.optimasc.datatypes.primitives;

import java.io.InputStream;
import java.util.Hashtable;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.defined.IntType;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.io.XMLSchemaDeserializer;
import com.optimasc.datatypes.manager.*;

import junit.framework.TestCase;

public class DeserializerTest extends TestCase
{
  public static final String LANGUAGE_TAG_PATTERN = "(((en-GB-oed|i-ami|i-bnn|i-default|i-enochian|i-hak|i-klingon|i-lux|i-mingo|i-navajo|i-pwn|i-tao|i-tay|i-tsu|sgn-BE-FR|sgn-BE-NL|sgn-CH-DE)|(art-lojban|cel-gaulish|no-bok|no-nyn|zh-guoyu|zh-hakka|zh-min|zh-min-nan|zh-xiang))|((([A-Za-z]{2,3}(-([A-Za-z]{3}(-[A-Za-z]{3}){0,2}))?)|[A-Za-z]{4}|[A-Za-z]{5,8})(-([A-Za-z]{4}))?(-([A-Za-z]{2}|[0-9]{3}))?(-([A-Za-z0-9]{5,8}|[0-9][A-Za-z0-9]{3}))*(-([0-9A-WY-Za-wy-z](-[A-Za-z0-9]{2,8})+))*(-(x(-[A-Za-z0-9]{1,8})+))?)|(x(-[A-Za-z0-9]{1,8})+))";


  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testNormal01()
  {
    TypeReference typeRef;
    InputStream stream = getClass().getClassLoader().getResourceAsStream("res/schemas.xsd");
    XMLSchemaDeserializer input = new XMLSchemaDeserializer();
    TypeSymbolTable dataTypes = input.load(stream);
    /* Check if all the values are valid now. */

    typeRef =  dataTypes.get("myInteger");
    assertNotNull(typeRef);
    IntegralType integerType = (IntegralType)typeRef.getType();
    
    
    IntType intType = (IntType) dataTypes.getType("MyLimitedInteger");
    assertNotNull(intType);
    assertEquals(0, intType.getMinInclusive().intValue());
    assertEquals(12, intType.getMaxInclusive().intValue());

    typeRef = dataTypes.get("Text");
    assertNotNull(typeRef);
    StringType stringType = (StringType)typeRef.getType();
    assertEquals(0, stringType.getMinLength());
    assertEquals(1024, stringType.getMaxLength());
/*    assertEquals(null, stringType.getPatterns()); */
    assertEquals(null, stringType.getChoices());

    typeRef = dataTypes.get("shortNormalizedString");
    assertNotNull(typeRef);
    stringType = (StringType)typeRef.getType();
    assertEquals(0, stringType.getMinLength());
    assertEquals(250, stringType.getMaxLength());
/*    assertEquals(null, stringType.getPatterns()); */
    assertEquals(null, stringType.getChoices());

    typeRef = dataTypes.get("DCMITYPE");
    assertNotNull(typeRef);
    stringType = (StringType)typeRef.getType();
/*    assertEquals(null, stringType.getPatterns()); */
    Object[] choices = stringType.getChoices();
    assertEquals(12, choices.length);


    typeRef =  dataTypes.get("ratingLevel");
    assertNotNull(typeRef);
    integerType = (IntegralType)typeRef.getType();
    assertEquals(0, integerType.getMinInclusive().intValue());
    assertEquals(100, integerType.getMaxInclusive().intValue());

    typeRef = dataTypes.get("LicenseType");
    assertNotNull(typeRef);
    stringType = (StringType)typeRef.getType();
/*    assertEquals(null, stringType.getPatterns()); */
    choices = stringType.getChoices();
    assertEquals(7, choices.length);


    typeRef = dataTypes.get("MyBoolean");
    assertNotNull(typeRef);
    BooleanType booleanType = (BooleanType)typeRef.getType();

    typeRef = dataTypes.get("LanguageTag");
    assertNotNull(typeRef);
    stringType = (StringType)typeRef.getType();
    assertEquals(0, integerType.getMinInclusive().intValue());
/*    assertEquals(LANGUAGE_TAG_PATTERN, stringType.getPatterns()); */
    choices = stringType.getChoices();
    assertEquals(8, stringType.getChoices().length);
  }


/*  public void testNormal02()
  {
    Object o;
    InputStream stream = getClass().getResourceAsStream("/res/metadata.xsd");
    XMLSchemaDeserializer input = new XMLSchemaDeserializer();
    TypeSymbolTable dataTypes = input.load(stream);
    /* Check if all the values are valid now.

<xsd:simpleType name="nonNegativeReal">
<xsd:simpleType name="CompressionType">
<xsd:simpleType name="CodecType">
<xsd:simpleType name="ProperNameContact">
<xsd:simpleType name="OpenDate">
<xsd:simpleType name="shortNormalizedString">
<xsd:simpleType name="algoType">
<xsd:simpleType name="FFID">
<xsd:simpleType name="CPUType">
<xsd:simpleType name="OSType">
<xsd:simpleType name="FileSuffixType">
<xsd:simpleType name="DCMITYPE">
<xsd:simpleType name="URN">
<xsd:simpleType name="ratingLevel">
<xsd:simpleType name="hashType">
<xsd:simpleType name="CRC32Type">
<xsd:simpleType name="MD5Type">
<xsd:simpleType name="LicenseType">
<xsd:simpleType name="GenreType">
<xsd:simpleType name="versionString">


    typeRef =  dataTypes.get("Text");
    assertNotNull(typeRef);
    StringType stringType = (StringType)o;

  }*/


}
