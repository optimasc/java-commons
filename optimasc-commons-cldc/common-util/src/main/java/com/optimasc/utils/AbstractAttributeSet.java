package com.optimasc.utils;

import java.util.Enumeration;
import java.util.Hashtable;

public abstract class AbstractAttributeSet implements AttributeSet
{
  protected Hashtable attributes;
  
  protected AbstractAttributeSet()
  {
    super();
    attributes = new Hashtable();
  }

  /** {@inheritDoc} */
  public Object get(String attributeName, Class expectedClass)
  {
    Object o = attributes.get(attributeName);
    if (o == null)
    {
      return null;
    }
    Class clz = o.getClass();
    if (expectedClass != null)
    {
      if (expectedClass.isAssignableFrom(clz) == false)
      {
        throw new IllegalArgumentException(
            "Wrong class instance of attribute, expected '" + expectedClass.getName()
                + "' but got '" + o.getClass().getName() + "'.");
      }
    }
    return o;
  }

  /** {@inheritDoc} */
  public String[] getKeys()
  {
    String[] keys = new String[attributes.size()];
    Enumeration e = attributes.keys();
    int i = 0;
    while (e.hasMoreElements())
    {
      keys[i++] = (String) e.nextElement();
    }
    return keys;
  }

}
