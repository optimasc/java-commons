package com.optimasc.definitions;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;


/**
 * An in-memory registry of <code>Entity</code> objects.  
 *   
 * @author Carl Eric Codere
 *
 */
public class EntityRegistry
{
  /** The actual registry. */
  protected Map/*<String, Entity>*/ ids;
  protected Set/*<Entity>*/ values; 
  
  /** Constructs an empty entity registry. 
   */
  public EntityRegistry()
  {
    values = new HashSet/*<Entity>*/();
    ids = new HashMap/*<Entity>*/();
  }


  
  

  /** Clear all entries in the registry. */
  public void clear()
  {
    ids.clear();
    values.clear();
  }
  
  /** Lookup in the registry for the specified entity. Lookups
   *  searches for the entity with the specified ID or 
   *  expandedName.
   * 
   * @param name [in] The ID or expanded name of the entity 
   *    to search for.
   * @param clz [in] The type of entity class
   *   to look for, or <code>null</code> if
   *   any entry can be returned.
   * @return <code>null</code> if not found,
   *   otherwise the actual entity instance. 
   */
  public Entity lookup(String name, Class clz)
  {
    Entity object = (Entity) ids.get(name);
    if ((object != null) && (clz!=null))
    {
       if (clz.isAssignableFrom(object.getClass()))
         return object;
       return null;
    }
    return object;
  }

  /**
   * Return all the named entities in the current
   * registry.
   *
   */
  public Iterator/*<Entity>*/ iterator()
  {
    return values.iterator();
  }

  /**
   * Add a new Entity to the registry. 
   * If an Entity with same name or ID is added, then it 
   * will not be added and the function will return false.
   * 
   * @param object [in]
   *          [in] The named entity to add.
   */
  public boolean add(Entity object)
  {
    Entity oldEntity = (Entity) ids.get(object.getID());
    if (oldEntity != null)
    {
      return false;
    }
    oldEntity = (Entity) ids.get(object.getExpandedName());
    if (oldEntity != null)
    {
      return false;
    }
    ids.put(object.getID(), object);
    ids.put(object.getExpandedName(), object);
    values.add(object);
    return true;
  }

  /** Return an immutable copy of the
   *  underlying collection of the entities in this registry.
   * 
   * @return A set
   */
  public Set getAsSet()
  {
    return Collections.unmodifiableSet(values);
  }

  /** Remove the specified entity.
   * 
   * @param entity [in] The entity to remobe. 
   * @return <code>true<code>
   *   if the specified entity
   *   was present, otherwise <code>false</code>
   */
  public boolean remove(Entity item)
  {
    ids.remove(item.getExpandedName());
    ids.remove(item.getID());
    return values.remove(item);
  }
  

  /** Adds all of the elements in the specified collection to this set if they're 
   *  not already present and they are of class <code>Entity</code>.
   *  
   * @param c [in] The collection to add
   * @throws ClassCastException Thrown if one of the class is not an
   *   instance of the <code>Entity</code> class.
   */
  public void addAll(Collection/*<Entity>*/ c)
  {
    Iterator/*<Entity>*/ it = c.iterator();
    while (it.hasNext())
    {
      Entity e = (Entity) it.next();
      /* If the item is not present, add it otherwise skip it. */
      if (values.contains(e)==false)
      {
        add(e);
      }
    }
  }
  
  /** Returns the number of entities in this registry.
   * 
   */
  public int size()
  {
    return values.size();
  }
  
}
