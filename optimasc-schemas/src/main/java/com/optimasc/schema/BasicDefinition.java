package com.optimasc.schema;

/**
 * Immutable, plain-field implementation of {@link Definition} for use in
 * contexts that need schema definition objects.
 *
 * <p>All fields are set at construction time and are never modified
 * afterwards. No setters exist on this class or any subclass in the
 * {@code Basic*} family; the only way to obtain a different value is to
 * construct a new instance. This makes instances safe to share across
 * threads and to use as map keys without defensive copying.</p>
 *
 * <p>Two instances are considered equal when they share the same OID
 * (case-insensitive; both may be {@code null}) and the same expanded name
 * (case-sensitive). {@link #hashCode()} is consistent with that contract.
 * {@link #toString()} returns the local name, satisfying the Swing
 */
public class BasicDefinition implements Definition
{
  // -----------------------------------------------------------------------
  // Fields — Definition
  // -----------------------------------------------------------------------

  /** OID or other standardised identifier. May be {@code null}. */
  protected final String oid;

  /** Namespace URI. {@code null} means no namespace. */
  protected final String namespaceURI;

  /** Local name. Never {@code null} or empty. */
  protected final String localName;

  /** Human-readable description. May be {@code null}. */
  protected final String description;

  /**
   * Origin of this definition (e.g. {@code "RFC 4519"}). May be {@code null}.
   */
  protected final String origin;
  
  /** Whether this definition is obsolete. Defaults to {@code false}. */
  protected final boolean obsolete;

  /** Full constructor.
   *
   * @param oid          [in] OID in dotted-decimal notation, or {@code null}.
   * @param namespaceURI [in] Namespace URI ending with {@code '/'} or
   *                     {@code '#'}, or {@code null}.
   * @param localName    [in] Local name. Must not be {@code null} or empty,
   *                     and must not exceed {@link Definition#NAME_MAX_LENGTH}
   *                     characters.
   * @param description  [in] Human-readable description, or {@code null}.
   *                     When non-null, must not exceed
   *                     {@link Definition#DESC_MAX_LENGTH} characters.
   * @param origin       [in] Standard or specification this definition
   *                     originates from, or {@code null}.
   * @param obsolete     [in] {@code true} if this definition is obsolete.
   * @throws IllegalArgumentException if any argument violates its constraints.
   */
  public BasicDefinition(String oid, String namespaceURI, String localName,
      String description, String origin, boolean obsolete)
  {
    // ---- id ----
    if (oid != null && oid.length() > 0)
      validateOID(oid);
    this.oid = (oid != null && oid.length() == 0) ? null : oid;
    
    // ---- namespaceURI ----
    if (namespaceURI != null && namespaceURI.length() > 0)
    {
      char last = namespaceURI.charAt(namespaceURI.length() - 1);
      if (last != '/' && last != '#')
        throw new IllegalArgumentException(
            "Namespace URI must end with '/' or '#', but got: '"
            + namespaceURI + "'.");
    }
    this.namespaceURI = namespaceURI;


    // ---- localName ----
    if (localName == null || localName.length() == 0)
      throw new IllegalArgumentException(
          "Local name must not be null or empty.");
    if (localName.length() > NAME_MAX_LENGTH)
      throw new IllegalArgumentException(
          "Local name must not exceed " + NAME_MAX_LENGTH
              + " characters, but has " + localName.length() + ".");
    if (localName.indexOf(':') >= 0)
      throw new IllegalArgumentException(
          "Local name must not contain a namespace prefix ':'. " + "Got: '"
              + localName + "'.");
    this.localName = localName;
    
    
    // ---- description ----
    if (description != null && description.length() > DESC_MAX_LENGTH)
      throw new IllegalArgumentException(
          "Description must not exceed " + DESC_MAX_LENGTH
          + " characters, but has " + description.length() + ".");
    this.description = description;
    
    this.origin = origin;
    this.obsolete = obsolete;
  }

  /** Convenience constructor with {@code obsolete} defaulting to
   *  {@code false} and {@code origin} defaulting to {@code null}.
   *
   * @param oid          [in] OID, or {@code null}.
   * @param namespaceURI [in] Namespace URI, or {@code null}.
   * @param localName    [in] Local name. Must not be {@code null} or empty.
   * @param description  [in] Description, or {@code null}.
   * @throws IllegalArgumentException if any argument violates its constraints.
   */
  public BasicDefinition(String oid, String namespaceURI, String localName,
      String description)
  {
    this(oid, namespaceURI, localName, description, null, false);
  }

  /** Convenience constructor with all optional fields except {@code origin}.
   *
   * @param oid          [in] OID, or {@code null}.
   * @param namespaceURI [in] Namespace URI, or {@code null}.
   * @param localName    [in] Local name. Must not be {@code null} or empty.
   * @param description  [in] Description, or {@code null}.
   * @param origin       [in] Origin string, or {@code null}.
   * @throws IllegalArgumentException if any argument violates its constraints.
   */
  public BasicDefinition(String oid, String namespaceURI, String localName,
      String description, String origin)
  {
    this(oid, namespaceURI, localName, description, origin, false);
  }


  @Override
  public String getLocalName()
  {
    return localName;
  }
  

  @Override
  public String getNamespaceURI()
  {
    return namespaceURI;
  }

  /** {@inheritDoc} */
  public String getExpandedName()
  {
    String ns    = getNamespaceURI();
    String local = getLocalName();
    return ns != null ? ns + local : local;
  }

  @Override
  public String getOID()
  {
    return oid;
  }

  @Override
  public String getDescription()
  {
    return description;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Always returns {@code false} for instances of this class; the constructor
   * does not accept an obsolescence flag.
   * </p>
   */
  @Override
  public boolean isObsolete()
  {
    return obsolete;
  }
  
  // -----------------------------------------------------------------------
  // Object overrides
  // -----------------------------------------------------------------------

  /**
   * Returns the local name of this definition, consistent with the Swing
   * cell-renderer contract ({@code JList}, {@code JComboBox}, {@code JTree}
   * call {@code toString()} directly).
   */
  public String toString()
  {
    return localName;
  }
  

  @Override
  public String getOrigin()
  {
    return origin;
  }
  
  /**
   * Returns a hash code consistent with {@link #equals(Object)}, based on the
   * lower-cased OID and the expanded name.
   */
  public int hashCode()
  {
    int result = 17;
    result = 31 * result + (oid != null ? oid.toLowerCase().hashCode() : 0);
    String name = getExpandedName();
    result = 31 * result + (name != null ? name.hashCode() : 0);
    return result;
  }
  
  /**
   * Two {@code AttributeDefinition} instances are equal if and only if they
   * have the same OID (case-insensitive, both may be {@code null}) and the same
   * expanded name (case-sensitive).
   */
  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!(obj instanceof Definition))
      return false;
    Definition other = (Definition) obj;
    String thisID = getOID();
    String otherID = other.getOID();
    if (thisID == null ? otherID != null : !thisID.equalsIgnoreCase(otherID))
      return false;
    String thisName = getExpandedName();
    String otherName = other.getExpandedName();
    return thisName == null ? otherName == null : thisName.equals(otherName);
  }
  
  
  
  //-----------------------------------------------------------------------
  // helpers
  // -----------------------------------------------------------------------

  /**
   * Validates that {@code oid} is a syntactically correct OID in dotted-decimal
   * notation.
   *
   * @param oid
   *          [in] Must not be {@code null} or empty.
   * @throws IllegalArgumentException
   *           if {@code oid} is not valid.
   */
  protected static void validateOID(String oid)
  {
    int start = 0;
    int len = oid.length();
    while (start <= len)
    {
      int dot = oid.indexOf('.', start);
      int end = (dot == -1) ? len : dot;
      if (end == start)
        throw new IllegalArgumentException(
            "OID '" + oid + "' contains an empty arc.");
      String arc = oid.substring(start, end);
      for (int i = 0; i < arc.length(); i++)
      {
        if (!Character.isDigit(arc.charAt(i)))
          throw new IllegalArgumentException(
              "OID '" + oid + "' contains a non-numeric arc: '" + arc + "'.");
      }
      if (arc.length() > 1 && arc.charAt(0) == '0')
        throw new IllegalArgumentException("OID '" + oid
            + "' contains an arc with a leading zero: '" + arc + "'.");
      if (dot == -1)
        break;
      start = dot + 1;
    }
  }

}
