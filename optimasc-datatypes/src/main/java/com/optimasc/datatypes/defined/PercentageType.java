package com.optimasc.datatypes.defined;

import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific defined
 *  datatype that represents a percentage, an integer
 *  value within the range 0 to 100 inclusive.
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(0..100)</code> ASN.1 datatype</li>
 *   <li><code>integer range(0..100)</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>Percentage</code> IETF draft-codere-ldapsyntax syntax
 *     (OID: 1.3.6.1.4.1.61799.5.40.2.20)</li>
 *  </ul>
 *
 *  <p>Internally, values of this type are represented as an
 *  <code>Integer</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class PercentageType extends IntType
{
  /** The highest value of a percentage. */
  public static final int MAX_VALUE = 100;

  public PercentageType()
  {
    super(0, MAX_VALUE);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }
}
