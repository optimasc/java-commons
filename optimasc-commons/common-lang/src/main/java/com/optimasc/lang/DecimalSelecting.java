package com.optimasc.lang;

import java.math.BigDecimal;

public class DecimalSelecting implements Selecting
{
  /** Represents a range selection, with an inclusive lower-bound
   *  and inclusive upper-bound.
   * 
   */
  public static class DecimalSelectRange extends DecimalSelecting implements SelectingRange
  {
    public BigDecimal minInclusive;
    public BigDecimal maxInclusive;
    
    public DecimalSelectRange(BigDecimal minInclusive, BigDecimal maxInclusive)
    {
      super();
      this.minInclusive = minInclusive;
      this.maxInclusive = maxInclusive;
    }

    public BigDecimal getMinInclusive()
    {
      return minInclusive; 
    }

    public BigDecimal getMaxInclusive()
    {
      return maxInclusive; 
    }
    
  }
  
  /** Represents a single allowed value.
   * 
   */
  public static class DecimalSelectValue extends DecimalSelecting implements SelectingValue
  {
    public BigDecimal value;

    public DecimalSelectValue(BigDecimal value)
    {
      super();
      this.value = value;
    }

    public BigDecimal getValue()
    {
      return value; 
    }
  }
  
  
}
