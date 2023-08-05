package com.optimasc.datatypes.derived;

import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that is a signed 8-bit integer value.
 *  
 * @author Carl Eric Codere
 *
 */
public class ByteType extends IntegralType
{
  protected static final Byte BYTE_INSTANCE = new Byte((byte) 0);
  
  public static final ByteType DEFAULT_INSTANCE = new ByteType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public ByteType()
  {
    super();
    setMinInclusive(Byte.MIN_VALUE);
    setMaxInclusive(Byte.MAX_VALUE);
    type = Datatype.TINYINT;
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Byte.class;
    }

    public Object getObjectType()
    {
      return BYTE_INSTANCE;
    }

    public Object parse(String value) throws ParseException
    {
      try
      {
        Long longValue = Long.valueOf(value);
        validate(longValue);
        return new Byte(value);
      } catch (NumberFormatException e)
      {
        throw new ParseException("Cannot parse string to integer type.", 0);
      } catch (DatatypeException e)
      {
        throw new ParseException("Cannot parse string to integer type.", 0);
      }
    }

}
