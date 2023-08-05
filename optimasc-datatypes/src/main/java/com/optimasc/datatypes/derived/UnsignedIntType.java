package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

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
  
  public static final UnsignedIntType DEFAULT_INSTANCE = new UnsignedIntType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
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
    
    public Class getClassType()
    {
      return Integer.class;
    }

    public Object getObjectType()
    {
      
      return INTEGER_INSTANCE;
    }
    
}
