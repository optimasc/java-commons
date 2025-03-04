package com.optimasc.datatypes;

import java.util.HashMap;

import com.optimasc.datatypes.primitives.BooleanType;

import omg.org.astm.type.TypeReference;

/** Default member object implementation. */
public class DefaultMemberObject implements MemberObject
{
  protected String identifier;
  protected TypeReference typeReference;
  protected AccessKind access;
  protected boolean optional;
  protected HashMap userData;
  
  public DefaultMemberObject(String identifier, TypeReference typeRef, AccessKind access, boolean optional)
  {
    super();
    this.identifier = identifier;
    this.typeReference = typeRef;
    this.access = access;
    this.optional = optional;
  }
  
  public DefaultMemberObject(String identifier, TypeReference typeRef)
  {
    this(identifier,typeRef,AccessKind.Public,false);
  }

  public Object getUserData(String key)
  {
    return userData.get(key);
  }

  public Object setUserData(String key, Object data)
  {
    return userData.put(key, data);
  }

  public String getIdentifier()
  {
    return identifier;
  }

  public int getOffset()
  {
    return 0;
  }

  public TypeReference getDefinitionType()
  {
    return typeReference;
  }

  public void setDefinitionType(TypeReference typeRef)
  {
    typeReference = typeRef;
  }

  public AccessKind getAccessKind()
  {
    return access;
  }

  /** This implementations returns true if 
   *  <code>access</code>, <code>identifier</code>,
   *  <code>type</code> of <code>TypeReference</code> 
   *  are all equal. The identifier equality comparison
   *  is case-sensitivite.
   * 
   */
  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }
      if (!(obj instanceof MemberObject))
      {
          return false;
      }
    MemberObject otherObject = (MemberObject)obj;
    if (this.access.equals(otherObject.getAccessKind())==false)
      return false;
    if (this.identifier.equals(otherObject.getIdentifier())==false)
      return false;
    if (this.typeReference.getType().equals(otherObject.getDefinitionType().getType())==false)
      return false;
    return true;
  }

  public boolean isOptional()
  {
    return optional;
  }
  
  

}
