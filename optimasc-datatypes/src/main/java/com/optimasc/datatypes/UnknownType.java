/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes;

import java.text.ParseException;

import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** This represents an unknown datatype that has not been resolved yet.
 *
 * @author Carl
 */
public class UnknownType extends Datatype 
{
   protected static final Object UNKNOWN_OBJECT = new Object();
   
   public UnknownType()
   {
        super(Datatype.NULL);
   }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
        return null;
    }

    public int getSize()
    {
        return -1;
    }

    public void validate(Object integerValue) throws IllegalArgumentException, DatatypeException
    {
    }

    /** The integer types are equal only and only if
     *  they have the same range and have the same datatype name.
    */
    public boolean equals(Object obj)
    {
        UnknownType anonType;
        if (!(obj instanceof UnknownType))
        {
            return false;
        }
        anonType = (UnknownType)obj;
        if (this.name.equals(anonType.getName())==false)
        {
            return false;
        }
        return true;
    }

    public Object getObjectType()
    {
      return UNKNOWN_OBJECT;
    }

    public Object parse(String value) throws ParseException
    {
      throw new UnsupportedOperationException("Parse method is not implemented.");
    }

    

}
