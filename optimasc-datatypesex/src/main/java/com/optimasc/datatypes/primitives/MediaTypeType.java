package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;
import com.optimasc.lang.MediaType;

/** Represents an Internet media type datatype, a type and a subtype
 *  with optional parameters, as defined by IETF RFC 6838.
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>Media Type</code> IETF draft-codere-ldapsyntax syntax
 *     (OID: 1.3.6.1.4.1.61799.5.40.26.5)</li>
 *   <li><code>VisibleString (SIZE(3..255))</code> ASN.1 datatype</li>
 *  </ul>
 *
 *  <p>Internally, values of this type are represented as
 *  {@link com.optimasc.lang.MediaType} objects.</p>
 *
 * @author Carl Eric Codère
 */
public class MediaTypeType extends PrimitiveType
{

  /** Creates a media type datatype with no restriction on its value space. */
  public MediaTypeType()
  {
    super(false);
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    if ((v instanceof TypeVisitorEx) == false)
      throw new IllegalArgumentException("Visitor must of type " + TypeVisitorEx.class.getName());
    return ((TypeVisitorEx) v).visit(this, arg);
  }

  public Class getClassType()
  {
    return MediaType.class;
  }

  public boolean isRestrictionOf(Datatype value)
  {
    if ((value instanceof MediaTypeType) == false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '" + value.getClass().getName() + "'.");
    }
    return false;
  }

  public int hashCode()
  {
    return MediaTypeType.class.hashCode();
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    return true;
  }
}
