package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an enumeration. An enumeration is always 
 *  represented by an ordered integral value or 
 *  by a string representation value.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>enumerated ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 *  
 *  To follow standard programming languages, you should
 *  use the EnumerationElement with the name of the symbol
 *  and the integral value.
 *  
 *  
 * @author Carl Eric Codère
 */
public abstract class EnumType extends PrimitiveType implements ConstructedSimple, EnumerationFacet
{
  /** Basic element type */
  protected Type elementType;
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

  public EnumType()
  {
    super(Datatype.OTHER,true);
  }

  public int getSize()
  {
    return 0;
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
     DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE, "Value is not in choice list.");
   }
  }

  public Object accept(TypeVisitor v, Object arg)
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

  public Type getBaseType()
  {
    return elementType;
  }

  public void setBaseType(Type value)
  {
    elementType = value;
  }
  
  

}
