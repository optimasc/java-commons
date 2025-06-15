package com.optimasc.datatypes.defined;

import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific defined 
 *  datatype that fits within a signed 32-bit integer value.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(-2147483648..2147483647)</code> ASN.1 datatype</li>
 *   <li><code>integer range(-2147483648..2147483647)</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>int</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as an 
 *  <code>Integer</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class IntType extends IntegralType
{
  protected IntType(int minValue, int maxValue)
  {
    super(minValue,maxValue);
  }

  public IntType()
  {
    super(Integer.MIN_VALUE, Integer.MAX_VALUE);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Integer.class;
    }
    
    
    protected Object toValueNumber(Number ordinalValue, TypeCheckResult conversionResult)
    {
      BigInteger returnValue = (BigInteger) super.toValueNumber(ordinalValue, conversionResult);
      if (returnValue == null)
      {
        return null;
      }
      return new Integer(returnValue.intValue());
    }

    
    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      Object result = super.toValue(ordinalValue, conversionResult);
      if (result == null)
      {
        return null;
      }
      return new Integer((int) ordinalValue);
    }  
    

    
}
