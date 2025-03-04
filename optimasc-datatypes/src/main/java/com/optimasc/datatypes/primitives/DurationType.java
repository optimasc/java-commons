package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.NumberEnumerationFacet;
import com.optimasc.datatypes.NumberEnumerationHelper;
import com.optimasc.datatypes.IntegerEnumerationFacet;
import com.optimasc.datatypes.IntegerEnumerationHelper;
import com.optimasc.datatypes.NumberRangeFacet;
import com.optimasc.datatypes.NumberRangeHelper;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.TimeUnitFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateTimeFormat;
import com.optimasc.lang.Duration;
import com.optimasc.lang.NumberComparator;

/** * Datatype that represents elapsed time. To avoid
 *    any issues with time ranges because of months
 *    and years, where the number of month varies. 
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
public class DurationType extends PrimitiveType implements OrderedFacet, NumberRangeFacet, TimeUnitFacet, IntegerEnumerationFacet
{
  private static DurationType defaultTypeInstance;
  private static TypeReference defaultTypeReference;
  
  protected NumberRangeHelper rangeHelper;
  protected IntegerEnumerationHelper enumHelper;
  
  protected int timeUnit;
  
  /** Creates a duration/timeinterval type with
   *  a default unit of milliseconds and unlimited
   *  non-negative number range.
   */
  public DurationType()
  {
    super(true);
    rangeHelper = new NumberRangeHelper(new Long(0),null);
    enumHelper = new IntegerEnumerationHelper(false);
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
    rangeHelper = new NumberRangeHelper(new Long(0),null);
    enumHelper = new IntegerEnumerationHelper(false);
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
    enumHelper = new IntegerEnumerationHelper(false);
    enumHelper.setChoices(choices);
    long[] sortedChoices = enumHelper.getChoicesAsInteger();
    rangeHelper = new NumberRangeHelper(new Long(sortedChoices[0]),new Long(sortedChoices[sortedChoices.length-1]));
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
    enumHelper = new IntegerEnumerationHelper(false);
    rangeHelper = new NumberRangeHelper(new Long(0),new Long(maxValue));
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
      
      if (otherObj.rangeHelper.equals(rangeHelper)==false)
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
    if (validateChoice(bigDecimal.longValue())==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Value is not one of the allowed values.");
      return null;
    }
      if (validateRange(bigDecimal)==false)
      {
        bigDecimal = getBoundedValue(bigDecimal);
        conversionResult.narrowingConversion = true;
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      }
    return new Long(bigDecimal.longValue());
  }

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    if (validateChoice(ordinalValue)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Value is not one of the allowed values.");
      return null;
    }
    if (validateRange(ordinalValue)==false)
    {
      Number bigValue = getBoundedValue(BigDecimal.valueOf(ordinalValue));
      conversionResult.narrowingConversion = true;
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      return new Long(bigValue.longValue());
    }
    return new Long(ordinalValue);
  }

  public boolean isBounded()
  {
    return rangeHelper.isBounded();
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
    
    boolean restriction = rangeHelper.isRestrictionOf(otherType);
    if (restriction == true)
      return true;
    
    
    long[] choices = enumHelper.getChoicesAsInteger();
    long[] otherChoices = otherType.getChoices();
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
    return rangeHelper.getMinInclusive();
  }

  public Number getMaxInclusive()
  {
    return rangeHelper.getMaxInclusive();
  }

  public boolean validateRange(long value)
  {
    return rangeHelper.validateRange(value);
  }

  public boolean validateRange(Number value)
  {
    return rangeHelper.validateRange(value);
  }

  public int getAccuracy()
  {
    return timeUnit;
  }

  public boolean validateChoice(long value)
  {
    return enumHelper.validateChoice(value);
  }
  
  public static TypeReference getInstance()
  {
    if (defaultTypeInstance == null)
    {
      defaultTypeInstance = new DurationType();
      defaultTypeReference = new NamedTypeReference("timeinterval(second, 10, 3)" ,defaultTypeInstance);
    }
    return defaultTypeReference; 
  }

  public boolean isNumeric()
  {
    return true;
  }

  public long[] getChoices()
  {
    return enumHelper.getChoicesAsInteger();
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

  
}
