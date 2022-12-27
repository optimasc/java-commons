package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;

public class YearType extends DateTimeType
{
  public YearType()
  {
    super(Datatype.DATE);
    setResolution(RESOLUTION_YEAR);
  }

}
