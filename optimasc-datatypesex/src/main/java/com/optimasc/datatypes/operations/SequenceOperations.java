package com.optimasc.datatypes.operations;

/** Operations that can be done on a sequence
 *  of values.
 * 
 * @author Carl Eric Codere
 *
 * @param <T> The type that will be operated on.
 */
public interface SequenceOperations<T,V>
{
   /** Returns true sequence contains no values, 
    */
  
  /** Verifies if the sequence contains any
   *  values or not.
   * 
   * @param sequence [in] The sequence that will
   *   be operated on.
   * @return <code>true</code> if the sequence
   *   contains no values.
   */
   public boolean isEmpty();
   
   /** Returns the first value of the sequence. If 
    *  the value is empty, returns a <code>null</code>
    *  value. */
   public V head();
   
   /** Returns a sequence of values formed by deleting
    *  the first value, if any, from the specified sequence.
    * 
    * @param sequence [in] The sequence that we need the 
    *   tail from.
    * @return A new sequence value without the first 
    *  element or an empty value if there is no data
    *  left in the resulting sequence or if new length
    *  is less than zero.
    */
   public T tail();
   
   /** Return an empty sequence of the specified type.
    * 
    * @return
    */
//   public T empty();

   /** Appends a value which should be of the 
    *  same type as the sequence type.
    * 
    * @param sequence [in] The sequence that will be operated on.
    * @param toAppend [in] A single element to add.
    * @return A sequence which contains the added value. 
    */
   public T append(V toAppend);
   
   /** Appends a sequence to this sequence 
    *  and returns a sequence which is the 
    *  concatenation of this and right sequences.
    * 
    * @param right [in] The sequence that will be concatenated to
    *   to this sequence. 
    */
   public T concatenate(T right);
   
}
