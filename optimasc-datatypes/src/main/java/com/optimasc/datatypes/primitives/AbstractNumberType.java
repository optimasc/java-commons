package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.NumberEnumerationFacet;
import com.optimasc.datatypes.NumberEnumerationHelper;
import com.optimasc.datatypes.NumberRangeHelper;
import com.optimasc.datatypes.NumberRangeSetterFacet;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.NumberComparator;

public abstract class AbstractNumberType extends PrimitiveType  implements NumberRangeSetterFacet,OrderedFacet, NumberEnumerationFacet
{

  public Object accept(TypeVisitor v, Object arg)
  {
    // TODO Auto-generated method stub
    return null;
  }

  protected NumberRangeHelper rangeHelper;
  protected NumberEnumerationHelper enumHelper;
  protected int scale;
  protected int roundingMode;
  
  
  /** Creates an unbounded, specified scaled type. 
   * 
   * @param scale [in] Indicates the scale (the number of 
   *  digits in the fractional parts). 0 indicates
   *  an integer value.
   */
  protected AbstractNumberType(int scale)
  {
    super(true);
    if (scale < 0)
    {
      throw new IllegalArgumentException("scale should be a non negative number.");
    }
    this.scale = scale;
    roundingMode = BigDecimal.ROUND_HALF_EVEN;
    if (scale==0)
    {
      roundingMode = BigDecimal.ROUND_DOWN;
    }
    setRange(null,null);
    enumHelper = new NumberEnumerationHelper();
  }
  
  /** Creates an bounded numeric value. This creates 
   *  a range facet associated with this numeric type. The 
   *  scale of this numeric value will be based on the 
   *  scale of the ranges. 
   * 
   * @param minInclusive [in] The allowed minimum value, inclusive.
   * @param maxInclusive [in] The allowed maximum value, inclusive.
   * @throws IllegalArgumentException thrown if <code>minInclusive</code>
   *   is greater than <code>maxInclusive</code>.
   * @throws IllegalArgumentException thrown if <code>minInclusive</code>
   *   or <code>maxInclusive</code> are scaled, and their scales
   *   are different.
   *  
   */
  protected AbstractNumberType(BigDecimal minInclusive, BigDecimal maxInclusive)
  {
    super(true);
    setRange(minInclusive,maxInclusive);
    enumHelper = new NumberEnumerationHelper();
    this.scale = rangeHelper.getScale();
    roundingMode = BigDecimal.ROUND_HALF_EVEN;
    if (scale==0)
    {
      roundingMode = BigDecimal.ROUND_DOWN;
    }
  }
  
  /** Creates an numeric value that permits only certain values. This
   *  creates a selecting/enumeration facet associated with this type.
   *  
   *  <p>Furthermore, when setting the allowed choices, it will automatically
   *  create a range facet, with the minimum bound value and maximum bound
   *  value in the choices array.</p>
   *  
   *
   * @param choices [in] The permitted values for this numeric value.
   * @param scale [in] Indicates the scale (the number of 
   *  digits in the fractional parts). 0 indicates
   *  an integer value.
   * @throws IllegalArgumentException thrown if <code>choices</code> contains
   *  numbers with a scale or fractional digits and <code>scaled</code> parameter
   *  is false.
   *  
   */
  protected AbstractNumberType(Number choices[])
  {
    super(true);
    enumHelper = new NumberEnumerationHelper();
    setChoices(choices);
    // Get the lowest range and highest range that will become the
    // minValue and maxValue
    Number sortedChoices[] = enumHelper.getChoices();
    setRange(sortedChoices[0],sortedChoices[sortedChoices.length-1]);
    this.scale = rangeHelper.getScale();
    roundingMode = BigDecimal.ROUND_HALF_EVEN;
    if (scale==0)
    {
      roundingMode = BigDecimal.ROUND_DOWN;
    }
  }
  
  protected AbstractNumberType(long choices[])
  {
    super(true);
    enumHelper = new NumberEnumerationHelper();
    setChoices(choices);
    // Get the lowest range and highest range that will become the
    // minValue and maxValue
    Number sortedChoices[] = enumHelper.getChoices();
    setRange(sortedChoices[0],sortedChoices[sortedChoices.length-1]);
    this.scale = rangeHelper.getScale();
    roundingMode = BigDecimal.ROUND_HALF_EVEN;
    if (scale==0)
    {
      roundingMode = BigDecimal.ROUND_DOWN;
    }
  }
  
  
  public Class getClassType()
  {
    return BigDecimal.class;
  }

  public Number getMinInclusive()
  {
    return rangeHelper.getMinInclusive();
  }

  public Number getMaxInclusive()
  {
    return rangeHelper.getMaxInclusive();
  }

  public boolean isBounded()
  {
    return rangeHelper.isBounded();
  }
  
  
  /** Overrides the default implementation
   *  to return <code>true</code> for 
   *  this type and all derived types.
   * 
   */
  public boolean isNumeric()
  {
    return true;
  }

  /** Return the precision in base 10 of this 
   *  number, which is the total number of digits. For
   *  non scaled values, this may be an approximation.
   * 
   * @return The precision of this number.
   */
  public int getPrecision()
  {
    return rangeHelper.getPrecision();
  }
  
  
  /** Return the scale in base 10 of this 
   *  number, which is the number of digits
   *  in the fractional part.
   * 
   * @return The scale of this number
   */
  public int getScale()
  {
    return scale;
  }

  
  /** {@inheritDoc}
   * 
   *  <p>Specifically, this object will be 
   *  considered a restriction, in the following
   *  cases:</p>
   *  
   *  <ul>
   *   <li>The range of this object is less than
   *     the one passed in parameter</li>
   *  <li>This object has some selecting choices,
   *   and the one specified has none.</li>
   *  <li>This object has some selecting choices,
   *   and the number of selection choices is more 
   *   than the one specified by parameter.</li>
   * </ul>  
   * 
   */
  public boolean isRestrictionOf(Datatype value)
  {
    if ((value instanceof AbstractNumberType)==false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '"+value.getClass().getName()+"'.");
    }
    AbstractNumberType referenceType = (AbstractNumberType) value;
    
    boolean restriction = rangeHelper.isRestrictionOf(referenceType);
    if (restriction == true)
      return true;
    
    Object[] choices = enumHelper.getChoices();
    Object[] otherChoices = referenceType.getChoices();
    if ((choices!=null) && (otherChoices==null))
    {
      return true;
    }
    
    if ((choices==null) && (otherChoices!=null))
    {
      return false;
    }
    
    
    if ((otherChoices!=null) && (otherChoices.length < choices.length))
    {
      return true;
    }
    
    if ((referenceType.scale > scale) && (isBounded()==false) && (referenceType.isBounded()==false))
    {
      return true;
    }
    return false;
    
  }

  public boolean validateRange(long value)
  {
    return rangeHelper.validateRange(value);
  }

  /** {@inheritDoc}
   * 
   */
  public boolean validateRange(Number value)
  {
    if (isBounded())
    {
      return rangeHelper.validateRange(value);
    }
    if (scale==0)
    {
      if (TypeUtilities.isExact(value)==false)
      {
        return false;
      }
    }
    return true;
  }

  
  
  /**{@inheritDoc}
   * 
   * <p>Additionally, if the input object is <code>Byte</code>,
   * <code>Short</code>, <code>Integer</code> or <code>Long</code>
   * and the range of values is unsigned and exactly fits within the above
   * objects, the values are zero-extended before being converted to 
   * a <code>BigInteger</code>. This means that the value will be
   * within range in the resulting value.</p>
   * 
   * <p>Finally, when the value is <code>BigDecimal</code> it should
   * be of the scale at this type.</p>
   * 
   * 
   */
  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
    if (Number.class.isInstance(value))
    {
      return toValueNumber((Number)value,conversionResult);
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }

  protected Object toValueNumber(Number ordinalValue, TypeCheckResult conversionResult)
  {
    BigDecimal bigDecimal;
    
    // Throw and exception when value is not ordered.
    if (ordered ==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"This type is not ordered, hence unsupported value of class,  '"+ordinalValue.getClass().getName()+"'.");
      return null;
    }
    
    if (ordinalValue instanceof BigDecimal)
    {
      bigDecimal = (BigDecimal)ordinalValue;
    }
    else    
    if (ordinalValue instanceof BigInteger)
    {
      BigInteger bigInteger = (BigInteger)ordinalValue;
      bigDecimal = new BigDecimal(bigInteger);
    } else
    if ((ordinalValue instanceof Double) || (ordinalValue instanceof Float))  
    {
      bigDecimal = new BigDecimal(ordinalValue.doubleValue());
    } else
    {
      // If this is a natural number and the number of bits is 
      // exactly of the range is exactly 8/16/32/64 of the primitive
      // type, zero extend the value.
      if (rangeHelper.isNaturalNumber()==true)
      {
        Number maxRange = getMaxInclusive();
        if (maxRange != null)
        {
          BigInteger intRange = NumberComparator.toBigDecimal(maxRange).toBigInteger();
          int bitLength = intRange.bitLength();
          if ((ordinalValue instanceof Byte) && (bitLength==8))
          {
            bigDecimal = BigDecimal.valueOf(ordinalValue.longValue() & 0xFF);
          } else
          if ((ordinalValue instanceof Short) && (bitLength==16))
          {
            bigDecimal = BigDecimal.valueOf(ordinalValue.longValue() & 0xFFFF);
          } else
          if ((ordinalValue instanceof Integer) && (bitLength==32))
          {
             bigDecimal = BigDecimal.valueOf(ordinalValue.longValue() & 0xFFFFFFFF);
          } else
          {
            bigDecimal = BigDecimal.valueOf(ordinalValue.longValue());
          }
        } else
        {
          bigDecimal = BigDecimal.valueOf(ordinalValue.longValue());
        }
      } else
       bigDecimal = BigDecimal.valueOf(ordinalValue.longValue());
    }

    // Convert to correct required scale
    try {
      bigDecimal = bigDecimal.setScale(scale);
    } catch (ArithmeticException e)
    {
      bigDecimal = bigDecimal.setScale(scale,roundingMode);
      conversionResult.narrowingConversion = true;
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
    }

    if (validateChoice(bigDecimal)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Number is not one of the allowed values.");
      return null;
    }
    

    if (validateRange(bigDecimal)==false)
    {
      bigDecimal = NumberComparator.toBigDecimal(getBoundedValue(bigDecimal));
      conversionResult.narrowingConversion = true;
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
    }
    
    return bigDecimal;
  }

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    if (validateRange(ordinalValue)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      return null;
    }
    BigDecimal v = BigDecimal.valueOf(ordinalValue);
    v = v.setScale(scale);
    if (validateChoice(v)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      return null;
    }
    return v;
  }

  public Number[] getChoices()
  {
    return enumHelper.getChoices();
  }
  
  public boolean validateChoice(Object value)
  {
    return enumHelper.validateChoice((BigDecimal)value);
  }
  

  public boolean validateChoice(Number value)
  {
    return enumHelper.validateChoice(value);
  }

  protected void setChoices(long[] choices)
  {
    enumHelper.setChoices(choices);
  }
  
  public void setChoices(Number[] choices)
  {
    enumHelper.setChoices(choices);
  }
  

  public boolean validateChoice(long value)
  {
    return enumHelper.validateChoice(value);
  }


  public boolean equals(Object obj)
  {
    if (obj==null)
      return false;
    if (obj==this)
      return true;
    if ((obj instanceof AbstractNumberType)==false)
    {
      return false;
    }
    AbstractNumberType otherType = (AbstractNumberType) obj;
    if (otherType.rangeHelper.equals(rangeHelper)==false)
    {
      return false;
    }
    
    if ((otherType.enumHelper==null) && (enumHelper!=null))
    {
      return false;
    }
    
    if ((otherType.enumHelper!=null) && (enumHelper==null))
    {
      return false;
    }
    
    // No enumeration constraint for both, then its true
    if ((otherType.enumHelper==null) && (enumHelper==null))
    {
      return true;
    }
    
    
    if (otherType.enumHelper.equals(enumHelper)==false)
    {
      return false;
    }
    
    if (otherType.scale != scale)
    {
      return false;
    }
    
    return true;
  }
  
  /** If the value is not within the specified range,
   *  this method sets the value to the specified bounds
   * 
   * @param value
   * @return
   */
  protected Number getBoundedValue(Number value)
  {
    Number minInclusive = getMinInclusive();
    Number maxInclusive = getMaxInclusive(); 
    
    if ((minInclusive != null) && (NumberComparator.INSTANCE.compare(value,minInclusive)==-1))
    {
      return getMinInclusive();
    } else
    if ((minInclusive != null) &&  (NumberComparator.INSTANCE.compare(value,maxInclusive)==1))
    {
        return getMaxInclusive();
    }
    return value;
    
  }
  
  public void setRange(Number minInclusive, Number maxInclusive)
  {
    rangeHelper = new NumberRangeHelper(minInclusive,maxInclusive);
  }
  
}
