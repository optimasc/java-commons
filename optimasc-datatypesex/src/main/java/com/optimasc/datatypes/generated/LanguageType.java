package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.Locale;
import java.util.regex.Pattern;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class LanguageType extends Datatype implements EnumerationFacet, Parseable
{
  protected EnumerationHelper enumHelper;

  public LanguageType()
  {
    super(Datatype.OTHER,false);
    enumHelper = new EnumerationHelper(this);
  }

  @Override
  public Class getClassType()
  {
    return Locale.class;
  }


  @Override
  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    checkClass(value);
    if (validateChoice(value) == false)
    {
      DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
          "The Locale does not match the datatype specification");
    }
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

  public Object parseObject(String value) throws ParseException
  {
    /* Check if the pattern is valid. */
    Pattern pattern = Pattern
        .compile("(((en-GB-oed|i-ami|i-bnn|i-default|i-enochian|i-hak|i-klingon|i-lux|i-mingo|i-navajo|i-pwn|i-tao|i-tay|i-tsu|sgn-BE-FR|sgn-BE-NL|sgn-CH-DE)|(art-lojban|cel-gaulish|no-bok|no-nyn|zh-guoyu|zh-hakka|zh-min|zh-min-nan|zh-xiang))|((([A-Za-z]{2,3}(-([A-Za-z]{3}(-[A-Za-z]{3}){0,2}))?)|[A-Za-z]{4}|[A-Za-z]{5,8})(-([A-Za-z]{4}))?(-([A-Za-z]{2}|[0-9]{3}))?(-([A-Za-z0-9]{5,8}|[0-9][A-Za-z0-9]{3}))*(-([0-9A-WY-Za-wy-z](-[A-Za-z0-9]{2,8})+))*(-(x(-[A-Za-z0-9]{1,8})+))?)|(x(-[A-Za-z0-9]{1,8})+))");
    if (pattern.matcher(value).matches() == false)
    {
      throw new ParseException("Error parsing language code", 0);
    }
    Locale locale = new Locale(value);
    try
    {
      validate(locale);
    } catch (IllegalArgumentException e)
    {
      return new ParseException("Cannot parse language specifier.", 0);
    } catch (DatatypeException e)
    {
      return new ParseException("Cannot parse language specifier.", 0);
    }
    return locale;
  }

}
