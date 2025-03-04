/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.Restriction;

/** Abstract base class representing a Primitive datatype as defined
 *  in ISO/IEC 11404:2007.
 *
 * @author Carl Eric Codere
 */
public abstract class PrimitiveType extends Datatype implements Restriction {

   public PrimitiveType(boolean ordered)
   {
        super(ordered);
   }
}
