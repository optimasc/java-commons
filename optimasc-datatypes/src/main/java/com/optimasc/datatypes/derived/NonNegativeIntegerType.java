package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegralType;

public class NonNegativeIntegerType extends IntegralType
{
  protected static final String REGEX_NONNEGATIVE_PATTERN = "[0-9]+";

  
  public NonNegativeIntegerType()
  {
    super();
    setMinInclusive(0);
  }


  public String getPattern()
  {
    return REGEX_NONNEGATIVE_PATTERN;
  }
  
  

}
