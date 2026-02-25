package com.optimasc.definitions;

import junit.framework.TestCase;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

import com.optimasc.definitions.ClassDefinition.ObjectClassKind;

/**
 * JUnit 3 test cases for DefaultEntityFactory.getObjectInstance method
 * using the CoreDefinitions schema registry.
 * Compatible with Java 1.4 SE.
 * 
 * @author Test Suite
 */
public class DefaultEntityFactoryTest extends TestCase
{
  private DefaultEntityFactory factory;
  private EntityRegistry schemaRegistry;

  protected void setUp() throws Exception
  {
    super.setUp();
    // Use the pre-defined schema registry from CoreDefinitions
    schemaRegistry = CoreDefinitions.getSchemaRegistry();
    factory = new DefaultEntityFactory(schemaRegistry);
  }

  protected void tearDown() throws Exception
  {
    factory = null;
    schemaRegistry = null;
    super.tearDown();
  }

  // ========== Basic Entity Creation Tests ==========

  /**
   * Test creating a simple entity with minimal parameters (no objectClass)
   * Should create a DefaultEntity instance
   */
  public void testCreateSimpleEntity()
  {
    Map attributes = new HashMap();
    Entity entity = factory.getObjectInstance(null, null, "testEntity", 
        new HashMap(), attributes);
    
    assertNotNull("Entity should not be null", entity);
    assertEquals("testEntity", entity.getName());
    assertEquals("testEntity"+Entity.KEY_ID_SUFFIX, entity.getID());
    assertNull("Namespace URI should be null", entity.getNamespaceURI());
    assertTrue("Should be instance of DefaultEntity", 
        entity instanceof DefaultEntity);
    // Without objectClass, should be exactly DefaultEntity, not a subclass
    assertEquals("Should be DefaultEntity class", 
        DefaultEntity.class, entity.getClass());
  }

  /**
   * Test creating an entity with namespace URI
   */
  public void testCreateEntityWithNamespace()
  {
    Map attributes = new HashMap();
    String namespaceURI = "http://example.com/schema";
    
    Entity entity = factory.getObjectInstance(null, namespaceURI, "testEntity",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertEquals("testEntity"+Entity.KEY_ID_SUFFIX, entity.getID());
    assertEquals("testEntity", entity.getName());
    assertEquals(namespaceURI, entity.getNamespaceURI());
  }

  /**
   * Test creating an entity with various valid attributes but no objectClass
   */
  public void testCreateEntityWithValidAttributes()
  {
    Map attributes = new HashMap();
    attributes.put(Definition.KEY_DESCRIPTION, "Test description");
    attributes.put(Definition.KEY_DISPLAY_NAME, "Test Display Name");
    attributes.put(Definition.KEY_OBSOLETE, Boolean.FALSE);
    
    Entity entity = factory.getObjectInstance(null, null, "testEntity",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    // Access via get() since it's a DefaultEntity, not DefaultDefinition
    assertEquals("Test description", 
        entity.get(Definition.KEY_DESCRIPTION, String.class));
    assertEquals("Test Display Name", 
        entity.get(Definition.KEY_DISPLAY_NAME, String.class));
    assertEquals(Boolean.FALSE, 
        entity.get(Definition.KEY_OBSOLETE, Boolean.class));
  }

  // ========== Class Definition Instantiation Tests ==========

  /**
   * Test creating an AttributeDefinition instance via objectClass
   */
  public void testCreateAttributeDefinitionInstance()
  {
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, 
        CoreDefinitions.Classes.NAME_CLASS_ATTRIBUTE_DEFINITION);
    attributes.put(ItemDefinition.KEY_TYPE_NAME, 
        CoreDefinitions.Types.ID_TYPE_STRING);
    attributes.put(Entity.KEY_NAME, "testAttribute");
    
    Entity entity = factory.getObjectInstance(null, null, "testAttribute",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertTrue("Should be instance of DefaultItemDefinition", 
        entity instanceof DefaultItemDefinition);
    assertTrue("Should be instance of ItemDefinition", 
        entity instanceof ItemDefinition);
    assertEquals("testAttribute", entity.getName());
    assertEquals(CoreDefinitions.Classes.NAME_CLASS_ATTRIBUTE_DEFINITION, 
        entity.get(Definition.KEY_OBJECT_CLASS_NAME, String.class));
    assertEquals(Boolean.FALSE, 
        entity.get(Definition.KEY_OBSOLETE, Boolean.class));
  }

  /**
   * Test creating a ClassDefinition instance via objectClass
   */
  public void testCreateClassDefinitionInstance()
  {
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, 
        CoreDefinitions.Classes.NAME_CLASS_CLASS_DEFINITION);
    attributes.put(Entity.KEY_NAME, "testClass");
    
    Entity entity = factory.getObjectInstance(null, null, "testClass",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertTrue("Should be instance of DefaultClassDefinition", 
        entity instanceof DefaultClassDefinition);
    assertTrue("Should be instance of ClassDefinition", 
        entity instanceof ClassDefinition);
    assertEquals("testClass", entity.getName());
    assertEquals(CoreDefinitions.Classes.NAME_CLASS_CLASS_DEFINITION, 
        entity.get(Definition.KEY_OBJECT_CLASS_NAME, String.class));
    assertEquals(Boolean.FALSE, 
        entity.get(Definition.KEY_OBSOLETE, Boolean.class));
  }

  /**
   * Test creating a SyntaxDefinition (TypeDefinition) instance via objectClass
   */
  public void testCreateSyntaxDefinitionInstance()
  {
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, 
        CoreDefinitions.Classes.NAME_CLASS_SYNTAX_DEFINITION);
    attributes.put(Entity.KEY_NAME, "testSyntax");
    
    Entity entity = factory.getObjectInstance(null, null, "testSyntax",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertTrue("Should be instance of DefaultTypeDefinition", 
        entity instanceof DefaultTypeDefinition);
    assertTrue("Should be instance of TypeDefinition", 
        entity instanceof TypeDefinition);
    assertEquals("testSyntax", entity.getName());
    assertEquals(CoreDefinitions.Classes.NAME_CLASS_SYNTAX_DEFINITION, 
        entity.get(Definition.KEY_OBJECT_CLASS_NAME, String.class));
    assertEquals(Boolean.FALSE, 
        entity.get(Definition.KEY_OBSOLETE, Boolean.class));
  }

  // ========== Null/Invalid Name Tests ==========

  /**
   * Test that null name throws IllegalArgumentException
   */
  public void testCreateEntityWithNullName()
  {
    Map attributes = new HashMap();
    try
    {
      factory.getObjectInstance(null, null, null, new HashMap(), attributes);
      fail("Should throw IllegalArgumentException for null name");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("must not be null") >= 0);
    }
  }

  /**
   * Test that empty string name throws IllegalArgumentException
   */
  public void testCreateEntityWithEmptyName()
  {
    Map attributes = new HashMap();
    try
    {
      factory.getObjectInstance(null, null, "", new HashMap(), attributes);
      fail("Should throw IllegalArgumentException for empty name");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("empty string") >= 0);
    }
  }

  /**
   * Test that whitespace-only name throws IllegalArgumentException
   */
  public void testCreateEntityWithWhitespaceName()
  {
    Map attributes = new HashMap();
    try
    {
      factory.getObjectInstance(null, null, "   ", new HashMap(), attributes);
      fail("Should throw IllegalArgumentException for whitespace name");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("whitespace") >= 0);
    }
  }

  // ========== Attribute Validation Tests ==========

  /**
   * Test validation of string attribute with correct type
   */
  public void testValidateStringAttribute()
  {
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_NAME, "validName");
    attributes.put(Definition.KEY_DESCRIPTION, "Valid description");
    
    Entity entity = factory.getObjectInstance(null, null, "testEntity",
        new HashMap(), attributes);
    
    assertNotNull(entity);
  }

  /**
   * Test validation of boolean attribute with correct type
   * Creates a proper Definition instance to test the isObsolete() method
   */
  public void testValidateBooleanAttribute()
  {
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, 
        CoreDefinitions.Classes.NAME_CLASS_ATTRIBUTE_DEFINITION);
    attributes.put(Definition.KEY_OBSOLETE, Boolean.TRUE);
    attributes.put(ItemDefinition.KEY_TYPE_NAME, 
        CoreDefinitions.Types.ID_TYPE_OCTET_STRING);
    
    Entity entity = factory.getObjectInstance(null, null, "testEntity",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertTrue("Should be a Definition", entity instanceof Definition);
    Definition def = (Definition) entity;
    assertEquals(true, def.isObsolete());
  }

  /**
   * Test validation fails when wrong type is provided for boolean attribute
   */
  public void testValidateBooleanAttributeWithWrongType()
  {
    Map attributes = new HashMap();
    // Providing String instead of Boolean for OBSOLETE
    attributes.put(Definition.KEY_OBSOLETE, "true");
    
    try
    {
      factory.getObjectInstance(null, null, "testEntity", 
          new HashMap(), attributes);
      fail("Should throw IllegalArgumentException for wrong type");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("class") >= 0);
    }
  }

  /**
   * Test maximum description length validation
   */
  public void testDescriptionMaxLengthValidation()
  {
    Map attributes = new HashMap();
    // Create a description longer than DESC_MAX_LENGTH (1024 characters)
    StringBuffer longDesc = new StringBuffer();
    for (int i = 0; i < 1100; i++)
    {
      longDesc.append('x');
    }
    attributes.put(Definition.KEY_DESCRIPTION, longDesc.toString());
    
    try
    {
      factory.getObjectInstance(null, null, "testEntity", 
          new HashMap(), attributes);
      fail("Should throw IllegalArgumentException for description too long");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("1024") >= 0);
    }
  }

  // ========== Single vs Multiple Value Tests ==========

  /**
   * Test that single-valued attribute rejects array
   */
  public void testSingleValuedAttributeRejectsArray()
  {
    Map attributes = new HashMap();
    // KEY_DESCRIPTION is defined as single-valued in schema
    attributes.put(Definition.KEY_DESCRIPTION, new String[] {"desc1", "desc2"});
    
    try
    {
      factory.getObjectInstance(null, null, "testEntity", 
          new HashMap(), attributes);
      fail("Should reject array for single-valued attribute");
    }
    catch (IllegalArgumentException e)
    {
    }
  }

  // ========== Class Definition Tests ==========

  /**
   * Test that abstract class cannot be instantiated
   * Uses the "top" class which is defined as abstract
   */
  public void testCannotInstantiateAbstractClass()
  {
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, 
        CoreDefinitions.Classes.NAME_CLASS_TOP);
    
    try
    {
      factory.getObjectInstance(null, null, "instance", 
          new HashMap(), attributes);
      fail("Should not be able to instantiate abstract class 'top'");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("abstract") >= 0);
    }
  }

  /**
   * Test mandatory attributes validation for class definition
   * The "top" class requires KEY_OBJECT_CLASS_NAME as mandatory
   */
  public void testMandatoryAttributesValidation()
  {
    // Create a custom class definition with mandatory attributes
    String[] mandatoryAttribs = new String[] {
        Definition.KEY_DESCRIPTION, 
        Definition.KEY_DISPLAY_NAME
    };
    DefaultClassDefinition classDef = new DefaultClassDefinition(
        null, "1.2.3.5", null, "TestClassWithMandatory", null, mandatoryAttribs,
        ObjectClassKind.structuralClass.toString(), null, "Test class");
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(classDef);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, "TestClassWithMandatory");
    attributes.put(Definition.KEY_DESCRIPTION, "Has description");
    // Missing mandatory DISPLAY_NAME attribute
    
    try
    {
      testFactory.getObjectInstance(null, null, "instance", 
          new HashMap(), attributes);
      fail("Should fail due to missing mandatory attribute");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("Missing attribute") >= 0);
      assertTrue(e.getMessage().indexOf("displayName") >= 0);
    }
  }

  /**
   * Test successful creation with all mandatory attributes present
   */
  public void testMandatoryAttributesPresent()
  {
    String[] mandatoryAttribs = new String[] {
        Definition.KEY_DESCRIPTION,
        Definition.KEY_DISPLAY_NAME
    };
    DefaultClassDefinition classDef = new DefaultClassDefinition(
        null, "1.2.3.6", null, "TestClass2", null, mandatoryAttribs,
        ObjectClassKind.structuralClass.toString(), null, "Test class");
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(classDef);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, "TestClass2");
    attributes.put(Definition.KEY_DESCRIPTION, "Test description");
    attributes.put(Definition.KEY_DISPLAY_NAME, "Test Display");
    
    Entity entity = testFactory.getObjectInstance(null, null, "instance",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertEquals("Test description", 
        entity.get(Definition.KEY_DESCRIPTION, String.class));
    assertEquals("Test Display", 
        entity.get(Definition.KEY_DISPLAY_NAME, String.class));
  }

  /**
   * Test that ClassDefinition class can instantiate DefaultClassDefinition
   */
  public void testClassDefinitionInstantiation()
  {
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, 
        CoreDefinitions.Classes.NAME_CLASS_CLASS_DEFINITION);
    attributes.put(Entity.KEY_NAME, "myClass");
    attributes.put(Definition.KEY_DESCRIPTION, "My class description");
    
    Entity entity = factory.getObjectInstance(null, null, "myClass",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertTrue(entity instanceof DefaultClassDefinition);
    ClassDefinition classDef = (ClassDefinition) entity;
    assertEquals("myClass", classDef.getName());
    assertEquals("My class description", classDef.getDescription());
  }

  // ========== Type Conversion Tests ==========

  /**
   * Test that integer type is validated correctly
   */
  public void testIntegerTypeValidation()
  {
    // Create an item definition with INTEGER type
    DefaultItemDefinition intItem = new DefaultItemDefinition(
        "1.2.3.100", null, "testInteger", "Test integer attribute",
        CoreDefinitions.Types.ID_TYPE_INTEGER, 
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(intItem);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    attributes.put("testInteger", new BigInteger("12345"));
    
    Entity entity = testFactory.getObjectInstance(null, null, "instance",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertEquals(new BigInteger("12345"), 
        entity.get("testInteger", BigInteger.class));
  }

  /**
   * Test that wrong type for integer attribute fails validation
   */
  public void testIntegerTypeValidationFailure()
  {
    DefaultItemDefinition intItem = new DefaultItemDefinition(
        "1.2.3.101", null, "testInteger2", "Test integer attribute",
        CoreDefinitions.Types.ID_TYPE_INTEGER,
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(intItem);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    // Providing String instead of BigInteger
    attributes.put("testInteger2", "12345");
    
    try
    {
      testFactory.getObjectInstance(null, null, "instance", 
          new HashMap(), attributes);
      fail("Should fail with wrong type for integer");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("class") >= 0);
    }
  }

  /**
   * Test binary (byte array) type validation
   */
  public void testBinaryTypeValidation()
  {
    DefaultItemDefinition binaryItem = new DefaultItemDefinition(
        "1.2.3.102", null, "testBinary", "Test binary attribute",
        CoreDefinitions.Types.ID_TYPE_OCTET_STRING,
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(binaryItem);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    byte[] data = new byte[] {0x01, 0x02, 0x03, 0x04};
    attributes.put("testBinary", data);
    
    Entity entity = testFactory.getObjectInstance(null, null, "instance",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    byte[] result = (byte[]) entity.get("testBinary", byte[].class);
    assertNotNull(result);
    assertEquals(4, result.length);
  }

  // ========== Multi-valued Attribute Tests ==========

  /**
   * Test multi-valued attribute with array
   */
  public void testMultiValuedAttributeWithArray()
  {
    DefaultItemDefinition multiItem = new DefaultItemDefinition(
        "1.2.3.103", null, "testMulti", "Test multi attribute",
        CoreDefinitions.Types.ID_TYPE_STRING,
        DefaultItemDefinition.VALUE_TYPE_BAG, null, false);
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(multiItem);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    String[] values = new String[] {"value1", "value2", "value3"};
    attributes.put("testMulti", values);
    
    Entity entity = testFactory.getObjectInstance(null, null, "instance",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    String[] result = (String[]) entity.get("testMulti", String[].class);
    assertNotNull(result);
    assertEquals(3, result.length);
  }

  /**
   * Test multi-valued attribute type checking with wrong element type
   */
  public void testMultiValuedAttributeWrongElementType()
  {
    DefaultItemDefinition multiItem = new DefaultItemDefinition(
        "1.2.3.104", null, "testMulti2", "Test multi attribute",
        CoreDefinitions.Types.ID_TYPE_STRING,
        DefaultItemDefinition.VALUE_TYPE_SEQ, null, false);
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(multiItem);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    // Providing Integer array instead of String array
    Integer[] values = new Integer[] {new Integer(1), new Integer(2)};
    attributes.put("testMulti2", values);
    
    try
    {
      testFactory.getObjectInstance(null, null, "instance",
          new HashMap(), attributes);
      fail("Should fail with wrong element type in array");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("array") >= 0);
    }
  }

  // ========== Edge Cases and Special Tests ==========

  /**
   * Test creating entity with empty attributes map
   */
  public void testCreateEntityWithEmptyAttributes()
  {
    Map attributes = new HashMap();
    
    Entity entity = factory.getObjectInstance(null, null, "testEntity",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertEquals("testEntity", entity.getName());
  }

  /**
   * Test that entity registry is accessible from factory
   */
  public void testGetSchemaRegistry()
  {
    EntityRegistry reg = factory.getSchemaRegistry();
    assertNotNull(reg);
    assertEquals(schemaRegistry, reg);
  }

  /**
   * Test creating multiple entities with same factory
   */
  public void testCreateMultipleEntities()
  {
    Map attrs1 = new HashMap();
    attrs1.put(Definition.KEY_DESCRIPTION, "First entity");
    
    Map attrs2 = new HashMap();
    attrs2.put(Definition.KEY_DESCRIPTION, "Second entity");
    
    Entity entity1 = factory.getObjectInstance(null, null, "entity1",
        new HashMap(), attrs1);
    Entity entity2 = factory.getObjectInstance(null, null, "entity2",
        new HashMap(), attrs2);
    
    assertNotNull(entity1);
    assertNotNull(entity2);
    assertNotSame(entity1, entity2);
    assertEquals("entity1", entity1.getName());
    assertEquals("entity2", entity2.getName());
  }

  /**
   * Test with maximum allowed name length
   */
  public void testMaximumNameLength()
  {
    // NAME_MAX_LENGTH is 48 characters
    StringBuffer name = new StringBuffer();
    for (int i = 0; i < Entity.NAME_MAX_LENGTH; i++)
    {
      name.append('a');
    }
    
    Map attributes = new HashMap();
    Entity entity = factory.getObjectInstance(null, null, name.toString(),
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertEquals(Entity.NAME_MAX_LENGTH, entity.getName().length());
  }

  /**
   * Test that name exceeding maximum length fails
   */
  public void testExceedMaximumNameLength()
  {
    // NAME_MAX_LENGTH is 48 characters
    StringBuffer name = new StringBuffer();
    for (int i = 0; i < Entity.NAME_MAX_LENGTH + 1; i++)
    {
      name.append('a');
    }
    
    Map attributes = new HashMap();
    try
    {
      factory.getObjectInstance(null, null, name.toString(), 
          new HashMap(), attributes);
      fail("Should fail with name exceeding maximum length");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("48") >= 0);
    }
  }

  /**
   * Test object ID attribute validation
   */
  public void testObjectIdentifierAttribute()
  {
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_ID, "1.2.3.4.5.6");
    
    Entity entity = factory.getObjectInstance(null, null, "testEntity",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    String oid = entity.getID();
    assertEquals("1.2.3.4.5.6", oid);
  }

  /**
   * Test invalid object identifier format
   */
  public void testInvalidObjectIdentifier()
  {
    Map attributes = new HashMap();
    // Invalid OID format (contains letters)
    attributes.put(Entity.KEY_ID, "1.2.abc.4");
    
    try
    {
      factory.getObjectInstance(null, null, "testEntity",
          new HashMap(), attributes);
      fail("Should fail with invalid OID format");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("OID") >= 0 ||
                 e.getMessage().indexOf("OBJECT IDENTIFIER") >= 0);
    }
  }

  /**
   * Test attribute with maximum value length constraint
   */
  public void testAttributeMaxValueLength()
  {
    DefaultItemDefinition stringItem = new DefaultItemDefinition(
        "1.2.3.105", null, "testString", "Test string attribute",
        CoreDefinitions.Types.ID_TYPE_STRING + "{10}",
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(stringItem);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    // String longer than 10 characters
    attributes.put("testString", "This is a very long string");
    
    try
    {
      testFactory.getObjectInstance(null, null, "instance",
          new HashMap(), attributes);
      fail("Should fail when exceeding maximum value length");
    }
    catch (IllegalArgumentException e)
    {
      assertTrue(e.getMessage().indexOf("maximum length") >= 0 ||
                 e.getMessage().indexOf("10") >= 0);
    }
  }

  /**
   * Test namespace URI validation
   */
  public void testNamespaceURIValidation()
  {
    Map attributes = new HashMap();
    String nsUri = "http://www.example.com/schemas/test";
    
    Entity entity = factory.getObjectInstance(null, nsUri, "testEntity",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    assertEquals(nsUri, entity.getNamespaceURI());
    assertEquals("testEntity", entity.getLocalName());
  }

  /**
   * Test that when no Java class is specified, DefaultEntity is created
   * even when objectClass is set
   */
  public void testObjectClassWithoutJavaClass()
  {
    // Create a class definition without Java class name
    DefaultClassDefinition classDef = new DefaultClassDefinition(
        null, "1.2.3.200", null, "NoJavaClass", null, null,
        ObjectClassKind.structuralClass.toString(), null, "Class without Java implementation");
    
    EntityRegistry testRegistry = new EntityRegistry();
    testRegistry.addAll(CoreDefinitions.getSchemaRegistry().getAsSet());
    testRegistry.add(classDef);
    
    DefaultEntityFactory testFactory = new DefaultEntityFactory(testRegistry);
    
    Map attributes = new HashMap();
    attributes.put(Entity.KEY_OBJECT_CLASS_NAME, "NoJavaClass");
    
    Entity entity = testFactory.getObjectInstance(null, null, "instance",
        new HashMap(), attributes);
    
    assertNotNull(entity);
    // Should fall back to DefaultEntity
    assertEquals(DefaultEntity.class, entity.getClass());
  }
}