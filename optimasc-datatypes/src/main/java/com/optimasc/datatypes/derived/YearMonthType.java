package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;

public class YearMonthType extends DateTimeType
{
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new YearMonthType());
  
  
  public YearMonthType()
  {
    super(Datatype.DATE);
    setResolution(RESOLUTION_MONTH);
  }

  public Object parse(String value) throws ParseException
  {
    throw new IllegalArgumentException("Parse method is not implemented");
  }

}
