package com.optimasc.datatypes.aggregate;

import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;

/** Represents an unordered list of elements, each of the elements is separated by
 *  the others by a space and a semi-colon. This the same separator used from the
 *  Adobe XMP Java implementation.
 *  
 * @author Carl Eric Codere
 *
 */
public class BagType extends ListType
{
  public BagType()
  {
    super();
    setOrdered(true);
    setDelimiter(";");
  }

  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    if ((v instanceof TypeVisitorEx)==false)
      throw new IllegalArgumentException("Visitor must of type "+TypeVisitorEx.class.getName());
    return ((TypeVisitorEx)v).visit(this, arg);
  }

}
