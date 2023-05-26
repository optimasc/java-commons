package com.optimasc.datatypes.aggregate;

import java.util.Arrays;
import java.util.BitSet;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.aggregate.ArrayType.Dimension;
import com.optimasc.datatypes.derived.RangeType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.primitives.EnumType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.PrimitiveType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.Duration;

/** * Datatype that represents a set. The baseType must be an ordered value.
*
*  This is equivalent to the following datatypes:
*  <ul>
*   <li>
*   <li><code>set</code> ISO/IEC 11404 General purpose datatype</li>
*  </ul>
*  
* <p>Internally, values of this type are represented as {@link BitSet} objects.</p>
* 
* 
* @author Carl Eric Codere
*
*/
public class SetType extends Datatype implements ConstructedSimple
{
  protected static final BitSet INSTANCE = new BitSet();
  
  protected PrimitiveType baseType;
  
  public SetType(PrimitiveType baseType)
  {
    super(Datatype.OTHER,false);
    setBaseType(baseType);
  }
  
  public Type getBaseType()
  {
    return baseType;
  }
  
  protected boolean isAllowedType(Type value)
  {
    if ((value instanceof IntegralType) || (value instanceof CharacterType) || (value instanceof EnumType) ||
        (value instanceof BooleanType))
    {
      return true;
    }
    return false;
  }

  public void setBaseType(Type value)
  {
    if (isAllowedType(value))
    {
      baseType = (PrimitiveType)value;
      return;
    } else
    if (value instanceof RangeType)
    {
      Type rangeElementType = ((RangeType)value).getBaseType();
      if (isAllowedType(rangeElementType))
      {
        baseType = (PrimitiveType)value;
        return;
      }
    }
    else
    {
      throw new IllegalArgumentException("Set base types must be of IntegralType, Character, Enumeration or Range of these types");
    }
  }

  
  public Object getObjectType()
  {
    return INSTANCE;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    checkClass(value);
  }

  public Class getClassType()
  {
    return BitSet.class;
  }

  @Override
  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
  }

  @Override
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
    if (!(obj instanceof SetType))
    {
      return false;
    }
    return super.equals(obj);
  }


}
