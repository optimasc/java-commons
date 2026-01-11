package com.optimasc.utils;

/** Represents a definition for a named object.
 *   
 *  <p>A definition consists of  several attributes: a name
 *  that identifies, a potential unique identifier as well 
 *  as an optional description. Unless otherwise noted in the 
 *  key definitions, all attributes are represented as 
 *  {@link java.lang.String}.
 *  </p>
 *  
 *  <p>A named object can represent anything, such 
 *  as a type definition, an directory entry in ITU-T X.520,
 *  a definition or schema or an actual literal definition</p>
 *  
 *  <p>Among others, this class may be used to represent a 
 *    <code>SyntaxDescription</code> in LDAP (IETF RFC 4512)</p>
 *  
 *  <p>The following attributes are associated with a definition:</p>
 *  
 *  <table>
 *    <tr><th>Attribute name</th><th>Mandatory</th><th>Type</th><th>Description</th></tr>
 *    <tr><td>{@link #KEY_DESC}</td><td>FALSE</td><td><code>String</code></td><td>Description associated with this item.</td></tr>
 *    <tr><td>{@link #KEY_DISPLAY_NAME}</td><td>FALSE</td><td><code>String</code></td><td>Friendly display name label associated with this item.</td></tr>
 *    <tr><td>{@link #KEY_ID}</td><td>TRUE</td><td><code>String</code></td><td>Unique identifier, automatically assigned if not explicitly set. Must be an URI or an OBJECT IDENTIFIER if explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_NAME}</td><td>TRUE</td><td><code>String</code></td><td>Qualified Name associated with the item. Must be explicitly assigned</td></tr>
 *    <tr><td>{@link #KEY_NAME_NS_URI}</td><td>FALSE</td><td><code>String</code></td><td>Namespace URI associated with the item.</td></tr>
 *    <tr><td>{@link #KEY_OBSOLETE}</td><td>TRUE</td><td><code>Boolean</code></td><td>Indicates if this definition is obsolete or not. Set to <code>FALSE</code> if not explicitly set.</td></tr>
 *  </table>
 * 
 * @author Carl Eric Codere
 *
 */
public interface Definition extends AttributeSet
{
    /**
     * Maximum length of the name attribute in characters. This is 
     * specified in IETFC RFC 4520 - Object identifier descriptors.
     */
    public static final int NAME_MAX_LENGTH = 48;
    
    
    /**
     * Maximum length of the description attribute in characters. This is here to be
     * compatible for historical reasons, and is defined in ITU-T X.520 Upper
     * Bounds annex.
     */
    public static final int DESC_MAX_LENGTH = 1024;
    

    /**
     * Key for the name associated to this definition. This is equivalent to
     * LDAP-NAME in ITU-T X.501. This value is mandatory and represents a descriptor.
     * In case the name is namespace aware, this is the qualified name.
     */
    public static final String KEY_NAME = "NAME";
    
    
    /**
     * Key for the name namespace URI associated to this definition. 
     * This value is optional. 
     */
    public static final String KEY_NAME_NS_URI = "NAME_NS";
    
    
    /** Key for user friendly name associated with this 
     * definition. This value is optional mandatory. 
     * This is an additional definition item not specified in X.500
    */
   public static final String KEY_DISPLAY_NAME = "DISPLAY-NAME";
   
   /**
    * Key for the description associated with this definition. 
    * This is equivalent to LDAP-DESC in ITU-T X.501. 
    * This value is optional.
    */
   public static final String KEY_DESC = "DESC";
   
   
   /**
    * Key for OBSOLETE definition associated to this definition. This is equivalent to
    * OBSOLETE in IETFC RFC 4512. This value is optional and indicates if this 
    * definition is obsolete or not. The value is a {@link java.lang.Boolean}. 
    * 
    * The default value is <code>FALSE</code>.
    */
   public static final String KEY_OBSOLETE = "OBSOLETE";
   
   
   /**
    * Unique identifier associated to this definition. This is equivalent to
    * ID in ITU-T X.501. This value is normally mandatory and usually has the 
    * OBJECT IDENTIFIER format.
    */
   public static final String KEY_ID = "ID";
   
   /** The qualified name appended with this string represents
    *  the Unique Identifier if it is not explicitly set. 
    */
   static final String KEY_ID_SUFFIX = "-OID";
   
   /** Returns the description of the named item defined by this 
    *  instance,
    * 
    * @return The description of this attribute or <code>null</code>
    *   if not specified.
    */
   public String getDescription();

   /** Returns true if this named object definition
    *  is obsolete and should no longer be used.
    *  
    * @return true if this definition is no longer used.
    */
   public boolean isObsolete();
   
   
   /** Returns the local part of the named object.
    * 
    * @return A non-null value for this 
    *  named object.
    */
   public String getLocalName();

   /** Returns the namespace URI of this
    *  named object. 
    * 
    * @return A potentially <code>null</code> value
    *  of the namespace URI assocaited with
    *  this name.
    */
   public String getNamespaceURI();
   

   /** Returns the qualified name of this named
    *  object.
    * 
    * @return A non-null value for this 
    *  named object.
    */
   public String getName();
   
   /** Returns the expanded name of this named
    *  object. The expanded name syntax uses the RDF 
    *  expanded name definition, i,e: determined by appending 
    *  the local name part  of the qualified name after the 
    *  namespace name (URI reference)  part of the QName
    * 
    * @return A non-null value for this 
    *  named object.
    */
   public String getExpandedName();
   
   
   /** Returns the unique identifier associated with this definition.
    *  This is usually an OBJECT IDENTIFIER.
    * 
    * @return A non-null value.
    */
   public String getID();   
   
   
}
