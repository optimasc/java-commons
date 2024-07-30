package com.optimasc.datatypes;

public interface IntegerEnumerationFacet
{
  /** Returns the choices allowed for this choice type. If no choices
   *  have been specified, the value returned is <code>null</code>. */
  public long[] getChoices();
  /** Validates if the value is within the specified choices. If 
   *  no allowed choices have been defined, this method always
   *  returns <code>true</code>. */
  public boolean validateChoice(long value);
}
