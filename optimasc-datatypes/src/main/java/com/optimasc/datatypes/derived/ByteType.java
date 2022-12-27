package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.primitives.IntegerType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents a specific datatype that is a signed 8-bit integer value.
 *  
 * @author Carl Eric Codere
 *
 */
public class ByteType extends IntegerType
{
  public ByteType()
  {
    super();
    setMinInclusive(Byte.MIN_VALUE);
    setMaxInclusive(Byte.MAX_VALUE);
  }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
