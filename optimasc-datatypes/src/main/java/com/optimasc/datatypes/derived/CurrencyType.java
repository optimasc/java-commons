package com.optimasc.datatypes.derived;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.PrecisionFacet;
import com.optimasc.lang.Currency;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class CurrencyType extends RealType
{
  public static final CurrencyType DEFAULT_INSTANCE = new CurrencyType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  
  public CurrencyType()
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
