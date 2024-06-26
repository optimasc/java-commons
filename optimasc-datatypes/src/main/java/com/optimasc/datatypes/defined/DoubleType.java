package com.optimasc.datatypes.defined;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents an IEEE 754 64-bit floating point value (double,
 *  binary64),
 *  that is a approximation of a real number. 
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>real(2,53)</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>double</code> XMLSchema built-in datatype</li>
 *   <li><code>FLOAT</code> IETF RFC 6350 vCard Data type</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a 
 *  <code>Double</code> object.</p>
 *
 * @author Carl Eric Codère
 */

/** Represents an IEEE 754 64-bit floating point value */
public class DoubleType extends RealType
{
    public static final DoubleType DEFAULT_INSTANCE = new DoubleType();
    public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
    public DoubleType()
    {
      super(15,-1,new BigDecimal(Double.MIN_VALUE),new BigDecimal(Double.MAX_VALUE));
      type = Datatype.DOUBLE;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Double.class;
    }

    public Object toValue(Number number, TypeCheckResult conversionResult)
    {
      BigDecimal returnValue = (BigDecimal) super.toValue(number, conversionResult);
      if (returnValue == null)
      {
        return null;
      }
      return new Double(returnValue.doubleValue());
    }

    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      Object result = super.toValue(ordinalValue, conversionResult);
      if (result == null)
      {
        return null;
      }
      return new Double(ordinalValue);
    }  
    

}
