package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

public class CurrencyType extends RealType
{

  public CurrencyType()
  {
    super();
  }

  public Object accept(DatatypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  
}
