/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.defined;

import java.math.BigDecimal;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.CharacterSet;

/** Datatype that represents a character from the Unicode BMP. 
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>character(1.0.10646.0.3.1.208.302)</code> ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a <code>char</code>.</p>
 *
 * @author Carl Eric Codère
 */
public class UCS2CharType extends CharacterType 
{
  public static final UCS2CharType DEFAULT_INSTANCE = new UCS2CharType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public static final BigDecimal UCS2_MAX = BigDecimal.valueOf(65535);
  
  
  public UCS2CharType()
  {
      super(CharacterSet.BMP);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
