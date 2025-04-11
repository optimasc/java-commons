package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import com.optimasc.datatypes.Convertable;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.PrimitiveType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.text.Parsers;

/** Represents a language, which is represented by an ISO 649-1 2 letter
 *  code. It also supports the "x-default" value for language which will
 *  be equal to the default locale.
 * 
 *  <p>Internally the language is represented by the <code>Locale</code>
 *  java object.</p>
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
    return enumHelper.getChoices();
  }

  public void setChoices(Object[] choices)
  {
    enumHelper.setChoices(choices);
  }

  public boolean validateChoice(Object value)
  {
    return enumHelper.validateChoice(value);
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
      String langCode =  ((Locale)value).getLanguage();
      if (langCode.length()!=0)
      {
        locale =  new Locale(langCode);
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
      if (langCode.equals("x-default"))
      {
        locale =  Locale.getDefault();
      } else
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Character sequence is not an ISO 639-1 language code or 'x-default'.");
        return null;
      }
    }
    if (locale != null)
    {
    if (validateChoice(locale) == false)
    {
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"The Locale does not match the datatype specification");
      return null;
    }
    return locale;
    }
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
    return null;
  }


}
