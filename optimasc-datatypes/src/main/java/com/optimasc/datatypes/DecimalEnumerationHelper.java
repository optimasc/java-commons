package com.optimasc.datatypes;

import java.math.BigDecimal;
import java.util.Arrays;

/** Class that implements an enumeration helper manager. It assumes
 *  that the values are unlimited values which may have some 
 *  fractional digits, hence all operations are done through the
 *  <code>BigDecimal</code> class.
 *  
 *  <p>It is to note for searching, that a copy of the allowed
 *  choices is made internally and is sorted.</p>
 *   
 * @author Carl Eric Codere
 *
 */
public class DecimalEnumerationHelper implements DecimalEnumerationFacet
{
  protected BigDecimal[] sortedEnumeration;
  
  public BigDecimal[] getChoices()
  {
    return sortedEnumeration;
  }
  
  public DecimalEnumerationHelper()
  {
    sortedEnumeration = null;
  }

  /**
   * 
   * @param choices [in] The possible choices.
   * @throws IllegalArgumentException Thrown if the scales
   *   of the values are different.
   */
  public void setChoices(BigDecimal[] choices) throws IllegalArgumentException
  {
    int scale;
    if (choices == null)
    {
      sortedEnumeration = null;
      return;
    }
    if (choices.length > 0)
    {
      scale = choices[0].scale();
      for (int i=1; i < choices.length; i++)
      {
        if (choices[i].scale()!=scale)
        {
          throw new IllegalArgumentException("The scale of the numeric values must be equal.");
        }
      }
    }
    sortedEnumeration = new BigDecimal[choices.length];
    System.arraycopy(choices, 0, sortedEnumeration, 0, choices.length);
    Arrays.sort(sortedEnumeration);
    
    
  }

  public boolean validateChoice(BigDecimal value)
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
  
  
  public void setChoices(long[] choices)
  {
    if (choices == null)
    {
      sortedEnumeration = null;
      return;
    }
    sortedEnumeration = new BigDecimal[choices.length];
    for (int i=0; i < choices.length; i++)
    {
      sortedEnumeration[i] = BigDecimal.valueOf(choices[i]);
    }
    Arrays.sort(sortedEnumeration);
  }

  public boolean validateChoice(long value)
  {
    // No restriction
    if (sortedEnumeration == null)
    {
      return true;
    }
    BigDecimal v = BigDecimal.valueOf(value);
    if (Arrays.binarySearch(sortedEnumeration, v)<0)
       return false;
    return true;
  }

  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + DecimalEnumerationHelper.hashCode(sortedEnumeration);
    return result;
  }

  /** The enumerations are equal if both elements of the 
   *  enumeration contain the same numeric values.
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if ((obj instanceof DecimalEnumerationHelper)==false)
      return false;
    DecimalEnumerationHelper other = (DecimalEnumerationHelper) obj;
    
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
    
    
    // We cannot use Arrays.equals here, because BigDecimal.equals
    // also compares the scale, but we want to compare the numeric
    // values.
    if (sortedEnumeration.length != other.sortedEnumeration.length)
      return false;
    for (int i=0; i < sortedEnumeration.length; i++)
    {
      if (sortedEnumeration[i].compareTo(other.sortedEnumeration[i])!=0)
        return false;
    }
    return true;
  }
  
  private static int hashCode(Object[] array)
  {
    int prime = 31;
    if (array == null)
      return 0;
    int result = 1;
    for (int index = 0; index < array.length; index++)
    {
      result = prime * result + (array[index] == null ? 0 : array[index].hashCode());
    }
    return result;
  }

}
