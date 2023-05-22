package com.optimasc.datatypes;

/** Represents a length constraint / restriction facet.
 *
 * @author Carl Eric Codere
 */
public interface LengthFacet
{
  /** Sets minimum allowed length of this data type */
  public void setMinLength(int value);
  /** Sets maximum allowed length of this data type */
  public void setMaxLength(int value);
  /** Returns the minimum allowed elements. */
  public int getMinLength();
  /** Returns the maximum allowed elements. */
  public int getMaxLength();
}
