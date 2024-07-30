package com.optimasc.datatypes;

import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;


/** Interface to be implemented to do type checking.
 * 
 */
public interface TypeCheck
{
  /** Return the type to use for binary expression evaluation. This
   *  is usually used for arithmetic expression evaluation.
   * 
   * @param left [in] The left type
   * @param right [in] The right type
   * @return The type that should be used as result, or 
   *  <code>null</code> if the types are not supported.
   */
  public Type getCommonNumericType(Type left, Type right);
  
  /** Return the type to use for binary expression evaluation for
   *  integer based values such as as logical operators. 
   *  
   * @param left [in] The left type of the expression
   * @param right [in] The right type of the expression
   * @return The type that should be used as result, or <code>null</code>
   *   if this type is not supported.
   */
  public Type getCommonIntegerType(Type left, Type right);
  
  
  /** Generic type checking routines that will dispatch to appropriate protected 
   *  method to verify the type compatibility of types.
   * 
   * @param left [in] Left operand 
   * @param right [in] Right operand
   * @param explicit [in] Has explicit typecast been done 
   * @param extraInfo [in,out] Extra information for warnings or compile time warnings
   *    when types are not 100% compatible.
   * @return left or <code>null</code> if types are not compatible
   */
  public Type typeCheck(Type left, Type right, boolean explicit, TypeCheckResult extraInfo);
  
  
  /**
   * Returns if this type is an ordered type. An ordered type is defined as 
   * a type where its value space has a defined ordering hence each value
   * of that type can be compared with each other to give a relationship, such
   * as equality, less than, or greater then.</p>. This is the same definition 
   * as defined in ISO/IEC 11404:2007.
   * 
   * @param typ [in] The type to check.
   *          
   * @return true if the type represents a value space that has ordering.
   */
  public boolean isOrdered(Type typ);
  
  /** Returns if the specified type is a numeric type.  A type is said to be 
   *  numeric if its values are conceptually quantities (in some mathematical number
   *  system). There exists two types of numeric values, exact such as integer value
   *  and approximate such as floating point real values.
   *  
   * 
   * @param typ [in] The type to check.
   *          
   * @return true if the type represents a value that is numeric
   */
  public boolean isNumeric(Type typ);
  
  /** Returns true if the type represents a numeric
   *  type that is an approximation, such as complex
   *  or real numbers.
   * 
   * @param type The type to check 
   * @return true if this type represents an 
   *    approximation.
   */
  public boolean isApproximateNumeric(Type type);
  
  /** Returns true if the type represents a numeric
   *  type that is an exact number, such as integer
   *  values.
   * 
   * @param type The type to check 
   * @return true if this type represents an 
   *    exact numeric type.
   */
  public boolean isExactNumeric(Type type);
  
  /** Returns if the type is compatible
   *  with an integer type. This is usually
   *  the case when the type is a 
   *  {@link IntegralType}.
   * 
   * @param type [in] The type to check
   * @return true if the type is compatible
   *   with an integer type otherwise false.
   */
  public boolean isInteger(Type type);
  
}
