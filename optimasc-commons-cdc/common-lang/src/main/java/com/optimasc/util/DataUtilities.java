package com.optimasc.util;

import java.lang.reflect.Array;
import java.util.List;

public class DataUtilities
{
  /**
   * Verifies if the specified value is within the specified allowed maximum
   * length.
   * 
   * <p>
   * The following classes are supported to count the number of elements:
   * </p>
   * 
   * <ul>
   * <li>Any native array (byte[], int[], etc..)</li>
   * <li>Any instance of object that is compatible with
   * <code>java.lang.CharSequence</code></li>
   * <li>Any instance of object that is compatible with
   * <code>java.util.List</code></li>
   * </ul>
   * 
   * <p>
   * If the class is not one of the above, this function returns
   * <code>true</code>.
   * </p>
   * 
   * @param value
   *          [in] The object value to verify. The value must not be
   *          <code>null</code>.
   * @param maxLength
   *          [in] The maximum length, which should be a non-negative number.
   * @return <code>true</code> if the length of <code>value</code> is within
   *         <code>maxLength</code> or if it is not one of the supported class
   *         where length can be calculated, otherwise <code>false</code>.
   * @throws IllegalArgumentException
   *           If <code>maxLength</code> is negative.
   */
  public static boolean verifyLength(Object value, int maxLength)
  {
    if (maxLength < 0)
      throw new IllegalArgumentException("'maxLength' should be a non-negative number.");
    if (value instanceof CharSequence)
    {
      CharSequence charSequence = (CharSequence) value;
      if (charSequence.length() > maxLength)
      {
        return false;
      }
      return true;
    }
    if (value instanceof List)
    {
      List list = (List) value;
      if (list.size() > maxLength)
      {
        return false;
      }
      return true;
    }
    if (value.getClass().isArray())
    {
      int arrayLength = Array.getLength(value);
      if (arrayLength > maxLength)
      {
        return false;
      }
      return true;
    }
    return true;
  }

}
