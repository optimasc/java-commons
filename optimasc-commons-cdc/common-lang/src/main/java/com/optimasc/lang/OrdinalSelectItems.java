package com.optimasc.lang;

import com.optimasc.lang.NumberedSelectItems.NumberAnyValue;
import com.optimasc.lang.NumberedSelectItems.NumberSelectRange;
import com.optimasc.lang.NumberedSelectItems.NumberSelectValue;

public class OrdinalSelectItems
{
  /** Represents a range selection, with an inclusive lower-bound
   *  and inclusive upper-bound.
   * 
   */
  public static class OrdinalSelectRange extends NumberSelectRange implements SelectRange, OrdinalSelectItem
  {
    public OrdinalSelectRange(long minInclusive, long maxInclusive)
    {
      super(new Long(minInclusive),new Long(maxInclusive));
      scale = 0;
    }
  }
  
  /** Represents a any value.
   * 
   */
  public static class OrdinalAnyValue extends NumberAnyValue implements OrdinalSelectItem
  {
    public Number getValue()
    {
      return null;
    }
  }
  
  
  /** Represents a single allowed value.
   * 
   */
  public static class OrdinalSelectValue extends NumberSelectValue implements OrdinalSelectItem
  {
    public OrdinalSelectValue(long value)
    {
      super(new Long(value));
      scale = 0;
    }

  }
  

  private static String pad(String input, int padLength)
  {
    while (input.length()<padLength)
    {
      input = "0"+input; 
    }
    return input;
  }
  
  public static String toString(OrdinalSelectItem selectItems[])
  {
    StringBuffer buffer = new StringBuffer();
    for (int i=0; i < selectItems.length; i++)
    {
      SelectItem rawItem = selectItems[i];
      if (rawItem instanceof OrdinalSelectValue)
      {
        OrdinalSelectValue item = (OrdinalSelectValue) rawItem;
        buffer.append(pad(Integer.toHexString(item.getValue().intValue()),8));
        buffer.append(",");
      } else
      {
        OrdinalSelectRange item = (OrdinalSelectRange) rawItem;
        
        // Compare the values.
        // this.minInclusive <= other.minInclusive AND 
        // this.maxInclusive >= other.maxInclusive
        buffer.append(pad(Integer.toHexString(item.getMinInclusive().intValue()),8));
        buffer.append("-");
        buffer.append(pad(Integer.toHexString(item.getMinInclusive().intValue()),8));
      }
    }
    return buffer.toString();
  }
  
  
  
}
