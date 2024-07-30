package com.optimasc.datatypes.operations;

public interface SetOperations<T,V>
{
  /** Verifies if specified value is contained
   *  in the set. 
   * 
   * @param o [in] object to be checked for containment in this set
   * @return Returns true if this set contains the specified element.
   */
  public boolean isIn(V o);
  
  /** Returns the union of this set and the specified 
   *  set.
   *  
   *  <p>The returned set will contain an element C
   *  only if it is in this set or the specified set.</p> 
   * 
   * @param right [in] The other set
   * @return The new set which is the union of this 
   *   set and the specified set.
   */
  public T union(T right);

  /** Returns the union of this set and the specified 
   *  set.
   *  
   *  <p>The returned set will contain an element C
   *  only if it is in this set and the specified set.</p> 
   * 
   * @param right [in] The other set
   * @return The new set which is the union of this 
   *   set and the specified set.
   */
  public T intersection(T right);
  
  /** Returns the difference of this set and the specified 
   *  set.
   *  
   *  <p>The returned set will contain an element C
   *  only if it is in this set and not in the specified set.</p> 
   * 
   * @param right [in] The other set
   * @return The new set which is the difference of this 
   *   set and the specified set.
   */
  public T difference(T right);

  /** Returns true if this set contains no element.
   * 
   */
  public boolean isEmpty();
  
}
