package com.optimasc.util;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;

import com.optimasc.lang.OrdinalSelecting;
import com.optimasc.util.SetExpressionFormatter;
import com.optimasc.util.SetExpressionFormatter.SelectingExpression;;


/** Basic class for pattern matching algorithms. This
 *  class is a simplified and configured version of the 
 *  <code>java.util.regex</code> package for J2SE 1.4 and later, as it
 *  only supports the following pattern elements:
 *  
 *  <ul>
 *   <li>Match of any character using the <code>"."</code> pattern.</li>
 *   <li>Match of any combination of characters (wildcard) using the <code>".*"</code> pattern.</li>
 *   <li>Match a character from a set (bracketed expression), such as <code>"[A-Za]"</code>.</li>
 *   <li>Match the previous element one or more times (option) using the <code>"?"</code> postfix operator.</li>
 *  </ul>  
 * 
 * @author Carl Eric Codere
 *
 */
public class BasicRegexPattern extends BasePattern
{
      protected String anyChar = ".";
      protected String wildcard = ".*";
      protected int minLength;
      protected int maxLength;
  
      public BasicRegexPattern(String pattern) throws IllegalArgumentException
      {
        super(pattern);
        SetExpressionFormatter exprFormatter = new SetExpressionFormatter();
        this.pattern = pattern;
        List localPatternItems = new ArrayList/*<SelectingExpression>*/();
        // Compile the pattern.
        int i =0;
        while (i<pattern.length())
        {
          // Zero or one time -- Optional item.
          if (pattern.startsWith("?", i))
          {
            // Get previous expression and make it optional
            SelectingExpression expr = (SelectingExpression) localPatternItems.get(localPatternItems.size()-1);
            expr.minCount = 0;
            expr.maxCount = 1;
            i++;
          } else
/*          // One or several times
          if (c == '+')
          {
            // Get previous expression and make it optional
            SelectingExpression expr = (SelectingExpression) patternItems.get(i-1);
            expr.minCount = 1;
            expr.maxCount = Integer.MAX_VALUE;
            i++;
          } else
          // One or several times
          if (c == '*')
          {
            // Get previous expression and make it optional
            SelectingExpression expr = (SelectingExpression) patternItems.get(i-1);
            expr.minCount = 0;
            expr.maxCount = Integer.MAX_VALUE;
            i++;
          } else*/
          if (pattern.startsWith(wildcard, i))
            {
              localPatternItems.add(null);
              i = i + wildcard.length();
          } else
          if (pattern.startsWith(anyChar, i))
          {
            OrdinalSelecting items[] = new OrdinalSelecting[]{new OrdinalSelecting.OrdinalAnyValue()};
            SelectingExpression expr = new SelectingExpression(false,items);
            localPatternItems.add(expr);
            i = i + anyChar.length();
          } else
          if (pattern.startsWith("[", i))
          {
            ParsePosition pos = new ParsePosition(i);
            SelectingExpression expr = (SelectingExpression) exprFormatter.parseObject(pattern, pos);
            if (expr == null)
              throw new IllegalArgumentException("Bracket expression syntax error!");
            localPatternItems.add(expr);
            i = pos.getIndex();
          } else
          // Escape the next character  
          if (pattern.startsWith("\\", i))
          {
            OrdinalSelecting items[] = new OrdinalSelecting[]{new OrdinalSelecting.OrdinalSelectValue(pattern.charAt(i+1))};
            SelectingExpression expr = new SelectingExpression(false,items);
            localPatternItems.add(expr);
            i = i + 2;
          } else
          {
            OrdinalSelecting items[] = new OrdinalSelecting[]{new OrdinalSelecting.OrdinalSelectValue(pattern.charAt(i))};
            SelectingExpression expr = new SelectingExpression(false,items);
            localPatternItems.add(expr);
            i++;
          }
        }
        patternItems = (SelectingExpression[]) localPatternItems.toArray(new SelectingExpression[0]);
        calculateLengths(patternItems);
      }
      
      /** Calculate the minimum length and maximum length of 
       *  the string.
       * 
       * @param items [in] The pattern list
       */
      protected void calculateLengths(SelectingExpression[] items)
      {
        long minLength = 0;
        long maxLength = 0;
        for (int i=0; i < items.length; i++)
        {
           SelectingExpression item = items[i];
           // Wildcard! 
           if (item == null)
           {
             /* minLength = minLength + 0; */
             maxLength = Integer.MAX_VALUE;
           } else
           {
             minLength = minLength + item.minCount;
             maxLength = maxLength + item.maxCount;
           }
        }
        // Check for overflow
        if ((minLength < 0) || (minLength > Integer.MAX_VALUE))
        {
          minLength = Integer.MAX_VALUE;
        }
        if ((maxLength < 0) || (maxLength > Integer.MAX_VALUE))
        {
          maxLength = Integer.MAX_VALUE;
        }
        this.minLength = (int) minLength;
        this.maxLength = (int) maxLength;
        
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
