package com.optimasc.datatypes.aggregate;

import java.text.ParseException;
import java.util.Arrays;

import omg.org.astm.type.TypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.PackedFacet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** This datatype represents the Array type that contains
 *  several data of the same type.
 *  
 * @author Carl Eric Codere
 *
 */
public class ArrayType extends Datatype implements ConstructedSimple, PackedFacet
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
  }
  
  protected Dimension ranks[];
  
  private TypeReference dataType;
  /** Indicates if data alignment for underlying platform are ignored, and data is byte aligned. */
  protected boolean packed;

  public ArrayType()
  {
    super(Datatype.ARRAY,false);
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
    super(Datatype.ARRAY,false);
    this.dataType = type;
    this.ranks = ranks;
  }

    public Dimension[] getRanks()
    {
        return ranks;
    }

  /** Return the number of elements in the array */
  protected int getElements()
  {
    int count = 0;
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

  public void validate(Object valueArray) throws IllegalArgumentException,
      DatatypeException
  {
    int length=0;
    
    if (valueArray.getClass().isArray()==true)
    {
      Class clz = valueArray.getClass();
      // Primitive datatypes
      if (clz==long[].class)
      {
        length= ((long[])valueArray).length;
      } else
      if ((clz==int[].class))
      {
        length= ((int[])valueArray).length;
      } else
      if (clz==short[].class)
      {
        length= ((short[])valueArray).length;
      } else
      if (clz==byte[].class)
      {
        length= ((byte[])valueArray).length;
      } else
      if (clz==double[].class)
        {
        length= ((double[])valueArray).length;
        }
      else
        if (clz==float[].class)
        {
        length= ((float[])valueArray).length;
        }
      else
      if (clz==boolean[].class)
      {
        length= ((boolean[])valueArray).length;
      } else
        if (clz==char[].class)
        {
        length= ((char[])valueArray).length;
      } else
      {
          length =  ((Object[])valueArray).length;        
      }
    } else
    {
      throw new IllegalArgumentException(
          "Invalid object type - should be an array");
    }
    if (getElements()!=length)
    {
      DatatypeException.throwIt(DatatypeException.ERROR_ILLEGAL_VALUE,
          "The number of actual array elements is not expected value, got "+length+", expected "+getElements()+".");
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

public boolean isSubset(Object obj)
{
  return false;
}


public boolean isSuperset(Object obj)
{
  return false;
}

public TypeReference getBaseTypeReference()
{
  return dataType;
}

public void setBaseTypeReference(TypeReference value)
{
  dataType = value;
}

public Object getObjectType()
{
  // TODO Auto-generated method stub
  return null;
}

public boolean isPacked()
{
  return packed;
}

public void setPacked(boolean packed)
{
  this.packed = packed;
}


}
