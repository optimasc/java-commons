package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.TypeUtilities;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**  Datatype that represents an approximation of a real number represented as floating point value. 
 *   The value is specified as a scaled values with a base 2 radix, hence 2^-(scale).
 *  
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>real</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>float</code> and <code>double</code> XMLSchema built-in datatype</li>
 *   <li><code>FLOAT</code>, <code>REAL</code> or <code>DOUBLE PRECISION</code> in SQL2003</li>
 *   
 *  </ul>
 *  
 * <p>Internally, values of this type are represented as {@link Double}.</p>
 *  
 *
 * @author Carl Eric Codere
 */
public class RealType extends AbstractNumberType
{
    /** Creates a real type value with the specified 
     *  precision and scale.
     * 
     * @param scale The number of decimal digits.
     */
    public RealType(int scale)
    {
        super(scale);
    }
    
    protected RealType(int scale, double minValue, double maxValue)
    {
        super(scale);
    }
    
    
    /** Creates a real type value which is equal to 
     *  a double precision numeric value.
     * 
     */
    public RealType()
    {
        super(53);
    }
    
    public Class getClassType()
    {
      return Double.class;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    
    /** Compares this RealType to the specified object. 
     *  The result is true if and only if the argument is not null 
     *  and is a RealType object that has the same constraints 
     *  (minInclusive, maxInclusive,scale) as this object
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
        RealType realType;
        if (!(obj instanceof RealType))
        {
            return false;
        }
        realType = (RealType) obj;
        return super.equals(obj);
    }
    
    protected Object toValueNumber(Number ordinalValue, TypeCheckResult conversionResult)
    {
      BigDecimal returnValue = (BigDecimal) super.toValueNumber(ordinalValue, conversionResult);
      if (returnValue == null)
      {
        return null;
      }
      return new Double(returnValue.doubleValue());
    }

    public Object toValue(long ordinalValue, TypeCheckResult conversionResult)
    {
      Object result = super.toValue(ordinalValue, conversionResult);
      if (result == null)
      {
        return null;
      }
      return new Double(ordinalValue);
    }

    public String getGPDName()
    {
      return "real";
    }  
    

}
