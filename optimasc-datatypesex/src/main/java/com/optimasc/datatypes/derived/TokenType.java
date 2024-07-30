package com.optimasc.datatypes.derived;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.defined.UCS2CharType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;

public class TokenType extends NormalizedStringType
{
  public TokenType(TypeReference charType)
  {
    super(0,Integer.MAX_VALUE,charType);
    setWhitespace(WHITESPACE_COLLAPSE);
  }
  
  
  public TokenType(int minLength, int maxLength, TypeReference charType)
  {
    super(minLength, maxLength,charType);
    setWhitespace(WHITESPACE_COLLAPSE);
  }

  public TokenType()
  {
    super(0,Integer.MAX_VALUE,UCS2CharType.DEFAULT_TYPE_REFERENCE);
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
