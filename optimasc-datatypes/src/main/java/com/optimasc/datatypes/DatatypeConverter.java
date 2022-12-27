package com.optimasc.datatypes;

import java.text.ParseException;

/** Represents a way to convert from this datatype */
public interface DatatypeConverter
{
 /** Converts the string representation of this datatype to its value
  *  representation.
  *  
  * @param string
  * @return
  * @throws ParseException
  */
  public Object parse(String value) throws ParseException;  
  
 //public String valueToString(Object value) throws ParseException;
}
