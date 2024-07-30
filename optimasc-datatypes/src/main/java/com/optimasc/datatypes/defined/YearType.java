package com.optimasc.datatypes.defined;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.date.DateTime;

/** The date type represents a year in the proleptic Gregorian Calender.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>time(year, 10, 0)</codE> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>gYear</code> XMLSchema built-in datatype</li>
 *  </ul>
 *  
 * @author Carl Eric Codere
 */
public class YearType extends DateTimeType
{
  public static final YearType DEFAULT_INSTANCE = new YearType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public YearType()
  {
    super(DateTime.TimeAccuracy.YEAR,false);
  }

}
