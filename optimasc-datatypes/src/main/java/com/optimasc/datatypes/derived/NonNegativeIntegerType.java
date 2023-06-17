package com.optimasc.datatypes.derived;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.primitives.IntegralType;

public class NonNegativeIntegerType extends IntegralType
{
  protected static final String REGEX_NONNEGATIVE_PATTERN = "[0-9]+";
  
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new NonNegativeIntegerType());
  

  
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
