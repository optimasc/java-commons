package com.optimasc.datatypes.visitor;

import com.optimasc.datatypes.aggregate.BagType;
import com.optimasc.datatypes.aggregate.ListType;
import com.optimasc.datatypes.aggregate.SequenceListType;
import com.optimasc.datatypes.aggregate.SequenceType;
import com.optimasc.datatypes.derived.NormalizedStringType;
import com.optimasc.datatypes.derived.TokenType;
import com.optimasc.datatypes.derived.UnicodeCharType;
import com.optimasc.datatypes.generated.StringTypeEx;
import com.optimasc.datatypes.primitives.URIType;

public interface TypeVisitorEx extends TypeVisitor
{
  /********************** Derived types *************************/
  
  public Object visit(UnicodeCharType n, Object arg);
  
  public Object visit(TokenType n, Object arg);
  
  public Object visit(NormalizedStringType n, Object arg);
  
  /********************** Aggregate types *************************/
  
  public Object visit(BagType n, Object arg);
  
  public Object visit(SequenceListType n, Object arg);
  
  /********************** Generated types *************************/

  public Object visit(URIType n, Object arg);
  
  public Object visit(StringTypeEx n, Object arg);
  
}
