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
 *  embedded within them in the specified order.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>SEQUENCE ASN.1 datatype</li>
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
