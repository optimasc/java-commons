package com.optimasc.datatypes.defined;

import java.math.BigDecimal;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an IEEE 754 32-bit floating point value */
public class SingleType extends RealType
{
  public static final SingleType DEFAULT_INSTANCE = new SingleType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);

  
  public SingleType()
  {
    super(24);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Float.class;
    }

    public Object toValue(Number number, TypeCheckResult conversionResult)
    {
      BigDecimal returnValue = (BigDecimal) super.toValue(number, conversionResult);
      if (returnValue == null)
      {
        return null;
      }
      return new Float(returnValue.floatValue());
    }

    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      Object result = super.toValue(ordinalValue, conversionResult);
      if (result == null)
      {
        return null;
      }
      return new Float(ordinalValue);
    }  
    
    
}
