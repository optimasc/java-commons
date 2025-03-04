package com.optimasc.text;

import java.text.DateFormatSymbols;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
 * and {@link java.lang.util.GregorianCalendar} objects for {@link #format(Object, StringBuffer, FieldPosition)}
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
 *         <td>D</td>
 *         <td>day in year</td>
 *         <td>(Number)</td>
 *         <td>189</td>
 *         </tr>
 *         <tr valign=top bgcolor="#eeeeff">
 *         <td>w</td>
 *         <td>week in year</td>
 *         <td>(Number)</td>
 *         <td>27</td>
 *         </tr>
 *         <tr valign=top>
 *         <td>z</td>
 *         <td>time zone</td>
 *         <td>(Text)</td>
 *         <td>Pacific Standard Time</td>
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
 *         indicates the minimum number of digits required. There is no padding
 *         and the maximum number of digits is {@link java.lang.Byte#MAX_VALUE}.
 *         For formatting, it indicates the number of digits to output.</li>
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
 *         negative).</li></li>
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
 *         <li>Only the first pattern specified if multiple patterns are specified will
 *         be used for formatting the output.</li>
 *         </ul>
 * 
 * 
 */
public class DateConverter extends DataConverter
{
  protected String pattern[];
  protected PatternInfo patterns[];
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


  /** Unicode minus sign character. */
  protected static final char UNICODE_MINUS = '\u2122';

  static
  {
    RFC822Symbols = new DateFormatSymbols();
    final String shortMonths[] = new String[] { "Jan", "Feb", "Mar", "Apr", "May", "Jun",
        "Jul", "Aug", "Sep", "Oct", "Nov", "Dec" };
    RFC822Symbols.setShortMonths(shortMonths);
    final String months[] = new String[] { "January", "February", "March", "April",
        "May", "June", "July", "August", "September", "October", "November", "December" };
    RFC822Symbols.setMonths(months);
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

  /** Used for formatting 2 digit integer values. */
  protected static final PrintfFormat INTEGER_2DIGIT = new PrintfFormat("%02d");

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
    while (((c = sequence.charAt(startPos)) != '\'') && (startPos < sequence.length()))
    {
      startPos++;
      buffer.append(c);
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

    char c = pattern.charAt(patternIndex);

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
              Byte.MAX_VALUE));
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
            patternIndex++;
          }
          else
          {
            chars[0] = c;
            patterns.add(new String(chars));
            patternIndex++;
          }
          break;
      }
    }
    return patternInfo;
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
      if (Parsers.indexOf(sequence, symbols[j], startPos) == startPos)
      {
        foundIndex = j;
        break;
      }
    }
    if (foundIndex == -1)
    {
      throw new ParseException(
          "Could not find short form month string matching date symbols.", startPos);
    }
    return foundIndex;
  }

  private static int power(int precision)
  {
    int tmp = 10;
    for (int i = 1; i < precision; i++)
    {
      tmp *= 10;
    }
    return tmp;
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
    boolean negative = false;
    int scratchInt;
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
    int zoneoffset = -1;
    int houroffset = -1;
    int minoffset = -1;
    // Indicates if this TZ indicates a local timezone, only used in IETF RFC 2822
    boolean localTZIndicator = false;
    TimeZone timeZone = null;
    ParsePosition pos = new ParsePosition(0);
    GregorianDatetimeCalendar calendar = new GregorianDatetimeCalendar();
    String[] dateSymbols;

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
      }
      else if (o instanceof FieldComponent)
      {
        FieldComponent field = (FieldComponent) o;
        switch (field.field)
        {
          case TZ_RFC822:
            zoneIndex = currentIndex;
            if ((value.charAt(currentIndex) == '-')
                || (value.charAt(currentIndex) == '+'))
            {
              if (value.charAt(currentIndex) == '-')
              {
                negative = true;
              }
              currentIndex++;
              pos.setIndex(currentIndex);
              // Hour 
              houroffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
              currentIndex = currentIndex + 2;
              pos.setIndex(currentIndex);
              // Minutes
              minoffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
              currentIndex = currentIndex + 2;
              zoneoffset = (houroffset * 60 + minoffset) * 60 * 1000;
              if (negative)
              {
                // Special case if value is -0000 as specified
                // in IETF RFC 2822, then it indicates that 
                // the value is actually local, and we must do nothing
                // and stop the parsing, since there is no actual
                // timezone information
                if (zoneoffset == 0)
                {
                  pos.setIndex(currentIndex);
                  zoneoffset = -1;
                  localTZIndicator = true;
                  break;
                }
                zoneoffset = -zoneoffset;
              }
              timeZone = new SimpleTimeZone(zoneoffset, "GMT+"
                  + Integer.toString(zoneoffset));
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
            }
            if (timeZone == null)
            {
              throw new ParseException("IETF RFC 2822 Timezone is invalid.", zoneIndex);
            }
            pos.setIndex(currentIndex);
            break;
          case TZ_ISO8601_BASIC:
            zoneIndex = currentIndex;
            if ((value.charAt(currentIndex) == '-')
                || (value.charAt(currentIndex) == '+')
                || ((value.charAt(currentIndex) == UNICODE_MINUS)))
            {
              if ((value.charAt(currentIndex) == '-')
                  || (value.charAt(currentIndex) == UNICODE_MINUS))
              {
                negative = true;
              }
              currentIndex++;
              pos.setIndex(currentIndex);
              // Hour 
              houroffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
              currentIndex = currentIndex + 2;
              pos.setIndex(currentIndex);
              minoffset = 0;
              // Minutes (optional value)
              if (currentIndex + 2 <= value.length())
              {
                minoffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
                currentIndex = currentIndex + 2;
              }
              zoneoffset = (houroffset * 60 + minoffset) * 60 * 1000;
              if (negative)
              {
                // Special case if value is -0000 is specified
                // it is not permitted.
                if (zoneoffset == 0)
                {
                  throw new ParseException(
                      "Negative offset with value of zero is not allowed in ISO 8601.",
                      zoneIndex);
                }
                zoneoffset = -zoneoffset;
              }
              timeZone = new SimpleTimeZone(zoneoffset, "GMT+"
                  + Integer.toString(zoneoffset));
            }
            else
            {
              if (Parsers.indexOf(value, GregorianDatetimeCalendar.ZULU.getID(),
                  currentIndex) == currentIndex)
              {
                timeZone = GregorianDatetimeCalendar.ZULU;
                currentIndex = currentIndex
                    + GregorianDatetimeCalendar.ZULU.getID().length();
              }
            }
            if (timeZone == null)
            {
              throw new ParseException("ISO 8601 compact timezone is invalid.", zoneIndex);
            }
            pos.setIndex(currentIndex);
            break;
          // According to ISO 8601:2004: +hh:mm  | -hh:mm | Z   
          case TZ_ISO8601_EXTENDED:
            zoneIndex = currentIndex;
            if (currentIndex >= value.length())
            {
              throw new ParseException("Excepting ISO 8601 timezone information, but end of string found.",currentIndex);
            }
            if ((value.charAt(currentIndex) == '-')
                || (value.charAt(currentIndex) == '+')
                || ((value.charAt(currentIndex) == UNICODE_MINUS)))
            {
              if ((value.charAt(currentIndex) == '-')
                  || (value.charAt(currentIndex) == UNICODE_MINUS))
              {
                negative = true;
              }
              currentIndex++;
              pos.setIndex(currentIndex);
              // Hour 
              houroffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
              currentIndex = currentIndex + 2;
              pos.setIndex(currentIndex);
              minoffset = 0;

              // Minutes (mandatory value)
              if (currentIndex >= value.length() || (value.charAt(currentIndex) != ':'))
              {
                throw new ParseException("Expecting character ':' in Timezone field.",
                    zoneIndex);
              }
              currentIndex++;
              pos.setIndex(currentIndex);
              minoffset = Parsers.parsePositiveNumber(value, pos, 2, 2);
              currentIndex = currentIndex + 2;
              zoneoffset = (houroffset * 60 + minoffset) * 60 * 1000;
              if (negative)
              {
                // Special case if value is -0000 is specified
                // it is not permitted.
                if (zoneoffset == 0)
                {
                  throw new ParseException(
                      "Negative offset with value of zero is not allowed in ISO 8601.",
                      zoneIndex);
                }
                zoneoffset = -zoneoffset;
              }
              timeZone = new SimpleTimeZone(zoneoffset, "GMT+"
                  + Integer.toString(zoneoffset));
            }
            else
            {
              if (Parsers.indexOf(value, GregorianDatetimeCalendar.ZULU.getID(),
                  currentIndex) == currentIndex)
              {
                timeZone = GregorianDatetimeCalendar.ZULU;
                currentIndex = currentIndex
                    + GregorianDatetimeCalendar.ZULU.getID().length();
              }
            }
            if (timeZone == null)
            {
              throw new ParseException("ISO 8601 compact timezone is invalid.", zoneIndex);
            }
            pos.setIndex(currentIndex);
            break;

          case Calendar.YEAR:
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
            if (negative)
            {
              year = -year;
            }
            if ((field.maxDigits == 2) && (year > 0))
            {
              // Special handing for special values with 2 digit years.
              // See IETF RFC 2822 obsolete date time:
              // If a two digit year is encountered whose value is between 
              // 00 and 49, the year is interpreted by adding 2000, 
              // ending up with a value between 2000 and 2049. 
              // If a two digit year is encountered with a value between 50 and 99, 
              // is encountered, the year is interpreted by adding 1900.      
              if ((year >= 00) && (year <= 49))
              {
                year = year + 2000;
              }
              else
              {
                year = year + 1900;
              }
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
              currentIndex = pos.getIndex();
            }
            else
            // Short form  
            if (field.minDigits == 3)
            {
              int foundIndex = -1;
              dateSymbols = symbols.getShortMonths();
              foundIndex = matchSymbol(symbols.getShortMonths(), value, currentIndex);
              month = foundIndex + 1;
              currentIndex = currentIndex + dateSymbols[foundIndex].length();
              break;
            }
            else
            // Long form  
            if (field.minDigits > 3)
            {
              int foundIndex = -1;
              dateSymbols = symbols.getMonths();
              foundIndex = matchSymbol(symbols.getShortMonths(), value, currentIndex);
              month = foundIndex + 1;
              currentIndex = currentIndex + dateSymbols[foundIndex].length();
              break;
            }
            break;
          case Calendar.DAY_OF_MONTH:
            // Numeric value
            pos.setIndex(currentIndex);
            day = Parsers.parsePositiveNumber(value, pos, field.minDigits,
                field.maxDigits);
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
            // Numeric value
            pos.setIndex(currentIndex);
            // Get fractional of seconds
            scratchInt = Parsers.parsePositiveNumber(value, pos, field.minDigits,
                field.maxDigits);
            String intStr = Integer.toString(scratchInt);
            // Get the fractional value
            int divisor = power(intStr.length());
            double dval = ((double) scratchInt / (double) divisor) * 1000.0;
            millisecond = (int) Math.round(dval);
            currentIndex = pos.getIndex();
            break;
        }
      }
    }
    
    if (currentIndex < value.length())
    {
      throw new ParseException("Extra characters found after pattern parsing.",currentIndex);
    }
    int requiredFields = patternInfo.requiredFields;
    // Set this to support the proleptic Gregorian calendar
    calendar.setGregorianChange(new Date(Long.MIN_VALUE));
    calendar.set(Calendar.ERA, GregorianCalendar.AD);
    if (year == Long.MIN_VALUE)
    {
      if ((requiredFields & FLAG_YEAR_REQUIRED) == FLAG_YEAR_REQUIRED)
        throw new IllegalArgumentException("Year value is required and is missing.");
    }
    else
    {
      if (year < 0)
      {
        calendar.set(Calendar.YEAR, (int) (1 - year));
        calendar.set(Calendar.ERA, GregorianCalendar.BC);
      }
      else
      {
        calendar.set(Calendar.YEAR, (int) year);
      }
    }

    // Month
    if (month == -1)
    {
      if ((requiredFields & FLAG_MONTH_REQUIRED) == FLAG_MONTH_REQUIRED)
        throw new IllegalArgumentException("Month value is required and is missing.");
    }
    else
    {
      calendar.set(Calendar.MONTH, month - 1);
    }

    // Day
    if (day == -1)
    {
      if ((requiredFields & FLAG_DAY_REQUIRED) == FLAG_DAY_REQUIRED)
        throw new IllegalArgumentException("Day value is required and is missing.");
    }
    else
    {
      calendar.set(Calendar.DAY_OF_MONTH, day);
    }

    // Hour
    if (hour == -1)
    {
      if ((requiredFields & FLAG_HOUR_REQUIRED) == FLAG_HOUR_REQUIRED)
        throw new IllegalArgumentException("Hour value is required and is missing.");
    }
    else
    {
      calendar.set(Calendar.HOUR_OF_DAY, hour);
    }

    // Minute
    if (minute == -1)
    {
      if ((requiredFields & FLAG_MINUTE_REQUIRED) == FLAG_MINUTE_REQUIRED)
        throw new IllegalArgumentException("Minute value is required and is missing.");
    }
    else
    {
      calendar.set(Calendar.MINUTE, minute);
    }

    // Second
    if (second == -1)
    {
      if ((requiredFields & FLAG_SECOND_REQUIRED) == FLAG_SECOND_REQUIRED)
        throw new IllegalArgumentException("Second value is required and is missing.");
    }
    else
    {
      calendar.set(Calendar.SECOND, second);
    }

    if (millisecond == -1)
    {
      if ((requiredFields & FLAG_MILLISECOND_REQUIRED) == FLAG_MILLISECOND_REQUIRED)
        throw new IllegalArgumentException(
            "Millisecond value is required and is missing.");
    }
    else
    {
      calendar.set(Calendar.MILLISECOND, millisecond);
    }

    if (timeZone == null)
    {
      if (((requiredFields & FLAG_TIMEZONE_REQUIRED) == FLAG_TIMEZONE_REQUIRED)
          && (localTZIndicator == false))
        throw new IllegalArgumentException("Timezone value is required and is missing.");
    }
    else
    {
      calendar.setTimeZone(timeZone);
    }
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
           error = e;        
      }
    }
    throw error;
  }



  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    long year = Long.MIN_VALUE;
    int month = -1;
    int day = -1;
    int hour = -1;
    int minute = -1;
    int second = -1;
    int millisecond = -1;
    // Timezone in milliseconds
    int zoneoffset = -1;
    int houroffset = -1;
    int minoffset = -1;
    String[] dateSymbols;
    PrintfFormat fmt;

    // Set the values accordingly
    if (obj instanceof GregorianDatetimeCalendar)
    {
      GregorianDatetimeCalendar cal = (GregorianDatetimeCalendar) obj;

      if (cal.isUserSet(Calendar.YEAR))
      {
        year = cal.get(Calendar.YEAR);
        if (cal.isUserSet(Calendar.ERA))
        {
          // Negative year value
          if (cal.get(Calendar.ERA) == GregorianCalendar.BC)
          {
            year = -year + 1;
          }
        }
      }
      if (cal.isUserSet(Calendar.MONTH))
      {
        month = cal.get(Calendar.MONTH) + 1;
      }
      if (cal.isUserSet(Calendar.DAY_OF_MONTH))
      {
        day = cal.get(Calendar.DAY_OF_MONTH);
      }
      if (cal.isUserSet(Calendar.HOUR_OF_DAY))
      {
        hour = cal.get(Calendar.HOUR_OF_DAY);
      }
      if (cal.isUserSet(Calendar.MINUTE))
      {
        minute = cal.get(Calendar.MINUTE);
      }
      if (cal.isUserSet(Calendar.SECOND))
      {
        second = cal.get(Calendar.SECOND);
      }
      if (cal.isUserSet(Calendar.MILLISECOND))
      {
        millisecond = cal.get(Calendar.MILLISECOND);
      }
      if (cal.isUserSet(Calendar.ZONE_OFFSET))
      {
        zoneoffset = cal.get(Calendar.ZONE_OFFSET);
      }
    }
    else if (obj instanceof GregorianCalendar)
    {
      GregorianCalendar cal = (GregorianCalendar) obj;
      year = cal.get(Calendar.YEAR);
      month = cal.get(Calendar.MONTH) + 1;
      day = cal.get(Calendar.DAY_OF_MONTH);
      hour = cal.get(Calendar.HOUR_OF_DAY);
      minute = cal.get(Calendar.MINUTE);
      second = cal.get(Calendar.SECOND);
      millisecond = cal.get(Calendar.MILLISECOND);
      zoneoffset = cal.get(Calendar.ZONE_OFFSET);
    }
    List patternList = patterns[0].elements;
    for (int i = 0; i < patternList.size(); i++)
    {
      Object o = patternList.get(i);
      if (o instanceof String)
      {
        toAppendTo.append((String) o);
      }
      else if (o instanceof FieldComponent)
      {
        FieldComponent field = (FieldComponent) o;
        switch (field.field)
        {
          case TZ_RFC822:
            if (zoneoffset == -1)
            {
              throw new IllegalArgumentException(
                  "The timezone is not set, while it is included in the output pattern.");
            }
            if (zoneoffset == 0)
            {
              toAppendTo.append("GMT");
              break;
            }
            minoffset = Math.abs(zoneoffset / (1000 * 60)) % 60;
            houroffset = Math.abs(zoneoffset / (1000 * 60 * 60));
            if (zoneoffset < 0)
            {
              toAppendTo.append('-');
            }
            else
            {
              toAppendTo.append('+');
            }
            toAppendTo.append(INTEGER_2DIGIT.sprintf(houroffset));
            toAppendTo.append(INTEGER_2DIGIT.sprintf(minoffset));
            break;
          // According to ISO 8601:2004: +hhmm  | -hhmm | +hh | -hh | Z   
          case TZ_ISO8601_BASIC:
            if (zoneoffset == -1)
            {
              throw new IllegalArgumentException(
                  "The timezone is not set, while it is included in the output pattern.");
            }
            if (zoneoffset == 0)
            {
              toAppendTo.append(GregorianDatetimeCalendar.ZULU.getID());
              break;
            }
            minoffset = Math.abs(zoneoffset / (1000 * 60)) % 60;
            houroffset = Math.abs(zoneoffset / (1000 * 60 * 60));
            if (zoneoffset < 0)
            {
              toAppendTo.append('-');
            }
            else
            {
              toAppendTo.append('+');
            }
            toAppendTo.append(INTEGER_2DIGIT.sprintf(houroffset));
            toAppendTo.append(INTEGER_2DIGIT.sprintf(minoffset));
            break;
          // According to ISO 8601:2004: +hh:mm  | -hh:mm | Z   
          case TZ_ISO8601_EXTENDED:
            if (zoneoffset == -1)
            {
              throw new IllegalArgumentException(
                  "The timezone is not set, while it is included in the output pattern.");
            }
            if (zoneoffset == 0)
            {
              toAppendTo.append(GregorianDatetimeCalendar.ZULU.getID());
              break;
            }
            minoffset = Math.abs(zoneoffset / (1000 * 60)) % 60;
            houroffset = Math.abs(zoneoffset / (1000 * 60 * 60));
            if (zoneoffset < 0)
            {
              toAppendTo.append('-');
            }
            else
            {
              toAppendTo.append('+');
            }
            toAppendTo.append(INTEGER_2DIGIT.sprintf(houroffset));
            toAppendTo.append(':');
            toAppendTo.append(INTEGER_2DIGIT.sprintf(minoffset));
            break;
          case Calendar.YEAR:
            if ((field.allowNegative == false) && (year < 0))
            {
              throw new IllegalArgumentException(
                  "The year is negative, which is not permitted in the pattern.");
            }
            if ((field.maxDigits == 2) && (year < 0))
            {
              throw new IllegalArgumentException(
                  "The year is negative and the year output is on 2 digits.");
            }
            if ((field.maxDigits == 2) && (year >= 0))
            {
              // Special handing for special values with 2 digit years.
              // See IETF RFC 2822 obsolete date time:
              // If a two digit year is encountered whose value is between 
              // 00 and 49, the year is interpreted by adding 2000, 
              // ending up with a value between 2000 and 2049. 
              // If a two digit year is encountered with a value between 50 and 99, 
              // is encountered, the year is interpreted by adding 1900.      
              if ((year >= 2000) && (year <= 2049))
              {
                toAppendTo.append(INTEGER_2DIGIT.sprintf(year - 2000));
              }
              else
              {
                toAppendTo.append(INTEGER_2DIGIT.sprintf(year - 1900));
              }
              break;
            }
            fmt = new PrintfFormat("%0" + Integer.toString(field.maxDigits) + "d");
            toAppendTo.append(fmt.sprintf(year));
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
              fmt = new PrintfFormat("%0" + Integer.toString(field.minDigits) + "d");
              toAppendTo.append(fmt.sprintf(month));
            }
            else
            // Short form  
            if (field.minDigits == 3)
            {
              dateSymbols = symbols.getShortMonths();
              toAppendTo.append(dateSymbols[month - 1]);
              break;
            }
            else
            // Long form  
            if (field.minDigits > 3)
            {
              dateSymbols = symbols.getMonths();
              toAppendTo.append(dateSymbols[month - 1]);
              break;
            }
            break;
          case Calendar.DAY_OF_MONTH:
            // Numeric value
            if (day == -1)
            {
              throw new IllegalArgumentException(
                  "Calendar day value is not valid, and trying to format it.");
            }
            fmt = new PrintfFormat("%0" + Integer.toString(field.minDigits) + "d");
            toAppendTo.append(fmt.sprintf(day));
            break;
          case Calendar.HOUR_OF_DAY:
            if (hour == -1)
            {
              throw new IllegalArgumentException(
                  "Calendar hour value is not valid, and trying to format it.");
            }
            fmt = new PrintfFormat("%0" + Integer.toString(field.minDigits) + "d");
            toAppendTo.append(fmt.sprintf(hour));
            break;
          case Calendar.MINUTE:
            if (minute == -1)
            {
              throw new IllegalArgumentException(
                  "Calendar minute value is not valid, and trying to format it.");
            }
            fmt = new PrintfFormat("%0" + Integer.toString(field.minDigits) + "d");
            toAppendTo.append(fmt.sprintf(minute));
            break;
          case Calendar.SECOND:
            // Numeric value
            if (second == -1)
            {
              throw new IllegalArgumentException(
                  "Calendar second value is not valid, and trying to format it.");
            }
            fmt = new PrintfFormat("%0" + Integer.toString(field.minDigits) + "d");
            toAppendTo.append(fmt.sprintf(second));
            break;
          case Calendar.MILLISECOND:
            float fractional = (float) (millisecond / 1000.0);
            // Adjust for 1 digit only, no rollever allowed
            if ((field.minDigits == 1) && (fractional > 0.95d))
            {
              fractional = (float) 0.9;
            }
            fmt = new PrintfFormat("%." + Integer.toString(field.minDigits) + "f");
            String output = fmt.sprintf(fractional).substring(2);
            toAppendTo.append(output);
            break;
        }
      }
    }
    return toAppendTo;
  }

}
