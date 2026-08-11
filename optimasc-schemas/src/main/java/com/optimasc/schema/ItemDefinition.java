package com.optimasc.schema;

import com.optimasc.schema.Definition;

/** Represents the schema definition of a named item — an attribute,
 *  property, element, or column.
 *
 *  <p>An {@code ItemDefinition} describes the <em>rules</em> governing
 *  a single item slot: its data type, its value multiplicity and
 *  ordering, whether it is read-only, and optional constraints such as
 *  a maximum value length, a context type for alternative values, or a
 *  restricted set of permitted choices. It does not hold live values;
 *  it describes how the values of one item are constrained.</p>
 *
 *  <p>The value multiplicity and ordering declared by an
 *  {@code ItemDefinition} constrain how many values the item may hold
 *  and whether their order is significant:</p>
 *  <ul>
 *    <li>{@link #VALUE_TYPE_SINGLE} — at most one value.</li>
 *    <li>{@link #VALUE_TYPE_BAG} — multiple values with no defined
 *        order.</li>
 *    <li>{@link #VALUE_TYPE_SEQ} — multiple ordered values.</li>
 *    <li>{@link #VALUE_TYPE_ALT} — multiple alternative values, each
 *        keyed by a context value.</li>
 *  </ul>
 *
 *  <p>Among others, this interface can represent an
 *  {@code AttributeTypeDescription} as defined in IETF RFC 4512 and
 *  ITU-T X.501, a property descriptor in XMP or Dublin Core, a column
 *  definition in a SQL table, or a field descriptor in a structured
 *  binary format.</p>
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
 *    <tr><td>{@link #KEY_TYPE_NAME}</td><td>TRUE</td>
 *        <td>{@code String}</td>
 *        <td>Type name or OBJECT IDENTIFIER of the data type for
 *            values of this item. Equivalent to SYNTAX in RFC 4512.
 *            Must be set for a valid item definition.</td></tr>
 *    <tr><td>{@link #KEY_VALUE_TYPE}</td><td>TRUE</td>
 *        <td>{@code String}</td>
 *        <td>Value multiplicity and ordering. One of
 *            {@link #VALUE_TYPE_SINGLE}, {@link #VALUE_TYPE_BAG},
 *            {@link #VALUE_TYPE_SEQ}, or {@link #VALUE_TYPE_ALT}.
 *            Defaults to {@link #VALUE_TYPE_SINGLE} when not
 *            explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_READONLY}</td><td>TRUE</td>
 *        <td>{@code Boolean}</td>
 *        <td>Whether values of this item are read-only with respect
 *            to user applications. Equivalent to
 *            NO-USER-MODIFICATION in RFC 4512. Defaults to
 *            {@code FALSE} when not explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_USAGE}</td><td>TRUE</td>
 *        <td>{@link AttributeUsage}</td>
 *        <td>Intended usage of this item. Equivalent to USAGE in
 *            RFC 4512. Defaults to
 *            {@link AttributeUsage#userApplication} when not
 *            explicitly set.</td></tr>
 *    <tr><td>{@link #KEY_MAX_VALUE_LENGTH}</td><td>FALSE</td>
 *        <td>{@code Number}</td>
 *        <td>Maximum length in characters (strings) or octets
 *            (binary). {@link Integer} or {@link Long}. Absent means
 *            no restriction.</td></tr>
 *    <tr><td>{@link #KEY_CONTEXT_TYPE}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Context type identifier for alternative values. Only
 *            meaningful when {@link #KEY_VALUE_TYPE} is
 *            {@link #VALUE_TYPE_ALT}. Typical values are
 *            {@link #CONTEXT_LANGUAGE} or
 *            {@link #CONTEXT_LOCATION}.</td></tr>
 *    <tr><td>{@link #KEY_CHOICES}</td><td>FALSE</td>
 *        <td>multi-valued, distinct</td>
 *        <td>Set of permitted or suggested values for this item.
 *            Enforced or advisory per {@link #KEY_CHOICE_TYPE}. Absent
 *            means no choice constraint.</td></tr>
 *    <tr><td>{@link #KEY_CHOICE_TYPE}</td><td>FALSE</td>
 *        <td>{@code String}</td>
 *        <td>Whether {@link #KEY_CHOICES} is enforced
 *            ({@link #CHOICE_TYPE_CLOSED}) or advisory
 *            ({@link #CHOICE_TYPE_OPEN}). Undefined when no choices are
 *            declared.</td></tr>
 *  </table>
 *
 *  @see Definition
 *  @see AttributeUsage
 *  @author Carl Eric Codere
 */
public interface ItemDefinition extends Definition
{
  // -----------------------------------------------------------------------
  // Object-class name constant
  // -----------------------------------------------------------------------

  /** The canonical type-discriminator value for an
   *  {@link ItemDefinition}, suitable for use under
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
  String OBJECT_CLASS_NAME = "AttributeDefinition";

  // -----------------------------------------------------------------------
  // Attribute key constants
  // -----------------------------------------------------------------------

  /** Key for the type name or syntax identifier of this item's values.
   *
   *  <p>The value stored under this key is a {@link String} that is
   *  typically an OBJECT IDENTIFIER in dotted-decimal notation
   *  (e.g. {@code "1.3.6.1.4.1.1466.115.121.1.15"} for the LDAP
   *  Directory String syntax), though other string identifiers are
   *  also accepted. Equivalent to SYNTAX in ITU-T X.501 and
   *  RFC 4512.</p>
   *
   *  <p>This attribute is mandatory for a valid item definition.</p>
   */
  String KEY_TYPE_NAME = "typeName";

  /** Key for the value multiplicity and ordering of this item.
   *
   *  <p>The value stored under this key is a {@link String} that must
   *  be one of:</p>
   *  <ul>
   *    <li>{@link #VALUE_TYPE_SINGLE} — single value only</li>
   *    <li>{@link #VALUE_TYPE_BAG}    — multiple unordered values</li>
   *    <li>{@link #VALUE_TYPE_SEQ}    — multiple ordered values</li>
   *    <li>{@link #VALUE_TYPE_ALT}    — alternative values with
   *        context keys</li>
   *  </ul>
   *
   *  <p>This attribute is mandatory. Defaults to
   *  {@link #VALUE_TYPE_SINGLE} when not explicitly set.</p>
   */
  String KEY_VALUE_TYPE = "valueType";

  /** Key for the read-only flag of this item.
   *
   *  <p>The value stored under this key is a {@link Boolean}:</p>
   *  <ul>
   *    <li>{@code TRUE}  — values may only be modified by the
   *        system (equivalent to NO-USER-MODIFICATION in
   *        RFC 4512)</li>
   *    <li>{@code FALSE} — values may be modified by user
   *        applications (default)</li>
   *  </ul>
   *
   *  <p>This attribute is mandatory. Defaults to {@code FALSE}
   *  when not explicitly set.</p>
   */
  String KEY_READONLY = "readOnly";

  /** Key for the intended usage of this item.
   *
   *  <p>The value stored under this key is an
   *  {@link AttributeUsage} instance. Equivalent to USAGE in
   *  ITU-T X.501 and RFC 4512.</p>
   *
   *  <p>This attribute is mandatory. Defaults to
   *  {@link AttributeUsage#userApplication} when not explicitly
   *  set.</p>
   */
  String KEY_USAGE = "usage";

  /** Key for the maximum length constraint on values of this item.
   *
   *  <p>The value stored under this key is a {@link Number} instance
   *  (typically {@link Integer} or {@link Long}) giving the maximum
   *  permitted length of each value. How length is measured depends on
   *  the value's runtime type; see {@link #getMaxValueLength()} for the
   *  full per-type rule (characters for text, octets for binary, and so
   *  on).</p>
   *
   *  <p>This attribute is optional. When absent, there is no length
   *  restriction on values.</p>
   */
  String KEY_MAX_VALUE_LENGTH = "maxValueLength";

  /** Key for the context type identifier used with alternative values.
   *
   *  <p>The value stored under this key is a {@link String}
   *  identifier that specifies how to distinguish between alternative
   *  values when {@link #KEY_VALUE_TYPE} is {@link #VALUE_TYPE_ALT}.
   *  Pre-defined values are {@link #CONTEXT_LANGUAGE} and
   *  {@link #CONTEXT_LOCATION}; custom identifiers are also
   *  permitted.</p>
   *
   *  <p>This attribute is required when {@link #KEY_VALUE_TYPE} is
   *  {@link #VALUE_TYPE_ALT} and not applicable for other value
   *  types.</p>
   */
  String KEY_CONTEXT_TYPE = "contextType";

  // -----------------------------------------------------------------------
  // Choice (enumeration) constants
  // -----------------------------------------------------------------------

  /** Key for the set of choice values that this item restricts its
   *  values to.
   *
   *  <p>This is a multi-valued attribute. Each choice value's runtime
   *  class corresponds to the value representation associated with this
   *  item's {@link #getTypeName()}; how that representation is resolved
   *  is implementation-specific and outside the scope of this
   *  definition. Choice values are distinct: no value may appear more
   *  than once. Insertion order is preserved and is semantically
   *  significant for presentation purposes (e.g. populating a
   *  drop-down list), even though membership testing itself is
   *  order-independent.</p>
   *
   *  <p>Whether membership in this set is enforced or merely advisory
   *  is governed by {@link #KEY_CHOICE_TYPE}. These choices narrow the
   *  values permitted for this particular item; they are applied in
   *  addition to any constraints inherent to the item's underlying
   *  type, not in place of them.</p>
   *
   *  <p>Analogous to the open and closed choice value types defined by
   *  the XMP specification, and — for the closed case — to the
   *  {@code X-ENUM} extension in LDAP syntax descriptions (RFC 4512
   *  section 4.2) and to {@code xs:enumeration} facets in XML Schema
   *  simple type restrictions.</p>
   *
   *  <p>This attribute is optional. When absent, no choice constraint
   *  is declared and any value compatible with this item's type is
   *  permitted. When present it must declare at least one value; an
   *  empty choice set is not a valid state.</p>
   */
  String KEY_CHOICES = "choices";

  /** Key for the choice type, indicating whether the values listed
   *  under {@link #KEY_CHOICES} are an enforced constraint or an
   *  advisory set of suggestions.
   *
   *  <p>The value stored under this key is a {@link String} that must
   *  be one of {@link #CHOICE_TYPE_OPEN} or {@link #CHOICE_TYPE_CLOSED}.
   *  It is only meaningful when {@link #KEY_CHOICES} is present.</p>
   *
   *  <p>This attribute is optional and has no default; it is undefined
   *  (and {@link #getChoiceType()} returns {@code null}) when no choice
   *  list is declared.</p>
   */
  String KEY_CHOICE_TYPE = "choiceType";

  /** Choice type constant indicating an <em>open</em> choice: the
   *  values listed under {@link #KEY_CHOICES} are a recommended or
   *  commonly-used set, but other values compatible with this item's
   *  type are also valid.
   *
   *  <p>Corresponds to the open choice value type in the XMP
   *  specification. Useful for populating an editable selection control
   *  that permits free entry of values not present in the list.</p>
   */
  String CHOICE_TYPE_OPEN = "open";

  /** Choice type constant indicating a <em>closed</em> choice: every
   *  value of this item must be equal (via {@link Object#equals}) to
   *  one of the values listed under {@link #KEY_CHOICES}.
   *
   *  <p>Corresponds to the closed choice value type in the XMP
   *  specification, to the {@code X-ENUM} extension in LDAP syntax
   *  descriptions (RFC 4512 section 4.2), and to {@code xs:enumeration}
   *  facets in XML Schema simple type restrictions.</p>
   */
  String CHOICE_TYPE_CLOSED = "closed";

  // -----------------------------------------------------------------------
  // Value-type constants
  // -----------------------------------------------------------------------

  /** Value type constant indicating that this item holds at most one
   *  value.
   *
   *  <p>A single-valued item permits no more than one value, and value
   *  order is not significant. Multi-element collections are not
   *  permitted as the item's value set.</p>
   */
  String VALUE_TYPE_SINGLE = "";

  /** Value type constant indicating an unordered collection of values
   *  (bag).
   *
   *  <p>A bag item may hold zero or more values whose order is not
   *  significant. The order in which values are stored or retrieved
   *  carries no meaning. Whether duplicate values are permitted depends
   *  on the implementation.</p>
   *
   *  <p>This definition follows the RDF {@code rdf:Bag} semantics.</p>
   */
  String VALUE_TYPE_BAG = "Bag";

  /** Value type constant indicating an ordered sequence of values.
   *
   *  <p>A sequence item may hold zero or more values whose order is
   *  significant and must be preserved; duplicate values are
   *  permitted.</p>
   *
   *  <p>This definition follows the RDF {@code rdf:Seq} semantics.</p>
   */
  String VALUE_TYPE_SEQ = "Seq";

  /** Value type constant indicating alternative values, each keyed by
   *  a context value.
   *
   *  <p>Multiple values are stored (one per context key), but only the
   *  value matching the current context is selected and presented at a
   *  time. The context type is specified by {@link #KEY_CONTEXT_TYPE}.</p>
   *
   *  <p>Example: a {@code description} item with context type
   *  {@link #CONTEXT_LANGUAGE} stores one value per language
   *  ({@code "en"}, {@code "fr"}, {@code "de"}); the appropriate
   *  value is selected based on the user's locale.</p>
   *
   *  <p>This definition follows the RDF {@code rdf:Alt} semantics.</p>
   */
  String VALUE_TYPE_ALT = "Alt";

  // -----------------------------------------------------------------------
  // Pre-defined context type identifiers
  // -----------------------------------------------------------------------

  /** Pre-defined context type for language-based alternatives.
   *
   *  <p>When this context type is used, alternative values are
   *  distinguished by language using ISO 639-1 (two-letter) or
   *  ISO 639-2 (three-letter) language codes.</p>
   *
   *  <p>Examples: {@code "en"} (English), {@code "fr"} (French),
   *  {@code "de"} (German), {@code "eng"}, {@code "fra"},
   *  {@code "deu"}.</p>
   *
   *  <p>Corresponds to the {@code LangAlt} type in XMP and language
   *  options in LDAP.</p>
   */
  String CONTEXT_LANGUAGE = "LangAlt";

  /** Pre-defined context type for location-based alternatives.
   *
   *  <p>When this context type is used, alternative values are
   *  distinguished by location. Defined context values
   *  (case-insensitive):</p>
   *  <ul>
   *    <li>{@code "Home"}   — home or personal location</li>
   *    <li>{@code "Office"} — work or business location</li>
   *    <li>{@code "Other"}  — any other location</li>
   *  </ul>
   */
  String CONTEXT_LOCATION = "LocationAlt";

  // -----------------------------------------------------------------------
  // Typed accessors
  // -----------------------------------------------------------------------

  /** Returns the type name or syntax identifier for values of this
   *  item.
   *
   *  <p>Typically an OBJECT IDENTIFIER that identifies a data-type
   *  definition in the same schema (e.g.
   *  {@code "1.3.6.1.4.1.1466.115.121.1.15"} for the LDAP Directory
   *  String syntax). Equivalent to the SYNTAX field in RFC 4512
   *  {@code AttributeTypeDescription}.</p>
   *
   * @return The type name or OID, or {@code null} if not set
   *         (which indicates an invalid or incomplete item
   *         definition).
   */
  String getTypeName();

  /** Returns the value-type identifier, indicating the multiplicity
   *  and ordering of values for this item.
   *
   *  <p>The returned value is always one of:</p>
   *  <ul>
   *    <li>{@link #VALUE_TYPE_SINGLE} — exactly one value</li>
   *    <li>{@link #VALUE_TYPE_BAG}    — zero or more unordered
   *        values</li>
   *    <li>{@link #VALUE_TYPE_SEQ}    — zero or more ordered
   *        values</li>
   *    <li>{@link #VALUE_TYPE_ALT}    — zero or more alternative
   *        values keyed by context</li>
   *  </ul>
   *
   * @return A non-null value-type constant for a valid item
   *         definition. Defaults to {@link #VALUE_TYPE_SINGLE} when
   *         not explicitly set.
   */
  String getValueType();

  /** Returns the context type identifier for alternative values, or
   *  {@code null} if this item does not use alternative values.
   *
   *  <p>This value is only meaningful when {@link #getValueType()}
   *  returns {@link #VALUE_TYPE_ALT}. It specifies how alternative
   *  values are distinguished, typically one of
   *  {@link #CONTEXT_LANGUAGE} or {@link #CONTEXT_LOCATION}, though
   *  custom identifiers are also permitted.</p>
   *
   * @return The context type identifier, or {@code null} if this
   *         item does not have alternative values.
   */
  String getContextType();

  /** Returns the intended usage of this item definition.
   *
   *  <p>Equivalent to the USAGE field in RFC 4512
   *  {@code AttributeTypeDescription}. Indicates whether this
   *  item is managed by user applications or by the underlying
   *  directory / system infrastructure.</p>
   *
   * @return A non-null {@link AttributeUsage} value. Defaults to
   *         {@link AttributeUsage#userApplication} when not
   *         explicitly set.
   */
  AttributeUsage getUsage();

  /** Returns {@code true} if this item holds at most one value.
   *
   *  <p>Equivalent to checking
   *  {@code getValueType().equals(VALUE_TYPE_SINGLE)}. Items with value
   *  types {@link #VALUE_TYPE_BAG}, {@link #VALUE_TYPE_SEQ}, or
   *  {@link #VALUE_TYPE_ALT} all return {@code false} because they may
   *  hold multiple values (even though ALT items typically present only
   *  one value at a time based on context).</p>
   *
   * @return {@code true} if at most one value is permitted,
   *         {@code false} if multiple values are permitted.
   */
  boolean isSingleValued();

  /** Returns {@code true} if the values of this item are ordered.
   *
   *  <p>Equivalent to checking
   *  {@code getValueType().equals(VALUE_TYPE_SEQ)}. Only sequence
   *  items are considered ordered; single-valued, bag, and
   *  alternative items all return {@code false}.</p>
   *
   * @return {@code true} if value order is significant (sequence),
   *         {@code false} otherwise.
   */
  boolean isOrdered();

  /** Returns {@code true} if values of this item are read-only with
   *  respect to user applications.
   *
   *  <p>Equivalent to the NO-USER-MODIFICATION flag in RFC 4512.
   *  When {@code true}, only the underlying system or directory
   *  service may modify the item; user applications must treat
   *  it as read-only.</p>
   *
   * @return {@code true} if read-only, {@code false} if user
   *         applications may modify values (the default).
   */
  boolean isReadOnly();

  /** Returns the maximum permitted length for values of this item,
   *  or {@code -1} if no length restriction is defined.
   *
   *  <p>When a maximum is defined, it is compared against the measured
   *  length of each value. Length is measured according to the value's
   *  runtime type:</p>
   *  <ul>
   *    <li>{@link CharSequence}: number of characters.</li>
   *    <li>{@code byte[]}: number of octets.</li>
   *    <li>{@code char[]}: number of characters.</li>
   *    <li>{@link java.io.InputStream}: number of bytes currently
   *        available, as reported by
   *        {@link java.io.InputStream#available()}.</li>
   *    <li>{@link java.sql.Clob}: number of characters, as reported by
   *        {@link java.sql.Clob#length()}.</li>
   *    <li>{@link java.sql.Blob}: number of octets, as reported by
   *        {@link java.sql.Blob#length()}.</li>
   *  </ul>
   *
   *  <p>For values of any other type, no length constraint is enforced
   *  even when a maximum is defined. A return value of {@code -1}
   *  indicates that no upper bound has been set for this item
   *  definition.</p>
   *
   * @return The maximum value length as a positive integer, or
   *         {@code -1} if unrestricted.
   */
  long getMaxValueLength();

  /** Returns the set of choice values that this item restricts its
   *  values to, or {@code null} if no choice constraint is declared.
   *
   *  <p>Choice values are strings — controlled vocabulary terms,
   *  enumeration labels, language codes, or similar textual
   *  identifiers. Numeric or structured enumerations belong on the
   *  type layer (via the type's constraining facets), not on the
   *  item definition.</p>
   *
   *  <p>The set contains no duplicate values; iteration order reflects
   *  the order in which the choices were declared, so the returned set
   *  may be used directly for ordered presentation (e.g. populating a
   *  drop-down list). Whether the returned values constrain validity
   *  or are merely advisory is determined by
   *  {@link #getChoiceType()}.</p>
   *
   *  <p>When non-null, the returned set is a non-empty, unmodifiable,
   *  insertion-order-preserving snapshot; any attempt to mutate it
   *  throws {@link java.lang.UnsupportedOperationException}.</p>
   *
   * @return A non-null, non-empty, unmodifiable, insertion-ordered
   *         {@link java.util.Set} of distinct {@link String} choice
   *         values, or {@code null} if no choice constraint is
   *         declared.
   */
  java.util.Set<String> getChoices();  

  /** Returns the choice type, indicating whether the values returned by
   *  {@link #getChoices()} are an enforced constraint or an advisory
   *  set of suggestions.
   *
   *  <p>Returns one of {@link #CHOICE_TYPE_OPEN} (other compatible
   *  values are also valid) or {@link #CHOICE_TYPE_CLOSED} (values must
   *  be members of the choice set). Returns {@code null} when no
   *  choice set is declared (i.e. when {@link #getChoices()} returns
   *  {@code null}).</p>
   *
   * @return {@link #CHOICE_TYPE_OPEN} or {@link #CHOICE_TYPE_CLOSED}
   *         when a choice set is present, or {@code null} if no choice
   *         constraint is declared.
   */
  String getChoiceType();
}