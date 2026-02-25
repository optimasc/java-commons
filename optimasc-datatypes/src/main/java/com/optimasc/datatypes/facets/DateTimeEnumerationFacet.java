package com.optimasc.datatypes.facets;

import java.util.Calendar;
import java.util.GregorianCalendar;

import com.optimasc.datatypes.BoundedProperty;

/** Represents a choice between different elements of type
 *  <code>Calendar</code>. This this is equivalent to an enumeration of 
 *  XML Schema containing date-time, date or time specifications.
 *  
 *  <p>The choice of values must be of type <code>Calendar</code> and
 *  the objects returned by this implementation
 *  are of type <code>Calendar</code> </p>
 * 
 * This is equivalent to the following constraints:
 * <ul>
 * <li><code>SingleValue</code> ASN.1 subtype</li>
 * <li><code>selecting</code> ISO/IEC 11404 subtype</li>
 * <li><code>enumeration</code> XMLSchema constraint</li>
 * </ul>
 */  
public interface DateTimeEnumerationFacet extends EnumerationFacet,BoundedProperty
{
  /** Returns the choices allowed for this choice type. If no choices
   *  have been specified, the value returned is <code>null</code>.
   *  
   *  The array runtime type depends on the enumerated type, but
   *  it will be  correctly returned so it can be correctly typecast.
   *  
   **/
  public Calendar[] getAllowedValuesAsCalendars();

  
  /** Returns the lower bound of the value choices.
   * 
   * @return The lowest bound value in the choices.
   */
  public Calendar getMinInclusive();
  /** Returns the upper bound of the value choices.
   * 
   * @return The upper bound value in the choices.
   */
  public Calendar getMaxInclusive();
  
}
