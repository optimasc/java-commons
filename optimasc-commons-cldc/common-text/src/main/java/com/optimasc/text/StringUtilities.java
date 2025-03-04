/*
 *
 * Copyright (c) 2011 Optima SC Inc. All rights reserved.
 *
 * Redistribution and use in source and binary forms,
 * with or without modification in commercial and
 * non-commercial packages/software, are permitted
 * provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above
 * copyright notice, this list of conditions and the
 * following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the
 * above copyright notice, this list of conditions and the
 * following disclaimer in the documentation and/or other
 * materials provided with the distribution.
 *
 * 3. The end-user documentation included with the
 * redistribution, if any, must include the following
 * acknowledgment:
 *
 * "This product includes software developed by
 * Carl Eric Codere of Optima SC Inc."
 *
 * Alternately, this acknowledgment may appear in the
 * software itself, if and wherever such third-party
 * acknowledgments normally appear.
 *
 * 4. The names "Optima SC Inc." and "Carl Eric Codere" must
 * not be used to endorse or promote products derived from
 * this software without prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESSED OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A
 * PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL
 * OPTIMA SC INC. OR ITS CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.optimasc.text;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import com.optimasc.text.PrintfFormat;

/**
 * Contains static methods and utilities related to string manipulation and
 * matching. Contains some code from Apache Commons library (thus falling
 *   under the Apache License 2.0).
 *
 * @author Apache Commons
 * @author Carl Eric Codere
 */
public final class StringUtilities
{

  /** Matches MS-DOS / UNIX style wildcards. A wildcard
   *  is represented by a '*' character which matches
   *  zero or more characters and the '?' which matches
   *  a single character.
   *   
   * @param N1 [in] The string, which may contain wildcards
   *   that will be used as basis for the match.
   * @param N2 [in] The value that will be compared.
   * @return true if there N1 matches N2.
   */
  public static boolean wildcardMatch(String N1, String N2)
  {
    int P1;
    int P2;
    boolean match;
    if ((N1.length() == 0) && (N2.length() == 0))
    {
      return true;
    }
    P1 = 0;
    P2 = 0;
    match = true;
    if (N1.length() == 0)
    {
      if (N2.charAt(0) == '*')
      {
        match = true;
      } else
      {
        return false;
      }
    } else
    {
      if (N2.length() == 0)
      {
        if (N1.charAt(0) == '*')
        {
          match = true;
        } else
        {
          return false;
        }
      }
    }

    while ((match == true) && (P1 < N1.length()) && (P2 < N2.length()))
    {
      if ((N1.charAt(P1) == '?') || (N2.charAt(P2) == '?'))
      {
        P1++;
        P2++;
      } else if (N1.charAt(P1) == '*')
      {
        P1++;
        if (P1 < N1.length())
        {
          while ((P2 < N2.length())
              && (wildcardMatch(N1.substring(P1, N1.length()),
                  N2.substring(P2, N2.length())) == false))
          {
            P2++;
          }
          if (P2 >= N2.length())
          {
            return false;
          }
          P1 = N1.length() + 1;
          P2 = N2.length() + 1;
        } else
        {
          P2 = N2.length() + 1;
        }
      } else if (N2.charAt(P2) == '*')
      {
        P2++;
        if (P2 < N2.length())
        {

          while ((P1 < N1.length())
              && (wildcardMatch(N1.substring(P1, N1.length()),
                  N2.substring(P2, N2.length())) == false))
          {
            P1++;
          }
          if (P1 >= N1.length())
          {
            return false;
          }

          P1 = N1.length() + 1;
          P2 = N2.length() + 1;
        } else
        {
          P1 = N1.length() + 1;

        }
      } else if (N1.charAt(P1) == N2.charAt(P2))
      {
        P1++;
        P2++;
      } else
      {
        return false;
      }
    } // end while

    if (P1 >= N1.length())
    {
      while ((P2 < N2.length()) && (N2.charAt(P2) == '*'))
      {
        P2++;
      }
      if (P2 < N2.length())
      {
        return false;
      }
    }

    if (P2 >= N2.length())
    {
      while ((P1 < N1.length()) && (N1.charAt(P1) == '*'))
      {
        P1++;
      }
      if (P1 < N1.length())
      {
        return false;
      }
    }
    return match;
  }

  /**
   * Removes the specified string from the specified string and returns the
   * modified string.
   *
   * @param str
   *          String that needs to be modified.
   * @param toRemove
   *          The String pattern to remove.
   * @return The string with the removed pattern
   */
  public static String removeString(String str, String toRemove)
  {
    int i;
    if (str == null)
    {
      return null;
    }
    if (toRemove.length() == 0)
    {
      return str;
    }
    while ((i = str.indexOf(toRemove)) >= 0)
    {
      str = str.substring(0, i)
          + str.substring(i + toRemove.length(), str.length());
      if (str.length() == 0)
      {
        break;
      }
    }
    return str;
  }

  /** Whitespace normalization: Preserve all whitespace */
  public static final int WHITESPACE_PRESERVE = 1;
  /** Whitespace normalization: All occurrences of 
   *  0x09 (tab), 0x0A (line feed) and 0x0D (carriage return) 
   *  are replaced with 0x20 (space) characters.  */
  public static final int WHITESPACE_REPLACE = 2;
  /** Whitespace normalization: After the processing implied by 
   *  {@link #WHITESPACE_REPLACE}, contiguous sequences of 0x20's are 
   *  collapsed to a single 0x20, and leading and trailing 0x20's are removed. 
   */
  public static final int WHITESPACE_COLLAPSE = 3;

  /**
   * Normalizes the whitespace in a string. It can be used in three different 
   * ways:
   *
   * If normType is {@link #WHITESPACE_PRESERVE} nothing is done on the string and it is
   * returned as is, if normType is {@link #WHITESPACE_REPLACE} all occurrences of #x9
   * (tab), #xA (line feed) and #xD (carriage return) are replaced with #x20
   * (space). if normType is {@link #WHITESPACE_COLLAPSE} after the processing implied by
   * WHITESPACE_REPLACE, contiguous sequences of #x20's are collapsed to a
   * single #x20, and leading and trailing #x20's are removed.
   *
   * The values returned is a newly allocated string.
   * 
   * <p>The behaviour is equivalent to the <code>whiteSpace</code> attribute
   * of XMLSchema Second Edition.</p>
   * 
   * @param s [in] The string that will be normalzied.
   * @param normType [in] Normalization type  
   * @return The normalized string
   */
  public static String normalizeWhitespaces(String s, int normType)
  {
    // Preserve the whiteSpace, simply do nothing
    if (normType == WHITESPACE_PRESERVE)
    {
      return s;
    }
    // Now remove the following: 0x0D, 0x0A, 0x09 and replace
    // them with spaces.
    s = s.replace('\n', ' ');
    s = s.replace('\r', ' ');
    s = s.replace('\u0009', ' ');
    // Stop here if we are at whitespace replace.
    if (normType == WHITESPACE_REPLACE)
    {
      return s;
    }

    StringBuffer res = new StringBuffer();
    int prevIndex = 0;
    int currIndex = -1;
    int stringLength = s.length();

    String searchString = "  ";
    while ((currIndex = s.indexOf(searchString, currIndex + 1)) >= 0)
    {
      res.append(s.substring(prevIndex, currIndex + 1));
      while (currIndex < stringLength && s.charAt(currIndex) == ' ')
      {
        currIndex++;
      }

      prevIndex = currIndex;
    }
    res.append(s.substring(prevIndex));
    return res.toString().trim();
  }

  /** Splits this string around matches of the given string.
   * 
   *  <p>This is a subset of the Java 1.4 SDK String <code>split</code>
   *  method which is not available in JSR 219 CDC foundation profile.</p>
   * 
   * @param original [in] The string to split
   * @param separator [in] The separator string used to 
   *   split the string.
   * @return The separated string.
   */
  public static String[] split(String original, String separator)
  {
    Vector nodes = new Vector();
    int index = original.indexOf(separator);
    while (index >= 0)
    {
      nodes.addElement(original.substring(0, index));
      original = original.substring(index + separator.length());
      index = original.indexOf(separator);
    }
    nodes.addElement(original);

    String[] result = new String[nodes.size()];
    if (nodes.size() > 0)
    {
      for (int loop = 0; loop < nodes.size(); loop++)
      {
        result[loop] = (String) nodes.elementAt(loop);
      }

    }

    return result;
  }

  private static final String TRUE = "true";
  private static final String FALSE = "false";

  /**
   * Converts the specified boolean to its string representation.
   * 
   * <p>This method is added here since it is available in 
   * Java 1.4 SDK but not in the JSR 219 CDC Foundation profile</p>
   * 
   * @param value
   *            the boolean to convert.
   * @return "true" if {@code value} is {@code true}, "false" otherwise.
   */  
  public static String booleanToString(boolean b)
  {
    if (b)
      return TRUE;
    return FALSE;

  }

  /**
   * Converts a numeric value to its string representation by also left-padding
   * the value with zeros.
   *
   * @param num
   *          The number to convert
   * @param digits
   *          The minimum required number of digits
   * @return The string left-padded with zeros
   */
  public final static String intToString(int num, int digits)
  {
    String str = Integer.toString(num);
    StringBuffer s = new StringBuffer(digits + str.length());
    int zeroes = digits - str.length();
    for (int i = 0; i < zeroes; i++)
    {
      s.append(0);
    }
    return s.append(num).toString();
  }


  /** Convert a string to a C string that contains escape sequences
   *  as required
   *
   * @param s The string to convert
   * @return The converted string
   */
  public static String JavaToEscape(String s)
  {
    int i;
    char c;
    StringBuffer buf = new StringBuffer(s.length());
    i = 0;
    while (i < s.length())
    {
      c = s.charAt(i);
      i++;
      switch (c)
      {
        case '\u0007':
          buf.append("\\a");
          break;
        case '\u0008':
          buf.append("\\b");
          break;
        case '\u000C':
          buf.append("\\f");
          break;
        case '\n':
          buf.append("\\n");
          break;
        case '\r':
          buf.append("\\n");
          break;
        case '\t':
          buf.append("\\t");
          break;
        case '\u000B':
          buf.append("\\v");
          break;
        case '\'':
          buf.append("\\'");
          break;
        case '"':
          buf.append("\\\"");
          break;
        case '\\':
          buf.append("\\\\");
          break;
        default:
          buf.append(c);
          break;
      } // end switch
    }
    return buf.toString();
  }

  /** Convert a string that contains C Escape characters
   *  to a normal Java String.
   *
   * @param s The string to convert
   * @return The converted java string
   */
  public static String EscapeToJava(String s)
  {
    int i;
    char c;
    StringBuffer buf = new StringBuffer(s.length());
    i = 0;
    while (i < s.length())
    {
      c = s.charAt(i);
      if ((c == '\\') && (i < s.length()))
      {
        i++;
        c = s.charAt(i);
        i++;
        switch (c)
        {
        case '#':
          c = '#';
          break;
        case 'a':
          c = '\u0007';
          break;
        case 'b':
          c = '\u0008';
          break;
        case 'f':
          c = '\u000C';
          break;
        case 'n':
          c = '\n';
          break;
        case 'r':
          c = '\r';
          break;
        case 't':
          c = '\t';
          break;
        case 'v':
          c = '\u000B';
          break;
        case '?':
          c = '?';
          break;
        case '\'':
          c = '\'';
          break;
        case '"':
          c = '"';
          break;
        case '\\':
          c = '\\';
          break;
        case ' ':
          c = ' ';
          break;
        case '<':
          c = '<';
          break;
        /*           '0'..'7':
                     Begin
                       { check if actual octal or null character }
                       { BUG in freepascal with octal values !!  }
                       temp:=s[i];
                       if ((i+1) <= length(s)) then
                         begin
                          if  (s[i+1] in ['0'..'7'])  then
                             Begin
                                temp:=temp+s[i+1];
                                inc(i);
                                if (s[i+1] in ['0'..'7']) then
                                   Begin
                                    temp:=temp+s[i+1];
                                     inc(i);
                                   End;
                             End;
                         end;
                          c:=chr(ValOctal(temp,code));

                     end;
                   'x':
                     Begin
                       temp:=s[i+1];
                       inc(i);
                       if ((i+1) <= length(s)) then
                       begin
                          temp:=temp+s[i+1];
                          inc(i);
                       end;
                       c:=chr(ValHexaDecimal(temp,code));
                     end;
                   else
                     Begin
                       code := -1;
                       c:=s[i];
                     end;
                  end;
                end*/
        } // end switch
      } else
      {
        i++;
      } // endif
      buf.append(c);
    } // end while
    return buf.toString();
  }

  /**
   * Dump the data in the specified array to a printstream. The format
   * of the data is split into two parts, the left parts printing the
   * hex parts while the right part prints the ASCII representation
   * of the value.
   *
   * @param out [in] The output stream where the text will be output.
   * @param bytes [in] The bytes that will be printed
   * @param length [in] The length of the array
   * @param width [in] The width in characters of the ASCII part
   *   and hexadecimal part.
   * @throws IOException
   */
  public static void dumpData(PrintStream out, byte[] bytes, int length,
      int width) throws IOException
  {
    for (int index = 0; index < length; index += width)
    {
      printHex(out, bytes, index, width);
      printASCII(out, bytes, index, width);
      out.println();
    }
  }

  /** Prints hexadecimal representation of binary values in a buffer 
   *  to the output stream. Each hexadecimal value is separated
   *  from the others by a space.
   * 
   * @param out [in] The output stream where the text will be output.
   * @param bytes [in] The bytes that will be printed
   * @param offset [in] The offset in the buffer
   * @param length [in] The maximum number of bytes to print
   */
  public static void printHex(PrintStream out, byte[] bytes, int offset,
      int length)
  {
    PrintfFormat pf = new PrintfFormat("%02x ");
    int index;
    for (index = 0; index < length; index++)
    {
      if (index + offset < bytes.length)
      {
        out.print(pf.sprintf(bytes[index + offset] & 0xFF));
      } else
      {
        out.print("   ");
      }
    }
  }

  /** Prints ASCII representation of binary values in a buffer 
   *  to the output stream. When the character is not printable
   *  as ASCII a dot "."  character is output instead.
   * 
   * @param out [in] The output stream where the text will be output.
   * @param bytes [in] The bytes that will be printed
   * @param offset [in] The offset in the buffer
   * @param length [in] The maximum number of bytes to print
   */
  public static void printASCII(PrintStream out, byte[] bytes, int offset,
      int length) throws UnsupportedEncodingException
  {
    int val;
    char c;
    for (int index = 0; index < length; index++)
    {
      if (index + offset < bytes.length)
      {
        val = bytes[index + offset] & 0xFF;
        if (val < 0x20) // control characters
          c = '.';
        else
          c = (char) val;
        out.print(c);
      } else
      {
        out.print("  ");
      }
    }
  }


  // Wrapping
  //--------------------------------------------------------------------------
  /**
   * <p>Wraps a single line of text, identifying words by <code>' '</code>.</p>
   *
   * <p>New lines will be separated by the system property line separator.
   * Very long words, such as URLs will <i>not</i> be wrapped.</p>
   *
   * <p>Leading spaces on a new line are stripped.
   * Trailing spaces are not stripped.</p>
   *
   * <pre>
   * WordUtils.wrap(null, *) = null
   * WordUtils.wrap("", *) = ""
   * </pre>
   *
   * The is based on the Apache Commons Lang library licensed under Apache License
   * 2.0.
   *
   * @param str  the String to be word wrapped, may be null
   * @param wrapLength  the column to wrap the words at, less than 1 is treated as 1
   * @return a line with newlines inserted, <code>null</code> if null input
   */
  public static String wrap(String str, int wrapLength) {
      return wrap(str, wrapLength, null, false);
  }

  /**
   * <p>Wraps a single line of text, identifying words by <code>' '</code>.</p>
   *
   * <p>Leading spaces on a new line are stripped.
   * Trailing spaces are not stripped.</p>
   *
   * <pre>
   * WordUtils.wrap(null, *, *, *) = null
   * WordUtils.wrap("", *, *, *) = ""
   * </pre>
   * The is based on the Apache Commons Lang library licensed under Apache License
   * 2.0.
   *
   * @param str  the String to be word wrapped, may be null
   * @param wrapLength  the column to wrap the words at, less than 1 is treated as 1
   * @param newLineStr  the string to insert for a new line,
   *  <code>null</code> uses the system property line separator
   * @param wrapLongWords  true if long words (such as URLs) should be wrapped
   * @return a line with newlines inserted, <code>null</code> if null input
   */
  public static String wrap(String str, int wrapLength, String newLineStr, boolean wrapLongWords) {
      if (str == null) {
          return null;
      }
      if (newLineStr == null) {
          newLineStr = "\n";
      }
      if (wrapLength < 1) {
          wrapLength = 1;
      }
      int inputLineLength = str.length();
      int offset = 0;
      StringBuffer wrappedLine = new StringBuffer(inputLineLength + 32);

      while (inputLineLength - offset > wrapLength) {
          if (str.charAt(offset) == ' ') {
              offset++;
              continue;
          }
          int spaceToWrapAt = str.lastIndexOf(' ', wrapLength + offset);

          if (spaceToWrapAt >= offset) {
              // normal case
              wrappedLine.append(str.substring(offset, spaceToWrapAt));
              wrappedLine.append(newLineStr);
              offset = spaceToWrapAt + 1;

          } else {
              // really long word or URL
              if (wrapLongWords) {
                  // wrap really long word one line at a time
                  wrappedLine.append(str.substring(offset, wrapLength + offset));
                  wrappedLine.append(newLineStr);
                  offset += wrapLength;
              } else {
                  // do not wrap really long word, just extend beyond limit
                  spaceToWrapAt = str.indexOf(' ', wrapLength + offset);
                  if (spaceToWrapAt >= 0) {
                      wrappedLine.append(str.substring(offset, spaceToWrapAt));
                      wrappedLine.append(newLineStr);
                      offset = spaceToWrapAt + 1;
                  } else {
                      wrappedLine.append(str.substring(offset));
                      offset = inputLineLength;
                  }
              }
          }
      }

      // Whatever is left in line is short enough to just pass through
      wrappedLine.append(str.substring(offset));

      return wrappedLine.toString();
  }

}
