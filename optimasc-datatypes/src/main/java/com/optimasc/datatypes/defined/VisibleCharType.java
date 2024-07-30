package com.optimasc.datatypes.defined;

import java.math.BigDecimal;

import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.CharacterSet;

/** Datatype that represents a character printable character from
 *  the ASCII character set identified as IANA Character set 'ISO-10646-UCS-Basic'.
 *  The actual encoding is on 7-bits and excludes all control characters 
 *  (it contains 95 characters).
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>character(11.0.10646.0.3.1.1)</code> ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a <code>char</code>.</p>
 *
 * @author Carl Eric Codère
 */
public class VisibleCharType extends ASCIICharType
{
  public static final BigDecimal VISIBLE_MIN = BigDecimal.valueOf(0x20);
  public static final BigDecimal VISIBLE_MAX = BigDecimal.valueOf(0x7E);
  
  public VisibleCharType()
  {
    super(CharacterSet.GRAPHIC_IRV);
  }
  
  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

}
