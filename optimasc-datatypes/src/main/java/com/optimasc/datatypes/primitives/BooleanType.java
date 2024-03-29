/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeConverter;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Parseable;
import com.optimasc.datatypes.PatternFacet;
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
 *  </ul>
 *
 *  <p>Contrary to ISO/IEC 11404, this type is considered ordered. </p>
 * <p>Internally, values of this type are represented as {@link Boolean} objects. </p>
 *  
 *
 * @author Carl Eric Codère
 */
public class BooleanType extends PrimitiveType implements DatatypeConverter, PatternFacet, Parseable
{
  public static final BooleanType DEFAULT_INSTANCE = new BooleanType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);
  
  protected static final String REGEX_PATTERN = "true|false|0|1|TRUE|FALSE";
  
    /** Creates a new boolean type definition.
     *
     * @param name The datatype name associated with this  type.
     * @param comment The comment or note associated with this datatype.
     */
    public BooleanType()
    {
        super(Datatype.BOOLEAN,true);
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

    public Object getObjectType()
    {
      return Boolean.FALSE;
    }

    public Object parse(String value) throws ParseException
    {
      Boolean b;
      String lowerCase = value.toLowerCase();
      if (lowerCase.equals("true") || lowerCase.equals("1"))
      {
        b =  Boolean.TRUE;
      }
      else
      if (lowerCase.equals("false") || lowerCase.equals("0"))
      {
        b =  Boolean.FALSE;
      } 
      else
      {
        throw new ParseException("Cannot parse boolean value",0);
      }
      try
      {
        validate(b);
      } catch (DatatypeException e)
      {
        throw new ParseException("Cannot parse boolean value",0);
      }
      return b;
    }
    
    public String getPattern()
    {
      return REGEX_PATTERN;
    }

    public void setPattern(String value)
    {
      throw new IllegalArgumentException("Pattern is read only for this datatype.");
    }
    

}
