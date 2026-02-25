package com.optimasc.datatypes;

import com.optimasc.datatypes.facets.Facet;

/** Interface for data types that are bounded, 
 *  such as with ranges of values.
 *
 */
public interface BoundedProperty extends Facet
{
  /** Returns if this datatype is bounded with
   *  minimal and/or maximal ranges.
   * 
   * @return <code>true</code> if this datatype
   *   is bounded, otherwise <code>false</code>.
   */
  public boolean isBounded();
}
