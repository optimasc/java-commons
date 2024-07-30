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
 *  <code>Integer</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class ByteType extends ShortType
{
  public static final ByteType DEFAULT_INSTANCE = new ByteType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public ByteType()
  {
    super(Byte.MIN_VALUE, Byte.MAX_VALUE);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
