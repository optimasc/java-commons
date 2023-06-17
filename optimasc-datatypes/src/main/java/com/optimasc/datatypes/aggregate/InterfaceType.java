package com.optimasc.datatypes.aggregate;

import java.util.Vector;

import omg.org.astm.type.NamedTypeReference;

import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents an interface, such as used in the Java
 *  programming language.
 * 
 * @author Carl Eric Codere
 *
 */
public class InterfaceType extends DerivableAggregateType
{
  
  public InterfaceType()
  {
    super();
  }
  
  
  public InterfaceType(NamedTypeReference parent)
  {
    super(parent);
  }
  
  
  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this,arg);
  }

}
