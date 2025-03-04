package com.optimasc.datatypes;

/** Facet used for date-time and time types.
 * 
 * @author carl
 *
 */
public interface TimeFacet extends TimeUnitFacet
{
  /** Indicates if this type is defined as
   *  having 'local time' or having timezone
   *  information.
   *  
   */
  public boolean isLocalTime();
  
  public void setLocalTime(boolean localTime);  
}
