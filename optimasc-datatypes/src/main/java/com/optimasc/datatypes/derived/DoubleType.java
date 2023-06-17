package com.optimasc.datatypes.derived;

import java.math.BigDecimal;
import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an IEEE 754 64-bit floating point value */
public class DoubleType extends RealType
{
    protected static final Double DOUBLE_INSTANCE = new Double(0);
  
  
    public DoubleType()
    {
      super(15,-1);
      setMinInclusive(Double.MIN_VALUE);
      setMaxInclusive(Double.MAX_VALUE);
      type = Datatype.DOUBLE;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Double.class;
    }

    public Object getObjectType()
    {
      return DOUBLE_INSTANCE;
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
        objectValue = new Double(intNumerator*1.0 / intDenominator*1.0);
      } else
      {
        objectValue = new Double(value);
      }
      try
      {
        validate(objectValue);
        return objectValue;
      } catch (IllegalArgumentException e)
      {
        throw new ParseException("Cannot parse double point value.",0);
      } catch (DatatypeException e)
      {
        throw new ParseException("Cannot parse double point value.",0);
      }
    }

}
