/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.NotFoundException;
import com.optimasc.datatypes.visitor.DatatypeVisitor;
import java.util.Vector;

public abstract class EnumeratedType extends PrimitiveType implements EnumerationFacet
{
  protected Datatype elementType;
  protected Object[] choices;
  
  
  /** Possible representation of an enumeration element. */
  public class  EnumerationElement
  {
    protected String name;
    protected String value;
    
    public EnumerationElement(String token, String value)
    {
      this.name = token;
      this.value = value;
    }

    public String getName()
    {
      return name;
    }

    public void setName(String name)
    {
      this.name = name;
    }

    public String getValue()
    {
      return value;
    }

    public void setValue(String value)
    {
      this.value = value;
    }
    
    
  }

  public EnumeratedType()
  {
    super(Datatype.OTHER);
  }

  public int getSize()
  {
    return elementType.getSize();
  }

  public Class getClassType()
  {
    return elementType.getClass();
  }
  
  
  public Object[] getChoices()
  {
    return choices;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
   if (validateChoice(value) == false)
   {
     DatatypeException.throwIt(DatatypeException.ILLEGAL_VALUE, "Value is not in choice list.");
   }
  }

  public Object accept(DatatypeVisitor v, Object arg)
  {
    return null;
  }

  public void setChoices(Object[] choices)
  {
    this.choices = choices;
  }

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

  public Datatype getElementType()
  {
    return elementType;
  }

  public void setElementType(Datatype value)
  {
    elementType = value;
  }
  
  

}
