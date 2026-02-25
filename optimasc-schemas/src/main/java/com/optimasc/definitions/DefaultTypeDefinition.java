package com.optimasc.definitions;

import java.text.Format;

/**
 * Default implementation of {@link TypeDefinition}.
 * 
 * <p>
 * This class provides a concrete implementation for type definitions, which
 * specify how data types are represented in Java and how they are converted
 * to/from string representations. It extends {@link DefaultDefinition} and
 * implements {@link TypeDefinition}.
 * </p>
 * 
 * <p>
 * Two constructors are provided:
 * </p>
 * <ul>
 * <li>One accepting {@link Class} and {@link Format} instances directly</li>
 * <li>One accepting fully qualified class names as strings, which are then
 * loaded and instantiated at construction time</li>
 * </ul>
 * 
 * @author Carl Eric Codere
 */
public class DefaultTypeDefinition extends DefaultDefinition implements TypeDefinition
{
  /**
   * Creates a type definition with direct class and formatter instances.
   * 
   * @param id
   *          [in] The unique identifier for this type definition. This should
   *          be an OBJECT IDENTIFIER in dotted decimal notation (e.g.,
   *          "1.2.3.4"). Can be <code>null</code> for auto-generation.
   * @param namespaceURI
   *          [in] The namespace URI associated with this type definition, or
   *          <code>null</code> if no namespace is associated.
   * @param qualifiedName
   *          [in] The qualified name of this type definition. This value is
   *          mandatory and must not be <code>null</code> or empty. This is
   *          similar to the descriptor in the LDAP specification.
   * @param description
   *          [in] A human-readable description of this type definition, or
   *          <code>null</code> if no description is provided.
   * @param clz
   *          [in] The Java {@link Class} object representing the type used for
   *          values of this data type in memory. If this value is
   *          <code>null</code> the class is set to
   *          <code>java.lang.String</code>.
   * @param formatter
   *          [in] The {@link Format} instance used to parse string
   *          representations into Java objects and format Java objects into
   *          strings, or <code>null</code> if no formatter is available.
   * 
   * @throws IllegalArgumentException
   *           if <code>qualifiedName</code> or if <code>id</code> is not a
   *           valid OBJECT IDENTIFIER.
   */
  public DefaultTypeDefinition(String id, String namespaceURI, String qualifiedName,
      String description, Class clz, Format formatter)
  {
    super(id, namespaceURI, qualifiedName, description);

    if (clz == null)
    {
      clz = String.class;
    }

    // Store the class name as a String, as per TypeDefinition contract
    put(KEY_TYPE_JAVA_CLASS_NAME, clz.getName());
    if (formatter != null)
      put(KEY_FORMATTER, formatter);
  }

  /**
   * Creates a type definition from fully qualified class names.
   * 
   * <p>
   * This constructor is useful when type definitions are loaded from external
   * sources (such as configuration files or databases) where only the class
   * names are available as strings. The classes are loaded and the formatter is
   * instantiated during construction.
   * </p>
   * 
   * @param id
   *          [in] The unique identifier for this type definition. Can be
   *          <code>null</code> for auto-generation.
   * @param namespaceURI
   *          [in] The namespace URI associated with this type definition, or
   *          <code>null</code> if no namespace is associated.
   * @param qualifiedName
   *          [in] The qualified name of this type definition. This value is
   *          mandatory and must not be <code>null</code> or empty.
   * @param description
   *          [in] A human-readable description of this type definition, or
   *          <code>null</code> if no description is provided.
   * @param className
   *          [in] The fully qualified name of the Java class used to represent
   *          values of this type (e.g., "java.lang.String"). This class will be
   *          loaded using {@link Class#forName(String)}. This value is
   *          mandatory and must not be <code>null</code>.
   * @param formatterName
   *          [in] The fully qualified name of the {@link Format} class to be
   *          instantiated for parsing and formatting (e.g.,
   *          "com.example.MyFormatter"). The class must have a public
   *          no-argument constructor. This value is optional and may be set to
   *          <code>null</code>.
   * 
   * @throws IllegalArgumentException
   *           if any class cannot be found, if the formatter class cannot be
   *           instantiated, or if any required parameter is invalid.
   */
  public DefaultTypeDefinition(String id, String namespaceURI, String qualifiedName,
      String description, String className, String formatterName)
  {
    super(id, namespaceURI, qualifiedName, description);
    try
    {
      Class clz = Class.forName(className);
    } catch (ClassNotFoundException e2)
    {
      throw new IllegalArgumentException("'" + className + "' not found.");
    }
    Format formatter = null;
    Class formatterClass = null;
    if (formatterName != null)
    {
      try
      {
        formatterClass = Class.forName(formatterName);
      } catch (ClassNotFoundException e1)
      {
        IllegalArgumentException exc = new IllegalArgumentException("'" + formatterName + "' not found.");
        exc.initCause(e1);
        throw exc;
      }
      if (formatterClass == null)
      {
        throw new IllegalArgumentException("'" + formatterName + "' not found.");
      }
    }
    try
    {
      formatter = (Format) formatterClass.newInstance();
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
    }
    if (formatter != null)   
       put(KEY_FORMATTER, formatter);
    put(KEY_TYPE_JAVA_CLASS_NAME, className);
  }

  public Format getFormatter()
  {
    return (Format) get(KEY_FORMATTER, Format.class);
  }

  public Class getClz()
  {
    String className = (String) get(KEY_TYPE_JAVA_CLASS_NAME, String.class);
    if (className != null)
    {
      Class clz;
      try
      {
        clz = Class.forName(className);
      } catch (ClassNotFoundException e)
      {
        throw new IllegalArgumentException("'" + className + "' not found.");
      }
      return clz;
    }
    return null;
  }
  
  public DefaultTypeDefinition(String namespaceURI, String qualifiedName)
  {
    super(namespaceURI, qualifiedName);
    put(KEY_TYPE_JAVA_CLASS_NAME, String.class.getName());
  }
  
  

  /**
   * Sets the specified attribute with the specified value. This method
   * validates that KEY_TYPE_JAVA_CLASS_NAME is a String and that KEY_FORMATTER
   * is a Format instance.
   * 
   * @param attrID
   *          [in] The attribute ID.
   * @param value
   *          [in] The attribute value.
   * 
   * @throws IllegalArgumentException
   *           if the attribute value is invalid.
   */
  protected void put(String attrID, Object value)
  {
    // Validate KEY_TYPE_JAVA_CLASS_NAME - must be a String
    if (attrID.equals(KEY_TYPE_JAVA_CLASS_NAME))
    {
      if (value == null)
      {
        throw new IllegalArgumentException("'" + KEY_TYPE_JAVA_CLASS_NAME
            + "' attribute must not be null.");
      }
      if (!(value instanceof String))
      {
        throw new IllegalArgumentException("'" + KEY_TYPE_JAVA_CLASS_NAME
            + "' attribute must be a String (class name).");
      }
      String className = (String) value;
      if (className.length() == 0)
      {
        throw new IllegalArgumentException("'" + KEY_TYPE_JAVA_CLASS_NAME
            + "' attribute must not be empty.");
      }
      // Optionally verify the class exists (commented out for performance)
      try
      {
        Class.forName(className);
      } catch (ClassNotFoundException e)
      {
        throw new IllegalArgumentException("Class '" + className + "' not found.");
      }
    }

    // Validate KEY_FORMATTER - must be a Format instance or null
    if (attrID.equals(KEY_FORMATTER))
    {
      if (value != null && !(value instanceof Format))
      {
        throw new IllegalArgumentException("'" + KEY_FORMATTER
            + "' attribute must be a Format instance or null.");
      }
    }

    super.put(attrID, value);
  }

}
