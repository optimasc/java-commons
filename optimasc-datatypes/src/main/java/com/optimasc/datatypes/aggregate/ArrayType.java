package com.optimasc.datatypes.aggregate;

import java.text.ParseException;
import java.util.Arrays;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** This datatype represents the Array type that contains
 *  several data of the same type.
 *  
 * @author Carl Eric Codere
 *
 */
public class ArrayType extends Datatype implements ConstructedSimple
{
  /** Represents the bounds of an array. */
  public static class Dimension
  {
    public int lowBound;
    public int highBound;
  }
  
  protected Dimension ranks[];
  
  private Type dataType;

  public ArrayType()
  {
    super(Datatype.ARRAY,false);
  }

  public ArrayType(String name, String comment, Dimension[] ranks, Type type, int flags)
  {
    super(name, comment, Datatype.ARRAY,false, flags);
    this.dataType = type;
    this.ranks = ranks;
  }

    public void setDataType(Type dataType)
    {
        this.dataType = dataType;
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
  
  public int getSize()
  {
    return -1;
//    return (getElements()*dataType.getSize());
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
   ArrayType arrType;

   if (!(obj instanceof ArrayType))
      return false;
   arrType = (ArrayType)obj;
   if (Arrays.equals(arrType.ranks,ranks)==false)
      return false;
   if (this.dataType.equals(arrType.getBaseType())==false)
      return false;
   return true;
}

public boolean isSubset(Object obj)
{
  return false;
}


public boolean isSuperset(Object obj)
{
  return false;
}

public Type getBaseType()
{
  return dataType;
}

public void setBaseType(Type value)
{
  dataType = value;
}

public Object getObjectType()
{
  // TODO Auto-generated method stub
  return null;
}


public Object parse(String value) throws ParseException
{
  throw new UnsupportedOperationException("Parse method is not implemented.");
}


}
