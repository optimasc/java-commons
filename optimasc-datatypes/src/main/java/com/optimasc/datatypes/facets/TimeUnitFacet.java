package com.optimasc.datatypes.facets;


/** Represents the unit of the numerical value of a time / timeinterval
 *  representation.
 *  
 * @author Carl Eric Codere
 *
 */
public interface TimeUnitFacet extends Facet
{
  public void setAccuracy(int accuracy);
  /** Returns the time unit accuracy of
   *  this time type.
   */
  public int getAccuracy();
}
