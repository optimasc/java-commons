package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.util.Arrays;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.BoundedFacet;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.NumberRangeFacet;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.NumberComparator;

/**
 * Abstract Datatype that represents a finite number of distinguished values
 * having an intrinsic order.
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li><code>ENUMERATED</code> ASN.1 datatype</li>
 * <li><code>enumerated</code> ISO/IEC 11404 General purpose datatype</li>
 * </ul>
 * 
 * <p>
 * The choices which represents the different elements of the enumeration must
 * be of the same type. To have them correctly ordered and represented by an
 * ordinal values, several options exist.
 * </p>
 * 
 * <ul>
 * <li>choices can be of {@link #EnumerationElement} where the
 * <code>value</code> field should represent an integer value which will
 * represent an index. The lowest value and highest values will provide range of
 * the enumeration.</li>
 * <li>Otherwise for other Object types, the lowest and highest range value
 * representation shall be the minimum index and highest index of the choices
 * array.</li>
 * </ul>
 * 
 * <p>
 * Internally, values of this type are represented as <code>Object[]</code> and
 * the maximum supported ordinal value is up to {@link Integer#MAX_VALUE}
 * </p>
 * 
 * @author Carl Eric Codère
 */
public class EnumeratedType extends PrimitiveType implements EnumerationFacet,
    BoundedFacet, OrderedFacet
{
  private static EnumeratedType defaultTypeInstance;
  private static TypeReference defaultTypeReference;
  
  
  protected static final Object DEFAULT_CHOICES[] = new Object[0]; 

  /** Basic element type */
  protected Object[] choices;
  /** The maximum allowed number of elements in the enumerated type. */
  protected int maxChoices;

  /** The maximum number of enumeration elements, from C90 standard. */
  protected static final int DEFAULT_MAX_CHOICES = 127;

  /** Possible representation of an enumeration element. */
  public static class EnumerationElement
  {
    protected String name;
    protected int value;

    public EnumerationElement(String token, int value)
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

    public int getValue()
    {
      return value;
    }

    public void setValue(int value)
    {
      this.value = value;
    }

    public boolean equals(Object obj)
    {
      if (obj == null)
        return false;
      if (obj == this)
      {
        return true;
      }
      if ((obj instanceof EnumerationElement) == false)
      {
        return false;
      }
      EnumerationElement otherObj = (EnumerationElement) obj;
      if (name.equals(otherObj.name) == false)
        return false;
      if (value != otherObj.value)
        return false;
      return true;
    }

    public String toString()
    {
      return name;
    }

    
  }

  public EnumeratedType()
  {
    super(true);
    this.choices = DEFAULT_CHOICES;
    this.maxChoices = DEFAULT_MAX_CHOICES;
  }

  public EnumeratedType(Object[] choices)
  {
    super(true);
    this.maxChoices = DEFAULT_MAX_CHOICES;
    setChoices(choices);
  }

  public EnumeratedType(int maxChoices)
  {
    this();
    if (maxChoices < 0)
    {
      throw new IllegalArgumentException("Maximum number of allowed "
          + "specification must be a non-negative number.");
    }
    this.maxChoices = maxChoices;
  }

  public Class getClassType()
  {
    return Object[].class;
  }

  public Object[] getChoices()
  {
    return choices;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  /**
   * Set possible choices. Setting this value to <code>null</code> 
   * indicates that this enumerated type has no enumeration values (e.g no choices
   * 
   * @throws IllegalArgumentException
   *           Thrown if number of choices is beyond supported by this
   *           specification, as set by {@link #maxChoices}.
   * 
   * 
   */
  public void setChoices(Object[] choices)
  {
    if (choices == null)
    {
      this.choices = DEFAULT_CHOICES;
      return;
    }
    if (choices.length > maxChoices)
    {
      throw new IllegalArgumentException(
          "Trying to set more elements in the enumeration than allowed."
              + " Maximum allowed number of enumerated values is "
              + Integer.toString(maxChoices) + ".");
    }
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

  /**
   * {@inheritDoc}
   * 
   * <p>
   * For enumerated types, if the allowed choices have not been set, this method
   * shall return <code>null</code>.
   * 
   */
  public Number getMinInclusive()
  {
    if ((choices != null) && (choices.length > 0) && (choices[0] != null))
    {
      long minValue = Long.MAX_VALUE;
      Object item = choices[0];
      if (item instanceof EnumerationElement)
      {
        for (int i = 0; i < choices.length; i++)
        {
          EnumerationElement element = ((EnumerationElement) choices[i]);
          long numberedItemValue = element.getValue();
          if (numberedItemValue < minValue)
          {
            minValue = numberedItemValue;
          }
        }
        return new Long(minValue);
      }
      else
      {
        // Return minimum index of choices array 
        return new Long(0);
      }
    }
    return null;
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * For enumerated types, if the allowed choices have not been set, this method
   * shall return <code>null</code>.
   * 
   */
  public Number getMaxInclusive()
  {
    if ((choices != null) && (choices.length > 0) && (choices[0] != null))
    {
      long maxValue = Long.MIN_VALUE;
      Object item = choices[0];
      if (item instanceof EnumerationElement)
      {
        for (int i = 0; i < choices.length; i++)
        {
          EnumerationElement element = ((EnumerationElement) choices[i]);
          long numberedItemValue = element.getValue();
          if (numberedItemValue > maxValue)
          {
            maxValue = numberedItemValue;
          }
        }
        return new Long(maxValue);
      }
      else
      {
        // Return maximum index of choices array 
        return new Long(choices.length - 1);
      }
    }
    else
    {
      return null;
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
    if (!(obj instanceof EnumeratedType))
    {
      return false;
    }
    EnumeratedType otherEnum = (EnumeratedType) obj;
    if (otherEnum.maxChoices != maxChoices)
      return false;
    if (Arrays.equals(otherEnum.choices, choices) == false)
    {
      return false;
    }
    return true;
  }

  /**
   * Return the enumeration index value. If the Enumeration contains Objects of
   * type {@link EnumerationElement}, then the value which should be convertable
   * to an <code>int</code> will be used as ordinal value. Otherwise, each
   * object's {@link Object#toString()} in the enumeration element to find the
   * symbol, and the value returned will be the index matching this symbol.
   * 
   * @param symbol
   *          The enumeration symbol name.
   * @return -1 upon error or if the symbol is not found in this enumeration,
   *         otherwise the ordinal value of the enumeration literal represented
   *         by this enumerated symbol.
   */
  public int getEnumOrdinalValue(String symbol)
  {
    Object[] choices = getChoices();
    if (choices == null)
      return -1;
    if (choices.length == 0)
      return -1;

    if (choices[0] instanceof EnumerationElement)
    {
      for (int i = 0; i < choices.length; i++)
      {
        EnumerationElement enumElement = (EnumerationElement) choices[i];
        if (enumElement.getName().equals(symbol))
        {
          return enumElement.getValue();
        }
      }
      return -1;
    }
    else
    {
      for (int i = 0; i < choices.length; i++)
      {
        if (choices[i].toString().equals(symbol))
        {
          return i;
        }
      }
      return -1;
    }
  }

  protected int getEnumIndex(String symbol)
  {
    Object[] choices = getChoices();
    if (choices[0] instanceof EnumerationElement)
    {
      for (int i = 0; i < choices.length; i++)
      {
        EnumerationElement enumElement = (EnumerationElement) choices[i];
        if (enumElement.getName().equals(symbol))
        {
          return i;
        }
      }
      return -1;
    }
    else
    {
      for (int i = 0; i < choices.length; i++)
      {
        if (choices[i].toString().equals(symbol))
        {
          return i;
        }
      }
      return -1;
    }
  }

  public boolean isRestrictionOf(Datatype value)
  {
    if ((value instanceof EnumeratedType) == false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '"
          + value.getClass().getName() + "'.");
    }
    NumberRangeFacet rangeType;
    rangeType = (NumberRangeFacet) value;
    
    BigDecimal minOtherValue = NumberComparator.toBigDecimal(rangeType.getMinInclusive());
    BigDecimal maxOtherValue = NumberComparator.toBigDecimal(rangeType.getMaxInclusive());

    // No bounds at all - no restrictions in both ranges
    if ((rangeType.isBounded()==false) && (isBounded()==false))
    {
      return false;
    }

    // This value has one bound, and other no bound.
    if ((rangeType.isBounded()==false) && (isBounded()==true))
    {
      return true;
    }
    
    // Bounded in this object and the one specified
    
    BigDecimal rangeValue = null;
    BigDecimal otherRangeValue = null;
    
    if ((getMinInclusive() != null) && (getMaxInclusive()!=null))
    {
      rangeValue = NumberComparator.toBigDecimal(getMaxInclusive()).subtract(NumberComparator.toBigDecimal(getMinInclusive()));
    }
    if ((minOtherValue != null) && (maxOtherValue!=null))
    {
      otherRangeValue = maxOtherValue.subtract(minOtherValue);
    }
        
    if (rangeValue.compareTo(otherRangeValue)<0)
      return true;
    
    
    return false;
  }

  /**
   * {@inheritDoc}
   * 
   * <p>
   * Enumerated types are always bounded, hence this method will always return
   * <code>true</code>.
   * </p>
   * 
   */
  public boolean isBounded()
  {
    return true;
  }

  /**
   * Return the object associated with the numeric value. If the choices are of
   * type <code>EnumerationElement</code>, the value is searched using that's
   * value fields, otherwise it represents the index in the enumerated choices.
   * 
   * @param value
   *          [in] The ordinal value to search for.
   * @return The associated object, or <code>null</code> if not found or 
   *   out of range.
   * @throws IllegalArgumentException If the value is a negative number.
   */
  public Object getChoice(int value)
  {
    if (value < 0)
    {
      throw new IllegalArgumentException("Enumerated ordinal value must be a natural number.");
    }
    if ((choices != null) && (choices.length > 0) && (choices[0] != null))
    {
      Object[] choices = getChoices();
      if (choices[0] instanceof EnumerationElement)
      {
        for (int i = 0; i < choices.length; i++)
        {
          EnumerationElement enumElement = (EnumerationElement) choices[i];
          if (enumElement.getValue() == value)
          {
            return choices[i];
          }
        }
        return null;
      }
      else
      {
        if (value > (choices.length-1))
        {
          return null;
        }
        return choices[value];
      }
    }
    else
    {
      return null;
    }
  }

  /** {@inheritDoc}
   * 
   * <p>
   * If the value is a <code>Number</code> it is converted to the numeric value
   * associated with the choice, if any are present.
   * </p>
   * 
   * <p>
   * If the value is another Object type, it is converted to a string, and it
   * searched in the enumeration list.
   * </p>
   * 
   * 
   * 
   */
  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
    // If this is a numeric value, search for the value
    if (value instanceof Number)
    {
      return toValue(((Number) value).intValue(), conversionResult);
    }
    else
    {
      // Search for the choice index
      int index = getEnumIndex(value.toString());
      // If found, return the enumeration choice
      if (index != -1)
        return choices[index];
    }
    conversionResult.error = new DatatypeException(
        DatatypeException.ERROR_DATA_TYPE_MISMATCH, "Enumerated element '"
            + value.toString() + "' not found.");
    return null;
  }

  public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
    Object obj = getChoice((int) ordinalValue);
    if (obj == null)
    {
      conversionResult.error = new DatatypeException(
          DatatypeException.ERROR_DATA_TYPE_MISMATCH, "Enumerated element with value "
              + "'" + Integer.toString((int) ordinalValue) + "' not found.");
      return null;
    }
    return obj;
  }

  public boolean validateRange(long value)
  {
    if (isBounded() == false)
    {
      return true;
    }
    BigDecimal v = BigDecimal.valueOf(value);
    return validateRange(v);
  }

  public boolean validateRange(Number value)
  {
    if (choices == null)
    {
      return false;
    }
    if (choices.length == 0)
    {
      return false;
    }

    // Compare the values.
    // value < minInclusive
    if (NumberComparator.INSTANCE.compare(value,getMinInclusive()) == -1)
    {
      return false;
    }
    // value >= maxInclusive 
    if (NumberComparator.INSTANCE.compare(value,getMaxInclusive()) == 1)
    {
      return false;
    }
    return true;
  }
  
  public static TypeReference getInstance()
  {
    if (defaultTypeInstance == null)
    {
      defaultTypeInstance = new EnumeratedType();
      defaultTypeReference = new NamedTypeReference("enumerated()" ,defaultTypeInstance);
    }
    return defaultTypeReference; 
  }


}
