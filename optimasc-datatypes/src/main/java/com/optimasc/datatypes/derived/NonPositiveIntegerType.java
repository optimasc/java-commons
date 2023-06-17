package com.optimasc.datatypes.derived;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.primitives.IntegralType;

public class NonPositiveIntegerType extends IntegralType
{
  protected static final String REGEX_NONPOSITIVE_PATTERN = "-[0-9]+";
  
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new NonPositiveIntegerType());
  

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
