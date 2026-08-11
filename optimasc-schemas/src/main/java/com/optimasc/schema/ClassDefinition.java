package com.optimasc.definitions;

/** Represents the schema of a class that may
 *  contains named items. A class could represent
 *  a simple LDAP class, an structured document, where
 *  each element is represented as an attribute,
 *  or a SQL table definition where each named item
 *  is a column definition.
 *  
 *  <p>The following attributes are associated with a class, on top of
 *  those which are inherited:</p>
 *  
 *  <table>
 *    <tr><th>Attribute name</th><th>Mandatory</th><th>Type</th><th>Description</th></tr>
 *    <tr><td>{@link #KEY_KIND}</td><td>TRUE</td><td><code>ObjectClassKind</code></td><td>Indicates the type of class this represents, either abstract or concrete ("structural"). By 
 *       default the value is {@link ObjectClassKind#structuralClass} if this is not explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_MANDATORY_ATTRIBS}</td><td>FALSE</td><td><code>String[]</code></td><td>Represents the mandatory named items (attributes, properties, columns) associated with this class. By default the value is <code>null</code></td></tr>
 *    <tr><td>{@link #KEY_OPTIONAL_ATTRIBS}</td><td>FALSE</td><td><code>String[]</code></td><td>Represents the optional named items (attributes, properties, columns) associated with this class. By default the value is <code>null</code></td></td></tr>
 *    <tr><td>{@link #KEY_RESTRICTED}</td><td>TRUE</td><td><code>Boolean</code></td><td>Indicates if the instances of this class cannot be directly modified by user applications.
 *      By default, this value is <code>FALSE</code> if not explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_SUPERCLASS}</td><td>FALSE</td><td><code>String</code></td><td>
 *       Represents the super class name, or <code>null</code> if it is not defined.</td></tr>
 *    <tr><td>{@link #KEY_TYPE_JAVA_CLASS_NAME}</td><td>FALSE</td><td><code>String</code></td><td>
 *       Represents the Java Class name to instantiate for this class, or <code>null</code> if it is not defined. The
 *       public constructor of this class must be derived from {@link Entity} and must have two-parameter
 *       constructor with a namespaceURI and Name as string parameters.</td></tr>
 *  </table>
 *  
 *  
 * @author Carl Eric Codere
 *
 */
public interface ClassDefinition extends Definition
{
  
  /**
   * Represents an Enumeration for Class Kind
   */
  public static class ObjectClassKind
  {
      /**
       * A class kind that can be used to derive other classes. Objects are not
       * allowed to be of this class type.
       */
      public static final ObjectClassKind abstractClass = new ObjectClassKind("abstract", 0);
      /**
       * Structural object class kind.
       * 
       */
      public static final ObjectClassKind structuralClass = new ObjectClassKind(
          "structural", 1);
      /**
       * Auxiliary object class kind.
       */
      public static final ObjectClassKind auxiliaryClass = new ObjectClassKind("auxiliary",
          2);

      private final String name;
      private final int ordinal;

      private ObjectClassKind(String enumSymName, int value)
      {
        this.name = enumSymName;
        this.ordinal = value;
      }

      /**
       * Parse text into an element of this enumeration.
       *
       * @param aText
       *          takes one of the values allowed in the enumeration symbols.
       */
      public static ObjectClassKind valueOf(String aText)
      {
        for (int i=0; i < values.length; i++)
        {
          ObjectClassKind p = values[i];
          if (aText.equals(p.toString()))
          {
            return p;
          }
        }
        //this method is unusual in that IllegalArgumentException is
        //possibly thrown not at its beginning, but at its end.
        throw new IllegalArgumentException("Cannot parse into an element of Suit : '"
            + aText + "'");
      }

      public int compareTo(Object that)
      {
        return ordinal - ((ObjectClassKind) that).ordinal;
      }

      public String toString()
      {
        return name;
      }

      /**
       * These two lines are all that's necessary to export a List of VALUES.
       */
      private static final ObjectClassKind[] values = { abstractClass, structuralClass,
          auxiliaryClass };
  }
  
  /**
   * Key for the type of this class. This is equivalent to KIND in
   * ITU-T X.501. This value is mandatory. Internally the allowed
   * values are one of the following:
   * 
   * <ul>
   *  <li>{@link ObjectClassKind#abstractClass}</li>
   *  <li>{@link ObjectClassKind#structuralClass}</li>
   *  <li>{@link ObjectClassKind#auxiliaryClass}</li>
   * </ul>
   */
  public static final String KEY_KIND = "KIND";

  /**
   * Key for the parent of this class. This is equivalent to SUBCLASS OF in
   * ITU-T X.501. This value is optional.
   */
  public static final String KEY_SUPERCLASS = "SUP";
  /**
   * Key for the mandatory attributes of this object. This is equivalent to MUST
   * CONTAIN in ITU-T X.501. This value is optional.
   */
  public static final String KEY_MANDATORY_ATTRIBS = "MUST";
  /**
   * Key for the optional attributes of this object. This is equivalent to MAY
   * CONTAIN in ITU-T X.501. This value is optional.
   */
  public static final String KEY_OPTIONAL_ATTRIBS = "MAY";
  /**
   * Key for an array representing the allowed parents nodes of this class or
   * <code>null</code> if this objectClass does not allow any parents. This is
   * an extension to the X.500 and LDAP standards. This is the same definition
   * as the <code>possSuperiors</code> attribute in Active Directory.
   * 
   */
  public static final String KEY_ALLOWED_PARENTS = "possSuperiors";
  /**
   * Key for indicating if this is a restricted object. Restricted objects
   * cannot be modified by user applications, only by the system itself. This is
   * the same definition as the <code>@restricted</code> property in the
   * <code>ContentDirectory:v4</code> UPnP specification.
   * 
   */
  public static final String KEY_RESTRICTED = "restricted";
  
  
  /**
   * Key for the java class representation associated with this class. This
   * attribute is optional.
   */
  public static final String KEY_TYPE_JAVA_CLASS_NAME = "CLASS";
  
  
  /**
   * Return mandatory attributes required for this class. It 
   * does not return the mandatory attributes defined in 
   * the parent hierarchy of this class. In the case
   * there are no mandatory attributes, this shall return
   * an empty array.
   * 
   * @throws NamingException
   */
  public String[] getMandatoryAttributes();
  
  /** Returns the class names that are allowed to be parents of this
   *  specified class.  In the case
   *  there are no allowed parents, this shall return
   *  an empty array.
   * 
   * @return The list of class names allowed that can become 
   *   contexts of this subcontext.
   * @throws NamingException
   */
  public String[] getAllowedParents();
  
  /** Return the parent class name of this class.
   * 
   * @return The parent class of this class or <code>null</code>
   *  if this class has no parent.
   */
  public String getParent();
  
  /** Return true if this class definitions represents
   *  an abstract class. This is done by verifying
   *  that {@link #KEY_KIND} is equal to {@link #CLASS_ABSTRACT}.
   * 
   */
  public boolean isAbstract();
  
  /** Return the java class instance to represent this object. 
   *  The return value can  be <code>null</code> if it was not set.
   * 
   * @return
   */
  public Class getClz();
  
  
  
}
