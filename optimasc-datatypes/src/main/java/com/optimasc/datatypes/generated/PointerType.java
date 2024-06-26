/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.generated;

import java.text.ParseException;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.defined.BinaryType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.VoidType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.GregorianDatetimeCalendar;

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
  public static final PointerType DEFAULT_INSTANCE = new PointerType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  protected TypeReference baseType;

   /** Creates a pointer type that points to <code>VoidType</code>.
    * 
    */
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

    public Object toValue(Object value, TypeCheckResult conversionResult)
    {
      return null;
    }

    
    
}
