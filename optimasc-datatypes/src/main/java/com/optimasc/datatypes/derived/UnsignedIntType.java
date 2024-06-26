package com.optimasc.datatypes.derived;

import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.defined.NonNegativeIntegerType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that
 *  is an  unsigned 32-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */
public class UnsignedIntType extends NonNegativeIntegerType
{
  public static final UnsignedIntType DEFAULT_INSTANCE = new UnsignedIntType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public UnsignedIntType()
  {
    super(BigInteger.valueOf(4294967295L));
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    public Class getClassType()
    {
      return Long.class;
    }
    
    
    public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
    {
      BigInteger returnValue = (BigInteger) super.toValue(ordinalValue, conversionResult);
      if (returnValue == null)
      {
        return null;
      }
      return new Long(returnValue.longValue());
    }

    
    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      Object result = super.toValue(ordinalValue, conversionResult);
      if (result == null)
      {
        return null;
      }
      return new Long(ordinalValue);
    }  
    

}
