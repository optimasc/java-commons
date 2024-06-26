package com.optimasc.datatypes.primitives;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

public class VoidType extends Type
{
  public static final VoidType DEFAULT_INSTANCE = new VoidType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);

  
  public VoidType()
  {
    super(false);
  }
  

  public Class getClassType()
  {
    return null;
  }
  
  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
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
      if (!(obj instanceof VoidType))
      {
          return false;
      }
      return true;
  }

}
