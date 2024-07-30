package com.optimasc.datatypes;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

/** Constraining facet helper for selecting types when the element
 *  types are date-time or time values. 
 * 
 * @author Carl Eric Codere
 *
 */
public class DateTimeEnumerationHelper implements DateTimeEnumerationFacet
{
  protected Class datatypeClass;
  protected Calendar[] sortedEnumeration;
  protected Comparator comparator;
  
  public DateTimeEnumerationHelper(Class clz, Comparator comparator)
  {
    super();
    this.datatypeClass = clz;
    this.comparator = comparator;
  }
  
  public Calendar[] getChoices()
  {
    return sortedEnumeration;
  }

  public void setChoices(Calendar[] choices)
  {
    for (int i = 0; i < choices.length; i++)
    {
      if (datatypeClass.isInstance(choices[i]) == false)
      {
        throw new IllegalArgumentException("Enumeration elements should be of type '"+ datatypeClass.getName()+"'");
      }
    }
    sortedEnumeration = new Calendar[choices.length];
    System.arraycopy(choices, 0, sortedEnumeration, 0, choices.length);
    Arrays.sort(sortedEnumeration,comparator);    
  }

  public boolean validateChoice(Calendar value)
  {
    // No restriction
    if (sortedEnumeration == null)
    {
      return true;
    }
    if (Arrays.binarySearch(sortedEnumeration, value,comparator)<0)
       return false;
    return true;
  }
  
  /** The enumerations are equal if both elements of the 
   *  enumeration contain the Calendar values.
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if ((obj instanceof DateTimeEnumerationHelper)==false)
      return false;
    DateTimeEnumerationHelper other = (DateTimeEnumerationHelper) obj;
    
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
    for (int i=0; i < sortedEnumeration.length; i++)
    {
      if (comparator.compare(sortedEnumeration[i],other.sortedEnumeration[i])!=0)
        return false;
    }
    return true;
  }
  
}
