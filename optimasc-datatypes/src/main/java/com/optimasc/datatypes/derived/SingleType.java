package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents an IEEE 754 32-bit floating point value */
public class SingleType extends RealType
{

  public SingleType()
  {
    super();
    setMinInclusive(Float.MIN_VALUE);
    setMaxInclusive(Float.MAX_VALUE);
  }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }


}
