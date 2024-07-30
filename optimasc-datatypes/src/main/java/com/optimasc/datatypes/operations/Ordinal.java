package com.optimasc.datatypes.operations;

import java.math.BigInteger;

/** Interface to be implemented by classes that 
 *  have its values that can be represented
 *  as an ordinal value, in other words as
 *  an ordered set.
 *  
 * @author Carl Eric Codere
 *
 */
public interface Ordinal
{
   /** Returns the ordinal value of this 
    *  value representation.
    *  
    */
   public BigInteger ordinal();
}
