package com.optimasc.datatypes.defined;


import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.date.DateTime;

/** Represents a full date and time specification with
 *  a second resolution including timezone information.
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>time(second, 10, 0)</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li><code>dateTimestamp</code> XMLSchema 1.1 built-in datatype</li>
 *  </ul>
 */  
public class TimestampType extends DateTimeType
{

  public TimestampType()
  {
    super(DateTime.TimeAccuracy.SECOND,true);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
}
