package com.optimasc.datatypes.primitives;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.FieldPosition;
import java.text.Format;
import java.text.NumberFormat;
import java.text.ParsePosition;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DatatypeTest;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;

public class URITypeTest  extends DatatypeTest
{
  protected URIType defaultInstance;

  
  protected void setUp() throws Exception
  {
    super.setUp();
    defaultInstance = new URIType();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  
  public static class URNISBNValidator extends Format
  {
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
      if ((obj instanceof URI)==false)
      {
        throw new IllegalArgumentException("Expecting URI class, got '"+obj.getClass().getName()+"'."); 
      }
      URI value = (URI) obj;
      if (value.isOpaque()==false)
      {
        throw new IllegalArgumentException("URI is not an URN:ISBN type");
      }
      if (value.getScheme().toUpperCase().equals("URN")==false)
      {
        throw new IllegalArgumentException("URI is not an URN:ISBN type");
      }
      String isbnValue = value.getSchemeSpecificPart();
      StringBuffer buffer = new StringBuffer();
      for (int i=0; i < isbnValue.length(); i++)
      {
        char c = isbnValue.charAt(i);
        c = Character.toUpperCase(c);
        if ((c == '-') || (c == ' '))
        {
          continue;
        }
        buffer.append(c);
      }
      isbnValue = buffer.toString();
      buffer =null;
      isbnValue = isbnValue.toUpperCase();
      if (isbnValue.startsWith("ISBN:")==false)
      {
        throw new IllegalArgumentException("URI is not an URN:ISBN type");
      }
      isbnValue = isbnValue.substring(5);
      
      if ((isbnValue.length()==10) || (isbnValue.length()==13))
      {
        for (int i=0; i < isbnValue.length(); i++)
        {
          char c = isbnValue.charAt(i);
          if (((c >= '0') && (c <= '9')) || (c=='X'))
          {
            continue;
          }
          throw new IllegalArgumentException("ISBN value contains a non-valid character '"+c+"'.");
        }
      } else
      {
        throw new IllegalArgumentException("ISBN value contains invalid characters");
      }
      return toAppendTo.append(value.toString());
    }

    public Object parseObject(String source, ParsePosition pos)
    {
      try
      {
        URI uri = new URI(source);
        format(uri);
        return uri;
      } catch (URISyntaxException e)
      {
        pos.setErrorIndex(0);
        return null;
      } catch (IllegalArgumentException e)
      {
        pos.setErrorIndex(0);
        return null;
      }
    }
    
  }

  /*----------------------- Generic API ------------------------*/
  public void testGetClassType()
  {
    assertEquals(URI.class, defaultInstance.getClassType());
  }
  
  public void testStringAttributes()
  {
    testBasicDataType(defaultInstance);
  }
  
  public void testUserData()
  {
    testUserData(defaultInstance);
  }

  public void testProperties()
  {
    URIType instance = defaultInstance;
    assertEquals(false,instance.isOrdered());
    assertEquals(false,instance.isNumeric());
    assertEquals(null,instance.getFormatValidator());
  }
  
  /*------------------ format functionality ----------------*/
  
  public void testFormatValidatorNone() throws URISyntaxException
  {
    // Valid value
    URI uri = new URI("http://www.optimasc.com");
    URIType instance = defaultInstance;
    assertEquals(null,instance.getFormatValidator());
    assertEquals(true,instance.validateFormat(uri));
  }
  
  
  public void testFormatValidatorISBN() throws URISyntaxException
  {
    Format isbnValidator = new URNISBNValidator();  
    URIType instance = new URIType(isbnValidator);
    assertEquals(isbnValidator,instance.getFormatValidator());
    
    URI uri = new URI("http://www.optimasc.com");
    assertEquals(false,instance.validateFormat(uri));
    uri = new URI("URN:OID:1.2.3.4");
    assertEquals(false,instance.validateFormat(uri));
    uri = new URI("URN:ISBN:951-0-18435-7");
    assertEquals(true,instance.validateFormat(uri));
    uri = new URI("URN:ISBN:978-951-0-18435-6");
    assertEquals(true,instance.validateFormat(uri));
    uri = new URI("URN:ISBN:9789510184356");
    assertEquals(true,instance.validateFormat(uri));
  }
  
  /*------------------ toValue functionality ----------------*/
  public void testtoValueValidatorNone() throws URISyntaxException
  {
    URIType instance = defaultInstance;
    TypeCheckResult conversionResult = new TypeCheckResult();
    
    // Valid values
    URI uri = new URI("http://www.optimasc.com");
    assertEquals(uri,instance.toValue(uri, conversionResult));
    assertEquals(null,conversionResult.error);
    
    assertEquals(uri,instance.toValue("http://www.optimasc.com", conversionResult));
    assertEquals(null,conversionResult.error);
    
    // Invalid URI's
    assertEquals(null,instance.toValue("http\\www.optimasc.com", conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());

    // Invalid Object
    assertEquals(null,instance.toValue(new Integer(3), conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
    
  }
  
  public void testtoValueValidatorISBN() throws URISyntaxException
  {
    Format isbnValidator = new URNISBNValidator();  
    URIType instance = new URIType(isbnValidator);
    TypeCheckResult conversionResult = new TypeCheckResult();
    
    // Valid values
    URI uri = new URI("URN:ISBN:951-0-18435-7");
    assertEquals(uri,instance.toValue(uri, conversionResult));
    assertEquals(null,conversionResult.error);
    
    assertEquals(uri,instance.toValue("urn:ISBN:951-0-18435-7", conversionResult));
    assertEquals(null,conversionResult.error);
    
    
    // Invalid URI's
    uri = new URI("http://www.optimasc.com");
    assertEquals(null,instance.toValue(uri, conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
    
    assertEquals(null,instance.toValue("news:comp.lang.java", conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());

    // Invalid Object
    assertEquals(null,instance.toValue(new Integer(3), conversionResult));
    assertEquals(DatatypeException.ERROR_DATA_TYPE_MISMATCH,((DatatypeException)conversionResult.error).getCode());
    
  }
  
  

  /*------------------ equals/restriction functionality ----------------*/
  public void testEqualsRestriction()
  {
    Format isbnValidator = new URNISBNValidator();  
    URIType instance01 = new URIType(isbnValidator);
    URIType instance02 = new URIType(NumberFormat.getInstance());
    URIType instance03 = new URIType(isbnValidator);
    
    assertTrue(defaultInstance.equals(defaultInstance));
    assertFalse(defaultInstance.isRestrictionOf(defaultInstance));

    assertFalse(defaultInstance.equals(instance01));
    assertFalse(defaultInstance.isRestrictionOf(instance01));
    assertTrue(instance01.isRestrictionOf(defaultInstance));
    
    assertFalse(instance01.equals(instance02));
    assertTrue(instance01.equals(instance03));
    
    
  }
}
