package com.optimasc.definitions;

import java.util.Map;

/** Interface to instantiate entity object instances.
 * 
 *  Entity providers should implement this interface
 *  to instantiate entity object instances or derived
 *  classes from the Entity interface.
 *  
 *  <p>Normally {@link #getObjectInstance(Object, String, String, Map, Map)} 
 *  method should validate the  attribute values to be assigned to
 *  the created instance and throw an Exception if they are not valid.
 *  
 * @author Carl Eric Codere
 *
 */
public interface EntityFactory
{
   /** Creates an entity object instance. 
    * 
    *  The creation of the entity object instance  
    * 
    * @param ref [in] Reference object that can be used as 
    *  additional data for the instantiation. If not
    *  used this can be <code>null</code>.  
    * @param namespaceURI [in] The namespaceURI associated
    *   with the name of this entity, this can be <code>null</code>
    *   if there is no namespace associated with this name.
    * @param name [in] The qualified name of the entity 
    *   to create. This value is mandatory and must not
    *   be <code>null</code>.
    * @param env [in] Environment attributes that 
    *   can be used as configuration to create
    *   the object instance. This value cannot
    *   be <code>null</code> even though it may
    *   be empty.
    * @param attributes [in] The attributes to 
    *   assign to the created entity. This value cannot
    *   be <code>null</code> even though it may
    *   be empty.
    * @return An instance of the <code>Entity</code> or
    *   a derived class from <code>Entity</code>.
    * @throws IllegalArgumentException In case of error.
    * @throws ClassCastException In case of error.
    */
   public Entity getObjectInstance(Object ref, String namespaceURI, String namespace, Map/*<String,Object>*/ env,
       Map/*<String,Object>*/ attributes);
   
   /** Return the schema registry associated with 
    *  this factory, the value returned can be <code>null</code>
    *  if there is no schema registry.
    * 
    * @return
    */
   public EntityRegistry getSchemaRegistry();
}
