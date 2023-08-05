package com.optimasc.datatypes;

import java.util.HashMap;
import java.util.Map;

import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Represents the basic Type which conforms to OMG AMST Specification 1.0. All
 * types should be derived from this class.
 * 
 * @author Carl Eric Codère
 *
 */
public abstract class Type implements UserConfiguration
{
  /** User-specific data associated with this datatype */
  protected Map userData;

  protected String comment;

  protected boolean isConst;

  /**
   * Indicates if this is an ordered value, that means to say that each value
   * has a predecessor or successor
   */
  protected boolean ordered;

  /**
   * Returns the Java object type used to hold value data for this type for this
   * implementation.
   */
  public abstract Class getClassType();

  /**
   * Create a new type, indicating if the value space contains elements which
   * has an ordering.
   * 
   * @param ordered
   */
  public Type(boolean ordered)
  {
    this.ordered = ordered;
    this.userData = new HashMap();
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
    if (getClassType().isInstance(obj) == false)
    {
      throw new IllegalArgumentException("Invalid class : got '"
          + obj.getClass().getName() + "' expected '" + getClassType().getName() + "'");
    }
  }

  public Object getUserData(String key)
  {
    return userData.get(key);
  }

  public Object setUserData(String key, Object data)
  {
    return userData.put(key, data);
  }

  /** Returns the comment associated with this datatype if any. */

  /**
   * Returns the comment associated with this datatype. The comment returned
   * should have been set by {@link #setComment(String)}.
   * 
   * @return The comment associated with this type, or null if none has been
   *         set.
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

  /**
   * Return if this type's value space contains ordered values.
   * 
   * @return true if this type has oredering, otherwise false.
   */
  public boolean isOrdered()
  {
    return ordered;
  }

  /**
   * Generic implementation of equals that verifies basic facets for equalities.
   * It may need to be overriden for specific types.
   * 
   */
  public boolean equals(Object obj)
  {
    if (obj == this)
      return true;
    if (obj instanceof Type)
    {
      if (this instanceof ConstructedSimple)
      {
        ConstructedSimple thisObject = (ConstructedSimple) this;
        if ((obj instanceof ConstructedSimple) == false)
        {
          return false;
        }
        ConstructedSimple otherObject = (ConstructedSimple) obj;
        if (otherObject.getBaseTypeReference().equals(thisObject.getBaseTypeReference()) == false)
        {
          return false;
        }
      }

      if (this instanceof LengthFacet)
      {
        LengthFacet thisObject = (LengthFacet) this;
        if ((obj instanceof LengthFacet) == false)
        {
          return false;
        }
        LengthFacet otherObject = (LengthFacet) obj;
        if (otherObject.getMinLength() != thisObject.getMinLength())
        {
          return false;
        }
        if (otherObject.getMaxLength() != thisObject.getMaxLength())
        {
          return false;
        }
      }

      if (this instanceof PatternFacet)
      {
        PatternFacet thisObject = (PatternFacet) this;
        if ((obj instanceof PatternFacet) == false)
        {
          return false;
        }
        PatternFacet otherObject = (PatternFacet) obj;
        if (otherObject.getPattern().equals(thisObject.getPattern()) == false)
        {
          return false;
        }
      }

      if (this instanceof CharacterSetEncodingFacet)
      {
        CharacterSetEncodingFacet thisObject = (CharacterSetEncodingFacet) this;
        if ((obj instanceof CharacterSetEncodingFacet) == false)
        {
          return false;
        }
        CharacterSetEncodingFacet otherObject = (CharacterSetEncodingFacet) obj;
        if (otherObject.getCharSetName().equals(thisObject.getCharSetName()) == false)
        {
          return false;
        }
      }

      if (this instanceof EnumerationFacet)
      {
        EnumerationFacet thisObject = (EnumerationFacet) this;
        if ((obj instanceof EnumerationFacet) == false)
        {
          return false;
        }
        EnumerationFacet otherObject = (EnumerationFacet) obj;
        Object[] otherChoices = otherObject.getChoices();
        Object[] thisChoices = thisObject.getChoices();

        if (otherChoices.length != thisChoices.length)
        {
          return false;
        }
        for (int i = 0; i < otherChoices.length; i++)
        {
          if (otherChoices[i].equals(thisChoices[i]) == false)
            return false;
        }
      }
    }
    return true;
  }

}
