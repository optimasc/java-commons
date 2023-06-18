package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;

public class TokenType extends NormalizedStringType
{
  public TokenType()
  {
    super();
    setWhitespace(WHITESPACE_COLLAPSE);
  }
  
  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    if ((v instanceof TypeVisitorEx)==false)
      throw new IllegalArgumentException("Visitor must of type "+TypeVisitorEx.class.getName());
    return ((TypeVisitorEx)v).visit(this, arg);
  }
  

}
