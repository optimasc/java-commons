package com.optimasc.datatypes;

import com.optimasc.lang.CharacterSet;

/** Interface for data types that represent a specific character set encoding repertoire. 
 **/
public interface CharacterSetEncodingFacet extends Restriction
{
  /** Return the character set repertoire. */
  public CharacterSet getCharacterSet();
  /** Set the character set repertoire. */
//  public void setCharacterSet(CharacterSet charset);
  
}
