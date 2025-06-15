package com.optimasc.datatypes.defined;

import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific defined 
 *  datatype that fits within an unsigned 16-bit integer value.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(0..65535)</code> ASN.1 datatype</li>
 *   <li><code>integer range(0..65535)</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>unsignedShort</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as an 
 *  <code>Integer</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class UnsignedShortType extends NonNegativeIntegerType
{
  public UnsignedShortType()
  {
    super(65535);
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
