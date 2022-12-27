package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegerType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents a specific datatype that is a signed 16-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */

public class ShortType extends IntegerType
{
  public ShortType()
  {
    super();
    setMinInclusive(Short.MIN_VALUE);
    setMaxInclusive(Short.MAX_VALUE);
  }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }


}
