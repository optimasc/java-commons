package com.optimasc.utils;

import java.util.Vector;

import junit.framework.TestCase;

public class LinkedListTest extends TestCase
{

  public LinkedListTest(String name)
  {
    super(name);
  }

  protected void setUp() throws Exception
  {
    super.setUp();
  }

  protected void tearDown() throws Exception
  {
    super.tearDown();
  }
  
  /** This does the complete testing of the LinkedList class by calling the different methods.
   *  The only method which is not tested is the addAll() with a vector parameter 
   * */
  public void testLinkedList()
  {
    LinkedList list = new LinkedList();
    String s1 = "object1";
    String s2 = "object2";
    String s3 = "object3";
    String s4 = "notincluded";

    assertEquals(true,list.isEmpty());
    assertEquals(0,list.size());
    list.add(s1);
    assertEquals(1,list.size());
    
    list.add(s2);
    assertEquals(2,list.size());
    
    list.add(s3);
    assertEquals(3,list.size());
    
    assertEquals(s1,list.get(0));
    assertEquals(s2,list.get(1));
    assertEquals(s3,list.get(2));
    
    assertEquals(s1,list.getFirst());
    assertEquals(false,list.isEmpty());
    
    Object[] o = list.toArray();
    assertEquals(3,o.length);
    assertEquals(s1.toString(),o[0].toString());
    assertEquals(s2.toString(),o[1].toString());
    assertEquals(s3.toLowerCase(),o[2].toString());
    
    // Work on an empty list
    list.clear();
    assertEquals(null,list.getFirst());
    assertEquals(true,list.isEmpty());
    assertEquals(0,list.size());
    assertEquals(0,list.toArray().length);
    
    // Add the values from an array
    list.addAll(o,o.length);
    assertEquals(s1,list.get(0));
    assertEquals(s2,list.get(1));
    assertEquals(s3,list.get(2));
    try 
    {
      list.get(3);
      fail("Failed - exception should be thrown!");
    } catch (IndexOutOfBoundsException e)
    {
      
    }
    
    assertEquals(3,list.size());
    assertEquals(s1,list.getFirst());
    assertEquals(false,list.isEmpty());

    // Try to remove non existent object
    assertEquals(false,list.remove(s4));
    // Remove object from index 1
    assertEquals(true,list.remove(s2));
    assertEquals(2,list.size());
    assertEquals(s1,list.get(0));
    assertEquals(s3,list.get(1));
    
    // Remove the last elements from the list
    assertEquals(s1, list.remove());
    assertEquals(s3, list.remove());
    assertEquals(true,list.isEmpty());
    assertEquals(null,list.getFirst());
    assertEquals(0,list.size());

    // Try to remove objects when empty
    assertEquals(false, list.remove(s1));
    assertEquals(false, list.remove(null));
    try 
    {
      list.get(0);
      fail("Failed - exception should be thrown!");
    } catch (IndexOutOfBoundsException e)
    {
      
    }
    
    try 
    {
      o = null;
      list.addAll(o,3);
      fail("Failed - exception should be thrown!");
    } catch (NullPointerException e)
    {
      
    }
    
    try 
    {
      Vector v = null;
      list.addAll(v);
      fail("Failed - exception should be thrown!");
    } catch (NullPointerException e)
    {
      
    }
    // After removing add the new values to the list
    list.add(s1);
    assertEquals(s1,list.get(0));
    assertEquals(1,list.size());
    
    
/*    
    public boolean addAll(Vector c)
    public Object remove()
  */  
  }

}
