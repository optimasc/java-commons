package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.PrecisionFacet;
import com.optimasc.datatypes.RealRangeFacet;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**  Datatype that represents a decimal or approximate floating point value. For
 *   performance reasons, derived real types should be
 *   used instead of this one.
 *  
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>REAL</code> ASN.1 datatype</li>
 *   <li><code>real</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>decimal</code> XMLSchema built-in datatype</li>
 *   <li><code>NUMERIC</code> or <code>DECIMAL</code> in SQL2003</li>
 *   
 *  </ul>
 *  
 * <p>Internally, values of this type are represented as {@link BigDecimal}.</p>
 *  
 *
 * @author Carl Eric Codere
 */
public class RealType extends PrimitiveType implements RealRangeFacet, Parseable, DatatypeConverter, PrecisionFacet
{
  public static final RealType DEFAULT_INSTANCE = new RealType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  
    protected static final Float FLOAT_INSTANCE = new Float(0);  
    protected static final Double DOUBLE_INSTANCE = new Double(0);  
    protected static final BigDecimal ZERO = new BigDecimal(0);
    
    protected double minInclusive;
    protected double maxInclusive;
    protected int precision;
    protected int scale;

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
        this.precision = precision;
        this.scale = scale;
    }
    
    /** Creates a real type value with a default
     *  precision of 12 digits and a scale of
     *  4 digits.
     * 
     */
    public RealType()
    {
        super(Datatype.REAL,true);
        this.precision = 12;
        this.scale = 4;
    }
    
    

    public void validate(Object value) throws IllegalArgumentException,
            DatatypeException
    {
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
     *  (minInclusive, maxIncluseive) as this object
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
        if (this.minInclusive != realType.minInclusive)
        {
            return false;
        }
        if (this.maxInclusive != realType.maxInclusive)
        {
            return false;
        }
        return true;
    }


    public double getMinInclusive()
    {
      return minInclusive;
    }


    public void setMinInclusive(double minInclusive)
    {
      this.minInclusive = minInclusive;
    }


    public double getMaxInclusive()
    {
      return maxInclusive;
    }


    public void setMaxInclusive(double maxInclusive)
    {
      this.maxInclusive = maxInclusive;
    }


    public Object getObjectType()
    {
      return ZERO;
    }


    public Object parse(String value) throws ParseException
    {
      Number objectValue;
      /* Represented as a fractional value! */
      if (value.indexOf("/")!=-1)
      {
        String numerator = value.substring(0,value.indexOf("/"));
        String denominator = value.substring(value.indexOf("/")+1);
        int intNumerator = Integer.parseInt(numerator);
        int intDenominator = Integer.parseInt(denominator);
        objectValue = new BigDecimal(intNumerator*1.0 / intDenominator*1.0);
      } else
      {
        objectValue = new BigDecimal(value);
      }
      try
      {
        validate(objectValue);
        return objectValue;
      } catch (IllegalArgumentException e)
      {
        throw new ParseException("Cannot parse floating point value.",0);
      } catch (DatatypeException e)
      {
        throw new ParseException("Cannot parse floating point value.",0);
      }
    }


    public int getPrecision()
    {
      return 0;
    }


    public int getScale()
    {
      return 0;
    }
    
    

}
