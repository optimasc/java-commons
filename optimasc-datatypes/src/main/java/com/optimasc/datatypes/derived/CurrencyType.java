package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.PrecisionFacet;
import com.optimasc.lang.Currency;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class CurrencyType extends RealType
{
  public CurrencyType(int precision)
  {
    super(8,2);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }

  public Class getClassType()
  {
    return Currency.class;
  }

  public Object getObjectType()
  {
    return Currency.ZERO;
  }


  
}
