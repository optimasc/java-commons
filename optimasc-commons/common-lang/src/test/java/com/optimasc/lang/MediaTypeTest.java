package com.optimasc.lang;

import java.text.ParseException;

import junit.framework.TestCase;

public class MediaTypeTest extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  public void testMediaTypeSimpleValid()
  {
    String type;
    String subType;
    MediaType mediaType;
    
    try
    {
     type = "application";
     subType = "zip";
     mediaType = new MediaType(type,subType);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
     assertEquals(type + "/" + subType,mediaType.toString());
    } catch (ParseException e)
    {
      fail();
    }
    
    try
    {
     type = "video";
     subType = "MP4V-ES";
     mediaType = new MediaType(type,subType);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
     assertEquals(type + "/" + subType,mediaType.toString());
    } catch (ParseException e)
    {
      fail();
    }
    
    try
    {
     type = "application";
     subType = "prs.ace+json";
     mediaType = new MediaType(type,subType);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("json",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
     assertEquals(type + "/" + subType,mediaType.toString());
    } catch (ParseException e)
    {
      fail();
    }
    
    
  }
  
  
  public void testMediaTypeRawDataValid()
  {
    String type;
    String subType;
    String rawData;
    MediaType mediaType;
    
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
     assertEquals(type + "/" + subType,mediaType.toString());
    } catch (ParseException e)
    {
      fail();
    }
    
    try
    {
     type = "video";
     subType = "MP4V-ES";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
     assertEquals(type + "/" + subType,mediaType.toString());
    } catch (ParseException e)
    {
      fail();
    }
    
    try
    {
     type = "application";
     subType = "prs.ace+json";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("json",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
    } catch (ParseException e)
    {
      fail();
    }

    // With parameters
    try
    {
     type = "application";
     subType = "vnd.picsel";
     rawData = type + "/" + subType + ";version=01;charset=US-ASCII";
     mediaType = new MediaType(rawData);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("",mediaType.getSuffix());
     assertEquals(2,mediaType.getParameters().size());
     assertEquals("01",mediaType.getParameter("VERSION"));
     assertEquals("01",mediaType.getParameter("version"));
     assertEquals("US-ASCII",mediaType.getParameter("charset"));
     assertEquals(type + "/" + subType,mediaType.getBaseType());
     mediaType.removeParameter("CHARSET");
     assertEquals(null,mediaType.getParameter("charset"));
     mediaType.setParameter("format", "invalid");
     assertEquals("invalid",mediaType.getParameter("format"));
     assertEquals(type + "/" + subType+";format=invalid;version=01",mediaType.toString());
    } catch (ParseException e)
    {
      fail();
    }
    
  }
  
  
  public void testMediaTypeSimpleInvalid()
  {
    String type;
    String subType;
    MediaType mediaType;
    boolean fail;
    
    // Illegal characters
    try
    {
     fail = true;
     type = "app?lication";
     subType = "zip";
     mediaType = new MediaType(type,subType);
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(3,e.getErrorOffset());
    }
    assertFalse(fail);
    
    try
    {
     fail = true;
     type = "application";
     subType = "?:ip";
     mediaType = new MediaType(type,subType);
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(0,e.getErrorOffset());
    }
    assertFalse(fail);
    
    try
    {
     fail = true;
     type = "";
     subType = "zip";
     mediaType = new MediaType(type,subType);
    } catch (ParseException e)
    {
      fail = false;
      assertEquals(0,e.getErrorOffset());
    }
    assertFalse(fail);
    
    
  }
  
  public void testMediaTypeMatchesRawData()
  {
    MediaType mediaType;
    String type;
    String subType;
    String rawData;
    
    // Standard matches: No parameters
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertTrue(mediaType.matches("application/zip"));
    } catch (ParseException e)
    {
      fail();
    }
    
    // Standard matches: No parameters, case change
    try
    {
     type = "application";
     subType = "ZIP";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertTrue(mediaType.matches("application/zip"));
    } catch (ParseException e)
    {
      fail();
    }
    
    // Standard matches:  This media type contains some
    // parameters not present in other.
    try
    {
     type = "application";
     subType = "ZIP";
     rawData = type + "/" + subType+";version=01";
     mediaType = new MediaType(rawData);
     assertTrue(mediaType.matches("application/zip"));
    } catch (ParseException e)
    {
      fail();
    }
    
    // Standard matches:  This media type contains some
    // parameters present in both
    try
    {
     type = "application";
     subType = "ZIP";
     rawData = type + "/" + subType+";version=01";
     mediaType = new MediaType(rawData);
     assertTrue(mediaType.matches("application/zip;version=01"));
    } catch (ParseException e)
    {
      fail();
    }
    
    // Standard matches:  This media type contains some
    // parameters present in both
    try
    {
     type = "application";
     subType = "ZIP";
     rawData = type + "/" + subType+";version=01;format=zip";
     mediaType = new MediaType(rawData);
     assertTrue(mediaType.matches("application/zip;version=01"));
    } catch (ParseException e)
    {
      fail();
    }
    
    
    
    // No match, different baseType
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertFalse(mediaType.matches("application/vnd.opendocument.odf"));
    } catch (ParseException e)
    {
      fail();
    }
    
    // No match, same baseType, some parameters in input to compare with
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertFalse(mediaType.matches("application/zip;version=01"));
    } catch (ParseException e)
    {
      fail();
    }
    
    // No match, same baseType, some parameters in both media types
    // but additional parameters in input
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType+";version=01";
     mediaType = new MediaType(rawData);
     assertFalse(mediaType.matches("application/zip;version=01;format=zip"));
    } catch (ParseException e)
    {
      fail();
    }
    
    // No match, same baseType, some parameters in both media types
    // but some values are different
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType+";version=02;format=zip";
     mediaType = new MediaType(rawData);
     assertFalse(mediaType.matches("application/zip;version=01;format=zip"));
    } catch (ParseException e)
    {
      fail();
    }
    
    
  }  
  
  public void testMediaTypeMatches()
  {
    MediaType mediaType;
    MediaType otherMediaType;
    String type;
    String subType;
    String rawData;
    
    // Standard matches: No parameters
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/zip");
     assertTrue(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    // Standard matches: No parameters, case change
    try
    {
     type = "application";
     subType = "ZIP";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/zip");
     assertTrue(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    // Standard matches:  This media type contains some
    // parameters not present in other.
    try
    {
     type = "application";
     subType = "ZIP";
     rawData = type + "/" + subType+";version=01";
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/zip");
     assertTrue(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    // Standard matches:  This media type contains some
    // parameters present in both
    try
    {
     type = "application";
     subType = "ZIP";
     rawData = type + "/" + subType+";version=01";
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/zip;version=01");
     assertTrue(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    // Standard matches:  This media type contains some
    // parameters present in both
    try
    {
     type = "application";
     subType = "ZIP";
     rawData = type + "/" + subType+";version=01;format=zip";
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/zip;version=01");
     assertTrue(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    
    
    // No match, different baseType
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/vnd.opendocument.odf");
     assertFalse(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    // No match, same baseType, some parameters in input to compare with
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/zip;version=01");
     assertFalse(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    // No match, same baseType, some parameters in both media types
    // but additional parameters in input
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType+";version=01";
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/zip;version=01;format=zip");
     assertFalse(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    // No match, same baseType, some parameters in both media types
    // but some values are different
    try
    {
     type = "application";
     subType = "zip";
     rawData = type + "/" + subType+";version=02;format=zip";
     mediaType = new MediaType(rawData);
     otherMediaType = new MediaType("application/zip;version=01;format=zip");
     assertFalse(mediaType.matches(otherMediaType));
    } catch (ParseException e)
    {
      fail();
    }
    
    
    
  }
  
  
  public void testMediaTypeRawDataInvalid()
  {
    String type;
    String subType;
    String rawData;
    MediaType mediaType;
    boolean fail = true;
    
    try
    {
     fail = true;
     type = "application";
     subType = "";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
     assertEquals(type + "/" + subType,mediaType.toString());
    } catch (ParseException e)
    {
      assertEquals(12,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try
    {
     fail = true;
     type = "";
     subType = "MP4V-ES";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
     assertEquals(type + "/" + subType,mediaType.toString());
    } catch (ParseException e)
    {
      assertEquals(0,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    try
    {
     fail = true;
     type = "application";
     subType = "prs.ace+json()";
     rawData = type + "/" + subType;
     mediaType = new MediaType(rawData);
     assertEquals(type,mediaType.getPrimaryType());
     assertEquals(subType,mediaType.getSubType());
     assertEquals("json",mediaType.getSuffix());
     assertEquals(0,mediaType.getParameters().size());
     assertEquals(type + "/" + subType,mediaType.getBaseType());
    } catch (ParseException e)
    {
      assertEquals(24,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    // With parameters - missing attribute value
    try
    {
     fail = true;
     type = "application";
     subType = "vnd.picsel";
     rawData = type + "/" + subType + ";version=01;charset=";
     mediaType = new MediaType(rawData);
    } catch (ParseException e)
    {
      assertEquals(22,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);

    // Empty attribute name
    try
    {
     fail = true;
     type = "application";
     subType = "vnd.picsel";
     rawData = type + "/" + subType + ";=01";
     mediaType = new MediaType(rawData);
    } catch (ParseException e)
    {
      assertEquals(22,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
    try
    {
     fail = true;
     type = "application";
     subType = "vnd.picsel";
     rawData = type + "/" + subType + ";";
     mediaType = new MediaType(rawData);
    } catch (ParseException e)
    {
      assertEquals(22,e.getErrorOffset());
      fail = false;
    }
    assertFalse(fail);
    
    
  }
  
}
