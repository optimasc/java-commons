/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.optimasc.datatypes.primitives;

import java.math.BigDecimal;

import com.optimasc.datatypes.Datatype;
import com.optimasc.datatypes.DecimalRangeFacet;
import com.optimasc.datatypes.Restriction;
import com.optimasc.datatypes.visitor.TypeVisitor;

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
