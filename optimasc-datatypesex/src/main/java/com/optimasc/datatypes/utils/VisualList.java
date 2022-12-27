package com.optimasc.datatypes.utils;

import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * Represents a list, where each elements of the list is separated by a
 * separator character.
 * 
 * @author Carl Eric Codere
 * 
 * @param <E>
 */
public class VisualList<E> extends Vector<E>
{
  /** Default separator character to separate elements in the list. */
  protected static final String SEPARATOR_CHAR = ";";
  protected String separatorChar = SEPARATOR_CHAR;
  
  /**
   * 
   */
  private static final long serialVersionUID = 5033320633604035545L;
  
  
  public String getSeparatorChar()
  {
    return separatorChar;
  }

  public void setSeparatorChar(String separatorChar)
  {
    this.separatorChar = separatorChar;
  }

  public synchronized String toString()
  {
    Iterator<E> i = iterator();
    if (!i.hasNext())
      return "";

    StringBuilder sb = new StringBuilder();
    for (;;)
    {
      E e = i.next();
      sb.append(e == this ? "(this Collection)" : e);
      if (!i.hasNext())
        return sb.toString();
      sb.append(separatorChar);
    }
  }
  
  public static VisualList<String> parse(String list, String separatorChar)
  {
    VisualList<String> visualList = new VisualList<String>();
    StringTokenizer st = new StringTokenizer(list,separatorChar);
    while (st.hasMoreTokens()) 
    {
        visualList.add(st.nextToken());
    }
    return visualList;
  }

}
