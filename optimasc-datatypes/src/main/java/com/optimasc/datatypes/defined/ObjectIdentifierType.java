package com.optimasc.datatypes.defined;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Convertable;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.facets.LengthFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents an ITU-T/ISO Object identifier as
 *  a sequence of natural numbers.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>OBJECT IDENTIFIER</code> ASN.1 datatype</li>
 *   <li><code>objectidentifier</code> ISO/IEC 11404 defined datatype</li>
 *  </ul>
 *  
 *  <p>Values of this type are represented as a <code>java.lang.String</code>
 *  holding the dotted-decimal form, for example <code>1.3.6.1.4.1</code>:
 *  two or more arcs separated by <code>.</code>, each a decimal number
 *  without leading zeros (the <code>numericoid</code> production of IETF
 *  RFC 4517). Arcs are not limited in size, so object identifiers such as
 *  those under <code>2.25</code>, whose arcs are 128-bit numbers, are
 *  supported. Descriptors such as <code>cn</code> are not values of this
 *  type.</p>
 *
 * @author Carl Eric Codère
 */
public class ObjectIdentifierType extends SequenceType implements Convertable
{
  private static final TypeReference TYPE_REFERENCE =  new UnnamedTypeReference(new NonNegativeIntegerType(Integer.MAX_VALUE));
  
  public ObjectIdentifierType()
  {
    super(1,LengthFacet.UNBOUNDED, TYPE_REFERENCE,int[].class);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
  }

  /** @return <code>String.class</code>; values are in dotted-decimal form. */
  public Class getClassType()
  {
    return String.class;
  }

  /**
   * Returns whether the string is a numeric object identifier: two or more
   * decimal arcs separated by <code>.</code>, without leading zeros.
   */
  public static boolean isNumericOID(CharSequence value)
  {
    int length = value.length();
    int arcs = 0;
    int start = 0;
    for (int i = 0; i <= length; i++)
    {
      if ((i == length) || (value.charAt(i) == '.'))
      {
        int size = i - start;
        if (size == 0)
          return false;
        if ((size > 1) && (value.charAt(start) == '0'))
          return false;
        arcs++;
        start = i + 1;
        continue;
      }
      char c = value.charAt(i);
      if ((c < '0') || (c > '9'))
        return false;
    }
    return arcs >= 2;
  }

  public boolean isValid(Object value)
  {
    return (value instanceof String) && isNumericOID((String) value);
  }

  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    if (value instanceof CharSequence)
    {
      String s = value.toString();
      if (isNumericOID(s))
        return s;
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,
          "Value is not a numeric object identifier in dotted-decimal form.");
      return null;
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,
        "Unsupported value of class '" + (value == null ? "null" : value.getClass().getName()) + "'.");
    return null;
  }

  public String toString()
  {
    return "objectidentifier";
  }
}
