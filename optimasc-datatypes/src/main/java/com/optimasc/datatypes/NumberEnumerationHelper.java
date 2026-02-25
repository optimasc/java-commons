package com.optimasc.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;

import com.optimasc.datatypes.facets.NumberEnumerationFacet;
import com.optimasc.lang.NumberComparator;
import com.optimasc.lang.NumberSelectItem;
import com.optimasc.lang.NumberedSelectItems;
import com.optimasc.lang.NumberedSelectItems.NumberSelectRange;
import com.optimasc.lang.NumberedSelectItems.NumberSelectValue;
import com.optimasc.lang.SelectValue;

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
  protected NumberSelectItem values[];
  protected Class allowedValueClass = Number.class;
  
  
  protected static BigDecimal convert(Number n)
  {
    BigDecimal v = null;
    if (n instanceof BigDecimal)
    {
      v = (BigDecimal)n;
    } else
    if (n instanceof BigInteger)
    {
        v = new BigDecimal((BigInteger)n);
    } else
    if ((n instanceof Double) || (n instanceof Float))
    {
          v = new BigDecimal(n.doubleValue());
    } else
      v = new BigDecimal(n.longValue());
    return v;
  }
  
  public NumberEnumerationHelper()
  {
  }
  
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
  public NumberEnumerationHelper(Number minInclusive, Number maxInclusive)
  {
    NumberSelectItem[] items = new NumberSelectItem[1];
    items[0] = new NumberedSelectItems.NumberSelectRange(minInclusive, maxInclusive);
    setAllowedValuesAsSelectItems(items);
  }
  
  
  public boolean isValid(long value)
  {
    if (values == null)
    {
      return true;
    }
    return NumberedSelectItems.validateValue(values, new Long(value));
  }
  
  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + NumberEnumerationHelper.hashCode(values);
    return result;
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



  public void setAllowedValuesAsSelectItems(NumberSelectItem[] values)
  {
    this.values = values; 
  }



  public NumberSelectItem[] getAllowedValuesAsSelectItems()
  {
    return values;
  }



  public boolean isBounded()
  {
    return (getMinInclusive() != null) || (getMaxInclusive() != null);
  }



  /** Returns the ISO/IEC 11404:2007 syntax of this 
   *  subtype, either as a range or selecting type.
   * 
   */
  public String toString()
  {
    if (values == null)
    {
      return "";
    }
    // Range
    if ((values.length == 1) && (values[0] instanceof NumberSelectRange))
    {
      NumberSelectRange range = (NumberSelectRange) values[0];
      return "range(" + range.toString() + ")"; 
    }
    // Selecting
    StringBuffer buffer = new StringBuffer();
    buffer.append("selecting(");
    for (int i = 0; i < (values.length-1); i++)
    {
      buffer.append(values[i].toString());
      buffer.append(',');
    }
    buffer.append(values[values.length-1].toString());
    buffer.append(")");
    return buffer.toString();
  }

  public void setAllowedValues(long[] values)
  {
    NumberSelectItem numbers[] = new NumberSelectItem[values.length];
    for (int i=0; i < values.length; i++)
    {
      numbers[i] = new NumberSelectValue(new Long(values[i]));
    }
    this.values = numbers;
  }



  public Number getMinInclusive()
  {
    if (values == null)
      return null;
    return NumberedSelectItems.getMinInclusive(values);
  }



  public Number getMaxInclusive()
  {
    if (values == null)
      return null;
    return NumberedSelectItems.getMaxInclusive(values);
  }

  
  public boolean isNaturalNumber()
  {
    if (isBounded()==false)
      return false;
    
    Number minInclusive = getMinInclusive();
    if ((minInclusive != null) && (NumberComparator.INSTANCE.compare(minInclusive, BigDecimal.valueOf(0))>=0))
    {
      return true;
    }
    return false;
  }
  
  public int getScale()
  {
      Number maxInclusive = getMaxInclusive();
      if (maxInclusive == null)
      {
        return  Short.MAX_VALUE;
      }
      return  NumberComparator.getScale(maxInclusive);
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
    Number maxInclusive = getMaxInclusive();
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
  public boolean isRestrictionOf(NumberEnumerationFacet value)
  {
    NumberEnumerationFacet rangeType;
    rangeType = (NumberEnumerationFacet) value;
    
    Number minOtherValue = rangeType.getMinInclusive();
    Number maxOtherValue = rangeType.getMaxInclusive();
    Number minInclusive = getMinInclusive();
    Number maxInclusive = getMaxInclusive();

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

  public Object[] getAllowedValues()
  {
    return values;
  }

  public void setAllowedValues(Object[] choices)
  {
    values = new NumberSelectItem[choices.length];
    // Undefined scale.
    int scale = -1;
    for (int i = 0; i < choices.length; i++)
    {
      if (allowedValueClass.isInstance(choices[i]) == false)
      {
        values = null;
        throw new IllegalArgumentException("Enumeration elements should be of type '"+ allowedValueClass.getName()+"'");
      }
      values[i] = new NumberSelectValue((Number)choices[i]);
      if (scale == -1)
      {
        scale = values[i].getScale();
      } else
      {
        if (scale != values[i].getScale())
        {
          throw new IllegalArgumentException("Scale of allowed values is different");
        }
      }
    }
  }

  public boolean isValid(Object value)
  {
    if (allowedValueClass.isInstance(value)==false)
      return false;
    if (values==null)
    {
      return true;
    }
    return NumberedSelectItems.validateValue(values,(Number) value);
  }

  public Class getAllowedValuesClass()
  {
    return allowedValueClass;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if ((obj instanceof NumberEnumerationHelper)==false)
      return false;
    NumberEnumerationHelper other = (NumberEnumerationHelper) obj;
    
    if (allowedValueClass != other.allowedValueClass)
    {
      return false;
    }
    return Arrays.equals(values, other.values);

  }
  
  

}
