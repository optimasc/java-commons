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
 *  datatype that is an unsigned 8-bit integer value.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(0..255)</code> ASN.1 datatype</li>
 *   <li><code>octet</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>unsignedByte</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a 
 *  <code>Short</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class UnsignedByteType extends NonNegativeIntegerType
{
  public static final UnsignedByteType DEFAULT_INSTANCE = new UnsignedByteType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  

  
  public UnsignedByteType()
  {
    super(255);
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
