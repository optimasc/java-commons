package com.optimasc.datatypes.defined;

import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a specific defined 
 *  datatype that is a signed 8-bit integer value.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(-128..127)</code> ASN.1 datatype</li>
 *   <li><code>integer range(-128..127)</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>byte</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a 
 *  <code>Byte</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class ByteType extends IntegralType
{
  public static final ByteType DEFAULT_INSTANCE = new ByteType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public ByteType()
  {
    super(Datatype.TINYINT, Byte.MIN_VALUE, Byte.MAX_VALUE);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Byte.class;
    }
    
    
    public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
    {
      BigInteger returnValue = (BigInteger) super.toValue(ordinalValue, conversionResult);
      if (returnValue == null)
      {
        return null;
      }
      return new Byte(returnValue.byteValue());
    }

    
    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      Object result = super.toValue(ordinalValue, conversionResult);
      if (result == null)
      {
        return null;
      }
      return new Byte((byte) ordinalValue);
    }  
    

}
