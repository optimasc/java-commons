package com.optimasc.datatypes;

import java.math.BigDecimal;

import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;

/** For ordered values represents additional
 *  operations that can be done to determine
 *  if the value is within defined constraints.
 * 
 * @author Carl Eric Codere
 *
 */
public interface OrderedFacet extends Convertable, NumberRangeFacet
{
   /** Tries to convert this valid ordered value 
    *  to its natural java type representation. 
    *  A valid value is those that meets the 
    *  <code>Select</code> subtype of ISO/IEC 11404:2007, 
    *  the <code>Range</code> subtype of ISO/IEC 11404:2007. 
    *  It is equivalent of the  <code>minInclusive</code>, 
    *  <code>maxInclusive</code> and <code>enumeration</code> 
    *  XMLSchema 2004 constraining facets.  
    * 
    * @param ordinalValue [in] The value to verify
    *   and convert to its natural representation for this
    *   datatype.
    * @param conversionResult [in,out] In the case the
    *   return value is <code>null</code> the {@link TypeCheckResult#error}
    *   contains information on the error. In the case that
    *   there is a loss of precision of the target datatype, the 
    *   {@link TypeCheckResult#narrowingConversion} field will be true, 
    *   but the conversion will still succeed.
    * @return <code>null</code> if the ordinal value
    *   does not meet one of the constraining facets
    *   of this datatype.
    */
   public Object toValue(long ordinalValue, TypeCheckResult conversionResult);
   
}
