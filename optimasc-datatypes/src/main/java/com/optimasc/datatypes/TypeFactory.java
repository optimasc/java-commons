package com.optimasc.datatypes;

import java.util.HashMap;

import omg.org.astm.type.TypeReference;
import omg.org.astm.type.UnnamedTypeReference;

/** Class that permits to create Datatype values and
 *  retrieve the default instance values.
 *  
 */
public class TypeFactory
{
  
    /* Map with clz underlying type and object a type reference */
    protected static HashMap/*<clz,TypeReference>*/ instances;
    
    static
    {
      instances = new HashMap();
    }
 
    /** Return the default instance of the specified 
     *  Datatype or type class as a TypeReference. 
     *  The default instance and its UnnamedTypeReference 
     *  is created if it is not already present.
     *  
     * @param clz [in] The Class reference of the type to return. 
     * @return A <code>TypeReference<code> containing the default
     *   type of the class.
     * @throws InstantiationException   Thrown if cannot instantiate the 
     *   the type.
     * @throws IllegalAccessException  Thrown if cannot instantiate the 
     *   the type.
     * @throws IllegalArgumentException Thrown if trying to create a type
     *   of type <code>TypeReference</code> or if the type instance
     *   cannot be created.
     */
    public static TypeReference getDefaultInstance(Class clz) throws IllegalArgumentException
    {
      if (clz.isAssignableFrom(TypeReference.class))
      {
        throw new IllegalArgumentException("Type references cannot be directly instantiated");
      }
      TypeReference typeRef =  (TypeReference) instances.get(clz);
      if (typeRef == null)
      {
        Type type = null;
        try
        {
          type = (Type) clz.newInstance();
        } catch (InstantiationException e)
        {
          throw new IllegalArgumentException(e.getMessage());
        } catch (IllegalAccessException e)
        {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
        typeRef = new UnnamedTypeReference(type);
        instances.put(clz, typeRef);
      }
      return typeRef;
    }
}
