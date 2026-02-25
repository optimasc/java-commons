package com.optimasc.datatypes;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;

import com.optimasc.datatypes.facets.DateTimeEnumerationFacet;

/** Constraining facet helper for selecting types when the element
 *  types are date-time or time values. All input objects
 *  and output objects are of type <code>Calendar</code>. 
 * 
 * @author Carl Eric Codere
 *
 */
public class DateTimeEnumerationHelper extends EnumerationHelper implements DateTimeEnumerationFacet
{
  public DateTimeEnumerationHelper(Comparator comparator)
  {
    super(Calendar.class,comparator);
  }
  
  public Calendar[] getAllowedValuesAsCalendars()
  {
    return (Calendar[]) getAllowedValues();
  }

  public Calendar getMinInclusive()
  {
    if (sortedEnumeration == null)
      return null;
    return getAllowedValuesAsCalendars()[0];
  }

  public Calendar getMaxInclusive()
  {
    if (sortedEnumeration == null)
      return null;
    Calendar[] choices =getAllowedValuesAsCalendars(); 
    return choices[choices.length-1];
  }

  public boolean isBounded()
  {
    return (getMinInclusive() != null) || (getMaxInclusive() != null);
  }
  
  

}
