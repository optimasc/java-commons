package com.optimasc.datatypes.aggregate;

import java.text.ParseException;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** This datatype represents the Array type that contains
 *  several data of the same type.
 *  
 * @author Carl Eric Codere
 *
 */
public class ArrayType extends Datatype implements ConstructedSimple
{
  // The minimum index value.
  private int minIndex;
     
  // The maximum index value
  private int maxIndex;
  
  private Datatype dataType;

  public ArrayType()
  {
    super(Datatype.ARRAY);
  }

  public ArrayType(String name, String comment, int minIndex, int maxIndex, Datatype type, int flags)
  {
    super(name, comment, Datatype.ARRAY,flags);
    this.dataType = type;
    this.maxIndex = maxIndex;
    this.minIndex = minIndex;
  }

    public void setDataType(Datatype dataType)
    {
        this.dataType = dataType;
    }

    public int getMaxIndex()
    {
        return maxIndex;
    }

    public void setMaxIndex(int maxIndex)
    {
        this.maxIndex = maxIndex;
    }

    public int getMinIndex()
    {
        return minIndex;
    }

    public void setMinIndex(int minIndex)
    {
        this.minIndex = minIndex;
    }

  /** Return the number of elements in the array */
  protected int getElements()
  {
    return (maxIndex - minIndex)+1;
  }
  
  public int getSize()
  {
    return (getElements()*dataType.getSize());
  }

  public Class getClassType()
  {
    return dataType.getClass();
  }

  public void validate(Object integerValue) throws IllegalArgumentException,
      DatatypeException
  {
  }

    public Object accept(DatatypeVisitor v, Object arg)
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
   if (this.minIndex != arrType.minIndex)
      return false;
   if (this.maxIndex != arrType.maxIndex)
      return false;
   if (this.dataType.equals(arrType.getElementType())==false)
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

public Datatype getElementType()
{
  return dataType;
}

public void setElementType(Datatype value)
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
