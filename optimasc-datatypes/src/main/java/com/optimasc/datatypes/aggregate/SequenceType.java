package com.optimasc.datatypes.aggregate;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

public abstract class SequenceType extends Datatype implements LengthFacet, ConstructedSimple
{
  protected TypeReference elementType;
  
  /** The minimum and maximum length in bytes. */
  protected LengthHelper lengthHelper;
  
  
  protected SequenceType(int type, TypeReference baseType)
  {
    super(type, false);
    elementType = baseType;
    lengthHelper = new LengthHelper();
  }
  
  protected SequenceType(int type, int minLength, int maxLength, TypeReference baseType)
  {
    super(type, false);
    elementType = baseType;
    lengthHelper.setLength(minLength,maxLength);
  }
  
  
  public TypeReference getBaseTypeReference()
  {
    return elementType;
  }

  public void setBaseTypeReference(TypeReference value)
  {
    elementType = value;
  }

  public int getMinLength()
  {
    return lengthHelper.getMinLength();
  }

  public int getMaxLength()
  {
    return lengthHelper.getMaxLength();
  }

  public boolean isRestriction(Type value)
  {
    return lengthHelper.isRestriction(value);
  }

  public boolean isBounded()
  {
    return lengthHelper.isBounded();
  }
  
  

}
