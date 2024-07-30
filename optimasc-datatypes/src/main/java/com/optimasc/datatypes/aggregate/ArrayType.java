package com.optimasc.datatypes.aggregate;

import java.text.ParseException;
import java.util.Arrays;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Convertable;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.PackedFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents a sequence of elements of the same
 *  datatype that can be referred to by an integer based index. Arrays
 *  have lower and upper bounds and can be multi-dimensional.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li></code>array</code> ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 *
 * @author Carl Eric Codere
 *
 */
public class ArrayType extends Datatype implements ConstructedSimple, PackedFacet, Convertable
{
  
  
  /** Represents the bounds of an array. */
  public static class Dimension
  {
    public int lowBound;
    public int highBound;
    
    public Dimension(int lowerBound, int higherBound)
    {
      this.lowBound = lowerBound;
      this.highBound = higherBound;
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
        if (!(obj instanceof Dimension))
        {
            return false;
        }
      Dimension otherObj = (Dimension)obj;
      if (otherObj.lowBound != lowBound)
      {
        return false;
      }
      if (otherObj.highBound != highBound)
      {
        return false;
      }
      return true;
    }
    
    
  }
  
  protected Dimension ranks[];
  
  private TypeReference dataType;
  /** Indicates if data alignment for underlying platform are ignored, and data is byte aligned. */
  protected boolean packed;

  public ArrayType()
  {
    super(false);
  }

  /** Array definition of base type and specified
   *  dimensions. If the <code>ranks</code> array
   *  contains <code>null</code> entries, it indicates
   *  an array with unknown number of elements (A
   *  dynamic array). 
   * 
   * @param ranks The bounds of each dimensions,
   *   but see note above.
   * @param type The data type of the array elements.
   */
  public ArrayType(Dimension[] ranks, TypeReference type)
  {
    super(false);
    this.dataType = type;
    this.ranks = ranks;
  }

    public Dimension[] getRanks()
    {
        return ranks;
    }
    
    
    public void setRanks(Dimension[] ranks)
    {
        this.ranks = ranks;
    }
    

  /** Return the number of elements in the array. If the rank is 
   *  is not defined, the value returned is -1. */
  protected int getElements()
  {
    int count = 0;
    if (ranks == null)
    {
      return -1;
    }
    for (int i=0; i < ranks.length; i++)
    {
      count = count + (ranks[i].highBound-ranks[i].lowBound+1);
    }
    return count;
  }
  
  public Class getClassType()
  {
    Class clz = dataType.getClass();
    
    // Primitive datatypes
    if ((clz==long.class) || (clz==Long.class))
    {
      return long[].class;
    } else
    if ((clz==int.class) || (clz==Integer.class))
    {
      return int[].class;
    } else
    if ((clz==short.class) || (clz==Short.class))
    {
        return short[].class;
    } else
    if ((clz==byte.class) || (clz==Byte.class))
    {
          return byte[].class;
    } else
    if ((clz==double.class) || (clz==Double.class))
      {
          return double[].class;
      }
    else
      if ((clz==float.class) || (clz==Float.class))
      {
          return float[].class;
      }
    else
    if ((clz==boolean.class) || (clz==Boolean.class))
    {
        return boolean[].class;
    } else
    if ((clz==char.class) || (Character.class.isAssignableFrom(Character.class)))
    {
        return char[].class;
    } else
    {
        return Object[].class;        
    }
  }


    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

/** The objects are equal if they actualy have
    the same indexes and have the same datatype.
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
  if (!(obj instanceof ArrayType))
  {
    return false;
  }
  ArrayType arrType = (ArrayType)obj;
  if (Arrays.equals(arrType.ranks,ranks)==false)
     return false;
  return super.equals(obj);
}

public TypeReference getBaseTypeReference()
{
  return dataType;
}

public void setBaseTypeReference(TypeReference value)
{
  dataType = value;
}

public boolean isPacked()
{
  return packed;
}

public void setPacked(boolean packed)
{
  this.packed = packed;
}

/** {@inheritDoc}
 * 
 *  <p>The input to this method is expected to be an 
 *  array, and the return value will be the same array after
 *  verifying the constraints associated with this datatype
 *  definition.</p> 
 *  
 * 
 */
public Object toValue(Object value, TypeCheckResult conversionResult)
{
  int length=0;
  
  if (value.getClass().isArray()==true)
  {
    Class clz = value.getClass();
    // Primitive datatypes
    if (clz==long[].class)
    {
      length= ((long[])value).length;
    } else
    if ((clz==int[].class))
    {
      length= ((int[])value).length;
    } else
    if (clz==short[].class)
    {
      length= ((short[])value).length;
    } else
    if (clz==byte[].class)
    {
      length= ((byte[])value).length;
    } else
    if (clz==double[].class)
      {
      length= ((double[])value).length;
      }
    else
      if (clz==float[].class)
      {
      length= ((float[])value).length;
      }
    else
    if (clz==boolean[].class)
    {
      length= ((boolean[])value).length;
    } else
      if (clz==char[].class)
      {
      length= ((char[])value).length;
    } else
    {
        length =  ((Object[])value).length;        
    }
  } else
  {
    throw new IllegalArgumentException(
        "Invalid object type - should be an array");
  }
  
  int elements = getElements();
  if ((elements != -1) && (elements!=length))
  {
    conversionResult.error = new DatatypeException(DatatypeException.ERROR_BOUNDS_RANGE,
        "Length must be between, " + elements
        +", got "+length);
    return null;
  }
 conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
 return null;
}


}
