package com.optimasc.utils;

import com.optimasc.xml.QualifiedName;

public class DefaultItemDefinition extends AbstractAttributeSet implements ItemDefinition
{
  /**
   * Creates a named item definition with a specific OBJECT IDENTIFIER for <code>KEY_ID</code>. 
   * The following are the defaults:
   * 
   * <ul>
   * <li>{@link Definition#KEY_OBSOLETE}: <code>Boolean.FALSE</code></li>
   * </ul>
   * 
   * @param displayName
   *          [in] The friendly name of this item when it should be displayed to
   *          the user. This value must be non-null.
   * @param oid
   *          [in] The identifier for this attribute. This is an OBJECT IDENTIFIER.
   * @param namespaceURI
   *          [in] The namespace URI associated with this named item. This
   *          implementation accepts <code>null</code> for this value.           
   * @param qualifiedName
   *          [in] The qualified name of this of this named item. This is
   *          similar to to the descriptor in the LDAP specification and
   *          is mandatory.
   * @param description
   *          [in] The human readable description associated with this named
   *          item. This implementation accepts <code>null</code> for this value.
   * @param typeName
   *          [in] The type name or syntax which represents the type associated
   *          with this item's value. This value is mandatory and must be non-null.
   * @param singleValue
   *          [in] Indicates if this named item can contain a single value or
   *          more than one value.
   * @param readOnly
   *          [in] Indicates if this named can be modified by user applications
   *          or only by the system.
   */
  public DefaultItemDefinition(String displayName, String oid, String namespaceURI,
      String qualifiedName, String description, String typeName, boolean singleValue,
      boolean readOnly)
  {
    this(displayName,namespaceURI,qualifiedName,description,typeName,singleValue,readOnly);
    if (oid != null)
    {
      validateObjectIdentifier(oid,false);
      put(KEY_ID,oid);
    }
  }
  
  
  /**
   * Creates a named item definition with only required values.. 
   * The following are the defaults:
   * 
   * <ul>
   * <li>{@link Definition#KEY_OBSOLETE}: <code>Boolean.FALSE</code></li>
   * <li>{@link Definition#KEY_SINGLEVALUE}: <code>Boolean.FALSE</code></li>
   * <li>{@link Definition#KEY_READONLY}: <code>Boolean.FALSE</code></li>
   * <li><code>null</code> namespace</li>
   * <li><code>null</code> description</li>
   * </ul>
   * 
   * @param displayName
   *          [in] The friendly name of this item when it should be displayed to
   *          the user. This value must be non-null.
   * @param qualifiedName
   *          [in] The qualified name of this of this named item. This is
   *          similar to to the descriptor in the LDAP specification and
   *          is mandatory.
   * @param typeName
   *          [in] The type name or syntax which represents the type associated
   *          with this item's value. This value is mandatory and must be non-null.
   */
  public DefaultItemDefinition(String displayName, 
      String qualifiedName, String typeName)
  {
    this(displayName, null, qualifiedName, null, typeName, false, false);
  }
  
  
  /**
   * Creates a named item definition with an internally named <code>KEY_ID</code>. 
   * 
   * The following are the defaults:
   * 
   * <ul>
   * <li>{@link Definition#KEY_OBSOLETE}: <code>Boolean.FALSE</code></li>
   * <li>{@link Definition#KEY_ID}: Equal to the qualifiedName parameter
   *  with an "-OID" suffix.</li>
   * </ul>
   * 
   * @param displayName
   *          [in] The friendly name of this item when it should be displayed to
   *          the user. This value must be non-null.
   * @param namespaceURI
   *          [in] The namespace URI associated with this named item. This
   *          implementation accepts <code>null</code> for this value.           
   * @param qualifiedName
   *          [in] The qualified name of this of this named item. This is
   *          similar to to the descriptor in the LDAP specification and
   *          is mandatory.
   * @param description
   *          [in] The human readable description associated with this named
   *          item. This implementation accepts <code>null</code> for this value.
   * @param typeName
   *          [in] The type name or syntax which represents the type associated
   *          with this item's value. This value is mandatory and must be non-null.
   * @param singleValue
   *          [in] Indicates if this named item can contain a single value or
   *          more than one value.
   * @param readOnly
   *          [in] Indicates if this named can be modified by user applications
   *          or only by the system.
   */
  public DefaultItemDefinition(String displayName, String namespaceURI,
      String qualifiedName, String description, String typeName, boolean singleValue,
      boolean readOnly)
  {
    put(KEY_NAME,qualifiedName);
    put(KEY_DESC,description);
    put(KEY_DISPLAY_NAME,displayName);
    put(KEY_TYPE_NAME,typeName);
    put(KEY_ID, qualifiedName+"-OID");
    put(KEY_SINGLEVALUE,new Boolean(singleValue));
    put(KEY_READONLY,new Boolean(readOnly));
    put(KEY_OBSOLETE, Boolean.FALSE);
  }
  
  /** Sets the specified attribute with the specified
   *  value. This method does checking on standard
   *  attributes to verify if they are valid.
   * 
   * @param attrID [in] The attribute ID.
   * @param value [in] The attribute value.
   * 
   * @throws IllegalArgumentException if
   *   the attribute value or item is not valid.
   */
  protected void put(String attrID, Object value)
  {
    if (attrID.equals(KEY_NAME))
    {
      String qualifiedName = (String)value;
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
      attributes.put(KEY_NAME, qualifiedName);
      return;
    }
    
    if (attrID.equals(KEY_DESC))
    {
      String description = (String)value;
      if ((description != null) && (description.length() > DESC_MAX_LENGTH))
      {
        throw new IllegalArgumentException("Description of attribute is more than "
            + Integer.toString(DESC_MAX_LENGTH) + " characters.");
      }
      if (description != null)
        attributes.put(KEY_DESC, description);
      return;
    }
    
    if (attrID.equals(KEY_DISPLAY_NAME))
    {
      String displayName = (String)value;
      if ((displayName == null) || ((displayName != null) && (displayName.length() == 0)))
      {
        throw new IllegalArgumentException("Invalid displayName of attribute.");
      }
      if (displayName != null)
        attributes.put(KEY_DISPLAY_NAME, displayName);
      return;
    }    

    if (attrID.equals(KEY_TYPE_NAME))
    {
      String typeName = (String)value;
      if ((typeName == null) || ((typeName != null) && (typeName.length() == 0)))
      {
        throw new IllegalArgumentException(
            "Invalid syntax, it is mandatory and class must be specified.");
      }
      attributes.put(KEY_TYPE_NAME, typeName);
      return;
    }    

    if (attrID.equals(KEY_SINGLEVALUE))
    {
      Boolean b = (Boolean)value;
      attributes.put(KEY_SINGLEVALUE, b);
      return;
    }
    
    if (attrID.equals(KEY_READONLY))
    {
      Boolean b = (Boolean)value;
      attributes.put(KEY_READONLY, b);
      return;
    }
    
    if (attrID.equals(KEY_OBSOLETE))
    {
      Boolean b = (Boolean)value;
      attributes.put(KEY_OBSOLETE, b);
      return;
    }

    /* None of the above, then simply add it */
    put(attrID,value);
    
  }
  

  /**
   * Returns true if this attribute instance can have one or more values.
   * 
   * @return true if this attribute can have more than one value, otherwise
   *         false.
   */
  public boolean isSingleValued()
  {
    Boolean value = (Boolean) get(KEY_SINGLEVALUE, Boolean.class);
    if (value == null)
      return false;
    return value.booleanValue();
  }

  /**
   * Indicates if the attribute represented by this definition can be modified
   * by a standard user, or if its a read-only attribute.
   * 
   * @return true if attribute is read-only otherwise false if it can be
   *         modified by the user.
   */
  public boolean isReadOnly()
  {
    Boolean value = (Boolean) get(KEY_READONLY, Boolean.class);
    if (value == null)
      return false;
    return value.booleanValue();
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

  public String getTypeName()
  {
    String value = (String) get(KEY_TYPE_NAME, String.class);
    return value;
  }

  /**
   * Validates a fully numeric OBJECT IDENTIFIER syntax, and optionally checks
   * the length braces and returns the noidlen specifier if present. 
   * 
   * @param oid
   *          [in] The OBJECT IDENTIFIER value to validate.
   * @param allowLength
   *          [in] Allow length specifier in braces as defined in IETF RFC 4512.
   * @return The length of the value or -1 if there is no length.
   * @throws IllegalArgumentException
   *           If the Object identifier is invalid.
   */
  public static int validateObjectIdentifier(String oid, boolean allowLength)
  {
    final int EXPECT_DIGIT = 0;
    final int EXPECT_DOT = 1;
    final int EXPECT_END = 2;
    int nextState = EXPECT_DIGIT;
    int count = oid.length();
    int i = 0;
    StringBuffer buffer = new StringBuffer();
    while (i < count)
    {
      if (nextState == EXPECT_DIGIT)
      {
        while ((i < count) && ((oid.charAt(i) >= '0') && (oid.charAt(i) <= '9')))
        {
          i++;
          nextState = EXPECT_DOT;
        }
      }
      if (i >= count)
      {
        break;
      }
      if (allowLength == true)
      {
        if (oid.charAt(i) == '{')
        {
          if (oid.charAt(oid.length()-1)!='}')
          {
            throw new IllegalArgumentException("Illegal OBJECT IDENTIFIER syntax: Missing '}' to specify the length");
            
          }
          i++;
          while ((i < count) && (oid.charAt(i) != '}'))
          {
            buffer.append(oid.charAt(i));
            i++;
          }
          i++;
          nextState = EXPECT_END;
          if (i>=count)
              break;
        }
      }
      if ((oid.charAt(i) == '.') && (nextState == EXPECT_DOT))
      {
        i++;
        nextState = EXPECT_DIGIT;
        continue;
      }
      throw new IllegalArgumentException("Illegal OBJECT IDENTIFIER syntax.");
    }
    if (nextState == EXPECT_DIGIT)
    {
      throw new IllegalArgumentException("Illegal OBJECT IDENTIFIER syntax.");
    }
    if (buffer.length()==0)
    {
      return -1;
    }
    return Integer.parseInt(buffer.toString());
  }

}
