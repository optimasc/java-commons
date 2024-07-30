package com.optimasc.datatypes;

import omg.org.astm.type.TypeReference;

/** Certain datatypes are elements of another Datatype, they then implement
 *  this interface. Examples of such are strings that are composed of
 *  characters. 
 *   
 * @author Carl Eric Codere
 *
 */
public interface ConstructedSimple
{
  public TypeReference getBaseTypeReference();
}
