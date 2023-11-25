package com.optimasc.utils;

import java.util.Vector;

/**
*  Implements a linked list that is also compatible with the Java SE List 
*  interface. It supports adding nulls to the list.
 * 
 * <p>
 * <strong>Note that this implementation is not synchronized.</strong> If
 * multiple threads access a linked list concurrently, and at least one of the
 * threads modifies the list structurally, it <i>must</i> be synchronized
 * externally. (A structural modification is any operation that adds or deletes
 * one or more elements; merely setting the value of an element is not a
 * structural modification.) This is typically accomplished by synchronizing on
 * some object that naturally encapsulates the list.
 * 
 * @author Carl Eric Codere
 * @see Vector
 */
public class LinkedList
{
  // head of the linked list
  private LinkedListNode head;
  // tail of the link list
  private LinkedListNode tail;
  // Contains the number elements in the list.
  private int size;

  public LinkedList()
  {
    head = null;
    size = 0;
    tail = null;
  }

  /**
   * Appends the specified element to the end of this list.
   * 
   * @param o
   *          element to be appended to this list
   * @return <tt>true</tt>
   */
  public boolean add(Object o)
  {
    if (head == null)
    {
      head = new LinkedListNode(o);
      tail = head;
    } else
    {
      LinkedListNode temp = new LinkedListNode(o);
      tail.next = temp;
      tail = temp;
    }
    size++;
    return true;
  }

  /**
   * Returns the first element in this list.
   * 
   * @return the first element in this list or null if the list is empty.
   */
  public Object getFirst()
  {
    if (head == null)
      return null;
    return head.data;
  }

  /**
   * Appends all of the elements in the specified collection to the end of this
   * list, in the order that they are returned by the specified collection's
   * iterator.
   */
  public boolean addAll(Vector c)
  {
    int i;
    // Add all elements of the vector in the correct order.
    for (i = 0; i < c.size(); i++)
    {
      add(c.elementAt(i));
    }
    return true;
  }

  /**
   * Appends all of the elements in the specified collection to the end of this
   * list, in the order that they are returned by the specified collection's
   * iterator.
   */
  public boolean addAll(Object[] c, int len)
  {
    int i;
    // Add all elements of the vector in the correct order.
    for (i = 0; i < len; i++)
    {
      add(c[i]);
    }
    return true;

  }

  public boolean isEmpty()
  {
    if (head == null)
      return true;
    return false;
  }

  /**
   * Returns the element at the specified position in this list.
   * 
   * @param index
   *          index of the element to return
   * @return the element at the specified position in this list
   * @throws IndexOutOfBoundsException
   */
  public Object get(int index)
  {
    int i = 0;
    LinkedListNode t1 = head;
    if (t1 == null)
      throw new IndexOutOfBoundsException();
    while (t1 != null)
    {
      if (index == i)
        break;
      t1 = t1.next;
      i++;
    }
    if (t1 == null)
      throw new IndexOutOfBoundsException();
    return t1.data;
  }


  /**
   * Retrieves and removes the head (first element) of this list.
   * 
   * @return the head of this list
   */
  public Object remove()
  {
    if (head != null)
    {
      LinkedListNode temp = head;
      head = head.next;
      size--;
      return temp.data;
    } else
    {
      size--;
      return head.data;
    }
  }

  public String toString()
  {
    Object t1;
    String s;
    int i = 0;
    s = "[";
    while ((t1 = get(i)) != null)
    {
      i++;
      s += t1.toString() + ",";
    }
    s += "]";
    return s;
  }

  /**
   * Returns an array containing all of the elements in this list in proper
   * sequence (from first to last element).
   * 
   * <p>
   * The returned array will be "safe" in that no references to it are
   * maintained by this list. (In other words, this method must allocate a new
   * array). The caller is thus free to modify the returned array.
   * 
   * <p>
   * This method acts as bridge between array-based and collection-based APIs.
   * 
   * @return an array containing all of the elements in this list in proper
   *         sequence
   */
  public Object[] toArray()
  {
    Object[] result = new Object[size];
    Object o;
    int i = 0;

    for (i = 0; i < size; i++)
    {
      o = get(i);
      result[i] = o;
    }
    return result;
  }

  /**
   * Removes the first occurrence of the specified element from this list, if it
   * is present. If this list does not contain the element, it is unchanged.
   * More formally, removes the element with the lowest index <tt>i</tt> such
   * that
   * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>
   * (if such an element exists). Returns <tt>true</tt> if this list contained
   * the specified element (or equivalently, if this list changed as a result of
   * the call).
   * 
   * @param o
   *          element to be removed from this list, if present
   * @return <tt>true</tt> if this list contained the specified element
   */
  public boolean remove(Object o)
  {
        LinkedListNode t1 = head;
        LinkedListNode previous = head;
        if (t1 == null)
          return false;
        while (t1 != null)
        {
          if (((t1.data == null) && (o == null)) || (t1.data.equals(o)))
          {
            if (t1 == tail)
             tail = previous; 
            previous.next = t1.next;
            size--;
            return true;
          }  
          t1 = t1.next;
        }
    return false;
  }

  /**
   * Removes all of the elements from this list.
   */
  public void clear()
  {
    LinkedListNode e = head;
    while (e != null)
    {
      LinkedListNode next = e.next;
      e.next = null;
      e.data = null;
      e = next;
    }
    head = null;
    tail = null;
    size = 0;
  }

  /**
   * Returns the number of elements in this list.
   * 
   * @return the number of elements in this list
   */
  public int size()
  {
    return size;
  }

}
