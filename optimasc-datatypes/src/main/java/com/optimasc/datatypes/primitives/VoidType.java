package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.derived.SingleType;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class VoidType extends PrimitiveType
{
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new VoidType());

  
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
