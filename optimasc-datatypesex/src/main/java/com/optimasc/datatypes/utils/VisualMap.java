package com.optimasc.datatypes.utils;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class VisualMap<K,V> extends LinkedHashMap<K, V>
{
  /** Default separator character to separate elements in the list. */
  protected static final String ENTRY_SEPARATOR_CHAR = ";";
  /** Default separator character to separate the key from the value in the list. */
  protected static final String KEY_SEPARATOR_CHAR = "=";
  protected String entrySeparatorChar = ENTRY_SEPARATOR_CHAR;
  protected String KeySeparatorChar = KEY_SEPARATOR_CHAR;
 
  
  public String getEntrySeparatorChar()
  {
    return entrySeparatorChar;
  }

  public void setEntrySeparatorChar(String separatorChar)
  {
    this.entrySeparatorChar = separatorChar;
  }
  
  
  public synchronized String toString()
  {
    Iterator<K> i = keySet().iterator();
    if (!i.hasNext())
      return "";

    StringBuilder sb = new StringBuilder();
    for (;;)
    {
      K e = i.next();
      Object value = get(e);
      sb.append(e == this ? "(this Collection)" : e+"="+value.toString());
      if (!i.hasNext())
        return sb.toString();
      sb.append(entrySeparatorChar);
    }
  }
  
  
  public static VisualMap<String,String> parse(String list, String keySeparatorChar, String entrySeparatorChar)
  {
    VisualMap<String,String> visualMap = new VisualMap<String, String>();
    StringTokenizer st = new StringTokenizer(list,entrySeparatorChar);
    while (st.hasMoreTokens()) 
    {
        String entryString = st.nextToken();
        String entry[] = entryString.split(keySeparatorChar); 
        visualMap.put(entry[0],entry[1]);
    }
    return visualMap;
  }

}
