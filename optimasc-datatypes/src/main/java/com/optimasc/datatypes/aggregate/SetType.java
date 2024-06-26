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
import com.optimasc.datatypes.defined.LatinCharType;
import com.optimasc.datatypes.defined.UnsignedByteType;
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
 *     <li>{@link UnsignedByteType}</li>
 *     <li>{@link EnumType} that is within range</li>
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
  public static final SetType DEFAULT_INSTANCE = new SetType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  
  protected TypeReference baseType;
  
  public SetType(TypeReference baseType)
  {
    super(Datatype.OTHER,false);
    setBaseTypeReference(baseType);
  }
  
  /** Constructs a set type based on 
   *  an unsigned byte.  
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
    if ((value instanceof UnsignedByteType) || (value instanceof LatinCharType) ||
        (value instanceof BooleanType))
    {
      return true;
    }
    // If the value of the enumeration is within the allowed range then its ok, otherwise its false.
    if ((value instanceof EnumType))
    {
      EnumType enumType = (EnumType) value;
      // Must fit witihn an usigned byte.
      if ((enumType.getMaxInclusive().longValue() <= 255) && (enumType.getMinInclusive().longValue()>=0))
      {
        return true;
      }
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
    {
      throw new IllegalArgumentException("Set base types must be of UnsignedByteType, LatinCharacter, Enumeration with ordinal values that fit in a byte, or Range of these types");
    }
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
