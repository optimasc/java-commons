package com.optimasc.datatypes.generated;

import java.lang.ref.Reference;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.VoidType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents a means to reference another datatype
 *  located in memory. This implementation assumes that the management
 *  of the references are managed at runtime, hence not ordered
 *  and no arithmetic operation can be operated on it. 
 *  Compare with the pointer type.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li></code>pointer</code> ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 *
 * @author Carl Eric Codere
 *
 */
public class ReferenceType extends Datatype implements ConstructedSimple
{
  protected TypeReference baseType;

  /** Creates a reference type that points to <code>VoidType</code>.
   * 
   */
  public ReferenceType()
  {
       super(false);
       setBaseTypeReference(VoidType.getInstance());
  }
  
  public ReferenceType(TypeReference pointsTo)
  {
       super(false);
       setBaseTypeReference(pointsTo);
  }

  public TypeReference getBaseTypeReference()
  {
    return baseType;
  }

  public void setBaseTypeReference(TypeReference value)
  {
    baseType = value;
  }

  public Class getClassType()
  {
    return Reference.class;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
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
    if (!(obj instanceof ReferenceType))
    {
      return false;
    }
    return super.equals(obj);
  }


}
