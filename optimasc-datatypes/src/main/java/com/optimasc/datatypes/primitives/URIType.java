package com.optimasc.datatypes.primitives;

import java.net.URI;
import java.net.URISyntaxException;
import java.text.Format;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Convertable;
import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.PatternFacet;
import com.optimasc.datatypes.PatternHelper;
import com.optimasc.datatypes.TypeUtilities.TypeCheckResult;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.util.Pattern;

/** Represents a Uniform Resource Identifier (URI) datatype as defined 
 *  by IETF RFC 3986. 
 *  
 *  <p>To restrict the value space of the allowed values for an
 *  URI, a <code>java.text.Format</code> implementation may be used, and its
 *  <code>format()</code> method will be used to verify the
 *  validity of the object, and should throw an <code>IllegalArgumentException</code>
 *  if the URI syntax is not valid.
 * 
 * @author Carl Eric Codere
 *
 */
public class URIType extends PrimitiveType implements Convertable
{
  protected Format formatter;

  private static URIType defaultTypeInstance;
  private static TypeReference defaultTypeReference;
  
  
  
  public URIType()
  {
    super(false);
    formatter = null;
/*    setMinLength(0);
    // Known limitation of IE as of 2017
    // See https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers 
    //
    setMaxLength(2000);*/
  }
  
  /** Creates an URI datatype restricted by the values specified
   *  by the validator class.
   * 
   * @param validator [in] A formatter that will be used to restrict
   *  the value space of the class of this type.
   */
  public URIType(Format validator)
  {
    super(false);
    this.formatter = validator;
    
/*    setMinLength(0);
    // Known limitation of IE as of 2017
    // See https://stackoverflow.com/questions/417142/what-is-the-maximum-length-of-a-url-in-different-browsers 
    //
    setMaxLength(2000);*/
  }
  

  public Class getClassType()
  {
    return URI.class;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }

  public boolean isRestrictionOf(Datatype value)
  {
    if ((value instanceof URIType)==false)
    {
      throw new IllegalArgumentException("Expecting parameter of type '"+value.getClass().getName()+"'.");
    }
    URIType otherType = (URIType) value;
    if ((formatter!=null) && (otherType.formatter==null))
    {
      return true;
    }
    return false;
  }

  /** {@inheritDoc}
   * 
   *  <p>This implementation supports as input either a <code>String</code>
   *  which will then be parsed and converted to an URI, or directly a <code>URI</code>
   *  class. In all cases, if a formatter has been set to restrict the syntax
   *  of this URI, it will be used on the string representation of the value
   *  to check its validity.</p>
   * 
   */
  public Object toValue(Object value, TypeCheckResult conversionResult)
  {
    URI result;
    if (value instanceof URI)
    {
      if (validateFormat((URI)value)==false)
      {
        conversionResult.error = new DatatypeException(
            DatatypeException.ERROR_DATA_TYPE_MISMATCH, "Datatype definition does not match allowed pattern input.");
           return null;
      }
      return value;
    }
    
    if (value instanceof String)
    {
        String uriString =(String)value;
        try
        {
        result =  new URI(uriString);
        if (validateFormat(result)==false)
        {
          conversionResult.error = new DatatypeException(
              DatatypeException.ERROR_DATA_TYPE_MISMATCH, "Datatype definition does not match allowed pattern input.");
             return null;
        }
        return result;
      } catch (URISyntaxException e)
      {
        conversionResult.error = new DatatypeException(
            DatatypeException.ERROR_DATA_TYPE_MISMATCH, e.getMessage());
        return null;
      }
    }
    conversionResult.error = new DatatypeException(
        DatatypeException.ERROR_DATA_TYPE_MISMATCH, "Not allowed URI type.");
    return null;
  }

  public Format getFormatValidator()
  {
    return formatter;
  }

  
  /** Validates if the value of string is within the allowed pattern name
   *  of allowed values.
   * 
   * @param value The value to check
   * @return true if the validation is successful, otherwise false.
   */
  public boolean validateFormat(URI value)
  {
    if (formatter==null)
      return true;
    try 
    {
     formatter.format(value);
    } catch (IllegalArgumentException e)
    {
      return false;
    }
    return true;
  }

  public int hashCode()
  {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((formatter == null) ? 0 : formatter.hashCode());
    return result;
  }

  public boolean equals(Object obj)
  {
    if (this == obj)
      return true;
    if (!super.equals(obj))
      return false;
    if (getClass() != obj.getClass())
      return false;
    URIType other = (URIType) obj;
    if (formatter == null)
    {
      if (other.formatter != null)
        return false;
    }
    else if (!formatter.equals(other.formatter))
      return false;
    return true;
  }
  
  public static TypeReference getInstance()
  {
    if (defaultTypeInstance == null)
    {
      defaultTypeInstance = new URIType();
      defaultTypeReference = new UnnamedTypeReference(defaultTypeInstance);
    }
    return defaultTypeReference; 
  }

  
  
}
