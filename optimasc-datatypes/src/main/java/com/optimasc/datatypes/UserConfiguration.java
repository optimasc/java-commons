package com.optimasc.datatypes;

/** Represents a set of methods to access and set 
 * user/developer added information which is not
 * part of the initial class.
 *  
 * @author Carl Eric Codere
 *
 */
public interface UserConfiguration
{
  /** Retrieves the object associated to a key on a this object.
   *  The object must first have been set to this node by calling {@link #setUserData(String, Object)} 
   *  with the same key.
   * 
   * @param key The key the object is associated to. 
   * @return Returns the user data associated to the given object, or <code>null</code> if there was none.

   */
  public Object getUserData(String key);
  /** Associate an object to a key on this node. The object can later be retrieved from this object
   *  by calling {@link #getUserData(String)}  with the same key. 
   * 
   * @param key
   * @param data
   * @return Returns the data previously associated to the given key on this object, or <code>null</code> if there 
   *  was none.
   */
  public Object setUserData(String key, Object data);
}
