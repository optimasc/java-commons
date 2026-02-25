package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.NumberEnumerationHelper;
import com.optimasc.datatypes.OrderedProperty;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.facets.NumberEnumerationFacet;
import com.optimasc.datatypes.facets.TimeUnitFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateTime;
import com.optimasc.date.DateTimeFormat;
import com.optimasc.lang.Duration;
import com.optimasc.lang.NumberComparator;
import com.optimasc.lang.NumberSelectItem;

/** * Datatype that represents elapsed time. To avoid
 *    any issues with time ranges because of months
 *    and years, where the number of month varies, only
 *    the day, hour, minutes, and/or seconds fields are allowed. 
 *    The range of values allowed for duration is the following in ISO
 *    8601 notation: <code>P[n][n]DT[n]H[n]M[n]S</code> or <code>P[n]W</code>.
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>DURATION</code> subset ASN.1 datatype</li>
 *   <li><code>timeinterval(timeunit,10,factor)</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>dayTimeDuration</code> XMLSchema 1.1 built-in datatype</li>
 *   <li><code>INTERVAL</code> of day, hour, minute, second in SQL2003</li>
 *  </ul>
 *  
 * <p>Internally, values of this type are represented as {@link Long} objects that
 *   represent the number of time units elapsed, and must be a natural number (non-negative)</p>
 * 
 * @author Carl Eric Codere
 *
 */
public class DurationType extends PrimitiveType implements OrderedProperty, TimeUnitFacet, NumberEnumerationFacet
{
  protected NumberEnumerationHelper enumHelper;
  
  protected int timeUnit;
  
  /** Creates a duration/timeinterval type with
   *  a default unit of milliseconds and unlimited
   *  non-negative number range.
   */
  public DurationType()
  {
    super(true);
    enumHelper = new NumberEnumerationHelper(new Long(0),null);
    timeUnit = DateTimeFormat.TimeUnit.MILLISECONDS;
  }

  /** Creates a duration/timeinterval type with
   *  the specified duration and unlimited non-negative
   *  maximum duration range.
   *  
   *  @param unit [in] The time unit to use for the duration.
   */
  public DurationType(int unit)
  {
    super(true);
    DateTimeFormat.TimeUnit.validate(unit);
    enumHelper = new NumberEnumerationHelper(new Long(0),null);
    timeUnit = unit;
  }
  

  /** Creates a duration/timeinterval type with
   *  the specified duration and unlimited non-negative
   *  maximum duration range.
   * 
   *  @param unit [in] The time unit to use for the duration.
   * @param choices
   */
  public DurationType(int unit, long[] choices)
  {
    super(true);
    DateTimeFormat.TimeUnit.validate(unit);
    enumHelper = new NumberEnumerationHelper();
    setAllowedValues(choices);
    timeUnit = unit;
  }
  
  
  /** Creates a duration/timeinterval type with
   *  the specified duration and specified
   *  maximum duration range.
   * 
   *  @param unit [in] The time unit to use for the duration.
   * @param maxValue The maximum value range
   *  for the duration in the specified unit.
   */
  public DurationType(int unit, long maxValue)
  {
    super(true);
    DateTimeFormat.TimeUnit.validate(unit);
    enumHelper = new NumberEnumerationHelper(new Long(0),new Long(maxValue));
    timeUnit = unit;
  }
  
  
  
  
  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  

  public Class getClassType()
  {
    return Long.class;
  }

  /** A duration type is equal only if  both have the same time unit and
   *  the same allowed range.   
   */
  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }
      if (!(obj instanceof DurationType))
      {
          return false;
      }
      DurationType otherObj = (DurationType) obj;
      if (otherObj.timeUnit!=timeUnit)
      {
        return false;
      }
      
      if ((otherObj.enumHelper==null) && (enumHelper!=null))
      {
        return false;
      }
      
      if ((otherObj.enumHelper!=null) && (enumHelper==null))
      {
        return false;
      }
      
      // No enumeration constraint for both, then its true
      if ((otherObj.enumHelper==null) && (enumHelper==null))
      {
        return true;
      }
      
      
      if (otherObj.enumHelper.equals(enumHelper)==false)
      {
        return false;
      }
      
      
      return true;
  }

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
    Number bigDecimal;
    
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
    {
      bigDecimal = BigDecimal.valueOf(ordinalValue.longValue());
    }
    
    if (isWithinRange(bigDecimal)==false)
    {
      bigDecimal = NumberComparator.toBigDecimal(getBoundedValue(bigDecimal));
      conversionResult.narrowingConversion = true;
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      return new Long(bigDecimal.longValue());
    }
    

    if (isValid(bigDecimal)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Number is not one of the allowed values.");
      return null;
    }
    return new Long(bigDecimal.longValue());
  }

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    if (isWithinRange(new Long(ordinalValue))==false)
    {
      Number bigValue = getBoundedValue(BigDecimal.valueOf(ordinalValue));
      conversionResult.narrowingConversion = true;
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      return new Long(bigValue.longValue());
    }
    if (isValid(ordinalValue)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Value is not one of the allowed values.");
      return null;
    }
    return new Long(ordinalValue);
  }

  public boolean isBounded()
  {
    return enumHelper.isBounded();
  }

  
  public NumberSelectItem[] getAllowedValuesAsSelectItems()
  {
    return enumHelper.getAllowedValuesAsSelectItems();
  }
  
  /** {@inheritDoc}
   * 
   *  <p>Specifically, this object will be 
   *  considered a restriction, in the following
   *  cases:</p>
   *  
   *  <ul>
   *   <li>The timeUnit  of this object is less accurage than
   *     the one passed in parameter</li>
   *  <li>This object is bounded and the bounded range is smaller
   *   in magnitude than the one specified in parameter.</li>
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
    if ((value instanceof DurationType)==false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '"+value.getClass().getName()+"'.");
    }
    DurationType otherType = (DurationType) value;
    if (timeUnit < otherType.timeUnit)
    {
      return true;
    }
    
    boolean restriction = enumHelper.isRestrictionOf(otherType);
    if (restriction == true)
      return true;
    
    
    Object[] choices = enumHelper.getAllowedValuesAsSelectItems();
    Object[] otherChoices = otherType.getAllowedValuesAsSelectItems();
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
    return false;
  }

  public Number getMinInclusive()
  {
    return enumHelper.getMinInclusive();
  }

  public Number getMaxInclusive()
  {
    return enumHelper.getMaxInclusive();
  }

  public int getAccuracy()
  {
    return timeUnit;
  }

  public boolean isValid(long value)
  {
    return enumHelper.isValid(value);
  }
  
  
  public boolean isNumeric()
  {
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

  public void setAccuracy(int accuracy)
  {
    timeUnit = accuracy;
  }

  
  public String toString()
  {
    switch (this.timeUnit)
    {
       case DateTime.TimeAccuracy.YEAR:
         return "timeinterval(year, 10, 1)";
       case DateTime.TimeAccuracy.DAY:
         return "timeinterval(day, 10, 1)";
       case DateTime.TimeAccuracy.MINUTE:
         return "timeinterval(minute, 10, 1)";
       case DateTime.TimeAccuracy.SECOND:
          return "timeinterval(second, 10, 1)";
       case DateTime.TimeAccuracy.MILLISECOND:
         return "timeinterval(second, 10, 3)";
    default:
      return null;
    }
  }

  public Object[] getAllowedValues()
  {
    return enumHelper.getAllowedValues();
  }

  public void setAllowedValues(Object[] choices)
  {
    Class allowedValueClass = enumHelper.getAllowedValuesClass();
    for (int i=0; i < choices.length; i++)
    {
      if (allowedValueClass.isInstance(choices[i])==false)
      {
        throw new IllegalArgumentException("Enumeration elements should be of type '"+ allowedValueClass.getName()+"'");
      }
      if (NumberComparator.getScale((Number)choices[i])>0)
      {
        throw new IllegalArgumentException("Scale should be zero for integer choices.");
      }
      if (NumberComparator.INSTANCE.compare(BigInteger.ZERO, (Number)choices[i])==-1)
      {
        throw new IllegalArgumentException("Number must be a natural number (A non negative number).");
      }
      
    }
    enumHelper.setAllowedValues(choices);
  }

  public boolean isValid(Object value)
  {
    return enumHelper.isValid(value);
  }

  public Class getAllowedValuesClass()
  {
    return enumHelper.getAllowedValuesClass();
  }

  public void setAllowedValuesAsSelectItems(NumberSelectItem[] values)
  {
    enumHelper.setAllowedValuesAsSelectItems(values);
  }

  public void setAllowedValues(long[] values)
  {
    for (int i=0; i < values.length; i++)
    {
      if ((values[i])<0)
      {
        throw new IllegalArgumentException("Number must be a natural number (A non negative number).");
      }
      
    }
    enumHelper.setAllowedValues(values);
  }
  
  
  public boolean isWithinRange(Number value)
  {
    Number minInclusive = getMinInclusive();
    Number maxInclusive = getMaxInclusive();
    
    if (minInclusive != null)
    {
      if (NumberComparator.INSTANCE.compare(value, minInclusive)==-1)
      {
        return false;
      }
    }

    if (maxInclusive != null)
    {
      if (NumberComparator.INSTANCE.compare(value, maxInclusive)==1)
      {
        return false;
      }
    }
    return true;
  }
  

  
}
