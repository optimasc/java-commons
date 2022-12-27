/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.derived;

import java.text.ParseException;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.CharType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents an UCS-2 character.
 *
 * @author Carl Eric Codere
 */
public class UCS2CharType extends CharType {
  

    public UCS2CharType()
    {
      super();
      setRepertoireList("UTF-16BE");
    }

    public int getSize()
    {
        return 2;
    }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public boolean isValidCharacter(int c)
    {
      if ((c >= 0) && (c <= 0xFFFF))
      {
            return true;
      }
      return false;
    }


    
    
}
