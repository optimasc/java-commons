package com.optimasc.datatypes.defined;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific defined datatype that is a 
 *  signed 64-bit integer value.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(-9223372036854775808..9223372036854775807)</code> ASN.1 datatype</li>
 *   <li><code>integer range(-9223372036854775808..9223372036854775807)</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>long</code> XMLSchema built-in datatype</li>
 *   <li><code>INTEGER</code> IETF RFC 6350 vCard Data type</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a 
 *  <code>Long</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class LongType extends IntegralType
{
  public static final LongType DEFAULT_INSTANCE = new LongType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  
  
  public LongType()
  {
    super(Datatype.BIGINT,BigInteger.valueOf(Long.MIN_VALUE),BigInteger.valueOf(Long.MAX_VALUE));
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
