package com.optimasc.date;

import java.util.Calendar;

/**
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
 */
public abstract class DateEncoder
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
  
  /** Converts the value to a Date that contains 
   *  the year, month and day in the Gregorian
   *  proleptic calendar. There may be loss
   *  of precision since the value returned
   *  contains only year, month and day fields.
   * 
   * @param value [in] The internal value to decode.
   * @return The date format.
   */
  public abstract DateTime decode(long value);
  
  /** Converts a year, month, day value to 
   *  an encoded values. 
   * 
   * @param year [in] The year number, either positive
   *   indicate CE or negative or zero for BCE.
   * @param month [in] The month number, from 1 to 12.
   * @param day [in] The day number from 1, to 31
   * @return The encoded value
   * @throws IllegalArgumentException If one of the
   *   input parameters is invalid.
   * @throws IllegalArgumentException If the year 
   *   is not within the supported range.
   */
  public abstract long encode(DateTime value);
  
  /** Returns the minimum year range supported
   *  by this encoded value.  
   */
  public abstract int getMinimumYear();

  /** Returns the maximum year range supported
   *  by this encoded value.  
   */
  public abstract int getMaximumYear();

}
