/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.aggregate;

import com.optimasc.datatypes.VariableInstance;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.visitor.TypeVisitor;
import java.util.Vector;

/** This represents either a class or a module that contains
 *  methods and variables.
 *
 * @author Carl
 */
public class ClassType extends RecordType
{

    /* Parent class of this class */
    protected ClassType parent;
    /* List of implemented interfaces */
    protected Vector   _implements;
    /* List of methods */
    protected Vector   methods;
    protected boolean  _interface;

    public ClassType()
    {
      methods = new Vector();
    }
    
    public ClassType getParent()
    {
        return parent;
    }

    public void setParent(ClassType parent)
    {
        this.parent = parent;
    }



/**
   * Adds the specified filed to this record definition.
   *
   * @param fieldIdentifier
   *          The actual field identifier to access the element.
   * @param field
   *          The field datatype definition.
   */
  public void addMethod(ProcedureType routine)
  {
    methods.addElement(routine);
  }


  /**
   * Returns the values associated with this name
   *
   */
  public ProcedureType geMethod(int index)
  {
    return  (ProcedureType)methods.elementAt(index);
  }

  public int geMethodCount()
  {
    return methods.size();
  }



    public boolean isInterface()
    {
        return _interface;
    }

    public void setInterface(boolean _interface)
    {
        this._interface = _interface;
    }


    /** Search for the specified field with specified access qualifiers
     *  in the current class and parent classes starting with the current
     *  class.
     *
     * @return null if field with specified name is not found, otherwise
     *   the actual field definition.
     */
 /*  public ProcedureType lookupMethod(String name, int flags)
    {
        int i;
        ProcedureType var = null;
        ClassType parentType = null;
        for (i = 0; i < methods.size(); i++)
        {
            var = (ProcedureType) methods.elementAt(i);
            if (var.getName().equals(name))
            {
                if ((var.getModifiers() & flags) != 0)
                {
                    return var;
                }
            }
        }
        // Look through all parents
        if (var == null)
        {
            parentType = parent;
            while (parentType != null)
            {
                var = parentType.lookupMethod(name, flags);
                if (var != null)
                    return var;
                parentType = parentType.getParent();
            }
        }
        return null;
    }*/

    public VariableInstance lookupField(String name, int flags)
    {
        VariableInstance var = super.lookupField(name, flags);
        ClassType parentType = null;
        // Look through all parents
        if (var == null)
        {
            parentType = parent;
            while (parentType != null)
            {
                var = parentType.lookupField(name, flags);
                if (var != null)
                    return var;
                parentType = parentType.getParent();
            }
        }
        return null;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
