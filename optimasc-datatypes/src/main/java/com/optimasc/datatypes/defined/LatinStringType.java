package com.optimasc.datatypes.defined;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.TypeFactory;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents a character string datatype from the ISO-8859-1 character
 *  set. The actual encoding 
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>VisibleString</code> ASN.1 datatype</li>
 *   <li><code>characterstring</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>CHARACTER</code>,<code>CHARACTER VARYING</code>,<code>CHARACTER LARGE OBJECT</code>,
 *     <code>NATIONAL CHARACTER</code>,<code>NATIONAL CHARACTER VARYING</code> or <code>NATIONAL CHARACTER LARGE OBJECT</code> 
 *     in SQL2003</li>
 *  </ul>
 *  
 *  <p>By default, the allowed minimum length of the string type is 0 characters, and the default maximum length
 *  is {@code Integer#MAX_VALUE}, {@code pattern} and {@code choices} are set to null.</p>
 *  
 *  <p>Internally, values of this type are represented as {@link String}.</p>
 *
 * @author Carl Eric Codère
 */
public class LatinStringType extends StringType
{
  public LatinStringType()
  {
      super(TypeFactory.getDefaultInstance(LatinCharType.class));
  }
  
  public LatinStringType(int minLength, int maxLength)
  {
      super(minLength,maxLength,TypeFactory.getDefaultInstance(LatinCharType.class));
  }
  
  
  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  
  

}
