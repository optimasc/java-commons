package com.optimasc.datatypes;

import java.text.ParseException;

public interface Parseable
{
  /** Tries to parse this string representation to the specified instance datatype. The
   *  parsing is as lenient as possible.
   * 
   * @param value The string to parse.
   * @return The object instance containing the value.
   */
  public abstract Object parse(String value) throws ParseException;

}
