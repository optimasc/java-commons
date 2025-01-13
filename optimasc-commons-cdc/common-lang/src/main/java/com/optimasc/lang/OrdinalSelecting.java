package com.optimasc.lang;

import java.math.BigDecimal;

/** Represents an enumeration of ordinal values represented 
 *  by an integer list. This is equivalent to the 
 *  <code>selecting</code> subtype in ISO/IEC 11404:2007 and
 *  supports both unique values and range of values to define
 *  the enumeration.
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class OrdinalSelecting
{
  /** Represents a range selection, with an inclusive lower-bound
   *  and inclusive upper-bound.
   * 
   */
  public static class OrdinalSelectRange extends OrdinalSelecting implements SelectingRange
  {
    public int minInclusive;
    public int maxInclusive;
    protected BigDecimal cachedMinValue;
    protected BigDecimal cachedMaxValue;
    
    public OrdinalSelectRange(int minInclusive, int maxInclusive)
    {
      super();
      this.minInclusive = minInclusive;
      this.maxInclusive = maxInclusive;
    }

    public BigDecimal getMinInclusive()
    {
      if (cachedMinValue == null)
        cachedMinValue = BigDecimal.valueOf(minInclusive);
      return cachedMinValue; 
    }

    public BigDecimal getMaxInclusive()
    {
      if (cachedMaxValue == null)
        cachedMaxValue = BigDecimal.valueOf(maxInclusive);
      return cachedMaxValue; 
    }
    
  }
  
  /** Represents a any value.
   * 
   */
  public static class OrdinalAnyValue extends OrdinalSelecting implements SelectingValue
  {
    public BigDecimal getValue()
    {
      return null;
    }
  }
  
  
  /** Represents a single allowed value.
   * 
   */
  public static class OrdinalSelectValue extends OrdinalSelecting implements SelectingValue
  {
    public int value;
    protected BigDecimal cachedValue;

    public OrdinalSelectValue(int value)
    {
      super();
      this.value = value;
    }

    public BigDecimal getValue()
    {
      if (cachedValue == null)
        cachedValue = BigDecimal.valueOf(value);
      return cachedValue; 
    }
  }
  
  /** Method that takes as input a list of selecting items and
   *  verifies that the value passed is within the list of
   *  values and ranges.
   * 
   * @param selectItem [in] The list of ranges and values.
   * @param value [in] The value to compare with
   * @return <code>true</code> if the value is within the 
   *   selecting values or ranges, otherwise <code>false</code>.
   */
  public static boolean isValid(OrdinalSelecting selectItems[], long value)
  {
      for (int i=0; i < selectItems.length; i++)
      {
        OrdinalSelecting rawItem = selectItems[i];
        // Any value is allowed
        if (rawItem instanceof OrdinalAnyValue)
        {
          return true;
        }
        // An exact value
        if (rawItem instanceof OrdinalSelectValue)
        {
          OrdinalSelectValue item = (OrdinalSelectValue) rawItem;
          if (value == item.value)
          {
            return true;
          }
        } else
        // A range value
        {
          OrdinalSelectRange item = (OrdinalSelectRange) rawItem;
          
          // Compare the values.
          // this.minInclusive <= other.minInclusive AND 
          // this.maxInclusive >= other.maxInclusive
          if ((value >= item.minInclusive) && (value <= item.maxInclusive))
          {
            return true;
          }
        }
      }
      return false;
    }
  
  
  private static String pad(String input, int padLength)
  {
    while (input.length()<padLength)
    {
      input = "0"+input; 
    }
    return input;
  }
  
  public static String toString(OrdinalSelecting selectItems[])
  {
    StringBuffer buffer = new StringBuffer();
    for (int i=0; i < selectItems.length; i++)
    {
      OrdinalSelecting rawItem = selectItems[i];
      if (rawItem instanceof OrdinalSelectValue)
      {
        OrdinalSelectValue item = (OrdinalSelectValue) rawItem;
        buffer.append(pad(Integer.toHexString(item.value),8));
        buffer.append(",");
      } else
      {
        OrdinalSelectRange item = (OrdinalSelectRange) rawItem;
        
        // Compare the values.
        // this.minInclusive <= other.minInclusive AND 
        // this.maxInclusive >= other.maxInclusive
        buffer.append(pad(Integer.toHexString(item.minInclusive),8));
        buffer.append("-");
        buffer.append(pad(Integer.toHexString(item.maxInclusive),8));
      }
    }
    return buffer.toString();
  }
  
  /** Returns the maximum value in the selecting items.
   * 
   * @param selectItems [in] The list of select items
   *  to scan.
   * @return The highest value in the selection items
   */
  public static long getMaxInclusive(OrdinalSelecting selectItems[])
  {
    long maxValue = Long.MIN_VALUE;
    
    for (int i=0; i < selectItems.length; i++)
    {
      OrdinalSelecting rawItem = selectItems[i];
      if (rawItem instanceof OrdinalSelectValue)
    {
        OrdinalSelectValue item = (OrdinalSelectValue) rawItem;
        if (maxValue <  item.value)
        {
          maxValue = item.value;
        }
      } else
      {
        OrdinalSelectRange item = (OrdinalSelectRange) rawItem;
        
        if (maxValue < item.maxInclusive)
        {
          maxValue = item.maxInclusive;
        }
      }
    }
    return maxValue;
  }
  
  /** Returns the minimum value in the selecting items.
   * 
   * @param selectItems [in] The list of select items
   *  to scan.
   * @return The lowest value in the selection items
   */
  public static long getMinInclusive(OrdinalSelecting selectItems[])
  {
    long minValue = Long.MAX_VALUE;
    
    for (int i=0; i < selectItems.length; i++)
    {
      OrdinalSelecting rawItem = selectItems[i];
      if (rawItem instanceof OrdinalSelectValue)
      {
        OrdinalSelectValue item = (OrdinalSelectValue) rawItem;
        if (minValue >  item.value)
        {
          minValue = item.value;
        }
      } else
      {
        OrdinalSelectRange item = (OrdinalSelectRange) rawItem;
        
        if (minValue >= item.minInclusive)
        {
          minValue = item.minInclusive;
        }
      }
    }
    return minValue;
  }
  
  
  
}
