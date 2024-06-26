package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;

public class YearType extends DateTimeType
{
  public static final YearType DEFAULT_INSTANCE = new YearType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public YearType()
  {
    super(Datatype.DATE);
    setResolution(RESOLUTION_YEAR);
  }

  public static UnnamedTypeReference getDefaultTypeReference()
  {
    return DEFAULT_TYPE_REFERENCE;
  }
  
}
