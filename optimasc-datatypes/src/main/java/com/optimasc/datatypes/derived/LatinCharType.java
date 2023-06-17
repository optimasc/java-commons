/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * 
 * @author Carl Eric Codere
 */
public class LatinCharType extends CharacterType
{
  
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new LatinCharType());
  
  public LatinCharType()
  {
    super();
    setCharSetName("ISO-8859-1");
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  public boolean isValidCharacter(int c)
  {
    if ((c >= 0) && (c <= 0xFF))
    {
          return true;
    }
    return false;
  }

  public long getMinInclusive()
  {
    return 0;
  }

  public long getMaxInclusive()
  {
    return 255;
  }

}
