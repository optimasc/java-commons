package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import com.optimasc.datatypes.Convertable;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.facets.EnumerationFacet;
import com.optimasc.datatypes.primitives.PrimitiveType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.text.Parsers;
import com.optimasc.text.StandardFormatters;

/**
 * Represents a language identifier. Values are <code>java.util.Locale</code>
 * instances with a non-empty language code.
 *
 * <p>
 * {@link #toValue(Object, TypeCheckResult)} accepts:
 * </p>
 *
 * <ul>
 * <li>a <code>Locale</code>: its language, country and variant are kept
 * unchanged. A <code>Locale</code> without a language code is rejected.</li>
 * <li>a character sequence holding a 2-letter ISO 639-1 language code, in any
 * case, which gives a <code>Locale</code> with that language only.</li>
 * <li>the character sequence <code>x-default</code>, in any case, which
 * gives {@link StandardFormatters.LocaleTypeConverter#X_DEFAULT}. As in XMP,
 * it denotes the default alternative rather than a particular language; it
 * is not replaced by the default locale of the Java virtual machine.</li>
 * </ul>
 *
 * <p>
 * To parse a full language tag, including its region, use
 * {@link StandardFormatters.LocaleTypeConverter}.
 * </p>
 */
public class LanguageType extends PrimitiveType implements EnumerationFacet, Convertable
{
  protected EnumerationHelper enumHelper;

  public LanguageType()
  {
    super(false);
    enumHelper = new EnumerationHelper(Locale.class);
  }

  public Class getClassType()
  {
    return Locale.class;
  }


  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Object[] getChoices()
  {
    return enumHelper.getAllowedValues();
  }

  public void setAllowedValues(Object[] choices)
  {
    enumHelper.setAllowedValues(choices);
  }

  public boolean isValid(Object value)
  {
    return enumHelper.isValid(value);
  }

  public boolean isRestrictionOf(Datatype value)
  {
    // TODO Auto-generated method stub
    return false;
  }

  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    Locale locale = null;
    if (value instanceof Locale)
    {
      // Only if the language is valid.
      // Keep the language, country and variant; Locale is immutable.
      String langCode =  ((Locale)value).getLanguage();
      if (langCode.length()!=0)
      {
        locale =  (Locale) value;
      } else
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Locale value does not contain any language specifier.");
        return null;
      }
    }
    
    if (value instanceof CharSequence)
    {
      CharSequence charSequence = (CharSequence) value;
      String langCode;
      StringBuffer buffer = new StringBuffer();
      for (int i=0; i < charSequence.length(); i++)
      {
        buffer.append(charSequence.charAt(i));
      }
      langCode = buffer.toString().toLowerCase();
      if (langCode.length() == 2)
      {
        locale =  new Locale(buffer.toString());
      } else
      if (langCode.equals(StandardFormatters.LocaleTypeConverter.X_DEFAULT_TAG))
      {
        locale =  StandardFormatters.LocaleTypeConverter.X_DEFAULT;
      } else
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Character sequence is not an ISO 639-1 language code or 'x-default'.");
        return null;
      }
    }
    if (locale != null)
    {
    if (isValid(locale) == false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"The Locale does not match the datatype specification");
      return null;
    }
    return locale;
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }

  public Object[] getAllowedValues()
  {
    return enumHelper.getAllowedValues();
  }

  public Class getAllowedValuesClass()
  {
    return enumHelper.getAllowedValuesClass();
  }


}
