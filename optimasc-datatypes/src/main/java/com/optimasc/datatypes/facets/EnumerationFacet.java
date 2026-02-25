package com.optimasc.datatypes.facets;


/** Represents an enumeration of different allowed
 *  values. 
 * 
 * This is equivalent to the following constraints:
 * <ul>
 * <li><code>SingleValue</code> ASN.1 subtype</li>
 * <li><code>selecting</code> ISO/IEC 11404 subtype</li>
 * <li><code>enumeration</code> XMLSchema constraint</li>
 * <li><code>X-ALLOWED-VALUE</code> LDAP Attribute definition extension syntax constraining facet</li>
 * 
 * </ul>
 */  
public interface EnumerationFacet extends Facet
{
  /** Returns the choices allowed for this choice type. If no choices
   *  have been specified, the value returned is <code>null</code>.
   *  
   *  The array runtime type depends on the enumerated type, but
   *  it will be  correctly returned so it can be correctly typecast
   *  to the class type returned by {@link #getAllowedValuesClass()}.
   *  
   **/
  public Object[] getAllowedValues();

  /** Sets the specific allowed values. The choices
   *  parameter should be a class compatible with
   *  the class returned by {@link #getAllowedValuesClass()}
   * 
   * @param choices [in] The possibe allowed choice
   *   allowed values, must be of the correct type
   *   depending on the enumeration type.
   */
  public void setAllowedValues(Object[] choices);
  
  
  /** Validates if the value is within the allowed values. If 
   *  no allowed choices have been defined, this method always
   *  returns <code>true</code>. 
   *  
   *  @throws IllegalArgumentException Thrown if the 
   *   object class is not the one required for this datatype. */
  public boolean isValid(Object value);
  
  /** Returns the class of the objects associated
   *  with the allowed values using the object parameter
   *  version generic API's.
   */
   public Class getAllowedValuesClass();

}
