package com.optimasc.utils;

import com.optimasc.xml.QualifiedName;

public class DefaultClassDefinition extends AbstractAttributeSet implements ClassDefinition
{
  /** Creates a class definition. 
   * 
   * @param parentClass
   *          [in] The qualified name of this parent class, or <code>null</code>
   *          if there is no parent class.
   * @param oid 
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
   * @param abstractClass [in] Indicates if this class type is abstract 
   *          or not.
   * @param description
   *          [in] The user-friendly description of this class. This
   *          may be null.         
   */
  public DefaultClassDefinition(String parentClass, String oid, 
      String qualifiedName, String[] allowedChildren,
      String[] mandatoryAttribs, boolean abstractClass, String description)
  {
    if ((qualifiedName == null)
        || ((qualifiedName != null) && (qualifiedName.length() == 0)))
    {
      throw new IllegalArgumentException("Invalid name of attribute.");
    }
    if (qualifiedName.length() > NAME_MAX_LENGTH)
    {
      throw new IllegalArgumentException("Name of attribute is more than "
          + Integer.toString(NAME_MAX_LENGTH) + " characters.");
    }

    if ((description != null) && (description.length() > DESC_MAX_LENGTH))
    {
      throw new IllegalArgumentException("Description of attribute is more than "
          + Integer.toString(DESC_MAX_LENGTH) + " characters.");
    }
    
    if (oid != null)
    {
      DefaultItemDefinition.validateObjectIdentifier(oid,false);
      attributes.put(KEY_ID, oid);
    }
    

    attributes.put(KEY_NAME, qualifiedName);
    if (description != null)
      attributes.put(KEY_DESC, description);


    if (allowedChildren != null)
    {
      attributes.put(KEY_ALLOWED_CHILDREN,allowedChildren);
    }
    
    if (parentClass != null)
    {
      attributes.put(KEY_SUPERCLASS,parentClass);
    }
    // Add the mandatory attributes
    if (mandatoryAttribs != null)
    {
      attributes.put(KEY_MANDATORY_ATTRIBS,mandatoryAttribs);
    }
    if (abstractClass)
    {
      attributes.put(KEY_KIND,"abstract");
    } else
    {
      attributes.put(KEY_KIND,"structural");
    }
    attributes.put(KEY_OBSOLETE,Boolean.FALSE);
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



  public String getDescription()
  {
    String value = (String) get(KEY_DESC, String.class);
    return value;
  }

  public boolean isObsolete()
  {
    Boolean value = (Boolean) get(KEY_OBSOLETE, Boolean.class);
    if (value == null)
      return false;
    return value.booleanValue();
  }

  public String getLocalName()
  {
    String value = QualifiedName.getLocalPart((String) get(KEY_NAME, String.class));
    return value;
  }

  public String getNamespaceURI()
  {
    String value = (String) get(KEY_NAME_NS_URI, String.class);
    return value;
  }

  public String getNodeName()
  {
    String value = (String) get(KEY_NAME, String.class);
    return value;
  }

  public String getFriendlyName()
  {
    String name = (String) get(KEY_DISPLAY_NAME, String.class);
    return name;
  }

  /**
   * {@inheritDoc}
   * 
   */
  public String getExpandedName()
  {
    String namespaceURI = getNamespaceURI();
    String localName = getLocalName();
    if (namespaceURI == null)
    {
      return localName;
    }
    return namespaceURI + localName;
  }

  /**
   * Returns the expanded name of this metadata term, as returned by
   * {@link #getExpandedName() }.
   *
   * @return The expanded name of this metadata term.
   */
  public String toString()
  {
    return getExpandedName();
  }

  public String getID()
  {
    String value = (String) get(KEY_ID, String.class);
    return value;
  }
  

  public boolean isAbstract()
  {
    String s = (String) get(KEY_KIND,String.class);
    if (s.equalsIgnoreCase("abstract"))
    {
      return true;
    }
    return false;
  }


}
