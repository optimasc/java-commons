package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

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
  
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new UnsignedShortType());
  

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

    public Class getClassType()
    {
      return Short.class;
    }

    public Object getObjectType()
    {
      return SHORT_INSTANCE;
    }
    
}
