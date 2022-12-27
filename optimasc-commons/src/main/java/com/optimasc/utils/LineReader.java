package com.optimasc.utils;

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

  protected static final int MAX_LINE_LENGTH = 1024;

  protected static byte[] buffer = new byte[MAX_LINE_LENGTH];
  protected static StringBuffer stringBuffer = new StringBuffer(256);


  /**
   * Reads a single line using encoded in ISO-8859-1 format using the specified reader.
   * It supports the following line encodings:
   *   \n\r (Windows format)
   *   \n (UNIX format)
   *
   * There is no limitation on the size of the line.
   *
   * @throws java.io.IOException if an exception occurs when reading the
   * line
   */
  public static String readISOLine(InputStream reader) throws IOException {
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
              stringBuffer.append(readChar);
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
   * The maximum line length is limited to MAX_LINE_LENGTH bytes.
   *
   * @throws java.io.IOException if an exception occurs when reading the
   * line
   */
  public static String readUTF8Line(InputStream reader) throws IOException {
      int index = 0;
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
              buffer[index] = (byte)(readChar & 0xFF);
              index++;
          }
          // Read the next character
          readChar = reader.read();
      }
      return new String(buffer, 0, index, "UTF-8");
  }
}
