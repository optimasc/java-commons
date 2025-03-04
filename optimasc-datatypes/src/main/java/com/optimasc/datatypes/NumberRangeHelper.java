package com.optimasc.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.optimasc.lang.NumberComparator;

/** Class that used internally to manage ranges of Decimal and ordered
 *  values. This class supports numbers that are scaled hence that
 *  contains fractional digits as non-scaled values.
 * 
 * @author Carl Eric Codere
 *
 */
public class NumberRangeHelper implements NumberRangeFacet
{
  protected Number minInclusive;
  protected Number maxInclusive;
  protected int scale;
  
  
  /** Creates an allowed range of values that supports scaling, hence
   *  fractional values. The scale of both <code>minInclusive</code>
   *  <code>maxInclusive</code> should be equal and will indicate the
   *  scale of the range.
   * 
   * @param minInclusive [in] The minimum inclusive allowed value, a <code>null</code>
   *   value indicates no lower inclusive bound.
   * @param maxInclusive [in] The maximum inclusive allowed value, a <code>null</code>
   *   value indicates no upper inclusive bound. 
   * @throws IllegalArgumentException thrown if <code>minInclusive</code>
   *   is greater than <code>maxInclusive</code>.
   * @throw IllegalArgumentException thrown if bounds scales are not
   *   equal.  
   */
  public NumberRangeHelper(Number minInclusive, Number maxInclusive)
  {
    super();
    setRange(minInclusive, maxInclusive);
  }
  
  
  public boolean isBounded()
  {
    return (minInclusive != null) || (maxInclusive != null);
  }

  /** Verifies the range of this type with the one specified
   *  in parameter. 
   *  
   *  <p>This method will return true when the following
   *  conditions are met:</p>
   *  
   *  <ul>
   *   <li>If this range is bounded upper or lower, and the other range passed
   *    are not bounded.</li>
   *   <li>If this range is bounded, and the other range is also
   *    bounded and the total value range is smaller than the 
   *    one passed in parameter.</li>
   *  </ul>
   * 
   * @param value
   * @return
   */
  public boolean isRestrictionOf(NumberRangeFacet value)
  {
    NumberRangeFacet rangeType;
    rangeType = (NumberRangeFacet) value;
    
    Number minOtherValue = rangeType.getMinInclusive();
    Number maxOtherValue = rangeType.getMaxInclusive();

    // No bounds at all - no restrictions in both ranges
    if ((value.isBounded()==false) && (isBounded()==false))
    {
      return false;
    }

    // This value has one bound, and other no bound.
    if ((value.isBounded()==false) && (isBounded()==true))
    {
      return true;
    }
    
    if ((value.isBounded()==true) && (isBounded()==false))
    {
      return false;
    }
    
    // Both are bounded values
    // Possible choices:
    
    // No bounds at all/
    if ((minOtherValue == null) && (maxOtherValue == null))
    {
        if ((minInclusive == null) && (maxInclusive == null))
        {
          return false;
        }
        return true;
    }
    
    if ((minOtherValue!=null) && (maxOtherValue==null))
    {
      if (minInclusive != null)
      {
        return (NumberComparator.INSTANCE.compare(minInclusive,minOtherValue))==1 || (maxInclusive != null);
      }
      return false;
    }
        
    if ((minOtherValue==null) && (maxOtherValue!=null))
    {
      if (maxInclusive != null)
      {
        return (NumberComparator.INSTANCE.compare(maxInclusive,maxOtherValue))==-1 || (minInclusive != null);
      }
      return false;
    }
    if (minInclusive == null)
      return false;
    if (maxInclusive == null)
      return false;
    
    return ((NumberComparator.INSTANCE.compare(minInclusive,minOtherValue))==1) && (NumberComparator.INSTANCE.compare(maxInclusive,maxOtherValue))==-1;
  }


  public Number getMinInclusive()
  {
    return minInclusive;
  }

  public Number getMaxInclusive()
  {
    return maxInclusive;
  }

  public boolean validateRange(long value)
  {
    if (isBounded()==false)
    {
      return true;
    }
    BigDecimal v = BigDecimal.valueOf(value);
    return validateRange(v);
  }

  public boolean validateRange(Number value)
  {
    if (scale==0)
    {
      if (TypeUtilities.isExact(value)==false)
      {
        return false;
      }
    }
    if (isBounded()==false)
    {
      return true;
    }
    // Compare the values.
    // value < minInclusive
    if ((minInclusive != null) && (NumberComparator.INSTANCE.compare(value, minInclusive)==-1))
    {
      return false;
    }
    // value >= maxInclusive 
    if ((maxInclusive != null) &&  (NumberComparator.INSTANCE.compare(value,maxInclusive)==1))
    {
      return false;
    }
    return true;
  }

  /** Return the precision of a number. The value
   *  returned is an approximation of the number of 
   *  digits for integer values. For example, following
   *  industry standards, an 32-bit signed integer
   *  has a precision of 10 digits even if the maximum
   *  value is not 9999999999 but 2147483647.
   * 
   */
  public int getPrecision()
  {
    if (maxInclusive == null)
    {
      return Integer.MAX_VALUE;
    }
    BigDecimal v = NumberComparator.toBigDecimal(maxInclusive);
    // Calculate the precision of the number
    // Get the unscaled value and then get the precision.
    BigInteger value = v.unscaledValue();
    BigInteger NINE = BigInteger.valueOf(9); 
    BigInteger TEN = BigInteger.valueOf(10); 
        
    int precision = 0;
    while (value.compareTo(NINE) > 0)
    {
      value = value.divide(TEN);
      precision++;
    }
    return precision+1;
  }

  public int getScale()
  {
    if (maxInclusive == null)
    {
      return Short.MAX_VALUE;
    }
    return NumberComparator.getScale(maxInclusive);
  }


  /** The ranges are equal if both either supports scaled
   *  or don't support scaled, and if the allowed range
   *  is exactly the same numeric value (including same scale).
   * 
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if ((obj instanceof NumberRangeHelper)==false)
      return false;
    NumberRangeHelper other = (NumberRangeHelper) obj;
    if (maxInclusive == null)
    {
      if (other.maxInclusive != null)
        return false;
    }
    // We check that the scales are equal here!
    else if (!maxInclusive.equals(other.maxInclusive))
      return false;
    if (minInclusive == null)
    {
      if (other.minInclusive != null)
        return false;
    }
    // We check that the scales are equal here!
    else if (!minInclusive.equals(other.minInclusive))
      return false;
    if (scale != other.scale)
      return false;
    return true;
  }
  
  public boolean isNaturalNumber()
  {
    if (isBounded()==false)
      return false;
    if ((minInclusive != null) && (NumberComparator.INSTANCE.compare(minInclusive, BigDecimal.valueOf(0))>=0))
    {
      return true;
    }
    return false;
  }
  
  public static boolean checkIntersection(BigDecimal x1, BigDecimal x2, BigDecimal y1, BigDecimal y2)
  {
    if (x1 != null)
    {
      if (y1 != null)
      {
        // x1 <= y1
        int compareX1Y1Result = x1.compareTo(y1);
        boolean valueX1Y1Result = (compareX1Y1Result == 0) || (compareX1Y1Result == -1);
        boolean valueY1X1Result = (compareX1Y1Result == 0) || (compareX1Y1Result == 1);
        if (valueX1Y1Result && ((x2 == null) || (x2.compareTo(y1)==1)))
            {
              return true;
            }
        else
        if (valueY1X1Result && ((y2 != null) || (y2.compareTo(x1)==1)))
        {
          return true;
        }
        else
          return false;
      }
    } else
    {
      if (y1 == null)
        return true;
      else
      {
        if ((x2 == null) || (x2.compareTo(y1)==1))
        {
          return true;
        }
      }
    }
    return false;
  }


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
            "minInclusive is greater in magniture than maxInclusive.");
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


}
