package com.optimasc.text;

import java.math.BigInteger;
import java.text.ParseException;
import java.text.ParsePosition;

public class Parsers
{
  
  /** Used for numeric converter <code>parseNumber</code>. */
  private static final long[] POWERS_OF_TEN = {
      1L,
     10L,
    100L,
   1000L,
  10000L,
 100000L,
1000000L,
10000000L,
100000000L,
1000000000L,
10000000000000L,
100000000000000L,
10000000000000000L,
100000000000000000L,
1000000000000000000L};    
  
  // Do not instantiate
  protected Parsers()
  {
    
  }

  /** Tries to parse a numeric integer value into a long value. The
   *  parsing starts at index <code>startPos</code> and ends at
   *  <code>endPos - 1</code>. There are also configuration parameters
   *  permit to determine if the + and - sign are allowed and 
   *  if leading zeros are allowed.
   * 
   * @param value [in] The value to parse
   * @param startPos [in] Index where parsing must start.
   * @param endPos [in]  Index of the character following the last character that need to be parsed.
   * @param allowPlusSign [in] Can the first character be a '+' character.
   * @param allowMinusSign [in] Can the first character be a '-' character.
   * @param leadingZero [in] Are leading zero's allowed
   * @return The converted numeric value.
   * @throws ParseException If there is an error and parsing cannot
   *   continue.
   */
  public static long parseNumber(CharSequence value, int startPos, int endPos, boolean allowPlusSign, boolean allowMinusSign, boolean leadingZero) throws ParseException
  {
      boolean negative;
      char c;
      // Remember endPos is the character following the last character to parse
      int length = endPos-startPos;
      // This is the actual number of indexes to go through
      if (length <= 0)
      {
        throw new ParseException(
            "The string length must least 1 character.",0);
      }
      long finalValue = 0;
      // Get first character
      if (value.charAt(startPos)=='+')
      {
        if (allowPlusSign == false)
        {
          throw new ParseException("Numeric value cannot start with a '+' character.",0);
        }
        negative = false;
        // Skip sign when parsing number
        startPos++;
      } else
      if (value.charAt(startPos)=='-')
      {
        if (allowMinusSign == false)
        {
          throw new ParseException("Numeric value cannot start with a '-' character.",0);
        }
        negative = true;
        // Skip sign when parsing number
        startPos++;
      } else
      {
        negative = false;
      }

      // New length excluding start characters + and -
      length = endPos-startPos;
      if ((value.charAt(startPos)=='0') && (length > 1) && (leadingZero==false))
      {
        throw new ParseException(
            "Numeric value cannot start with leading '0' digits.",0);
      }
      long multiplier = POWERS_OF_TEN[length-1];
      for (int i=startPos; i < endPos; i++)
      {
        c = value.charAt(i);
        if ((c < '0') || (c > '9'))
        {
          throw new ParseException(
              "Numeric value contains non-digit values.",i);
        }
        finalValue = finalValue + ((c - 0x30)*multiplier);
        multiplier = multiplier / 10;
      }
      if (negative)
      {
        finalValue = -finalValue;
      }
      return finalValue;  
  }
  
  
  /** Tries to parse a numeric integer value into a long value. The
   *  parsing starts at index <code>startPos</code> and ends at
   *  <code>endPos - 1</code>. There are also configuration parameters
   *  permit to determine if the + and - sign are allowed and 
   *  if leading zeros are allowed.
   * 
   * @param value [in] The value to parse
   * @param startPos [in] Index where parsing must start.
   * @param endPos [in]  Index of the character following the last character that need to be parsed.
   * @param allowPlusSign [in] Can the first character be a '+' character.
   * @param allowMinusSign [in] Can the first character be a '-' character.
   * @param leadingZero [in] Are leading zero's allowed
   * @return The converted numeric value.
   * @throws ParseException If there is an error and parsing cannot
   *   continue.
   */
  public static BigInteger parseNumberAsBigInteger(CharSequence value, int startPos, int endPos, boolean allowPlusSign, boolean allowMinusSign, boolean leadingZero) throws ParseException
  {
      boolean negative;
      char c;
      // Remember endPos is the character following the last character to parse
      int length = endPos-startPos;
      // This is the actual number of indexes to go through
      if (length <= 0)
      {
        throw new ParseException(
            "The string length must least 1 character.",0);
      }
      long finalValue = 0;
      // Get first character
      if (value.charAt(startPos)=='+')
      {
        if (allowPlusSign == false)
        {
          throw new ParseException("Numeric value cannot start with a '+' character.",0);
        }
        negative = false;
        // Skip sign when parsing number
        startPos++;
      } else
      if (value.charAt(startPos)=='-')
      {
        if (allowMinusSign == false)
        {
          throw new ParseException("Numeric value cannot start with a '-' character.",0);
        }
        negative = true;
        // Skip sign when parsing number
        startPos++;
      } else
      {
        negative = false;
      }

      // New length excluding start characters + and -
      length = endPos-startPos;
      if ((value.charAt(startPos)=='0') && (length > 1) && (leadingZero==false))
      {
        throw new ParseException(
            "Numeric value cannot start with leading '0' digits.",0);
      }
      
      CharSequence numbers = value.subSequence(startPos, endPos);
      BigInteger number = null;
      try
      {
         number = new BigInteger(numbers.toString());
      } catch (NumberFormatException e)
      {
        throw new ParseException(
            e.getMessage(),startPos);
      }
      if (negative)
      {
        number = number.negate();
      }
      return number;
  }
  

  /** Parses a numeric value that has the specified number of digits, left padded 
   *  with zero.  
   * 
   * @param value [in] The value to parse.
   * @param pos [in,out] On entry the parse position to start at, and 
   *   on exit, the last parse position 
   * @param minDigits [in] The minimum number of digits. Minimum value is 1
   * @param maxDigits [in] The maximum number of digits.
   * @return The numeric value 
   * @throws ParseException The parsing error information, in certain
   *   cases {@link ParseException#getErrorOffset()} may return {@link ParsePosition#getIndex()} if
   *   the issue is on the full string (like minimum length is not matched), and not on a specific character at a specified index.
   * @throws IllegalArgumentException if minDigits is greater than maxDigits or if either are negative or maxDigits
   *   is zero.
   */
  public static int parsePositiveNumber(CharSequence value, ParsePosition pos, int minDigits, int maxDigits) throws ParseException
  {
    int startPos = pos.getIndex();
    int currentLength = value.length()-startPos;
    int finalValue;
    char c;
    
    if ((minDigits < 1) || (maxDigits < 0) || (minDigits > maxDigits))
    {
      throw new IllegalArgumentException("minDigits and maxDigits values are invalid.");
    }
    
    // This is the actual number of indexes to go through
    if (currentLength < minDigits)
    {
      throw new ParseException(
          "The string length must least contain "+minDigits+" digits.",pos.getIndex());
    }
    // Required values.
    for (int i=0; i < minDigits; i++)
    {
      c = value.charAt(startPos);
      if ((c < '0') || (c > '9'))
      {
        pos.setErrorIndex(startPos);
        throw new ParseException("Numeric value contains non-digit values.",pos.getErrorIndex());
      }
      startPos++;
    }
    // Optional values, go to max Length or maxDigits
    int maxLength = Math.min(maxDigits, value.length()-startPos+1);
    for (int i=minDigits; i < maxLength; i++)
    {
      c = value.charAt(startPos);
      if ((c < '0') || (c > '9'))
      {
        break;
      }
      startPos++;
    }
    String strValue = value.subSequence(pos.getIndex(),startPos).toString();
    pos.setIndex(startPos);
    finalValue = Integer.parseInt(strValue);
    return finalValue;
  }
  
  
  /** Parses a gYear with the defined allowed range of values. A year
   *  is composed of at least 4 digits representing a Gregorian 
   *  Calendar year. Depending on the configuration, it permits
   *  to have extended precision beyond 4 digits, with either
   *  a required '+' for extended precision. It also permits
   *  to allow negative numbers. 
   * 
   * @param value [in] 
   * @param pos [in,out] On entry the position to start parsing from,
   *   and on output, the last position that was parsed.
   * @param requirePlus [in] Requires a '+' character if the value
   *   is a positive number.
   * @param allowNegative [in] Allow negative numbers with a preceding
   *  '-' character  sign.
   * @param minValue [in] The minimum allowed value for the year.
   * @param maxValue [in] The maximum allowed value for the year.
   * @return The years as an integer.
   * @throws ParseException If the rules for above syntax are not followed
   */
  public static int parseYear(CharSequence value, ParsePosition pos, boolean requirePlus, boolean allowNegative, int minValue, int maxValue) throws ParseException
  {
    boolean negative = false;
    int finalValue;
    int startPos = pos.getIndex();
    
    int lenMinValue = Integer.toString(Math.abs(minValue)).length();
    int lenMaxValue = Integer.toString(Math.abs(maxValue)).length();
    
    int minDigits = Math.max(4,lenMinValue);
    int maxDigits = Math.max(4,lenMaxValue);
    char c;
    // This is the actual number of indexes to go through
    if (value.length() <= 0)
    {
      throw new ParseException(
          "The string length must least 1 character.",0);
    }
    c = value.charAt(0);
    if (c == '+')
    {
      if (requirePlus==false)
      {
        pos.setErrorIndex(startPos);
        throw new ParseException("Numeric value cannot start with a '-' character.",pos.getErrorIndex());
      }
      startPos++;
    }
    else
    if (c == '-')
    {
      if (allowNegative == false)
      {
        pos.setErrorIndex(startPos);
        throw new ParseException("Numeric value cannot start with a '-' character.",pos.getErrorIndex());
      }
      negative = true;
      startPos++;
    }

    pos.setIndex(startPos);
    finalValue = parsePositiveNumber(value,pos,minDigits,maxDigits);
    if (negative)
    {
      finalValue = -finalValue;
    }
    if ((finalValue < minValue) || (finalValue > maxValue))
    {
      throw new ParseException("Year is beyond allowed limits: "+minValue+" and "+maxValue,startPos);
    }
    return finalValue;
  }
  
  public static int indexOf(CharSequence input, char c, int fromIndex)
  {
    int inLength = input.length();
    if (fromIndex < inLength)
    {
      if (fromIndex < 0)
      {
        fromIndex = 0;
      }
      if (c >= 0 && c <= Character.MAX_VALUE)
      {
        for (int i = fromIndex; i < inLength; i++)
        {
          if (input.charAt(i) == c)
          {
            return i;
          }
        }
      }
    }
    return -1;
  }

  public static CharSequence trim(CharSequence in)
  {
    int count = in.length();
    int start = 0, last = count - 1;
    int end = last;
    while ((start <= end) && (in.charAt(start) <= ' '))
    {
      start++;
    }
    while ((end >= start) && (in.charAt(end) <= ' '))
    {
      end--;
    }
    if (start == 0 && end == last)
    {
      return in;
    }
    return in.subSequence(start, end + 1);
  }

  public static CharSequence trimChar(CharSequence in, char c)
  {
    int count = in.length();
    int start = 0, last = count - 1;
    int end = last;
    while ((start <= end) && (in.charAt(start) == c))
    {
      start++;
    }
    while ((end >= start) && (in.charAt(end) == c))
    {
      end--;
    }
    if (start == 0 && end == last)
    {
      return in;
    }
    return in.subSequence(start, end + 1);
  }

  public static int indexOfIgnoreCase(CharSequence input, char c, int fromIndex)
  {
    int inLength = input.length();
    c = Character.toUpperCase(c);
    if (fromIndex < inLength)
    {
      if (fromIndex < 0)
      {
        fromIndex = 0;
      }
      if (c >= 0 && c <= Character.MAX_VALUE)
      {
        for (int i = fromIndex; i < inLength; i++)
        {
          if (Character.toUpperCase(input.charAt(i)) == c)
          {
            return i;
          }
        }
      }
    }
    return -1;
  }

  /**
   * Search a sequence of characters in another sequence of characters and
   * returns the character index where it is found or -1 if not found.
   * 
   * @param input
   *          [in] The character sequence to search in.
   * @param toMatch
   *          [in] The character sequence to search for.
   * @param fromIndex
   *          [in] Start index in input to start search for.
   * @return The position where the first match was not found or -1 if not
   *         found.
   */
  public static int indexOf(CharSequence input, CharSequence subString, int fromIndex)
  {
    int inLength = input.length();
    if (fromIndex < 0)
    {
      fromIndex = 0;
    }
    int subCount = subString.length();
    if (subCount > 0)
    {
      if (subCount + fromIndex > inLength)
      {
        return -1;
      }
      int subOffset = 0;
      char firstChar = subString.charAt(subOffset);
      int end = subOffset + subCount;
      while (true)
      {
        int i = Parsers.indexOf(input, firstChar, fromIndex);
        if (i == -1 || subCount + i > inLength)
        {
          return -1; // handles subCount > count || start >= count
        }
        int o1 = 0 + i, o2 = subOffset;
        while (++o2 < end && input.charAt(++o1) == subString.charAt(o2))
        {
          // Intentionally empty
        }
        if (o2 == end)
        {
          return i;
        }
        fromIndex = i + 1;
      }
    }
    return fromIndex < inLength ? fromIndex : inLength;
  }

  /**
   * Search a sequence of characters in another sequence of characters and
   * returns the character index where it is found or -1 if not found.
   * 
   * @param input
   *          [in] The character sequence to search in.
   * @param toMatch
   *          [in] The character sequence to search for.
   * @param fromIndex
   *          [in] Start index in input to start search for.
   * @return The position where the first match was not found or -1 if not
   *         found.
   */
  public static int indexOfIgnoreCase(CharSequence input, CharSequence subString,
      int fromIndex)
  {
    int inLength = input.length();
    if (fromIndex < 0)
    {
      fromIndex = 0;
    }
    int subCount = subString.length();
    if (subCount > 0)
    {
      if (subCount + fromIndex > inLength)
      {
        return -1;
      }
      int subOffset = 0;
      char firstChar = subString.charAt(subOffset);
      int end = subOffset + subCount;
      while (true)
      {
        int i = Parsers.indexOfIgnoreCase(input, firstChar, fromIndex);
        if (i == -1 || subCount + i > inLength)
        {
          return -1; // handles subCount > count || start >= count
        }
        int o1 = 0 + i, o2 = subOffset;
        while (++o2 < end
            && Character.toUpperCase(input.charAt(++o1)) == Character
                .toUpperCase(subString.charAt(o2)))
        {
          // Intentionally empty
        }
        if (o2 == end)
        {
          return i;
        }
        fromIndex = i + 1;
      }
    }
    return fromIndex < inLength ? fromIndex : inLength;
  }


}
