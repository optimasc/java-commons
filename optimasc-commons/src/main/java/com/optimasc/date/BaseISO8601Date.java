/**
 * Copyright (c) 2009 Optima SC Inc. All Rights Reserved.
 *
 * File Version: $Revision$
 *
 * -----------------------------------------------------------------------------
 * THIS SOFTWARE IS NOT DESIGNED OR INTENDED FOR USE OR RESALE AS ON-LINE
 * CONTROL EQUIPMENT IN HAZARDOUS ENVIRONMENTS REQUIRING FAIL-SAFE
 * PERFORMANCE, SUCH AS IN THE OPERATION OF NUCLEAR FACILITIES, AIRCRAFT
 * NAVIGATION OR COMMUNICATION SYSTEMS, AIR TRAFFIC CONTROL, DIRECT LIFE
 * SUPPORT MACHINES, OR WEAPONS SYSTEMS, IN WHICH THE FAILURE OF THE
 * SOFTWARE COULD LEAD DIRECTLY TO DEATH, PERSONAL INJURY, OR SEVERE
 * PHYSICAL OR ENVIRONMENTAL DAMAGE ("HIGH RISK ACTIVITIES"). GEMALTO
 * SPECIFICALLY DISCLAIMS ANY EXPRESS OR IMPLIED WARRANTY OF FITNESS FOR
 * HIGH RISK ACTIVITIES.
 */
package com.optimasc.date;

import com.optimasc.text.PrintfFormat;
import com.optimasc.utils.StringTokenizer;

import java.util.*;

/**
 * This class represents a small ISO 8601 parser and generator that is used to
 * parse complete Calendar objects with its default values. It can also generate
 * partial date-time strings.
 * 
 */
public class BaseISO8601Date
{

  protected static final int MINUTE_MINIMUM = 0;
  protected static final int MINUTE_MAXIMUM = 59;
  protected static final int SECOND_MINIMUM = 0;
  protected static final int SECOND_MAXIMUM = 59;
  protected static final int HOUR_MINIMUM = 0;
  protected static final int HOUR_MAXIMUM = 24;
  protected static final int DAY_OF_MONTH_MINIMUM = 01;
  protected static final int DAY_OF_MONTH_MAXIMUM = 31;
  // -14 hours converted to milliseconds.
  protected static final int TIMEZONE_OFFSET_MIN = -50400000;
  // +14 hours converted to milliseconds.
  protected static final int TIMEZONE_OFFSET_MAX = +50400000;

  /**
   * Formats a <code>Calendar</code> value into an ISO8601-compliant date/time
   * string.
   * 
   * Checks if given token is present in tokenizer.
   * 
   * @param st
   *          tokenizer object.
   * @param token
   *          token to check.
   * @return true if given token is available, false otherwise.
   */
  private static boolean check(StringTokenizer st, String token)
  {
    try
    {
      return st.nextToken().equals(token);
    } catch (NoSuchElementException ex)
    {
      return false;
    }
  }
  
  
  public static Calendar parseDate(Calendar calendar, String dateString)
      throws IllegalArgumentException
  {
    String s;
    int tzoffset;
    String cal = dateString;
    // YYYY-MM-DDThh:mm:ss.sTZD
    StringTokenizer st = new StringTokenizer(cal, "-T:.+Z", true);

    // Possible formats are as follows:
    // For year:
    // +YYYYY | -YYYYY | YYYY | -YYYY -> gYear
    // For Year-Month:
    // gYear-MM -> gYearMonth
    // For Year-Month-Day
    // gYearMonth-dd -> date
    // '-'? yyyy '-' mm '-' dd 'T' hh ':' mm ':' ss ('.' s+)? (zzzzzz)?
      // Year
      if (st.hasMoreTokens())
      {
        s = st.nextToken();
        // Remove the leading + character if present so
        // it conforms to XML Schema
        if (s.equals("+") == true)
        {
          s = st.nextToken();
          // The length of the next token must be 5 digits
          // as stated in ISO 8601.
          if (s.length() != 5)
          {
            throw new IllegalArgumentException(
                "Extended year must be composed of 5 digits");
          }
        } else
        // Add the - and following digits
        if (s.equals("-") == true)
        {
          s = s + st.nextToken();
          // The length of the next token must be 4 OR 5 digits
          // as stated in ISO 8601 and W3C XML Schema
          if (((s.length() == 5) || (s.length() == 6)) == false)
          {
            throw new IllegalArgumentException(
                "Extended year must be composed of 4 or 5 digits");
          }
        } else if (s.length() != 4)
        {
          throw new IllegalArgumentException(
              "Year must be composed of 4 digits");
        }

        int year = Integer.parseInt(s);
        if ((year < -99999) || (year > 99999))
        {
          throw new IllegalArgumentException(
              "Year is not within a valid range.");
        }

        calendar.set(Calendar.YEAR, year);
      } else
      {
        return calendar;
      }
      // Month
      if (check(st, "-"))
      {
        // We end with the Separator character
        // which is not a valid date format
        if (st.hasMoreTokens() == false)
        {
          throw new IllegalArgumentException("Month must be coded on 2 digits.");
        }
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException("Month must be coded on 2 digits.");
        }

        int month = Integer.parseInt(s) - 1;
        if ((month < 0) || (month > 11))
        {
          throw new IllegalArgumentException(
              "Month values must be within 01 and 12.");
        }
        calendar.set(Calendar.MONTH, month);
      } else
      {
        return calendar;
      }

      // Day
      if (check(st, "-"))
      {
        // We end with the Separator character
        // which is not a valid date format
        if (st.hasMoreTokens() == false)
        {
          throw new IllegalArgumentException(
              "Day values must be encoded on 2 digits.");
        }
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException(
              "Day values must be encoded on 2 digits.");
        }

        int day = Integer.parseInt(s);
        if ((day < DAY_OF_MONTH_MINIMUM) || (day > DAY_OF_MONTH_MAXIMUM))
        {
          throw new IllegalArgumentException(
              "Day values must be within 01 and 31.");
        }

        calendar.set(Calendar.DAY_OF_MONTH, day);
      } else
      {
        return calendar;
      }
      return calendar;
  }
  

  public static Calendar parseTime(Calendar calendar, String timeString)
      throws IllegalArgumentException
  {
    String s;
    int tzoffset;
    String cal = timeString;
    // hh:mm:ss.sTZD
    StringTokenizer st = new StringTokenizer(cal, "-:.+Z", true);

    try
    {
      /******************* Time separator ******************/
      s = st.nextToken();
      if (s.length() != 2)
      {
        throw new IllegalArgumentException("Hours must be coded on 2 digits.");
      }
      int hour = Integer.parseInt(s);
      // 24:00 is also allowed according to ISO 8601:2004
      // and will be represented by the next day in the Java calendar object
      if ((hour < HOUR_MINIMUM) || (hour > HOUR_MAXIMUM))
      {
        throw new IllegalArgumentException("Hours must be between on "
            + HOUR_MINIMUM + " and " + HOUR_MAXIMUM);
      }
      calendar.set(Calendar.HOUR_OF_DAY, hour);

      // Minutes IS NOT optional
      if (check(st, ":") && (st.hasMoreTokens()))
      {
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException(
              "Minutes must be coded on 2 digits.");
        }
        int minutes = Integer.parseInt(s);
        if ((minutes < MINUTE_MINIMUM) || (minutes > MINUTE_MAXIMUM))
        {
          throw new IllegalArgumentException("Minutes must be between "
              + MINUTE_MINIMUM + " and " + MINUTE_MAXIMUM);
        }
        calendar.set(Calendar.MINUTE, minutes);
      } else
      {
        throw new IllegalArgumentException("Invalid minutes field.");
      }

      //
      // Not mandatory now
      //

      // Secondes
      if (!st.hasMoreTokens())
      {
        return calendar;
      }
      String tok = st.nextToken();
      if (tok.equals(":"))
      { // secondes
        if (st.hasMoreTokens())
        {
          s = st.nextToken();
          if (s.length() != 2)
          {
            throw new IllegalArgumentException(
                "Seconds should be coded on 2 digits.");
          }
          int secondes = Integer.parseInt(s);
          if ((secondes < SECOND_MINIMUM) || (secondes > SECOND_MAXIMUM))
          {
            throw new IllegalArgumentException(
                "Seconds should be coded on 2 digits.");
          }
          calendar.set(Calendar.SECOND, secondes);
          if (!st.hasMoreTokens())
          {
            return calendar;
          }

          // frac sec
          tok = st.nextToken();
          if (tok.equals("."))
          {
            // bug fixed, thx to Martin Bottcher
            String nt = st.nextToken();
            while (nt.length() < 3)
            {
              nt += "0";
            }
            nt = nt.substring(0, 3); // Cut trailing chars..
            int millisec = Integer.parseInt(nt);
            // int millisec = Integer.parseInt(st.nextToken()) * 10;
            calendar.set(Calendar.MILLISECOND, millisec);
            if (!st.hasMoreTokens())
            {
              return calendar;
            }
            tok = st.nextToken();
          } else
          {
            // calendar.set(Calendar.MILLISECOND, 0);
          }
        } else
        {
          throw new IllegalArgumentException("Seconds should be specified.");
        }
      } else
      {
        // calendar.set(Calendar.SECOND, 0);
        // calendar.set(Calendar.MILLISECOND, 0);
      }

      // Timezone
      if (!tok.equals("Z")) // UTC
      {
        if (!(tok.equals("+") || tok.equals("-")))
        {
          throw new IllegalArgumentException("only Z, + or - allowed");
        }
        boolean plus = tok.equals("+");
        if (!st.hasMoreTokens())
        {
          throw new IllegalArgumentException("Missing timezone hour field.");
        }
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException(
              "Timezone hour field should be coded on 2 digits.");
        }
        int tzhour = Integer.parseInt(s);
        if ((tzhour < 0) || (tzhour > 14))
        {
          throw new IllegalArgumentException(
              "Timezone hours field must be between 00 and 14.");
        }

        int tzmin = 0;
        if (check(st, ":") && (st.hasMoreTokens()))
        {
          s = st.nextToken();
          if (s.length() != 2)
          {
            throw new IllegalArgumentException(
                "Timezone minutes field should be coded on 2 digits.");
          }
          tzmin = Integer.parseInt(s);
          if ((tzmin < MINUTE_MINIMUM) || (tzmin > MINUTE_MAXIMUM))
          {
            throw new IllegalArgumentException(
                "Timezone minutes field must be between " + MINUTE_MINIMUM
                    + " and " + MINUTE_MAXIMUM);
          }
        } else
        {
          throw new IllegalArgumentException("Invalid exception.");
        }
        // Convert the timezone information into milliseconds
        // a Minute = 60 seconds * 1000 milliseconds
        tzoffset = tzhour * 60 * 60 * 1000 + tzmin * 1000 * 60;
        if ((tzoffset < TIMEZONE_OFFSET_MIN)
            || (tzoffset > TIMEZONE_OFFSET_MAX))
        {
          throw new IllegalArgumentException("Timezone offset is invalid.");
        }
        if (plus == false)
        {
          tzoffset = -tzoffset;
        }
        // We must do an approximation here to determine the correct
        // offset since we cannot find it directly, otherwise do
        // not set any timezone information.
        String[] offsetIDs = TimeZone.getAvailableIDs();

        for (int i = 0; i < offsetIDs.length; i++)
        {
          TimeZone tz = TimeZone.getTimeZone(offsetIDs[i]);
          // Found a match set the calendar to this timezone
          if (tz.getRawOffset() == tzoffset)
          {
            calendar.setTimeZone(tz);
            break;
          }
        }
      } else
      // Timezone is UTC
      {
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
      }
    } catch (NumberFormatException ex)
    {
      throw new IllegalArgumentException(
          "Invalid numeric values in date or time field.");
    }
    return calendar;
  }

  /**
   * This parses a date in ISO 8601 Date format, and returns it as a calendar
   * value. The following format are currently supported for the DateTime
   * formats:
   * 
   * All formats from note: http://www.w3.org/TR/NOTE-datetime Extended Year
   * formats: +/-YYYYY for the year
   * 
   * The following calendar fields can be set by the returned calendar (all
   * depending on the data present)
   * 
   * GregorianCalendar.ERA (Always set) Calendar.YEAR (Always) All others are
   * conditional: Calendar.MONTH Calendar.DAY_OF_MONTH Calendar.HOUR_OF_DAY
   * Calendar.MINUTE Calendar.SECOND Calendar.MILLISECOND Calendar.ZONE_OFFSET
   * 
   * @param dateString
   *          The date string representation to parse.
   * @return A Calendar instance with all appropriate fields initialized.
   * @throws IllegalArgumentException
   */
  public static Calendar parseTimeStamp(Calendar calendar, String dateString)
      throws IllegalArgumentException
  {
    String s;
    int tzoffset;
    String cal = dateString;
    // YYYY-MM-DDThh:mm:ss.sTZD
    StringTokenizer st = new StringTokenizer(cal, "-T:.+Z", true);

    // Possible formats are as follows:
    // For year:
    // +YYYYY | -YYYYY | YYYY | -YYYY -> gYear
    // For Year-Month:
    // gYear-MM -> gYearMonth
    // For Year-Month-Day
    // gYearMonth-dd -> date
    // '-'? yyyy '-' mm '-' dd 'T' hh ':' mm ':' ss ('.' s+)? (zzzzzz)?

    try
    {
      // Year
      if (st.hasMoreTokens())
      {
        s = st.nextToken();
        // Remove the leading + character if present so
        // it conforms to XML Schema
        if (s.equals("+") == true)
        {
          s = st.nextToken();
          // The length of the next token must be 5 digits
          // as stated in ISO 8601.
          if (s.length() != 5)
          {
            throw new IllegalArgumentException(
                "Extended year must be composed of 5 digits");
          }
        } else
        // Add the - and following digits
        if (s.equals("-") == true)
        {
          s = s + st.nextToken();
          // The length of the next token must be 4 OR 5 digits
          // as stated in ISO 8601 and W3C XML Schema
          if (((s.length() == 5) || (s.length() == 6)) == false)
          {
            throw new IllegalArgumentException(
                "Extended year must be composed of 4 or 5 digits");
          }
        } else if (s.length() != 4)
        {
          throw new IllegalArgumentException(
              "Year must be composed of 4 digits");
        }

        int year = Integer.parseInt(s);
        if ((year < -99999) || (year > 99999))
        {
          throw new IllegalArgumentException(
              "Year is not within a valid range.");
        }

        calendar.set(Calendar.YEAR, year);
      } else
      {
        return calendar;
      }
      // Month
      if (check(st, "-"))
      {
        // We end with the Separator character
        // which is not a valid date format
        if (st.hasMoreTokens() == false)
        {
          throw new IllegalArgumentException("Month must be coded on 2 digits.");
        }
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException("Month must be coded on 2 digits.");
        }

        int month = Integer.parseInt(s) - 1;
        if ((month < 0) || (month > 11))
        {
          throw new IllegalArgumentException(
              "Month values must be within 01 and 12.");
        }
        calendar.set(Calendar.MONTH, month);
      } else
      {
        return calendar;
      }

      // Day
      if (check(st, "-"))
      {
        // We end with the Separator character
        // which is not a valid date format
        if (st.hasMoreTokens() == false)
        {
          throw new IllegalArgumentException(
              "Day values must be encoded on 2 digits.");
        }
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException(
              "Day values must be encoded on 2 digits.");
        }

        int day = Integer.parseInt(s);
        if ((day < DAY_OF_MONTH_MINIMUM) || (day > DAY_OF_MONTH_MAXIMUM))
        {
          throw new IllegalArgumentException(
              "Day values must be within 01 and 31.");
        }

        calendar.set(Calendar.DAY_OF_MONTH, day);
      } else
      {
        return calendar;
      }

      /******************* Time separator ******************/

      // Hour
      if (check(st, "T"))
      {
        // We end with the Separator character
        // which is not a valid date format
        if (st.hasMoreTokens() == false)
        {
          throw new IllegalArgumentException("Invalid time value.");
        }
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException("Hours must be coded on 2 digits.");
        }
        int hour = Integer.parseInt(s);
        // 24:00 is also allowed according to ISO 8601:2004
        // and will be represented by the next day in the Java calendar object
        if ((hour < HOUR_MINIMUM) || (hour > HOUR_MAXIMUM))
        {
          throw new IllegalArgumentException("Hours must be between on "
              + HOUR_MINIMUM + " and " + HOUR_MAXIMUM);
        }
        calendar.set(Calendar.HOUR_OF_DAY, hour);
      } else
      {
        return calendar;
      }

      // Minutes IS NOT optional
      if (check(st, ":") && (st.hasMoreTokens()))
      {
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException(
              "Minutes must be coded on 2 digits.");
        }
        int minutes = Integer.parseInt(s);
        if ((minutes < MINUTE_MINIMUM) || (minutes > MINUTE_MAXIMUM))
        {
          throw new IllegalArgumentException("Minutes must be between "
              + MINUTE_MINIMUM + " and " + MINUTE_MAXIMUM);
        }
        calendar.set(Calendar.MINUTE, minutes);
      } else
      {
        throw new IllegalArgumentException("Invalid minutes field.");
      }

      //
      // Not mandatory now
      //

      // Secondes
      if (!st.hasMoreTokens())
      {
        return calendar;
      }
      String tok = st.nextToken();
      if (tok.equals(":"))
      { // secondes
        if (st.hasMoreTokens())
        {
          s = st.nextToken();
          if (s.length() != 2)
          {
            throw new IllegalArgumentException(
                "Seconds should be coded on 2 digits.");
          }
          int secondes = Integer.parseInt(s);
          if ((secondes < SECOND_MINIMUM) || (secondes > SECOND_MAXIMUM))
          {
            throw new IllegalArgumentException(
                "Seconds should be coded on 2 digits.");
          }
          calendar.set(Calendar.SECOND, secondes);
          if (!st.hasMoreTokens())
          {
            return calendar;
          }

          // frac sec
          tok = st.nextToken();
          if (tok.equals("."))
          {
            // bug fixed, thx to Martin Bottcher
            String nt = st.nextToken();
            while (nt.length() < 3)
            {
              nt += "0";
            }
            nt = nt.substring(0, 3); // Cut trailing chars..
            int millisec = Integer.parseInt(nt);
            // int millisec = Integer.parseInt(st.nextToken()) * 10;
            calendar.set(Calendar.MILLISECOND, millisec);
            if (!st.hasMoreTokens())
            {
              return calendar;
            }
            tok = st.nextToken();
          } else
          {
            // calendar.set(Calendar.MILLISECOND, 0);
          }
        } else
        {
          throw new IllegalArgumentException("Seconds should be specified.");
        }
      } else
      {
        // calendar.set(Calendar.SECOND, 0);
        // calendar.set(Calendar.MILLISECOND, 0);
      }

      // Timezone
      if (!tok.equals("Z")) // UTC
      {
        if (!(tok.equals("+") || tok.equals("-")))
        {
          throw new IllegalArgumentException("only Z, + or - allowed");
        }
        boolean plus = tok.equals("+");
        if (!st.hasMoreTokens())
        {
          throw new IllegalArgumentException("Missing timezone hour field.");
        }
        s = st.nextToken();
        if (s.length() != 2)
        {
          throw new IllegalArgumentException(
              "Timezone hour field should be coded on 2 digits.");
        }
        int tzhour = Integer.parseInt(s);
        if ((tzhour < 0) || (tzhour > 14))
        {
          throw new IllegalArgumentException(
              "Timezone hours field must be between 00 and 14.");
        }

        int tzmin = 0;
        if (check(st, ":") && (st.hasMoreTokens()))
        {
          s = st.nextToken();
          if (s.length() != 2)
          {
            throw new IllegalArgumentException(
                "Timezone minutes field should be coded on 2 digits.");
          }
          tzmin = Integer.parseInt(s);
          if ((tzmin < MINUTE_MINIMUM) || (tzmin > MINUTE_MAXIMUM))
          {
            throw new IllegalArgumentException(
                "Timezone minutes field must be between " + MINUTE_MINIMUM
                    + " and " + MINUTE_MAXIMUM);
          }
        } else
        {
          throw new IllegalArgumentException("Invalid exception.");
        }
        // Convert the timezone information into milliseconds
        // a Minute = 60 seconds * 1000 milliseconds
        tzoffset = tzhour * 60 * 60 * 1000 + tzmin * 1000 * 60;
        if ((tzoffset < TIMEZONE_OFFSET_MIN)
            || (tzoffset > TIMEZONE_OFFSET_MAX))
        {
          throw new IllegalArgumentException("Timezone offset is invalid.");
        }
        if (plus == false)
        {
          tzoffset = -tzoffset;
        }
        // We must do an approximation here to determine the correct
        // offset since we cannot find it directly, otherwise do
        // not set any timezone information.
        String[] offsetIDs = TimeZone.getAvailableIDs();

        for (int i = 0; i < offsetIDs.length; i++)
        {
          TimeZone tz = TimeZone.getTimeZone(offsetIDs[i]);
          // Found a match set the calendar to this timezone
          if (tz.getRawOffset() == tzoffset)
          {
            calendar.setTimeZone(tz);
            break;
          }
        }
      } else
      // Timezone is UTC
      {
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
      }
    } catch (NumberFormatException ex)
    {
      throw new IllegalArgumentException(
          "Invalid numeric values in date or time field.");
    }

    return calendar;
  }

  /**
   * Converts a calendar object to an ISO 8601 string conformant representation.
   * This shall be represented as an ISO 8601 representation, depending on the
   * parameters passed.
   * 
   * @param cal
   *          The calendar to use for the conversion
   * @param useTime
   *          Set to true if the time information should be added.
   * @param useTimezone
   *          Set to true if the timezone information should be added.
   * @return The ISO 8601 String representation of this date
   * @throws java.lang.IllegalArgumentException
   */
  public static String toString(Calendar cal, boolean useTime,
      boolean useTimezone) throws IllegalArgumentException
  {
    PrintfFormat TwoDigitFormat;
    PrintfFormat YearFormat;
    String s = null;
    Object o[];

    TwoDigitFormat = new PrintfFormat("%02d");
    YearFormat = new PrintfFormat("%04d");

    if (cal == null)
    {
      throw new IllegalArgumentException("Calendar parameter is null.");
    }
    s = YearFormat.sprintf(cal.get(Calendar.YEAR));

    // Verify if month is set
    s = s + "-" + TwoDigitFormat.sprintf(cal.get(Calendar.MONTH) + 1);
    s = s + "-" + TwoDigitFormat.sprintf(cal.get(Calendar.DAY_OF_MONTH));
    if (useTime == true)
    {
      s = s + "T" + TwoDigitFormat.sprintf(cal.get(Calendar.HOUR_OF_DAY)) + ":"
          + TwoDigitFormat.sprintf(cal.get(Calendar.MINUTE));
      s = s + ":" + TwoDigitFormat.sprintf(cal.get(Calendar.SECOND));
      // Timezone offset
      TimeZone tz = cal.getTimeZone();
      if (useTimezone == true)
      {
        int tzoffset = tz.getRawOffset();
        if (tzoffset == 0)
          s = s + "Z";
        else
        {
          int tzminutes = tzoffset / (1000 * 60);
          int tzhour = tzminutes / 60;
          tzminutes = tzminutes % 60;
          if (tzminutes > 0)
          {
            s = s + "+";
          }
          s = s + TwoDigitFormat.sprintf(tzhour) + ":"
              + TwoDigitFormat.sprintf(tzminutes);
        }

      }
    }

    return s;
  }

  /**
   * Converts a calendar object to an ISO 8601 string conformant representation.
   * This shall be represented as an ISO 8601 representation, depending on the
   * parameters passed.
   * 
   * @param cal
   *          The calendar to use for the conversion
   * @param useTime
   *          Set to true if the time information should be added.
   * @param useTimezone
   *          Set to true if the timezone information should be added.
   * @return The ISO 8601 String representation of this date
   * @throws java.lang.IllegalArgumentException
   */
  public static String toCompactString(Calendar cal, boolean useTime,
      boolean useTimezone) throws IllegalArgumentException
  {
    PrintfFormat TwoDigitFormat;
    PrintfFormat YearFormat;
    String s = null;
    Object o[];

    TwoDigitFormat = new PrintfFormat("%02d");
    YearFormat = new PrintfFormat("%04d");

    if (cal == null)
    {
      throw new IllegalArgumentException("Calendar parameter is null.");
    }
    s = YearFormat.sprintf(cal.get(Calendar.YEAR));

    // Verify if month is set
    s = s + TwoDigitFormat.sprintf(cal.get(Calendar.MONTH) + 1);
    s = s + TwoDigitFormat.sprintf(cal.get(Calendar.DAY_OF_MONTH));
    if (useTime == true)
    {
      s = s + "T" + TwoDigitFormat.sprintf(cal.get(Calendar.HOUR_OF_DAY))
          + TwoDigitFormat.sprintf(cal.get(Calendar.MINUTE));
      s = s + TwoDigitFormat.sprintf(cal.get(Calendar.SECOND));
      // Timezone offset
      TimeZone tz = cal.getTimeZone();
      if (useTimezone == true)
      {
        int tzoffset = tz.getRawOffset();
        if (tzoffset == 0)
          s = s + "Z";
        else
        {
          int tzminutes = tzoffset / (1000 * 60);
          int tzhour = tzminutes / 60;
          tzminutes = tzminutes % 60;
          if (tzminutes > 0)
          {
            s = s + "+";
          }
          s = s + TwoDigitFormat.sprintf(tzhour) + ":"
              + TwoDigitFormat.sprintf(tzminutes);
        }

      }
    }

    return s;
  }

}

/*
 * 
 * $Log$
 */
