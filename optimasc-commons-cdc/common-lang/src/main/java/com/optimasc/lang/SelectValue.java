package com.optimasc.lang;

import java.math.BigDecimal;

public interface SelectValue extends SelectItem
{
  /** Represents the actual value as a Java object. */
  public Object getObject();
}
