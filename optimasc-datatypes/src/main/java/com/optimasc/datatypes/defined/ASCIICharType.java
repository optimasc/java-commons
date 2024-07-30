package com.optimasc.datatypes.defined;

import java.math.BigDecimal;

import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.CharacterSet;

/** Datatype that represents a character from the IANA 'US-ASCII'
 *  character set. The actual encoding is on 7-bit/8-bits and
 *  includes all control characters.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>IA5String(Size(1))</code> ASN.1 datatype</li>
 *   <li><code>character(1.3.6.1.4.1.1466.115.121.1.26)</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>CHARACTER(1) CHARACTER SET ISO8BIT</code> in SQL2003</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a <code>char</code>.</p>
 *
 * @author Carl Eric Codère
 */
public class ASCIICharType extends CharacterType
{
  public static final BigDecimal ASCII_MAX = BigDecimal.valueOf(127);
  
  public ASCIICharType()
  {
    super(CharacterSet.ASCII);
  }
  
  protected ASCIICharType(CharacterSet charSet)
  {
    super(charSet);
  }
  
  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

}
