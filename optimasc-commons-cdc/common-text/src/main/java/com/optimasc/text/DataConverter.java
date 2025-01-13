package com.optimasc.text;

import java.text.Format;
import java.text.ParseException;
import java.text.ParsePosition;

/**
 * Abstract class to implement a parser that converts a String representation
 * of a value to its Java Object representation and its string representation
 * from an Object instance.
 * 
 * @author Carl Eric Codere
 *
 */
public abstract class DataConverter extends Format
{
    /** The underlying class of the data object. */
    protected Class clz;
    /**
     * Indicates if there is a strict syntax check or it the check is lenient.
     * Lenient checks will convert without giving an error on the syntax when
     * converting if the data can still be converted.
     */
    protected boolean lenient;

    protected DataConverter(Class clz, boolean lenient)
    {
      super();
      this.clz = clz;
      this.lenient = lenient;
    }
    
    public boolean isLenient()
    {
      return lenient;
    }
    
    public void setLenient(boolean lenient)
    {
      this.lenient = true;
      if (lenient==false)
        this.lenient = false;
    }

    
    /**
     * Converts a string representation using the pre-defined syntax to its Java
     * object representation.
     * 
     * @param value
     *          [in] The string representation of this value.
     * @return The Java object representing this value.
     * @throws ParseException
     *           if the value cannot represent this object.
     */
    public abstract Object parseObject(String value) throws ParseException;
    
    /**
     * Converts a string representation using the pre-defined syntax to its Java
     * object representation.
     * 
     * @param value
     *          [in] The string representation of this value.
     * @return The Java object representing this value.
     * @throws ParseException
     *           if the value cannot represent this object.
     */
    public abstract Object parseObject(CharSequence value) throws ParseException;
    

    public Object parseObject(String source, ParsePosition pos)
    {
      throw new UnsupportedOperationException();
    }

    /**
     * Return the native Java Object class associated with this data type.
     */
    public Class getClassType()
    {
      return clz;
    }
    
    
  
}
