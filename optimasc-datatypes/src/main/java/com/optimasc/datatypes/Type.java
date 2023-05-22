package com.optimasc.datatypes;

import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents the basic Type which conforms to OMG AMST Specification 1.0. 
 *  All types should be derived from this class.
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
   
   /** Returns the Java object type used to hold value data for
    *  this type for this implementation. 
    */
   public abstract Class getClassType();
   
   /** Create a new type, indicating if the value
    *  space contains elements which has an ordering.
    * 
    * @param ordered 
    */
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

   /** Return user specified user data associated 
    *  with this data.
    *  
    * @return user data.
    */
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
 
   /** Returns the comment associated with this datatype if any. */
   
   /** Returns the comment associated with this datatype. The
    *  comment returned should have been set by {@link #setComment(String)}.
    * 
    * @return The comment associated with this type, or null if
    *   none has been set.
    */
   public String getComment()
   {
     return comment;
   }

   /** Sets a comment associated with this datatype. */
   public void setComment(String comment)
   {
     this.comment = comment;
   }


  /** Return if this type's value space contains
   *  ordered values.
   * 
   * @return true if this type has oredering, otherwise
   *  false.
   */
  public boolean isOrdered()
  {
    return ordered;
  }

}
