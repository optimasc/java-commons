package com.optimasc.datatypes.derived;

import java.text.ParseException;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that
 *  is an  unsigned 32-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class UnsignedIntType extends NonNegativeIntegerType
{
  protected static final Integer INTEGER_INSTANCE = new Integer(0);
  
  public UnsignedIntType()
  {
    super();
    setMinInclusive(0);
    setMaxInclusive(4294967295L);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    public int getSize()
    {
      return 4;
    }

    public Class getClassType()
    {
      return Integer.class;
    }

    public Object getObjectType()
    {
      
      return INTEGER_INSTANCE;
    }
    
    public Object parse(String value) throws ParseException
    {
      throw new ParseException("Cannot parse string to integer type.", 0);
    }

}
