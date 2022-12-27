package com.optimasc.datatypes;

/** Represents constraints / restrictions
 *  on integer values.
 *
 * @author Carl Eric Codere
 */
public interface IntegerRangeFacet
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
