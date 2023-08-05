package com.optimasc.datatypes.generated;

import java.text.ParseException;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.primitives.EnumType;

/** Represents a choice between different elements all of
 *  the same datatype. It is required to have the value
 *  between one of the chosen elements.
 * 
 * @author Carl Eric Codere
 *
 */
public class ClosedChoiceType extends EnumType implements ConstructedSimple, Parseable
{
  protected TypeReference elementTypeReference;
  protected Type elementType;
  
  public ClosedChoiceType()
  {
    super();
  }

  @Override
  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
   if (validateChoice(value) == false)
   {
     DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE, "Value is not in choice list.");
   }
  }


  @Override
  public boolean validateChoice(Object value)
  {
    for (int i = 0; i < choices.length; i++)
    {
      if (value.equals(choices[i]))
      {
        return true;
      }
    }
   return false;
  }

  public Object getObjectType()
  {
    return choices;
  }


  @Override
  public Object parse(String value) throws ParseException
  {
    try
    {
      validate(value);
      if (elementType instanceof Parseable)
      {
        return ((Parseable)elementType).parse(value);
      }
      throw new UnsupportedOperationException("Unsupporting of element Type");
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Error validating string.",0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Error validating string.",0);
    }
    
  }

  @Override
  public TypeReference getBaseTypeReference()
  {
    return elementTypeReference;
  }

  @Override
  public void setBaseTypeReference(TypeReference value)
  {
    this.elementTypeReference = value;
    this.elementType = value.getType();
  }
  

}
