package com.optimasc.utils;

import java.util.Vector;

public final class QuickSort
{
  protected Comparator comparator;
  
  public QuickSort(Comparator c)
  {
    this.comparator = c;
  }
  
  /**
   * This is a generic version of C.A.R Hoare's Quick Sort algorithm. This
   * will handle vectors that are already sorted, and vectors with duplicate
   * keys.
   * <p>
   * If you think of a one dimensional vector as going from the lowest index
   * on the left to the highest index on the right then the parameters to this
   * function are lowest index or left and highest index or right.
   * 
   * @param v
   *            A <code>Vector</code> of <code>Ordered</code> items.
   * @param lo0
   *            Left boundary of vector partition.
   * @param hi0
   *            Right boundary of vector partition.
   * @exception ClassCastException
   *                If the vector contains objects that are not
   *                <code>Ordered</code>.
   */
  public final void sort(Vector v, int lo0, int hi0)
      throws ClassCastException {
    int lo = lo0;
    int hi = hi0;
    Object mid;

    if (hi0 > lo0) { // arbitrarily establish partition element as the
              // midpoint of the vector
      mid = v.elementAt((lo0 + hi0) / 2);

      // loop through the vector until indices cross
      while (lo <= hi) {
        // find the first element that is greater than or equal to
        // the partition element starting from the left index
        while ((lo < hi0)
            && (0 > (comparator.compare(v.elementAt(lo), mid))))
            {
          ++lo;
            }

        // find an element that is smaller than or equal to
        // the partition element starting from the right index
        while ((hi > lo0)
            && (0 < (comparator.compare(v.elementAt(hi), mid))))
            {
          --hi;
            }

        // if the indexes have not crossed, swap
        if (lo <= hi)
          swap(v, lo++, hi--);
      }

      // if the right index has not reached the left side of array
      // must now sort the left partition
      if (lo0 < hi)
        sort(v, lo0, hi);

      // if the left index has not reached the right side of array
      // must now sort the right partition
      if (lo < hi0)
        sort(v, lo, hi0);
    }
  }

  private static void swap(Vector v, int i, int j) {
    Object o;

    o = v.elementAt(i);
    v.setElementAt(v.elementAt(j), i);
    v.setElementAt(o, j);
  }

  /**
   * This is a generic version of C.A.R Hoare's Quick Sort algorithm. This
   * will handle arrays that are already sorted, and arrays with duplicate
   * keys.
   * <p>
   * If you think of a one dimensional array as going from the lowest index on
   * the left to the highest index on the right then the parameters to this
   * function are lowest index or left and highest index or right.
   * 
   * @param a
   *            An array of <code>Ordered</code> items.
   * @param lo0
   *            Left boundary of array partition.
   * @param hi0
   *            Right boundary of array partition.
   */
  public final void sort(Object[] a, int lo0, int hi0) {
    int lo = lo0;
    int hi = hi0;
    Object mid;

    if (hi0 > lo0) {
      // arbitrarily establish partition element as the midpoint of the
      // array
      mid = a[(lo0 + hi0) / 2];

      // loop through the vector until indices cross
      while (lo <= hi) {
        // find the first element that is greater than or equal to
        // the partition element starting from the left index
        while ((lo < hi0) && (0 > comparator.compare(a[lo],mid)))
          ++lo;

        // find an element that is smaller than or equal to
        // the partition element starting from the right Index.
        while ((hi > lo0) && (0 < comparator.compare(a[hi],mid)))
          --hi;

        // if the indexes have not crossed, swap
        if (lo <= hi)
          swap(a, lo++, hi--);
      }

      // if the right index has not reached the left side of array
      // must now sort the left partition
      if (lo0 < hi)
        sort(a, lo0, hi);

      // if the left index has not reached the right side of array
      // must now sort the right partition
      if (lo < hi0)
        sort(a, lo, hi0);
    }
  }

  /**
   * Swaps two elements of an array.
   * 
   * @param a
   *            The array of elements.
   * @param i
   *            The index of one item to swap.
   * @param j
   *            The index of the other item to swap.
   */
  private static void swap(Object[] a, int i, int j) {
    Object o;
    o = a[i];
    a[i] = a[j];
    a[j] = o;
  }


}
