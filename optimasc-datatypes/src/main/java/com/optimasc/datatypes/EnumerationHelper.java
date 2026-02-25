package com.optimasc.datatypes;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;

import com.optimasc.datatypes.facets.EnumerationFacet;

public class EnumerationHelper implements EnumerationFacet
{
  /** The underlying required class type 
   *  of the object.
   */
  protected Class allowedValueClass;
  /** The sorted enumeration. The array is an actually
   *  an array of type <code>datatypeClass</code>. 
   */
  protected Object[] sortedEnumeration;
  /** Optional comparator value or null if 
   *  default comparable value should be used.
   */
  protected Comparator comparator;
  
  
  public EnumerationHelper(Class clz)
  {
    super();
    allowedValueClass = clz;
    comparator = null;
  }
  
  public EnumerationHelper(Class clz, Comparator comparator)
  {
    super();
    allowedValueClass = clz;
    this.comparator = comparator;
  }
  
  
  public Object[] getAllowedValues()
  {
    return sortedEnumeration;
  }


  /** Sets the allowed or valid choices. The
   *  actual array is copied into a new array
   *  instance of the correct type and
   *  then is sorted into ascending order
   *  by calling the {@link #sort(Object[])}
   *  method. 
   * 
   */
  public void setAllowedValues(Object choices[])
  {
    for (int i = 0; i < choices.length; i++)
    {
      if (allowedValueClass.isInstance(choices[i]) == false)
      {
        throw new IllegalArgumentException("Enumeration elements should be of type '"+ allowedValueClass.getName()+"'");
      }
    }
    sortedEnumeration = (Object[])Array.newInstance(allowedValueClass,choices.length);
    System.arraycopy(choices, 0, sortedEnumeration, 0, choices.length);
    if (comparator!=null)
    {
      Arrays.sort(sortedEnumeration,comparator);
    } else
    {
     Arrays.sort(sortedEnumeration);
    }
  }
  
  public boolean isValid(Object value)
  {
    // No restriction
    if (sortedEnumeration == null)
    {
      return true;
    }
    if (comparator!=null)
    {
      if (Arrays.binarySearch(sortedEnumeration, value,comparator)<0)
        return false;
    } else
    {
      if (Arrays.binarySearch(sortedEnumeration, value)<0)
       return false;
    }
    return true;
  }
  
  /** The enumerations are equal if both elements of the 
   *  enumeration contain the specified values.
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if ((obj instanceof EnumerationHelper)==false)
      return false;
    EnumerationHelper other = (EnumerationHelper) obj;
    
    if (allowedValueClass != other.allowedValueClass)
    {
      return false;
    }
    
    if ((sortedEnumeration == null) && (other.sortedEnumeration!=null))
    {
      return false;
    }
    if ((sortedEnumeration != null) && (other.sortedEnumeration==null))
    {
      return false;
    }
    
    if ((sortedEnumeration == null) && (other.sortedEnumeration==null))
    {
      return true;
    }
    
    if (sortedEnumeration.length != other.sortedEnumeration.length)
      return false;
    
    if (comparator == null)
    {
      return Arrays.equals(sortedEnumeration, sortedEnumeration);
    } else
    {
      for (int i=0; i < sortedEnumeration.length; i++)
      {
        if (comparator.compare(sortedEnumeration[i],other.sortedEnumeration[i])!=0)
          return false;
      }
      
    }
    return true;
  }

  public Class getAllowedValuesClass()
  {
    return allowedValueClass;
  }
  
  

}
