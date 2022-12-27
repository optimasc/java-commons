package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegerType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents a specific datatype that
 *  is a signed 32-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class IntType extends IntegerType
{

  public IntType()
  {
    super();
    setMinInclusive(Integer.MIN_VALUE);
    setMaxInclusive(Integer.MAX_VALUE);
  }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }


}
