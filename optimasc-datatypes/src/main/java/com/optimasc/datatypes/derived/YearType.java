package com.optimasc.datatypes.derived;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;

public class YearType extends DateTimeType
{
  public YearType()
  {
    super(Datatype.DATE);
    setResolution(RESOLUTION_YEAR);
  }

  public Object parse(String value) throws ParseException
  {
    throw new IllegalArgumentException("Parse method is not implemented");
  }
  
}
