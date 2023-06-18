package com.optimasc.datatypes.derived;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitorEx;
import com.optimasc.datatypes.visitor.DefaultDatatypeVisitorEx;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a Unicode character codepoint, where full
 *  Unicode planes are supported.
 * 
 * @author Carl Eric Codere
 *
 */
public class UnicodeCharacter extends CharacterType
{
  protected Charset charSet = null; 
  protected CharsetEncoder charsetEncoder = null;
  
  

  public UnicodeCharacter()
  {
    super();
    setCharSetName("ISO-10646-UCS-4");
  }

  @Override
  public long getMinInclusive()
  {
    return Character.MIN_CODE_POINT;
  }

  @Override
  public long getMaxInclusive()
  {
    return Character.MAX_CODE_POINT;
  }

  @Override
  public void setCharSetName(String charSetName)
  {
    this.charSetName = charSetName;
    Charset cs = Charset.forName(charSetName);
    charsetEncoder = cs.newEncoder();
    charsetEncoder.onMalformedInput(CodingErrorAction.REPORT);
    charsetEncoder.onUnmappableCharacter(CodingErrorAction.REPORT);
  }
  
  /** Checks if this character is valid for this repertoire.
   * 
   * @param codePoint The character to validate represented as an UCS-4 character.
   * @return true if this character is valid or not.
   */
  @Override
  public boolean isValidCharacter(int codePoint)
  {
    try
    {
    CharBuffer cb = CharBuffer.allocate(1);
    char[] chars = Character.toChars(codePoint);
    for (int i = 0; i < chars.length; i++)
    {
      cb.append(chars[i]);
    }
    charsetEncoder.encode(cb);
    } catch (CharacterCodingException e)
    {
      return false;
    }
    return true;
  }

  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    if ((v instanceof TypeVisitorEx)==false)
      throw new IllegalArgumentException("Visitor must of type "+TypeVisitorEx.class.getName());
    return ((TypeVisitorEx)v).visit(this, arg);
  }


}
