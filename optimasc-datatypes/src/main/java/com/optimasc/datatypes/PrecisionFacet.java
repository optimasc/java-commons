package com.optimasc.datatypes;

/** Represents the precision and scale of a numeric value. The precision
 *  is the total number of digits in base 10 that can be represented in 
 *  this number.   
 *  
 *  The scale is the number of digits in the fractional part,
 *  for pure integer values, scale is 0.
 *  
 *  
 *  <p>For example, 999.99 has a precision of 5 and a scale of 2.</p>
 * 
 * @author Carl Eric Codere
 *
 */
public interface PrecisionFacet extends Restriction
{
  /** Return the precision in base 10 of this 
   *  number, which is the total number of digits.
   * 
   * @return The precision of this number
   */
  public int getPrecision();
  /** Return the scale in base 10 of this 
   *  number, which is the number of digits
   *  in the fractional part.
   * 
   * @return The scale of this number
   */
  public int getScale();
}
