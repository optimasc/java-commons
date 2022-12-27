package com.optimasc.utils;

import java.util.NoSuchElementException;

public class StringTokenizer
{
  String m_str;
  String m_delims;
  int m_length;
  int m_position;
  boolean m_delimiters = false;

  public StringTokenizer(String str, String delims)
  {
    m_delims = delims;
    m_str = str;
    m_length = str.length();
    m_position = 0;
    m_delimiters = false;
  }

  public StringTokenizer(String str, String delims, boolean returnDelims)
  {
    m_delims = delims;
    m_str = str;
    m_length = str.length();
    m_position = 0;
    m_delimiters = returnDelims;
  }

  public String nextToken()
  {
    eatDelimeters();
    int start = m_position;

    if ((m_position >= m_length))
        throw new NoSuchElementException("End of tokens reached.");

    if ((m_delimiters == true) && (m_delims.indexOf(m_str.charAt(m_position)) != -1))
    {
        m_position++;
    }
    else
    {
        while (m_position < m_length)
        {
            if (m_delims.indexOf(m_str.charAt(m_position)) == -1)
                 m_position++;
            else
              break;
        }
    }
    if (start == m_position)
        throw new NoSuchElementException("End of tokens reached.");
    return m_str.substring(start, m_position);
    // If we must return the delimiters and if this is was a delimiter
    // character then skip it for next run.
//    if ((m_delimiters == true) && (m_delims.indexOf(m_str.charAt(m_position)) != -1))
//        m_position++;
  }

  private void eatDelimeters()
  {
    while (m_position < m_length)
    {
      char c = m_str.charAt(m_position);
      if ((m_delims.indexOf(c) >= 0) && (m_delimiters==false))
      {
        m_position++;
      } else
      {
        return;
      }

    }
  }

  public boolean hasMoreTokens()
  {
    eatDelimeters();
    return (m_position < m_length);
  }

}
