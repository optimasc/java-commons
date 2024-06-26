package com.optimasc.datatypes.defined;

import java.math.BigInteger;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.IntegralType;

/** Represents a specific defined 
 *  datatype that is a natural number (a non-negative value).
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(0..MAX)</code> ASN.1 datatype</li>
 *   <li><code>naturalnumber</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>nonNegativeInteger</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a 
 *  <code>BigDecimal</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class NonNegativeIntegerType extends IntegralType
{
  protected static final String REGEX_NONNEGATIVE_PATTERN = "[0-9]+";
  
 public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new NonNegativeIntegerType());

  
  public NonNegativeIntegerType()
  {
    super(Datatype.INTEGER,BigInteger.ZERO,null);
  }
  
  
  protected NonNegativeIntegerType(int maxInclusive)
  {
    super(Datatype.INTEGER,BigInteger.ZERO,BigInteger.valueOf(maxInclusive));
  }
  
  protected NonNegativeIntegerType(BigInteger maxInclusive)
  {
    super(Datatype.INTEGER,BigInteger.ZERO,maxInclusive);
  }
  
  
}
