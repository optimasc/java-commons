package com.optimasc.datatypes;

/** Represents a regular expression pattern
 *  facet to limit the value space of a
 *  string type. 
 * 
 * @author Carl Eric Codere
 *
 */
public interface PatternFacet
{
  public String getPattern();
  public void setPattern(String value);
}
