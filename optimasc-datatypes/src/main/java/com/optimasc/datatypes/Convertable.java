package com.optimasc.datatypes;

import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;

/** Interface for data types that can convert data to their java language
 *  representation.
 *
 */
public interface Convertable
{
  /** Tries to convert this valid value to its natural java type 
   *  representation. 
   *  A valid value is those that meets the 
   *  <code>Select</code> subtype of ISO/IEC 11404:2007, 
   *  the <code>Range</code> subtype of ISO/IEC 11404:2007.
   *  It is equivalent of the  <code>minInclusive</code>, 
   *  <code>maxInclusive</code>m <code>enumeration</code> and 
   *  <code>pattern</code>(if applicable) XMLSchema 2004 
   *  constraining facets.  
   * 
   * @param value [in] The value to verify
   *   and convert to its natural representation for this
   *   datatype.
   * @param conversionResult [in,out] In the case the
   *   return value is <code>null</code> the {@link TypeCheckResult#error}
   *   contains information on the error. In the case that
   *   there is a loss of precision of the target datatype, the 
   *   {@link TypeCheckResult#narrowingConversion} field will be true, 
   *   {@link TypeCheckResult#error} will be equal to 
   *   {@link DatatypeException#ERROR_DATA_NUMERIC_OUT_OF_RANGE}
   *   but the conversion will still succeed.
   * @return <code>null</code> if the value cannot be 
   *   converted to this datatype natural format.
   */
  public abstract Object toValue(java.lang.Object value, TypeCheckResult conversionResult);

}
