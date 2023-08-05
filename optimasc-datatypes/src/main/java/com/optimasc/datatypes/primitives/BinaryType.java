package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.derived.UnsignedByteType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Datatype that represents binary data. It includes a minLength
 * and maxLength attribute as a facet indicating the allowed length of the data.
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li><code>OCTET STRING</code> ASN.1 datatype</li>
 * <li><code>octetstring</code> ISO/IEC 11404 General purpose datatype</li>
 * <li><code>hexBinary</code> and <code>base64Binary</code> XMLSchema built-in datatype</li>
 * <li><code>BINARY LARGE OBJECT</code> in SQL2003</li>
 * </ul>
 * 
 * By default, the allowed minimum length of the binary blob type is 0 octects,
 * and the default maximum length is {@code Integer.MAX_VALUE}.
 * 
 * Internally, values of this type are represented as <code>byte[]</code>.
 * 
 * @author Carl Eric Codère
 */
public class BinaryType extends SequenceType implements LengthFacet, PatternFacet, DatatypeConverter, Parseable
{
  public static final BinaryType DEFAULT_INSTANCE = new BinaryType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  protected static final byte[] INSTANCE_BINARY = new byte[0];
  
  protected static final String REGEX_PATTERN = "([0-9a-fA-F][0-9a-fA-F])+";

  /** The minimum and maximum length in bytes. */
  protected LengthHelper lengthHelper;
  

  public BinaryType()
  {
    super(Datatype.BINARY,false,UnsignedByteType.DEFAULT_TYPE_REFERENCE);
    lengthHelper = new LengthHelper();
    setMinLength(0);
    setMaxLength(Integer.MAX_VALUE);
  }
  
  /**
   * Creates a new binary datatype with the specified minimum length and maximum
   * length.
   * 
   * @param minLength
   *          The minimum lengh of the bytes associated with this datatype.
   * @param maxLength
   *          The maximum length of the bytes associated with this datatype.
   */
  public BinaryType(int minLength, int maxLength)
  {
    super(Datatype.BINARY,false,UnsignedByteType.DEFAULT_TYPE_REFERENCE);
    lengthHelper = new LengthHelper();
    setMinLength(minLength);
    setMaxLength(maxLength);
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
      DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
          "The byte array does not match the datatype specification");
    }
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

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

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
  
  public static String toString(byte[] values)
  {
    StringBuffer buffer = new StringBuffer(64);
    for (int i=0; i < values.length; i++)
    {
        buffer.append(Character.forDigit((values[i] >> 4) & 0xF, 16));
        buffer.append(Character.forDigit((values[i] & 0xF), 16));
    }
    return buffer.toString();
  }


}
