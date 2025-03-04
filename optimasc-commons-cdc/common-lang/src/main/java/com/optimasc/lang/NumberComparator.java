package com.optimasc.lang;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Comparator;

/** Implements a generic number comparator.
 * 
 * @author Carl Eric Codere
 *
 */
public class NumberComparator implements Comparator
{
  public static final NumberComparator INSTANCE = new NumberComparator();
  
  /** Convert a <code>Number</code> to its <code>BigDecimal</code>
   *  equivalent.
   * 
   * @param n [in] The Number to convert
   * @return The <code>BigDecimal</code> representation of the
   *   input number.
   */
  public static BigDecimal toBigDecimal(Number n)
  {
    if (n instanceof BigDecimal)
    {
      return (BigDecimal) n;
    } else
    if (n instanceof BigInteger)
    {
        return new BigDecimal((BigInteger)n);
    } else
    if ((n instanceof Double) || (n instanceof Float))
    {
        return new BigDecimal(n.doubleValue());
    } else
    {
      return new BigDecimal(n.longValue());
    }
  }
  
  
  /** Convert a <code>Number</code> to its <code>BigDecimal</code>
   *  equivalent.
   * 
   * @param n [in] The Number to convert
   * @return The <code>BigDecimal</code> representation of the
   *   input number.
   */
  public static int getScale(Number n)
  {
    if (n instanceof BigDecimal)
    {
      return ((BigDecimal) n).scale();
    } else
    if ((n instanceof Double) || (n instanceof Float))
    {
        return (new BigDecimal(n.doubleValue())).scale();
    } else
    {
      return 0;
    }
  }
  
  
  
  public int compare(Object o1, Object o2)
  {
    BigDecimal number1 = toBigDecimal((Number) o1);
    BigDecimal number2 = toBigDecimal((Number) o2);

    return ((BigDecimal)number1).compareTo(number2);
  }

}
