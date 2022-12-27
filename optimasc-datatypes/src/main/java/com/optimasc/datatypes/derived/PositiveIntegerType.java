package com.optimasc.datatypes.derived;

public class PositiveIntegerType extends NonNegativeIntegerType
{

  public PositiveIntegerType()
  {
    super();
    setMinInclusive(1);
  }

}
