package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

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
  
  public static final IntType DEFAULT_INSTANCE = new IntType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);


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
