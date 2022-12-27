package com.optimasc.datatypes;

/** Represents a length constraint / restriction.
 *
 * @author Carl Eric Codere
 */
public interface LengthFacet
{
  /** Sets minimum allowed length of this data type */
  public void setMinLength(int value);
  /** Sets maximum allowed length of this data type */
  public void setMaxLength(int value);
  public int getMinLength();
  public int getMaxLength();
}
