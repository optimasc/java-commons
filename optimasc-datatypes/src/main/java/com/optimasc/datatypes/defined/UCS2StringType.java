package com.optimasc.datatypes.defined;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.TypeFactory;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents a character string datatype from the Unicode
 *  BMP Plane. The actual encoding is UCS-2 character encoding, even 
 *  though UTF-16 support is checked in the validation routine.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>BMPString</code> ASN.1 datatype</li>
 *   <li><code>characterstring</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>string</code> XMLSchema built-in datatype</li>
 *   <li><code>CHARACTER</code>,<code>CHARACTER VARYING</code>,<code>CHARACTER LARGE OBJECT</code>,
 *     <code>NATIONAL CHARACTER</code>,<code>NATIONAL CHARACTER VARYING</code> or <code>NATIONAL CHARACTER LARGE OBJECT</code> 
 *     in SQL2003</li>
 *  </ul>
 *  
 *  <p>By default, the allowed minimum length of the string type is 0 characters, and the default maximum length
 *  is {@code Integer#MAX_VALUE}, {@code pattern} and {@code choices} are set to null.</p>
 *  
 *  <p>Internally, values of this type are represented as a {@link String}.</p>
 *
 * @author Carl Eric Codère
 */
public class UCS2StringType extends StringType
{
  
  public UCS2StringType()
  {
      super(TypeFactory.getDefaultInstance(UCS2CharType.class));
  }
  
  public UCS2StringType(int minLength, int maxLength)
  {
      super(minLength,maxLength,TypeFactory.getDefaultInstance(UCS2CharType.class));
  }
  
  
  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  
  

}
