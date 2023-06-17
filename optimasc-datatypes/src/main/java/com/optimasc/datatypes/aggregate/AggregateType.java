package com.optimasc.datatypes.aggregate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.PackedFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;

public abstract class AggregateType extends Datatype implements PackedFacet
{
  protected static final Object UNKNOWN_OBJECT = new Object();
  
  
  /** Contains the members of this aggregate type. This contains implementations of {@link MemberObject}  */
  protected List members;
  /** Indicates if data alignment for underlying platform are ignored, and data is byte aligned. */
  protected boolean packed;
  
  public AggregateType(int typ)
  {
    super(typ,false);
  }
  
  public AggregateType(int typ, MemberObject[] members)
  {
    super(typ,false);
    this.members = Arrays.asList(members);
  }
  
  
  public Object getObjectType()
  {
    return null;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
  }

  public Class getClassType()
  {
    return null;
  }

  /** Search for the specified member with specified name
   *  in the current aggregate type.
   *
   * @return null if field with specified name is not found, otherwise
   *   the actual field definition.
   */
  public MemberObject lookupMember(String name)
  {
      int i;
      MemberObject var;
      for (i = 0; i < members.size(); i++)
      {
          var = (MemberObject) members.get(i);
          if (var.getIdentifier().equals(name))
          {
             return var;
          }
      }
      return null;
  }
  
  public void addMember(MemberObject member)
  {
    members.add(member);
  }
  
  public void setMember(int index,MemberObject member)
  {
    members.add(index,member);
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
  


}
