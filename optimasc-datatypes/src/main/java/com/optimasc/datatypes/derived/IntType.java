package com.optimasc.datatypes.derived;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that
 *  is a signed 32-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class IntType extends IntegralType
{
  protected static final Integer INTEGER_INSTANCE = new Integer(0);

  public IntType()
  {
    super();
    setMinInclusive(Integer.MIN_VALUE);
    setMaxInclusive(Integer.MAX_VALUE);
    type = Datatype.INTEGER;
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Integer.class;
    }

    public Object getObjectType()
    {
      return super.getObjectType();
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
        return new Integer(value);
      } catch (NumberFormatException e)
      {
        throw new ParseException("Cannot parse string to integer type.", 0);
      } catch (DatatypeException e)
      {
        throw new ParseException("Cannot parse string to integer type.", 0);
      }
    }
    


}
