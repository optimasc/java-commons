package com.optimasc.datatypes.manager;

public interface ValueNode extends Node
{
   public Object getDefaultValue();
   public boolean isNillable();
   public Object getValue();
   public void setValue();
}
