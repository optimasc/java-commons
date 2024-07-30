package com.optimasc.datatypes;

public class LengthHelper implements LengthFacet
{
  protected int minLength;
  protected int maxLength;

  
  /** Constructs a length helper instance 
   *  with no length bounds.
   */
  public LengthHelper()
  {
    super();
    minLength = Integer.MIN_VALUE;
    maxLength = Integer.MIN_VALUE;
  }
  
  /** Constructs a length helper instance 
   *  with no length bounds.
   */
  public LengthHelper(int minLength)
  {
    super();
    if (minLength < 0)
    {
      throw new IllegalArgumentException("Length constraint must be a non-negative number but it was "+Integer.toString(minLength));
    }
    this.minLength = minLength;
    maxLength = Integer.MIN_VALUE;
  }  

  public void setLength(int minValue, int maxValue)
  {
    if (minValue < 0)
    {
      throw new IllegalArgumentException("Length constraint must be a non-negative number but it was "+Integer.toString(minValue));
    }
    minLength = minValue;
    if ((maxValue != Integer.MIN_VALUE) && (maxValue < minValue))
    {
      throw new IllegalArgumentException("Length constraint 'maxLength' is smaller in value then 'minLength'");
    }
    maxLength = maxValue;
  }

  public int getMinLength()
  {
    return minLength;
  }

  public int getMaxLength()
  {
    return maxLength;
  }

  public boolean isRestrictionOf(LengthFacet value)
  {
    if (value instanceof LengthFacet==false)
    {
      throw new IllegalArgumentException("Types are not compatible");
    }
    
    int thisMinLength = (minLength < 0)? 0: minLength;
    int otherMinLength = (value.getMinLength() < 0)? 0: value.getMinLength();
    
    int thisMaxLength = (maxLength < 0)? Integer.MAX_VALUE: maxLength;
    int otherMaxLength = (value.getMaxLength() < 0)? Integer.MAX_VALUE: value.getMaxLength();
    
    int thisTotalLength = thisMaxLength - thisMinLength; 
    int otherTotalLength = otherMaxLength - otherMinLength; 
    
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
    if ((maxLength != Integer.MIN_VALUE) && (length > maxLength))
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
    String minLengthStr = Integer.toString(minLength);
    String maxLengthStr = Integer.toString(maxLength);
    if (minLength == Integer.MIN_VALUE)
    {
      minLengthStr = "*";
    }
    if (maxLength == Integer.MIN_VALUE)
    {
      maxLengthStr = "*";
    }
    
    return "Size("+minLengthStr + ".."+maxLengthStr+ ")";
  }

  
}
