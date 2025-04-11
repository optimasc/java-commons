package com.optimasc.datatypes.utils;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URI;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.ParseException;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import com.optimasc.datatypes.Convertable;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.defined.BinaryType;
import com.optimasc.datatypes.defined.DateType;
import com.optimasc.datatypes.defined.IntType;
import com.optimasc.datatypes.defined.LatinStringType;
import com.optimasc.datatypes.defined.NonNegativeIntegerType;
import com.optimasc.datatypes.defined.ObjectIdentifierType;
import com.optimasc.datatypes.defined.ShortType;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.defined.TimestampType;
import com.optimasc.datatypes.defined.YearType;
import com.optimasc.datatypes.generated.LanguageType;
import com.optimasc.datatypes.io.XMLSchemaDeserializer.FacetData;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.DecimalType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.primitives.URIType;
import com.optimasc.lang.GregorianDatetimeCalendar;
import com.optimasc.lang.MediaType;
import com.optimasc.text.NumericFormatters;
import com.optimasc.text.StandardDateFormats;
import com.optimasc.text.StandardFormatters;
import com.optimasc.text.StandardFormatters.Latin1StringConverter;

/**
 * A class that is used to validate a value against a datatype. It 
 * represents a static map of basic Object types and their formatters
 * to parse them from their string representation to their Object 
 * representation and to convert them back to string types.
 * 
 */
public class DataValidator
{
  /** Class representing validation data. */
  public static class ValidationData
  {
    Class<?> classType;
    Format converters[];

    /** Create a new validation object.
     * 
     * @param classType [in] The Java Object type associated
     *   with the formatter.
     * @param converters [in] A list of {@link java.text.Format} classes
     *   used to parse the data to the specific object representation
     *   and to convert the data back to a string representation.
     */
    public ValidationData(Class<?> classType, Format converters[])
    {
      this.classType = classType;
      this.converters = converters;
    }
  }

  /** Mapping between Java types and parsers. This can be 
   *  updated as needed by the implementation. */
  public static Map<Class<?>, ValidationData> map;
  
  static
  {
    map = new Hashtable<Class<?>, ValidationData>();

    map.put(byte[].class, new ValidationData(byte[].class, new Format[] {
        new StandardFormatters.Base64CanonicalConverter(),
        new StandardFormatters.HexBinaryConverter() }));
    map.put(Boolean.class, new ValidationData(Boolean.class,
        new Format[] { new StandardFormatters.BooleanConverter() }));

    map.put(BigInteger.class, new ValidationData(BigInteger.class,
        new Format[] { new NumericFormatters.IntegerCanonicalConverter() }));

    map.put(Integer.class, new ValidationData(Integer.class,
        new Format[] { new NumericFormatters.IntConverter() }));

    map.put(Long.class, new ValidationData(Long.class,
        new Format[] { new NumericFormatters.LongConverter() }));

    map.put(String.class, new ValidationData(String.class, new Format[] {
        Latin1StringConverter.getInstance(), new StandardFormatters.StringConverter() }));

    map.put(URI.class, new ValidationData(URI.class,
        new Format[] { new StandardFormatters.URIConverter() }));

    map.put(GregorianDatetimeCalendar.class, new ValidationData(
        GregorianDatetimeCalendar.class, new Format[] {
            StandardDateFormats.DATE_ISO_8824, StandardDateFormats.DATE_EXIF,
            StandardDateFormats.W3C_DATETIME, StandardDateFormats.ISO8601_DATE,
            StandardDateFormats.ASN1_GENERALIZEDTIME }));

    /*    new ValidationData(ShortType.class,
            new Format[]{new NumericFormatters.ShortConverter()},
        new ValidationData(UnsignedByteType.class,
            new Format[]{new NumericFormatters.unsignedByteConverter()},*/

    DecimalFormat realFormat = new DecimalFormat();
    realFormat.setParseBigDecimal(true);

    map.put(BigDecimal.class, new ValidationData(BigDecimal.class,
        new Format[] { realFormat }));

    map.put(int[].class, new ValidationData(int[].class,
        new Format[] { new StandardFormatters.OIDConverter() }));

    map.put(Locale.class, new ValidationData(Locale.class,
        new Format[] { new StandardFormatters.LocaleTypeConverter() }));

    map.put(MediaType.class, new ValidationData(MediaType.class,
        new Format[] { new StandardFormatters.MediaTypeConverter() }));
  }

  /**
   * Verifies the validity of the value according to its type definition. In the
   * case where the input is a string, it also tries to convert the type to its
   * Java Object representation.
   * 
   * @param typ
   *          [in] The datatype definition.
   * @param value
   *          [in] The object value
   * @return The converted object.
   * @throws ParseException
   *           If the object does not meet the datatype constraints or if it
   *           cannot be converted from a string to its Java representation.
   */
  public static Object validate(Datatype typ, Object value) throws ParseException
  {
    Class clz = typ.getClassType();
    ValidationData v;
    Format[] converters;
    Object convertedObj = value;
    /* If the value is a string type, then try to convert it to its
     * correct representation.
     */
    if (value instanceof String)
    {
      ParseException parseError = null;
      v = map.get(clz);
      if (v != null)
      {
        converters = v.converters;
        for (int i = 0; i < converters.length; i++)
        {
          try
          {
            convertedObj = converters[i].parseObject((String) value);
          } catch (ParseException e)
          {
            parseError = e;
            continue;
          }
          parseError = null;
        }
        if (parseError != null)
        {
          throw parseError;
        }
      }
    }
    /* No error - object is converted correctly */
    if (typ instanceof Convertable)
    {
      TypeCheckResult res = new TypeCheckResult();
      convertedObj = ((Convertable) typ).toValue(convertedObj, res);
      if (res.error != null)
      {
        throw new ParseException(res.error.getMessage(), 0);
      }
      return convertedObj;
    }
    return convertedObj;
  }
}
