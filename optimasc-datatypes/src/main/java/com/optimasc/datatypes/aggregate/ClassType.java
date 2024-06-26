/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.aggregate;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.MemberObject;
import com.optimasc.datatypes.generated.ProcedureType;
import com.optimasc.datatypes.visitor.TypeVisitor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import omg.org.astm.type.NamedTypeReference;

/** This represents either a class or a module that contains
 *  methods and variables. 
 *  
 * @author Carl Eric Codere
 */
public class ClassType extends DerivableAggregateType
{
    public ClassType()
    {
      super();
    }
    
    public ClassType(NamedTypeReference parent, MemberObject[] members)
    {
      super(parent,members);
    }
    
    public ClassType(NamedTypeReference parent, InterfaceType[] _implements, MemberObject[] members)
    {
      super(parent,members);
    }
    
    
    public ClassType(NamedTypeReference parent)
    {
      super(parent);
    }
    

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }

    
}
