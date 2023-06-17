package com.optimasc.datatypes.derived;

import omg.org.astm.type.UnnamedTypeReference;

public class NegativeIntegerType extends NonPositiveIntegerType
{
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new NegativeIntegerType());

  public NegativeIntegerType()
  {
    super();
    setMaxInclusive(-1);
  }
  
}
