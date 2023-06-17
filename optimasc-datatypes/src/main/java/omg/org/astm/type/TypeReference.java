package omg.org.astm.type;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a reference to a type. This is
 *  an abstract class. 
 *  */ 
public abstract class TypeReference extends Type
{
   /** The type definition */
   protected Type type;
   
  public TypeReference(Type typ)
  {
    super(false);
    this.type = typ;
  }

  public Type getType()
  {
    return this.type;
  }
  
  
  public void setType(Type type)
  {
    this.type = type;
  }

  public boolean equals(Object obj)
  {
    if (obj==null)
      return false;
    if (obj==this)
      return true;
    
    if (obj instanceof TypeReference)
    {
      TypeReference otherObj = (TypeReference) obj;
      if (otherObj.getType().equals(getType())==true)
      {
        return true;
      }
    }
    return false;
  }
   
  public Class getClassType()
  {
    // TODO Auto-generated method stub
    return null;
  }

}
