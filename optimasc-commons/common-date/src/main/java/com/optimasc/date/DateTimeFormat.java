package com.optimasc.date;

import java.util.Calendar;

/** Class used to represent a date and optional according to a 
 *  reference 'epoch' in the specified time unit.
 *  
 * @author Carl Eric Codere
 *
 */
public class DateTimeFormat
{
  /** Default internal value which is in day from a reference to instance of 0000-01-01 */ 
  public static final DateTimeFormat DEFAULT_DAY = new DateTimeFormat(DateTimeFormat.TimeUnit.DAYS,0);
  /** Default internal value which is in seconds from a reference to instance of 0000-01-01 00:00:00 UTC */ 
  public static final DateTimeFormat DEFAULT_SECONDS = new DateTimeFormat(DateTimeFormat.TimeUnit.SECONDS,0);
  
  /** Represents a time unit, used for durations as well
   *  as internal encoding of date and time values.
   *  
   * @author Carl Eric Codere
   *
   */
  public static class TimeUnit
  {
    /** The scale is in days. */
    public static final int DAYS = Calendar.DAY_OF_MONTH; 
    /** The scale is in seconds. */
    public static final int SECONDS = Calendar.SECOND; 
    /** The scale is in millisecond. */
    public static final int MILLISECONDS = Calendar.MILLISECOND; 

    /** Verifies that the parameter passed is one of the specified 
     *  time unit values.
     * 
     * @param timeUnit [in] The time unit.
     * @throws IllegalArgumentException Thrown if the time unit
     *  value is unknown.
     */
    public static void validate(int timeUnit) throws IllegalArgumentException
    {
      switch (timeUnit)
      {
        case MILLISECONDS:
        case SECONDS:
        case DAYS:
          break;
        default:
          throw new IllegalArgumentException("Unsupported time unit.");
      }
    }
  }
  
  /** The scale in units of this expected date format. */
  protected int timeUnit;
  /** The epoch from 0000-00-00 in UTC in the specified
   *  scale.
   */
  protected long epoch;
  
  /** Create a date-time format with the specified 
   *  unit with a reference 'epoch' based on a referential
   *  of 0000-00-00 in UTC in the specified time units.
   * 
   * @param timeUnit [in] The time units for this reference
   * @param epoch [in] The number of time units since 0000-00-00 UTC.
   */
  public DateTimeFormat(int timeUnit, long epoch)
  {
    super();
    TimeUnit.validate(timeUnit);
    this.timeUnit = timeUnit;
    this.epoch = epoch;
  }
}
