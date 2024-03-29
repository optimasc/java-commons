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

/** Represents an UCS-2 character.
 *
 * @author Carl Eric Codere
 */
public class UCS2CharType extends CharacterType 
{
  public static final UCS2CharType DEFAULT_INSTANCE = new UCS2CharType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
    public UCS2CharType()
    {
      super();
      setCharSetName("ISO-10646-UCS-2");
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public boolean isValidCharacter(int c)
    {
      if ((c >= 0) && (c <= Character.MAX_VALUE))
      {
        // Surrogate values are not allowed.
        if (Character.getType((char)c)==Character.SURROGATE)
        {
          return false;
        }
        // Private values are not allowed.
        if (Character.getType((char)c)==Character.PRIVATE_USE)
        {
          return false;
        }
        return true;
      }
      return false;
    }

    public Class getClassType()
    {
      return Character.class;
    }

    public long getMinInclusive()
    {
      return 0;
    }

    public long getMaxInclusive()
    {
      return 0xFFFF;
    }
    
    
    
}
