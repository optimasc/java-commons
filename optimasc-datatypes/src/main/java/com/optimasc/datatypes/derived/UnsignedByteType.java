package com.optimasc.datatypes.derived;

import java.text.ParseException;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that is an unsigned 8-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */

public class UnsignedByteType extends NonNegativeIntegerType
{
  protected static final Byte BYTE_INSTANCE = new Byte((byte) 0);

  
  public UnsignedByteType()
  {
    super();
    setMinInclusive(0);
    setMaxInclusive(255);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    
    public int getSize()
    {
      return 1;
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
      throw new ParseException("Cannot parse string to integer type.", 0);
    }
    

}
