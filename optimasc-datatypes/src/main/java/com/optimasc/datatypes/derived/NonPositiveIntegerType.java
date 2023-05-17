package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegralType;

public class NonPositiveIntegerType extends IntegralType
{
  protected static final String REGEX_NONPOSITIVE_PATTERN = "-[0-9]+";

  public NonPositiveIntegerType()
  {
    super();
    setMaxInclusive(0);
  }

  public String getPattern()
  {
    return REGEX_NONPOSITIVE_PATTERN;
  }
  
  

}
