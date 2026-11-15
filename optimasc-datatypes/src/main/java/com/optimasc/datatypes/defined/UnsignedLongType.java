package com.optimasc.datatypes.defined;

import java.math.BigInteger;

import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific defined
 *  datatype that fits within an unsigned 64-bit integer value.
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(0..18446744073709551615)</code> ASN.1 datatype</li>
 *   <li><code>integer range(0..18446744073709551615)</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>unsignedLong</code> XMLSchema built-in datatype</li>
 *  </ul>
 *
 *  <p>Internally, values of this type are represented as a
 *  <code>BigInteger</code> object, since the range does not fit
 *  in a <code>Long</code>.</p>
 *
 * @author Carl Eric Codère
 */
public class UnsignedLongType extends NonNegativeIntegerType
{
  /** 18446744073709551615, the highest value of an unsigned 64-bit integer. */
  public static final BigInteger MAX_VALUE = new BigInteger("18446744073709551615");

  public UnsignedLongType()
  {
    super(MAX_VALUE);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  public Class getClassType()
  {
    return BigInteger.class;
  }
}
