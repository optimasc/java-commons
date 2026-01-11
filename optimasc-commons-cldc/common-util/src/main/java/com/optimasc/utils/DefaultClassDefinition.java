package com.optimasc.utils;


public class DefaultClassDefinition extends DefaultDefinition implements ClassDefinition
{
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
   * @param classKind [in] Indicates the type of class, usually {@link ClassDefinition#CLASS_ABSTRACT}
   *      {@link ClassDefinition#CLASS_STRUCTURAL}. In case, this is <code>null</code>
   *      it defaults to {@link ClassDefinition#CLASS_STRUCTURAL}.
   * @param description
   *          [in] The user-friendly description of this class. This
   *          may be null.         
   */
  public DefaultClassDefinition(String parentClass, String id, 
      String namespaceURI, String qualifiedName, String[] allowedChildren,
      String[] mandatoryAttribs, String classKind, String description)
  {
    super(id,namespaceURI, qualifiedName,description);
    put(KEY_ALLOWED_CHILDREN,allowedChildren);
    put(KEY_SUPERCLASS,parentClass);
    put(KEY_MANDATORY_ATTRIBS,mandatoryAttribs);
    if (classKind == null)
    {
      put(KEY_KIND,CLASS_STRUCTURAL);
    } else
    {
      put(KEY_KIND,classKind);
    }
    put(KEY_RESTRICTED,Boolean.FALSE);
  }
  
  
  public DefaultClassDefinition(String namespaceURI, String qualifiedName)
  {
    this(null, null, namespaceURI, qualifiedName, 
        null, null,CLASS_STRUCTURAL,null);
  }
  
  
  public String[] getMandatoryAttributes()
  {
    String name[] = (String[]) get(KEY_MANDATORY_ATTRIBS, String[].class);
    return name;
  }

  public String[] getAllowedChildren()
  {
    String name[] = (String[]) get(KEY_ALLOWED_CHILDREN, String[].class);
    return name;
  }

  public String getParent()
  {
    return (String)get(KEY_SUPERCLASS,String.class);
  }


  public boolean isAbstract()
  {
    String s = (String) get(KEY_KIND,String.class);
    if (s.equalsIgnoreCase(CLASS_ABSTRACT))
    {
      return true;
    }
    return false;
  }


}
