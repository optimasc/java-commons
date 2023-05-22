package com.optimasc.datatypes;

/** Represents a regular expression pattern
 *  facet to parse the value representation of 
 *  this datatype.
 * 
 * @author Carl Eric Codere
 *
 */
public interface PatternFacet
{
  public String getPattern();
  public void setPattern(String value);
}
