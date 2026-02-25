package com.optimasc.datatypes;

import com.optimasc.datatypes.facets.LengthFacet;

public class LengthHelper implements LengthFacet
{
  protected long minLength;
  protected long maxLength;

  
  /** Constructs a length helper instance 
   *  with no length bounds.
   */
  public LengthHelper()
  {
    super();
    minLength = 0;
    maxLength = UNBOUNDED;
  }
  
  /** Constructs a length helper instance 
   *  with a lower bound and an unlimited upper bound.
   */
  public LengthHelper(long minLength)
  {
    super();
    if (minLength < 0)
    {
      throw new IllegalArgumentException("Length constraint must be a non-negative number but it was "+Long.toString(minLength));
    }
    this.minLength = minLength;
    maxLength = UNBOUNDED;
  }  

  public void setLength(long minValue, long maxValue)
  {
    if (minValue < 0)
    {
      throw new IllegalArgumentException("Length constraint must be a non-negative number but it was "+Long.toString(minValue));
    }
    minLength = minValue;
    if ((maxValue != UNBOUNDED) && (maxValue < minValue))
    {
      throw new IllegalArgumentException("Length constraint 'maxLength' is smaller in value then 'minLength'");
    }
    maxLength = maxValue;
  }

  public long getMinLength()
  {
    return minLength;
  }

  public long getMaxLength()
  {
    return maxLength;
  }

  public boolean isRestrictionOf(LengthFacet value)
  {
    if (value instanceof LengthFacet==false)
    {
      throw new IllegalArgumentException("Types are not compatible");
    }
    
    long thisMinLength = (minLength < 0)? 0: minLength;
    long otherMinLength = (value.getMinLength() < 0)? 0: value.getMinLength();
    
    long thisMaxLength = maxLength;
    long otherMaxLength = value.getMaxLength();
    
    if ((thisMaxLength == UNBOUNDED) && (otherMaxLength != UNBOUNDED))
    {
      return false;
    }
    
    if ((otherMaxLength == UNBOUNDED) && (thisMaxLength != UNBOUNDED))
    {
      return true;
    }
    if ((otherMaxLength == UNBOUNDED) && (thisMaxLength == UNBOUNDED))
    {
      return false;
    }
    
    
    long thisTotalLength = thisMaxLength - thisMinLength; 
    long otherTotalLength = otherMaxLength - otherMinLength; 
    
    if (thisTotalLength < otherTotalLength)
    {
      return true;
    }
    return false;
  }

  public boolean isBounded()
  {
    return true;
  }
  
  public boolean validateLength(long length)
  {
    if (length < minLength)
    {
      return false;
    }
    if ((maxLength != UNBOUNDED) && (length > maxLength))
    {
      return false;
    }
    return true;
  }

  /** Gives a string representation of the length constraint.
   *  An unbounded elements is represented as '*' character,
   *  as defined in ISO 11404:2007. 
   * 
   */
  public String toString()
  {
    String minLengthStr = Long.toString(minLength);
    String maxLengthStr = Long.toString(maxLength);
    if (minLength == UNBOUNDED)
    {
      minLengthStr = "*";
    }
    if (maxLength == UNBOUNDED)
    {
      maxLengthStr = "*";
    }
    if (minLength == maxLength)
    {
      return "size("+minLengthStr+")";
      
    }
    return "size("+minLengthStr + ".."+maxLengthStr+ ")";
  }

}
