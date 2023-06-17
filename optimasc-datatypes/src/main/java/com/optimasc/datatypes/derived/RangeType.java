package com.optimasc.datatypes.derived;

import omg.org.astm.type.TypeReference;

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
 * another as an Integral value. </p>
 * 
 * <p>The current implementation supports the following types their
 *  derives types, and their corresponding values:
 *   <ul>
 *     <li><@link BooleanType}</li>
 *     <li><@link CharacterType}</li>
 *     <li><@link IntegralType}</li>
 *     <li><@link EnumType}</li>
 *   </ul>
 * </p>
 * 
 * @author Carl Eric Codère
 */
public class RangeType extends Type implements BoundedRangeFacet, ConstructedSimple
{
  // The numeric value of the lowBound
  protected long lowBound;
     
  // The numeric value of the upper band
  protected long highBound;
  
  // The value of the lower bound. The value here is of type.
  protected Object lowValue;
     
  // The value of the upper bound. The value here is of type.
  protected Object highValue;
  
  
  protected TypeReference dataType;
  
  
  public RangeType(Object minBound, Object maxBound, TypeReference typeRef)
  {
    super(true);
    Type type = typeRef.getType();
    if (type.isOrdered()==false)
    {
      throw new IllegalArgumentException("Only ordered values can be used for range types");
    }
    if (type instanceof RangeType)
    {
      throw new IllegalArgumentException(RangeType.class.getName()+" of type "+RangeType.class.getName()+" is not allowed.");
    }
    this.dataType = typeRef;
    // Check that the classes are allowed.
    Class classValue = type.getClassType();
    if ((classValue.isAssignableFrom(maxBound.getClass())) && (classValue.isAssignableFrom(minBound.getClass())))
    {
        if ((maxBound instanceof Number) && (minBound instanceof Number))
        {
          this.highBound = ((Number)maxBound).longValue();
          this.lowBound = ((Number)minBound).longValue();
        } else
        if ((maxBound instanceof Character) && (minBound instanceof Character))
        {
          this.highBound = (long)((Character)maxBound).charValue();
          this.lowBound = (long)((Character)minBound).charValue();
        } else
        if ((maxBound instanceof Boolean) && (minBound instanceof Boolean))
        {
          this.highBound = ((Boolean)maxBound).booleanValue()?1:0;
          this.lowBound = ((Boolean)minBound).booleanValue()?1:0;
        } else
        {
          throw new IllegalArgumentException("Unsupported bounds values");
        }
    } else
    {
      throw new IllegalArgumentException("Exepcted bounds values of class "+classValue.getName());
    }
    this.highValue = maxBound;
    this.lowValue = minBound;
  }
  
  public TypeReference getBaseType()
  {
    return dataType;
  }

  public void setBaseType(TypeReference value)
  {
    dataType = value;

  }

  public Class getClassType()
  {
    return dataType.getType().getClassType();
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
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

  public Object getLowValue()
  {
    return lowValue;
  }

  public Object getHighValue()
  {
    return highValue;
  }
  
  

}
