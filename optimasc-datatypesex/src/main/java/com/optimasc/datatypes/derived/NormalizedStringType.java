package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.defined.UCS2CharType;
import com.optimasc.datatypes.generated.StringTypeEx;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;

/** Represents a normalized string type.
 * 
 * @author Carl Eric Codere
 *
 */
public class NormalizedStringType extends StringTypeEx
{
  public NormalizedStringType(int minLength, int maxLength, TypeReference charType)
  {
    super(minLength, maxLength, charType);
    setWhitespace(WHITESPACE_REPLACE);
  }
  
  public NormalizedStringType(TypeReference charType)
  {
    super(0, Integer.MAX_VALUE, charType);
    setWhitespace(WHITESPACE_REPLACE);
  }
  
  public NormalizedStringType()
  {
    super(0, Integer.MAX_VALUE, UCS2CharType.DEFAULT_TYPE_REFERENCE);
    setWhitespace(WHITESPACE_REPLACE);
  }
  
  

  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    if ((v instanceof TypeVisitorEx)==false)
      throw new IllegalArgumentException("Visitor must of type "+TypeVisitorEx.class.getName());
    return ((TypeVisitorEx)v).visit(this, arg);
  }
  
}
