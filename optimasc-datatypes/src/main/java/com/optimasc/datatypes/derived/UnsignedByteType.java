package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegerType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents a specific datatype that is an unsigned 8-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */

public class UnsignedByteType extends NonNegativeIntegerType
{

  public UnsignedByteType()
  {
    super();
    setMinInclusive(0);
    setMaxInclusive(255);
  }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }



}
