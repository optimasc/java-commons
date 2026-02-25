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
 *  embedded within them. Each embedded datatype is named with a unique identifier
 *  (this is also called a field). By default, the ordering is significant, and {@link #setOrdered(boolean)}
 *  should be called if it should not be significant.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li><code>SEQUENCE</code> ASN.1 datatype if <code>ordered</code> is true, or <code>SET</code> ASN.1 datatype
 *     if <code>ordered</code> is false.</li>
 *   <li><code>sequence</code> XMLSchema element if <code>ordered</code> is true, or <code>all</code> 
 *     XMLSchema element if <code>ordered</code> is false.</li>
 *   <li><code>record</code> ISO/IEC 11404 General purpose datatype</li>
 *   <li>Similar to a <code>Table</code> in SQL2003.</li>
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
