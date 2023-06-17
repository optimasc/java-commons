package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;

public class YearType extends DateTimeType
{
  protected static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(new YearType());
  
  public YearType()
  {
    super(Datatype.DATE);
    setResolution(RESOLUTION_YEAR);
  }

  public Object parse(String value) throws ParseException
  {
    throw new IllegalArgumentException("Parse method is not implemented");
  }
  
  public static UnnamedTypeReference getDefaultTypeReference()
  {
    return DEFAULT_TYPE_REFERENCE;
  }
  
}
