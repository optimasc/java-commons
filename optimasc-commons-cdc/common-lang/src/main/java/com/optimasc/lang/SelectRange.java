package com.optimasc.lang;

import java.math.BigDecimal;

/** Represents a numeric value range. */
public interface SelectRange extends SelectItem
{
  /** Returns the minimum inclusive value allowed for this
   *  ordered value. If this value has not been set,
   *  or if the type has not been configured to be ordered,
   *  the return value will be <code>null</code>.
   * 
   * @return The minimum inclusive value allowed or <code>null</code>
   *  if it is not set.
   */
  public Number getMinInclusive();
  /** Returns the maximum inclusive value allowed for this
   *  ordered value. If this value has not been set,
   *  or if the type has not been configured to be ordered,
   *  the return value will be <code>null</code>.
   * 
   * @return The maximum inclusive value allowed or <code>null</code>
   *  if it is not set.
   */
  public Number getMaxInclusive();
}
