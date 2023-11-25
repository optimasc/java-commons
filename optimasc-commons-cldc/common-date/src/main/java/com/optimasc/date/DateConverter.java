package com.optimasc.date;

import java.util.Calendar;

/** Abstract class used to convert a <code>datetime</code>
 *  or <code>date</code> represented as a {@link java.util.Calendar}
 *  to an encoded numeric value. 
 *  
 *  <p>The numeric value is represented
 *  as a numeric value represented a long primitive type, with the
 *  lower significant bits of the primitive type containing the
 *  encoded value.</p>
 *  
 *  <p>The actual supported time precision is given by the method
 *  {@link #getPrecision()} and the minimum number of bits required
 *  to represent the encoded value in the <code>long</code> is
 *  given by {@link #getMinBits()}. For example, if the encoded
 *  value fits minimally in 16-bits, as defined in the 
 *  original specification, then {@link DateConverter#getMinBits()} 
 *  will return 16, and only the lower 16-bits of the value could be used if 
 *  official encoded precision wants to be used, hence value could
 *  be truncated to that precision for saving to a stream. Of course, 
 *  if expanded range is required compared to officially documented encoding, 
 *  more bits could be used by the encoding system. </p>
 *  
 *  
 *   */
public abstract class DateConverter
{
  /** From a calendar instance, return a derived class
   *  specific numeric value representation of the calendar. Each
   *  date format converter will interpret the value
   *  differently.
   *
   * @param cal [in] The calendar that will be converted.
   * @return The numeric associated with this calendar
   */
  public abstract long encode(final Calendar cal);
  /** From a class specific representation of a data format, convert
   *  the value to a <code>Calendar</code> representation.
   *
   * @param datetime [in] Representation of calendar value
   * @return The calendar representation of the <code>datetime</code>
   */
  public abstract Calendar decode(final long datetime);
  
  /** Return the precision supported by this encoded value. Possible
   *  values from lowest precision to highest precision are
   *  as follows:
   *
   *  <dl>
   *   <dt>{@link java.util.Calendar#YEAR}</dt>
   *   <dd>The long value represents years (YYYYY) </dd>
   *   <dt>{@link java.util.Calendar#MONTH}</dt>
   *   <dd>The long value can represent a year and a month (YYYY-MM) </dd>
   *   <dt>{@link java.util.Calendar#DAY_OF_MONTH}</dt>
   *   <dd>The long value can represent a full date (YYYY-MM-DD)</dd>
   *   <dt>{@link java.util.Calendar#SECOND}</dt>
   *   <dd>The long value can represent a full date and time up to a precision
   *    of seconds (YYYY-MM-DDThh:mm:ss).</dd>
   *   <dt>{@link java.util.Calendar#MILLISECOND}</dt>
   *   <dd>The long value can represent a full date and time up to a precision
   *    of seconds (YYYY-MM-DDThh:mm:Sss.sss).</dd>
   *  </dl>
   *
   */
  public abstract int getPrecision();
  
  /** Returns the minimum number of bits required
   *  to encode the <code>datetime</code> or 
   *  <code>date</code>.
   * 
   * @return
   */
  public abstract int getMinBits();

}
