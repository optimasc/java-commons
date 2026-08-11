package com.optimasc.schema;

/** Validates that an in-memory value conforms to a type's value
 *  space — range, length, enumeration, and structural constraints —
 *  independently of any string or wire-format representation.
 *
 *  <p>Validation operates on the value as it exists in memory.  
 *  It answers only the question "is this a legal value of this type?"
 *  and makes no commitment about how the value is encoded in any
 *  external format.  For example if we wish to represent
 *  a unsigned octet between 0-255 for a specific type
 *  and the object passed is an integral Number then the
 *  method would verify that the value is within 0 and 255.</p>
 *
 *  <h3>Contract</h3>
 *  <ul>
 *    <li>{@link #isValid(Object)} returns {@code true} if and only
 *        if the value satisfies all constraints of the associated
 *        type (range, length, enumeration, pattern, and so on).</li>
 *    <li>A {@code null} argument is always considered invalid and
 *        must return {@code false}; implementations must never
 *        throw on a {@code null} argument.</li>
 *    <li>A value whose runtime class is incompatible with the
 *        expected value class is a programming error; implementations
 *        must throw {@link IllegalArgumentException} in that case,
 *        not return {@code false}, so the caller receives immediate
 *        diagnostic feedback rather than a silent validity failure.</li>
 *    <li>No checked exception is thrown under any circumstances;
 *        constraint failures are reported as {@code false}, not as
 *        exceptions.</li>
 *  </ul>
 *
 *  <h3>Intentional design note</h3>
 *  <p>This interface is intentionally {@code Predicate}-shaped
 *  (one boolean method over {@code Object}) for future compatibility
 *  with {@code java.util.function.Predicate} on Java&nbsp;8 and later.
 *  It is defined here rather than reusing a library predicate in order
 *  to preserve the zero-dependency contract of the base schema
 *  package and to carry domain-specific semantics in its name and
 *  documentation.</p>
 *
 *  @author Carl Eric Codere
 */
public interface ValueValidator
{
  /** Returns {@code true} if {@code value} is a legal value of the
   *  associated type.
   *
   *  <p>The check covers all value-space constraints of the type:
   *  numeric range ({@code minInclusive}/{@code maxInclusive}),
   *  length ({@code minLength}/{@code maxLength}), enumeration
   *  (allowed-values set), and structural or pattern constraints.
   *  It does not check anything about how the value would be
   *  serialised.</p>
   *
   *  <p>A {@code null} argument always returns {@code false}.
   *  A value whose runtime class is incompatible with the type's
   *  expected value class throws {@link IllegalArgumentException}
   *  immediately, as this indicates a programming error rather than
   *  a domain validity failure.</p>
   *
   * @param value [in] The in-memory value to validate.  May be
   *              {@code null}; {@code null} is always invalid.
   * @return {@code true} if {@code value} satisfies all constraints
   *         of this type; {@code false} if it is {@code null} or
   *         violates any constraint.
   * @throws IllegalArgumentException if the runtime class of
   *   {@code value} is incompatible with the value class this
   *   validator expects.
   */
  boolean isValid(Object value);
}