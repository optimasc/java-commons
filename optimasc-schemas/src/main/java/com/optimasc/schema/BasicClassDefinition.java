package com.optimasc.schema;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Immutable, plain-field implementation of {@link ClassDefinition} for use in
 * contexts that need schema class-definition objects but do not require the
 * full {@link com.optimasc.entity.Entity} contract.
 *
 * <p>All fields are set at construction time and are never modified
 * afterwards. No setters exist on this class; the only way to obtain a
 * different value is to construct a new instance. This makes instances safe
 * to share across threads and to use as map keys without defensive
 * copying.</p>
 *
 * <p>Typical usage is in metadata-core contexts where class definitions are
 * loaded from a CSV catalog and consumed as pure schema objects. When
 * entity-tree, attribute-backed storage, or
 * {@link com.optimasc.entity.ModifiableEntity} support is needed instead,
 * use {@link com.optimasc.entity.schema.DefaultClassDefinition}.</p>
 *
 * <p>The distinction between {@code null} and an empty array for
 * {@code allowedParents} and {@code mandatoryAttributes} is meaningful at
 * construction time:</p>
 * <ul>
 *   <li>{@code null} — unspecified; the constraint is unknown or not
 *       applicable.</li>
 *   <li>empty array — explicitly declared as none; no parents are
 *       permitted / no items are mandatory.</li>
 * </ul>
 * <p>Both cases collapse to an empty, unmodifiable {@link Set} at the
 * getter level; callers that need to distinguish the two must do so before
 * construction.</p>
 *
 * <p>This class is not {@code final}; subclasses must remain immutable —
 * they may add final fields and additional constructors but must not
 * introduce setters or any other state-mutating methods.</p>
 *
 * @see ClassDefinition
 * @see BasicDefinition
 * @see BasicItemDefinition
 * @see com.optimasc.entity.schema.DefaultClassDefinition
 * @author Carl Eric Codere
 */
public class BasicClassDefinition extends BasicDefinition
    implements ClassDefinition
{
  // -----------------------------------------------------------------------
  // Fields
  // -----------------------------------------------------------------------

  /** Kind of this object class. Never {@code null}. */
  protected final ObjectClassKind kind;

  /** Expanded name or OID of the superclass, or {@code null}. */
  protected final String superClass;

  /**
   * Expanded names or OIDs of mandatory items. {@code null} means
   * unspecified; an empty set means explicitly none. Never contains
   * {@code null} or empty-string elements.
   */
  protected final Set<String> mandatoryAttributes;

  /**
   * Expanded names or OIDs of allowed parent classes. {@code null} means
   * unspecified (any parent permitted); an empty set means explicitly none.
   * Never contains {@code null} or empty-string elements.
   */
  protected final Set<String> allowedParents;

  /** Whether instances of this class are restricted from user modification. */
  protected final boolean restricted;

  /**
   * Java class to instantiate for instances of this class, or {@code null}
   * if not specified.
   */
  protected final Class<?> clz;

  // -----------------------------------------------------------------------
  // Constructors
  // -----------------------------------------------------------------------

  /**
   * Full constructor.
   *
   * @param oid                 [in] OID in dotted-decimal notation, or
   *                            {@code null}.
   * @param namespaceURI        [in] Namespace URI ending with {@code '/'}
   *                            or {@code '#'}, or {@code null}.
   * @param localName           [in] Local name. Must not be {@code null}
   *                            or empty, and must not exceed
   *                            {@link Definition#NAME_MAX_LENGTH} characters.
   * @param description         [in] Human-readable description, or
   *                            {@code null}. When non-null must not exceed
   *                            {@link Definition#DESC_MAX_LENGTH} characters.
   * @param origin              [in] Standard or specification this definition
   *                            originates from, or {@code null}.
   * @param superClass          [in] Expanded name or OID of the superclass,
   *                            or {@code null} if none.
   * @param kind                [in] Object class kind. Must not be
   *                            {@code null}.
   * @param allowedParents      [in] Expanded names or OIDs of permitted
   *                            parent classes. {@code null} means
   *                            unspecified; empty means explicitly none.
   *                            Each non-null element must be non-empty; if
   *                            it starts with a digit it must be a valid
   *                            dotted-decimal OID.
   * @param mandatoryAttributes [in] Expanded names or OIDs of mandatory
   *                            items. {@code null} means unspecified; empty
   *                            means explicitly none. Each non-null element
   *                            must be non-empty; if it starts with a digit
   *                            it must be a valid dotted-decimal OID.
   * @param clz                 [in] Java class to instantiate for instances
   *                            of this class, or {@code null}.
   * @throws IllegalArgumentException if any argument violates its
   *   constraints.
   */
  public BasicClassDefinition(String oid, String namespaceURI, String localName,
      String description, String origin, String superClass, ObjectClassKind kind,
      String[] allowedParents, String[] mandatoryAttributes, Class<?> clz)
  {
    super(oid, namespaceURI, localName, description, origin);

    if (kind == null)
      throw new IllegalArgumentException("kind must not be null.");
    this.kind       = kind;
    this.superClass = superClass;
    this.restricted = false;
    this.clz        = clz;

    this.allowedParents      = internNameArray(allowedParents,      "allowedParents");
    this.mandatoryAttributes = internNameArray(mandatoryAttributes, "mandatoryAttributes");
  }

  /**
   * Convenience constructor with {@code kind} defaulting to
   * {@link ObjectClassKind#structuralClass}, {@code origin} and {@code clz}
   * defaulting to {@code null}, and {@code restricted} to {@code false}.
   *
   * @param oid                 [in] OID, or {@code null}.
   * @param namespaceURI        [in] Namespace URI, or {@code null}.
   * @param localName           [in] Local name. Must not be {@code null}
   *                            or empty.
   * @param description         [in] Description, or {@code null}.
   * @param superClass          [in] Expanded name or OID of the superclass,
   *                            or {@code null}.
   * @param allowedParents      [in] Expanded names or OIDs of permitted
   *                            parent classes. {@code null} means
   *                            unspecified; empty means explicitly none.
   * @param mandatoryAttributes [in] Expanded names or OIDs of mandatory
   *                            items. {@code null} means unspecified; empty
   *                            means explicitly none.
   * @throws IllegalArgumentException if any argument violates its
   *   constraints.
   */
  public BasicClassDefinition(String oid, String namespaceURI, String localName,
      String description, String superClass, String[] allowedParents,
      String[] mandatoryAttributes)
  {
    this(oid, namespaceURI, localName, description, null, superClass,
        ObjectClassKind.structuralClass, allowedParents, mandatoryAttributes,
        null);
  }

  // -----------------------------------------------------------------------
  // ClassDefinition — typed accessors
  // -----------------------------------------------------------------------

  /** {@inheritDoc} */
  public ObjectClassKind getKind()
  {
    return kind;
  }

  /** {@inheritDoc} */
  public boolean isAbstract()
  {
    return ObjectClassKind.abstractClass.equals(kind);
  }

  /** {@inheritDoc} */
  public String getSuperClass()
  {
    return superClass;
  }

  /**
   * {@inheritDoc}
   *
   * <p>Returns an empty set when the mandatory-attributes array supplied
   * at construction time was either {@code null} (unspecified) or empty
   * (explicitly none).</p>
   */
  public Set<String> getMandatoryAttributes()
  {
    return mandatoryAttributes != null
        ? mandatoryAttributes
        : Collections.<String>emptySet();
  }

  /**
   * {@inheritDoc}
   *
   * <p>Returns an empty set when the allowed-parents array supplied at
   * construction time was either {@code null} (unspecified) or empty
   * (explicitly none).</p>
   */
  public Set<String> getAllowedParents()
  {
    return allowedParents != null
        ? allowedParents
        : Collections.<String>emptySet();
  }

  /** {@inheritDoc} */
  public boolean isRestricted()
  {
    return restricted;
  }

  /** {@inheritDoc} */
  public Class<?> getClz()
  {
    return clz;
  }

  // -----------------------------------------------------------------------
  // Private helpers
  // -----------------------------------------------------------------------

  /**
   * Converts a {@code String[]} of expanded names or OIDs into an
   * unmodifiable, insertion-order-preserving {@link Set}, or returns
   * {@code null} when the input is {@code null} (unspecified).
   *
   * <p>Each element must be non-null and non-empty. If an element starts
   * with a digit it is validated as a dotted-decimal OID via
   * {@link #validateOID(String)}.</p>
   *
   * @param values    [in] Source array, or {@code null}.
   * @param fieldName [in] Name of the field being validated, used in
   *                  exception messages.
   * @return An unmodifiable, insertion-ordered {@link Set}, or {@code null}
   *         if {@code values} is {@code null}.
   * @throws IllegalArgumentException if any element is null, empty, or an
   *   invalid OID.
   */
  private static Set<String> internNameArray(String[] values, String fieldName)
  {
    if (values == null)
      return null;
    if (values.length == 0)
      return Collections.<String>emptySet();
    LinkedHashSet<String> set = new LinkedHashSet<String>(values.length);
    for (int i = 0; i < values.length; i++)
    {
      String v = values[i];
      if (v == null || v.length() == 0)
        throw new IllegalArgumentException(
            fieldName + "[" + i + "] must not be null or empty.");
      if (Character.isDigit(v.charAt(0)))
        validateOID(v);
      set.add(v);
    }
    return Collections.unmodifiableSet(set);
  }
}