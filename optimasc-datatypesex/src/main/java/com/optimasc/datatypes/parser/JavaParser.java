package com.optimasc.datatypes.parser;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.defined.StringType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.IntegralType;

public class JavaParser implements DatatypeParser
{
  public Object parse(String value, Datatype datatype) throws ParseException,
      DatatypeException
  {
    /* Validate the value. */
    
    if (datatype instanceof StringType)
    {
      datatype.validate(value);
      return value;
    }
    
    if (datatype instanceof EnumerationFacet)
    {
      datatype.validate(value);
      return value;
    }
    
    if (datatype instanceof BooleanType)
    {
      value = value.toUpperCase();
      if (value.equals("0") || value.equals("false"))
      {
          return Boolean.FALSE;
      } else
      if (value.equals("1") || value.equals("true"))
      {
          return Boolean.TRUE;
      }
      throw new ParseException("Cannot parse string to Boolean type.",0);
    }
    
    
    if (datatype instanceof IntegralType)
    {
      IntegralType integerType = (IntegralType)datatype;
      try 
      {
        long longValue = Long.parseLong(value);
        integerType.validate(longValue);
      } catch (NumberFormatException e)
      {
        throw new ParseException("Cannot parse string to Long type.",0);
      }
    }
    return null;
  }

  public String toString(Object value) throws IllegalArgumentException, DatatypeException
  {
/*    if (value instanceof Boolean)
    {
      return value.toString();
    }
    if ((value instanceof Integer) || (value instanceof Long) || (value instanceof Byte) || (value instanceof Short))
    {
      return value.toString();
    }
    
    if (value instanceof String)
    {
      return (String)value;
    }
    
    if (value instanceof char[])
    {
      return new String((char[])value);
    }*/
    return value.toString();
    
//    return null;
    
  }

}
