package com.optimasc.datatypes.primitives;

import java.text.ParseException;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.Duration;

/** * Datatype that represents elapsed time.
 *
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>DURATION</code> ASN.1 datatype</li>
 *   <li><code>timeinterval</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>Duration</code> XMLSchema built-in datatype</li>
 *   <li><code>INTERVAL</code> in SQL2003</li>
 *  </ul>
 *  
 * <p>Internally, values of this type are represented as {@link Duration} objects.</p>
 * 
 * 
 * @author Carl Eric Codere
 *
 */
public class DurationType extends PrimitiveType
{
  protected static final Duration INSTANCE = new Duration(0); 

  public DurationType()
  {
    super(Datatype.OTHER,true);
  }
  
  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }
  

  public Class getClassType()
  {
    return Duration.class;
  }

  public void validate(Object value) throws IllegalArgumentException, DatatypeException
  {
  }

  public Object getObjectType()
  {
    return INSTANCE;
  }


}
