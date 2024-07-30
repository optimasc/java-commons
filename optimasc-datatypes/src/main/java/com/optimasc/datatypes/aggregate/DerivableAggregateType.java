package com.optimasc.datatypes.aggregate;

import java.util.ArrayList;
import java.util.List;

import omg.org.astm.type.NamedTypeReference;

import com.optimasc.datatypes.AccessKind;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an object oriented aggregate type. This
 *  is an abstract class representing aggregate types
 *  that may be derived from others. 
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class DerivableAggregateType extends AggregateType
{
  /** The list of type names this type derives from, can
   *  be null,  but if it is not null, the type of this type 
   *  reference needs to be of type {@link DerivableAggregateType}.
   *  */
  protected List /*<NamedTypeReference>*/ derivesFrom;
  protected AccessKind accessKind;
  protected boolean _abstract;
  
  public DerivableAggregateType(NamedTypeReference parent)
  {
    super();
    addDerivesFrom(parent);
  }
  
  public DerivableAggregateType(NamedTypeReference parent, MemberObject[] members)
  {
    super(members);
    addDerivesFrom(parent);
  }
  
  
  public DerivableAggregateType()
  {
    super();
    this.derivesFrom = new ArrayList();
  }
  

  public AccessKind getAccessKind()
  {
    return accessKind;
  }

  public void setAccessKind(AccessKind accessKind)
  {
    this.accessKind = accessKind;
  }
  
  public boolean isAbstract()
  {
    return _abstract;
  }

  public void setAbstract(boolean _abstract)
  {
    this._abstract = _abstract;
  }


  /**
   * Search for the specified parent name and returns it if
   * it is found.
   * 
   * @param name
   *          [in] The name of type aggregate type to look for.
   * @param ignoreCase
   *          [in] true if the name comparison should be done in a
   *          case-insensitive way.
   * @return The derivable aggregate object or <code>null</code> if it
   *    was not found.
   */
  public NamedTypeReference lookupParent(String name, boolean ignoreCase)
  {
    int i;
    NamedTypeReference var;
    if (derivesFrom == null)
      return null;
    if (ignoreCase == true)
    {
      for (i = 0; i < derivesFrom.size(); i++)
      {
        var = (NamedTypeReference) derivesFrom.get(i);
        if (var.getTypeName().equalsIgnoreCase(name))
        {
          return var;
        }
      }
    }
    else
    {
      for (i = 0; i < derivesFrom.size(); i++)
      {
        var = (NamedTypeReference) derivesFrom.get(i);
        if (var.getTypeName().equals(name))
        {
          return var;
        }
      }
    }
    return null;
  }

  public void addDerivesFrom(NamedTypeReference member)
  {
    if (member == null)
    {
      return;
    }
    if ((member.getType() instanceof DerivableAggregateType)==false)
    {
      throw new IllegalArgumentException("The type of the reference must be an instance of "+DerivableAggregateType.class.getName());
    }
    derivesFrom.add(member);
  }

  public void setDerivesFrom(int index, NamedTypeReference member)
  {
    if ((member.getType() instanceof DerivableAggregateType)==false)
    {
      throw new IllegalArgumentException("The type of the reference must be an instance of "+DerivableAggregateType.class.getName());
    }
    derivesFrom.set(index, member);
  }

  public int getDerivesFromCount()
  {
    return derivesFrom.size();
  }

  public NamedTypeReference getDerivesFrom(int index)
  {
    return (NamedTypeReference) derivesFrom.get(index);
  }

  public boolean equals(Object obj)
  {
    boolean result = super.equals(obj);
    if (result == false)
      return false;
    if (!(obj instanceof DerivableAggregateType))
    {
      return false;
    }
    DerivableAggregateType otherObj = (DerivableAggregateType) obj;
    if (otherObj.derivesFrom.equals(derivesFrom) == false)
    {
      return false;
    }
    return true;
  }
  
  
}
