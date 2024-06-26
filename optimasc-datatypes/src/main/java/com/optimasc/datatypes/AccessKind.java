package com.optimasc.datatypes;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.optimasc.datatypes.generated.FormalParameterType.ParameterType;

/** Represents an Enumeration visibility access of member elements
 *  within a specific namespace, such as a class.
 *  
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public final class AccessKind implements Comparable
{
  /** A public element is visible to all elements that can access the contents 
   *  of the namespace. 
   * 
   */
  public static final AccessKind Public = new AccessKind("Public");
  /**
   * A protected element is visible to elements that have a generalization (derived from) relationship to the 
   * namespace that owns it. Hence all derived classes from this class
   * can access protected members.
   */
  public static final AccessKind Protected = new AccessKind("Protected");
  /**
   * A private element is only visible inside the namespace that owns it.
   */
  public static final AccessKind Private = new AccessKind("Private");
  /**
   * A package element is visible to all elements within thr same namespace.
   * 
   */
  public static final AccessKind Package = new AccessKind("Package");

  private final String name;
  private static int nextOrdinal = 0;
  private final int ordinal = nextOrdinal++;

  private AccessKind(String enumSymName)
  {
    this.name = enumSymName;
  }

  /**
   * Parse text into an element of this enumeration.
   *
   * @param aText
   *          takes one of the values allowed in the enumeration symbols.
   */
  public static AccessKind valueOf(String aText)
  {
    Iterator iter = VALUES.iterator();
    while (iter.hasNext())
    {
      AccessKind p = (AccessKind) iter.next();
      if (aText.equals(p.toString()))
      {
        return p;
      }
    }
    //this method is unusual in that IllegalArgumentException is
    //possibly thrown not at its beginning, but at its end.
    throw new IllegalArgumentException("Cannot parse into an element of Suit : '"
        + aText + "'");
  }

  public int compareTo(Object that)
  {
    return ordinal - ((AccessKind) that).ordinal;
  }

  public String toString()
  {
    return name;
  }
  
  /**
   * These two lines are all that's necessary to export a List of VALUES.
   */
   private static final AccessKind[] values = {Public, Protected, Private, Package};
   //VALUES needs to be located here, otherwise illegal forward reference
   public static final List VALUES = Collections.unmodifiableList(Arrays.asList(values));


}
