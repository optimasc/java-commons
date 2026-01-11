package com.optimasc.utils;


/** Represents the schema of a class that may
 *  contains named items. A class could represent
 *  a simple LDAP class, an structured document, where
 *  each element is represented as an attribute,
 *  or a SQL table definition where each named item
 *  is a column definition.
 *  
 *  <p>The following attributes are associated with a class, on top of
 *  those which are inherited:</p>
 *  
 *  <table>
 *    <tr><th>Attribute name</th><th>Mandatory</th><th>Type</th><th>Description</th></tr>
 *    <tr><td>{@link #KEY_KIND}</td><td>TRUE</td><td><code>String</code></td><td>Indicates the type of class this represents, either abstract or concrete ("structural"). By 
 *       default the value is {@link #CLASS_STRUCTURAL} if this is not explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_MANDATORY_ATTRIBS}</td><td>FALSE</td><td><code>String[]</code></td><td>Represents the mandatory named items (attributes, properties, columns) associated with this class. By default the value is <code>null</code></td></tr>
 *    <tr><td>{@link #KEY_OPTIONAL_ATTRIBS}</td><td>FALSE</td><td><code>String[]</code></td><td>Represents the optional named items (attributes, properties, columns) associated with this class. By default the value is <code>null</code></td></td></tr>
 *    <tr><td>{@link #KEY_RESTRICTED}</td><td>TRUE</td><td><code>Boolean</code></td><td>Indicates if the instances of this class cannot be directly modified by user applications.
 *      By default, this value is <code>FALSE</code> if not explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_SUPERCLASS}</td><td>FALSE</td><td><code>String</code></td><td>
 *       Represents the super class name, or <code>null</code> if it is not defined.</td></tr>
 *  </table>
 *  
 *  
 * @author Carl Eric Codere
 *
 */
public interface ClassDefinition extends Definition
{
  /**
   * Key for the type of this class. This is equivalent to KIND in
   * ITU-T X.501. This value is mandatory. Internally it can have
   * the value {@link ClassDefinition#CLASS_STRUCTURAL} or 
   * {@link ClassDefinition#CLASS_ABSTRACT}.
   */
  public static final String KEY_KIND = "KIND";

  /**
   * Key for the parent of this class. This is equivalent to SUBCLASS OF in
   * ITU-T X.501. This value is optional.
   */
  public static final String KEY_SUPERCLASS = "SUP";
  /**
   * Key for the mandatory attributes of this object. This is equivalent to MUST
   * CONTAIN in ITU-T X.501. This value is optional.
   */
  public static final String KEY_MANDATORY_ATTRIBS = "MUST";
  /**
   * Key for the optional attributes of this object. This is equivalent to MAY
   * CONTAIN in ITU-T X.501. This value is optional.
   */
  public static final String KEY_OPTIONAL_ATTRIBS = "MAY";
  /**
   * Key for an array representing the allowed children nodes of this class or
   * <code>null</code> if this objectClass does not allow any children. This is
   * an extension to the X.500 and LDAP standards. This is the same definition
   * as the <code>upnp:createClass</code> property in the
   * <code>ContentDirectory:v4</code> UPnP specification.
   */
  public static final String KEY_ALLOWED_CHILDREN = "X-createClass";
  /**
   * Key for indicating if this is a restricted object. Restricted objects
   * cannot be modified by user applications, only by the system itself. This is
   * the same definition as the <code>@restricted</code> property in the
   * <code>ContentDirectory:v4</code> UPnP specification.
   * 
   */
  public static final String KEY_RESTRICTED = "restricted";
  
  
  /** Standard class kind. Default class definition */
  public static final String CLASS_STRUCTURAL = "structural";
  /** Abstract class kind. A class that cannot directly
   *  be instantiated */
  public static final String CLASS_ABSTRACT = "abstract";
  
  
  /**
   * Return mandatory attributes required for this class. It 
   * does not return the mandatory attributes defined in 
   * the parent hierarchy of this class. In the case
   * there are no mandatory attributes, this shall return
   * an empty array.
   * 
   * @throws NamingException
   */
  public String[] getMandatoryAttributes();
  
  /** Returns the class names that are allowed to be children of this
   *  specified class.  It does not return the children defined
   *  in the parent hierarchy of this class. In the case
   *  there are no allowed children, this shall return
   *  an empty array.
   * 
   * @return The list of class names allowed that can become 
   *   subcontexts of this context.
   * @throws NamingException
   */
  public String[] getAllowedChildren();
  
  /** Return the parent class name of this class.
   * 
   * @return The parent class of this class or <code>null</code>
   *  if this class has no parent.
   */
  public String getParent();
  
  /** Return true if this class definitions represents
   *  an abstract class. This is done by verifying
   *  that {@link #KEY_KIND} is equal to {@link #CLASS_ABSTRACT}.
   * 
   */
  public boolean isAbstract();
  
  
}
