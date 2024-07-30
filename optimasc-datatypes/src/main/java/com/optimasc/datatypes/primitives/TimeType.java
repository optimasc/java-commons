package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DateTimeEnumerationFacet;
import com.optimasc.datatypes.DateTimeEnumerationHelper;
import com.optimasc.datatypes.DecimalRangeHelper;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.Restriction;
import com.optimasc.datatypes.TimeUnitFacet;
import com.optimasc.datatypes.TimeFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.defined.UnsignedByteType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateConverter;
import com.optimasc.date.DateTime;
import com.optimasc.date.DateTimeFormat;
import com.optimasc.date.DateTime.Time;
import com.optimasc.date.DateTimeFormat.TimeUnit;
import com.optimasc.date.TimeComparator;
import com.optimasc.lang.GregorianDatetimeCalendar;

/** Datatype that represents an instant of time that recurs every day. 
 *  The value space of time is the space of time of day value in 24-hour
 *  format.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>time</code> XMLSchema built-in datatype</li>
 *   <li><code>TIME</code> in SQL2003</li>
 *  </ul>
 *  
 *  <p>A time of day also has a unit base, such as seconds or 
 *  milliseconds, and it can be either be a 'local time' where the
 *  timezone is completely ignored even if set.</p>
 *  
 * <p>Internally, values of this type are represented as 
 * {@link GregorianDatetimeCalendar} objects or as an integer value
 * that represents the number of time units elapsed from midnight.</p>
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public class TimeType extends PrimitiveType implements TimeFacet, OrderedFacet, DateTimeEnumerationFacet
{
  private static TimeType defaultTypeInstance;
  private static TypeReference defaultTypeReference;
  
  
  /* TIME : (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])([\.,]\d+)?([zZ]|([\+-])([01]\d|2[0-3]):?([0-5]\d)?)? */
  protected static final String REGEX_PATTERN = "(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])(?:[\\.,](\\d+))?([zZ]|([\\+-])([01]\\d|2[0-3]):?([0-5]\\d)?)?";
  
  public static final int PATTERN_GROUP_HOURS = 1; 
  public static final int PATTERN_GROUP_MINUTES = 2; 
  public static final int PATTERN_GROUP_SECONDS = 3; 
  public static final int PATTERN_GROUP_FRACTIONAL = 4; 
  public static final int PATTERN_GROUP_TIMEZONE = 5;
  
  /** Minimum value for when the resolution is seconds
   *  and the time is 00:00:00.0000.
   */
  public static int MIN_VALUE_SECONDS = 0;
  
  /** Maximum value for when the resolution is seconds
   *  and the time is 23:59:60. It includes the 
   *  leap second value.
   */
  public static int MAX_VALUE_SECONDS = (24*60*60)+1-1;
  

  /** Maximum value for when the resolution is minutes
   *  and the time is 00:00:00 
   */
  public static int MIN_VALUE_MINUTES = 0;
  
  /** Maximum value for when the resolution is minutes
   *  and the time is 23:59. 
   */
  public static int MAX_VALUE_MINUTES = (24*60)-1;
  
  

  /** Minimum value for when the resolution is in milliseconds
   *  and the time is 00:00:00.0000.
   */
  public static int MIN_VALUE_MILLISECONDS = 0;
  
  /** Maximum value for when the resolution is miliseconds
   *  and the time is 23:59:60. It includes the 
   *  leap second value.
   */
  public static int MAX_VALUE_MILLISECONDS = MAX_VALUE_SECONDS*1000;
  
  /** The Class instance representing the value of this type. */ 
  public static final Class TYPE = GregorianDatetimeCalendar.class;
  
  protected int accuracy;
  protected boolean localTime;
  protected DateTimeEnumerationHelper enumHelper;
  protected DecimalRangeHelper rangeHelper;
  
  protected static final DecimalRangeHelper milliRangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(MIN_VALUE_MILLISECONDS),
      BigDecimal.valueOf(MAX_VALUE_MILLISECONDS));
  
  protected static final DecimalRangeHelper secondRangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(MIN_VALUE_SECONDS),
      BigDecimal.valueOf(MAX_VALUE_SECONDS));
  
  protected static final DecimalRangeHelper minuteRangeHelper =  new DecimalRangeHelper(BigDecimal.valueOf(MIN_VALUE_MINUTES),
      BigDecimal.valueOf(MAX_VALUE_MINUTES));

  
  

  /** Creates a time type with an accuracy of 
   *  a second and which contains no timezone information.
   *  
   *  <p>This is equivalent to the <code>TIME-OF-DAY</code>
   *  ASN.1 datatype (ITU-T X.680 later editions).</p>
   *  
   */
  public TimeType()
  {
    this(DateTime.TimeAccuracy.SECOND, true);
  }
  
  public TimeType(int accuracy, boolean localTime)
  {
    this(accuracy,localTime,null);
  }
  
  public TimeType(int accuracy, boolean localTime, Calendar[] choices)
  {
    super(true);
    validateAccuracy(accuracy);
    this.accuracy = accuracy;
    this.localTime = localTime;
    this.enumHelper = new DateTimeEnumerationHelper(GregorianCalendar.class,new TimeComparator(accuracy,localTime));
    if (choices != null)
    {
      enumHelper.setChoices(choices);
    }
  }
  

  public Class getClassType()
  {
    return TYPE;
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    
    /** Compares this TimeType to the specified object. 
     *  The result is true if and only if the argument is not null 
     *  and is a TimeType object and they have the same time unit
     *  and timezone information indicator.
     * 
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
        if (!(obj instanceof TimeType))
        {
            return false;
        }
        TimeType otherObject = (TimeType) obj;
        if (otherObject.accuracy != accuracy)
        {
          return false;
        }
        if (otherObject.localTime != localTime)
        {
          return false;
        }
        if ((otherObject.enumHelper==null) && (enumHelper!=null))
        {
          return false;
        }
        
        if ((otherObject.enumHelper!=null) && (enumHelper==null))
        {
          return false;
        }
        
        // No enumeration constraint for both, then its true
        if ((otherObject.enumHelper==null) && (enumHelper==null))
        {
          return true;
        }
        
        
        if (otherObject.enumHelper.equals(enumHelper)==false)
        {
          return false;
        }
        
        return true;
    }

    /** {@inheritDoc}
     * 
     *  <p>Additionally the value returned by this method is always
     *  normalised to UTC if this datatype is defined as supporting
     *  Timezone information.</p>
     *  
     *  <p>It supports the following input types:
     *    <ul>
     *      <li>A {@link java.util.GregorianCalendar} object, where only
     *       the time part and timzeone (depending on type definition) will
     *       be used.</li>
     *      <li>A {@link java.lang.Number} object that can be converted to an integer value space
     *       and will be used according to time unit to set the correct time. In the case
     *       this type is defined as supporting timezone information, it is assumed that this
     *       number represents a time normalized to the UTC timezone, otherwise it is considered
     *       a local time.</li>
     *      <li>A {@link java.util.Date} object. In the case
     *       this type is defined as supporting timezone information, it is assumed that this
     *       date represents a time normalized to the UTC timezone.</li>
     *      <li>A {@link com.optimasc.date.DateConverter.Time} object.</li>
     *    </ul>
     *  </p>
     *  
     *  @return A {@link com.optimasc.lang.GregorianDatetimeCalendar} object compatible
     *    with the <code>GregorianCalendar</code> class with the correct fields set and
     *    normalized to UTC if there is timezone information support.
     * 
     */
    public Object toValue(Object value, TypeCheckResult conversionResult)
    {
      conversionResult.reset();
      
      // If date convert it to a calendar and continue and 
      // pass through to next step
      if (value instanceof java.util.Date)
      {
        java.util.Date d = (Date) value;
        Calendar cal = new GregorianDatetimeCalendar();
        if (localTime==false)
        {
          cal.setTimeZone(DateTime.ZULU);
        }
        // The value from date is always in UTC
        cal.setTimeInMillis(d.getTime());
        value = cal;
      }
      
      if (value instanceof GregorianCalendar)
      {
        Calendar inputCalendar = (Calendar) value;
        Calendar cal = new GregorianDatetimeCalendar();
        if (localTime == false)
        {
           cal.setTimeZone(DateTime.ZULU);
           inputCalendar = DateTime.normalize(inputCalendar);
        }
        // Only set the correct fields depending on the accuracy value
        cal.set(Calendar.HOUR_OF_DAY, inputCalendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, inputCalendar.get(Calendar.MINUTE));
        if (accuracy == DateTime.TimeAccuracy.SECOND)
        {
          cal.set(Calendar.SECOND, inputCalendar.get(Calendar.SECOND));
        }
        if (accuracy == DateTime.TimeAccuracy.MILLISECOND)
        {
          cal.set(Calendar.SECOND, inputCalendar.get(Calendar.SECOND));
          cal.set(Calendar.MILLISECOND, inputCalendar.get(Calendar.MILLISECOND));
        }
        if (validateChoice(cal)==false)
        {
          conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Value is not one of the values allowed by enumeration.");
          return null;
        }
        return cal;
      }
      
      if (value instanceof Number)
      {
        return toValue(((Number)value).longValue(),conversionResult);
      }
      
      if (value instanceof Time)
      {
        Time t = (Time) value;
        Calendar cal = new GregorianDatetimeCalendar();
        if ((localTime==false) && (t.localTime==false))
        {
          cal.setTimeZone(DateTime.ZULU);
        } else
        if ((localTime==true) && (t.localTime==true))
        {
          
        } else
        {
          conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"localTime for type and value are not compatible.");
          return null;
        }
        
        cal.set(Calendar.HOUR_OF_DAY, t.hour);
        cal.set(Calendar.MINUTE, t.minute);
        if (accuracy == DateTime.TimeAccuracy.SECOND)
        {
          cal.set(Calendar.SECOND, t.second);
        }
        if (accuracy == DateTime.TimeAccuracy.MILLISECOND)
        {
          cal.set(Calendar.SECOND, t.second);
          cal.set(Calendar.MILLISECOND, t.millisecond);
        }
        if (validateChoice(cal)==false)
        {
          conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Value is not one of the values allowed by enumeration.");
          return null;
        }
        return cal;
      }
      
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
      return null;
    }

    public int getAccuracy()
    {
      return accuracy;
    }

    public boolean isLocalTime()
    {
      return localTime;
    }


    /** Tries to convert the numeric value representing the number of units, as 
     *  specified by this type's accuracy, of time elapsed since midnight to
     *  a <code>GregoriandCalendar</code> representation.  
     */
    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      conversionResult.reset();
      Time timeResult = null;
      if (accuracy == DateTime.TimeAccuracy.MINUTE)
      {
        if (validateRange(ordinalValue)==false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,
              "The numeric value does not represent a valid number "
              + "of minutes elapsed since midnight.");
          return null;
        }
        // Convert the value in seconds to milliseconds, and then get the time
        // components.
        timeResult = DateTime.Time.toTime((int)(ordinalValue*60*1000), localTime);
      } else
      if (accuracy == DateTime.TimeAccuracy.SECOND)
      {
        if (validateRange(ordinalValue)==false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,
              "The numeric value does not represent a valid number "
              + "of seconds elapsed since midnight.");
          return null;
        }
        // Convert the value in seconds to milliseconds, and then get the time
        // components.
        timeResult = DateTime.Time.toTime((int)(ordinalValue*1000), localTime);
      } else
      if (accuracy == DateTime.TimeAccuracy.MILLISECOND)
      {
        if (validateRange(ordinalValue)==false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,
              "The numeric value does not represent a valid number "
              + "of milliseconds elapsed since midnight.");
          return null;
        }
        timeResult = DateTime.Time.toTime((int)ordinalValue, localTime);
      }
        
      
      Calendar cal;
      if (localTime == false)
      {
         cal = new GregorianDatetimeCalendar();
         cal.setTimeZone(GregorianDatetimeCalendar.ZULU);
      } else
      {
        cal = new GregorianDatetimeCalendar();
      }
      
      cal.set(Calendar.HOUR_OF_DAY, timeResult.hour);
      cal.set(Calendar.MINUTE, timeResult.minute);
      cal.set(Calendar.SECOND, timeResult.second);
      if (accuracy == DateTime.TimeAccuracy.MILLISECOND)
      {
        cal.set(Calendar.MILLISECOND, timeResult.millisecond);
      }
      if (validateChoice(cal)==false)
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"The value is not within the list of allowed value as defined by"
            + "the enumration.");
        return null;
      }
      return cal;
   }
    
   /** Verifies the validity of the accuracy for this time type.
    * 
    * @param acc [in] The accuracy for this time type.
    * @throws IllegalArgumentException Thrown if the
    *   accuracy is not supported for this time type.
    */
   protected void validateAccuracy(int acc)
   {
     switch (acc)
     {
       case DateTime.TimeAccuracy.MILLISECOND:
         rangeHelper = milliRangeHelper;
         break;
       case DateTime.TimeAccuracy.SECOND:
         rangeHelper = secondRangeHelper;
         break;
       case DateTime.TimeAccuracy.MINUTE:
         rangeHelper = minuteRangeHelper;
         break;
       default:
       throw new IllegalArgumentException("Unsupported accuracy for this time type.");
     }
   }

  public Calendar[] getChoices()
  {
    return enumHelper.getChoices();
  }

  public boolean validateChoice(Calendar value)
  {
    return enumHelper.validateChoice(value);
  }

  /** {@inheritDoc}
   * 
   *  <p>Specifically, this object will be 
   *  considered a restriction, in the following
   *  cases:</p>
   *  
   *  <ul>
   *   <li>The accuracy of this object is less than
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
    if ((value instanceof TimeType)==false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '"+value.getClass().getName()+"'.");
    }
    TimeType otherTimeType = (TimeType) value;
    if (accuracy < otherTimeType.accuracy)
    {
      return true;
    }
    
    Object[] choices = enumHelper.getChoices();
    Object[] otherChoices = otherTimeType.getChoices();
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

  public boolean isBounded()
  {
    return true;
  }
  
  public static TypeReference getInstance()
  {
    if (defaultTypeInstance == null)
    {
      defaultTypeInstance = new TimeType();
      defaultTypeReference = new UnnamedTypeReference(defaultTypeInstance);
    }
    return defaultTypeReference; 
  }

}
