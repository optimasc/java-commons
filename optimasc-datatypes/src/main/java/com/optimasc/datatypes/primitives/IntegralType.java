/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import java.math.BigInteger;
import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.BoundedRangeFacet;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.PrecisionFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * This datatype represents an integral numeric value.
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li>INTEGER ASN.1 datatype</li>
 * <li>integer ISO/IEC 11404 General purpose datatype</li>
 * <li>integer XMLSchema built-in datatype</li>
 * </ul>
 * 
 * By default, the allowed range for this type is {@code Integer.MIN_VALUE}..
 * {@code Integer.MAX_VALUE}.
 * 
 * 
 * @author Carl Eric Codère
 */
public class IntegralType extends PrimitiveType implements BoundedRangeFacet,
    DatatypeConverter, PatternFacet, PrecisionFacet
{

  protected static final String REGEX_INTEGER_PATTERN = "-?[0-9]+";

  protected long minInclusive;

  protected long maxInclusive;

  public IntegralType()
  {
    super(Datatype.INTEGER,true);
    minInclusive = Integer.MIN_VALUE;
    maxInclusive = Integer.MAX_VALUE;
  }
  
  protected IntegralType(int type, int minInclusive, int maxInclusive)
  {
    super(type,true);
    this.minInclusive = minInclusive;
    this.maxInclusive = maxInclusive;
  }
  

  /**
   * Validates if the Integer, Byte or Short object is compatible with the
   * defined datatype.
   * 
   * @param value
   *          The object to check, either an Integer, Byte, Short object.
   * @throws IllegalArgumentException
   *           Throws this exception it is an invalid Object type.
   * @throws NumberFormatException
   *           Throws this exception if the value is not within the specified
   *           range
   */
  public void validate(java.lang.Object value) throws IllegalArgumentException,
      DatatypeException
  {
    long longValue;
    if (value instanceof BigInteger)
    {
      BigInteger b;
      b = (BigInteger) value;
      longValue = b.longValue();
    }
    else if (value instanceof Byte)
    {
      Byte b;
      b = (Byte) value;
      longValue = b.byteValue();
    }
    else if (value instanceof Short)
    {
      Short s;
      s = (Short) value;
      longValue = s.shortValue();

    }
    else if (value instanceof Integer)
    {
      Integer i;
      i = (Integer) value;
      longValue = i.intValue();
    }
    else if (value instanceof Long)
    {
      Long i;
      i = (Long) value;
      longValue = i.longValue();
    }
    else
      throw new IllegalArgumentException(
          "Invalid object type - should be Long, Integer, Short or Byte instance");

    if ((longValue > maxInclusive) || (longValue < minInclusive))
    {
      DatatypeException.throwIt(DatatypeException.ERROR_NUMERIC_OUT_OF_RANGE,
          "Integer is not within valid range.");
    }
  }

  /**
   * Validates if the int value is within the range of this defined datatype
   * 
   * @param integerValue
   *          The integer value to check.
   * @throws NumberFormatException
   *           Throws this exception if the value is not within the specified
   *           range
   */
  public void validate(long integerValue) throws DatatypeException
  {
    if ((integerValue > maxInclusive) || (integerValue < minInclusive))
    {
      DatatypeException.throwIt(DatatypeException.ERROR_NUMERIC_OUT_OF_RANGE,
          "Integer is not within valid range.");
    }
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

  public int getSize()
  {
    return getStorageSize(minInclusive, maxInclusive);
  }

  public Class getClassType()
  {
    return BigInteger.class;
  }

  public Object getObjectType()
  {
    return BigInteger.ZERO;
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
    IntegralType intType;
    if (!(obj instanceof IntegralType))
    {
      return false;
    }
    intType = (IntegralType) obj;
    if (this.minInclusive != intType.minInclusive)
    {
      return false;
    }
    if (this.maxInclusive != intType.maxInclusive)
    {
      return false;
    }
    return true;
  }

  /**
   * Checks if the parameter type is a subset of this type. It is a subset only
   * and only if the data ranges are within the ranges of this type.
   */
  public boolean isSubset(Object obj)
  {
    IntegralType intType;
    if (!(obj instanceof IntegralType))
      return false;
    intType = (IntegralType) obj;
    if ((this.minInclusive <= intType.minInclusive)
        && (this.maxInclusive >= intType.maxInclusive))
      return true;
    return false;
  }

  public boolean isSuperset(Object obj)
  {
    if (!(obj instanceof IntegralType))
      return false;
    if (isSubset(obj))
    {
      return false;
    }
    return true;
  }

  public long getMinInclusive()
  {
    return minInclusive;
  }

  public long getMaxInclusive()
  {
    return maxInclusive;
  }

  public void setMinInclusive(long value)
  {
    minInclusive = value;
  }

  public void setMaxInclusive(long value)
  {
    maxInclusive = value;
  }

  public Object parse(String value) throws ParseException
  {
    try
    {
      Long longValue = Long.valueOf(value);
      validate(longValue);
      return new BigInteger(value);
    } catch (NumberFormatException e)
    {
      throw new ParseException("Cannot parse string to integer type.", 0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Cannot parse string to integer type.", 0);
    }
  }

  public String getPattern()
  {
    return REGEX_INTEGER_PATTERN;
  }

  public void setPattern(String value)
  {
    throw new IllegalArgumentException("Pattern is read only for this datatype.");
  }

  public int getPrecision()
  {
    // Calculate the precision of the number
    long value = maxInclusive;
    int precision = 0;
    while (value > 9)
    {
      value = value / 10;
      precision++;
    }
    return precision;
  }

  public int getScale()
  {
    // Always return 0.
    return 0;
  }

}