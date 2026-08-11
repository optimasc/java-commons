package com.optimasc.schema;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.xml.namespace.QName;

/**
 * A lightweight, immutable description of a single attribute slot.
 *
 * <p>This class is the simplest way to define the schema of an attribute —
 * the named item whose values a metadata provider reads or writes. It
 * implements {@link ItemDefinition}. It carries no dependency on
 * any entity, attribute-store, or backing-map infrastructure, and is
 * intended for use in plugin and engine code that needs to declare attribute
 * vocabulary without pulling in the full entity or schema-tree stack.</p>
 *
 * <p>All fields are set at construction time and are immutable thereafter.
 * The primary constructor validates all arguments:</p>
 * <ul>
 *   <li>{@code valueType} must be one of the {@code VALUE_TYPE_*} constants;
 *       {@code contextType} is required if and only if {@code valueType} is
 *       {@link #VALUE_TYPE_ALT}.</li>
 *   <li>Choice values must be non-null {@link String} instances; duplicate
 *       values are silently collapsed and only the first occurrence is
 *       retained. {@code choiceType} must be present if and only if choices
 *       are declared. Numeric or structured enumerations belong on the type
 *       layer (via the type's constraining facets), not on the item
 *       definition.</li>
 *   <li>{@code typeName} must not contain a {@code {n}} length suffix in the
 *       primary constructor; pass the length via {@code maxValueLength}
 *       instead. The {@link #AttributeDefinition(String, QName, String,
 *       String, String, String, boolean, String[], String) QName constructor}
 *       does accept an inline {@code {n}} suffix and extracts the length
 *       automatically.</li>
 * </ul>
 *
 * <p>{@link #toString()} returns the local name ({@link #getLocalName()}) so
 * that instances render correctly in Swing components such as {@code JList},
 * {@code JComboBox}, and {@code JTree} whose default cell renderers call
 * {@code toString()} directly.</p>
 *
 * <p>This class is thread-safe by virtue of immutability.</p>
 *
 * @see ItemDefinition
 * @see BasicDefinition
 * @see Definition
 * @author Carl Eric Codere
 */
public class BasicAttributeDefinition extends BasicDefinition implements ItemDefinition
{

  // -----------------------------------------------------------------------
  // Fields — ItemDefinition
  // -----------------------------------------------------------------------

  /** Type name or OID (bare, without any {@code {n}} suffix). */
  protected final String typeName;

  /** One of the {@code VALUE_TYPE_*} constants. Never {@code null}. */
  protected final String valueType;

  /**
   * Context type identifier. Non-null only when {@link #valueType} is
   * {@link #VALUE_TYPE_ALT}.
   */
  protected final String contextType;

  /** {@code true} if values are system-only (NO-USER-MODIFICATION). */
  protected final boolean readOnly;

  /**
   * Maximum value length in characters or octets, or {@code -1} if
   * unrestricted. Derived from the optional {@code {n}} suffix of the type name
   * supplied to the constructor.
   */
  protected final long maxValueLength;

  /**
   * Unmodifiable, insertion-ordered set of choice values, or
   * {@code null} when no choice constraint is declared.
   */
  protected final Set<String> choices;
  
  /**
   * {@link #CHOICE_TYPE_OPEN} or {@link #CHOICE_TYPE_CLOSED}, or {@code null}
   * when {@link #choices} is {@code null}.
   */
  protected final String choiceType;

  // -----------------------------------------------------------------------
  // Constructors
  // -----------------------------------------------------------------------

  //-----------------------------------------------------------------------
  //Constructors
  //-----------------------------------------------------------------------

  /** Constructs a fully-specified, immutable attribute definition with
   *  an explicit maximum value length and an optional choice constraint.
   *
   *  <p>This is the primary constructor; all other constructors delegate
   *  to this one. Validation rules:</p>
   *  <ul>
   *    <li>{@code localName} must not be {@code null}, empty, or longer
   *        than {@link Definition#NAME_MAX_LENGTH} characters, and must
   *        not contain a {@code ':'} character.</li>
   *    <li>{@code id} — when non-null and non-empty — must be a
   *        syntactically valid OID in dotted-decimal notation.</li>
   *    <li>{@code description} — when non-null — must not exceed
   *        {@link Definition#DESC_MAX_LENGTH} characters.</li>
   *    <li>{@code typeName} must not be {@code null} or empty. When its
   *        first character is a digit it is validated as an OID in
   *        dotted-decimal notation. No {@code {n}} suffix is accepted
   *        here; pass the length via {@code maxValueLength} instead.</li>
   *    <li>{@code maxValueLength} must be {@code -1} (no restriction)
   *        or a non-negative value.</li>
   *    <li>{@code valueType} must be one of {@link #VALUE_TYPE_SINGLE},
   *        {@link #VALUE_TYPE_BAG}, {@link #VALUE_TYPE_SEQ}, or
   *        {@link #VALUE_TYPE_ALT}.</li>
   *    <li>{@code contextType} is required when {@code valueType} is
   *        {@link #VALUE_TYPE_ALT} and must be {@code null} for all
   *        other value types.</li>
   *    <li>When {@code choices} is non-null it must be non-empty and every
   *        element must be a non-null {@link String}. Duplicate values are
   *        silently collapsed; only the first occurrence is retained.
   *        {@code choiceType} is then required and must be one of
   *        {@link #CHOICE_TYPE_OPEN} or {@link #CHOICE_TYPE_CLOSED}.</li>
   *    <li>When {@code choices} is {@code null}, {@code choiceType} must
   *        also be {@code null}.</li>   *        
   *  </ul>
   *
   * @param oid            [in] Unique identifier (OID in dotted-decimal
   *                       notation), or {@code null}.
   * @param namespaceURI   [in] Namespace URI, or {@code null} / empty
   *                       string when no namespace applies. An empty
   *                       string is normalised to {@code null}.
   * @param localName      [in] Local name of this attribute. Must not be
   *                       {@code null} or empty, must not exceed
   *                       {@link Definition#NAME_MAX_LENGTH} characters,
   *                       and must not contain {@code ':'}.
   * @param description    [in] Human-readable description, or
   *                       {@code null}. When non-null, must not exceed
   *                       {@link Definition#DESC_MAX_LENGTH} characters.
   * @param origin         [in] Origin of this definition (e.g.
   *                       {@code "RFC 4519"} or {@code "XMP"}), or
   *                       {@code null} if not applicable.
   * @param typeName       [in] Type name or OID identifying the data
   *                       type of values. Must not be {@code null} or
   *                       empty. Must not contain a {@code {n}} suffix;
   *                       pass the length via {@code maxValueLength}.
   * @param maxValueLength [in] Maximum value length in characters
   *                       (strings) or octets (binary), or {@code -1}
   *                       if no restriction applies.
   * @param valueType      [in] Value multiplicity and ordering. Must be
   *                       one of {@link #VALUE_TYPE_SINGLE},
   *                       {@link #VALUE_TYPE_BAG},
   *                       {@link #VALUE_TYPE_SEQ}, or
   *                       {@link #VALUE_TYPE_ALT}. Must not be
   *                       {@code null}.
   * @param contextType    [in] Context type identifier (e.g.
   *                       {@link #CONTEXT_LANGUAGE}). Required when
   *                       {@code valueType} is {@link #VALUE_TYPE_ALT};
   *                       must be {@code null} for all other value
   *                       types.
   * @param readOnly       [in] {@code true} if values of this item may
   *                       only be modified by the system.
   * @param choices        [in] Array of permitted or suggested values,
   *                       or {@code null} if no choice constraint is
   *                       declared. When non-null, must be non-empty and
   *                       all elements must be non-null and
   *                       assignment-compatible with the first element.
   * @param choiceType     [in] {@link #CHOICE_TYPE_OPEN} or
   *                       {@link #CHOICE_TYPE_CLOSED}. Required when
   *                       {@code choices} is non-null; must be
   *                       {@code null} when {@code choices} is
   *                       {@code null}.
   * @throws IllegalArgumentException if any argument violates its
   *   constraints.
   */
  public BasicAttributeDefinition(String oid, String namespaceURI, String localName,
      String description, String origin, String typeName, long maxValueLength,
      String valueType, String contextType, boolean readOnly,
      String[] choices, String choiceType)
  {
    super(oid,namespaceURI,localName,description,origin);

    // ---- typeName ----
    if (typeName == null || typeName.length() == 0)
      throw new IllegalArgumentException(
          "Type name must not be null or empty.");
    if (typeName.indexOf('{') >= 0)
      throw new IllegalArgumentException(
          "Type name must not contain a '{n}' length suffix in this "
          + "constructor; pass the length via maxValueLength instead. "
          + "Got: '" + typeName + "'.");
    if (Character.isDigit(typeName.charAt(0)))
      validateOID(typeName);
    this.typeName = typeName;

    // ---- maxValueLength ----
    if (maxValueLength < -1L)
      throw new IllegalArgumentException(
          "maxValueLength must be -1 (unrestricted) or a non-negative "
          + "value, but got: " + maxValueLength + ".");
    this.maxValueLength = maxValueLength;

    // ---- valueType / contextType ----
    if (valueType == null)
      throw new IllegalArgumentException(
          "Value type must not be null.");
    if (valueType.equals(VALUE_TYPE_SINGLE)
        || valueType.equals(VALUE_TYPE_BAG)
        || valueType.equals(VALUE_TYPE_SEQ))
    {
      if (contextType != null)
        throw new IllegalArgumentException(
            "Context type must be null for value type '"
            + valueType + "'.");
      this.contextType = null;
    }
    else if (valueType.equals(VALUE_TYPE_ALT))
    {
      if (contextType == null)
        throw new IllegalArgumentException(
            "Context type is required for value type '"
            + VALUE_TYPE_ALT + "'.");
      this.contextType = contextType;
    }
    else
    {
      throw new IllegalArgumentException(
          "Unknown value type '" + valueType
          + "'. Must be one of: VALUE_TYPE_SINGLE, VALUE_TYPE_BAG,"
          + " VALUE_TYPE_SEQ, VALUE_TYPE_ALT.");
    }
    this.valueType = valueType;
    this.readOnly  = readOnly;

    // ---- choices / choiceType ----
    if (choices == null)
    {
      if (choiceType != null)
        throw new IllegalArgumentException(
            "Choice type must be null when no choices are declared.");
      this.choices    = null;
      this.choiceType = null;
    }
    else
    {
      if (choices.length == 0)
        throw new IllegalArgumentException(
            "Choices must not be empty.");
      if (!CHOICE_TYPE_OPEN.equals(choiceType)
          && !CHOICE_TYPE_CLOSED.equals(choiceType))
        throw new IllegalArgumentException(
            "Choice type must be CHOICE_TYPE_OPEN or "
            + "CHOICE_TYPE_CLOSED, but got: '" + choiceType + "'.");
      if (choices[0] == null)
        throw new IllegalArgumentException(
            "Choice element at index 0 must not be null.");
      LinkedHashSet<String> set = new LinkedHashSet<String>();
      set.add(choices[0]);
      for (int i = 1; i < choices.length; i++)
      {
        if (choices[i] == null)
          throw new IllegalArgumentException(
              "Choice element at index " + i
              + " must not be null.");
        set.add(choices[i]);
      }
      this.choices    = Collections.unmodifiableSet(set);
      this.choiceType = choiceType;
    }
  }
  /**
   * Constructs an immutable attribute definition with an explicit maximum value
   * length and no choice constraint.
   *
   * <p>
   * Equivalent to calling the full constructor with {@code choices = null} and
   * {@code choiceType = null}.
   * </p>
   *
   * @param oid
   *          [in] Unique identifier (OID in dotted-decimal notation), or
   *          {@code null}.
   * @param namespaceURI
   *          [in] Namespace URI, or {@code null} / empty string when no
   *          namespace applies.
   * @param localName
   *          [in] Local name. Must not be {@code null} or empty, must not
   *          exceed {@link Definition#NAME_MAX_LENGTH} characters, and must not
   *          contain {@code ':'}.
   * @param description
   *          [in] Human-readable description, or {@code null}.
   * @param typeName
   *          [in] Type name or OID. Must not be {@code null} or empty, and must
   *          not contain a {@code {n}} suffix.
   * @param maxValueLength
   *          [in] Maximum value length, or {@code -1} if unrestricted.
   * @param valueType
   *          [in] One of {@link #VALUE_TYPE_SINGLE}, {@link #VALUE_TYPE_BAG},
   *          {@link #VALUE_TYPE_SEQ}, or {@link #VALUE_TYPE_ALT}.
   * @param contextType
   *          [in] Required when {@code valueType} is {@link #VALUE_TYPE_ALT};
   *          {@code null} otherwise.
   * @param readOnly
   *          [in] {@code true} if system-only.
   * @throws IllegalArgumentException
   *           if any argument violates its constraints.
   */
  public BasicAttributeDefinition(String oid, String namespaceURI, String localName,
      String description, String typeName, long maxValueLength,
      String valueType, String contextType, boolean readOnly)
  {
    this(oid, namespaceURI, localName, description, null, typeName,
        maxValueLength, valueType, contextType, readOnly, null, null);    
  }

  /**
   * Constructs a fully-specified, immutable attribute definition from a
   * {@link QName}, with an optional inline length constraint encoded as a
   * {@code {n}} suffix on {@code typeName}.
   *
   * <p>
   * The namespace URI and local part are extracted from {@code name}; the
   * prefix is discarded. A {@code QName} whose namespace URI is the empty
   * string is treated as having no namespace ({@code null}).
   * </p>
   *
   * <p>
   * The {@code typeName} parameter accepts an optional inline length constraint
   * suffix of the form {@code "typeName{n}"}, where {@code n} is a non-negative
   * integer (e.g. {@code "DirectoryString{64}"},
   * {@code "1.3.6.1.4.1.1466.115.121.1.15{128}"}). When present, the suffix is
   * stripped and {@code n} is used as the maximum value length. When absent, no
   * length restriction is applied ({@code maxValueLength} is {@code -1}).
   * </p>
   *
   * <p>
   * When {@code choices} is non-null, all elements must be non-null and
   * assignment-compatible with the first element's class. Duplicate values (per
   * {@link Object#equals}) are silently collapsed; only the first occurrence is
   * retained.
   * </p>
   *
   * @param oid
   *          [in] Unique identifier (OID in dotted-decimal notation), or
   *          {@code null}.
   * @param name
   *          [in] Qualified name carrying the namespace URI and local part.
   *          Must not be {@code null}; the local part must not be empty and
   *          must not exceed {@link Definition#NAME_MAX_LENGTH} characters.
   * @param description
   *          [in] Human-readable description, or {@code null}. When non-null,
   *          must not exceed {@link Definition#DESC_MAX_LENGTH} characters.
   * @param typeName
   *          [in] Type name or OID. May include an inline length constraint
   *          suffix (e.g. {@code "DirectoryString{64}"}).
   * @param valueType
   *          [in] One of {@link #VALUE_TYPE_SINGLE}, {@link #VALUE_TYPE_BAG},
   *          {@link #VALUE_TYPE_SEQ}, or {@link #VALUE_TYPE_ALT}.
   * @param contextType
   *          [in] Required when {@code valueType} is {@link #VALUE_TYPE_ALT};
   *          {@code null} otherwise.
   * @param readOnly
   *          [in] {@code true} if system-only.
   * @param choices
   *          [in] Array of permitted or suggested values, or {@code null}.
   * @param choiceType
   *          [in] {@link #CHOICE_TYPE_OPEN} or {@link #CHOICE_TYPE_CLOSED} when
   *          {@code choices} is non-null; {@code null} otherwise.
   * @throws IllegalArgumentException
   *           if any argument violates its constraints.
   */
  public BasicAttributeDefinition(String oid, QName name, String description,
      String typeName, String valueType, String contextType, boolean readOnly,
      String[] choices, String choiceType)
  {
    this(oid,
        extractNamespaceURI(name),
        extractLocalName(name),
        description,
        null,
        extractTypeName(typeName),
        extractMaxValueLength(typeName),
        valueType,
        contextType,
        readOnly,
        choices,
        choiceType);    
  }

  private static String extractNamespaceURI(QName name)
  {
    if (name == null)
      throw new IllegalArgumentException("QName must not be null.");
    String ns = name.getNamespaceURI();
    return (ns == null || ns.length() == 0) ? null : ns;
  }

  private static String extractLocalName(QName name)
  {
    if (name == null)
      throw new IllegalArgumentException("QName must not be null.");
    return name.getLocalPart(); // primary constructor validates further
  }

  /**
   * Strips the {@code {n}} suffix from a type name, returning the bare type
   * name, or the original string if no suffix is present.
   */
  private static String extractTypeName(String typeName)
  {
    if (typeName == null)
      return null; // primary constructor will reject this
    int brace = typeName.indexOf('{');
    return (brace == -1) ? typeName : typeName.substring(0, brace);
  }

  /**
   * Extracts the length value from a {@code {n}} suffix, or returns {@code -1}
   * if no suffix is present.
   */
  private static long extractMaxValueLength(String typeName)
  {
    if (typeName == null)
      return -1L;
    int brace = typeName.indexOf('{');
    if (brace == -1)
      return -1L;
    int close = typeName.indexOf('}', brace);
    if (close == -1)
      throw new IllegalArgumentException("Type name '" + typeName
          + "' contains an opening '{' with no matching '}'.");
    String s = typeName.substring(brace + 1, close);
    try
    {
      long v = Long.parseLong(s);
      if (v < 0)
        throw new IllegalArgumentException("Type name '" + typeName
            + "' contains a negative length constraint.");
      return v;
    } catch (NumberFormatException e)
    {
      throw new IllegalArgumentException("Type name '" + typeName
          + "' contains an invalid length constraint '{" + s
          + "}': must be a non-negative integer.");
    }
  }



  // -----------------------------------------------------------------------
  // ItemDefinition — getters
  // -----------------------------------------------------------------------

  /** {@inheritDoc} */
  public String getTypeName()
  {
    return typeName;
  }

  /** {@inheritDoc} */
  public String getValueType()
  {
    return valueType;
  }

  /** {@inheritDoc} */
  public String getContextType()
  {
    return contextType;
  }

  /** Returns the intended usage of this item definition.
  *
  *  <p>Always returns {@link AttributeUsage#userApplication}.
  *  This class does not support other usage values.</p>
  *
  * @return {@link AttributeUsage#userApplication}, always.
  */  
  public AttributeUsage getUsage()
  {
    return AttributeUsage.userApplication;
  }

  /** {@inheritDoc} */
  public boolean isSingleValued()
  {
    return VALUE_TYPE_SINGLE.equals(valueType);
  }

  /** {@inheritDoc} */
  public boolean isOrdered()
  {
    return VALUE_TYPE_SEQ.equals(valueType);
  }

  /** {@inheritDoc} */
  public boolean isReadOnly()
  {
    return readOnly;
  }

  /** {@inheritDoc} */
  public long getMaxValueLength()
  {
    return maxValueLength;
  }

  /** {@inheritDoc} */
  public Set<String> getChoices()
  {
    return choices;
  }  

  /** {@inheritDoc} */
  public String getChoiceType()
  {
    return choiceType;
  }



  // 
}