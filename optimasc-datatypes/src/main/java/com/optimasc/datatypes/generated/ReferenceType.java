package com.optimasc.datatypes.generated;

import java.lang.ref.Reference;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class ReferenceType extends Datatype implements ConstructedSimple
{
  protected TypeReference baseType;

  
  public ReferenceType()
  {
       super(Datatype.REF,false);
  }
  
  public ReferenceType(TypeReference pointsTo)
  {
       super(Datatype.REF,false);
       setBaseType(pointsTo);
  }

  public TypeReference getBaseType()
  {
    return baseType;
  }

  public void setBaseType(TypeReference value)
  {
    baseType = value;
  }

  public Object getObjectType()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    // TODO Auto-generated method stub

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
