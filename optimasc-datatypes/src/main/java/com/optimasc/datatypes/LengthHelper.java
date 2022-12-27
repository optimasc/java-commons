package com.optimasc.datatypes;

public class LengthHelper implements LengthFacet
{
  protected int minLength;
  protected int maxLength;
  
  public void setMinLength(int value)
  {
    if (value < 0)
    {
      throw new IllegalArgumentException("Length constraint must be a non-negative number but it was "+Integer.toString(value));
    }
    minLength = value;
  }

  public void setMaxLength(int value)
  {
    if (value < 0)
    {
      throw new IllegalArgumentException("Length constraint must be a non-negative number but it was "+Integer.toString(value));
    }
    maxLength = value;
  }

  public int getMinLength()
  {
    return minLength;
  }

  public int getMaxLength()
  {
    return maxLength;
  }

}
