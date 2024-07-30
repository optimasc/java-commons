package com.optimasc.datatypes.aggregate;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Convertable;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.EnumerationFacet;
import com.optimasc.datatypes.LengthFacet;
import com.optimasc.datatypes.LengthHelper;
import com.optimasc.datatypes.Restriction;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.defined.BinaryType;
import com.optimasc.datatypes.primitives.TimeType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.OctetSequence;

/**
 * Represents an ordered sequences of values from the element base datatype. It
 * includes a minLength and maxLength attribute as a facet indicating the
 * allowed length of the data. The default implementation also supports
 * enumeration/selecting values, supporting the following potential types:
 * 
 * <ul>
 *  <li>Arrays of primitive types</li>
 *  <li>Arrays of objects</li>
 *  <li><code>CharSequence</code></li>
 *  <li><code>OctetSequence</code></li>
 * </ul>
 * 
 * <p>It is to note that there is no strict checking between the class type
 * returned and the actual base type reference java classes, but the following is assumed:</p>
 * 
 * <p>Arrays of primitive types are associated with their equivalent Object instances,
 * for example, an array of int (<code>int[]</code>) is assumed to be associated with
 * <code>IntegralType</code>.</p>   
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li><code>SEQUENCE OF</code> ASN.1 datatype</li>
 * <li><code>sequence</code> ISO/IEC 11404 aggregate datatype</li>
 * <li><code>list</code> XSD XMLSchema built-in datatype</li>
 * <li><code>rdf:Seq</code> XMP Specification (2012) ordered array datatype</li>
 * </ul>
 * 
 * @author Carl Eric Codere
 *
 */
public class SequenceType extends Datatype implements LengthFacet,Restriction,
    ConstructedSimple, EnumerationFacet
{
  protected TypeReference elementType;

  /** The minimum and maximum length in bytes. */
  protected LengthHelper lengthHelper;
  /** Select list */
  protected Object enumeration[];
  /** Expected class type. */
  protected Class classType;

  /** Creates a sequence with a minimum length of 0 elements
   *  and unbounded upper length with no selection constraint. 
   * 
   * @param baseType [in] The base type of the sequence.
   * @param resultType [in] The expected class type of 
   *  the values space for this type.
   */
  public SequenceType(TypeReference baseType, Class resultType)
  {
    super(false);
    elementType = baseType;
    lengthHelper = new LengthHelper(0);
    classType = resultType;
    if (checkClassType(baseType.getType().getClassType(),resultType)==false)
    {
      throw new IllegalArgumentException("Unsupported sequence type.");
    }
  }
  
  /** Creates a sequence with specified minimum and maximum
   *  elements and no selection constraint. 
   * 
   * @param minLength [in] Expected minimum number of
   *  elements in this sequence.
   * @param maxLength [in] Expected maximum number of
   *  elements in this sequence.
   * @param baseType [in] The base type of the sequence.
   * @param resultType [in] The expected class type of 
   *  the values space for this type.
   */
  public SequenceType(int minLength, int maxLength, TypeReference baseType, Class resultType)
  {
    super(false);
    if (checkClassType(baseType.getType().getClassType(),resultType)==false)
    {
      throw new IllegalArgumentException("Unsupported sequence type.");
    }
    elementType = baseType;
    lengthHelper = new LengthHelper();
    lengthHelper.setLength(minLength, maxLength);
    classType = resultType;
  }

  /** Creates a sequence that must have values equal
   *  to one the specified selection values. The
   *  minimum length and maximum length is automatically
   *  calculated from the selection values.
   *
   * @param choices [in] A list of allowed values, where
   *  the choices elements must be of type <code>resultType</code>.
   * @param baseType [in] The base type of the sequence.
   * @param resultType [in] The expected class type of 
   *  the values space for this type.
   * @throws IllegalArgumentException If the actual value 
   *  of choices elements is not of the correct java object
   *  type or if they do not meet the constraints of <code>baseType</code>.
   */
  public SequenceType(Object choices[], TypeReference baseType, Class resultType)
  {
    super(false);
    Class elementClass = getClassType();
    classType = resultType;
    elementType = baseType;
    lengthHelper = new LengthHelper();
    if (checkClassType(baseType.getType().getClassType(),resultType)==false)
    {
      throw new IllegalArgumentException("Unsupported sequence type.");
    }

    //=--- Verify the validity of the choices ---=// 
    TypeCheckResult conversionResult = new TypeCheckResult();
    // Verify validity of each choice element - they should be of a specific type.
    for (int i = 0; i < choices.length; i++)
    {
      Object choiceValue = choices[i];
      if (validateValues(choiceValue,conversionResult)==false)
      {
        IllegalArgumentException exc = new IllegalArgumentException();
        exc.initCause(conversionResult.error);
        throw exc;
      }
    }
    
    enumeration = new Object[choices.length];
    System.arraycopy(choices, 0, enumeration, 0, choices.length);
    lengthHelper.setLength(getMinLength(choices, elementClass),
        getMaxLength(choices, elementClass));
  }

  public TypeReference getBaseTypeReference()
  {
    return elementType;
  }

  public int getMinLength()
  {
    return lengthHelper.getMinLength();
  }

  public int getMaxLength()
  {
    return lengthHelper.getMaxLength();
  }

  public boolean isRestrictionOf(Datatype value)
  {
    if ((value instanceof SequenceType)==false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '"+value.getClass().getName()+"'.");
    }
    
    SequenceType otherSequenceType = (SequenceType) value;
    // Different sequence types
    if (classType!=otherSequenceType.classType)
    {
      throw new IllegalArgumentException("Expecting classType '"+classType.getName()+"'.");
    }
    
    
    boolean restriction = lengthHelper.isRestrictionOf(otherSequenceType);
    if (restriction == true)
    {
      return true;
    }
    
    Object[] choices = enumeration;
    Object[] otherChoices = otherSequenceType.enumeration;
    if ((choices!=null) && (otherChoices==null))
    {
      return true;
    }
    
    if ((choices==null) && (otherChoices!=null))
    {
      return false;
    }
    
    
    if ((otherChoices!=null) && (otherChoices.length < choices.length))
    {
      return true;
    }
    
    if (getBaseTypeReference().getType() instanceof Restriction)
    {
      Restriction thisTypeR = (Restriction) getBaseTypeReference().getType();
      return thisTypeR.isRestrictionOf((Datatype) otherSequenceType.getBaseTypeReference().getType()); 
    }
    return false;
  }

  public boolean isBounded()
  {
    return lengthHelper.isBounded();
  }

  /**
   * Return the minmimum length of the arrays in this enumeration.
   * 
   * @param choices
   *          [in] An array of primitive arrays.
   * @param expectedObject
   *          [in] The expected class of each choice.
   * @return The minimum array length of the choices.
   */
  private int getMinLength(Object choices[], Class expectedObject)
  {
    int minLength = Integer.MAX_VALUE;
    for (int i = 0; i < choices.length; i++)
    {
      int arrayLength = getLength(choices[i]);
      if (arrayLength < minLength)
        minLength = arrayLength;
    }
    return minLength;

  }

  /**
   * Return the maximum length of byte arrays in this enumeration.
   * 
   * @param choices
   *          [in] An array of primitive arrays.
   * @param expectedObject
   *          [in] The expected class of each choice.
   * @return The maximum array length of the choices.
   */
  private int getMaxLength(Object choices[], Class expectedObject)
  {
    int maxLength = 0;

    for (int i = 0; i < choices.length; i++)
    {
      int arrayLength = getLength(choices[i]);
      if (arrayLength > maxLength)
        maxLength = arrayLength;
    }
    return maxLength;
  }
  
  /**
   * Compares this SequenceType to the specified object. The result is true if and
   * only if the argument is not null and is a Sequence type object that has the
   * same constraints (minLenghth, maxLength, base type, allowed values enumeration) as this object
   * 
   */
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

    SequenceType binType;
    if (!(obj instanceof SequenceType))
    {
      return false;
    }
    binType = (SequenceType) obj;
    if (this.getMinLength() != binType.getMinLength())
    {
      return false;
    }
    if (this.getMaxLength() != binType.getMaxLength())
    {
      return false;
    }
    
    if (this.getBaseTypeReference().equals(binType.getBaseTypeReference())==false)
    {
      return false;
    }
    
    if ((enumeration == null) && (binType.enumeration!=null))
    {
      return false;
    }
    if ((enumeration != null) && (binType.enumeration==null))
    {
      return false;
    }
    
    if ((enumeration == null) && (binType.enumeration==null))
    {
      return true;
    }
    
    if (enumeration.length != binType.enumeration.length)
      return false;
    for (int i=0; i < enumeration.length; i++)
    {
      if (equalValues(enumeration[i],binType.enumeration[i])==false)
        return false;
    }
    return true;
  }
  

  /**
   * {@inheritDoc}
   * 
   */
  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    conversionResult.reset();
    Class expectedClassType = getClassType();
    if (expectedClassType.isInstance(value))
    {
      // If no bounds defined, simply return the value
      if (isBounded() == false)
        return value;

      // We need to do this check first, since choices update the minLength..maxLength values
      if (validateChoice(value) == false)
      {
        conversionResult.error = new DatatypeException(
            DatatypeException.ERROR_DATA_TYPE_MISMATCH,
            "The value is not within the list of allowed value as defined by "
                + "the enumration.");
        return null;
      }
      if (validateLength(value) == false)
      {
        conversionResult.error = new DatatypeException(
            DatatypeException.ERROR_BOUNDS_RANGE, "Length of array must be between, "
                + lengthHelper.toString());
        return null;
      }
      if (validateValues(value,conversionResult) == false)
      {
        return null;
      }
      return value;
    }
    conversionResult.error = new DatatypeException(
        DatatypeException.ERROR_DATA_TYPE_MISMATCH, "Unsupported value of class '"
            + value.getClass().getName() + "'.");
    return null;
  }

  
  /** Method that compares a value with another values. The
   *  values to be compared are values of the type returned
   *  by {@link #getClassType()}. This generic method supports
   *  comparing the following, and may be overriden for 
   *  optimization purposes by derived classes:
   *  
   *  <ul>
   *   <li><code>CharSequence</code></li>
   *   <li><code>boolean[]</code></li>
   *   <li><code>byte[]</code></li>
   *   <li><code>short[]</code></li>
   *   <li><code>int[]</code></li>
   *   <li><code>long[]</code></li>
   *   <li><code>float[]</code></li>
   *   <li><code>double[]</code></li>
   *   <li><code>Object[]</code></li>
   *  </ul>
   * 
   * @param left [in] The value on the left
   * @param right [in] The value on the right
   * @return <code>true</code> if the values 
   *  are equal otherwise false.
   */
  protected static boolean equalValues(Object left, Object right)
  {
    if ((left instanceof CharSequence) && (right instanceof CharSequence))
    {
      CharSequence leftValue = (CharSequence) left;
      CharSequence rightValue = (CharSequence) right;
      
      if (leftValue.length()==rightValue.length())
      {
        boolean found = true;
        for (int j=0; j < leftValue.length(); j++)
        {
          if (leftValue.charAt(j)!=rightValue.charAt(j))
          {
            found = false;
            break;
          }
        }
        if (found==true)
          return true;
      }
      return false;
    }
    
    if ((left instanceof OctetSequence) && (right instanceof OctetSequence))
    {
      OctetSequence leftValue = (OctetSequence) left;
      OctetSequence rightValue = (OctetSequence) right;
      
      if (leftValue.length()==rightValue.length())
      {
        boolean found = true;
        for (int j=0; j < leftValue.length(); j++)
        {
          if (leftValue.octetAt(j)!=rightValue.octetAt(j))
          {
            found = false;
            break;
          }
        }
        if (found==true)
          return true;
      }
      return false;
    }
    
    
    if ((left instanceof byte[]) && (right instanceof byte[]))
    {
      byte[] leftValue = (byte[])left;
      byte[] rightValue = (byte[])right;
      if (Arrays.equals(leftValue,rightValue)==true)
        {
          return true;
        }
      return false;
    }
    
    
    if ((left instanceof boolean[]) && (right instanceof boolean[]))
    {
      boolean[] leftValue = (boolean[])left;
      boolean[] rightValue = (boolean[])right;
      if (Arrays.equals(leftValue,rightValue)==true)
      {
          return true;
      }
      return false;
    }
  
  if ((left instanceof short[]) && (right instanceof short[]))
  {
    short[] leftValue = (short[])left;
    short[] rightValue = (short[])right;
    if (Arrays.equals(leftValue,rightValue)==true)
      {
        return true;
      }
    return false;
  }
  
  if ((left instanceof int[]) && (right instanceof int[]))
  {
    int[] leftValue = (int[])left;
    int[] rightValue = (int[])right;
    if (Arrays.equals(leftValue,rightValue)==true)
      {
        return true;
      }
    return false;
  }
  
  if ((left instanceof long[]) && (right instanceof long[]))
  {
    long[] leftValue = (long[])left;
    long[] rightValue = (long[])right;
    if (Arrays.equals(leftValue,rightValue)==true)
      {
        return true;
      }
    return false;
  }
  
  if ((left instanceof float[]) && (right instanceof float[]))
  {
    float[] leftValue = (float[])left;
    float[] rightValue = (float[])right;
    if (Arrays.equals(leftValue,rightValue)==true)
      {
        return true;
      }
    return false;
  }
  
  if ((left instanceof double[]) && (right instanceof double[]))
  {
    double[] leftValue = (double[])left;
    double[] rightValue = (double[])right;
    if (Arrays.equals(leftValue,rightValue)==true)
      {
        return true;
      }
    return false;
  }
  
  if ((left instanceof char[]) && (right instanceof char[]))
  {
    char[] leftValue = (char[])left;
    char[] rightValue = (char[])right;
    if (Arrays.equals(leftValue,rightValue)==true)
      {
        return true;
      }
    return false;
  }
  
  
  if ((left instanceof Object[]) && right instanceof Object[])
  {
    Object[] leftValue = (Object[])left;
    Object[] rightValue = (Object[])right;
    if (Arrays.equals(leftValue,rightValue)==true)
      {
        return true;
      }
    return false;
  }
  throw new IllegalArgumentException("Unsupported java type");
  }
  
  
  protected boolean validateValues(Object value, TypeCheckResult conversionResult)
  {
    Class elementClass = getClassType();
    Type typeDef = elementType.getType();
    if (elementClass.isInstance(value) == false)
    {
      conversionResult.error = new DatatypeException(
          DatatypeException.ERROR_DATA_TYPE_MISMATCH, "Unsupported value of class '"
              + value.getClass().getName() + "'.");
      return false;
    }
    // If this is an array, verify the validity of each element in the array.
    if (typeDef instanceof Convertable)
    {
      Convertable valueConverter = (Convertable) typeDef;
      if (elementClass.isArray())
      {
        int arrayLength = Array.getLength(value);
        for (int j = 0; j < arrayLength; j++)
        {
          Object elementValue = Array.get(value, j);
          if (valueConverter.toValue(elementValue, conversionResult) == null)
          {
            return false;
          }
          if (conversionResult.error != null)
          {
            return false;
          }
          
        }
        return true;
      } else
      if (value instanceof CharSequence)
      {
        CharSequence charSequence = (CharSequence) value;
        for (int i=0; i < charSequence.length(); i++)
        {
          if (valueConverter.toValue(new Character(charSequence.charAt(i)), conversionResult) == null)
          {
            return false;
          }
        }
        return true;
      } else
      if (value instanceof OctetSequence)
      {
        OctetSequence octetSequence = (OctetSequence) value;
        for (int i=0; i < octetSequence.length(); i++)
        {
          if (valueConverter.toValue(new Integer(octetSequence.octetAt(i)), conversionResult) == null)
          {
            return false;
          }
        }
        return true;
      }
      else
      {
        if (valueConverter.toValue(value, conversionResult) == null)
        {
          return false;
        }
        return true;
      }
    }
    conversionResult.error = new DatatypeException(
        DatatypeException.ERROR_DATA_TYPE_MISMATCH, "Unsupported value of class '"
            + value.getClass().getName() + "'.");
    return false;
  }

  public boolean validateChoice(Object value)
  {
    if (enumeration==null)
        return true;
    for (int i=0; i < enumeration.length; i++)
    {
      if (equalValues(enumeration[i],value)==true)
        return true;
    }
    return false;
  }
  
  /** Return the length of the object. This generic method supports
   *  get the length of the following objects, and may be overriden for 
   *  optimization purposes by derived classes:
   *  
   *  <ul>
   *   <li><code>CharSequence</code></li>
   *   <li><code>OctetSequence</code></li>
   *   <li><code>boolean[]</code></li>
   *   <li><code>byte[]</code></li>
   *   <li><code>short[]</code></li>
   *   <li><code>int[]</code></li>
   *   <li><code>long[]</code></li>
   *   <li><code>float[]</code></li>
   *   <li><code>double[]</code></li>
   *   <li><code>Object[]</code></li>
   *  </ul>
   *
   * 
   * @param value [in] The value to get the length from.
   * @return
   */
  protected int getLength(Object value)
  {
    if (value instanceof CharSequence)
    {
      return ((CharSequence) value).length();
    }
    else
    if (value instanceof OctetSequence)
    {
      return ((OctetSequence) value).length();
    }
    else if (value.getClass().isArray())
    {
      return Array.getLength(value);
    }
    throw new IllegalArgumentException(
        "Unsupported or unknown class type to verify the length.");
  }

  /**
   * Method that verifies that the object value that should have the concept of
   * size of length fits within the constraints.
   * 
   * <p>
   * The default implementation supports all objects which are arrays, as well
   * as <code>CharSequence</code> type. An instance implementation may implement
   * this default implementation for performance purposes.
   * </p>
   * 
   * 
   * @param value
   * @return
   */
  protected boolean validateLength(Object value)
  {
    int length = getLength(value);
    if (lengthHelper.validateLength(length) == false)
    {
      return false;
    }
    return true;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  public Class getClassType()
  {
    return classType;
  }
  
  
  
  protected static boolean checkClassType(Class baseType, Class expectedClass)
  {
    return true;
/*    // Special case for characters
    if ((baseType==char.class) || (baseType==Character.class) || (baseType==int.class))
    {
      if (expectedClass==char[].class)
      {
        return true;
      }
      if (expectedClass==int[].class)
      {
        return true;
      }
      if (expectedClass.isAssignableFrom(CharSequence.class))
      {
        return true;
      }
      return false;  
    }
    
    // Special case for octet values
    if ((baseType==byte.class) || (baseType==Byte.class) || (baseType==int.class))
    {
      if (expectedClass==byte[].class)
      {
        return true;
      }
      if (expectedClass.isAssignableFrom(OctetSequence.class))
      {
        return true;
      }
      return false;  
    }
    
    
    
    // If the baseType is a primitive, then the expectedClass
    // should be an array of that primitive.
    if (baseType.isPrimitive())
    {
      Class componentType = expectedClass.getComponentType();
      if (componentType == baseType)
      {
        return true;
      }
      return false;
    }
    
    // We expect array
    if (expectedClass.isArray()==false)
    {
      return false;
    }
    
    Class componentType = expectedClass.getComponentType();
    // Special cases for primitive type wrappers.
    for (int i=0; i < WRAPPER_CLASSES.length; i++)
    {
      if (baseType==WRAPPER_CLASSES[i])
      {
        if ((componentType==PRIMITIVE_CLASSES[i]) || (componentType.isAssignableFrom(WRAPPER_CLASSES[i])))
        {
          return true;
        }
        return false;
      }
    }
    // Must be equal object types
    if (componentType.isAssignableFrom(baseType))
    {
      return true;
    }
    return false;*/
  }

  public Object[] getChoices()
  {
    return enumeration;
  }
  
  
}
