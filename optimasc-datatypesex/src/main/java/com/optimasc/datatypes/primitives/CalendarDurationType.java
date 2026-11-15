package com.optimasc.datatypes.primitives;

import javax.xml.datatype.Duration;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;

/** Represents a duration datatype made of a month component
 *  (years and months) and a day-time component (days, hours,
 *  minutes and seconds), which are never converted into each other.
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>duration</code> XMLSchema built-in datatype</li>
 *   <li><code>Duration</code> IETF draft-codere-ldapsyntax syntax
 *     (OID: 1.3.6.1.4.1.61799.5.40.34)</li>
 *   <li><code>record (years : timeinterval(year, 10, 0), months : timeinterval(month, 10, 0),
 *     days : timeinterval(day, 10, 0), time : timeinterval(second, 10, 3))</code>
 *     ISO/IEC 11404 datatype</li>
 *  </ul>
 *
 *  <p>Internally, values of this type are represented as
 *  <code>javax.xml.datatype.Duration</code> objects.</p>
 *
 *  <p>This type is not ordered: the order relation of durations is
 *  partial, for example <code>P1M</code> and <code>P30D</code> cannot
 *  be compared.</p>
 *
 *  <p>This differs from {@link DurationType}, which represents an
 *  elapsed time as a single count of one time unit.</p>
 *
 * @author Carl Eric Codère
 */
public class CalendarDurationType extends PrimitiveType
{

  /** Creates a duration type with no restriction on its value space. */
  public CalendarDurationType()
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
    return Duration.class;
  }

  public boolean isRestrictionOf(Datatype value)
  {
    if ((value instanceof CalendarDurationType) == false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '" + value.getClass().getName() + "'.");
    }
    return false;
  }

  public int hashCode()
  {
    return CalendarDurationType.class.hashCode();
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
