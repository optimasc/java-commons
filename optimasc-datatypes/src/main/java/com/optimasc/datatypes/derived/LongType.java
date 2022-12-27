package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegerType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents a specific datatype that is a signed 64-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class LongType extends IntegerType
{

  public LongType()
  {
    super();
    setMinInclusive(Long.MIN_VALUE);
    setMaxInclusive(Long.MAX_VALUE);
  }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }


}
