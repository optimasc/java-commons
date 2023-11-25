package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.GregorianDateTime;

import java.text.ParseException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import omg.org.astm.type.UnnamedTypeReference;

/** Datatype that represents an instant of time that recurs every day. 
 *  The value space of time is the space of time of day value.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>TIME-OF-DAY</code> ASN.1 datatype</li>
 *   <li><code>time</code> XMLSchema built-in datatype</li>
 *   <li><code>TIME</code> in SQL2003</li>
 *  </ul>
 *  
 * <p>Internally, values of this type are represented as {@link GregorianDateTime} objects.</p>
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public class TimeType extends Datatype implements PatternFacet, Parseable
{
  protected static final GregorianDateTime INSTANCE_TYPE = new GregorianDateTime();
  
  public static final TimeType DEFAULT_INSTANCE = new TimeType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  
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

  public Class getClassType()
  {
    return GregorianDateTime.class;
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
      try 
      {
        GregorianDateTime cal = GregorianDateTime.parse(value);
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
