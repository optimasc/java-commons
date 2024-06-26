/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Datatype that represents a boolean value which represents a logical
 *  true of false state.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>BOOLEAN</code> ASN.1 datatype</li>
 *   <li></code>boolean</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>boolean</code> XMLSchema built-in datatype</li>
 *   <li><code>BOOLEAN</code> in SQL2003</li>
 *   <li><code>BOOLEAN</code> IETF RFC 6350 vCard Data type</li>
 *  </ul>
 *
 * <p>Internally, values of this type are represented as {@link Boolean} objects. </p>
 *  
 *
 * @author Carl Eric Codère
 */
public class BooleanType extends PrimitiveType implements OrderedFacet
{
  /** Type instance which is fully compatible with XMLSchema and  ISO/IEC 11404,
   *  where the type is <em>not</em> considered ordered.
   * 
   */
  public static final BooleanType DEFAULT_INSTANCE = new BooleanType(false);
  /** Type reference instance which is fully compatible with XMLSchema and  ISO/IEC 11404,
   *  where the type is <em>not</em> considered ordered.
   * 
   */
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  /** Boolean type definition, where the boolean value is considered an 
   *  ordered value.
   * 
   */
  public static final BooleanType DEFAULT_ORDERED_INSTANCE = new BooleanType(true);
  /** Boolean type reference instance, where the boolean value is considered an 
   *  ordered value.
   * 
   */
  public static final UnnamedTypeReference DEFAULT_ORDERED_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_ORDERED_INSTANCE);
  
    /** Constructs a new boolean type definition  
     * 
     * @param ordered [in] Indicates if this datatype
     *   shall be considered an ordered value.
     */
    protected BooleanType(boolean ordered)
    {
        super(Datatype.BOOLEAN,ordered);
    }
    
    
    /** Constructs a new boolean type definition that 
     *  has no ordering.  
     * 
     */
    public BooleanType()
    {
        super(Datatype.BOOLEAN,false);
    }
    
    
    /** Validates if the Boolean object is compatible with the defined datatype.
     *
     * @param value The object to check must be a Boolean object.
     * @throws IllegalArgumentException Throws this exception it is an invalid Object  type.
     */
    public void validate(java.lang.Object value) throws IllegalArgumentException, DatatypeException
    {
      checkClass(value);
    }

    public Class getClassType()
    {
      return Boolean.class;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    /** Compares this BooleanType to the specified object. 
     *  The result is true if and only if the argument is not null 
     *  and is a BoleanType object as this object.
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
        if (!(obj instanceof BooleanType))
        {
            return false;
        }
        return true;
    }


    /** {@inheritDoc}
     * 
     *  <p>This specific implementation will return <code>null</code> if 
     *  the boolean type is not defined as being ordered. If the boolean
     *  type is defined as ordered,any non-value will return {@link Boolean#TRUE}, 
     *  otherwise  {@link Boolean#FALSE} will be returned.
     *  </p>
     * 
     */
    public Object toValue(Number ordinalValue, TypeCheckResult conversionResult)
    {
      conversionResult.reset();
      
      // Throw and exception when value is not ordered.
      if (ordered ==false)
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"This type is not ordered, hence unsupported value of class,  '"+ordinalValue.getClass().getName()+"'.");
        return null;
      }
      
      if (ordinalValue instanceof BigDecimal)
      {
        BigDecimal bigDecimal = (BigDecimal)ordinalValue;  
        if (bigDecimal.compareTo(TypeUtilities.DECIMAL_ZERO)==0)
        {
          return Boolean.FALSE;
        }
        return Boolean.TRUE;
      } else
      if (ordinalValue instanceof BigInteger)
      {
        BigInteger bigInteger = (BigInteger)ordinalValue;  
        if (bigInteger.compareTo(BigInteger.ZERO)==0)
        {
          return Boolean.FALSE;
        }
        return Boolean.TRUE;
      } else
      {
        long value = ordinalValue.longValue();
        if (value == 0)
        {
          return Boolean.FALSE;
          
        }
        return Boolean.TRUE;
      }
    }
    
    
    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      conversionResult.reset();
      
      // Throw and exception when value is not ordered.
      if (ordered ==false)
      {
        conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,
            "This type is not ordered, hence int value is not allowed.");
        return null;
      }
      
      if (ordinalValue == 0)
      {
        return Boolean.FALSE;
      }
      return Boolean.TRUE;
    }
    


    /** {@inheritDoc}
     * 
     *  <p>This implementation supports as input either a 
     *  <code>Boolean</code> or a <code>Number</code> object
     *  as data value. In the case of a number, any zero
     *  value is considered false, and other values will
     *  return as <code>Boolean.TRUE</code>. The value
     *  returned is of type <code>Boolean</code>.</p> 
     * 
     */
    public Object toValue(Object value, TypeCheckResult conversionResult)
    {
      if (Boolean.class.isInstance(value))
      {
        conversionResult.reset();
        return value;
      }
      if (Number.class.isInstance(value))
      {
        return toValue((Number)value,conversionResult);
      }
      conversionResult.error = new DatatypeException(DatatypeException.ERROR_DATA_TYPE_MISMATCH,"Unsupported value of class '"+value.getClass().getName()+"'.");
      return null;
    }

}
