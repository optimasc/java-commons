package com.optimasc.datatypes;

import java.text.ParseException;

import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a generic datatype. This is the base class for all possible
 *  datatypes. It has several important properties:
 *  
 *  <ul>
 *  <li>{@link #getClassType()} returns the class associated with the value of this type. </li>
 *  <li>{@link #getObjectType()} returns an instance of the object associated with the
 *    value of this type, except if the <code>EnumerationFacet</code> is available
 *    and an enumeration is set, then it returns an array of the possible values
 *    for this datatype.
 *  </ul>
 * 
 * @author Carl Eric Codere
 */
public abstract class Datatype extends Type
{
  
  
  // Internal possible datatypes with 

  // Derived from java.sql.types
  public static final int ARRAY = 2003; // Array
  public static final int BIGINT = -5; // Unsupported
  public static final int BINARY = -2; // byte[]
  public static final int BIT = -7; // Unsupported
  public static final int BLOB = 2004; // byte[]
  public static final int BOOLEAN = 16; // Boolean
  public static final int CHAR = 1; // Character
  public static final int CLOB = 2005; // String
  public static final int DATALINK = 70;
  public static final int DATE = 91; // GregorianDateTime
  public static final int DECIMAL = 3;
  public static final int DISTINCT = 2001;
  public static final int DOUBLE = 8; // Double
  public static final int FLOAT = 6; // Float 
  public static final int INTEGER = 4; // Integer
  public static final int JAVA_OBJECT = 2000;
  public static final int LONGVARBINARY = -4;
  public static final int LONGVARCHAR = -1;
  public static final int NULL = 0;
  public static final int NUMERIC = 2;
  public static final int OTHER = 1111;
  public static final int REAL = 7; // Double
  public static final int REF = 2006;
  public static final int SMALLINT = 5; // Short
  public static final int STRUCT = 2002;
  public static final int TIME = 92; // GregorianDateTime
  public static final int TIMESTAMP = 93; // GregorianDateTime  
  public static final int TINYINT = -6; // Byte
  public static final int VARBINARY = -3; // byte[]
  public static final int VARCHAR = 12; // String
  // Extensions 
  public static final int ENUM = 10000;


  protected String name;

  protected int type;



  public Datatype(int type, boolean ordered)
  {
    super(ordered);
    this.type = type;
  }

  public Datatype(String name, String comment, int type, boolean ordered)
  {
    super(ordered);
    this.name = name;
    this.comment = comment;
    this.type = type;
  }

  /**
   * Returns the main instance of this object associated with this datatype.
   * 
   */
  public abstract Object getObjectType();

  /** Validates the value of this value represented as the specified
   *  {@link #getObjectType()} value and throws an exception if it does
   *  meet the facet requirements.   
   * 
   * @param value The value specified in the correct object instance.
   * @throws IllegalArgumentException Thrown when the datatype
   *  is not of the correct class type.
   * @throws DatatypeException When the validation on facets and constraints fails.
   */
  public abstract void validate(java.lang.Object value) throws IllegalArgumentException, DatatypeException;


  /** Returns the name of this datatype. */
  public String getName()
  {
    return name;
  }

  public int getType()
  {
    return type;
  }


  public String toString()
  {
    return name;
  }


  public void setName(String name)
  {
    this.name = name;
  }
  

}
