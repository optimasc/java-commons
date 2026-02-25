package com.optimasc.datatypes.facets;

import com.optimasc.datatypes.Restriction;
import com.optimasc.lang.CharacterSet;

/** Interface to constrain to a specific character set encoding repertoire. 
 **/
public interface CharacterSetEncodingFacet extends Facet
{
  /** Return the character set repertoire. */
  public CharacterSet getCharacterSet();
  /** Set the character set repertoire. */
//  public void setCharacterSet(CharacterSet charset);
  
}
