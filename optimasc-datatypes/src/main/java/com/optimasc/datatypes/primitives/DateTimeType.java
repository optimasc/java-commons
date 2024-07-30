package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.BoundedFacet;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DateTimeEnumerationFacet;
import com.optimasc.datatypes.DateTimeEnumerationHelper;
import com.optimasc.datatypes.DecimalRangeFacet;
import com.optimasc.datatypes.TimeFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateTime;
import com.optimasc.date.DateTimeComparator;
import com.optimasc.lang.GregorianDatetimeCalendar;

/**
 * Abstract Datatype that represents a date and time with a specified
 * resolution. This can be used to represent dates or timestamps.
 * 
 * This may be equivalent to the following datatypes, depending on resolution:
 * <ul>
 * <li><code>GeneralizedTime</code>, <code>DATE</code> or <code>DATE-TIME</code>
 * ASN.1 datatype</li>
 * <li><code>time</code> ISO/IEC 11404 General purpose datatype</li>
 * <li><code>dateTime</code> or <code>date</code> XMLSchema built-in datatype</li>
 * <li><code>DATE</code> or <code>TIMESTAMP</code> in SQL2003</li>
 * </ul>
 * 
 * <p>
 * The default non parameter constructor creates equivalent to an undefined
 * resolution.
 * </p>
 * 
 * <p>Internally, values of this type are represented as 
 * {@link GregorianDatetimeCalendar} objects or as an integer value
 * that represents the number of resolution units elapsed from epoch.</p>
 * 
 * @author Carl Eric Codère
 */
public class DateTimeType extends PrimitiveType implements BoundedFacet, TimeFacet, DateTimeEnumerationFacet
{
 
  private static DateTimeType defaultTypeInstance;
  private static TypeReference defaultTypeReference;
  
  
  protected static final String REGEX_PATTERN = "([0-9][0-9][0-9][0-9])(?:(?:-(0[1-9]|1[0-2])(?:-([12]\\d|0[1-9]|3[01]))?)?(?:T(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])(?:[\\.,](\\d+))?([zZ]|([\\+-])([01]\\d|2[0-3]):?([0-5]\\d)?)?)?)";
  protected static final String PATTERN_YEAR = "([0-9][0-9][0-9][0-9])";
  protected static final String PATTERN_MONTH = "(0[1-9]|1[0-2])";
  protected static final String PATTERN_DAY = "([12][0-9]|0[1-9]|3[01])";
  protected static final String PATTERN_TIME = TimeType.REGEX_PATTERN;

  protected static final int MINUTE_MINIMUM = 0;
  protected static final int MINUTE_MAXIMUM = 59;
  protected static final int SECOND_MINIMUM = 0;
  protected static final int SECOND_MAXIMUM = 59;
  protected static final int HOUR_MINIMUM = 0;
  protected static final int HOUR_MAXIMUM = 24;
  protected static final int DAY_OF_MONTH_MINIMUM = 01;
  protected static final int DAY_OF_MONTH_MAXIMUM = 31;
  // -14 hours converted to milliseconds.
  protected static final int TIMEZONE_OFFSET_MIN = -50400000;
  // +14 hours converted to milliseconds.
  protected static final int TIMEZONE_OFFSET_MAX = +50400000;

  /** The accuracy of this date/time */
  protected int accuracy;
  /** Is this a local time or is this a date-time with a timezone */
  protected boolean localTime;
  protected DateTimeEnumerationHelper enumHelper;
  protected Calendar minInclusive;
  protected Calendar maxInclusive;
  protected DateTimeComparator comparator;


  /**
   * Creates a standard date time type with an accuracy
   * to the second using local time and unlimited
   * encoded range.
   * 
   *  <p>This is equivalent to the <code>DATE-TIME</code>
   *  ASN.1 datatype (ITU-T X.680 later editions).</p>
   * 
   */
  public DateTimeType()
  {
    this(DateTime.TimeAccuracy.SECOND, true,null);
  }
  
  /** Creates a date time type with the specified 
   *  accuracy and unlimited encoded range.
   * 
   * @param accuracy [in] The accuracy of this 
   *   date and time value.
   * @param localTime [in] true if this is a local time,
   *   hence with no timezone.
   */
  public DateTimeType(int accuracy, boolean localTime)
  {
    this(accuracy,localTime,null);
  }
  
  /** Creates a date time type with the specified 
   *  accuracy, restricted selecting option. The
   *  bounds of allowed values will be equal to
   *  the lowest selecting and option value respectively.
   * 
   * @param accuracy [in] The accuracy of this 
   *   date and time value.
   * @param localTime [in] true if this is a local time,
   *   hence with no timezone.
   * @param choices [in] Selection restriction, only these
   *  allowed values shall be considered valid.  
   */
  public DateTimeType(int accuracy, boolean localTime, Calendar[] choices)
  {
    super(true);
    validateAccuracy(accuracy);
    this.accuracy = accuracy;
    this.localTime = localTime;
    this.comparator= new DateTimeComparator(accuracy,localTime);
    this.enumHelper = new DateTimeEnumerationHelper(GregorianCalendar.class,comparator);
    if (choices != null)
    {
      enumHelper.setChoices(choices);
    }
    Calendar sortedEnumeration[] = enumHelper.getChoices();
    if (sortedEnumeration != null)
    {
      minInclusive = sortedEnumeration[0];
      maxInclusive = sortedEnumeration[sortedEnumeration.length-1];
    }
  }
  
  /** Creates a date time type with the specified 
   *  accuracy, and bounded range.
   * 
   * @param accuracy [in] The accuracy of this 
   *   date and time value.
   * @param localTime [in] true if this is a local time,
   *   hence with no timezone.
   * @param minInclusive [in] The minimum calendar value 
   *   allowed.
   * @param maxInclusive [in] The maximum calendar value 
   *   allowed.
   * @throws IllegalArgumentException if <code>minInclusive</code>
   *   is value pointing after <code>maxInclusive</code>.  
   */
  public DateTimeType(int accuracy, boolean localTime, Calendar minInclusive, Calendar maxInclusive)
  {
    super(true);
    validateAccuracy(accuracy);
    this.accuracy = accuracy;
    this.localTime = localTime;
    this.comparator= new DateTimeComparator(accuracy,localTime);
    this.enumHelper = new DateTimeEnumerationHelper(GregorianCalendar.class,comparator);
    this.minInclusive = minInclusive;
    this.maxInclusive = maxInclusive;
    if ((minInclusive != null) && (maxInclusive != null))
    {
      if (comparator.compare(maxInclusive, minInclusive)==-1)
      {
        throw new IllegalArgumentException("'maxInclusive' points to a point in time before 'minInclusive'");
      }
    }
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
      case DateTime.TimeAccuracy.YEAR:
      case DateTime.TimeAccuracy.DAY:
      case DateTime.TimeAccuracy.MILLISECOND:
      case DateTime.TimeAccuracy.SECOND:
      case DateTime.TimeAccuracy.MINUTE:
        break;
      default:
      throw new IllegalArgumentException("Unsupported accuracy for this datetime type.");
    }
  }
  
  
  
  
  public int getAccuracy()
  {
    return accuracy;
  }

  public Class getClassType()
  {
    return GregorianDatetimeCalendar.class;
  }

  /** A date time specification is equal to another date time  
   *  specification if the following conditions are met:
   *  
   *  <ul>
   *   <li>The resolution of both date-time are equal</li>
   *   <li>Both require timezone information or represent local date and time</li>
   *  </ul>
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

    DateTimeType datetimeType;
    if (!(obj instanceof DateTimeType))
    {
      return false;
    }
    datetimeType = (DateTimeType) obj;
    if (this.getAccuracy() != datetimeType.getAccuracy())
    {
      return false;
    }
    if (this.isLocalTime() != datetimeType.isLocalTime())
    {
      return false;
    }
    
    if ((datetimeType.enumHelper==null) && (enumHelper!=null))
    {
      return false;
    }
    
    if ((datetimeType.enumHelper!=null) && (enumHelper==null))
    {
      return false;
    }
    
    // No enumeration constraint for both, then its true
    if ((datetimeType.enumHelper==null) && (enumHelper==null))
    {
      return true;
    }
    
    
    if (datetimeType.enumHelper.equals(enumHelper)==false)
    {
      return false;
    }
    
    return true;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * This method supports the following input types that will be verified and
   * compared to and will be converted to the correct class type:
   * <ul>
   * <li>{@link com.optimasc.lang.GregorianDatetimeCalendar}</li>
   * <li>{@link com.optimasc.date.DateConverter.DateTime}</li>
   * <li>{@link java.util.Date}</li>
   * </ul>
   * </p>
   * 
   */
  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
    
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
    
    if (value instanceof DateTime)
    {
      DateTime dt = (DateTime) value;
      Calendar cal = dt.toCalendar();
      if (localTime==false)
      {
        cal.setTimeZone(DateTime.ZULU);
      }
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
      cal.set(Calendar.ERA, inputCalendar.get(Calendar.ERA));
      cal.set(Calendar.YEAR, inputCalendar.get(Calendar.YEAR));
      
      if (accuracy == DateTime.TimeAccuracy.DAY)
      {
        cal.set(Calendar.MONTH, inputCalendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, inputCalendar.get(Calendar.DAY_OF_MONTH));
      }
      if (accuracy == DateTime.TimeAccuracy.MINUTE)
      {
        cal.set(Calendar.MONTH, inputCalendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, inputCalendar.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, inputCalendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, inputCalendar.get(Calendar.MINUTE));
      }
      if (accuracy == DateTime.TimeAccuracy.SECOND)
      {
        cal.set(Calendar.MONTH, inputCalendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, inputCalendar.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, inputCalendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, inputCalendar.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, inputCalendar.get(Calendar.SECOND));
      }
      if (accuracy == DateTime.TimeAccuracy.SECOND)
      {
        cal.set(Calendar.MONTH, inputCalendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, inputCalendar.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, inputCalendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, inputCalendar.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, inputCalendar.get(Calendar.SECOND));
      }
      if (accuracy == DateTime.TimeAccuracy.MILLISECOND)
      {
        cal.set(Calendar.MONTH, inputCalendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, inputCalendar.get(Calendar.DAY_OF_MONTH));
        cal.set(Calendar.HOUR_OF_DAY, inputCalendar.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, inputCalendar.get(Calendar.MINUTE));
        cal.set(Calendar.SECOND, inputCalendar.get(Calendar.SECOND));
        cal.set(Calendar.MILLISECOND, inputCalendar.get(Calendar.MILLISECOND));
      }      
      if (validateChoice(cal)==false)
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Value is not one of the values allowed by enumeration.");
        return null;
      }
      if (validateRange(cal)==false)
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Value is not one of the values allowed by enumeration.");
        return null;
      }
      return cal;
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }
  
  public boolean isLocalTime()
  {
    return localTime;
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
      if ((value instanceof DateTimeType)==false)
      {
        throw new IllegalArgumentException("Expecting parameter of type '"+value.getClass().getName()+"'.");
      }
      DateTimeType otherTimeType = (DateTimeType) value;
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
      
      // Other type of restriction based on range
      
      Calendar minOtherValue = otherTimeType.getMinInclusive();
      Calendar maxOtherValue = otherTimeType.getMaxInclusive();

      // No bounds at all - no restrictions in both ranges
      if ((otherTimeType.isBounded()==false) && (isBounded()==false))
      {
        return false;
      }

      // This value has one bound, and other no bound.
      if ((otherTimeType.isBounded()==false) && (isBounded()==true))
      {
        return true;
      }
      
      // Bounded in this object and the one specified
      
      
      
      long rangeValue = Long.MAX_VALUE;
      long otherRangeValue = Long.MAX_VALUE;
      
      if ((minInclusive != null) && (maxInclusive!=null))
      {
        rangeValue = maxInclusive.getTimeInMillis()-minInclusive.getTimeInMillis();
      }
      if ((minOtherValue != null) && (maxOtherValue!=null))
      {
        otherRangeValue = maxOtherValue.getTimeInMillis()-minOtherValue.getTimeInMillis();
      }
          
      if (rangeValue < otherRangeValue)
        return true;
      
      
      return false;
      
  }

  public Calendar[] getChoices()
  {
    return enumHelper.getChoices();
  }

  public boolean validateChoice(Calendar value)
  {
    return enumHelper.validateChoice(value);
  }
  
  
  public Calendar getMinInclusive()
  {
    return minInclusive;
  }

  public Calendar getMaxInclusive()
  {
    return maxInclusive;
  }

  public boolean validateRange(Calendar value)
  {
    if ((minInclusive != null) && (comparator.compare(value, minInclusive)==-1))
    {
      return false;
    }
    if ((maxInclusive != null) &&  (comparator.compare(value,maxInclusive)==1))
    {
      return false;
    }
    return true;
  }
  
  public boolean isBounded()
  {
    return (minInclusive != null) || (maxInclusive != null);
  }
  
  
  public static TypeReference getInstance()
  {
    if (defaultTypeInstance == null)
    {
      defaultTypeInstance = new DateTimeType();
      defaultTypeReference = new NamedTypeReference("time(second)" ,defaultTypeInstance);
    }
    return defaultTypeReference; 
  }
  
}
