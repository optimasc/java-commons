package com.optimasc.schema;

/** Represents any named, described concept that benefits from a
 *  standardised identity and a human-readable description.
 *
 *  <p>A {@code Definition} carries the descriptive identity shared by
 *  many kinds of named things: a local name, an optional namespace and
 *  expanded (IRI) name, an optional standardised identifier (OID), a
 *  description, an obsolescence flag, and an origin. It is deliberately
 *  free of any dependency on a particular runtime, storage, or
 *  container model, so the same contract can describe a schema element,
 *  a controlled-vocabulary term, a dictionary entry, a code-list entry,
 *  a glossary item, or a metadata element from any descriptive
 *  standard.</p>
 *
 *  <p>Schema modelling is the primary use case. Concrete schema
 *  sub-interfaces refine this contract for specific roles: an item
 *  (attribute, property, or column) definition, a data-type definition,
 *  and a class (object-class or table) definition. These typically live
 *  in their own packages and may carry additional dependencies; this
 *  base interface intentionally does not.</p>
 *
 *  <p>As a non-schema example, a {@code Definition} may model a
 *  dictionary entry, where {@link #getLocalName()} is the headword,
 *  {@link #getDescription()} is the definition text, and
 *  {@link #getOID()} carries a standardised term identifier.</p>
 *
 *  <p>This interface prescribes only the descriptive contract below. It
 *  does not require any particular storage mechanism, nor that
 *  implementations be browsable, observable, or modifiable. An
 *  implementation that is also part of a richer object model (for
 *  example one that is additionally navigable as a tree node) may
 *  expose those capabilities through separate interfaces; nothing in
 *  this contract assumes them.</p>
 *
 *  <p>The constants below define a canonical attribute-key vocabulary
 *  for implementations that are backed by a keyed attribute store. Such
 *  implementations should use these keys so that definitions are
 *  interchangeable across backends; implementations that are not
 *  attribute-backed may ignore them and satisfy the typed accessors by
 *  any means. The keys are a naming convention, not a mandated storage
 *  model.</p>
 *
 *  <table border="1">
 *    <tr><th>Key constant</th><th>Mandatory</th><th>Java type</th>
 *        <th>Description</th></tr>
 *    <tr><td>{@link #KEY_NAME}</td><td>TRUE</td><td>{@code String}</td>
 *        <td>Local name (descriptor) of this definition. Maximum
 *            {@link #NAME_MAX_LENGTH} characters. Equivalent to the
 *            NAME field in RFC 4512 AttributeTypeDescription and the
 *            commonName attribute; also suitable as a headword or term
 *            identifier in non-schema uses.</td></tr>
 *    <tr><td>{@link #KEY_OID}</td><td>FALSE</td><td>{@code String}</td>
 *        <td>An OBJECT IDENTIFIER in dotted-decimal notation (e.g.
 *            {@code "2.5.4.3"}) or any other standardised term
 *            identifier. Optional; may be absent for ad-hoc or
 *            anonymous definitions.</td></tr>
 *    <tr><td>{@link #KEY_NAME_NS_URI}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Namespace URI associated with the name (e.g.
 *            {@code "http://example.com/schema/"}). Must end with
 *            {@code '/'} or {@code '#'} when present.</td></tr>
 *    <tr><td>{@link #KEY_OBJECT_CLASS_NAME}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Type discriminator naming the kind of definition (for
 *            example the value of a sub-interface's
 *            {@code OBJECT_CLASS_NAME}). Allows a generic consumer to
 *            recover the specific definition kind without reflection.
 *            Corresponds to the object class name in ITU-T X.501.</td></tr>
 *    <tr><td>{@link #KEY_DESCRIPTION}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Human-readable description or definition text. Maximum
 *            {@link #DESC_MAX_LENGTH} characters. Equivalent to the
 *            DESC field in RFC 4512 and the description attribute; also
 *            suitable as the definition body of a dictionary entry or
 *            glossary term.</td></tr>
 *    <tr><td>{@link #KEY_DISPLAY_NAME}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Friendly UI label. When absent, a display-name lookup
 *            should fall back to {@link #getLocalName()}. Defined in
 *            IETF RFC 2798.</td></tr>
 *    <tr><td>{@link #KEY_OBSOLETE}</td><td>TRUE</td>
 *        <td>{@code Boolean}</td>
 *        <td>Whether this definition is obsolete or deprecated.
 *            Defaults to {@code FALSE}. Equivalent to the OBSOLETE
 *            keyword in RFC 4512; also applicable to superseded
 *            dictionary senses or retired code-list entries.</td></tr>
 *    <tr><td>{@link #KEY_ORIGIN}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Standard, specification, or source from which this
 *            definition originates (e.g. {@code "RFC 4519"},
 *            {@code "ITU-T X.520"}, {@code "Merriam-Webster"}).</td></tr>
 *  </table>
 *
 *  @author Carl Eric Codere
 */
public interface Definition 
{
  // -----------------------------------------------------------------------
  // Length constraints
  // -----------------------------------------------------------------------

  /** Maximum length of the {@link #KEY_NAME} attribute in characters.
   *  Specified in IETF RFC 4520 (Object Identifier Descriptors).
   *  Value: {@code 48}.
   */
  int NAME_MAX_LENGTH = 48;

  /** Maximum length of the {@link #KEY_DESCRIPTION} attribute in
   *  characters. Defined for historical compatibility with the
   *  ITU-T X.520 Upper Bounds annex. Value: {@code 1024}.
   */
  int DESC_MAX_LENGTH = 1024;

  // -----------------------------------------------------------------------
  // Attribute key constants
  // -----------------------------------------------------------------------

  /** Key for the local name (descriptor) of this definition.
   *  Equivalent to LDAP-NAME / NAME in ITU-T X.501 and RFC 4512.
   *  Mandatory; maximum length is {@link #NAME_MAX_LENGTH} characters.
   *  
   *  <p>This limit applies to the bare local name returned by {@code getLocalName()}, 
   *  not to the namespace URI or the expanded name.</p>
   */
  String KEY_NAME = "commonName";

  /** Key for the namespace URI associated with the name. Optional. When
   *  present, must end with {@code '/'} or {@code '#'} to allow
   *  unambiguous expanded-name construction.
   */
  String KEY_NAME_NS_URI = "commonNameNamespace";

  /** Key for the object class name of this definition, as defined in
   *  ITU-T X.501. Optional. Used by a schema factory to verify
   *  mandatory attributes and to instantiate the correct concrete type.
   */
  String KEY_OBJECT_CLASS_NAME = "objectClass";

  /** Key for the unique identifier of this definition. Equivalent to
   *  OBJECT IDENTIFIER in ITU-T X.501. Should be in dotted-decimal
   *  OID notation (e.g. {@code "2.5.4.3"}). Incorrectly named
   *  in ITU-T X.520 (2019) as "OBJECT IDENTIFIER", converted
   *  to allowed attribute identifier.
   */
  String KEY_OID = "objectIdentifier";

  /** Key for the human-readable description of this definition.
   *  Equivalent to LDAP-DESC / DESC in ITU-T X.501 and RFC 4512.
   *  Optional; maximum length is {@link #DESC_MAX_LENGTH} characters.
   *  Defined in IETF RFC 4519.
   */
  String KEY_DESCRIPTION = "description";

  /** Key for a user-friendly UI display label. Optional. When absent,
   *  {@link Entity#getDisplayName()} falls back to the value of
   *  {@link #KEY_NAME}. Defined in IETF RFC 2798.
   */
  String KEY_DISPLAY_NAME = "displayName";

  /** Key for the obsolete flag. Equivalent to the OBSOLETE keyword in
   *  RFC 4512. Value is a {@link Boolean}. Defaults to {@code FALSE}
   *  when not explicitly set.
   */
  String KEY_OBSOLETE = "obsolete";

  /** Key for the origin (standard or specification) of this definition.
   *  Optional. Examples: {@code "RFC 4519"}, {@code "ITU-T X.520"}.
   */
  String KEY_ORIGIN = "origin";
  
  /** The canonical type-discriminator value for an
   *  {@link Definition}, suitable for use under
   *  {@link Definition#KEY_OBJECT_CLASS_NAME}.
   *
   *  <p>A generic consumer that traverses a collection of mixed
   *  {@link Definition} instances may compare this value against a
   *  definition's object-class name to distinguish item definitions
   *  from type definitions or class definitions without reflection.</p>
   *
   *  <p>The value ({@value}) is intentionally human-readable and maps
   *  to the Java interface role so that cross-language serialisers can
   *  recover the definition kind without additional metadata.</p>
   */
  String OBJECT_CLASS_NAME = "Definition";  

  // -----------------------------------------------------------------------
  // Typed accessors
  // -----------------------------------------------------------------------

  /** Returns the local name (descriptor) of this definition, e.g. 
   *  "cn", "title", "yearMonthDuration". The local name is a 
   *  bare identifier without any namespace prefix; namespace 
   *  context is carried separately by {@link #getNamespaceURI()}. 
   *  Subject to the {@link #NAME_MAX_LENGTH} limit.
   * 
   */
  String getLocalName();

  /** Returns the namespace URI associated with this definition's name,
   *  or {@code null} if the definition has no namespace.
   *
   *  <p>When present, the namespace URI must end with {@code '/'} or
   *  {@code '#'} to allow unambiguous expanded-name construction by
   *  concatenation (see {@link #getExpandedName()}).</p>
   *
   * @return A non-empty namespace URI ending with {@code '/'} or
   *         {@code '#'}, or {@code null} if no namespace is
   *         associated with this definition.
   */
  String getNamespaceURI();

  /** Returns the expanded name of this definition in RDF IRI format.
   *
   *  <p>When a namespace URI is present, the expanded name is
   *  {@code namespaceURI + localName}. When no namespace is present,
   *  the expanded name equals the name returned by
   *  {@link #getLocalName()}.</p>
   *
   * @return A non-null expanded name for this definition.
   */
  String getExpandedName();

  /** Returns the unique identifier (OID) of this definition.
   *
   *  <p>Equivalent to the numeric OID in an RFC 4512
   *  {@code AttributeTypeDescription} or
   *  {@code ObjectClassDescription}. If no explicit OID was assigned,
   *  returns {@code null}.</p>
   *
   * @return The object identifier, or null if absent
   */
  String getOID();

  /** Returns the human-readable description of this definition, or
   *  {@code null} if no description has been set.
   *
   *  <p>Equivalent to the DESC field in RFC 4512. When non-null, the
   *  returned string must not exceed {@link #DESC_MAX_LENGTH}
   *  characters.</p>
   *
   * @return The description, or {@code null} if absent.
   */
  String getDescription();

  /** Returns {@code true} if this definition is obsolete and should no
   *  longer be used in new schema designs.
   *
   *  <p>Equivalent to the OBSOLETE keyword in RFC 4512. Obsolete
   *  definitions are retained for backward compatibility but must not
   *  be referenced when defining new attributes or object classes.</p>
   *
   * @return {@code true} if this definition is obsolete,
   *         {@code false} otherwise (including when not explicitly
   *         set, in which case the default is {@code false}).
   */
  boolean isObsolete();

  /** Returns the origin (standard or specification) of this definition,
   *  or {@code null} if no origin has been recorded.
   *
   *  <p>Typical values: {@code "RFC 4519"},
   *  {@code "ITU-T X.520"}, {@code "MS Active Directory"}.</p>
   *
   * @return The origin string, or {@code null} if not specified.
   */
  String getOrigin();
}
