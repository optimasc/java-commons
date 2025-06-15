/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.defined;

import java.math.BigDecimal;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.CharacterSet;

/** Datatype that represents a character from the IANA 'ISO-8859-1'
 *  character set. The actual encoding is on 8-bits and includes
 *  all control characters. 
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>character(1.0.10646.0.3.1.1.2.208)</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>CHARACTER(1) CHARACTER SET ISO8BIT</code> in SQL2003</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a <code>char</code>.</p>
 *
 * @author Carl Eric Codère
 */
public class LatinCharType extends CharacterType
{
  public static final BigDecimal LATIN1_MAX = BigDecimal.valueOf(255);
  
  public LatinCharType()
  {
    super(CharacterSet.ISO8BIT);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

}
