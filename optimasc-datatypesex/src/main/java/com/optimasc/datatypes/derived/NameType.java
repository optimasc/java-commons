package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.TypeFactory;
import com.optimasc.datatypes.defined.UCS2CharType;
import com.optimasc.datatypes.defined.UnsignedByteType;
import com.optimasc.text.StandardFormatters;

public class NameType extends TokenType
{
  public NameType()
  {
    super(new StandardFormatters.NCNameConverter(),TypeFactory.getDefaultInstance(UCS2CharType.class));
  }
  
}
