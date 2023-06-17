package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that is a signed 16-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */

public class ShortType extends IntegralType
{
  protected static final Short SHORT_INSTANCE = new Short((short) 0);
  
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new ShortType());

  
  public ShortType()
  {
    super();
    setMinInclusive(Short.MIN_VALUE);
    setMaxInclusive(Short.MAX_VALUE);
    type = Datatype.SMALLINT;
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Short.class;
    }

    public Object getObjectType()
    {
      return SHORT_INSTANCE;
    }
    
    public Object parse(String value) throws ParseException
    {
      try
      {
        Long longValue = Long.valueOf(value);
        validate(longValue);
        return new Short(value);
      } catch (NumberFormatException e)
      {
        throw new ParseException("Cannot parse string to integer type.", 0);
      } catch (DatatypeException e)
      {
        throw new ParseException("Cannot parse string to integer type.", 0);
      }
    }
    

}
