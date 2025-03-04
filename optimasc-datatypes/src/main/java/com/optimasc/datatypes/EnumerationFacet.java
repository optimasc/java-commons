package com.optimasc.datatypes;

/** Represents a choice between different elements. This
 *  this is equivalent to an enumeration, where only one 
 *  element is allowed. 
 *  
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
public interface EnumerationFacet
{
  /** Returns the choices allowed for this choice type. If no choices
   *  have been specified, the value returned is <code>null</code>. */  
  public Object[] getChoices();
  
  public void setChoices(Object[] choices);
  
  
  /** Validates if the value is within the specified choices. If 
   *  no allowed choices have been defined, this method always
   *  returns <code>true</code>. 
   *  
   *  @throws IllegalArgumentException Thrown if the 
   *   object class is not the one required for this datatype. */
  public boolean validateChoice(Object value);

}
