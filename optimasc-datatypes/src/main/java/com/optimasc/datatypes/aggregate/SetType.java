package com.optimasc.datatypes.aggregate;

import java.util.Arrays;
import java.util.BitSet;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.ConstructedSimple;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.aggregate.ArrayType.Dimension;
import com.optimasc.datatypes.derived.ByteType;
import com.optimasc.datatypes.derived.LatinCharType;
import com.optimasc.datatypes.derived.RangeType;
import com.optimasc.datatypes.derived.UnsignedByteType;
import com.optimasc.datatypes.primitives.BooleanType;
import com.optimasc.datatypes.primitives.CharacterType;
import com.optimasc.datatypes.primitives.EnumType;
import com.optimasc.datatypes.primitives.IntegralType;
import com.optimasc.datatypes.primitives.PrimitiveType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.Duration;

/** * Datatype that represents a set. The baseType must be an ordered value, and
 *    only allowed types are:
 *    
 *    <ul>
 *     <li>{@link BooleanType}</li>
 *     <li>{@link LatinCharType}</li>
 *     <li>{@link ByteType}</li>
 *     <li>{@link UnsignedByteType}</li>
 *     <li>A range of the above types</li>
 *    </ul>
 *    
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
  
  public static final SetType DEFAULT_INSTANCE = new SetType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  
  protected TypeReference baseType;
  
  public SetType(TypeReference baseType)
  {
    super(Datatype.OTHER,false);
    setBaseTypeReference(baseType);
  }
  
  /** Constructs a set type based on 
   *  a byte.  
   */
  public SetType()
  {
    super(Datatype.OTHER,false);
    setBaseTypeReference(UnsignedByteType.DEFAULT_TYPE_REFERENCE);
  }
  
  
  public TypeReference getBaseTypeReference()
  {
    return baseType;
  }
  
  protected boolean isAllowedType(Type value)
  {
    if ((value instanceof ByteType) || (value instanceof UnsignedByteType) || (value instanceof LatinCharType) || (value instanceof EnumType) ||
        (value instanceof BooleanType))
    {
      return true;
    }
    return false;
  }

  public void setBaseTypeReference(TypeReference value)
  {
    if (isAllowedType(value.getType()))
    {
      baseType = (TypeReference)value;
      return;
    } else
    if (value.getType() instanceof RangeType)
    {
      Type rangeElementType = ((RangeType)value.getType()).getBaseTypeReference().getType();
      if (isAllowedType(rangeElementType))
      {
        baseType = (TypeReference)value;
        return;
      }
    }
    else
    {
      throw new IllegalArgumentException("Set base types must be of ByteType, UnsignedByteType, LatinCharacter, Enumeration or Range of these types");
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

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
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
    if (!(obj instanceof SetType))
    {
      return false;
    }
    return super.equals(obj);
  }


}
