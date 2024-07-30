package com.optimasc.datatypes;

import java.util.Arrays;

public class EnumerationHelper implements EnumerationFacet
{
  protected Class datatypeClass;
  protected Object[] sortedEnumeration;
  
  public EnumerationHelper(Class clz)
  {
    super();
    datatypeClass = clz;
  }
  
  public Object[] getChoices()
  {
    return sortedEnumeration;
  }

  public void setChoices(Object choices[])
  {
    for (int i = 0; i < choices.length; i++)
    {
      if (datatypeClass.isInstance(choices[i]) == false)
      {
        throw new IllegalArgumentException("Enumeration elements should be of type '"+ datatypeClass.getName()+"'");
      }
    }
    sortedEnumeration = new Object[choices.length];
    System.arraycopy(choices, 0, sortedEnumeration, 0, choices.length);
    Arrays.sort(sortedEnumeration);    
  }

  public boolean validateChoice(Object value)
  {
    // No restriction
    if (sortedEnumeration == null)
    {
      return true;
    }
    if (Arrays.binarySearch(sortedEnumeration, value)<0)
       return false;
    return true;
  }
}
