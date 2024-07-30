package com.optimasc.datatypes.generated;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Datatype that represents a namespace identifier. 
 */ 
public class NameSpaceType extends Type
{
  public static final NameSpaceType DEFAULT_INSTANCE = new NameSpaceType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  public NameSpaceType()
  {
    super(false);
  }

  public Class getClassType()
  {
    return null;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
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
      if (!(obj instanceof NameSpaceType))
      {
          return false;
      }
      return true;
  }


}
