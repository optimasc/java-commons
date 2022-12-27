package com.optimasc.datatypes.parser;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;

public interface DatatypeParser
{
  /** Parses a value represented as a string and converts it into the specified
   *  correct value representation.  
   * 
   * @param value The string representation of the datatype to parse.
   * @param dataType The associated datatype with this value.
   * @return The object containing the value.
   * @throws IllegalArgumentException If the validation failed.
   * @throws DatatypeException
   */
  public abstract Object parse(java.lang.String value, Datatype datatype) throws IllegalArgumentException, ParseException, DatatypeException;

  /** Converts a specific value type to a String representation
   * 
   * @param value The value to convert to a string.
   * @return The value converted to a string.
   * @throws IllegalArgumentException
   * @throws DatatypeException
   */
  public abstract java.lang.String toString(Object value) throws IllegalArgumentException, DatatypeException;
  
}
