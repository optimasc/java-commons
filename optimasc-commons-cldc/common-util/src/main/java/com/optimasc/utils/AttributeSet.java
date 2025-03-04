package com.optimasc.utils;

/** Interface representing a set of attributes, where 
 *  each attribute is represented by an object.
 * 
 * @author Carl Eric Codere
 *
 */
public interface AttributeSet
{
  /** Returns the specified attribute value in this attribute
   *  set. 
   * 
   * @param attributeName [in] The attribute name to retrieve.
   *   This is usually an expanded name attribute.
   * @param expectedClass [in] The expected class output
   *  for this attribute for verification. If this value
   *  is <code>null</code>, then no class verification will be made.
   * @return The object, or <code>null</code> if there is no
   *  such attribute in this attribute map or if the value
   *  of this attribute is <code>null</code>.
   * @throws IllegalArgumentException if the value is not
   *  of the specified type.
   */
  public Object get(String attributeName, Class expectedClass);
  
  /** Returns the attribute names/keys in this attribute set.
   *  The keys returned are expanded names of the attributes.
   * 
   * @return A non-null list of keys, in the case this
   *  attribute set contains no key, an empty array
   *  shall be returned.
   */
  public String[] getKeys();
}
