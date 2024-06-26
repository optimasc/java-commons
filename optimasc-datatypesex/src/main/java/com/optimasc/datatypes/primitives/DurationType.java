package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DecimalRangeFacet;
import com.optimasc.datatypes.DecimalRangeHelper;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.TimeUnitFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.Duration;

/** * Datatype that represents elapsed time. To avoid
 *    any issues with time ranges because of months
 *    and years, where the number of month varies. 
 *    The range of values allowed for duration is the following in ISO
 *    8601 notation: <code>P[n][n]DT[n]H[n]M[n]S</code> or <code>P[n]W</code>.
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>DURATION</code> subset ASN.1 datatype</li>
 *   <li><code>timeinterval(10,3)</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>Duration</code> subset XMLSchema built-in datatype</li>
 *   <li><code>INTERVAL</code> of day, hour, minute, second in SQL2003</li>
 *  </ul>
 *  
 * <p>Internally, values of this type are represented as {@link Long} objects that
 *   represent the number of milliseconds elapsed, and must be a natural number (non-negative)</p>
 * 
 * @author Carl Eric Codere
 *
 */
public class DurationType extends PrimitiveType implements OrderedFacet, DecimalRangeFacet, TimeUnitFacet
{
  public static final DurationType DEFAULT_INSTANCE = new DurationType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  protected DecimalRangeHelper rangeHelper;
  
  protected TimeUnit timeUnit;
  
  /** Creates a duration/timeinterval type with
   *  a default unit of milliseconds.
   * 
   */
  public DurationType()
  {
    super(Datatype.OTHER,true);
    rangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(0),null,false);
    timeUnit = TimeUnit.MILLISECONDS;
  }
  
  public DurationType(TimeUnit unit)
  {
    super(Datatype.OTHER,true);
    rangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(0),null,false);
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
      if (otherObj.timeUnit.equals(timeUnit)==false)
      {
        return false;
      }
      
      return true;
  }

  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    if (Number.class.isInstance(value))
    {
      return toValue((Number)value,conversionResult);
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }

  public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
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
    {
      bigDecimal = BigDecimal.valueOf(ordinalValue.longValue());
    }
    if (validateRange(bigDecimal)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      return null;
    }
    return new Long(bigDecimal.longValue());
  }

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    if (validateRange(ordinalValue)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      return null;
    }
    return new Long(ordinalValue);
  }

  public boolean isBounded()
  {
    return rangeHelper.isBounded();
  }

  public boolean isRestriction(Type value)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public boolean isSubset(Type type)
  {
    return rangeHelper.isSubset(type);
  }

  public BigDecimal getMinInclusive()
  {
    return rangeHelper.getMinInclusive();
  }

  public BigDecimal getMaxInclusive()
  {
    return rangeHelper.getMaxInclusive();
  }

  public boolean validateRange(long value)
  {
    return rangeHelper.validateRange(value);
  }

  public boolean validateRange(BigDecimal value)
  {
    return rangeHelper.validateRange(value);
  }

  public TimeUnit getTimeUnit()
  {
    return timeUnit;
  }
}
