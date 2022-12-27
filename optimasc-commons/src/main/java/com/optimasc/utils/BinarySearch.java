package com.optimasc.utils;

import java.util.Vector;

public class BinarySearch
{

    /** Searches for a element of a vector.
     *  @param v is a Vector of Strings, sorted from lowest to highest
     *  (by compareTo).
     *  @param s is the String for which one is searching.
     *  @returns -1 otherwise the index of the element
     */
        
  
      public static int search(Vector v, Comparator c, Object o) {
        int  left, right;
        // If the String is anywhere, it is in positions left through right-1.
        // The String is not in a position before left.
        // The String is not in a position after right-1.
        if (v.size() == 0) return -1;  // Note: code in the book does
                                          // not handle empty Vectors
        left = 0;
        right = v.size();
        
        while  (left != right - 1)  {
          int  m  =  (right+left)/2;
          Object sm  =  v.elementAt(m);
          if (c.compare(sm, o)<0) {
              left  =  m;        // Move left to the middle.
          }  
          else if (c.compare(sm,o)>0) {
              right  =  m;       // Move right to the middle.
          }
          else  {
              left  =  m;
              right  =  m+1;
          }
        }
        if (v.elementAt(left).equals(o))
        {
          return left;
        }
        return -1;
      }
      
      public static int search(Object[] v, Comparator c, Object o) {
        int  left, right;
        // If the String is anywhere, it is in positions left through right-1.
        // The String is not in a position before left.
        // The String is not in a position after right-1.
        if (v.length == 0) return -1;  // Note: code in the book does
                                          // not handle empty Vectors
        left = 0;
        right = v.length;
        
        while  (left != right - 1)  {
          int  m  =  (right+left)/2;
          Object sm  =  v[m];
          if (c.compare(sm, o)<0) {
              left  =  m;        // Move left to the middle.
          }  
          else if (c.compare(sm,o)>0) {
              right  =  m;       // Move right to the middle.
          }
          else  {
              left  =  m;
              right  =  m+1;
          }
        }
        if (v[left].equals(o))
        {
          return left;
        }
        return -1;
      }
      

}
