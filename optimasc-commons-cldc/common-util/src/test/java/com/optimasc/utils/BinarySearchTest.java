package com.optimasc.utils;

import java.util.Vector;

import junit.framework.TestCase;

public class BinarySearchTest extends TestCase
{

  public BinarySearchTest(String name)
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

  public void testSearchVectorComparatorObject()
  {
    Comparator cmp = StringComparer.getInstance();
    Vector v = new Vector();
    v.addElement(new String("A"));
    v.addElement(new String("B"));
    v.addElement(new String("G"));
    v.addElement(new String("Z"));
    v.addElement(new String("Z"));
    assertEquals(0,BinarySearch.search(v, cmp, "A"));
    assertEquals(1,BinarySearch.search(v, cmp, "B"));
    assertEquals(2,BinarySearch.search(v, cmp, "G"));
    assertEquals(3,BinarySearch.search(v, cmp, "Z"));
    assertEquals(-1,BinarySearch.search(v, cmp, "C"));
    assertEquals(-1,BinarySearch.search(v, cmp, "AA"));
  }

  public void testSearchObjectArrayComparatorObject()
  {
    Comparator cmp = StringComparer.getInstance();
    Object v[] = new String[5];
    v[0] = new String("A");
    v[1] = new String("B");
    v[2] = new String("G");
    v[3] = new String("Z");
    v[4] = new String("Z");
    assertEquals(0,BinarySearch.search(v, cmp, "A"));
    assertEquals(1,BinarySearch.search(v, cmp, "B"));
    assertEquals(2,BinarySearch.search(v, cmp, "G"));
    assertEquals(3,BinarySearch.search(v, cmp, "Z"));
    assertEquals(-1,BinarySearch.search(v, cmp, "C"));
    assertEquals(-1,BinarySearch.search(v, cmp, "AA"));
  }

}
