package com.optimasc.math;

/** Rational number representation. A rational number is represented
 *  as a fractional number with a numerator and denominator.
 *  
 *  @author Carl Eric Codere
 *
 */
public class Rational extends Number implements Comparable 
{
    private static final long serialVersionUID = 3792016500480406052L;

    /** Represents the rational number zero. */
    public static final Rational ZERO = new Rational(0, 1);

    private int num;   // the numerator
    private int den;   // the denominator

    /** Represents the rational number one. */
    public static final Rational ONE = new Rational(1,1);

    /** Represents the rational number two. */
    public static final Rational TWO = new Rational(2,1);

    /** Create an initialize a new rational number. The
     *  value is not normalized and is used as is.
     * 
     * @param numerator
     * @param denominator
     */
    public Rational(int numerator, int denominator) 
    {

        if (denominator == 0) {
            throw new ArithmeticException("denominator is zero");
        }

        // reduce fraction
        int g = gcd(numerator, denominator);
        num = numerator   / g;
        den = denominator / g;

        // needed only for negative numbers
        if (den < 0) { den = -den; num = -num; }
    }
    
    public Rational(int numerator, int denominator, boolean normalize) 
    {

        if (denominator == 0) {
            throw new ArithmeticException("denominator is zero");
        }

        if (normalize == true)
        {
          // reduce fraction
          int g = gcd(numerator, denominator);
          num = numerator   / g;
          den = denominator / g;
        } else
        {
          num = numerator;
          den = denominator;
        }

        // needed only for negative numbers
        if (den < 0) { den = -den; num = -num; }
    }
    

    
    /** Return the numerator of this rational number. */
    public int numerator()   { return num; }
    /** Return the denominator of this rational number. */
    public int denominator() { return den; }

    /** Returns the string representation of (this) */
    public String toString() 
    { 
        if (den == 1) return num + "";
        else          return num + "/" + den;
    }

    // return { -1, 0, +1 } if a < b, a = b, or a > b
    public int compareTo(Object o) {
        Rational a = this;
        Rational b = (Rational) o;
        int lhs = a.num * b.den;
        int rhs = a.den * b.num;
        if (lhs < rhs) return -1;
        if (lhs > rhs) return +1;
        return 0;
    }

    // is this Rational object equal to y?
    public boolean equals(Object y) {
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Rational b = (Rational) y;
        return compareTo(b) == 0;
    }

    // hashCode consistent with equals() and compareTo()
    // (better to hash the numerator and denominator and combine)
    public int hashCode() {
        return this.toString().hashCode();
    }



    // return gcd(|m|, |n|)
    public static int gcd(int m, int n) {
        if (m < 0) m = -m;
        if (n < 0) n = -n;
        if (0 == n) return m;
        else return gcd(n, m % n);
    }

    // return lcm(|m|, |n|)
    public static int lcm(int m, int n) {
        if (m < 0) m = -m;
        if (n < 0) n = -n;
        return m * (n / gcd(m, n));    // parentheses important to avoid overflow
    }
    
    
    /** return a * value where value is an integer multiplier */
    public Rational times(int value) 
    {
      return new Rational(value*num,den);
    }

    /**  return a * b, staving off overflow as much as possible by cross-cancellation */
    public Rational times(Rational b) {
        Rational a = this;

        // reduce p1/q2 and p2/q1, then multiply, where a = p1/q1 and b = p2/q2
        Rational c = new Rational(a.num, b.den);
        Rational d = new Rational(b.num, a.den);
        return new Rational(c.num * d.num, c.den * d.den);
    }

    
    public static void addFraction(int num1, int denum1,int num2, int denum2, Rational result)
    {

      // special cases
      if (num1 == 0)
      {
        result.den = denum2;
        result.num = num2;
        return;
      }
      if (num2 == 0)
      {
        result.den = denum1;
        result.num = num1;
        return;
      }

      // Find gcd of numerators and denominators
      int f = gcd(num1, num2);
      int g = gcd(denum1, denum2);

      // add cross-product terms for numerator
      int newNum = (num1 / f) * (denum2 / g) + (num2 / f) * (denum1 / g);
      int newDenum = lcm(denum1, denum2);

      // multiply back in
      newNum *= f;
      result.num = newNum;
      result.den = newDenum;
    }

    // return a + b, staving off overflow
    public Rational plus(Rational b) {
        Rational a = this;

        // special cases
        if (a.compareTo(ZERO) == 0) return b;
        if (b.compareTo(ZERO) == 0) return a;

        // Find gcd of numerators and denominators
        int f = gcd(a.num, b.num);
        int g = gcd(a.den, b.den);

        // add cross-product terms for numerator
        Rational s = new Rational((a.num / f) * (b.den / g) + (b.num / f) * (a.den / g),
                                  lcm(a.den, b.den));

        // multiply back in
        s.num *= f;
        return s;
    }

    // return -a
    public Rational negate() {
        return new Rational(-num, den);
    }

    // return |a|
    public Rational abs() {
        if (num >= 0) return this;
        else return negate();
    }

    // return a - b
    public Rational minus(Rational b) {
        Rational a = this;
        return a.plus(b.negate());
    }


    public Rational reciprocal() { return new Rational(den, num);  }

    /** Return a / b */
    public Rational divides(Rational b) {
        Rational a = this;
        return a.times(b.reciprocal());
    }

    public int intValue()
    {
      return (int) num / den;
    }

    public long longValue()
    {
      return (long) num / den;
    }

    public float floatValue()
    {
      return (float) num / den;
    }

    public double doubleValue()
    {
      return (double) num / den;
    }



}