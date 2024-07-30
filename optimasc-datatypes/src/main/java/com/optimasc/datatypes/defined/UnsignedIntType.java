package com.optimasc.datatypes.defined;

import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific defined 
 *  datatype that fits within an unsigned 32-bit integer value.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(0..4294967295)</code> ASN.1 datatype</li>
 *   <li><code>integer range(0..4294967295)</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>unsignedInt</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as an 
 *  <code>Long</code> object.</p>
 *
 * @author Carl Eric Codère
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
    
    
    public Object toValueNumber(Number ordinalValue, TypeCheckResult conversionResult)
    {
      BigInteger returnValue = (BigInteger) super.toValueNumber(ordinalValue, conversionResult);
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
