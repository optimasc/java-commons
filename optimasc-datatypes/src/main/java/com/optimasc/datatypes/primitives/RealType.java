package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.DecimalEnumerationFacet;
import com.optimasc.datatypes.DecimalEnumerationHelper;
import com.optimasc.datatypes.DecimalRangeHelper;
import com.optimasc.datatypes.OrderedFacet;
import com.optimasc.datatypes.PrecisionFacet;
import com.optimasc.datatypes.DecimalRangeFacet;
import com.optimasc.datatypes.SubSet;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**  Datatype that represents an approximation of a real number represented as  floating point value. 
 *   For performance reasons, derived real types should be used instead of this one.
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
 * <p>Internally, values of this type are represented as {@link BigDecimal}.</p>
 *  
 *
 * @author Carl Eric Codere
 */
public class RealType extends AbstractNumberType
{
  public static final RealType DEFAULT_INSTANCE = new RealType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
    /** Creates a real type value with the specified 
     *  precision and scale.
     * 
     * @param precision The total precision of the
     *  value in digits.
     * @param scale The number of decimal digits.
     */
    public RealType(int precision, int scale)
    {
        super(Datatype.REAL,true);
    }
    
    protected RealType(int precision, int scale, BigDecimal minValue, BigDecimal maxValue)
    {
        super(Datatype.REAL,minValue,maxValue,true);
    }
    
    
    /** Creates a real type value with no limit on precision
     *  and value.
     * 
     */
    public RealType()
    {
        super(Datatype.REAL,true);
    }
    
    /** Depending on the range of the values determine the storage size of the integer. */
    public static int getStorageSize(double minInclusive, double maxInclusive)
    {
      if ((maxInclusive <= Float.MAX_VALUE) && (minInclusive >= Float.MIN_VALUE))
      {
          return 4;
      }
      if ((maxInclusive <= Double.MAX_VALUE) && (minInclusive >= Double.MIN_VALUE))
      {
          return 2;
      }
      return 0;
    }

    public Class getClassType()
    {
      return BigDecimal.class;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    
    /** Compares this RealType to the specified object. 
     *  The result is true if and only if the argument is not null 
     *  and is a RealType object that has the same constraints 
     *  (minInclusive, maxIncluseive,scale,precision) as this object
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

}
