package com.optimasc.datatypes.generated;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.util.regex.Pattern;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.EnumerationHelper;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

public class URIType extends Datatype implements LengthFacet, EnumerationFacet, PatternFacet
{
  protected static final String SAMPLE_URI = "http://www.example.com/";
  protected String pattern;
  protected EnumerationHelper enumHelper;
  protected LengthHelper lengthHelper;
  
  
  public URIType()
  {
    super(Datatype.OTHER);
    enumHelper = new EnumerationHelper(this);
    lengthHelper = new LengthHelper();
    setMinLength(0);
    /* Known limitation of IE as of 2017
     * See https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers 
     * */
    setMaxLength(2000);
  }

  @Override
  public Object[] getChoices()
  {
    return enumHelper.getChoices();
  }

  @Override
  public void setChoices(Object[] choices)
  {
    enumHelper.setChoices(choices);
  }

  @Override
  public boolean validateChoice(Object value)
  {
    return enumHelper.validateChoice(value);
  }

  @Override
  public void setMinLength(int value)
  {
    lengthHelper.setMinLength(value);
  }

  @Override
  public void setMaxLength(int value)
  {
    lengthHelper.setMaxLength(value);
  }

  @Override
  public int getMinLength()
  {
    return lengthHelper.getMinLength();
  }

  @Override
  public int getMaxLength()
  {
    return lengthHelper.getMaxLength();
  }

  @Override
  public int getSize()
  {
    return -1;
  }

  @Override
  public Class getClassType()
  {
    return URI.class;
  }

  @Override
  public Object getObjectType()
  {
    try
    {
      return new URI(SAMPLE_URI);
    } catch (URISyntaxException e)
    {
      return null;
    }
  }

  @Override
  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    checkClass(value);
    if (value instanceof URI)
    {
      URI uri = (URI)value;
      if ((uri.toString().length() < getMinLength()) || (uri.toString().length() > getMaxLength()))
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The URI does not match the datatype specification");
      }
      validatePattern(uri);
      if (validateChoice(uri)==false)
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The URI does not match the datatype specification");
      }
      return;
    }
  }

  @Override
  public Object accept(DatatypeVisitor v, Object arg)
  {
    return null;
  }

  @Override
  public String getPattern()
  {
    return pattern;
  }

  @Override
  public void setPattern(String value)
  {
    pattern = value;
  }
  
  protected void validatePattern(URI uri) throws DatatypeException
  {
    Pattern regexPattern;
    if (pattern != null)
    {
      regexPattern =Pattern.compile(pattern);
      if (regexPattern.matcher(uri.toString()).matches()==false)
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The URI does not match the pattern specification '"+pattern+"'");
      }
    }
  }

  @Override
  public Object parse(String value) throws ParseException
  {
    URI uri = null;
    try
    {
      uri = new URI(value);
      validate(uri);
      return uri;
    } catch (URISyntaxException e)
    {
      throw new ParseException("Error parsing URI",0);
    }
    catch (DatatypeException e)
    {
      throw new ParseException("Error parsing URI",0);
    }    
  }
  
  
  
}
