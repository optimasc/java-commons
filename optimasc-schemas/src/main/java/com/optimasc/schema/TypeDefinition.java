package com.optimasc.schema;

/** Immutable descriptor of a named data type in a schema catalog.
 *
 *  <p>A {@code TypeDefinition} binds a catalog identity (name, OID,
 *  description — inherited from {@link Definition}) to a value space:
 *  the set of in-memory values that are legal for this type.  It
 *  answers two questions that any consumer of typed metadata needs:</p>
 *  <ol>
 *    <li><em>What Java class carries values of this type?</em>
 *        ({@link #getValueClass()})</li>
 *    <li><em>Is this particular value legal?</em>
 *        ({@link #isValid(Object)})</li>
 *  </ol>
 *
 *  <p>This interface is intentionally minimal and carries no dependency
 *  on any external type system or validation library.  How the value
 *  space is actually enforced  is left entirely
 *  to the implementing class.  Similarly, string serialisation of
 *  values (ASN.1, XML Schema, LDAP, SQL, …) is outside the scope of
 *  this interface and is the responsibility of the consuming backend
 *  adapter.</p>
 *
 *  @see Definition
 *  @author Carl Eric Codere
 */
public interface TypeDefinition extends Definition
{
  // -----------------------------------------------------------------------
  // Object-class name constant
  // -----------------------------------------------------------------------

  /** The canonical type-discriminator value for an
   *  {@link TypeDefinition}, suitable for use under
   *  {@link Definition#KEY_OBJECT_CLASS_NAME}.
   *
   *  <p>The value ({@value}) is intentionally human-readable and maps
   *  to the Java interface role so that cross-language serialisers can
   *  recover the definition kind without additional metadata.</p>
   */
  String OBJECT_CLASS_NAME = "TypeDefinition";
  
  
  /** Returns the Java class whose instances represent in-memory
   *  values of this type.
   *
   *  <p>Consumers may use {@link Class#isInstance(Object)} as a fast
   *  pre-check before calling {@link #isValid(Object)}.  The class
   *  returned here is the same class that {@link #isValid(Object)}
   *  expects; passing an instance of a different class to
   *  {@code isValid} will throw {@link IllegalArgumentException}.</p>
   *
   * @return Non-null Java class for in-memory values of this type.
   */
  Class<?> getValueClass();

  /** Returns {@code true} if {@code value} is a legal value of this
   *  type.
   *
   *  <p>The check covers all value-space constraints of the type as
   *  defined by the implementation — which may include numeric range,
   *  string length, enumeration membership, structural or pattern
   *  constraints, or any combination thereof.  It makes no commitment
   *  about how the value would be serialised in any external format.</p>
   *
   *  <p>Contract:</p>
   *  <ul>
   *    <li>A {@code null} argument always returns {@code false};
   *        implementations must never throw on {@code null}.</li>
   *    <li>A value whose runtime class is incompatible with
   *        {@link #getValueClass()} throws
   *        {@link IllegalArgumentException} immediately — this is a
   *        programming error, not a domain validity failure, and must
   *        not be silently swallowed as {@code false}.</li>
   *    <li>No checked exception is thrown under any circumstances;
   *        all constraint failures are reported as {@code false}.</li>
   *  </ul>
   *
   * @param value [in] The in-memory value to validate.  May be
   *              {@code null}; {@code null} is always invalid.
   * @return {@code true} if {@code value} satisfies all value-space
   *         constraints of this type; {@code false} if it is
   *         {@code null} or violates any constraint.
   * @throws IllegalArgumentException if the runtime class of
   *   {@code value} is incompatible with {@link #getValueClass()}.
   */
  boolean isValid(Object value);
}