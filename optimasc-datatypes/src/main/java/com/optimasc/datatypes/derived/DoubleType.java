package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents an IEEE 754 64-bit floating point value */
public class DoubleType extends RealType
{
    public DoubleType()
    {
      super();
      setMinInclusive(Double.MIN_VALUE);
      setMaxInclusive(Double.MAX_VALUE);
      
    }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }


}
