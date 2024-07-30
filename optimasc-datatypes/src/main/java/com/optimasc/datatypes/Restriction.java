package com.optimasc.datatypes;

public interface Restriction
{
  /** Returns if this type is a restriction of the 
   *  specified type. A restriction indicates that 
   *  there the value space is smaller or that there are
   *  other constraints associated with it such as the
   *  allowed length value. See the XSD Schema restriction
   *  element for more information. 
   * 
   * @param value [in] The tyoe to verify against
   * @return <code>true</code> if this type is
   *   a restriction of the specified type.
   * @throws IllegalArgumentException if the 
   *   type is not the same base data type
   *   as this type.
   */
  public boolean isRestrictionOf(Datatype value);

}
