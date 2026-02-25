package com.optimasc.definitions;


public class DefaultDefinition extends DefaultEntity implements Definition
{
  /** Constructs a definition with a basic name and an OBJECT IDENTIFIER.
   * 
   * The following are the defaults:
   * 
   * <ul>
   * <li>{@link Definition#KEY_OBSOLETE}: <code>Boolean.FALSE</code></li>
   * </ul>
   * 
   * @param id [in] The non-null ASN1 OBJECT IDENTIFIER value in dotted digit notation.
   * @param namespaceURI [in] The namespace URI of this named object, can be <code>null</code>
   * @param qualifiedName [in] A non-null qualified name of the object.
   * @param description [in] A description associated with this definition, , can be <code>null</code>.
   * @throws IllegalArgumentException In case the qualifiedName
   *   is not a valid name or if <code>id</code> is not a valid
   *   OBJECT IDENTIFIER. 
   */
  public DefaultDefinition(String id, String namespaceURI, String qualifiedName,
      String description)
  {
    super(id, namespaceURI, qualifiedName);
    put(KEY_OBSOLETE,Boolean.FALSE);
    put(KEY_DESCRIPTION,description);
  }

  public DefaultDefinition(String namespaceURI, String qualifiedName)
  {
    super(namespaceURI, qualifiedName);
    put(KEY_OBSOLETE,Boolean.FALSE);
  }

  public String getDescription()
  {
    String value = (String) get(KEY_DESCRIPTION, String.class);
    return value;
  }

  public boolean isObsolete()
  {
    Boolean value = (Boolean) get(KEY_OBSOLETE, Boolean.class);
    if (value == null)
      return false;
    return value.booleanValue();
  }
  
  
  public String getOrigin()
  {
    String value = (String) get(KEY_ORIGIN, String.class);
    return value;
  }

  protected void put(String attrID, Object value)
  {
    if (attrID.equals(KEY_DESCRIPTION))
    {
      String description = (String)value;
      if ((description != null) && (description.length() > DESC_MAX_LENGTH))
      {
        throw new IllegalArgumentException("Description of attribute is more than "
            + Integer.toString(DESC_MAX_LENGTH) + " characters.");
      }
      if (description != null)
        attributes.put(KEY_DESCRIPTION, description);
      return;
    }
    super.put(attrID, value);
  }
  
  

}
