package com.optimasc.datatypes;

/** Interface that is implemented to represent
 *  a type that may be bounded, such as with
 *  ranges of values.
 * 
 *
 */
public interface BoundedFacet
{
  /** Returns if this datatype is bounded with
   *  minimal and/or maximal ranges.
   * 
   * @return <code>true</code> if this datatype
   *   is bounded, otherwise <code>false</code>.
   */
  public boolean isBounded();
}
