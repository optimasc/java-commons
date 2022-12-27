package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/**
 * This class is used to represent a binary datatype. It includes a minLength
 * and maxLength attribute as a facet indicating the allowed length of the data.
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li>OCTET STRING ASN.1 datatype</li>
 * <li>octetstring ISO/IEC 11404 General purpose datatype</li>
 * <li>hexBinary and base64Binary XMLSchema built-in datatype</li>
 * </ul>
 * 
 * By default, the allowed minimum length of the binary blob type is 0 octects,
 * and the default maximum length is {@code Integer.MAX_VALUE}.
 * 
 * @author Carl Eric Codère
 */
public class BinaryType extends Datatype implements LengthFacet, PatternFacet, DatatypeConverter
{
  protected static final byte[] INSTANCE_BINARY = new byte[0];
  
  protected static final String REGEX_PATTERN = "([0-9a-fA-F][0-9a-fA-F])+";

  /** The minimum and maximum length in bytes. */
  protected LengthHelper lengthHelper;
  

  /**
   * Creates a new binary datatype with the specified minimum length and maximum
   * length and with an optional regular expression pattern.
   * 
   * @param name
   *          The associated datatype name
   * @param minLength
   *          The minimum lengh of the bytes associated with this datatype.
   * @param maxLength
   *          The maximum length of the bytes associated with this datatype.
   * @param comment
   *          The comment associated with this datatype.
   */
  public BinaryType()
  {
    super(Datatype.BINARY);
    lengthHelper = new LengthHelper();
    setMinLength(0);
    setMaxLength(Integer.MAX_VALUE);
  }

  /**
   * Validates if the byte[] array is valid with the defined datatype.
   * 
   * @param value
   *          The object to check, this should be a byte[] object instance
   * @throws IllegalArgumentException
   *           Throws this exception it is an invalid Object type.
   * @throws NumberFormatException
   *           Throws this exception if the value is not within the specified
   *           range and pattern
   */
  public void validate(java.lang.Object value) throws IllegalArgumentException, DatatypeException
  {
    byte[] bArray;
    checkClass(value);
    bArray = (byte[]) value;
    if ((bArray.length < getMinLength()) || (bArray.length > getMaxLength()))
    {
      DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,
          "The byte array does not match the datatype specification");
    }
  }

  public int getSize()
  {
    return getMaxLength();
  }

  public Class getClassType()
  {
    return byte[].class;
  }

  public void setMinLength(int value)
  {
    lengthHelper.setMinLength(value);
  }

  public void setMaxLength(int value)
  {
    lengthHelper.setMaxLength(value);
  }

  public int getMinLength()
  {
    return lengthHelper.getMinLength();
  }

  public int getMaxLength()
  {
    return lengthHelper.getMaxLength();
  }

  public Object accept(DatatypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  /**
   * Validates the equality of two . They shall be equal if they return that
   * they will allow the same value ranges and are both of BinaryType or its
   * derived classes.
   * 
   */

  /**
   * Compares this BinaryType to the specified object. The result is true if and
   * only if the argument is not null and is a BinaryType object that has the
   * same constraints (minLenghth, maxLength) as this object
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

    BinaryType binType;
    if (!(obj instanceof BinaryType))
    {
      return false;
    }
    binType = (BinaryType) obj;
    if (this.getMinLength() != binType.getMinLength())
    {
      return false;
    }
    if (this.getMaxLength() != binType.getMaxLength())
    {
      return false;
    }
    return true;
  }

  public boolean isSubset(Object obj)
  {
    return false;
  }

  public boolean isSuperset(Object obj)
  {
    return false;
  }

  public Object getObjectType()
  {
    return INSTANCE_BINARY;
  }

  public Object parse(String value) throws ParseException
  {
    int len = value.length();
    byte[] data = new byte[len / 2];
    for (int i = 0; i < len; i += 2)
    {
      int b1 = Character.digit(value.charAt(i), 16);
      if (b1 == -1)
      {
        throw new ParseException("Cannot convert hexBinary to byte", i);
      }
      int b2 = Character.digit(value.charAt(i + 1), 16);
      if (b1 == -1)
      {
        throw new ParseException("Cannot convert hexBinary to byte", i + 1);
      }
      data[i / 2] = (byte) ((b1 << 4) + b2);
    }
    try
    {
      validate(data);
    } catch (DatatypeException e)
    {
      throw new ParseException("Cannot convert hexBinary to byte", 0);
    }
    return data;
  }

  public String getPattern()
  {
    return REGEX_PATTERN;
  }

  public void setPattern(String value)
  {
    throw new IllegalArgumentException("Pattern is read only for this datatype.");
  }

}
