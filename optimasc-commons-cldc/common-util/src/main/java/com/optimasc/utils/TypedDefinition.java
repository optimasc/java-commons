package com.optimasc.utils;

public interface TypedDefinition extends Definition
{
  /**
   * Key to get the string representation syntax or type 
   * name associated with this definition. This value
   * is usually an OBJECT IDENTIFIER but this implementation
   * allowed other string values. This value 
   * is optional and is equivalent to LDAP-SYNTAX in X.501 and
   * represents the type of the attribute.
   */
  public static final String KEY_TYPE_NAME = "SYNTAX";
  
  /** Returns the syntax type declared for the associated element, object 
   *  or attribute, or <code>null</code> if unknown.
   *   
   * @return
   */
  public String getTypeName();
  
}
