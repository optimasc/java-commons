package com.optimasc.datatypes;

import java.util.Calendar;
import java.util.GregorianCalendar;

/** Represents a choice between different elements of type
 *  Calendar. This this is equivalent to an enumeration of XML
 *  Schema.. 
 *  
 * 
 * This is equivalent to the following constraints:
 * <ul>
 * <li><code>SingleValue</code> ASN.1 subtype</li>
 * <li><code>selecting</code> ISO/IEC 11404 subtype</li>
 * <li><code>enumeration</code> XMLSchema constraint</li>
 * </ul>
 */  
public interface DateTimeEnumerationFacet
{
  /** Returns the choices allowed for this choice type. If no choices
   *  have been specified, the value returned is <code>null</code>. */  
  public Calendar[] getChoices();
  
  /** Validates if the value is within the specified choices. If 
   *  no allowed choices have been defined, this method always
   *  returns <code>true</code>. 
   *  
   *  @throws IllegalArgumentException Thrown if the 
   *   object class is not the one required for this datatype. */
  public boolean validateChoice(Calendar value);
}
