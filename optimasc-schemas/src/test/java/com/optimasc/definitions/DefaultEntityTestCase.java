package com.optimasc.definitions;

import junit.framework.TestCase;

public class DefaultEntityTestCase extends TestCase
{

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }

  public void testConstructorWithNamespaceAndName()
  {
    DefaultEntity entity = new DefaultEntity("http://example.com/ns/", "MyEntity");
    assertEquals("MyEntity", entity.getName());
    assertEquals("MyEntity", entity.getLocalName());
    assertEquals("http://example.com/ns/MyEntity", entity.getExpandedName());
    assertEquals("http://example.com/ns/", entity.getNamespaceURI());
    assertEquals("MyEntity-OID", entity.getID());
  }

  
  public void testInvalidValues()
  {
    Entity entity;
    // Invalid name length.
    try 
    {
      entity = new DefaultEntity(null,"http://www.w3.org/2001/XMLSchema#","xs:mefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw");
      fail("Failed test, exception should be thrown.");
    } catch (IllegalArgumentException e)
    {
    }

    // Invalid ID
    try 
    {
      entity = new DefaultEntity("1__","http://www.w3.org/2001/XMLSchema#","xs:mefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvw");
      fail("Failed test, exception should be thrown.");
    } catch (IllegalArgumentException e)
    {
    }
  }

  
  public void testExpandedConstructor()
  {
    // ID: No unique identifier
    Entity entity = new DefaultEntity(null,"http://www.w3.org/2001/XMLSchema#","xs:mefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuv");
    assertEquals("http://www.w3.org/2001/XMLSchema#",entity.getNamespaceURI());
    assertEquals("xs:mefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuv",entity.getName());
    assertEquals("xs:mefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuv"+Definition.KEY_ID_SUFFIX,entity.getID());
    assertEquals("http://www.w3.org/2001/XMLSchema#mefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuv",entity.getExpandedName());
    assertEquals("mefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuv",entity.getLocalName());
    
    
    // ID: OBJECT IDENTIFIER
    entity = new DefaultEntity("1.2.3.4","http://www.w3.org/2001/XMLSchema#","xs:Name");
    assertEquals("http://www.w3.org/2001/XMLSchema#",entity.getNamespaceURI());
    assertEquals("xs:Name",entity.getName());
    assertEquals("1.2.3.4",entity.getID());
    assertEquals("http://www.w3.org/2001/XMLSchema#Name",entity.getExpandedName());
    assertEquals("Name",entity.getLocalName());

  }

  
  public void testMinimalConstructor()
  {
     Entity entity = new DefaultEntity(null,"xs:Name");
     assertEquals(null,entity.getNamespaceURI());
     assertEquals("xs:Name",entity.getName());
     assertEquals("xs:Name"+Definition.KEY_ID_SUFFIX,entity.getID());
     assertEquals("Name",entity.getExpandedName());
     assertEquals("Name",entity.getLocalName());
     
     
     entity =new DefaultEntity("http://www.w3.org/2001/XMLSchema#","xs:Integer");
     assertEquals("http://www.w3.org/2001/XMLSchema#",entity.getNamespaceURI());
     assertEquals("xs:Integer",entity.getName());
     assertEquals("xs:Integer"+Definition.KEY_ID_SUFFIX,entity.getID());
     assertEquals("http://www.w3.org/2001/XMLSchema#Integer",entity.getExpandedName());
     assertEquals("Integer",entity.getLocalName());
  }
  
  
  public void testConstructorWithIdAndDescription()
  {
    DefaultEntity entity = new DefaultEntity("1.2.3.4", "http://example.com/ns/",
        "MyEntity");
    assertEquals("MyEntity", entity.getName());
    assertEquals("MyEntity-OID".equals(entity.getID()) ? false : true, true); // overridden by OID
    assertEquals("1.2.3.4", entity.getID());
  }

  public void testGetExpandedNameWithoutNamespace()
  {
    DefaultEntity entity = new DefaultEntity(null, "NameOnly");
    assertEquals("NameOnly", entity.getExpandedName());
  }

  public void testPutInvalidName()
  {
    try
    {
      new DefaultEntity(null, ""); // empty name
      fail("Expected IllegalArgumentException for empty name");
    } catch (IllegalArgumentException e)
    {
      // expected
    }

    try
    {
      String longName = new String(new char[DefaultEntity.NAME_MAX_LENGTH + 1]).replace(
          '\0', 'A');
      new DefaultEntity(null, longName);
      fail("Expected IllegalArgumentException for name too long");
    } catch (IllegalArgumentException e)
    {
      // expected
    }
  }


  public void testEqualsMethod()
  {
    DefaultEntity entity1 = new DefaultEntity("1.2.3.4", "ns", "EntityA");
    DefaultEntity entity2 = new DefaultEntity("1.2.3.4", "ns", "EntityA");
    assertTrue(entity1.equals(entity2));

    DefaultEntity entity3 = new DefaultEntity("1.2.3.5", "ns", "EntityA");
    assertFalse(entity1.equals(entity3));
  }


}
