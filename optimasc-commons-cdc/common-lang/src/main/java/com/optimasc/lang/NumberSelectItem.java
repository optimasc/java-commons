package com.optimasc.lang;

/** Represents an enumeration of numeric values represented 
 *  by an ooject list. This is equivalent to the 
 *  <code>selecting</code> subtype in ISO/IEC 11404:2007 and
 *  supports both unique values and range of values to define
 *  the enumeration.
 * 
 * @author Carl Eric Codere
 *
 */
public interface NumberSelectItem extends SelectItem
{
  /** Return the scale of the number. Scale of 0 means an integer
   *  value */
  public int getScale();
}
