package com.optimasc.utils;


/** Default implementation for a named item definition. A named item 
 *  definition can represent an attribute, property, a SQL column definition, 
 *  or an XML element definition among others.
 *  Item definitions are usually associated with a datatype. 
 */
public class DefaultItemDefinition extends DefaultDefinition implements ItemDefinition
{
  /** Indicates that the named item's value contains only a single value. 
   *  Used for <code>multiValueType</code> parameter in constructor.
   */
  public static final String VALUE_TYPE_NONE = null;
  /**  Indicates that the named item's value contains a list of unordered
   *   values. Used for <code>multiValueType</code> parameter in constructor.
   *   This definition is taken from <code>RDF</code>.
   */
  public static final String VALUE_TYPE_BAG = "Bag";
  /**  Indicates that the named item's value contains a list of ordered
   *   values. Used for <code>multiValueType</code> parameter in constructor.
   *   This definition is taken from <code>RDF</code>.
   */
  public static final String VALUE_TYPE_SEQ = "Seq";
  /**  Indicates that the named item's value contains a list of alternative
   *   values. Used for <code>multiValueType</code> parameter in constructor.
   *   This definition is taken from <code>RDF</code>.
   */
  public static final String VALUE_TYPE_ALT = "Alt";
  
  
  /**
   * Creates a named item definition with a specific OBJECT IDENTIFIER for <code>KEY_ID</code>. 
   * 
   * @param id
   *          [in] The identifier for this attribute. This is an OBJECT IDENTIFIER that
   *          supports the length extension in its syntax within braces, which
   *          limits the length of the data.
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
   * @param multiValueType
   *          [in] Indicates if this named item can contain a single value or
   *          more than one value, and if more than one value how it is ordered.
   *          Internally this is converted into multiple attributes, and
   *          this is considered a convenience method.
   * @param contextType 
   *          [in] The possible context type associated with the values. This
   *          can be <code>null</code>, but if non-null but only if 
   *          <code>multiValueType</code> is equal to {@link ItemDefinition# 
   * @param readOnly
   *          [in] Indicates if this named can be modified by user applications
   *          or only by the system.
   */
  public DefaultItemDefinition(String id, String namespaceURI,
      String qualifiedName, String description, String typeName, String multipleValueType, String contextType, 
      boolean readOnly)
  {
    super(id,namespaceURI,qualifiedName,description);
    put(KEY_TYPE_NAME,typeName);
    if (multipleValueType==null)
    {
      put(KEY_SINGLEVALUE,Boolean.TRUE);
    } else
    if (multipleValueType.equals(VALUE_TYPE_ALT))
    {
      put(KEY_SINGLEVALUE,Boolean.FALSE);
      put(KEY_ORDERED,Boolean.FALSE);
      if (contextType == null)
      {
        throw new IllegalArgumentException("Context must be specified when the value is of type '"+VALUE_TYPE_ALT+"'.");
      }      
      put(KEY_CONTEXT_TYPE,contextType);
    }
    else
    if (multipleValueType.equals(VALUE_TYPE_BAG))
    {
      put(KEY_SINGLEVALUE,Boolean.FALSE);
      put(KEY_ORDERED,Boolean.FALSE);
      if (contextType != null)
      {
        throw new IllegalArgumentException("Context must NOT be specified when the value is of type '"+VALUE_TYPE_BAG+"'.");
      }
    } else
    if (multipleValueType.equals(VALUE_TYPE_SEQ))
    {
      put(KEY_SINGLEVALUE,Boolean.FALSE);
      put(KEY_ORDERED,Boolean.TRUE);
      if (contextType != null)
      {
        throw new IllegalArgumentException("Context must NOT be specified when the value is of type '"+VALUE_TYPE_SEQ+"'.");
      }
    } else
    {
      throw new IllegalArgumentException("'multiTypeValue' is not valid.");
    }
    
    put(KEY_READONLY,new Boolean(readOnly));
  }
  
  
  /**
   * Creates a named item definition with only required values. The other
   * values are assigned default values. 
   * 
   * @param qualifiedName
   *          [in] The qualified name of this of this named item. This is
   *          similar to to the descriptor in the LDAP specification and
   *          is mandatory.
   */
  public DefaultItemDefinition(String namespaceURI, String qualifiedName)
  {
    this(null,namespaceURI,qualifiedName,null,null,null,VALUE_TYPE_NONE,false);
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
  
  public boolean isOrdered()
  {
    Boolean value = (Boolean) get(KEY_ORDERED, Boolean.class);
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


  public String getTypeName()
  {
    String value = (String) get(KEY_TYPE_NAME, String.class);
    return value;
  }


  protected void put(String attrID, Object value)
  {
    /* Verify validity of attributes of this type. */
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
    
    
    if (attrID.equals(KEY_ID))
    {
      String oid = (String)value;
      if ((oid != null) && (oid.length() == 0))
      {
        throw new IllegalArgumentException("Invalid OID of attribute.");
      }
      if (oid != null)
      {
        if (oid.endsWith("-OID")==false)
        {
        int maxLength = validateObjectIdentifier(oid,true);
        if (maxLength != -1)
        {
          attributes.put(KEY_MAX_LENGTH,new Long(maxLength));
        }
        }
        attributes.put(KEY_ID, oid);
      }
      return;
    }
    
    if (attrID.equals(KEY_MAX_LENGTH))
    {
      Long l = (Long)value;
      attributes.put(KEY_MAX_LENGTH, l);
      return;
    }
    
    

    if (attrID.equals(KEY_SINGLEVALUE))
    {
      Boolean b = (Boolean)value;
      attributes.put(KEY_SINGLEVALUE, b);
      return;
    }
    
    if (attrID.equals(KEY_ORDERED))
    {
      Boolean b = (Boolean)value;
      attributes.put(KEY_ORDERED, b);
      return;
    }
    
    
    if (attrID.equals(KEY_READONLY))
    {
      Boolean b = (Boolean)value;
      attributes.put(KEY_READONLY, b);
      return;
    }
    /* Pass to parent to verify generic attributes. */
    super.put(attrID, value);
  }


  public String getContextType()
  {
    String value = (String) get(KEY_CONTEXT_TYPE, String.class);
    return value;
  }


}
