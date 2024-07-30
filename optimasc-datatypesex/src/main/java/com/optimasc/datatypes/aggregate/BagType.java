package com.optimasc.datatypes.aggregate;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;

/** Represents an unordered list of elements where elements
 *  can be duplicated. Reordering of the elements of a Bag
 *  are allowed.
 *  
 * This is equivalent to the following datatypes:
 * <ul>
 * <li><code>SET OF</code> ASN.1 datatype</li>
 * <li><code>bag</code> ISO/IEC 11404 aggregate datatype</li>
 * <li><code>rdf:Bag</code> XMP Specification (2012) unordered array datatype</li>
 * </ul>
 *  
 * @author Carl Eric Codere
 *
 */
public class BagType extends SequenceType
{
  public BagType(TypeReference baseType, Class resultType)
  {
    super(baseType,resultType);
  }
  
  public BagType(int minLength, int maxLength, TypeReference baseType, Class resultType)
  {
    super(minLength,maxLength,baseType,resultType);
  }

  public BagType(Object choices[], TypeReference baseType, Class resultType)
  {
    super(choices,baseType,resultType);
  }
  

  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    if ((v instanceof TypeVisitorEx)==false)
      throw new IllegalArgumentException("Visitor must of type "+TypeVisitorEx.class.getName());
    return ((TypeVisitorEx)v).visit(this, arg);
  }
  
  
  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }

    if (!(obj instanceof BagType))
    {
      return false;
    }
    return super.equals(obj);
  }
  

}
