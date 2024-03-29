package com.optimasc.datatypes.primitives;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class ExceptionType extends Datatype
{
  public static final ExceptionType DEFAULT_INSTANCE = new ExceptionType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public ExceptionType()
  {
    super(Datatype.OTHER, false);
  }

  protected static final Exception INSTANCE_TYPE = new Exception();

  public Object getObjectType()
  {
    return INSTANCE_TYPE;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    checkClass(value);
  }

  public Class getClassType()
  {
    return Exception.class;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
  }

}
