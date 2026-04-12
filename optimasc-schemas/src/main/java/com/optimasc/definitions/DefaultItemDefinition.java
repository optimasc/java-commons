package com.optimasc.definitions;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.optimasc.text.KeyValueHelper;
import com.optimasc.utils.BaseTypeUtilities;

/**
 * Default implementation for a named item definition. A named item definition
 * can represent an attribute, property, a SQL column definition, or an XML
 * element definition among others. Item definitions are usually associated with
 * a datatype.
 */
public class DefaultItemDefinition extends DefaultDefinition implements ItemDefinition
{

/**
   * Creates a named item definition with a specific OBJECT IDENTIFIER for <code>KEY_ID</code>. 
   * 
   * @param id
   *          [in] The unique identifier for this attribute. This is  
   *          an OBJECT IDENTIFIER or <code>null</code>.
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
   *          When this value is an Object Identifier that points to a type, it supports
   *          specifying the maximum length within braces at the end of the OBJECT IDENTIFIER
   *          such as 1.3.4{32} where the value in braces the maximum number of characters
   *          or octets for sequence types.
   * @param multiValueType
   *          [in] Indicates if this named item can contain a single value or
   *          more than one value, and if more than one value how it is ordered.
   * @param contextType 
   *          [in] The possible context type associated with the values. This
   *          can be <code>null</code>, but if non-null but only if 
   *          <code>multiValueType</code> is equal to {@link ItemDefinition# 
   * @param readOnly
   *          [in] Indicates if this named can be modified by user applications
   *          or only by the system.
   */
  public DefaultItemDefinition(String id, String namespaceURI, String qualifiedName,
      String description, String typeName, String multipleValueType, String contextType,
      boolean readOnly)
  {
    super(id, namespaceURI, qualifiedName, description);
    put(KEY_TYPE_NAME, typeName);
    if (multipleValueType.equals(VALUE_TYPE_SINGLE))
    {
      put(KEY_VALUE_TYPE, multipleValueType);
    }
    else if (multipleValueType.equals(VALUE_TYPE_ALT))
    {
      put(KEY_VALUE_TYPE, multipleValueType);
      if (contextType == null)
      {
        throw new IllegalArgumentException(
            "Context must be specified when the value is of type '" + VALUE_TYPE_ALT
                + "'.");
      }
      put(KEY_CONTEXT_TYPE, contextType);
    }
    else if (multipleValueType.equals(VALUE_TYPE_BAG))
    {
      put(KEY_VALUE_TYPE, multipleValueType);
      if (contextType != null)
      {
        throw new IllegalArgumentException(
            "Context must NOT be specified when the value is of type '" + VALUE_TYPE_BAG
                + "'.");
      }
    }
    else if (multipleValueType.equals(VALUE_TYPE_SEQ))
    {
      put(KEY_VALUE_TYPE, multipleValueType);
      if (contextType != null)
      {
        throw new IllegalArgumentException(
            "Context must NOT be specified when the value is of type '" + VALUE_TYPE_SEQ
                + "'.");
      }
    }
    else
    {
      throw new IllegalArgumentException("'multiTypeValue' is not valid.");
    }

    put(KEY_READONLY, new Boolean(readOnly));
  }

  /**
   * Creates a named item definition with only required values. The other values
   * are assigned default values.
   * 
   * @param qualifiedName
   *          [in] The qualified name of this of this named item. This is
   *          similar to to the descriptor in the LDAP specification and is
   *          mandatory.
   */
  public DefaultItemDefinition(String namespaceURI, String qualifiedName)
  {
    super(namespaceURI, qualifiedName);
    put(KEY_VALUE_TYPE, VALUE_TYPE_BAG);
    put(KEY_READONLY, Boolean.FALSE);
  }

  /**
   * Returns true if this attribute instance can have one or more values.
   * 
   * @return true if this attribute can have more than one value, otherwise
   *         false.
   */
  public boolean isSingleValued()
  {
    String value = (String) get(KEY_VALUE_TYPE, String.class);
    if (value.equals(VALUE_TYPE_SINGLE))
      return true;
    return false;
  }

  public boolean isOrdered()
  {
    String value = (String) get(KEY_VALUE_TYPE, String.class);
    if (value.equals(VALUE_TYPE_SEQ))
      return true;
    return false;
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
  
  public String getContextType()
  {
    String value = (String) get(KEY_CONTEXT_TYPE, String.class);
    return value;
  }

  public String getValueType()
  {
    String value = (String) get(KEY_VALUE_TYPE, String.class);
    return value;
  }

  public Object getValueForContext(String attrID, String context)
  {
    if (attrID == null || context == null)
    {
      return null;
    }

    // Get the attribute value
    Object value = get(attrID, Object.class);
    if (value == null)
    {
      return null;
    }

    // Convert to array if needed
    Object[] altValues = null;
    if (value instanceof Object[])
    {
      altValues = (Object[]) value;
    }
    else if (value instanceof List)
    {
      altValues = ((List) value).toArray();
    }
    else if (value instanceof String[])
    {
      String[] strArray = (String[]) value;
      altValues = new Object[strArray.length];
      System.arraycopy(strArray, 0, altValues, 0, strArray.length);
    }
    else
    {
      return null;
    }

    // Use helper to find the value
    return KeyValueHelper.getValueForContext(altValues, context);
  }

  public Map getContextMap(String attrID)
  {
    if (attrID == null)
    {
      return null;
    }

    // Get the attribute value
    Object value = get(attrID, Object.class);
    if (value == null)
    {
      return null;
    }

    // Verify this is an ALT type attribute
    // (You'll need access to the schema/item definition here)
    // For now, we'll assume any array could be ALT values

    // Convert to array if needed
    Object[] altValues = null;
    if (value instanceof Object[])
    {
      altValues = (Object[]) value;
    }
    if (value instanceof List)
    {
      altValues = ((List) value).toArray();
    }
    
    else if (value instanceof String[])
    {
      String[] strArray = (String[]) value;
      altValues = new Object[strArray.length];
      System.arraycopy(strArray, 0, altValues, 0, strArray.length);
    }
    else
    {
      return null;
    }

    // Convert to Map and cache
    Map contextMap = KeyValueHelper.toKeyValueMap(altValues);

    return Collections.unmodifiableMap(contextMap);
  }
  

  protected void put(String attrID, Object value)
  {
    /* Verify validity of attributes of this type. */
    if (attrID.equals(KEY_TYPE_NAME))
    {
      String typeName = (String) value;
      if ((typeName == null) || ((typeName != null) && (typeName.length() == 0)))
      {
        throw new IllegalArgumentException(
            "Invalid syntax, it is mandatory and class must be specified.");
      }
      try
      {
        int maxLength = BaseTypeUtilities.validateObjectIdentifier(typeName, true);
        if (maxLength != -1)
        {
          attributes.put(KEY_MAX_VALUE_LENGTH, new Long(maxLength));
        }
      } catch (IllegalArgumentException e)
      {
        // This is not an OBJECT IDENTIFIER do nothing and just save the value.
      }
      // Remove the length value from the attribute name
      int index = typeName.indexOf('{');
      if (index != -1)
        typeName = typeName.substring(0, index);
      attributes.put(KEY_TYPE_NAME, typeName);
      return;
    }

    if (attrID.equals(KEY_ID))
    {
      String oid = (String) value;
      if ((oid != null) && (oid.length() == 0))
      {
        throw new IllegalArgumentException("Invalid OID of attribute.");
      }
      if (oid != null)
      {
        attributes.put(KEY_ID, oid);
      }
      return;
    }

    if (attrID.equals(KEY_MAX_VALUE_LENGTH))
    {
      Long l = (Long) value;
      attributes.put(KEY_MAX_VALUE_LENGTH, l);
      return;
    }

    if (attrID.equals(KEY_READONLY))
    {
      Boolean b = (Boolean) value;
      attributes.put(KEY_READONLY, b);
      return;
    }
    /* Pass to parent to verify generic attributes. */
    super.put(attrID, value);
  }


}
