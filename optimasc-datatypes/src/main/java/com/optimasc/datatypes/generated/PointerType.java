/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.generated;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.TypeFactory;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.VoidType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents a means to reference another datatype
 *  located in memory. This implementation assumes that the pointer
 *  is managed by the developer. Compare this with the 
 *  Reference type. 
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li></code>pointer</code> ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 *
 *  <p>Contrary to ISO/IEC 11404, this type is considered ordered. </p>
 *  
 * @author Carl Eric Codere
 *
 */
public class PointerType extends Datatype implements ConstructedSimple 
{
  protected TypeReference baseType;

   /** Creates a pointer type that points to <code>VoidType</code>.
    * 
    */
   public PointerType()
   {
        super(true);
        setBaseTypeReference(TypeFactory.getDefaultInstance(VoidType.class));
   }
   
   public PointerType(TypeReference pointerTo)
   {
        super(true);
        setBaseTypeReference(pointerTo);
   }


    public Class getClassType()
    {
        return null;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public TypeReference getBaseTypeReference()
    {
      return baseType;
    }

    public void setBaseTypeReference(TypeReference value)
    {
      baseType = value;
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
      if (!(obj instanceof PointerType))
      {
        return false;
      }
      return super.equals(obj);
    }

    public String getGPDName()
    {
      return "pointer to ("+baseType.getType().getGPDName()+")";
    }
    
    

}
