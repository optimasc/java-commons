package com.optimasc.datatypes;

import java.math.BigDecimal;
import java.math.BigInteger;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.aggregate.DerivableAggregateType;
import com.optimasc.datatypes.defined.LatinCharType;
import com.optimasc.datatypes.defined.UCS2CharType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.lang.CharacterSet;
import com.optimasc.lang.NumberComparator;

/** General type utilities */
public class TypeUtilities
{
  
  public static final BigDecimal DECIMAL_ZERO = BigDecimal.valueOf(0);
  public static final BigDecimal DECIMAL_ONE = BigDecimal.valueOf(1);
  
  /** Class that provides extra information on the result of a type compatibility
   *  check.
   *
   */
  public static class TypeCheckResult
  {
    /**
     * Requires dynamic or compile time range check to verify if values are out
     * of bounds.
     */
    public boolean requiresRangeCheck;
    /**
     * Confirmation that destination type is narrower than source type. Usually
     * when this value is set, <code>requiresRangeCheck</code> is also set, it
     * may also indicates that the destination length may overflow for example
     * when copying a fixed length string to another fixed length string which
     * is a smaller.
     */
    public boolean narrowingConversion;
    /** Dangerous incompatible types, even though accepted by the feature set,
     *  for example function pointer assignment with different points. This
     *  is used for non-ordinal types. 
     * 
     */
    public boolean incompatibleTypes;
    /** Check must be done at runtime, as the type 
     *  is dynamic such as classes and interfaces. 
     * 
     */
    public boolean runtimeTypeCheck;
    
    /** Additional information on error condition if any. */
    public Exception error;
    

    /** Reset values to default values. */
    public void reset()
    {
      narrowingConversion = false;
      requiresRangeCheck = false;
      incompatibleTypes = false;
      runtimeTypeCheck = false;
      error = null;
    }
  }
  
  
  /** Checks the bounds of left and right and returns true 
   *  if there may be an out of bounds if value is assigned to left. 
   * 
   * @param left The left type that may receive the value.
   * @param right The right type that is the source of the value.
   * @return true if an out of range value may occur.
   */
  public static boolean isOutOfBounds(NumberRangeFacet left, NumberRangeFacet right)
  {
    // Compare the values.
    // this.minInclusive <= other.minInclusive AND 
    // this.maxInclusive >= other.maxInclusive 
    boolean minEquality = NumberComparator.INSTANCE.compare(left.getMinInclusive(),right.getMinInclusive())==0;
    boolean minLess = NumberComparator.INSTANCE.compare(left.getMinInclusive(),right.getMinInclusive()) == -1;
    boolean maxEquality = NumberComparator.INSTANCE.compare(left.getMaxInclusive(),right.getMaxInclusive())==0;
    boolean maxGreater = NumberComparator.INSTANCE.compare(left.getMaxInclusive(),right.getMaxInclusive()) == 1;
    
    if (((minEquality) || (minLess)) && ((maxEquality) || (maxGreater)))
      return true;
    return false;
  }
  
  
  /** Checks the length of left and right and returns true 
   *  if the value assigned to left from right may result
   *  in a bigger value in length than actually allowed. 
   * 
   * @param left The left type that may receive the value.
   * @param right The right type that is the source of the value.
   * @return true if an out of range value may occur.
   */
  public static boolean isBigger(LengthFacet left, LengthFacet right)
  {
    if (right.getMaxLength() > left.getMaxLength())
    {
      return true;
    }
    return false;
  }
  
  /** The following character sets will map to
   *  LatinCharType.
   */
  protected static final CharacterSet LATIN1_CHARSETS[] =
  {
    CharacterSet.GRAPHIC_IRV,
    CharacterSet.ASCII,
    CharacterSet.LATIN1,
    CharacterSet.ISO8BIT
  };
  
  /** The following character sets will map to
   *  UCS2CharType.
   */
  protected static final CharacterSet BMP_CHARSETS[] =
  {
    CharacterSet.BMP,  
  };
  
  
  /** From a list of character set, return the associated type.
   * 
   * @param charSet
   * @return
   */
  public static TypeReference getTypeFromCharacterSet(CharacterSet charSet)
  {
    for (int i=0; i < LATIN1_CHARSETS.length; i++)
    {
      if (charSet.equals(LATIN1_CHARSETS[i]))
      {
        return TypeFactory.getDefaultInstance(LatinCharType.class);
      }
    }
    
    for (int i=0; i < BMP_CHARSETS.length; i++)
    {
      if (charSet.equals(BMP_CHARSETS[i]))
      {
        return TypeFactory.getDefaultInstance(UCS2CharType.class);
      }
    }
    throw new IllegalArgumentException("The character set "+charSet.toString()+" cannot be mapped to a supported type.");
  }
  
/** Returns true if this type represents a primitive numeric value.
 *  In this implementation CharacterType is NOT considered
 *  a numeric type.
 *  
 * @param type
 * @return
 */
public boolean isPrimitiveNumeric(Type type)
{
  if (type instanceof RealType)
  {
    return true;
  }
  if (type instanceof IntegralType)
  {
    return true;
  }
  return false;
}

 
 /** Verifies if the right operand is derived from the
  *  left operand. 
  * 
  * @param left Parent of the derived type
  * @param right Potentially derived type from left
  * @return true if right derives from left. 
  */
 public static boolean derivesFrom(DerivableAggregateType left, Type right)
 {
   Type rightExpr = right;
   // Go through all parents of the right class to see if it matches
   // the left class at any time in the class parent hierarchy.
   while (rightExpr != null)
   {
     if (rightExpr.equals(left))
     {
       return true;
     }
     if ((rightExpr instanceof DerivableAggregateType)==false)
     {
       return false;
     }
     int derivesFromCount = ((DerivableAggregateType)rightExpr).getDerivesFromCount();
     for (int i=0; i < derivesFromCount; i++)
     {
       NamedTypeReference ref = ((DerivableAggregateType)rightExpr).getDerivesFrom(i);
       if (derivesFrom(left,ref.getType())==true)
       {
         return true;
       }
     }
     return false;
   }
   return false;
 }
 
 /** Resolves a type, such as a {@link TypeReference} to
  *  to its underlying datatype if it exists.
  * 
  * @param type [in] The type to resolve.
  * @return <code>null</code> if this type is 
  *  not a TypeReference pointing to a Datatype
  *  or is not a direct Datatype, otherwise the
  *  datatype instance.
  */
 public static Datatype getDatatype(Type type)
 {
   if (type == null)
   {
     return null;
   }
   if (type instanceof Datatype)
   {
     return (Datatype) type;
   }
   if (type instanceof TypeReference)
   {
     TypeReference typRef = (TypeReference) type;
     return getDatatype(typRef.getType());
   }
   return null;
 }

 /** Returns the minimum number of bits to represent
  *  the value as a two-complement binary value.
  * 
  * @param x The value to check
  * @return The number of bits required to represent this
  *   number.
  */
 public static int getBitsForNumber(long x)
 {
   int result = (int)Math.ceil(Math.log(x) / Math.log(2));
   return result;
 }
 
 /** Returns the minimum number of bits to represent
  *  the value as a two-complement binary value.
  * 
  * @param x The value to check
  * @return The number of bits required to represent this
  *   number.
  */
 public static int getBitsForNumber(Number x)
 {
   double v = x.doubleValue();
   int result = (int)Math.ceil(Math.log(v) / Math.log(2));
   return result;
 }
 

 /** Represents <code>Long.MIN_VALUE</code>-1. */
 public static final String LONG_MIN_EXCLUSIVE = "-9223372036854775809";
 /** Represents <code>Long.MAX_VALUE</code>+1. */
 public static final String LONG_MAX_EXCLUSIVE = "9223372036854775808";
 
 public static final BigInteger BIGINT_MIN_LONG = new BigInteger(LONG_MIN_EXCLUSIVE);
 public static final BigInteger BIGINT_MAX_LONG = new BigInteger(LONG_MAX_EXCLUSIVE);

 public static final BigDecimal BIGDECIMAL_MIN_LONG = new BigDecimal(LONG_MIN_EXCLUSIVE);
 public static final BigDecimal BIGDECIMAL_MAX_LONG = new BigDecimal(LONG_MAX_EXCLUSIVE);
 
 public static final BigDecimal BIGDECIMAL_MIN_INT = new BigDecimal(Integer.MIN_VALUE-1);
 public static final BigDecimal BIGDECIMAL_MAX_INT = new BigDecimal(Integer.MAX_VALUE+1);
 
 
  /** Returns true if the value can be represented
   *  within the bounds of an exact long value.  
   *  
  * @param value [in] The value to verify.
  * @return true if the value fits within a long value.
  */
 public static boolean isLongValueExact(BigInteger value)
 {
   // Within lower bounds.
   if (value.compareTo(BIGINT_MIN_LONG)==1)
   {
     return true;
   }
   // Within upper bounds
   if (value.compareTo(BIGINT_MAX_LONG)==-1)
   {
     return true;
   }
   return false;
 }
 
 
 /** Returns true if the value can be represented
  *  within the bounds of an exact long value.
  *  
  *  <p><em>Warning</em>: Due to the limitation of
  *  the Java 1.4 <code>BigDecimal</code> implementation
  *  integer values with a positive scale (digits after
  *  the points) will not be considered as whole
  *  numbers, except for the zero value.</p>
  *  
 * @param value [in] The value to verify.
 * @return true if the value fits within a long value and
 *   is exact.
 */
public static boolean isLongValueExact(BigDecimal value)
{
  // The number is scaled, so maybe an approximation.
  if (value.scale()>0)
  {
    if (value.signum()!=0)
      return false;
  }
  
  // Value is either zero or a integer value
  
  // Within lower bounds.
  if (value.compareTo(BIGDECIMAL_MIN_LONG)==1)
  {
    return true;
  }
  // Within upper bounds
  if (value.compareTo(BIGDECIMAL_MAX_LONG)==-1)
  {
    return true;
  }
  return false;
}

public static boolean isExact(Number value)
{
  BigDecimal bd = NumberComparator.toBigDecimal(value); 
  // The number is scaled, so maybe an approximation.
  if (bd.scale()>0)
  {
    if (bd.signum()!=0)
      return false;
  }
  return true;
}

/** Returns true if the value can be represented
 *  within the bounds of an exact int value.
 *  
 *  <p><em>Warning</em>: Due to the limitation of
 *  the Java 1.4 <code>BigDecimal</code> implementation
 *  integer values with a positive scale (digits after
 *  the points) will not be considered as whole
 *  numbers, except for the zero value.</p>
 *  
* @param value [in] The value to verify.
* @return true if the value fits within a int value and
*   is exact.
*/
public static boolean isIntegerValueExact(BigDecimal value)
{
 // The number is scaled, so maybe an approximation.
 if (value.scale()>0)
 {
   if (value.signum()!=0)
     return false;
 }
 
 // Value is either zero or a integer value
 
 // Within lower bounds.
 if (value.compareTo(BIGDECIMAL_MIN_INT)==1)
 {
   return true;
 }
 // Within upper bounds
 if (value.compareTo(BIGDECIMAL_MAX_INT)==-1)
 {
   return true;
 }
 return false;
}



 
 
}
