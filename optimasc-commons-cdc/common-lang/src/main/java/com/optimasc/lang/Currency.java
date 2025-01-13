package com.optimasc.lang;

import java.math.BigDecimal;
import java.text.ParseException;

/** Signed decimal currency value. This represents a currency value 
 *  with a scale of 2 digits and unlimited precision.
 * 
 * @author Carl Eric Codere
 *
 */
public class Currency extends Number implements Comparable
{
  public static final Currency ZERO = new Currency("0.00");
  
  
  private static final long serialVersionUID = -1551505231950291570L;
  
  protected static final int CURRENCY_SCALE = 2;
  protected BigDecimal value;
   
   public Currency(String currencyValue) throws IllegalArgumentException
   {
     value = new BigDecimal(currencyValue);
     if ((value.scale()!=CURRENCY_SCALE) && (value.scale()!=0))
     {
       throw new IllegalArgumentException("Currency requires 2 or 0 digits in the fractional part");
     }
   }
   
   public Currency(double currencyValue) throws ParseException
   {
     value = new BigDecimal(currencyValue);
     if ((value.scale()!=CURRENCY_SCALE) && (value.scale()!=0))
     {
       throw new ParseException("Currency requires 2 or 0 digits in the fractional part",0);
     }
   }

  public int intValue()
  {
    return value.intValue();
  }

  public long longValue()
  {
    return value.longValue();
  }

  public float floatValue()
  {
    return value.floatValue();
  }

  public double doubleValue()
  {
    return value.doubleValue();
  }
  
  public BigDecimal decimalValue()
  {
    return value;
  }

  public int compareTo(Object o)
  {
    if (o instanceof BigDecimal)
      return value.compareTo((BigDecimal)o);
    return -1;
  }
}
