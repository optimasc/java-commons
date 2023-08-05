package omg.org.astm.type;

import com.optimasc.datatypes.Type;
import com.optimasc.datatypes.visitor.TypeVisitor;

/** Represents a reference to a named type. This represents a 
 *  reference to a named type, for example INTEGER in certain 
 *  languages.   
 * 
 * @author Carl Eric Cod�re
 *
 */
public class NamedTypeReference extends TypeReference
{
   /** Represents the name of the Type being reference. */
   protected String typeName;
  
   /** Creates a named reference to a Type definition.
    * 
    * @param typeName The name for this type.
    * @param type The associated and original type definition
    */
  public NamedTypeReference(String typeName, Type type)
  {
    super(type);
    this.typeName = typeName;
  }
  
  
  public String getTypeName()
  {
    return typeName;
  }

  // TODO: To check if we should the typeName or not
  public boolean equals(Object obj)
  {
    boolean b = super.equals(obj);
    if ((b) && (obj instanceof NamedTypeReference))
    {
      NamedTypeReference otherObj = (NamedTypeReference) obj; 
      if (getTypeName().equals(otherObj.getTypeName()))
      {
        return true;
      }
    }
    return false;
  }


  public Object accept(TypeVisitor v, Object arg)
  {
    return v.visit(this, arg);
  }
  
  
   
}
