package com.optimasc.datatypes;

import com.optimasc.util.Pattern;

/** Represents a regular expression pattern
 *  facet to limit the value space of a
 *  string representation of a value. 
 *  
 *  This is equivalent to the following constraints:
 *  <ul>
 *   <li><code>PATTERN</code> ASN.1 constraint</li>
 *   <li><code>pattern</code> XMLSchema constraining facet</li>
 *   <li><code>X-VALUE-REGEX</code> and LDAP Attribute definition extension syntax constraining facet</li>
 *  </ul>
 *  
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
  
//  public void setPatterns(Pattern[] patterns);
  
  /** Validates if the character sequence of this
   *  object fits within the allowed patterns.
   * 
   * @param value [in] The value to verify
   * @return <code>true</code> if the the value
   *   fits one of the allowed patterns, otherwise <code>false</code>.
   */
  public boolean validatePatterns(CharSequence value);
}
