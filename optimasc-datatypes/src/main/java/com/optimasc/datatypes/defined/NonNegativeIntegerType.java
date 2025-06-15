package com.optimasc.datatypes.defined;

import java.math.BigInteger;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.IntegralType;

/** Represents a specific defined datatype that is a natural number 
 *  (a non-negative value).
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>INTEGER(0..MAX)</code> ASN.1 datatype</li>
 *   <li><code>naturalnumber</code> ISO/IEC 11404 defined datatype</li>
 *   <li><code>nonNegativeInteger</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 *  <p>Internally, values of this type are represented as a 
 *  <code>BigInteger</code> object.</p>
 *
 * @author Carl Eric Codère
 */
public class NonNegativeIntegerType extends IntegralType
{
  protected static final String REGEX_NONNEGATIVE_PATTERN = "[0-9]+";

  
  public NonNegativeIntegerType()
  {
    super(BigInteger.ZERO,null);
  }
  
  
  protected NonNegativeIntegerType(int maxInclusive)
  {
    super(BigInteger.ZERO,BigInteger.valueOf(maxInclusive));
  }
  
  protected NonNegativeIntegerType(BigInteger maxInclusive)
  {
    super(BigInteger.ZERO,maxInclusive);
  }
  
}
