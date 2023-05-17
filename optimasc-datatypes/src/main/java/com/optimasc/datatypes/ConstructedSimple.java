package com.optimasc.datatypes;

/** Certain datatypes are elements of  another Datatype, they then implement
 *  this interface. Examples of such are strings that are composed of
 *  characters or arrays. 
 *   
 * @author Carl Eric Codere
 *
 */
public interface ConstructedSimple
{
  public Type getBaseType();
  public void setBaseType(Type value);
}
