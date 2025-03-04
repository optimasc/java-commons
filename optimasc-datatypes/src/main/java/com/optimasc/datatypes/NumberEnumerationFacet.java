package com.optimasc.datatypes;

import java.math.BigDecimal;

public interface NumberEnumerationFacet
{
  /** Returns the choices allowed for this choice type. If no choices
   *  have been specified, the value returned is <code>null</code>. */
  public Number[] getChoices();
  
  /** Set the choices allowed for this choice type.  */
  public void setChoices(Number[] choices);
  
  /** Validates if the value is within the specified choices. If 
   *  no allowed choices have been defined, this method always
   *  returns <code>true</code>. */
  public boolean validateChoice(Number value);
  
  /** Validates if the value is within the specified choices. If 
   *  no allowed choices have been defined, this method always
   *  returns <code>true</code>. */
  public boolean validateChoice(long value);  
}
