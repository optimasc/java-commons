package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an IEEE 754 32-bit floating point value */
public class SingleType extends RealType
{
  protected static final Float FLOAT_INSTANCE = new Float(0);

  
  public SingleType()
  {
    super(7,-1);
    setMinInclusive(Float.MIN_VALUE);
    setMaxInclusive(Float.MAX_VALUE);
    type = Datatype.FLOAT;
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
      return Float.class;
    }

    public Object getObjectType()
    {
      return FLOAT_INSTANCE;
    }


}
