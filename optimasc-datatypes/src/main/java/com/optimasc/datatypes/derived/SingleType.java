package com.optimasc.datatypes.derived;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.primitives.RealType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an IEEE 754 32-bit floating point value */
public class SingleType extends RealType
{
  protected static final Float FLOAT_INSTANCE = new Float(0);

  public static final SingleType DEFAULT_INSTANCE = new SingleType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);

  
  public SingleType()
  {
    super(7,-1);
    setMinInclusive(Float.MIN_VALUE);
    setMaxInclusive(Float.MAX_VALUE);
    type = Datatype.FLOAT;
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
      return Float.class;
    }

    public Object getObjectType()
    {
      return FLOAT_INSTANCE;
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
        objectValue = new Float(intNumerator*1.0 / intDenominator*1.0);
      } else
      {
        objectValue = new Float(value);
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
