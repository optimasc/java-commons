package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Datatype that represents a namespace identifier. 
 */ 
public class NameSpaceType extends Type
{
  public NameSpaceType()
  {
    super(false);
  }

  public Class getClassType()
  {
    return null;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

}
