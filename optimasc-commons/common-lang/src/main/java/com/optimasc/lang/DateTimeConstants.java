package com.optimasc.lang;

public interface DateTimeConstants
{
  /**
   * Value for first month of year.
   */
  public static final int JANUARY  = 1;

  /**
   * Value for second month of year.
   */
  public static final int FEBRUARY = 2;

  /**
   * Value for third month of year.
   */
  public static final int MARCH    = 3;

  /**
   * Value for fourth month of year.
   */
  public static final int APRIL    = 4;

  /**
   * Value for fifth month of year.
   */
  public static final int MAY      = 5;

  /**
   * Value for sixth month of year.
   */
  public static final int JUNE     = 6;

  /**
   * Value for seventh month of year.
   */
  public static final int JULY     = 7;

  /**
   * Value for eighth month of year.
   */
  public static final int AUGUST   = 8;

  /**
   * Value for ninth month of year.
   */
  public static final int SEPTEMBER = 9;

  /**
   * Value for tenth month of year.
   */
  public static final int OCTOBER = 10;

  /**
   * Value for eleven month of year.
   */
  public static final int NOVEMBER = 11;

  /**
   * Value for twelve month of year.
   */
  public static final int DECEMBER = 12;

  /**
   * <p>Comparison result.</p>
   */
  public static final int LESSER = -1;

  /**
   * <p>Comparison result.</p>
   */
  public static final int EQUAL =  0;

  /**
   * <p>Comparison result.</p>
   */
  public static final int GREATER =  1;

  /**
   * <p>Comparison result.</p>
   */
  public static final int INDETERMINATE =  2;
  
  /**
   * Designation that an "int" field is not set.
   */
  public static final int FIELD_UNDEFINED = Integer.MIN_VALUE;
  
  /**
  * <p>A constant that represents the years field.</p>
  */
 public static final int YEARS = 0;
 
 /**
  * <p>A constant that represents the months field.</p>
  */
 public static final int MONTHS = 1;
 
 /**
  * <p>A constant that represents the days field.</p>
  */
 public static final int DAYS = 2;
 
 /**
  * <p>A constant that represents the hours field.</p>
  */
 public static final int HOURS = 3;
 
 /**
  * <p>A constant that represents the minutes field.</p>
  */
 public static final int MINUTES = 4;
 
 /**
  * <p>A constant that represents the seconds field.</p>
  */
 public static final int SECONDS = 5;
 

 /** Minimum timezone value in minutes. */
 public static final int MIN_TIMEZONE_OFFSET = -14 * 60;
 /** Maximum timezone value in minutes. */
 public static final int MAX_TIMEZONE_OFFSET = 14 * 60;

}
