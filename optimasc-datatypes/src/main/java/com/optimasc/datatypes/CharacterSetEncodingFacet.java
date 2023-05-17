package com.optimasc.datatypes;

/** Represents a character set encoding repertoire. */
public interface CharacterSetEncodingFacet
{
  /** Return the character set encoding. */
  public String getCharSetName();
  /** Sets the character set encoding. */
  public void setCharSetName(String charSetName);
}
