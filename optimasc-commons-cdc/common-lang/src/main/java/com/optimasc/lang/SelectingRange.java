package com.optimasc.lang;

import java.math.BigDecimal;

public interface SelectingRange extends Selecting
{
  public BigDecimal getMinInclusive();
  public BigDecimal getMaxInclusive();
}
