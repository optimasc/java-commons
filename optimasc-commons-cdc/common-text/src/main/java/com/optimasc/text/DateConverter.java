package com.optimasc.text;

import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.SimpleTimeZone;
import java.util.TimeZone;
import java.util.Vector;

import com.optimasc.lang.GregorianDatetimeCalendar;

/**
 * Class for formatting and parsing dates according to a specific pattern. It
 * allows for formatting (date -> text), parsing (text -> date), and
 * normalisation. It supports both {@link com.optimasc.lang.GregorianDatetimeCalendar}
 * and {@link java.util.GregorianCalendar} objects for {@link #format(Object, StringBuffer, FieldPosition)}
 * and {@link #parseObject(CharSequence)}.
 * 
 * @author Carl Eric Codere
 *
 *         <h4>Date and Time Format Syntax</h4>
 *         <p>
 *         To specify the time format, use a <em>time pattern</em> string. In
 *         this pattern, all ASCII letters are reserved as pattern letters,
 *         which are defined as follows:
 *         </p>
 *         <table border=0 cellspacing=3 cellpadding=0>
 *         <tr bgcolor="#ccccff">
 *         <th>Symbol</th>
 *         <th>Meaning</th>
 *         <th>Presentation</th>
 *         <th>Example</th>
 *         </tr>
 *         <tr valign=top bgcolor="#eeeeff">
 *         <td>y</td>
 *         <td>year</td>
 *         <td>(Number)</td>
 *         <td>1996</td>
 *         </tr>
 *         <tr valign=top>
 *         <td>M</td>
 *         <td>month in year</td>
 *         <td>(Text &amp; Number)</td>
 *         <td>July &amp; 07</td>
 *         </tr>
 *         <tr valign=top bgcolor="#eeeeff">
 *         <td>d</td>
 *         <td>day in month</td>
 *         <td>(Number)</td>
 *         <td>10</td>
 *         </tr>  
 *         <tr valign=top>
 *         <td>H</td>
 *         <td>hour in day (0&tilde;23)</td>
 *         <td>(Number)</td>
 *         <td>0</td>
 *         </tr>
 *         <tr valign=top bgcolor="#eeeeff">
 *         <td>m</td>
 *         <td>minute in hour</td>
 *         <td>(Number)</td>
 *         <td>30</td>
 *         </tr>
 *         <tr valign=top>
 *         <td>s</td>
 *         <td>second in minute</td>
 *         <td>(Number)</td>
 *         <td>55</td>
 *         </tr>
 *         <tr valign=top bgcolor="#eeeeff">
 *         <td>S</td>
 *         <td>fractional second</td>
 *         <td>(Number)</td>
 *         <td>978</td>
 *         </tr>
 *         <tr valign=top>
 *         <td>E</td>
 *         <td>day of week (English abbreviation)</td>
 *         <td>(Text)</td>
 *         <td>Tue</td>
 *         </tr>
 *         <tr valign=top bgcolor="#eeeeff">
 *         <td>Z</td>
 *         <td>time zone (RFC 822)</td>
 *         <td>(Number)</td>
 *         <td>-0800</td>
 *         </tr>
 *         <tr valign=top>
 *         <td>X</td>
 *         <td>time zone (Extended ISO 8601)</td>
 *         <td>(Text)</td>
 *         <td>-08:00; Z</td>
 *         </tr>
 *         <tr valign=top bgcolor="#eeeeff">
 *         <td>x</td>
 *         <td>time zone (Basic ISO 8601)</td>
 *         <td>(Text)</td>
 *         <td>-08; -0800; Z</td>
 *         </tr>
 *         <tr valign=top>
 *         <td>'</td>
 *         <td>escape for text</td>
 *         <td>(Delimiter)</td>
 *         <td>'Date='</td>
 *         </tr>
 *         <tr valign=top bgcolor="#eeeeff">
 *         <td>''</td>
 *         <td>single quote</td>
 *         <td>(Literal)</td>
 *         <td>'o''clock'</td>
 *         </tr>
 *         </table>
 *         <p>
 *         The count of pattern letters determines the format:
 *         <dl>
 *         <dt><strong>(Text)</strong></dd>
 *         <dd>4 or more pattern letters &rarr; use the full form, less than 4
 *         pattern letters &rarr; use a short or abbreviated form if one exists
 *         for both parsing and formatting.</dd>
 *         <dt><strong>(Number)</strong></dt>
 *         <dd>
 *         <ul>
 *         <li>For parsing and formatting, the minimum number of digits
 *         required, left padded with zeros as required. The maximum number of
 *         digits depends on the actual field value.</li>
 *         <li>Fractional digits are handled specially, for parsing the pattern
 *         indicates the minimum number of digits required. Any number of digits
 *         is accepted, and digits beyond the millisecond are truncated, not
 *         rounded. For formatting, it indicates the minimum number of digits to
 *         output; more digits, up to 3, are written when needed to represent
 *         the milliseconds exactly.</li>
 *         <li>Year is handled specially; that is, if the count of 'y' is 2, the
 *         year will be truncated to 2 digits when formatting and parsing will
 *         expect 2 digits. When parsing and the value of year is 2 digits, the
 *         rules according to IETF RFC 2822 are followed: if the value of the
 *         year is If a two digit year is encountered whose value is between 00
 *         and 49, the year is interpreted by adding 2000, ending up with a
 *         value between 2000 and 2049. If a two digit year is encountered with
 *         a value between 50 and 99, is encountered, the year is interpreted by
 *         adding 1900. If the count is 'y' is above 4, all values above 4 are
 *         optional for parsing, and will be padded with zeros on the left when
 *         formatting.</li>
 *         <li>Unlike other fields, fractional seconds are padded on the right
 *         with zero.</li>
 *         </ul>
 *         </dd>
 *         <dt><strong>(Text & Number)</strong></dt>
 *         <dd>3 or over, use text, otherwise use number.</dd>
 *         </dl>
 * 
 *         <p>
 *         Any characters in the pattern that are not in the ranges of
 *         ['a'..'z'] and ['A'..'Z'] will be treated as quoted text. For
 *         instance, characters like ':', '.', ' ', '#' and '@' will appear in
 *         the resulting time text even they are not embraced within single
 *         quotes.
 *         </p>
 * 
 *         <p>
 *         A pattern containing any invalid pattern letter will result in an
 *         exception thrown during formatting or parsing.
 *         </p>
 * 
 *         <h4>Optional fields and partial parsing</h4>
 * 
 *         <p>It is allowed to specify several patterns for parsing, 
 *         this permits to simulate dates and times where some of the fields
 *         are optional. It will try to match one of the patterns with
 *         the input.
 *         </p>
 * 
 * 
 *         <h4>Examples Using the US Locale</h4>
 * 
 *         <pre>
 * Format Pattern                       Result
 * --------------                       -------
 * "yyyy.MM.dd G 'at' HH:mm:ss vvvv" &rarr;  1996.07.10 AD at 15:08:56 Pacific Time
 * "EEE, MMM d, ''yy"                &rarr;  Wed, July 10, '96
 * "h:mm a"                          &rarr;  12:08 PM
 * "hh 'o''clock' a, zzzz"           &rarr;  12 o'clock PM, Pacific Daylight Time
 * "K:mm a, vvv"                     &rarr;  0:00 PM, PT
 * "yyyyy.MMMMM.dd GGG hh:mm aaa"    &rarr;  01996.July.10 AD 12:08 PM
 * </pre>
 * 
 *         <h4>Parsing return value</h4>
 * 
 *         <p>
 *         The following information applies to the parsing return value:
 *         </p>
 * 
 *         <ul>
 *         <li>The instance class returned is of type
 *         {@link com.optimasc.lang.GregorianDatetimeCalendar} which permits to
 *         see which fields are actually valid and externally set and not
 *         internally calculated.</li>
 *         <li>The Calendar instance returned will always be a Proleptic
 *         Gregorian Calendar</li>
 *         <li>The ERA will be set according to the sign of the (the year will
 *         be converted to BCE/BC with a positive year if the original year was
 *         negative).</li>
 *         <li>Values that do not represent a valid date, such as 31 February,
 *         are rejected.</li>
 *         <li>All errors are reported as {@link java.text.ParseException}. When
 *         several patterns are specified, the error reported is the one of the
 *         pattern that matched the input the furthest.</li>
 *         </ul>
 * 
 *         <h4>Formatting values</h4>
 * 
 *         <p>
 *         When formatting, if the field is in the pattern and it is not set or
 *         defined in the <code>Calendar</code> an
 *         {@link java.lang.IllegalArgumentException} will be thrown.
 *         </p>
 * 
 *         <ul>
 *         <li>If the ERA is not set, then it is assumed that the ERA is in the
 *         common era (AD/CE). The year is always printed as a numeric integer,
 *         with possible negative sign if it points to before the common era.</li>
 *         <li>If the formatting requires a 2 year digit output, the year is
 *         interpreted according to IETF RFC 2822.</li>
 *         <li>If the formatting requires an ISO 8601 Basic timezone
 *         information, both the hours and minutes will be output.</li>
 *         <li>When multiple patterns are specified, the pattern used for
 *         formatting is the one that uses the most fields available in the
 *         value, the first one in case of equality. A millisecond value of zero
 *         is considered not available, so that whole seconds are written
 *         without a fraction when a pattern without fraction exists.</li>
 *         <li>For a plain <code>Calendar</code>, the timezone offset written
 *         includes daylight saving time.</li>
 *         <li>A {@link com.optimasc.lang.GregorianDatetimeCalendar} whose
 *         time is the end of day is written with hour 24, and one that is a
 *         leap second is written with second 60.</li>
 *         </ul>
 * 
 * 
 */
public class DateConverter extends DataConverter
{
  protected String pattern[];
  protected PatternInfo patterns[];
  /**
   * Patterns used for formatting, or <code>null</code> to use the parsing
   * patterns.
   */
  protected PatternInfo formatPatterns[];
  protected DateFormatSymbols symbols;
  public static final DateFormatSymbols RFC822Symbols;

  public static final int TZ_RFC822 = -55;
  public static final int TZ_ISO8601_BASIC = -56;
  public static final int TZ_ISO8601_EXTENDED = -57;

  public static final int UNDEFINED_PRECISION = -1;

  protected static final int FLAG_YEAR_REQUIRED = 1 << 7;
  protected static final int FLAG_MONTH_REQUIRED = 1 << 6;
  protected static final int FLAG_DAY_REQUIRED = 1 << 5;
  protected static final int FLAG_HOUR_REQUIRED = 1 << 4;
  protected static final int FLAG_MINUTE_REQUIRED = 1 << 3;
  protected static final int FLAG_SECOND_REQUIRED = 1 << 2;
  protected static final int FLAG_MILLISECOND_REQUIRED = 1 << 1;
  protected static final int FLAG_TIMEZONE_REQUIRED = 1 << 0;

  protected static final int FLAG_DATE_MASK = FLAG_YEAR_REQUIRED | FLAG_MONTH_REQUIRED
      | FLAG_DAY_REQUIRED;
  protected static final int FLAG_TIME_MASK = FLAG_HOUR_REQUIRED | FLAG_MINUTE_REQUIRED
      | FLAG_SECOND_REQUIRED | FLAG_MILLISECOND_REQUIRED;


  /** Unicode minus sign character (U+2212). */
  protected static final char UNICODE_MINUS = '−';

  /**
   * Maximum number of digits accepted in a fraction of second when parsing.
   * Digits beyond the millisecond are truncated.
   */
  protected int maxFractionDigits = Integer.MAX_VALUE;

  /**
   * When <code>true</code> and the converter is lenient, a fraction of second
   * may have fewer digits than the pattern requires (at least one).
   */
  protected boolean lenientFractionDigits = false;

  /** Lowest astronomical year accepted when parsing. */
  protected int minYear = Integer.MIN_VALUE;
  /** Highest astronomical year accepted when parsing. */
  protected int maxYear = Integer.MAX_VALUE;

  static
  {
    RFC822Symbols = new DateFormatSymbols(java.util.Locale.US);
    final String shortMonths[] = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    RFC822Symbols.setShortMonths(shortMonths);
    final String months[] = new String[] { "January", "February", "March", "April",
        "May", "June", "July", "August", "September", "October", "November", "December" };
    RFC822Symbols.setMonths(months);
    final String shortWeekdays[] = new String[] { "", "Sun", "Mon", "Tue", "Wed", "Thu",
        "Fri", "Sat" };
    RFC822Symbols.setShortWeekdays(shortWeekdays);
  }

  // RFC822 named Timezones
  public static final TimeZone RFC822TimeZones[] = new TimeZone[] {
      new SimpleTimeZone(0, "UT"), new SimpleTimeZone(0, "GMT"),
      new SimpleTimeZone(-4 * 60 * 60 * 1000, "EDT"),
      new SimpleTimeZone(-5 * 60 * 60 * 1000, "EST"),
      new SimpleTimeZone(-5 * 60 * 60 * 1000, "CDT"),
      new SimpleTimeZone(-6 * 60 * 60 * 1000, "CST"),
      new SimpleTimeZone(-6 * 60 * 60 * 1000, "MDT"),
      new SimpleTimeZone(-7 * 60 * 60 * 1000, "MST"),
      new SimpleTimeZone(-7 * 60 * 60 * 1000, "PDT"),
      new SimpleTimeZone(-8 * 60 * 60 * 1000, "PST") };

  public DateConverter()
  {
    super(Calendar.class, true);
    patterns = null;
    symbols = RFC822Symbols;
  }
  
  public DateConverter(String[] patterns)
  {
    this();
    applyPatterns(patterns);
  }

  protected static class FieldComponent
  {
    /** Calendar field id */
    int field;
    int minDigits;
    int maxDigits;
    boolean allowNegative;

    public FieldComponent(int field, int minDigits, int maxDigits)
    {
      super();
      this.field = field;
      this.allowNegative = false;
      this.minDigits = minDigits;
      this.maxDigits = maxDigits;
    }

    public FieldComponent(int field, int minDigits, int maxDigits, boolean allowNegative)
    {
      super();
      this.field = field;
      this.allowNegative = allowNegative;
      this.minDigits = minDigits;
      this.maxDigits = maxDigits;
    }

    public String toString()
    {
      return "FieldComponent [field=" + field + ", minDigits=" + minDigits
          + ", maxDigits=" + maxDigits + ", allowNegative=" + allowNegative + "]";
    }

  }

  public Object parseObject(String value) throws ParseException
  {
    return parseObject((CharSequence) value);
  }
  
  /**
   * Error on a value that matches a pattern, but is not valid, such as an
   * invalid date or a value that the type does not allow. It takes precedence
   * over the syntax errors of the other patterns.
   */
  protected static class InvalidValueException extends ParseException
  {
    private static final long serialVersionUID = 1L;

    public InvalidValueException(String message, int errorOffset)
    {
      super(message, errorOffset);
    }
  }

  /** Information on a parsed pattern. */
  protected static class PatternInfo
  {
    /** Bitmask indicating the fields required. */
    protected int requiredFields;
    protected List elements;
    
    public PatternInfo()
    {
      elements = new Vector();
    }
  }

  /**
   * Parse a literal pattern, as defined in {@link java.text.SimpleDateFormat}
   * where the literal is enclosed in single quote characters.
   * 
   * @param sequence
   *          [in] The sequence of the pattern
   * @param startPos
   *          [in] The start position where the quote character occurs.
   * @return The literal value.
   */
  protected String getLiteral(CharSequence sequence, int startPos)
  {
    char c;
    StringBuffer buffer = new StringBuffer();
    c = sequence.charAt(startPos);
    if (c != '\'')
    {
      throw new IllegalArgumentException("Illegal token value, expected \"'\" character.");
    }
    startPos++;
    while ((startPos < sequence.length()) && ((c = sequence.charAt(startPos)) != '\''))
    {
      startPos++;
      buffer.append(c);
    }
    if (startPos >= sequence.length())
    {
      throw new IllegalArgumentException("Unterminated quoted literal in pattern.");
    }
    return buffer.toString();
  }

  /**
   * Return the next token composed of expectedChar starting at the start
   * position.
   */
  protected String getToken(char expectedChar, CharSequence sequence, int startPos)
  {
    char c;
    c = sequence.charAt(startPos);
    if (c != expectedChar)
    {
      throw new IllegalArgumentException("Illegal token value, expected '" + expectedChar
          + "' character.");
    }
    StringBuffer buffer = new StringBuffer();
    while (((startPos < sequence.length()) && (c = sequence.charAt(startPos)) == expectedChar))
    {
      startPos++;
      buffer.append(c);
    }
    return buffer.toString();
  }

  public void applyPatterns(String stringPatterns[]) throws IllegalArgumentException
  {
    PatternInfo tempPatterns[] = new PatternInfo[stringPatterns.length];
    for (int i = 0; i < stringPatterns.length; i++)
    {
      tempPatterns[i] = evaluatePattern(stringPatterns[i]);
    }
    this.patterns = tempPatterns;
    this.formatPatterns = null;
  }

  protected PatternInfo evaluatePattern(String pattern)
  {
    int patternLength = pattern.length();
    String literal;
    String token;
    int patternIndex = 0;
    boolean allowNegativeYears = false;
    char[] chars = new char[1];

    PatternInfo patternInfo = new PatternInfo();
    List patterns = patternInfo.elements;

    char c;

    // Start 
    while (patternIndex < patternLength)
    {
      c = pattern.charAt(patternIndex);
      switch (c)
      {
        case 'S':
          token = getToken('S', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          patterns.add(new FieldComponent(Calendar.MILLISECOND, token.length(),
              Integer.MAX_VALUE));
          patternInfo.requiredFields |= FLAG_MILLISECOND_REQUIRED;
          break;
        case 's':
          token = getToken('s', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          patterns
              .add(new FieldComponent(Calendar.SECOND, token.length(), token.length()));
          patternInfo.requiredFields |= FLAG_SECOND_REQUIRED;
          break;
        case 'm':
          token = getToken('m', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          patterns
              .add(new FieldComponent(Calendar.MINUTE, token.length(), token.length()));
          patternInfo.requiredFields |= FLAG_MINUTE_REQUIRED;
          break;
        case 'd':
          int maxLength = 2;
          int minLength = 2;
          token = getToken('d', pattern, patternIndex);
          if (token.length() < 2)
          {
            minLength = 1;
          }
          patternIndex = patternIndex + token.length();
          patterns.add(new FieldComponent(Calendar.DAY_OF_MONTH, minLength, maxLength));
          patternInfo.requiredFields |= FLAG_DAY_REQUIRED;
          break;
        case 'E':
          token = getToken('E', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          patterns.add(new FieldComponent(Calendar.DAY_OF_WEEK, token.length(), token
              .length()));
          break;
        case 'H':
          token = getToken('H', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          patterns.add(new FieldComponent(Calendar.HOUR_OF_DAY, token.length(), token
              .length()));
          patternInfo.requiredFields |= FLAG_HOUR_REQUIRED;
          break;
        case 'M':
          token = getToken('M', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          if (token.length() < 2)
          {
            throw new IllegalArgumentException(
                "Month pattern must be at least of 2 characters such as 'MM'");
          }
          patterns
              .add(new FieldComponent(Calendar.MONTH, token.length(), token.length()));
          patternInfo.requiredFields |= FLAG_MONTH_REQUIRED;
          break;
        case 'y':
          token = getToken('y', pattern, patternIndex);
          int minIndex = 2;
          patternIndex = patternIndex + token.length();
          if (token.length() < 2)
          {
            throw new IllegalArgumentException(
                "Year pattern must be at least of 2 characters such as 'yy'");
          }
          if (token.length() > 2)
          {
            minIndex = 4;
          }
          patternInfo.requiredFields |= FLAG_YEAR_REQUIRED;
          patterns.add(new FieldComponent(Calendar.YEAR, minIndex, token.length(),
              allowNegativeYears));
          break;
        // IETF RFC 822 / IETF RFC 2822 timezone  
        case 'Z':
          token = getToken('Z', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          patterns.add(new FieldComponent(TZ_RFC822, 1, 5, false));
          patternInfo.requiredFields |= FLAG_TIMEZONE_REQUIRED;
          break;
        // ISO 8601 Expanded / Standard timezone specification  
        case 'X':
          token = getToken('X', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          patterns.add(new FieldComponent(TZ_ISO8601_EXTENDED, 1, 6, false));
          patternInfo.requiredFields |= FLAG_TIMEZONE_REQUIRED;
          break;
        // ISO 8601 Expanded / Standard timezone specification  
        case 'x':
          token = getToken('x', pattern, patternIndex);
          patternIndex = patternIndex + token.length();
          patterns.add(new FieldComponent(TZ_ISO8601_BASIC, 1, 5, false));
          patternInfo.requiredFields |= FLAG_TIMEZONE_REQUIRED;
          break;
        // Special character that can be used in front of yyyy
        case '-':
          if (patternIndex + 1 < patternLength)
          {
            if (pattern.charAt(patternIndex + 1) == 'y')
            {
              allowNegativeYears = true;
              patternIndex++;
              break;
            }
          }
          chars[0] = c;
          patterns.add(new String(chars));
          patternIndex++;
          break;
        case '\'':
          literal = getLiteral(pattern, patternIndex);
          patternIndex = patternIndex + literal.length() + 2;
          patterns.add(literal);
          break;
        default:
          if ((c >= 'A') && (c <= 'Z') || (c >= 'a') && (c <= 'z'))
          {
            throw new IllegalArgumentException("Unsupported pattern letter '" + c
                + "' in pattern '" + pattern + "'");
          }
          chars[0] = c;
          patterns.add(new String(chars));
          patternIndex++;
          break;
      }
    }
    return patternInfo;
  }

  /**
   * Sets the patterns used for formatting, when they differ from the patterns
   * accepted for parsing.
   * 
   * @param stringPatterns
   *          [in] The patterns to use for formatting, or <code>null</code> to
   *          use the parsing patterns.
   * @throws IllegalArgumentException
   *           Thrown if the format or syntax of a pattern is invalid.
   */
  public void applyFormatPatterns(String stringPatterns[]) throws IllegalArgumentException
  {
    if (stringPatterns == null)
    {
      formatPatterns = null;
      return;
    }
    PatternInfo tempPatterns[] = new PatternInfo[stringPatterns.length];
    for (int i = 0; i < stringPatterns.length; i++)
    {
      tempPatterns[i] = evaluatePattern(stringPatterns[i]);
    }
    formatPatterns = tempPatterns;
  }

  /**
   * Applies the given pattern string to this date format.
   * 
   * @param pattern
   *          [in] The pattern to apply.
   * @throws IllegalArgumentException
   *           Thrown if the format or syntax of the pattern is invalid.
   */
  public void applyPattern(String pattern) throws IllegalArgumentException
  {
    patterns = new PatternInfo[1];
    patterns[0] = evaluatePattern(pattern);
    formatPatterns = null;
  }

  /**
   * Search for a match in a sequence character from a list of strings.
   * 
   * @param symbols
   *          [in] The possible options for the symbol.
   * @param sequence
   *          [in] The sequence that will be searched
   * @param startPos
   *          [in] The start position in the sequence where to search.
   * @return The array index in <code>symbols</code> if there is a match.
   * @throws ParseException
   *           If there is no match, exception is thrown
   */
  protected int matchSymbol(String[] symbols, CharSequence sequence, int startPos)
      throws ParseException
  {
    int foundIndex = -1;
    // Search for a match
    for (int j = 0; j < symbols.length; j++)
    {
      // Is the pattern a match
      if ((symbols[j].length() > 0)
          && (Parsers.indexOf(sequence, symbols[j], startPos) == startPos))
      {
        foundIndex = j;
        break;
      }
    }
    if (foundIndex == -1)
    {
      throw new ParseException(
          "Could not find a string matching date symbols.", startPos);
    }
    return foundIndex;
  }

  /**
   * Returns <code>true</code> if the astronomical year is a leap year in the
   * proleptic Gregorian calendar.
   */
  protected static boolean isLeapYear(long year)
  {
    long mod4 = ((year % 4) + 4) % 4;
    long mod100 = ((year % 100) + 100) % 100;
    long mod400 = ((year % 400) + 400) % 400;
    return (mod4 == 0) && ((mod100 != 0) || (mod400 == 0));
  }

  /**
   * Returns the number of days in a month of the proleptic Gregorian calendar.
   * 
   * @param year
   *          [in] The astronomical year
   * @param month
   *          [in] The month, from 1 to 12
   */
  protected static int daysInMonth(long year, int month)
  {
    switch (month)
    {
      case 2:
        return isLeapYear(year) ? 29 : 28;
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      default:
        return 31;
    }
  }

  private static void checkAvailable(CharSequence value, int index, String what)
      throws ParseException
  {
    if (index >= value.length())
    {
      throw new ParseException("Expecting " + what + ", but end of string found.", index);
    }
  }

  /**
   * Parses a timezone offset of the form <code>hh[[:]mm]</code> after the sign
   * at <code>index</code>.
   * 
   * @return the offset in milliseconds, with the sign applied, and sets the
   *         end index in <code>pos</code>.
   */
  private static int parseOffset(CharSequence value, int index, boolean separator,
      boolean minutesRequired, ParsePosition pos) throws ParseException
  {
    int zoneIndex = index;
    char sign = value.charAt(index);
    boolean negative = (sign == '-') || (sign == UNICODE_MINUS);
    index++;
    pos.setIndex(index);
    int houroffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
    index = pos.getIndex();
    int minoffset = 0;
    if (separator)
    {
      if (index >= value.length() || (value.charAt(index) != ':'))
      {
        throw new ParseException("Expecting character ':' in Timezone field.", zoneIndex);
      }
      index++;
      pos.setIndex(index);
      minoffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
    }
    else if (minutesRequired || (index + 2 <= value.length()))
    {
      minoffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
    }
    if (minoffset > 59)
    {
      throw new ParseException("Timezone minutes must be between 00 and 59.", index);
    }
    int zoneoffset = (houroffset * 60 + minoffset) * 60 * 1000;
    if (negative)
    {
      if ((zoneoffset == 0) && (minutesRequired == false))
      {
        throw new ParseException(
            "Negative offset with value of zero is not allowed in ISO 8601.", zoneIndex);
      }
      zoneoffset = -zoneoffset;
    }
    return zoneoffset;
  }

  /** Parses the value according to the specified pattern specification.
   * 
   * @param patternInfo [in] The pattern specification to match.
   * @param value [in] The value to match
   * @return The defined and set calendar
   * @throws ParseException If there is any parsing error parsing this
   *   date-time pattern.
   */
  protected Calendar parseObject(PatternInfo patternInfo, CharSequence value)
      throws ParseException
  {
    int currentIndex = 0;
    long year = Long.MIN_VALUE;
    int month = -1;
    int day = -1;
    int hour = -1;
    int minute = -1;
    int second = -1;
    int millisecond = -1;
    // Timezone character index
    int zoneIndex;
    // Timezone in milliseconds
    int zoneoffset;
    // Indicates if this TZ indicates a local timezone, only used in IETF RFC 2822
    boolean localTZIndicator = false;
    TimeZone timeZone = null;
    ParsePosition pos = new ParsePosition(0);
    String[] dateSymbols;
    char c;

    for (int i = 0; i < patternInfo.elements.size(); i++)
    {
      Object o = patternInfo.elements.get(i);
      if (o instanceof String)
      {
        String expected = (String) o;
        if (Parsers.indexOf(value, expected, currentIndex) != currentIndex)
        {
          throw new ParseException("Expected '" + expected + "'", currentIndex);
        }
        currentIndex = currentIndex + expected.length();
        continue;
      }
      FieldComponent field = (FieldComponent) o;
      switch (field.field)
      {
        case TZ_RFC822:
          zoneIndex = currentIndex;
          checkAvailable(value, currentIndex, "timezone");
          c = value.charAt(currentIndex);
          if ((c == '-') || (c == '+'))
          {
            zoneoffset = parseOffset(value, currentIndex, false, true, pos);
            currentIndex = pos.getIndex();
            // Special case if value is -0000 as specified
            // in IETF RFC 2822, then it indicates that 
            // the value is actually local, there is no actual
            // timezone information
            if ((zoneoffset == 0) && (c == '-'))
            {
              localTZIndicator = true;
            }
            else
            {
              timeZone = new SimpleTimeZone(zoneoffset, "GMT+" + Integer.toString(zoneoffset));
            }
          }
          else
          {
            // Other format as strings
            for (int k = 0; k < RFC822TimeZones.length; k++)
            {
              if (Parsers.indexOf(value, RFC822TimeZones[k].getID(), currentIndex) == currentIndex)
              {
                timeZone = RFC822TimeZones[k];
                currentIndex = currentIndex + RFC822TimeZones[k].getID().length();
                break;
              }
            }
            if (timeZone == null)
            {
              throw new ParseException("IETF RFC 2822 Timezone is invalid.", zoneIndex);
            }
          }
          break;
        case TZ_ISO8601_BASIC:
        case TZ_ISO8601_EXTENDED:
          zoneIndex = currentIndex;
          checkAvailable(value, currentIndex, "ISO 8601 timezone information");
          c = value.charAt(currentIndex);
          if ((c == '-') || (c == '+') || (c == UNICODE_MINUS))
          {
            zoneoffset = parseOffset(value, currentIndex,
                field.field == TZ_ISO8601_EXTENDED, false, pos);
            currentIndex = pos.getIndex();
            timeZone = new SimpleTimeZone(zoneoffset, "GMT+" + Integer.toString(zoneoffset));
          }
          else if (c == 'Z')
          {
            timeZone = GregorianDatetimeCalendar.ZULU;
            currentIndex++;
          }
          else
          {
            throw new ParseException("ISO 8601 timezone is invalid.", zoneIndex);
          }
          break;

        case Calendar.YEAR:
          boolean negative = false;
          checkAvailable(value, currentIndex, "year");
          if (value.charAt(currentIndex) == '-')
          {
            if (field.allowNegative == false)
            {
              throw new ParseException(
                  "A negative sign is present where it should not be.", currentIndex);
            }
            currentIndex++;
            negative = true;
          }
          pos.setIndex(currentIndex);
          year = Parsers.parsePositiveNumber(value, pos, field.minDigits,
              field.maxDigits);
          if ((field.maxDigits == 2) && (negative == false))
          {
            // Special handing for special values with 2 digit years.
            // See IETF RFC 2822 obsolete date time:
            // If a two digit year is encountered whose value is between 
            // 00 and 49, the year is interpreted by adding 2000, 
            // ending up with a value between 2000 and 2049. 
            // If a two digit year is encountered with a value between 50 and 99, 
            // is encountered, the year is interpreted by adding 1900.      
            if (year <= 49)
            {
              year = year + 2000;
            }
            else
            {
              year = year + 1900;
            }
          }
          if (negative)
          {
            year = -year;
          }
          currentIndex = pos.getIndex();
          break;
        case Calendar.MONTH:
          // Numeric value
          pos.setIndex(currentIndex);
          if (field.minDigits <= 2)
          {
            month = Parsers.parsePositiveNumber(value, pos, field.minDigits,
                field.maxDigits);
            if ((month < 1) || (month > 12))
            {
              throw new ParseException("Month must be between 1 and 12.", currentIndex);
            }
            currentIndex = pos.getIndex();
          }
          else
          {
            // Short form (3 letters) or long form
            dateSymbols = (field.minDigits == 3) ? symbols.getShortMonths() : symbols
                .getMonths();
            int foundIndex = matchSymbol(dateSymbols, value, currentIndex);
            month = foundIndex + 1;
            currentIndex = currentIndex + dateSymbols[foundIndex].length();
          }
          break;
        case Calendar.DAY_OF_WEEK:
          // The day of week is verified to be a valid name, but its
          // consistency with the date is not verified.
          dateSymbols = symbols.getShortWeekdays();
          currentIndex = currentIndex
              + dateSymbols[matchSymbol(dateSymbols, value, currentIndex)].length();
          break;
        case Calendar.DAY_OF_MONTH:
          // Numeric value
          pos.setIndex(currentIndex);
          day = Parsers.parsePositiveNumber(value, pos, field.minDigits,
              field.maxDigits);
          if ((day < 1) || (day > 31) || ((year != Long.MIN_VALUE) && (month != -1)
              && (day > daysInMonth(year, month))))
          {
            throw new ParseException("The day is not valid for the month.", currentIndex);
          }
          currentIndex = pos.getIndex();
          break;
        case Calendar.HOUR_OF_DAY:
          // Numeric value
          pos.setIndex(currentIndex);
          hour = Parsers.parsePositiveNumber(value, pos, field.minDigits,
              field.maxDigits);
          currentIndex = pos.getIndex();
          break;
        case Calendar.MINUTE:
          // Numeric value
          pos.setIndex(currentIndex);
          minute = Parsers.parsePositiveNumber(value, pos, field.minDigits,
              field.maxDigits);
          currentIndex = pos.getIndex();
          break;
        case Calendar.SECOND:
          // Numeric value
          pos.setIndex(currentIndex);
          second = Parsers.parsePositiveNumber(value, pos, field.minDigits,
              field.maxDigits);
          currentIndex = pos.getIndex();
          break;
        case Calendar.MILLISECOND:
          // Fraction of second: any number of digits, truncated to milliseconds.
          int start = currentIndex;
          millisecond = 0;
          int scale = 100;
          while ((currentIndex < value.length()) && (value.charAt(currentIndex) >= '0')
              && (value.charAt(currentIndex) <= '9'))
          {
            millisecond = millisecond + (value.charAt(currentIndex) - '0') * scale;
            scale = scale / 10;
            currentIndex++;
          }
          if (currentIndex - start > maxFractionDigits)
          {
            throw new ParseException("Fraction of second must contain at most "
                + maxFractionDigits + " digits.", start + maxFractionDigits);
          }
          int minFractionDigits = (lenient && lenientFractionDigits) ? 1 : field.minDigits;
          if (currentIndex - start < minFractionDigits)
          {
            throw new ParseException("Fraction of second must contain at least "
                + minFractionDigits + " digits.", currentIndex);
          }
          break;
      }
    }
    
    if (currentIndex < value.length())
    {
      throw new ParseException("Extra characters found after pattern parsing.",currentIndex);
    }

    if ((year != Long.MIN_VALUE) && ((year < minYear) || (year > maxYear)))
    {
      throw new InvalidValueException("Year must be between " + minYear + " and " + maxYear + ".", 0);
    }
    if ((year != Long.MIN_VALUE) && (month != -1) && (day != -1))
    {
      if ((month < 1) || (month > 12) || (day < 1) || (day > daysInMonth(year, month)))
      {
        throw new InvalidValueException("The date is not a valid calendar date.", 0);
      }
    }

    // Convert parsed values to GregorianDatetimeCalendar conventions:
    // -1 (not parsed) -> FIELD_UNDEFINED, month from 1-based to 0-based
    int calMonth = (month == -1) ? GregorianDatetimeCalendar.FIELD_UNDEFINED : month - 1;
    int calDay = (day == -1) ? GregorianDatetimeCalendar.FIELD_UNDEFINED : day;
    int calHour = (hour == -1) ? GregorianDatetimeCalendar.FIELD_UNDEFINED : hour;
    int calMinute = (minute == -1) ? GregorianDatetimeCalendar.FIELD_UNDEFINED : minute;
    int calSecond = (second == -1) ? GregorianDatetimeCalendar.FIELD_UNDEFINED : second;
    int calMillis = (millisecond == -1) ? GregorianDatetimeCalendar.FIELD_UNDEFINED : millisecond;
    int calTz = (timeZone == null) ? GregorianDatetimeCalendar.FIELD_UNDEFINED
        : timeZone.getRawOffset();

    GregorianDatetimeCalendar calendar;
    try
    {
      if (year == Long.MIN_VALUE)
      {
        calendar = new GregorianDatetimeCalendar(calHour, calMinute, calSecond, calMillis,
            calTz);
      }
      else
      {
        if ((year < Integer.MIN_VALUE) || (year > Integer.MAX_VALUE))
        {
          throw new InvalidValueException("Year is out of range.", 0);
        }
        calendar = new GregorianDatetimeCalendar((int) year, calMonth, calDay, calHour,
            calMinute, calSecond, calMillis, calTz);
      }
    } catch (IllegalArgumentException e)
    {
      throw new InvalidValueException(e.getMessage(), 0);
    }
    try
    {
      return adjust(calendar);
    } catch (InvalidValueException e)
    {
      throw e;
    } catch (ParseException e)
    {
      throw new InvalidValueException(e.getMessage(), e.getErrorOffset());
    }
  }

  /**
   * Called with the result of a successful parse, to apply the further
   * constraints or conversions of a specific type. The default implementation
   * returns the calendar unchanged.
   * 
   * @param calendar
   *          [in] The parsed value
   * @return The value to return to the caller
   * @throws ParseException
   *           if the value is not valid for the type
   */
  protected Calendar adjust(GregorianDatetimeCalendar calendar) throws ParseException
  {
    return calendar;
  }

  public Object parseObject(CharSequence value) throws ParseException
  {
    ParseException error = null;
    // Search for allowed pattern - and catch all ParseExceptions
    // at end if there is no match on any different patterns, then cause an error.
    for (int index = 0; index < patterns.length; index++)
    {
      PatternInfo patternInfo = patterns[index];
      try
      {
        return parseObject(patternInfo, value);
      } catch (ParseException e)
      {
        // Report the error of the first pattern that matched an invalid
        // value, otherwise that of the pattern that matched the furthest.
        if (error instanceof InvalidValueException)
        {
          continue;
        }
        if ((error == null) || (e instanceof InvalidValueException)
            || (e.getErrorOffset() > error.getErrorOffset()))
        {
          error = e;
        }
      } catch (RuntimeException e)
      {
        ParseException pe = new ParseException(String.valueOf(e.getMessage()), 0);
        if (error == null)
        {
          error = pe;
        }
      }
    }
    throw error;
  }

  /** Returns the number of bits set in a value. */
  private static int bitCount(int value)
  {
    int count = 0;
    while (value != 0)
    {
      count += value & 1;
      value = value >>> 1;
    }
    return count;
  }

  /** Appends a number padded on the left with zeros to <code>digits</code>. */
  private static void appendPadded(StringBuffer buffer, long value, int digits)
  {
    if (value < 0)
    {
      buffer.append('-');
      value = -value;
    }
    String str = Long.toString(value);
    for (int i = str.length(); i < digits; i++)
    {
      buffer.append('0');
    }
    buffer.append(str);
  }

  /** Appends a timezone offset in milliseconds as <code>+hh[:]mm</code>. */
  private static void appendOffset(StringBuffer buffer, int zoneoffset, boolean separator)
  {
    int minutes = Math.abs(zoneoffset) / (60 * 1000);
    buffer.append((zoneoffset < 0) ? '-' : '+');
    appendPadded(buffer, minutes / 60, 2);
    if (separator)
    {
      buffer.append(':');
    }
    appendPadded(buffer, minutes % 60, 2);
  }

  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    long year = Long.MIN_VALUE;
    int month = -1;
    int day = -1;
    int dayOfWeek = -1;
    int hour = -1;
    int minute = -1;
    int second = -1;
    int millisecond = -1;
    // Timezone in milliseconds
    int zoneoffset = 0;
    boolean zoneSet = false;
    String[] dateSymbols;

    // Set the values accordingly
    if (obj instanceof GregorianDatetimeCalendar)
    {
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) obj;

      if (cal.isUserSet(Calendar.YEAR))
      {
        year = cal.get(Calendar.YEAR);
        // Astronomical year numbering
        if (cal.get(Calendar.ERA) == GregorianCalendar.BC)
        {
          year = -year + 1;
        }
      }
      if (cal.isUserSet(Calendar.MONTH))
      {
        month = cal.get(Calendar.MONTH) + 1;
      }
      if (cal.isUserSet(Calendar.DAY_OF_MONTH))
      {
        day = cal.get(Calendar.DAY_OF_MONTH);
        dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
      }
      if (cal.isUserSet(Calendar.HOUR_OF_DAY))
      {
        hour = cal.isEndOfDay() ? 24 : cal.get(Calendar.HOUR_OF_DAY);
      }
      if (cal.isUserSet(Calendar.MINUTE))
      {
        minute = cal.get(Calendar.MINUTE);
      }
      if (cal.isUserSet(Calendar.SECOND))
      {
        second = cal.isLeapSecond() ? 60 : cal.get(Calendar.SECOND);
      }
      if (cal.isUserSet(Calendar.MILLISECOND))
      {
        millisecond = cal.get(Calendar.MILLISECOND);
      }
      if (cal.isUserSet(Calendar.ZONE_OFFSET))
      {
        zoneSet = true;
        zoneoffset = cal.get(Calendar.ZONE_OFFSET);
        if (cal.isUserSet(Calendar.DST_OFFSET))
        {
          zoneoffset += cal.get(Calendar.DST_OFFSET);
        }
      }
    }
    else if (obj instanceof Calendar)
    {
      Calendar cal = (Calendar) obj;
      year = cal.get(Calendar.YEAR);
      if (cal.get(Calendar.ERA) == GregorianCalendar.BC)
      {
        year = -year + 1;
      }
      month = cal.get(Calendar.MONTH) + 1;
      day = cal.get(Calendar.DAY_OF_MONTH);
      dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
      hour = cal.get(Calendar.HOUR_OF_DAY);
      minute = cal.get(Calendar.MINUTE);
      second = cal.get(Calendar.SECOND);
      millisecond = cal.get(Calendar.MILLISECOND);
      zoneSet = true;
      zoneoffset = cal.get(Calendar.ZONE_OFFSET) + cal.get(Calendar.DST_OFFSET);
    }
    else
    {
      throw new IllegalArgumentException("Object value is not instance of '"
          + clz.getName() + "'");
    }

    // Select the pattern that uses the most fields available.
    int available = 0;
    if (year != Long.MIN_VALUE)
      available |= FLAG_YEAR_REQUIRED;
    if (month != -1)
      available |= FLAG_MONTH_REQUIRED;
    if (day != -1)
      available |= FLAG_DAY_REQUIRED;
    if (hour != -1)
      available |= FLAG_HOUR_REQUIRED;
    if (minute != -1)
      available |= FLAG_MINUTE_REQUIRED;
    if (second != -1)
      available |= FLAG_SECOND_REQUIRED;
    if (millisecond > 0)
      available |= FLAG_MILLISECOND_REQUIRED;
    if (zoneSet)
      available |= FLAG_TIMEZONE_REQUIRED;
    PatternInfo candidates[] = (formatPatterns != null) ? formatPatterns : patterns;
    PatternInfo selected = candidates[0];
    int bestCount = -1;
    for (int i = 0; i < candidates.length; i++)
    {
      int required = candidates[i].requiredFields;
      if (((required & ~available) == 0) && (bitCount(required) > bestCount))
      {
        selected = candidates[i];
        bestCount = bitCount(required);
      }
    }

    List patternList = selected.elements;
    for (int i = 0; i < patternList.size(); i++)
    {
      Object o = patternList.get(i);
      if (o instanceof String)
      {
        toAppendTo.append((String) o);
        continue;
      }
      FieldComponent field = (FieldComponent) o;
      switch (field.field)
      {
        case TZ_RFC822:
        case TZ_ISO8601_BASIC:
        case TZ_ISO8601_EXTENDED:
          if (zoneSet == false)
          {
            throw new IllegalArgumentException(
                "The timezone is not set, while it is included in the output pattern.");
          }
          if ((zoneoffset == 0) && (field.field != TZ_RFC822))
          {
            toAppendTo.append(GregorianDatetimeCalendar.ZULU.getID());
            break;
          }
          appendOffset(toAppendTo, zoneoffset, field.field == TZ_ISO8601_EXTENDED);
          break;
        case Calendar.YEAR:
          if (year == Long.MIN_VALUE)
          {
            throw new IllegalArgumentException(
                "Calendar year value is not valid, and trying to format it.");
          }
          if ((field.allowNegative == false) && (year < 0))
          {
            throw new IllegalArgumentException(
                "The year is negative, which is not permitted in the pattern.");
          }
          if (field.maxDigits == 2)
          {
            // 2 digit years: see IETF RFC 2822 obsolete date time.
            if ((year >= 2000) && (year <= 2049))
            {
              appendPadded(toAppendTo, year - 2000, 2);
            }
            else if ((year >= 1950) && (year <= 1999))
            {
              appendPadded(toAppendTo, year - 1900, 2);
            }
            else
            {
              throw new IllegalArgumentException(
                  "The year cannot be represented on 2 digits.");
            }
            break;
          }
          if (Long.toString(Math.abs(year)).length() > field.maxDigits)
          {
            throw new IllegalArgumentException("The year " + year
                + " cannot be represented on " + field.maxDigits + " digits.");
          }
          appendPadded(toAppendTo, year, field.minDigits);
          break;
        case Calendar.MONTH:
          if (month == -1)
          {
            throw new IllegalArgumentException(
                "Calendar month value is not valid, and trying to format it.");
          }
          // Numeric value
          if (field.minDigits <= 2)
          {
            appendPadded(toAppendTo, month, field.minDigits);
          }
          else
          {
            dateSymbols = (field.minDigits == 3) ? symbols.getShortMonths() : symbols
                .getMonths();
            toAppendTo.append(dateSymbols[month - 1]);
          }
          break;
        case Calendar.DAY_OF_WEEK:
          if (dayOfWeek == -1)
          {
            throw new IllegalArgumentException(
                "Calendar day value is not valid, and trying to format the day of week.");
          }
          toAppendTo.append(symbols.getShortWeekdays()[dayOfWeek]);
          break;
        case Calendar.DAY_OF_MONTH:
          // Numeric value
          if (day == -1)
          {
            throw new IllegalArgumentException(
                "Calendar day value is not valid, and trying to format it.");
          }
          appendPadded(toAppendTo, day, field.minDigits);
          break;
        case Calendar.HOUR_OF_DAY:
          if (hour == -1)
          {
            throw new IllegalArgumentException(
                "Calendar hour value is not valid, and trying to format it.");
          }
          appendPadded(toAppendTo, hour, field.minDigits);
          break;
        case Calendar.MINUTE:
          if (minute == -1)
          {
            throw new IllegalArgumentException(
                "Calendar minute value is not valid, and trying to format it.");
          }
          appendPadded(toAppendTo, minute, field.minDigits);
          break;
        case Calendar.SECOND:
          // Numeric value
          if (second == -1)
          {
            throw new IllegalArgumentException(
                "Calendar second value is not valid, and trying to format it.");
          }
          appendPadded(toAppendTo, second, field.minDigits);
          break;
        case Calendar.MILLISECOND:
          if (millisecond == -1)
          {
            throw new IllegalArgumentException(
                "Calendar millisecond value is not valid, and trying to format it.");
          }
          // Exact value: three digits without trailing zeros, but at least
          // the number of digits of the pattern.
          StringBuffer fraction = new StringBuffer();
          appendPadded(fraction, millisecond, 3);
          int length = 3;
          while ((length > field.minDigits) && (fraction.charAt(length - 1) == '0'))
          {
            length--;
          }
          fraction.setLength(length);
          for (int k = length; k < field.minDigits; k++)
          {
            fraction.append('0');
          }
          toAppendTo.append(fraction.toString());
          break;
      }
    }
    return toAppendTo;
  }

}
