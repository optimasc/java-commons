package com.optimasc.utils;

import java.io.IOException;
import java.io.InputStream;

import junit.framework.TestCase;

public class PropertyListTest extends TestCase
{

  public PropertyListTest(String name)
  {
    super(name);
  }

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testLoad()
  {
    InputStream inStream = getClass().getResourceAsStream("/res/test.properties");
    PropertyList list = new PropertyList();
    try
    {
      list.load(inStream);
      inStream.close();
      // Normal property values and check for correct carriage returns
      assertEquals("my first property", list.getString("property.1"));
      assertEquals("my second property", list.getString("property.2"));
      assertEquals("my third property", list.getString("property.3"));
      // Validate UTF-8 encoding of data
      assertEquals("ma quatri\u00E8me propri\u00E9t\u00E9",list.getString("property.quatre"));
      // Empty property
      assertEquals("",list.getString("property.empty"));
      // A property containing only whitespace is considered to be empty.
      assertEquals("",list.getString("property.whitespace"));
      // A property that does not exist should be returned a null
      assertEquals(null,list.getString("property.doesnotexist"));
      assertEquals("This is a very string that contains\nsome newline characters.\n",list.getString("escapeString"));
    } catch (IOException e)
    {
      fail("JUnit load property failed!");
    }
  }

  public void testStore()
  {
    InputStream inStream = getClass().getResourceAsStream("/res/test.properties");
    PropertyList list = new PropertyList();
    try
    {
      list.load(inStream);
      inStream.close();
      list.store(System.out,"My comment");
    } catch (IOException e)
    {
      fail("JUnit load property failed!");
    }
  }

  /** Tests a normal memory related Property List */
  public void testStandard()
  {
    PropertyList list = new PropertyList();
    assertEquals("DefaultValue",list.getString("MyKey", "DefaultValue"));
    
    list.putString("key1", "value01");
    list.putString("key2", "value02");
    list.putString("key3", "value03");
    
    assertEquals("value01",list.getString("key1"));
    assertEquals("value02",list.getString("key2"));
    assertEquals("value03",list.getString("key3"));
    
    list.clear();
    assertEquals(null,list.getString("key1"));
    assertEquals(null,list.getString("key2"));
    assertEquals(null,list.getString("key3"));

    list.putString("key1", "value01");
    list.putString("key2", "value02");
    list.putString("key3", "value03");
    
    list.remove("key1");
    list.remove("key2");
    list.remove("key3");
    
    assertEquals(null,list.getString("key1"));
    assertEquals(null,list.getString("key2"));
    assertEquals(null,list.getString("key3"));

    
  }
}
