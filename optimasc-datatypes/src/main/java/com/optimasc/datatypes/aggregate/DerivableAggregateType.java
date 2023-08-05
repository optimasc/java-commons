package com.optimasc.datatypes.aggregate;

import omg.org.astm.type.NamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an object oriented aggregate type. This
 *  is an abstract class representing aggregate types
 *  that may be derived from others. 
 * 
 * @author carl
 *
 */
public abstract class DerivableAggregateType extends AggregateType
{
  /** Parent class of this class, this value can be null */
  protected NamedTypeReference derivesFrom;

  public DerivableAggregateType(NamedTypeReference parent)
  {
    super(Datatype.OTHER);
    this.derivesFrom = parent;
  }
  
  public DerivableAggregateType(NamedTypeReference parent, MemberObject[] members)
  {
    super(Datatype.OTHER,members);
    this.derivesFrom = parent;
  }
  
  
  public DerivableAggregateType()
  {
    super(Datatype.OTHER);
    this.derivesFrom = null;
    
  }
  
  
  public NamedTypeReference getDerivesFrom()
  {
      return derivesFrom;
  }
  
  public void setDerivesFrom(NamedTypeReference parent)
  {
      this.derivesFrom = parent;
  }

  
  
}
