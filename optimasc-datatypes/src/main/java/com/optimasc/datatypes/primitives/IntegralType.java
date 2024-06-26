/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DecimalRangeHelper;
import com.optimasc.datatypes.DecimalEnumerationFacet;
import com.optimasc.datatypes.PrecisionFacet;
import com.optimasc.datatypes.DecimalRangeFacet;
import com.optimasc.datatypes.SubSet;
import com.optimasc.datatypes.Type;
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
 * 
 * </ul>
 * 
 * <p>Internally, values of this type are represented as {@link BigInteger}.</p>
 * 
 * 
 * @author Carl Eric Codère
 */
public class IntegralType extends DecimalType
{
  public static final IntegralType DEFAULT_INSTANCE = new IntegralType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  protected static final String REGEX_INTEGER_PATTERN = "-?[0-9]+";
  

  public IntegralType()
  {
    super(Datatype.INTEGER,false);
  }
  
  protected IntegralType(int type, int minInclusive, int maxInclusive)
  {
    super(type,false);
    rangeHelper = new DecimalRangeHelper(BigDecimal.valueOf(minInclusive),
        BigDecimal.valueOf(maxInclusive));
  }
  
  protected IntegralType(int type, BigInteger minInclusive, BigInteger maxInclusive)
  {
    super(type,false);
    rangeHelper = new DecimalRangeHelper(new BigDecimal(minInclusive),
        new BigDecimal(maxInclusive),false);
  }
  
  


  /**
   * Depending on the range of the values determine the storage size of the
   * integer.
   */
  public static int getStorageSize(long minInclusive, long maxInclusive)
  {
    if ((maxInclusive <= Byte.MAX_VALUE) && (minInclusive >= Byte.MIN_VALUE))
    {
      return 1;
    }
    if ((maxInclusive <= Short.MAX_VALUE) && (minInclusive >= Short.MIN_VALUE))
    {
      return 2;
    }
    if ((maxInclusive <= Integer.MAX_VALUE) && (minInclusive >= Integer.MIN_VALUE))
    {
      return 4;
    }
    if ((maxInclusive <= Long.MAX_VALUE) && (minInclusive >= Long.MIN_VALUE))
    {
      return 8;
    }
    return 0;
  }

  public Class getClassType()
  {
    return BigInteger.class;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  /**
   * Compares this IntegerType to the specified object. The result is true if
   * and only if the argument is not null and is a IntegerType object that has
   * the same constraints (minInclusive, maxIncluseive) as this object
   * 
   */
  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }
    if (!(obj instanceof IntegralType))
    {
      return false;
    }
    return super.equals(obj);
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
  public void setChoices(BigDecimal[] choices)
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
  
  
  public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
  {
    BigDecimal returnValue = (BigDecimal) super.toValue(ordinalValue, conversionResult);
    if (returnValue == null)
    {
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
  

}
