package com.optimasc.schema;

import java.util.Set;

import com.optimasc.schema.Definition;

/** Represents the schema definition of a class — an object class,
 *  structured document type, SQL table, or any other named container
 *  that may hold named items.
 *
 *  <p>A {@code ClassDefinition} describes the rules governing a class
 *  of instances: which named items (attributes, properties, columns)
 *  are mandatory, what parent classes are permitted, and whether
 *  instances may be modified by user applications. It does not hold
 *  live data; it defines the schema contract that conforming instances
 *  must satisfy. The nature of those instances (for example whether
 *  they are runtime entities, table rows, or document nodes) is
 *  determined by the consuming runtime, not by this definition.</p>
 *
 *  <p>This model follows an <em>open-world</em> assumption for items:
 *  only the items listed in {@link #KEY_MANDATORY_ATTRIBUTES} are
 *  enforced. Any item not declared as mandatory is implicitly
 *  permitted, allowing instances to carry extension items without
 *  requiring the class definition to enumerate them. Consumers must
 *  therefore not reject unknown items encountered on an instance.
 *  This differs deliberately from the LDAP / X.500 MAY list, which acts
 *  as a closed whitelist; no equivalent of MAY CONTAIN is defined
 *  here.</p>
 *
 *  <p>In LDAP / X.500 terms, a {@code ClassDefinition} corresponds to
 *  an {@code ObjectClassDescription} as defined in RFC 4512 and
 *  ITU-T X.501. In SQL terms, it corresponds to a table definition.
 *  In XML Schema terms, it corresponds to a complex type with
 *  {@code ##any} wildcard content.</p>
 *
 *  <p>The constants below define a canonical attribute-key vocabulary
 *  for implementations that are backed by a keyed store (for example a
 *  {@code Map} or an attribute container). Such implementations should
 *  use these keys so that definitions are interchangeable across
 *  backends; implementations that store their fields by other means may
 *  ignore the keys and satisfy the typed accessors directly. The keys
 *  are a naming convention, not a mandated storage model. The following
 *  keys are defined in addition to those inherited from
 *  {@link Definition}:</p>
 *
 *  <table border="1">
 *    <tr><th>Key constant</th><th>Mandatory</th><th>Java type</th>
 *        <th>Description</th></tr>
 *    <tr><td>{@link #KEY_CLASS_KIND}</td><td>TRUE</td>
 *        <td>{@link ObjectClassKind}</td>
 *        <td>Whether this class is abstract, structural, or
 *            auxiliary. Equivalent to KIND in ITU-T X.501. Defaults
 *            to {@link ObjectClassKind#structuralClass} if not
 *            explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_SUPERCLASS}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Expanded name of the parent class. Equivalent to
 *            SUP / SUBCLASS OF in ITU-T X.501.</td></tr>
*    <tr><td>{@link #KEY_MANDATORY_ATTRIBUTES}</td><td>FALSE</td>
 *        <td>multi-valued {@code String}, ordered</td>
 *        <td>Expanded names or OIDs of items that must be present on
 *            every instance of this class. Multi-valued and distinct;
 *            declaration order is significant and is preserved.
 *            Equivalent to MUST CONTAIN in ITU-T X.501. Absent means
 *            none are declared. Items not listed here are implicitly
 *            permitted (open-world); there is no closed MAY
 *            list.</td></tr>
*    <tr><td>{@link #KEY_ALLOWED_PARENTS}</td><td>FALSE</td>
 *        <td>multi-valued {@code String}</td>
 *        <td>Expanded names or OIDs of classes that are permitted to
 *            be structural parents (containers) of instances of this
 *            class. Multi-valued and distinct; order is not
 *            significant. Extension to X.500 / LDAP; equivalent to
 *            the {@code possSuperiors} attribute in Active Directory.
 *            Absent means any parent is permitted.</td></tr>                        
 *    <tr><td>{@link #KEY_RESTRICTED}</td><td>TRUE</td>
 *        <td>{@code Boolean}</td>
 *        <td>Whether instances of this class are restricted from
 *            modification by user applications. Equivalent to the
 *            {@code @restricted} property in UPnP
 *            ContentDirectory:v4. Defaults to {@code FALSE}.</td></tr>
 *    <tr><td>{@link #KEY_TYPE_JAVA_CLASS_NAME}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Fully-qualified Java class name to instantiate for
 *            instances of this class. The named class must expose a
 *            public {@code (String namespaceURI, String name)}
 *            constructor; any further base-type requirement is imposed
 *            by the consuming runtime, not by this definition.</td></tr>
 *  </table>
 *
 *  @see Definition
 *  @see ItemDefinition
 *  @author Carl Eric Codere
 */
public interface ClassDefinition extends Definition
{
  // -----------------------------------------------------------------------
  // Nested enumeration-style class
  // -----------------------------------------------------------------------

  /** Indicates whether a class is abstract, structural, or auxiliary.
   *
   *  <p>This enumeration-style class is the portable equivalent of the
   *  KIND field defined in ITU-T X.501 and RFC 4512
   *  {@code ObjectClassDescription}:</p>
   *  <ul>
   *    <li>{@link #abstractClass} — may only be used as a base class;
   *        instances may not be direct members of an abstract
   *        class.</li>
   *    <li>{@link #structuralClass} — the normal, concrete class kind;
   *        every instance belongs to exactly one structural class in
   *        its inheritance chain.</li>
   *    <li>{@link #auxiliaryClass} — may be mixed into a structural
   *        class to add optional items; an instance may belong to
   *        zero or more auxiliary classes in addition to its
   *        structural class.</li>
   *  </ul>
   *
   *  <p>Use {@link #valueOf(String)} to parse the RFC 4512 string
   *  representation; use {@link #toString()} to obtain it.</p>
   */
  public static class ObjectClassKind implements Comparable<ObjectClassKind>
  {
    /** Abstract class kind. Instances may not be direct members of
     *  this class; it may only be used as a base for other classes.
     */
    public static final ObjectClassKind abstractClass =
        new ObjectClassKind("abstract", 0);

    /** Structural class kind. The normal, concrete class. Every
     *  instance belongs to exactly one structural class (or a subclass
     *  thereof) in its structural chain.
     */
    public static final ObjectClassKind structuralClass =
        new ObjectClassKind("structural", 1);

    /** Auxiliary class kind. May be mixed into a structural class to
     *  add optional items. An instance may belong to zero or more
     *  auxiliary classes in addition to its structural class.
     */
    public static final ObjectClassKind auxiliaryClass =
        new ObjectClassKind("auxiliary", 2);

    private static final ObjectClassKind[] VALUES = {
      abstractClass, structuralClass, auxiliaryClass
    };

    private final String name;
    private final int ordinal;

    private ObjectClassKind(String name, int ordinal)
    {
      this.name    = name;
      this.ordinal = ordinal;
    }

    /** Parses the RFC 4512 string representation of an object class
     *  kind into one of the pre-defined constants.
     *
     *  <p>Accepted values (case-sensitive):</p>
     *  <ul>
     *    <li>{@code "abstract"}</li>
     *    <li>{@code "structural"}</li>
     *    <li>{@code "auxiliary"}</li>
     *  </ul>
     *
     * @param text [in] The kind string. Must not be {@code null}.
     * @return The corresponding {@link ObjectClassKind} constant.
     * @throws IllegalArgumentException if {@code text} does not match
     *                                  any known kind value.
     */
    public static ObjectClassKind valueOf(String text)
    {
      for (int i = 0; i < VALUES.length; i++)
      {
        if (VALUES[i].name.equals(text))
          return VALUES[i];
      }
      throw new IllegalArgumentException(
          "Cannot parse '" + text + "' into an ObjectClassKind. "
          + "Expected one of: abstract, structural, auxiliary.");
    }

    /** Compares this kind to another by ordinal. */
    public int compareTo(ObjectClassKind that)
    {
      return this.ordinal - ((ObjectClassKind) that).ordinal;
    }

    /** Returns the RFC 4512 string representation of this kind
     *  (e.g. {@code "abstract"}, {@code "structural"},
     *  {@code "auxiliary"}).
     */
    public String toString()
    {
      return name;
    }
  }

  // -----------------------------------------------------------------------
  // Object-class name constant
  // -----------------------------------------------------------------------

  /** The canonical type-discriminator value for a
   *  {@link ClassDefinition}, suitable for use under
   *  {@link Definition#KEY_OBJECT_CLASS_NAME}.
   *
   *  <p>A generic consumer that traverses a collection of mixed
   *  {@link Definition} instances may compare this value against a
   *  definition's object-class name to distinguish class definitions
   *  from item definitions or type definitions without reflection.</p>
   *
   *  <p>The value ({@value}) is intentionally human-readable and maps
   *  to the Java interface role so that cross-language serialisers can
   *  recover the definition kind without additional metadata.</p>
   */
  String OBJECT_CLASS_NAME = "ClassDefinition";

  // -----------------------------------------------------------------------
  // Attribute key constants
  // -----------------------------------------------------------------------

  /** Key for the kind of this object class.
   *
   *  <p>The value stored under this key is an {@link ObjectClassKind}
   *  instance. Equivalent to KIND in ITU-T X.501 and RFC 4512.
   *  Mandatory; defaults to {@link ObjectClassKind#structuralClass}
   *  when not explicitly set.</p>
   */
  String KEY_CLASS_KIND = "classKind";

  /** Key for the parent (super) class of this class.
   *
   *  <p>The value stored under this key is a {@link String} containing
   *  the expanded name or OID of the parent class. Equivalent to SUP
   *  / SUBCLASS OF in ITU-T X.501 and RFC 4512. Optional; {@code null}
   *  means this class has no declared parent.</p>
   */
  String KEY_SUPERCLASS = "superClass";

  /** Key for the mandatory items of this class.
  *
  *  <p>The value stored under this key is a {@code String[]} of
  *  expanded names or OIDs of items that must be present on every
  *  instance of this class. Equivalent to MUST CONTAIN in ITU-T
  *  X.501. Optional; defaults to an empty array when not explicitly
  *  set.</p>
  *
  *  <p>Note: this key lists only the mandatory items declared
  *  directly on this class, not those inherited from parent classes.
  *  Declaration order is significant and is preserved, as it may
  *  affect serialization formats.</p>
  */  
  String KEY_MANDATORY_ATTRIBUTES = "mandatoryAttributes";

  /** Key for the allowed parent classes of this class.
  *
  *  <p>The value stored under this key is a {@code String[]} of
  *  expanded names or OIDs of classes that are permitted to act as
  *  structural parents (containers) of instances of this class.
  *  Extension to X.500 / LDAP; equivalent to the
  *  {@code possSuperiors} attribute in Microsoft Active Directory.
  *  Optional; an empty array means no parent constraint is declared.
  *  Order is not significant.</p>
  */  
  String KEY_ALLOWED_PARENTS = "allowedParents";

  /** Key for the restricted flag of this class.
   *
   *  <p>The value stored under this key is a {@link Boolean}.
   *  When {@code TRUE}, instances of this class may only be modified
   *  by the underlying system, not by user applications. Equivalent
   *  to the {@code @restricted} property in UPnP
   *  ContentDirectory:v4. Mandatory; defaults to {@code FALSE} when
   *  not explicitly set.</p>
   */
  String KEY_RESTRICTED = "restricted";

  /** Key for the fully-qualified Java class name to instantiate for
   *  instances belonging to this class.
   *
   *  <p>The value stored under this key is a {@link String} containing
   *  the canonical Java class name. The named class must expose a
   *  public two-parameter constructor of the form
   *  {@code (String namespaceURI, String name)}; any further
   *  base-type requirement is imposed by the consuming runtime, not by
   *  this definition. Optional; {@code null} means no specific Java
   *  type is associated.</p>
   */
  String KEY_TYPE_JAVA_CLASS_NAME = "javaClassName";

  // -----------------------------------------------------------------------
  // Typed accessors
  // -----------------------------------------------------------------------

  /** Returns the kind of this object class.
   *
   *  <p>Equivalent to the KIND field in RFC 4512
   *  {@code ObjectClassDescription}. Determines whether this class
   *  is abstract, structural, or auxiliary.</p>
   *
   * @return A non-null {@link ObjectClassKind} value. Defaults to
   *         {@link ObjectClassKind#structuralClass} when not
   *         explicitly set.
   */
  ObjectClassKind getKind();

  /** Returns {@code true} if this class is abstract.
   *
   *  <p>Convenience method equivalent to
   *  {@code getKind() == ObjectClassKind.abstractClass}. Abstract
   *  classes may not be directly instantiated; they serve only as
   *  bases for other class definitions.</p>
   *
   * @return {@code true} if this class is abstract,
   *         {@code false} otherwise.
   */
  boolean isAbstract();

  /** Returns the expanded name, or OID, of this class's superclass,
   *  or {@code null} if this class has no declared superclass.
   *
   *  <p>Equivalent to the SUP field in RFC 4512
   *  {@code ObjectClassDescription}. When non-null, a schema-aware
   *  consumer can resolve the name within its schema to obtain the
   *  parent {@link ClassDefinition} and its inherited mandatory and
   *  optional items.</p>
   *
   *  <p>Note: this describes the schema-level inheritance relationship
   *  between class definitions; it is independent of any runtime
   *  containment or parent-node relationship a consuming model may
   *  impose on instances.</p>
   *
   * @return The superclass name or OID, or {@code null} if no
   *         superclass is declared.
   */
  String getSuperClass();

  /** Returns the expanded names or OIDs of items that must be present
   *  on every instance of this class.
   *
   *  <p>Equivalent to the MUST field in RFC 4512
   *  {@code ObjectClassDescription}. This method returns only the
   *  mandatory items declared directly on this class; mandatory items
   *  inherited from parent classes are not included. A schema-aware
   *  consumer must walk the inheritance chain to collect all mandatory
   *  items.</p>
   *
   *  <p>Declaration order is significant and is preserved, as it may
   *  affect serialization formats. The returned set contains no
   *  duplicate values.</p>
   *
   *  <p>The returned set is an unmodifiable, insertion-order-preserving
   *  snapshot; any attempt to mutate it throws
   *  {@link java.lang.UnsupportedOperationException}.</p>
   *
   * @return A non-null, unmodifiable, insertion-ordered {@link Set} of
   *         distinct expanded-name or OID strings; possibly empty when
   *         no mandatory items are declared.
   */  
  Set<String> getMandatoryAttributes();

  /** Returns the expanded names or OIDs of classes that are permitted
   *  to act as structural parents of instances of this class.
   *
   *  <p>Extension to X.500 / LDAP; equivalent to the
   *  {@code possSuperiors} attribute in Microsoft Active Directory.
   *  When the set is empty, no parent constraint is declared and any
   *  class may act as a parent.</p>
   *
   *  <p>The returned set is unmodifiable; any attempt to mutate it
   *  throws {@link java.lang.UnsupportedOperationException}. Iteration
   *  order is not significant.</p>
   *
   * @return A non-null, unmodifiable {@link Set} of permitted parent
   *         expanded-name or OID strings; possibly empty when no
   *         constraint is declared.
   */  
  Set<String> getAllowedParents();

  /** Returns {@code true} if instances of this class may not be
   *  modified by user applications.
   *
   *  <p>Equivalent to the {@code @restricted} property in UPnP
   *  ContentDirectory:v4. When {@code true}, the underlying system
   *  alone may create, modify, or delete instances of this class.</p>
   *
   * @return {@code true} if instances are restricted from user
   *         modification, {@code false} otherwise (the default).
   */
  boolean isRestricted();

  /** Returns the Java class used to instantiate instances belonging to
   *  this class, or {@code null} if no specific Java type is
   *  associated.
   *
   *  <p>When non-null, the returned class exposes a public
   *  two-parameter constructor of the form
   *  {@code (String namespaceURI, String name)}. Any further
   *  base-type requirement on the returned class is imposed by the
   *  consuming runtime, not by this definition.</p>
   *
   * @return The {@link Class} to instantiate for instances of this
   *         object class, or {@code null} if not specified.
   */
  Class<?> getClz();
}