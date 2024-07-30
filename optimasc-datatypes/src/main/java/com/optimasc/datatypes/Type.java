package com.optimasc.datatypes;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.util.Pattern;
import com.optimasc.utils.UserConfiguration;

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

  /**
   * Indicates if this is an ordered value, that means to say that each value
   * has a predecessor or successor
   */
  protected boolean ordered;

  /**
   * Returns the Java object type used to hold value data for this type for this
   * implementation. If there is no java datatype representing this type,
   * this shall return a <code>null</code> value.
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
   * @return true if this type has ordering, otherwise false.
   */
  public boolean isOrdered()
  {
    return ordered;
  }
  
  /**
   * Return if this type's value space are conceptually
   * quantities (in some mathematical number system). 
   * The default implementation returns <code>false</code>.
   * 
   * @return true if this type's value space represents
   *  a number.
   */
   public boolean isNumeric()
   {
     return false;
   }
  

  /**
   * Generic implementation of equals that verifies basic facets for equalities.
   * It may need to be overriden for specific types.
   * 
   */
  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }
    if (obj instanceof Type)
    {
      Type otherType = (Type) obj;
      if (isOrdered()!=otherType.isOrdered())
      {
        return false;
      }
      if (isNumeric()!=otherType.isNumeric())
      {
        return false;
      }
      
      
      
      
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
        
        Pattern thesePatterns[] = thisObject.getPatterns();
        Pattern otherPatterns[] = otherObject.getPatterns();
        if ((thesePatterns == null) && (otherPatterns != null))
        {
          return false;
        }
        if ((thesePatterns != null) && (otherPatterns == null))
        {
          return false;
        }
        if ((thesePatterns != null) && (otherPatterns != null))
        {
          if (Arrays.equals(thesePatterns, otherPatterns)==false)
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
        if (otherObject.getCharacterSet().equals(thisObject.getCharacterSet()) == false)
        {
          return false;
        }
      }
      
      
      if (this instanceof DecimalRangeFacet)
      {
        DecimalRangeFacet thisObject = (DecimalRangeFacet) this;
        if ((obj instanceof DecimalRangeFacet) == false)
        {
          return false;
        }
        DecimalRangeFacet otherObject = (DecimalRangeFacet) obj;
        
        DecimalRangeFacet other = (DecimalRangeFacet) obj;
        BigDecimal maxInclusive = thisObject.getMaxInclusive();
        BigDecimal minInclusive = thisObject.getMinInclusive();
        if (maxInclusive == null)
        {
          if (other.getMaxInclusive() != null)
            return false;
        }
        // We check that the scales are equal here!
        else if (!maxInclusive.equals(other.getMaxInclusive()))
          return false;
        if (minInclusive == null)
        {
          if (other.getMinInclusive() != null)
            return false;
        }
        // We check that the scales are equal here!
        else if (!minInclusive.equals(other.getMinInclusive()))
          return false;
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
