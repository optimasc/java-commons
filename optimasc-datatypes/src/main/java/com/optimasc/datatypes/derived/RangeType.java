package com.optimasc.datatypes.derived;

import com.optimasc.datatypes.BoundedRangeFacet;
import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;


/**
 * This datatype represents ranges of ordered values.
 * 
 * This is equivalent to the following datatypes:
 * <ul>
 * <li>range ISO/IEC 11404 General purpose datatype</li>
 * </ul>
 * 
 * <p>As specified in ISO/IEC 11404, the range type
 * only supports ordered values. Internally these 
 * ordered values must be represented in a way or 
 * another as an integral value.</p> 
 * 
 * @author Carl Eric Codère
 */
public class RangeType extends Type implements BoundedRangeFacet, ConstructedSimple
{
  // The minimum index value.
  private long lowBound;
     
  // The maximum index value
  private long highBound;
  
  private Type dataType;
  
  
  public RangeType(String name, String comment, long minBound, long maxBound, Type type, int flags)
  {
    super(false);
    if (type.isOrdered()==false)
    {
      throw new IllegalArgumentException("Only ordered values can be used for range types");
    }
    this.dataType = type;
    this.highBound = maxBound;
    this.lowBound = minBound;
    
  }
  
  public Type getBaseType()
  {
    return dataType;
  }

  public void setBaseType(Type value)
  {
    dataType = value;

  }

  public Class getClassType()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    // TODO Auto-generated method stub
    return null;
  }

  public void setMinInclusive(long value)
  {
    // TODO Auto-generated method stub
    
  }

  public void setMaxInclusive(long value)
  {
    // TODO Auto-generated method stub
    
  }

  public long getMinInclusive()
  {
    return lowBound;
  }

  public long getMaxInclusive()
  {
    return highBound;
  }

}
