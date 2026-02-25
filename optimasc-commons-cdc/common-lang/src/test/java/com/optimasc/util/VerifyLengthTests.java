package com.optimasc.util;

import junit.framework.TestCase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class VerifyLengthTests extends TestCase
{
  /* ---------- maxLength validation ---------- */

  public void testNegativeMaxLengthThrowsException()
  {
    try
    {
      DataUtilities.verifyLength("abc", -1);
      fail("Expected IllegalArgumentException");
    }
    catch (IllegalArgumentException expected)
    {
      // expected
    }
  }

  /* ---------- CharSequence ---------- */

  public void testCharSequenceWithinLimit()
  {
    assertTrue(DataUtilities.verifyLength("abc", 3));
  }

  public void testCharSequenceAtLimit()
  {
    assertTrue(DataUtilities.verifyLength("abc", 3));
  }

  public void testCharSequenceExceedsLimit()
  {
    assertFalse(DataUtilities.verifyLength("abcd", 3));
  }

  public void testEmptyCharSequence()
  {
    assertTrue(DataUtilities.verifyLength("", 0));
  }

  /* ---------- List ---------- */

  public void testListWithinLimit()
  {
    List list = new ArrayList();
    list.add("a");
    list.add("b");

    assertTrue(DataUtilities.verifyLength(list, 2));
  }

  public void testListAtLimit()
  {
    List list = new ArrayList();
    list.add("a");
    list.add("b");

    assertTrue(DataUtilities.verifyLength(list, 2));
  }

  public void testListExceedsLimit()
  {
    List list = new ArrayList();
    list.add("a");
    list.add("b");
    list.add("c");

    assertFalse(DataUtilities.verifyLength(list, 2));
  }

  public void testEmptyList()
  {
    List list = new ArrayList();
    assertTrue(DataUtilities.verifyLength(list, 0));
  }

  /* ---------- Arrays ---------- */

  public void testObjectArrayWithinLimit()
  {
    String[] array = new String[] { "a", "b" };
    assertTrue(DataUtilities.verifyLength(array, 2));
  }

  public void testObjectArrayExceedsLimit()
  {
    String[] array = new String[] { "a", "b", "c" };
    assertFalse(DataUtilities.verifyLength(array, 2));
  }

  public void testPrimitiveArrayWithinLimit()
  {
    int[] array = new int[] { 1, 2, 3 };
    assertTrue(DataUtilities.verifyLength(array, 3));
  }

  public void testPrimitiveArrayExceedsLimit()
  {
    byte[] array = new byte[] { 1, 2 };
    assertFalse(DataUtilities.verifyLength(array, 1));
  }

  public void testEmptyArray()
  {
    Object emptyArray = Array.newInstance(String.class, 0);
    assertTrue(DataUtilities.verifyLength(emptyArray, 0));
  }

  /* ---------- Unsupported types ---------- */

  public void testUnsupportedTypeReturnsTrue()
  {
    Object unsupported = new Object();
    assertTrue(DataUtilities.verifyLength(unsupported, 0));
  }

  public void testNumberTypeReturnsTrue()
  {
    Integer number = new Integer(10);
    assertTrue(DataUtilities.verifyLength(number, 0));
  }
}
