package com.optimasc.datatypes.derived;

import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitorEx;
import com.optimasc.datatypes.visitor.DefaultDatatypeVisitorEx;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.CharacterSet;

/** Represents a Unicode character codepoint, where full
 *  Unicode planes are supported.
 * 
 * @author Carl Eric Codere
 *
 */
public class UnicodeCharType extends CharacterType
{
  public static final UnicodeCharType DEFAULT_INSTANCE = new UnicodeCharType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  protected Charset charSet = null; 
  protected CharsetEncoder charsetEncoder = null;
  
  

  public UnicodeCharType()
  {
    super();
    setCharacterSet(CharacterSet.UNICODE);
  }

  public long getMinInclusive()
  {
    return Character.MIN_CODE_POINT;
  }

  public long getMaxInclusive()
  {
    return Character.MAX_CODE_POINT;
  }

  public void setCharacterSet(String charSetName)
  {
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
