package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.BoundedRangeFacet;
import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Abstract Datatype that represents a finite number of distinguished
 * values having an intrinsic order. 
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 *   <li><code>ENUMERATED</code> ASN.1 datatype</li>
 * <li><code>enumerated</code> ISO/IEC 11404 General purpose datatype</li>
 * </ul>
 * 
 * <p>
 * The choices which represents the different elements of the enumeration can be
 * of different types. To have them correctly ordered and represented by an
 * ordinal values, several options exist.
 * </p>
 * 
 * <ul>
 * <li>choices can be of {@link #EnumerationElement} where the
 * <code>value</code> field should represent an integral value which will
 * represent an index. The lowest value and highest values will provide range of
 * the enumeration.</li>
 * <li>choices can be of {@link Number} where the lowest value shall represent
 * the minimum range, and the higest value shall represent the highest range.</li>
 * <li>Otherwise for other Object types, the lowest and highest range value
 * representation shall be the minimum index and highest index of the choices
 * array.</li>
 * </ul>
 * 
 * <p>Internally, values of this type are represented as <code>Object[]</code>.</p>
 * 
 * @author Carl Eric Codère
 */
public abstract class EnumType extends PrimitiveType implements EnumerationFacet, BoundedRangeFacet
{
  /** Basic element type */
  protected Object[] choices;

  /** Possible representation of an enumeration element. */
  public class EnumerationElement
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

    public boolean equals(Object obj)
    {
      if (obj==null)
        return false;
      if (obj == this)
      {
          return true;
      }
      if ((obj instanceof EnumerationElement)==false)
      {
        return false;
      }
      EnumerationElement otherObj = (EnumerationElement) obj;
      if (name.equals(otherObj.name)==false)
        return false;
      if (value.equals(otherObj.value)==false)
        return false;
      return true;
    }
    
    

  }

  public EnumType()
  {
    super(Datatype.OTHER, true);
  }

  public Class getClassType()
  {
    return Object[].class;
  }

  public Object[] getChoices()
  {
    return choices;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    if (validateChoice(value) == false)
    {
      DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
          "Value is not in choice list.");
    }
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return null;
  }

  /** Set possible choices and assigns it the
   *  BaseType depending on the underlying
   *  type.
   * 
   */
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

  public void setMinInclusive(long value)
  {
    throw new IllegalArgumentException("Unsupported operation.");
  }

  public void setMaxInclusive(long value)
  {
    throw new IllegalArgumentException("Unsupported operation.");
  }

  public long getMinInclusive()
  {
    if ((choices != null) && (choices.length > 0) && (choices[0] != null))
    {
      long minValue = Long.MAX_VALUE;
      Object item = choices[0];
      if (item instanceof Number)
      {
        for (int i = 0; i < choices.length; i++)
        {
          long numberedItemValue = ((Number) choices[i]).longValue();
          if (numberedItemValue < minValue)
          {
            minValue = numberedItemValue;
          }
        }
        return minValue;
      }
      else if (item instanceof EnumerationElement)
      {
        for (int i = 0; i < choices.length; i++)
        {
          EnumerationElement element = ((EnumerationElement) choices[i]);
          long numberedItemValue = Long.parseLong(element.getValue());
          if (numberedItemValue < minValue)
          {
            minValue = numberedItemValue;
          }
        }
        return minValue;
      }
      else
      {
        // Return minimum index of choices array 
        return 0;
      }
    }
    return 0;
  }

  public long getMaxInclusive()
  {
    if ((choices != null) && (choices.length > 0) && (choices[0] != null))
    {
      long maxValue = Long.MIN_VALUE;
      Object item = choices[0];
      if (item instanceof Number)
      {
        for (int i = 0; i < choices.length; i++)
        {
          long numberedItemValue = ((Number) choices[i]).longValue();
          if (numberedItemValue > maxValue)
          {
            maxValue = numberedItemValue;
          }
        }
        return maxValue;
      }
      else if (item instanceof EnumerationElement)
      {
        for (int i = 0; i < choices.length; i++)
        {
          EnumerationElement element = ((EnumerationElement) choices[i]);
          long numberedItemValue = Long.parseLong(element.getValue());
          if (numberedItemValue > maxValue)
          {
            maxValue = numberedItemValue;
          }
        }
        return maxValue;
      }
      else
      {
        // Return maximum index of choices array 
        return choices.length-1;
      }
    }
    else
    {
      return 0;
    }
  }

  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }
    if (!(obj instanceof EnumType))
    {
        return false;
    }
    return super.equals(obj);
  }

  /**
   * Return the enumeration index value. If Enumeration contains Objects of type
   * string, then the original / rank of the enumeration is the index starting
   * from 0 of the enumeration. If the Enumeration contains Objects of type
   * EnumerationElement, then the value which should be convertable to Integer
   * will be used as ordinal value.
   * 
   * @param symbol
   *          The enumeration symbol name.
   * @return -1 upon error, otherwise the ordinal value of the enumeration
   *         literal represented by this enum symbol.
   */
  public int getEnumOrdinalValue(String symbol)
  {
    Object[] choices = getChoices();
    if (choices[0] instanceof String)
    {
      for (int i = 0; i < choices.length; i++)
      {
        if (choices[i].equals(symbol))
        {
          return i;
        }
      }
      return -1;
    }
    if (choices[0] instanceof EnumerationElement)
    {
      for (int i = 0; i < choices.length; i++)
      {
        EnumerationElement enumElement = (EnumerationElement) choices[i];
        if (enumElement.getName().equals(symbol))
        {
          return Integer.parseInt(enumElement.getValue());
        }
      }
      return -1;
    }
    return -1;
  }

}
