package com.optimasc.datatypes;

/** Represents a length restriction facet.
 *  This limits the number of elements allowed for this
 *  type.  When the bounds are valid and defined
 *  then the type represents a bounded value space and
 *  is {@link #isBounded()} returns <code>true</code>. 
 *  
 *  <p>The minimum length value is zero. </p>
 *  
 *  This is equivalent to the following constraints:
 *  <ul>
 *   <li><code>SIZE(minLength,maxLength)</code> ASN.1 constraint</li>
 *   <li><code>size(minLength,maxLength)</code> ISO/IEC 11404 subtype</li>
 *   <li><code>minLength</code> and <code>maxLength</code> XMLSchema constraining facets</li>
 *  </ul>
 *  
 * @author Carl Eric Codère
 */
public interface LengthFacet
{
  /** Returns the minimum allowed elements. The default
   *  value for minimum number of allowed elements is zero.
   */
  public int getMinLength();
  /** Returns the maximum allowed elements. If the value
   *  returned is {@link Integer#MIN_VALUE} then the maximum length 
   *  is not defined.
   */
  public int getMaxLength();
}
