package com.optimasc.datatypes;

/** Represents a choice between different elements, this is equivalent to an
 *  enumeration, where only one element is allowed. */
public interface EnumerationFacet
{
  /** Returns the choices allowed for this choice type. */
  public Object[] getChoices();
  
  /** Sets and replaces the current choice types. */
  public void setChoices(Object[] choices);
  
  /** Validates if the value is within the specified choices. */
  public boolean validateChoice(Object value);

}
