package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;

public class YearMonthType extends DateTimeType
{
  public YearMonthType()
  {
    super(Datatype.DATE);
    setResolution(RESOLUTION_MONTH);
  }

}
