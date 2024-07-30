package com.optimasc.datatypes;

import java.math.BigDecimal;

/** Represents a range restriction facet.
 *  This limits the range of values for the type. It
 *  only applies to <code>ordered</code> values.
 *    
 *  When the bounds are valid and defined
 *  then the type represents a bounded value space and
 *  is {@link #isBounded()} returns <code>true</code>. 
 *  
 *  This is equivalent to the following constraints:
 *  <ul>
 *   <li><code>(minInclusive..maxInclusive)</code> ASN.1 constraint</li>
 *   <li><code>range(minInclusive,maxInclusive)</code> ISO/IEC 11404 subtype</li>
 *   <li><code>minInclusive</code> and <code>maxInclusive</code> XMLSchema constraining facets</li>
 *  </ul>
 *  
 * @author Carl Eric Codère
 */

public interface DecimalRangeFacet extends BoundedFacet
{
  /** Returns the minimum inclusive value allowed for this
   *  ordered value. If this value has not been set,
   *  or if the type has not been configured to be ordered,
   *  the return value will be <code>null</code>.
   * 
   * @return The minimum inclusive value allowed or null.
   */
  public BigDecimal getMinInclusive();
  /** Returns the maximum inclusive value allowed for this
   *  ordered value. If this value has not been set,
   *  or if the type has not been configured to be ordered,
   *  the return value will be <code>null</code>.
   * 
   * @return The maximum inclusive value allowed or null.
   */
  public BigDecimal getMaxInclusive();
  /** Verifies that the specified ordered numeric 
   *  value representation for this type is within
   *  the allowed bounded value space. 
   *  
   * @param value The numeric value to verify
   * @return <code>true</code> if the value
   *  is within range otherwise <code>false</code>.
   */
  public boolean validateRange(long value);
  /** Verifies that the specified ordered numeric 
   *  value representation for this type is within
   *  the allowed bounded value space. This ignores
   *  the value scales when doing the comparison.
   *  
   * @param value The numeric value to verify
   * @return <code>true</code> if the value
   *  is within range otherwise <code>false</code>.
   */
  public boolean validateRange(BigDecimal value);
}
