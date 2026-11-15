package com.optimasc.text;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.FieldPosition;
import java.text.ParseException;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;

/**
 * Converter for durations made of a month component and a day-time
 * component, returning a <code>javax.xml.datatype.Duration</code>.
 *
 * <ul>
 * <li>{@link #getInstance()}: W3C XML Schema Definition Language (XSD) 1.1
 * <code>duration</code> type. The lexical form is validated by
 * {@link StandardDateFormatters.XSDDurationConverter}: fractions of second of
 * more than 3 digits are truncated.</li>
 * <li>{@link #getLDAPInstance()}: IETF draft-codere-ldapsyntax
 * <code>Duration</code> syntax (OID: 1.3.6.1.4.1.61799.5.40.34). The lexical
 * form is validated by {@link StandardDateFormatters.DurationConverter}:
 * fractions of second of more than 3 digits are rejected.</li>
 * </ul>
 *
 * <p>
 * In both cases negative durations are rejected and only the seconds
 * component may have a fraction. The validated canonical form is converted
 * with <code>javax.xml.datatype.DatatypeFactory</code>.
 * </p>
 *
 * <p>
 * {@link #format} rejects a <code>Duration</code> that is negative or that
 * has a fraction on a component other than seconds, and writes the canonical
 * representation of the lexical converter, so that formatting followed by
 * parsing yields an equal value.
 * </p>
 *
 * <p>
 * Instances are stateless and safe for concurrent use: each thread uses its
 * own <code>DatatypeFactory</code>.
 * </p>
 */
public class CalendarDurationConverter extends DataConverter
{
  private static final DataConverter instance = new CalendarDurationConverter();
  private static final DataConverter ldapInstance = new CalendarDurationConverter(
      StandardDateFormatters.DurationConverter.getInstance());

  /** The <code>DatatypeFactory</code> of each thread, which JAXP does not
   *  require to be thread-safe. */
  private static final ThreadLocal factories = new ThreadLocal()
  {
    protected Object initialValue()
    {
      try
      {
        return DatatypeFactory.newInstance();
      } catch (DatatypeConfigurationException e)
      {
        IllegalStateException ex = new IllegalStateException(
            "No javax.xml.datatype.DatatypeFactory implementation is available.");
        ex.initCause(e);
        throw ex;
      }
    }
  };

  /** Validates the lexical form and returns its canonical representation. */
  protected final DataConverter lexicalConverter;

  /** Creates a converter for the XSD <code>duration</code> type. */
  public CalendarDurationConverter()
  {
    this(StandardDateFormatters.XSDDurationConverter.getInstance());
  }

  /**
   * Creates a converter whose lexical form is validated by the specified
   * converter.
   *
   * @param lexicalConverter
   *          [in] A converter returning the canonical representation of a
   *          duration as a <code>String</code>, such as
   *          {@link StandardDateFormatters.DurationConverter}. Must not be
   *          <code>null</code>.
   */
  protected CalendarDurationConverter(DataConverter lexicalConverter)
  {
    super(Duration.class, false);
    if (lexicalConverter == null)
      throw new IllegalArgumentException("Lexical converter must not be null.");
    this.lexicalConverter = lexicalConverter;
  }

  /** Returns the converter for the XSD <code>duration</code> type. */
  public static DataConverter getInstance()
  {
    return instance;
  }

  /** Returns the converter for the IETF draft-codere-ldapsyntax
   *  <code>Duration</code> syntax. */
  public static DataConverter getLDAPInstance()
  {
    return ldapInstance;
  }

  public Object parseObject(CharSequence value) throws ParseException
  {
    String canonical = (String) lexicalConverter.parseObject(value);
    try
    {
      return ((DatatypeFactory) factories.get()).newDuration(canonical);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Illegal duration '" + value + "': " + e.getMessage(), 0);
    }
  }

  public Object parseObject(String value) throws ParseException
  {
    return parseObject((CharSequence) value);
  }

  public StringBuffer format(Object value, StringBuffer toAppendTo, FieldPosition pos)
  {
    if ((value instanceof Duration) == false)
    {
      throw new IllegalArgumentException("Object value is not instance of '"
          + clz.getName() + "'");
    }
    Duration duration = (Duration) value;
    if (duration.getSign() < 0)
    {
      throw new IllegalArgumentException("Negative durations are not allowed.");
    }
    StringBuffer lexical = new StringBuffer("P");
    appendInteger(lexical, duration, DatatypeConstants.YEARS, 'Y');
    appendInteger(lexical, duration, DatatypeConstants.MONTHS, 'M');
    appendInteger(lexical, duration, DatatypeConstants.DAYS, 'D');
    if (duration.isSet(DatatypeConstants.HOURS) || duration.isSet(DatatypeConstants.MINUTES)
        || duration.isSet(DatatypeConstants.SECONDS))
    {
      lexical.append('T');
      appendInteger(lexical, duration, DatatypeConstants.HOURS, 'H');
      appendInteger(lexical, duration, DatatypeConstants.MINUTES, 'M');
      if (duration.isSet(DatatypeConstants.SECONDS))
      {
        BigDecimal seconds = toBigDecimal(duration.getField(DatatypeConstants.SECONDS));
        lexical.append(seconds.toPlainString()).append('S');
      }
    }
    try
    {
      return toAppendTo.append((String) lexicalConverter.parseObject(lexical.toString()));
    } catch (ParseException e)
    {
      throw new IllegalArgumentException("Illegal duration '" + lexical + "': "
          + e.getMessage());
    }
  }

  /** Appends a component that must be an integer, if it is set. */
  private static void appendInteger(StringBuffer lexical, Duration duration,
      DatatypeConstants.Field field, char designator)
  {
    if (duration.isSet(field) == false)
    {
      return;
    }
    BigDecimal number = toBigDecimal(duration.getField(field));
    BigInteger integer;
    try
    {
      integer = number.toBigIntegerExact();
    } catch (ArithmeticException e)
    {
      throw new IllegalArgumentException("Only seconds may have a fraction.");
    }
    lexical.append(integer.toString()).append(designator);
  }

  private static BigDecimal toBigDecimal(Number number)
  {
    if (number instanceof BigDecimal)
    {
      return (BigDecimal) number;
    }
    if (number instanceof BigInteger)
    {
      return new BigDecimal((BigInteger) number);
    }
    return new BigDecimal(number.toString());
  }
}
