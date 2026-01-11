package com.optimasc.utils;

/** Represents the schema of a named item. A named item
 *  is any object, attribute, property or element that is named and
 *  is associated with a specific data type.
 *  
 *  <p>Among others, this class may be used to represent an 
 *    <code>AttributeTypeDescription</code> in LDAP (IETF RFC 4512)</p>
 *    
 *  <p>The following attributes are associated with a definition, on top of
 *  those which are inherited:</p>
 *  
 *  <table>
 *    <tr><th>Attribute name</th><th>Mandatory</th><th>Type</th><th>Description</th></tr>
 *    <tr><td>{@link #KEY_MAX_LENGTH}</td><td>FALSE</td><td><code>Long</code></td><td>Represents the maximum length of characters or octets of data associated 
 *      with the value associated with this named item.</td></tr>
 *    <tr><td>{@link #KEY_ORDERED}</td><td>FALSE</td><td><code>Boolean</code></td><td>Indicates if multiple values are ordered or not. This value is only valid when {@link #isSingleValued()} returns <code>FALSE</code></td></tr>
 *    <tr><td>{@link #KEY_READONLY}</td><td>TRUE</td><td><code>Boolean</code></td><td>Indicates if the named item's value can be updated. By default the value is <code>FALSE</code></td></tr>
 *    <tr><td>{@link #KEY_SINGLEVALUE}</td><td>TRUE</td><td><code>Boolean</code></td><td>Indicates if there can be multiple values associated with this named item. By default the value is <code>FALSE</code></td></td></tr>
 *    <tr><td>{@link #KEY_TYPE_NAME}</td><td>FALSE</td><td><code>String</code></td><td>Represents the data type associated with the value of the named item.</td></tr>
 *    <tr><td>{@link #KEY_CONTEXT_TYPE}</td><td>FALSE</td><td><code>String</code></td><td>
 *       Represents the allowed alternative associated with the value
 *       returned by this named item.</td></tr>
 *  </table>
 *  
 *  <p>The {@link #KEY_CONTEXT_TYPE} is used to indicate that the values 
 *  returned by the named items are actual alternatives based on
 *  some criteria. This is similar to contexts in ITU-T X.501, LangAlt
 *  in XMP, or LDAP Language tags. More concretely, there could be
 *  several attribute text values, with each value being in a different
 *  language, the context being used to identify the language of the value.
 *  This is similar to an associative array or map where the context value (key)
 *  selects the appropriate value. 
 *  </p>
 *  
 *  <p>The following context types are pre-defined, even though 
 *  actual allowed values are not defined and must be done
 *  by the  </p>
 *  
 *  <dl>
 *    <dt>CONTEXT_LANG</dt>
 *    <dd>Language alternative - either an ISO 639-1 or ISO 639-2 code</dd>
 *    <dt>CONTEXT_LOCATION</dt>
 *    <dd>Location alternative - allowed values : Home, Office, Other (case insensitive)</dd>
 *  </dl>
 *    
 *  
 * @author Carl Eric Codere
 *
 */
public interface ItemDefinition extends Definition
{
  /**
   * Key to indicate if this named item's value can be modified by the user or not. 
   * By default, this value is FALSE.
   */
  public static final String KEY_READONLY = "NO-USER-MODIFICATION";
  /**
   * Key to indicates if this named item's value is single-valued or not. 
   * By default this value is FALSE
   */
  public static final String KEY_SINGLEVALUE = "SINGLE-VALUE";

  /**
   * Key to indicate the maximum length of the named item's value. This
   * value is not present by default, and when present it is represented
   * as an <code>Integer</code> object.
   */
  public static final String KEY_MAX_LENGTH = "X-MAX-VALUE-LENGTH";
  
  /**
   * Key to indicate if this named items values are ordered or not. This 
   * value is <code>Boolean</code> by default. If there is a single
   * value allowed this is necessarily to <code>TRUE</code>. In the 
   * case of contexts, this should be <code>FALSE</code>.
   */
  public static final String KEY_ORDERED = "X-ORDERED";
  
  /**
   * Key to get the string representation syntax or type 
   * name associated with the value of this named item. This value
   * is usually an OBJECT IDENTIFIER but this implementation
   * allows other string values. This value 
   * is optional and is equivalent to LDAP-SYNTAX in X.501 and
   * represents the type of the attribute.
   */
  public static final String KEY_TYPE_NAME = "SYNTAX";
  
  
  /**
   * Key to indicate the allowed context, or actual alternatives
   * of the values that may be returned by the value associated
   * with this named item. This option is optional, and 
   * is only valid when {@link #KEY_SINGLEVALUE} is false. 
   *    
   * name associated with the value of this named item. This value
   * is usually an OBJECT IDENTIFIER but this implementation
   * allows other string values. This value 
   * is optional and is equivalent to LDAP-SYNTAX in X.501 and
   * represents the type of the attribute.
   */
  public static final String KEY_CONTEXT_TYPE = "CONTEXT";
  
  /** Context type: Location alternative between home, office
   *  and other location. 
   */
  public static final String CONTEXT_LOCATION = "LocationAlt";
  
  /** Context type: Language alternative using ISO 639-1 
   *  or ISO 639-2 code.
   */
  public static final String CONTEXT_LANGUAGE = "LangAlt";
  
  /** Returns the syntax type declared for the associated element, object 
   *  or attribute, or <code>null</code> if unknown.
   *   
   * @return
   */
  public String getTypeName();
  
  /** Returns the allowed contexts or keys associated with 
   *  each value of the represenation of this named item. This
   *  value is optional.
   *   
   * @return The context type, or <code>null</code> if 
   *   there is no context associated with the values returned
   *   by the representation of this named item.
   */
  public String getContextType();
  
  
  
  /** Returns true if this object definition instance
   *  can have only one value or more than
   *  one value. If this returns false, then
   *  this is considered a list of values
   * 
   * @return true if this attribute can 
   *   have one value, otherwise false.
   */
  public boolean isSingleValued();
  
  /** Indicates if the attribute represented by this definition
   *  can be modified by a standard user, or if its a read-only
   *  attribute.
   * 
   * @return true if attribute is read-only otherwise 
   *   false if it can be modified by the user.
   */
  public boolean isReadOnly();
  
  /** Indicates whether attribute's values represented 
   *  by this definition are ordered. 
   *  If an attribute's values are ordered, duplicate values are allowed. If an attribute's values are unordered, they are presented in any order and there are no duplicate values.  
   * 
   */
  public boolean isOrdered();

}
