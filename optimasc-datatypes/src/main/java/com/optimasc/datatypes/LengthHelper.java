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
    maxLength = Integer.MAX_VALUE;
  }

  public void setLength(int minValue, int maxValue)
  {
    if ((minValue < 0) && (minValue != Integer.MIN_VALUE))
    {
      throw new IllegalArgumentException("Length constraint must be a non-negative number but it was "+Integer.toString(minValue));
    }
    minLength = minValue;
    if (maxValue < minValue)
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

  public boolean isRestriction(Type value)
  {
    if (value instanceof LengthFacet==false)
    {
      throw new IllegalArgumentException("Types are not compatible");
    }
    LengthFacet otherLength = (LengthFacet) value;
    if (minLength > otherLength.getMinLength())
    {
      return true;
    }
    if (maxLength < otherLength.getMaxLength())
    {
      return true;
    }
    return false;
  }

  public boolean isBounded()
  {
    return (minLength != Integer.MIN_VALUE) && (maxLength != Integer.MIN_VALUE);
  }
  
  public boolean validateLength(long length)
  {
    if (isBounded()==false)
    {
      return true;
    }
    if (length < minLength)
    {
      return false;
    }
    if (length > maxLength)
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
