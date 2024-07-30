package com.optimasc.datatypes;

/** Represents the unit of the numerical value of a time / timeinterval
 *  representation.
 *  
 * @author Carl Eric Codere
 *
 */
public interface TimeUnitFacet
{
  /** Returns the time unit accuracy of
   *  this time type.
   */
  public int getAccuracy();
}
