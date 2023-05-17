package com.optimasc.datatypes.derived;

import java.text.ParseException;

import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that
 *  is an  unsigned 16-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class UnsignedShortType extends NonNegativeIntegerType
{
  protected static final Short SHORT_INSTANCE = new Short((short) 0);
  

  public UnsignedShortType()
  {
    super();
    setMinInclusive(0);
    setMaxInclusive(65535);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public int getSize()
    {
      return 2;
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
      throw new ParseException("Cannot parse string to integer type.", 0);
    }
    

}
