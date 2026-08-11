package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DateTimeEnumerationHelper;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.OrderedProperty;
import com.optimasc.datatypes.Restriction;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.defined.UnsignedByteType;
import com.optimasc.datatypes.facets.DateTimeEnumerationFacet;
import com.optimasc.datatypes.facets.EnumerationFacet;
import com.optimasc.datatypes.facets.TimeFacet;
import com.optimasc.datatypes.facets.TimeUnitFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateConverter;
import com.optimasc.date.DateTime;
import com.optimasc.date.DateTime.TimeAccuracy;
import com.optimasc.date.DateTimeFormat;
import com.optimasc.date.DateTime.Time;
import com.optimasc.date.DateTimeFormat.TimeUnit;
import com.optimasc.date.TimeComparator;
import com.optimasc.lang.GregorianDatetimeCalendar;
import com.optimasc.lang.NumberSelectItem;
import com.optimasc.lang.NumberedSelectItems;

/** Datatype that represents an instant of time that recurs every day. 
 *  The value space of time is the space of time of day value in 24-hour
 *  format.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>time</code> XMLSchema built-in datatype</li>
 *   <li><code>TIME</code> in SQL2003</li>
 *   <li><code>TIME-OF-DAY</code> ASN.1 datatype</li>
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
public class TimeType extends PrimitiveType implements TimeFacet, OrderedProperty, DateTimeEnumerationFacet
{
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
  public static int MAX_VALUE_SECONDS = (23*60*60) + (59*60) + 60;
  

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
  protected NumberedSelectItems.NumberSelectRange rangeHelper;
  
  protected static final NumberedSelectItems.NumberSelectRange milliRangeHelper = new NumberedSelectItems.NumberSelectRange(BigDecimal.valueOf(MIN_VALUE_MILLISECONDS),
      BigDecimal.valueOf(MAX_VALUE_MILLISECONDS));
  
  protected static final NumberedSelectItems.NumberSelectRange secondRangeHelper = new NumberedSelectItems.NumberSelectRange(BigDecimal.valueOf(MIN_VALUE_SECONDS),
      BigDecimal.valueOf(MAX_VALUE_SECONDS));
  
  protected static final NumberedSelectItems.NumberSelectRange minuteRangeHelper =  new NumberedSelectItems.NumberSelectRange(BigDecimal.valueOf(MIN_VALUE_MINUTES),
      BigDecimal.valueOf(MAX_VALUE_MINUTES));

  
  protected static final Calendar lowTime = new  GregorianDatetimeCalendar(0,0, 0, GregorianDatetimeCalendar.FIELD_UNDEFINED, 0);
  protected static final Calendar highTime = new GregorianDatetimeCalendar(23,59, 59, GregorianDatetimeCalendar.FIELD_UNDEFINED, 0);
  protected static final Calendar lowTimeMilli = new  GregorianDatetimeCalendar(0,0, 0, 00, 00);
  protected static final Calendar highTimeMilli = new GregorianDatetimeCalendar(23,59, 59, 999, 00);
  protected static final Calendar lowTimeMin = new  GregorianDatetimeCalendar(0,0, GregorianDatetimeCalendar.FIELD_UNDEFINED, GregorianDatetimeCalendar.FIELD_UNDEFINED, 00);
  protected static final Calendar highTimeMin = new GregorianDatetimeCalendar(23,59, GregorianDatetimeCalendar.FIELD_UNDEFINED, GregorianDatetimeCalendar.FIELD_UNDEFINED, 00);
  
  protected static final Calendar lowTimeNoTZ = new  GregorianDatetimeCalendar(0,0, 0, GregorianDatetimeCalendar.FIELD_UNDEFINED, GregorianDatetimeCalendar.FIELD_UNDEFINED);
  protected static final Calendar highTimeNoTZ = new GregorianDatetimeCalendar(23,59, 59, GregorianDatetimeCalendar.FIELD_UNDEFINED, GregorianDatetimeCalendar.FIELD_UNDEFINED);
  protected static final Calendar lowTimeMilliNoTZ = new  GregorianDatetimeCalendar(0,0, 0, 00, GregorianDatetimeCalendar.FIELD_UNDEFINED);
  protected static final Calendar highTimeMilliNoTZ = new GregorianDatetimeCalendar(23,59, 59, 999, GregorianDatetimeCalendar.FIELD_UNDEFINED);
  protected static final Calendar lowTimeMinNoTZ = new  GregorianDatetimeCalendar(0,0, GregorianDatetimeCalendar.FIELD_UNDEFINED, GregorianDatetimeCalendar.FIELD_UNDEFINED, GregorianDatetimeCalendar.FIELD_UNDEFINED);
  protected static final Calendar highTimeMinNoTZ = new GregorianDatetimeCalendar(23,59, GregorianDatetimeCalendar.FIELD_UNDEFINED, GregorianDatetimeCalendar.FIELD_UNDEFINED, GregorianDatetimeCalendar.FIELD_UNDEFINED);
  
  protected GregorianTimeComparator timeComparator;
  
  
  /**
   * A comparator that compares the fields associated with the time in calendar
   * objects up to the specified accuracy. It supports both ignoring (when
   * <code>localTime</code> is set) timezone or normalizing to UTC before
   * comparing.
   * 
   * @author Carl Eric Codere.
   */
  public static class GregorianTimeComparator implements Comparator
  {
    protected int accuracy;
    protected boolean localTime;

    /**
     * @param accuracy
     *          [in] The accuracy to which the compare fields against.
     * @param localTime
     *          [in] If comparison will ignore timezones or not.
     */
    public GregorianTimeComparator(int accuracy, boolean localTime)
    {
      this.accuracy = accuracy;
      this.localTime = localTime;
    }

    protected int timeCompare(Calendar left, Calendar right)
    {
      // Normalize both times to UTC before comparing, only if
      // these are not local times.
      if (localTime == false)
      {
        left = (Calendar) DateTime.normalize(left);
        right = (Calendar) DateTime.normalize(right);
      }

      // Handle endOfDay sentinel for GregorianDatetimeCalendar instances,
      // since internally 24:00:00 is stored as 00:00:00 in Calendar fields.
    int leftHour = (left instanceof GregorianDatetimeCalendar)
          && ((GregorianDatetimeCalendar) left).isEndOfDay() ? 24
          : left.get(Calendar.HOUR_OF_DAY);
      int rightHour = (right instanceof GregorianDatetimeCalendar)
          && ((GregorianDatetimeCalendar) right).isEndOfDay() ? 24
          : right.get(Calendar.HOUR_OF_DAY);

      if (leftHour < rightHour) return -1;
      if (leftHour > rightHour) return 1;

      // Hours are equal, check minutes
      int leftMinute = left.get(Calendar.MINUTE);
      int rightMinute = right.get(Calendar.MINUTE);

      if (accuracy == DateTime.TimeAccuracy.MINUTE)
      {
        if (leftMinute < rightMinute) return -1;
        if (leftMinute > rightMinute) return 1;
        return 0;
      }

      if (leftMinute < rightMinute) return -1;
      if (leftMinute > rightMinute) return 1;

      // Minutes are equal, check seconds
      int leftSecond = left.get(Calendar.SECOND);
      int rightSecond = right.get(Calendar.SECOND);

      if (accuracy == DateTime.TimeAccuracy.SECOND)
      {
        if (leftSecond < rightSecond) return -1;
        if (leftSecond > rightSecond) return 1;
        return 0;
      }

      if (leftSecond < rightSecond) return -1;
      if (leftSecond > rightSecond) return 1;

      // Seconds are equal, check milliseconds
      int leftMillis = left.get(Calendar.MILLISECOND);
      int rightMillis = right.get(Calendar.MILLISECOND);

      if (leftMillis < rightMillis) return -1;
      if (leftMillis > rightMillis) return 1;
      return 0;
    }

    public int compare(Object o1, Object o2)
    {
      return timeCompare((Calendar) o1, (Calendar) o2);
    }
  }
  
  
  protected Calendar minValue;
  protected Calendar maxValue;
  

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
    timeComparator = new GregorianTimeComparator(accuracy,localTime);
    this.enumHelper = new DateTimeEnumerationHelper(timeComparator);
    if (choices != null)
    {
      enumHelper.setAllowedValues(choices);
    }
    if (accuracy == TimeAccuracy.SECOND)
    {
      if (localTime == false)
      {
        minValue = lowTime;
        maxValue = highTime;
      } else
      {
        minValue = lowTimeNoTZ;
        maxValue = highTimeNoTZ;
        
      }
    }
    if (accuracy == TimeAccuracy.MILLISECOND)
    {
      if (localTime == false)
      {
        minValue = lowTimeMilli;
        maxValue = highTimeMilli;
      } else
      {
        minValue = lowTimeMilliNoTZ;
        maxValue = highTimeMilliNoTZ;
        
      }
    }
    if (accuracy == TimeAccuracy.MINUTE)
    {
      if (localTime == false)
      {
        minValue = lowTimeMin;
        maxValue = highTimeMin;
      } else
      {
        minValue = lowTimeMinNoTZ;
        maxValue = highTimeMinNoTZ;
        
      }
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
      
      if (value instanceof java.util.Date)
      {
        java.util.Date d = (Date) value;
        GregorianCalendar cal = new GregorianCalendar();
        if (localTime == false)
        {
          cal.setTimeZone(DateTime.ZULU);
        }
        cal.setTimeInMillis(d.getTime());
        value = cal;
      }
      
      if (value instanceof GregorianCalendar)
      {
        Calendar inputCalendar = (Calendar) value;
        if (localTime == false)
        {
          inputCalendar = DateTime.normalize(inputCalendar);
        }
        int calHour = inputCalendar.get(Calendar.HOUR_OF_DAY);
        int calMinute = inputCalendar.get(Calendar.MINUTE);
        int calSecond = GregorianDatetimeCalendar.FIELD_UNDEFINED;
        int calMillis = GregorianDatetimeCalendar.FIELD_UNDEFINED;
        int calTz = localTime ? GregorianDatetimeCalendar.FIELD_UNDEFINED : 0;
        if (accuracy == DateTime.TimeAccuracy.SECOND)
        {
          calSecond = inputCalendar.get(Calendar.SECOND);
        }
        if (accuracy == DateTime.TimeAccuracy.MILLISECOND)
        {
          calSecond = inputCalendar.get(Calendar.SECOND);
          calMillis = inputCalendar.get(Calendar.MILLISECOND);
        }
        Calendar cal = new GregorianDatetimeCalendar(calHour, calMinute, calSecond, calMillis, calTz);
        if (isValid(cal) == false)
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
        if ((localTime == false) && (t.localTime == false))
        {
        } else
        if ((localTime == true) && (t.localTime == true))
        {
        } else
        {
          conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"localTime for type and value are not compatible.");
          return null;
        }
        int calSecond = GregorianDatetimeCalendar.FIELD_UNDEFINED;
        int calMillis = GregorianDatetimeCalendar.FIELD_UNDEFINED;
        int calTz = localTime ? GregorianDatetimeCalendar.FIELD_UNDEFINED : 0;
        if (accuracy == DateTime.TimeAccuracy.SECOND)
        {
          calSecond = t.second;
        }
        if (accuracy == DateTime.TimeAccuracy.MILLISECOND)
        {
          calSecond = t.second;
          calMillis = t.millisecond;
        }
        Calendar cal = new GregorianDatetimeCalendar(t.hour, t.minute, calSecond, calMillis, calTz);
        if (isValid(cal) == false)
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
        if (validateChoice(ordinalValue)==false)
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
        if (validateChoice(ordinalValue)==false)
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
        if (validateChoice(ordinalValue)==false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,
              "The numeric value does not represent a valid number "
              + "of milliseconds elapsed since midnight.");
          return null;
        }
        timeResult = DateTime.Time.toTime((int)ordinalValue, localTime);
      }
        
      
      int calSecond = GregorianDatetimeCalendar.FIELD_UNDEFINED;
      int calMillis = GregorianDatetimeCalendar.FIELD_UNDEFINED;
      int calTz = localTime ? GregorianDatetimeCalendar.FIELD_UNDEFINED : 0;
      if (accuracy == DateTime.TimeAccuracy.SECOND)
      {
        calSecond = timeResult.second;
      }
      if (accuracy == DateTime.TimeAccuracy.MILLISECOND)
      {
        calSecond = timeResult.second;
        calMillis = timeResult.millisecond;
      }
      Calendar cal = new GregorianDatetimeCalendar(timeResult.hour, timeResult.minute, calSecond, calMillis, calTz);
      if (isValid(cal)==false)
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

  public Calendar[] getAllowedValuesAsCalendars()
  {
    return enumHelper.getAllowedValuesAsCalendars();
  }

  public boolean isValid(Object value)
  {
    if (value instanceof Number)
    {
      TypeCheckResult result = new TypeCheckResult();
      Calendar cal =  (Calendar) toValue(((Number)value).longValue(),result);
      if (result.error!=null)
        return false;
      value = cal;
    }
    Calendar min = getMinInclusive();
    Calendar max = getMaxInclusive();
    
    if (timeComparator.compare(min, value)>0)
      return false;
    if (timeComparator.compare(value,max)>0)
      return false;
    
    if (enumHelper.getAllowedValues()!=null)
    {
      return enumHelper.isValid(value);
    }
    return true;
  }
  
  
  public boolean isValid(long value)
  {
    TypeCheckResult result = new TypeCheckResult();
    Calendar cal = (Calendar) toValue(value,result);
    if (result.error!=null)
      return false;
    return enumHelper.isValid(cal);
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
    
    Object[] choices = enumHelper.getAllowedValuesAsCalendars();
    Object[] otherChoices = otherTimeType.getAllowedValuesAsCalendars();
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

  /** Returns if the ordinal value is within the 
   *  allowed range for this specific accuracy.
   * 
   * @param value [in] The number of units since midnight.
   * @return <code>true</code> if the value is valud, otherwise <code>false</code>.
   */
  protected boolean validateChoice(long value)
  {
    return NumberedSelectItems.validateValue(new NumberSelectItem[]{rangeHelper}, new Long(value));
  }

  public boolean isBounded()
  {
    return true;
  }
  

  public void setLocalTime(boolean localTime)
  {
    this.localTime = localTime;
  }

  public void setAccuracy(int accuracy)
  {
    validateAccuracy(accuracy);
    this.accuracy = accuracy;
  }

  public void setAllowedValues(Object[] choices)
  {
    enumHelper.setAllowedValues(choices);
  }

  public Calendar getMinInclusive()
  {
    return minValue;
//    return enumHelper.getMinInclusive();
  }

  public Calendar getMaxInclusive()
  {
    return maxValue;
//    return enumHelper.getMaxInclusive();
  }

  public Object[] getAllowedValues()
  {
    return enumHelper.getAllowedValues();
  }

  public Class getAllowedValuesClass()
  {
    return enumHelper.getAllowedValuesClass();
  }
  
  

}
