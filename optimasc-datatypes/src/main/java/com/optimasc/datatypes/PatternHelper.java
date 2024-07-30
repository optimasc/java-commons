package com.optimasc.datatypes;

import java.util.Arrays;

import com.optimasc.util.Pattern;

public class PatternHelper implements PatternFacet
{
  protected Pattern patterns[];
  protected int minLength;
  protected int maxLength;
  
  public PatternHelper()
  {
    patterns = null;
  }
  
  public PatternHelper(Pattern patterns[])
  {
    this.patterns = patterns;
    // Get minLength and maxLength of all patterns
    minLength = getMinLength(patterns);
    maxLength = getMaxLength(patterns);
  }
  

  public Pattern[] getPatterns()
  {
    return patterns;
  }

  public boolean validatePatterns(CharSequence value)
  {
    if (patterns == null)
    {
      return true;
    }
    for (int i=0; i < patterns.length; i++)
    {
      if (patterns[i].matches(value)==true)
      {
        return true;
      }
    }
    return false;
  }

  

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    PatternHelper other = (PatternHelper) obj;
    if (!Arrays.equals(patterns, other.patterns))
      return false;
    return true;
  }

  public boolean isRestrictionOf(PatternFacet value)
  {
    int thisMinLength = (minLength < 0)? 0: minLength;
    int otherMinLength = getMinLength(value.getPatterns());
    
    int thisMaxLength = (maxLength < 0)? Integer.MAX_VALUE: maxLength;
    int otherMaxLength = getMaxLength(value.getPatterns());
    
    int thisTotalLength = thisMaxLength - thisMinLength; 
    int otherTotalLength = otherMaxLength - otherMinLength; 
    
    if (thisTotalLength < otherTotalLength)
    {
      return true;
    }
    return false;
  }
  
  
  /**
   * Return the minimum length of all the patterns..
   * 
   */
  private int getMinLength(Pattern choices[])
  {
    int minLength = Integer.MAX_VALUE;
    for (int i = 0; i < choices.length; i++)
    {
      int itemMinLength = choices[i].getMinLength();
      if (itemMinLength < minLength)
        minLength = itemMinLength;
    }
    return minLength;

  }

  /**
   * Return the maximum length of all the patterns..
   * 
   */
  private int getMaxLength(Pattern choices[])
  {
    int maxLength = 0;

    for (int i = 0; i < choices.length; i++)
    {
      int itemMaxLength = choices[i].getMaxLength();
      if (itemMaxLength > maxLength)
        maxLength = itemMaxLength;
    }
    return maxLength;
  }
  
  

}
