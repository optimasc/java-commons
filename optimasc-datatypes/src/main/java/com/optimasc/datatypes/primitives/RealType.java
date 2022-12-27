package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.RealRangeFacet;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents a numerical representation of a floating point
 *  value.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>REAL ASN.1 datatype</li>
 *   <li>real ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 *
 * @author Carl Eric Codere
 */
public class RealType extends PrimitiveType implements RealRangeFacet, DatatypeConverter
{
    protected static final Float FLOAT_INSTANCE = new Float(0);  
    protected static final Double DOUBLE_INSTANCE = new Double(0);  
    
    private double minInclusive;
    private double maxInclusive;

    public RealType()
    {
        super(Datatype.REAL);
    }
    

    public void validate(Object integerValue) throws IllegalArgumentException,
            DatatypeException
    {
    }

    public void validate(double doubleValue) throws IllegalArgumentException, DatatypeException
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

    public int getSize()
    {
        return getStorageSize(maxInclusive, minInclusive);
    }

    public Class getClassType()
    {
      switch (getSize())
      {
        case 4:
            return Float.class;
        case 8:
            return Double.class;
      }
      return null;
    }

    public Object accept(DatatypeVisitor v, Object arg)
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
      switch (getSize())
      {
        case 4:
            return FLOAT_INSTANCE;
        case 8:
            return DOUBLE_INSTANCE;
      }
      return null;
    }


    public Object parse(String value) throws ParseException
    {
      Object objectValue;
      /* Represented as a fractional value! */
      if (value.indexOf("/")!=-1)
      {
        String numerator = value.substring(0,value.indexOf("/"));
        String denominator = value.substring(value.indexOf("/")+1);
        int intNumerator = Integer.parseInt(numerator);
        int intDenominator = Integer.parseInt(denominator);
        if (getSize()==8)
        {
          objectValue = new Double(intNumerator*1.0 / intDenominator*1.0);
        } else
        {
          objectValue = new Float(intNumerator*1.0 / intDenominator*1.0);
        }
      } else
      {
      if (getSize()==8)
      {
        objectValue = Double.valueOf(value);
      } else
      {
        objectValue = Float.valueOf(value);
      }
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
    

}
