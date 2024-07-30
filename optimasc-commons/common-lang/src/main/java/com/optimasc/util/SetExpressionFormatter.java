package com.optimasc.util;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.optimasc.lang.OrdinalSelecting;
import com.optimasc.lang.OrdinalSelecting.OrdinalSelectRange;
import com.optimasc.lang.OrdinalSelecting.OrdinalSelectValue;

/** Parses a bracket expression, as defined in the Opengroup POSX 2004 
 *  specification (Basic Regular expression), hence it is a subset of the PERL expressions.
 *
 */
public class SetExpressionFormatter extends Format
{
   /** Represents a selecting expression, as used
    *  in bracketed expressions for one element value.
    */  
  public static class SelectingExpression
  {
    /** Matches all values NOT in the selection list. */
    public boolean invertSelection;
    protected OrdinalSelecting selectItems[];
    // Minimum number of repetitions
    protected int minCount;
    // Maximum number of repetitions
    protected int maxCount;
    
    public SelectingExpression(boolean invertSelection, OrdinalSelecting selectItems[])
    {
      super();
      this.invertSelection=invertSelection;
      this.selectItems=selectItems;
      this.minCount = 1;
      this.maxCount = 1;
    }
    
    public SelectingExpression(boolean invertSelection, OrdinalSelecting selectItems[], int minCount, int maxCount)
    {
      super();
      this.invertSelection=invertSelection;
      this.selectItems=selectItems;
      this.minCount = minCount;
      this.maxCount = maxCount;
    }
    
    public OrdinalSelecting[] get()
    {
      return selectItems; 
    }
    
    public OrdinalSelecting get(int index)
    {
      return selectItems[index]; 
    }
    
    
    public int size()
    {
      return selectItems.length;
    }
    
    
  }
  
  public static String printChar(char c)
  {
    if ((c >= 0x20) && (c <= 0x7E))
    {
      return "c";
    } else
    {
      throw new IllegalArgumentException("Unsupported character!"); 
    }
  }
  
  
  public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
  {
    if ((obj instanceof SelectingExpression) == false)
    {
      throw new IllegalArgumentException("Object value is not instance of '"
          + SelectingExpression.class.getName() + "'");
    }
    SelectingExpression select= (SelectingExpression) obj;
    toAppendTo.append('[');
    if (select.invertSelection)
    {
      toAppendTo.append('^');
    }
    OrdinalSelecting[] items = select.get();
    
    for (int i = 0; i < items.length; i++)
    {
      OrdinalSelecting item = items[i];
      if (item instanceof OrdinalSelectValue)
      {
        toAppendTo.append(printChar((char) ((OrdinalSelectValue)item).value));
      } else
      if (item instanceof OrdinalSelectRange)
      {
        toAppendTo.append(printChar((char) ((OrdinalSelectRange)item).minInclusive));
        toAppendTo.append('-');
        toAppendTo.append(printChar((char) ((OrdinalSelectRange)item).maxInclusive));
      } else
      {
        throw new IllegalArgumentException("Unsupported type in bracket expression.");
      }
    }
    toAppendTo.append(']');
    return toAppendTo;
  }

  /** Parses a bracket expression, as defined in the Opengroup POSX 2004 
   *  specification (Basic Regular expression), hence it is a subset of the PERL expressions.
   *  
   *  <p>An empty bracket expression will be considered an error conditions.</p> 
   * 
   * @param value [in] The bracket expression
   * @param pos [in] The position where to start the parsing
   * @return The list of expressions or <code>null</code> in case of error.
   */
  public Object parseObject(String source, ParsePosition pos)
  {
    OrdinalSelecting selectValue;
    boolean invert = true;
    pos.setErrorIndex(-1);
    int i=pos.getIndex();
    
    if (source.length()==0)
    {
      return null;
    }
    List selectingItems = new ArrayList();
    if (source.charAt(i)=='[')
    {
      i++; 
    }
    boolean firstChar = true;
    while (i < source.length())
    {
      int c = source.charAt(i);
      // If the first character is a ^ character invert the condition, 
      // 
      if (((firstChar)) && (c=='^'))
      {
        invert = true;
      }
      else
      // If the first character is a closing bracket, 
      // it is assumed to be a literal.
      if (((firstChar)) && (c==']'))
      {
        if (i == source.length()-1)
        {
          pos.setErrorIndex(i);
          return null;
        }
        selectValue = new OrdinalSelecting.OrdinalSelectValue(']');
        selectingItems.add(selectValue);
      }
      else
      // If the hyphen character is the first character
      // or last character in the brackets, it is 
      // considered a literal.
      if (((firstChar) || (i==source.length()-1)) && (c=='-'))
      {
        selectValue = new OrdinalSelecting.OrdinalSelectValue('-');
        selectingItems.add(selectValue);
      }
      else
      {
        firstChar = false;
        if (c == ']')
        {
          i++;
          pos.setIndex(i);
          return new SelectingExpression(invert,(OrdinalSelecting[]) selectingItems.toArray(new OrdinalSelecting[]{}));
        }
        if (source.charAt(i+1)=='-')
        {
          int endRange = source.charAt(i+2);
          selectValue = new OrdinalSelecting.OrdinalSelectRange(c,endRange);
          selectingItems.add(selectValue);
          i = i + 2;
        } else
        {
          selectValue = new OrdinalSelecting.OrdinalSelectValue(c);
          selectingItems.add(selectValue);
        }
      }
      i++;
    }
    pos.setIndex(i);
    return new SelectingExpression(invert,(OrdinalSelecting[]) selectingItems.toArray(new OrdinalSelecting[]{}));
  }

}
