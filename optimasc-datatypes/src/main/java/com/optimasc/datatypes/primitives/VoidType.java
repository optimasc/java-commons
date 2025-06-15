package com.optimasc.datatypes.primitives;

import omg.org.astm.type.NamedTypeReference;
import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;
import com.optimasc.lang.GregorianDatetimeCalendar;

/** Datatype that represents an object that is syntactically
 *  required but which carries no information.
 *  
 * This is equivalent to the following datatypes:
 * <ul>
 * <li><code>void</code> ISO/IEC 11404 General purpose datatype</li>
 * </ul>
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public class VoidType extends Type
{
  public VoidType()
  {
    super(false);
  }
  

  public Class getClassType()
  {
    return null;
  }
  
  public Object accept(TypeVisitor v, Object arg)
  {
      return v.visit(this,arg);
  }

  public boolean equals(Object obj)
  {
    /* null always not equal. */
    if (obj == null)
      return false;
    /* Same reference returns true. */
    if (obj == this)
    {
      return true;
    }
      if (!(obj instanceof VoidType))
      {
          return false;
      }
      return true;
  }
  
  public String getGPDName()
  {
    return "void";
  }
  
  
  

}
