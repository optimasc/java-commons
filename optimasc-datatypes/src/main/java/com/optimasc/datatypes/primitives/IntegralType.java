/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DecimalRangeHelper;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Datatype that represents an integral numeric value. For
 * performance reasons, derived integral types should be
 * used instead of this one.
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li><code>INTEGER</code> ASN.1 datatype</li>
 * <li><code>integer</code> ISO/IEC 11404 General purpose datatype</li>
 * <li><code>integer</code> XMLSchema built-in datatype</li>
 * </ul>
 * 
 * <p>Internally, values of this type are represented as {@link BigInteger}, but
 * when converting from decimal types, the rounding mode is 
 * <code>BigDecimal.ROUND_DOWN</code>.</p>
 * 
 * 
 * @author Carl Eric Codère
 */
public class IntegralType extends DecimalType
{
  private static IntegralType defaultTypeInstance;
  private static TypeReference defaultTypeReference;
  
  protected static final String REGEX_INTEGER_PATTERN = "-?[0-9]+";
  

  /** Creates an unbounded integer type. */ 
  public IntegralType()
  {
    super(0);
  }
  
  /** Creates a bounded integer type. */ 
  public IntegralType(int minInclusive, int maxInclusive)
  {
    super(0);
    rangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(minInclusive),
        BigDecimal.valueOf(maxInclusive));
  }
  
  /** Creates a bounded integer type. */ 
  public IntegralType(BigInteger minInclusive, BigInteger maxInclusive)
  {
    super(0);
    BigDecimal minValue = null;
    BigDecimal maxValue = null;
    if (minInclusive != null)
      minValue = new BigDecimal(minInclusive);
    if (maxInclusive != null)
      maxValue = new BigDecimal(maxInclusive);
    rangeHelper = new DecimalRangeHelper(minValue,
        maxValue);
  }
  
  /** Creates an integer numeric type with selected values allowed only (enumeration facet). */ 
  public IntegralType(BigDecimal[] choices)
  {
    super(choices);
  }
  
  /** Creates an integer numeric type with selected values allowed only (enumeration facet). */ 
  public IntegralType(long[] choices)
  {
    super(choices);
  }
  
  


  public Class getClassType()
  {
    return BigInteger.class;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  /** Returns the scale of this number, this
   *  value is always zero for this datatype
   *  and derived types.
   */
  public int getScale()
  {
    return 0;
  }

  /** {@inheritDoc}
   * 
   *  <p>On top of the standard API specification, 
   *  this verifies that the scale of each element
   *  in the choices is zero, hence representing an
   *  integer value.</p>
   *  
   *  @throws IllegalArgumentException If any of the
   *    value does not have a scale of zero.
   * 
   */
  protected void setChoices(BigDecimal[] choices)
  {
    for (int i=0; i < choices.length; i++)
    {
      if (choices[i].scale()>0)
      {
        throw new IllegalArgumentException("Scale should be zero for integer choices.");
      }
    }
    enumHelper.setChoices(choices);
  }
  
  
  protected Object toValueNumber(Number ordinalValue, TypeCheckResult conversionResult)
  {
    BigDecimal returnValue = (BigDecimal) super.toValueNumber(ordinalValue, conversionResult);
    if (returnValue == null)
    {
      return null;
    }
    if (TypeUtilities.isExact(returnValue)==false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_NUMERIC_OUT_OF_RANGE,"Number is outside of valide range");
      return null;
    }
    return returnValue.toBigInteger();
  }

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    Object result = super.toValue(ordinalValue, conversionResult);
    if (result == null)
    {
      return null;
    }
    return BigInteger.valueOf(ordinalValue);
  }  
  

  public static TypeReference getInstance()
  {
    if (defaultTypeInstance == null)
    {
      defaultTypeInstance = new IntegralType();
      defaultTypeReference = new NamedTypeReference("integer" ,defaultTypeInstance);
    }
    return defaultTypeReference; 
  }
  
}
