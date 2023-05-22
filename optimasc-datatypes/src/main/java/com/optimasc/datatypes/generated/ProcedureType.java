/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.generated;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.VariableInstance;
import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.visitor.TypeVisitor;

import java.text.ParseException;
import java.util.Vector;

/**
 *
 * @author Carl
 */
public class ProcedureType extends Type {

    /** The return type of this routine */
    protected Type   returnType;

  // Contains the parameters
  protected Vector formalParameters;

  protected ClassType parent;

  /** This is a definition of a routine.
   *
   * @param name The name of the routine
   * @param comment The comment associated with this routine
   * @param parent The parent of this routine if this is defined within a class
   *     otherwise this is null.
   * @param flags The flags and qualifiers and modifiers
   *   associated with this routine, this is usage specific.
   */
   public ProcedureType()
   {
       super(false);
       formalParameters = new Vector();
       returnType = null;
   }

    public ClassType getParent()
    {
        return parent;
    }

    public void setParent(ClassType parent)
    {
        this.parent = parent;
    }

   
    public Datatype getParameterType(int index)
    {
        return (Datatype)formalParameters.elementAt(index);
    }

    public VariableInstance getParameter(int index)
    {
        return (VariableInstance)formalParameters.elementAt(index);
    }

    public int getParameterCount()
    {
        return formalParameters.size();
    }



    public void addParameter(VariableInstance param)
    {
        formalParameters.addElement(param);
    }

    public Type getReturnType()
    {
        return returnType;
    }

    public void setReturnType(Datatype returnType)
    {
        this.returnType = returnType;
    }

    public void validate(java.lang.Object integerValue) throws IllegalArgumentException, DatatypeException
    {

    }

    public Class getClassType()
    {
        return null;
    }

    public Object accept(TypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    public Object getObjectType()
    {
      // TODO Auto-generated method stub
      return null;
    }
    
}
