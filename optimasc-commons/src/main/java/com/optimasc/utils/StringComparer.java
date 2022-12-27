package com.optimasc.utils;


/** Class that is used as a Comparator implementation for Strings.
 * 
 * @author Carl
 */
public final class StringComparer implements Comparator
{
    private static Comparator singleInstance;

    /**
     * Force use of instance method to get object instance.
     */
    private StringComparer()
    {
    }

    ;

    /**
     * Returns a WordDefinitionComparer object.
     */
    public static Comparator getInstance()
    {
        if (singleInstance == null)
        {
            singleInstance = new StringComparer();
        }
        return singleInstance;
    }

    public final int compare(Object o1, Object o2) throws ClassCastException
    {
        String s1 = o1.toString();
        String s2 = o2.toString();
        return s1.compareTo(s2);
    }

}
