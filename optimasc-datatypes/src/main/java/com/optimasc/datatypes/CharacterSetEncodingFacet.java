package com.optimasc.datatypes;

import com.optimasc.lang.CharacterSet;

/** Represents a character set encoding repertoire. */
public interface CharacterSetEncodingFacet extends Restriction
{
  /** Return the character set repertoire. */
  public CharacterSet getCharacterSet();
}
