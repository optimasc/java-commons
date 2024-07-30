package com.optimasc.util;

/** Represents an expression pattern.
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class Pattern
{
  protected String pattern;
  
  public Pattern(String pattern)
  {
    super();
    this.pattern = pattern;
  }
  
  /** Returns the expression from which this pattern was compiled from. */
  public String pattern()
  {
    return pattern;
  }
  
  /** Returns the minimum length of the string
   *  from the associated pattern.
   */
  public abstract int getMinLength();
  /** Returns the maximum length of the string
   *  from the associated pattern.
   */
  public abstract int getMaxLength();
  
  
  /** Creates a matcher that will match the given input against this pattern. */
  public abstract boolean matches(CharSequence input);

  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((pattern == null) ? 0 : pattern.hashCode());
    return result;
  }

  /** Patterns are equal if both are pattern
   *  values are the same and they are of the
   *  same instance class.
   * 
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Pattern other = (Pattern) obj;
    if (pattern == null)
    {
      if (other.pattern != null)
        return false;
    }
    else if (!pattern.equals(other.pattern))
      return false;
    return true;
  }
  
  
}


