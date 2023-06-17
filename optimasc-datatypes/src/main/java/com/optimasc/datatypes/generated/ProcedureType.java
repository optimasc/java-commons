/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.generated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.DatatypeException;
import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.aggregate.ClassType;
import com.optimasc.datatypes.primitives.VoidType;
import com.optimasc.datatypes.visitor.TypeVisitor;

/**
 * Represents a procedure/function/method declaration. A declaration does not
 * contain any symbol names, only parameter types and return types.
 *
 * @author Carl Eric Codere
 */
public class ProcedureType extends Type
{
  /** The return type of this routine */
  protected TypeReference returnType;

  /**
   * List of parameters, which is an implementation specific list of {@link FormalParameterType} 
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
   * @param derivesFrom
   *          The parent of this routine if this is defined within a class
   *          otherwise this is null.
   * @param flags
   *          The flags and qualifiers and modifiers associated with this
   *          routine, this is usage specific.
   */
  public ProcedureType()
  {
    super(false);
    this.formalParameters = new ArrayList();
    this.returnType = new UnnamedTypeReference(new VoidType());
  }
  
  public ProcedureType(TypeReference returnType,FormalParameterType[] params)
  {
    super(false);
    this.formalParameters = Arrays.asList(params);
    this.returnType = returnType;
  }
  

  public ClassType getParent()
  {
    return parent;
  }

  public void setParent(ClassType parent)
  {
    this.parent = parent;
  }

  public FormalParameterType getParameter(int index)
  {
    return (FormalParameterType) formalParameters.get(index);
  }

  public int getParameterCount()
  {
    return formalParameters.size();
  }

  public void addParameter(FormalParameterType param)
  {
    formalParameters.add(param);
  }
  
  public void setParameter(int index, FormalParameterType param)
  {
    formalParameters.set(index,param);
  }
  

  public TypeReference getReturnType()
  {
    return (TypeReference) returnType;
  }

  public void setReturnType(TypeReference returnType)
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

  public void setParameters(List formalParameters)
  {
    this.formalParameters = formalParameters;
  }
  
  

}
