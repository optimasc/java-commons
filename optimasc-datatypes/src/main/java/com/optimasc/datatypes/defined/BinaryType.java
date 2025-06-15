package com.optimasc.datatypes.defined;

import java.util.Comparator;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.TypeFactory;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.ByteArrayComparator;

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
  protected static final String REGEX_PATTERN = "([0-9a-fA-F][0-9a-fA-F])+";
  
  /** The arrays of choices. */
//  protected byte[] enumeration[];
  protected static final Comparator comparator = new ByteArrayComparator();
  
  
  public BinaryType()
  {
    super(TypeFactory.getDefaultInstance(UnsignedByteType.class), byte[].class);
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
    super(minLength,maxLength,TypeFactory.getDefaultInstance(UnsignedByteType.class),byte[].class);
  }
  
  public BinaryType(byte[] choices[])
  {
    super(choices,TypeFactory.getDefaultInstance(UnsignedByteType.class),byte[].class);
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
    if (!(obj instanceof BinaryType))
    {
      return false;
    }
    return super.equals(obj);
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

  /** {@inheritDoc} 
   * 
   */
  public Object[] getChoices()
  {
    return  enumeration;
  }

  /** {@inheritDoc} 
   * 
   */
/*  public boolean validateChoice(byte[] value)
  {
    if (enumeration == null)
      return true;
    for (int i=0; i < enumeration.length; i++)
    {
      if (Arrays.equals(enumeration[i], value)==true)
      {
        return true;
      }
    }
    return false;
  }*/

/*  protected boolean validateChoice(Object value)
  {
    return validateChoice((byte[])value);
  }*/
  

  public String getGPDName()
  {
    return "octetstring";
  }

  

}
