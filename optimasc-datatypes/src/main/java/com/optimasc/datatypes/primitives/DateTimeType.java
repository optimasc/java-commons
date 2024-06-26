package com.optimasc.datatypes.primitives;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.optimasc.datatypes.AccessKind;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.TimezoneFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateConverter;
import com.optimasc.date.DateConverter.DateTime;
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
public class DateTimeType extends PrimitiveType implements OrderedFacet, TimezoneFacet
{
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

  /** Resolution is up to the year level. */
  public static final int RESOLUTION_YEAR = Calendar.YEAR;
  /** Resolution is up to the year-month level. */
  public static final int RESOLUTION_MONTH = Calendar.MONTH;
  /** Resolution is up to the year-month-day level. */
  public static final int RESOLUTION_DAY = Calendar.DAY_OF_MONTH;
  /** Resolution is up to the year-month-day-hour level. */
  public static final int RESOLUTION_HOUR = Calendar.HOUR_OF_DAY;
  /** Resolution is up to the year-month-day-hour-minute level. */
  public static final int RESOLUTION_MINUTE = Calendar.MINUTE;
  /** Resolution is up to the year-month-day-hour-minute-second level. */
  public static final int RESOLUTION_SECOND = Calendar.SECOND;
  /** Resolution is up to the year-month-day-hour-minute-second-ms level. */
  public static final int RESOLUTION_MILLISECOND = Calendar.MILLISECOND;

  /** Resolution is undefined */
  public static final int RESOLUTION_UNDEFINED = -1;
  /** The resolution of this date/time */
  protected int resolution;
  /** Does the time part contain timezone information */
  protected boolean hasTimezone;

  protected DateTimeType(int type)
  {
    super(type, true);
  }

  /**
   * Creates a standard date time type with an unknown resolution
   * and support for timezone information.
   * 
   */
  public DateTimeType()
  {
    super(Datatype.TIMESTAMP, true);
    setResolution(RESOLUTION_UNDEFINED);
    hasTimezone = true;
  }
  
  
  public int getResolution()
  {
    return resolution;
  }

  public void setResolution(int resolution)
  {
    this.resolution = resolution;
  }

  public Class getClassType()
  {
    return GregorianDatetimeCalendar.class;
  }

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
    if (this.getResolution() != datetimeType.getResolution())
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
    if (value instanceof DateConverter.DateTime)
    {
      // If the time component is required, and its not present,
      // we throw an error.
      if (resolution >= RESOLUTION_HOUR)
      {
         DateTime dateTime = (DateTime) value;
         !!
         if (resolution >= RESOLUTION_YEAR)
         
      }
      
    }
    if (value instanceof GregorianDatetimeCalendar)
    {
      GregorianDatetimeCalendar cal;
      int resolution = getResolution();
      /* Check the precision of the data. */
      cal = (GregorianDatetimeCalendar) value;

      if (resolution >= RESOLUTION_SECOND)
      {
        if (cal.isUserSet(Calendar.SECOND) == false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW, "Second field is invalid.");
          return null;
        }
      }

      if (resolution >= RESOLUTION_MINUTE)
      {
        if (cal.isUserSet(Calendar.MINUTE) == false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW, "Minute field is invalid.");
          return null;
        }
      }

      if (resolution >= RESOLUTION_HOUR)
      {
        if (cal.isUserSet(Calendar.HOUR_OF_DAY) == false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW, "Hour field is invalid.");
          return null;
        }
      }

      if (resolution >= RESOLUTION_DAY)
      {
        if (cal.isUserSet(Calendar.DAY_OF_MONTH) == false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW, "Day field is invalid.");
          return null;
        }
      }

      if (resolution >= RESOLUTION_MONTH)
      {
        if (cal.isUserSet(Calendar.MONTH) == false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW, "Month field is invalid.");
          return null;
        }
      }

      if (resolution >= RESOLUTION_YEAR)
      {
        if (cal.isUserSet(Calendar.MONTH) == false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_DATETIME_OVERFLOW, "Year field is invalid.");
          return null;
        }
      }
    }
    return null;
  }

  public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean hasTimezone()
  {
    return hasTimezone;
  }

}
