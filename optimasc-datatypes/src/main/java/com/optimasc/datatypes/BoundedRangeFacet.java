package com.optimasc.datatypes;

/** Represents the lower and upper bounds on exact ordered
 *  values. The representation must be convered to an
 *  integral value, see specific datatype information
 *  for integral value representation.
 *
 * @author Carl Eric Codere
 */
public interface BoundedRangeFacet
{
  /** Set the minimum allowed value of this integer. */
  public void setMinInclusive(long value);
  /** Set the maximum allowed value of this integer. */
  public void setMaxInclusive(long value);
  /** Returns the minimum allowed value of this integer. */
  public long getMinInclusive();
  /** Returns the maximum allowed value of this integer. */
  public long getMaxInclusive();
}
