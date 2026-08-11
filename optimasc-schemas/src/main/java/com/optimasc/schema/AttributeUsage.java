package com.optimasc.schema;

/** Indicates the intended usage of an attribute definition within a
 *  directory or data store.
 *
 *  <p>This enumeration-style class is the portable equivalent of the
 *  USAGE field defined in ITU-T X.501 and RFC 4512
 *  {@code AttributeTypeDescription}. It distinguishes attributes that
 *  are managed by user applications from those that are managed
 *  exclusively by the underlying directory service or system.</p>
 *
 *  <p>In LDAP / X.500, only {@link #userApplication} attributes may
 *  normally be written by client applications. Attributes with any of
 *  the other usage values are considered operational and are typically
 *  maintained by the directory server itself; clients may read them
 *  (subject to access control) but generally may not write them.</p>
 *
 *  <p>Instances of this class are singletons; use the pre-defined
 *  constants {@link #userApplication}, {@link #directoryOperation},
 *  {@link #distributedOperation}, and {@link #dSAOperation} rather
 *  than constructing new instances.</p>
 *
 *  <p>Use {@link #valueOf(String)} to parse a string into one of
 *  these constants. Use {@link #toString()} to obtain the canonical
 *  string representation as defined in RFC 4512.</p>
 *
 *  @see ItemDefinition#getUsage()
 *  @author Carl Eric Codere
 */
public final class AttributeUsage implements Comparable
{
  // -----------------------------------------------------------------------
  // Pre-defined constants
  // -----------------------------------------------------------------------

  /** Attribute is for use by user applications.
   *
   *  <p>This is the normal case for schema attributes that client
   *  applications read and write (e.g. {@code cn}, {@code mail},
   *  {@code description}). Equivalent to {@code userApplications}
   *  in RFC 4512.</p>
   */
  public static final AttributeUsage userApplication =
      new AttributeUsage("userApplications", 0);

  /** Attribute is a directory operational attribute managed by the
   *  directory service itself.
   *
   *  <p>Directory operational attributes hold information used by the
   *  directory server to administer and operate the directory (e.g.
   *  {@code subschemaSubentry}, {@code namingContexts}). Clients may
   *  read them subject to access control, but the server manages their
   *  values. Equivalent to {@code directoryOperation} in RFC 4512.</p>
   */
  public static final AttributeUsage directoryOperation =
      new AttributeUsage("directoryOperation", 1);

  /** Attribute is a distributed operational attribute shared across
   *  DSAs participating in a distributed directory.
   *
   *  <p>These attributes are used to coordinate information across
   *  multiple Directory System Agents (DSAs) in a distributed X.500
   *  deployment (e.g. {@code accessControlScheme}). Equivalent to
   *  {@code distributedOperation} in RFC 4512.</p>
   */
  public static final AttributeUsage distributedOperation =
      new AttributeUsage("distributedOperation", 2);

  /** Attribute is a DSA-specific operational attribute local to a
   *  single DSA.
   *
   *  <p>DSA operational attributes are used by a single Directory
   *  System Agent for its own internal administration and are not
   *  shared across DSAs (e.g. {@code dSAQuality}). Equivalent to
   *  {@code dSAOperation} in RFC 4512.</p>
   */
  public static final AttributeUsage dSAOperation =
      new AttributeUsage("dSAOperation", 3);

  // -----------------------------------------------------------------------
  // All values — used by valueOf()
  // -----------------------------------------------------------------------

  private static final AttributeUsage[] VALUES = {
    userApplication,
    directoryOperation,
    distributedOperation,
    dSAOperation
  };

  // -----------------------------------------------------------------------
  // Fields
  // -----------------------------------------------------------------------

  /** RFC 4512 string representation. */
  private final String name;

  /** Ordinal for {@link #compareTo(Object)}. */
  private final int ordinal;

  // -----------------------------------------------------------------------
  // Constructor (private — use the constants)
  // -----------------------------------------------------------------------

  private AttributeUsage(String name, int ordinal)
  {
    this.name    = name;
    this.ordinal = ordinal;
  }

  // -----------------------------------------------------------------------
  // Factory
  // -----------------------------------------------------------------------

  /** Parses the RFC 4512 string representation of an attribute usage
   *  into one of the pre-defined constants.
   *
   *  <p>Accepted values (case-sensitive, as specified by RFC 4512):</p>
   *  <ul>
   *    <li>{@code "userApplications"}</li>
   *    <li>{@code "directoryOperation"}</li>
   *    <li>{@code "distributedOperation"}</li>
   *    <li>{@code "dSAOperation"}</li>
   *  </ul>
   *
   * @param text [in] The RFC 4512 usage string. Must not be
   *             {@code null}.
   * @return The corresponding {@link AttributeUsage} constant.
   * @throws IllegalArgumentException if {@code text} does not match
   *                                  any known usage value.
   */
  public static AttributeUsage valueOf(String text)
  {
    for (int i = 0; i < VALUES.length; i++)
    {
      if (VALUES[i].name.equals(text))
        return VALUES[i];
    }
    throw new IllegalArgumentException(
        "Cannot parse '" + text + "' into an AttributeUsage value. "
        + "Expected one of: userApplications, directoryOperation, "
        + "distributedOperation, dSAOperation.");
  }

  // -----------------------------------------------------------------------
  // Comparable
  // -----------------------------------------------------------------------

  /** Compares this usage to another by ordinal. */
  public int compareTo(Object that)
  {
    return this.ordinal - ((AttributeUsage) that).ordinal;
  }

  // -----------------------------------------------------------------------
  // Object overrides
  // -----------------------------------------------------------------------

  /** Returns the RFC 4512 string representation of this usage value
   *  (e.g. {@code "userApplications"}, {@code "directoryOperation"}).
   */
  public String toString()
  {
    return name;
  }
}
