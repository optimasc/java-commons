/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.generated;

import java.text.ParseException;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.VoidType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** This datatype represents a pointer to another object.
 *
 * @author Carl Eric Codere
 */
public class PointerType extends Datatype implements ConstructedSimple 
{
    
  
   protected TypeReference baseType;

   public PointerType()
   {
        super(Datatype.REF,true);
        setBaseTypeReference(VoidType.DEFAULT_TYPE_REFERENCE);
   }
   
   public PointerType(TypeReference pointerTo)
   {
        super(Datatype.REF,true);
        setBaseTypeReference(pointerTo);
   }


    public Class getClassType()
    {
        return null;
    }

    public void validate(Object integerValue) throws IllegalArgumentException, DatatypeException
    {
        
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

    public Object getObjectType()
    {
      // TODO Auto-generated method stub
      return null;
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

    
    
}
