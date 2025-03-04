/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.optimasc.datatypes.aggregate;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.visitor.TypeVisitor;


/** This datatype represents a record datatype that consists of other datatypes
 *  embedded within them. By default, the ordering is significant, and {@link #setOrdered(boolean)}
 *  should be called if it should not be significant.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>SEQUENCE ASN.1 datatype if <code>ordered</code> is true, or SET ASN.1 datatype
 *     if <code>ordered</code> is false.</li>
 *   <li><code>record</code> ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 * 
 * @author Carl Eric Codere
 */
public class RecordType extends AggregateType
{
    public RecordType()
    {
        super();
    }
    
    public RecordType(MemberObject[] fields)
    {
        super(fields);
    }
    
    
    public Class getClassType()
    {
        return null;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
}
