package com.optimasc.datatypes.primitives;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.TimeUnitFacet;
import com.optimasc.datatypes.TimezoneFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateConverter;
import com.optimasc.date.DateTime;
import com.optimasc.date.DateTime.Time;
import com.optimasc.lang.GregorianDatetimeCalendar;

/** Datatype that represents an instant of time that recurs every day. 
 *  The value space of time is the space of time of day value in 24-hour
 *  format.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>TIME-OF-DAY</code> ASN.1 datatype</li>
 *   <li><code>time</code> XMLSchema built-in datatype</li>
 *   <li><code>TIME</code> in SQL2003</li>
 *  </ul>
 *  
 *  <p>A time of day also has a unit base, such as seconds or 
 *  milliseconds, and it can be either be a 'local time' where the
 *  timezone is unknown. </p>
 *  
 * <p>Internally, values of this type are represented as 
 * {@link GregorianDatetimeCalendar} objects or as an integer value
 * that represents the number of time units elapsed from midnight.</p>
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public class TimeType extends Datatype implements TimeUnitFacet, TimezoneFacet, OrderedFacet
{
  public static final Datatype DEFAULT_INSTANCE = new TimeType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
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
  public static int MAX_VALUE_SECONDS = 24*60*61;
  
  
  
  /** Minimum value for when the resolution is in milliseconds
   *  and the time is 00:00:00.0000.
   */
  public static int MIN_VALUE_MILLISECONDS = 0;
  
  /** Maximum value for when the resolution is miliseconds
   *  and the time is 23:59:60. It includes the 
   *  leap second value.
   */
  public static int MAX_VALUE_MILLISECONDS = MAX_VALUE_SECONDS*1000;
  
  
  
  protected int timeUnit;
  protected boolean hasTimezone;
  

  /** Creates a time type with a resolution of 
   *  a millisecond and which contains timezone
   *  information.
   */
  public TimeType()
  {
    super(Datatype.TIME,true);
    timeUnit = TimeUnitFacet.UNIT_MILLISECOND;
    hasTimezone = true;
  }
  
  public TimeType(int resolution, boolean hasTimeZone)
  {
    super(Datatype.TIME,true);
    timeUnit = resolution;
    hasTimezone = hasTimeZone;
  }

  public Class getClassType()
  {
    return GregorianDatetimeCalendar.class;
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
        if (otherObject.timeUnit != timeUnit)
        {
          return false;
        }
        if (otherObject.hasTimezone != hasTimezone)
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
        cal.setTimeInMillis(d.getTime());
        value = cal;
      }
      
      if (value instanceof GregorianCalendar)
      {
        Calendar inputCalendar = (Calendar) value;
        if (hasTimezone == true)
        {
           inputCalendar = DateTime.normalize(inputCalendar);
        }
        Calendar cal = new GregorianDatetimeCalendar();
        cal.set(Calendar.HOUR_OF_DAY, inputCalendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, inputCalendar.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, inputCalendar.get(Calendar.SECOND));
        if (timeUnit == TimeUnitFacet.UNIT_MILLISECOND)
        {
          cal.set(Calendar.MILLISECOND, inputCalendar.get(Calendar.MILLISECOND));
        }
        return cal;
      }
      if (value instanceof Number)
      {
        return toValue((Number)value,conversionResult);
      }
      if (value instanceof Time)
      {
        Time t = (Time) value;
        Calendar cal = new GregorianDatetimeCalendar();
        
        if ((t.localTime==true) && (hasTimezone == true))
        {
          !!!
        }
        
        cal.set(Calendar.HOUR_OF_DAY, t.hour);
        cal.set(Calendar.MINUTE, t.minute);
        cal.set(Calendar.SECOND, t.second);
        if (timeUnit == TimeUnitFacet.UNIT_MILLISECOND)
        {
          cal.set(Calendar.MILLISECOND, t.millisecond);
        }
        return cal;
      }
      
      return null;
    }

    public int getTimeUnit()
    {
      return timeUnit;
    }

    public boolean hasTimezone()
    {
      return hasTimezone;
    }

    /** {@inheritDoc} 
     * 
     * <p>Converts the number value representing either seconds or 
     *  milliseconds elapsed since midnight to a 
     *  {@link com.optimasc.lang.GregorianDatetimeCalendar} object.</p>
     *  
     *  <p>If the value is outside the bounds allowed for the time,
     *  it returns a <code>null</code> value. </p>
     *  
     */
    public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
    {
      return toValue(ordinalValue.longValue(),conversionResult);
    }

    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      conversionResult.reset();
      Time timeResult = null;
      if (timeUnit == UNIT_SECOND)
      {
        if ((ordinalValue < MIN_VALUE_SECONDS) || (ordinalValue > MAX_VALUE_SECONDS))
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,
              "The numeric value does not represent a valid number "
              + "of seconds elapsed since midnight.");
          return null;
        }
        // Convert the value in seconds to milliseconds, and then get the time
        // components.
        timeResult = DateTime.Time.toTime((int)(ordinalValue*1000), hasTimezone==false);
      }
      if (timeUnit == UNIT_MILLISECOND)
      {
        if ((ordinalValue < MIN_VALUE_MILLISECONDS) || (ordinalValue > MAX_VALUE_MILLISECONDS))
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW,
              "The numeric value does not represent a valid number "
              + "of milliseconds elapsed since midnight.");
          return null;
        }
        timeResult = DateTime.Time.toTime((int)ordinalValue, hasTimezone==false);
      }
      
      Calendar cal;
      if (hasTimezone == true)
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
      if (timeUnit == TimeUnitFacet.UNIT_MILLISECOND)
      {
        cal.set(Calendar.MILLISECOND, timeResult.millisecond);
      }
      return cal;
   }  
}
