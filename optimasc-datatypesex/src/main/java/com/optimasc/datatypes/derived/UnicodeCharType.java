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
    super(CharacterSet.UNICODE);
  }

  public void setCharacterSet(String charSetName)
  {
    Charset cs = Charset.forName(charSetName);
    charsetEncoder = cs.newEncoder();
    charsetEncoder.onMalformedInput(CodingErrorAction.REPORT);
    charsetEncoder.onUnmappableCharacter(CodingErrorAction.REPORT);
  }
  

  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    if ((v instanceof TypeVisitorEx)==false)
      throw new IllegalArgumentException("Visitor must of type "+TypeVisitorEx.class.getName());
    return ((TypeVisitorEx)v).visit(this, arg);
  }


}
