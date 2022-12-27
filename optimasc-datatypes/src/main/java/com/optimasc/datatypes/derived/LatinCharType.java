/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.derived;

import java.text.ParseException;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.CharType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/**
 * 
 * @author Carl Eric Codere
 */
public class LatinCharType extends CharType
{

  public LatinCharType()
  {
    super();
    setRepertoireList("ISO-8859-1");
  }

  public int getSize()
  {
    return 1;
  }

  public Object accept(DatatypeVisitor v, Object arg)
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
  

}
