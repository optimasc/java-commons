/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.aggregate;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.visitor.TypeVisitor;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import omg.org.astm.type.NamedTypeReference;

/** This represents either a class or a module that contains
 *  methods and variables.
 *
 * @author Carl
 */
public class ClassType extends DerivableAggregateType
{
    /* List of implemented interfaces  */
    protected List   _implements;

    public ClassType()
    {
      super();
      _implements = new Vector(); 
    }
    
    public ClassType(NamedTypeReference parent, MemberObject[] members)
    {
      super(parent,members);
      _implements = new Vector(); 
    }
    
    public ClassType(NamedTypeReference parent, InterfaceType[] _implements, MemberObject[] members)
    {
      super(parent,members);
      this._implements = Arrays.asList(_implements); 
    }
    
    
    public ClassType(NamedTypeReference parent)
    {
      super(parent);
      _implements = new Vector(); 
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


    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

}
