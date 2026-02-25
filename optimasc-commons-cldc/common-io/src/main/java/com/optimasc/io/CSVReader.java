package com.optimasc.io;

import java.util.Vector;

public class CSVReader
{
  
  /**
   * Parses a CSV line, handling quoted fields that may contain commas.
   * 
   * @param line
   *          the CSV line to parse
   * @param delimiter
   *          the delimiter character
   * @return array of field values as strings
   */
  public static String[] parseCSVLine(String line, String delimiter)
  {
    Vector/*<String>*/ values = new Vector/*<String>*/();
    StringBuffer currentValue = new StringBuffer();
    boolean inQuotes = false;

    for (int i = 0; i < line.length(); i++)
    {
      char c = line.charAt(i);

      if (c == '"')
      {
        // Handle quotes - either start/end of quoted field or escaped quote
        if (inQuotes && i < line.length() - 1 && line.charAt(i + 1) == '"')
        {
          // Escaped quote inside quoted field
          currentValue.append('"');
          i++; // Skip the next quote
        }
        else
        {
          // Toggle quote state
          inQuotes = !inQuotes;
        }
      }
      else if (c == delimiter.charAt(0) && !inQuotes)
      {
        // Found delimiter outside quotes - end of field
        values.addElement(currentValue.toString());
        currentValue = new StringBuffer();
      }
      else
      {
        // Regular character
        currentValue.append(c);
      }
    }

    // Add the last field
    values.addElement(currentValue.toString());
    
    String result[] = new String[values.size()];
    for (int i=0; i < values.size(); i++)
    {
      result[i] = (String) values.elementAt(i);
    }

    values.setSize(0);
    values = null;
    return result;
  }
  
  
}
