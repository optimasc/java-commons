package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an IEEE 754 64-bit floating point value */
public class DoubleType extends RealType
{
    protected static final Double DOUBLE_INSTANCE = new Double(0);
  
  
    public DoubleType()
    {
      super(15,-1);
      setMinInclusive(Double.MIN_VALUE);
      setMaxInclusive(Double.MAX_VALUE);
      type = Datatype.DOUBLE;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public int getSize()
    {
      return 8;
    }

    public Class getClassType()
    {
      return Double.class;
    }

    public Object getObjectType()
    {
      return DOUBLE_INSTANCE;
    }


}
