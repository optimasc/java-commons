package com.optimasc.datatypes.defined;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.lang.Currency;
import com.optimasc.datatypes.primitives.DecimalType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class CurrencyType extends DecimalType
{
  public static final CurrencyType DEFAULT_INSTANCE = new CurrencyType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  
  public CurrencyType()
  {
    super(2);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }

  public Class getClassType()
  {
    return Currency.class;
  }


}
