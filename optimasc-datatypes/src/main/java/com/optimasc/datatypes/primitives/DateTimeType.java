package com.optimasc.datatypes.primitives;

import java.text.ParseException;
import java.util.GregorianCalendar;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.BaseISO8601Date;
import com.optimasc.lang.DateTimeConstants;
import com.optimasc.lang.GregorianDateTime;

/**
 * Abstract Datatype that represents a date and time with a specified resolution. This
 * can be used to represent dates or timestamps.
 * 
 *  This may be equivalent to the following datatypes, depending on resolution:
 *  <ul>
 *   <li><code>GeneralizedTime</code>, <code>DATE</code> or <code>DATE-TIME</code> ASN.1 datatype</li>
 *   <li><code>time</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>dateTime</code> or <code>date</code> XMLSchema built-in datatype</li>
 *   <li><code>DATE</code> or <code>TIMESTAMP</code> in SQL2003</li>
 *  </ul>
 * 
 * <p>The default non parameter constructor creates equivalent to an undefined
 * resolution.</p>
 * 
 * <p>Internally, values of this type are represented as {@link GregorianDateTime} objects.</p>
 * 
 * @author Carl Eric Codère
 */
public class DateTimeType extends PrimitiveType implements PatternFacet, Parseable
{
  protected static final GregorianDateTime INSTANCE = new GregorianDateTime();
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

  public Class getClassType()
  {
    return GregorianDateTime.class;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    GregorianDateTime cal;
    int resolution = getResolution();
    checkClass(value);
    /* Check the precision of the data. */
    cal = (GregorianDateTime) value;

    if (resolution >= RESOLUTION_SECOND)
    {
      if (cal.getSecond()==DateTimeConstants.FIELD_UNDEFINED)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Second field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_MINUTE)
    {
      if (cal.getMinute()==DateTimeConstants.FIELD_UNDEFINED)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Minute field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_HOUR)
    {
      if (cal.getHour()==DateTimeConstants.FIELD_UNDEFINED)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Hour field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_DAY)
    {
      if (cal.getDay()==DateTimeConstants.FIELD_UNDEFINED)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Day field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_MONTH)
    {
      if (cal.getMonth()==DateTimeConstants.FIELD_UNDEFINED)
      {
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
            "Month field is invalid.");
      }
    }

    if (resolution >= RESOLUTION_YEAR)
    {
      if (cal.getYear()==DateTimeConstants.FIELD_UNDEFINED)
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

  public Object parse(String value) throws ParseException
  {
    GregorianDateTime cal = new GregorianDateTime();

    if (resolution == RESOLUTION_YEAR)
    {
      int v = Integer.parseInt(value);
      cal.setYear(v);
    try {  
      validate(cal);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Error parsing date value.",0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Error parsing date value.",0);
    }
      return cal;
    } 
    else
    if (resolution == RESOLUTION_MONTH)
    {
      try 
      {
        cal = GregorianDateTime.parse(value);
        validate(cal);
        return cal;
      } catch (IllegalArgumentException e)
      {
        throw new ParseException("Error parsing date value.",0);
      } catch (DatatypeException e)
      {
        throw new ParseException("Error parsing date value.",0);
      }
    } else
    if (resolution == RESOLUTION_DAY)
    {
        try 
        {
          cal = GregorianDateTime.parse(value);
          validate(cal);
          return cal;
        } catch (IllegalArgumentException e)
        {
          throw new ParseException("Error parsing date value.",0);
        } catch (DatatypeException e)
        {
          throw new ParseException("Error parsing date value.",0);
        }
    } else
    if (resolution == RESOLUTION_SECOND)
    {
      cal.clear();
        try 
        {
          cal = GregorianDateTime.parse(value);
          validate(cal);
          return cal;
        } catch (IllegalArgumentException e)
        {
          throw new ParseException("Error parsing date value.",0);
        } catch (DatatypeException e)
        {
          throw new ParseException("Error parsing date value.",0);
        }
    } else
    {
      throw new IllegalArgumentException("Unsupported parsing for this resolution in "+this.getClass().getName());
    }
  }
  
  

}
