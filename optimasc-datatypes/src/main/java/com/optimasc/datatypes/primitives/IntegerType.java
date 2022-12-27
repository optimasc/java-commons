/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.IntegerRangeFacet;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

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
public class IntegerType extends PrimitiveType implements IntegerRangeFacet, DatatypeConverter, PatternFacet
{
  protected static final Byte BYTE_INSTANCE = new Byte((byte) 0);
  protected static final Short SHORT_INSTANCE = new Short((short) 0);
  protected static final Integer INTEGER_INSTANCE = new Integer(0);
  protected static final Long LONG_INSTANCE = new Long(0);
  
  protected static final String REGEX_INTEGER_PATTERN = "-?[0-9]+";

  protected long minInclusive;

  protected long maxInclusive;

  public IntegerType()
  {
    super(Datatype.INTEGER);
    minInclusive = Integer.MIN_VALUE;
    maxInclusive = Integer.MAX_VALUE;
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
  public void validate(java.lang.Object value) throws IllegalArgumentException, DatatypeException
  {
    long longValue;
    if (value instanceof Byte)
    {
      Byte b;
      b = (Byte) value;
      longValue = b.byteValue();
    } else if (value instanceof Short)
    {
      Short s;
      s = (Short) value;
      longValue = s.shortValue();

    } else if (value instanceof Integer)
    {
      Integer i;
      i = (Integer) value;
      longValue = i.intValue();
    }
    else
    if (value instanceof Long)
    {
      Long i;
      i = (Long) value;
      longValue = i.longValue();
    } else
      throw new IllegalArgumentException(
          "Invalid object type - should be Long, Integer, Short or Byte instance");

    if ((longValue > maxInclusive) || (longValue < minInclusive))
    {
      DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,
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
      DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,
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
    switch (getSize())
    {
      case 1:
        return Byte.class;
      case 2:
        return Short.class;
      case 4:
        return Integer.class;
      case 8:
        return Long.class;
    }
    return Integer.class;
  }

  public Object getObjectType()
  {
    switch (getSize())
    {
      case 1:
        return BYTE_INSTANCE;
      case 2:
        return SHORT_INSTANCE;
      case 4:
        return INTEGER_INSTANCE;
      case 8:
        return LONG_INSTANCE;
    }
    return INTEGER_INSTANCE;
  }

  public Object accept(DatatypeVisitor v, Object arg)
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
    IntegerType intType;
    if (!(obj instanceof IntegerType))
    {
      return false;
    }
    intType = (IntegerType) obj;
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
    IntegerType intType;
    if (!(obj instanceof IntegerType))
      return false;
    intType = (IntegerType) obj;
    if ((this.minInclusive <= intType.minInclusive) && (this.maxInclusive >= intType.maxInclusive))
      return true;
    return false;
  }

  public boolean isSuperset(Object obj)
  {
    if (!(obj instanceof IntegerType))
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
      switch (getSize())
      {
        case 1:
            return new Byte((byte)longValue.longValue());
        case 2:
          return new Short((short)longValue.longValue());
        case 4:
          return new Integer((int)longValue.longValue());
        case 8:
          return longValue;
        default:
          return null;
      }
    } catch (NumberFormatException e)
    {
      throw new ParseException("Cannot parse string to integer type.",0);
  } catch (DatatypeException e)
  {
    throw new ParseException("Cannot parse string to integer type.",0);
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
  

}
