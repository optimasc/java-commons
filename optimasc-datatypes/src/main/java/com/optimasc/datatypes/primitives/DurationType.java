package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

/** Represents an elapsed time.
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>timeinterval ISO/IEC 11404 General purpose datatype</li>
 *   <li>Duration XMLSchema built-in datatype</li>
 *  </ul>
 *
 * 
 * 
 * @author Carl Eric Codere
 *
 */
public class DurationType extends PrimitiveType
{

  public DurationType()
  {
    super(Datatype.OTHER);
  }
  
  public Object accept(DatatypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  

  public int getSize()
  {
    // TODO Auto-generated method stub
    return 0;
  }

  public Class getClassType()
  {
    // TODO Auto-generated method stub
    return null;
//    return javax.xml.datatype.Duration.class;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
    // TODO Auto-generated method stub
    
  }

  public Object getObjectType()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public Object parse(String value) throws ParseException
  {
    // TODO Auto-generated method stub
    return null;
  }
  
  

}
