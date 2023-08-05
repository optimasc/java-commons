package com.optimasc.datatypes.aggregate;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.TypeVisitor;

public abstract class SequenceType extends Datatype implements ConstructedSimple
{
  protected TypeReference elementType;
  
  public SequenceType(int type, boolean ordered, TypeReference baseType)
  {
    super(type, ordered);
    elementType = baseType;
  }
  

  
  @Override
  public TypeReference getBaseTypeReference()
  {
    return elementType;
  }

  @Override
  public void setBaseTypeReference(TypeReference value)
  {
    throw new IllegalArgumentException("Unsupported operation");
  }


}
