package com.optimasc.utils;


/** Represents a class definition that may contain attributes.
 * 
 * @author Carl Eric Codere
 *
 */
public interface ClassDefinition extends Definition
{
  /**
   * Key for the name of this class. This is equivalent to LDAP-NAME in ITU-T
   * X.501. This value is the actual short name of an object class. This is the
   * same definition as the <code>upnp:class</code> property in the
   * <code>ContentDirectory:v4</code> UPnP specification.
   */
//  public static final String KEY_NAME = Definition.KEY_NAME;
  /** Type of class enumeration */
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
  public static final String KEY_OPTIONALS_ATTRIBS = "MAY";
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
   *  an abstract class.
   * 
   */
  public boolean isAbstract();
  
  
}
