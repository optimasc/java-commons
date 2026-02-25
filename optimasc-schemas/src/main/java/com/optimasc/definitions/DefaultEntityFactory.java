package com.optimasc.definitions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Default implementation of the entity factory.
 * 
 * This implementation also verifies the compliance with schemas when they are
 * available.
 * 
 * @author Carl Eric Codere
 *
 */
public class DefaultEntityFactory implements EntityFactory
{
  /** Entity registry */
  protected EntityRegistry schemaRegistry;

  /**
   * Creates a default factory using the specified pre-defined entity
   * information with schema information.
   * 
   * @param schemaRegistry
   *          [in] The registry of definitions that contains schema information.
   */
  public DefaultEntityFactory(EntityRegistry schemaRegistry)
  {
    super();
    this.schemaRegistry = schemaRegistry;
  }

  /**
   * Creates and initializes an {@link Entity}.
   * 
   * <p>The creation process performs the following steps:</p>
   * <ul>
   *   <li>Verifies the validity of each attribute against the schema using
   *     {@link #verifyAttribute(EntityRegistry, Object, Object)}.</li>
   *   <li>If an {@code objectClass} attribute is present, validates the class
   *        definition using {@link #verifyClassDefinition(EntityRegistry, String, java.util.Enumeration)}.</li>
   *   <li>If a valid class definition is found, instantiates the corresponding
   *       Java class using a constructor with {@code (String namespaceURI, String name)}. 
   *       The java class name to use is specified in {@link ClassDefinition#KEY_TYPE_JAVA_CLASS_NAME}
   *       of <code>ClassDefinition</code>.</li>
   *   <li>If no valid class definition is found a
   *       {@link DefaultEntity} is created.</li>
   *   <li>Assigns all provided attributes to the created entity.</li>
   * </ul>
   *
   * <p>If a class definition is used, the associated Java class must extend
   * {@link DefaultEntity} with a two parameter public constructor ({@code (String namespaceURI, String name)).</p>
   * 
   * @param ref [in] Optional reference object for instantiation (ignored in this implementation).
   * @param namespaceURI
   *          [in] The namespaceURI of the entity or <code>null</code> if none.
   * @param name
   *          [in] The qualified name of the entity to create. This value is
   *          mandatory and must not be <code>null</code> and must not be
   *          empty or only composed of whitespace.
   * @param env
   *          [in] Environment attributes for configuration (must not be {@code null};
 *            ignored in this implementation).
   * @param attributes
   *          [in] The attributes to assign to the created entity. This value
   *          cannot be <code>null</code> even though it may be empty.
   * @return An instance of the <code>Entity</code> or a derived class from
   *         <code>Entity</code>.
   * @throws IllegalArgumentException
   *          If attribute validation fails, the class definition is invalid, the
   *          <code>name</code> is null or invalid or the entity class cannot be instantiated.          
   * 
   */
  public Entity getObjectInstance(Object ref, String namespaceURI, String name,
      Map/*<String,Object>*/env, Map/*<String,Object>*/attributes)
  {
    DefaultEntity entity = null;
    if (name==null)
    {
      throw new IllegalArgumentException("'name' must not be null.");
    }
    if (name.trim().length()==0)
    {
      throw new IllegalArgumentException("'name' must not be an empty string or containing only whitespace.");
    }
    /* Create a copy of the attributes, which will contain the name.
     * 
     */
    Map/*<String,Object>*/ localAttributes = new HashMap(attributes);
    
    localAttributes.put(Entity.KEY_NAME, name);
    if (namespaceURI != null)
      localAttributes.put(Entity.KEY_NAME_NS_URI, namespaceURI);
    
    /* Verify the validity of the attributes */
    Iterator/*<String>*/it = localAttributes.keySet().iterator();
    while (it.hasNext())
    {
      Object key = it.next();
      Object value = localAttributes.get(key);
      // Verify each attribute
      IllegalArgumentException exception = SchemaUtilities.verifyAttribute(schemaRegistry, key, value);
      if (exception != null)
        throw exception;
    }
    
    /* Verify the validity of the class definition */
    String className = (String) attributes.get(Entity.KEY_OBJECT_CLASS_NAME);
    IllegalArgumentException exception = SchemaUtilities.verifyClassDefinition(schemaRegistry, className,
        Collections.enumeration(localAttributes.keySet()));
    if (exception != null)
      throw exception;
    if (className != null)
    {
      ClassDefinition classDef = (ClassDefinition) schemaRegistry.lookup(className,
          ClassDefinition.class);
      if (classDef != null)
      {
        Class clz = classDef.getClz();
        if (clz != null)
        {
          if (DefaultEntity.class.isAssignableFrom(clz) == false)
          {
            throw new IllegalArgumentException("'" + clz.getName()
                + "' should be derived from '" + DefaultEntity.class.getName() + "'.");
          }
          Constructor constructor = null;
          try
          {
            constructor = clz.getDeclaredConstructor(new Class[] { String.class,
                String.class });
          } catch (SecurityException e1)
          {
            IllegalArgumentException exc = new IllegalArgumentException(e1.getMessage());
            exc.initCause(e1);
            throw exc;
          } catch (NoSuchMethodException e1)
          {
            IllegalArgumentException exc = new IllegalArgumentException(e1.getMessage());
            exc.initCause(e1);
            throw exc;
          }
          try
          {
            entity = (DefaultEntity) constructor.newInstance(new String[] { namespaceURI,
                name });
          } catch (IllegalArgumentException e)
          {
            IllegalArgumentException exc = new IllegalArgumentException(e.getMessage());
            exc.initCause(e);
            throw exc;
          } catch (InstantiationException e)
          {
            IllegalArgumentException exc = new IllegalArgumentException(e.getMessage());
            exc.initCause(e);
            throw exc;
          } catch (IllegalAccessException e)
          {
            IllegalArgumentException exc = new IllegalArgumentException(e.getMessage());
            exc.initCause(e);
            throw exc;
          } catch (InvocationTargetException e)
          {
            IllegalArgumentException exc = new IllegalArgumentException(e.getMessage());
            exc.initCause(e);
            throw exc;
          }
        }
      }
    }

    if (entity == null)
    {
      entity = new DefaultEntity(namespaceURI, name);
    }

    /* Now set all attributes - except the ones that we added manually for checking */
    it = attributes.keySet().iterator();
    while (it.hasNext())
    {
      String key = it.next().toString();
      Object value = attributes.get(key);
      entity.put(key, value);
    }
    localAttributes = null;
    return entity;
  }

  public EntityRegistry getSchemaRegistry()
  {
    return schemaRegistry;
  }


}
