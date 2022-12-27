package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegerType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents a specific datatype that
 *  is an  unsigned 32-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class UnsignedIntType extends NonNegativeIntegerType
{

  public UnsignedIntType()
  {
    super();
    setMinInclusive(0);
    setMaxInclusive(4294967295L);
  }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
