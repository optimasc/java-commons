package com.optimasc.datatypes.defined;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Convertable;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents an ITU-T/ISO Object identifier as
 *  a sequence of natural numbers.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>OBJECT IDENTIFIER</code> ASN.1 datatype</li>
 *   <li><code>objectidentifier</code> ISO/IEC 11404 defined datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as an <code>int[]</code>.</p>
 *
 * @author Carl Eric Codère
 */
public class ObjectIdentifierType extends SequenceType implements Convertable
{
  public ObjectIdentifierType()
  {
    super(1,Integer.MIN_VALUE, new UnnamedTypeReference(new NonNegativeIntegerType(Integer.MAX_VALUE)),int[].class);
  }
  
  public ObjectIdentifierType(int[] choices[], TypeReference baseType)
  {
    super(choices,UnsignedIntType.DEFAULT_TYPE_REFERENCE,int[].class);
    enumeration = new long[choices.length][];
    System.arraycopy(choices, 0, enumeration, 0, choices.length);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
  }


  public boolean validateChoice(Object value)
  {
    return validateChoice((long[])value);
  }
  

}
