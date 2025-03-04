package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Datatype that represents a subset of numeric values which
 * can be represented as decimal values. 
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li><code>REAL</code> ASN.1 datatype</li>
 * <li><code>scaled(10,factor)</code> ISO/IEC 11404 General purpose datatype</li>
 * <li><code>decimal</code> XMLSchema built-in datatype</li>
 * <li><code>NUMERIC</code> SQL92 built-in datatype with unlimited precision</li>
 * 
 * </ul>
 * 
 * <p>Internally, values of this type are represented as {@link BigDecimal} with 
 * a rounding mode of <code>BigDecimal.ROUND_HALF_EVEN</code>.</p>
 * 
 * 
 * @author Carl Eric Codère
 */
public class DecimalType extends AbstractNumberType
{
  private static DecimalType defaultTypeInstance;
  private static TypeReference defaultTypeReference;
  
  
  /** Creates an unbounded scaled numeric type. */ 
  public DecimalType(int scale)
  {
    super(scale);
  }
  
  /** Creates a scaled value with a scale of 6 */ 
  public DecimalType()
  {
    super(6);
  }
  
  
  /** Creates a bounded scaled numeric type. */ 
  
  /** Creates a bounded scaled numeric type. The scale
   *  of both bounds, when specified, must be equal, and this
   *  will be the scale of this datatype.
   * 
   * @param minValue [in] The lower allowed inclusive bound
   * @param maxValue [in] The upper allowed inclusive bound
   * @throws IllegalArgumentException Thrown if the scales of the
   *  bounds are different. 
   */
  public DecimalType(BigDecimal minValue, BigDecimal maxValue) throws IllegalArgumentException
  {
    super(minValue,maxValue);
  }
  
  /** Creates a scaled numeric type with selected values allowed only 
   *  (enumeration facet). The scale of each choice value must be
   *  equal with each other, and this scale will be used as the scale
   *  of this datatype. 
   * 
   * @param choices [in] The possible choices of values
   * @throws IllegalArgumentException Thrown if any of 
   *   the scales of the choices differ.
   */
  public DecimalType(Number[] choices) throws IllegalArgumentException
  {
    super(choices);
  }
  
  /** Creates a scaled numeric type with selected values allowed only (enumeration facet). */ 
  public DecimalType(long[] choices)
  {
    super(choices);
  }
  
  
  
  public Class getClassType()
  {
    return BigDecimal.class;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }
  
  public static TypeReference getInstance()
  {
    if (defaultTypeInstance == null)
    {
      defaultTypeInstance = new DecimalType();
      defaultTypeReference = new NamedTypeReference("scaled(10,4)" ,defaultTypeInstance);
    }
    return defaultTypeReference; 
  }

}
