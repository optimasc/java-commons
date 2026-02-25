package com.optimasc.lang;

public class IntegerSelectItems
{
    public static class IntegerSelectValue implements SelectItem
    {
      protected int value;

      public IntegerSelectValue(int value)
      {
        super();
        this.value = value;
      }
    }
    
    public static class IntegerSelectRange implements SelectItem
    {
      protected int minInclusive;
      protected int maxInclusive;
      
      public IntegerSelectRange(int minInclusive, int maxInclusive)
      {
        super();
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
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
    public static boolean validateValue(SelectItem selectItems[], int value)
    {
        for (int i=0; i < selectItems.length; i++)
        {
          SelectItem rawItem = selectItems[i];
          // An exact value
          if (rawItem instanceof IntegerSelectValue)
          {
            IntegerSelectValue item = (IntegerSelectValue) rawItem;
            if (item.value==value)
              return true;
          } else
          // A range value
          {
            IntegerSelectRange item = (IntegerSelectRange) rawItem;
            int minInclusive = item.minInclusive;
            int maxInclusive = item.maxInclusive;
            
            // Compare the values.
            // value < minInclusive
            if ((value >= minInclusive) && (value <= maxInclusive))
            {
              return true;
            }
          }
        }
        return false;
      }
}
