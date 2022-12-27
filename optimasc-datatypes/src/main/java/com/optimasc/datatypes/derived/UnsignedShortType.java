package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegerType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents a specific datatype that
 *  is an  unsigned 16-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class UnsignedShortType extends NonNegativeIntegerType
{

  public UnsignedShortType()
  {
    super();
    setMinInclusive(0);
    setMaxInclusive(65535);
  }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
