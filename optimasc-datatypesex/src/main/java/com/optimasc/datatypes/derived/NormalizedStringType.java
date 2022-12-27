package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.generated.StringTypeEx;

/** Represents a normalized string type.
 * 
 * @author Carl Eric Codere
 *
 */
public class NormalizedStringType extends StringTypeEx
{
  public NormalizedStringType()
  {
    super();
    setWhitespace(WHITESPACE_REPLACE);
  }

  
}
