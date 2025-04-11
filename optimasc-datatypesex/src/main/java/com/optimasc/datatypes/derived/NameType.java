package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.defined.UCS2CharType;
import com.optimasc.text.StandardFormatters;

public class NameType extends TokenType
{
  public NameType()
  {
    super(new StandardFormatters.NCNameConverter(),UCS2CharType.DEFAULT_TYPE_REFERENCE);
  }
  
}
