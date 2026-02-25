package com.optimasc.definitions;

import java.text.Format;


/** Represents type information for a data type definition.
 *  
 *  <p>A type definition specifies how values of this type are represented
 *  in the Java language through a {@link Class} object, and optionally
 *  provides a {@link java.text.Format} for parsing and formatting string representations
 *  of values (equivalent to LDAP-SYNTAX in ITU X.520).</p>
 *  
 *  <p>This interface extends {@link Entity}, inheriting all entity attributes.
 *  The following additional attributes are specific to type definitions:</p>
 *  
 *  <table>
 *    <tr><th>Attribute name</th><th>Mandatory</th><th>Type</th><th>Description</th></tr>
 *    <tr><td>{@link #KEY_TYPE_JAVA_CLASS_NAME}</td><td>TRUE</td><td><code>String</code></td>
 *        <td>The Java class name used to represent values of this type in memory. If not specified, this value will be <code>java.lang.String</code></td></tr>
 *    <tr><td>{@link #KEY_FORMATTER}</td><td>FALSE</td><td><code>Format</code></td>
 *        <td>The formatter used to parse strings into Java objects and format Java objects into strings.</td></tr>
 *  </table>
 *  
 * @author Carl Eric Codere
 */
public interface TypeDefinition extends Definition
{
  /**
   * Key for the java class representation associated with this syntax. This
   * attribute is mandatory.
   */
  public static final String KEY_TYPE_JAVA_CLASS_NAME = "CLASS";
  
  
  /**
   * Key for the formatter instance associated with this type definition.
   * The value stored under this key is a {@link java.text.Format} instance
   * used to parse string representations into Java objects and format
   * Java objects into string representations. This attribute is optional.
   */  
  public static final String KEY_FORMATTER = "FORMATTER";
  
  
  /** Returns the formatter used to parse string representations of this type 
   *  into Java objects and to format Java objects into string representations.
   * 
   * @return The {@link java.text.Format} formatter, or <code>null</code> if 
   *         no formatter is associated with this type.
   */  
  public Format getFormatter();
  
  
  /** Returns the Java class used to represent values of this type in memory.
   *  This value is converted from the java class string representation.
   * 
   * @return This value is never <code>null</code>
   *         for valid type definitions.
   */  
  public Class getClz();

}
