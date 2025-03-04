package com.optimasc.datatypes;

import java.math.BigDecimal;
import java.util.Arrays;

/** A selecting enumeration that contains long values.
 * 
 * @author Carl Eric Codere
 *
 */
public class IntegerEnumerationHelper extends NumberEnumerationHelper
{
  /** Indicates if we allow negative values. */
  protected boolean allowNegative;
  
  
  
  public IntegerEnumerationHelper(boolean allowNegative)
  {
    super();
    this.allowNegative = allowNegative;
  }

  private static int hashCode(long[] array)
  {
    int prime = 31;
    if (array == null)
      return 0;
    long result = 1;
    for (int index = 0; index < array.length; index++)
    {
      result = prime * result + array[index];
    }
    return (int)result;
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

  protected long[] sortedEnumeration;
  protected Number[] cachedChoices;
  
  public Number[] getChoices()
  {
    if (cachedChoices != null)
    {
      return cachedChoices;
    }
    cachedChoices = new BigDecimal[sortedEnumeration.length];
    for (int i=0; i < sortedEnumeration.length; i++)
    {
      cachedChoices[i] = BigDecimal.valueOf(sortedEnumeration[i]); 
    }
    return cachedChoices;
  }
  
  public long[] getChoicesAsInteger()
  {
    return sortedEnumeration;
  }

  public void setChoices(BigDecimal[] choices)
  {
    if (choices == null)
    {
      sortedEnumeration = null;
      return;
    }
    for (int i=0; i < choices.length; i++)
    {
      if (TypeUtilities.isLongValueExact(choices[i])==false)
        throw new IllegalArgumentException("Value does not fit witihn a long value.");
      long value = choices[i].longValue();
      if ((allowNegative==false) && (value < 0))
      {
        throw new IllegalArgumentException("Value is negative, while negative values are not allowed.");
      }
      sortedEnumeration[i] = value;
    }
    sortedEnumeration = new long[choices.length];
    cachedChoices = null;
  }

  public boolean validateChoice(BigDecimal value)
  {
    if (TypeUtilities.isLongValueExact(value)==false)
      return false;
    if (Arrays.binarySearch(sortedEnumeration, value.longValue())<0)
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
    for (int i=0; i < choices.length; i++)
    {
      if ((allowNegative==false) && (choices[i] < 0))
      {
        throw new IllegalArgumentException("Value is negative, while negative values are not allowed.");
      }
    }
    sortedEnumeration = new long[choices.length];
    System.arraycopy(choices, 0, sortedEnumeration, 0, choices.length);
    Arrays.sort(sortedEnumeration);
    cachedChoices = null;
  }

  public boolean validateChoice(long value)
  {
    // No choice defined.
    if (sortedEnumeration == null)
    {
      return true;
    }
    if (Arrays.binarySearch(sortedEnumeration, value)<0)
       return false;
    return true;
  }

  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + IntegerEnumerationHelper.hashCode(cachedChoices);
    result = prime * result + IntegerEnumerationHelper.hashCode(sortedEnumeration);
    return result;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if ((obj instanceof IntegerEnumerationHelper)==false)
      return false;
    IntegerEnumerationHelper other = (IntegerEnumerationHelper) obj;
    if (!Arrays.equals(sortedEnumeration, other.sortedEnumeration))
      return false;
    return true;
  }

  
}
