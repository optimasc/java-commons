package com.optimasc.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.optimasc.io.LineReader;
import com.optimasc.text.BaseISO8601Date;
import com.optimasc.text.StringUtilities;

/**
 * Implements a simplified property list of data that can be read and output to
 * an output stream. The PropertyList is similar to the Properties Class of J2SE
 * except for the following limitations:
 * <ul>
 * <li>Unicode escape characters are not supported (since the data is UTF-8
 * encoded anyways)</li>
 * <li>The key and value separator must be an '=' or ':' character (white space
 * as separator is not supported)
 * <li>
 * <li>Physical endings should in \r\n or \n format (\r only is not supported)</li>
 * <li>Each physical line is limited to 1024 bytes in length</li>
 * <li>Key names cannot contain the '=' or ':' special characters (they cannot
 * be escaped)</li>
 * </ul>
 * 
 * @author Carl Eric Codere
 * 
 */
public class PropertyList
{
  private static final String ALTERNATE_COMMENT_CHAR = "!";
  private static final String COMMENT_CHAR = "#";
  private static final String KEY_SEPARATOR = "=";
  private static final String KEY_SEPARATOR_ALT = ":";

  private static final String NEWLINE = "\n";
  
  protected Hashtable hashTable;

  /**
   * Creates an empty property list with no default values.
   */
  public PropertyList()
  {
    hashTable = new Hashtable();
  }

  /**
   * Calls the <tt>Hashtable</tt> method <code>put</code>. Provided for
   * parallelism with the <tt>getProperty</tt> method. Enforces use of strings
   * for property keys and values. The value returned is the result of the
   * <tt>Hashtable</tt> call to <code>put</code>.
   * 
   * @param key
   *          the key to be placed into this property list.
   * @param value
   *          the value corresponding to <tt>key</tt>.
   * @return the previous value of the specified key in this property list, or
   *         <code>null</code> if it did not have one.
   * @see #getProperty
   */
  public synchronized void putString(String key, String value)
  {
    hashTable.put(key, value);
  }

  /**
   * Reads a property list (key and element pairs) from the input character
   * stream in a simple line-oriented format.
   * <p>
   * Properties are processed in terms of lines. There are two kinds of line,
   * <i>natural lines</i> and <i>logical lines</i>. A natural line is defined as
   * a line of characters that is terminated either by a set of line terminator
   * characters (<code>\n</code> or <code>\r\n</code>) or by the end of the
   * stream. A natural line may be either a blank line, a comment line, or hold
   * all or some of a key-element pair. A logical line holds all the data of a
   * key-element pair, which may be spread out across several adjacent natural
   * lines by escaping the line terminator sequence with a backslash character
   * <code>\</code>. Note that a comment line cannot be extended in this manner;
   * every natural line that is a comment must have its own comment indicator,
   * as described below. Lines are read from input until the end of the stream
   * is reached.
   * 
   * <p>
   * A natural line that contains only white space characters is considered
   * blank and is ignored. A comment line has an ASCII <code>'#'</code> or
   * <code>'!'</code> as its first non-white space character; comment lines are
   * also ignored and do not encode key-element information. In addition to line
   * terminators, this format considers all control characters to be whitespace.
   * 
   * <p>
   * If a logical line is spread across several natural lines, the backslash
   * escaping the line terminator sequence, the line terminator sequence, and
   * any white space at the start of the following line have no affect on the
   * key or element values. The remainder of the discussion of key and element
   * parsing (when loading) will assume all the characters constituting the key
   * and element appear on a single natural line after line continuation
   * characters have been removed. Note that it is <i>not</i> sufficient to only
   * examine the character preceding a line terminator sequence to decide if the
   * line terminator is escaped; there must be an odd number of contiguous
   * backslashes for the line terminator to be escaped. Since the input is
   * processed from left to right, a non-zero even number of 2<i>n</i>
   * contiguous backslashes before a line terminator (or elsewhere) encodes
   * <i>n</i> backslashes after escape processing.
   * 
   * <p>
   * The key contains all of the characters in the line starting with the first
   * non-white space character and up to, but not including, the first unescaped
   * <code>'='</code>, <code>':'</code> other than a line terminator. All of
   * these key termination characters may be included in the key by escaping
   * them with a preceding backslash character; for example,
   * <p>
   * 
   * <code>\:\=</code>
   * <p>
   * 
   * would be the two-character key <code>":="</code>. Line terminator
   * characters can be included using <code>\r</code> and <code>\n</code> escape
   * sequences. Any white space after the key is skipped; if the first non-white
   * space character after the key is <code>'='</code> or <code>':'</code>, then
   * it is ignored and any white space characters after it are also skipped. All
   * remaining characters on the line become part of the associated element
   * string; if there are no remaining characters, the element is the empty
   * string <code>&quot;&quot;</code>. Once the raw character sequences
   * constituting the key and element are identified, escape processing is
   * performed as described above.
   * 
   * <p>
   * As an example, each of the following three lines specifies the key
   * <code>"Truth"</code> and the associated element value <code>"Beauty"</code>:
   * <p>
   * 
   * <pre>
   * Truth = Beauty
   *  Truth:Beauty
   * Truth            :Beauty
   * </pre>
   * 
   * As another example, the following three lines specify a single property:
   * <p>
   * 
   * <pre>
   * fruits                           apple, banana, pear, \
   *                                  cantaloupe, watermelon, \
   *                                  kiwi, mango
   * </pre>
   * 
   * The key is <code>"fruits"</code> and the associated element is:
   * <p>
   * 
   * <pre>
   * &quot;apple, banana, pear, cantaloupe, watermelon, kiwi, mango&quot;
   * </pre>
   * 
   * Note that a space appears before each <code>\</code> so that a space will
   * appear after each comma in the final result; the <code>\</code>, line
   * terminator, and leading white space on the continuation line are merely
   * discarded and are <i>not</i> replaced by one or more other characters.
   * <p>
   * As a third example, the line:
   * <p>
   * 
   * <pre>
   * cheeses
   * </pre>
   * 
   * specifies that the key is <code>"cheeses"</code> and the associated element
   * is the empty string <code>""</code>.
   * <p>
   * <p>
   * 
   * <li>The method does not treat a backslash character, <code>\</code>, before
   * a non-valid escape character as an error; the backslash is silently
   * dropped. For example, in a Java string the sequence <code>"\z"</code> would
   * cause a compile time error. In contrast, this method silently drops the
   * backslash. Therefore, this method treats the two character sequence
   * <code>"\b"</code> as equivalent to the single character <code>'b'</code>.
   * 
   * <li>Escapes are not necessary for single and double quotes; however, by the
   * rule above, single and double quote characters preceded by a backslash
   * still yield single and double quote characters, respectively.
   * 
   * <li>Only a single 'u' character is allowed in a Uniocde escape sequence.
   * 
   * </ul>
   * <p>
   * The specified stream remains open after this method returns.
   * 
   * @param reader
   *          the input character stream.
   * @throws IOException
   *           if an error occurred when reading from the input stream.
   * @throws IllegalArgumentException
   *           if a malformed Unicode escape appears in the input.
   */
  public synchronized void load(InputStream inStream) throws IOException
  {
    String s;
    int i, j;
    int separatorIndex;
    String key;
    String value;
    while (true)
    {
      s = LineReader.readUTF8Line(inStream);
      if (s==null)
        break;
      s = s.trim();
      // Comment characters are ignored
      if (s.startsWith(COMMENT_CHAR) || s.startsWith(ALTERNATE_COMMENT_CHAR))
      {
        continue;
      }
      // Whitespace lines are ignored
      if (s.length() == 0)
        continue;
      i = s.indexOf(KEY_SEPARATOR);
      j = s.indexOf(KEY_SEPARATOR_ALT);
      // Check the location of the key-value separator.
      if (i > j)
      {
        separatorIndex = i;
      } else
      {
        separatorIndex = j;
      }
      if (separatorIndex == -1)
      {
        throw new IllegalArgumentException("Illegal format of properties file!");
      }
      key = s.substring(0, separatorIndex);
      value = s.substring(separatorIndex+1);
      // Convert escape characters 
      value = StringUtilities.EscapeToJava(value);
      putString(key, value);
    }
  }


  /**
   * Writes this property list (key and element pairs) in this
   * <code>Properties</code> table to the output character stream in a format
   * suitable for using the {@link #load(java.io.Reader) load(Reader)} method.
   * <p>
   * Properties from the defaults table of this <code>Properties</code> table
   * (if any) are <i>not</i> written out by this method.
   * <p>
   * If the comments argument is not null, then an ASCII <code>#</code>
   * character, the comments string, and a line separator are first written to
   * the output stream. Thus, the <code>comments</code> can serve as an
   * identifying comment. Any one of a line feed ('\n'), a carriage return
   * ('\r'), or a carriage return followed immediately by a line feed in
   * comments is replaced by a line separator generated by the
   * <code>Writer</code> and if the next character in comments is not character
   * <code>#</code> or character <code>!</code> then an ASCII <code>#</code> is
   * written out after that line separator.
   * <p>
   * Next, a comment line is always written, consisting of an ASCII
   * <code>#</code> character, the current date and time (as if produced by the
   * <code>toString</code> method of <code>Date</code> for the current time),
   * and a line separator as generated by the <code>Writer</code>.
   * <p>
   * Then every entry in this <code>Properties</code> table is written out, one
   * per line. For each entry the key string is written, then an ASCII
   * <code>=</code>, then the associated element string. For the key, all space
   * characters are written with a preceding <code>\</code> character. For the
   * element, leading space characters, but not embedded or trailing space
   * characters, are written with a preceding <code>\</code> character. The key
   * and element characters <code>#</code>, <code>!</code>, <code>=</code>, and
   * <code>:</code> are written with a preceding backslash to ensure that they
   * are properly loaded.
   * <p>
   * After the entries have been written, the output stream is flushed. The
   * output stream remains open after this method returns.
   * <p>
   * 
   * @param writer
   *          an output character stream writer.
   * @param comments
   *          a description of the property list.
   * @exception IOException
   *              if writing this property list to the specified output stream
   *              throws an <tt>IOException</tt>.
   * @exception ClassCastException
   *              if this <code>Properties</code> object contains any keys or
   *              values that are not <code>Strings</code>.
   * @exception NullPointerException
   *              if <code>writer</code> is null.
   * @since 1.6
   */
  public void store(OutputStream out, String comments)
      throws IOException
  {
    Calendar cal = Calendar.getInstance();
    if (comments != null)
    {
      String comment = new String("# " + comments+NEWLINE);
      out.write(comment.getBytes("UTF-8"));
    }
    cal.setTime(new Date());
    String s = new String("# " + BaseISO8601Date.toString(cal, true, false)+NEWLINE);
    out.write(s.getBytes("UTF-8"));
    
    synchronized (this) {
      for (Enumeration e = hashTable.keys(); e.hasMoreElements();) 
      {
          String key = (String)e.nextElement();
          String val = hashTable.get(key).toString();
          String s1 = new String(key+"="+StringUtilities.JavaToEscape(val)+NEWLINE);
          out.write(s1.getBytes("UTF-8"));
      }
    }
    out.flush();
  }

  /**
   * Searches for the property with the specified key in this property list. If
   * the key is not found in this property list, the default property list, and
   * its defaults, recursively, are then checked. The method returns
   * <code>null</code> if the property is not found.
   * 
   * @param key
   *          the property key.
   * @return the value in this property list with the specified key value.
   * @see #setProperty
   * @see #defaults
   */
  public String getString(String key)
  {
    Object oval = hashTable.get(key);
    String sval = (oval instanceof String) ? (String) oval : null;
    return sval;
  }
  
  
  

  /**
   * Searches for the property with the specified key in this property list. If
   * the key is not found in this property list, the default property list, and
   * its defaults, recursively, are then checked. The method returns the default
   * value argument if the property is not found.
   * 
   * @param key
   *          the hashtable key.
   * @param defaultValue
   *          a default value.
   * 
   * @return the value in this property list with the specified key value.
   * @see #setProperty
   * @see #defaults
   */
  public String getString(String key, String defaultValue)
  {
    String val = getString(key);
    return (val == null) ? defaultValue : val;
  }

  /**
   * Returns an enumeration of all the keys in this property list, including
   * distinct keys in the default property list if a key of the same name has
   * not already been found from the main properties list.
   * 
   * @return an enumeration of all the keys in this property list, including the
   *         keys in the default property list.
   * @throws ClassCastException
   *           if any key in this property list is not a string.
   * @see java.util.Enumeration
   * @see java.util.Properties#defaults
   * @see #stringPropertyNames
   */
  public Enumeration propertyNames()
  {
    Hashtable h = new Hashtable();
    enumerate(h);
    return h.keys();
  }

  /**
   * Prints this property list out to the specified output stream. This method
   * is useful for debugging.
   * 
   * @param out
   *          an output stream.
   * @throws ClassCastException
   *           if any key in this property list is not a string.
   */
  public void list(PrintStream out)
  {
    out.println("-- listing properties --");
    Hashtable h = new Hashtable();
    enumerate(h);
    for (Enumeration e = h.keys(); e.hasMoreElements();)
    {
      String key = (String) e.nextElement();
      String val = (String) h.get(key);
      if (val.length() > 40)
      {
        val = val.substring(0, 37) + "...";
      }
      out.println(key + "=" + val);
    }
  }

  /**
   * Enumerates all key/value pairs in the specified hashtable.
   * 
   * @param h
   *          the hashtable
   * @throws ClassCastException
   *           if any of the property keys is not of String type.
   */
  private synchronized void enumerate(Hashtable h)
  {
    for (Enumeration e = hashTable.keys(); e.hasMoreElements();)
    {
      String key = (String) e.nextElement();
      h.put(key, hashTable.get(key));
    }
  }

  public void clear()
  {
    hashTable.clear();
  }

  public void remove(String key)
  {
    hashTable.remove(key);
  }


}
