package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.generated.StringTypeEx;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;

/** Represents a normalized string type.
 * 
 * @author Carl Eric Codere
 *
 */
public class NormalizedStringType extends StringTypeEx
{
  public NormalizedStringType()
  {
    super();
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
