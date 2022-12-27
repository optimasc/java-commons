/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.optimasc.datatypes;



/** 
 *
 * @author ccodere
 */
public class DatatypeException extends Exception
{
  private int code; 
  /** Exception is thrown when the value validated is not legal
   *  according to its datatype.
   */
  public static final int ILLEGAL_VALUE  = 0;
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
  
  public static void throwIt(int code, String s) throws DatatypeException 
  {
     throw new DatatypeException(code,s);
  }
  
  
}
