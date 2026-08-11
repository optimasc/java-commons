package com.optimasc.schema;

import java.text.Format;

/**
 * Immutable descriptor of a named data type in the standard type catalog,
 * implementing {@link TypeDefinition} for use as a lightweight schema entry.
 *
 * <p>Each {@code TypeEntry} binds together the catalog identity required by
 * {@link Definition} (name, OID, namespace, description) with the
 * value-space contract required by {@link TypeDefinition} (value class).</p>
 *
 * <p>String serialisation of values (ASN.1, XML Schema, LDAP, …) is
 * outside the scope of this class and is the responsibility of the
 * consuming backend adapter.</p>
 *
 * @see StandardTypes
 * @see TypeDefinition
 * @see BasicDefinition
 * @author Carl Eric Codere
 */
public final class BasicTypeDefinition extends BasicDefinition implements TypeDefinition
{
  /** Expected Java class for runtime values. Never {@code null}. */
  private final Class<?> valueClass;

  /**
   * Constructs an immutable type entry.
   *
   * @param oid          [in] OID in dotted-decimal notation, or {@code null}.
   *                     When non-null and non-empty, must be a syntactically
   *                     valid OID.
   * @param namespaceURI [in] Namespace URI, or {@code null} when no namespace
   *                     applies. When non-null and non-empty, must end with
   *                     {@code '/'} or {@code '#'}.
   * @param localName    [in] Common name / local name. Must not be {@code null}
   *                     or empty, must not exceed
   *                     {@link Definition#NAME_MAX_LENGTH} characters, and must
   *                     not contain {@code ':'}.
   * @param description  [in] Human-readable description, or {@code null}. When
   *                     non-null, must not exceed
   *                     {@link Definition#DESC_MAX_LENGTH} characters.
   * @param valueClass   [in] Expected Java class for in-memory values. Must not
   *                     be {@code null}.
   * @throws IllegalArgumentException if any argument violates its constraints.
   */  
  public BasicTypeDefinition(String oid, String namespaceURI, String localName, String description, Class<?> valueClass)
  {
    super(oid,namespaceURI,localName,description);
    this.valueClass = valueClass;
  }

  // -----------------------------------------------------------------------
  // TypeDefinition — value-space accessors
  // -----------------------------------------------------------------------

  /** {@inheritDoc} */
  public Class<?> getValueClass()
  {
    return valueClass;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * validation verifies {@link Class#isInstance(Object)} 
   * against {@link #getValueClass()}.
   * </p>
   */
  public boolean isValid(Object value)
  {
    return valueClass.isInstance(value);
  }

}