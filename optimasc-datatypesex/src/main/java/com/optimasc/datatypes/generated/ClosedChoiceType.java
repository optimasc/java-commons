package com.optimasc.datatypes.generated;

import java.text.ParseException;
import java.util.regex.Pattern;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.EnumeratedType;

/** Represents a choice between different elements all of
 *  the same datatype. It is required to have the value
 *  between one of the chosen elements.
 * 
 * @author Carl Eric Codere
 *
 */
public class ClosedChoiceType extends EnumeratedType implements ConstructedSimple
{
  protected Datatype elementType;
  protected Object[] choices;

  public ClosedChoiceType()
  {
    super();
  }


  @Override
  public Class getClassType()
  {
    return elementType.getClass();
  }
  
  
  public Object[] getChoices()
  {
    return choices;
  }

  @Override
  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
   if (validateChoice(value) == false)
   {
     DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE, "Value is not in choice list.");
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
      return elementType.parse(value);
    } catch (IllegalArgumentException e)
    {
      throw new ParseException("Error validating string.",0);
    } catch (DatatypeException e)
    {
      throw new ParseException("Error validating string.",0);
    }
    
  }
  

}
