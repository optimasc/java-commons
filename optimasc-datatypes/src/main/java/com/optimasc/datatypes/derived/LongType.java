package com.optimasc.datatypes.derived;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that is a signed 64-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class LongType extends IntegralType
{
  protected static final Long LONG_INSTANCE = new Long(0);

  public LongType()
  {
    super();
    setMinInclusive(Long.MIN_VALUE);
    setMaxInclusive(Long.MAX_VALUE);
    type = Datatype.BIGINT;
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Long.class;
    }

    public Object getObjectType()
    {
      return LONG_INSTANCE;
    }

    public int getSize()
    {
      return 4;
    }
    
    public Object parse(String value) throws ParseException
    {
      try
      {
        Long longValue = Long.valueOf(value);
        validate(longValue);
        return new Long(value);
      } catch (NumberFormatException e)
      {
        throw new ParseException("Cannot parse string to integer type.", 0);
      } catch (DatatypeException e)
      {
        throw new ParseException("Cannot parse string to integer type.", 0);
      }
    }
    


}
