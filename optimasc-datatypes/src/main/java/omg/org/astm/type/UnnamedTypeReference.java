package omg.org.astm.type;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a reference where the actual type is directly
 *  defined without having an associated name.
 * 
 * @author Carl Eric Cod�re
 *
 */
public class UnnamedTypeReference extends TypeReference
{
  public UnnamedTypeReference(Type type)
  {
    super(type);
    this.type = type;
  }

  public boolean equals(Object obj)
  {
    boolean b = super.equals(obj);
    if ((b) && (obj instanceof UnnamedTypeReference))
    {
      return true;
    }
    return false;
  }

  
  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }
  
  
}
