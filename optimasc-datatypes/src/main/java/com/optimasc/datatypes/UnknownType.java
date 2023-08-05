/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.derived.ByteType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** This represents an unknown datatype that has not been resolved yet.
 *
 * @author Carl
 */
public class UnknownType extends Datatype 
{
  public static final UnknownType DEFAULT_INSTANCE = new UnknownType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  
   protected static final Object UNKNOWN_OBJECT = new Object();
   
   public UnknownType()
   {
        super(Datatype.NULL,false);
   }

    public Object accept(TypeVisitor v, Object arg)
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
