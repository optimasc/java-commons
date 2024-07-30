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
 *  datatype that fits within a signed 16-bit integer value.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(-32768..32767)</code> ASN.1 datatype</li>
 *   <li><code>integer range(-32768..32767)</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>short</code> XMLSchema built-in datatype</li>
 *   <li><code>SMALLINT</code> SQL-92 datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as an 
 *  <code>Integer</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class ShortType extends IntType
{
  public static final ShortType DEFAULT_INSTANCE = new ShortType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);

  protected ShortType(int minValue, int maxValue)
  {
    super(minValue,maxValue);
  }

  
  public ShortType()
  {
    super(Short.MIN_VALUE, Short.MAX_VALUE);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
