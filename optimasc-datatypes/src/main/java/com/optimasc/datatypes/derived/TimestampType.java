package com.optimasc.datatypes.derived;


import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.primitives.DateTimeType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a full date and time specification with
 *  a second resolution. 
 * 
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>GeneralizedTime ASN.1 datatype</li>
 *   <li>time(second, 10, 0) ISO/IEC 11404 General purpose datatype</li>
 *   <li>dateTime XMLSchema built-in datatype</li>
 *  </ul>
 */  
public class TimestampType extends DateTimeType
{
  public static final TimestampType DEFAULT_INSTANCE = new TimestampType();
  public static final UnnamedTypeReference DEFAULT_TYPE_REFERENCE = new UnnamedTypeReference(DEFAULT_INSTANCE);

  public TimestampType()
  {
    super(Datatype.TIMESTAMP);
    setResolution(RESOLUTION_SECOND);
  }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    /** Compares this TimestampType to the specified object. 
     *  The result is true if and only if the argument is not null 
     *  and is a TimestampType object.
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
        if (!(obj instanceof TimestampType))
        {
            return false;
        }
        return true;
    }
    
}
