package com.optimasc.utils;

import java.util.Hashtable;

/** A class used to manage properties.
 * 
 * @author Carl Eric Codère
 *
 */
public class PropertyArray
{
  protected Hashtable hashTable;
  protected boolean isCaseSensitive;
  
  
  public PropertyArray(boolean caseSensitive)
  {
    hashTable = new Hashtable();
    isCaseSensitive = caseSensitive;
  }
  
  public synchronized void putProperty(Property prop)
  {
    if (isCaseSensitive == false)
      hashTable.put(prop.getKey().toUpperCase(), prop);
    else
      hashTable.put(prop.getKey(), prop);
  }

  public synchronized Property getProperty(String key)
  {
    if (isCaseSensitive == false)
       return (Property)hashTable.get(key.toUpperCase());
    else
      return (Property)hashTable.get(key);
  }
  
  public void clear()
  {
    hashTable.clear();
  }

  public void remove(String key)
  {
    if (isCaseSensitive == false)
      hashTable.remove(key.toUpperCase());
    else
      hashTable.remove(key);
  }
  
  
}
