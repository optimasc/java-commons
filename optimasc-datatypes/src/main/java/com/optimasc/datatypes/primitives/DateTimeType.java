package com.optimasc.datatypes.primitives;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;
import java.util.TimeZone;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.BaseISO8601Date;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

/**
 * Represents a date and time with a specified resolution.
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li>time ISO/IEC 11404 General purpose datatype</li>
 * </ul>
 * 
 * The default non parameter constructor creates equivalent to an undefined
 * resolution.
 * 
 * @author Carl Eric Codère
 */
public class DateTimeType extends PrimitiveType implements PatternFacet
{
  protected static final Calendar INSTANCE = new GregorianCalendar();
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
  public static final int RESOLUTION_YEAR = 0x00;
  /** Resolution is up to the year-month level. */
  public static final int RESOLUTION_MONTH = 0x02;
  /** Resolution is up to the year-month-day level. */
  public static final int RESOLUTION_DAY = 0x04;
  /** Resolution is up to the year-month-day-hour level. */
  public static final int RESOLUTION_HOUR = 0x10;
  /** Resolution is up to the year-month-day-hour-minute level. */
  public static final int RESOLUTION_MINUTE = 0x20;
  /** Resolution is up to the year-month-day-hour-minute-second level. */
  public static final int RESOLUTION_SECOND = 0x40;
  /** Resolution is undefined */
  public static final int RESOLUTION_UNDEFINED = -1;

  protected int resolution;

  public DateTimeType(int type)
  {
    super(type,true);
  }

  public DateTimeType()
  {
    super(Datatype.TIMESTAMP,true);
    setResolution(RESOLUTION_UNDEFINED);
  }
  
  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  

  public int getResolution()
  {
    return resolution;
  }

  public void setResolution(int resolution)
  {
    this.resolution = resolution;
  }

  public int getSize()
  {
    return 4;
  }

  public Class getClassType()
  {
    return Calendar.class;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    Calendar cal;
    int resolution = getResolution();
    checkClass(value);
    /* Check the precision of the data. */
    cal = (Calendar) value;

    if (resolution >= RESOLUTION_SECOND)
    {
      if (cal.isSet(Calendar.SECOND)==false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Second field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_MINUTE)
    {
      if (cal.isSet(Calendar.MINUTE)==false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Minute field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_HOUR)
    {
      if (cal.isSet(Calendar.HOUR_OF_DAY) == false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Hour field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_DAY)
    {
      if (cal.isSet(Calendar.DAY_OF_MONTH) == false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Day field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_MONTH)
    {
      if (cal.isSet(Calendar.MONTH) == false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Month field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_YEAR)
    {
      if (cal.isSet(Calendar.YEAR) == false)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Year field is invalid.");
      }
    }
  }

  public Object getObjectType()
  {
    return INSTANCE;
  }

  public Object parse(String value) throws ParseException
  {
    try
    {
      DatatypeFactory dataFactory = DatatypeFactory.newInstance();
      XMLGregorianCalendar cal = dataFactory.newXMLGregorianCalendar(value);
      validate(cal);
      return cal.toGregorianCalendar();
    } catch (DatatypeConfigurationException e1)
    {
      throw new IllegalArgumentException("Internall error", e1);
    } catch (DatatypeException e)
    {
      throw new ParseException("Error parsing datetime value.", 0);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Error parsing datetime value.", 0);
    }

  }

  public String getPattern()
  {
    int resolution = getResolution();
    if (resolution == RESOLUTION_YEAR)
    {
      return PATTERN_YEAR;
    }
    if (resolution == RESOLUTION_MONTH)
    {
      return PATTERN_YEAR + "-" + PATTERN_MONTH;
    }
    if (resolution == RESOLUTION_DAY)
    {
      return PATTERN_YEAR + "-" + PATTERN_MONTH + "-" + PATTERN_DAY;
    } else
    {
      return REGEX_PATTERN;
    }
  }

  public void setPattern(String value)
  {
    throw new IllegalArgumentException("Pattern is read only for this datatype.");
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

}
