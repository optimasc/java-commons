package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.defined.UCS2CharType;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;
import com.optimasc.text.DataConverter;

/** Represents a normalized string type.
 * 
 * @author Carl Eric Codere
 *
 */
public class NormalizedStringType extends StringType
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
  
  public NormalizedStringType(DataConverter validator, TypeReference baseType)
  {
    super(validator,baseType);
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
