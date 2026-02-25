package com.optimasc.datatypes.facets;

import com.optimasc.datatypes.BoundedProperty;
import com.optimasc.lang.NumberSelectItem;

/** Represents a choice between different elements of type
 *  <code>Number</code>. This this is equivalent to an enumeration of 
 *  XML Schema containing numbers.
 *  
 *  <p>The choice of values must be of type <code>Number</code> and
 *  the objects returned by this implementation
 *  are of type <code>Number</code> </p>
 *  
 *  <p>Each number of the selection items should be of the same scale (number 
 *  of digits right of decimal) as the other select values, otherwise
 *  an <code>IllegalArgumentException</code> will be thrown.</p>
 *  
 * 
 * This is equivalent to the following constraints:
 * <ul>
 * <li><code>SingleValue</code> and <code>ValueRange</code> ASN.1 subtypes</li>
 * <li><code>selecting</code> ISO/IEC 11404 subtype</li>
 * <li><code>enumeration</code> XMLSchema constraint</li>
 * </ul>
 */  
public interface NumberEnumerationFacet extends EnumerationFacet,BoundedProperty
{
  /** Validates if the value is within allowed values. If 
   *  no allowed choices have been defined, this method always
   *  returns <code>true</code>. */
  public boolean isValid(long value);

  /** Sets the allowed values as selection items,
   *  which permits to define range of values. Each
   *  number should be of the same scale (number 
   *  of digits right of decimal) as the others.
   * 
   * @param values [in] The allowed choices of values as
   *   selection items.
   */
  public void setAllowedValuesAsSelectItems(NumberSelectItem[] values);  
  
  /** Returns the minimum inclusive value allowed for this
   *  ordered value. If this value has not been set,
   *  or if the type has not been configured to be ordered,
   *  the return value will be <code>null</code>. In the
   *  case where the choices of valid values are not continuous
   *  ranges, it will return the lowest number value. 
   * 
   * @return The minimum inclusive value allowed or null.
   */
  public Number getMinInclusive();
  /** Returns the maximum inclusive value allowed for this
   *  ordered value. If this value has not been set,
   *  or if the type has not been configured to be ordered,
   *  the return value will be <code>null</code>.
   *  
   *  In the case where the choices of valid values are not continuous
   *  ranges, it will return the highest number value.    
   * 
   * @return The maximum inclusive value allowed or null.
   */
  public Number getMaxInclusive();
  
  /** Returns the choices allowed for this choice type. If no choices
   *  have been specified, the value returned is <code>null</code>.
   *  
   *  The array runtime will contain objects of type <code>NumberSelectItem</code>.
   *  
   **/
  public NumberSelectItem[] getAllowedValuesAsSelectItems();
  
  /** Sets the discrete allowed values as a long array. 
   *  
   *  @param values [in] The allowed choices of values as <code>long</code>.
   *  
   **/
  public void setAllowedValues(long[] values);
  
  
}
