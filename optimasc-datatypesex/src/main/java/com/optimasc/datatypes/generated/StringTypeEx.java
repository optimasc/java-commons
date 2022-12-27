package com.optimasc.datatypes.generated;

import java.util.regex.Pattern;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.CharType;
import com.optimasc.datatypes.primitives.StringType;

/** Represents a String datatype that supports regular expressions
 *  as well as the CharSequence interface.
 * 
 * @author Carl Eric Codere
 *
 */
public class StringTypeEx extends StringType
{
  
  public StringTypeEx()
  {
    super();
  }

  @Override
  public void validatePattern(String value) throws DatatypeException
  {
    Pattern regexPattern;
    if (pattern != null)
    {
      regexPattern =Pattern.compile(pattern);
      if (regexPattern.matcher(value).matches()==false)
      {
        DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE,"The string does not match the pattern specification '"+pattern+"'");
      }
    }
  }

}
