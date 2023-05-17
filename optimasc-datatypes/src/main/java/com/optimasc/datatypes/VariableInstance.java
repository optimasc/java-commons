/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes;

import java.text.ParseException;

import com.optimasc.datatypes.aggregate.RecordType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** This represents either a field in a class, a variable
 *  instance associated with a datatype or a method parameter.
 *
 * @author Carl Eric Codère
 */
public class VariableInstance extends Datatype {


    protected Datatype dataType;
    /** Indicates if this value is used or not */
    protected boolean used;
    /** Indicates if this value is assigned or not */
    protected boolean assigned;
    protected RecordType parent;


    public VariableInstance(String name, Datatype type, RecordType parent, int modifiers)
    {
        super(name,null,0,false,modifiers);
        this.dataType = type;
        this.parent = parent;
    }

    public RecordType getParent()
    {
        return parent;
    }

    public void setParent(RecordType parent)
    {
        this.parent = parent;
    }



    /** Returns the name associated with this instance,
     *  it can be a method name, a class or interface name
     *  or a variable/field name depending on the case.
     *
     * @return
     */
    public String getName()
    {
       return name;
    }

    /** Returns the internal type associated with this
     *  instance.
     *
     * @return
     */
    public Datatype getDataType()
    {
        return (Datatype)dataType;
    }

    /** Sets the name associated with this instance. */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Returns the Node associated with this statement in the
     *  parser.
     * @return
     */
    public Object getUserData()
    {
        return userData;
    }

    public void setUserData(Object userData)
    {
        this.userData = userData;
    }

    public boolean isUsed()
    {
        return used;
    }

    public void setUsed(boolean used)
    {
        this.used = used;
    }

    public boolean isAssigned()
    {
        return assigned;
    }

    public void setAssigned(boolean assigned)
    {
        this.assigned = assigned;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    public Class getClassType()
    {
        return null;
    }
    

    public int getSize()
    {
        return -1;
    }

    public void validate(Object integerValue) throws IllegalArgumentException, DatatypeException
    {
    }

    public Object getObjectType()
    {
      return null;
    }
    
    public Object parse(String value) throws ParseException
    {
      throw new UnsupportedOperationException("Parse method is not implemented.");
    }
    

}
