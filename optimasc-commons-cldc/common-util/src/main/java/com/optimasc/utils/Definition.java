package com.optimasc.utils;

/** Represents a definition for a named object. 
 *  Each named object definition is represented
 *  by its name and one or more attributes 
 *  describing the name object. 
 * 
 * @author Carl Eric Codere
 *
 */
public interface Definition extends AttributeSet
{
    /**
     * Maximum length of the descriptor/name in characters. This is 
     * specified in IETFC RFC 4520 - Object identifier descriptors.
     */
    public static final int NAME_MAX_LENGTH = 48;
    
    
    /**
     * Maximum length of the attribute description in characters. This is here to be
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
    
    
    /** Key to get the user friendly name of this attribute. This value is
    * mandatory. This is an additional definition item not specified in X.500
    */
   public static final String KEY_DISPLAY_NAME = "DISPLAY-NAME";
   
   /**
    * Key for the description associated with this item definition. 
    * This is equivalent to LDAP-DESC in ITU-T X.501. This value is optional.
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
    * Unique IDENTIFIER associated to this definition. This is equivalent to
    * ID in ITU-T X.501. This value is normally mandatory and usually has the 
    * OBJECT IDENTIFIER format but any type of string value is accepted.
    */
   public static final String KEY_ID = "ID";
   
   /** Returns the description of the named item defined by this 
    *  instance,
    * 
    * @return The description of this attribute or <code>null</code>
    *   if not specified.
    */
   public String getDescription();

   /** Returns true if this name associated or Object identifier
    *  associated with this definition is no longer used.
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
    * @return A potentially null value
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
   public String getNodeName();
   
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
   
   
   /** Returns the friendly name for user
    *  display associated with this name.
    * 
    * @return A non-null value for this 
    *  named object.
    */
   public String getFriendlyName();
   
   /** Returns the unique identifier
    *  associated with this definition.
    * 
    * @return A potentiallt null
    */
   public String getID();   
   
   
}
