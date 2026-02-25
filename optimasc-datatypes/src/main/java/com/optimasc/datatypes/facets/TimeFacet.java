package com.optimasc.datatypes.facets;


/** Facet used for date-time and time types providing
 *  precisions on how to interpret the date-time and
 *  time values. 
 * 
 * @author Carl Eric Codere
 *
 */
public interface TimeFacet extends TimeUnitFacet
{
  /** Indicates if this type is defined as
   *  having 'local time' or having timezone
   *  information.
   *  
   *  @return <code>true</code> if the
   *   time component represents a local
   *   specific time or <code>false</code>
   *   if the time component represents
   *   a time with timezone information. 
   *  
   */
  public boolean isLocalTime();
  
  /** Sets to indicate if time component
   *  uses local time or time with a timezone
   *  indicator.
   * 
   * @param [in] localTime 
   */
  public void setLocalTime(boolean localTime);  
}
