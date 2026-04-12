package com.optimasc.definitions;

import java.math.BigInteger;
import java.util.GregorianCalendar;

import com.optimasc.definitions.ClassDefinition.ObjectClassKind;
import com.optimasc.text.NumericFormatters;
import com.optimasc.text.StandardDateFormatters;
import com.optimasc.text.StandardFormatters;

/**
 * Core definitions. Pre-defined type definitions, item definitions that may be
 * used by users.
 * 
 * 
 * 
 * 
 * @author Carl Eric Codere
 *
 */
public class CoreDefinitions
{
  /**
   * Basic type definitions. This includes and defines the most important and
   * minimum type definitions that can be useful.
   * 
   * <p>
   * The following type definitions are pre-defined and available for use:
   * </p>
   * <table>
   * <tr>
   * <th>Java Name</th>
   * <th>Name</th>
   * <th>ID</th>
   * <th>Java Object representation</th>
   * <th>Description</th>
   * </tr>
   * <tr>
   * <td>{@link #TYPE_BINARY}</td>
   * <td>OCTET STRING</td>
   * <td>{@link #ID_TYPE_OCTET_STRING}</td>
   * <td>byte[]</td>
   * <td>Binary data, represented as hexBinary</td>
   * </tr>
   * <tr>
   * <td>{@link #TYPE_BOOLEAN}</td>
   * <td>BOOLEAN</td>
   * <td>{@link #ID_TYPE_BOOLEAN}</td>
   * <td>java.lang.Boolean</td>
   * <td>A boolean value</td>
   * </tr>
   * <tr>
   * <td>{@link #TYPE_INTEGER}</td>
   * <td>INTEGER</td>
   * <td>{@link #ID_TYPE_INTEGER}</td>
   * <td>java.math.BigInteger</td>
   * <td>An integer value</td>
   * </tr>
   * <tr>
   * <td>{@link #TYPE_OID}</td>
   * <td>OBJECT IDENTIFIER</td>
   * <td>{@link #ID_TYPE_OBJECT_IDENTIFIER}</td>
   * <td>java.lang.String</td>
   * <td>An object identifier value, where components are digits</td>
   * </tr>
   * <tr>
   * <td>{@link #TYPE_STRING}</td>
   * <td>DirectoryString</td>
   * <td>{@link #ID_TYPE_STRING}</td>
   * <td>java.lang.String</td>
   * <td>A string value</td>
   * </tr>
   * <tr>
   * <td>{@link #TYPE_NCNAME}</td>
   * <td>NCName</td>
   * <td>{@link #ID_TYPE_NCNAME}</td>
   * <td>java.lang.String</td>
   * <td>A string value compatible with a subset of the XMLSchema NCName syntax</td>
   * </tr>
   * <tr>
   * <td>{@link #TYPE_JAVA_CLASS}</td>
   * <td>javaClass</td>
   * <td></td>
   * <td>java.lang.Class</td>
   * <td>A java class representation</td>
   * </tr>
   * </table>
   * 
   * */
  public static class Types
  {

    /** Object Identifier for syntax of a BOOLEAN */
    public static final String ID_TYPE_BOOLEAN = "1.3.6.1.4.1.1466.115.121.1.7";
    protected static final String NAME_BOOLEAN = "BOOLEAN";

    /** Java class of a BOOLEAN */
    public static final Class CLASS_BOOLEAN = Boolean.class;

    /**
     * Type definition for BOOLEAN type. A Boolean type is represented by a
     * <code>java.lang.Boolean</code> java object instance. It supports input as
     * TRUE or FALSE as string representation without case sensitivity. The
     * canonical representation on output is TRUE or FALSE all in upper-case.
     */
    public static final DefaultTypeDefinition TYPE_BOOLEAN =
    /* ID, NamespaceURI, qualifiedName, Description, Class, formatter */
    new DefaultTypeDefinition(ID_TYPE_BOOLEAN, null, NAME_BOOLEAN, NAME_BOOLEAN,
        CLASS_BOOLEAN, new StandardFormatters.BooleanConverter());

    /** Object Identifier for syntax of a NCName */
    public static final String ID_TYPE_NCNAME = "1.3.6.1.4.1.61799.5.40.26.6";
    protected static final String NAME_NCNAME = "NCName";
    /** Java class of a NCVName */
    public static final Class CLASS_NCNAME = String.class;
    /**
     * Type definition for NCName type. A NCName type is represented by a
     * <code>java.lang.String</code> java object instance. It is a subset of the
     * <code>NCName</code> type in XMLSchema.
     */
    public static final DefaultTypeDefinition TYPE_NCNAME =
    /* ID, NamespaceURI, qualifiedName, Description, Class, formatter */
    new DefaultTypeDefinition(ID_TYPE_NCNAME, null, NAME_NCNAME, NAME_NCNAME,
        CLASS_NCNAME, new StandardFormatters.NCNameConverter());

    /**
     * Object Identifier for syntax of a string, equivalent to
     * UnboundedDirectoryString in X.520
     */
    public static final String ID_TYPE_STRING = "1.3.6.1.4.1.1466.115.121.1.15";
    protected static final String NAME_STRING = "DirectoryString";
    /** Java class of a string, equivalent to UnboundedDirectoryString in X.520 */
    public static final Class CLASS_STRING = String.class;
    /**
     * Type definition for generic string type. This is a generic type
     * representing any unicode string. This is represented by a
     * <code>java.lang.String</code> java object.
     */
    public static final DefaultTypeDefinition TYPE_STRING =
    /* ID, NamespaceURI, qualifiedName, Description, Class, formatter */
    new DefaultTypeDefinition(ID_TYPE_STRING, null, NAME_STRING, "Directory String",
        CLASS_STRING, new StandardFormatters.StringConverter());

    /** Object Identifier for syntax of INTEGER */
    public static final String ID_TYPE_INTEGER = "1.3.6.1.4.1.1466.115.121.1.27";
    protected static final String NAME_INTEGER = "INTEGER";
    /** Java class for Integer */
    public static final Class CLASS_INTEGER = BigInteger.class;
    /**
     * Type definition for INTEGER type. This is a generic type representing an
     * integer numeric value. This is represented by a
     * <code>java.math.BigInteger</code> java object.
     */
    public static final DefaultTypeDefinition TYPE_INTEGER =
    /* NamespaceURI, qualifiedName, Description, Class, formatter */
    new DefaultTypeDefinition(
    /* ID, NamespaceURI, qualifiedName, Description, Class, formatter */
    ID_TYPE_INTEGER, null, NAME_INTEGER, NAME_INTEGER, CLASS_INTEGER,
        new NumericFormatters.IntegerCanonicalConverter());

    /** Object Identifier for syntax of an OBJECT IDENTIFIER */
    public static final String ID_TYPE_OBJECT_IDENTIFIER = "1.3.6.1.4.1.1466.115.121.1.38";
    protected static final String NAME_OID = "OBJECT IDENTIFIER";
    /** Java class of an OBJECT IDENTIFIER */
    public static final Class CLASS_OID = String.class;
    /** Type definition for OBJECT IDENTIFIER type. */
    public static final DefaultTypeDefinition TYPE_OID =
    /* ID, NamespaceURI, qualifiedName, Description, Class, formatter */
    new DefaultTypeDefinition(ID_TYPE_OBJECT_IDENTIFIER, null, NAME_OID, NAME_OID,
        CLASS_OID, new StandardFormatters.OIDConverter());

    /** Object Identifier for syntax of an array of bytes / OCTET STRING */
    public static final String ID_TYPE_OCTET_STRING = "1.3.6.1.4.1.1466.115.121.1.40";
    protected static final String NAME_OCTETSTRING = "OCTET STRING";
    /** Java class of a string composed of numeric values only / NumericString */
    public static final Class CLASS_OCTETSTRING = byte[].class;
    /**
     * Type definition for an OCTET STRING type. This is a generic type
     * representing binary data. This is represented by a <code>byte[]</code>
     * java object.
     */
    public static final DefaultTypeDefinition TYPE_BINARY =
    /* ID, NamespaceURI, qualifiedName, Description, Class, formatter */
    new DefaultTypeDefinition(ID_TYPE_OCTET_STRING, null, NAME_OCTETSTRING,
        "Octet String", CLASS_OCTETSTRING, new StandardFormatters.HexBinaryConverter());

    /** Object Identifier for syntax of a class type */
    //    public static final String ID_TYPE_OCTET_STRING = "1.3.6.1.4.1.1466.115.121.1.40";
    protected static final String NAME_JAVA_CLASS = "javaClass";
    /** Java class of a string composed of numeric values only / NumericString */
    public static final Class CLASS_JAVA_CLASS = Class.class;
    /**
     * Type definition for a Java Class type. This is a generic type
     * representing a fully qualified java class name. This is represented by a
     * <code>Class</code> java object.
     */
    public static final DefaultTypeDefinition TYPE_JAVA_CLASS =
    /* ID, NamespaceURI, qualifiedName, Description, Class, formatter */
    new DefaultTypeDefinition(null, null, NAME_JAVA_CLASS, "Java Class",
        CLASS_JAVA_CLASS, null);//StandardFormatters.ClassConverter.getInstance());  

    /** Object Identifier for syntax of a GeneralizedTime */
    public static final String ID_TYPE_GENERALIZEDTIME = "1.3.6.1.4.1.1466.115.121.1.24";
    /** Object Identifier for syntax of a GeneralizedTime */
    protected static final String NAME_GENERALIZEDTIME = "GeneralizedTime";
    /** Java class of a string composed of numeric values only / NumericString */
    public static final Class CLASS_GENERALIZEDTIME = GregorianCalendar.class;
    /**
     * Type definition for an ASN1 GeneralizedTime. This is a generic type
     * representing a date/time. This is represented by a
     * <code>java.util.GregorianCalendar</code> java object.
     */
    public static final DefaultTypeDefinition TYPE_GENERALIZEDTIME =
    /* ID, NamespaceURI, qualifiedName, Description, Class, formatter */
    new DefaultTypeDefinition(ID_TYPE_GENERALIZEDTIME, null, NAME_GENERALIZEDTIME,
        "Generalized Time", CLASS_GENERALIZEDTIME,
        new StandardDateFormatters.GeneralizedTimeConverter());//StandardFormatters.ClassConverter.getInstance())

  }

  /**
   * Basic Attribute definitions. This includes and defines the attributes that
   * are used and defined in the different definition classes defined in this
   * package such as {@link Entity}, {@link Definition}, {@link ClassDefinition}
   * and {@link TypeDefinition}.
   * 
   * <p>
   * The following attribute definitions are pre-defined and available for use:
   * </p>
   * <table>
   * <tr>
   * <th>Java Name</th>
   * <th>Name</th>
   * <th>ID</th>
   * <th>Description</th>
   * </tr>
   * <tr>
   * <td>{@link #ATTRIBUTE_NAME}</td>
   * <td>{@link Definition#KEY_NAME}</td>
   * <td>{@link #ID_ATTRIBUTE_NAME}</td>
   * <td>Unique qualified name attribute, descriptor in LDAP identifying the
   * definition</td>
   * </tr>
   * <tr>
   * <td>{@link #ATTRIBUTE_DESCRIPTION}</td>
   * <td>{@link Definition#KEY_DESCRIPTION}</td>
   * <td>{@link #ID_ATTRIBUTE_DESCRIPTION}</td>
   * <td>Description attribute, describes the definition</td>
   * </tr>
   * <tr>
   * <td>{@link #ATTRIBUTE_OBJECT_IDENTIFIER}</td>
   * <td>{@link Definition#KEY_ID}</td>
   * <td>{@link #ID_ATTRIBUTE_OBJECT_IDENTIFIER}</td>
   * <td>Object identifier, unique identification of the definition</td>
   * </tr>
   * <tr>
   * <td>{@link #ATTRIBUTE_DISPLAY_NAME}</td>
   * <td>{@link Definition#KEY_DISPLAY_NAME}</td>
   * <td>{@link #ID_ATTRIBUTE_DISPLAY_NAME}</td>
   * <td>Display name attribute, display label associated with the definition</td>
   * </tr>
   * <tr>
   * <td>{@link #ATTRIBUTE_NAME_NS_URI}</td>
   * <td>{@link Definition#KEY_NAME_NS_URI}</td>
   * <td></td>
   * <td>Namespace URI attribute, defines the namespace URI used associated with
   * the name.</td>
   * </tr>
   * <tr>
   * <td>{@link #ATTRIBUTE_OBSOLETE}</td>
   * <td>{@link Definition#KEY_OBSOLETE}</td>
   * <td></td>
   * <td>Obsolete attribute, indicates if the definition is obsolete or not.</td>
   * </tr>
   * </table>
   * 
   * 
   * @author Carl Eric Codere
   *
   */
  public static class Attributes
  {
    /** Object Identifier for the 'commonName' attribute */
    public static final String ID_ATTRIBUTE_NAME = "2.5.4.3";
    /** Object Identifier for the 'description' attribute */
    public static final String ID_ATTRIBUTE_DESCRIPTION = "2.5.4.13";
    /** Object Identifier for the 'Object Identifier' attribute */
    public static final String ID_ATTRIBUTE_OBJECT_IDENTIFIER = "2.5.4.106";
    /** Object Identifier for the 'displayName' attribute */
    public static final String ID_ATTRIBUTE_DISPLAY_NAME = "2.16.840.1.113730.3.1.241";
    /** Object Identifier for the 'objectClass' attribute */
    public static final String ID_ATTRIBUTE_OBJECT_CLASS_NAME = "2.5.4.0";

    /**
     * Attribute definition for the 'commonName' ITU-T X.520 attribute. This defines
     * the {@link Definition#KEY_NAME} value.
     */
    public static final ItemDefinition ATTRIBUTE_NAME = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        ID_ATTRIBUTE_NAME, null, Entity.KEY_NAME, "commonName", Types.ID_TYPE_STRING,
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /** The definition of the "objectClass" attribute */
    public static final ItemDefinition ATTRIBUTE_OBJECT_CLASS = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        ID_ATTRIBUTE_OBJECT_CLASS_NAME, null, Entity.KEY_OBJECT_CLASS_NAME,
        "Object class of this object", Types.ID_TYPE_STRING,
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition for the 'namespaceURI' attribute. This defines the
     * {@link Definition#KEY_NAME_NS_URI} value.
     */
    public static final ItemDefinition ATTRIBUTE_NAME_NS_URI = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, Entity.KEY_NAME_NS_URI, "Name Namespace URI", Types.ID_TYPE_STRING,
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition for the 'obsolete' attribute. This defines the
     * {@link Definition#KEY_OBSOLETE} value.
     */
    public static final ItemDefinition ATTRIBUTE_OBSOLETE = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, Definition.KEY_OBSOLETE, "Definition is obsolete",
        Types.ID_TYPE_BOOLEAN, DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition for the 'x-origin' attribute. This defines the
     * {@link Definition#KEY_ORIGIN} value.
     */
    public static final ItemDefinition ATTRIBUTE_ORIGIN = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, Definition.KEY_ORIGIN, "Origin of this definition",
        Types.ID_TYPE_STRING, DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition for the 'description' ITU-T X.520 attribute. This
     * defines the {@link Definition#KEY_DESCRIPTION} value.
     */
    public static final ItemDefinition ATTRIBUTE_DESCRIPTION = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        ID_ATTRIBUTE_DESCRIPTION, null, Definition.KEY_DESCRIPTION, "Description",
        Types.ID_TYPE_STRING + "{" + Integer.toString(Definition.DESC_MAX_LENGTH) + "}",
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition for the 'Object Identifier' ITU-T X.520 attribute.
     * This defines the {@link Definition#KEY_ID} value.
     */
    public static final ItemDefinition ATTRIBUTE_OBJECT_IDENTIFIER = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        ID_ATTRIBUTE_OBJECT_IDENTIFIER, null, Entity.KEY_ID, "Object Identifier",
        Types.ID_TYPE_OBJECT_IDENTIFIER, DefaultItemDefinition.VALUE_TYPE_SINGLE, null,
        false);

    /**
     * Attribute definition for the 'displayName' IETF RFC 2798 used as a
     * friendly name to display to the user. This defines the
     * {@link Definition#KEY_DISPLAY_NAME} value.
     */
    public static final ItemDefinition ATTRIBUTE_DISPLAY_NAME = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        ID_ATTRIBUTE_DISPLAY_NAME, null, Definition.KEY_DISPLAY_NAME, "Display name",
        Types.ID_TYPE_STRING, DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /*------------------ Type definitions attributes. ---------------------*/

    /**
     * Attribute definition for the 'formatter' used to format data to correct
     * java object instance. This is a fully qualified name of a class.
     * {@link TypeDefinition#KEY_FORMATTER} value.
     */
    public static final ItemDefinition ATTRIBUTE_FORMATTER_NAME = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, TypeDefinition.KEY_FORMATTER, "Formatter class name",
        Types.NAME_JAVA_CLASS, DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition for the 'Class' used to indicate the fully qualify
     * named value to represent a value for this type. See
     * {@link TypeDefinition#KEY_TYPE_JAVA_CLASS_NAME} value.
     */
    public static final ItemDefinition ATTRIBUTE_TYPE_JAVA_CLASS_NAME = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, TypeDefinition.KEY_TYPE_JAVA_CLASS_NAME,
        "Type definition associated class name for value", Types.ID_TYPE_STRING,
        DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /*------------------ Item definitions attributes. ---------------------*/

    /**
     * Attribute definition to be used to indicate that the item's definition
     * values are read-only. See {@link ItemDefinition#KEY_READONLY} value.
     */
    public static final ItemDefinition ATTRIBUTE_READ_ONLY = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, ItemDefinition.KEY_READONLY, "Read-only attribute value",
        Types.ID_TYPE_BOOLEAN, DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition to be used to indicate that the item's definition
     * values's types. See {@link ItemDefinition#KEY_TYPE_NAME} value.
     */
    public static final ItemDefinition ATTRIBUTE_TYPE_NAME = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, ItemDefinition.KEY_TYPE_NAME, "Type name of values",
        Types.ID_TYPE_STRING, DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition to be used to indicate that the maximum allowed
     * length of these values. This is only applicable to sequences (character
     * strings or octet strings). See {@link ItemDefinition#KEY_MAX_VALUE_LENGTH}
     * value.
     */
    public static final ItemDefinition ATTRIBUTE_MAX_VALUE_LENGTH = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, ItemDefinition.KEY_MAX_VALUE_LENGTH, "The maximum length allowed",
        Types.ID_TYPE_INTEGER, DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);

    /**
     * Attribute definition to be used to indicate the allowed value syntaxes.
     * See {@link ItemDefinition#KEY_VALUE_TYPE} value.
     */
    public static final ItemDefinition ATTRIBUTE_VALUE_TYPE = new DefaultItemDefinition(
        // Unique Identifier, namespaceURI, qualifiedName, description, typeName, multipleValueType,
        // contextType, readOnly
        null, null, ItemDefinition.KEY_VALUE_TYPE, "The value type",
        Types.ID_TYPE_STRING, DefaultItemDefinition.VALUE_TYPE_SINGLE, null, false);
  }

  /**
   * Basic class definitions. This includes and defines the following classes:
   * {@link ClassDefinition}, {@link ItemDefinition} and {@link TypeDefinition}.
   * 
   * <p>
   * The following class definitions are pre-defined and available for use:
   * </p>
   * <table>
   * <tr>
   * <th>Java Name</th>
   * <th>Name</th>
   * <th>ID</th>
   * <th>Description</th>
   * </tr>
   * <tr>
   * <td>{@link CoreDefinitions.Classes#CLASS_TOP}</td>
   * <td>Top</td>
   * <td>{@link #ID_CLASS_TOP}</td>
   * <td>Root abstract class</td>
   * </tr>
   * <tr>
   * <td>{@link CoreDefinitions.Classes#CLASS_ATTRIBUTE_DEFINTION}</td>
   * <td>AttributeDefinition</td>
   * <td>null</td>
   * <td>Attribute definition</td>
   * </tr>
   * <tr>
   * <td>{@link CoreDefinitions.Classes#CLASS_CLASS_DEFINTION}</td>
   * <td>ClassDefinition</td>
   * <td>null</td>
   * <td>Class definition</td>
   * </tr>
   * <tr>
   * <td>{@link CoreDefinitions.Classes#CLASS_SYNTAX_DEFINTION}</td>
   * <td>SyntaxDefinition</td>
   * <td>null</td>
   * <td>Type definition</td>
   * </tr>
   * </table>
   * 
   * 
   * @author Carl Eric Codere
   *
   */
  public static class Classes
  {
    public static final String NAME_CLASS_TOP = "top";
    public static final String ID_CLASS_TOP = "2.5.6.0";
    /** Default root class definition as defined in ITU-T X.501 */
    public static final ClassDefinition CLASS_TOP = new DefaultClassDefinition(null, // parentClass
        ID_CLASS_TOP, // ID
        null, // namespaceURI
        NAME_CLASS_TOP, // qualifiedName
        null, // allowedChildren = null, none allowed
        new String[] { Definition.KEY_OBJECT_CLASS_NAME }, // mandatoryAttribs
        ObjectClassKind.abstractClass.toString(), // class kind
        null, // java class name    
        "Root class");

    public static final String NAME_CLASS_CLASS_DEFINITION = "ClassDefinition";
    /** Default class definition for classes */
    public static final ClassDefinition CLASS_CLASS_DEFINTION = new DefaultClassDefinition(
        null, // parentClass
        null, // ID
        null, // namespaceURI
        NAME_CLASS_CLASS_DEFINITION, // qualifiedName
        null, // allowedChildren = null, none allowed
        new String[] { Definition.KEY_NAME, Definition.KEY_OBJECT_CLASS_NAME }, // mandatoryAttribs
        ObjectClassKind.structuralClass.toString(), // class kind
        DefaultClassDefinition.class.getName(), // java class name    
        "Class definition");

    public static final String NAME_CLASS_ATTRIBUTE_DEFINITION = "AttributeDefinition";
    /** Default class definition for an attribute */
    public static final ClassDefinition CLASS_ATTRIBUTE_DEFINTION = new DefaultClassDefinition(
        null, // parentClass
        null, // ID
        null, // namespaceURI
        NAME_CLASS_ATTRIBUTE_DEFINITION, // qualifiedName
        null, // allowedChildren = null, none allowed
        new String[] { Definition.KEY_NAME, Definition.KEY_OBJECT_CLASS_NAME,
            ItemDefinition.KEY_TYPE_NAME }, // mandatoryAttribs
        ObjectClassKind.structuralClass.toString(), // class kind
        DefaultItemDefinition.class.getName(), // java class name    
        "Attribute definition");

    public static final String NAME_CLASS_SYNTAX_DEFINITION = "SyntaxDefinition";
    /** Default class definition for an attribute */
    public static final ClassDefinition CLASS_SYNTAX_DEFINTION = new DefaultClassDefinition(
        null, // parentClass
        null, // ID
        null, // namespaceURI
        NAME_CLASS_SYNTAX_DEFINITION, // qualifiedName
        null, // allowedChildren = null, none allowed
        new String[] { Definition.KEY_NAME, Definition.KEY_OBJECT_CLASS_NAME }, // mandatoryAttribs
        ObjectClassKind.structuralClass.toString(), // class kind
        DefaultTypeDefinition.class.getName(), // java class name    
        "Type definition");

  }

  /** Default schema registry populated with base definitions. */
  private static EntityRegistry schemaRegistry; // = createRegistry();

  public static EntityRegistry getSchemaRegistry()
  {
    if (schemaRegistry == null)
    {
      EntityRegistry registry = new EntityRegistry();
      registry.add(Types.TYPE_BINARY);
      registry.add(Types.TYPE_BOOLEAN);
      registry.add(Types.TYPE_INTEGER);
      registry.add(Types.TYPE_JAVA_CLASS);
      registry.add(Types.TYPE_NCNAME);
      registry.add(Types.TYPE_OID);
      registry.add(Types.TYPE_STRING);
      registry.add(Types.TYPE_GENERALIZEDTIME);
      registry.add(Attributes.ATTRIBUTE_DESCRIPTION);
      registry.add(Attributes.ATTRIBUTE_DISPLAY_NAME);
      registry.add(Attributes.ATTRIBUTE_FORMATTER_NAME);
      registry.add(Attributes.ATTRIBUTE_MAX_VALUE_LENGTH);
      registry.add(Attributes.ATTRIBUTE_NAME);
      registry.add(Attributes.ATTRIBUTE_NAME_NS_URI);
      registry.add(Attributes.ATTRIBUTE_OBJECT_CLASS);
      registry.add(Attributes.ATTRIBUTE_OBJECT_IDENTIFIER);
      registry.add(Attributes.ATTRIBUTE_OBSOLETE);
      registry.add(Attributes.ATTRIBUTE_ORIGIN);
      registry.add(Attributes.ATTRIBUTE_READ_ONLY);
      registry.add(Attributes.ATTRIBUTE_TYPE_JAVA_CLASS_NAME);
      registry.add(Attributes.ATTRIBUTE_TYPE_NAME);
      registry.add(Attributes.ATTRIBUTE_VALUE_TYPE);
      registry.add(Classes.CLASS_ATTRIBUTE_DEFINTION);
      registry.add(Classes.CLASS_CLASS_DEFINTION);
      registry.add(Classes.CLASS_SYNTAX_DEFINTION);
      registry.add(Classes.CLASS_TOP);
      schemaRegistry = registry;
    }
    return schemaRegistry;
  }

}
