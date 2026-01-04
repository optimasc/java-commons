package com.optimasc.io;

import java.io.IOException;
import java.io.InputStream;

/** A class that is used to read logical lines that can be terminated
 *  by the different line terminator and supports different encodings.
 *
 *  It supports reading from both Windows (\r\n) and UNIX (\n) line
 *  endings.
 *
 * @author Carl Eric Codere
 *
 */
public class LineReader
{

  /** Maximum expected line length in bytes. 
   *  The value here is based on the <code>_POSIX2_LINE_MAX</code> value. */
  protected static final int MAX_LINE_LENGTH = 2048;

  /**
   * Reads a single line using encoded in ISO-8859-1 format using the specified reader.
   * It supports the following line encodings:
   *   \n\r (Windows format)
   *   \n (UNIX format)
   *
   * There is no limitation on the size of the line.
   *
   * @return The next line of text from the input stream, or <code>null</code> 
   *  if the end of file is encountered before a byte can be read. 
   * @throws java.io.IOException if an exception occurs when reading the
   * line
   */
  public static String readISOLine(InputStream reader) throws IOException 
  {
      StringBuffer stringBuffer = new StringBuffer(256); 
      // Test whether the end of file has been reached. If so, return null.
      int readChar = reader.read();
      if (readChar == -1) {
          return null;
      }
      // Read until end of file or new line
      while ((readChar != -1) && (readChar != '\n')) {
          // Append the read character to the string. Some operating systems
          // such as Microsoft Windows prepend newline character ('\n') with
          // carriage return ('\r'). This is part of the newline character
          // and therefore an exception that should not be appended to the
          // string.
          if (readChar != '\r') {
              stringBuffer.append((char)readChar);
          }
          // Read the next character
          readChar = reader.read();
      }
      return stringBuffer.toString();
  }


  /**
   * Reads a single line using encoded in UTF-8 format using the specified reader.
   * It supports the following line encodings:
   *   \r\n (Windows format)
   *   \n (UNIX format)
   * There is no limitation on the size of the line.
   *
   * @return The next line of text from the input stream, or <code>null</code> 
   *  if the end of file is encountered before a byte can be read. 
   * @throws java.io.IOException if an exception occurs when reading the
   * line
   */
  public static String readUTF8Line(InputStream reader) throws IOException 
  {
      java.io.ByteArrayOutputStream bis = new java.io.ByteArrayOutputStream(MAX_LINE_LENGTH);
      // Test whether the end of file has been reached. If so, return null.
      int readChar = reader.read();
      if (readChar == -1) {
          return null;
      }
      // Read until end of file or new line
      while ((readChar != -1) && (readChar != '\n')) {
          // Append the read character to the string. Some operating systems
          // such as Microsoft Windows prepend newline character ('\n') with
          // carriage return ('\r'). This is part of the newline character
          // and therefore an exception that should not be appended to the
          // string.
          if (readChar != '\r') {
              bis.write((byte)(readChar & 0xFF));               
          }
          // Read the next character
          readChar = reader.read();
      }
      return new String(bis.toByteArray(), 0, bis.size(), "UTF-8");
  }
}
