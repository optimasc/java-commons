package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegerType;

public class NonPositiveIntegerType extends IntegerType
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
