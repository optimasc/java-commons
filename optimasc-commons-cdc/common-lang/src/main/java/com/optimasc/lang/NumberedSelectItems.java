package com.optimasc.lang;

public class NumberedSelectItems
{
  /** Represents a numeric range selection, with an inclusive lower-bound
   *  and inclusive upper-bound. A <code>null</code> value 
   *  representing an unbounded value.
   * 
   */
  public static class NumberSelectRange implements NumberSelectItem, SelectRange
  {
    public Number minInclusive;
    public Number maxInclusive;
    protected int scale;
    
    /** Creates a number range. The <code>null</code> value representing
     *  an unbounded value.
     *  
     * @param minInclusive [in] The inclusive minimum value. 
     * @param maxInclusive [in] The maximum inclusive value.
     * @throws IllegalArgumentException Thrown if 
     *   <code>maxInclusive</code> is less than <code>minInclusive</code> 
     */
    public NumberSelectRange(Number minInclusive, Number maxInclusive)
    {
      super();
      setRange(minInclusive,maxInclusive);
    }

    /** Returns the minimum inclusive value allowed for this
     *  ordered value. If this value has not been set,
     *  or if the type has not been configured to be ordered,
     *  the return value will be <code>null</code>.
     * 
     * @return The minimum inclusive value allowed or <code>null</code>
     *  if it is not set.
     */
    public Number getMinInclusive()
    {
      return minInclusive; 
    }

    /** Returns the maximum inclusive value allowed for this
     *  ordered value. If this value has not been set,
     *  or if the type has not been configured to be ordered,
     *  the return value will be <code>null</code>.
     * 
     * @return The maximum inclusive value allowed or <code>null</code>
     *  if it is not set.
     */
    public Number getMaxInclusive()
    {
      return maxInclusive; 
    }
    
    /** Sets the inclusive minimum and maximum range of a number. 
     * 
     * @param minInclusive [in] The minimum range inclusive, or <code>null</code>
     *   if the minimum value is unbounded.
     * @param maxInclusive [in] The maximum range inclusive, or <code>null</code>
     *   if the maximum value is unbounded.
     * @throws IllegalArgumentException  
     *  <ul>
     *   <li>If the scale of the ranges is different.</li>
     *   <li>If minInclusive is greater in magnitude than maxInclusive</li>
     *  </ul> 
     *    
     */
    public void setRange(Number minInclusive, Number maxInclusive)
    {
      if ((minInclusive != null)  && (maxInclusive != null))
      {
        if (NumberComparator.getScale(minInclusive)!=NumberComparator.getScale(maxInclusive))
        {
          throw new IllegalArgumentException("Scale of the ranges should be equal");
        }
        if (NumberComparator.INSTANCE.compare(minInclusive,maxInclusive)==1)
        {
          throw new IllegalArgumentException(
              "minInclusive is greater in magnitude than maxInclusive.");
        }
        scale =  NumberComparator.getScale(minInclusive);
      } else
      if ((minInclusive != null)  && (maxInclusive == null))
      {
        scale =  NumberComparator.getScale(minInclusive);
      } else
      if ((maxInclusive != null)  && (minInclusive == null))
      {
        scale =  NumberComparator.getScale(maxInclusive);
      }
      this.minInclusive = minInclusive;
      this.maxInclusive = maxInclusive;
    }
    
   
    public int getScale()
    {
      return scale;
    }
    
    /** Returns the actual value */ 
    public String toString()
    {
      String lowerBound = "*";
      String upperBound = "*";
      if (minInclusive != null)
      {
        lowerBound = NumberComparator.toBigDecimal(minInclusive).toString(); 
      }
      if (maxInclusive != null)
      {
        upperBound = NumberComparator.toBigDecimal(maxInclusive).toString(); 
      }
      return lowerBound + ".." + upperBound;
    }
    
    
    public boolean equals(Object obj)
    {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if ((obj instanceof NumberSelectRange)==false)
        return false;
      NumberSelectRange other = (NumberSelectRange) obj;
      
      if (scale != other.scale)
        return false;
      
      if ((minInclusive == other.minInclusive) && (maxInclusive == other.maxInclusive))
      {
        return true;
      }
      
      if (  ((minInclusive == null) && (other.minInclusive != null)) || ((other.minInclusive == null) && (minInclusive != null)))
      {
        return false;
      }
      
      if (  ((maxInclusive == null) && (other.maxInclusive != null)) || ((other.maxInclusive == null) && (maxInclusive != null)))
      {
        return false;
      }
      
      if (  ((maxInclusive == null) && (other.maxInclusive == null)) && (minInclusive != null) && (other.minInclusive!= null))
      {
        return (NumberComparator.INSTANCE.compare(minInclusive, other.minInclusive)==0); 
      }
      
      if (  ((minInclusive == null) && (other.minInclusive == null)) && (maxInclusive != null) && (other.maxInclusive!= null))
      {
        return (NumberComparator.INSTANCE.compare(maxInclusive, other.maxInclusive)==0); 
      }
      
      return (
           (NumberComparator.INSTANCE.compare(minInclusive, other.minInclusive)==0) && 
           (NumberComparator.INSTANCE.compare(maxInclusive, other.maxInclusive)==0));
    }

    
  }
  
  /** Represents any numeric value.
   * 
   */
  public static class NumberAnyValue implements NumberSelectItem
  {
    public NumberAnyValue()
    {
    }

    public int getScale()
    {
      return 0;
    }

    public boolean equals(Object obj)
    {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if ((obj instanceof NumberAnyValue)==false)
        return false;
      return true;
    }

    public String toString()
    {
      return "*";
    }
    
   
  }
  
  
  /** Represents a single numeric value.
   * 
   */
  public static class NumberSelectValue  implements NumberSelectItem, SelectValue
  {
    public Number value;
    protected int scale;

    public NumberSelectValue(Number value)
    {
      super();
      if (value == null)
      {
        throw new IllegalArgumentException("Value must not be null");
      }
      scale =  NumberComparator.getScale(value);
      this.value = value;
    }

    public Number getValue()
    {
      return value; 
    }

    public int getScale()
    {
      return scale;
    }

    public Object getObject()
    {
      return value; 
    }

    public boolean equals(Object obj)
    {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if ((obj instanceof NumberSelectValue)==false)
        return false;
      NumberSelectValue other = (NumberSelectValue) obj;
      
      if (scale != other.scale)
        return false;
      return NumberComparator.INSTANCE.compare(this, obj)==0;
    }

    /** Returns the actual value */ 
    public String toString()
    {
      return NumberComparator.toBigDecimal(value).toString();
    }
    
    
    
    
  }
  
  /** Returns the maximum value in the selecting items.
   * 
   * @param selectItems [in] The list of select items
   *  to scan.
   * @return The highest value in the selection items or
   *  <code>null</code> if the value is unbounded.
   */
  public static Number getMaxInclusive(NumberSelectItem selectItems[])
  {
    Number maxValue = null;
    Number maxInclusive = null;
    
    for (int i=0; i < selectItems.length; i++)
    {
      SelectItem rawItem = selectItems[i];
      if (rawItem instanceof NumberSelectValue)
    {
        NumberSelectValue item = (NumberSelectValue) rawItem;
        Number value = item.getValue();
        if (maxValue == null)
        {
          maxValue = value;
        }
        // if (maxValue <  item.value)
        if (NumberComparator.INSTANCE.compare(maxValue,value)<0)
        {
          maxValue = value;
        }
      } else
      {
        NumberSelectRange item = (NumberSelectRange) rawItem;
        
        /* Unbounded value! */
        if (item.getMaxInclusive()==null)
        {
          return null;
        }
        
        maxInclusive = item.getMaxInclusive();
        if (maxValue == null)
        {
          maxValue = maxInclusive;
        }
        
        // if (maxValue < maxInclusive)
        if (NumberComparator.INSTANCE.compare(maxValue,maxInclusive)<0)
        {
          maxValue = maxInclusive;
        }
      }
    }
    return maxValue;
  }
  
  
  /** Returns the minimum value in the selecting items.
   * 
   * @param selectItems [in] The list of select items
   *  to scan.
   * @return The lowest value in the selection items or
   *  <code>null</code> if the value is unbounded.
   */
  public static Number getMinInclusive(NumberSelectItem selectItems[])
  {
    Number minValue = null;
    
    for (int i=0; i < selectItems.length; i++)
    {
      SelectItem rawItem = selectItems[i];
      if (rawItem instanceof NumberSelectValue)
      {
        NumberSelectValue item = (NumberSelectValue) rawItem;
        Number value = item.getValue();
        if (minValue == null)
        {
          minValue = value;
        }
        // if (minValue >  item.value)
        if (NumberComparator.INSTANCE.compare(minValue,value)>0)
        {
          minValue = value;
        }
      } else
      {
        NumberSelectRange item = (NumberSelectRange) rawItem;
        
        Number minInclusive = item.getMinInclusive();
        if (minInclusive == null)
        {
          return null;
        }
        if (minValue == null)
        {
          minValue = minInclusive;
        }
        // if (minValue > minInclusive)
        if (NumberComparator.INSTANCE.compare(minValue,minInclusive)>0)
        {
          minValue = minInclusive;
        }
        
      }
    }
    return minValue;
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
  public static boolean validateValue(NumberSelectItem selectItems[], Number value)
  {
      for (int i=0; i < selectItems.length; i++)
      {
        NumberSelectItem rawItem = selectItems[i];
        // Any value is allowed
        if (rawItem instanceof NumberAnyValue)
        {
          return true;
        }
        // An exact value
        if (rawItem instanceof NumberSelectValue)
        {
          NumberSelectValue item = (NumberSelectValue) rawItem;
          /** Value must be of same scale */
          if (item.getScale()==0)
          {
            if (NumberComparator.getScale(value)!=0)
            {
              continue;
            }
          }
          /* Values are equal */
          if (NumberComparator.INSTANCE.compare(value, item.getValue())==0)
          {
            return true;
          }
        } else
        // A range value
        {
          NumberSelectRange item = (NumberSelectRange) rawItem;
          Number minInclusive = item.getMinInclusive();
          Number maxInclusive = item.getMaxInclusive();
          
          /** Value must be of same scale */
          if (item.getScale()==0)
          {
            if (NumberComparator.getScale(value)!=0)
            {
              continue;
            }
          }
          // Compare the values.
          // value < minInclusive
          if ((minInclusive != null) && (NumberComparator.INSTANCE.compare(value, minInclusive)==-1))
          {
            continue;
          }
          // value >= maxInclusive 
          if ((maxInclusive != null) &&  (NumberComparator.INSTANCE.compare(value,maxInclusive)==1))
          {
            continue;
          }
          return true;
        }
      }
      return false;
    }

}
