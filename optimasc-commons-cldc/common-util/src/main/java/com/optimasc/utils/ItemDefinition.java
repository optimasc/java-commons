package com.optimasc.utils;

/** Interface to a definition of a named item. A named item
 *  is any object, attribute or element that is named and
 *  can be typed. This description the schema of this 
 *  named item.
 *  
 * @author Carl Eric Codere
 *
 */
public interface ItemDefinition extends TypedDefinition
{
  /**
   * Key indicates if this attribute can be modified by the user or not. By
   * default, this value is FALSE.
   */
  public static final String KEY_READONLY = "NO-USER-MODIFICATION";
  /**
   * Key to indicates if this attribute is single-valued or not. By default this
   * value is FALSE
   */
  public static final String KEY_SINGLEVALUE = "SINGLE-VALUE";
  
  
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

}
