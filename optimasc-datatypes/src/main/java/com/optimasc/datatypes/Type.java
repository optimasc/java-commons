package com.optimasc.datatypes;

import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents the basic Type which conforms 
 *  to OMG AMST Specification 1.0. All types
 *  should be derived from this class.
 * 
 * @author Carl Eric Codère
 *
 */
public abstract class Type
{
    /** User-specific data associated with this datatype */
   protected Object userData;
   
   protected int modifiers;
   
   protected String comment;
  
   protected boolean isConst;
   
   /** Indicates if this is an ordered value, that means to say
    *  that each value has a predecessor or successor */
   protected boolean ordered;
   
   /** Returns the Java object type used to hold data for
    *  this type. 
    */
   public abstract Class getClassType();
   
   public Type(boolean ordered)
   {
     this.ordered = ordered;
   }
   
   
   /**
    * Accept method for visitor support.
    * 
    * @param v
    *          the visitor implementation
    * @param arg
    *          any value relevant for the visitor
    * @return the result of the visit
    */
   public abstract Object accept(TypeVisitor v, Object arg);
   
   protected void checkClass(Object obj) throws IllegalArgumentException
   {
     if (getClassType().isInstance(obj)==false)
     {
       throw new IllegalArgumentException("Invalid class : got '"+obj.getClass().getName()+"' expected '"+getClassType().getName()+"'");
     }
   }
   
   public Object getUserData()
   {
     return userData;
   }

   public void setUserData(Object userData)
   {
     this.userData = userData;
   }

   public int getModifiers()
   {
     return modifiers;
   }

   public void setModifiers(int modifiers)
   {
     this.modifiers = modifiers;
   }
 
   /** Returns the name associated with this datatype */
   public String getComment()
   {
     return comment;
   }

   public void setComment(String comment)
   {
     this.comment = comment;
   }


  public boolean isOrdered()
  {
    return ordered;
  }

}
