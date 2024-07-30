package com.optimasc.datatypes;

import com.optimasc.util.Pattern;

/** Represents a regular expression pattern
 *  facet to limit the value space of a
 *  string representation of a value. 
 * 
 * @author Carl Eric Codere
 *
 */
public interface PatternFacet
{
  /** Returns the register expression patterns
   *  used to validate the character representation
   *  of this object.
   * 
   * @return The array of patterns or <code>null</code> if no
   *   patterns have been registered.
   */
  public Pattern[] getPatterns();
  /** Validates if the character sequence of this
   *  object fits within the allowed patterns.
   * 
   * @param value [in] The value to verify
   * @return <code>true</code> if the the value
   *   fits one of the allowed patterns, otherwise <code>false</code>.
   */
  public boolean validatePatterns(CharSequence value);
}
