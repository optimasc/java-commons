package com.optimasc.datatypes.generated;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class ExceptionType extends Datatype
{
  public ExceptionType()
  {
    super(false);
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
