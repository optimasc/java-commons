package com.optimasc.definitions;

import com.optimasc.definitions.SchemaUtilities.DefaultAttributeValue;


public class DefaultClassDefinition extends DefaultDefinition implements ClassDefinition
{
  protected static final String[] EMPTY_STRING_ARRAY = new String[0];

  /** Default values for the class definition if not explicitly set */
  public static DefaultAttributeValue[] DEFAULT_VALUES =
  {
    new DefaultAttributeValue(KEY_MANDATORY_ATTRIBS,EMPTY_STRING_ARRAY),
    new DefaultAttributeValue(KEY_OPTIONAL_ATTRIBS,EMPTY_STRING_ARRAY),
    new DefaultAttributeValue(KEY_KIND,ObjectClassKind.structuralClass),
    new DefaultAttributeValue(KEY_RESTRICTED,Boolean.FALSE),
  };
  
  /** Creates a class definition. 
   * 
   * @param parentClass
   *          [in] The qualified name of this parent class, or <code>null</code>
   *          if there is no parent class.
   * @param id 
   *          [in] The unique identifier associated with this class, or <code>null</code>
   *          if this is not known. This is usually an OBJECT IDENTIFIER.
   * @param namespaceURI
   *          [in] The namespace URI of this class name. If this class name does not
   *          have any namespace URI, this value should be <code>null</code>.
   * @param qualifiedName
   *          [in] The qualified name of this class. This value is mandatory.
   * @param allowedChildren
   *          [in] The qualified name of the allowed children of this class in
   *          the hierarchy. If this value is <code>null</code>, then
   *          any children are allowed. If this value is an empty string array
   *          no children are allowed.
   * @param mandatoryAttribs
   *          [in] The mandatory attributes required for this object, this may
   *          be null if there are no mandatory attributes. 
   * @param classKind [in] Indicates the type of class, usually the value 
   *      {@link ObjectClassKind#abstractClass#toString()} or
   *      {@link ObjectClassKind#structuralClass#toString()}. In case, this is <code>null</code>
   *      it defaults to {@link ObjectClassKind#structuralClass}.
   * @param javaClassName [in] The java class name used to instantiate
   *      this class definition, or <code>null<code> if not specified.      
   * @param description
   *          [in] The user-friendly description of this class. This
   *          may be null.         
   */
  public DefaultClassDefinition(String parentClass, String id, 
      String namespaceURI, String qualifiedName, String[] allowedChildren,
      String[] mandatoryAttribs, String classKind, String javaClassName, String description)
  {
    super(id,namespaceURI, qualifiedName,description);
    // Set default values
    for (int i=0; i < DEFAULT_VALUES.length; i++)
    {
      put(DEFAULT_VALUES[i].id,DEFAULT_VALUES[i].value);
    }
    if (allowedChildren != null)
    {
      put(KEY_ALLOWED_PARENTS,allowedChildren);
    }
    if (parentClass != null)
    {
      put(KEY_SUPERCLASS,parentClass);
    }
    if (mandatoryAttribs != null)
    {
      put(KEY_MANDATORY_ATTRIBS,mandatoryAttribs);
    }
    if (classKind != null)
    {
      put(KEY_KIND,ObjectClassKind.valueOf(classKind));
    }
    if (javaClassName != null)
    {
      put(KEY_TYPE_JAVA_CLASS_NAME,javaClassName);
    }
  }
  
  
  public DefaultClassDefinition(String namespaceURI, String qualifiedName)
  {
    this(null, null, namespaceURI, qualifiedName, 
        null, null,ObjectClassKind.structuralClass.toString(),null,null);
  }
  
  
  public String[] getMandatoryAttributes()
  {
    String name[] = (String[]) get(KEY_MANDATORY_ATTRIBS, String[].class);
    return name;
  }

  public String[] getAllowedParents()
  {
    String name[] = (String[]) get(KEY_ALLOWED_PARENTS, String[].class);
    return name;
  }

  public String getParent()
  {
    return (String)get(KEY_SUPERCLASS,String.class);
  }


  public boolean isAbstract()
  {
    ObjectClassKind kind = (ObjectClassKind) get(KEY_KIND,ObjectClassKind.class);
    if (kind == ObjectClassKind.abstractClass)
    {
      return true;
    }
    return false;
  }


  public Class getClz()
  {
    String className = (String) get(ClassDefinition.KEY_TYPE_JAVA_CLASS_NAME, String.class);
    if (className != null)
      try
      {
        return Class.forName(className);
      } catch (ClassNotFoundException e)
      {
        return null;
      }
    return null;
  }


  protected void put(String attrID, Object value)
  {
    if (attrID.equals(KEY_KIND))
    {
      if (value instanceof String)
      {
        attributes.put(KEY_KIND,ObjectClassKind.valueOf((String)value));
      } else
      if (value instanceof ObjectClassKind)
      {
        attributes.put(KEY_KIND,value);
      } else
      {
        throw new IllegalArgumentException("'"+KEY_KIND+"' must of be type '"+String.class.getName()+"' or '"+ObjectClassKind.class.getName()+"'.");
      }
      return;
    }
    super.put(attrID, value);
  }

  
  
}
