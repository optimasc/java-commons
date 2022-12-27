package com.optimasc.datatypes.visitor;

import com.optimasc.datatypes.aggregate.ListType;

public interface DatatypeVisitorEx extends DatatypeVisitor
{
  /********************** Aggregate types *************************/
  
  public Object visit(ListType n, Object arg);

}
