package com.optimasc.util;

import java.text.ParsePosition;

import com.optimasc.lang.OrdinalSelecting;
import com.optimasc.lang.OrdinalSelecting.OrdinalAnyValue;
import com.optimasc.lang.OrdinalSelecting.OrdinalSelectRange;
import com.optimasc.lang.OrdinalSelecting.OrdinalSelectValue;
import com.optimasc.util.SetExpressionFormatter.SelectingExpression;

/** Implementation of a basic expression evaluation
 *  based on {@link com.optimasc.util.SetExpressionFormatter.SelectingExpression}.
 *  
 *  <p>To use this class the pattern compiler should fill up the {@link #patternItems}
 *  arrays containing for each character a <code>SelectingExpression</code>.</p>
 *
 */
public abstract class BasePattern extends Pattern
{
  protected SelectingExpression patternItems[];
  
  
  public BasePattern(String pattern)
  {
    super(pattern);
  }
  
  protected static boolean matchChars(SelectingExpression expr, int value, ParsePosition pos)
  {
    OrdinalSelecting selectItems[] = expr.get();
    for (int i=0; i < selectItems.length; i++)
    {
      OrdinalSelecting rawItem = selectItems[i];
      // Any value is allowed
      if (rawItem instanceof OrdinalAnyValue)
      {
        pos.setIndex(pos.getIndex()+1);
        return true;
      } 
      // An exact value
      if (rawItem instanceof OrdinalSelectValue)
      {
        OrdinalSelectValue item = (OrdinalSelectValue) rawItem;
        if (value == item.value)
        {
          if (expr.maxCount == 1)
          {
             pos.setIndex(pos.getIndex()+1);
             return true;
          }
        } else
        {
          continue;
        }
      } else
      // A range value
      {
        OrdinalSelectRange item = (OrdinalSelectRange) rawItem;
        
        // Compare the values.
        // this.minInclusive <= other.minInclusive AND 
        // this.maxInclusive >= other.maxInclusive
        if ((value >= item.minInclusive) && (value <= item.maxInclusive))
        {
          if (expr.maxCount == 1)
          {
             pos.setIndex(pos.getIndex()+1);
             return true;
          }
        } else
        {
          continue;
        }
      }
    }
    if (expr.minCount==0)
    {
      return true;
    }
    return false;
  }
  

    /** Attempts to match the entire region against the pattern. 
     * */ 
    public boolean matches(CharSequence text)
    {
      SelectingExpression patternValues[] = patternItems;
      ParsePosition pos = new ParsePosition(0);
      int n = text.length();
      int m = patternValues.length;
      int i = 0, j = 0, startIndex = -1, match = 0;
      // The pattern is an empty string!
      if (patternValues.length==0)
      {
        if (text.length()!=0)
          return false;
        return true;
      }
      SelectingExpression patterns = (SelectingExpression) patternValues[j];

      // While not reached end of text
      while (i < n)  
      {
          // While not reached end of pattern
          if (j < m)
            patterns = (SelectingExpression) patternValues[j];
          // If the current characters match or the
          // pattern has a ANY_CHAR, move to the next
          // characters in both pattern and text.
          pos = new ParsePosition(i);
          if ((j < m) && (patterns!=null) && matchChars(patterns, text.charAt(i),pos)
                  )
          {
              i = pos.getIndex();
              j++;
          }
          // If the pattern has a WILDCHAR character, mark the
          // current position in the pattern and the text
          // as a proper match.
          else if (j < m && (patterns==null)) {
              startIndex = j;
              match = i;
              j++;
          }
          // If we have not found any match and no WILDCHAR
          // character, backtrack to the last WILDCHAR
          // character position and try for a different
          // match.
          else if (startIndex != -1) 
          {
              j = startIndex + 1;
              match++;
              i = match;
          }
          // If none of the above cases comply, the
          // pattern does not match.
          else 
          {
              return false;
          }
      }

      // Consume any remaining WILDCHAR characters in the given
      // pattern.
      while ((j < m) && (patterns==null)) 
      {
          patterns = (SelectingExpression) patternValues[j]; 
          j++;
      }

      // If we have reached the end of both the pattern
      // and the text, the pattern matches the text.
      return j == m;
    }

}
