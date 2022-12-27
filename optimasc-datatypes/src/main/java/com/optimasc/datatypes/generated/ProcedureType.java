/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.generated;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.VariableInstance;
import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.visitor.DatatypeVisitor;

import java.text.ParseException;
import java.util.Vector;

/**
 *
 * @author Carl
 */
public class ProcedureType extends Datatype {

    /** The return type of this routine */
    protected Datatype   returnType;

  // Contains the fields
  protected Vector parameters;

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
       super(0);
       parameters = new Vector();
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
        return (Datatype)parameters.elementAt(index);
    }

    public VariableInstance getParameter(int index)
    {
        return (VariableInstance)parameters.elementAt(index);
    }

    public int getParameterCount()
    {
        return parameters.size();
    }



    public void addParameter(VariableInstance param)
    {
        parameters.addElement(param);
    }

    public Datatype getReturnType()
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

    public int getSize()
    {
        return 0;
    }

    public Object accept(DatatypeVisitor v, Object arg)
    {
        return v.visit(this,arg);
    }
    
    public Object getObjectType()
    {
      // TODO Auto-generated method stub
      return null;
    }
    
    public Object parse(String value) throws ParseException
    {
      throw new UnsupportedOperationException("Parse method is not implemented.");
    }

}
