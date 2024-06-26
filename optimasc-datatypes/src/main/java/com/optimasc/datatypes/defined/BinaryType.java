package com.optimasc.datatypes.defined;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Datatype that represents binary data. It includes a minLength
 * and maxLength attribute as a facet indicating the allowed length of the data.
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li><code>OCTET STRING</code> ASN.1 datatype</li>
 * <li><code>octetstring</code> ISO/IEC 11404 defined datatype</li>
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
public class BinaryType extends SequenceType 
{
  public static final BinaryType DEFAULT_INSTANCE = new BinaryType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  protected static final String REGEX_PATTERN = "([0-9a-fA-F][0-9a-fA-F])+";

  
  public BinaryType()
  {
    super(Datatype.BINARY,UnsignedByteType.DEFAULT_TYPE_REFERENCE);
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
    super(Datatype.BINARY,minLength,maxLength,UnsignedByteType.DEFAULT_TYPE_REFERENCE);
  }
  

  
  /** {@inheritDoc}
   * 
   *  <p>The input to this method is expected to be a byte
   *  array, and the return value will be the byte array after
   *  verifying the constraints associated with this datatype
   *  definition.</p> 
   *  
   * 
   */
  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
    if (byte[].class==value.getClass())
    {
      // If no bounds defined, simply return the value
      if (isBounded()==false)
        return value;
      
      byte bArray[] = (byte[])value;
      if (lengthHelper.validateLength(bArray.length)==false)
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_BOUNDS_RANGE,
            "Length of byte array must be between, " + lengthHelper.toString()
            +", got "+bArray.length);
        return null;
      }
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }

  public Class getClassType()
  {
    return byte[].class;
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
