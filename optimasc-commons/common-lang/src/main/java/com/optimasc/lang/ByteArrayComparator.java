package com.optimasc.lang;

import java.util.Comparator;

public class ByteArrayComparator implements Comparator
{
  public int compare(Object o1, Object o2)
  {
    byte[] left = (byte[]) o1;
    byte[] right = (byte[]) o2;
    for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++)
    {
      int a = (left[i] & 0xff);
      int b = (right[j] & 0xff);
      if (a != b)
      {
        return a - b;
      }
    }
    return left.length - right.length;
  }
}
