package com.optimasc.datatypes.primitives;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class ExceptionType extends Datatype
{
  public static final ExceptionType DEFAULT_INSTANCE = new ExceptionType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public ExceptionType()
  {
    super(Datatype.OTHER, false);
  }

  public Class getClassType()
  {
    return Exception.class;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
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
      if (!(obj instanceof ExceptionType))
      {
          return false;
      }
      return true;
  }

  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    if (Exception.class.isInstance(value))
    {
      return value;
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }
  
  

}
