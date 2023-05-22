/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.optimasc.datatypes.aggregate;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.VariableInstance;
import com.optimasc.datatypes.visitor.TypeVisitor;

import java.text.ParseException;
import java.util.Vector;

/** This datatype represents a record datatype that consists of other datatypes
 *  embedded within them in the specified order.
 *  
 *  This is equivalent to the following datatypes:
 *  <ul>
 *   <li>SEQUENCE ASN.1 datatype</li>
 *   <li>sequence/record ISO/IEC 11404 General purpose datatype</li>
 *  </ul>
 * 
 * @author Carl Eric Codere
 */
public class RecordType extends Datatype
{
    protected static final Object UNKNOWN_OBJECT = new Object();
  
    // Contains the fields

    protected Vector fields;

    public RecordType()
    {
        super(Datatype.STRUCT,false);
        fields = new Vector();
    }

    public RecordType(String name, String comment, int flags)
    {
        super(name, comment, Datatype.STRUCT, false, flags);
        fields = new Vector();
    }

    /**
     * Adds the specified filed to this record definition.
     *
     */
    public void addField(VariableInstance field)
    {
        fields.addElement(field);
    }

    public VariableInstance geField(int index)
    {
        return (VariableInstance) fields.elementAt(index);
    }

    public int geFieldCount()
    {
        return fields.size();
    }

    public Class getClassType()
    {
        return null;
    }

    public void validate(Object integerValue) throws IllegalArgumentException,
            DatatypeException
    {
    }

    /** Search for the specified field with specified access qualifiers
     *  in the current class and parent classes starting with the current
     *  class.
     *
     * @return null if field with specified name is not found, otherwise
     *   the actual field definition.
     */
    public VariableInstance lookupField(String name, int flags)
    {
        int i;
        VariableInstance var;
        for (i = 0; i < fields.size(); i++)
        {
            var = (VariableInstance) fields.elementAt(i);
            if (var.getName().equals(name))
            {
                if ((var.getModifiers() & flags) != 0)
                {
                    return var;
                }
            }
        }
        return null;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    public Object getObjectType()
    {
      return UNKNOWN_OBJECT;
    }
    
}
