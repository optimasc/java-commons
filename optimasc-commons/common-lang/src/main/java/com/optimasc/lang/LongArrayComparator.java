package com.optimasc.lang;

import java.util.Comparator;

public class LongArrayComparator implements Comparator
{
  public int compare(Object o1, Object o2)
  {
    long[] left = (long[]) o1;
    long[] right = (long[]) o2;
    for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++)
    {
      long a = (left[i]);
      long b = (right[j]);
      if (a != b)
      {
        return (int)(a - b);
      }
    }
    return left.length - right.length;
  }
}
