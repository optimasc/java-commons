package com.optimasc.datatypes.facets;


/** Represents a length restriction facet.
 *  This limits the number of elements allowed for this
 *  type.  
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
 * @author Carl Eric Codere
 */
public interface LengthFacet extends Facet
{
  /** Unbounded value */
  public static long UNBOUNDED = Long.MIN_VALUE; 
  
  /** Returns the minimum allowed elements. The default
   *  value for minimum number of allowed elements is zero.
   */
  public long getMinLength();
  /** Returns the maximum allowed elements. If the value
   *  returned is {@link #UNBOUNDED} then the maximum length 
   *  is not defined.
   */
  public long getMaxLength();
  
  /** Sets the minimum and maximum number of allowed elements.
   * 
   * @param minLength [in] The minimum length, must be
   *   a non negative integer.
   * @param maxLength [in] The maximum length or 
   * {@link #UNBOUNDED} when the maximum length 
   *  is not defined.
   * @throws IllegalArgumentException if minLength is less
   *   than zero, or minLength is greater than maxLength.  
   * 
   */
  public void setLength(long minLength, long maxLength);
  
}
