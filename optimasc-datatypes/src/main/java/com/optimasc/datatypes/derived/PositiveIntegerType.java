package com.optimasc.datatypes.derived;

import omg.org.astm.type.UnnamedTypeReference;

public class PositiveIntegerType extends NonNegativeIntegerType
{
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new PositiveIntegerType());

  public PositiveIntegerType()
  {
    super();
    setMinInclusive(1);
  }

}
