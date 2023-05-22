package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class VoidType extends PrimitiveType
{

  public VoidType()
  {
    super(Datatype.OTHER, false);
  }

  public Object getObjectType()
  {
    return null;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
  }

  public Class getClassType()
  {
    return null;
  }
  
  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  

}
