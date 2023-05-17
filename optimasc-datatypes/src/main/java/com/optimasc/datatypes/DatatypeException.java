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
  private int code; 
  /** Code is set when value does not match data type
   *  specification. This is the catch all generic error.
   */
  public static final int ERROR_ILLEGAL_VALUE  = 0x22000;
  
  /** Code is set when character is not allowed in this
   *  cahracter set.
   */
  public static final int ERROR_ILLEGAL_CHARACTER  = 0x22021;
  
  /** Code is set when the numeric value is out of range.
   */
  public static final int ERROR_NUMERIC_OUT_OF_RANGE  = 0x22003;
  /** Code is set when the string is not of correct length.
   */
  public static final int ERROR_STRING_ILLEGAL_LENGTH  = 0x22026;
  /** Code is set when the string will be truncated because
   *  its length is bigger than allowed length.
   */
  public static final int ERROR_STRING_TRUNCATION  = 0x22001;
  
  /** Code is set when a datetime is not within range.
   */
  public static final int ERROR_ILLEGAL_DATETIME  = 0x22007;
  
  
  
  /** Exception is thrown when adding a value or a symbol that
   *  is already present and duplicates are not allowed.
   */
  public static final int DUPLICATES_NOT_ALLOWED  = 1;
  
  private static final String value2str[]  = 
  {
    "Illegal value for this datatype."
  };
  
  public DatatypeException(int code, String s)
  {
    super(s);
    this.code = code;
  }
  
  
  
  public int getCode()
  {
    return code;
  }



  public static void throwIt(int code, String s) throws DatatypeException 
  {
     throw new DatatypeException(code,s);
  }
  
  
}
