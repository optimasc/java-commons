package com.optimasc.io;

import java.util.Enumeration;
import java.util.Vector;


/** Represents a type safe enumeration representing byte order
 *  of data in memory (The endian of the data).
 *  
 *  
 * 
 * @author Carl Eric Codere
 *
 */
public class ByteOrder
{
    /** Represents little-endian byte ordering. In this order, the bytes of a multibyte value are ordered 
     *  from least significant to most significant.   
     * 
     */
    public static final ByteOrder LITTLE_ENDIAN = new ByteOrder("LITTLE-ENDIAN");
    /** Represents big-endian byte ordering. In this order, the bytes of a multibyte value are ordered 
     *  from most significant to least significant.   
     * 
     */
    public static final ByteOrder BIG_ENDIAN = new ByteOrder("BIG-ENDIAN");

    private final String name;
    private static int nextOrdinal = 0;
    private final int ordinal = nextOrdinal++;

    private ByteOrder(String enumSymName)
    {
      this.name = enumSymName;
    }

    /**
     * Parse text into an element of this enumeration.
     *
     * @param aText
     *          takes one of the values allowed in the enumeration symbols.
     */
    public static ByteOrder valueOf(String aText)
    {
      Enumeration iter = VALUES.elements();
      while (iter.hasMoreElements())
      {
        ByteOrder p = (ByteOrder) iter.nextElement();
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
      return ordinal - ((ByteOrder) that).ordinal;
    }

    public String toString()
    {
      return name;
    }
    
    /**
     * These two lines are all that's necessary to export a List of VALUES.
     */
     private static final ByteOrder[] values = {LITTLE_ENDIAN,BIG_ENDIAN};
     //VALUES needs to be located here, otherwise illegal forward reference
     public static final Vector VALUES;
     
     static {
       VALUES = new Vector();
       VALUES.addElement(values[0]);
       VALUES.addElement(values[1]);
     }


}
