package com.optimasc.datatypes;

/** Certain datatypes are elements of 
 *  another Datatype, they then implement
 *  this interface.
 *   
 * @author Carl Eric Codere
 *
 */
public interface ConstructedSimple
{
  public Datatype getElementType();
  public void setElementType(Datatype value);
}
