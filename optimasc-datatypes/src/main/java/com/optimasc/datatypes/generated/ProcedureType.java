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
import java.util.List;
import java.util.Vector;

/**
 * Represents a procedure/function/method declaration. A declaration does not
 * contain any symbol names, only parameter types and return types.
 *
 * @author Carl Eric Codere
 */
public class ProcedureType extends Type
{

  /** The return type of this routine */
  protected Object returnType;

  /**
   * List of parameters, which is an implementation specific list of Objects
   * representing the typeInformation
   */
  protected List formalParameters;

  protected ClassType parent;

  /**
   * This is a definition of a routine.
   *
   * @param name
   *          The name of the routine
   * @param comment
   *          The comment associated with this routine
   * @param parent
   *          The parent of this routine if this is defined within a class
   *          otherwise this is null.
   * @param flags
   *          The flags and qualifiers and modifiers associated with this
   *          routine, this is usage specific.
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

  public Object getParameter(int index)
  {
    return formalParameters.get(index);
  }

  public int getParameterCount()
  {
    return formalParameters.size();
  }

  public void addParameter(Object param)
  {
    formalParameters.add(param);
  }

  public Object getReturnType()
  {
    return returnType;
  }

  public void setReturnType(Object returnType)
  {
    this.returnType = returnType;
  }

  public void validate(java.lang.Object integerValue) throws IllegalArgumentException,
      DatatypeException
  {

  }

  public Class getClassType()
  {
    return null;
  }

  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }

  public Object getObjectType()
  {
    // TODO Auto-generated method stub
    return null;
  }

  public boolean equals(Object obj)
  {
    if (obj == null)
      return false;
    if (obj == this)
      return true;
    if (obj instanceof ProcedureType)
    {
      ProcedureType otherObj = (ProcedureType) obj;
      if ((returnType != null))
      {
        if ((returnType.equals(otherObj)) == false)
        {
          return false;
        }
      }
      // Check parameters
      if (formalParameters.size() != otherObj.formalParameters.size())
      {
        return false;
      }
      // All parameters are equal
      for (int i = 0; i < formalParameters.size(); i++)
      {
        if (formalParameters.get(i).equals(otherObj.formalParameters.get(i)) == false)
          return false;
      }
      return true;
    }
    return false;
  }

}
