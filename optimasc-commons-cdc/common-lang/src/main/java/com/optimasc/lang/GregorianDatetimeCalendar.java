package com.optimasc.lang;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

/**
 * Gregorian Calendar that does not necessarily represent a fixed instant in
 * time, supporting partial date-time specifications as described by
 * <a href="https://www.w3.org/TR/NOTE-datetime">W3C Date and Time Formats</a>
 * and ISO 8601:2004.
 *
 * <p>
 * Two features distinguish this class from {@link java.util.GregorianCalendar}:
 * </p>
 *
 * <ol>
 * <li><b>Astronomical year numbering.</b> Per ISO 8601:2004, the year value
 * accepted by constructors and returned by {@link #getYear()} uses astronomical
 * numbering where 0 represents 1 BC, -1 represents 2 BC, and so on. Internally
 * the era and year fields of the parent class are populated accordingly.</li>
 * <li><b>Partial field specification.</b> Any subset of the standard date-time
 * fields (year, month, day, hour, minute, second, millisecond, timezone) may be
 * left unspecified. Fields explicitly set by the caller are distinguished from
 * those populated internally with defaults through the {@link #isUserSet(int)}
 * query. Getters such as {@link #getMonth()} return {@link #FIELD_UNDEFINED}
 * for fields that were not user-specified, while the underlying
 * {@link Calendar#get(int)} still returns the defaulted value.</li>
 * </ol>
 *
 * <p>
 * <b>Immutability.</b> Instances of this class are practically immutable. All
 * mutating methods inherited from {@link Calendar} and {@link GregorianCalendar}
 * ({@code set}, {@code add}, {@code roll}, {@code setTime}, {@code setTimeInMillis},
 * {@code setTimeZone}, {@code clear()}) are overridden to throw
 * {@link UnsupportedOperationException}. Note that {@link Calendar#clear(int)}
 * is declared {@code final} in the parent class and therefore cannot be
 * intercepted; callers must not invoke it. All field values are populated at
 * construction time, using defaults (January, day 1, 00:00:00.000, UTC) for
 * fields the caller does not specify.
 * </p>
 *
 * <p>
 * <b>Arithmetic.</b> Because this class is immutable, it does not expose any
 * date arithmetic API. To perform arithmetic, convert to a mutable
 * {@link GregorianCalendar} via {@link #toGregorianCalendar()}, mutate it, and
 * construct a new {@code GregorianDatetimeCalendar} from the result:
 * </p>
 *
 * <pre>
 * GregorianDatetimeCalendar partial = ...;
 * GregorianCalendar mutable = partial.toGregorianCalendar();
 * mutable.add(Calendar.DAY_OF_MONTH, 7);
 * GregorianDatetimeCalendar result = new GregorianDatetimeCalendar(mutable);
 * </pre>
 *
 * <p>
 * <b>Timezone handling.</b> Timezone information must be supplied via
 * constructor arguments. Months use 0-based indexing in constructor arguments
 * to match {@link Calendar} conventions, but {@link #getMonth()} returns
 * 1-based values matching ISO 8601 conventions.
 * </p>
 *
 * @author Carl Eric Codere
 */
public class GregorianDatetimeCalendar extends GregorianCalendar
{
  /** ISO 8601 UTC timezone. */
  public static final TimeZone ZULU = new SimpleTimeZone(0, "Z");

  /**
   * Value returned by the explicit getters of this class when the corresponding
   * field was not user-specified at construction time.
   */
  public static final int FIELD_UNDEFINED = Integer.MIN_VALUE;

  private static final long serialVersionUID = 3816584737503218331L;

  /**
   * Construction-time snapshot indicating whether each field was explicitly
   * set by the caller. Once construction completes this array is not modified.
   * Note however that {@link Calendar#clear(int)} is final in the parent class
   * and cannot be intercepted; if a caller invokes it the snapshot will become
   * stale for the affected field.
   */
  protected boolean userSet[];

  /**
   * Indicates that the time-of-day was specified as 24:00:00. Stored
   * internally as 00:00:00 on the same day; consumers must consult this flag
   * to reconstruct the original representation.
   */
  protected boolean endOfDay;

  /** Indicates whether this instance represents a local (unzoned) time. */
  protected boolean localTime;

  /** Set to true at the end of every constructor to lock the instance. */
  private transient boolean frozen;

  // ---------------------------------------------------------------------------
  // Constructors
  // ---------------------------------------------------------------------------

  /**
   * Constructs an empty calendar in the default timezone with no fields
   * user-set. Primarily useful as a base for reflection or subclass
   * construction; most callers should use one of the field-taking constructors.
   */
  public GregorianDatetimeCalendar()
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    superClear();
    localTime = false;
    endOfDay = false;
    applyDefaults();
    frozen = true;
  }

  /**
   * Constructs an empty calendar in the default timezone, marked as local time
   * or zoned per the argument.
   *
   * @param localTime true if this instance represents a local (unzoned) time
   */
  protected GregorianDatetimeCalendar(boolean localTime)
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    superClear();
    this.localTime = localTime;
    endOfDay = false;
    applyDefaults();
    frozen = true;
  }
  
  /**
   * Constructs a partial date-time with only the year specified. All other
   * fields are populated with defaults (January, day 1, 00:00:00.000) and
   * marked as not user-set.
   *
   * @param year astronomical year; 0 represents 1 BC, -1 represents 2 BC,
   *          positive values represent AD years
   */
  public GregorianDatetimeCalendar(int year)
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    superClear();
    localTime = true;
    endOfDay = false;

    applyAstronomicalYear(year);
    applyDefaults();
    frozen = true;
  }
  

  /**
   * Constructs a date in the default timezone.
   *
   * @param year astronomical year; 0 represents 1 BC, -1 represents 2 BC
   * @param month month, 0 for January through 11 for December, or
   *          {@link #FIELD_UNDEFINED}
   * @param dayOfMonth day of month between 1 and 31, or {@link #FIELD_UNDEFINED}
   * @throws IllegalArgumentException if any value is outside its allowed range
   */
  public GregorianDatetimeCalendar(int year, int month, int dayOfMonth)
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    superClear();
    localTime = true;
    endOfDay = false;

    applyAstronomicalYear(year);
    applyMonth(month);
    applyDayOfMonth(dayOfMonth);
    applyDefaults();
    frozen = true;
  }

  /**
   * Constructs a date in the specified timezone.
   *
   * @param year astronomical year; 0 represents 1 BC, -1 represents 2 BC
   * @param month month, 0 for January through 11 for December, or
   *          {@link #FIELD_UNDEFINED}
   * @param dayOfMonth day of month between 1 and 31, or {@link #FIELD_UNDEFINED}
   * @param tz the timezone
   * @throws IllegalArgumentException if any value is outside its allowed range
   */
  public GregorianDatetimeCalendar(int year, int month, int dayOfMonth, TimeZone tz)
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    superClear();
    endOfDay = false;

    applyAstronomicalYear(year);
    applyMonth(month);
    applyDayOfMonth(dayOfMonth);
    applyTimeZone(tz);
    applyDefaults();
    frozen = true;
  }

  /**
   * Constructs a date-time in local (unzoned) time. Hour must be fully
   * specified if provided; minute and second may be {@link #FIELD_UNDEFINED}.
   * A value of 24 for hour is accepted only when minute and second are 0 or
   * undefined, and represents end-of-day (24:00:00).
   *
   * @param year astronomical year
   * @param month month, 0 through 11
   * @param dayOfMonth day of month, 1 through 31
   * @param hour hour of day, 0 through 23, or 24 for end-of-day
   * @param minute minute, 0 through 59, or {@link #FIELD_UNDEFINED}
   * @param second second, 0 through 60 (allowing leap seconds), or
   *          {@link #FIELD_UNDEFINED}
   * @throws IllegalArgumentException if any value is outside its allowed range
   */
  public GregorianDatetimeCalendar(int year, int month, int dayOfMonth, int hour,
      int minute, int second)
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    superClear();
    localTime = true;
    endOfDay = false;

    applyAstronomicalYear(year);
    if (month == FIELD_UNDEFINED)
      throw new IllegalArgumentException("Month is required when hour is specified.");
    applyMonth(month);
    if (dayOfMonth == FIELD_UNDEFINED)
      throw new IllegalArgumentException("Day is required when hour is specified.");
    applyDayOfMonth(dayOfMonth);
    applyTimeOfDay(hour, minute, second, FIELD_UNDEFINED);
    applyDefaults();
    frozen = true;
  }

  /**
   * Constructs a fully-specified date-time with timezone.
   *
   * @param year astronomical year
   * @param month month, 0 through 11
   * @param dayOfMonth day of month, 1 through 31
   * @param hour hour of day, 0 through 23, or 24 for end-of-day
   * @param minute minute, 0 through 59
   * @param second second, 0 through 60
   * @param milliseconds millisecond, 0 through 999, or {@link #FIELD_UNDEFINED}
   * @param tz timezone offset in milliseconds, or {@link #FIELD_UNDEFINED}
   * @throws IllegalArgumentException if any value is outside its allowed range
   */
  public GregorianDatetimeCalendar(int year, int month, int dayOfMonth, int hour,
      int minute, int second, int milliseconds, int tz)
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    superClear();
    localTime = true;
    endOfDay = false;

    applyAstronomicalYear(year);
    applyMonth(month);
    applyDayOfMonth(dayOfMonth);
    applyTimeOfDay(hour, minute, second, milliseconds);
    applyTimeZoneOffset(tz);
    applyDefaults();
    frozen = true;
  }

  /**
   * Constructs a time-of-day. A value of 24 for hour is accepted only when
   * minute, second and milliseconds are 0 or undefined.
   *
   * @param hour hour of day, 0 through 23, or 24 for end-of-day
   * @param minute minute, 0 through 59
   * @param second second, 0 through 60, or {@link #FIELD_UNDEFINED}
   * @param milliseconds millisecond, 0 through 999, or {@link #FIELD_UNDEFINED}
   * @param tz timezone offset in milliseconds, or {@link #FIELD_UNDEFINED}
   * @throws IllegalArgumentException if any value is outside its allowed range
   */
  public GregorianDatetimeCalendar(int hour, int minute, int second, int milliseconds,
      int tz)
  {
    super(TimeZone.getDefault(), Locale.getDefault());
    userSet = new boolean[FIELD_COUNT];
    superClear();
    localTime = true;
    endOfDay = false;

    applyTimeOfDay(hour, minute, second, milliseconds);
    applyTimeZoneOffset(tz);
    applyDefaults();
    frozen = true;
  }

  /**
   * Constructs an instance from an existing {@link GregorianCalendar},
   * treating every standard date-time field as user-specified.
   *
   * <p>
   * The following fields are copied and marked user-set: {@code YEAR},
   * {@code MONTH}, {@code DAY_OF_MONTH}, {@code HOUR_OF_DAY}, {@code MINUTE},
   * {@code SECOND}, {@code MILLISECOND}, {@code ZONE_OFFSET}, {@code ERA},
   * and {@code DST_OFFSET}. The timezone is copied verbatim.
   * </p>
   *
   * <p>
   * The end-of-day flag cannot be recovered from a plain {@code Calendar} and
   * defaults to false. If the source represented 24:00:00 it will have been
   * normalized to 00:00:00 of the next day.
   * </p>
   *
   * @param source the source calendar; must not be null
   * @throws NullPointerException if source is null
   */
  public GregorianDatetimeCalendar(GregorianCalendar source)
  {
    super(source.getTimeZone(), Locale.getDefault());
    if (source == null)
      throw new NullPointerException("source");
    userSet = new boolean[FIELD_COUNT];
    superClear();
    endOfDay = false;
    localTime = false;

    // Copy all standard date-time fields.
    int[] fields = {
        Calendar.ERA, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
        Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND, Calendar.MILLISECOND,
        Calendar.ZONE_OFFSET, Calendar.DST_OFFSET
    };
    for (int i = 0; i < fields.length; i++)
    {
      super.set(fields[i], source.get(fields[i]));
      userSet[fields[i]] = true;
    }
    frozen = true;
  }

  /**
   * Constructs an instance from an existing {@link GregorianCalendar}, marking
   * only the specified fields as user-set. Non-specified fields are populated
   * with defaults.
   *
   * @param source the source calendar; must not be null
   * @param userSetFields array of {@link Calendar} field constants to mark as
   *          user-set; must not be null. May be empty.
   * @throws NullPointerException if either argument is null
   * @throws IllegalArgumentException if any element of {@code userSetFields}
   *           is not a valid Calendar field constant
   */
  public GregorianDatetimeCalendar(GregorianCalendar source, int[] userSetFields)
  {
    super(source.getTimeZone(), Locale.getDefault());
    if (source == null)
      throw new NullPointerException("source");
    if (userSetFields == null)
      throw new NullPointerException("userSetFields");
    userSet = new boolean[FIELD_COUNT];
    superClear();
    endOfDay = false;
    localTime = false;

    for (int i = 0; i < userSetFields.length; i++)
    {
      int field = userSetFields[i];
      if (field < 0 || field >= FIELD_COUNT)
        throw new IllegalArgumentException("Invalid Calendar field: " + field);
      super.set(field, source.get(field));
      userSet[field] = true;
    }
    // ERA is required to correctly interpret YEAR; copy it if YEAR was requested
    // but ERA was not explicitly requested.
    if (userSet[Calendar.YEAR] && !userSet[Calendar.ERA])
    {
      super.set(Calendar.ERA, source.get(Calendar.ERA));
    }
    applyDefaults();
    frozen = true;
  }

  // ---------------------------------------------------------------------------
  // Static factory
  // ---------------------------------------------------------------------------

  /**
   * Returns a calendar representing the same instant as the input, expressed
   * in UTC. The returned calendar has all standard fields marked user-set,
   * with wall-clock values shifted to UTC per ISO 8601 normalization.
   *
   * <p>
   * This preserves the instant in time: for an input representing 14:00 in a
   * UTC+3 zone, the result represents 11:00 UTC. DST is honored via the
   * source's actual offset at that instant.
   * </p>
   *
   * @param input the calendar to normalize; must not be null
   * @return a new GregorianDatetimeCalendar in UTC
   * @throws NullPointerException if input is null
   */
  public static GregorianDatetimeCalendar normalize(Calendar input)
  {
    if (input == null)
      throw new NullPointerException("input");
    GregorianCalendar utc = new GregorianCalendar(ZULU);
    utc.setTimeInMillis(input.getTimeInMillis());
    return new GregorianDatetimeCalendar(utc);
  }

  // ---------------------------------------------------------------------------
  // Read accessors
  // ---------------------------------------------------------------------------

  /**
   * Returns whether the specified field was explicitly set at construction.
   */
  public boolean isUserSet(int field)
  {
    return userSet[field];
  }

  /** @return true if this instance represents 24:00:00 end-of-day */
  public boolean isEndOfDay()
  {
    return endOfDay;
  }

  /** @return true if this instance represents a local (unzoned) time */
  public boolean isLocalTime()
  {
    return localTime;
  }

  /**
   * @return the astronomical year (0 = 1 BC, -1 = 2 BC), or
   *         {@link #FIELD_UNDEFINED} if not user-set
   */
  public int getYear()
  {
    if (!isUserSet(Calendar.YEAR))
      return FIELD_UNDEFINED;
    int year = get(Calendar.YEAR);
    if (get(Calendar.ERA) == GregorianCalendar.BC)
      return 1 - year;
    return year;
  }

  /**
   * @return the month between 1 and 12, or {@link #FIELD_UNDEFINED} if not
   *         user-set. Note that this differs from
   *         {@link Calendar#get(int) get(MONTH)} which returns 0-based values.
   */
  public int getMonth()
  {
    if (!isUserSet(Calendar.MONTH))
      return FIELD_UNDEFINED;
    return get(Calendar.MONTH) + 1;
  }

  /**
   * @return the day of month between 1 and 31, or {@link #FIELD_UNDEFINED} if
   *         not user-set
   */
  public int getDay()
  {
    if (!isUserSet(Calendar.DAY_OF_MONTH))
      return FIELD_UNDEFINED;
    return get(Calendar.DAY_OF_MONTH);
  }

  /**
   * @return the hour between 0 and 23, or 24 if end-of-day was specified, or
   *         {@link #FIELD_UNDEFINED} if not user-set
   */
  public int getHour()
  {
    if (endOfDay)
      return 24;
    if (!isUserSet(Calendar.HOUR_OF_DAY))
      return FIELD_UNDEFINED;
    return get(Calendar.HOUR_OF_DAY);
  }

  /**
   * @return the minute between 0 and 59, or {@link #FIELD_UNDEFINED} if not
   *         user-set
   */
  public int getMinute()
  {
    if (!isUserSet(Calendar.MINUTE))
      return FIELD_UNDEFINED;
    return get(Calendar.MINUTE);
  }

  /**
   * @return the second between 0 and 60, or {@link #FIELD_UNDEFINED} if not
   *         user-set
   */
  public int getSecond()
  {
    if (!isUserSet(Calendar.SECOND))
      return FIELD_UNDEFINED;
    return get(Calendar.SECOND);
  }

  /**
   * @return the millisecond between 0 and 999, or {@link #FIELD_UNDEFINED} if
   *         not user-set
   */
  public int getMillisecond()
  {
    if (!isUserSet(Calendar.MILLISECOND))
      return FIELD_UNDEFINED;
    return get(Calendar.MILLISECOND);
  }

  /**
   * @return the timezone offset in minutes, or {@link #FIELD_UNDEFINED} if not
   *         user-set
   */
  public int getTimezone()
  {
    if (!isUserSet(Calendar.ZONE_OFFSET))
      return FIELD_UNDEFINED;
    return get(Calendar.ZONE_OFFSET) / (1000 * 60);
  }

  // ---------------------------------------------------------------------------
  // Conversion
  // ---------------------------------------------------------------------------

  /**
   * Returns a fresh, mutable {@link GregorianCalendar} equivalent to this
   * partial date-time. Fields that were not user-set are populated with
   * defaults (January, day 1, 00:00:00.000, UTC). The returned instance is
   * independent of this one; callers may mutate it and perform arithmetic
   * without affecting this immutable object.
   *
   * <p>
   * If this instance represents 24:00:00 end-of-day, the returned calendar
   * uses {@code HOUR_OF_DAY = 24} at construction time and relies on lenient
   * mode to normalize to 00:00:00 of the next day. The end-of-day distinction
   * is lost in the conversion.
   * </p>
   *
   * @return a new independent GregorianCalendar
   */
  public GregorianCalendar toGregorianCalendar()
  {
    GregorianCalendar out = new GregorianCalendar(getTimeZone());
    out.clear();
    out.set(Calendar.ERA, get(Calendar.ERA));
    out.set(Calendar.YEAR, get(Calendar.YEAR));
    out.set(Calendar.MONTH, get(Calendar.MONTH));
    out.set(Calendar.DAY_OF_MONTH, get(Calendar.DAY_OF_MONTH));
    if (endOfDay)
    {
      out.set(Calendar.HOUR_OF_DAY, 24);
    }
    else
    {
      out.set(Calendar.HOUR_OF_DAY, get(Calendar.HOUR_OF_DAY));
    }
    out.set(Calendar.MINUTE, get(Calendar.MINUTE));
    out.set(Calendar.SECOND, get(Calendar.SECOND));
    out.set(Calendar.MILLISECOND, get(Calendar.MILLISECOND));
    // Trigger normalization so caller sees consistent state.
    out.getTimeInMillis();
    return out;
  }

  // ---------------------------------------------------------------------------
  // Overrides — mutation prevention
  // ---------------------------------------------------------------------------

  /**
   * @throws UnsupportedOperationException always; this class is immutable.
   *           Use {@link #toGregorianCalendar()} to obtain a mutable copy.
   */
  public void set(int field, int value)
  {
    if (frozen)
      throw new UnsupportedOperationException(
          "GregorianDatetimeCalendar is immutable; use toGregorianCalendar() for arithmetic.");
    super.set(field, value);
  }

  /**
   * @throws UnsupportedOperationException always; this class is immutable.
   */
  public void add(int field, int amount)
  {
    throw new UnsupportedOperationException(
        "GregorianDatetimeCalendar is immutable; use toGregorianCalendar() for arithmetic.");
  }

  /**
   * @throws UnsupportedOperationException always; this class is immutable.
   */
  public void roll(int field, int amount)
  {
    throw new UnsupportedOperationException(
        "GregorianDatetimeCalendar is immutable; use toGregorianCalendar() for arithmetic.");
  }

  /**
   * @throws UnsupportedOperationException always; this class is immutable.
   */
  public void roll(int field, boolean up)
  {
    throw new UnsupportedOperationException(
        "GregorianDatetimeCalendar is immutable; use toGregorianCalendar() for arithmetic.");
  }

  /**
   * @throws UnsupportedOperationException always; this class is immutable.
   */
  public void setTimeInMillis(long millis)
  {
    if (frozen)
      throw new UnsupportedOperationException(
          "GregorianDatetimeCalendar is immutable.");
    super.setTimeInMillis(millis);
  }

  /**
   * @throws UnsupportedOperationException always; this class is immutable.
   */
  public void setTimeZone(TimeZone value)
  {
    if (frozen)
      throw new UnsupportedOperationException(
          "GregorianDatetimeCalendar is immutable.");
    super.setTimeZone(value);
  }

  // ---------------------------------------------------------------------------
  // Object contract
  // ---------------------------------------------------------------------------

  public Object clone()
  {
    GregorianDatetimeCalendar other = (GregorianDatetimeCalendar) super.clone();
    other.userSet = (boolean[]) userSet.clone();
    other.frozen = true;
    return other;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!(obj instanceof GregorianDatetimeCalendar))
      return false;

    GregorianDatetimeCalendar other = (GregorianDatetimeCalendar) obj;
    if (endOfDay != other.endOfDay)
      return false;
    if (localTime != other.localTime)
      return false;
    if (!getTimeZone().equals(other.getTimeZone()))
      return false;

    int[] fields = {
        Calendar.ERA, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
        Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
        Calendar.MILLISECOND, Calendar.ZONE_OFFSET
    };
    for (int i = 0; i < fields.length; i++)
    {
      int field = fields[i];
      if (isUserSet(field) != other.isUserSet(field))
        return false;
      if (isUserSet(field) && get(field) != other.get(field))
        return false;
    }
    return true;
  }

  public int hashCode()
  {
    int result = 17;
    int[] fields = {
        Calendar.ERA, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
        Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
        Calendar.MILLISECOND, Calendar.ZONE_OFFSET
    };
    for (int i = 0; i < fields.length; i++)
    {
      int field = fields[i];
      if (isUserSet(field))
        result = 31 * result + get(field);
    }
    result = 31 * result + (endOfDay ? 1 : 0);
    result = 31 * result + (localTime ? 1 : 0);
    return result;
  }

  /**
   * Returns the ISO 8601 representation of this date-time. Only user-set
   * fields are emitted; unset trailing fields are omitted. Astronomical year
   * numbering is used: year 0 is emitted as "0000" (representing 1 BC), year
   * -1 as "-0001" (representing 2 BC), and so on. Years greater than 9999 are
   * emitted with a leading "+" sign per ISO 8601 §4.1.2.4.
   */
  public String toString()
  {
    StringBuffer buffer = new StringBuffer();

    int year = getYear();
    int month = getMonth();
    int day = getDay();
    int hour = getHour();
    int minute = getMinute();
    int second = getSecond();
    int millisecond = getMillisecond();
    int timezone = getTimezone();

    if (year != FIELD_UNDEFINED)
    {
      appendYear(buffer, year);
    }
    if (month != FIELD_UNDEFINED)
    {
      buffer.append('-');
      buffer.append(addPrefix(2, month));
      if (day != FIELD_UNDEFINED)
      {
        buffer.append('-');
        buffer.append(addPrefix(2, day));
      }
    }

    if (hour != FIELD_UNDEFINED)
    {
      buffer.append('T');
      buffer.append(addPrefix(2, hour));
      if (minute != FIELD_UNDEFINED)
      {
        buffer.append(':');
        buffer.append(addPrefix(2, minute));
      }
      if (second != FIELD_UNDEFINED)
      {
        buffer.append(':');
        buffer.append(addPrefix(2, second));
      }
      if (millisecond != FIELD_UNDEFINED)
      {
        buffer.append('.');
        buffer.append(addPrefix(3, millisecond));
      }
      if (timezone != FIELD_UNDEFINED && !localTime)
      {
        if (timezone == 0)
        {
          buffer.append('Z');
        }
        else
        {
          int tzHour = Math.abs(timezone / 60);
          int tzMinute = Math.abs(timezone % 60);
          buffer.append(timezone < 0 ? '-' : '+');
          buffer.append(addPrefix(2, tzHour));
          buffer.append(':');
          buffer.append(addPrefix(2, tzMinute));
        }
      }
    }
    return buffer.toString();
  }
  
//---------------------------------------------------------------------------
 // Addition 1: matches() method
 // ---------------------------------------------------------------------------

 /**
  * Returns {@code true} if every field that was user-set in this partial
  * date-time has the same value in the given calendar. Fields that were not
  * user-set in this instance are ignored in the comparison.
  *
  * <p>
  * This differs from {@link #equals(Object)} in two ways: it accepts any
  * {@link Calendar} (not just {@code GregorianDatetimeCalendar}), and it
  * performs a one-directional subsumption check rather than a symmetric
  * equality test. For example, a partial date-time representing {@code 2024-03}
  * will match any calendar whose year is 2024 and month is March, regardless
  * of day, hour, or timezone.
  * </p>
  *
  * <p>
  * The comparison uses the raw field values from {@link Calendar#get(int)}.
  * For the {@code YEAR} field, the era is also compared to ensure that
  * AD and BC years are distinguished correctly.
  * </p>
  *
  * @param other the calendar to compare against; must not be null
  * @return {@code true} if all user-set fields in this instance match the
  *         corresponding fields in {@code other}
  * @throws NullPointerException if {@code other} is null
  */
 public boolean matches(Calendar other)
 {
   if (other == null)
     throw new NullPointerException("other");

   int[] fields = {
       Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH,
       Calendar.HOUR_OF_DAY, Calendar.MINUTE, Calendar.SECOND,
       Calendar.MILLISECOND, Calendar.ZONE_OFFSET
   };
   for (int i = 0; i < fields.length; i++)
   {
     int field = fields[i];
     if (!isUserSet(field))
       continue;
     if (get(field) != other.get(field))
       return false;
     // When YEAR matches, also verify ERA so that AD 1 != BC 1.
     if (field == Calendar.YEAR)
     {
       if (get(Calendar.ERA) != other.get(Calendar.ERA))
         return false;
     }
   }
   // End-of-day check. At this point, all user-set fields (including
   // ZONE_OFFSET if specified) have already been verified to match.
   if (endOfDay)
   {
     if (other instanceof GregorianDatetimeCalendar)
     {
       if (!((GregorianDatetimeCalendar) other).isEndOfDay())
         return false;
     }
     else
     {
       // A standard Calendar cannot represent 24:00:00, so a strict match
       // on end-of-day is not possible. We accept 00:00:00 as a match
       // since that is how 24:00:00 normalizes.
       if (other.get(Calendar.HOUR_OF_DAY) != 0
           || other.get(Calendar.MINUTE) != 0
           || other.get(Calendar.SECOND) != 0
           || other.get(Calendar.MILLISECOND) != 0)
         return false;       
     }
   }
   return true;
 }


  // ---------------------------------------------------------------------------
  // Helpers
  // ---------------------------------------------------------------------------

  /**
   * Left-pads a non-negative integer with zeros to the given width.
   *
   * @param newLength target total length
   * @param value non-negative value
   */
  public static String addPrefix(int newLength, int value)
  {
    StringBuffer builder = new StringBuffer();
    builder.append(value);
    while (builder.length() < newLength)
    {
      builder.insert(0, '0');
    }
    return builder.toString();
  }

  /**
   * Appends an astronomical year to the buffer using ISO 8601 formatting.
   * Years 0000..9999 are emitted zero-padded to four digits. Negative years
   * are emitted with a leading '-' sign. Years above 9999 are emitted with a
   * leading '+' sign.
   */
  protected static void appendYear(StringBuffer buffer, int astronomicalYear)
  {
    if (astronomicalYear < 0)
    {
      buffer.append('-');
      buffer.append(addPrefix(4, -astronomicalYear));
    }
    else if (astronomicalYear > 9999)
    {
      buffer.append('+');
      buffer.append(astronomicalYear);
    }
    else
    {
      buffer.append(addPrefix(4, astronomicalYear));
    }
  }

  /**
   * Invokes {@link Calendar#clear()} on the parent while this instance is
   * still being constructed, bypassing the immutability guard.
   */
  protected void superClear()
  {
    boolean wasFrozen = frozen;
    frozen = false;
    try
    {
      super.clear();
    }
    finally
    {
      frozen = wasFrozen;
    }
    if (userSet != null)
      Arrays.fill(userSet, false);
  }

  /** Marks {@code field} as user-set and stores {@code value}. */
  protected void setField(int field, int value)
  {
    boolean wasFrozen = frozen;
    frozen = false;
    try
    {
      super.set(field, value);
    }
    finally
    {
      frozen = wasFrozen;
    }
    userSet[field] = true;
    if (field == HOUR_OF_DAY)
    {
      userSet[HOUR] = true;
      userSet[AM_PM] = true;
    }
    else if (field == HOUR)
    {
      userSet[HOUR_OF_DAY] = true;
      userSet[AM_PM] = true;
    }
  }

  /**
   * Applies default values (January, day 1, 00:00:00.000, UTC) to any field
   * that was not user-set, so the underlying Calendar has a fully-populated
   * state for arithmetic and conversion.
   */
  protected void applyDefaults()
  {
    boolean wasFrozen = frozen;
    frozen = false;
    try
    {
      if (!userSet[Calendar.ERA])
        super.set(Calendar.ERA, GregorianCalendar.AD);
      if (!userSet[Calendar.YEAR])
        super.set(Calendar.YEAR, 1970);
      if (!userSet[Calendar.MONTH])
        super.set(Calendar.MONTH, Calendar.JANUARY);
      if (!userSet[Calendar.DAY_OF_MONTH])
        super.set(Calendar.DAY_OF_MONTH, 1);
      if (!userSet[Calendar.HOUR_OF_DAY])
        super.set(Calendar.HOUR_OF_DAY, 0);
      if (!userSet[Calendar.MINUTE])
        super.set(Calendar.MINUTE, 0);
      if (!userSet[Calendar.SECOND])
        super.set(Calendar.SECOND, 0);
      if (!userSet[Calendar.MILLISECOND])
        super.set(Calendar.MILLISECOND, 0);
    }
    finally
    {
      frozen = wasFrozen;
    }
  }

  protected void applyAstronomicalYear(int year)
  {
    if (year <= 0)
    {
      setField(Calendar.ERA, GregorianCalendar.BC);
      setField(Calendar.YEAR, 1 - year);
    }
    else
    {
      setField(Calendar.ERA, GregorianCalendar.AD);
      setField(Calendar.YEAR, year);
    }
  }

  protected void applyMonth(int month)
  {
    if (month == FIELD_UNDEFINED)
      return;
    if (month < getMinimum(Calendar.MONTH) || month > getMaximum(Calendar.MONTH))
      throw new IllegalArgumentException("The month must be between 0 and 11.");
    setField(Calendar.MONTH, month);
  }

  protected void applyDayOfMonth(int day)
  {
    if (day == FIELD_UNDEFINED)
      return;
    if (day < getMinimum(Calendar.DAY_OF_MONTH)
        || day > getMaximum(Calendar.DAY_OF_MONTH))
      throw new IllegalArgumentException("The day of month must be between 1 and 31.");
    setField(Calendar.DAY_OF_MONTH, day);
  }

  protected void applyTimeOfDay(int hour, int minute, int second, int milliseconds)
  {
    // Intercept 24:00:00 before the normal HOUR_OF_DAY range check.
    if (hour == 24)
    {
      if ((minute != 0 && minute != FIELD_UNDEFINED)
          || (second != 0 && second != FIELD_UNDEFINED)
          || (milliseconds != 0 && milliseconds != FIELD_UNDEFINED))
      {
        throw new IllegalArgumentException(
            "24:00:00 is only valid when minute, second and milliseconds are 0.");
      }
      endOfDay = true;
      setField(Calendar.HOUR_OF_DAY, 0);
      setField(Calendar.MINUTE, 0);
      setField(Calendar.SECOND, 0);
      if (milliseconds != FIELD_UNDEFINED)
        setField(Calendar.MILLISECOND, 0);
      return;
    }

    if (hour == FIELD_UNDEFINED)
      return;
    if (hour < getMinimum(Calendar.HOUR_OF_DAY)
        || hour > getMaximum(Calendar.HOUR_OF_DAY))
      throw new IllegalArgumentException("The hour of day must be between 0 and 23.");
    setField(Calendar.HOUR_OF_DAY, hour);

    if (minute == FIELD_UNDEFINED)
      return;
    if (minute < getMinimum(Calendar.MINUTE) || minute > getMaximum(Calendar.MINUTE))
      throw new IllegalArgumentException("The minute must be between 0 and 59.");
    setField(Calendar.MINUTE, minute);

    if (second == FIELD_UNDEFINED)
      return;
    // Accept up to 60 to allow ISO 8601 leap seconds.
    if (second < getMinimum(Calendar.SECOND) || second > 60)
      throw new IllegalArgumentException("The second must be between 0 and 60.");
    setField(Calendar.SECOND, second);

    if (milliseconds == FIELD_UNDEFINED)
      return;
    if (milliseconds < getMinimum(Calendar.MILLISECOND)
        || milliseconds > getMaximum(Calendar.MILLISECOND))
      throw new IllegalArgumentException("The milliseconds must be between 0 and 999.");
    setField(Calendar.MILLISECOND, milliseconds);
  }

  protected void applyTimeZone(TimeZone tz)
  {
    boolean wasFrozen = frozen;
    frozen = false;
    try
    {
      super.setTimeZone(tz);
    }
    finally
    {
      frozen = wasFrozen;
    }
    setField(DST_OFFSET, tz.getDSTSavings());
    setField(ZONE_OFFSET, tz.getRawOffset());
    localTime = false;
  }

  protected void applyTimeZoneOffset(int tzMillis)
  {
    if (tzMillis == FIELD_UNDEFINED)
      return;
    if (tzMillis < getMinimum(Calendar.ZONE_OFFSET)
        || tzMillis > getMaximum(Calendar.ZONE_OFFSET))
      throw new IllegalArgumentException(
          "The timezone offset must be a valid range in milliseconds.");
    applyTimeZone(new SimpleTimeZone(tzMillis, ""));
  }
}
