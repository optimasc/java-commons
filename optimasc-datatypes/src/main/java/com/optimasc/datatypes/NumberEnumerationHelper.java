package com.optimasc.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

/** Class that implements an enumeration helper manager. It assumes
 *  that the values are unlimited values which may have some 
 *  fractional digits.
 *  
 *  <p>It is to note for searching, that a copy of the allowed
 *  choices is made internally and is sorted.</p>
 *   
 * @author Carl Eric Codere
 *
 */
public class NumberEnumerationHelper implements NumberEnumerationFacet
{
  public static class NumericValue implements Comparable
  {
    protected BigDecimal value;
    protected Number originalValue;
    
    public NumericValue(BigDecimal value, Number originalValue)
    {
      this.value = value;
      this.originalValue = originalValue;
    }

    public int compareTo(Object o)
    {
      return value.compareTo(((NumericValue)(o)).value);
    }
  }
  
  protected NumericValue[] sortedEnumeration;
  protected Number[] choices;
  
  public Number[] getChoices()
  {
    return choices;
  }
  
  public NumberEnumerationHelper()
  {
    sortedEnumeration = null;
  }
  
  
  protected NumericValue convert(Number n)
  {
    NumericValue v = null;
    if (n instanceof BigDecimal)
    {
      v = new NumericValue((BigDecimal) n,n);
    } else
    if (n instanceof BigInteger)
    {
        v = new NumericValue(new BigDecimal((BigInteger)n),n);
    } else
    if ((n instanceof Double) || (n instanceof Float))
    {
          v = new NumericValue(new BigDecimal(n.doubleValue()),n);
    } else
      v = new NumericValue(new BigDecimal(n.longValue()),n);
    return v;
  }

  /**
   * 
   * @param choices [in] The possible choices.
   * @throws IllegalArgumentException Thrown if the scales
   *   of the values are different.
   */
  public void setChoices(Number[] choices) throws IllegalArgumentException
  {
    int scale;
    if (choices == null)
    {
      sortedEnumeration = null;
      return;
    }
    if (choices.length > 0)
    {
      sortedEnumeration = new NumericValue[choices.length];
      this.choices = choices;
      for (int i=0; i < choices.length; i++)
      {
        Number n = choices[i];
        NumericValue v = null;
        v = convert(n);
        sortedEnumeration[i] = v;
      }
    }
    Arrays.sort(sortedEnumeration);
    /** Now get back the sorted array */
    for (int i=0; i < sortedEnumeration.length; i++)
    {
      this.choices[i] = sortedEnumeration[i].originalValue;
    }
  }

  public boolean validateChoice(Number value)
  {
    // No restriction
    if (sortedEnumeration == null)
    {
      return true;
    }
    NumericValue otherValue = convert(value);
    if (Arrays.binarySearch(sortedEnumeration, otherValue)<0)
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
    sortedEnumeration = new NumericValue[choices.length];
    for (int i=0; i < choices.length; i++)
    {
      sortedEnumeration[i] = new NumericValue(BigDecimal.valueOf(choices[i]),new Long(choices[i]));
    }
    Arrays.sort(sortedEnumeration);
    /** Now get back the sorted array */
    this.choices = new Number[sortedEnumeration.length];
    for (int i=0; i < sortedEnumeration.length; i++)
    {
      this.choices[i] = sortedEnumeration[i].originalValue;
    }
  }

  public boolean validateChoice(long value)
  {
    // No restriction
    if (sortedEnumeration == null)
    {
      return true;
    }
    NumericValue v =convert(BigDecimal.valueOf(value));
    if (Arrays.binarySearch(sortedEnumeration, v)<0)
       return false;
    return true;
  }

  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + NumberEnumerationHelper.hashCode(sortedEnumeration);
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
    if ((obj instanceof NumberEnumerationHelper)==false)
      return false;
    NumberEnumerationHelper other = (NumberEnumerationHelper) obj;
    
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
