package com.optimasc.datatypes.derived;

public class TokenType extends NormalizedStringType
{
  public TokenType()
  {
    super();
    setWhitespace(WHITESPACE_COLLAPSE);
  }

}
