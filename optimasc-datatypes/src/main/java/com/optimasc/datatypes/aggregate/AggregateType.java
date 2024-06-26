package com.optimasc.datatypes.aggregate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.PackedFacet;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Abstract class used for aggregate types. Two Aggregate types are only
 * considered equal if the list of its members are all equal, including the
 * position in the member list.
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class AggregateType extends Datatype implements PackedFacet
{
  /**
   * Contains the members of this aggregate type. This contains implementations
   * of {@link MemberObject}
   */
  protected List members;
  /**
   * Indicates if data alignment for underlying platform are ignored, and data
   * is byte aligned.
   */
  protected boolean packed;

  /**
   * Constructs an aggregate type with an empty member list.
   * 
   * @param typ
   */
  public AggregateType(int typ)
  {
    super(typ, false);
    this.members = new ArrayList();
  }

  public AggregateType(int typ, MemberObject[] members)
  {
    super(typ, false);
    this.members = Arrays.asList(members);
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
  }

  public Class getClassType()
  {
    return null;
  }

  /**
   * Search for the specified member with specified name in the current
   * aggregate type.
   * 
   * @param name
   *          [in] The identifier of the member to look for.
   * @param ignoreCase
   *          [in] true if the identifier comparison should be done in a
   *          case-insensitive way.
   * @return The member object or <code>null</code> if this member
   *     was not found.
   */
  public MemberObject lookupMember(String name, boolean ignoreCase)
  {
    int i;
    MemberObject var;
    if (ignoreCase == true)
    {
      for (i = 0; i < members.size(); i++)
      {
        var = (MemberObject) members.get(i);
        if (var.getIdentifier().equalsIgnoreCase(name))
        {
          return var;
        }
      }
    }
    else
    {
      for (i = 0; i < members.size(); i++)
      {
        var = (MemberObject) members.get(i);
        if (var.getIdentifier().equals(name))
        {
          return var;
        }
      }
    }
    return null;
  }

  public void addMember(MemberObject member)
  {
    members.add(member);
  }

  public void setMember(int index, MemberObject member)
  {
    members.set(index, member);
  }

  public int getMemberCount()
  {
    return members.size();
  }

  public MemberObject getMember(int index)
  {
    return (MemberObject) members.get(index);
  }

  public boolean isPacked()
  {
    return packed;
  }

  public void setPacked(boolean packed)
  {
    this.packed = packed;
  }

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
    if (!(obj instanceof AggregateType))
    {
      return false;
    }
    AggregateType otherObj = (AggregateType) obj;
    if (otherObj.members.equals(members) == false)
    {
      return false;
    }
    return true;
  }

}
