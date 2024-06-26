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

public interface DecimalRangeFacet extends BoundedFacet, Restriction, SubSet
{
  public BigDecimal getMinInclusive();
  public BigDecimal getMaxInclusive();
  public boolean validateRange(long value);
  public boolean validateRange(BigDecimal value);
}
