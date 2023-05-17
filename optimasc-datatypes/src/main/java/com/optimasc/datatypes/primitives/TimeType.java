package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.BaseISO8601Date;
import com.optimasc.utils.StringTokenizer;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/** Time represents an instant of time that recurs every day. 
 *  The value space of time is the space of time of day value. 
 * 
 * @author Carl Eric Codere
 *
 */
public class TimeType extends Datatype implements PatternFacet
{
  protected static final Calendar INSTANCE_TYPE = Calendar.getInstance();
  
  /* TIME : (0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])([\.,]\d+)?([zZ]|([\+-])([01]\d|2[0-3]):?([0-5]\d)?)? */
  protected static final String REGEX_PATTERN = "(0[0-9]|1[0-9]|2[0-3]):([0-5][0-9]):([0-5][0-9])(?:[\\.,](\\d+))?([zZ]|([\\+-])([01]\\d|2[0-3]):?([0-5]\\d)?)?";
  
  public static final int PATTERN_GROUP_HOURS = 1; 
  public static final int PATTERN_GROUP_MINUTES = 2; 
  public static final int PATTERN_GROUP_SECONDS = 3; 
  public static final int PATTERN_GROUP_FRACTIONAL = 4; 
  public static final int PATTERN_GROUP_TIMEZONE = 5;
  

  public TimeType()
  {
    super(Datatype.TIME,true);
  }

  public int getSize()
  {
    return 4;
  }

  public Class getClassType()
  {
    return Calendar.class;
  }

  public void validate(Object value) throws IllegalArgumentException,
      DatatypeException
  {
    checkClass(value);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    
    /** Compares this TimeType to the specified object. 
     *  The result is true if and only if the argument is not null 
     *  and is a TimeType object.
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
        return true;
    }
    
    public Object getObjectType()
    {
      return INSTANCE_TYPE;
    }
    
    public Object parse(String value) throws ParseException
    {
      /*
      matcher.reset(value);
      if (matcher.matches()==false)
      {
        throw new java.text.ParseException("does not match regex", 0);
      }
      Calendar calendar = Calendar.getInstance();
      calendar.clear();

      // Hours
      String hours = matcher.group(PATTERN_GROUP_HOURS);
      if (hours == null)
      {
        throw new java.text.ParseException("Hours must be 00 and 24", 0);
      }
      int hoursInt = Integer.parseInt(hours);
      if ((hoursInt < 0) || (hoursInt > 24))
      {
        throw new java.text.ParseException("Hours must be 00 and 24", 0);
      }
      calendar.set(Calendar.HOUR_OF_DAY, hoursInt);
      
      // Minutes
      String minutes = matcher.group(PATTERN_GROUP_MINUTES);
      if (minutes == null)
      {
        throw new java.text.ParseException("Minutes must be 00 and 59", 0);
      }
      int minutesInt = Integer.parseInt(minutes);
      if ((minutesInt < 0) || (minutesInt > 59))
      {
        throw new java.text.ParseException("Hours must be 00 and 59", 0);
      }
      calendar.set(Calendar.MINUTE, minutesInt);
      
      // Seconds
      String seconds = matcher.group(PATTERN_GROUP_SECONDS);
      if (seconds == null)
      {
        throw new java.text.ParseException("Seconds must be 00 and 59", 0);
      }
      int secondsInt = Integer.parseInt(seconds);
      if ((secondsInt < 0) || (secondsInt > 59))
      {
        throw new java.text.ParseException("Seconds must be 00 and 59", 0);
      }
      calendar.set(Calendar.SECOND, secondsInt);
      
      // Milliseconds - optional
      String fractional = matcher.group(PATTERN_GROUP_FRACTIONAL);
      if (fractional != null)
      {
        // Remove , or .
        fractional = fractional.substring(1);
        if (fractional.length() < 3)
        {
          fractional += "0";
        }
        fractional = fractional.substring(0, 3);
        int fractionalInt = Integer.parseInt(fractional);
        if ((fractionalInt < 0) || (fractionalInt > 999))
        {
          throw new java.text.ParseException("Milliseconds must be 0 and 999", 0);
        }
        calendar.set(Calendar.MILLISECOND, fractionalInt);
      }
      // Timezone - optional
      String timezone = matcher.group(PATTERN_GROUP_TIMEZONE);
      if (timezone != null)
      {
        if (timezone.equals("Z")==false) // UTC
        {
        } else
        // Timezone is UTC
        {
          calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        }
      }
      return calendar;*/
      Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
      cal.clear();
      try 
      {
        cal = BaseISO8601Date.parseTime(cal,value);
        validate(cal);
        return cal;
      } catch (IllegalArgumentException e)
      {
        throw new ParseException("Error parsing time value.",0);
      } catch (DatatypeException e)
      {
        throw new ParseException("Error parsing time value.",0);
      }
    }
    
    public String getPattern()
    {
      return REGEX_PATTERN;
    }

    public void setPattern(String value)
    {
      throw new IllegalArgumentException("Pattern is read only for this datatype.");
    }
    

}
