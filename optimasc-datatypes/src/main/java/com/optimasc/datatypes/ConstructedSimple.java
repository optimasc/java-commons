package com.optimasc.datatypes;

import omg.org.astm.type.TypeReference;

/** Interface for data types that are elements of another data type. 
 *  Examples of such are strings that are composed of
 *  characters. 
 *   
 * @author Carl Eric Codere
 *
 */
public interface ConstructedSimple
{
  public TypeReference getBaseTypeReference();
}
