/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.optimasc.datatypes;



/** Exception codes. The error code values should be equal in values
 *  than those defined in SQL2003 standard when possible.
 *
 * @author Carl Eric Codere.
 */
public class DatatypeException extends Exception
{
  private String code;
  
  /** SQL-2003 Feature not supported error code. */
  public static final String ERROR_FEATURE_NOT_SUPPORTED = "0A000";
  
  /** SQL-2003 Generic data exception with no-subclass. */
  public static final String ERROR_DATA_NO_SUBCLASS="22000";
  
  /** SQL-2003: Characters: The character specified is not allowed in this character repertoire. */
  public static final String ERROR_DATA_CHARACTER_NOT_REPERTOIRE = "22021";
  /** SQL-2003: Arrays: Trying to access an array element by invalid index. */
  public static final String ERROR_DATA_ARRAY_ELEMENT = "2202E";
  /** SQL-2003: Arrays: Operation on array would lead to truncation of array data. */
  public static final String ERROR_DATA_ARRAY_TRUNCATION = "2202F";
  /** SQL-2003: Datetime: Values of date time fields is outside of valid ranges. */
  public static final String ERROR_DATA_DATETIME_OVERFLOW = "22008";
  /** SQL-2003: Datetime: Invalid timezone offset value. */
  public static final String ERROR_DATA_DATETIME_INVALID_TZ = "22009";
  /** SQL-2003: Numeric: Division by zero. */
  public static final String ERROR_DATA_DIVIDE_ZERO = "22012";
  /** SQL-2003: Numeric: Numeric value is out of range */
  public static final String ERROR_DATA_NUMERIC_OUT_OF_RANGE = "22003";
  /** SQL-2003: General: Types are not compatible. */
  public static final String ERROR_DATA_TYPE_MISMATCH = "2200G";
  /** SQL-2003: Duration/Interval: Duration is outside supported range.
   *  Contrary to the SQL-2003 standard which can also raise this issue
   *  when there is loss of precision, in this definition here, it only
   *  means that the value is outside supported range. 
   * */
  public static final String ERROR_DATA_DURATION_OVERFLOW = "22015";
  

  
  // All others are proprietary and based on the last 2 digits of Delphi XE
  // Error codes
  
  
  public static final String ERROR_OPERATOR_NOT_APPLICABLE_TO_OPERAND = "22515";
  public static final String ERROR_BOUNDS_RANGE = "22511";
  public static final String ERROR_CONST_EXPRESSION_EXPECTED = "22526";
  public static final String ERROR_INVAID_TYPECAST = "22589";
  public static final String ERROR_ARITHMERIC_OVERFLOW = "22599";
  public static final String ERROR_UNDECLARED_IDENTIFIER = "22503";
  public static final String ERROR_ORDINAL_EXPECTED = "22501";
  public static final String ERROR_REAL_NUMBER_SYNTAX = "22553";
  public static final String ERROR_LEFT_CANNOT_BE_ASSIGNED = "22564";
  public static final String ERROR_EXPRESSION_MUST_BE_BOOLEAN = "22512";
  public static final String ERROR_EXPRESSION_MUST_BE_INTEGER = "22513";
  
  
  public DatatypeException(String code, String s)
  {
    super(s);
    this.code = code;
  }
  
  
  
  public String getCode()
  {
    return code;
  }



  public static void throwIt(String code, String s) throws DatatypeException 
  {
     throw new DatatypeException(code,s);
  }
  
  
}
