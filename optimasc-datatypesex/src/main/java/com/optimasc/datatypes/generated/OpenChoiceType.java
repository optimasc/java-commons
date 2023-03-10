package com.optimasc.datatypes.generated;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.EnumerationHelper;

/** Represents a choice between different elements all of
 *  the same datatype. It is possible to have another
 *  value outside the proposed choices, but it must be
 *  of the value must of the same datatype as the others.
 * 
 * @author Carl Eric Codere
 *
 */
public class OpenChoiceType extends UnionType implements EnumerationFacet, ConstructedSimple
{
  protected Datatype elementType;
  protected EnumerationHelper enumHelper;
  
  public OpenChoiceType()
  {
    super();
  }

  @Override
  public Datatype getElementType()
  {
    return elementType;
  }

  @Override
  public void setElementType(Datatype value)
  {
    elementType = value; 
  }

  @Override
  public Object[] getChoices()
  {
    return enumHelper.getChoices();
  }

  @Override
  public void setChoices(Object[] choices)
  {
    int i;
    for (i = 0; i < choices.length; i++)
    {
      if (elementType.getClass().isInstance(choices[i]))
      {
        throw new IllegalArgumentException("Choice values are not of a valid type");
      }
    }
    enumHelper.setChoices(choices);
  }

  @Override
  public boolean validateChoice(Object value)
  {
    return false;
  }

}
