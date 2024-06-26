package com.optimasc.datatypes.generated;

import java.util.regex.Pattern;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.derived.UCS2CharType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.datatypes.visitor.TypeVisitorEx;

/** Represents a String datatype that supports regular expressions
 *  as well as the CharSequence interface.
 * 
 * @author Carl Eric Codere
 *
 */
public class StringTypeEx extends StringType
{
  
  public StringTypeEx(TypeReference charType)
  {
    super(0,Integer.MAX_VALUE,charType);
  }
  
  public StringTypeEx(int minLength, int maxLength, TypeReference charType)
  {
    super(minLength,maxLength,charType);
  }
  
  public StringTypeEx()
  {
    super(0,Integer.MAX_VALUE,UCS2CharType.DEFAULT_TYPE_REFERENCE);
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
        DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,"The string does not match the pattern specification '"+pattern+"'");
      }
    }
  }
  
  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    if ((v instanceof TypeVisitorEx)==false)
      throw new IllegalArgumentException("Visitor must of type "+TypeVisitorEx.class.getName());
    return ((TypeVisitorEx)v).visit(this, arg);
  }
  

}
