package com.optimasc.datatypes;

import omg.org.astm.type.NamedTypeReference;

import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.aggregate.DerivableAggregateType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.RealType;

/** General type utilities */
public class TypeUtilities
{
  /** Checks the bounds of left and right and returns true 
   *  if there may be an out of bounds if value is assigned to left. 
   * 
   * @param left The left type that may receive the value.
   * @param right The right type that is the source of the value.
   * @return true if an out of range value may occur.
   */
  public static boolean isOutOfBounds(BoundedRangeFacet left, BoundedRangeFacet right)
  {
    if ((left.getMaxInclusive() < right.getMaxInclusive())
        || (left.getMinInclusive() > right.getMinInclusive()))
    {
      return true;
    }
    return false;
  }
  
  protected static final String UNICODE_CHARSETS[] = { "UTF-8", "ISO-10646-UCS-2",
    "UCS-2", "ISO-10646-UCS-4", "UTF-7", "UTF-16BE", "UTF-16LE", "UTF-16", "UTF-32",
    "UTF-32BE", "UTF-32LE", "UCS-4" };

/**
 * Returns if the specified character set represents a unicode character set.
 * 
 * @param charSetName
 * @return true if this represents a Unicode character set.
 */
public static boolean isUnicodeCharset(String charSetName)
{
  for (int i = 0; i < UNICODE_CHARSETS.length; i++)
  {
    if (charSetName.equals(UNICODE_CHARSETS[i]))
    {
      return true;
    }
  }
  return false;
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

 
 /** Verifies if the right operand is  derived from the
  *  left  operand. 
  * 
  * @param left Parent of the derived type
  * @param right Potentially derived type from left
  * @return true if right derives from left. 
  */
 public static boolean derivesFrom(DerivableAggregateType left, DerivableAggregateType right)
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
     NamedTypeReference ref = ((DerivableAggregateType)rightExpr).getDerivesFrom();
     if (ref == null)
       return false;
     rightExpr = ref.getType();
   }
   return false;
 }


}
