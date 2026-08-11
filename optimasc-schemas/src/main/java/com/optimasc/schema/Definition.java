package com.optimasc.definitions;


/** Represents a named definition. 
 * 
 *  <p>A definition consists of several attributes or properties: a name
 *  that identifies, a potential unique identifier as well 
 *  as an optional description. Unless otherwise noted in the 
 *  key definitions, all attributes are represented as 
 *  {@link java.lang.String}.
 *  </p>
 *  
 *  <p>The following attributes are associated with a definition:</p>
 *  
 *  <table>
*    <tr><td>{@link #KEY_DESCRIPTION}</td><td>FALSE</td><td><code>String</code></td><td>Description associated with this item.</td></tr>
*    <tr><td>{@link #KEY_DISPLAY_NAME}</td><td>FALSE</td><td><code>String</code></td><td>Friendly display name label associated with this item.</td></tr>
*    <tr><td>{@link #KEY_OBSOLETE}</td><td>TRUE</td><td><code>Boolean</code></td><td>Indicates if this definition is obsolete or not. Set to <code>FALSE</code> if not explicitly set.</td></tr>
*    <tr><td>{@link #KEY_ORIGIN}</td><td>FALSE</td><td><code>String</code></td><td>Indicates the origin, such as standard associated with this definition.</td></tr>
 *  </table>
 * 
 * @author Carl Eric Codere
 *
 */
public interface Definition extends Entity
{
  /**
   * Maximum length of the description attribute in characters. This is here to be
   * compatible for historical reasons, and is defined in ITU-T X.520 Upper
   * Bounds annex.
   */
  public static final int DESC_MAX_LENGTH = 1024;
  
  
  /**
   * Key for the origin of this entity. The origin
   * indicates, such as in the case of definitions, where
   * the definition comes from, such as a standard.
   *  
   * This attribute is optional. 
   */
  public static final String KEY_ORIGIN = "ORIGIN";
  
  
  /** Key for user friendly name associated with this 
   * named entity. This attribute is optional. 
   * This is defined in IETF RFC 2798
  */
 public static final String KEY_DISPLAY_NAME = "displayName";
 
 /**
  * Key for the description associated with this named entity. 
  * This is equivalent to LDAP-DESC in ITU-T X.501. 
  * This value is optional.
  */
 public static final String KEY_DESCRIPTION = "DESCRIPTION";
 
 
 /**
  * Key for OBSOLETE definition associated to this named entity. This is equivalent to
  * OBSOLETE in IETFC RFC 4512. This value is optional and indicates if this 
  * named entity is obsolete or not. The value is a {@link java.lang.Boolean}. 
  * 
  * The default value is <code>FALSE</code>.
  */
 public static final String KEY_OBSOLETE = "OBSOLETE";
  
  /** Returns the description of this entity.
   * 
   * @return The description or <code>null</code>
   *   if not specified.
   */
  public String getDescription();

  /** Returns true if this named entity is obsolete 
   * and should no longer be used.
   *  
   * @return true if this entity is no longer used.
   */
  public boolean isObsolete();
  
  /** Returns the origin of this named entity.
   * 
   * @return A potentially <code>null</code> value
   */
  public String getOrigin();

}
