package com.optimasc.datatypes;

import com.optimasc.utils.UserConfiguration;

import omg.org.astm.type.TypeReference;

/** Represents members of an aggregate type. This base
 *  interface should be implemented by the different 
 *  implementations to represent fields/attributes/methods
 *  inside a type definition.
 * 
 * @author Carl Eric Codere
 *
 */
public interface MemberObject extends UserConfiguration
{
  /** Represents the member identifier name */
  public String getIdentifier();
  /** Represents the offset of this member in the aggregate type. 
   *  The first offset is 0. This can be {@link java.long.Integer#MIN_INTEGER} 
   *  if this is not supported. */
  public int getOffset();
  /** Represents a reference to a type associated with this field. This
   *  value may be <code>null</code> depending on the aggregate type. */
  public TypeReference getDefinitionType();
  /** Set a reference to a type associated with this field. This
   *  value may be <code>null</code> depending on the aggregate type. */
  public void setDefinitionType(TypeReference typeRef);
  /** Returns the visibility of this member object in relation
   *  to the containing type.
   */
  public AccessKind getAccessKind();
}
