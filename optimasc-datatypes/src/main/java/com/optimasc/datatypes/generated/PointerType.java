/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.generated;

import java.text.ParseException;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** This datatype represents a pointer to another object.
 *
 * @author Carl Eric Codere
 */
public class PointerType extends Datatype implements ConstructedSimple {

   protected Datatype pointerTo;

   public PointerType()
   {
        super(Datatype.REF);
   }

    public Class getClassType()
    {
        return null;
    }

    public int getSize()
    {
        return 0;
    }

    public void validate(Object integerValue) throws IllegalArgumentException, DatatypeException
    {
        
    }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Datatype getElementType()
    {
      return pointerTo;
    }

    public void setElementType(Datatype value)
    {
      pointerTo = value;
    }

    public Object getObjectType()
    {
      // TODO Auto-generated method stub
      return null;
    }

    public Object parse(String value) throws ParseException
    {
      throw new UnsupportedOperationException("Parse method is not implemented.");
    }
    
    
}
