/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.NumberEnumerationHelper;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.NumberComparator;
import com.optimasc.lang.NumberSelectItem;
import com.optimasc.lang.NumberedSelectItems;
import com.optimasc.lang.NumberedSelectItems.NumberSelectRange;
import com.optimasc.lang.NumberedSelectItems.NumberSelectValue;

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
 * <p>Internally, values of this type are represented as {@link BigInteger} and 
 * value conversion returns a <code>BigInteger</code>, but when converting 
 * from decimal types, the rounding mode is <code>BigDecimal.ROUND_DOWN</code>.</p>
 * 
 * 
 * @author Carl Eric Codère
 */
public class IntegralType extends DecimalType
{
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
    enumHelper = new NumberEnumerationHelper(BigInteger.valueOf(minInclusive),
        BigInteger.valueOf(maxInclusive));
  }
  
  /** Creates a bounded integer type. */ 
  public IntegralType(BigInteger minInclusive, BigInteger maxInclusive)
  {
    super(0);
    enumHelper = new NumberEnumerationHelper(minInclusive,
        maxInclusive);
  }
  
  /** Creates an integer numeric type with selected values allowed only (enumeration facet). */ 
  public IntegralType(BigInteger[] choices)
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
  public void setAllowedValues(Object[] choices)
  {
    enumHelper = new NumberEnumerationHelper();
    Class allowedValueClass = enumHelper.getAllowedValuesClass();
    for (int i=0; i < choices.length; i++)
    {
      if (allowedValueClass.isInstance(choices[i])==false)
      {
        throw new IllegalArgumentException("Enumeration elements should be of type '"+ allowedValueClass.getName()+"'");
      }
      if (NumberComparator.getScale((Number)choices[i])>0)
      {
        throw new IllegalArgumentException("Scale should be zero for integer choices.");
      }
    }
    enumHelper.setAllowedValues(choices);
  }
  
  
  public void setAllowedValuesAsSelectItems(NumberSelectItem[] choices)
  {
    for (int i=0; i < choices.length; i++)
    {
      if (choices[i] instanceof NumberedSelectItems.NumberSelectValue)
      {
        NumberedSelectItems.NumberSelectValue v = (NumberSelectValue) choices[i];
        if (NumberComparator.getScale(v.getValue())>0)
        {
          throw new IllegalArgumentException("Scale should be zero for integer choices.");
        }
      } else
      if (choices[i] instanceof NumberedSelectItems.NumberSelectRange)
      {
        NumberedSelectItems.NumberSelectRange r = (NumberSelectRange) choices[i];
        if ((NumberComparator.getScale(r.getMinInclusive())>0) || (NumberComparator.getScale(r.getMaxInclusive())>0))
        {
          throw new IllegalArgumentException("Scale should be zero for integer choices.");
        }
      }
      else
      if (choices[i] instanceof NumberedSelectItems.NumberAnyValue)
      {
        
      } else
      {
        throw new IllegalArgumentException("Illegal select item instance - internal error.");
      }
      if (NumberComparator.getScale((Number)choices[i])>0)
      {
        throw new IllegalArgumentException("Scale should be zero for integer choices.");
      }
    }
    enumHelper.setAllowedValues(choices);
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

  public String toString()
  {
    if (enumHelper == null)
    {
      return "integer";
    }
    String constraint = enumHelper.toString();
    if (constraint.length()==0)
       return "integer";
    return "integer "+constraint;
  }  
  

}
