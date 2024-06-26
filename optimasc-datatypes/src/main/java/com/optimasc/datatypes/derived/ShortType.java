package com.optimasc.datatypes.derived;

import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific datatype that is a signed 16-bit integer value.
 *  
 * @author Carl Eric Cod�re
 *
 */

public class ShortType extends IntegralType
{
  public static final ShortType DEFAULT_INSTANCE = new ShortType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);

  
  public ShortType()
  {
    super(Datatype.SMALLINT,Short.MIN_VALUE, Short.MAX_VALUE);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Short.class;
    }
    
    
    public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
    {
      BigInteger returnValue = (BigInteger) super.toValue(ordinalValue, conversionResult);
      if (returnValue == null)
      {
        return null;
      }
      return new Short(returnValue.shortValue());
    }

    
    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      Object result = super.toValue(ordinalValue, conversionResult);
      if (result == null)
      {
        return null;
      }
      return new Short((short) ordinalValue);
    }  
    

}
