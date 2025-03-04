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
 *   <li><code>X-MIN-VALUE-LENGTH</code> and <code>X-MAX-VALUE-LENGTH</code> LDAP Attribute definition extension syntax constraining facet</li>
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
  
  /** Sets the minimum and maximum number of allowed elements.
   * 
   * @param minLength [in] The minimum length, must be
   *   a non negative integer.
   * @param maxLength [in] The maximum length or 
   * {@link Integer#MIN_VALUE} when the maximum length 
   *  is not defined.
   * @throws IllegalArgumentException if minLength is less
   *   than zero, or minLength is greater than maxLength.  
   * 
   */
  public void setLength(int minLength, int maxLength);
  
}
