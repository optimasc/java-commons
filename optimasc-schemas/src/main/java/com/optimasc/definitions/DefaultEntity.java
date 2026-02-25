package com.optimasc.definitions;

import com.optimasc.utils.AbstractAttributeSet;
import com.optimasc.utils.BaseTypeUtilities;
import com.optimasc.xml.QualifiedName;

/** Default implementation for a named object.
 */
public class DefaultEntity extends AbstractAttributeSet implements Entity
{
  /** Constructs an entity entry with a basic name.
   * 
   * The following are the defaults:
   * 
   * <ul>
   * <li>{@link Definition#KEY_ID}: Equal to the qualifiedName parameter
   *  with an "-OID" suffix.</li>
   * </ul>
   * 
   * @param namespaceURI [in] The namespace URI of this named object, can be <code>null</code>
   * @param qualifiedName [in] A non-null qualified name of the object.
   * @throws IllegalArgumentException In case the qualifiedName
   *   is not a valid name. 
   */
  public DefaultEntity(String namespaceURI, String qualifiedName)
  {
    super();
    put(KEY_NAME,qualifiedName);
    put(KEY_NAME_NS_URI,namespaceURI);
    put(KEY_ID,qualifiedName+KEY_ID_SUFFIX);
  }
  
  /** Constructs an entity entry with a basic name and an identifier.
   * 
   * @param id [in] The non-null unique identifier associated with this named
   *   entity.
   * @param namespaceURI [in] The namespace URI of this named object, can be <code>null</code>
   * @param qualifiedName [in] A non-null qualified name of the object.
   * @throws IllegalArgumentException In case the qualifiedName
   *   is not a valid name or if <code>id</code> is not a valid
   *   OBJECT IDENTIFIER. 
   */
  public DefaultEntity(java.lang.String id, String namespaceURI, String qualifiedName)
  {
    this(namespaceURI, qualifiedName);
    put(KEY_ID,id);
  }
  
  
  /** {@inheritDoc} */
  public Object get(String attributeName, Class expectedClass)
  {
    return super.get(attributeName, expectedClass);
  }

  /** {@inheritDoc} */
  public String[] getKeys()
  {
    return super.getKeys();
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

  public String getName()
  {
    String value = (String) get(KEY_NAME, String.class);
    return value;
  }
  

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

  public String getID()
  {
    String value = (String) get(KEY_ID, String.class);
    return value;
  }
  
  
  public String getObjectClass()
  {
    String value = (String) get(KEY_OBJECT_CLASS_NAME, String.class);
    return value;
  }
  
  
  /** Returns all attributes of this item. */
  public String toString()
  {
    return attributes.toString();
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
    
    if (attrID.equals(KEY_ID))
    {
      String id = (String)value;
      if ((id != null) && (id.length() == 0))
      {
        throw new IllegalArgumentException("Invalid ID of attribute.");
      }
      if (id != null)
      {
        attributes.put(KEY_ID, id);
      }
      return;
    }
    
    /* None of the above, then simply add it if its non null */
    if (value != null)
      attributes.put(attrID,value);
  }

  /** Two entities are equal if they point to 
   *  same object or their ID and expandedName
   *  are equal. 
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if ((obj instanceof Entity)==false)
    {
      return false;
    }
    Entity otherEntity = (Entity) obj;
    if (getID().equalsIgnoreCase(otherEntity.getID())==false)
    {
       return false;  
    }
    if (getExpandedName().equals(otherEntity.getExpandedName())==false)
    {
       return false;  
    }
    return true;
  }

  
  
}
